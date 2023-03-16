/*    */ package net.dv8tion.jda.api.events.guild.voice;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.VoiceChannel;
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
/*    */ public class GenericGuildVoiceUpdateEvent
/*    */   extends GenericGuildVoiceEvent
/*    */   implements GuildVoiceUpdateEvent
/*    */ {
/*    */   protected final VoiceChannel joined;
/*    */   protected final VoiceChannel left;
/*    */   
/*    */   public GenericGuildVoiceUpdateEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member, @Nullable VoiceChannel left, @Nullable VoiceChannel joined) {
/* 46 */     super(api, responseNumber, member);
/* 47 */     this.left = left;
/* 48 */     this.joined = joined;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public VoiceChannel getChannelLeft() {
/* 55 */     return this.left;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public VoiceChannel getChannelJoined() {
/* 62 */     return this.joined;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getPropertyIdentifier() {
/* 69 */     return "voice-channel";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Member getEntity() {
/* 76 */     return getMember();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public VoiceChannel getOldValue() {
/* 83 */     return getChannelLeft();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public VoiceChannel getNewValue() {
/* 90 */     return getChannelJoined();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 96 */     return "MemberVoiceUpdate[" + getPropertyIdentifier() + "](" + getOldValue() + "->" + getNewValue() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\voice\GenericGuildVoiceUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */