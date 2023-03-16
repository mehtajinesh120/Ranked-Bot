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
/*     */ public enum MessageType
/*     */ {
/*  29 */   DEFAULT(0, false),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  35 */   RECIPIENT_ADD(1),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   RECIPIENT_REMOVE(2),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   CALL(3),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  52 */   CHANNEL_NAME_CHANGE(4),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  57 */   CHANNEL_ICON_CHANGE(5),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   CHANNEL_PINNED_ADD(6),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   GUILD_MEMBER_JOIN(7),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  72 */   GUILD_MEMBER_BOOST(8),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   GUILD_BOOST_TIER_1(9),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   GUILD_BOOST_TIER_2(10),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  87 */   GUILD_BOOST_TIER_3(11),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  92 */   CHANNEL_FOLLOW_ADD(12),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   GUILD_DISCOVERY_DISQUALIFIED(14),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   GUILD_DISCOVERY_REQUALIFIED(15),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   GUILD_DISCOVERY_GRACE_PERIOD_INITIAL_WARNING(16),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 112 */   GUILD_DISCOVERY_GRACE_PERIOD_FINAL_WARNING(17),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 118 */   THREAD_CREATED(18),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 123 */   INLINE_REPLY(19, false),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 129 */   APPLICATION_COMMAND(20, false),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 135 */   THREAD_STARTER_MESSAGE(21),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   GUILD_INVITE_REMINDER(22),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 145 */   UNKNOWN(-1);
/*     */ 
/*     */ 
/*     */   
/*     */   private final int id;
/*     */ 
/*     */   
/*     */   private final boolean system;
/*     */ 
/*     */ 
/*     */   
/*     */   MessageType(int id, boolean system) {
/* 157 */     this.id = id;
/* 158 */     this.system = system;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getId() {
/* 168 */     return this.id;
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
/*     */   public boolean isSystem() {
/* 180 */     return this.system;
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
/*     */   @Nonnull
/*     */   public static MessageType fromId(int id) {
/* 195 */     for (MessageType type : values()) {
/*     */       
/* 197 */       if (type.id == id)
/* 198 */         return type; 
/*     */     } 
/* 200 */     return UNKNOWN;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\MessageType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */