/*     */ package net.dv8tion.jda.api.managers;
/*     */ 
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Icon;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.Webhook;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface WebhookManager
/*     */   extends Manager<WebhookManager>
/*     */ {
/*     */   public static final long NAME = 1L;
/*     */   public static final long CHANNEL = 2L;
/*     */   public static final long AVATAR = 4L;
/*     */   
/*     */   @Nonnull
/*     */   default TextChannel getChannel() {
/* 114 */     return getWebhook().getChannel();
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
/*     */   @Nonnull
/*     */   default Guild getGuild() {
/* 127 */     return getWebhook().getGuild();
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   WebhookManager reset(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   WebhookManager reset(long... paramVarArgs);
/*     */   
/*     */   @Nonnull
/*     */   Webhook getWebhook();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookManager setName(@Nonnull String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookManager setAvatar(@Nullable Icon paramIcon);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   WebhookManager setChannel(@Nonnull TextChannel paramTextChannel);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\managers\WebhookManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */