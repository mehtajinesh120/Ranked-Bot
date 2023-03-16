/*      */ package com.neovisionaries.ws.client;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InterruptedIOException;
/*      */ import java.net.Socket;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Timer;
/*      */ import java.util.TimerTask;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ class ReadingThread
/*      */   extends WebSocketThread
/*      */ {
/*      */   private boolean mStopRequested;
/*      */   private WebSocketFrame mCloseFrame;
/*   42 */   private List<WebSocketFrame> mContinuation = new ArrayList<WebSocketFrame>();
/*      */   private final PerMessageCompressionExtension mPMCE;
/*   44 */   private Object mCloseLock = new Object();
/*      */   
/*      */   private Timer mCloseTimer;
/*      */   
/*      */   private CloseTask mCloseTask;
/*      */   private long mCloseDelay;
/*      */   private boolean mNotWaitForCloseFrame;
/*      */   
/*      */   public ReadingThread(WebSocket websocket) {
/*   53 */     super("ReadingThread", websocket, ThreadType.READING_THREAD);
/*      */     
/*   55 */     this.mPMCE = websocket.getPerMessageCompressionExtension();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void runMain() {
/*      */     try {
/*   64 */       main();
/*      */     }
/*   66 */     catch (Throwable t) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*   71 */       WebSocketException cause = new WebSocketException(WebSocketError.UNEXPECTED_ERROR_IN_READING_THREAD, "An uncaught throwable was detected in the reading thread: " + t.getMessage(), t);
/*      */ 
/*      */       
/*   74 */       ListenerManager manager = this.mWebSocket.getListenerManager();
/*   75 */       manager.callOnError(cause);
/*   76 */       manager.callOnUnexpectedError(cause);
/*      */     } 
/*      */ 
/*      */     
/*   80 */     notifyFinished();
/*      */   }
/*      */ 
/*      */   
/*      */   private void main() {
/*      */     boolean keepReading;
/*   86 */     this.mWebSocket.onReadingThreadStarted();
/*      */ 
/*      */     
/*      */     do {
/*   90 */       synchronized (this) {
/*      */         
/*   92 */         if (this.mStopRequested) {
/*      */           break;
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*   99 */       WebSocketFrame frame = readFrame();
/*      */       
/*  101 */       if (frame == null) {
/*      */         break;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  108 */       keepReading = handleFrame(frame);
/*      */     }
/*  110 */     while (keepReading);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  117 */     waitForCloseFrame();
/*      */ 
/*      */     
/*  120 */     cancelClose();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void requestStop(long closeDelay) {
/*  126 */     synchronized (this) {
/*      */       
/*  128 */       if (this.mStopRequested) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/*  133 */       this.mStopRequested = true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  140 */     interrupt();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  150 */     this.mCloseDelay = closeDelay;
/*  151 */     scheduleClose();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnFrame(WebSocketFrame frame) {
/*  161 */     this.mWebSocket.getListenerManager().callOnFrame(frame);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnContinuationFrame(WebSocketFrame frame) {
/*  171 */     this.mWebSocket.getListenerManager().callOnContinuationFrame(frame);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnTextFrame(WebSocketFrame frame) {
/*  181 */     this.mWebSocket.getListenerManager().callOnTextFrame(frame);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnBinaryFrame(WebSocketFrame frame) {
/*  191 */     this.mWebSocket.getListenerManager().callOnBinaryFrame(frame);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnCloseFrame(WebSocketFrame frame) {
/*  201 */     this.mWebSocket.getListenerManager().callOnCloseFrame(frame);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnPingFrame(WebSocketFrame frame) {
/*  211 */     this.mWebSocket.getListenerManager().callOnPingFrame(frame);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnPongFrame(WebSocketFrame frame) {
/*  221 */     this.mWebSocket.getListenerManager().callOnPongFrame(frame);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnTextMessage(byte[] data) {
/*  231 */     if (this.mWebSocket.isDirectTextMessage()) {
/*      */       
/*  233 */       this.mWebSocket.getListenerManager().callOnTextMessage(data);
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */     
/*      */     try {
/*  241 */       String message = Misc.toStringUTF8(data);
/*      */ 
/*      */       
/*  244 */       callOnTextMessage(message);
/*      */     }
/*  246 */     catch (Throwable t) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  251 */       WebSocketException wse = new WebSocketException(WebSocketError.TEXT_MESSAGE_CONSTRUCTION_ERROR, "Failed to convert payload data into a string: " + t.getMessage(), t);
/*      */ 
/*      */       
/*  254 */       callOnError(wse);
/*  255 */       callOnTextMessageError(wse, data);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnTextMessage(String message) {
/*  266 */     this.mWebSocket.getListenerManager().callOnTextMessage(message);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnBinaryMessage(byte[] message) {
/*  276 */     this.mWebSocket.getListenerManager().callOnBinaryMessage(message);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnError(WebSocketException cause) {
/*  286 */     this.mWebSocket.getListenerManager().callOnError(cause);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnFrameError(WebSocketException cause, WebSocketFrame frame) {
/*  296 */     this.mWebSocket.getListenerManager().callOnFrameError(cause, frame);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnMessageError(WebSocketException cause, List<WebSocketFrame> frames) {
/*  306 */     this.mWebSocket.getListenerManager().callOnMessageError(cause, frames);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnMessageDecompressionError(WebSocketException cause, byte[] compressed) {
/*  316 */     this.mWebSocket.getListenerManager().callOnMessageDecompressionError(cause, compressed);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void callOnTextMessageError(WebSocketException cause, byte[] data) {
/*  326 */     this.mWebSocket.getListenerManager().callOnTextMessageError(cause, data);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private WebSocketFrame readFrame() {
/*  332 */     WebSocketFrame frame = null;
/*  333 */     WebSocketException wse = null;
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  338 */       frame = this.mWebSocket.getInput().readFrame();
/*      */ 
/*      */       
/*  341 */       verifyFrame(frame);
/*      */ 
/*      */       
/*  344 */       return frame;
/*      */     }
/*  346 */     catch (InterruptedIOException e) {
/*      */       
/*  348 */       if (this.mStopRequested)
/*      */       {
/*      */ 
/*      */         
/*  352 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  359 */       wse = new WebSocketException(WebSocketError.INTERRUPTED_IN_READING, "Interruption occurred while a frame was being read from the web socket: " + e.getMessage(), e);
/*      */     
/*      */     }
/*  362 */     catch (IOException e) {
/*      */       
/*  364 */       if (this.mStopRequested && isInterrupted())
/*      */       {
/*      */ 
/*      */         
/*  368 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  375 */       wse = new WebSocketException(WebSocketError.IO_ERROR_IN_READING, "An I/O error occurred while a frame was being read from the web socket: " + e.getMessage(), e);
/*      */     
/*      */     }
/*  378 */     catch (WebSocketException e) {
/*      */ 
/*      */       
/*  381 */       wse = e;
/*      */     } 
/*      */     
/*  384 */     boolean error = true;
/*      */ 
/*      */ 
/*      */     
/*  388 */     if (wse instanceof NoMoreFrameException) {
/*      */ 
/*      */       
/*  391 */       this.mNotWaitForCloseFrame = true;
/*      */ 
/*      */       
/*  394 */       if (this.mWebSocket.isMissingCloseFrameAllowed())
/*      */       {
/*  396 */         error = false;
/*      */       }
/*      */     } 
/*      */     
/*  400 */     if (error) {
/*      */ 
/*      */       
/*  403 */       callOnError(wse);
/*  404 */       callOnFrameError(wse, frame);
/*      */     } 
/*      */ 
/*      */     
/*  408 */     WebSocketFrame closeFrame = createCloseFrame(wse);
/*      */ 
/*      */     
/*  411 */     this.mWebSocket.sendFrame(closeFrame);
/*      */ 
/*      */     
/*  414 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verifyFrame(WebSocketFrame frame) throws WebSocketException {
/*  421 */     verifyReservedBits(frame);
/*      */ 
/*      */     
/*  424 */     verifyFrameOpcode(frame);
/*      */ 
/*      */     
/*  427 */     verifyFrameMask(frame);
/*      */ 
/*      */     
/*  430 */     verifyFrameFragmentation(frame);
/*      */ 
/*      */     
/*  433 */     verifyFrameSize(frame);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verifyReservedBits(WebSocketFrame frame) throws WebSocketException {
/*  440 */     if (this.mWebSocket.isExtended()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  451 */     verifyReservedBit1(frame);
/*  452 */     verifyReservedBit2(frame);
/*  453 */     verifyReservedBit3(frame);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verifyReservedBit1(WebSocketFrame frame) throws WebSocketException {
/*  463 */     if (this.mPMCE != null) {
/*      */ 
/*      */       
/*  466 */       boolean verified = verifyReservedBit1ForPMCE(frame);
/*      */       
/*  468 */       if (verified) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  474 */     if (!frame.getRsv1()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  481 */     throw new WebSocketException(WebSocketError.UNEXPECTED_RESERVED_BIT, "The RSV1 bit of a frame is set unexpectedly.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean verifyReservedBit1ForPMCE(WebSocketFrame frame) throws WebSocketException {
/*  493 */     if (frame.isTextFrame() || frame.isBinaryFrame())
/*      */     {
/*      */ 
/*      */ 
/*      */       
/*  498 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  502 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verifyReservedBit2(WebSocketFrame frame) throws WebSocketException {
/*  511 */     if (!frame.getRsv2()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  518 */     throw new WebSocketException(WebSocketError.UNEXPECTED_RESERVED_BIT, "The RSV2 bit of a frame is set unexpectedly.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verifyReservedBit3(WebSocketFrame frame) throws WebSocketException {
/*  528 */     if (!frame.getRsv3()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  535 */     throw new WebSocketException(WebSocketError.UNEXPECTED_RESERVED_BIT, "The RSV3 bit of a frame is set unexpectedly.");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verifyFrameOpcode(WebSocketFrame frame) throws WebSocketException {
/*  553 */     switch (frame.getOpcode()) {
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */         return;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  569 */     if (this.mWebSocket.isExtended()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  576 */     throw new WebSocketException(WebSocketError.UNKNOWN_OPCODE, "A frame has an unknown opcode: 0x" + 
/*      */         
/*  578 */         Integer.toHexString(frame.getOpcode()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verifyFrameMask(WebSocketFrame frame) throws WebSocketException {
/*  596 */     if (frame.getMask())
/*      */     {
/*      */       
/*  599 */       throw new WebSocketException(WebSocketError.FRAME_MASKED, "A frame from the server is masked.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verifyFrameFragmentation(WebSocketFrame frame) throws WebSocketException {
/*  611 */     if (frame.isControlFrame()) {
/*      */ 
/*      */       
/*  614 */       if (!frame.getFin())
/*      */       {
/*      */         
/*  617 */         throw new WebSocketException(WebSocketError.FRAGMENTED_CONTROL_FRAME, "A control frame is fragmented.");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  627 */     boolean continuationExists = (this.mContinuation.size() != 0);
/*      */ 
/*      */     
/*  630 */     if (frame.isContinuationFrame()) {
/*      */ 
/*      */       
/*  633 */       if (!continuationExists)
/*      */       {
/*      */         
/*  636 */         throw new WebSocketException(WebSocketError.UNEXPECTED_CONTINUATION_FRAME, "A continuation frame was detected although a continuation had not started.");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  647 */     if (continuationExists)
/*      */     {
/*      */       
/*  650 */       throw new WebSocketException(WebSocketError.CONTINUATION_NOT_CLOSED, "A non-control frame was detected although the existing continuation had not been closed.");
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void verifyFrameSize(WebSocketFrame frame) throws WebSocketException {
/*  660 */     if (!frame.isControlFrame()) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  672 */     byte[] payload = frame.getPayload();
/*      */     
/*  674 */     if (payload == null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  680 */     if (125 < payload.length)
/*      */     {
/*      */       
/*  683 */       throw new WebSocketException(WebSocketError.TOO_LONG_CONTROL_FRAME_PAYLOAD, "The payload size of a control frame exceeds the maximum size (125 bytes): " + payload.length);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private WebSocketFrame createCloseFrame(WebSocketException wse) {
/*  694 */     switch (wse.getError())
/*      */     
/*      */     { 
/*      */       
/*      */       case INSUFFICENT_DATA:
/*      */       case INVALID_PAYLOAD_LENGTH:
/*      */       case NO_MORE_FRAME:
/*  701 */         closeCode = 1002;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  736 */         return WebSocketFrame.createCloseFrame(closeCode, wse.getMessage());case TOO_LONG_PAYLOAD: case INSUFFICIENT_MEMORY_FOR_PAYLOAD: closeCode = 1009; return WebSocketFrame.createCloseFrame(closeCode, wse.getMessage());case NON_ZERO_RESERVED_BITS: case UNEXPECTED_RESERVED_BIT: case UNKNOWN_OPCODE: case FRAME_MASKED: case FRAGMENTED_CONTROL_FRAME: case UNEXPECTED_CONTINUATION_FRAME: case CONTINUATION_NOT_CLOSED: case TOO_LONG_CONTROL_FRAME_PAYLOAD: closeCode = 1002; return WebSocketFrame.createCloseFrame(closeCode, wse.getMessage());case INTERRUPTED_IN_READING: case IO_ERROR_IN_READING: closeCode = 1008; return WebSocketFrame.createCloseFrame(closeCode, wse.getMessage()); }  int closeCode = 1008; return WebSocketFrame.createCloseFrame(closeCode, wse.getMessage());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handleFrame(WebSocketFrame frame) {
/*  743 */     callOnFrame(frame);
/*      */ 
/*      */     
/*  746 */     switch (frame.getOpcode()) {
/*      */       
/*      */       case 0:
/*  749 */         return handleContinuationFrame(frame);
/*      */       
/*      */       case 1:
/*  752 */         return handleTextFrame(frame);
/*      */       
/*      */       case 2:
/*  755 */         return handleBinaryFrame(frame);
/*      */       
/*      */       case 8:
/*  758 */         return handleCloseFrame(frame);
/*      */       
/*      */       case 9:
/*  761 */         return handlePingFrame(frame);
/*      */       
/*      */       case 10:
/*  764 */         return handlePongFrame(frame);
/*      */     } 
/*      */ 
/*      */     
/*  768 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handleContinuationFrame(WebSocketFrame frame) {
/*  776 */     callOnContinuationFrame(frame);
/*      */ 
/*      */     
/*  779 */     this.mContinuation.add(frame);
/*      */ 
/*      */     
/*  782 */     if (!frame.getFin())
/*      */     {
/*      */       
/*  785 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  790 */     byte[] data = getMessage(this.mContinuation);
/*      */ 
/*      */     
/*  793 */     if (data == null)
/*      */     {
/*      */       
/*  796 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  800 */     if (((WebSocketFrame)this.mContinuation.get(0)).isTextFrame()) {
/*      */ 
/*      */       
/*  803 */       callOnTextMessage(data);
/*      */     
/*      */     }
/*      */     else {
/*      */       
/*  808 */       callOnBinaryMessage(data);
/*      */     } 
/*      */ 
/*      */     
/*  812 */     this.mContinuation.clear();
/*      */ 
/*      */     
/*  815 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] getMessage(List<WebSocketFrame> frames) {
/*  822 */     byte[] data = concatenatePayloads(this.mContinuation);
/*      */ 
/*      */     
/*  825 */     if (data == null)
/*      */     {
/*      */       
/*  828 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  833 */     if (this.mPMCE != null && ((WebSocketFrame)frames.get(0)).getRsv1())
/*      */     {
/*      */       
/*  836 */       data = decompress(data);
/*      */     }
/*      */     
/*  839 */     return data;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] concatenatePayloads(List<WebSocketFrame> frames) {
/*      */     Throwable cause;
/*      */     try {
/*  849 */       ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*      */ 
/*      */       
/*  852 */       for (WebSocketFrame webSocketFrame : frames) {
/*      */ 
/*      */         
/*  855 */         byte[] payload = webSocketFrame.getPayload();
/*      */ 
/*      */         
/*  858 */         if (payload == null || payload.length == 0) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*  864 */         baos.write(payload);
/*      */       } 
/*      */ 
/*      */       
/*  868 */       return baos.toByteArray();
/*      */     }
/*  870 */     catch (IOException e) {
/*      */       
/*  872 */       cause = e;
/*      */     }
/*  874 */     catch (OutOfMemoryError e) {
/*      */       
/*  876 */       cause = e;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  882 */     WebSocketException wse = new WebSocketException(WebSocketError.MESSAGE_CONSTRUCTION_ERROR, "Failed to concatenate payloads of multiple frames to construct a message: " + cause.getMessage(), cause);
/*      */ 
/*      */     
/*  885 */     callOnError(wse);
/*  886 */     callOnMessageError(wse, frames);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  891 */     WebSocketFrame frame = WebSocketFrame.createCloseFrame(1009, wse.getMessage());
/*      */ 
/*      */     
/*  894 */     this.mWebSocket.sendFrame(frame);
/*      */ 
/*      */     
/*  897 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] getMessage(WebSocketFrame frame) {
/*  904 */     byte[] payload = frame.getPayload();
/*      */ 
/*      */ 
/*      */     
/*  908 */     if (this.mPMCE != null && frame.getRsv1())
/*      */     {
/*      */       
/*  911 */       payload = decompress(payload);
/*      */     }
/*      */     
/*  914 */     return payload;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private byte[] decompress(byte[] input) {
/*      */     try {
/*  925 */       return this.mPMCE.decompress(input);
/*      */     }
/*  927 */     catch (WebSocketException e) {
/*      */       
/*  929 */       WebSocketException wse = e;
/*      */ 
/*      */ 
/*      */       
/*  933 */       callOnError(wse);
/*  934 */       callOnMessageDecompressionError(wse, input);
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  939 */       WebSocketFrame frame = WebSocketFrame.createCloseFrame(1003, wse.getMessage());
/*      */ 
/*      */       
/*  942 */       this.mWebSocket.sendFrame(frame);
/*      */ 
/*      */       
/*  945 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handleTextFrame(WebSocketFrame frame) {
/*  952 */     callOnTextFrame(frame);
/*      */ 
/*      */     
/*  955 */     if (!frame.getFin()) {
/*      */ 
/*      */       
/*  958 */       this.mContinuation.add(frame);
/*      */ 
/*      */       
/*  961 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  966 */     byte[] payload = getMessage(frame);
/*      */ 
/*      */     
/*  969 */     callOnTextMessage(payload);
/*      */ 
/*      */     
/*  972 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handleBinaryFrame(WebSocketFrame frame) {
/*  979 */     callOnBinaryFrame(frame);
/*      */ 
/*      */     
/*  982 */     if (!frame.getFin()) {
/*      */ 
/*      */       
/*  985 */       this.mContinuation.add(frame);
/*      */ 
/*      */       
/*  988 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  993 */     byte[] payload = getMessage(frame);
/*      */ 
/*      */     
/*  996 */     callOnBinaryMessage(payload);
/*      */ 
/*      */     
/*  999 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handleCloseFrame(WebSocketFrame frame) {
/* 1006 */     StateManager manager = this.mWebSocket.getStateManager();
/*      */ 
/*      */     
/* 1009 */     this.mCloseFrame = frame;
/*      */     
/* 1011 */     boolean stateChanged = false;
/*      */     
/* 1013 */     synchronized (manager) {
/*      */ 
/*      */       
/* 1016 */       WebSocketState state = manager.getState();
/*      */ 
/*      */       
/* 1019 */       if (state != WebSocketState.CLOSING && state != WebSocketState.CLOSED) {
/*      */ 
/*      */         
/* 1022 */         manager.changeToClosing(StateManager.CloseInitiator.SERVER);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1034 */         this.mWebSocket.sendFrame(frame);
/*      */         
/* 1036 */         stateChanged = true;
/*      */       } 
/*      */     } 
/*      */     
/* 1040 */     if (stateChanged)
/*      */     {
/*      */       
/* 1043 */       this.mWebSocket.getListenerManager().callOnStateChanged(WebSocketState.CLOSING);
/*      */     }
/*      */ 
/*      */     
/* 1047 */     callOnCloseFrame(frame);
/*      */ 
/*      */     
/* 1050 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handlePingFrame(WebSocketFrame frame) {
/* 1057 */     callOnPingFrame(frame);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1068 */     WebSocketFrame pong = WebSocketFrame.createPongFrame(frame.getPayload());
/*      */ 
/*      */     
/* 1071 */     this.mWebSocket.sendFrame(pong);
/*      */ 
/*      */     
/* 1074 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean handlePongFrame(WebSocketFrame frame) {
/* 1081 */     callOnPongFrame(frame);
/*      */ 
/*      */     
/* 1084 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void waitForCloseFrame() {
/* 1090 */     if (this.mNotWaitForCloseFrame) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1096 */     if (this.mCloseFrame != null) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/* 1101 */     WebSocketFrame frame = null;
/*      */ 
/*      */ 
/*      */     
/* 1105 */     scheduleClose();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     do {
/*      */       try {
/* 1112 */         frame = this.mWebSocket.getInput().readFrame();
/*      */       }
/* 1114 */       catch (Throwable t) {
/*      */         break;
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1121 */       if (frame.isCloseFrame()) {
/*      */ 
/*      */         
/* 1124 */         this.mCloseFrame = frame;
/*      */         
/*      */         break;
/*      */       } 
/* 1128 */     } while (!isInterrupted());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void notifyFinished() {
/* 1138 */     this.mWebSocket.onReadingThreadFinished(this.mCloseFrame);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void scheduleClose() {
/* 1144 */     synchronized (this.mCloseLock) {
/*      */       
/* 1146 */       cancelCloseTask();
/* 1147 */       scheduleCloseTask();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void scheduleCloseTask() {
/* 1154 */     this.mCloseTask = new CloseTask();
/* 1155 */     this.mCloseTimer = new Timer("ReadingThreadCloseTimer");
/* 1156 */     this.mCloseTimer.schedule(this.mCloseTask, this.mCloseDelay);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void cancelClose() {
/* 1162 */     synchronized (this.mCloseLock) {
/*      */       
/* 1164 */       cancelCloseTask();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void cancelCloseTask() {
/* 1171 */     if (this.mCloseTimer != null) {
/*      */       
/* 1173 */       this.mCloseTimer.cancel();
/* 1174 */       this.mCloseTimer = null;
/*      */     } 
/*      */     
/* 1177 */     if (this.mCloseTask != null) {
/*      */       
/* 1179 */       this.mCloseTask.cancel();
/* 1180 */       this.mCloseTask = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private class CloseTask
/*      */     extends TimerTask
/*      */   {
/*      */     private CloseTask() {}
/*      */     
/*      */     public void run() {
/*      */       try {
/* 1192 */         Socket socket = ReadingThread.this.mWebSocket.getSocket();
/* 1193 */         if (socket != null)
/*      */         {
/* 1195 */           socket.close();
/*      */         }
/*      */       }
/* 1198 */       catch (Throwable throwable) {}
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\ReadingThread.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */