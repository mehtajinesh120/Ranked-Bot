/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.MessageChannel;
/*    */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
/*    */ import net.dv8tion.jda.api.events.message.guild.GuildMessageDeleteEvent;
/*    */ import net.dv8tion.jda.api.events.message.priv.PrivateMessageDeleteEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.PrivateChannelImpl;
/*    */ import net.dv8tion.jda.internal.entities.TextChannelImpl;
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
/*    */ public class MessageDeleteHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public MessageDeleteHandler(JDAImpl api) {
/* 33 */     super(api);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/*    */     PrivateChannel privateChannel;
/* 39 */     long messageId = content.getLong("id");
/* 40 */     long channelId = content.getLong("channel_id");
/*    */     
/* 42 */     TextChannel textChannel = getJDA().getTextChannelById(channelId);
/* 43 */     if (textChannel == null)
/*    */     {
/* 45 */       privateChannel = getJDA().getPrivateChannelById(channelId);
/*    */     }
/* 47 */     if (privateChannel == null) {
/*    */       
/* 49 */       getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle);
/* 50 */       EventCache.LOG.debug("Got message delete for a channel/group that is not yet cached. ChannelId: {}", Long.valueOf(channelId));
/* 51 */       return null;
/*    */     } 
/*    */     
/* 54 */     if (privateChannel instanceof TextChannel) {
/*    */       
/* 56 */       TextChannelImpl tChan = (TextChannelImpl)privateChannel;
/* 57 */       if (getJDA().getGuildSetupController().isLocked(tChan.getGuild().getIdLong()))
/* 58 */         return Long.valueOf(tChan.getGuild().getIdLong()); 
/* 59 */       if (tChan.hasLatestMessage() && messageId == privateChannel.getLatestMessageIdLong())
/* 60 */         tChan.setLastMessageId(0L); 
/* 61 */       getJDA().handleEvent((GenericEvent)new GuildMessageDeleteEvent((JDA)
/*    */             
/* 63 */             getJDA(), this.responseNumber, messageId, (TextChannel)tChan));
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 68 */       PrivateChannelImpl pChan = (PrivateChannelImpl)privateChannel;
/* 69 */       if (privateChannel.hasLatestMessage() && messageId == privateChannel.getLatestMessageIdLong())
/* 70 */         pChan.setLastMessageId(0L); 
/* 71 */       getJDA().handleEvent((GenericEvent)new PrivateMessageDeleteEvent((JDA)
/*    */             
/* 73 */             getJDA(), this.responseNumber, messageId, (PrivateChannel)pChan));
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 78 */     getJDA().handleEvent((GenericEvent)new MessageDeleteEvent((JDA)
/*    */           
/* 80 */           getJDA(), this.responseNumber, messageId, (MessageChannel)privateChannel));
/*    */     
/* 82 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\MessageDeleteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */