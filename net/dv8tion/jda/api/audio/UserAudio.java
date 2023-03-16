/*    */ package net.dv8tion.jda.api.audio;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.entities.User;
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
/*    */ public class UserAudio
/*    */ {
/*    */   protected User user;
/*    */   protected short[] audioData;
/*    */   
/*    */   public UserAudio(@Nonnull User user, @Nonnull short[] audioData) {
/* 33 */     this.user = user;
/* 34 */     this.audioData = audioData;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public User getUser() {
/* 45 */     return this.user;
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
/*    */   
/*    */   @Nonnull
/*    */   public byte[] getAudioData(double volume) {
/* 63 */     return OpusPacket.getAudioData(this.audioData, volume);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\UserAudio.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */