/*     */ package net.dv8tion.jda.api.utils.cache;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
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
/*     */ 
/*     */ 
/*     */ public interface MemberCacheView
/*     */   extends SnowflakeCacheView<Member>
/*     */ {
/*     */   @Nullable
/*     */   default Member getElementById(@Nonnull String id) {
/*  65 */     return getElementById(MiscUtil.parseSnowflake(id));
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
/*     */   default List<Member> getElementsByNickname(@Nullable String name) {
/* 133 */     return getElementsByNickname(name, false);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   Member getElementById(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   List<Member> getElementsByUsername(@Nonnull String paramString, boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   List<Member> getElementsByNickname(@Nullable String paramString, boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   List<Member> getElementsWithRoles(@Nonnull Role... paramVarArgs);
/*     */   
/*     */   @Nonnull
/*     */   List<Member> getElementsWithRoles(@Nonnull Collection<Role> paramCollection);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\cache\MemberCacheView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */