/*     */ package net.dv8tion.jda.api.hooks;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
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
/*     */ public class InterfacedEventManager
/*     */   implements IEventManager
/*     */ {
/*  44 */   private final CopyOnWriteArrayList<EventListener> listeners = new CopyOnWriteArrayList<>();
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
/*     */   public void register(@Nonnull Object listener) {
/*  60 */     if (!(listener instanceof EventListener))
/*     */     {
/*  62 */       throw new IllegalArgumentException("Listener must implement EventListener");
/*     */     }
/*  64 */     this.listeners.add((EventListener)listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void unregister(@Nonnull Object listener) {
/*  70 */     if (!(listener instanceof EventListener))
/*     */     {
/*     */       
/*  73 */       JDALogger.getLog(getClass()).warn("Trying to remove a listener that does not implement EventListener: {}", 
/*     */           
/*  75 */           (listener == null) ? "null" : listener.getClass().getName());
/*     */     }
/*     */ 
/*     */     
/*  79 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Object> getRegisteredListeners() {
/*  86 */     return Collections.unmodifiableList(new ArrayList(this.listeners));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void handle(@Nonnull GenericEvent event) {
/*  92 */     for (EventListener listener : this.listeners) {
/*     */ 
/*     */       
/*     */       try {
/*  96 */         listener.onEvent(event);
/*     */       }
/*  98 */       catch (Throwable throwable) {
/*     */         
/* 100 */         JDAImpl.LOG.error("One of the EventListeners had an uncaught exception", throwable);
/* 101 */         if (throwable instanceof Error)
/* 102 */           throw (Error)throwable; 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\hooks\InterfacedEventManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */