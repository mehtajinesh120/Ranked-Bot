/*     */ package net.dv8tion.jda.internal.handle;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkChannelEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateAfkTimeoutEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateBannerEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostCountEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateBoostTierEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateCommunityUpdatesChannelEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateExplicitContentLevelEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateFeaturesEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateIconEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateLocaleEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateMaxMembersEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateMaxPresencesEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateNSFWLevelEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateNotificationLevelEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateRegionEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateRulesChannelEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateSplashEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateSystemChannelEvent;
/*     */ import net.dv8tion.jda.api.events.guild.update.GuildUpdateVanityCodeEvent;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*     */ 
/*     */ public class GuildUpdateHandler extends SocketHandler {
/*     */   public GuildUpdateHandler(JDAImpl api) {
/*  41 */     super(api);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Long handleInternally(DataObject content) {
/*     */     Set<String> features;
/*  47 */     long id = content.getLong("id");
/*  48 */     if (getJDA().getGuildSetupController().isLocked(id)) {
/*  49 */       return Long.valueOf(id);
/*     */     }
/*  51 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(id);
/*  52 */     if (guild == null) {
/*     */       
/*  54 */       EventCache.LOG.debug("Caching GUILD_UPDATE for guild with id: {}", Long.valueOf(id));
/*  55 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, id, this.responseNumber, this.allContent, this::handle);
/*  56 */       return null;
/*     */     } 
/*     */ 
/*     */     
/*  60 */     int maxMembers = content.getInt("max_members", 0);
/*  61 */     int maxPresences = content.getInt("max_presences", 5000);
/*  62 */     if (guild.getMaxMembers() == 0) {
/*     */ 
/*     */       
/*  65 */       guild.setMaxPresences(maxPresences);
/*  66 */       guild.setMaxMembers(maxMembers);
/*     */     } 
/*     */     
/*  69 */     long ownerId = content.getLong("owner_id");
/*  70 */     int boostCount = content.getInt("premium_subscription_count", 0);
/*  71 */     int boostTier = content.getInt("premium_tier", 0);
/*  72 */     String description = content.getString("description", null);
/*  73 */     String vanityCode = content.getString("vanity_url_code", null);
/*  74 */     String bannerId = content.getString("banner", null);
/*  75 */     String name = content.getString("name");
/*  76 */     String iconId = content.getString("icon", null);
/*  77 */     String splashId = content.getString("splash", null);
/*  78 */     String region = content.getString("region");
/*  79 */     Guild.VerificationLevel verificationLevel = Guild.VerificationLevel.fromKey(content.getInt("verification_level"));
/*  80 */     Guild.NotificationLevel notificationLevel = Guild.NotificationLevel.fromKey(content.getInt("default_message_notifications"));
/*  81 */     Guild.MFALevel mfaLevel = Guild.MFALevel.fromKey(content.getInt("mfa_level"));
/*  82 */     Guild.NSFWLevel nsfwLevel = Guild.NSFWLevel.fromKey(content.getInt("nsfw_level", -1));
/*  83 */     Guild.ExplicitContentLevel explicitContentLevel = Guild.ExplicitContentLevel.fromKey(content.getInt("explicit_content_filter"));
/*  84 */     Guild.Timeout afkTimeout = Guild.Timeout.fromKey(content.getInt("afk_timeout"));
/*  85 */     Locale locale = Locale.forLanguageTag(content.getString("preferred_locale"));
/*     */     
/*  87 */     VoiceChannel afkChannel = content.isNull("afk_channel_id") ? null : (VoiceChannel)guild.getVoiceChannelsView().get(content.getLong("afk_channel_id"));
/*     */     
/*  89 */     TextChannel systemChannel = content.isNull("system_channel_id") ? null : (TextChannel)guild.getTextChannelsView().get(content.getLong("system_channel_id"));
/*     */     
/*  91 */     TextChannel rulesChannel = content.isNull("rules_channel_id") ? null : (TextChannel)guild.getTextChannelsView().get(content.getLong("rules_channel_id"));
/*     */     
/*  93 */     TextChannel communityUpdatesChannel = content.isNull("public_updates_channel_id") ? null : (TextChannel)guild.getTextChannelsView().get(content.getLong("public_updates_channel_id"));
/*     */     
/*  95 */     if (!content.isNull("features")) {
/*     */       
/*  97 */       DataArray featureArr = content.getArray("features");
/*  98 */       features = (Set<String>)StreamSupport.stream(featureArr.spliterator(), false).map(String::valueOf).collect(Collectors.toSet());
/*     */     }
/*     */     else {
/*     */       
/* 102 */       features = Collections.emptySet();
/*     */     } 
/*     */     
/* 105 */     if (ownerId != guild.getOwnerIdLong()) {
/*     */       
/* 107 */       long oldOwnerId = guild.getOwnerIdLong();
/* 108 */       Member oldOwner = guild.getOwner();
/* 109 */       Member newOwner = (Member)guild.getMembersView().get(ownerId);
/* 110 */       if (newOwner == null)
/* 111 */         WebSocketClient.LOG.debug("Received {} with owner not in cache. UserId: {} GuildId: {}", new Object[] { this.allContent.get("t"), Long.valueOf(ownerId), Long.valueOf(id) }); 
/* 112 */       guild.setOwner(newOwner);
/* 113 */       guild.setOwnerId(ownerId);
/* 114 */       getJDA().handleEvent((GenericEvent)new GuildUpdateOwnerEvent((JDA)
/*     */             
/* 116 */             getJDA(), this.responseNumber, (Guild)guild, oldOwner, oldOwnerId, ownerId));
/*     */     } 
/*     */ 
/*     */     
/* 120 */     if (!Objects.equals(description, guild.getDescription())) {
/*     */       
/* 122 */       String oldDescription = guild.getDescription();
/* 123 */       guild.setDescription(description);
/* 124 */       getJDA().handleEvent((GenericEvent)new GuildUpdateDescriptionEvent((JDA)
/*     */             
/* 126 */             getJDA(), this.responseNumber, (Guild)guild, oldDescription));
/*     */     } 
/*     */     
/* 129 */     if (!Objects.equals(bannerId, guild.getBannerId())) {
/*     */       
/* 131 */       String oldBanner = guild.getBannerId();
/* 132 */       guild.setBannerId(bannerId);
/* 133 */       getJDA().handleEvent((GenericEvent)new GuildUpdateBannerEvent((JDA)
/*     */             
/* 135 */             getJDA(), this.responseNumber, (Guild)guild, oldBanner));
/*     */     } 
/*     */     
/* 138 */     if (!Objects.equals(vanityCode, guild.getVanityCode())) {
/*     */       
/* 140 */       String oldCode = guild.getVanityCode();
/* 141 */       guild.setVanityCode(vanityCode);
/* 142 */       getJDA().handleEvent((GenericEvent)new GuildUpdateVanityCodeEvent((JDA)
/*     */             
/* 144 */             getJDA(), this.responseNumber, (Guild)guild, oldCode));
/*     */     } 
/*     */     
/* 147 */     if (maxMembers != guild.getMaxMembers()) {
/*     */       
/* 149 */       int oldMax = guild.getMaxMembers();
/* 150 */       guild.setMaxMembers(maxMembers);
/* 151 */       getJDA().handleEvent((GenericEvent)new GuildUpdateMaxMembersEvent((JDA)
/*     */             
/* 153 */             getJDA(), this.responseNumber, (Guild)guild, oldMax));
/*     */     } 
/*     */     
/* 156 */     if (maxPresences != guild.getMaxPresences()) {
/*     */       
/* 158 */       int oldMax = guild.getMaxPresences();
/* 159 */       guild.setMaxPresences(maxPresences);
/* 160 */       getJDA().handleEvent((GenericEvent)new GuildUpdateMaxPresencesEvent((JDA)
/*     */             
/* 162 */             getJDA(), this.responseNumber, (Guild)guild, oldMax));
/*     */     } 
/*     */     
/* 165 */     if (boostCount != guild.getBoostCount()) {
/*     */       
/* 167 */       int oldCount = guild.getBoostCount();
/* 168 */       guild.setBoostCount(boostCount);
/* 169 */       getJDA().handleEvent((GenericEvent)new GuildUpdateBoostCountEvent((JDA)
/*     */             
/* 171 */             getJDA(), this.responseNumber, (Guild)guild, oldCount));
/*     */     } 
/*     */     
/* 174 */     if (Guild.BoostTier.fromKey(boostTier) != guild.getBoostTier()) {
/*     */       
/* 176 */       Guild.BoostTier oldTier = guild.getBoostTier();
/* 177 */       guild.setBoostTier(boostTier);
/* 178 */       getJDA().handleEvent((GenericEvent)new GuildUpdateBoostTierEvent((JDA)
/*     */             
/* 180 */             getJDA(), this.responseNumber, (Guild)guild, oldTier));
/*     */     } 
/*     */     
/* 183 */     if (!Objects.equals(name, guild.getName())) {
/*     */       
/* 185 */       String oldName = guild.getName();
/* 186 */       guild.setName(name);
/* 187 */       getJDA().handleEvent((GenericEvent)new GuildUpdateNameEvent((JDA)
/*     */             
/* 189 */             getJDA(), this.responseNumber, (Guild)guild, oldName));
/*     */     } 
/*     */     
/* 192 */     if (!Objects.equals(iconId, guild.getIconId())) {
/*     */       
/* 194 */       String oldIconId = guild.getIconId();
/* 195 */       guild.setIconId(iconId);
/* 196 */       getJDA().handleEvent((GenericEvent)new GuildUpdateIconEvent((JDA)
/*     */             
/* 198 */             getJDA(), this.responseNumber, (Guild)guild, oldIconId));
/*     */     } 
/*     */     
/* 201 */     if (!features.equals(guild.getFeatures())) {
/*     */       
/* 203 */       Set<String> oldFeatures = guild.getFeatures();
/* 204 */       guild.setFeatures(features);
/* 205 */       getJDA().handleEvent((GenericEvent)new GuildUpdateFeaturesEvent((JDA)
/*     */             
/* 207 */             getJDA(), this.responseNumber, (Guild)guild, oldFeatures));
/*     */     } 
/*     */     
/* 210 */     if (!Objects.equals(splashId, guild.getSplashId())) {
/*     */       
/* 212 */       String oldSplashId = guild.getSplashId();
/* 213 */       guild.setSplashId(splashId);
/* 214 */       getJDA().handleEvent((GenericEvent)new GuildUpdateSplashEvent((JDA)
/*     */             
/* 216 */             getJDA(), this.responseNumber, (Guild)guild, oldSplashId));
/*     */     } 
/*     */     
/* 219 */     if (!Objects.equals(region, guild.getRegionRaw())) {
/*     */       
/* 221 */       String oldRegion = guild.getRegionRaw();
/* 222 */       guild.setRegion(region);
/* 223 */       getJDA().handleEvent((GenericEvent)new GuildUpdateRegionEvent((JDA)
/*     */             
/* 225 */             getJDA(), this.responseNumber, (Guild)guild, oldRegion));
/*     */     } 
/*     */     
/* 228 */     if (!Objects.equals(verificationLevel, guild.getVerificationLevel())) {
/*     */       
/* 230 */       Guild.VerificationLevel oldVerificationLevel = guild.getVerificationLevel();
/* 231 */       guild.setVerificationLevel(verificationLevel);
/* 232 */       getJDA().handleEvent((GenericEvent)new GuildUpdateVerificationLevelEvent((JDA)
/*     */             
/* 234 */             getJDA(), this.responseNumber, (Guild)guild, oldVerificationLevel));
/*     */     } 
/*     */     
/* 237 */     if (!Objects.equals(notificationLevel, guild.getDefaultNotificationLevel())) {
/*     */       
/* 239 */       Guild.NotificationLevel oldNotificationLevel = guild.getDefaultNotificationLevel();
/* 240 */       guild.setDefaultNotificationLevel(notificationLevel);
/* 241 */       getJDA().handleEvent((GenericEvent)new GuildUpdateNotificationLevelEvent((JDA)
/*     */             
/* 243 */             getJDA(), this.responseNumber, (Guild)guild, oldNotificationLevel));
/*     */     } 
/*     */     
/* 246 */     if (!Objects.equals(mfaLevel, guild.getRequiredMFALevel())) {
/*     */       
/* 248 */       Guild.MFALevel oldMfaLevel = guild.getRequiredMFALevel();
/* 249 */       guild.setRequiredMFALevel(mfaLevel);
/* 250 */       getJDA().handleEvent((GenericEvent)new GuildUpdateMFALevelEvent((JDA)
/*     */             
/* 252 */             getJDA(), this.responseNumber, (Guild)guild, oldMfaLevel));
/*     */     } 
/*     */     
/* 255 */     if (!Objects.equals(explicitContentLevel, guild.getExplicitContentLevel())) {
/*     */       
/* 257 */       Guild.ExplicitContentLevel oldExplicitContentLevel = guild.getExplicitContentLevel();
/* 258 */       guild.setExplicitContentLevel(explicitContentLevel);
/* 259 */       getJDA().handleEvent((GenericEvent)new GuildUpdateExplicitContentLevelEvent((JDA)
/*     */             
/* 261 */             getJDA(), this.responseNumber, (Guild)guild, oldExplicitContentLevel));
/*     */     } 
/*     */     
/* 264 */     if (!Objects.equals(afkTimeout, guild.getAfkTimeout())) {
/*     */       
/* 266 */       Guild.Timeout oldAfkTimeout = guild.getAfkTimeout();
/* 267 */       guild.setAfkTimeout(afkTimeout);
/* 268 */       getJDA().handleEvent((GenericEvent)new GuildUpdateAfkTimeoutEvent((JDA)
/*     */             
/* 270 */             getJDA(), this.responseNumber, (Guild)guild, oldAfkTimeout));
/*     */     } 
/*     */     
/* 273 */     if (!Objects.equals(locale, guild.getLocale())) {
/*     */       
/* 275 */       Locale oldLocale = guild.getLocale();
/* 276 */       guild.setLocale(locale.toLanguageTag());
/* 277 */       getJDA().handleEvent((GenericEvent)new GuildUpdateLocaleEvent((JDA)
/*     */             
/* 279 */             getJDA(), this.responseNumber, (Guild)guild, oldLocale));
/*     */     } 
/*     */     
/* 282 */     if (!Objects.equals(afkChannel, guild.getAfkChannel())) {
/*     */       
/* 284 */       VoiceChannel oldAfkChannel = guild.getAfkChannel();
/* 285 */       guild.setAfkChannel(afkChannel);
/* 286 */       getJDA().handleEvent((GenericEvent)new GuildUpdateAfkChannelEvent((JDA)
/*     */             
/* 288 */             getJDA(), this.responseNumber, (Guild)guild, oldAfkChannel));
/*     */     } 
/*     */     
/* 291 */     if (!Objects.equals(systemChannel, guild.getSystemChannel())) {
/*     */       
/* 293 */       TextChannel oldSystemChannel = guild.getSystemChannel();
/* 294 */       guild.setSystemChannel(systemChannel);
/* 295 */       getJDA().handleEvent((GenericEvent)new GuildUpdateSystemChannelEvent((JDA)
/*     */             
/* 297 */             getJDA(), this.responseNumber, (Guild)guild, oldSystemChannel));
/*     */     } 
/*     */     
/* 300 */     if (!Objects.equals(rulesChannel, guild.getRulesChannel())) {
/*     */       
/* 302 */       TextChannel oldRulesChannel = guild.getRulesChannel();
/* 303 */       guild.setRulesChannel(rulesChannel);
/* 304 */       getJDA().handleEvent((GenericEvent)new GuildUpdateRulesChannelEvent((JDA)
/*     */             
/* 306 */             getJDA(), this.responseNumber, (Guild)guild, oldRulesChannel));
/*     */     } 
/*     */     
/* 309 */     if (!Objects.equals(communityUpdatesChannel, guild.getCommunityUpdatesChannel())) {
/*     */       
/* 311 */       TextChannel oldCommunityUpdatesChannel = guild.getCommunityUpdatesChannel();
/* 312 */       guild.setCommunityUpdatesChannel(communityUpdatesChannel);
/* 313 */       getJDA().handleEvent((GenericEvent)new GuildUpdateCommunityUpdatesChannelEvent((JDA)
/*     */             
/* 315 */             getJDA(), this.responseNumber, (Guild)guild, oldCommunityUpdatesChannel));
/*     */     } 
/*     */     
/* 318 */     if (content.hasKey("nsfw_level") && nsfwLevel != guild.getNSFWLevel()) {
/*     */       
/* 320 */       Guild.NSFWLevel oldNSFWLevel = guild.getNSFWLevel();
/* 321 */       guild.setNSFWLevel(nsfwLevel);
/* 322 */       getJDA().handleEvent((GenericEvent)new GuildUpdateNSFWLevelEvent((JDA)
/*     */             
/* 324 */             getJDA(), this.responseNumber, (Guild)guild, oldNSFWLevel));
/*     */     } 
/*     */     
/* 327 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildUpdateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */