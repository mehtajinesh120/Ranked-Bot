/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Collectors;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Emote;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageReaction;
/*     */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
/*     */ import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
/*     */ import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionAddEvent;
/*     */ import net.dv8tion.jda.api.events.message.priv.react.PrivateMessageReactionRemoveEvent;
/*     */ import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
/*     */ import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.EmoteImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.entities.MemberImpl;
/*     */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageReactionHandler
/*     */   extends SocketHandler
/*     */ {
/*     */   private final boolean add;
/*     */   
/*     */   public MessageReactionHandler(JDAImpl api, boolean add) {
/*  47 */     super(api);
/*  48 */     this.add = add;
/*     */   }
/*     */   
/*     */   protected Long handleInternally(DataObject content) {
/*     */     PrivateChannel privateChannel;
/*     */     MessageReaction.ReactionEmote rEmote;
/*  54 */     if (!content.isNull("guild_id")) {
/*     */       
/*  56 */       long guildId = content.getLong("guild_id");
/*  57 */       if (getJDA().getGuildSetupController().isLocked(guildId)) {
/*  58 */         return Long.valueOf(guildId);
/*     */       }
/*     */     } 
/*  61 */     DataObject emoji = content.getObject("emoji");
/*     */     
/*  63 */     long userId = content.getLong("user_id");
/*  64 */     long messageId = content.getLong("message_id");
/*  65 */     long channelId = content.getLong("channel_id");
/*     */     
/*  67 */     Long emojiId = emoji.isNull("id") ? null : Long.valueOf(emoji.getLong("id"));
/*  68 */     String emojiName = emoji.getString("name", null);
/*  69 */     boolean emojiAnimated = emoji.getBoolean("animated");
/*     */     
/*  71 */     if (emojiId == null && emojiName == null) {
/*     */       
/*  73 */       WebSocketClient.LOG.debug("Received a reaction {} with no name nor id. json: {}", 
/*  74 */           JDALogger.getLazyString(() -> this.add ? "add" : "remove"), content);
/*  75 */       return null;
/*     */     } 
/*     */     
/*  78 */     Guild guild = getJDA().getGuildById(content.getUnsignedLong("guild_id", 0L));
/*  79 */     MemberImpl member = null;
/*  80 */     if (guild != null) {
/*     */       
/*  82 */       member = (MemberImpl)guild.getMemberById(userId);
/*     */       
/*  84 */       Optional<DataObject> memberJson = content.optObject("member");
/*  85 */       if (memberJson.isPresent()) {
/*     */         
/*  87 */         DataObject json = memberJson.get();
/*  88 */         if (member == null || !member.hasTimeJoined()) {
/*  89 */           member = getJDA().getEntityBuilder().createMember((GuildImpl)guild, json);
/*     */         
/*     */         }
/*     */         else {
/*     */           
/*  94 */           Objects.requireNonNull(guild);
/*     */           
/*  96 */           List<Role> roles = (List<Role>)json.getArray("roles").stream(DataArray::getUnsignedLong).map(guild::getRoleById).filter(Objects::nonNull).collect(Collectors.toList());
/*  97 */           getJDA().getEntityBuilder().updateMember((GuildImpl)guild, member, json, roles);
/*     */         } 
/*     */         
/* 100 */         getJDA().getEntityBuilder().updateMemberCache(member);
/*     */       } 
/* 102 */       if (member == null && this.add && guild.isLoaded()) {
/*     */         
/* 104 */         WebSocketClient.LOG.debug("Dropping reaction event for unknown member {}", content);
/* 105 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/* 109 */     User user = getJDA().getUserById(userId);
/* 110 */     if (user == null && member != null)
/* 111 */       user = member.getUser(); 
/* 112 */     if (user == null)
/*     */     {
/* 114 */       if (this.add && guild != null) {
/*     */         
/* 116 */         getJDA().getEventCache().cache(EventCache.Type.USER, userId, this.responseNumber, this.allContent, this::handle);
/* 117 */         EventCache.LOG.debug("Received a reaction for a user that JDA does not currently have cached. UserID: {} ChannelId: {} MessageId: {}", new Object[] {
/* 118 */               Long.valueOf(userId), Long.valueOf(channelId), Long.valueOf(messageId) });
/* 119 */         return null;
/*     */       } 
/*     */     }
/*     */     
/* 123 */     TextChannel textChannel = getJDA().getTextChannelById(channelId);
/* 124 */     if (textChannel == null)
/* 125 */       privateChannel = getJDA().getPrivateChannelById(channelId); 
/* 126 */     if (privateChannel == null) {
/*     */       
/* 128 */       getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle);
/* 129 */       EventCache.LOG.debug("Received a reaction for a channel that JDA does not currently have cached");
/* 130 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 134 */     if (emojiId != null) {
/*     */       EmoteImpl emoteImpl;
/* 136 */       Emote emote = getJDA().getEmoteById(emojiId.longValue());
/* 137 */       if (emote == null)
/*     */       {
/* 139 */         if (emojiName != null) {
/*     */           
/* 141 */           emoteImpl = (new EmoteImpl(emojiId.longValue(), getJDA())).setAnimated(emojiAnimated).setName(emojiName);
/*     */         }
/*     */         else {
/*     */           
/* 145 */           WebSocketClient.LOG.debug("Received a reaction {} with a null name. json: {}", 
/* 146 */               JDALogger.getLazyString(() -> this.add ? "add" : "remove"), content);
/* 147 */           return null;
/*     */         } 
/*     */       }
/* 150 */       rEmote = MessageReaction.ReactionEmote.fromCustom((Emote)emoteImpl);
/*     */     }
/*     */     else {
/*     */       
/* 154 */       rEmote = MessageReaction.ReactionEmote.fromUnicode(emojiName, (JDA)getJDA());
/*     */     } 
/* 156 */     MessageReaction reaction = new MessageReaction((MessageChannel)privateChannel, rEmote, messageId, (userId == getJDA().getSelfUser().getIdLong()), -1);
/*     */     
/* 158 */     if (this.add) {
/* 159 */       onAdd(reaction, user, (Member)member, userId);
/*     */     } else {
/* 161 */       onRemove(reaction, user, (Member)member, userId);
/* 162 */     }  return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void onAdd(MessageReaction reaction, User user, Member member, long userId) {
/* 167 */     JDAImpl jda = getJDA();
/* 168 */     switch (reaction.getChannelType()) {
/*     */       
/*     */       case TEXT:
/* 171 */         jda.handleEvent((GenericEvent)new GuildMessageReactionAddEvent((JDA)jda, this.responseNumber, 
/*     */ 
/*     */               
/* 174 */               Objects.<Member>requireNonNull(member), reaction));
/*     */         break;
/*     */       case PRIVATE:
/* 177 */         jda.usedPrivateChannel(reaction.getChannel().getIdLong());
/* 178 */         jda.handleEvent((GenericEvent)new PrivateMessageReactionAddEvent((JDA)jda, this.responseNumber, reaction, userId));
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case GROUP:
/* 184 */         WebSocketClient.LOG.debug("Received a reaction add for a group which should not be possible");
/*     */         return;
/*     */     } 
/*     */     
/* 188 */     jda.handleEvent((GenericEvent)new MessageReactionAddEvent((JDA)jda, this.responseNumber, user, member, reaction, userId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void onRemove(MessageReaction reaction, User user, Member member, long userId) {
/* 196 */     JDAImpl jda = getJDA();
/* 197 */     switch (reaction.getChannelType()) {
/*     */       
/*     */       case TEXT:
/* 200 */         jda.handleEvent((GenericEvent)new GuildMessageReactionRemoveEvent((JDA)jda, this.responseNumber, member, reaction, userId));
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case PRIVATE:
/* 206 */         jda.usedPrivateChannel(reaction.getChannel().getIdLong());
/* 207 */         jda.handleEvent((GenericEvent)new PrivateMessageReactionRemoveEvent((JDA)jda, this.responseNumber, reaction, userId));
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case GROUP:
/* 213 */         WebSocketClient.LOG.debug("Received a reaction remove for a group which should not be possible");
/*     */         return;
/*     */     } 
/*     */     
/* 217 */     jda.handleEvent((GenericEvent)new MessageReactionRemoveEvent((JDA)jda, this.responseNumber, user, member, reaction, userId));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\MessageReactionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */