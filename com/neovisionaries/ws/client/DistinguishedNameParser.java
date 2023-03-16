/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DistinguishedNameParser
/*     */ {
/*     */   private final String dn;
/*     */   private final int length;
/*     */   private int pos;
/*     */   private int beg;
/*     */   private int end;
/*     */   private int cur;
/*     */   private char[] chars;
/*     */   
/*     */   public DistinguishedNameParser(X500Principal principal) {
/*  43 */     this.dn = principal.getName("RFC2253");
/*  44 */     this.length = this.dn.length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String nextAT() {
/*  51 */     for (; this.pos < this.length && this.chars[this.pos] == ' '; this.pos++);
/*     */     
/*  53 */     if (this.pos == this.length) {
/*  54 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  58 */     this.beg = this.pos;
/*     */ 
/*     */     
/*  61 */     this.pos++;
/*  62 */     for (; this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] != ' '; this.pos++);
/*     */ 
/*     */ 
/*     */     
/*  66 */     if (this.pos >= this.length) {
/*  67 */       throw new IllegalStateException("Unexpected end of DN: " + this.dn);
/*     */     }
/*     */ 
/*     */     
/*  71 */     this.end = this.pos;
/*     */ 
/*     */ 
/*     */     
/*  75 */     if (this.chars[this.pos] == ' ') {
/*  76 */       for (; this.pos < this.length && this.chars[this.pos] != '=' && this.chars[this.pos] == ' '; this.pos++);
/*     */ 
/*     */       
/*  79 */       if (this.chars[this.pos] != '=' || this.pos == this.length) {
/*  80 */         throw new IllegalStateException("Unexpected end of DN: " + this.dn);
/*     */       }
/*     */     } 
/*     */     
/*  84 */     this.pos++;
/*     */ 
/*     */ 
/*     */     
/*  88 */     for (; this.pos < this.length && this.chars[this.pos] == ' '; this.pos++);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  93 */     if (this.end - this.beg > 4 && this.chars[this.beg + 3] == '.' && (this.chars[this.beg] == 'O' || this.chars[this.beg] == 'o') && (this.chars[this.beg + 1] == 'I' || this.chars[this.beg + 1] == 'i') && (this.chars[this.beg + 2] == 'D' || this.chars[this.beg + 2] == 'd'))
/*     */     {
/*     */ 
/*     */       
/*  97 */       this.beg += 4;
/*     */     }
/*     */     
/* 100 */     return new String(this.chars, this.beg, this.end - this.beg);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String quotedAV() {
/* 106 */     this.beg = ++this.pos;
/* 107 */     this.end = this.beg;
/*     */     
/*     */     while (true) {
/* 110 */       if (this.pos == this.length) {
/* 111 */         throw new IllegalStateException("Unexpected end of DN: " + this.dn);
/*     */       }
/*     */       
/* 114 */       if (this.chars[this.pos] == '"') {
/*     */         
/* 116 */         this.pos++; break;
/*     */       } 
/* 118 */       if (this.chars[this.pos] == '\\') {
/* 119 */         this.chars[this.end] = getEscaped();
/*     */       } else {
/*     */         
/* 122 */         this.chars[this.end] = this.chars[this.pos];
/*     */       } 
/* 124 */       this.pos++;
/* 125 */       this.end++;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 130 */     for (; this.pos < this.length && this.chars[this.pos] == ' '; this.pos++);
/*     */ 
/*     */     
/* 133 */     return new String(this.chars, this.beg, this.end - this.beg);
/*     */   }
/*     */ 
/*     */   
/*     */   private String hexAV() {
/* 138 */     if (this.pos + 4 >= this.length)
/*     */     {
/* 140 */       throw new IllegalStateException("Unexpected end of DN: " + this.dn);
/*     */     }
/*     */     
/* 143 */     this.beg = this.pos;
/* 144 */     this.pos++;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 149 */       if (this.pos == this.length || this.chars[this.pos] == '+' || this.chars[this.pos] == ',' || this.chars[this.pos] == ';') {
/*     */         
/* 151 */         this.end = this.pos;
/*     */         
/*     */         break;
/*     */       } 
/* 155 */       if (this.chars[this.pos] == ' ') {
/* 156 */         this.end = this.pos;
/* 157 */         this.pos++;
/*     */ 
/*     */         
/* 160 */         for (; this.pos < this.length && this.chars[this.pos] == ' '; this.pos++);
/*     */         break;
/*     */       } 
/* 163 */       if (this.chars[this.pos] >= 'A' && this.chars[this.pos] <= 'F') {
/* 164 */         this.chars[this.pos] = (char)(this.chars[this.pos] + 32);
/*     */       }
/*     */       
/* 167 */       this.pos++;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 172 */     int hexLen = this.end - this.beg;
/* 173 */     if (hexLen < 5 || (hexLen & 0x1) == 0) {
/* 174 */       throw new IllegalStateException("Unexpected end of DN: " + this.dn);
/*     */     }
/*     */ 
/*     */     
/* 178 */     byte[] encoded = new byte[hexLen / 2];
/* 179 */     for (int i = 0, p = this.beg + 1; i < encoded.length; p += 2, i++) {
/* 180 */       encoded[i] = (byte)getByte(p);
/*     */     }
/*     */     
/* 183 */     return new String(this.chars, this.beg, hexLen);
/*     */   }
/*     */ 
/*     */   
/*     */   private String escapedAV() {
/* 188 */     this.beg = this.pos;
/* 189 */     this.end = this.pos;
/*     */     while (true) {
/* 191 */       if (this.pos >= this.length)
/*     */       {
/* 193 */         return new String(this.chars, this.beg, this.end - this.beg);
/*     */       }
/*     */       
/* 196 */       switch (this.chars[this.pos]) {
/*     */         
/*     */         case '+':
/*     */         case ',':
/*     */         case ';':
/* 201 */           return new String(this.chars, this.beg, this.end - this.beg);
/*     */         
/*     */         case '\\':
/* 204 */           this.chars[this.end++] = getEscaped();
/* 205 */           this.pos++;
/*     */           continue;
/*     */ 
/*     */         
/*     */         case ' ':
/* 210 */           this.cur = this.end;
/*     */           
/* 212 */           this.pos++;
/* 213 */           this.chars[this.end++] = ' ';
/*     */           
/* 215 */           for (; this.pos < this.length && this.chars[this.pos] == ' '; this.pos++) {
/* 216 */             this.chars[this.end++] = ' ';
/*     */           }
/* 218 */           if (this.pos == this.length || this.chars[this.pos] == ',' || this.chars[this.pos] == '+' || this.chars[this.pos] == ';')
/*     */           {
/*     */             
/* 221 */             return new String(this.chars, this.beg, this.cur - this.beg);
/*     */           }
/*     */           continue;
/*     */       } 
/* 225 */       this.chars[this.end++] = this.chars[this.pos];
/* 226 */       this.pos++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private char getEscaped() {
/* 233 */     this.pos++;
/* 234 */     if (this.pos == this.length) {
/* 235 */       throw new IllegalStateException("Unexpected end of DN: " + this.dn);
/*     */     }
/*     */     
/* 238 */     switch (this.chars[this.pos]) {
/*     */       
/*     */       case ' ':
/*     */       case '"':
/*     */       case '#':
/*     */       case '%':
/*     */       case '*':
/*     */       case '+':
/*     */       case ',':
/*     */       case ';':
/*     */       case '<':
/*     */       case '=':
/*     */       case '>':
/*     */       case '\\':
/*     */       case '_':
/* 253 */         return this.chars[this.pos];
/*     */     } 
/*     */ 
/*     */     
/* 257 */     return getUTF8();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char getUTF8() {
/* 264 */     int res = getByte(this.pos);
/* 265 */     this.pos++;
/*     */     
/* 267 */     if (res < 128)
/* 268 */       return (char)res; 
/* 269 */     if (res >= 192 && res <= 247) {
/*     */       int count;
/*     */       
/* 272 */       if (res <= 223) {
/* 273 */         count = 1;
/* 274 */         res &= 0x1F;
/* 275 */       } else if (res <= 239) {
/* 276 */         count = 2;
/* 277 */         res &= 0xF;
/*     */       } else {
/* 279 */         count = 3;
/* 280 */         res &= 0x7;
/*     */       } 
/*     */ 
/*     */       
/* 284 */       for (int i = 0; i < count; i++) {
/* 285 */         this.pos++;
/* 286 */         if (this.pos == this.length || this.chars[this.pos] != '\\') {
/* 287 */           return '?';
/*     */         }
/* 289 */         this.pos++;
/*     */         
/* 291 */         int b = getByte(this.pos);
/* 292 */         this.pos++;
/* 293 */         if ((b & 0xC0) != 128) {
/* 294 */           return '?';
/*     */         }
/*     */         
/* 297 */         res = (res << 6) + (b & 0x3F);
/*     */       } 
/* 299 */       return (char)res;
/*     */     } 
/* 301 */     return '?';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getByte(int position) {
/* 312 */     if (position + 1 >= this.length) {
/* 313 */       throw new IllegalStateException("Malformed DN: " + this.dn);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 318 */     int b1 = this.chars[position];
/* 319 */     if (b1 >= 48 && b1 <= 57) {
/* 320 */       b1 -= 48;
/* 321 */     } else if (b1 >= 97 && b1 <= 102) {
/* 322 */       b1 -= 87;
/* 323 */     } else if (b1 >= 65 && b1 <= 70) {
/* 324 */       b1 -= 55;
/*     */     } else {
/* 326 */       throw new IllegalStateException("Malformed DN: " + this.dn);
/*     */     } 
/*     */     
/* 329 */     int b2 = this.chars[position + 1];
/* 330 */     if (b2 >= 48 && b2 <= 57) {
/* 331 */       b2 -= 48;
/* 332 */     } else if (b2 >= 97 && b2 <= 102) {
/* 333 */       b2 -= 87;
/* 334 */     } else if (b2 >= 65 && b2 <= 70) {
/* 335 */       b2 -= 55;
/*     */     } else {
/* 337 */       throw new IllegalStateException("Malformed DN: " + this.dn);
/*     */     } 
/*     */     
/* 340 */     return (b1 << 4) + b2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String findMostSpecific(String attributeType) {
/* 351 */     this.pos = 0;
/* 352 */     this.beg = 0;
/* 353 */     this.end = 0;
/* 354 */     this.cur = 0;
/* 355 */     this.chars = this.dn.toCharArray();
/*     */     
/* 357 */     String attType = nextAT();
/* 358 */     if (attType == null) {
/* 359 */       return null;
/*     */     }
/*     */     while (true) {
/* 362 */       String attValue = "";
/*     */       
/* 364 */       if (this.pos == this.length) {
/* 365 */         return null;
/*     */       }
/*     */       
/* 368 */       switch (this.chars[this.pos]) {
/*     */         case '"':
/* 370 */           attValue = quotedAV();
/*     */           break;
/*     */         case '#':
/* 373 */           attValue = hexAV();
/*     */           break;
/*     */         
/*     */         case '+':
/*     */         case ',':
/*     */         case ';':
/*     */           break;
/*     */         default:
/* 381 */           attValue = escapedAV();
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 387 */       if (attributeType.equalsIgnoreCase(attType)) {
/* 388 */         return attValue;
/*     */       }
/*     */       
/* 391 */       if (this.pos >= this.length) {
/* 392 */         return null;
/*     */       }
/*     */       
/* 395 */       if (this.chars[this.pos] != ',' && this.chars[this.pos] != ';' && 
/* 396 */         this.chars[this.pos] != '+') {
/* 397 */         throw new IllegalStateException("Malformed DN: " + this.dn);
/*     */       }
/*     */       
/* 400 */       this.pos++;
/* 401 */       attType = nextAT();
/* 402 */       if (attType == null)
/* 403 */         throw new IllegalStateException("Malformed DN: " + this.dn); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\DistinguishedNameParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */