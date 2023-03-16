/*     */ package net.dv8tion.jda.api.utils;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.annotations.Incubating;
/*     */ import net.dv8tion.jda.api.OnlineStatus;
/*     */ import net.dv8tion.jda.api.entities.GuildVoiceState;
/*     */ import net.dv8tion.jda.api.entities.Member;
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
/*     */ @FunctionalInterface
/*     */ public interface MemberCachePolicy
/*     */ {
/*     */   public static final MemberCachePolicy NONE = member -> false;
/*     */   public static final MemberCachePolicy ALL = member -> true;
/*  72 */   public static final MemberCachePolicy OWNER = Member::isOwner;
/*     */ 
/*     */   
/*     */   public static final MemberCachePolicy ONLINE;
/*     */ 
/*     */   
/*     */   public static final MemberCachePolicy VOICE;
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  83 */     ONLINE = (member -> (member.getOnlineStatus() != OnlineStatus.OFFLINE && member.getOnlineStatus() != OnlineStatus.UNKNOWN));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  88 */     VOICE = (member -> {
/*     */         GuildVoiceState voiceState = member.getVoiceState();
/*  90 */         return (voiceState != null && voiceState.getChannel() != null);
/*     */       });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Incubating
/* 101 */   public static final MemberCachePolicy PENDING = Member::isPending;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   public static final MemberCachePolicy DEFAULT = VOICE.or(OWNER);
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
/*     */   @Nonnull
/*     */   default MemberCachePolicy or(@Nonnull MemberCachePolicy policy) {
/* 137 */     Checks.notNull(policy, "Policy");
/* 138 */     return member -> (cacheMember(member) || policy.cacheMember(member));
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
/*     */   @Nonnull
/*     */   default MemberCachePolicy and(@Nonnull MemberCachePolicy policy) {
/* 156 */     return member -> (cacheMember(member) && policy.cacheMember(member));
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
/*     */   @Nonnull
/*     */   static MemberCachePolicy any(@Nonnull MemberCachePolicy policy, @Nonnull MemberCachePolicy... policies) {
/* 173 */     Checks.notNull(policy, "Policy");
/* 174 */     Checks.notNull(policies, "Policy");
/* 175 */     for (MemberCachePolicy p : policies)
/* 176 */       policy = policy.or(p); 
/* 177 */     return policy;
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
/*     */   @Nonnull
/*     */   static MemberCachePolicy all(@Nonnull MemberCachePolicy policy, @Nonnull MemberCachePolicy... policies) {
/* 194 */     Checks.notNull(policy, "Policy");
/* 195 */     Checks.notNull(policies, "Policy");
/* 196 */     for (MemberCachePolicy p : policies)
/* 197 */       policy = policy.and(p); 
/* 198 */     return policy;
/*     */   }
/*     */   
/*     */   boolean cacheMember(@Nonnull Member paramMember);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\MemberCachePolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */