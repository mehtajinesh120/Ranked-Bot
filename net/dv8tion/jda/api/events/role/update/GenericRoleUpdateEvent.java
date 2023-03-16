/*    */ package net.dv8tion.jda.api.events.role.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Role;
/*    */ import net.dv8tion.jda.api.events.UpdateEvent;
/*    */ import net.dv8tion.jda.api.events.role.GenericRoleEvent;
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
/*    */ public abstract class GenericRoleUpdateEvent<T>
/*    */   extends GenericRoleEvent
/*    */   implements UpdateEvent<Role, T>
/*    */ {
/*    */   protected final T previous;
/*    */   protected final T next;
/*    */   protected final String identifier;
/*    */   
/*    */   public GenericRoleUpdateEvent(@Nonnull JDA api, long responseNumber, @Nonnull Role role, @Nullable T previous, @Nullable T next, @Nonnull String identifier) {
/* 43 */     super(api, responseNumber, role);
/* 44 */     this.previous = previous;
/* 45 */     this.next = next;
/* 46 */     this.identifier = identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Role getEntity() {
/* 53 */     return this.role;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getPropertyIdentifier() {
/* 60 */     return this.identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getOldValue() {
/* 67 */     return this.previous;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getNewValue() {
/* 74 */     return this.next;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 80 */     return "RoleUpdate[" + getPropertyIdentifier() + "](" + getOldValue() + "->" + getNewValue() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\rol\\update\GenericRoleUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */