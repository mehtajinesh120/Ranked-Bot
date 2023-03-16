/*    */ package net.dv8tion.jda.api.exceptions;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.Permission;
/*    */ import net.dv8tion.jda.api.entities.GuildChannel;
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
/*    */ public class MissingAccessException
/*    */   extends InsufficientPermissionException
/*    */ {
/*    */   public MissingAccessException(@Nonnull GuildChannel channel, @Nonnull Permission permission) {
/* 36 */     super(channel, permission);
/*    */   }
/*    */ 
/*    */   
/*    */   public MissingAccessException(@Nonnull GuildChannel channel, @Nonnull Permission permission, @Nonnull String reason) {
/* 41 */     super(channel, permission, reason);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\exceptions\MissingAccessException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */