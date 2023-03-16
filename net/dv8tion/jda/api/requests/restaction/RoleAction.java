/*     */ package net.dv8tion.jda.api.requests.restaction;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Icon;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
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
/*     */ public interface RoleAction
/*     */   extends AuditableRestAction<Role>
/*     */ {
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default RoleAction setColor(@Nullable Color color) {
/* 119 */     return setColor((color != null) ? Integer.valueOf(color.getRGB()) : null);
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RoleAction setPermissions(@Nullable Permission... permissions) {
/* 157 */     if (permissions != null) {
/* 158 */       Checks.noneNull((Object[])permissions, "Permissions");
/*     */     }
/* 160 */     return setPermissions((permissions == null) ? null : Long.valueOf(Permission.getRaw(permissions)));
/*     */   }
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default RoleAction setPermissions(@Nullable Collection<Permission> permissions) {
/* 185 */     if (permissions != null) {
/* 186 */       Checks.noneNull(permissions, "Permissions");
/*     */     }
/* 188 */     return setPermissions((permissions == null) ? null : Long.valueOf(Permission.getRaw(permissions)));
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   RoleAction setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
/*     */   
/*     */   @Nonnull
/*     */   RoleAction timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
/*     */   
/*     */   @Nonnull
/*     */   RoleAction deadline(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   Guild getGuild();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RoleAction setName(@Nullable String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RoleAction setHoisted(@Nullable Boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RoleAction setMentionable(@Nullable Boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RoleAction setColor(@Nullable Integer paramInteger);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RoleAction setPermissions(@Nullable Long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RoleAction setIcon(@Nullable Icon paramIcon);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RoleAction setIcon(@Nullable String paramString);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\RoleAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */