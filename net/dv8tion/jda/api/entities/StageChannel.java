/*    */ package net.dv8tion.jda.api.entities;
/*    */ 
/*    */ import javax.annotation.CheckReturnValue;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.Permission;
/*    */ import net.dv8tion.jda.api.requests.restaction.StageInstanceAction;
/*    */ import net.dv8tion.jda.internal.utils.Checks;
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
/*    */ public interface StageChannel
/*    */   extends VoiceChannel
/*    */ {
/*    */   @Nullable
/*    */   StageInstance getStageInstance();
/*    */   
/*    */   @Nonnull
/*    */   @CheckReturnValue
/*    */   StageInstanceAction createStageInstance(@Nonnull String paramString);
/*    */   
/*    */   default boolean isModerator(@Nonnull Member member) {
/* 93 */     Checks.notNull(member, "Member");
/* 94 */     return member.hasPermission(this, new Permission[] { Permission.MANAGE_CHANNEL, Permission.VOICE_MUTE_OTHERS, Permission.VOICE_MOVE_OTHERS });
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\StageChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */