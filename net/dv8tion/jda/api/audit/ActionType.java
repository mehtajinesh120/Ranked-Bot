/*     */ package net.dv8tion.jda.api.audit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum ActionType
/*     */ {
/*  47 */   GUILD_UPDATE(1, TargetType.GUILD),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   CHANNEL_CREATE(10, TargetType.CHANNEL),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  82 */   CHANNEL_UPDATE(11, TargetType.CHANNEL),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 100 */   CHANNEL_DELETE(12, TargetType.CHANNEL),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   CHANNEL_OVERRIDE_CREATE(13, TargetType.CHANNEL),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 130 */   CHANNEL_OVERRIDE_UPDATE(14, TargetType.CHANNEL),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 149 */   CHANNEL_OVERRIDE_DELETE(15, TargetType.CHANNEL),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 155 */   KICK(20, TargetType.MEMBER),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 166 */   PRUNE(21, TargetType.MEMBER),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 171 */   BAN(22, TargetType.MEMBER),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   UNBAN(23, TargetType.MEMBER),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   MEMBER_UPDATE(24, TargetType.MEMBER),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 199 */   MEMBER_ROLE_UPDATE(25, TargetType.MEMBER),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 210 */   MEMBER_VOICE_MOVE(26, TargetType.MEMBER),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 220 */   MEMBER_VOICE_KICK(27, TargetType.MEMBER),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 225 */   BOT_ADD(28, TargetType.MEMBER),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 241 */   ROLE_CREATE(30, TargetType.ROLE),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 255 */   ROLE_UPDATE(31, TargetType.ROLE),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 270 */   ROLE_DELETE(32, TargetType.ROLE),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 286 */   INVITE_CREATE(40, TargetType.INVITE),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 291 */   INVITE_UPDATE(41, TargetType.INVITE),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 306 */   INVITE_DELETE(42, TargetType.INVITE),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 320 */   WEBHOOK_CREATE(50, TargetType.WEBHOOK),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 333 */   WEBHOOK_UPDATE(51, TargetType.WEBHOOK),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 346 */   WEBHOOK_REMOVE(52, TargetType.WEBHOOK),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 357 */   EMOTE_CREATE(60, TargetType.EMOTE),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 369 */   EMOTE_UPDATE(61, TargetType.EMOTE),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 379 */   EMOTE_DELETE(62, TargetType.EMOTE),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 385 */   MESSAGE_CREATE(70, TargetType.UNKNOWN),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 390 */   MESSAGE_UPDATE(71, TargetType.UNKNOWN),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 400 */   MESSAGE_DELETE(72, TargetType.MEMBER),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 410 */   MESSAGE_BULK_DELETE(73, TargetType.CHANNEL),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 421 */   MESSAGE_PIN(74, TargetType.CHANNEL),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 432 */   MESSAGE_UNPIN(75, TargetType.CHANNEL),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 437 */   INTEGRATION_CREATE(80, TargetType.INTEGRATION),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 442 */   INTEGRATION_UPDATE(81, TargetType.INTEGRATION),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 447 */   INTEGRATION_DELETE(82, TargetType.INTEGRATION),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 463 */   STAGE_INSTANCE_CREATE(83, TargetType.STAGE_INSTANCE),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 479 */   STAGE_INSTANCE_UPDATE(84, TargetType.STAGE_INSTANCE),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 495 */   STAGE_INSTANCE_DELETE(85, TargetType.STAGE_INSTANCE),
/*     */   
/* 497 */   UNKNOWN(-1, TargetType.UNKNOWN);
/*     */   
/*     */   private final int key;
/*     */   
/*     */   private final TargetType target;
/*     */   
/*     */   ActionType(int key, TargetType target) {
/* 504 */     this.key = key;
/* 505 */     this.target = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getKey() {
/* 515 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TargetType getTargetType() {
/* 526 */     return this.target;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ActionType from(int key) {
/* 531 */     for (ActionType type : values()) {
/*     */       
/* 533 */       if (type.key == key)
/* 534 */         return type; 
/*     */     } 
/* 536 */     return UNKNOWN;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audit\ActionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */