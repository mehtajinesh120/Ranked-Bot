/*    */ package net.dv8tion.jda.internal.audio;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Guild;
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
/*    */ public class ConnectionRequest
/*    */ {
/*    */   protected final long guildId;
/*    */   protected long nextAttemptEpoch;
/*    */   protected ConnectionStage stage;
/*    */   protected long channelId;
/*    */   
/*    */   public ConnectionRequest(Guild guild) {
/* 32 */     this.stage = ConnectionStage.DISCONNECT;
/* 33 */     this.guildId = guild.getIdLong();
/*    */   }
/*    */ 
/*    */   
/*    */   public ConnectionRequest(VoiceChannel channel, ConnectionStage stage) {
/* 38 */     this.channelId = channel.getIdLong();
/* 39 */     this.guildId = channel.getGuild().getIdLong();
/* 40 */     this.stage = stage;
/* 41 */     this.nextAttemptEpoch = System.currentTimeMillis();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setStage(ConnectionStage stage) {
/* 46 */     this.stage = stage;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setChannel(VoiceChannel channel) {
/* 51 */     this.channelId = channel.getIdLong();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setNextAttemptEpoch(long epochMillis) {
/* 56 */     this.nextAttemptEpoch = epochMillis;
/*    */   }
/*    */ 
/*    */   
/*    */   public VoiceChannel getChannel(JDA api) {
/* 61 */     return api.getVoiceChannelById(this.channelId);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getChannelId() {
/* 66 */     return this.channelId;
/*    */   }
/*    */ 
/*    */   
/*    */   public ConnectionStage getStage() {
/* 71 */     return this.stage;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getNextAttemptEpoch() {
/* 76 */     return this.nextAttemptEpoch;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getGuildIdLong() {
/* 81 */     return this.guildId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 87 */     return this.stage + "(" + Long.toUnsignedString(this.guildId) + "#" + Long.toUnsignedString(this.channelId) + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\audio\ConnectionRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */