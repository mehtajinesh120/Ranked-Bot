/*    */ package net.dv8tion.jda.api.exceptions;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.Permission;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PermissionException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final Permission permission;
/*    */   
/*    */   public PermissionException(String reason) {
/* 39 */     this(Permission.UNKNOWN, reason);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected PermissionException(@Nonnull Permission permission) {
/* 50 */     this(permission, "Cannot perform action due to a lack of Permission. Missing permission: " + permission.toString());
/*    */   }
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
/*    */   protected PermissionException(@Nonnull Permission permission, String reason) {
/* 63 */     super(reason);
/* 64 */     Checks.notNull(permission, "permission");
/* 65 */     this.permission = permission;
/*    */   }
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
/*    */   @Nonnull
/*    */   public Permission getPermission() {
/* 79 */     return this.permission;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\exceptions\PermissionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */