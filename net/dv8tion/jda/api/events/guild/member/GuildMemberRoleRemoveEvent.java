/*    */ package net.dv8tion.jda.api.events.guild.member;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Role;
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
/*    */ public class GuildMemberRoleRemoveEvent
/*    */   extends GenericGuildMemberEvent
/*    */ {
/*    */   private final List<Role> removedRoles;
/*    */   
/*    */   public GuildMemberRoleRemoveEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member, @Nonnull List<Role> removedRoles) {
/* 48 */     super(api, responseNumber, member);
/* 49 */     this.removedRoles = Collections.unmodifiableList(removedRoles);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<Role> getRoles() {
/* 59 */     return this.removedRoles;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\member\GuildMemberRoleRemoveEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */