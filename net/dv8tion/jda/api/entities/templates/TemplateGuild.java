/*     */ package net.dv8tion.jda.api.entities.templates;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.annotations.ForRemoval;
/*     */ import net.dv8tion.jda.api.Region;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TemplateGuild
/*     */   implements ISnowflake
/*     */ {
/*     */   private final long id;
/*     */   private final String name;
/*     */   private final String description;
/*     */   private final String region;
/*     */   private final String iconId;
/*     */   private final Guild.VerificationLevel verificationLevel;
/*     */   private final Guild.NotificationLevel notificationLevel;
/*     */   private final Guild.ExplicitContentLevel explicitContentLevel;
/*     */   private final Locale locale;
/*     */   private final Guild.Timeout afkTimeout;
/*     */   private final TemplateChannel afkChannel;
/*     */   private final TemplateChannel systemChannel;
/*     */   private final List<TemplateRole> roles;
/*     */   private final List<TemplateChannel> channels;
/*     */   
/*     */   public TemplateGuild(long id, String name, String description, String region, String iconId, Guild.VerificationLevel verificationLevel, Guild.NotificationLevel notificationLevel, Guild.ExplicitContentLevel explicitContentLevel, Locale locale, Guild.Timeout afkTimeout, TemplateChannel afkChannel, TemplateChannel systemChannel, List<TemplateRole> roles, List<TemplateChannel> channels) {
/*  58 */     this.id = id;
/*  59 */     this.name = name;
/*  60 */     this.description = description;
/*  61 */     this.region = region;
/*  62 */     this.iconId = iconId;
/*  63 */     this.verificationLevel = verificationLevel;
/*  64 */     this.notificationLevel = notificationLevel;
/*  65 */     this.explicitContentLevel = explicitContentLevel;
/*  66 */     this.locale = locale;
/*  67 */     this.afkTimeout = afkTimeout;
/*  68 */     this.afkChannel = afkChannel;
/*  69 */     this.systemChannel = systemChannel;
/*  70 */     this.roles = Collections.unmodifiableList(roles);
/*  71 */     this.channels = Collections.unmodifiableList(channels);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/*  77 */     return this.id;
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
/*  88 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDescription() {
/* 100 */     return this.description;
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
/*     */   @Deprecated
/*     */   @ForRemoval(deadline = "5.0.0")
/*     */   public Region getRegion() {
/* 117 */     return Region.fromKey(this.region);
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
/*     */   @Deprecated
/*     */   @ForRemoval(deadline = "5.0.0")
/*     */   public String getRegionRaw() {
/* 133 */     return this.region;
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
/*     */   @Nullable
/*     */   public String getIconId() {
/* 146 */     return this.iconId;
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
/*     */   @Nullable
/*     */   public String getIconUrl() {
/* 159 */     return (this.iconId == null) ? null : 
/* 160 */       String.format("https://cdn.discordapp.com/icons/%s/%s.%s", new Object[] { Long.valueOf(this.id), this.iconId, this.iconId.startsWith("a_") ? "gif" : "png" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild.VerificationLevel getVerificationLevel() {
/* 171 */     return this.verificationLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild.NotificationLevel getDefaultNotificationLevel() {
/* 182 */     return this.notificationLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild.ExplicitContentLevel getExplicitContentLevel() {
/* 193 */     return this.explicitContentLevel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Locale getLocale() {
/* 204 */     return this.locale;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild.Timeout getAfkTimeout() {
/* 215 */     return this.afkTimeout;
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
/*     */   @Nullable
/*     */   public TemplateChannel getAfkChannel() {
/* 229 */     return this.afkChannel;
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
/*     */   @Nullable
/*     */   public TemplateChannel getSystemChannel() {
/* 242 */     return this.systemChannel;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<TemplateRole> getRoles() {
/* 253 */     return this.roles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<TemplateChannel> getChannels() {
/* 264 */     return this.channels;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\templates\TemplateGuild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */