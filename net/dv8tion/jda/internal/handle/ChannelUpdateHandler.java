/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import gnu.trove.map.hash.TLongObjectHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.StoreChannel;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdateNameEvent;
/*     */ import net.dv8tion.jda.api.events.channel.category.update.CategoryUpdatePermissionsEvent;
/*     */ import net.dv8tion.jda.api.events.channel.store.update.StoreChannelUpdateNameEvent;
/*     */ import net.dv8tion.jda.api.events.channel.store.update.StoreChannelUpdatePositionEvent;
/*     */ import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateNewsEvent;
/*     */ import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateParentEvent;
/*     */ import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdatePermissionsEvent;
/*     */ import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateSlowmodeEvent;
/*     */ import net.dv8tion.jda.api.events.channel.text.update.TextChannelUpdateTopicEvent;
/*     */ import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdatePermissionsEvent;
/*     */ import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateRegionEvent;
/*     */ import net.dv8tion.jda.api.events.channel.voice.update.VoiceChannelUpdateUserLimitEvent;
/*     */ import net.dv8tion.jda.api.events.guild.override.PermissionOverrideCreateEvent;
/*     */ import net.dv8tion.jda.api.events.guild.override.PermissionOverrideDeleteEvent;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.AbstractChannelImpl;
/*     */ import net.dv8tion.jda.internal.entities.CategoryImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.entities.PermissionOverrideImpl;
/*     */ import net.dv8tion.jda.internal.entities.StoreChannelImpl;
/*     */ import net.dv8tion.jda.internal.entities.TextChannelImpl;
/*     */ import net.dv8tion.jda.internal.entities.VoiceChannelImpl;
/*     */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*     */ 
/*     */ public class ChannelUpdateHandler extends SocketHandler {
/*  48 */   public ChannelUpdateHandler(JDAImpl api) { super(api); } protected Long handleInternally(DataObject content) { StoreChannelImpl storeChannel; String topic; VoiceChannelImpl voiceChannel; CategoryImpl category; String str1; TextChannelImpl textChannel; int userLimit; String oldName; int i; Category parent; int bitrate, oldPosition; Long oldParent; String region, str2; Category category1; String oldTopic;
/*     */     Long long_1;
/*     */     int j;
/*     */     String str3;
/*     */     boolean oldNsfw;
/*     */     String oldRegion;
/*  54 */     int oldSlowmode, k, oldLimit, oldBitrate, rawType = content.getInt("type");
/*  55 */     boolean news = (rawType == 5);
/*  56 */     ChannelType type = ChannelType.fromId(rawType);
/*  57 */     if (type == ChannelType.GROUP) {
/*     */       
/*  59 */       WebSocketClient.LOG.warn("Ignoring CHANNEL_UPDATE for a group which we don't support");
/*  60 */       return null;
/*     */     } 
/*     */     
/*  63 */     long channelId = content.getLong("id");
/*  64 */     Long parentId = content.isNull("parent_id") ? null : Long.valueOf(content.getLong("parent_id"));
/*  65 */     int position = content.getInt("position");
/*  66 */     String name = content.getString("name");
/*  67 */     boolean nsfw = content.getBoolean("nsfw");
/*  68 */     int slowmode = content.getInt("rate_limit_per_user", 0);
/*  69 */     DataArray permOverwrites = content.getArray("permission_overwrites");
/*  70 */     switch (type)
/*     */     
/*     */     { 
/*     */       case STORE:
/*  74 */         storeChannel = (StoreChannelImpl)getJDA().getStoreChannelById(channelId);
/*  75 */         if (storeChannel == null) {
/*     */           
/*  77 */           getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle);
/*  78 */           EventCache.LOG.debug("CHANNEL_UPDATE attempted to update a StoreChannel that does not exist. JSON: {}", content);
/*  79 */           return null;
/*     */         } 
/*  81 */         str1 = storeChannel.getName();
/*  82 */         i = storeChannel.getPositionRaw();
/*     */         
/*  84 */         if (!Objects.equals(str1, name)) {
/*     */           
/*  86 */           storeChannel.setName(name);
/*  87 */           getJDA().handleEvent((GenericEvent)new StoreChannelUpdateNameEvent((JDA)
/*     */                 
/*  89 */                 getJDA(), this.responseNumber, (StoreChannel)storeChannel, str1));
/*     */         } 
/*     */         
/*  92 */         if (!Objects.equals(Integer.valueOf(i), Integer.valueOf(position))) {
/*     */           
/*  94 */           storeChannel.setPosition(position);
/*  95 */           getJDA().handleEvent((GenericEvent)new StoreChannelUpdatePositionEvent((JDA)
/*     */                 
/*  97 */                 getJDA(), this.responseNumber, (StoreChannel)storeChannel, i));
/*     */         } 
/*     */ 
/*     */         
/* 101 */         applyPermissions((AbstractChannelImpl<?, ?>)storeChannel, permOverwrites);
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
/*     */ 
/*     */ 
/*     */         
/* 294 */         return null;case TEXT: topic = content.getString("topic", null); textChannel = (TextChannelImpl)getJDA().getTextChannelsView().get(channelId); if (textChannel == null) { getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle); EventCache.LOG.debug("CHANNEL_UPDATE attempted to update a TextChannel that does not exist. JSON: {}", content); return null; }  parent = textChannel.getParent(); oldParent = (parent == null) ? null : Long.valueOf(parent.getIdLong()); str2 = textChannel.getName(); oldTopic = textChannel.getTopic(); j = textChannel.getPositionRaw(); oldNsfw = textChannel.isNSFW(); oldSlowmode = textChannel.getSlowmode(); if (!Objects.equals(str2, name)) { textChannel.setName(name); getJDA().handleEvent((GenericEvent)new TextChannelUpdateNameEvent((JDA)getJDA(), this.responseNumber, (TextChannel)textChannel, str2)); }  if (!Objects.equals(oldParent, parentId)) { textChannel.setParent((parentId == null) ? 0L : parentId.longValue()); getJDA().handleEvent((GenericEvent)new TextChannelUpdateParentEvent((JDA)getJDA(), this.responseNumber, (TextChannel)textChannel, parent)); }  if (!Objects.equals(oldTopic, topic)) { textChannel.setTopic(topic); getJDA().handleEvent((GenericEvent)new TextChannelUpdateTopicEvent((JDA)getJDA(), this.responseNumber, (TextChannel)textChannel, oldTopic)); }  if (j != position) { textChannel.setPosition(position); getJDA().handleEvent((GenericEvent)new TextChannelUpdatePositionEvent((JDA)getJDA(), this.responseNumber, (TextChannel)textChannel, j)); }  if (oldNsfw != nsfw) { textChannel.setNSFW(nsfw); getJDA().handleEvent((GenericEvent)new TextChannelUpdateNSFWEvent((JDA)getJDA(), this.responseNumber, (TextChannel)textChannel, oldNsfw)); }  if (oldSlowmode != slowmode) { textChannel.setSlowmode(slowmode); getJDA().handleEvent((GenericEvent)new TextChannelUpdateSlowmodeEvent((JDA)getJDA(), this.responseNumber, (TextChannel)textChannel, oldSlowmode)); }  if (news != textChannel.isNews()) { textChannel.setNews(news); getJDA().handleEvent((GenericEvent)new TextChannelUpdateNewsEvent((JDA)getJDA(), this.responseNumber, (TextChannel)textChannel)); }  applyPermissions((AbstractChannelImpl<?, ?>)textChannel, permOverwrites); return null;case STAGE: case VOICE: voiceChannel = (VoiceChannelImpl)getJDA().getVoiceChannelsView().get(channelId); userLimit = content.getInt("user_limit"); bitrate = content.getInt("bitrate"); region = content.getString("rtc_region", null); if (voiceChannel == null) { getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle); EventCache.LOG.debug("CHANNEL_UPDATE attempted to update a VoiceChannel that does not exist. JSON: {}", content); return null; }  category1 = voiceChannel.getParent(); long_1 = (category1 == null) ? null : Long.valueOf(category1.getIdLong()); str3 = voiceChannel.getName(); oldRegion = voiceChannel.getRegionRaw(); k = voiceChannel.getPositionRaw(); oldLimit = voiceChannel.getUserLimit(); oldBitrate = voiceChannel.getBitrate(); if (!Objects.equals(str3, name)) { voiceChannel.setName(name); getJDA().handleEvent((GenericEvent)new VoiceChannelUpdateNameEvent((JDA)getJDA(), this.responseNumber, (VoiceChannel)voiceChannel, str3)); }  if (!Objects.equals(oldRegion, region)) { voiceChannel.setRegion(region); getJDA().handleEvent((GenericEvent)new VoiceChannelUpdateRegionEvent((JDA)getJDA(), this.responseNumber, (VoiceChannel)voiceChannel, oldRegion)); }  if (!Objects.equals(long_1, parentId)) { voiceChannel.setParent((parentId == null) ? 0L : parentId.longValue()); getJDA().handleEvent((GenericEvent)new VoiceChannelUpdateParentEvent((JDA)getJDA(), this.responseNumber, (VoiceChannel)voiceChannel, category1)); }  if (k != position) { voiceChannel.setPosition(position); getJDA().handleEvent((GenericEvent)new VoiceChannelUpdatePositionEvent((JDA)getJDA(), this.responseNumber, (VoiceChannel)voiceChannel, k)); }  if (oldLimit != userLimit) { voiceChannel.setUserLimit(userLimit); getJDA().handleEvent((GenericEvent)new VoiceChannelUpdateUserLimitEvent((JDA)getJDA(), this.responseNumber, (VoiceChannel)voiceChannel, oldLimit)); }  if (oldBitrate != bitrate) { voiceChannel.setBitrate(bitrate); getJDA().handleEvent((GenericEvent)new VoiceChannelUpdateBitrateEvent((JDA)getJDA(), this.responseNumber, (VoiceChannel)voiceChannel, oldBitrate)); }  applyPermissions((AbstractChannelImpl<?, ?>)voiceChannel, permOverwrites); return null;case CATEGORY: category = (CategoryImpl)getJDA().getCategoryById(channelId); if (category == null) { getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle); EventCache.LOG.debug("CHANNEL_UPDATE attempted to update a Category that does not exist. JSON: {}", content); return null; }  oldName = category.getName(); oldPosition = category.getPositionRaw(); if (!Objects.equals(oldName, name)) { category.setName(name); getJDA().handleEvent((GenericEvent)new CategoryUpdateNameEvent((JDA)getJDA(), this.responseNumber, (Category)category, oldName)); }  if (!Objects.equals(Integer.valueOf(oldPosition), Integer.valueOf(position))) { category.setPosition(position); getJDA().handleEvent((GenericEvent)new CategoryUpdatePositionEvent((JDA)getJDA(), this.responseNumber, (Category)category, oldPosition)); }  applyPermissions((AbstractChannelImpl<?, ?>)category, permOverwrites); return null; }  WebSocketClient.LOG.debug("CHANNEL_UPDATE provided an unrecognized channel type JSON: {}", content); return null; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void applyPermissions(AbstractChannelImpl<?, ?> channel, DataArray permOverwrites) {
/* 300 */     TLongObjectHashMap tLongObjectHashMap = new TLongObjectHashMap(channel.getOverrideMap());
/* 301 */     List<IPermissionHolder> changed = new ArrayList<>(tLongObjectHashMap.size());
/* 302 */     GuildImpl guildImpl = channel.getGuild();
/* 303 */     for (int i = 0; i < permOverwrites.length(); i++) {
/*     */       
/* 305 */       DataObject overrideJson = permOverwrites.getObject(i);
/* 306 */       long id = overrideJson.getUnsignedLong("id", 0L);
/* 307 */       if (handlePermissionOverride((PermissionOverride)tLongObjectHashMap.remove(id), overrideJson, id, channel)) {
/* 308 */         addPermissionHolder(changed, (Guild)guildImpl, id);
/*     */       }
/*     */     } 
/* 311 */     tLongObjectHashMap.forEachValue(override -> {
/*     */           channel.getOverrideMap().remove(override.getIdLong());
/*     */           
/*     */           addPermissionHolder(changed, guild, override.getIdLong());
/*     */           
/*     */           this.api.handleEvent((GenericEvent)new PermissionOverrideDeleteEvent((JDA)this.api, this.responseNumber, (GuildChannel)channel, override));
/*     */           
/*     */           return true;
/*     */         });
/*     */     
/* 321 */     if (changed.isEmpty())
/*     */       return; 
/* 323 */     switch (channel.getType()) {
/*     */       
/*     */       case CATEGORY:
/* 326 */         this.api.handleEvent((GenericEvent)new CategoryUpdatePermissionsEvent((JDA)this.api, this.responseNumber, (Category)channel, changed));
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case STORE:
/* 332 */         this.api.handleEvent((GenericEvent)new StoreChannelUpdatePermissionsEvent((JDA)this.api, this.responseNumber, (StoreChannel)channel, changed));
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case STAGE:
/*     */       case VOICE:
/* 339 */         this.api.handleEvent((GenericEvent)new VoiceChannelUpdatePermissionsEvent((JDA)this.api, this.responseNumber, (VoiceChannel)channel, changed));
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case TEXT:
/* 345 */         this.api.handleEvent((GenericEvent)new TextChannelUpdatePermissionsEvent((JDA)this.api, this.responseNumber, (TextChannel)channel, changed));
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addPermissionHolder(List<IPermissionHolder> changed, Guild guild, long id) {
/*     */     Member member;
/* 355 */     Role role = guild.getRoleById(id);
/* 356 */     if (role == null)
/* 357 */       member = guild.getMemberById(id); 
/* 358 */     if (member != null) {
/* 359 */       changed.add(member);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean handlePermissionOverride(PermissionOverride currentOverride, DataObject override, long overrideId, AbstractChannelImpl<?, ?> channel) {
/* 366 */     long allow = override.getLong("allow");
/* 367 */     long deny = override.getLong("deny");
/* 368 */     int type = override.getInt("type");
/* 369 */     boolean isRole = (type == 0);
/* 370 */     if (!isRole) {
/*     */       
/* 372 */       if (type != 1) {
/*     */         
/* 374 */         EntityBuilder.LOG.debug("Ignoring unknown invite of type '{}'. JSON: {}", Integer.valueOf(type), override);
/* 375 */         return false;
/*     */       } 
/* 377 */       if (!this.api.isCacheFlagSet(CacheFlag.MEMBER_OVERRIDES) && overrideId != this.api.getSelfUser().getIdLong())
/*     */       {
/* 379 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 383 */     if (currentOverride != null) {
/*     */       
/* 385 */       long oldAllow = currentOverride.getAllowedRaw();
/* 386 */       long oldDeny = currentOverride.getDeniedRaw();
/* 387 */       PermissionOverrideImpl impl = (PermissionOverrideImpl)currentOverride;
/* 388 */       if (oldAllow == allow && oldDeny == deny) {
/* 389 */         return false;
/*     */       }
/* 391 */       if (overrideId == channel.getGuild().getIdLong() && (allow | deny) == 0L) {
/*     */ 
/*     */         
/* 394 */         channel.getOverrideMap().remove(overrideId);
/* 395 */         this.api.handleEvent((GenericEvent)new PermissionOverrideDeleteEvent((JDA)this.api, this.responseNumber, (GuildChannel)channel, currentOverride));
/*     */ 
/*     */ 
/*     */         
/* 399 */         return true;
/*     */       } 
/*     */       
/* 402 */       impl.setAllow(allow);
/* 403 */       impl.setDeny(deny);
/* 404 */       this.api.handleEvent((GenericEvent)new PermissionOverrideUpdateEvent((JDA)this.api, this.responseNumber, (GuildChannel)channel, currentOverride, oldAllow, oldDeny));
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */       
/* 412 */       if (overrideId == channel.getGuild().getIdLong() && (allow | deny) == 0L) {
/* 413 */         return false;
/*     */       }
/* 415 */       PermissionOverrideImpl impl = new PermissionOverrideImpl((GuildChannel)channel, overrideId, isRole), permissionOverrideImpl1 = impl;
/* 416 */       impl.setAllow(allow);
/* 417 */       impl.setDeny(deny);
/* 418 */       channel.getOverrideMap().put(overrideId, permissionOverrideImpl1);
/* 419 */       this.api.handleEvent((GenericEvent)new PermissionOverrideCreateEvent((JDA)this.api, this.responseNumber, (GuildChannel)channel, (PermissionOverride)permissionOverrideImpl1));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 425 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\ChannelUpdateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */