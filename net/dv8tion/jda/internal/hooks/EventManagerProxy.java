/*     */ package net.dv8tion.jda.internal.hooks;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.hooks.IEventManager;
/*     */ import net.dv8tion.jda.api.hooks.InterfacedEventManager;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
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
/*     */ public class EventManagerProxy
/*     */   implements IEventManager
/*     */ {
/*     */   private final ExecutorService executor;
/*     */   private IEventManager subject;
/*     */   
/*     */   public EventManagerProxy(IEventManager subject, ExecutorService executor) {
/*  36 */     this.subject = subject;
/*  37 */     this.executor = executor;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSubject(IEventManager subject) {
/*  42 */     this.subject = (subject == null) ? (IEventManager)new InterfacedEventManager() : subject;
/*     */   }
/*     */ 
/*     */   
/*     */   public IEventManager getSubject() {
/*  47 */     return this.subject;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void register(@Nonnull Object listener) {
/*  53 */     this.subject.register(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(@Nonnull Object listener) {
/*  59 */     this.subject.unregister(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handle(@Nonnull GenericEvent event) {
/*     */     try {
/*  67 */       if (this.executor != null && !this.executor.isShutdown()) {
/*  68 */         this.executor.execute(() -> handleInternally(event));
/*     */       } else {
/*  70 */         handleInternally(event);
/*     */       } 
/*  72 */     } catch (RejectedExecutionException ex) {
/*     */       
/*  74 */       JDAImpl.LOG.warn("Event-Pool rejected event execution! Running on handling thread instead...");
/*  75 */       handleInternally(event);
/*     */     }
/*  77 */     catch (Exception ex) {
/*     */       
/*  79 */       JDAImpl.LOG.error("Encountered exception trying to schedule event", ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleInternally(@Nonnull GenericEvent event) {
/*     */     try {
/*  88 */       this.subject.handle(event);
/*     */     }
/*  90 */     catch (RuntimeException e) {
/*     */       
/*  92 */       JDAImpl.LOG.error("The EventManager.handle() call had an uncaught exception", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Object> getRegisteredListeners() {
/* 100 */     return this.subject.getRegisteredListeners();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\hooks\EventManagerProxy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */