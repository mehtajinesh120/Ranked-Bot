/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.net.SocketFactory;
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
/*     */ public class SocketInitiator
/*     */ {
/*     */   private final SocketFactory mSocketFactory;
/*     */   private final Address mAddress;
/*     */   private final int mConnectTimeout;
/*     */   private final String[] mServerNames;
/*     */   private final DualStackMode mMode;
/*     */   private final int mFallbackDelay;
/*     */   
/*     */   private class Signal
/*     */   {
/*     */     private final CountDownLatch mLatch;
/*     */     private final int mMaxDelay;
/*     */     
/*     */     Signal(int maxDelay) {
/*  43 */       this.mLatch = new CountDownLatch(1);
/*  44 */       this.mMaxDelay = maxDelay;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     boolean isDone() {
/*  50 */       return (this.mLatch.getCount() == 0L);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void await() throws InterruptedException {
/*  56 */       this.mLatch.await(this.mMaxDelay, TimeUnit.MILLISECONDS);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void done() {
/*  62 */       this.mLatch.countDown();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class SocketRacer
/*     */     extends Thread
/*     */   {
/*     */     private final SocketInitiator.SocketFuture mFuture;
/*     */ 
/*     */     
/*     */     private final SocketFactory mSocketFactory;
/*     */ 
/*     */     
/*     */     private final SocketAddress mSocketAddress;
/*     */ 
/*     */     
/*     */     private String[] mServerNames;
/*     */ 
/*     */     
/*     */     private final int mConnectTimeout;
/*     */ 
/*     */     
/*     */     private final SocketInitiator.Signal mStartSignal;
/*     */ 
/*     */     
/*     */     private final SocketInitiator.Signal mDoneSignal;
/*     */ 
/*     */     
/*     */     SocketRacer(SocketInitiator.SocketFuture future, SocketFactory socketFactory, SocketAddress socketAddress, String[] serverNames, int connectTimeout, SocketInitiator.Signal startSignal, SocketInitiator.Signal doneSignal) {
/*  93 */       this.mFuture = future;
/*  94 */       this.mSocketFactory = socketFactory;
/*  95 */       this.mSocketAddress = socketAddress;
/*  96 */       this.mServerNames = serverNames;
/*  97 */       this.mConnectTimeout = connectTimeout;
/*  98 */       this.mStartSignal = startSignal;
/*  99 */       this.mDoneSignal = doneSignal;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/* 104 */       Socket socket = null;
/*     */ 
/*     */       
/*     */       try {
/* 108 */         if (this.mStartSignal != null)
/*     */         {
/* 110 */           this.mStartSignal.await();
/*     */         }
/*     */ 
/*     */         
/* 114 */         if (this.mFuture.hasSocket()) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 120 */         socket = this.mSocketFactory.createSocket();
/*     */ 
/*     */         
/* 123 */         SNIHelper.setServerNames(socket, this.mServerNames);
/*     */ 
/*     */         
/* 126 */         socket.connect(this.mSocketAddress, this.mConnectTimeout);
/*     */ 
/*     */         
/* 129 */         complete(socket);
/*     */       }
/* 131 */       catch (Exception e) {
/*     */         
/* 133 */         abort(e);
/*     */         
/* 135 */         if (socket != null) {
/*     */           
/*     */           try {
/*     */             
/* 139 */             socket.close();
/*     */           }
/* 141 */           catch (IOException iOException) {}
/*     */         }
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void complete(Socket socket) {
/* 152 */       synchronized (this.mFuture) {
/*     */ 
/*     */         
/* 155 */         if (this.mDoneSignal.isDone()) {
/*     */           return;
/*     */         }
/*     */ 
/*     */         
/* 160 */         this.mFuture.setSocket(this, socket);
/*     */ 
/*     */         
/* 163 */         this.mDoneSignal.done();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     void abort(Exception exception) {
/* 170 */       synchronized (this.mFuture) {
/*     */ 
/*     */         
/* 173 */         if (this.mDoneSignal.isDone()) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 179 */         this.mFuture.setException(exception);
/*     */ 
/*     */         
/* 182 */         this.mDoneSignal.done();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class SocketFuture
/*     */   {
/*     */     private CountDownLatch mLatch;
/*     */ 
/*     */ 
/*     */     
/*     */     private List<SocketInitiator.SocketRacer> mRacers;
/*     */ 
/*     */ 
/*     */     
/*     */     private Socket mSocket;
/*     */ 
/*     */ 
/*     */     
/*     */     private Exception mException;
/*     */ 
/*     */ 
/*     */     
/*     */     private SocketFuture() {}
/*     */ 
/*     */ 
/*     */     
/*     */     synchronized boolean hasSocket() {
/* 213 */       return (this.mSocket != null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     synchronized void setSocket(SocketInitiator.SocketRacer current, Socket socket) {
/* 220 */       if (this.mLatch == null || this.mRacers == null)
/*     */       {
/* 222 */         throw new IllegalStateException("Cannot set socket before awaiting!");
/*     */       }
/*     */ 
/*     */       
/* 226 */       if (this.mSocket == null) {
/*     */         
/* 228 */         this.mSocket = socket;
/*     */ 
/*     */         
/* 231 */         for (SocketInitiator.SocketRacer racer : this.mRacers) {
/*     */ 
/*     */           
/* 234 */           if (racer == current) {
/*     */             continue;
/*     */           }
/*     */           
/* 238 */           racer.abort(new InterruptedException());
/* 239 */           racer.interrupt();
/*     */         } 
/*     */       } else {
/*     */ 
/*     */         
/*     */         try {
/*     */           
/* 246 */           socket.close();
/*     */         }
/* 248 */         catch (IOException iOException) {}
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 255 */       this.mLatch.countDown();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     synchronized void setException(Exception exception) {
/* 262 */       if (this.mLatch == null || this.mRacers == null)
/*     */       {
/* 264 */         throw new IllegalStateException("Cannot set exception before awaiting!");
/*     */       }
/*     */ 
/*     */       
/* 268 */       if (this.mException == null)
/*     */       {
/* 270 */         this.mException = exception;
/*     */       }
/*     */ 
/*     */       
/* 274 */       this.mLatch.countDown();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Socket await(List<SocketInitiator.SocketRacer> racers) throws Exception {
/* 281 */       this.mRacers = racers;
/*     */ 
/*     */       
/* 284 */       this.mLatch = new CountDownLatch(this.mRacers.size());
/*     */ 
/*     */       
/* 287 */       for (SocketInitiator.SocketRacer racer : this.mRacers)
/*     */       {
/* 289 */         racer.start();
/*     */       }
/*     */ 
/*     */       
/* 293 */       this.mLatch.await();
/*     */ 
/*     */       
/* 296 */       if (this.mSocket != null)
/*     */       {
/* 298 */         return this.mSocket;
/*     */       }
/* 300 */       if (this.mException != null)
/*     */       {
/* 302 */         throw this.mException;
/*     */       }
/*     */ 
/*     */       
/* 306 */       throw new WebSocketException(WebSocketError.SOCKET_CONNECT_ERROR, "No viable interface to connect");
/*     */     }
/*     */   }
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
/*     */   public SocketInitiator(SocketFactory socketFactory, Address address, int connectTimeout, String[] serverNames, DualStackMode mode, int fallbackDelay) {
/* 325 */     this.mSocketFactory = socketFactory;
/* 326 */     this.mAddress = address;
/* 327 */     this.mConnectTimeout = connectTimeout;
/* 328 */     this.mServerNames = serverNames;
/* 329 */     this.mMode = mode;
/* 330 */     this.mFallbackDelay = fallbackDelay;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Socket establish(InetAddress[] addresses) throws Exception {
/* 337 */     SocketFuture future = new SocketFuture();
/*     */ 
/*     */     
/* 340 */     List<SocketRacer> racers = new ArrayList<SocketRacer>(addresses.length);
/* 341 */     int delay = 0;
/* 342 */     Signal startSignal = null;
/* 343 */     for (InetAddress address : addresses) {
/*     */ 
/*     */       
/* 346 */       if ((this.mMode != DualStackMode.IPV4_ONLY || address instanceof java.net.Inet4Address) && (this.mMode != DualStackMode.IPV6_ONLY || address instanceof java.net.Inet6Address)) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 353 */         delay += this.mFallbackDelay;
/*     */ 
/*     */         
/* 356 */         Signal doneSignal = new Signal(delay);
/*     */ 
/*     */         
/* 359 */         SocketAddress socketAddress = new InetSocketAddress(address, this.mAddress.getPort());
/* 360 */         SocketRacer racer = new SocketRacer(future, this.mSocketFactory, socketAddress, this.mServerNames, this.mConnectTimeout, startSignal, doneSignal);
/*     */ 
/*     */         
/* 363 */         racers.add(racer);
/*     */ 
/*     */         
/* 366 */         startSignal = doneSignal;
/*     */       } 
/*     */     } 
/*     */     
/* 370 */     return future.await(racers);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\SocketInitiator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */