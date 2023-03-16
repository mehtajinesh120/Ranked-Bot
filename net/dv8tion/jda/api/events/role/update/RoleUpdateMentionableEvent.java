/*    */ package net.dv8tion.jda.api.events.role.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Role;
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
/*    */ public class RoleUpdateMentionableEvent
/*    */   extends GenericRoleUpdateEvent<Boolean>
/*    */ {
/*    */   public static final String IDENTIFIER = "mentionable";
/*    */   
/*    */   public RoleUpdateMentionableEvent(@Nonnull JDA api, long responseNumber, @Nonnull Role role, boolean wasMentionable) {
/* 37 */     super(api, responseNumber, role, Boolean.valueOf(wasMentionable), Boolean.valueOf(!wasMentionable), "mentionable");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean wasMentionable() {
/* 47 */     return getOldValue().booleanValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Boolean getOldValue() {
/* 54 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Boolean getNewValue() {
/* 61 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\rol\\update\RoleUpdateMentionableEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */