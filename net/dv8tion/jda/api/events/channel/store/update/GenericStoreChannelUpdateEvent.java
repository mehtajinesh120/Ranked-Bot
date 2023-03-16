/*    */ package net.dv8tion.jda.api.events.channel.store.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.StoreChannel;
/*    */ import net.dv8tion.jda.api.events.UpdateEvent;
/*    */ import net.dv8tion.jda.api.events.channel.store.GenericStoreChannelEvent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GenericStoreChannelUpdateEvent<T>
/*    */   extends GenericStoreChannelEvent
/*    */   implements UpdateEvent<StoreChannel, T>
/*    */ {
/*    */   protected final T prev;
/*    */   protected final T next;
/*    */   protected final String identifier;
/*    */   
/*    */   public GenericStoreChannelUpdateEvent(@Nonnull JDA api, long responseNumber, @Nonnull StoreChannel channel, @Nullable T prev, @Nullable T next, @Nonnull String identifier) {
/* 42 */     super(api, responseNumber, channel);
/* 43 */     this.prev = prev;
/* 44 */     this.next = next;
/* 45 */     this.identifier = identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getPropertyIdentifier() {
/* 52 */     return this.identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public StoreChannel getEntity() {
/* 59 */     return this.channel;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getOldValue() {
/* 66 */     return this.prev;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getNewValue() {
/* 73 */     return this.next;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 79 */     return "StoreChannelUpdate[" + getPropertyIdentifier() + "](" + getOldValue() + "->" + getNewValue() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\stor\\update\GenericStoreChannelUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */