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
/*    */ 
/*    */ public class RoleUpdatePositionEvent
/*    */   extends GenericRoleUpdateEvent<Integer>
/*    */ {
/*    */   public static final String IDENTIFIER = "position";
/*    */   private final int oldPositionRaw;
/*    */   private final int newPositionRaw;
/*    */   
/*    */   public RoleUpdatePositionEvent(@Nonnull JDA api, long responseNumber, @Nonnull Role role, int oldPosition, int oldPositionRaw) {
/* 40 */     super(api, responseNumber, role, Integer.valueOf(oldPosition), Integer.valueOf(role.getPosition()), "position");
/* 41 */     this.oldPositionRaw = oldPositionRaw;
/* 42 */     this.newPositionRaw = role.getPositionRaw();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOldPosition() {
/* 52 */     return getOldValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOldPositionRaw() {
/* 62 */     return this.oldPositionRaw;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNewPosition() {
/* 72 */     return getNewValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNewPositionRaw() {
/* 82 */     return this.newPositionRaw;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Integer getOldValue() {
/* 89 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Integer getNewValue() {
/* 96 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\rol\\update\RoleUpdatePositionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */