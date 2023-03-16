/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.util.Timer;
/*     */ import java.util.TimerTask;
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
/*     */ abstract class PeriodicalFrameSender
/*     */ {
/*     */   private final WebSocket mWebSocket;
/*     */   private String mTimerName;
/*     */   private Timer mTimer;
/*     */   private boolean mScheduled;
/*     */   private long mInterval;
/*     */   private PayloadGenerator mGenerator;
/*     */   
/*     */   public PeriodicalFrameSender(WebSocket webSocket, String timerName, PayloadGenerator generator) {
/*  36 */     this.mWebSocket = webSocket;
/*  37 */     this.mTimerName = timerName;
/*  38 */     this.mGenerator = generator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  44 */     setInterval(getInterval());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stop() {
/*  50 */     synchronized (this) {
/*     */       
/*  52 */       if (this.mTimer == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  57 */       this.mScheduled = false;
/*  58 */       this.mTimer.cancel();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getInterval() {
/*  65 */     synchronized (this) {
/*     */       
/*  67 */       return this.mInterval;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInterval(long interval) {
/*  74 */     if (interval < 0L)
/*     */     {
/*  76 */       interval = 0L;
/*     */     }
/*     */     
/*  79 */     synchronized (this) {
/*     */       
/*  81 */       this.mInterval = interval;
/*     */     } 
/*     */     
/*  84 */     if (interval == 0L) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  89 */     if (!this.mWebSocket.isOpen()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  94 */     synchronized (this) {
/*     */       
/*  96 */       if (this.mTimer == null)
/*     */       {
/*  98 */         if (this.mTimerName == null) {
/*     */           
/* 100 */           this.mTimer = new Timer();
/*     */         }
/*     */         else {
/*     */           
/* 104 */           this.mTimer = new Timer(this.mTimerName);
/*     */         } 
/*     */       }
/*     */       
/* 108 */       if (!this.mScheduled)
/*     */       {
/* 110 */         this.mScheduled = schedule(this.mTimer, new Task(), interval);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PayloadGenerator getPayloadGenerator() {
/* 118 */     synchronized (this) {
/*     */       
/* 120 */       return this.mGenerator;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPayloadGenerator(PayloadGenerator generator) {
/* 127 */     synchronized (this) {
/*     */       
/* 129 */       this.mGenerator = generator;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTimerName() {
/* 136 */     return this.mTimerName;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTimerName(String timerName) {
/* 142 */     synchronized (this) {
/*     */       
/* 144 */       this.mTimerName = timerName;
/*     */     } 
/*     */   }
/*     */   
/*     */   private final class Task
/*     */     extends TimerTask
/*     */   {
/*     */     private Task() {}
/*     */     
/*     */     public void run() {
/* 154 */       PeriodicalFrameSender.this.doTask();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void doTask() {
/* 161 */     synchronized (this) {
/*     */       
/* 163 */       if (this.mInterval == 0L || !this.mWebSocket.isOpen()) {
/*     */         
/* 165 */         this.mScheduled = false;
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */ 
/*     */       
/* 172 */       this.mWebSocket.sendFrame(createFrame());
/*     */ 
/*     */       
/* 175 */       this.mScheduled = schedule(this.mTimer, new Task(), this.mInterval);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private WebSocketFrame createFrame() {
/* 183 */     byte[] payload = generatePayload();
/*     */ 
/*     */     
/* 186 */     return createFrame(payload);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] generatePayload() {
/* 192 */     if (this.mGenerator == null)
/*     */     {
/* 194 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 200 */       return this.mGenerator.generate();
/*     */     }
/* 202 */     catch (Throwable t) {
/*     */ 
/*     */       
/* 205 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean schedule(Timer timer, Task task, long interval) {
/*     */     try {
/* 215 */       timer.schedule(task, interval);
/*     */ 
/*     */       
/* 218 */       return true;
/*     */     }
/* 220 */     catch (RuntimeException e) {
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
/* 237 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract WebSocketFrame createFrame(byte[] paramArrayOfbyte);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\PeriodicalFrameSender.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */