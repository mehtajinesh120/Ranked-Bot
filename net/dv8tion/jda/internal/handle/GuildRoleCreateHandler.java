/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Role;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.role.RoleCreateEvent;
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
/*    */ public class GuildRoleCreateHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public GuildRoleCreateHandler(JDAImpl api) {
/* 29 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 35 */     long guildId = content.getLong("guild_id");
/* 36 */     if (getJDA().getGuildSetupController().isLocked(guildId)) {
/* 37 */       return Long.valueOf(guildId);
/*    */     }
/* 39 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(guildId);
/* 40 */     if (guild == null) {
/*    */       
/* 42 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/* 43 */       EventCache.LOG.debug("GUILD_ROLE_CREATE was received for a Guild that is not yet cached: {}", content);
/* 44 */       return null;
/*    */     } 
/*    */     
/* 47 */     Role newRole = getJDA().getEntityBuilder().createRole(guild, content.getObject("role"), guild.getIdLong());
/* 48 */     getJDA().handleEvent((GenericEvent)new RoleCreateEvent((JDA)
/*    */           
/* 50 */           getJDA(), this.responseNumber, newRole));
/*    */     
/* 52 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildRoleCreateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */