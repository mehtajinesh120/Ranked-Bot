/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ class ListenerManager
/*     */ {
/*     */   private final WebSocket mWebSocket;
/*  27 */   private final List<WebSocketListener> mListeners = new ArrayList<WebSocketListener>();
/*     */   
/*     */   private boolean mSyncNeeded = true;
/*     */   
/*     */   private List<WebSocketListener> mCopiedListeners;
/*     */ 
/*     */   
/*     */   public ListenerManager(WebSocket websocket) {
/*  35 */     this.mWebSocket = websocket;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<WebSocketListener> getListeners() {
/*  41 */     return this.mListeners;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListener(WebSocketListener listener) {
/*  47 */     if (listener == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  52 */     synchronized (this.mListeners) {
/*     */       
/*  54 */       this.mListeners.add(listener);
/*  55 */       this.mSyncNeeded = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addListeners(List<WebSocketListener> listeners) {
/*  62 */     if (listeners == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  67 */     synchronized (this.mListeners) {
/*     */       
/*  69 */       for (WebSocketListener listener : listeners) {
/*     */         
/*  71 */         if (listener == null) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/*  76 */         this.mListeners.add(listener);
/*  77 */         this.mSyncNeeded = true;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeListener(WebSocketListener listener) {
/*  85 */     if (listener == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  90 */     synchronized (this.mListeners) {
/*     */       
/*  92 */       if (this.mListeners.remove(listener))
/*     */       {
/*  94 */         this.mSyncNeeded = true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeListeners(List<WebSocketListener> listeners) {
/* 102 */     if (listeners == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 107 */     synchronized (this.mListeners) {
/*     */       
/* 109 */       for (WebSocketListener listener : listeners) {
/*     */         
/* 111 */         if (listener == null) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 116 */         if (this.mListeners.remove(listener))
/*     */         {
/* 118 */           this.mSyncNeeded = true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearListeners() {
/* 127 */     synchronized (this.mListeners) {
/*     */       
/* 129 */       if (this.mListeners.size() == 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 134 */       this.mListeners.clear();
/* 135 */       this.mCopiedListeners = null;
/* 136 */       this.mSyncNeeded = true;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private List<WebSocketListener> getSynchronizedListeners() {
/* 143 */     synchronized (this.mListeners) {
/*     */       
/* 145 */       if (!this.mSyncNeeded)
/*     */       {
/* 147 */         return this.mCopiedListeners;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 152 */       List<WebSocketListener> copiedListeners = new ArrayList<WebSocketListener>(this.mListeners.size());
/*     */       
/* 154 */       for (WebSocketListener listener : this.mListeners)
/*     */       {
/* 156 */         copiedListeners.add(listener);
/*     */       }
/*     */ 
/*     */       
/* 160 */       this.mCopiedListeners = copiedListeners;
/* 161 */       this.mSyncNeeded = false;
/*     */       
/* 163 */       return copiedListeners;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnStateChanged(WebSocketState newState) {
/* 170 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 174 */         listener.onStateChanged(this.mWebSocket, newState);
/*     */       }
/* 176 */       catch (Throwable t) {
/*     */         
/* 178 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnConnected(Map<String, List<String>> headers) {
/* 186 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 190 */         listener.onConnected(this.mWebSocket, headers);
/*     */       }
/* 192 */       catch (Throwable t) {
/*     */         
/* 194 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnConnectError(WebSocketException cause) {
/* 202 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 206 */         listener.onConnectError(this.mWebSocket, cause);
/*     */       }
/* 208 */       catch (Throwable t) {
/*     */         
/* 210 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnDisconnected(WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
/* 220 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 224 */         listener.onDisconnected(this.mWebSocket, serverCloseFrame, clientCloseFrame, closedByServer);
/*     */       
/*     */       }
/* 227 */       catch (Throwable t) {
/*     */         
/* 229 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnFrame(WebSocketFrame frame) {
/* 237 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 241 */         listener.onFrame(this.mWebSocket, frame);
/*     */       }
/* 243 */       catch (Throwable t) {
/*     */         
/* 245 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnContinuationFrame(WebSocketFrame frame) {
/* 253 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 257 */         listener.onContinuationFrame(this.mWebSocket, frame);
/*     */       }
/* 259 */       catch (Throwable t) {
/*     */         
/* 261 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnTextFrame(WebSocketFrame frame) {
/* 269 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 273 */         listener.onTextFrame(this.mWebSocket, frame);
/*     */       }
/* 275 */       catch (Throwable t) {
/*     */         
/* 277 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnBinaryFrame(WebSocketFrame frame) {
/* 285 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 289 */         listener.onBinaryFrame(this.mWebSocket, frame);
/*     */       }
/* 291 */       catch (Throwable t) {
/*     */         
/* 293 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnCloseFrame(WebSocketFrame frame) {
/* 301 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 305 */         listener.onCloseFrame(this.mWebSocket, frame);
/*     */       }
/* 307 */       catch (Throwable t) {
/*     */         
/* 309 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnPingFrame(WebSocketFrame frame) {
/* 317 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 321 */         listener.onPingFrame(this.mWebSocket, frame);
/*     */       }
/* 323 */       catch (Throwable t) {
/*     */         
/* 325 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnPongFrame(WebSocketFrame frame) {
/* 333 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 337 */         listener.onPongFrame(this.mWebSocket, frame);
/*     */       }
/* 339 */       catch (Throwable t) {
/*     */         
/* 341 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnTextMessage(String message) {
/* 349 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 353 */         listener.onTextMessage(this.mWebSocket, message);
/*     */       }
/* 355 */       catch (Throwable t) {
/*     */         
/* 357 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnTextMessage(byte[] data) {
/* 365 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 369 */         listener.onTextMessage(this.mWebSocket, data);
/*     */       }
/* 371 */       catch (Throwable t) {
/*     */         
/* 373 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnBinaryMessage(byte[] message) {
/* 381 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 385 */         listener.onBinaryMessage(this.mWebSocket, message);
/*     */       }
/* 387 */       catch (Throwable t) {
/*     */         
/* 389 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnSendingFrame(WebSocketFrame frame) {
/* 397 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 401 */         listener.onSendingFrame(this.mWebSocket, frame);
/*     */       }
/* 403 */       catch (Throwable t) {
/*     */         
/* 405 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnFrameSent(WebSocketFrame frame) {
/* 413 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 417 */         listener.onFrameSent(this.mWebSocket, frame);
/*     */       }
/* 419 */       catch (Throwable t) {
/*     */         
/* 421 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnFrameUnsent(WebSocketFrame frame) {
/* 429 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 433 */         listener.onFrameUnsent(this.mWebSocket, frame);
/*     */       }
/* 435 */       catch (Throwable t) {
/*     */         
/* 437 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnThreadCreated(ThreadType threadType, Thread thread) {
/* 445 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 449 */         listener.onThreadCreated(this.mWebSocket, threadType, thread);
/*     */       }
/* 451 */       catch (Throwable t) {
/*     */         
/* 453 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnThreadStarted(ThreadType threadType, Thread thread) {
/* 461 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 465 */         listener.onThreadStarted(this.mWebSocket, threadType, thread);
/*     */       }
/* 467 */       catch (Throwable t) {
/*     */         
/* 469 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnThreadStopping(ThreadType threadType, Thread thread) {
/* 477 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 481 */         listener.onThreadStopping(this.mWebSocket, threadType, thread);
/*     */       }
/* 483 */       catch (Throwable t) {
/*     */         
/* 485 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnError(WebSocketException cause) {
/* 493 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 497 */         listener.onError(this.mWebSocket, cause);
/*     */       }
/* 499 */       catch (Throwable t) {
/*     */         
/* 501 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnFrameError(WebSocketException cause, WebSocketFrame frame) {
/* 509 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 513 */         listener.onFrameError(this.mWebSocket, cause, frame);
/*     */       }
/* 515 */       catch (Throwable t) {
/*     */         
/* 517 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnMessageError(WebSocketException cause, List<WebSocketFrame> frames) {
/* 525 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 529 */         listener.onMessageError(this.mWebSocket, cause, frames);
/*     */       }
/* 531 */       catch (Throwable t) {
/*     */         
/* 533 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnMessageDecompressionError(WebSocketException cause, byte[] compressed) {
/* 541 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 545 */         listener.onMessageDecompressionError(this.mWebSocket, cause, compressed);
/*     */       }
/* 547 */       catch (Throwable t) {
/*     */         
/* 549 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnTextMessageError(WebSocketException cause, byte[] data) {
/* 557 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 561 */         listener.onTextMessageError(this.mWebSocket, cause, data);
/*     */       }
/* 563 */       catch (Throwable t) {
/*     */         
/* 565 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnSendError(WebSocketException cause, WebSocketFrame frame) {
/* 573 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 577 */         listener.onSendError(this.mWebSocket, cause, frame);
/*     */       }
/* 579 */       catch (Throwable t) {
/*     */         
/* 581 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnUnexpectedError(WebSocketException cause) {
/* 589 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 593 */         listener.onUnexpectedError(this.mWebSocket, cause);
/*     */       }
/* 595 */       catch (Throwable t) {
/*     */         
/* 597 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void callHandleCallbackError(WebSocketListener listener, Throwable cause) {
/*     */     try {
/* 607 */       listener.handleCallbackError(this.mWebSocket, cause);
/*     */     }
/* 609 */     catch (Throwable throwable) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void callOnSendingHandshake(String requestLine, List<String[]> headers) {
/* 617 */     for (WebSocketListener listener : getSynchronizedListeners()) {
/*     */ 
/*     */       
/*     */       try {
/* 621 */         listener.onSendingHandshake(this.mWebSocket, requestLine, headers);
/*     */       }
/* 623 */       catch (Throwable t) {
/*     */         
/* 625 */         callHandleCallbackError(listener, t);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\ListenerManager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */