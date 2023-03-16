/*     */ package net.dv8tion.jda.api.requests;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.emote.GenericEmoteEvent;
/*     */ import net.dv8tion.jda.api.events.guild.GuildBanEvent;
/*     */ import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
/*     */ import net.dv8tion.jda.api.events.guild.invite.GenericGuildInviteEvent;
/*     */ import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
/*     */ import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
/*     */ import net.dv8tion.jda.api.events.guild.voice.GenericGuildVoiceEvent;
/*     */ import net.dv8tion.jda.api.events.message.GenericMessageEvent;
/*     */ import net.dv8tion.jda.api.events.message.MessageBulkDeleteEvent;
/*     */ import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
/*     */ import net.dv8tion.jda.api.events.message.guild.react.GenericGuildMessageReactionEvent;
/*     */ import net.dv8tion.jda.api.events.message.priv.GenericPrivateMessageEvent;
/*     */ import net.dv8tion.jda.api.events.message.priv.react.GenericPrivateMessageReactionEvent;
/*     */ import net.dv8tion.jda.api.events.message.react.GenericMessageReactionEvent;
/*     */ import net.dv8tion.jda.api.events.user.UserTypingEvent;
/*     */ import net.dv8tion.jda.api.events.user.update.GenericUserPresenceEvent;
/*     */ import net.dv8tion.jda.api.events.user.update.GenericUserUpdateEvent;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheFlag;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum GatewayIntent
/*     */ {
/*  90 */   GUILD_MEMBERS(1),
/*     */ 
/*     */ 
/*     */   
/*  94 */   GUILD_BANS(2),
/*     */ 
/*     */ 
/*     */   
/*  98 */   GUILD_EMOJIS(3),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 106 */   GUILD_WEBHOOKS(5),
/*     */ 
/*     */ 
/*     */   
/* 110 */   GUILD_INVITES(6),
/*     */ 
/*     */ 
/*     */   
/* 114 */   GUILD_VOICE_STATES(7),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 121 */   GUILD_PRESENCES(8),
/*     */ 
/*     */ 
/*     */   
/* 125 */   GUILD_MESSAGES(9),
/*     */ 
/*     */ 
/*     */   
/* 129 */   GUILD_MESSAGE_REACTIONS(10),
/*     */ 
/*     */ 
/*     */   
/* 133 */   GUILD_MESSAGE_TYPING(11),
/*     */ 
/*     */ 
/*     */   
/* 137 */   DIRECT_MESSAGES(12),
/*     */ 
/*     */ 
/*     */   
/* 141 */   DIRECT_MESSAGE_REACTIONS(13),
/*     */ 
/*     */ 
/*     */   
/* 145 */   DIRECT_MESSAGE_TYPING(14);
/*     */   
/*     */   public static final int ALL_INTENTS;
/*     */   
/*     */   public static final int DEFAULT;
/*     */   
/*     */   private final int rawValue;
/*     */   private final int offset;
/*     */   
/*     */   static {
/* 155 */     ALL_INTENTS = 0x1 | getRaw(EnumSet.allOf(GatewayIntent.class));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 173 */     DEFAULT = ALL_INTENTS & (getRaw(GUILD_MEMBERS, new GatewayIntent[] { GUILD_PRESENCES, GUILD_WEBHOOKS, GUILD_MESSAGE_TYPING, DIRECT_MESSAGE_TYPING }) ^ 0xFFFFFFFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   GatewayIntent(int offset) {
/* 180 */     this.offset = offset;
/* 181 */     this.rawValue = 1 << offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRawValue() {
/* 191 */     return this.rawValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getOffset() {
/* 202 */     return this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static EnumSet<GatewayIntent> getIntents(int raw) {
/* 216 */     EnumSet<GatewayIntent> set = EnumSet.noneOf(GatewayIntent.class);
/* 217 */     for (GatewayIntent intent : values()) {
/*     */       
/* 219 */       if ((intent.getRawValue() & raw) != 0)
/* 220 */         set.add(intent); 
/*     */     } 
/* 222 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getRaw(@Nonnull Collection<GatewayIntent> set) {
/* 238 */     int raw = 0;
/* 239 */     for (GatewayIntent intent : set)
/* 240 */       raw |= intent.rawValue; 
/* 241 */     return raw;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getRaw(@Nonnull GatewayIntent intent, @Nonnull GatewayIntent... set) {
/* 259 */     Checks.notNull(intent, "Intent");
/* 260 */     Checks.notNull(set, "Intent");
/* 261 */     return getRaw(EnumSet.of(intent, set));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static EnumSet<GatewayIntent> fromCacheFlags(@Nonnull CacheFlag flag, @Nonnull CacheFlag... other) {
/* 281 */     Checks.notNull(flag, "CacheFlag");
/* 282 */     Checks.noneNull((Object[])other, "CacheFlag");
/* 283 */     return fromCacheFlags(EnumSet.of(flag, other));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static EnumSet<GatewayIntent> fromCacheFlags(@Nonnull Collection<CacheFlag> flags) {
/* 301 */     EnumSet<GatewayIntent> intents = EnumSet.noneOf(GatewayIntent.class);
/* 302 */     for (CacheFlag flag : flags) {
/*     */       
/* 304 */       Checks.notNull(flag, "CacheFlag");
/* 305 */       GatewayIntent intent = flag.getRequiredIntent();
/* 306 */       if (intent != null) {
/* 307 */         intents.add(intent);
/*     */       }
/*     */     } 
/* 310 */     return intents;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @SafeVarargs
/*     */   public static EnumSet<GatewayIntent> fromEvents(@Nonnull Class<? extends GenericEvent>... events) {
/* 328 */     Checks.noneNull((Object[])events, "Event");
/* 329 */     return fromEvents(Arrays.asList(events));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static EnumSet<GatewayIntent> fromEvents(@Nonnull Collection<Class<? extends GenericEvent>> events) {
/* 346 */     EnumSet<GatewayIntent> intents = EnumSet.noneOf(GatewayIntent.class);
/* 347 */     for (Class<? extends GenericEvent> event : events) {
/*     */       
/* 349 */       Checks.notNull(event, "Event");
/*     */       
/* 351 */       if (GenericUserPresenceEvent.class.isAssignableFrom(event)) {
/* 352 */         intents.add(GUILD_PRESENCES); continue;
/* 353 */       }  if (GenericUserUpdateEvent.class.isAssignableFrom(event) || GenericGuildMemberEvent.class.isAssignableFrom(event) || GuildMemberRemoveEvent.class.isAssignableFrom(event)) {
/* 354 */         intents.add(GUILD_MEMBERS); continue;
/*     */       } 
/* 356 */       if (GuildBanEvent.class.isAssignableFrom(event) || GuildUnbanEvent.class.isAssignableFrom(event)) {
/* 357 */         intents.add(GUILD_BANS); continue;
/* 358 */       }  if (GenericEmoteEvent.class.isAssignableFrom(event)) {
/* 359 */         intents.add(GUILD_EMOJIS); continue;
/* 360 */       }  if (GenericGuildInviteEvent.class.isAssignableFrom(event)) {
/* 361 */         intents.add(GUILD_INVITES); continue;
/* 362 */       }  if (GenericGuildVoiceEvent.class.isAssignableFrom(event)) {
/* 363 */         intents.add(GUILD_VOICE_STATES); continue;
/*     */       } 
/* 365 */       if (GenericGuildMessageReactionEvent.class.isAssignableFrom(event)) {
/* 366 */         intents.add(GUILD_MESSAGE_REACTIONS); continue;
/* 367 */       }  if (GenericGuildMessageEvent.class.isAssignableFrom(event) || MessageBulkDeleteEvent.class.isAssignableFrom(event)) {
/* 368 */         intents.add(GUILD_MESSAGES); continue;
/*     */       } 
/* 370 */       if (GenericPrivateMessageReactionEvent.class.isAssignableFrom(event)) {
/* 371 */         intents.add(DIRECT_MESSAGE_REACTIONS); continue;
/* 372 */       }  if (GenericPrivateMessageEvent.class.isAssignableFrom(event)) {
/* 373 */         intents.add(DIRECT_MESSAGES); continue;
/*     */       } 
/* 375 */       if (GenericMessageReactionEvent.class.isAssignableFrom(event)) {
/* 376 */         Collections.addAll(intents, new GatewayIntent[] { GUILD_MESSAGE_REACTIONS, DIRECT_MESSAGE_REACTIONS }); continue;
/*     */       } 
/* 378 */       if (GenericMessageEvent.class.isAssignableFrom(event)) {
/* 379 */         Collections.addAll(intents, new GatewayIntent[] { GUILD_MESSAGES, DIRECT_MESSAGES }); continue;
/*     */       } 
/* 381 */       if (UserTypingEvent.class.isAssignableFrom(event))
/* 382 */         Collections.addAll(intents, new GatewayIntent[] { GUILD_MESSAGE_TYPING, DIRECT_MESSAGE_TYPING }); 
/*     */     } 
/* 384 */     return intents;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static EnumSet<GatewayIntent> from(@Nonnull Collection<Class<? extends GenericEvent>> events, @Nonnull Collection<CacheFlag> flags) {
/* 403 */     EnumSet<GatewayIntent> intents = fromEvents(events);
/* 404 */     intents.addAll(fromCacheFlags(flags));
/* 405 */     return intents;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\GatewayIntent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */