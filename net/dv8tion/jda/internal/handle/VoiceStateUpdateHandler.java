/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.Objects;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildVoiceState;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceDeafenEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMuteEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceRequestToSpeakEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfDeafenEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSelfMuteEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceStreamEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceSuppressEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceVideoEvent;
/*     */ import net.dv8tion.jda.api.hooks.VoiceDispatchInterceptor;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildVoiceStateImpl;
/*     */ import net.dv8tion.jda.internal.entities.MemberImpl;
/*     */ import net.dv8tion.jda.internal.entities.VoiceChannelImpl;
/*     */ import net.dv8tion.jda.internal.managers.AudioManagerImpl;
/*     */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*     */ 
/*     */ public class VoiceStateUpdateHandler
/*     */   extends SocketHandler
/*     */ {
/*     */   public VoiceStateUpdateHandler(JDAImpl api) {
/*  38 */     super(api);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Long handleInternally(DataObject content) {
/*  44 */     Long guildId = content.isNull("guild_id") ? null : Long.valueOf(content.getLong("guild_id"));
/*  45 */     if (guildId == null)
/*  46 */       return null; 
/*  47 */     if (getJDA().getGuildSetupController().isLocked(guildId.longValue())) {
/*  48 */       return guildId;
/*     */     }
/*     */     
/*  51 */     if (content.isNull("member")) {
/*     */       
/*  53 */       WebSocketClient.LOG.debug("Discarding VOICE_STATE_UPDATE with missing member. JSON: {}", content);
/*  54 */       return null;
/*     */     } 
/*     */     
/*  57 */     handleGuildVoiceState(content);
/*  58 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleGuildVoiceState(DataObject content) {
/*  63 */     long userId = content.getLong("user_id");
/*  64 */     long guildId = content.getLong("guild_id");
/*  65 */     Long channelId = !content.isNull("channel_id") ? Long.valueOf(content.getLong("channel_id")) : null;
/*  66 */     String sessionId = !content.isNull("session_id") ? content.getString("session_id") : null;
/*  67 */     boolean selfMuted = content.getBoolean("self_mute");
/*  68 */     boolean selfDeafened = content.getBoolean("self_deaf");
/*  69 */     boolean guildMuted = content.getBoolean("mute");
/*  70 */     boolean guildDeafened = content.getBoolean("deaf");
/*  71 */     boolean suppressed = content.getBoolean("suppress");
/*  72 */     boolean stream = content.getBoolean("self_stream");
/*  73 */     boolean video = content.getBoolean("self_video", false);
/*  74 */     String requestToSpeak = content.getString("request_to_speak_timestamp", null);
/*  75 */     OffsetDateTime requestToSpeakTime = null;
/*  76 */     long requestToSpeakTimestamp = 0L;
/*  77 */     if (requestToSpeak != null) {
/*     */       
/*  79 */       requestToSpeakTime = OffsetDateTime.parse(requestToSpeak);
/*  80 */       requestToSpeakTimestamp = requestToSpeakTime.toInstant().toEpochMilli();
/*     */     } 
/*     */     
/*  83 */     Guild guild = getJDA().getGuildById(guildId);
/*  84 */     if (guild == null) {
/*     */       
/*  86 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/*  87 */       EventCache.LOG.debug("Received a VOICE_STATE_UPDATE for a Guild that has yet to be cached. JSON: {}", content);
/*     */       
/*     */       return;
/*     */     } 
/*  91 */     VoiceChannelImpl channel = (channelId != null) ? (VoiceChannelImpl)guild.getVoiceChannelById(channelId.longValue()) : null;
/*  92 */     if (channel == null && channelId != null) {
/*     */       
/*  94 */       getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId.longValue(), this.responseNumber, this.allContent, this::handle);
/*  95 */       EventCache.LOG.debug("Received VOICE_STATE_UPDATE for a VoiceChannel that has yet to be cached. JSON: {}", content);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 100 */     DataObject memberJson = content.getObject("member");
/* 101 */     MemberImpl member = getJDA().getEntityBuilder().createMember((GuildImpl)guild, memberJson);
/* 102 */     if (member == null)
/*     */       return; 
/* 104 */     GuildVoiceStateImpl vState = (GuildVoiceStateImpl)member.getVoiceState();
/* 105 */     if (vState == null)
/*     */       return; 
/* 107 */     vState.setSessionId(sessionId);
/* 108 */     VoiceDispatchInterceptor voiceInterceptor = getJDA().getVoiceInterceptor();
/* 109 */     boolean isSelf = guild.getSelfMember().equals(member);
/*     */     
/* 111 */     boolean wasMute = vState.isMuted();
/* 112 */     boolean wasDeaf = vState.isDeafened();
/*     */     
/* 114 */     if (selfMuted != vState.isSelfMuted()) {
/*     */       
/* 116 */       vState.setSelfMuted(selfMuted);
/* 117 */       getJDA().getEntityBuilder().updateMemberCache(member);
/* 118 */       getJDA().handleEvent((GenericEvent)new GuildVoiceSelfMuteEvent((JDA)getJDA(), this.responseNumber, (Member)member));
/*     */     } 
/* 120 */     if (selfDeafened != vState.isSelfDeafened()) {
/*     */       
/* 122 */       vState.setSelfDeafened(selfDeafened);
/* 123 */       getJDA().getEntityBuilder().updateMemberCache(member);
/* 124 */       getJDA().handleEvent((GenericEvent)new GuildVoiceSelfDeafenEvent((JDA)getJDA(), this.responseNumber, (Member)member));
/*     */     } 
/* 126 */     if (guildMuted != vState.isGuildMuted()) {
/*     */       
/* 128 */       vState.setGuildMuted(guildMuted);
/* 129 */       getJDA().getEntityBuilder().updateMemberCache(member);
/* 130 */       getJDA().handleEvent((GenericEvent)new GuildVoiceGuildMuteEvent((JDA)getJDA(), this.responseNumber, (Member)member));
/*     */     } 
/* 132 */     if (guildDeafened != vState.isGuildDeafened()) {
/*     */       
/* 134 */       vState.setGuildDeafened(guildDeafened);
/* 135 */       getJDA().getEntityBuilder().updateMemberCache(member);
/* 136 */       getJDA().handleEvent((GenericEvent)new GuildVoiceGuildDeafenEvent((JDA)getJDA(), this.responseNumber, (Member)member));
/*     */     } 
/* 138 */     if (suppressed != vState.isSuppressed()) {
/*     */       
/* 140 */       vState.setSuppressed(suppressed);
/* 141 */       getJDA().getEntityBuilder().updateMemberCache(member);
/* 142 */       getJDA().handleEvent((GenericEvent)new GuildVoiceSuppressEvent((JDA)getJDA(), this.responseNumber, (Member)member));
/*     */     } 
/* 144 */     if (stream != vState.isStream()) {
/*     */       
/* 146 */       vState.setStream(stream);
/* 147 */       getJDA().getEntityBuilder().updateMemberCache(member);
/* 148 */       getJDA().handleEvent((GenericEvent)new GuildVoiceStreamEvent((JDA)getJDA(), this.responseNumber, (Member)member, stream));
/*     */     } 
/* 150 */     if (video != vState.isSendingVideo()) {
/*     */       
/* 152 */       vState.setVideo(video);
/* 153 */       getJDA().getEntityBuilder().updateMemberCache(member);
/* 154 */       getJDA().handleEvent((GenericEvent)new GuildVoiceVideoEvent((JDA)getJDA(), this.responseNumber, (Member)member, video));
/*     */     } 
/* 156 */     if (wasMute != vState.isMuted())
/* 157 */       getJDA().handleEvent((GenericEvent)new GuildVoiceMuteEvent((JDA)getJDA(), this.responseNumber, (Member)member)); 
/* 158 */     if (wasDeaf != vState.isDeafened())
/* 159 */       getJDA().handleEvent((GenericEvent)new GuildVoiceDeafenEvent((JDA)getJDA(), this.responseNumber, (Member)member)); 
/* 160 */     if (requestToSpeakTimestamp != vState.getRequestToSpeak()) {
/*     */       
/* 162 */       OffsetDateTime oldRequestToSpeak = vState.getRequestToSpeakTimestamp();
/* 163 */       vState.setRequestToSpeak(requestToSpeakTime);
/* 164 */       getJDA().handleEvent((GenericEvent)new GuildVoiceRequestToSpeakEvent((JDA)getJDA(), this.responseNumber, (Member)member, oldRequestToSpeak, requestToSpeakTime));
/*     */     } 
/*     */     
/* 167 */     if (!Objects.equals(channel, vState.getChannel())) {
/*     */       
/* 169 */       VoiceChannelImpl oldChannel = (VoiceChannelImpl)vState.getChannel();
/* 170 */       vState.setConnectedChannel((VoiceChannel)channel);
/*     */       
/* 172 */       if (oldChannel == null) {
/*     */         
/* 174 */         channel.getConnectedMembersMap().put(userId, member);
/* 175 */         getJDA().getEntityBuilder().updateMemberCache(member);
/* 176 */         getJDA().handleEvent((GenericEvent)new GuildVoiceJoinEvent((JDA)
/*     */               
/* 178 */               getJDA(), this.responseNumber, (Member)member));
/*     */       
/*     */       }
/* 181 */       else if (channel == null) {
/*     */         
/* 183 */         oldChannel.getConnectedMembersMap().remove(userId);
/* 184 */         if (isSelf)
/* 185 */           getJDA().getDirectAudioController().update(guild, null); 
/* 186 */         getJDA().getEntityBuilder().updateMemberCache(member, memberJson.isNull("joined_at"));
/* 187 */         getJDA().handleEvent((GenericEvent)new GuildVoiceLeaveEvent((JDA)
/*     */               
/* 189 */               getJDA(), this.responseNumber, (Member)member, (VoiceChannel)oldChannel));
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 194 */         AudioManagerImpl mng = (AudioManagerImpl)getJDA().getAudioManagersView().get(guildId);
/*     */         
/* 196 */         if (isSelf && mng != null && voiceInterceptor == null) {
/*     */ 
/*     */ 
/*     */           
/* 200 */           if (mng.isConnected()) {
/* 201 */             mng.setConnectedChannel((VoiceChannel)channel);
/*     */           }
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 207 */           if (mng.isConnected()) {
/* 208 */             getJDA().getDirectAudioController().update(guild, (VoiceChannel)channel);
/*     */           }
/*     */         } 
/*     */         
/* 212 */         channel.getConnectedMembersMap().put(userId, member);
/* 213 */         oldChannel.getConnectedMembersMap().remove(userId);
/* 214 */         getJDA().getEntityBuilder().updateMemberCache(member);
/* 215 */         getJDA().handleEvent((GenericEvent)new GuildVoiceMoveEvent((JDA)
/*     */               
/* 217 */               getJDA(), this.responseNumber, (Member)member, (VoiceChannel)oldChannel));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 222 */     if (isSelf && voiceInterceptor != null)
/*     */     {
/* 224 */       if (voiceInterceptor.onVoiceStateUpdate(new VoiceDispatchInterceptor.VoiceStateUpdate((VoiceChannel)channel, (GuildVoiceState)vState, this.allContent))) {
/* 225 */         getJDA().getDirectAudioController().update(guild, (VoiceChannel)channel);
/*     */       }
/*     */     }
/* 228 */     ((GuildImpl)guild).updateRequestToSpeak();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\VoiceStateUpdateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */