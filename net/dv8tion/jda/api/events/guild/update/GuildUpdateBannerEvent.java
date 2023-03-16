/*     */ package net.dv8tion.jda.api.events.guild.update;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*     */ import net.dv8tion.jda.annotations.ForRemoval;
/*     */ import net.dv8tion.jda.annotations.ReplaceWith;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildUpdateBannerEvent
/*     */   extends GenericGuildUpdateEvent<String>
/*     */ {
/*     */   public static final String IDENTIFIER = "banner";
/*     */   
/*     */   public GuildUpdateBannerEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nullable String previous) {
/*  41 */     super(api, responseNumber, guild, previous, guild.getBannerId(), "banner");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getNewBannerId() {
/*  52 */     return getNewValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getNewBannerUrl() {
/*  63 */     return (this.next == null) ? null : String.format("https://cdn.discordapp.com/banners/%s/%s.png", new Object[] { this.guild.getId(), this.next });
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
/*     */   @Deprecated
/*     */   @ForRemoval(deadline = "5.0.0")
/*     */   @DeprecatedSince("4.2.0")
/*     */   @ReplaceWith("getNewBannerUrl()")
/*     */   public String getNewBannerIdUrl() {
/*  80 */     return getNewBannerUrl();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getOldBannerId() {
/*  91 */     return getOldValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getOldBannerUrl() {
/* 102 */     return (this.previous == null) ? null : String.format("https://cdn.discordapp.com/banners/%s/%s.png", new Object[] { this.guild.getId(), this.previous });
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guil\\update\GuildUpdateBannerEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */