/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import gnu.trove.map.hash.TLongObjectHashMap;
/*    */ import net.dv8tion.jda.api.entities.ChannelType;
/*    */ import net.dv8tion.jda.api.utils.data.DataArray;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.EntityBuilder;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReadyHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public ReadyHandler(JDAImpl api) {
/* 33 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 39 */     EntityBuilder builder = getJDA().getEntityBuilder();
/*    */     
/* 41 */     DataArray guilds = content.getArray("guilds");
/*    */     
/* 43 */     TLongObjectHashMap tLongObjectHashMap = new TLongObjectHashMap();
/* 44 */     for (int i = 0; i < guilds.length(); i++) {
/*    */       
/* 46 */       DataObject guild = guilds.getObject(i);
/* 47 */       long id = guild.getUnsignedLong("id");
/* 48 */       DataObject previous = (DataObject)tLongObjectHashMap.put(id, guild);
/* 49 */       if (previous != null) {
/* 50 */         WebSocketClient.LOG.warn("Found duplicate guild for id {} in ready payload", Long.valueOf(id));
/*    */       }
/*    */     } 
/* 53 */     DataObject selfJson = content.getObject("user");
/* 54 */     selfJson.put("application_id", content
/* 55 */         .optObject("application")
/* 56 */         .map(obj -> Long.valueOf(obj.getUnsignedLong("id")))
/* 57 */         .orElse(Long.valueOf(selfJson.getUnsignedLong("id"))));
/*    */ 
/*    */     
/* 60 */     builder.createSelfUser(selfJson);
/* 61 */     if (getJDA().getGuildSetupController().setIncompleteCount(tLongObjectHashMap.size()))
/*    */     {
/* 63 */       tLongObjectHashMap.forEachEntry((id, guild) -> {
/*    */             getJDA().getGuildSetupController().onReady(id, guild);
/*    */             
/*    */             return true;
/*    */           });
/*    */     }
/*    */     
/* 70 */     handleReady(content);
/* 71 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleReady(DataObject content) {
/* 76 */     EntityBuilder builder = getJDA().getEntityBuilder();
/* 77 */     DataArray privateChannels = content.getArray("private_channels");
/*    */     
/* 79 */     for (int i = 0; i < privateChannels.length(); i++) {
/*    */       
/* 81 */       DataObject chan = privateChannels.getObject(i);
/* 82 */       ChannelType type = ChannelType.fromId(chan.getInt("type"));
/*    */ 
/*    */       
/* 85 */       switch (type) {
/*    */         
/*    */         case PRIVATE:
/* 88 */           builder.createPrivateChannel(chan);
/*    */           break;
/*    */         default:
/* 91 */           WebSocketClient.LOG.warn("Received a Channel in the private_channels array in READY of an unknown type! Type: {}", type);
/*    */           break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\ReadyHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */