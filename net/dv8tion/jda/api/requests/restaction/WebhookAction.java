/*    */ package net.dv8tion.jda.api.requests.restaction;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import javax.annotation.CheckReturnValue;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Icon;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ import net.dv8tion.jda.api.entities.Webhook;
/*    */ import net.dv8tion.jda.api.requests.RestAction;
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
/*    */ public interface WebhookAction
/*    */   extends AuditableRestAction<Webhook>
/*    */ {
/*    */   @Nonnull
/*    */   @CheckReturnValue
/*    */   WebhookAction setAvatar(@Nullable Icon paramIcon);
/*    */   
/*    */   @Nonnull
/*    */   @CheckReturnValue
/*    */   WebhookAction setName(@Nonnull String paramString);
/*    */   
/*    */   @Nonnull
/*    */   default Guild getGuild() {
/* 66 */     return getChannel().getGuild();
/*    */   }
/*    */   
/*    */   @Nonnull
/*    */   TextChannel getChannel();
/*    */   
/*    */   @Nonnull
/*    */   WebhookAction deadline(long paramLong);
/*    */   
/*    */   @Nonnull
/*    */   WebhookAction timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
/*    */   
/*    */   @Nonnull
/*    */   WebhookAction setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\WebhookAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */