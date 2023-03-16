/*     */ package org.jsoup.nodes;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nullable;
/*     */ import org.jsoup.SerializationException;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.Normalizer;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Attribute
/*     */   implements Map.Entry<String, String>, Cloneable
/*     */ {
/*  19 */   private static final String[] booleanAttributes = new String[] { "allowfullscreen", "async", "autofocus", "checked", "compact", "declare", "default", "defer", "disabled", "formnovalidate", "hidden", "inert", "ismap", "itemscope", "multiple", "muted", "nohref", "noresize", "noshade", "novalidate", "nowrap", "open", "readonly", "required", "reversed", "seamless", "selected", "sortable", "truespeed", "typemustmatch" };
/*     */ 
/*     */ 
/*     */   
/*     */   private String key;
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private String val;
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Attributes parent;
/*     */ 
/*     */ 
/*     */   
/*     */   public Attribute(String key, @Nullable String value) {
/*  37 */     this(key, value, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Attribute(String key, @Nullable String val, @Nullable Attributes parent) {
/*  47 */     Validate.notNull(key);
/*  48 */     key = key.trim();
/*  49 */     Validate.notEmpty(key);
/*  50 */     this.key = key;
/*  51 */     this.val = val;
/*  52 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/*  61 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKey(String key) {
/*  69 */     Validate.notNull(key);
/*  70 */     key = key.trim();
/*  71 */     Validate.notEmpty(key);
/*  72 */     if (this.parent != null) {
/*  73 */       int i = this.parent.indexOfKey(this.key);
/*  74 */       if (i != -1)
/*  75 */         this.parent.keys[i] = key; 
/*     */     } 
/*  77 */     this.key = key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue() {
/*  86 */     return Attributes.checkNotNull(this.val);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasDeclaredValue() {
/*  94 */     return (this.val != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String setValue(@Nullable String val) {
/* 103 */     String oldVal = this.val;
/* 104 */     if (this.parent != null) {
/* 105 */       int i = this.parent.indexOfKey(this.key);
/* 106 */       if (i != -1) {
/* 107 */         oldVal = this.parent.get(this.key);
/* 108 */         this.parent.vals[i] = val;
/*     */       } 
/*     */     } 
/* 111 */     this.val = val;
/* 112 */     return Attributes.checkNotNull(oldVal);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String html() {
/* 120 */     StringBuilder sb = StringUtil.borrowBuilder();
/*     */     
/*     */     try {
/* 123 */       html(sb, (new Document("")).outputSettings());
/* 124 */     } catch (IOException exception) {
/* 125 */       throw new SerializationException(exception);
/*     */     } 
/* 127 */     return StringUtil.releaseBuilder(sb);
/*     */   }
/*     */   
/*     */   protected void html(Appendable accum, Document.OutputSettings out) throws IOException {
/* 131 */     html(this.key, this.val, accum, out);
/*     */   }
/*     */   
/*     */   protected static void html(String key, @Nullable String val, Appendable accum, Document.OutputSettings out) throws IOException {
/* 135 */     key = getValidKey(key, out.syntax());
/* 136 */     if (key == null)
/* 137 */       return;  htmlNoValidate(key, val, accum, out);
/*     */   }
/*     */ 
/*     */   
/*     */   static void htmlNoValidate(String key, @Nullable String val, Appendable accum, Document.OutputSettings out) throws IOException {
/* 142 */     accum.append(key);
/* 143 */     if (!shouldCollapseAttribute(key, val, out)) {
/* 144 */       accum.append("=\"");
/* 145 */       Entities.escape(accum, Attributes.checkNotNull(val), out, true, false, false, false);
/* 146 */       accum.append('"');
/*     */     } 
/*     */   }
/*     */   
/* 150 */   private static final Pattern xmlKeyValid = Pattern.compile("[a-zA-Z_:][-a-zA-Z0-9_:.]*");
/* 151 */   private static final Pattern xmlKeyReplace = Pattern.compile("[^-a-zA-Z0-9_:.]");
/* 152 */   private static final Pattern htmlKeyValid = Pattern.compile("[^\\x00-\\x1f\\x7f-\\x9f \"'/=]+");
/* 153 */   private static final Pattern htmlKeyReplace = Pattern.compile("[\\x00-\\x1f\\x7f-\\x9f \"'/=]");
/*     */   
/*     */   @Nullable
/*     */   public static String getValidKey(String key, Document.OutputSettings.Syntax syntax) {
/* 157 */     if (syntax == Document.OutputSettings.Syntax.xml && !xmlKeyValid.matcher(key).matches()) {
/* 158 */       key = xmlKeyReplace.matcher(key).replaceAll("");
/* 159 */       return xmlKeyValid.matcher(key).matches() ? key : null;
/*     */     } 
/* 161 */     if (syntax == Document.OutputSettings.Syntax.html && !htmlKeyValid.matcher(key).matches()) {
/* 162 */       key = htmlKeyReplace.matcher(key).replaceAll("");
/* 163 */       return htmlKeyValid.matcher(key).matches() ? key : null;
/*     */     } 
/* 165 */     return key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 174 */     return html();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Attribute createFromEncoded(String unencodedKey, String encodedValue) {
/* 184 */     String value = Entities.unescape(encodedValue, true);
/* 185 */     return new Attribute(unencodedKey, value, null);
/*     */   }
/*     */   
/*     */   protected boolean isDataAttribute() {
/* 189 */     return isDataAttribute(this.key);
/*     */   }
/*     */   
/*     */   protected static boolean isDataAttribute(String key) {
/* 193 */     return (key.startsWith("data-") && key.length() > "data-".length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final boolean shouldCollapseAttribute(Document.OutputSettings out) {
/* 203 */     return shouldCollapseAttribute(this.key, this.val, out);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static boolean shouldCollapseAttribute(String key, @Nullable String val, Document.OutputSettings out) {
/* 208 */     return (out
/* 209 */       .syntax() == Document.OutputSettings.Syntax.html && (val == null || ((val
/* 210 */       .isEmpty() || val.equalsIgnoreCase(key)) && isBooleanAttribute(key))));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isBooleanAttribute(String key) {
/* 217 */     return (Arrays.binarySearch((Object[])booleanAttributes, Normalizer.lowerCase(key)) >= 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object o) {
/* 222 */     if (this == o) return true; 
/* 223 */     if (o == null || getClass() != o.getClass()) return false; 
/* 224 */     Attribute attribute = (Attribute)o;
/* 225 */     if ((this.key != null) ? !this.key.equals(attribute.key) : (attribute.key != null)) return false; 
/* 226 */     return (this.val != null) ? this.val.equals(attribute.val) : ((attribute.val == null));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 231 */     int result = (this.key != null) ? this.key.hashCode() : 0;
/* 232 */     result = 31 * result + ((this.val != null) ? this.val.hashCode() : 0);
/* 233 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attribute clone() {
/*     */     try {
/* 239 */       return (Attribute)super.clone();
/* 240 */     } catch (CloneNotSupportedException e) {
/* 241 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\Attribute.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */