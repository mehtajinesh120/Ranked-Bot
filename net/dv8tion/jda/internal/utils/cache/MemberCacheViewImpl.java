/*     */ package net.dv8tion.jda.internal.utils.cache;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.utils.cache.MemberCacheView;
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
/*     */ public class MemberCacheViewImpl
/*     */   extends SnowflakeCacheViewImpl<Member>
/*     */   implements MemberCacheView
/*     */ {
/*     */   public MemberCacheViewImpl() {
/*  32 */     super(Member.class, Member::getEffectiveName);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getElementById(long id) {
/*  38 */     return get(id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Member> getElementsByUsername(@Nonnull String name, boolean ignoreCase) {
/*  45 */     Checks.notEmpty(name, "Name");
/*  46 */     if (isEmpty())
/*  47 */       return Collections.emptyList(); 
/*  48 */     List<Member> members = new ArrayList<>();
/*  49 */     forEach(member -> {
/*     */           String nick = member.getUser().getName();
/*     */           if (equals(ignoreCase, nick, name)) {
/*     */             members.add(member);
/*     */           }
/*     */         });
/*  55 */     return Collections.unmodifiableList(members);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Member> getElementsByNickname(@Nullable String name, boolean ignoreCase) {
/*  62 */     if (isEmpty())
/*  63 */       return Collections.emptyList(); 
/*  64 */     List<Member> members = new ArrayList<>();
/*  65 */     forEach(member -> {
/*     */           String nick = member.getNickname();
/*     */           
/*     */           if (nick == null) {
/*     */             if (name == null) {
/*     */               members.add(member);
/*     */             }
/*     */             return;
/*     */           } 
/*     */           if (equals(ignoreCase, nick, name)) {
/*     */             members.add(member);
/*     */           }
/*     */         });
/*  78 */     return Collections.unmodifiableList(members);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Member> getElementsWithRoles(@Nonnull Role... roles) {
/*  85 */     Checks.notNull(roles, "Roles");
/*  86 */     return getElementsWithRoles(Arrays.asList(roles));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Member> getElementsWithRoles(@Nonnull Collection<Role> roles) {
/*  93 */     Checks.noneNull(roles, "Roles");
/*  94 */     if (isEmpty())
/*  95 */       return Collections.emptyList(); 
/*  96 */     List<Member> members = new ArrayList<>();
/*  97 */     forEach(member -> {
/*     */           if (member.getRoles().containsAll(roles)) {
/*     */             members.add(member);
/*     */           }
/*     */         });
/* 102 */     return members;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\cache\MemberCacheViewImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */