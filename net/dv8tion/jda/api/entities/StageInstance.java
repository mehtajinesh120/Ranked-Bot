/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.managers.StageInstanceManager;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface StageInstance
/*     */   extends ISnowflake
/*     */ {
/*     */   @Nonnull
/*     */   default List<Member> getSpeakers() {
/*  90 */     return Collections.unmodifiableList((List<? extends Member>)getChannel().getMembers()
/*  91 */         .stream()
/*  92 */         .filter(member -> !member.getVoiceState().isSuppressed())
/*  93 */         .collect(Collectors.toList()));
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
/*     */   default List<Member> getAudience() {
/* 111 */     return Collections.unmodifiableList((List<? extends Member>)getChannel().getMembers()
/* 112 */         .stream()
/* 113 */         .filter(member -> member.getVoiceState().isSuppressed())
/* 114 */         .collect(Collectors.toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   Guild getGuild();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   StageChannel getChannel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   String getTopic();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   PrivacyLevel getPrivacyLevel();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isDiscoverable();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RestAction<Void> delete();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RestAction<Void> requestToSpeak();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   RestAction<Void> cancelRequestToSpeak();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   StageInstanceManager getManager();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum PrivacyLevel
/*     */   {
/* 192 */     UNKNOWN(-1),
/*     */     
/* 194 */     PUBLIC(1),
/*     */     
/* 196 */     GUILD_ONLY(2);
/*     */     
/*     */     private final int key;
/*     */ 
/*     */     
/*     */     PrivacyLevel(int key) {
/* 202 */       this.key = key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getKey() {
/* 212 */       return this.key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public static PrivacyLevel fromKey(int key) {
/* 226 */       for (PrivacyLevel level : values()) {
/*     */         
/* 228 */         if (level.key == key)
/* 229 */           return level; 
/*     */       } 
/* 231 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\StageInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */