/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.api.utils.data.SerializableData;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MessageEmbed
/*     */   implements SerializableData
/*     */ {
/*     */   public static final int TITLE_MAX_LENGTH = 256;
/*     */   public static final int AUTHOR_MAX_LENGTH = 256;
/*     */   public static final int VALUE_MAX_LENGTH = 1024;
/*     */   public static final int DESCRIPTION_MAX_LENGTH = 4096;
/*     */   public static final int TEXT_MAX_LENGTH = 2048;
/*     */   public static final int URL_MAX_LENGTH = 2000;
/*     */   public static final int EMBED_MAX_LENGTH_BOT = 6000;
/*     */   public static final int EMBED_MAX_LENGTH_CLIENT = 2000;
/* 113 */   protected final Object mutex = new Object();
/*     */   
/*     */   protected final String url;
/*     */   
/*     */   protected final String title;
/*     */   protected final String description;
/*     */   protected final EmbedType type;
/*     */   protected final OffsetDateTime timestamp;
/*     */   protected final int color;
/*     */   protected final Thumbnail thumbnail;
/*     */   protected final Provider siteProvider;
/*     */   protected final AuthorInfo author;
/*     */   protected final VideoInfo videoInfo;
/*     */   protected final Footer footer;
/*     */   protected final ImageInfo image;
/*     */   protected final List<Field> fields;
/* 129 */   protected volatile int length = -1;
/* 130 */   protected volatile DataObject json = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MessageEmbed(String url, String title, String description, EmbedType type, OffsetDateTime timestamp, int color, Thumbnail thumbnail, Provider siteProvider, AuthorInfo author, VideoInfo videoInfo, Footer footer, ImageInfo image, List<Field> fields) {
/* 137 */     this.url = url;
/* 138 */     this.title = title;
/* 139 */     this.description = description;
/* 140 */     this.type = type;
/* 141 */     this.timestamp = timestamp;
/* 142 */     this.color = color;
/* 143 */     this.thumbnail = thumbnail;
/* 144 */     this.siteProvider = siteProvider;
/* 145 */     this.author = author;
/* 146 */     this.videoInfo = videoInfo;
/* 147 */     this.footer = footer;
/* 148 */     this.image = image;
/* 149 */     this
/* 150 */       .fields = (fields != null && !fields.isEmpty()) ? Collections.<Field>unmodifiableList(fields) : Collections.<Field>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getUrl() {
/* 161 */     return this.url;
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
/*     */   public String getTitle() {
/* 174 */     return this.title;
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
/*     */   public String getDescription() {
/* 187 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EmbedType getType() {
/* 198 */     return this.type;
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
/*     */   public Thumbnail getThumbnail() {
/* 211 */     return this.thumbnail;
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
/*     */   public Provider getSiteProvider() {
/* 224 */     return this.siteProvider;
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
/*     */   public AuthorInfo getAuthor() {
/* 237 */     return this.author;
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
/*     */   @Nullable
/*     */   public VideoInfo getVideoInfo() {
/* 252 */     return this.videoInfo;
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
/*     */   public Footer getFooter() {
/* 265 */     return this.footer;
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
/*     */   public ImageInfo getImage() {
/* 277 */     return this.image;
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
/*     */   public List<Field> getFields() {
/* 292 */     return this.fields;
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
/*     */   public Color getColor() {
/* 304 */     return (this.color != 536870911) ? new Color(this.color) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColorRaw() {
/* 315 */     return this.color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public OffsetDateTime getTimestamp() {
/* 326 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 336 */     return (this.color == 536870911 && this.timestamp == null && 
/*     */       
/* 338 */       getImage() == null && 
/* 339 */       getThumbnail() == null && 
/* 340 */       getLength() == 0);
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
/*     */   public int getLength() {
/* 352 */     if (this.length > -1)
/* 353 */       return this.length; 
/* 354 */     synchronized (this.mutex) {
/*     */       
/* 356 */       if (this.length > -1)
/* 357 */         return this.length; 
/* 358 */       this.length = 0;
/*     */       
/* 360 */       if (this.title != null)
/* 361 */         this.length += Helpers.codePointLength(this.title); 
/* 362 */       if (this.description != null)
/* 363 */         this.length += Helpers.codePointLength(this.description.trim()); 
/* 364 */       if (this.author != null)
/* 365 */         this.length += Helpers.codePointLength(this.author.getName()); 
/* 366 */       if (this.footer != null)
/* 367 */         this.length += Helpers.codePointLength(this.footer.getText()); 
/* 368 */       if (this.fields != null)
/*     */       {
/* 370 */         for (Field f : this.fields) {
/* 371 */           this.length += Helpers.codePointLength(f.getName()) + Helpers.codePointLength(f.getValue());
/*     */         }
/*     */       }
/* 374 */       return this.length;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSendable() {
/* 389 */     if (isEmpty()) {
/* 390 */       return false;
/*     */     }
/* 392 */     int length = getLength();
/* 393 */     return (length <= 6000);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 399 */     if (!(obj instanceof MessageEmbed))
/* 400 */       return false; 
/* 401 */     if (obj == this)
/* 402 */       return true; 
/* 403 */     MessageEmbed other = (MessageEmbed)obj;
/* 404 */     return (Objects.equals(this.url, other.url) && 
/* 405 */       Objects.equals(this.title, other.title) && 
/* 406 */       Objects.equals(this.description, other.description) && 
/* 407 */       Objects.equals(this.type, other.type) && 
/* 408 */       Objects.equals(this.thumbnail, other.thumbnail) && 
/* 409 */       Objects.equals(this.siteProvider, other.siteProvider) && 
/* 410 */       Objects.equals(this.author, other.author) && 
/* 411 */       Objects.equals(this.videoInfo, other.videoInfo) && 
/* 412 */       Objects.equals(this.footer, other.footer) && 
/* 413 */       Objects.equals(this.image, other.image) && (this.color & 0xFFFFFF) == (other.color & 0xFFFFFF) && 
/*     */       
/* 415 */       Objects.equals(this.timestamp, other.timestamp) && 
/* 416 */       Helpers.deepEquals(this.fields, other.fields));
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
/*     */   public DataObject toData() {
/* 429 */     if (this.json != null)
/* 430 */       return this.json; 
/* 431 */     synchronized (this.mutex) {
/*     */       
/* 433 */       if (this.json != null)
/* 434 */         return this.json; 
/* 435 */       DataObject obj = DataObject.empty();
/* 436 */       if (this.url != null)
/* 437 */         obj.put("url", this.url); 
/* 438 */       if (this.title != null)
/* 439 */         obj.put("title", this.title); 
/* 440 */       if (this.description != null)
/* 441 */         obj.put("description", this.description); 
/* 442 */       if (this.timestamp != null)
/* 443 */         obj.put("timestamp", this.timestamp.format(DateTimeFormatter.ISO_INSTANT)); 
/* 444 */       if (this.color != 536870911)
/* 445 */         obj.put("color", Integer.valueOf(this.color & 0xFFFFFF)); 
/* 446 */       if (this.thumbnail != null)
/* 447 */         obj.put("thumbnail", DataObject.empty().put("url", this.thumbnail.getUrl())); 
/* 448 */       if (this.siteProvider != null) {
/*     */         
/* 450 */         DataObject siteProviderObj = DataObject.empty();
/* 451 */         if (this.siteProvider.getName() != null)
/* 452 */           siteProviderObj.put("name", this.siteProvider.getName()); 
/* 453 */         if (this.siteProvider.getUrl() != null)
/* 454 */           siteProviderObj.put("url", this.siteProvider.getUrl()); 
/* 455 */         obj.put("provider", siteProviderObj);
/*     */       } 
/* 457 */       if (this.author != null) {
/*     */         
/* 459 */         DataObject authorObj = DataObject.empty();
/* 460 */         if (this.author.getName() != null)
/* 461 */           authorObj.put("name", this.author.getName()); 
/* 462 */         if (this.author.getUrl() != null)
/* 463 */           authorObj.put("url", this.author.getUrl()); 
/* 464 */         if (this.author.getIconUrl() != null)
/* 465 */           authorObj.put("icon_url", this.author.getIconUrl()); 
/* 466 */         obj.put("author", authorObj);
/*     */       } 
/* 468 */       if (this.videoInfo != null)
/* 469 */         obj.put("video", DataObject.empty().put("url", this.videoInfo.getUrl())); 
/* 470 */       if (this.footer != null) {
/*     */         
/* 472 */         DataObject footerObj = DataObject.empty();
/* 473 */         if (this.footer.getText() != null)
/* 474 */           footerObj.put("text", this.footer.getText()); 
/* 475 */         if (this.footer.getIconUrl() != null)
/* 476 */           footerObj.put("icon_url", this.footer.getIconUrl()); 
/* 477 */         obj.put("footer", footerObj);
/*     */       } 
/* 479 */       if (this.image != null)
/* 480 */         obj.put("image", DataObject.empty().put("url", this.image.getUrl())); 
/* 481 */       if (!this.fields.isEmpty()) {
/*     */         
/* 483 */         DataArray fieldsArray = DataArray.empty();
/* 484 */         for (Field field : this.fields)
/*     */         {
/* 486 */           fieldsArray
/* 487 */             .add(DataObject.empty()
/* 488 */               .put("name", field.getName())
/* 489 */               .put("value", field.getValue())
/* 490 */               .put("inline", Boolean.valueOf(field.isInline())));
/*     */         }
/* 492 */         obj.put("fields", fieldsArray);
/*     */       } 
/* 494 */       return this.json = obj;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Thumbnail
/*     */   {
/*     */     protected final String url;
/*     */     
/*     */     protected final String proxyUrl;
/*     */     
/*     */     protected final int width;
/*     */     
/*     */     protected final int height;
/*     */ 
/*     */     
/*     */     public Thumbnail(String url, String proxyUrl, int width, int height) {
/* 511 */       this.url = url;
/* 512 */       this.proxyUrl = proxyUrl;
/* 513 */       this.width = width;
/* 514 */       this.height = height;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getUrl() {
/* 525 */       return this.url;
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
/*     */     public String getProxyUrl() {
/* 537 */       return this.proxyUrl;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getWidth() {
/* 547 */       return this.width;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getHeight() {
/* 557 */       return this.height;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 563 */       if (!(obj instanceof Thumbnail))
/* 564 */         return false; 
/* 565 */       Thumbnail thumbnail = (Thumbnail)obj;
/* 566 */       return (thumbnail == this || (Objects.equals(thumbnail.url, this.url) && 
/* 567 */         Objects.equals(thumbnail.proxyUrl, this.proxyUrl) && thumbnail.width == this.width && thumbnail.height == this.height));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Provider
/*     */   {
/*     */     protected final String name;
/*     */ 
/*     */     
/*     */     protected final String url;
/*     */ 
/*     */ 
/*     */     
/*     */     public Provider(String name, String url) {
/* 584 */       this.name = name;
/* 585 */       this.url = url;
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
/*     */     @Nullable
/*     */     public String getName() {
/* 598 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getUrl() {
/* 609 */       return this.url;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 615 */       if (!(obj instanceof Provider))
/* 616 */         return false; 
/* 617 */       Provider provider = (Provider)obj;
/* 618 */       return (provider == this || (Objects.equals(provider.name, this.name) && 
/* 619 */         Objects.equals(provider.url, this.url)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class VideoInfo
/*     */   {
/*     */     protected final String url;
/*     */ 
/*     */     
/*     */     protected final int width;
/*     */     
/*     */     protected final int height;
/*     */ 
/*     */     
/*     */     public VideoInfo(String url, int width, int height) {
/* 636 */       this.url = url;
/* 637 */       this.width = width;
/* 638 */       this.height = height;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getUrl() {
/* 649 */       return this.url;
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
/*     */     public int getWidth() {
/* 662 */       return this.width;
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
/*     */     public int getHeight() {
/* 676 */       return this.height;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 682 */       if (!(obj instanceof VideoInfo))
/* 683 */         return false; 
/* 684 */       VideoInfo video = (VideoInfo)obj;
/* 685 */       return (video == this || (Objects.equals(video.url, this.url) && video.width == this.width && video.height == this.height));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ImageInfo
/*     */   {
/*     */     protected final String url;
/*     */     
/*     */     protected final String proxyUrl;
/*     */     
/*     */     protected final int width;
/*     */     
/*     */     protected final int height;
/*     */ 
/*     */     
/*     */     public ImageInfo(String url, String proxyUrl, int width, int height) {
/* 703 */       this.url = url;
/* 704 */       this.proxyUrl = proxyUrl;
/* 705 */       this.width = width;
/* 706 */       this.height = height;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getUrl() {
/* 717 */       return this.url;
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
/*     */     public String getProxyUrl() {
/* 729 */       return this.proxyUrl;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getWidth() {
/* 739 */       return this.width;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getHeight() {
/* 749 */       return this.height;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 755 */       if (!(obj instanceof ImageInfo))
/* 756 */         return false; 
/* 757 */       ImageInfo image = (ImageInfo)obj;
/* 758 */       return (image == this || (Objects.equals(image.url, this.url) && 
/* 759 */         Objects.equals(image.proxyUrl, this.proxyUrl) && image.width == this.width && image.height == this.height));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class AuthorInfo
/*     */   {
/*     */     protected final String name;
/*     */ 
/*     */     
/*     */     protected final String url;
/*     */     
/*     */     protected final String iconUrl;
/*     */     
/*     */     protected final String proxyIconUrl;
/*     */ 
/*     */     
/*     */     public AuthorInfo(String name, String url, String iconUrl, String proxyIconUrl) {
/* 778 */       this.name = name;
/* 779 */       this.url = url;
/* 780 */       this.iconUrl = iconUrl;
/* 781 */       this.proxyIconUrl = proxyIconUrl;
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
/*     */     public String getName() {
/* 793 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getUrl() {
/* 804 */       return this.url;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getIconUrl() {
/* 815 */       return this.iconUrl;
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
/*     */     public String getProxyIconUrl() {
/* 827 */       return this.proxyIconUrl;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 833 */       if (!(obj instanceof AuthorInfo))
/* 834 */         return false; 
/* 835 */       AuthorInfo author = (AuthorInfo)obj;
/* 836 */       return (author == this || (Objects.equals(author.name, this.name) && 
/* 837 */         Objects.equals(author.url, this.url) && 
/* 838 */         Objects.equals(author.iconUrl, this.iconUrl) && 
/* 839 */         Objects.equals(author.proxyIconUrl, this.proxyIconUrl)));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Footer
/*     */   {
/*     */     protected final String text;
/*     */     
/*     */     protected final String iconUrl;
/*     */     
/*     */     protected final String proxyIconUrl;
/*     */ 
/*     */     
/*     */     public Footer(String text, String iconUrl, String proxyIconUrl) {
/* 854 */       this.text = text;
/* 855 */       this.iconUrl = iconUrl;
/* 856 */       this.proxyIconUrl = proxyIconUrl;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getText() {
/* 867 */       return this.text;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getIconUrl() {
/* 878 */       return this.iconUrl;
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
/*     */     public String getProxyIconUrl() {
/* 890 */       return this.proxyIconUrl;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 896 */       if (!(obj instanceof Footer))
/* 897 */         return false; 
/* 898 */       Footer footer = (Footer)obj;
/* 899 */       return (footer == this || (Objects.equals(footer.text, this.text) && 
/* 900 */         Objects.equals(footer.iconUrl, this.iconUrl) && 
/* 901 */         Objects.equals(footer.proxyIconUrl, this.proxyIconUrl)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Field
/*     */   {
/*     */     protected final String name;
/*     */ 
/*     */ 
/*     */     
/*     */     protected final String value;
/*     */ 
/*     */     
/*     */     protected final boolean inline;
/*     */ 
/*     */ 
/*     */     
/*     */     public Field(String name, String value, boolean inline, boolean checked) {
/* 922 */       if (checked) {
/*     */         
/* 924 */         if (name == null || value == null)
/* 925 */           throw new IllegalArgumentException("Both Name and Value must be set!"); 
/* 926 */         if (name.length() > 256)
/* 927 */           throw new IllegalArgumentException("Name cannot be longer than 256 characters."); 
/* 928 */         if (value.length() > 1024)
/* 929 */           throw new IllegalArgumentException("Value cannot be longer than 1024 characters."); 
/* 930 */         name = name.trim();
/* 931 */         value = value.trim();
/* 932 */         if (name.isEmpty()) {
/* 933 */           this.name = "‎";
/*     */         } else {
/* 935 */           this.name = name;
/* 936 */         }  if (value.isEmpty()) {
/* 937 */           this.value = "‎";
/*     */         } else {
/* 939 */           this.value = value;
/*     */         } 
/*     */       } else {
/*     */         
/* 943 */         this.name = name;
/* 944 */         this.value = value;
/*     */       } 
/* 946 */       this.inline = inline;
/*     */     }
/*     */ 
/*     */     
/*     */     public Field(String name, String value, boolean inline) {
/* 951 */       this(name, value, inline, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getName() {
/* 962 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getValue() {
/* 973 */       return this.value;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isInline() {
/* 983 */       return this.inline;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 989 */       if (!(obj instanceof Field))
/* 990 */         return false; 
/* 991 */       Field field = (Field)obj;
/* 992 */       return (field == this || (field.inline == this.inline && 
/* 993 */         Objects.equals(field.name, this.name) && 
/* 994 */         Objects.equals(field.value, this.value)));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\MessageEmbed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */