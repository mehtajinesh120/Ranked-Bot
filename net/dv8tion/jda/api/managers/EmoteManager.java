/*     */ package net.dv8tion.jda.api.managers;
/*     */ 
/*     */ import java.util.Set;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Emote;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Role;
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
/*     */ public interface EmoteManager
/*     */   extends Manager<EmoteManager>
/*     */ {
/*     */   public static final long NAME = 1L;
/*     */   public static final long ROLES = 2L;
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   EmoteManager setRoles(@Nullable Set<Role> paramSet);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   EmoteManager setName(@Nonnull String paramString);
/*     */   
/*     */   @Nonnull
/*     */   Emote getEmote();
/*     */   
/*     */   @Nonnull
/*     */   default Guild getGuild() {
/* 101 */     return getEmote().getGuild();
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   EmoteManager reset(long... paramVarArgs);
/*     */   
/*     */   @Nonnull
/*     */   EmoteManager reset(long paramLong);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\managers\EmoteManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */