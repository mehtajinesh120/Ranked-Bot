/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.api.entities.MessageType;
/*     */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
/*     */ import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
/*     */ import net.dv8tion.jda.api.events.message.guild.GuildMessageEmbedEvent;
/*     */ import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
/*     */ import net.dv8tion.jda.api.events.message.priv.PrivateMessageEmbedEvent;
/*     */ import net.dv8tion.jda.api.events.message.priv.PrivateMessageUpdateEvent;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*     */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageUpdateHandler
/*     */   extends SocketHandler
/*     */ {
/*     */   public MessageUpdateHandler(JDAImpl api) {
/*  38 */     super(api);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Long handleInternally(DataObject content) {
/*  44 */     if (!content.isNull("guild_id")) {
/*     */       
/*  46 */       long guildId = content.getLong("guild_id");
/*  47 */       if (getJDA().getGuildSetupController().isLocked(guildId)) {
/*  48 */         return Long.valueOf(guildId);
/*     */       }
/*     */     } 
/*     */     
/*  52 */     if (content.hasKey("author")) {
/*     */       
/*  54 */       if (content.hasKey("type")) {
/*     */         
/*  56 */         MessageType type = MessageType.fromId(content.getInt("type"));
/*  57 */         if (!type.isSystem())
/*  58 */           return handleMessage(content); 
/*  59 */         WebSocketClient.LOG.debug("JDA received a message update for an unexpected message type. Type: {} JSON: {}", type, content);
/*  60 */         return null;
/*     */       } 
/*  62 */       if (!content.isNull("embeds"))
/*     */       {
/*     */         
/*  65 */         handleMessageEmbed(content);
/*  66 */         return null;
/*     */       }
/*     */     
/*  69 */     } else if (!content.isNull("embeds")) {
/*  70 */       return handleMessageEmbed(content);
/*  71 */     }  return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private Long handleMessage(DataObject content) {
/*     */     Message message;
/*     */     TextChannel channel;
/*     */     try {
/*  79 */       message = getJDA().getEntityBuilder().createMessage(content);
/*     */     }
/*  81 */     catch (IllegalArgumentException e) {
/*     */       long channelId; long authorId;
/*  83 */       switch (e.getMessage()) {
/*     */ 
/*     */         
/*     */         case "MISSING_CHANNEL":
/*  87 */           channelId = content.getLong("channel_id");
/*  88 */           getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle);
/*  89 */           EventCache.LOG.debug("Received a message update for a channel that JDA does not currently have cached");
/*  90 */           return null;
/*     */ 
/*     */         
/*     */         case "MISSING_USER":
/*  94 */           authorId = content.getObject("author").getLong("id");
/*  95 */           getJDA().getEventCache().cache(EventCache.Type.USER, authorId, this.responseNumber, this.allContent, this::handle);
/*  96 */           EventCache.LOG.debug("Received a message update for a user that JDA does not currently have cached");
/*  97 */           return null;
/*     */       } 
/*     */       
/* 100 */       throw e;
/*     */     } 
/*     */ 
/*     */     
/* 104 */     switch (message.getChannelType()) {
/*     */ 
/*     */       
/*     */       case TEXT:
/* 108 */         channel = message.getTextChannel();
/* 109 */         if (getJDA().getGuildSetupController().isLocked(channel.getGuild().getIdLong()))
/* 110 */           return Long.valueOf(channel.getGuild().getIdLong()); 
/* 111 */         getJDA().handleEvent((GenericEvent)new GuildMessageUpdateEvent((JDA)
/*     */               
/* 113 */               getJDA(), this.responseNumber, message));
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case PRIVATE:
/* 119 */         getJDA().usedPrivateChannel(message.getChannel().getIdLong());
/* 120 */         getJDA().handleEvent((GenericEvent)new PrivateMessageUpdateEvent((JDA)
/*     */               
/* 122 */               getJDA(), this.responseNumber, message));
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case GROUP:
/* 128 */         WebSocketClient.LOG.warn("Received a MESSAGE_UPDATE for a group which is not supported");
/*     */         break;
/*     */ 
/*     */       
/*     */       default:
/* 133 */         WebSocketClient.LOG.warn("Received a MESSAGE_UPDATE with a unknown MessageChannel ChannelType. JSON: {}", content);
/* 134 */         return null;
/*     */     } 
/*     */ 
/*     */     
/* 138 */     getJDA().handleEvent((GenericEvent)new MessageUpdateEvent((JDA)
/*     */           
/* 140 */           getJDA(), this.responseNumber, message));
/*     */     
/* 142 */     return null;
/*     */   }
/*     */   
/*     */   private Long handleMessageEmbed(DataObject content) {
/*     */     TextChannel tChannel;
/* 147 */     EntityBuilder builder = getJDA().getEntityBuilder();
/* 148 */     long messageId = content.getLong("id");
/* 149 */     long channelId = content.getLong("channel_id");
/* 150 */     LinkedList<MessageEmbed> embeds = new LinkedList<>();
/* 151 */     MessageChannel channel = (MessageChannel)getJDA().getTextChannelsView().get(channelId);
/* 152 */     if (channel == null)
/* 153 */       channel = (MessageChannel)getJDA().getPrivateChannelsView().get(channelId); 
/* 154 */     if (channel == null) {
/*     */       
/* 156 */       getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle);
/* 157 */       EventCache.LOG.debug("Received message update for embeds for a channel/group that JDA does not have cached yet.");
/* 158 */       return null;
/*     */     } 
/*     */     
/* 161 */     DataArray embedsJson = content.getArray("embeds");
/* 162 */     for (int i = 0; i < embedsJson.length(); i++) {
/* 163 */       embeds.add(builder.createMessageEmbed(embedsJson.getObject(i)));
/*     */     }
/* 165 */     switch (channel.getType()) {
/*     */       
/*     */       case TEXT:
/* 168 */         tChannel = (TextChannel)channel;
/* 169 */         if (getJDA().getGuildSetupController().isLocked(tChannel.getGuild().getIdLong()))
/* 170 */           return Long.valueOf(tChannel.getGuild().getIdLong()); 
/* 171 */         getJDA().handleEvent((GenericEvent)new GuildMessageEmbedEvent((JDA)
/*     */               
/* 173 */               getJDA(), this.responseNumber, messageId, tChannel, embeds));
/*     */         break;
/*     */       
/*     */       case PRIVATE:
/* 177 */         getJDA().handleEvent((GenericEvent)new PrivateMessageEmbedEvent((JDA)
/*     */               
/* 179 */               getJDA(), this.responseNumber, messageId, (PrivateChannel)channel, embeds));
/*     */         break;
/*     */       
/*     */       case GROUP:
/* 183 */         WebSocketClient.LOG.error("Received a message update for a group which should not be possible");
/* 184 */         return null;
/*     */       default:
/* 186 */         WebSocketClient.LOG.warn("No event handled for message update of type {}", channel.getType());
/*     */         break;
/*     */     } 
/*     */     
/* 190 */     getJDA().handleEvent((GenericEvent)new MessageEmbedEvent((JDA)
/*     */           
/* 192 */           getJDA(), this.responseNumber, messageId, channel, embeds));
/*     */     
/* 194 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\MessageUpdateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */