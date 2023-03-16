/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
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
/*     */ public interface ApplicationInfo
/*     */   extends ISnowflake
/*     */ {
/*     */   boolean doesBotRequireCodeGrant();
/*     */   
/*     */   @Nonnull
/*     */   String getDescription();
/*     */   
/*     */   @Nullable
/*     */   String getTermsOfServiceUrl();
/*     */   
/*     */   @Nullable
/*     */   String getPrivacyPolicyUrl();
/*     */   
/*     */   @Nullable
/*     */   String getIconId();
/*     */   
/*     */   @Nullable
/*     */   String getIconUrl();
/*     */   
/*     */   @Nullable
/*     */   ApplicationTeam getTeam();
/*     */   
/*     */   @Nonnull
/*     */   ApplicationInfo setRequiredScopes(@Nonnull String... scopes) {
/* 114 */     Checks.noneNull((Object[])scopes, "Scopes");
/* 115 */     return setRequiredScopes(Arrays.asList(scopes));
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
/*     */   @Nonnull
/*     */   ApplicationInfo setRequiredScopes(@Nonnull Collection<String> paramCollection);
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
/*     */   default String getInviteUrl(@Nullable Collection<Permission> permissions) {
/* 149 */     return getInviteUrl((String)null, permissions);
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
/*     */   @Nonnull
/*     */   String getInviteUrl(@Nullable Permission... permissions) {
/* 167 */     return getInviteUrl((String)null, permissions);
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
/*     */   @Nonnull
/*     */   String getInviteUrl(@Nullable String paramString, @Nullable Collection<Permission> paramCollection);
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
/*     */   default String getInviteUrl(long guildId, @Nullable Collection<Permission> permissions) {
/* 209 */     return getInviteUrl(Long.toUnsignedString(guildId), permissions);
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
/*     */   String getInviteUrl(@Nullable String guildId, @Nullable Permission... permissions) {
/* 233 */     return getInviteUrl(guildId, (permissions == null) ? null : Arrays.<Permission>asList(permissions));
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
/*     */   @Nonnull
/*     */   String getInviteUrl(long guildId, @Nullable Permission... permissions) {
/* 254 */     return getInviteUrl(Long.toUnsignedString(guildId), permissions);
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   JDA getJDA();
/*     */   
/*     */   @Nonnull
/*     */   String getName();
/*     */   
/*     */   @Nonnull
/*     */   User getOwner();
/*     */   
/*     */   boolean isBotPublic();
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\ApplicationInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */