/*     */ package net.dv8tion.jda.internal.entities;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.AccountType;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Emote;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.entities.Webhook;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.ChannelAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.MessageAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.WebhookAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.pagination.ReactionPaginationAction;
/*     */ import net.dv8tion.jda.api.utils.AttachmentOption;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.TimeUtil;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.restaction.WebhookActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.restaction.pagination.ReactionPaginationActionImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.EncodingUtil;
/*     */ 
/*     */ public class TextChannelImpl extends AbstractChannelImpl<TextChannel, TextChannelImpl> implements TextChannel {
/*     */   private String topic;
/*     */   private long lastMessageId;
/*     */   
/*     */   public TextChannelImpl(long id, GuildImpl guild) {
/*  61 */     super(id, guild);
/*     */   }
/*     */   
/*     */   private boolean nsfw;
/*     */   
/*     */   public TextChannelImpl setPosition(int rawPosition) {
/*  67 */     getGuild().getTextChannelsView().clearCachedLists();
/*  68 */     return super.setPosition(rawPosition);
/*     */   }
/*     */   private boolean news;
/*     */   private int slowmode;
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<List<Webhook>> retrieveWebhooks() {
/*  75 */     checkPermission(Permission.MANAGE_WEBHOOKS);
/*     */     
/*  77 */     Route.CompiledRoute route = Route.Channels.GET_WEBHOOKS.compile(new String[] { getId() });
/*  78 */     JDAImpl jda = (JDAImpl)getJDA();
/*  79 */     return (RestAction<List<Webhook>>)new RestActionImpl((JDA)jda, route, (response, request) -> {
/*     */           DataArray array = response.getArray();
/*     */ 
/*     */           
/*     */           List<Webhook> webhooks = new ArrayList<>(array.length());
/*     */ 
/*     */           
/*     */           EntityBuilder builder = jda.getEntityBuilder();
/*     */           
/*     */           for (int i = 0; i < array.length(); i++) {
/*     */             try {
/*     */               webhooks.add(builder.createWebhook(array.getObject(i)));
/*  91 */             } catch (UncheckedIOException|NullPointerException e) {
/*     */               JDAImpl.LOG.error("Error while creating websocket from json", e);
/*     */             } 
/*     */           } 
/*     */           return Collections.unmodifiableList(webhooks);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookAction createWebhook(@Nonnull String name) {
/* 105 */     Checks.notBlank(name, "Webhook name");
/* 106 */     name = name.trim();
/* 107 */     Checks.notEmpty(name, "Name");
/* 108 */     Checks.notLonger(name, 100, "Name");
/* 109 */     checkPermission(Permission.MANAGE_WEBHOOKS);
/*     */     
/* 111 */     return (WebhookAction)new WebhookActionImpl(getJDA(), this, name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Webhook.WebhookReference> follow(@Nonnull String targetChannelId) {
/* 118 */     Checks.notNull(targetChannelId, "Target Channel ID");
/* 119 */     if (!isNews())
/* 120 */       throw new IllegalStateException("Can only follow news channels!"); 
/* 121 */     Route.CompiledRoute route = Route.Channels.FOLLOW_CHANNEL.compile(new String[] { getId() });
/* 122 */     DataObject body = DataObject.empty().put("webhook_channel_id", targetChannelId);
/* 123 */     return (RestAction<Webhook.WebhookReference>)new RestActionImpl(getJDA(), route, body, (response, request) -> {
/*     */           DataObject json = response.getObject();
/*     */           return new Webhook.WebhookReference((JDA)request.getJDA(), json.getUnsignedLong("webhook_id"), json.getUnsignedLong("channel_id"));
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> deleteMessages(@Nonnull Collection<Message> messages) {
/* 133 */     Checks.notEmpty(messages, "Messages collection");
/*     */     
/* 135 */     return deleteMessagesByIds((Collection<String>)messages.stream()
/* 136 */         .map(ISnowflake::getId)
/* 137 */         .collect(Collectors.toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> deleteMessagesByIds(@Nonnull Collection<String> messageIds) {
/* 144 */     checkPermission(Permission.MESSAGE_MANAGE, "Must have MESSAGE_MANAGE in order to bulk delete messages in this channel regardless of author.");
/* 145 */     if (messageIds.size() < 2 || messageIds.size() > 100) {
/* 146 */       throw new IllegalArgumentException("Must provide at least 2 or at most 100 messages to be deleted.");
/*     */     }
/* 148 */     long twoWeeksAgo = TimeUtil.getDiscordTimestamp(System.currentTimeMillis() - 1209600000L);
/* 149 */     for (String id : messageIds) {
/* 150 */       Checks.check((MiscUtil.parseSnowflake(id) > twoWeeksAgo), "Message Id provided was older than 2 weeks. Id: " + id);
/*     */     }
/* 152 */     return (RestAction<Void>)deleteMessages0(messageIds);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<Void> deleteWebhookById(@Nonnull String id) {
/* 159 */     Checks.isSnowflake(id, "Webhook ID");
/*     */     
/* 161 */     checkPermission(Permission.MANAGE_WEBHOOKS);
/*     */     
/* 163 */     Route.CompiledRoute route = Route.Webhooks.DELETE_WEBHOOK.compile(new String[] { id });
/* 164 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl(getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canTalk() {
/* 170 */     return canTalk(getGuild().getSelfMember());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canTalk(@Nonnull Member member) {
/* 176 */     if (!getGuild().equals(member.getGuild())) {
/* 177 */       throw new IllegalArgumentException("Provided Member is not from the Guild that this TextChannel is part of.");
/*     */     }
/* 179 */     return member.hasPermission(this, new Permission[] { Permission.MESSAGE_READ, Permission.MESSAGE_WRITE });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<CompletableFuture<Void>> purgeMessages(@Nonnull List<? extends Message> messages) {
/* 186 */     if (messages == null || messages.isEmpty())
/* 187 */       return Collections.emptyList(); 
/* 188 */     boolean hasPerms = getGuild().getSelfMember().hasPermission(this, new Permission[] { Permission.MESSAGE_MANAGE });
/* 189 */     if (!hasPerms)
/*     */     {
/* 191 */       for (Message m : messages) {
/*     */         
/* 193 */         if (m.getAuthor().equals(getJDA().getSelfUser()))
/*     */           continue; 
/* 195 */         throw new InsufficientPermissionException(this, Permission.MESSAGE_MANAGE, "Cannot delete messages of other users");
/*     */       } 
/*     */     }
/* 198 */     return super.purgeMessages(messages);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<CompletableFuture<Void>> purgeMessagesById(@Nonnull long... messageIds) {
/* 206 */     if (messageIds == null || messageIds.length == 0)
/* 207 */       return Collections.emptyList(); 
/* 208 */     if (getJDA().getAccountType() != AccountType.BOT || 
/* 209 */       !getGuild().getSelfMember().hasPermission(this, new Permission[] { Permission.MESSAGE_MANAGE })) {
/* 210 */       return super.purgeMessagesById(messageIds);
/*     */     }
/*     */     
/* 213 */     List<CompletableFuture<Void>> list = new LinkedList<>();
/* 214 */     TreeSet<Long> bulk = new TreeSet<>(Comparator.reverseOrder());
/* 215 */     TreeSet<Long> norm = new TreeSet<>(Comparator.reverseOrder());
/* 216 */     long twoWeeksAgo = TimeUtil.getDiscordTimestamp(System.currentTimeMillis() - 1209600000L + 10000L);
/* 217 */     for (long messageId : messageIds) {
/*     */       
/* 219 */       if (messageId > twoWeeksAgo) {
/* 220 */         bulk.add(Long.valueOf(messageId));
/*     */       } else {
/* 222 */         norm.add(Long.valueOf(messageId));
/*     */       } 
/*     */     } 
/*     */     
/* 226 */     if (!bulk.isEmpty()) {
/*     */       
/* 228 */       List<String> toDelete = new ArrayList<>(100);
/* 229 */       while (!bulk.isEmpty()) {
/*     */         
/* 231 */         toDelete.clear();
/* 232 */         for (int i = 0; i < 100 && !bulk.isEmpty(); i++)
/* 233 */           toDelete.add(Long.toUnsignedString(((Long)bulk.pollLast()).longValue())); 
/* 234 */         if (toDelete.size() == 1) {
/* 235 */           list.add(deleteMessageById(toDelete.get(0)).submit()); continue;
/* 236 */         }  if (!toDelete.isEmpty()) {
/* 237 */           list.add(deleteMessages0(toDelete).submit());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 242 */     if (!norm.isEmpty())
/*     */     {
/* 244 */       for (Iterator<Long> iterator = norm.iterator(); iterator.hasNext(); ) { long message = ((Long)iterator.next()).longValue();
/* 245 */         list.add(deleteMessageById(message).submit()); }
/*     */        } 
/* 247 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLatestMessageIdLong() {
/* 253 */     long messageId = this.lastMessageId;
/* 254 */     if (messageId == 0L)
/* 255 */       throw new IllegalStateException("No last message id found."); 
/* 256 */     return messageId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLatestMessage() {
/* 262 */     return (this.lastMessageId != 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelType getType() {
/* 269 */     return ChannelType.TEXT;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTopic() {
/* 275 */     return this.topic;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNSFW() {
/* 281 */     return this.nsfw;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNews() {
/* 287 */     return (this.news && getGuild().getFeatures().contains("NEWS"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSlowmode() {
/* 293 */     return this.slowmode;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Member> getMembers() {
/* 300 */     return Collections.unmodifiableList((List<? extends Member>)getGuild().getMembersView().stream()
/* 301 */         .filter(m -> m.hasPermission(this, new Permission[] { Permission.MESSAGE_READ
/* 302 */             })).collect(Collectors.toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPosition() {
/* 310 */     List<GuildChannel> channels = new ArrayList<>(getGuild().getTextChannels());
/* 311 */     channels.addAll(getGuild().getStoreChannels());
/* 312 */     Collections.sort(channels);
/* 313 */     for (int i = 0; i < channels.size(); i++) {
/*     */       
/* 315 */       if (equals(channels.get(i)))
/* 316 */         return i; 
/*     */     } 
/* 318 */     throw new IllegalStateException("Somehow when determining position we never found the TextChannel in the Guild's channels? wtf?");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ChannelAction<TextChannel> createCopy(@Nonnull Guild guild) {
/* 325 */     Checks.notNull(guild, "Guild");
/* 326 */     ChannelAction<TextChannel> action = guild.createTextChannel(this.name).setNSFW(this.nsfw).setTopic(this.topic).setSlowmode(this.slowmode);
/* 327 */     if (guild.equals(getGuild())) {
/*     */       
/* 329 */       Category parent = getParent();
/* 330 */       if (parent != null)
/* 331 */         action.setParent(parent); 
/* 332 */       for (PermissionOverride o : this.overrides.valueCollection()) {
/*     */         
/* 334 */         if (o.isMemberOverride()) {
/* 335 */           action.addMemberPermissionOverride(o.getIdLong(), o.getAllowedRaw(), o.getDeniedRaw()); continue;
/*     */         } 
/* 337 */         action.addRolePermissionOverride(o.getIdLong(), o.getAllowedRaw(), o.getDeniedRaw());
/*     */       } 
/*     */     } 
/* 340 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendMessage(@Nonnull CharSequence text) {
/* 347 */     checkPermission(Permission.MESSAGE_READ);
/* 348 */     checkPermission(Permission.MESSAGE_WRITE);
/* 349 */     return super.sendMessage(text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendMessage(@Nonnull MessageEmbed embed) {
/* 356 */     checkPermission(Permission.MESSAGE_READ);
/* 357 */     checkPermission(Permission.MESSAGE_WRITE);
/*     */     
/* 359 */     checkPermission(Permission.MESSAGE_EMBED_LINKS);
/* 360 */     return super.sendMessage(embed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendMessage(@Nonnull Message msg) {
/* 367 */     Checks.notNull(msg, "Message");
/*     */     
/* 369 */     checkPermission(Permission.MESSAGE_READ);
/* 370 */     checkPermission(Permission.MESSAGE_WRITE);
/* 371 */     if (msg.getContentRaw().isEmpty() && !msg.getEmbeds().isEmpty()) {
/* 372 */       checkPermission(Permission.MESSAGE_EMBED_LINKS);
/*     */     }
/*     */     
/* 375 */     return super.sendMessage(msg);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendFile(@Nonnull File file, @Nonnull String fileName, @Nonnull AttachmentOption... options) {
/* 382 */     checkPermission(Permission.MESSAGE_READ);
/* 383 */     checkPermission(Permission.MESSAGE_WRITE);
/* 384 */     checkPermission(Permission.MESSAGE_ATTACH_FILES);
/*     */     
/* 386 */     long maxSize = getGuild().getMaxFileSize();
/* 387 */     Checks.check((file == null || file.length() <= maxSize), "File may not exceed the maximum file length of %d bytes!", 
/* 388 */         Long.valueOf(maxSize));
/*     */ 
/*     */     
/* 391 */     return super.sendFile(file, fileName, options);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendFile(@Nonnull InputStream data, @Nonnull String fileName, @Nonnull AttachmentOption... options) {
/* 398 */     checkPermission(Permission.MESSAGE_READ);
/* 399 */     checkPermission(Permission.MESSAGE_WRITE);
/* 400 */     checkPermission(Permission.MESSAGE_ATTACH_FILES);
/*     */ 
/*     */     
/* 403 */     return super.sendFile(data, fileName, options);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction sendFile(@Nonnull byte[] data, @Nonnull String fileName, @Nonnull AttachmentOption... options) {
/* 410 */     checkPermission(Permission.MESSAGE_READ);
/* 411 */     checkPermission(Permission.MESSAGE_WRITE);
/* 412 */     checkPermission(Permission.MESSAGE_ATTACH_FILES);
/*     */     
/* 414 */     long maxSize = getGuild().getMaxFileSize();
/* 415 */     Checks.check((data == null || data.length <= maxSize), "File is too big! Max file-size is %d bytes", Long.valueOf(maxSize));
/*     */ 
/*     */     
/* 418 */     return super.sendFile(data, fileName, options);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Message> retrieveMessageById(@Nonnull String messageId) {
/* 425 */     checkPermission(Permission.MESSAGE_READ);
/* 426 */     checkPermission(Permission.MESSAGE_HISTORY);
/*     */ 
/*     */     
/* 429 */     return super.retrieveMessageById(messageId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<Void> deleteMessageById(@Nonnull String messageId) {
/* 436 */     Checks.isSnowflake(messageId, "Message ID");
/* 437 */     checkPermission(Permission.MESSAGE_READ);
/*     */ 
/*     */     
/* 440 */     return super.deleteMessageById(messageId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> pinMessageById(@Nonnull String messageId) {
/* 447 */     checkPermission(Permission.MESSAGE_READ, "You cannot pin a message in a channel you can't access. (MESSAGE_READ)");
/* 448 */     checkPermission(Permission.MESSAGE_MANAGE, "You need MESSAGE_MANAGE to pin or unpin messages.");
/*     */ 
/*     */     
/* 451 */     return super.pinMessageById(messageId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> unpinMessageById(@Nonnull String messageId) {
/* 458 */     checkPermission(Permission.MESSAGE_READ, "You cannot unpin a message in a channel you can't access. (MESSAGE_READ)");
/* 459 */     checkPermission(Permission.MESSAGE_MANAGE, "You need MESSAGE_MANAGE to pin or unpin messages.");
/*     */ 
/*     */     
/* 462 */     return super.unpinMessageById(messageId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<List<Message>> retrievePinnedMessages() {
/* 469 */     checkPermission(Permission.MESSAGE_READ, "Cannot get the pinned message of a channel without MESSAGE_READ access.");
/*     */ 
/*     */     
/* 472 */     return super.retrievePinnedMessages();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> addReactionById(@Nonnull String messageId, @Nonnull String unicode) {
/* 479 */     checkPermission(Permission.MESSAGE_HISTORY);
/*     */ 
/*     */     
/* 482 */     return super.addReactionById(messageId, unicode);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> addReactionById(@Nonnull String messageId, @Nonnull Emote emote) {
/* 489 */     checkPermission(Permission.MESSAGE_HISTORY);
/*     */ 
/*     */     
/* 492 */     return super.addReactionById(messageId, emote);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> clearReactionsById(@Nonnull String messageId) {
/* 499 */     Checks.isSnowflake(messageId, "Message ID");
/*     */     
/* 501 */     checkPermission(Permission.MESSAGE_MANAGE);
/* 502 */     Route.CompiledRoute route = Route.Messages.REMOVE_ALL_REACTIONS.compile(new String[] { getId(), messageId });
/* 503 */     return (RestAction<Void>)new RestActionImpl(getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> clearReactionsById(@Nonnull String messageId, @Nonnull String unicode) {
/* 510 */     Checks.notNull(messageId, "Message ID");
/* 511 */     Checks.notNull(unicode, "Emote Name");
/* 512 */     checkPermission(Permission.MESSAGE_MANAGE);
/*     */     
/* 514 */     String code = EncodingUtil.encodeReaction(unicode);
/* 515 */     Route.CompiledRoute route = Route.Messages.CLEAR_EMOTE_REACTIONS.compile(new String[] { getId(), messageId, code });
/* 516 */     return (RestAction<Void>)new RestActionImpl(getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> clearReactionsById(@Nonnull String messageId, @Nonnull Emote emote) {
/* 523 */     Checks.notNull(emote, "Emote");
/* 524 */     return clearReactionsById(messageId, emote.getName() + ":" + emote.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestActionImpl<Void> removeReactionById(@Nonnull String messageId, @Nonnull String unicode, @Nonnull User user) {
/*     */     String targetUser;
/* 531 */     Checks.isSnowflake(messageId, "Message ID");
/* 532 */     Checks.notNull(unicode, "Provided Unicode");
/* 533 */     unicode = unicode.trim();
/* 534 */     Checks.notEmpty(unicode, "Provided Unicode");
/* 535 */     Checks.notNull(user, "User");
/*     */     
/* 537 */     if (!getJDA().getSelfUser().equals(user)) {
/* 538 */       checkPermission(Permission.MESSAGE_MANAGE);
/*     */     }
/* 540 */     String encoded = EncodingUtil.encodeReaction(unicode);
/*     */ 
/*     */     
/* 543 */     if (user.equals(getJDA().getSelfUser())) {
/* 544 */       targetUser = "@me";
/*     */     } else {
/* 546 */       targetUser = user.getId();
/*     */     } 
/* 548 */     Route.CompiledRoute route = Route.Messages.REMOVE_REACTION.compile(new String[] { getId(), messageId, encoded, targetUser });
/* 549 */     return new RestActionImpl(getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReactionPaginationAction retrieveReactionUsersById(@Nonnull String messageId, @Nonnull String unicode) {
/* 556 */     Checks.isSnowflake(messageId, "Message ID");
/* 557 */     Checks.notEmpty(unicode, "Emoji");
/* 558 */     Checks.noWhitespace(unicode, "Emoji");
/*     */     
/* 560 */     checkPermission(Permission.MESSAGE_HISTORY);
/*     */     
/* 562 */     return (ReactionPaginationAction)new ReactionPaginationActionImpl((MessageChannel)this, messageId, EncodingUtil.encodeUTF8(unicode));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ReactionPaginationAction retrieveReactionUsersById(@Nonnull String messageId, @Nonnull Emote emote) {
/* 569 */     Checks.isSnowflake(messageId, "Message ID");
/* 570 */     Checks.notNull(emote, "Emote");
/*     */     
/* 572 */     checkPermission(Permission.MESSAGE_HISTORY);
/*     */     
/* 574 */     return (ReactionPaginationAction)new ReactionPaginationActionImpl((MessageChannel)this, messageId, String.format("%s:%s", new Object[] { emote, emote.getId() }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessageById(@Nonnull String messageId, @Nonnull CharSequence newContent) {
/* 581 */     checkPermission(Permission.MESSAGE_READ);
/* 582 */     checkPermission(Permission.MESSAGE_WRITE);
/* 583 */     return super.editMessageById(messageId, newContent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @Deprecated
/*     */   public MessageAction editMessageById(@Nonnull String messageId, @Nonnull MessageEmbed newEmbed) {
/* 591 */     checkPermission(Permission.MESSAGE_READ);
/* 592 */     checkPermission(Permission.MESSAGE_WRITE);
/* 593 */     checkPermission(Permission.MESSAGE_EMBED_LINKS);
/* 594 */     return super.editMessageById(messageId, newEmbed);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessageEmbedsById(@Nonnull String messageId, @Nonnull Collection<? extends MessageEmbed> newEmbeds) {
/* 601 */     checkPermission(Permission.MESSAGE_READ);
/* 602 */     checkPermission(Permission.MESSAGE_WRITE);
/* 603 */     checkPermission(Permission.MESSAGE_EMBED_LINKS);
/* 604 */     return super.editMessageEmbedsById(messageId, newEmbeds);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MessageAction editMessageById(@Nonnull String id, @Nonnull Message newContent) {
/* 611 */     Checks.notNull(newContent, "Message");
/*     */ 
/*     */     
/* 614 */     checkPermission(Permission.MESSAGE_READ);
/* 615 */     checkPermission(Permission.MESSAGE_WRITE);
/* 616 */     if (newContent.getContentRaw().isEmpty() && !newContent.getEmbeds().isEmpty()) {
/* 617 */       checkPermission(Permission.MESSAGE_EMBED_LINKS);
/*     */     }
/*     */     
/* 620 */     return super.editMessageById(id, newContent);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 626 */     return "TC:" + getName() + '(' + this.id + ')';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextChannelImpl setTopic(String topic) {
/* 633 */     this.topic = topic;
/* 634 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextChannelImpl setLastMessageId(long id) {
/* 639 */     this.lastMessageId = id;
/* 640 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextChannelImpl setNSFW(boolean nsfw) {
/* 645 */     this.nsfw = nsfw;
/* 646 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextChannelImpl setSlowmode(int slowmode) {
/* 651 */     this.slowmode = slowmode;
/* 652 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public TextChannelImpl setNews(boolean news) {
/* 657 */     this.news = news;
/* 658 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private RestActionImpl<Void> deleteMessages0(Collection<String> messageIds) {
/* 664 */     DataObject body = DataObject.empty().put("messages", messageIds);
/* 665 */     Route.CompiledRoute route = Route.Messages.DELETE_MESSAGES.compile(new String[] { getId() });
/* 666 */     return new RestActionImpl(getJDA(), route, body);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\TextChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */