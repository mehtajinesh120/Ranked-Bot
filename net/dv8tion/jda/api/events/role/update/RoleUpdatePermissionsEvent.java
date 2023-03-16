/*     */ package net.dv8tion.jda.api.events.role.update;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RoleUpdatePermissionsEvent
/*     */   extends GenericRoleUpdateEvent<EnumSet<Permission>>
/*     */ {
/*     */   public static final String IDENTIFIER = "permission";
/*     */   private final long oldPermissionsRaw;
/*     */   private final long newPermissionsRaw;
/*     */   
/*     */   public RoleUpdatePermissionsEvent(@Nonnull JDA api, long responseNumber, @Nonnull Role role, long oldPermissionsRaw) {
/*  42 */     super(api, responseNumber, role, Permission.getPermissions(oldPermissionsRaw), role.getPermissions(), "permission");
/*  43 */     this.oldPermissionsRaw = oldPermissionsRaw;
/*  44 */     this.newPermissionsRaw = role.getPermissionsRaw();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getOldPermissions() {
/*  55 */     return getOldValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getOldPermissionsRaw() {
/*  65 */     return this.oldPermissionsRaw;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getNewPermissions() {
/*  76 */     return getNewValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getNewPermissionsRaw() {
/*  86 */     return this.newPermissionsRaw;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getOldValue() {
/*  93 */     return super.getOldValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getNewValue() {
/* 100 */     return super.getNewValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\rol\\update\RoleUpdatePermissionsEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */