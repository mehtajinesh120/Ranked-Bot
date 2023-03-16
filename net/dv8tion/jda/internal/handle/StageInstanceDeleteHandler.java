/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.StageInstance;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.stage.StageInstanceDeleteEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*    */ import net.dv8tion.jda.internal.entities.StageChannelImpl;
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
/*    */ public class StageInstanceDeleteHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public StageInstanceDeleteHandler(JDAImpl api) {
/* 30 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 36 */     long guildId = content.getUnsignedLong("guild_id", 0L);
/* 37 */     if (getJDA().getGuildSetupController().isLocked(guildId)) {
/* 38 */       return Long.valueOf(guildId);
/*    */     }
/* 40 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(guildId);
/* 41 */     if (guild == null) {
/*    */       
/* 43 */       EventCache.LOG.debug("Caching STAGE_INSTANCE_DELETE for uncached guild with id {}", Long.valueOf(guildId));
/* 44 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/* 45 */       return null;
/*    */     } 
/*    */     
/* 48 */     long channelId = content.getUnsignedLong("channel_id", 0L);
/* 49 */     StageChannelImpl channel = (StageChannelImpl)guild.getStageChannelById(channelId);
/* 50 */     if (channel == null)
/* 51 */       return null; 
/* 52 */     StageInstance instance = channel.getStageInstance();
/* 53 */     channel.setStageInstance(null);
/* 54 */     if (instance != null)
/* 55 */       getJDA().handleEvent((GenericEvent)new StageInstanceDeleteEvent((JDA)getJDA(), this.responseNumber, instance)); 
/* 56 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\StageInstanceDeleteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */