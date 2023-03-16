/*    */ package net.dv8tion.jda.api.entities;
/*    */ 
/*    */ import java.util.Formattable;
/*    */ import java.util.Formatter;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.utils.MiscUtil;
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
/*    */ public interface IMentionable
/*    */   extends Formattable, ISnowflake
/*    */ {
/*    */   @Nonnull
/*    */   String getAsMention();
/*    */   
/*    */   default void formatTo(Formatter formatter, int flags, int width, int precision) {
/* 66 */     boolean leftJustified = ((flags & 0x1) == 1);
/* 67 */     boolean upper = ((flags & 0x2) == 2);
/* 68 */     String out = upper ? getAsMention().toUpperCase(formatter.locale()) : getAsMention();
/*    */     
/* 70 */     MiscUtil.appendTo(formatter, width, precision, leftJustified, out);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\IMentionable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */