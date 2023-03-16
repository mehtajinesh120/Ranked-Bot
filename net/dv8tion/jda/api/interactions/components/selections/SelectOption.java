/*     */ package net.dv8tion.jda.api.interactions.components.selections;
/*     */ 
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Emoji;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.api.utils.data.SerializableData;
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
/*     */ public class SelectOption
/*     */   implements SerializableData
/*     */ {
/*     */   public static final int LABEL_MAX_LENGTH = 100;
/*     */   public static final int VALUE_MAX_LENGTH = 100;
/*     */   public static final int DESCRIPTION_MAX_LENGTH = 100;
/*     */   private final String label;
/*     */   private final String value;
/*     */   private final String description;
/*     */   private final boolean isDefault;
/*     */   private final Emoji emoji;
/*     */   
/*     */   protected SelectOption(@Nonnull String label, @Nonnull String value) {
/*  67 */     this(label, value, null, false, null);
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
/*     */   protected SelectOption(@Nonnull String label, @Nonnull String value, @Nullable String description, boolean isDefault, @Nullable Emoji emoji) {
/*  90 */     Checks.notEmpty(label, "Label");
/*  91 */     Checks.notEmpty(value, "Value");
/*  92 */     Checks.notLonger(label, 100, "Label");
/*  93 */     Checks.notLonger(value, 100, "Value");
/*  94 */     if (description != null)
/*  95 */       Checks.notLonger(description, 100, "Description"); 
/*  96 */     this.label = label;
/*  97 */     this.value = value;
/*  98 */     this.description = description;
/*  99 */     this.isDefault = isDefault;
/* 100 */     this.emoji = emoji;
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public static SelectOption of(@Nonnull String label, @Nonnull String value) {
/* 122 */     return new SelectOption(label, value);
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
/*     */   @CheckReturnValue
/*     */   public SelectOption withLabel(@Nonnull String label) {
/* 140 */     return new SelectOption(label, this.value, this.description, this.isDefault, this.emoji);
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
/*     */   @CheckReturnValue
/*     */   public SelectOption withValue(@Nonnull String value) {
/* 159 */     return new SelectOption(this.label, value, this.description, this.isDefault, this.emoji);
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
/*     */   @CheckReturnValue
/*     */   public SelectOption withDescription(@Nullable String description) {
/* 179 */     return new SelectOption(this.label, this.value, description, this.isDefault, this.emoji);
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
/*     */   @CheckReturnValue
/*     */   public SelectOption withDefault(boolean isDefault) {
/* 195 */     return new SelectOption(this.label, this.value, this.description, isDefault, this.emoji);
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
/*     */   @CheckReturnValue
/*     */   public SelectOption withEmoji(@Nullable Emoji emoji) {
/* 211 */     return new SelectOption(this.label, this.value, this.description, this.isDefault, emoji);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getLabel() {
/* 222 */     return this.label;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getValue() {
/* 233 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDescription() {
/* 244 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefault() {
/* 254 */     return this.isDefault;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Emoji getEmoji() {
/* 265 */     return this.emoji;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/* 272 */     DataObject object = DataObject.empty();
/* 273 */     object.put("label", this.label);
/* 274 */     object.put("value", this.value);
/* 275 */     object.put("default", Boolean.valueOf(this.isDefault));
/* 276 */     if (this.emoji != null)
/* 277 */       object.put("emoji", this.emoji); 
/* 278 */     if (this.description != null && !this.description.isEmpty())
/* 279 */       object.put("description", this.description); 
/* 280 */     return object;
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
/*     */   @CheckReturnValue
/*     */   public static SelectOption fromData(@Nonnull DataObject data) {
/* 300 */     Checks.notNull(data, "DataObject");
/* 301 */     return new SelectOption(data
/* 302 */         .getString("label"), data
/* 303 */         .getString("value"), data
/* 304 */         .getString("description", null), data
/* 305 */         .getBoolean("default", false), data
/* 306 */         .optObject("emoji").map(Emoji::fromData).orElse(null));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\components\selections\SelectOption.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */