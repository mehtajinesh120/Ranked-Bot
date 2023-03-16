/*     */ package org.jsoup.parser;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.Normalizer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Tag
/*     */   implements Cloneable
/*     */ {
/*  15 */   private static final Map<String, Tag> tags = new HashMap<>();
/*     */   
/*     */   private String tagName;
/*     */   private String normalName;
/*     */   private boolean isBlock = true;
/*     */   private boolean formatAsBlock = true;
/*     */   private boolean empty = false;
/*     */   private boolean selfClosing = false;
/*     */   private boolean preserveWhitespace = false;
/*     */   private boolean formList = false;
/*     */   private boolean formSubmit = false;
/*     */   
/*     */   private Tag(String tagName) {
/*  28 */     this.tagName = tagName;
/*  29 */     this.normalName = Normalizer.lowerCase(tagName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/*  38 */     return this.tagName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String normalName() {
/*  46 */     return this.normalName;
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
/*     */   public static Tag valueOf(String tagName, ParseSettings settings) {
/*  60 */     Validate.notNull(tagName);
/*  61 */     Tag tag = tags.get(tagName);
/*     */     
/*  63 */     if (tag == null) {
/*  64 */       tagName = settings.normalizeTag(tagName);
/*  65 */       Validate.notEmpty(tagName);
/*  66 */       String normalName = Normalizer.lowerCase(tagName);
/*  67 */       tag = tags.get(normalName);
/*     */       
/*  69 */       if (tag == null) {
/*     */         
/*  71 */         tag = new Tag(tagName);
/*  72 */         tag.isBlock = false;
/*  73 */       } else if (settings.preserveTagCase() && !tagName.equals(normalName)) {
/*  74 */         tag = tag.clone();
/*  75 */         tag.tagName = tagName;
/*     */       } 
/*     */     } 
/*  78 */     return tag;
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
/*     */   public static Tag valueOf(String tagName) {
/*  91 */     return valueOf(tagName, ParseSettings.preserveCase);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlock() {
/* 100 */     return this.isBlock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean formatAsBlock() {
/* 109 */     return this.formatAsBlock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInline() {
/* 118 */     return !this.isBlock;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 127 */     return this.empty;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSelfClosing() {
/* 136 */     return (this.empty || this.selfClosing);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isKnownTag() {
/* 145 */     return tags.containsKey(this.tagName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isKnownTag(String tagName) {
/* 155 */     return tags.containsKey(tagName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean preserveWhitespace() {
/* 164 */     return this.preserveWhitespace;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFormListed() {
/* 172 */     return this.formList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFormSubmittable() {
/* 180 */     return this.formSubmit;
/*     */   }
/*     */   
/*     */   Tag setSelfClosing() {
/* 184 */     this.selfClosing = true;
/* 185 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 190 */     if (this == o) return true; 
/* 191 */     if (!(o instanceof Tag)) return false;
/*     */     
/* 193 */     Tag tag = (Tag)o;
/*     */     
/* 195 */     if (!this.tagName.equals(tag.tagName)) return false; 
/* 196 */     if (this.empty != tag.empty) return false; 
/* 197 */     if (this.formatAsBlock != tag.formatAsBlock) return false; 
/* 198 */     if (this.isBlock != tag.isBlock) return false; 
/* 199 */     if (this.preserveWhitespace != tag.preserveWhitespace) return false; 
/* 200 */     if (this.selfClosing != tag.selfClosing) return false; 
/* 201 */     if (this.formList != tag.formList) return false; 
/* 202 */     return (this.formSubmit == tag.formSubmit);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 207 */     int result = this.tagName.hashCode();
/* 208 */     result = 31 * result + (this.isBlock ? 1 : 0);
/* 209 */     result = 31 * result + (this.formatAsBlock ? 1 : 0);
/* 210 */     result = 31 * result + (this.empty ? 1 : 0);
/* 211 */     result = 31 * result + (this.selfClosing ? 1 : 0);
/* 212 */     result = 31 * result + (this.preserveWhitespace ? 1 : 0);
/* 213 */     result = 31 * result + (this.formList ? 1 : 0);
/* 214 */     result = 31 * result + (this.formSubmit ? 1 : 0);
/* 215 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 220 */     return this.tagName;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Tag clone() {
/*     */     try {
/* 226 */       return (Tag)super.clone();
/* 227 */     } catch (CloneNotSupportedException e) {
/* 228 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 234 */   private static final String[] blockTags = new String[] { "html", "head", "body", "frameset", "script", "noscript", "style", "meta", "link", "title", "frame", "noframes", "section", "nav", "aside", "hgroup", "header", "footer", "p", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "pre", "div", "blockquote", "hr", "address", "figure", "figcaption", "form", "fieldset", "ins", "del", "dl", "dt", "dd", "li", "table", "caption", "thead", "tfoot", "tbody", "colgroup", "col", "tr", "th", "td", "video", "audio", "canvas", "details", "menu", "plaintext", "template", "article", "main", "svg", "math", "center", "template", "dir", "applet", "marquee", "listing" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 243 */   private static final String[] inlineTags = new String[] { "object", "base", "font", "tt", "i", "b", "u", "big", "small", "em", "strong", "dfn", "code", "samp", "kbd", "var", "cite", "abbr", "time", "acronym", "mark", "ruby", "rt", "rp", "a", "img", "br", "wbr", "map", "q", "sub", "sup", "bdo", "iframe", "embed", "span", "input", "select", "textarea", "label", "button", "optgroup", "option", "legend", "datalist", "keygen", "output", "progress", "meter", "area", "param", "source", "track", "summary", "command", "device", "area", "basefont", "bgsound", "menuitem", "param", "source", "track", "data", "bdi", "s", "strike", "nobr" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 251 */   private static final String[] emptyTags = new String[] { "meta", "link", "base", "frame", "img", "br", "wbr", "embed", "hr", "input", "keygen", "col", "command", "device", "area", "basefont", "bgsound", "menuitem", "param", "source", "track" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 256 */   private static final String[] formatAsInlineTags = new String[] { "title", "a", "p", "h1", "h2", "h3", "h4", "h5", "h6", "pre", "address", "li", "th", "td", "script", "style", "ins", "del", "s" };
/*     */ 
/*     */ 
/*     */   
/* 260 */   private static final String[] preserveWhitespaceTags = new String[] { "pre", "plaintext", "title", "textarea" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 265 */   private static final String[] formListedTags = new String[] { "button", "fieldset", "input", "keygen", "object", "output", "select", "textarea" };
/*     */ 
/*     */   
/* 268 */   private static final String[] formSubmitTags = new String[] { "input", "keygen", "object", "select", "textarea" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 274 */     for (String tagName : blockTags) {
/* 275 */       Tag tag = new Tag(tagName);
/* 276 */       register(tag);
/*     */     } 
/* 278 */     for (String tagName : inlineTags) {
/* 279 */       Tag tag = new Tag(tagName);
/* 280 */       tag.isBlock = false;
/* 281 */       tag.formatAsBlock = false;
/* 282 */       register(tag);
/*     */     } 
/*     */ 
/*     */     
/* 286 */     for (String tagName : emptyTags) {
/* 287 */       Tag tag = tags.get(tagName);
/* 288 */       Validate.notNull(tag);
/* 289 */       tag.empty = true;
/*     */     } 
/*     */     
/* 292 */     for (String tagName : formatAsInlineTags) {
/* 293 */       Tag tag = tags.get(tagName);
/* 294 */       Validate.notNull(tag);
/* 295 */       tag.formatAsBlock = false;
/*     */     } 
/*     */     
/* 298 */     for (String tagName : preserveWhitespaceTags) {
/* 299 */       Tag tag = tags.get(tagName);
/* 300 */       Validate.notNull(tag);
/* 301 */       tag.preserveWhitespace = true;
/*     */     } 
/*     */     
/* 304 */     for (String tagName : formListedTags) {
/* 305 */       Tag tag = tags.get(tagName);
/* 306 */       Validate.notNull(tag);
/* 307 */       tag.formList = true;
/*     */     } 
/*     */     
/* 310 */     for (String tagName : formSubmitTags) {
/* 311 */       Tag tag = tags.get(tagName);
/* 312 */       Validate.notNull(tag);
/* 313 */       tag.formSubmit = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void register(Tag tag) {
/* 318 */     tags.put(tag.tagName, tag);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\Tag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */