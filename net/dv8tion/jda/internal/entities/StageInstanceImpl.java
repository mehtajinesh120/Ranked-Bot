/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.StageChannel;
/*     */ import net.dv8tion.jda.api.entities.StageInstance;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.managers.StageInstanceManager;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.managers.StageInstanceManagerImpl;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
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
/*     */ public class StageInstanceImpl
/*     */   implements StageInstance
/*     */ {
/*     */   private final long id;
/*     */   private StageChannel channel;
/*     */   private StageInstanceManager manager;
/*     */   private String topic;
/*     */   private StageInstance.PrivacyLevel privacyLevel;
/*     */   private boolean discoverable;
/*     */   
/*     */   public StageInstanceImpl(long id, StageChannel channel) {
/*  47 */     this.id = id;
/*  48 */     this.channel = channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/*  54 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/*  61 */     return getChannel().getGuild();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public StageChannel getChannel() {
/*  68 */     StageChannel real = this.channel.getJDA().getStageChannelById(this.channel.getIdLong());
/*  69 */     if (real != null)
/*  70 */       this.channel = real; 
/*  71 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getTopic() {
/*  78 */     return this.topic;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public StageInstance.PrivacyLevel getPrivacyLevel() {
/*  85 */     return this.privacyLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDiscoverable() {
/*  91 */     return this.discoverable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> delete() {
/*  98 */     checkPermissions();
/*  99 */     Route.CompiledRoute route = Route.StageInstances.DELETE_INSTANCE.compile(new String[] { this.channel.getId() });
/* 100 */     return (RestAction<Void>)new RestActionImpl(this.channel.getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> requestToSpeak() {
/* 107 */     Guild guild = getGuild();
/* 108 */     Route.CompiledRoute route = Route.Guilds.UPDATE_VOICE_STATE.compile(new String[] { guild.getId(), "@me" });
/* 109 */     DataObject body = DataObject.empty().put("channel_id", this.channel.getId());
/*     */     
/* 111 */     if (guild.getSelfMember().hasPermission((GuildChannel)getChannel(), new Permission[] { Permission.VOICE_MUTE_OTHERS })) {
/* 112 */       body.putNull("request_to_speak_timestamp").put("suppress", Boolean.valueOf(false));
/*     */     } else {
/* 114 */       body.put("request_to_speak_timestamp", OffsetDateTime.now().toString());
/*     */     } 
/* 116 */     if (!this.channel.equals(guild.getSelfMember().getVoiceState().getChannel()))
/* 117 */       throw new IllegalStateException("Cannot request to speak without being connected to the stage channel!"); 
/* 118 */     return (RestAction<Void>)new RestActionImpl(this.channel.getJDA(), route, body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> cancelRequestToSpeak() {
/* 125 */     Guild guild = getGuild();
/* 126 */     Route.CompiledRoute route = Route.Guilds.UPDATE_VOICE_STATE.compile(new String[] { guild.getId(), "@me" });
/*     */ 
/*     */ 
/*     */     
/* 130 */     DataObject body = DataObject.empty().putNull("request_to_speak_timestamp").put("suppress", Boolean.valueOf(true)).put("channel_id", this.channel.getId());
/*     */     
/* 132 */     if (!this.channel.equals(guild.getSelfMember().getVoiceState().getChannel()))
/* 133 */       throw new IllegalStateException("Cannot cancel request to speak without being connected to the stage channel!"); 
/* 134 */     return (RestAction<Void>)new RestActionImpl(this.channel.getJDA(), route, body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public StageInstanceManager getManager() {
/* 141 */     checkPermissions();
/* 142 */     if (this.manager == null)
/* 143 */       this.manager = (StageInstanceManager)new StageInstanceManagerImpl(this); 
/* 144 */     return this.manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public StageInstanceImpl setTopic(String topic) {
/* 149 */     this.topic = topic;
/* 150 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public StageInstanceImpl setPrivacyLevel(StageInstance.PrivacyLevel privacyLevel) {
/* 155 */     this.privacyLevel = privacyLevel;
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public StageInstanceImpl setDiscoverable(boolean discoverable) {
/* 161 */     this.discoverable = discoverable;
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkPermissions() {
/* 167 */     EnumSet<Permission> permissions = getGuild().getSelfMember().getPermissions((GuildChannel)getChannel());
/* 168 */     EnumSet<Permission> required = EnumSet.of(Permission.MANAGE_CHANNEL, Permission.VOICE_MUTE_OTHERS, Permission.VOICE_MOVE_OTHERS);
/* 169 */     for (Permission perm : required) {
/*     */       
/* 171 */       if (!permissions.contains(perm))
/* 172 */         throw new InsufficientPermissionException(getChannel(), perm, "You must be a stage moderator to manage a stage instance! Missing Permission: " + perm); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\StageInstanceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */