/*     */ package net.dv8tion.jda.api.hooks;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildVoiceState;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.managers.DirectAudioController;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.api.utils.data.SerializableData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface VoiceDispatchInterceptor
/*     */ {
/*     */   void onVoiceServerUpdate(@Nonnull VoiceServerUpdate paramVoiceServerUpdate);
/*     */   
/*     */   boolean onVoiceStateUpdate(@Nonnull VoiceStateUpdate paramVoiceStateUpdate);
/*     */   
/*     */   public static interface VoiceUpdate
/*     */     extends SerializableData
/*     */   {
/*     */     @Nonnull
/*     */     Guild getGuild();
/*     */     
/*     */     @Nonnull
/*     */     DataObject toData();
/*     */     
/*     */     @Nonnull
/*     */     default DirectAudioController getAudioController() {
/*  92 */       return getJDA().getDirectAudioController();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default long getGuildIdLong() {
/* 102 */       return getGuild().getIdLong();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     default String getGuildId() {
/* 113 */       return Long.toUnsignedString(getGuildIdLong());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     default JDA getJDA() {
/* 124 */       return getGuild().getJDA();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     default JDA.ShardInfo getShardInfo() {
/* 135 */       return getJDA().getShardInfo();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class VoiceServerUpdate
/*     */     implements VoiceUpdate
/*     */   {
/*     */     private final Guild guild;
/*     */     
/*     */     private final String endpoint;
/*     */     
/*     */     private final String token;
/*     */     private final String sessionId;
/*     */     private final DataObject json;
/*     */     
/*     */     public VoiceServerUpdate(Guild guild, String endpoint, String token, String sessionId, DataObject json) {
/* 152 */       this.guild = guild;
/* 153 */       this.endpoint = endpoint;
/* 154 */       this.token = token;
/* 155 */       this.sessionId = sessionId;
/* 156 */       this.json = json;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Guild getGuild() {
/* 163 */       return this.guild;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public DataObject toData() {
/* 170 */       return this.json;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getEndpoint() {
/* 181 */       return this.endpoint;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getToken() {
/* 192 */       return this.token;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getSessionId() {
/* 203 */       return this.sessionId;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class VoiceStateUpdate
/*     */     implements VoiceUpdate
/*     */   {
/*     */     private final VoiceChannel channel;
/*     */     
/*     */     private final GuildVoiceState voiceState;
/*     */     
/*     */     private final DataObject json;
/*     */     
/*     */     public VoiceStateUpdate(VoiceChannel channel, GuildVoiceState voiceState, DataObject json) {
/* 218 */       this.channel = channel;
/* 219 */       this.voiceState = voiceState;
/* 220 */       this.json = json;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Guild getGuild() {
/* 227 */       return this.voiceState.getGuild();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public DataObject toData() {
/* 234 */       return this.json;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public VoiceChannel getChannel() {
/* 245 */       return this.channel;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public GuildVoiceState getVoiceState() {
/* 256 */       return this.voiceState;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\hooks\VoiceDispatchInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */