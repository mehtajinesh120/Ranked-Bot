/*    */ package net.dv8tion.jda.api.entities;
/*    */ 
/*    */ import java.util.EnumSet;
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
/*    */ public enum ActivityFlag
/*    */ {
/* 27 */   INSTANCE(0),
/* 28 */   JOIN(1),
/* 29 */   SPECTATE(2),
/* 30 */   JOIN_REQUEST(3),
/* 31 */   SYNC(4),
/* 32 */   PLAY(5);
/*    */   
/*    */   private final int offset;
/*    */   
/*    */   private final int raw;
/*    */   
/*    */   ActivityFlag(int offset) {
/* 39 */     this.offset = offset;
/* 40 */     this.raw = 1 << offset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOffset() {
/* 50 */     return this.offset;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getRaw() {
/* 60 */     return this.raw;
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
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public static EnumSet<ActivityFlag> getFlags(int raw) {
/* 77 */     EnumSet<ActivityFlag> set = EnumSet.noneOf(ActivityFlag.class);
/* 78 */     if (raw == 0)
/* 79 */       return set; 
/* 80 */     for (ActivityFlag flag : values()) {
/*    */       
/* 82 */       if ((flag.getRaw() & raw) == flag.getRaw())
/* 83 */         set.add(flag); 
/*    */     } 
/* 85 */     return set;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\ActivityFlag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */