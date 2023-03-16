/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Role;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.guild.member.GuildMemberUpdateEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataArray;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*    */ import net.dv8tion.jda.internal.entities.MemberImpl;
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
/*    */ public class GuildMemberUpdateHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public GuildMemberUpdateHandler(JDAImpl api) {
/* 34 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 40 */     long id = content.getLong("guild_id");
/* 41 */     if (getJDA().getGuildSetupController().isLocked(id)) {
/* 42 */       return Long.valueOf(id);
/*    */     }
/* 44 */     DataObject userJson = content.getObject("user");
/* 45 */     long userId = userJson.getLong("id");
/* 46 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(id);
/* 47 */     if (guild == null) {
/*    */ 
/*    */ 
/*    */       
/* 51 */       EventCache.LOG.debug("Got GuildMember update but JDA currently does not have the Guild cached. Ignoring. {}", content);
/* 52 */       return null;
/*    */     } 
/*    */     
/* 55 */     MemberImpl member = (MemberImpl)guild.getMembersView().get(userId);
/* 56 */     if (member == null) {
/*    */       
/* 58 */       member = getJDA().getEntityBuilder().createMember(guild, content);
/*    */     }
/*    */     else {
/*    */       
/* 62 */       List<Role> newRoles = toRolesList(guild, content.getArray("roles"));
/* 63 */       getJDA().getEntityBuilder().updateMember(guild, member, content, newRoles);
/*    */     } 
/*    */     
/* 66 */     getJDA().getEntityBuilder().updateMemberCache(member);
/* 67 */     getJDA().handleEvent((GenericEvent)new GuildMemberUpdateEvent((JDA)getJDA(), this.responseNumber, (Member)member));
/* 68 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   private List<Role> toRolesList(GuildImpl guild, DataArray array) {
/* 73 */     LinkedList<Role> roles = new LinkedList<>();
/* 74 */     for (int i = 0; i < array.length(); i++) {
/*    */       
/* 76 */       long id = array.getLong(i);
/* 77 */       Role r = (Role)guild.getRolesView().get(id);
/* 78 */       if (r != null) {
/*    */         
/* 80 */         roles.add(r);
/*    */       }
/*    */       else {
/*    */         
/* 84 */         getJDA().getEventCache().cache(EventCache.Type.ROLE, id, this.responseNumber, this.allContent, this::handle);
/* 85 */         EventCache.LOG.debug("Got GuildMember update but one of the Roles for the Member is not yet cached.");
/* 86 */         return null;
/*    */       } 
/*    */     } 
/* 89 */     return roles;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildMemberUpdateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */