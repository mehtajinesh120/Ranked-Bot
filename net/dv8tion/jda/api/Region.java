/*     */ package net.dv8tion.jda.api;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum Region
/*     */ {
/*  31 */   AMSTERDAM("amsterdam", "Amsterdam", "🇳🇱", false),
/*  32 */   BRAZIL("brazil", "Brazil", "🇧🇷", false),
/*  33 */   EUROPE("europe", "Europe", "🇪🇺", false),
/*  34 */   EU_CENTRAL("eu-central", "EU Central", "🇪🇺", false),
/*  35 */   EU_WEST("eu-west", "EU West", "🇪🇺", false),
/*  36 */   FRANKFURT("frankfurt", "Frankfurt", "🇩🇪", false),
/*  37 */   HONG_KONG("hongkong", "Hong Kong", "🇭🇰", false),
/*  38 */   JAPAN("japan", "Japan", "🇯🇵", false),
/*  39 */   SOUTH_KOREA("south-korea", "South Korea", "🇰🇷", false),
/*  40 */   LONDON("london", "London", "🇬🇧", false),
/*  41 */   RUSSIA("russia", "Russia", "🇷🇺", false),
/*  42 */   INDIA("india", "India", "🇮🇳", false),
/*  43 */   SINGAPORE("singapore", "Singapore", "🇸🇬", false),
/*  44 */   SOUTH_AFRICA("southafrica", "South Africa", "🇿🇦", false),
/*  45 */   SYDNEY("sydney", "Sydney", "🇦🇺", false),
/*  46 */   US_CENTRAL("us-central", "US Central", "🇺🇸", false),
/*  47 */   US_EAST("us-east", "US East", "🇺🇸", false),
/*  48 */   US_SOUTH("us-south", "US South", "🇺🇸", false),
/*  49 */   US_WEST("us-west", "US West", "🇺🇸", false),
/*     */   
/*  51 */   VIP_AMSTERDAM("vip-amsterdam", "Amsterdam (VIP)", "🇳🇱", true),
/*  52 */   VIP_BRAZIL("vip-brazil", "Brazil (VIP)", "🇧🇷", true),
/*  53 */   VIP_EU_CENTRAL("vip-eu-central", "EU Central (VIP)", "🇪🇺", true),
/*  54 */   VIP_EU_WEST("vip-eu-west", "EU West (VIP)", "🇪🇺", true),
/*  55 */   VIP_FRANKFURT("vip-frankfurt", "Frankfurt (VIP)", "🇩🇪", true),
/*  56 */   VIP_JAPAN("vip-japan", "Japan (VIP)", "🇯🇵", true),
/*  57 */   VIP_SOUTH_KOREA("vip-south-korea", "South Korea (VIP)", "🇰🇷", true),
/*  58 */   VIP_LONDON("vip-london", "London (VIP)", "🇬🇧", true),
/*  59 */   VIP_SINGAPORE("vip-singapore", "Singapore (VIP)", "🇸🇬", true),
/*  60 */   VIP_SOUTH_AFRICA("vip-southafrica", "South Africa (VIP)", "🇿🇦", true),
/*  61 */   VIP_SYDNEY("vip-sydney", "Sydney (VIP)", "🇦🇺", true),
/*  62 */   VIP_US_CENTRAL("vip-us-central", "US Central (VIP)", "🇺🇸", true),
/*  63 */   VIP_US_EAST("vip-us-east", "US East (VIP)", "🇺🇸", true),
/*  64 */   VIP_US_SOUTH("vip-us-south", "US South (VIP)", "🇺🇸", true),
/*  65 */   VIP_US_WEST("vip-us-west", "US West (VIP)", "🇺🇸", true),
/*     */   
/*  67 */   UNKNOWN("", "Unknown Region", null, false),
/*     */   
/*  69 */   AUTOMATIC("automatic", "Automatic", null, false);
/*     */   
/*     */   public static final Set<Region> VOICE_CHANNEL_REGIONS;
/*     */   private final String key;
/*     */   
/*     */   static {
/*  75 */     VOICE_CHANNEL_REGIONS = Collections.unmodifiableSet(EnumSet.of(AUTOMATIC, new Region[] { 
/*     */             US_WEST, US_EAST, US_CENTRAL, US_SOUTH, SINGAPORE, SOUTH_AFRICA, SYDNEY, EUROPE, INDIA, SOUTH_KOREA, 
/*     */             BRAZIL, JAPAN, RUSSIA }));
/*     */   }
/*     */ 
/*     */   
/*     */   private final String name;
/*     */   
/*     */   Region(String key, String name, String emoji, boolean vip) {
/*  84 */     this.key = key;
/*  85 */     this.name = name;
/*  86 */     this.emoji = emoji;
/*  87 */     this.vip = vip;
/*     */   }
/*     */ 
/*     */   
/*     */   private final String emoji;
/*     */   
/*     */   private final boolean vip;
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/*  98 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getKey() {
/* 109 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getEmoji() {
/* 120 */     return this.emoji;
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
/*     */   public boolean isVip() {
/* 132 */     return this.vip;
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
/*     */   public static Region fromKey(@Nullable String key) {
/* 147 */     for (Region region : values()) {
/*     */       
/* 149 */       if (region.getKey().equals(key))
/*     */       {
/* 151 */         return region;
/*     */       }
/*     */     } 
/* 154 */     return UNKNOWN;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 160 */     return getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\Region.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */