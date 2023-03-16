/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*    */ import net.dv8tion.jda.api.hooks.VoiceDispatchInterceptor;
/*    */ import net.dv8tion.jda.api.utils.MiscUtil;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.audio.AudioConnection;
/*    */ import net.dv8tion.jda.internal.managers.AudioManagerImpl;
/*    */ import net.dv8tion.jda.internal.requests.WebSocketClient;
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
/*    */ public class VoiceServerUpdateHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public VoiceServerUpdateHandler(JDAImpl api) {
/* 33 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 39 */     long guildId = content.getLong("guild_id");
/* 40 */     if (getJDA().getGuildSetupController().isLocked(guildId))
/* 41 */       return Long.valueOf(guildId); 
/* 42 */     Guild guild = getJDA().getGuildById(guildId);
/* 43 */     if (guild == null) {
/* 44 */       throw new IllegalArgumentException("Attempted to start audio connection with Guild that doesn't exist!");
/*    */     }
/* 46 */     getJDA().getDirectAudioController().update(guild, guild.getSelfMember().getVoiceState().getChannel());
/*    */     
/* 48 */     if (content.isNull("endpoint"))
/*    */     {
/*    */ 
/*    */ 
/*    */       
/* 53 */       return null;
/*    */     }
/*    */ 
/*    */     
/* 57 */     String endpoint = content.getString("endpoint").replace(":80", "");
/* 58 */     String token = content.getString("token");
/* 59 */     String sessionId = guild.getSelfMember().getVoiceState().getSessionId();
/* 60 */     if (sessionId == null) {
/* 61 */       throw new IllegalArgumentException("Attempted to create audio connection without having a session ID. Did VOICE_STATE_UPDATED fail?");
/*    */     }
/* 63 */     VoiceDispatchInterceptor voiceInterceptor = getJDA().getVoiceInterceptor();
/* 64 */     if (voiceInterceptor != null) {
/*    */       
/* 66 */       voiceInterceptor.onVoiceServerUpdate(new VoiceDispatchInterceptor.VoiceServerUpdate(guild, endpoint, token, sessionId, this.allContent));
/* 67 */       return null;
/*    */     } 
/*    */     
/* 70 */     AudioManagerImpl audioManager = (AudioManagerImpl)getJDA().getAudioManagersView().get(guildId);
/* 71 */     if (audioManager == null) {
/*    */       
/* 73 */       WebSocketClient.LOG.debug("Received a VOICE_SERVER_UPDATE but JDA is not currently connected nor attempted to connect to a VoiceChannel. Assuming that this is caused by another client running on this account. Ignoring the event.");
/*    */ 
/*    */ 
/*    */       
/* 77 */       return null;
/*    */     } 
/*    */     
/* 80 */     MiscUtil.locked(audioManager.CONNECTION_LOCK, () -> {
/*    */           VoiceChannel target = guild.getSelfMember().getVoiceState().getChannel();
/*    */           
/*    */           if (target == null) {
/*    */             WebSocketClient.LOG.warn("Ignoring VOICE_SERVER_UPDATE for unknown channel");
/*    */             
/*    */             return;
/*    */           } 
/*    */           
/*    */           AudioConnection connection = new AudioConnection(audioManager, endpoint, sessionId, token, target);
/*    */           
/*    */           audioManager.setAudioConnection(connection);
/*    */           connection.startConnection();
/*    */         });
/* 94 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\VoiceServerUpdateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */