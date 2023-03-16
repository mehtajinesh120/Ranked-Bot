/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataArray;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
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
/*    */ public class MessageBulkDeleteHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public MessageBulkDeleteHandler(JDAImpl api) {
/* 32 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 38 */     if (!content.isNull("guild_id")) {
/*    */       
/* 40 */       long guildId = content.getLong("guild_id");
/* 41 */       if (getJDA().getGuildSetupController().isLocked(guildId))
/* 42 */         return Long.valueOf(guildId); 
/*    */     } 
/* 44 */     long channelId = content.getLong("channel_id");
/*    */     
/* 46 */     if (getJDA().isBulkDeleteSplittingEnabled()) {
/*    */       
/* 48 */       SocketHandler handler = (SocketHandler)getJDA().getClient().getHandlers().get("MESSAGE_DELETE");
/* 49 */       content.getArray("ids").forEach(id -> handler.handle(this.responseNumber, DataObject.empty().put("t", "MESSAGE_DELETE").put("d", DataObject.empty().put("channel_id", Long.toUnsignedString(channelId)).put("id", id))));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     }
/*    */     else {
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 60 */       TextChannel channel = getJDA().getTextChannelById(channelId);
/* 61 */       if (channel == null) {
/*    */         
/* 63 */         getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle);
/* 64 */         EventCache.LOG.debug("Received a Bulk Message Delete for a TextChannel that is not yet cached.");
/* 65 */         return null;
/*    */       } 
/*    */       
/* 68 */       if (getJDA().getGuildSetupController().isLocked(channel.getGuild().getIdLong())) {
/* 69 */         return Long.valueOf(channel.getGuild().getIdLong());
/*    */       }
/* 71 */       DataArray array = content.getArray("ids");
/* 72 */       List<String> messages = (List<String>)array.stream(DataArray::getString).collect(Collectors.toList());
/* 73 */       getJDA().handleEvent((GenericEvent)new MessageBulkDeleteEvent((JDA)
/*    */             
/* 75 */             getJDA(), this.responseNumber, channel, messages));
/*    */     } 
/*    */     
/* 78 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\MessageBulkDeleteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */