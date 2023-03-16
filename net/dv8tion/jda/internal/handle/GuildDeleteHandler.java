/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import gnu.trove.TLongCollection;
/*     */ import gnu.trove.set.TLongSet;
/*     */ import java.util.Objects;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
/*     */ import net.dv8tion.jda.api.entities.Category;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.StoreChannel;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
/*     */ import net.dv8tion.jda.api.events.guild.GuildUnavailableEvent;
/*     */ import net.dv8tion.jda.api.managers.AudioManager;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.managers.AudioManagerImpl;
/*     */ import net.dv8tion.jda.internal.requests.WebSocketClient;
/*     */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*     */ import net.dv8tion.jda.internal.utils.cache.AbstractCacheView;
/*     */ import net.dv8tion.jda.internal.utils.cache.SnowflakeCacheViewImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildDeleteHandler
/*     */   extends SocketHandler
/*     */ {
/*     */   public GuildDeleteHandler(JDAImpl api) {
/*  38 */     super(api);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Long handleInternally(DataObject content) {
/*  44 */     long id = content.getLong("id");
/*  45 */     GuildSetupController setupController = getJDA().getGuildSetupController();
/*  46 */     boolean wasInit = setupController.onDelete(id, content);
/*  47 */     if (wasInit || setupController.isUnavailable(id)) {
/*  48 */       return null;
/*     */     }
/*  50 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(id);
/*  51 */     boolean unavailable = content.getBoolean("unavailable");
/*  52 */     if (guild == null) {
/*     */ 
/*     */       
/*  55 */       WebSocketClient.LOG.debug("Received GUILD_DELETE for a Guild that is not currently cached. ID: {} unavailable: {}", Long.valueOf(id), Boolean.valueOf(unavailable));
/*  56 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  61 */     if (setupController.isUnavailable(id) && unavailable) {
/*  62 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  66 */     SnowflakeCacheViewImpl<Guild> guildView = getJDA().getGuildsView();
/*  67 */     SnowflakeCacheViewImpl<StoreChannel> storeView = getJDA().getStoreChannelsView();
/*  68 */     SnowflakeCacheViewImpl<TextChannel> textView = getJDA().getTextChannelsView();
/*  69 */     SnowflakeCacheViewImpl<VoiceChannel> voiceView = getJDA().getVoiceChannelsView();
/*  70 */     SnowflakeCacheViewImpl<Category> categoryView = getJDA().getCategoriesView();
/*  71 */     guildView.remove(id);
/*  72 */     UnlockHook hook = storeView.writeLock();
/*     */     
/*  74 */     try { guild.getStoreChannelCache()
/*  75 */         .forEachUnordered(chan -> storeView.getMap().remove(chan.getIdLong()));
/*  76 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*  77 */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  hook = textView.writeLock();
/*     */     
/*  79 */     try { guild.getTextChannelCache()
/*  80 */         .forEachUnordered(chan -> textView.getMap().remove(chan.getIdLong()));
/*  81 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*  82 */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  hook = voiceView.writeLock();
/*     */     
/*  84 */     try { guild.getVoiceChannelCache()
/*  85 */         .forEachUnordered(chan -> voiceView.getMap().remove(chan.getIdLong()));
/*  86 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*  87 */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  hook = categoryView.writeLock();
/*     */     
/*  89 */     try { guild.getCategoryCache()
/*  90 */         .forEachUnordered(chan -> categoryView.getMap().remove(chan.getIdLong()));
/*  91 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/*  94 */      getJDA().getClient().removeAudioConnection(id);
/*  95 */     AbstractCacheView<AudioManager> audioManagerView = getJDA().getAudioManagersView();
/*  96 */     AudioManagerImpl manager = (AudioManagerImpl)audioManagerView.get(id);
/*  97 */     if (manager != null)
/*  98 */       manager.closeAudioConnection(ConnectionStatus.DISCONNECTED_REMOVED_FROM_GUILD); 
/*  99 */     audioManagerView.remove(id);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     TLongSet memberIds = guild.getMembersView().keySet();
/*     */     
/* 106 */     Objects.requireNonNull(GuildImpl.class); getJDA().getGuildCache().stream().map(GuildImpl.class::cast)
/* 107 */       .forEach(g -> memberIds.removeAll((TLongCollection)g.getMembersView().keySet()));
/*     */     
/* 109 */     SnowflakeCacheViewImpl<User> userView = getJDA().getUsersView();
/* 110 */     UnlockHook unlockHook1 = userView.writeLock();
/*     */     
/* 112 */     try { long selfId = getJDA().getSelfUser().getIdLong();
/* 113 */       memberIds.forEach(memberId -> {
/*     */             if (memberId == selfId)
/*     */               return true; 
/*     */             userView.remove(memberId);
/*     */             getJDA().getEventCache().clear(EventCache.Type.USER, memberId);
/*     */             return true;
/*     */           });
/* 120 */       if (unlockHook1 != null) unlockHook1.close();  } catch (Throwable throwable) { if (unlockHook1 != null)
/*     */         try { unlockHook1.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 122 */      if (unavailable) {
/*     */       
/* 124 */       setupController.onUnavailable(id);
/* 125 */       guild.setAvailable(false);
/* 126 */       getJDA().handleEvent((GenericEvent)new GuildUnavailableEvent((JDA)
/*     */             
/* 128 */             getJDA(), this.responseNumber, (Guild)guild));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 133 */       getJDA().handleEvent((GenericEvent)new GuildLeaveEvent((JDA)
/*     */             
/* 135 */             getJDA(), this.responseNumber, (Guild)guild));
/*     */     } 
/*     */     
/* 138 */     getJDA().getEventCache().clear(EventCache.Type.GUILD, id);
/* 139 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildDeleteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */