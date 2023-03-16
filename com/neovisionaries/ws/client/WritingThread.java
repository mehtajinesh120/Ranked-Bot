/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class WritingThread
/*     */   extends WebSocketThread
/*     */ {
/*     */   private static final int SHOULD_SEND = 0;
/*     */   private static final int SHOULD_STOP = 1;
/*     */   private static final int SHOULD_CONTINUE = 2;
/*     */   private static final int SHOULD_FLUSH = 3;
/*     */   private static final int FLUSH_THRESHOLD = 1000;
/*     */   private final LinkedList<WebSocketFrame> mFrames;
/*     */   private final PerMessageCompressionExtension mPMCE;
/*     */   private boolean mStopRequested;
/*     */   private WebSocketFrame mCloseFrame;
/*     */   private boolean mFlushNeeded;
/*     */   private boolean mStopped;
/*     */   
/*     */   public WritingThread(WebSocket websocket) {
/*  43 */     super("WritingThread", websocket, ThreadType.WRITING_THREAD);
/*     */     
/*  45 */     this.mFrames = new LinkedList<WebSocketFrame>();
/*  46 */     this.mPMCE = websocket.getPerMessageCompressionExtension();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void runMain() {
/*     */     try {
/*  55 */       main();
/*     */     }
/*  57 */     catch (Throwable t) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  62 */       WebSocketException cause = new WebSocketException(WebSocketError.UNEXPECTED_ERROR_IN_WRITING_THREAD, "An uncaught throwable was detected in the writing thread: " + t.getMessage(), t);
/*     */ 
/*     */       
/*  65 */       ListenerManager manager = this.mWebSocket.getListenerManager();
/*  66 */       manager.callOnError(cause);
/*  67 */       manager.callOnUnexpectedError(cause);
/*     */     } 
/*     */     
/*  70 */     synchronized (this) {
/*     */ 
/*     */       
/*  73 */       this.mStopped = true;
/*  74 */       notifyAll();
/*     */     } 
/*     */ 
/*     */     
/*  78 */     notifyFinished();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void main() {
/*  84 */     this.mWebSocket.onWritingThreadStarted();
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/*  89 */       int result = waitForFrames();
/*     */       
/*  91 */       if (result == 1) {
/*     */         break;
/*     */       }
/*     */       
/*  95 */       if (result == 3) {
/*     */         
/*  97 */         flushIgnoreError();
/*     */         continue;
/*     */       } 
/* 100 */       if (result == 2) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 108 */         sendFrames(false);
/*     */       }
/* 110 */       catch (WebSocketException e) {
/*     */         break;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 120 */       sendFrames(true);
/*     */     }
/* 122 */     catch (WebSocketException webSocketException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void requestStop() {
/* 131 */     synchronized (this) {
/*     */ 
/*     */       
/* 134 */       this.mStopRequested = true;
/*     */ 
/*     */       
/* 137 */       notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean queueFrame(WebSocketFrame frame) {
/* 144 */     synchronized (this) {
/*     */ 
/*     */       
/*     */       while (true) {
/*     */         
/* 149 */         if (this.mStopped)
/*     */         {
/*     */           
/* 152 */           return false;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 157 */         if (this.mStopRequested || this.mCloseFrame != null) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 164 */         if (frame.isControlFrame()) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 171 */         int queueSize = this.mWebSocket.getFrameQueueSize();
/*     */ 
/*     */         
/* 174 */         if (queueSize == 0) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 181 */         if (this.mFrames.size() < queueSize) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 190 */           wait();
/*     */         }
/* 192 */         catch (InterruptedException interruptedException) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 198 */       if (isHighPriorityFrame(frame)) {
/*     */ 
/*     */         
/* 201 */         addHighPriorityFrame(frame);
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 206 */         this.mFrames.addLast(frame);
/*     */       } 
/*     */ 
/*     */       
/* 210 */       notifyAll();
/*     */     } 
/*     */ 
/*     */     
/* 214 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isHighPriorityFrame(WebSocketFrame frame) {
/* 220 */     return (frame.isPingFrame() || frame.isPongFrame());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addHighPriorityFrame(WebSocketFrame frame) {
/* 226 */     int index = 0;
/*     */ 
/*     */ 
/*     */     
/* 230 */     for (WebSocketFrame f : this.mFrames) {
/*     */ 
/*     */       
/* 233 */       if (!isHighPriorityFrame(f)) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 238 */       index++;
/*     */     } 
/*     */     
/* 241 */     this.mFrames.add(index, frame);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queueFlush() {
/* 247 */     synchronized (this) {
/*     */       
/* 249 */       this.mFlushNeeded = true;
/*     */ 
/*     */       
/* 252 */       notifyAll();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void flushIgnoreError() {
/*     */     try {
/* 261 */       flush();
/*     */     }
/* 263 */     catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void flush() throws IOException {
/* 271 */     this.mWebSocket.getOutput().flush();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int waitForFrames() {
/* 277 */     synchronized (this) {
/*     */ 
/*     */       
/* 280 */       if (this.mStopRequested)
/*     */       {
/* 282 */         return 1;
/*     */       }
/*     */ 
/*     */       
/* 286 */       if (this.mCloseFrame != null)
/*     */       {
/* 288 */         return 1;
/*     */       }
/*     */ 
/*     */       
/* 292 */       if (this.mFrames.size() == 0) {
/*     */ 
/*     */         
/* 295 */         if (this.mFlushNeeded) {
/*     */           
/* 297 */           this.mFlushNeeded = false;
/* 298 */           return 3;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 305 */           wait();
/*     */         }
/* 307 */         catch (InterruptedException interruptedException) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 312 */       if (this.mStopRequested)
/*     */       {
/* 314 */         return 1;
/*     */       }
/*     */       
/* 317 */       if (this.mFrames.size() == 0) {
/*     */         
/* 319 */         if (this.mFlushNeeded) {
/*     */           
/* 321 */           this.mFlushNeeded = false;
/* 322 */           return 3;
/*     */         } 
/*     */ 
/*     */         
/* 326 */         return 2;
/*     */       } 
/*     */     } 
/*     */     
/* 330 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendFrames(boolean last) throws WebSocketException {
/* 337 */     long lastFlushAt = System.currentTimeMillis();
/*     */ 
/*     */     
/*     */     while (true) {
/*     */       WebSocketFrame frame;
/*     */       
/* 343 */       synchronized (this) {
/*     */ 
/*     */         
/* 346 */         frame = this.mFrames.poll();
/*     */ 
/*     */         
/* 349 */         notifyAll();
/*     */ 
/*     */         
/* 352 */         if (frame == null) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 360 */       sendFrame(frame);
/*     */ 
/*     */       
/* 363 */       if (frame.isPingFrame() || frame.isPongFrame()) {
/*     */ 
/*     */         
/* 366 */         doFlush();
/* 367 */         lastFlushAt = System.currentTimeMillis();
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 372 */       if (!isFlushNeeded(last)) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 379 */       lastFlushAt = flushIfLongInterval(lastFlushAt);
/*     */     } 
/*     */     
/* 382 */     if (isFlushNeeded(last))
/*     */     {
/* 384 */       doFlush();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isFlushNeeded(boolean last) {
/* 391 */     return (last || this.mWebSocket.isAutoFlush() || this.mFlushNeeded || this.mCloseFrame != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long flushIfLongInterval(long lastFlushAt) throws WebSocketException {
/* 398 */     long current = System.currentTimeMillis();
/*     */ 
/*     */     
/* 401 */     if (1000L < current - lastFlushAt) {
/*     */ 
/*     */       
/* 404 */       doFlush();
/*     */ 
/*     */       
/* 407 */       return current;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 412 */     return lastFlushAt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doFlush() throws WebSocketException {
/*     */     try {
/* 422 */       flush();
/*     */       
/* 424 */       synchronized (this)
/*     */       {
/* 426 */         this.mFlushNeeded = false;
/*     */       }
/*     */     
/* 429 */     } catch (IOException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 434 */       WebSocketException cause = new WebSocketException(WebSocketError.FLUSH_ERROR, "Flushing frames to the server failed: " + e.getMessage(), e);
/*     */ 
/*     */       
/* 437 */       ListenerManager manager = this.mWebSocket.getListenerManager();
/* 438 */       manager.callOnError(cause);
/* 439 */       manager.callOnSendError(cause, null);
/*     */       
/* 441 */       throw cause;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendFrame(WebSocketFrame frame) throws WebSocketException {
/* 449 */     frame = WebSocketFrame.compressFrame(frame, this.mPMCE);
/*     */ 
/*     */     
/* 452 */     this.mWebSocket.getListenerManager().callOnSendingFrame(frame);
/*     */     
/* 454 */     boolean unsent = false;
/*     */ 
/*     */     
/* 457 */     if (this.mCloseFrame != null) {
/*     */ 
/*     */       
/* 460 */       unsent = true;
/*     */     
/*     */     }
/* 463 */     else if (frame.isCloseFrame()) {
/*     */       
/* 465 */       this.mCloseFrame = frame;
/*     */     } 
/*     */     
/* 468 */     if (unsent) {
/*     */ 
/*     */       
/* 471 */       this.mWebSocket.getListenerManager().callOnFrameUnsent(frame);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 476 */     if (frame.isCloseFrame())
/*     */     {
/*     */ 
/*     */       
/* 480 */       changeToClosing();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 486 */       this.mWebSocket.getOutput().write(frame);
/*     */     }
/* 488 */     catch (IOException e) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 493 */       WebSocketException cause = new WebSocketException(WebSocketError.IO_ERROR_IN_WRITING, "An I/O error occurred when a frame was tried to be sent: " + e.getMessage(), e);
/*     */ 
/*     */       
/* 496 */       ListenerManager manager = this.mWebSocket.getListenerManager();
/* 497 */       manager.callOnError(cause);
/* 498 */       manager.callOnSendError(cause, frame);
/*     */       
/* 500 */       throw cause;
/*     */     } 
/*     */ 
/*     */     
/* 504 */     this.mWebSocket.getListenerManager().callOnFrameSent(frame);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void changeToClosing() {
/* 510 */     StateManager manager = this.mWebSocket.getStateManager();
/*     */     
/* 512 */     boolean stateChanged = false;
/*     */     
/* 514 */     synchronized (manager) {
/*     */ 
/*     */       
/* 517 */       WebSocketState state = manager.getState();
/*     */ 
/*     */       
/* 520 */       if (state != WebSocketState.CLOSING && state != WebSocketState.CLOSED) {
/*     */ 
/*     */         
/* 523 */         manager.changeToClosing(StateManager.CloseInitiator.CLIENT);
/*     */         
/* 525 */         stateChanged = true;
/*     */       } 
/*     */     } 
/*     */     
/* 529 */     if (stateChanged)
/*     */     {
/*     */       
/* 532 */       this.mWebSocket.getListenerManager().callOnStateChanged(WebSocketState.CLOSING);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void notifyFinished() {
/* 539 */     this.mWebSocket.onWritingThreadFinished(this.mCloseFrame);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\WritingThread.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */