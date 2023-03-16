/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import java.util.regex.Matcher;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.api.utils.data.SerializableData;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.EncodingUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Emoji
/*     */   implements SerializableData, IMentionable
/*     */ {
/*     */   private final String name;
/*     */   private final long id;
/*     */   private final boolean animated;
/*     */   
/*     */   private Emoji(String name, long id, boolean animated) {
/*  45 */     this.name = name;
/*  46 */     this.id = id;
/*  47 */     this.animated = animated;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/*  59 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/*  65 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAnimated() {
/*  75 */     return this.animated;
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
/*     */   public boolean isUnicode() {
/*  87 */     return (this.id == 0L);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCustom() {
/*  97 */     return !isUnicode();
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
/*     */   public static Emoji fromUnicode(@Nonnull String code) {
/* 115 */     Checks.notEmpty(code, "Unicode");
/* 116 */     if (code.startsWith("U+") || code.startsWith("u+")) {
/*     */       
/* 118 */       StringBuilder emoji = new StringBuilder();
/* 119 */       String[] codepoints = code.trim().split("\\s*[uU]\\+");
/* 120 */       for (String codepoint : codepoints)
/* 121 */         emoji.append(codepoint.isEmpty() ? "" : EncodingUtil.decodeCodepoint("U+" + codepoint)); 
/* 122 */       code = emoji.toString();
/*     */     } 
/* 124 */     return new Emoji(code, 0L, false);
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
/*     */   public static Emoji fromEmote(@Nonnull String name, long id, boolean animated) {
/* 145 */     Checks.notEmpty(name, "Name");
/* 146 */     return new Emoji(name, id, animated);
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
/*     */   public static Emoji fromEmote(@Nonnull Emote emote) {
/* 163 */     Checks.notNull(emote, "Emote");
/* 164 */     return fromEmote(emote.getName(), emote.getIdLong(), emote.isAnimated());
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
/*     */   @Nonnull
/*     */   public static Emoji fromMarkdown(@Nonnull String code) {
/* 194 */     Matcher matcher = Message.MentionType.EMOTE.getPattern().matcher(code);
/* 195 */     if (matcher.matches()) {
/* 196 */       return fromEmote(matcher.group(1), Long.parseUnsignedLong(matcher.group(2)), code.startsWith("<a"));
/*     */     }
/* 198 */     return fromUnicode(code);
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
/*     */   public static Emoji fromData(@Nonnull DataObject emoji) {
/* 214 */     return new Emoji(emoji.getString("name"), emoji
/* 215 */         .getUnsignedLong("id", 0L), emoji
/* 216 */         .getBoolean("animated"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/* 223 */     DataObject json = DataObject.empty().put("name", this.name);
/* 224 */     if (this.id != 0L)
/*     */     {
/* 226 */       json.put("id", Long.valueOf(this.id))
/* 227 */         .put("animated", Boolean.valueOf(this.animated));
/*     */     }
/* 229 */     return json;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getAsMention() {
/* 236 */     return (this.id == 0L) ? this.name : String.format("<%s:%s:%s>", new Object[] { this.animated ? "a" : "", this.name, Long.toUnsignedString(this.id) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 242 */     return Objects.hash(new Object[] { this.name, Long.valueOf(this.id), Boolean.valueOf(this.animated) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 248 */     if (obj == this) return true; 
/* 249 */     if (!(obj instanceof Emoji)) return false; 
/* 250 */     Emoji other = (Emoji)obj;
/* 251 */     return (other.id == this.id && other.animated == this.animated && Objects.equals(other.name, this.name));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 257 */     return "E:" + this.name + "(" + this.id + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\Emoji.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */