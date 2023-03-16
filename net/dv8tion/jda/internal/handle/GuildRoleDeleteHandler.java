/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Emote;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Role;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.EmoteImpl;
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
/*    */ public class GuildRoleDeleteHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public GuildRoleDeleteHandler(JDAImpl api) {
/* 32 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 38 */     long guildId = content.getLong("guild_id");
/* 39 */     if (getJDA().getGuildSetupController().isLocked(guildId)) {
/* 40 */       return Long.valueOf(guildId);
/*    */     }
/* 42 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(guildId);
/* 43 */     if (guild == null) {
/*    */       
/* 45 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/* 46 */       EventCache.LOG.debug("GUILD_ROLE_DELETE was received for a Guild that is not yet cached: {}", content);
/* 47 */       return null;
/*    */     } 
/*    */     
/* 50 */     long roleId = content.getLong("role_id");
/* 51 */     Role removedRole = (Role)guild.getRolesView().remove(roleId);
/* 52 */     if (removedRole == null) {
/*    */ 
/*    */       
/* 55 */       WebSocketClient.LOG.debug("GUILD_ROLE_DELETE was received for a Role that is not yet cached: {}", content);
/* 56 */       return null;
/*    */     } 
/*    */ 
/*    */     
/* 60 */     guild.getMembersView().forEach(m -> {
/*    */           MemberImpl member = (MemberImpl)m;
/*    */           
/*    */           member.getRoleSet().remove(removedRole);
/*    */         });
/*    */     
/* 66 */     for (Emote emote : guild.getEmoteCache()) {
/*    */       
/* 68 */       EmoteImpl impl = (EmoteImpl)emote;
/* 69 */       if (impl.canProvideRoles()) {
/* 70 */         impl.getRoleSet().remove(removedRole);
/*    */       }
/*    */     } 
/* 73 */     getJDA().handleEvent((GenericEvent)new RoleDeleteEvent((JDA)
/*    */           
/* 75 */           getJDA(), this.responseNumber, removedRole));
/*    */     
/* 77 */     getJDA().getEventCache().clear(EventCache.Type.ROLE, roleId);
/* 78 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildRoleDeleteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */