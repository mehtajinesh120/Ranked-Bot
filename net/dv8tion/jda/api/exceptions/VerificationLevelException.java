/*    */ package net.dv8tion.jda.api.exceptions;
/*    */ 
/*    */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*    */ import net.dv8tion.jda.annotations.ForRemoval;
/*    */ import net.dv8tion.jda.api.entities.Guild;
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
/*    */ @Deprecated
/*    */ @ForRemoval(deadline = "4.4.0")
/*    */ @DeprecatedSince("4.2.0")
/*    */ public class VerificationLevelException
/*    */   extends IllegalStateException
/*    */ {
/*    */   public VerificationLevelException(Guild.VerificationLevel level) {
/* 30 */     super("Messages to this Guild can not be sent due to the Guilds verification level. (" + level.toString() + ')');
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\exceptions\VerificationLevelException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */