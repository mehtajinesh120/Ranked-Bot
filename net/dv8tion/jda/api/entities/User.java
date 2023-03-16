/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*     */ import net.dv8tion.jda.annotations.ForRemoval;
/*     */ import net.dv8tion.jda.annotations.ReplaceWith;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.internal.entities.UserById;
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
/*     */ public interface User
/*     */   extends IMentionable
/*     */ {
/*  82 */   public static final Pattern USER_TAG = Pattern.compile("(.{2,32})#(\\d{4})");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String AVATAR_URL = "https://cdn.discordapp.com/avatars/%s/%s.%s";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DEFAULT_AVATAR_URL = "https://cdn.discordapp.com/embed/avatars/%s.png";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String BANNER_URL = "https://cdn.discordapp.com/banners/%s/%s.%s";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final int DEFAULT_ACCENT_COLOR_RAW = 536870911;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   static User fromId(long id) {
/* 110 */     return (User)new UserById(id);
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
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   static User fromId(@Nonnull String id) {
/* 132 */     return fromId(MiscUtil.parseSnowflake(id));
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
/*     */   @Nonnull
/*     */   String getName();
/*     */ 
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
/*     */   String getDiscriminator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   String getAvatarId();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default String getAvatarUrl() {
/* 183 */     String avatarId = getAvatarId();
/* 184 */     return (avatarId == null) ? null : String.format("https://cdn.discordapp.com/avatars/%s/%s.%s", new Object[] { getId(), avatarId, avatarId.startsWith("a_") ? "gif" : "png" });
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
/*     */   String getDefaultAvatarId();
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
/*     */   default String getDefaultAvatarUrl() {
/* 209 */     return String.format("https://cdn.discordapp.com/embed/avatars/%s.png", new Object[] { getDefaultAvatarId() });
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
/*     */   default String getEffectiveAvatarUrl() {
/* 225 */     String avatarUrl = getAvatarUrl();
/* 226 */     return (avatarUrl == null) ? getDefaultAvatarUrl() : avatarUrl;
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
/*     */   @CheckReturnValue
/*     */   RestAction<Profile> retrieveProfile();
/*     */ 
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
/*     */   String getAsTag();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean hasPrivateChannel();
/*     */ 
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
/*     */   @CheckReturnValue
/*     */   RestAction<PrivateChannel> openPrivateChannel();
/*     */ 
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
/*     */   List<Guild> getMutualGuilds();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isBot();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSystem();
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
/*     */   JDA getJDA();
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
/*     */   EnumSet<UserFlag> getFlags();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int getFlagsRaw();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Profile
/*     */   {
/*     */     private final long userId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final String bannerId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private final int accentColor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Profile(long userId, String bannerId, int accentColor) {
/* 381 */       this.userId = userId;
/* 382 */       this.bannerId = bannerId;
/* 383 */       this.accentColor = accentColor;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getBannerId() {
/* 395 */       return this.bannerId;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getBannerUrl() {
/* 409 */       return (this.bannerId == null) ? null : String.format("https://cdn.discordapp.com/banners/%s/%s.%s", new Object[] { Long.toUnsignedString(this.userId), this.bannerId, this.bannerId.startsWith("a_") ? "gif" : "png" });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Color getAccentColor() {
/* 423 */       return (this.accentColor == 536870911) ? null : new Color(this.accentColor);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getAccentColorRaw() {
/* 434 */       return this.accentColor;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 440 */       return "UserProfile(userId=" + this.userId + ", bannerId='" + this.bannerId + "', accentColor=" + this.accentColor + ')';
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum UserFlag
/*     */   {
/* 453 */     STAFF(0, "Discord Employee"),
/* 454 */     PARTNER(1, "Partnered Server Owner"),
/* 455 */     HYPESQUAD(2, "HypeSquad Events"),
/* 456 */     BUG_HUNTER_LEVEL_1(3, "Bug Hunter Level 1"),
/*     */ 
/*     */     
/* 459 */     HYPESQUAD_BRAVERY(6, "HypeSquad Bravery"),
/* 460 */     HYPESQUAD_BRILLIANCE(7, "HypeSquad Brilliance"),
/* 461 */     HYPESQUAD_BALANCE(8, "HypeSquad Balance"),
/*     */     
/* 463 */     EARLY_SUPPORTER(9, "Early Supporter"),
/*     */ 
/*     */ 
/*     */     
/* 467 */     TEAM_USER(10, "Team User"),
/* 468 */     SYSTEM(12, "System User"),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 473 */     BUG_HUNTER_LEVEL_2(14, "Bug Hunter Level 2"),
/* 474 */     VERIFIED_BOT(16, "Verified Bot"),
/* 475 */     VERIFIED_DEVELOPER(17, "Early Verified Bot Developer"),
/* 476 */     CERTIFIED_MODERATOR(18, "Discord Certified Moderator"),
/*     */ 
/*     */ 
/*     */     
/* 480 */     BOT_HTTP_INTERACTIONS(19, "HTTP Interactions Bot"),
/*     */     
/* 482 */     UNKNOWN(-1, "Unknown");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 487 */     public static final UserFlag[] EMPTY_FLAGS = new UserFlag[0];
/*     */     
/*     */     private final int offset;
/*     */     
/*     */     private final int raw;
/*     */ 
/*     */     
/*     */     UserFlag(int offset, String name) {
/* 495 */       this.offset = offset;
/* 496 */       this.raw = 1 << offset;
/* 497 */       this.name = name;
/*     */     }
/*     */     
/*     */     private final String name;
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */     
/*     */     @Nonnull
/*     */     public String getName() {
/* 508 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getOffset() {
/* 518 */       return this.offset;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getRawValue() {
/* 529 */       return this.raw;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public static UserFlag getFromOffset(int offset) {
/* 545 */       for (UserFlag flag : values()) {
/*     */         
/* 547 */         if (flag.offset == offset)
/* 548 */           return flag; 
/*     */       } 
/* 550 */       return UNKNOWN;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public static EnumSet<UserFlag> getFlags(int flags) {
/* 565 */       EnumSet<UserFlag> foundFlags = EnumSet.noneOf(UserFlag.class);
/*     */       
/* 567 */       if (flags == 0) {
/* 568 */         return foundFlags;
/*     */       }
/* 570 */       for (UserFlag flag : values()) {
/*     */         
/* 572 */         if (flag != UNKNOWN && (flags & flag.raw) == flag.raw) {
/* 573 */           foundFlags.add(flag);
/*     */         }
/*     */       } 
/* 576 */       return foundFlags;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static int getRaw(@Nonnull UserFlag... flags) {
/* 592 */       Checks.noneNull((Object[])flags, "UserFlags");
/*     */       
/* 594 */       int raw = 0;
/* 595 */       for (UserFlag flag : flags) {
/*     */         
/* 597 */         if (flag != null && flag != UNKNOWN) {
/* 598 */           raw |= flag.raw;
/*     */         }
/*     */       } 
/* 601 */       return raw;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static int getRaw(@Nonnull Collection<UserFlag> flags) {
/* 621 */       Checks.notNull(flags, "Flag Collection");
/*     */       
/* 623 */       return getRaw(flags.<UserFlag>toArray(EMPTY_FLAGS));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\User.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */