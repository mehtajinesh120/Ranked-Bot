/*    */ package net.dv8tion.jda.internal.managers;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*    */ import net.dv8tion.jda.api.managers.DirectAudioController;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.requests.WebSocketClient;
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
/*    */ public class DirectAudioControllerImpl
/*    */   implements DirectAudioController
/*    */ {
/*    */   private final JDAImpl api;
/*    */   
/*    */   public DirectAudioControllerImpl(JDAImpl api) {
/* 34 */     this.api = api;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public JDAImpl getJDA() {
/* 41 */     return this.api;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void connect(@Nonnull VoiceChannel channel) {
/* 47 */     Checks.notNull(channel, "Voice Channel");
/* 48 */     JDAImpl jda = getJDA();
/* 49 */     WebSocketClient client = jda.getClient();
/* 50 */     client.queueAudioConnect(channel);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void disconnect(@Nonnull Guild guild) {
/* 56 */     Checks.notNull(guild, "Guild");
/* 57 */     JDAImpl jda = getJDA();
/* 58 */     WebSocketClient client = jda.getClient();
/* 59 */     client.queueAudioDisconnect(guild);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void reconnect(@Nonnull VoiceChannel channel) {
/* 65 */     Checks.notNull(channel, "Voice Channel");
/* 66 */     JDAImpl jda = getJDA();
/* 67 */     WebSocketClient client = jda.getClient();
/* 68 */     client.queueAudioReconnect(channel);
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void update(Guild guild, VoiceChannel channel) {
/* 92 */     Checks.notNull(guild, "Guild");
/* 93 */     JDAImpl jda = getJDA();
/* 94 */     WebSocketClient client = jda.getClient();
/* 95 */     client.updateAudioConnection(guild.getIdLong(), channel);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\DirectAudioControllerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */