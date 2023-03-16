/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import gnu.trove.map.TLongObjectMap;
/*    */ import java.util.function.Supplier;
/*    */ import net.dv8tion.jda.api.utils.data.DataArray;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*    */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*    */ import net.dv8tion.jda.internal.entities.MemberImpl;
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
/*    */ public class GuildMembersChunkHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public GuildMembersChunkHandler(JDAImpl api) {
/* 33 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 39 */     long guildId = content.getLong("guild_id");
/* 40 */     DataArray members = content.getArray("members");
/* 41 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(guildId);
/* 42 */     if (guild != null) {
/*    */       
/* 44 */       if (this.api.getClient().getChunkManager().handleChunk(guildId, content))
/* 45 */         return null; 
/* 46 */       WebSocketClient.LOG.debug("Received member chunk for guild that is already in cache. GuildId: {} Count: {} Index: {}/{}", new Object[] {
/* 47 */             Long.valueOf(guildId), Integer.valueOf(members.length()), Integer.valueOf(content.getInt("chunk_index")), Integer.valueOf(content.getInt("chunk_count"))
/*    */           });
/* 49 */       EntityBuilder builder = getJDA().getEntityBuilder();
/*    */ 
/*    */       
/* 52 */       TLongObjectMap<DataObject> presences = content.optArray("presences").map(it -> builder.convertToUserMap((), it)).orElseGet(gnu.trove.map.hash.TLongObjectHashMap::new);
/* 53 */       for (int i = 0; i < members.length(); i++) {
/*    */         
/* 55 */         DataObject object = members.getObject(i);
/* 56 */         long userId = object.getObject("user").getUnsignedLong("id");
/* 57 */         DataObject presence = (DataObject)presences.get(userId);
/* 58 */         MemberImpl member = builder.createMember(guild, object, null, presence);
/* 59 */         builder.updateMemberCache(member);
/*    */       } 
/* 61 */       return null;
/*    */     } 
/* 63 */     getJDA().getGuildSetupController().onMemberChunk(guildId, content);
/* 64 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildMembersChunkHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */