/*    */ package net.dv8tion.jda.api.interactions.commands;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum OptionType
/*    */ {
/* 27 */   UNKNOWN(-1),
/* 28 */   SUB_COMMAND(1),
/* 29 */   SUB_COMMAND_GROUP(2),
/* 30 */   STRING(3, true),
/* 31 */   INTEGER(4, true),
/* 32 */   BOOLEAN(5),
/* 33 */   USER(6),
/* 34 */   CHANNEL(7),
/* 35 */   ROLE(8),
/* 36 */   MENTIONABLE(9),
/* 37 */   NUMBER(10, true);
/*    */ 
/*    */ 
/*    */   
/*    */   private final int raw;
/*    */ 
/*    */ 
/*    */   
/*    */   private final boolean supportsChoices;
/*    */ 
/*    */ 
/*    */   
/*    */   OptionType(int raw, boolean supportsChoices) {
/* 50 */     this.raw = raw;
/* 51 */     this.supportsChoices = supportsChoices;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getKey() {
/* 61 */     return this.raw;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canSupportChoices() {
/* 71 */     return this.supportsChoices;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static OptionType fromKey(int key) {
/* 85 */     for (OptionType type : values()) {
/*    */       
/* 87 */       if (type.raw == key)
/* 88 */         return type; 
/*    */     } 
/* 90 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\commands\OptionType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */