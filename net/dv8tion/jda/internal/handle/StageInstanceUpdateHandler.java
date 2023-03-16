/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.StageChannel;
/*    */ import net.dv8tion.jda.api.entities.StageInstance;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.stage.update.StageInstanceUpdatePrivacyLevelEvent;
/*    */ import net.dv8tion.jda.api.events.stage.update.StageInstanceUpdateTopicEvent;
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
/*    */ 
/*    */ public class StageInstanceUpdateHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public StageInstanceUpdateHandler(JDAImpl api) {
/* 33 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 39 */     long guildId = content.getUnsignedLong("guild_id", 0L);
/* 40 */     if (getJDA().getGuildSetupController().isLocked(guildId)) {
/* 41 */       return Long.valueOf(guildId);
/*    */     }
/* 43 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(guildId);
/* 44 */     if (guild == null) {
/*    */       
/* 46 */       EventCache.LOG.debug("Caching STAGE_INSTANCE_UPDATE for uncached guild with id {}", Long.valueOf(guildId));
/* 47 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/* 48 */       return null;
/*    */     } 
/*    */     
/* 51 */     StageChannel channel = getJDA().getStageChannelById(content.getUnsignedLong("channel_id"));
/* 52 */     if (channel == null)
/* 53 */       return null; 
/* 54 */     StageInstance oldInstance = channel.getStageInstance();
/* 55 */     if (oldInstance == null) {
/* 56 */       return null;
/*    */     }
/* 58 */     String oldTopic = oldInstance.getTopic();
/* 59 */     StageInstance.PrivacyLevel oldLevel = oldInstance.getPrivacyLevel();
/* 60 */     StageInstance newInstance = getJDA().getEntityBuilder().createStageInstance(guild, content);
/* 61 */     if (newInstance == null) {
/* 62 */       return null;
/*    */     }
/* 64 */     if (!Objects.equals(oldTopic, newInstance.getTopic()))
/* 65 */       getJDA().handleEvent((GenericEvent)new StageInstanceUpdateTopicEvent((JDA)getJDA(), this.responseNumber, newInstance, oldTopic)); 
/* 66 */     if (oldLevel != newInstance.getPrivacyLevel())
/* 67 */       getJDA().handleEvent((GenericEvent)new StageInstanceUpdatePrivacyLevelEvent((JDA)getJDA(), this.responseNumber, newInstance, oldLevel)); 
/* 68 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\StageInstanceUpdateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */