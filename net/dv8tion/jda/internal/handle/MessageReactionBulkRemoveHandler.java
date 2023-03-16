/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.ChannelType;
/*    */ import net.dv8tion.jda.api.entities.MessageChannel;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveAllEvent;
/*    */ import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveAllEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.requests.WebSocketClient;
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
/*    */ public class MessageReactionBulkRemoveHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public MessageReactionBulkRemoveHandler(JDAImpl api) {
/* 30 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 36 */     long messageId = content.getLong("message_id");
/* 37 */     long channelId = content.getLong("channel_id");
/* 38 */     JDAImpl jda = getJDA();
/* 39 */     TextChannel channel = jda.getTextChannelById(channelId);
/* 40 */     if (channel == null) {
/*    */       
/* 42 */       jda.getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle);
/* 43 */       EventCache.LOG.debug("Received a reaction for a channel that JDA does not currently have cached channel_id: {} message_id: {}", Long.valueOf(channelId), Long.valueOf(messageId));
/* 44 */       return null;
/*    */     } 
/*    */     
/* 47 */     switch (channel.getType()) {
/*    */       
/*    */       case TEXT:
/* 50 */         jda.handleEvent((GenericEvent)new GuildMessageReactionRemoveAllEvent((JDA)jda, this.responseNumber, messageId, channel));
/*    */         break;
/*    */ 
/*    */ 
/*    */       
/*    */       case GROUP:
/* 56 */         WebSocketClient.LOG.error("Received a reaction bulk delete for a group which should not be possible");
/* 57 */         return null;
/*    */     } 
/*    */     
/* 60 */     jda.handleEvent((GenericEvent)new MessageReactionRemoveAllEvent((JDA)jda, this.responseNumber, messageId, (MessageChannel)channel));
/*    */ 
/*    */ 
/*    */     
/* 64 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\MessageReactionBulkRemoveHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */