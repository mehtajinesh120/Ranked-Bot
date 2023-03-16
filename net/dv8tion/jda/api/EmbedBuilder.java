/*     */ package net.dv8tion.jda.api;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.time.DateTimeException;
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.time.ZoneOffset;
/*     */ import java.time.temporal.TemporalAccessor;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.EmbedType;
/*     */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*     */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
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
/*     */ public class EmbedBuilder
/*     */ {
/*     */   public static final String ZERO_WIDTH_SPACE = "‎";
/*  46 */   public static final Pattern URL_PATTERN = Pattern.compile("\\s*(https?|attachment)://\\S+\\s*", 2);
/*     */   
/*  48 */   private final List<MessageEmbed.Field> fields = new LinkedList<>();
/*  49 */   private final StringBuilder description = new StringBuilder();
/*  50 */   private int color = 536870911;
/*     */ 
/*     */   
/*     */   private String url;
/*     */ 
/*     */   
/*     */   private String title;
/*     */ 
/*     */   
/*     */   private OffsetDateTime timestamp;
/*     */ 
/*     */   
/*     */   private MessageEmbed.Thumbnail thumbnail;
/*     */ 
/*     */   
/*     */   private MessageEmbed.AuthorInfo author;
/*     */   
/*     */   private MessageEmbed.Footer footer;
/*     */   
/*     */   private MessageEmbed.ImageInfo image;
/*     */ 
/*     */   
/*     */   public EmbedBuilder(@Nullable EmbedBuilder builder) {
/*  73 */     copyFrom(builder);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EmbedBuilder(@Nullable MessageEmbed embed) {
/*  84 */     copyFrom(embed);
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
/*     */   public MessageEmbed build() {
/*  99 */     if (isEmpty())
/* 100 */       throw new IllegalStateException("Cannot build an empty embed!"); 
/* 101 */     if (this.description.length() > 4096)
/* 102 */       throw new IllegalStateException(Helpers.format("Description is longer than %d! Please limit your input!", new Object[] { Integer.valueOf(4096) })); 
/* 103 */     if (length() > 6000)
/* 104 */       throw new IllegalStateException("Cannot build an embed with more than 6000 characters!"); 
/* 105 */     String description = (this.description.length() < 1) ? null : this.description.toString();
/*     */     
/* 107 */     return EntityBuilder.createMessageEmbed(this.url, this.title, description, EmbedType.RICH, this.timestamp, this.color, this.thumbnail, null, this.author, null, this.footer, this.image, new LinkedList<>(this.fields));
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
/*     */   public EmbedBuilder clear() {
/* 120 */     this.description.setLength(0);
/* 121 */     this.fields.clear();
/* 122 */     this.url = null;
/* 123 */     this.title = null;
/* 124 */     this.timestamp = null;
/* 125 */     this.color = 536870911;
/* 126 */     this.thumbnail = null;
/* 127 */     this.author = null;
/* 128 */     this.footer = null;
/* 129 */     this.image = null;
/* 130 */     return this;
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
/*     */   public void copyFrom(@Nullable EmbedBuilder builder) {
/* 142 */     if (builder != null) {
/*     */       
/* 144 */       setDescription(builder.description.toString());
/* 145 */       clearFields();
/* 146 */       this.fields.addAll(builder.fields);
/* 147 */       this.url = builder.url;
/* 148 */       this.title = builder.title;
/* 149 */       this.timestamp = builder.timestamp;
/* 150 */       this.color = builder.color;
/* 151 */       this.thumbnail = builder.thumbnail;
/* 152 */       this.author = builder.author;
/* 153 */       this.footer = builder.footer;
/* 154 */       this.image = builder.image;
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
/*     */   public void copyFrom(@Nullable MessageEmbed embed) {
/* 167 */     if (embed != null) {
/*     */       
/* 169 */       setDescription(embed.getDescription());
/* 170 */       clearFields();
/* 171 */       this.fields.addAll(embed.getFields());
/* 172 */       this.url = embed.getUrl();
/* 173 */       this.title = embed.getTitle();
/* 174 */       this.timestamp = embed.getTimestamp();
/* 175 */       this.color = embed.getColorRaw();
/* 176 */       this.thumbnail = embed.getThumbnail();
/* 177 */       this.author = embed.getAuthor();
/* 178 */       this.footer = embed.getFooter();
/* 179 */       this.image = embed.getImage();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 190 */     return ((this.title == null || this.title.trim().isEmpty()) && this.timestamp == null && this.thumbnail == null && this.author == null && this.footer == null && this.image == null && this.color == 536870911 && this.description
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 197 */       .length() == 0 && this.fields
/* 198 */       .isEmpty());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 209 */     int length = this.description.toString().trim().length();
/* 210 */     synchronized (this.fields) {
/*     */       
/* 212 */       length = ((Integer)this.fields.stream().map(f -> Integer.valueOf(f.getName().length() + f.getValue().length())).reduce(Integer.valueOf(length), Integer::sum)).intValue();
/*     */     } 
/* 214 */     if (this.title != null)
/* 215 */       length += this.title.length(); 
/* 216 */     if (this.author != null)
/* 217 */       length += this.author.getName().length(); 
/* 218 */     if (this.footer != null)
/* 219 */       length += this.footer.getText().length(); 
/* 220 */     return length;
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
/*     */   public boolean isValidLength() {
/* 233 */     int length = length();
/* 234 */     return (length <= 6000);
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
/*     */   @Nonnull
/*     */   public EmbedBuilder setTitle(@Nullable String title) {
/* 258 */     return setTitle(title, null);
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
/*     */   
/*     */   @Nonnull
/*     */   public EmbedBuilder setTitle(@Nullable String title, @Nullable String url) {
/* 287 */     if (title == null) {
/*     */       
/* 289 */       this.title = null;
/* 290 */       this.url = null;
/*     */     }
/*     */     else {
/*     */       
/* 294 */       Checks.notEmpty(title, "Title");
/* 295 */       Checks.check((title.length() <= 256), "Title cannot be longer than %d characters.", Integer.valueOf(256));
/* 296 */       if (Helpers.isBlank(url))
/* 297 */         url = null; 
/* 298 */       urlCheck(url);
/*     */       
/* 300 */       this.title = title;
/* 301 */       this.url = url;
/*     */     } 
/* 303 */     return this;
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
/*     */   public StringBuilder getDescriptionBuilder() {
/* 316 */     return this.description;
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
/*     */   public final EmbedBuilder setDescription(@Nullable CharSequence description) {
/* 336 */     this.description.setLength(0);
/* 337 */     if (description != null && description.length() >= 1)
/* 338 */       appendDescription(description); 
/* 339 */     return this;
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
/*     */   @Nonnull
/*     */   public EmbedBuilder appendDescription(@Nonnull CharSequence description) {
/* 362 */     Checks.notNull(description, "description");
/* 363 */     Checks.check((this.description.length() + description.length() <= 4096), "Description cannot be longer than %d characters.", 
/* 364 */         Integer.valueOf(4096));
/* 365 */     this.description.append(description);
/* 366 */     return this;
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
/*     */   public EmbedBuilder setTimestamp(@Nullable TemporalAccessor temporal) {
/* 385 */     if (temporal == null) {
/*     */       
/* 387 */       this.timestamp = null;
/*     */     }
/* 389 */     else if (temporal instanceof OffsetDateTime) {
/*     */       
/* 391 */       this.timestamp = (OffsetDateTime)temporal;
/*     */     } else {
/*     */       ZoneOffset offset;
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 398 */         offset = ZoneOffset.from(temporal);
/*     */       }
/* 400 */       catch (DateTimeException ignore) {
/*     */         
/* 402 */         offset = ZoneOffset.UTC;
/*     */       } 
/*     */       
/*     */       try {
/* 406 */         LocalDateTime ldt = LocalDateTime.from(temporal);
/* 407 */         this.timestamp = OffsetDateTime.of(ldt, offset);
/*     */       }
/* 409 */       catch (DateTimeException ignore) {
/*     */ 
/*     */         
/*     */         try {
/* 413 */           Instant instant = Instant.from(temporal);
/* 414 */           this.timestamp = OffsetDateTime.ofInstant(instant, offset);
/*     */         }
/* 416 */         catch (DateTimeException ex) {
/*     */           
/* 418 */           throw new DateTimeException("Unable to obtain OffsetDateTime from TemporalAccessor: " + temporal + " of type " + temporal
/* 419 */               .getClass().getName(), ex);
/*     */         } 
/*     */       } 
/*     */     } 
/* 423 */     return this;
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
/*     */   public EmbedBuilder setColor(@Nullable Color color) {
/* 442 */     this.color = (color == null) ? 536870911 : color.getRGB();
/* 443 */     return this;
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
/*     */   public EmbedBuilder setColor(int color) {
/* 461 */     this.color = color;
/* 462 */     return this;
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
/*     */ 
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
/*     */   public EmbedBuilder setThumbnail(@Nullable String url) {
/* 500 */     if (url == null) {
/*     */       
/* 502 */       this.thumbnail = null;
/*     */     }
/*     */     else {
/*     */       
/* 506 */       urlCheck(url);
/* 507 */       this.thumbnail = new MessageEmbed.Thumbnail(url, null, 0, 0);
/*     */     } 
/* 509 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public EmbedBuilder setImage(@Nullable String url) {
/* 549 */     if (url == null) {
/*     */       
/* 551 */       this.image = null;
/*     */     }
/*     */     else {
/*     */       
/* 555 */       urlCheck(url);
/* 556 */       this.image = new MessageEmbed.ImageInfo(url, null, 0, 0);
/*     */     } 
/* 558 */     return this;
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
/*     */   public EmbedBuilder setAuthor(@Nullable String name) {
/* 580 */     return setAuthor(name, null, null);
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
/*     */   
/*     */   @Nonnull
/*     */   public EmbedBuilder setAuthor(@Nullable String name, @Nullable String url) {
/* 609 */     return setAuthor(name, url, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public EmbedBuilder setAuthor(@Nullable String name, @Nullable String url, @Nullable String iconUrl) {
/* 659 */     if (name == null) {
/*     */       
/* 661 */       this.author = null;
/*     */     }
/*     */     else {
/*     */       
/* 665 */       Checks.check((name.length() <= 256), "Name cannot be longer than %d characters.", Integer.valueOf(256));
/* 666 */       urlCheck(url);
/* 667 */       urlCheck(iconUrl);
/* 668 */       this.author = new MessageEmbed.AuthorInfo(name, url, iconUrl, null);
/*     */     } 
/* 670 */     return this;
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
/*     */   public EmbedBuilder setFooter(@Nullable String text) {
/* 690 */     return setFooter(text, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public EmbedBuilder setFooter(@Nullable String text, @Nullable String iconUrl) {
/* 734 */     if (text == null) {
/*     */       
/* 736 */       this.footer = null;
/*     */     }
/*     */     else {
/*     */       
/* 740 */       Checks.check((text.length() <= 2048), "Text cannot be longer than %d characters.", Integer.valueOf(2048));
/* 741 */       urlCheck(iconUrl);
/* 742 */       this.footer = new MessageEmbed.Footer(text, iconUrl, null);
/*     */     } 
/* 744 */     return this;
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
/*     */   public EmbedBuilder addField(@Nullable MessageEmbed.Field field) {
/* 759 */     return (field == null) ? this : addField(field.getName(), field.getValue(), field.isInline());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EmbedBuilder addField(@Nullable String name, @Nullable String value, boolean inline) {
/* 792 */     if (name == null && value == null)
/* 793 */       return this; 
/* 794 */     this.fields.add(new MessageEmbed.Field(name, value, inline));
/* 795 */     return this;
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
/*     */   public EmbedBuilder addBlankField(boolean inline) {
/* 812 */     this.fields.add(new MessageEmbed.Field("‎", "‎", inline));
/* 813 */     return this;
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
/*     */   public EmbedBuilder clearFields() {
/* 827 */     this.fields.clear();
/* 828 */     return this;
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
/*     */   public List<MessageEmbed.Field> getFields() {
/* 842 */     return this.fields;
/*     */   }
/*     */ 
/*     */   
/*     */   private void urlCheck(@Nullable String url) {
/* 847 */     if (url != null) {
/*     */       
/* 849 */       Checks.check((url.length() <= 2000), "URL cannot be longer than %d characters.", Integer.valueOf(2000));
/* 850 */       Checks.check(URL_PATTERN.matcher(url).matches(), "URL must be a valid http(s) or attachment url.");
/*     */     } 
/*     */   }
/*     */   
/*     */   public EmbedBuilder() {}
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\EmbedBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */