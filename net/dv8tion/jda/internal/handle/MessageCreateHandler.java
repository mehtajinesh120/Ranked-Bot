/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.entities.MessageType;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
/*     */ import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
/*     */ import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.PrivateChannelImpl;
/*     */ import net.dv8tion.jda.internal.entities.TextChannelImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageCreateHandler
/*     */   extends SocketHandler
/*     */ {
/*     */   public MessageCreateHandler(JDAImpl api) {
/*  34 */     super(api);
/*     */   }
/*     */   protected Long handleInternally(DataObject content) {
/*     */     Message message;
/*     */     TextChannelImpl textChannelImpl;
/*     */     PrivateChannelImpl channel;
/*  40 */     MessageType type = MessageType.fromId(content.getInt("type"));
/*     */     
/*  42 */     if (type == MessageType.UNKNOWN) {
/*     */       
/*  44 */       WebSocketClient.LOG.debug("JDA received a message of unknown type. Type: {}  JSON: {}", type, content);
/*  45 */       return null;
/*     */     } 
/*     */     
/*  48 */     JDAImpl jda = getJDA();
/*  49 */     if (!content.isNull("guild_id")) {
/*     */       
/*  51 */       long guildId = content.getLong("guild_id");
/*  52 */       if (jda.getGuildSetupController().isLocked(guildId)) {
/*  53 */         return Long.valueOf(guildId);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*     */     try {
/*  59 */       message = jda.getEntityBuilder().createMessage(content, true);
/*     */     }
/*  61 */     catch (IllegalArgumentException e) {
/*     */       long channelId; long authorId;
/*  63 */       switch (e.getMessage()) {
/*     */ 
/*     */         
/*     */         case "MISSING_CHANNEL":
/*  67 */           channelId = content.getLong("channel_id");
/*  68 */           jda.getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle);
/*  69 */           EventCache.LOG.debug("Received a message for a channel that JDA does not currently have cached");
/*  70 */           return null;
/*     */ 
/*     */         
/*     */         case "MISSING_USER":
/*  74 */           authorId = content.getObject("author").getLong("id");
/*  75 */           jda.getEventCache().cache(EventCache.Type.USER, authorId, this.responseNumber, this.allContent, this::handle);
/*  76 */           EventCache.LOG.debug("Received a message for a user that JDA does not currently have cached");
/*  77 */           return null;
/*     */ 
/*     */         
/*     */         case "UNKNOWN_MESSAGE_TYPE":
/*  81 */           WebSocketClient.LOG.debug("Ignoring message with unknown type: {}", content);
/*  82 */           return null;
/*     */       } 
/*     */       
/*  85 */       throw e;
/*     */     } 
/*     */ 
/*     */     
/*  89 */     switch (message.getChannelType()) {
/*     */ 
/*     */       
/*     */       case TEXT:
/*  93 */         textChannelImpl = (TextChannelImpl)message.getTextChannel();
/*  94 */         if (jda.getGuildSetupController().isLocked(textChannelImpl.getGuild().getIdLong()))
/*  95 */           return Long.valueOf(textChannelImpl.getGuild().getIdLong()); 
/*  96 */         textChannelImpl.setLastMessageId(message.getIdLong());
/*  97 */         jda.handleEvent((GenericEvent)new GuildMessageReceivedEvent((JDA)jda, this.responseNumber, message));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 123 */         jda.handleEvent((GenericEvent)new MessageReceivedEvent((JDA)jda, this.responseNumber, message));
/*     */ 
/*     */ 
/*     */         
/* 127 */         return null;case PRIVATE: channel = (PrivateChannelImpl)message.getPrivateChannel(); channel.setLastMessageId(message.getIdLong()); this.api.usedPrivateChannel(channel.getIdLong()); jda.handleEvent((GenericEvent)new PrivateMessageReceivedEvent((JDA)jda, this.responseNumber, message)); jda.handleEvent((GenericEvent)new MessageReceivedEvent((JDA)jda, this.responseNumber, message)); return null;
/*     */       case GROUP:
/*     */         WebSocketClient.LOG.error("Received a MESSAGE_CREATE for a group channel which should not be possible");
/*     */         return null;
/*     */     } 
/*     */     WebSocketClient.LOG.warn("Received a MESSAGE_CREATE with a unknown MessageChannel ChannelType. JSON: {}", content);
/*     */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\MessageCreateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */