/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.StageInstance;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.stage.StageInstanceCreateEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.GuildImpl;
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
/*    */ public class StageInstanceCreateHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public StageInstanceCreateHandler(JDAImpl api) {
/* 29 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 35 */     long guildId = content.getUnsignedLong("guild_id", 0L);
/* 36 */     if (getJDA().getGuildSetupController().isLocked(guildId)) {
/* 37 */       return Long.valueOf(guildId);
/*    */     }
/* 39 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(guildId);
/* 40 */     if (guild == null) {
/*    */       
/* 42 */       EventCache.LOG.debug("Caching STAGE_INSTANCE_CREATE for uncached guild with id {}", Long.valueOf(guildId));
/* 43 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/* 44 */       return null;
/*    */     } 
/*    */     
/* 47 */     StageInstance instance = getJDA().getEntityBuilder().createStageInstance(guild, content);
/* 48 */     if (instance != null) {
/*    */       
/* 50 */       getJDA().handleEvent((GenericEvent)new StageInstanceCreateEvent((JDA)getJDA(), this.responseNumber, instance));
/* 51 */       guild.updateRequestToSpeak();
/*    */     } 
/* 53 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\StageInstanceCreateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */