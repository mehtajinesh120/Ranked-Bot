/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*     */ import net.dv8tion.jda.api.entities.StoreChannel;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.channel.category.CategoryDeleteEvent;
/*     */ import net.dv8tion.jda.api.events.channel.store.StoreChannelDeleteEvent;
/*     */ import net.dv8tion.jda.api.events.channel.text.TextChannelDeleteEvent;
/*     */ import net.dv8tion.jda.api.events.channel.voice.VoiceChannelDeleteEvent;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*     */ import net.dv8tion.jda.internal.utils.cache.SnowflakeCacheViewImpl;
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
/*     */ public class ChannelDeleteHandler
/*     */   extends SocketHandler
/*     */ {
/*     */   public ChannelDeleteHandler(JDAImpl api) {
/*  34 */     super(api); } protected Long handleInternally(DataObject content) { StoreChannel storeChannel;
/*     */     TextChannel textChannel;
/*     */     VoiceChannel channel;
/*     */     Category category;
/*     */     SnowflakeCacheViewImpl<PrivateChannel> privateView;
/*     */     PrivateChannel privateChannel;
/*  40 */     ChannelType type = ChannelType.fromId(content.getInt("type"));
/*     */     
/*  42 */     long guildId = 0L;
/*  43 */     if (type.isGuild()) {
/*     */       
/*  45 */       guildId = content.getLong("guild_id");
/*  46 */       if (getJDA().getGuildSetupController().isLocked(guildId)) {
/*  47 */         return Long.valueOf(guildId);
/*     */       }
/*     */     } 
/*  50 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(guildId);
/*  51 */     long channelId = content.getLong("id");
/*     */     
/*  53 */     switch (type)
/*     */     
/*     */     { 
/*     */       case STORE:
/*  57 */         storeChannel = (StoreChannel)getJDA().getStoreChannelsView().remove(channelId);
/*  58 */         if (storeChannel == null || guild == null) {
/*     */           
/*  60 */           WebSocketClient.LOG.debug("CHANNEL_DELETE attempted to delete a store channel that is not yet cached. JSON: {}", content);
/*  61 */           return null;
/*     */         } 
/*     */         
/*  64 */         guild.getStoreChannelView().remove(channelId);
/*  65 */         getJDA().handleEvent((GenericEvent)new StoreChannelDeleteEvent((JDA)
/*     */               
/*  67 */               getJDA(), this.responseNumber, storeChannel));
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
/* 148 */         getJDA().getEventCache().clear(EventCache.Type.CHANNEL, channelId);
/* 149 */         return null;case TEXT: textChannel = (TextChannel)getJDA().getTextChannelsView().remove(channelId); if (textChannel == null || guild == null) { WebSocketClient.LOG.debug("CHANNEL_DELETE attempted to delete a text channel that is not yet cached. JSON: {}", content); return null; }  guild.getTextChannelsView().remove(textChannel.getIdLong()); getJDA().handleEvent((GenericEvent)new TextChannelDeleteEvent((JDA)getJDA(), this.responseNumber, textChannel)); getJDA().getEventCache().clear(EventCache.Type.CHANNEL, channelId); return null;case STAGE: case VOICE: channel = (VoiceChannel)getJDA().getVoiceChannelsView().remove(channelId); if (channel == null || guild == null) { WebSocketClient.LOG.debug("CHANNEL_DELETE attempted to delete a voice channel that is not yet cached. JSON: {}", content); return null; }  guild.getVoiceChannelsView().remove(channel.getIdLong()); getJDA().handleEvent((GenericEvent)new VoiceChannelDeleteEvent((JDA)getJDA(), this.responseNumber, channel)); getJDA().getEventCache().clear(EventCache.Type.CHANNEL, channelId); return null;case CATEGORY: category = (Category)getJDA().getCategoriesView().remove(channelId); if (category == null || guild == null) { WebSocketClient.LOG.debug("CHANNEL_DELETE attempted to delete a category channel that is not yet cached. JSON: {}", content); return null; }  guild.getCategoriesView().remove(channelId); getJDA().handleEvent((GenericEvent)new CategoryDeleteEvent((JDA)getJDA(), this.responseNumber, category)); getJDA().getEventCache().clear(EventCache.Type.CHANNEL, channelId); return null;case PRIVATE: privateView = getJDA().getPrivateChannelsView(); privateChannel = (PrivateChannel)privateView.remove(channelId); if (privateChannel == null) { WebSocketClient.LOG.debug("CHANNEL_DELETE attempted to delete a private channel that is not yet cached. JSON: {}", content); return null; }  getJDA().getEventCache().clear(EventCache.Type.CHANNEL, channelId); return null;case GROUP: WebSocketClient.LOG.warn("Received a CHANNEL_DELETE for a channel of type GROUP which is not supported!"); return null; }  WebSocketClient.LOG.debug("CHANNEL_DELETE provided an unknown channel type. JSON: {}", content); getJDA().getEventCache().clear(EventCache.Type.CHANNEL, channelId); return null; }
/*     */ 
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\ChannelDeleteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */