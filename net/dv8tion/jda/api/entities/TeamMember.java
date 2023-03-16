/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface TeamMember
/*     */ {
/*     */   @Nonnull
/*     */   User getUser();
/*     */   
/*     */   @Nonnull
/*     */   MembershipState getMembershipState();
/*     */   
/*     */   @Nonnull
/*     */   default String getTeamId() {
/*  56 */     return Long.toUnsignedString(getTeamIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   long getTeamIdLong();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum MembershipState
/*     */   {
/*  72 */     INVITED(1),
/*     */     
/*  74 */     ACCEPTED(2),
/*     */     
/*  76 */     UNKNOWN(-1);
/*     */     
/*     */     private final int key;
/*     */ 
/*     */     
/*     */     MembershipState(int key) {
/*  82 */       this.key = key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getKey() {
/*  92 */       return this.key;
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
/*     */     public static MembershipState fromKey(int key) {
/* 106 */       for (MembershipState state : values()) {
/*     */         
/* 108 */         if (state.key == key)
/* 109 */           return state; 
/*     */       } 
/* 111 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\TeamMember.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */