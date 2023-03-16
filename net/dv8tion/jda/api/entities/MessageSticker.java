/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*     */ import net.dv8tion.jda.annotations.ForRemoval;
/*     */ import net.dv8tion.jda.annotations.ReplaceWith;
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
/*     */ public class MessageSticker
/*     */   implements ISnowflake
/*     */ {
/*     */   private final long id;
/*     */   private final String name;
/*     */   private final String description;
/*     */   private final long packId;
/*     */   private final String asset;
/*     */   private final StickerFormat formatType;
/*     */   private final Set<String> tags;
/*     */   @Deprecated
/*     */   @ForRemoval(deadline = "5.0.0")
/*     */   @ReplaceWith("ICON_URL")
/*     */   @DeprecatedSince("4.3.1")
/*     */   public static final String ASSET_URL = "https://cdn.discordapp.com/stickers/%s/%s.%s";
/*     */   public static final String ICON_URL = "https://cdn.discordapp.com/stickers/%s.%s";
/*     */   
/*     */   public MessageSticker(long id, String name, String description, long packId, String asset, StickerFormat formatType, Set<String> tags) {
/*  56 */     this.id = id;
/*  57 */     this.name = name;
/*  58 */     this.description = description;
/*  59 */     this.packId = packId;
/*  60 */     this.asset = asset;
/*  61 */     this.formatType = formatType;
/*  62 */     this.tags = tags;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/*  68 */     return this.id;
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
/*  79 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getDescription() {
/*  90 */     return this.description;
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
/*     */   public String getPackId() {
/* 103 */     return Long.toUnsignedString(getPackIdLong());
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
/*     */   public long getPackIdLong() {
/* 115 */     return this.packId;
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
/*     */   @ReplaceWith("getIconUrl()")
/*     */   @DeprecatedSince("4.3.1")
/*     */   public String getAssetHash() {
/* 133 */     return this.asset;
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
/*     */   @Deprecated
/*     */   @ForRemoval(deadline = "5.0.0")
/*     */   @ReplaceWith("getIconUrl()")
/*     */   @DeprecatedSince("4.3.1")
/*     */   public String getAssetUrl() {
/* 153 */     return String.format("https://cdn.discordapp.com/stickers/%s/%s.%s", new Object[] { Long.valueOf(this.id), this.asset, this.formatType.getExtension() });
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
/*     */   public String getIconUrl() {
/* 167 */     return Helpers.format("https://cdn.discordapp.com/stickers/%s.%s", new Object[] { getId(), this.formatType.getExtension() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public StickerFormat getFormatType() {
/* 178 */     return this.formatType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Set<String> getTags() {
/* 189 */     return this.tags;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum StickerFormat
/*     */   {
/* 197 */     PNG(1, "png"),
/*     */ 
/*     */ 
/*     */     
/* 201 */     APNG(2, "apng"),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 208 */     LOTTIE(3, "json"),
/*     */ 
/*     */ 
/*     */     
/* 212 */     UNKNOWN(-1, null);
/*     */     
/*     */     private final int id;
/*     */     
/*     */     private final String extension;
/*     */     
/*     */     StickerFormat(int id, String extension) {
/* 219 */       this.id = id;
/* 220 */       this.extension = extension;
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
/*     */     public String getExtension() {
/* 234 */       if (this == UNKNOWN)
/* 235 */         throw new IllegalStateException("Can only get extension of a known format"); 
/* 236 */       return this.extension;
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
/*     */     public static StickerFormat fromId(int id) {
/* 250 */       for (StickerFormat stickerFormat : values()) {
/*     */         
/* 252 */         if (stickerFormat.id == id)
/* 253 */           return stickerFormat; 
/*     */       } 
/* 255 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\MessageSticker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */