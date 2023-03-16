/*     */ package net.dv8tion.jda.api.utils.cache;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
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
/*     */ public interface UnifiedMemberCacheView
/*     */   extends CacheView<Member>
/*     */ {
/*     */   @Nonnull
/*     */   List<Member> getElementsById(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   default List<Member> getElementsById(@Nonnull String id) {
/*  65 */     return getElementsById(MiscUtil.parseSnowflake(id));
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
/*     */   
/*     */   @Nonnull
/*     */   List<Member> getElementsByUsername(@Nonnull String paramString, boolean paramBoolean);
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
/*     */   default List<Member> getElementsByUsername(@Nonnull String name) {
/* 100 */     return getElementsByUsername(name, false);
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
/*     */   List<Member> getElementsByNickname(@Nullable String paramString, boolean paramBoolean);
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
/*     */   default List<Member> getElementsByNickname(@Nullable String name) {
/* 133 */     return getElementsByNickname(name, false);
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   List<Member> getElementsWithRoles(@Nonnull Role... paramVarArgs);
/*     */   
/*     */   @Nonnull
/*     */   List<Member> getElementsWithRoles(@Nonnull Collection<Role> paramCollection);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\cache\UnifiedMemberCacheView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */