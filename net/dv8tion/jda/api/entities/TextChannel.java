/*      */ package net.dv8tion.jda.api.entities;
/*      */ 
/*      */ import java.util.Collection;
/*      */ import java.util.Formatter;
/*      */ import java.util.List;
/*      */ import javax.annotation.CheckReturnValue;
/*      */ import javax.annotation.Nonnull;
/*      */ import javax.annotation.Nullable;
/*      */ import net.dv8tion.jda.api.Permission;
/*      */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*      */ import net.dv8tion.jda.api.exceptions.MissingAccessException;
/*      */ import net.dv8tion.jda.api.requests.Request;
/*      */ import net.dv8tion.jda.api.requests.Response;
/*      */ import net.dv8tion.jda.api.requests.RestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.ChannelAction;
/*      */ import net.dv8tion.jda.api.requests.restaction.WebhookAction;
/*      */ import net.dv8tion.jda.api.utils.MiscUtil;
/*      */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*      */ import net.dv8tion.jda.internal.requests.Route;
/*      */ import net.dv8tion.jda.internal.utils.Checks;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public interface TextChannel
/*      */   extends GuildChannel, MessageChannel
/*      */ {
/*      */   public static final int MAX_SLOWMODE = 21600;
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Webhook.WebhookReference> follow(long targetChannelId) {
/*  228 */     return follow(Long.toUnsignedString(targetChannelId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Webhook.WebhookReference> follow(@Nonnull TextChannel targetChannel) {
/*  265 */     Checks.notNull(targetChannel, "Target Channel");
/*  266 */     Member selfMember = targetChannel.getGuild().getSelfMember();
/*  267 */     if (!selfMember.hasAccess(targetChannel))
/*  268 */       throw new MissingAccessException(targetChannel, Permission.VIEW_CHANNEL); 
/*  269 */     if (!selfMember.hasPermission(targetChannel, new Permission[] { Permission.MANAGE_WEBHOOKS }))
/*  270 */       throw new InsufficientPermissionException(targetChannel, Permission.MANAGE_WEBHOOKS); 
/*  271 */     return follow(targetChannel.getId());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Void> clearReactionsById(long messageId) {
/*  475 */     return clearReactionsById(Long.toUnsignedString(messageId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Void> clearReactionsById(long messageId, @Nonnull String unicode) {
/*  600 */     return clearReactionsById(Long.toUnsignedString(messageId), unicode);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Void> clearReactionsById(long messageId, @Nonnull Emote emote) {
/*  637 */     return clearReactionsById(Long.toUnsignedString(messageId), emote);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Void> removeReactionById(long messageId, @Nonnull String unicode, @Nonnull User user) {
/*  766 */     return removeReactionById(Long.toUnsignedString(messageId), unicode, user);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Void> removeReactionById(@Nonnull String messageId, @Nonnull Emote emote, @Nonnull User user) {
/*  826 */     Checks.notNull(emote, "Emote");
/*  827 */     return removeReactionById(messageId, emote.getName() + ":" + emote.getId(), user);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Void> removeReactionById(long messageId, @Nonnull Emote emote, @Nonnull User user) {
/*  887 */     return removeReactionById(Long.toUnsignedString(messageId), emote, user);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Message> crosspostMessageById(@Nonnull String messageId) {
/*  935 */     if (!isNews())
/*  936 */       throw new IllegalStateException("You can only crosspost messages in news channels!"); 
/*  937 */     Checks.isSnowflake(messageId);
/*  938 */     if (!getGuild().getSelfMember().hasAccess(this))
/*  939 */       throw new MissingAccessException(this, Permission.VIEW_CHANNEL); 
/*  940 */     Route.CompiledRoute route = Route.Messages.CROSSPOST_MESSAGE.compile(new String[] { getId(), messageId });
/*  941 */     return (RestAction<Message>)new RestActionImpl(getJDA(), route, (response, request) -> request.getJDA().getEntityBuilder().createMessage(response.getObject()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   default RestAction<Message> crosspostMessageById(long messageId) {
/*  988 */     return crosspostMessageById(Long.toUnsignedString(messageId));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   default void formatTo(Formatter formatter, int flags, int width, int precision) {
/*      */     String out;
/* 1017 */     boolean leftJustified = ((flags & 0x1) == 1);
/* 1018 */     boolean upper = ((flags & 0x2) == 2);
/* 1019 */     boolean alt = ((flags & 0x4) == 4);
/*      */ 
/*      */     
/* 1022 */     if (alt) {
/* 1023 */       out = "#" + (upper ? getName().toUpperCase(formatter.locale()) : getName());
/*      */     } else {
/* 1025 */       out = getAsMention();
/*      */     } 
/* 1027 */     MiscUtil.appendTo(formatter, width, precision, leftJustified, out);
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   String getTopic();
/*      */   
/*      */   boolean isNSFW();
/*      */   
/*      */   boolean isNews();
/*      */   
/*      */   int getSlowmode();
/*      */   
/*      */   @Nonnull
/*      */   ChannelAction<TextChannel> createCopy(@Nonnull Guild paramGuild);
/*      */   
/*      */   @Nonnull
/*      */   ChannelAction<TextChannel> createCopy();
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<List<Webhook>> retrieveWebhooks();
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   WebhookAction createWebhook(@Nonnull String paramString);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Webhook.WebhookReference> follow(@Nonnull String paramString);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> deleteMessages(@Nonnull Collection<Message> paramCollection);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> deleteMessagesByIds(@Nonnull Collection<String> paramCollection);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   AuditableRestAction<Void> deleteWebhookById(@Nonnull String paramString);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> clearReactionsById(@Nonnull String paramString);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> clearReactionsById(@Nonnull String paramString1, @Nonnull String paramString2);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> clearReactionsById(@Nonnull String paramString, @Nonnull Emote paramEmote);
/*      */   
/*      */   @Nonnull
/*      */   @CheckReturnValue
/*      */   RestAction<Void> removeReactionById(@Nonnull String paramString1, @Nonnull String paramString2, @Nonnull User paramUser);
/*      */   
/*      */   boolean canTalk();
/*      */   
/*      */   boolean canTalk(@Nonnull Member paramMember);
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\TextChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */