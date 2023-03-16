/*     */ package net.dv8tion.jda.api.events.guild.update;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*     */ import net.dv8tion.jda.annotations.ForRemoval;
/*     */ import net.dv8tion.jda.annotations.ReplaceWith;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Region;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ @ForRemoval(deadline = "5.0.0")
/*     */ @ReplaceWith("VoiceChannelUpdateRegionEvent")
/*     */ @DeprecatedSince("4.3.0")
/*     */ public class GuildUpdateRegionEvent
/*     */   extends GenericGuildUpdateEvent<Region>
/*     */ {
/*     */   public static final String IDENTIFIER = "region";
/*     */   private final String oldRegion;
/*     */   private final String newRegion;
/*     */   
/*     */   public GuildUpdateRegionEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nonnull String oldRegion) {
/*  50 */     super(api, responseNumber, guild, Region.fromKey(oldRegion), guild.getRegion(), "region");
/*  51 */     this.oldRegion = oldRegion;
/*  52 */     this.newRegion = guild.getRegionRaw();
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
/*     */   public Region getOldRegion() {
/*  66 */     return getOldValue();
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
/*     */   @Nonnull
/*     */   public String getOldRegionRaw() {
/*  79 */     return this.oldRegion;
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
/*     */   public Region getNewRegion() {
/*  93 */     return getNewValue();
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
/*     */   @Nonnull
/*     */   public String getNewRegionRaw() {
/* 106 */     return this.newRegion;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Region getOldValue() {
/* 113 */     return super.getOldValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Region getNewValue() {
/* 120 */     return super.getNewValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guil\\update\GuildUpdateRegionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */