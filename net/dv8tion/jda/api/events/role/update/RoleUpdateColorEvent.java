/*    */ package net.dv8tion.jda.api.events.role.update;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
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
/*    */ public class RoleUpdateColorEvent
/*    */   extends GenericRoleUpdateEvent<Integer>
/*    */ {
/*    */   public static final String IDENTIFIER = "color";
/*    */   
/*    */   public RoleUpdateColorEvent(@Nonnull JDA api, long responseNumber, @Nonnull Role role, int oldColor) {
/* 39 */     super(api, responseNumber, role, Integer.valueOf(oldColor), Integer.valueOf(role.getColorRaw()), "color");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Color getOldColor() {
/* 50 */     return (this.previous.intValue() != 536870911) ? new Color(this.previous.intValue()) : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOldColorRaw() {
/* 60 */     return getOldValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Color getNewColor() {
/* 71 */     return (this.next.intValue() != 536870911) ? new Color(this.next.intValue()) : null;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNewColorRaw() {
/* 81 */     return getNewValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Integer getOldValue() {
/* 88 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Integer getNewValue() {
/* 95 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\rol\\update\RoleUpdateColorEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */