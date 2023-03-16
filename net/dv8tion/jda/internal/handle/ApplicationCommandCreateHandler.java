/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.application.ApplicationCommandCreateEvent;
/*    */ import net.dv8tion.jda.api.interactions.commands.Command;
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
/*    */ public class ApplicationCommandCreateHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public ApplicationCommandCreateHandler(JDAImpl api) {
/* 29 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 36 */     long guildId = content.getUnsignedLong("guild_id");
/* 37 */     if (this.api.getGuildSetupController().isLocked(guildId))
/* 38 */       return Long.valueOf(guildId); 
/* 39 */     Guild guild = this.api.getGuildById(guildId);
/* 40 */     if (guildId != 0L && guild == null) {
/*    */       
/* 42 */       EventCache.LOG.debug("Received APPLICATION_COMMAND_UPDATE for Guild that isn't cache. GuildId: {}", Long.valueOf(guildId));
/* 43 */       this.api.getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/* 44 */       return null;
/*    */     } 
/*    */     
/* 47 */     Command command = new Command(this.api, guild, content);
/* 48 */     this.api.handleEvent((GenericEvent)new ApplicationCommandCreateEvent((JDA)this.api, this.responseNumber, command, guild));
/*    */ 
/*    */     
/* 51 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\ApplicationCommandCreateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */