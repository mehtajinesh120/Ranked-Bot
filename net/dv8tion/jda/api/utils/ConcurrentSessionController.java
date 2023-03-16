/*     */ package net.dv8tion.jda.api.utils;
/*     */ 
/*     */ import com.neovisionaries.ws.client.OpeningHandshakeException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Queue;
/*     */ import java.util.concurrent.ConcurrentLinkedQueue;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
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
/*     */ public class ConcurrentSessionController
/*     */   extends SessionControllerAdapter
/*     */   implements SessionController
/*     */ {
/*  50 */   private Worker[] workers = new Worker[1];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setConcurrency(int level) {
/*  56 */     assert level > 0 && level < Integer.MAX_VALUE;
/*  57 */     this.workers = new Worker[level];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void appendSession(@Nonnull SessionController.SessionConnectNode node) {
/*  63 */     getWorker(node).enqueue(node);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeSession(@Nonnull SessionController.SessionConnectNode node) {
/*  69 */     getWorker(node).dequeue(node);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized Worker getWorker(SessionController.SessionConnectNode node) {
/*  75 */     int i = node.getShardInfo().getShardId() % this.workers.length;
/*  76 */     Worker worker = this.workers[i];
/*  77 */     if (worker == null) {
/*     */       
/*  79 */       log.debug("Creating new worker handle for shard pool {}", Integer.valueOf(i));
/*  80 */       this.workers[i] = worker = new Worker(i);
/*     */     } 
/*  82 */     return worker;
/*     */   }
/*     */   
/*     */   private static class Worker
/*     */     implements Runnable {
/*  87 */     private final Queue<SessionController.SessionConnectNode> queue = new ConcurrentLinkedQueue<>();
/*     */     
/*     */     private final int id;
/*     */     private Thread thread;
/*     */     
/*     */     public Worker(int id) {
/*  93 */       this.id = id;
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized void start() {
/*  98 */       if (this.thread == null) {
/*     */         
/* 100 */         this.thread = new Thread(this, "ConcurrentSessionController-Worker-" + this.id);
/* 101 */         SessionControllerAdapter.log.debug("Running worker");
/* 102 */         this.thread.start();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public synchronized void stop() {
/* 108 */       this.thread = null;
/* 109 */       if (!this.queue.isEmpty()) {
/* 110 */         start();
/*     */       }
/*     */     }
/*     */     
/*     */     public void enqueue(SessionController.SessionConnectNode node) {
/* 115 */       SessionControllerAdapter.log.trace("Appending node to queue {}", node.getShardInfo());
/* 116 */       this.queue.add(node);
/* 117 */       start();
/*     */     }
/*     */ 
/*     */     
/*     */     public void dequeue(SessionController.SessionConnectNode node) {
/* 122 */       SessionControllerAdapter.log.trace("Removing node from queue {}", node.getShardInfo());
/* 123 */       this.queue.remove(node);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       try {
/* 131 */         while (!this.queue.isEmpty())
/*     */         {
/* 133 */           processQueue();
/*     */           
/* 135 */           TimeUnit.SECONDS.sleep(5L);
/*     */         }
/*     */       
/* 138 */       } catch (InterruptedException ex) {
/*     */         
/* 140 */         SessionControllerAdapter.log.error("Worker failed to process queue", ex);
/*     */       }
/*     */       finally {
/*     */         
/* 144 */         stop();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private void processQueue() throws InterruptedException {
/* 150 */       SessionController.SessionConnectNode node = null;
/*     */ 
/*     */       
/* 153 */       try { node = this.queue.remove();
/* 154 */         SessionControllerAdapter.log.debug("Running connect node for shard {}", node.getShardInfo());
/* 155 */         node.run(false); }
/*     */       
/* 157 */       catch (NoSuchElementException noSuchElementException) {  }
/* 158 */       catch (InterruptedException e)
/*     */       
/* 160 */       { this.queue.add(node);
/* 161 */         throw e; }
/*     */       
/* 163 */       catch (IllegalStateException|net.dv8tion.jda.api.exceptions.ErrorResponseException e)
/*     */       
/* 165 */       { if (Helpers.hasCause(e, OpeningHandshakeException.class)) {
/* 166 */           SessionControllerAdapter.log.error("Failed opening handshake, appending to queue. Message: {}", e.getMessage());
/* 167 */         } else if (!(e instanceof net.dv8tion.jda.api.exceptions.ErrorResponseException) || !(e.getCause() instanceof java.io.IOException)) {
/* 168 */           if (Helpers.hasCause(e, UnknownHostException.class))
/* 169 */           { SessionControllerAdapter.log.error("DNS resolution failed: {}", e.getMessage()); }
/* 170 */           else if (e.getCause() != null && !JDA.Status.RECONNECT_QUEUED.name().equals(e.getCause().getMessage()))
/* 171 */           { SessionControllerAdapter.log.error("Failed to establish connection for a node, appending to queue", e); }
/*     */           else
/* 173 */           { SessionControllerAdapter.log.error("Unexpected exception when running connect node", e); } 
/* 174 */         }  if (node != null)
/* 175 */           this.queue.add(node);  }
/*     */     
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\ConcurrentSessionController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */