/*    */ package net.dv8tion.jda.internal.entities;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.entities.TeamMember;
/*    */ import net.dv8tion.jda.api.entities.User;
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
/*    */ public class TeamMemberImpl
/*    */   implements TeamMember
/*    */ {
/*    */   private final User user;
/*    */   private final TeamMember.MembershipState state;
/*    */   private final long teamId;
/*    */   
/*    */   public TeamMemberImpl(User user, TeamMember.MembershipState state, long teamId) {
/* 33 */     this.user = user;
/* 34 */     this.state = state;
/* 35 */     this.teamId = teamId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public User getUser() {
/* 42 */     return this.user;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public TeamMember.MembershipState getMembershipState() {
/* 49 */     return this.state;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getTeamIdLong() {
/* 55 */     return this.teamId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 61 */     return Objects.hash(new Object[] { this.user, Long.valueOf(this.teamId) });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 67 */     if (obj == this)
/* 68 */       return true; 
/* 69 */     if (!(obj instanceof TeamMemberImpl))
/* 70 */       return false; 
/* 71 */     TeamMemberImpl member = (TeamMemberImpl)obj;
/* 72 */     return (member.teamId == this.teamId && member.user.equals(this.user));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 78 */     return "TeamMember(" + getTeamId() + ", " + this.user + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\TeamMemberImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */