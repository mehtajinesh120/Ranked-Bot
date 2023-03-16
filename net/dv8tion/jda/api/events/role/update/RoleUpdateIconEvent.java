/*    */ package net.dv8tion.jda.api.events.role.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Role;
/*    */ import net.dv8tion.jda.api.entities.RoleIcon;
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
/*    */ public class RoleUpdateIconEvent
/*    */   extends GenericRoleUpdateEvent<RoleIcon>
/*    */ {
/*    */   public static final String IDENTIFIER = "icon";
/*    */   
/*    */   public RoleUpdateIconEvent(@Nonnull JDA api, long responseNumber, @Nonnull Role role, @Nullable RoleIcon oldIcon) {
/* 39 */     super(api, responseNumber, role, oldIcon, role.getIcon(), "icon");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public RoleIcon getOldIcon() {
/* 50 */     return getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public RoleIcon getNewIcon() {
/* 61 */     return getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\rol\\update\RoleUpdateIconEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */