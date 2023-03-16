/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.time.temporal.TemporalUnit;
/*     */ import java.util.Objects;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.annotations.Incubating;
/*     */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.EncodingUtil;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Activity
/*     */ {
/*  49 */   public static final Pattern STREAMING_URL = Pattern.compile("https?://(www\\.)?(twitch\\.tv/|youtube\\.com/watch\\?v=).+", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isRich();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   RichPresence asRichPresence();
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
/*     */   @Nullable
/*     */   String getUrl();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   ActivityType getType();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Timestamps getTimestamps();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Emoji getEmoji();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   static Activity playing(@Nonnull String name) {
/* 126 */     Checks.notBlank(name, "Name");
/* 127 */     name = name.trim();
/* 128 */     Checks.notLonger(name, 128, "Name");
/* 129 */     return EntityBuilder.createActivity(name, null, ActivityType.DEFAULT);
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
/*     */   static Activity streaming(@Nonnull String name, @Nullable String url) {
/*     */     ActivityType type;
/* 152 */     Checks.notEmpty(name, "Provided game name");
/* 153 */     name = Helpers.isBlank(name) ? name : name.trim();
/* 154 */     Checks.notLonger(name, 128, "Name");
/*     */     
/* 156 */     if (isValidStreamingUrl(url)) {
/* 157 */       type = ActivityType.STREAMING;
/*     */     } else {
/* 159 */       type = ActivityType.DEFAULT;
/* 160 */     }  return EntityBuilder.createActivity(name, url, type);
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
/*     */   static Activity listening(@Nonnull String name) {
/* 178 */     Checks.notBlank(name, "Name");
/* 179 */     name = name.trim();
/* 180 */     Checks.notLonger(name, 128, "Name");
/* 181 */     return EntityBuilder.createActivity(name, null, ActivityType.LISTENING);
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
/*     */   @Incubating
/*     */   static Activity watching(@Nonnull String name) {
/* 202 */     Checks.notBlank(name, "Name");
/* 203 */     name = name.trim();
/* 204 */     Checks.notLonger(name, 128, "Name");
/* 205 */     return EntityBuilder.createActivity(name, null, ActivityType.WATCHING);
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
/*     */   static Activity competing(@Nonnull String name) {
/* 225 */     Checks.notBlank(name, "Name");
/* 226 */     name = name.trim();
/* 227 */     Checks.notLonger(name, 128, "Name");
/* 228 */     return EntityBuilder.createActivity(name, null, ActivityType.COMPETING);
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
/*     */   static Activity of(@Nonnull ActivityType type, @Nonnull String name) {
/* 250 */     return of(type, name, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   static Activity of(@Nonnull ActivityType type, @Nonnull String name, @Nullable String url) {
/* 278 */     Checks.notNull(type, "Type");
/* 279 */     switch (type) {
/*     */       
/*     */       case DEFAULT:
/* 282 */         return playing(name);
/*     */       case STREAMING:
/* 284 */         return streaming(name, url);
/*     */       case LISTENING:
/* 286 */         return listening(name);
/*     */       case WATCHING:
/* 288 */         return watching(name);
/*     */       case COMPETING:
/* 290 */         return competing(name);
/*     */     } 
/* 292 */     throw new IllegalArgumentException("ActivityType " + type + " is not supported!");
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
/*     */   static boolean isValidStreamingUrl(@Nullable String url) {
/* 306 */     return (url != null && STREAMING_URL.matcher(url).matches());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum ActivityType
/*     */   {
/* 317 */     DEFAULT(0),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 322 */     STREAMING(1),
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 327 */     LISTENING(2),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 334 */     WATCHING(3),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 342 */     CUSTOM_STATUS(4),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 351 */     COMPETING(5);
/*     */     
/*     */     private final int key;
/*     */ 
/*     */     
/*     */     ActivityType(int key) {
/* 357 */       this.key = key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getKey() {
/* 367 */       return this.key;
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
/*     */     public static ActivityType fromKey(int key) {
/* 382 */       switch (key)
/*     */       
/*     */       { 
/*     */         default:
/* 386 */           return DEFAULT;
/*     */         case 1:
/* 388 */           return STREAMING;
/*     */         case 2:
/* 390 */           return LISTENING;
/*     */         case 3:
/* 392 */           return WATCHING;
/*     */         case 4:
/* 394 */           return CUSTOM_STATUS;
/*     */         case 5:
/* 396 */           break; }  return COMPETING;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Timestamps
/*     */   {
/*     */     protected final long start;
/*     */ 
/*     */     
/*     */     protected final long end;
/*     */ 
/*     */ 
/*     */     
/*     */     public Timestamps(long start, long end) {
/* 412 */       this.start = start;
/* 413 */       this.end = end;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getStart() {
/* 423 */       return this.start;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Instant getStartTime() {
/* 434 */       return (this.start <= 0L) ? null : Instant.ofEpochMilli(this.start);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getEnd() {
/* 444 */       return this.end;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Instant getEndTime() {
/* 455 */       return (this.end <= 0L) ? null : Instant.ofEpochMilli(this.end);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getRemainingTime(TemporalUnit unit) {
/* 481 */       Checks.notNull(unit, "TemporalUnit");
/* 482 */       Instant end = getEndTime();
/* 483 */       return (end != null) ? Instant.now().until(end, unit) : -1L;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getElapsedTime(TemporalUnit unit) {
/* 509 */       Checks.notNull(unit, "TemporalUnit");
/* 510 */       Instant start = getStartTime();
/* 511 */       return (start != null) ? start.until(Instant.now(), unit) : -1L;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 517 */       return Helpers.format("RichPresenceTimestamp(%d-%d)", new Object[] { Long.valueOf(this.start), Long.valueOf(this.end) });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 523 */       if (!(obj instanceof Timestamps))
/* 524 */         return false; 
/* 525 */       Timestamps t = (Timestamps)obj;
/* 526 */       return (this.start == t.start && this.end == t.end);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 532 */       return Objects.hash(new Object[] { Long.valueOf(this.start), Long.valueOf(this.end) });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Emoji
/*     */     implements ISnowflake, IMentionable
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final long id;
/*     */     
/*     */     private final boolean animated;
/*     */ 
/*     */     
/*     */     public Emoji(String name, long id, boolean animated) {
/* 548 */       this.name = name;
/* 549 */       this.id = id;
/* 550 */       this.animated = animated;
/*     */     }
/*     */ 
/*     */     
/*     */     public Emoji(String name) {
/* 555 */       this(name, 0L, false);
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
/*     */     @Nonnull
/*     */     public String getName() {
/* 569 */       return this.name;
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
/*     */     @Nonnull
/*     */     public String getAsCodepoints() {
/* 586 */       if (!isEmoji())
/* 587 */         throw new IllegalStateException("Cannot convert custom emote to codepoints"); 
/* 588 */       return EncodingUtil.encodeCodepoints(this.name);
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
/*     */     public long getIdLong() {
/* 602 */       if (!isEmote())
/* 603 */         throw new IllegalStateException("Cannot get id for unicode emoji"); 
/* 604 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isAnimated() {
/* 615 */       return this.animated;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmoji() {
/* 625 */       return (this.id == 0L);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isEmote() {
/* 635 */       return (this.id != 0L);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getAsMention() {
/* 642 */       if (isEmoji()) {
/* 643 */         return this.name;
/*     */       }
/* 645 */       return String.format("<%s:%s:%s>", new Object[] { isAnimated() ? "a" : "", this.name, getId() });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 651 */       return (this.id == 0L) ? this.name.hashCode() : Long.hashCode(this.id);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 657 */       if (obj == this)
/* 658 */         return true; 
/* 659 */       if (!(obj instanceof Emoji))
/* 660 */         return false; 
/* 661 */       Emoji other = (Emoji)obj;
/* 662 */       return (this.id == 0L) ? other.name.equals(this.name) : (
/* 663 */         (other.id == this.id));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 669 */       if (isEmoji())
/* 670 */         return "ActivityEmoji(" + getAsCodepoints() + ')'; 
/* 671 */       return "ActivityEmoji(" + Long.toUnsignedString(this.id) + " / " + this.name + ')';
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\Activity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */