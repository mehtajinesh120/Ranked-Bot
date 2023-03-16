/*     */ package org.jsoup.parser;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import javax.annotation.Nullable;
/*     */ import org.jsoup.UncheckedIOException;
/*     */ import org.jsoup.helper.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharacterReader
/*     */ {
/*     */   static final char EOF = '￿';
/*     */   private static final int maxStringCacheLen = 12;
/*     */   static final int maxBufferLen = 32768;
/*     */   static final int readAheadLimit = 24576;
/*     */   private static final int minReadAheadLen = 1024;
/*     */   private char[] charBuf;
/*     */   private Reader reader;
/*     */   private int bufLength;
/*     */   private int bufSplitPoint;
/*     */   private int bufPos;
/*     */   private int readerPos;
/*  31 */   private int bufMark = -1;
/*     */   private static final int stringCacheSize = 512;
/*  33 */   private String[] stringCache = new String[512];
/*     */   @Nullable
/*  35 */   private ArrayList<Integer> newlinePositions = null;
/*  36 */   private int lineNumberOffset = 1; private boolean readFully;
/*     */   
/*     */   public CharacterReader(Reader input, int sz) {
/*  39 */     Validate.notNull(input);
/*  40 */     Validate.isTrue(input.markSupported());
/*  41 */     this.reader = input;
/*  42 */     this.charBuf = new char[Math.min(sz, 32768)];
/*  43 */     bufferUp();
/*     */   } @Nullable
/*     */   private String lastIcSeq; private int lastIcIndex;
/*     */   public CharacterReader(Reader input) {
/*  47 */     this(input, 32768);
/*     */   }
/*     */   
/*     */   public CharacterReader(String input) {
/*  51 */     this(new StringReader(input), input.length());
/*     */   }
/*     */   
/*     */   public void close() {
/*  55 */     if (this.reader == null)
/*     */       return; 
/*     */     
/*  58 */     try { this.reader.close(); }
/*  59 */     catch (IOException iOException) {  }
/*     */     finally
/*  61 */     { this.reader = null;
/*  62 */       this.charBuf = null;
/*  63 */       this.stringCache = null; }
/*     */   
/*     */   }
/*     */   
/*     */   private void bufferUp() {
/*     */     int pos, offset;
/*  69 */     if (this.readFully || this.bufPos < this.bufSplitPoint) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  74 */     if (this.bufMark != -1) {
/*  75 */       pos = this.bufMark;
/*  76 */       offset = this.bufPos - this.bufMark;
/*     */     } else {
/*  78 */       pos = this.bufPos;
/*  79 */       offset = 0;
/*     */     } 
/*     */     
/*     */     try {
/*  83 */       long skipped = this.reader.skip(pos);
/*  84 */       this.reader.mark(32768);
/*  85 */       int read = 0;
/*  86 */       while (read <= 1024) {
/*  87 */         int thisRead = this.reader.read(this.charBuf, read, this.charBuf.length - read);
/*  88 */         if (thisRead == -1)
/*  89 */           this.readFully = true; 
/*  90 */         if (thisRead <= 0)
/*     */           break; 
/*  92 */         read += thisRead;
/*     */       } 
/*  94 */       this.reader.reset();
/*  95 */       if (read > 0) {
/*  96 */         Validate.isTrue((skipped == pos));
/*  97 */         this.bufLength = read;
/*  98 */         this.readerPos += pos;
/*  99 */         this.bufPos = offset;
/* 100 */         if (this.bufMark != -1)
/* 101 */           this.bufMark = 0; 
/* 102 */         this.bufSplitPoint = Math.min(this.bufLength, 24576);
/*     */       } 
/* 104 */     } catch (IOException e) {
/* 105 */       throw new UncheckedIOException(e);
/*     */     } 
/* 107 */     scanBufferForNewlines();
/* 108 */     this.lastIcSeq = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int pos() {
/* 116 */     return this.readerPos + this.bufPos;
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
/*     */   public void trackNewlines(boolean track) {
/* 128 */     if (track && this.newlinePositions == null) {
/* 129 */       this.newlinePositions = new ArrayList<>(409);
/* 130 */       scanBufferForNewlines();
/*     */     }
/* 132 */     else if (!track) {
/* 133 */       this.newlinePositions = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTrackNewlines() {
/* 142 */     return (this.newlinePositions != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int lineNumber() {
/* 152 */     return lineNumber(pos());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   int lineNumber(int pos) {
/* 158 */     if (!isTrackNewlines()) {
/* 159 */       return 1;
/*     */     }
/* 161 */     int i = lineNumIndex(pos);
/* 162 */     if (i == -1)
/* 163 */       return this.lineNumberOffset; 
/* 164 */     return i + this.lineNumberOffset + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int columnNumber() {
/* 174 */     return columnNumber(pos());
/*     */   }
/*     */   
/*     */   int columnNumber(int pos) {
/* 178 */     if (!isTrackNewlines()) {
/* 179 */       return pos + 1;
/*     */     }
/* 181 */     int i = lineNumIndex(pos);
/* 182 */     if (i == -1)
/* 183 */       return pos + 1; 
/* 184 */     return pos - ((Integer)this.newlinePositions.get(i)).intValue() + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String cursorPos() {
/* 195 */     return lineNumber() + ":" + columnNumber();
/*     */   }
/*     */   
/*     */   private int lineNumIndex(int pos) {
/* 199 */     if (!isTrackNewlines()) return 0; 
/* 200 */     int i = Collections.binarySearch((List)this.newlinePositions, Integer.valueOf(pos));
/* 201 */     if (i < -1) i = Math.abs(i) - 2; 
/* 202 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void scanBufferForNewlines() {
/* 209 */     if (!isTrackNewlines()) {
/*     */       return;
/*     */     }
/* 212 */     if (this.newlinePositions.size() > 0) {
/*     */       
/* 214 */       int index = lineNumIndex(this.readerPos);
/* 215 */       if (index == -1) index = 0; 
/* 216 */       int linePos = ((Integer)this.newlinePositions.get(index)).intValue();
/* 217 */       this.lineNumberOffset += index;
/* 218 */       this.newlinePositions.clear();
/* 219 */       this.newlinePositions.add(Integer.valueOf(linePos));
/*     */     } 
/*     */     
/* 222 */     for (int i = this.bufPos; i < this.bufLength; i++) {
/* 223 */       if (this.charBuf[i] == '\n') {
/* 224 */         this.newlinePositions.add(Integer.valueOf(1 + this.readerPos + i));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 233 */     bufferUp();
/* 234 */     return (this.bufPos >= this.bufLength);
/*     */   }
/*     */   
/*     */   private boolean isEmptyNoBufferUp() {
/* 238 */     return (this.bufPos >= this.bufLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char current() {
/* 246 */     bufferUp();
/* 247 */     return isEmptyNoBufferUp() ? Character.MAX_VALUE : this.charBuf[this.bufPos];
/*     */   }
/*     */   
/*     */   char consume() {
/* 251 */     bufferUp();
/* 252 */     char val = isEmptyNoBufferUp() ? Character.MAX_VALUE : this.charBuf[this.bufPos];
/* 253 */     this.bufPos++;
/* 254 */     return val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void unconsume() {
/* 261 */     if (this.bufPos < 1) {
/* 262 */       throw new UncheckedIOException(new IOException("WTF: No buffer left to unconsume."));
/*     */     }
/* 264 */     this.bufPos--;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void advance() {
/* 271 */     this.bufPos++;
/*     */   }
/*     */ 
/*     */   
/*     */   void mark() {
/* 276 */     if (this.bufLength - this.bufPos < 1024) {
/* 277 */       this.bufSplitPoint = 0;
/*     */     }
/* 279 */     bufferUp();
/* 280 */     this.bufMark = this.bufPos;
/*     */   }
/*     */   
/*     */   void unmark() {
/* 284 */     this.bufMark = -1;
/*     */   }
/*     */   
/*     */   void rewindToMark() {
/* 288 */     if (this.bufMark == -1) {
/* 289 */       throw new UncheckedIOException(new IOException("Mark invalid"));
/*     */     }
/* 291 */     this.bufPos = this.bufMark;
/* 292 */     unmark();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int nextIndexOf(char c) {
/* 302 */     bufferUp();
/* 303 */     for (int i = this.bufPos; i < this.bufLength; i++) {
/* 304 */       if (c == this.charBuf[i])
/* 305 */         return i - this.bufPos; 
/*     */     } 
/* 307 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int nextIndexOf(CharSequence seq) {
/* 317 */     bufferUp();
/*     */     
/* 319 */     char startChar = seq.charAt(0);
/* 320 */     for (int offset = this.bufPos; offset < this.bufLength; offset++) {
/*     */       
/* 322 */       if (startChar != this.charBuf[offset])
/* 323 */         while (++offset < this.bufLength && startChar != this.charBuf[offset]); 
/* 324 */       int i = offset + 1;
/* 325 */       int last = i + seq.length() - 1;
/* 326 */       if (offset < this.bufLength && last <= this.bufLength) {
/* 327 */         for (int j = 1; i < last && seq.charAt(j) == this.charBuf[i]; ) { i++; j++; }
/* 328 */          if (i == last)
/* 329 */           return offset - this.bufPos; 
/*     */       } 
/*     */     } 
/* 332 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String consumeTo(char c) {
/* 341 */     int offset = nextIndexOf(c);
/* 342 */     if (offset != -1) {
/* 343 */       String consumed = cacheString(this.charBuf, this.stringCache, this.bufPos, offset);
/* 344 */       this.bufPos += offset;
/* 345 */       return consumed;
/*     */     } 
/* 347 */     return consumeToEnd();
/*     */   }
/*     */ 
/*     */   
/*     */   String consumeTo(String seq) {
/* 352 */     int offset = nextIndexOf(seq);
/* 353 */     if (offset != -1) {
/* 354 */       String str = cacheString(this.charBuf, this.stringCache, this.bufPos, offset);
/* 355 */       this.bufPos += offset;
/* 356 */       return str;
/* 357 */     }  if (this.bufLength - this.bufPos < seq.length())
/*     */     {
/* 359 */       return consumeToEnd();
/*     */     }
/*     */ 
/*     */     
/* 363 */     int endPos = this.bufLength - seq.length() + 1;
/* 364 */     String consumed = cacheString(this.charBuf, this.stringCache, this.bufPos, endPos - this.bufPos);
/* 365 */     this.bufPos = endPos;
/* 366 */     return consumed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String consumeToAny(char... chars) {
/* 376 */     bufferUp();
/* 377 */     int pos = this.bufPos;
/* 378 */     int start = pos;
/* 379 */     int remaining = this.bufLength;
/* 380 */     char[] val = this.charBuf;
/* 381 */     int charLen = chars.length;
/*     */ 
/*     */     
/* 384 */     label18: while (pos < remaining) {
/* 385 */       for (int i = 0; i < charLen; i++) {
/* 386 */         if (val[pos] == chars[i])
/*     */           break label18; 
/*     */       } 
/* 389 */       pos++;
/*     */     } 
/*     */     
/* 392 */     this.bufPos = pos;
/* 393 */     return (pos > start) ? cacheString(this.charBuf, this.stringCache, start, pos - start) : "";
/*     */   }
/*     */   
/*     */   String consumeToAnySorted(char... chars) {
/* 397 */     bufferUp();
/* 398 */     int pos = this.bufPos;
/* 399 */     int start = pos;
/* 400 */     int remaining = this.bufLength;
/* 401 */     char[] val = this.charBuf;
/*     */     
/* 403 */     while (pos < remaining && 
/* 404 */       Arrays.binarySearch(chars, val[pos]) < 0)
/*     */     {
/* 406 */       pos++;
/*     */     }
/* 408 */     this.bufPos = pos;
/* 409 */     return (this.bufPos > start) ? cacheString(this.charBuf, this.stringCache, start, pos - start) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String consumeData() {
/* 415 */     int pos = this.bufPos;
/* 416 */     int start = pos;
/* 417 */     int remaining = this.bufLength;
/* 418 */     char[] val = this.charBuf;
/*     */     
/* 420 */     while (pos < remaining) {
/* 421 */       switch (val[pos]) {
/*     */         case '\000':
/*     */         case '&':
/*     */         case '<':
/*     */           break;
/*     */       } 
/* 427 */       pos++;
/*     */     } 
/*     */     
/* 430 */     this.bufPos = pos;
/* 431 */     return (pos > start) ? cacheString(this.charBuf, this.stringCache, start, pos - start) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String consumeAttributeQuoted(boolean single) {
/* 437 */     int pos = this.bufPos;
/* 438 */     int start = pos;
/* 439 */     int remaining = this.bufLength;
/* 440 */     char[] val = this.charBuf;
/*     */     
/* 442 */     while (pos < remaining) {
/* 443 */       switch (val[pos]) {
/*     */         case '\000':
/*     */         case '&':
/*     */           break;
/*     */         case '\'':
/* 448 */           if (single)
/*     */             break; 
/* 450 */         case '"': if (!single)
/*     */             break;  break;
/* 452 */       }  pos++;
/*     */     } 
/*     */     
/* 455 */     this.bufPos = pos;
/* 456 */     return (pos > start) ? cacheString(this.charBuf, this.stringCache, start, pos - start) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String consumeRawData() {
/* 463 */     int pos = this.bufPos;
/* 464 */     int start = pos;
/* 465 */     int remaining = this.bufLength;
/* 466 */     char[] val = this.charBuf;
/*     */     
/* 468 */     while (pos < remaining) {
/* 469 */       switch (val[pos]) {
/*     */         case '\000':
/*     */         case '<':
/*     */           break;
/*     */       } 
/* 474 */       pos++;
/*     */     } 
/*     */     
/* 477 */     this.bufPos = pos;
/* 478 */     return (pos > start) ? cacheString(this.charBuf, this.stringCache, start, pos - start) : "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   String consumeTagName() {
/* 484 */     bufferUp();
/* 485 */     int pos = this.bufPos;
/* 486 */     int start = pos;
/* 487 */     int remaining = this.bufLength;
/* 488 */     char[] val = this.charBuf;
/*     */     
/* 490 */     while (pos < remaining) {
/* 491 */       switch (val[pos]) {
/*     */         case '\t':
/*     */         case '\n':
/*     */         case '\f':
/*     */         case '\r':
/*     */         case ' ':
/*     */         case '/':
/*     */         case '<':
/*     */         case '>':
/*     */           break;
/*     */       } 
/* 502 */       pos++;
/*     */     } 
/*     */     
/* 505 */     this.bufPos = pos;
/* 506 */     return (pos > start) ? cacheString(this.charBuf, this.stringCache, start, pos - start) : "";
/*     */   }
/*     */   
/*     */   String consumeToEnd() {
/* 510 */     bufferUp();
/* 511 */     String data = cacheString(this.charBuf, this.stringCache, this.bufPos, this.bufLength - this.bufPos);
/* 512 */     this.bufPos = this.bufLength;
/* 513 */     return data;
/*     */   }
/*     */   
/*     */   String consumeLetterSequence() {
/* 517 */     bufferUp();
/* 518 */     int start = this.bufPos;
/* 519 */     while (this.bufPos < this.bufLength) {
/* 520 */       char c = this.charBuf[this.bufPos];
/* 521 */       if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isLetter(c)) {
/* 522 */         this.bufPos++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 527 */     return cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
/*     */   }
/*     */   
/*     */   String consumeLetterThenDigitSequence() {
/* 531 */     bufferUp();
/* 532 */     int start = this.bufPos;
/* 533 */     while (this.bufPos < this.bufLength) {
/* 534 */       char c = this.charBuf[this.bufPos];
/* 535 */       if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isLetter(c)) {
/* 536 */         this.bufPos++;
/*     */       }
/*     */     } 
/*     */     
/* 540 */     while (!isEmptyNoBufferUp()) {
/* 541 */       char c = this.charBuf[this.bufPos];
/* 542 */       if (c >= '0' && c <= '9') {
/* 543 */         this.bufPos++;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 548 */     return cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
/*     */   }
/*     */   
/*     */   String consumeHexSequence() {
/* 552 */     bufferUp();
/* 553 */     int start = this.bufPos;
/* 554 */     while (this.bufPos < this.bufLength) {
/* 555 */       char c = this.charBuf[this.bufPos];
/* 556 */       if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f')) {
/* 557 */         this.bufPos++;
/*     */       }
/*     */     } 
/*     */     
/* 561 */     return cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
/*     */   }
/*     */   
/*     */   String consumeDigitSequence() {
/* 565 */     bufferUp();
/* 566 */     int start = this.bufPos;
/* 567 */     while (this.bufPos < this.bufLength) {
/* 568 */       char c = this.charBuf[this.bufPos];
/* 569 */       if (c >= '0' && c <= '9') {
/* 570 */         this.bufPos++;
/*     */       }
/*     */     } 
/*     */     
/* 574 */     return cacheString(this.charBuf, this.stringCache, start, this.bufPos - start);
/*     */   }
/*     */   
/*     */   boolean matches(char c) {
/* 578 */     return (!isEmpty() && this.charBuf[this.bufPos] == c);
/*     */   }
/*     */ 
/*     */   
/*     */   boolean matches(String seq) {
/* 583 */     bufferUp();
/* 584 */     int scanLength = seq.length();
/* 585 */     if (scanLength > this.bufLength - this.bufPos) {
/* 586 */       return false;
/*     */     }
/* 588 */     for (int offset = 0; offset < scanLength; offset++) {
/* 589 */       if (seq.charAt(offset) != this.charBuf[this.bufPos + offset])
/* 590 */         return false; 
/* 591 */     }  return true;
/*     */   }
/*     */   
/*     */   boolean matchesIgnoreCase(String seq) {
/* 595 */     bufferUp();
/* 596 */     int scanLength = seq.length();
/* 597 */     if (scanLength > this.bufLength - this.bufPos) {
/* 598 */       return false;
/*     */     }
/* 600 */     for (int offset = 0; offset < scanLength; offset++) {
/* 601 */       char upScan = Character.toUpperCase(seq.charAt(offset));
/* 602 */       char upTarget = Character.toUpperCase(this.charBuf[this.bufPos + offset]);
/* 603 */       if (upScan != upTarget)
/* 604 */         return false; 
/*     */     } 
/* 606 */     return true;
/*     */   }
/*     */   
/*     */   boolean matchesAny(char... seq) {
/* 610 */     if (isEmpty()) {
/* 611 */       return false;
/*     */     }
/* 613 */     bufferUp();
/* 614 */     char c = this.charBuf[this.bufPos];
/* 615 */     for (char seek : seq) {
/* 616 */       if (seek == c)
/* 617 */         return true; 
/*     */     } 
/* 619 */     return false;
/*     */   }
/*     */   
/*     */   boolean matchesAnySorted(char[] seq) {
/* 623 */     bufferUp();
/* 624 */     return (!isEmpty() && Arrays.binarySearch(seq, this.charBuf[this.bufPos]) >= 0);
/*     */   }
/*     */   
/*     */   boolean matchesLetter() {
/* 628 */     if (isEmpty())
/* 629 */       return false; 
/* 630 */     char c = this.charBuf[this.bufPos];
/* 631 */     return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isLetter(c));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean matchesAsciiAlpha() {
/* 639 */     if (isEmpty())
/* 640 */       return false; 
/* 641 */     char c = this.charBuf[this.bufPos];
/* 642 */     return ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'));
/*     */   }
/*     */   
/*     */   boolean matchesDigit() {
/* 646 */     if (isEmpty())
/* 647 */       return false; 
/* 648 */     char c = this.charBuf[this.bufPos];
/* 649 */     return (c >= '0' && c <= '9');
/*     */   }
/*     */   
/*     */   boolean matchConsume(String seq) {
/* 653 */     bufferUp();
/* 654 */     if (matches(seq)) {
/* 655 */       this.bufPos += seq.length();
/* 656 */       return true;
/*     */     } 
/* 658 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean matchConsumeIgnoreCase(String seq) {
/* 663 */     if (matchesIgnoreCase(seq)) {
/* 664 */       this.bufPos += seq.length();
/* 665 */       return true;
/*     */     } 
/* 667 */     return false;
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
/*     */   boolean containsIgnoreCase(String seq) {
/* 679 */     if (seq.equals(this.lastIcSeq)) {
/* 680 */       if (this.lastIcIndex == -1) return false; 
/* 681 */       if (this.lastIcIndex >= this.bufPos) return true; 
/*     */     } 
/* 683 */     this.lastIcSeq = seq;
/*     */     
/* 685 */     String loScan = seq.toLowerCase(Locale.ENGLISH);
/* 686 */     int lo = nextIndexOf(loScan);
/* 687 */     if (lo > -1) {
/* 688 */       this.lastIcIndex = this.bufPos + lo; return true;
/*     */     } 
/*     */     
/* 691 */     String hiScan = seq.toUpperCase(Locale.ENGLISH);
/* 692 */     int hi = nextIndexOf(hiScan);
/* 693 */     boolean found = (hi > -1);
/* 694 */     this.lastIcIndex = found ? (this.bufPos + hi) : -1;
/* 695 */     return found;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 700 */     if (this.bufLength - this.bufPos < 0)
/* 701 */       return ""; 
/* 702 */     return new String(this.charBuf, this.bufPos, this.bufLength - this.bufPos);
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
/*     */   private static String cacheString(char[] charBuf, String[] stringCache, int start, int count) {
/* 714 */     if (count > 12)
/* 715 */       return new String(charBuf, start, count); 
/* 716 */     if (count < 1) {
/* 717 */       return "";
/*     */     }
/*     */     
/* 720 */     int hash = 0;
/* 721 */     for (int i = 0; i < count; i++) {
/* 722 */       hash = 31 * hash + charBuf[start + i];
/*     */     }
/*     */ 
/*     */     
/* 726 */     int index = hash & 0x1FF;
/* 727 */     String cached = stringCache[index];
/*     */     
/* 729 */     if (cached != null && rangeEquals(charBuf, start, count, cached)) {
/* 730 */       return cached;
/*     */     }
/* 732 */     cached = new String(charBuf, start, count);
/* 733 */     stringCache[index] = cached;
/*     */ 
/*     */     
/* 736 */     return cached;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean rangeEquals(char[] charBuf, int start, int count, String cached) {
/* 743 */     if (count == cached.length()) {
/* 744 */       int i = start;
/* 745 */       int j = 0;
/* 746 */       while (count-- != 0) {
/* 747 */         if (charBuf[i++] != cached.charAt(j++))
/* 748 */           return false; 
/*     */       } 
/* 750 */       return true;
/*     */     } 
/* 752 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean rangeEquals(int start, int count, String cached) {
/* 757 */     return rangeEquals(this.charBuf, start, count, cached);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\CharacterReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */