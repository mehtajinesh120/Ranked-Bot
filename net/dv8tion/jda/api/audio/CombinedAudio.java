/*    */ package net.dv8tion.jda.api.audio;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public class CombinedAudio
/*    */ {
/*    */   protected List<User> users;
/*    */   protected short[] audioData;
/*    */   
/*    */   public CombinedAudio(@Nonnull List<User> users, @Nonnull short[] audioData) {
/* 35 */     this.users = Collections.unmodifiableList(users);
/* 36 */     this.audioData = audioData;
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
/*    */   public List<User> getUsers() {
/* 50 */     return this.users;
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
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public byte[] getAudioData(double volume) {
/* 70 */     return OpusPacket.getAudioData(this.audioData, volume);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\CombinedAudio.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */