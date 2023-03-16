/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import gnu.trove.map.TLongObjectMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Emote;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.emote.EmoteAddedEvent;
/*     */ import net.dv8tion.jda.api.events.emote.EmoteRemovedEvent;
/*     */ import net.dv8tion.jda.api.events.emote.update.EmoteUpdateNameEvent;
/*     */ import net.dv8tion.jda.api.events.emote.update.EmoteUpdateRolesEvent;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.EmoteImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.utils.UnlockHook;
/*     */ import net.dv8tion.jda.internal.utils.cache.SnowflakeCacheViewImpl;
/*     */ import org.apache.commons.collections4.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildEmojisUpdateHandler
/*     */   extends SocketHandler
/*     */ {
/*     */   public GuildEmojisUpdateHandler(JDAImpl api) {
/*  42 */     super(api);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Long handleInternally(DataObject content) {
/*     */     List<Emote> oldEmotes, newEmotes;
/*  48 */     if (!getJDA().isCacheFlagSet(CacheFlag.EMOTE))
/*  49 */       return null; 
/*  50 */     long guildId = content.getLong("guild_id");
/*  51 */     if (getJDA().getGuildSetupController().isLocked(guildId)) {
/*  52 */       return Long.valueOf(guildId);
/*     */     }
/*  54 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(guildId);
/*  55 */     if (guild == null) {
/*     */       
/*  57 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/*  58 */       return null;
/*     */     } 
/*     */     
/*  61 */     DataArray array = content.getArray("emojis");
/*     */     
/*  63 */     SnowflakeCacheViewImpl<Emote> emoteView = guild.getEmotesView();
/*  64 */     UnlockHook hook = emoteView.writeLock();
/*     */     
/*  66 */     try { TLongObjectMap<Emote> emoteMap = emoteView.getMap();
/*  67 */       oldEmotes = new ArrayList<>(emoteMap.valueCollection());
/*  68 */       newEmotes = new ArrayList<>();
/*  69 */       for (int i = 0; i < array.length(); i++) {
/*     */         
/*  71 */         DataObject current = array.getObject(i);
/*  72 */         long emoteId = current.getLong("id");
/*  73 */         EmoteImpl emote = (EmoteImpl)emoteMap.get(emoteId);
/*  74 */         EmoteImpl oldEmote = null;
/*     */         
/*  76 */         if (emote == null) {
/*     */           
/*  78 */           emote = new EmoteImpl(emoteId, guild);
/*  79 */           newEmotes.add(emote);
/*     */         
/*     */         }
/*     */         else {
/*     */           
/*  84 */           oldEmotes.remove(emote);
/*  85 */           oldEmote = emote.clone();
/*     */         } 
/*     */         
/*  88 */         emote.setName(current.getString("name"))
/*  89 */           .setAnimated(current.getBoolean("animated"))
/*  90 */           .setManaged(current.getBoolean("managed"));
/*     */         
/*  92 */         DataArray roles = current.getArray("roles");
/*  93 */         Set<Role> newRoles = emote.getRoleSet();
/*  94 */         Set<Role> oldRoles = new HashSet<>(newRoles);
/*  95 */         for (int j = 0; j < roles.length(); j++) {
/*     */           
/*  97 */           Role role = guild.getRoleById(roles.getString(j));
/*  98 */           if (role != null) {
/*     */             
/* 100 */             newRoles.add(role);
/* 101 */             oldRoles.remove(role);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 106 */         for (Role r : oldRoles)
/*     */         {
/*     */           
/* 109 */           newRoles.remove(r);
/*     */         }
/*     */ 
/*     */         
/* 113 */         emoteMap.put(emote.getIdLong(), emote);
/*     */         
/* 115 */         handleReplace((Emote)oldEmote, (Emote)emote);
/*     */       } 
/* 117 */       for (Emote e : oldEmotes)
/* 118 */         emoteMap.remove(e.getIdLong()); 
/* 119 */       if (hook != null) hook.close();  } catch (Throwable throwable) { if (hook != null)
/*     */         try { hook.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 121 */      for (Emote e : oldEmotes)
/*     */     {
/* 123 */       getJDA().handleEvent((GenericEvent)new EmoteRemovedEvent((JDA)
/*     */             
/* 125 */             getJDA(), this.responseNumber, e));
/*     */     }
/*     */ 
/*     */     
/* 129 */     for (Emote e : newEmotes)
/*     */     {
/* 131 */       getJDA().handleEvent((GenericEvent)new EmoteAddedEvent((JDA)
/*     */             
/* 133 */             getJDA(), this.responseNumber, e));
/*     */     }
/*     */ 
/*     */     
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleReplace(Emote oldEmote, Emote newEmote) {
/* 142 */     if (oldEmote == null || newEmote == null)
/*     */       return; 
/* 144 */     if (!Objects.equals(oldEmote.getName(), newEmote.getName()))
/*     */     {
/* 146 */       getJDA().handleEvent((GenericEvent)new EmoteUpdateNameEvent((JDA)
/*     */             
/* 148 */             getJDA(), this.responseNumber, newEmote, oldEmote
/* 149 */             .getName()));
/*     */     }
/*     */     
/* 152 */     if (!CollectionUtils.isEqualCollection(oldEmote.getRoles(), newEmote.getRoles()))
/*     */     {
/* 154 */       getJDA().handleEvent((GenericEvent)new EmoteUpdateRolesEvent((JDA)
/*     */             
/* 156 */             getJDA(), this.responseNumber, newEmote, oldEmote
/* 157 */             .getRoles()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildEmojisUpdateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */