/*    */ package net.dv8tion.jda.internal.entities;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.entities.ApplicationTeam;
/*    */ import net.dv8tion.jda.api.entities.TeamMember;
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
/*    */ public class ApplicationTeamImpl
/*    */   implements ApplicationTeam
/*    */ {
/*    */   private final String iconId;
/*    */   private final List<TeamMember> members;
/*    */   private final long id;
/*    */   private final long ownerId;
/*    */   
/*    */   public ApplicationTeamImpl(String iconId, List<TeamMember> members, long id, long ownerId) {
/* 34 */     this.iconId = iconId;
/* 35 */     this.members = Collections.unmodifiableList(members);
/* 36 */     this.id = id;
/* 37 */     this.ownerId = ownerId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getOwnerIdLong() {
/* 43 */     return this.ownerId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getIconId() {
/* 49 */     return this.iconId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<TeamMember> getMembers() {
/* 56 */     return this.members;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getIdLong() {
/* 62 */     return this.id;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 68 */     return Long.hashCode(this.id);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 74 */     if (obj == this)
/* 75 */       return true; 
/* 76 */     if (!(obj instanceof ApplicationTeamImpl))
/* 77 */       return false; 
/* 78 */     ApplicationTeamImpl app = (ApplicationTeamImpl)obj;
/* 79 */     return (app.id == this.id);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 85 */     return "ApplicationTeam(" + getId() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\ApplicationTeamImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */