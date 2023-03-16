/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.time.OffsetDateTime;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.GuildVoiceState;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.CompletedRestAction;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
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
/*     */ public class GuildVoiceStateImpl
/*     */   implements GuildVoiceState
/*     */ {
/*     */   private final JDA api;
/*     */   private Guild guild;
/*     */   private Member member;
/*     */   private VoiceChannel connectedChannel;
/*     */   private String sessionId;
/*     */   private long requestToSpeak;
/*     */   private boolean selfMuted = false;
/*     */   private boolean selfDeafened = false;
/*     */   private boolean guildMuted = false;
/*     */   private boolean guildDeafened = false;
/*     */   private boolean suppressed = false;
/*     */   private boolean stream = false;
/*     */   private boolean video = false;
/*     */   
/*     */   public GuildVoiceStateImpl(Member member) {
/*  52 */     this.api = member.getJDA();
/*  53 */     this.guild = member.getGuild();
/*  54 */     this.member = member;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelfMuted() {
/*  60 */     return this.selfMuted;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelfDeafened() {
/*  66 */     return this.selfDeafened;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  73 */     return this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSessionId() {
/*  79 */     return this.sessionId;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getRequestToSpeak() {
/*  84 */     return this.requestToSpeak;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public OffsetDateTime getRequestToSpeakTimestamp() {
/*  90 */     return (this.requestToSpeak == 0L) ? null : Helpers.toOffset(this.requestToSpeak);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> approveSpeaker() {
/*  97 */     return update(false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> declineSpeaker() {
/* 104 */     return update(true);
/*     */   }
/*     */ 
/*     */   
/*     */   private RestAction<Void> update(boolean suppress) {
/* 109 */     if (this.requestToSpeak == 0L || !(this.connectedChannel instanceof net.dv8tion.jda.api.entities.StageChannel))
/* 110 */       return (RestAction<Void>)new CompletedRestAction(this.api, null); 
/* 111 */     Member selfMember = getGuild().getSelfMember();
/* 112 */     boolean isSelf = selfMember.equals(this.member);
/* 113 */     if (!isSelf && !selfMember.hasPermission((GuildChannel)this.connectedChannel, new Permission[] { Permission.VOICE_MUTE_OTHERS })) {
/* 114 */       throw new InsufficientPermissionException(this.connectedChannel, Permission.VOICE_MUTE_OTHERS);
/*     */     }
/* 116 */     Route.CompiledRoute route = Route.Guilds.UPDATE_VOICE_STATE.compile(new String[] { this.guild.getId(), isSelf ? "@me" : getId() });
/*     */ 
/*     */     
/* 119 */     DataObject body = DataObject.empty().put("channel_id", this.connectedChannel.getId()).put("suppress", Boolean.valueOf(suppress));
/* 120 */     return (RestAction<Void>)new RestActionImpl(getJDA(), route, body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> inviteSpeaker() {
/* 127 */     if (!(this.connectedChannel instanceof net.dv8tion.jda.api.entities.StageChannel))
/* 128 */       return (RestAction<Void>)new CompletedRestAction(this.api, null); 
/* 129 */     if (!getGuild().getSelfMember().hasPermission((GuildChannel)this.connectedChannel, new Permission[] { Permission.VOICE_MUTE_OTHERS })) {
/* 130 */       throw new InsufficientPermissionException(this.connectedChannel, Permission.VOICE_MUTE_OTHERS);
/*     */     }
/* 132 */     Route.CompiledRoute route = Route.Guilds.UPDATE_VOICE_STATE.compile(new String[] { this.guild.getId(), getId() });
/*     */ 
/*     */ 
/*     */     
/* 136 */     DataObject body = DataObject.empty().put("channel_id", this.connectedChannel.getId()).put("suppress", Boolean.valueOf(false)).put("request_to_speak_timestamp", OffsetDateTime.now().toString());
/* 137 */     return (RestAction<Void>)new RestActionImpl(getJDA(), route, body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMuted() {
/* 143 */     return (isSelfMuted() || isGuildMuted());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDeafened() {
/* 149 */     return (isSelfDeafened() || isGuildDeafened());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGuildMuted() {
/* 155 */     return this.guildMuted;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGuildDeafened() {
/* 161 */     return this.guildDeafened;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuppressed() {
/* 167 */     return this.suppressed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStream() {
/* 173 */     return this.stream;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSendingVideo() {
/* 179 */     return this.video;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VoiceChannel getChannel() {
/* 185 */     return this.connectedChannel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/* 192 */     Guild realGuild = this.api.getGuildById(this.guild.getIdLong());
/* 193 */     if (realGuild != null)
/* 194 */       this.guild = realGuild; 
/* 195 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Member getMember() {
/* 202 */     Member realMember = getGuild().getMemberById(this.member.getIdLong());
/* 203 */     if (realMember != null)
/* 204 */       this.member = realMember; 
/* 205 */     return this.member;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inVoiceChannel() {
/* 211 */     return (getChannel() != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 217 */     return this.member.getIdLong();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 223 */     return this.member.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 229 */     if (obj == this)
/* 230 */       return true; 
/* 231 */     if (!(obj instanceof GuildVoiceState))
/* 232 */       return false; 
/* 233 */     GuildVoiceState oStatus = (GuildVoiceState)obj;
/* 234 */     return this.member.equals(oStatus.getMember());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 240 */     return "VS:" + getGuild().getName() + '(' + getId() + ')';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public GuildVoiceStateImpl setConnectedChannel(VoiceChannel connectedChannel) {
/* 247 */     this.connectedChannel = connectedChannel;
/* 248 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildVoiceStateImpl setSessionId(String sessionId) {
/* 253 */     this.sessionId = sessionId;
/* 254 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildVoiceStateImpl setSelfMuted(boolean selfMuted) {
/* 259 */     this.selfMuted = selfMuted;
/* 260 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildVoiceStateImpl setSelfDeafened(boolean selfDeafened) {
/* 265 */     this.selfDeafened = selfDeafened;
/* 266 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildVoiceStateImpl setGuildMuted(boolean guildMuted) {
/* 271 */     this.guildMuted = guildMuted;
/* 272 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildVoiceStateImpl setGuildDeafened(boolean guildDeafened) {
/* 277 */     this.guildDeafened = guildDeafened;
/* 278 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildVoiceStateImpl setSuppressed(boolean suppressed) {
/* 283 */     this.suppressed = suppressed;
/* 284 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildVoiceStateImpl setStream(boolean stream) {
/* 289 */     this.stream = stream;
/* 290 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildVoiceStateImpl setVideo(boolean video) {
/* 295 */     this.video = video;
/* 296 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuildVoiceStateImpl setRequestToSpeak(OffsetDateTime timestamp) {
/* 301 */     this.requestToSpeak = (timestamp == null) ? 0L : timestamp.toInstant().toEpochMilli();
/* 302 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\GuildVoiceStateImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */