/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.GuildChannel;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
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
/*    */ public class InviteDeleteHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public InviteDeleteHandler(JDAImpl api) {
/* 29 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 35 */     long guildId = content.getUnsignedLong("guild_id");
/* 36 */     if (getJDA().getGuildSetupController().isLocked(guildId))
/* 37 */       return Long.valueOf(guildId); 
/* 38 */     Guild guild = getJDA().getGuildById(guildId);
/* 39 */     if (guild == null) {
/*    */       
/* 41 */       EventCache.LOG.debug("Caching INVITE_DELETE for unknown guild {}", Long.valueOf(guildId));
/* 42 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/* 43 */       return null;
/*    */     } 
/* 45 */     long channelId = content.getUnsignedLong("channel_id");
/* 46 */     GuildChannel channel = guild.getGuildChannelById(channelId);
/* 47 */     if (channel == null) {
/*    */       
/* 49 */       EventCache.LOG.debug("Caching INVITE_DELETE for unknown channel {} in guild {}", Long.valueOf(channelId), Long.valueOf(guildId));
/* 50 */       getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle);
/* 51 */       return null;
/*    */     } 
/*    */     
/* 54 */     String code = content.getString("code");
/* 55 */     getJDA().handleEvent((GenericEvent)new GuildInviteDeleteEvent((JDA)
/*    */           
/* 57 */           getJDA(), this.responseNumber, code, channel));
/*    */     
/* 59 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\InviteDeleteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */