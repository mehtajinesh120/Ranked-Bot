/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
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
/*    */ public class GuildMemberAddHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public GuildMemberAddHandler(JDAImpl api) {
/* 29 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 35 */     long id = content.getLong("guild_id");
/* 36 */     boolean setup = getJDA().getGuildSetupController().onAddMember(id, content);
/* 37 */     if (setup) {
/* 38 */       return null;
/*    */     }
/* 40 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(id);
/* 41 */     if (guild == null) {
/*    */       
/* 43 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, id, this.responseNumber, this.allContent, this::handle);
/* 44 */       EventCache.LOG.debug("Caching member for guild that is not yet cached. Guild ID: {} JSON: {}", Long.valueOf(id), content);
/* 45 */       return null;
/*    */     } 
/*    */ 
/*    */     
/* 49 */     guild.onMemberAdd();
/* 50 */     MemberImpl member = getJDA().getEntityBuilder().createMember(guild, content);
/* 51 */     getJDA().getEntityBuilder().updateMemberCache(member);
/* 52 */     getJDA().handleEvent((GenericEvent)new GuildMemberJoinEvent((JDA)
/*    */           
/* 54 */           getJDA(), this.responseNumber, (Member)member));
/*    */     
/* 56 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildMemberAddHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */