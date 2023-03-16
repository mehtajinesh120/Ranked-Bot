/*    */ package net.dv8tion.jda.api.events.user.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.User;
/*    */ import net.dv8tion.jda.api.events.UpdateEvent;
/*    */ import net.dv8tion.jda.api.events.user.GenericUserEvent;
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
/*    */ public abstract class GenericUserUpdateEvent<T>
/*    */   extends GenericUserEvent
/*    */   implements UpdateEvent<User, T>
/*    */ {
/*    */   protected final T previous;
/*    */   protected final T next;
/*    */   protected final String identifier;
/*    */   
/*    */   public GenericUserUpdateEvent(@Nonnull JDA api, long responseNumber, @Nonnull User user, @Nullable T previous, @Nullable T next, @Nonnull String identifier) {
/* 55 */     super(api, responseNumber, user);
/* 56 */     this.previous = previous;
/* 57 */     this.next = next;
/* 58 */     this.identifier = identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public User getEntity() {
/* 65 */     return getUser();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getPropertyIdentifier() {
/* 72 */     return this.identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getOldValue() {
/* 79 */     return this.previous;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getNewValue() {
/* 86 */     return this.next;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 92 */     return "UserUpdate[" + getPropertyIdentifier() + "](" + getOldValue() + "->" + getNewValue() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\event\\use\\update\GenericUserUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */