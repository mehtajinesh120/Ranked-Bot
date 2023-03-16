/*    */ package net.dv8tion.jda.api.entities;
/*    */ 
/*    */ import java.time.OffsetDateTime;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.utils.TimeUtil;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ISnowflake
/*    */ {
/*    */   @Nonnull
/*    */   default String getId() {
/* 39 */     return Long.toUnsignedString(getIdLong());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   long getIdLong();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   default OffsetDateTime getTimeCreated() {
/* 59 */     return TimeUtil.getTimeCreated(getIdLong());
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\ISnowflake.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */