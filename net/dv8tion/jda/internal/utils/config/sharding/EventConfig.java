/*    */ package net.dv8tion.jda.internal.utils.config.sharding;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.function.IntFunction;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.hooks.IEventManager;
/*    */ import net.dv8tion.jda.internal.utils.Checks;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EventConfig
/*    */ {
/* 30 */   private final List<Object> listeners = new ArrayList();
/* 31 */   private final List<IntFunction<Object>> listenerProviders = new ArrayList<>();
/*    */   
/*    */   private final IntFunction<? extends IEventManager> eventManagerProvider;
/*    */   
/*    */   public EventConfig(@Nullable IntFunction<? extends IEventManager> eventManagerProvider) {
/* 36 */     this.eventManagerProvider = eventManagerProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addEventListener(@Nonnull Object listener) {
/* 41 */     Checks.notNull(listener, "Listener");
/* 42 */     this.listeners.add(listener);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeEventListener(@Nonnull Object listener) {
/* 47 */     Checks.notNull(listener, "Listener");
/* 48 */     this.listeners.remove(listener);
/*    */   }
/*    */ 
/*    */   
/*    */   public void addEventListenerProvider(@Nonnull IntFunction<Object> provider) {
/* 53 */     Checks.notNull(provider, "Provider");
/* 54 */     this.listenerProviders.add(provider);
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeEventListenerProvider(@Nonnull IntFunction<Object> provider) {
/* 59 */     Checks.notNull(provider, "Provider");
/* 60 */     this.listenerProviders.remove(provider);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<Object> getListeners() {
/* 66 */     return this.listeners;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<IntFunction<Object>> getListenerProviders() {
/* 72 */     return this.listenerProviders;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public IntFunction<? extends IEventManager> getEventManagerProvider() {
/* 78 */     return this.eventManagerProvider;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static EventConfig getDefault() {
/* 84 */     return new EventConfig(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\sharding\EventConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */