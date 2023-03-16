/*     */ package net.dv8tion.jda.api;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
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
/*     */ public enum Permission
/*     */ {
/*  31 */   CREATE_INSTANT_INVITE(0, true, true, "Create Instant Invite"),
/*  32 */   KICK_MEMBERS(1, true, false, "Kick Members"),
/*  33 */   BAN_MEMBERS(2, true, false, "Ban Members"),
/*  34 */   ADMINISTRATOR(3, true, false, "Administrator"),
/*  35 */   MANAGE_CHANNEL(4, true, true, "Manage Channels"),
/*  36 */   MANAGE_SERVER(5, true, false, "Manage Server"),
/*  37 */   MESSAGE_ADD_REACTION(6, true, true, "Add Reactions"),
/*  38 */   VIEW_AUDIT_LOGS(7, true, false, "View Audit Logs"),
/*  39 */   PRIORITY_SPEAKER(8, true, true, "Priority Speaker"),
/*  40 */   VIEW_GUILD_INSIGHTS(19, true, false, "View Server Insights"),
/*     */ 
/*     */   
/*  43 */   VIEW_CHANNEL(10, true, true, "Read Text Channels & See Voice Channels"),
/*     */ 
/*     */   
/*  46 */   MESSAGE_READ(10, true, true, "Read Messages"),
/*  47 */   MESSAGE_WRITE(11, true, true, "Send Messages"),
/*  48 */   MESSAGE_TTS(12, true, true, "Send TTS Messages"),
/*  49 */   MESSAGE_MANAGE(13, true, true, "Manage Messages"),
/*  50 */   MESSAGE_EMBED_LINKS(14, true, true, "Embed Links"),
/*  51 */   MESSAGE_ATTACH_FILES(15, true, true, "Attach Files"),
/*  52 */   MESSAGE_HISTORY(16, true, true, "Read History"),
/*  53 */   MESSAGE_MENTION_EVERYONE(17, true, true, "Mention Everyone"),
/*  54 */   MESSAGE_EXT_EMOJI(18, true, true, "Use External Emojis"),
/*  55 */   MESSAGE_EXT_STICKER(37, true, true, "Use External Stickers"),
/*  56 */   USE_SLASH_COMMANDS(31, true, true, "Use Slash Commands"),
/*     */   
/*  58 */   MANAGE_THREADS(34, true, true, "Manage Threads"),
/*  59 */   USE_PUBLIC_THREADS(35, true, true, "Use Public Threads"),
/*  60 */   USE_PRIVATE_THREADS(36, true, true, "Use Private Threads"),
/*     */ 
/*     */   
/*  63 */   VOICE_STREAM(9, true, true, "Video"),
/*  64 */   VOICE_CONNECT(20, true, true, "Connect"),
/*  65 */   VOICE_SPEAK(21, true, true, "Speak"),
/*  66 */   VOICE_MUTE_OTHERS(22, true, true, "Mute Members"),
/*  67 */   VOICE_DEAF_OTHERS(23, true, true, "Deafen Members"),
/*  68 */   VOICE_MOVE_OTHERS(24, true, true, "Move Members"),
/*  69 */   VOICE_USE_VAD(25, true, true, "Use Voice Activity"),
/*  70 */   VOICE_START_ACTIVITIES(39, true, true, "Launch Activities in Voice Channels"),
/*     */   
/*  72 */   NICKNAME_CHANGE(26, true, false, "Change Nickname"),
/*  73 */   NICKNAME_MANAGE(27, true, false, "Manage Nicknames"),
/*     */   
/*  75 */   MANAGE_ROLES(28, true, false, "Manage Roles"),
/*  76 */   MANAGE_PERMISSIONS(28, false, true, "Manage Permissions"),
/*  77 */   MANAGE_WEBHOOKS(29, true, true, "Manage Webhooks"),
/*  78 */   MANAGE_EMOTES(30, true, false, "Manage Emojis"),
/*     */   
/*  80 */   REQUEST_TO_SPEAK(32, true, true, "Request to Speak"),
/*     */   
/*  82 */   UNKNOWN(-1, false, false, "Unknown");
/*     */   
/*     */   public static final Permission[] EMPTY_PERMISSIONS;
/*     */   public static final long ALL_PERMISSIONS;
/*     */   
/*     */   static {
/*  88 */     EMPTY_PERMISSIONS = new Permission[0];
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     ALL_PERMISSIONS = getRaw(values());
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     ALL_CHANNEL_PERMISSIONS = getRaw((Collection<Permission>)Arrays.<Permission>stream(values())
/*  99 */         .filter(Permission::isChannel).collect(Collectors.toSet()));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     ALL_GUILD_PERMISSIONS = getRaw((Collection<Permission>)Arrays.<Permission>stream(values())
/* 105 */         .filter(Permission::isGuild).collect(Collectors.toSet()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     ALL_TEXT_PERMISSIONS = getRaw(new Permission[] { 
/*     */           MESSAGE_ADD_REACTION, MESSAGE_WRITE, MESSAGE_TTS, MESSAGE_MANAGE, MESSAGE_EMBED_LINKS, MESSAGE_ATTACH_FILES, MESSAGE_EXT_STICKER, MESSAGE_EXT_EMOJI, MESSAGE_HISTORY, MESSAGE_MENTION_EVERYONE, 
/*     */           USE_SLASH_COMMANDS, MANAGE_THREADS, USE_PUBLIC_THREADS, USE_PRIVATE_THREADS });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     ALL_VOICE_PERMISSIONS = getRaw(new Permission[] { VOICE_STREAM, VOICE_CONNECT, VOICE_SPEAK, VOICE_MUTE_OTHERS, VOICE_DEAF_OTHERS, VOICE_MOVE_OTHERS, VOICE_USE_VAD, PRIORITY_SPEAKER, REQUEST_TO_SPEAK, VOICE_START_ACTIVITIES });
/*     */   }
/*     */   public static final long ALL_CHANNEL_PERMISSIONS; public static final long ALL_GUILD_PERMISSIONS; public static final long ALL_TEXT_PERMISSIONS;
/*     */   public static final long ALL_VOICE_PERMISSIONS;
/*     */   private final int offset;
/*     */   private final long raw;
/*     */   private final boolean isGuild;
/*     */   private final boolean isChannel;
/*     */   private final String name;
/*     */   
/*     */   Permission(int offset, @Nonnull boolean isGuild, boolean isChannel, String name) {
/* 131 */     this.offset = offset;
/* 132 */     this.raw = 1L << offset;
/* 133 */     this.isGuild = isGuild;
/* 134 */     this.isChannel = isChannel;
/* 135 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/* 146 */     return this.name;
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
/*     */   public int getOffset() {
/* 158 */     return this.offset;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getRawValue() {
/* 169 */     return this.raw;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGuild() {
/* 180 */     return this.isGuild;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isChannel() {
/* 191 */     return this.isChannel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isText() {
/* 201 */     return ((this.raw & ALL_TEXT_PERMISSIONS) == this.raw);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVoice() {
/* 211 */     return ((this.raw & ALL_VOICE_PERMISSIONS) == this.raw);
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
/*     */   @Nonnull
/*     */   public static Permission getFromOffset(int offset) {
/* 227 */     for (Permission perm : values()) {
/*     */       
/* 229 */       if (perm.offset == offset)
/* 230 */         return perm; 
/*     */     } 
/* 232 */     return UNKNOWN;
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
/*     */   public static EnumSet<Permission> getPermissions(long permissions) {
/* 251 */     if (permissions == 0L)
/* 252 */       return EnumSet.noneOf(Permission.class); 
/* 253 */     EnumSet<Permission> perms = EnumSet.noneOf(Permission.class);
/* 254 */     for (Permission perm : values()) {
/*     */       
/* 256 */       if (perm != UNKNOWN && (permissions & perm.raw) == perm.raw)
/* 257 */         perms.add(perm); 
/*     */     } 
/* 259 */     return perms;
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
/*     */   public static long getRaw(@Nonnull Permission... permissions) {
/* 273 */     long raw = 0L;
/* 274 */     for (Permission perm : permissions) {
/*     */       
/* 276 */       if (perm != null && perm != UNKNOWN) {
/* 277 */         raw |= perm.raw;
/*     */       }
/*     */     } 
/* 280 */     return raw;
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
/*     */   public static long getRaw(@Nonnull Collection<Permission> permissions) {
/* 297 */     Checks.notNull(permissions, "Permission Collection");
/*     */     
/* 299 */     return getRaw(permissions.<Permission>toArray(EMPTY_PERMISSIONS));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\Permission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */