/*     */ package net.dv8tion.jda.api.audio.factory;
/*     */ 
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.NoRouteToHostException;
/*     */ import java.net.SocketException;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.annotation.CheckForNull;
/*     */ import net.dv8tion.jda.internal.audio.AudioConnection;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import org.slf4j.MDC;
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
/*     */ public class DefaultSendSystem
/*     */   implements IAudioSendSystem
/*     */ {
/*     */   private final IPacketProvider packetProvider;
/*     */   private Thread sendThread;
/*     */   private ConcurrentMap<String, String> contextMap;
/*     */   
/*     */   public DefaultSendSystem(IPacketProvider packetProvider) {
/*  44 */     this.packetProvider = packetProvider;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setContextMap(@CheckForNull ConcurrentMap<String, String> contextMap) {
/*  50 */     this.contextMap = contextMap;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void start() {
/*  56 */     DatagramSocket udpSocket = this.packetProvider.getUdpSocket();
/*     */     
/*  58 */     this.sendThread = new Thread(() -> {
/*     */           if (this.contextMap != null) {
/*     */             MDC.setContextMap(this.contextMap);
/*     */           }
/*     */           
/*     */           long lastFrameSent = System.currentTimeMillis();
/*     */           
/*     */           boolean sentPacket = true;
/*     */           while (!udpSocket.isClosed() && !this.sendThread.isInterrupted()) {
/*     */             try {
/*  68 */               boolean changeTalking = (!sentPacket || System.currentTimeMillis() - lastFrameSent > 20L);
/*     */               DatagramPacket packet = this.packetProvider.getNextPacket(changeTalking);
/*     */               sentPacket = (packet != null);
/*     */               if (sentPacket) {
/*     */                 udpSocket.send(packet);
/*     */               }
/*     */               long sleepTime = 20L - System.currentTimeMillis() - lastFrameSent;
/*  75 */             } catch (NoRouteToHostException e) {
/*     */               this.packetProvider.onConnectionLost();
/*     */               
/*     */               long sleepTime = 20L - System.currentTimeMillis() - lastFrameSent;
/*  79 */             } catch (SocketException socketException) {
/*     */               
/*     */               long sleepTime = 20L - System.currentTimeMillis() - lastFrameSent;
/*     */             }
/*  83 */             catch (Exception e) {
/*     */               AudioConnection.LOG.error("Error while sending udp audio data", e);
/*     */ 
/*     */ 
/*     */               
/*     */               long sleepTime = 20L - System.currentTimeMillis() - lastFrameSent;
/*     */             } finally {
/*     */               long sleepTime = 20L - System.currentTimeMillis() - lastFrameSent;
/*     */ 
/*     */               
/*     */               if (sleepTime > 0L) {
/*     */                 try {
/*     */                   Thread.sleep(sleepTime);
/*  96 */                 } catch (InterruptedException e) {
/*     */                   Thread.currentThread().interrupt();
/*     */                 } 
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               if (System.currentTimeMillis() < lastFrameSent + 60L) {
/*     */                 lastFrameSent += 20L;
/*     */               } else {
/*     */                 lastFrameSent = System.currentTimeMillis();
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 115 */     this.sendThread.setUncaughtExceptionHandler((thread, throwable) -> {
/*     */           JDALogger.getLog(DefaultSendSystem.class).error("Uncaught exception in audio send thread", throwable);
/*     */           
/*     */           start();
/*     */         });
/* 120 */     this.sendThread.setDaemon(true);
/* 121 */     this.sendThread.setName(this.packetProvider.getIdentifier() + " Sending Thread");
/* 122 */     this.sendThread.setPriority(7);
/* 123 */     this.sendThread.start();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shutdown() {
/* 129 */     if (this.sendThread != null)
/* 130 */       this.sendThread.interrupt(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\factory\DefaultSendSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */