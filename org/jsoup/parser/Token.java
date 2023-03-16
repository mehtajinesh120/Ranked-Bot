/*     */ package org.jsoup.parser;
/*     */ 
/*     */ import javax.annotation.Nullable;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.nodes.Attributes;
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class Token
/*     */ {
/*     */   TokenType type;
/*     */   static final int Unset = -1;
/*     */   private int startPos;
/*  14 */   private int endPos = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String tokenType() {
/*  20 */     return getClass().getSimpleName();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Token reset() {
/*  28 */     this.startPos = -1;
/*  29 */     this.endPos = -1;
/*  30 */     return this;
/*     */   }
/*     */   
/*     */   int startPos() {
/*  34 */     return this.startPos;
/*     */   }
/*     */   
/*     */   void startPos(int pos) {
/*  38 */     this.startPos = pos;
/*     */   }
/*     */   
/*     */   int endPos() {
/*  42 */     return this.endPos;
/*     */   }
/*     */   
/*     */   void endPos(int pos) {
/*  46 */     this.endPos = pos;
/*     */   }
/*     */   
/*     */   static void reset(StringBuilder sb) {
/*  50 */     if (sb != null)
/*  51 */       sb.delete(0, sb.length()); 
/*     */   }
/*     */   
/*     */   static final class Doctype
/*     */     extends Token {
/*  56 */     final StringBuilder name = new StringBuilder();
/*  57 */     String pubSysKey = null;
/*  58 */     final StringBuilder publicIdentifier = new StringBuilder();
/*  59 */     final StringBuilder systemIdentifier = new StringBuilder();
/*     */     boolean forceQuirks = false;
/*     */     
/*     */     Doctype() {
/*  63 */       this.type = Token.TokenType.Doctype;
/*     */     }
/*     */ 
/*     */     
/*     */     Token reset() {
/*  68 */       super.reset();
/*  69 */       reset(this.name);
/*  70 */       this.pubSysKey = null;
/*  71 */       reset(this.publicIdentifier);
/*  72 */       reset(this.systemIdentifier);
/*  73 */       this.forceQuirks = false;
/*  74 */       return this;
/*     */     }
/*     */     
/*     */     String getName() {
/*  78 */       return this.name.toString();
/*     */     }
/*     */     
/*     */     String getPubSysKey() {
/*  82 */       return this.pubSysKey;
/*     */     }
/*     */     
/*     */     String getPublicIdentifier() {
/*  86 */       return this.publicIdentifier.toString();
/*     */     }
/*     */     
/*     */     public String getSystemIdentifier() {
/*  90 */       return this.systemIdentifier.toString();
/*     */     }
/*     */     
/*     */     public boolean isForceQuirks() {
/*  94 */       return this.forceQuirks;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  99 */       return "<!doctype " + getName() + ">";
/*     */     } }
/*     */   static abstract class Tag extends Token { @Nullable
/*     */     protected String tagName; @Nullable
/*     */     protected String normalName; private final StringBuilder attrName; @Nullable
/*     */     private String attrNameS; private boolean hasAttrName; private final StringBuilder attrValue;
/*     */     
/*     */     Tag() {
/* 107 */       this.attrName = new StringBuilder();
/*     */       
/* 109 */       this.hasAttrName = false;
/*     */       
/* 111 */       this.attrValue = new StringBuilder();
/*     */       
/* 113 */       this.hasAttrValue = false;
/* 114 */       this.hasEmptyAttrValue = false;
/*     */       
/* 116 */       this.selfClosing = false;
/*     */     } @Nullable
/*     */     private String attrValueS; private boolean hasAttrValue; private boolean hasEmptyAttrValue; boolean selfClosing; @Nullable
/*     */     Attributes attributes; private static final int MaxAttributes = 512;
/*     */     Tag reset() {
/* 121 */       super.reset();
/* 122 */       this.tagName = null;
/* 123 */       this.normalName = null;
/* 124 */       reset(this.attrName);
/* 125 */       this.attrNameS = null;
/* 126 */       this.hasAttrName = false;
/* 127 */       reset(this.attrValue);
/* 128 */       this.attrValueS = null;
/* 129 */       this.hasEmptyAttrValue = false;
/* 130 */       this.hasAttrValue = false;
/* 131 */       this.selfClosing = false;
/* 132 */       this.attributes = null;
/* 133 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final void newAttribute() {
/* 142 */       if (this.attributes == null) {
/* 143 */         this.attributes = new Attributes();
/*     */       }
/* 145 */       if (this.hasAttrName && this.attributes.size() < 512) {
/*     */         
/* 147 */         String name = (this.attrName.length() > 0) ? this.attrName.toString() : this.attrNameS;
/* 148 */         name = name.trim();
/* 149 */         if (name.length() > 0) {
/*     */           String value;
/* 151 */           if (this.hasAttrValue) {
/* 152 */             value = (this.attrValue.length() > 0) ? this.attrValue.toString() : this.attrValueS;
/* 153 */           } else if (this.hasEmptyAttrValue) {
/* 154 */             value = "";
/*     */           } else {
/* 156 */             value = null;
/*     */           } 
/* 158 */           this.attributes.add(name, value);
/*     */         } 
/*     */       } 
/* 161 */       reset(this.attrName);
/* 162 */       this.attrNameS = null;
/* 163 */       this.hasAttrName = false;
/*     */       
/* 165 */       reset(this.attrValue);
/* 166 */       this.attrValueS = null;
/* 167 */       this.hasAttrValue = false;
/* 168 */       this.hasEmptyAttrValue = false;
/*     */     }
/*     */     
/*     */     final boolean hasAttributes() {
/* 172 */       return (this.attributes != null);
/*     */     }
/*     */     
/*     */     final boolean hasAttribute(String key) {
/* 176 */       return (this.attributes != null && this.attributes.hasKey(key));
/*     */     }
/*     */ 
/*     */     
/*     */     final void finaliseTag() {
/* 181 */       if (this.hasAttrName) {
/* 182 */         newAttribute();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     final String name() {
/* 188 */       Validate.isFalse((this.tagName == null || this.tagName.length() == 0));
/* 189 */       return this.tagName;
/*     */     }
/*     */ 
/*     */     
/*     */     final String normalName() {
/* 194 */       return this.normalName;
/*     */     }
/*     */     
/*     */     final String toStringName() {
/* 198 */       return (this.tagName != null) ? this.tagName : "[unset]";
/*     */     }
/*     */     
/*     */     final Tag name(String name) {
/* 202 */       this.tagName = name;
/* 203 */       this.normalName = ParseSettings.normalName(this.tagName);
/* 204 */       return this;
/*     */     }
/*     */     
/*     */     final boolean isSelfClosing() {
/* 208 */       return this.selfClosing;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     final void appendTagName(String append) {
/* 214 */       append = append.replace(false, '�');
/* 215 */       this.tagName = (this.tagName == null) ? append : this.tagName.concat(append);
/* 216 */       this.normalName = ParseSettings.normalName(this.tagName);
/*     */     }
/*     */     
/*     */     final void appendTagName(char append) {
/* 220 */       appendTagName(String.valueOf(append));
/*     */     }
/*     */ 
/*     */     
/*     */     final void appendAttributeName(String append) {
/* 225 */       append = append.replace(false, '�');
/*     */       
/* 227 */       ensureAttrName();
/* 228 */       if (this.attrName.length() == 0) {
/* 229 */         this.attrNameS = append;
/*     */       } else {
/* 231 */         this.attrName.append(append);
/*     */       } 
/*     */     }
/*     */     
/*     */     final void appendAttributeName(char append) {
/* 236 */       ensureAttrName();
/* 237 */       this.attrName.append(append);
/*     */     }
/*     */     
/*     */     final void appendAttributeValue(String append) {
/* 241 */       ensureAttrValue();
/* 242 */       if (this.attrValue.length() == 0) {
/* 243 */         this.attrValueS = append;
/*     */       } else {
/* 245 */         this.attrValue.append(append);
/*     */       } 
/*     */     }
/*     */     
/*     */     final void appendAttributeValue(char append) {
/* 250 */       ensureAttrValue();
/* 251 */       this.attrValue.append(append);
/*     */     }
/*     */     
/*     */     final void appendAttributeValue(char[] append) {
/* 255 */       ensureAttrValue();
/* 256 */       this.attrValue.append(append);
/*     */     }
/*     */     
/*     */     final void appendAttributeValue(int[] appendCodepoints) {
/* 260 */       ensureAttrValue();
/* 261 */       for (int codepoint : appendCodepoints) {
/* 262 */         this.attrValue.appendCodePoint(codepoint);
/*     */       }
/*     */     }
/*     */     
/*     */     final void setEmptyAttributeValue() {
/* 267 */       this.hasEmptyAttrValue = true;
/*     */     }
/*     */     
/*     */     private void ensureAttrName() {
/* 271 */       this.hasAttrName = true;
/*     */       
/* 273 */       if (this.attrNameS != null) {
/* 274 */         this.attrName.append(this.attrNameS);
/* 275 */         this.attrNameS = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void ensureAttrValue() {
/* 280 */       this.hasAttrValue = true;
/*     */       
/* 282 */       if (this.attrValueS != null) {
/* 283 */         this.attrValue.append(this.attrValueS);
/* 284 */         this.attrValueS = null;
/*     */       } 
/*     */     }
/*     */     
/*     */     public abstract String toString(); }
/*     */ 
/*     */   
/*     */   static final class StartTag
/*     */     extends Tag
/*     */   {
/*     */     StartTag() {
/* 295 */       this.type = Token.TokenType.StartTag;
/*     */     }
/*     */ 
/*     */     
/*     */     Token.Tag reset() {
/* 300 */       super.reset();
/* 301 */       this.attributes = null;
/* 302 */       return this;
/*     */     }
/*     */     
/*     */     StartTag nameAttr(String name, Attributes attributes) {
/* 306 */       this.tagName = name;
/* 307 */       this.attributes = attributes;
/* 308 */       this.normalName = ParseSettings.normalName(this.tagName);
/* 309 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 314 */       if (hasAttributes() && this.attributes.size() > 0) {
/* 315 */         return "<" + toStringName() + " " + this.attributes.toString() + ">";
/*     */       }
/* 317 */       return "<" + toStringName() + ">";
/*     */     }
/*     */   }
/*     */   
/*     */   static final class EndTag
/*     */     extends Tag {
/*     */     EndTag() {
/* 324 */       this.type = Token.TokenType.EndTag;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 329 */       return "</" + toStringName() + ">";
/*     */     }
/*     */   }
/*     */   
/*     */   static final class Comment extends Token {
/* 334 */     private final StringBuilder data = new StringBuilder();
/*     */     
/*     */     private String dataS;
/*     */     boolean bogus = false;
/*     */     
/*     */     Token reset() {
/* 340 */       super.reset();
/* 341 */       reset(this.data);
/* 342 */       this.dataS = null;
/* 343 */       this.bogus = false;
/* 344 */       return this;
/*     */     }
/*     */     
/*     */     Comment() {
/* 348 */       this.type = Token.TokenType.Comment;
/*     */     }
/*     */     
/*     */     String getData() {
/* 352 */       return (this.dataS != null) ? this.dataS : this.data.toString();
/*     */     }
/*     */     
/*     */     final Comment append(String append) {
/* 356 */       ensureData();
/* 357 */       if (this.data.length() == 0) {
/* 358 */         this.dataS = append;
/*     */       } else {
/* 360 */         this.data.append(append);
/*     */       } 
/* 362 */       return this;
/*     */     }
/*     */     
/*     */     final Comment append(char append) {
/* 366 */       ensureData();
/* 367 */       this.data.append(append);
/* 368 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     private void ensureData() {
/* 373 */       if (this.dataS != null) {
/* 374 */         this.data.append(this.dataS);
/* 375 */         this.dataS = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 381 */       return "<!--" + getData() + "-->";
/*     */     }
/*     */   }
/*     */   
/*     */   static class Character
/*     */     extends Token {
/*     */     private String data;
/*     */     
/*     */     Character() {
/* 390 */       this.type = Token.TokenType.Character;
/*     */     }
/*     */ 
/*     */     
/*     */     Token reset() {
/* 395 */       super.reset();
/* 396 */       this.data = null;
/* 397 */       return this;
/*     */     }
/*     */     
/*     */     Character data(String data) {
/* 401 */       this.data = data;
/* 402 */       return this;
/*     */     }
/*     */     
/*     */     String getData() {
/* 406 */       return this.data;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 411 */       return getData();
/*     */     }
/*     */   }
/*     */   
/*     */   static final class CData
/*     */     extends Character {
/*     */     CData(String data) {
/* 418 */       data(data);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 423 */       return "<![CDATA[" + getData() + "]]>";
/*     */     }
/*     */   }
/*     */   
/*     */   static final class EOF
/*     */     extends Token {
/*     */     EOF() {
/* 430 */       this.type = Token.TokenType.EOF;
/*     */     }
/*     */ 
/*     */     
/*     */     Token reset() {
/* 435 */       super.reset();
/* 436 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 441 */       return "";
/*     */     }
/*     */   }
/*     */   
/*     */   final boolean isDoctype() {
/* 446 */     return (this.type == TokenType.Doctype);
/*     */   }
/*     */   
/*     */   final Doctype asDoctype() {
/* 450 */     return (Doctype)this;
/*     */   }
/*     */   
/*     */   final boolean isStartTag() {
/* 454 */     return (this.type == TokenType.StartTag);
/*     */   }
/*     */   
/*     */   final StartTag asStartTag() {
/* 458 */     return (StartTag)this;
/*     */   }
/*     */   
/*     */   final boolean isEndTag() {
/* 462 */     return (this.type == TokenType.EndTag);
/*     */   }
/*     */   
/*     */   final EndTag asEndTag() {
/* 466 */     return (EndTag)this;
/*     */   }
/*     */   
/*     */   final boolean isComment() {
/* 470 */     return (this.type == TokenType.Comment);
/*     */   }
/*     */   
/*     */   final Comment asComment() {
/* 474 */     return (Comment)this;
/*     */   }
/*     */   
/*     */   final boolean isCharacter() {
/* 478 */     return (this.type == TokenType.Character);
/*     */   }
/*     */   
/*     */   final boolean isCData() {
/* 482 */     return this instanceof CData;
/*     */   }
/*     */   
/*     */   final Character asCharacter() {
/* 486 */     return (Character)this;
/*     */   }
/*     */   
/*     */   final boolean isEOF() {
/* 490 */     return (this.type == TokenType.EOF);
/*     */   }
/*     */   private Token() {}
/*     */   
/* 494 */   public enum TokenType { Doctype,
/* 495 */     StartTag,
/* 496 */     EndTag,
/* 497 */     Comment,
/* 498 */     Character,
/* 499 */     EOF; }
/*     */ 
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\Token.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */