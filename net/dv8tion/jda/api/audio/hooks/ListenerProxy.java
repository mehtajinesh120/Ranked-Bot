/*     */ package net.dv8tion.jda.api.audio.hooks;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.audio.SpeakingMode;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class ListenerProxy
/*     */   implements ConnectionListener
/*     */ {
/*  29 */   private static final Logger log = LoggerFactory.getLogger(ListenerProxy.class);
/*  30 */   private volatile ConnectionListener listener = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public void onPing(long ping) {
/*  35 */     if (this.listener == null)
/*     */       return; 
/*  37 */     ConnectionListener listener = this.listener;
/*     */     
/*     */     try {
/*  40 */       if (listener != null) {
/*  41 */         listener.onPing(ping);
/*     */       }
/*  43 */     } catch (Throwable t) {
/*     */       
/*  45 */       log.error("The ConnectionListener encountered and uncaught exception", t);
/*  46 */       if (t instanceof Error) {
/*  47 */         throw (Error)t;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStatusChange(@Nonnull ConnectionStatus status) {
/*  54 */     if (this.listener == null)
/*     */       return; 
/*  56 */     ConnectionListener listener = this.listener;
/*     */     
/*     */     try {
/*  59 */       if (listener != null) {
/*  60 */         listener.onStatusChange(status);
/*     */       }
/*  62 */     } catch (Throwable t) {
/*     */       
/*  64 */       log.error("The ConnectionListener encountered and uncaught exception", t);
/*  65 */       if (t instanceof Error) {
/*  66 */         throw (Error)t;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUserSpeaking(@Nonnull User user, @Nonnull EnumSet<SpeakingMode> modes) {
/*  73 */     if (this.listener == null)
/*     */       return; 
/*  75 */     ConnectionListener listener = this.listener;
/*     */     
/*     */     try {
/*  78 */       if (listener != null)
/*     */       {
/*  80 */         listener.onUserSpeaking(user, modes);
/*  81 */         listener.onUserSpeaking(user, modes.contains(SpeakingMode.VOICE));
/*  82 */         listener.onUserSpeaking(user, modes.contains(SpeakingMode.VOICE), modes.contains(SpeakingMode.SOUNDSHARE));
/*     */       }
/*     */     
/*  85 */     } catch (Throwable t) {
/*     */       
/*  87 */       log.error("The ConnectionListener encountered and uncaught exception", t);
/*  88 */       if (t instanceof Error) {
/*  89 */         throw (Error)t;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUserSpeaking(@Nonnull User user, boolean speaking) {}
/*     */   
/*     */   public void setListener(ConnectionListener listener) {
/*  98 */     this.listener = listener;
/*     */   }
/*     */ 
/*     */   
/*     */   public ConnectionListener getListener() {
/* 103 */     return this.listener;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\hooks\ListenerProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */