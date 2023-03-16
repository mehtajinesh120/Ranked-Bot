/*    */ package net.dv8tion.jda.api.events.role;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Role;
/*    */ import net.dv8tion.jda.api.events.Event;
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
/*    */ public abstract class GenericRoleEvent
/*    */   extends Event
/*    */ {
/*    */   protected final Role role;
/*    */   
/*    */   public GenericRoleEvent(@Nonnull JDA api, long responseNumber, @Nonnull Role role) {
/* 38 */     super(api, responseNumber);
/* 39 */     this.role = role;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Role getRole() {
/* 50 */     return this.role;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Guild getGuild() {
/* 61 */     return this.role.getGuild();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\role\GenericRoleEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */