/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ApplicationTeam
/*     */   extends ISnowflake
/*     */ {
/*     */   public static final String ICON_URL = "https://cdn.discordapp.com/team-icons/%s/%s.png";
/*     */   
/*     */   @Nullable
/*     */   default TeamMember getOwner() {
/*  46 */     return getMemberById(getOwnerIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   default String getOwnerId() {
/*  57 */     return Long.toUnsignedString(getOwnerIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getOwnerIdLong();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   String getIconId();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default String getIconUrl() {
/*  85 */     String iconId = getIconId();
/*  86 */     return (iconId == null) ? null : String.format("https://cdn.discordapp.com/team-icons/%s/%s.png", new Object[] { getId(), iconId });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   List<TeamMember> getMembers();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isMember(@Nonnull User user) {
/* 110 */     return (getMember(user) != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default TeamMember getMember(@Nonnull User user) {
/* 128 */     Checks.notNull(user, "User");
/* 129 */     return getMemberById(user.getIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default TeamMember getMemberById(@Nonnull String userId) {
/* 147 */     return getMemberById(MiscUtil.parseSnowflake(userId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default TeamMember getMemberById(long userId) {
/* 162 */     for (TeamMember member : getMembers()) {
/*     */       
/* 164 */       if (member.getUser().getIdLong() == userId)
/* 165 */         return member; 
/*     */     } 
/* 167 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\ApplicationTeam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */