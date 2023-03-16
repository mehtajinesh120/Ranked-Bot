/*     */ package org.jsoup.parser;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import javax.annotation.Nullable;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.nodes.Entities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Tokeniser
/*     */ {
/*     */   static final char replacementChar = '�';
/*  15 */   private static final char[] notCharRefCharsSorted = new char[] { '\t', '\n', '\r', '\f', ' ', '<', '&' };
/*     */ 
/*     */   
/*     */   static final int win1252ExtensionsStart = 128;
/*     */   
/*  20 */   static final int[] win1252Extensions = new int[] { 8364, 129, 8218, 402, 8222, 8230, 8224, 8225, 710, 8240, 352, 8249, 338, 141, 381, 143, 144, 8216, 8217, 8220, 8221, 8226, 8211, 8212, 732, 8482, 353, 8250, 339, 157, 382, 376 };
/*     */ 
/*     */   
/*     */   private final CharacterReader reader;
/*     */ 
/*     */   
/*     */   private final ParseErrorList errors;
/*     */ 
/*     */   
/*     */   static {
/*  30 */     Arrays.sort(notCharRefCharsSorted);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  36 */   private TokeniserState state = TokeniserState.Data; @Nullable
/*  37 */   private Token emitPending = null; private boolean isEmitPending = false;
/*     */   @Nullable
/*  39 */   private String charsString = null;
/*  40 */   private final StringBuilder charsBuilder = new StringBuilder(1024);
/*  41 */   StringBuilder dataBuffer = new StringBuilder(1024);
/*     */   
/*  43 */   Token.StartTag startPending = new Token.StartTag();
/*  44 */   Token.EndTag endPending = new Token.EndTag();
/*  45 */   Token.Tag tagPending = this.startPending;
/*  46 */   Token.Character charPending = new Token.Character();
/*  47 */   Token.Doctype doctypePending = new Token.Doctype();
/*  48 */   Token.Comment commentPending = new Token.Comment(); @Nullable
/*     */   private String lastStartTag; @Nullable
/*     */   private String lastStartCloseSeq;
/*     */   private static final int Unset = -1;
/*     */   private int markupStartPos;
/*  53 */   private int charStartPos = -1;
/*     */   
/*     */   private final int[] codepointHolder;
/*     */   
/*     */   private final int[] multipointHolder;
/*     */ 
/*     */   
/*     */   Token read() {
/*  61 */     while (!this.isEmitPending) {
/*  62 */       this.state.read(this, this.reader);
/*     */     }
/*     */ 
/*     */     
/*  66 */     StringBuilder cb = this.charsBuilder;
/*  67 */     if (cb.length() != 0) {
/*  68 */       String str = cb.toString();
/*  69 */       cb.delete(0, cb.length());
/*  70 */       Token token = this.charPending.data(str);
/*  71 */       this.charsString = null;
/*  72 */       return token;
/*  73 */     }  if (this.charsString != null) {
/*  74 */       Token token = this.charPending.data(this.charsString);
/*  75 */       this.charsString = null;
/*  76 */       return token;
/*     */     } 
/*  78 */     this.isEmitPending = false;
/*  79 */     assert this.emitPending != null;
/*  80 */     return this.emitPending;
/*     */   }
/*     */ 
/*     */   
/*     */   void emit(Token token) {
/*  85 */     Validate.isFalse(this.isEmitPending);
/*     */     
/*  87 */     this.emitPending = token;
/*  88 */     this.isEmitPending = true;
/*  89 */     token.startPos(this.markupStartPos);
/*  90 */     token.endPos(this.reader.pos());
/*  91 */     this.charStartPos = -1;
/*     */     
/*  93 */     if (token.type == Token.TokenType.StartTag) {
/*  94 */       Token.StartTag startTag = (Token.StartTag)token;
/*  95 */       this.lastStartTag = startTag.tagName;
/*  96 */       this.lastStartCloseSeq = null;
/*  97 */     } else if (token.type == Token.TokenType.EndTag) {
/*  98 */       Token.EndTag endTag = (Token.EndTag)token;
/*  99 */       if (endTag.hasAttributes()) {
/* 100 */         error("Attributes incorrectly present on end tag [/%s]", new Object[] { endTag.normalName() });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void emit(String str) {
/* 107 */     if (this.charsString == null) {
/* 108 */       this.charsString = str;
/*     */     } else {
/* 110 */       if (this.charsBuilder.length() == 0) {
/* 111 */         this.charsBuilder.append(this.charsString);
/*     */       }
/* 113 */       this.charsBuilder.append(str);
/*     */     } 
/* 115 */     this.charPending.startPos(this.charStartPos);
/* 116 */     this.charPending.endPos(this.reader.pos());
/*     */   }
/*     */ 
/*     */   
/*     */   void emit(StringBuilder str) {
/* 121 */     if (this.charsString == null) {
/* 122 */       this.charsString = str.toString();
/*     */     } else {
/* 124 */       if (this.charsBuilder.length() == 0) {
/* 125 */         this.charsBuilder.append(this.charsString);
/*     */       }
/* 127 */       this.charsBuilder.append(str);
/*     */     } 
/* 129 */     this.charPending.startPos(this.charStartPos);
/* 130 */     this.charPending.endPos(this.reader.pos());
/*     */   }
/*     */   
/*     */   void emit(char c) {
/* 134 */     if (this.charsString == null) {
/* 135 */       this.charsString = String.valueOf(c);
/*     */     } else {
/* 137 */       if (this.charsBuilder.length() == 0) {
/* 138 */         this.charsBuilder.append(this.charsString);
/*     */       }
/* 140 */       this.charsBuilder.append(c);
/*     */     } 
/* 142 */     this.charPending.startPos(this.charStartPos);
/* 143 */     this.charPending.endPos(this.reader.pos());
/*     */   }
/*     */   
/*     */   void emit(char[] chars) {
/* 147 */     emit(String.valueOf(chars));
/*     */   }
/*     */   
/*     */   void emit(int[] codepoints) {
/* 151 */     emit(new String(codepoints, 0, codepoints.length));
/*     */   }
/*     */   
/*     */   TokeniserState getState() {
/* 155 */     return this.state;
/*     */   }
/*     */ 
/*     */   
/*     */   void transition(TokeniserState newState) {
/* 160 */     switch (newState) {
/*     */       case TagOpen:
/* 162 */         this.markupStartPos = this.reader.pos();
/*     */         break;
/*     */       case Data:
/* 165 */         if (this.charStartPos == -1)
/* 166 */           this.charStartPos = this.reader.pos(); 
/*     */         break;
/*     */     } 
/* 169 */     this.state = newState;
/*     */   }
/*     */   
/*     */   void advanceTransition(TokeniserState newState) {
/* 173 */     transition(newState);
/* 174 */     this.reader.advance();
/*     */   }
/*     */   
/* 177 */   Tokeniser(CharacterReader reader, ParseErrorList errors) { this.codepointHolder = new int[1];
/* 178 */     this.multipointHolder = new int[2];
/*     */     this.reader = reader;
/* 180 */     this.errors = errors; } @Nullable int[] consumeCharacterReference(@Nullable Character additionalAllowedCharacter, boolean inAttribute) { if (this.reader.isEmpty())
/* 181 */       return null; 
/* 182 */     if (additionalAllowedCharacter != null && additionalAllowedCharacter.charValue() == this.reader.current())
/* 183 */       return null; 
/* 184 */     if (this.reader.matchesAnySorted(notCharRefCharsSorted)) {
/* 185 */       return null;
/*     */     }
/* 187 */     int[] codeRef = this.codepointHolder;
/* 188 */     this.reader.mark();
/* 189 */     if (this.reader.matchConsume("#")) {
/* 190 */       boolean isHexMode = this.reader.matchConsumeIgnoreCase("X");
/* 191 */       String numRef = isHexMode ? this.reader.consumeHexSequence() : this.reader.consumeDigitSequence();
/* 192 */       if (numRef.length() == 0) {
/* 193 */         characterReferenceError("numeric reference with no numerals", new Object[0]);
/* 194 */         this.reader.rewindToMark();
/* 195 */         return null;
/*     */       } 
/*     */       
/* 198 */       this.reader.unmark();
/* 199 */       if (!this.reader.matchConsume(";"))
/* 200 */         characterReferenceError("missing semicolon on [&#%s]", new Object[] { numRef }); 
/* 201 */       int charval = -1;
/*     */       try {
/* 203 */         int base = isHexMode ? 16 : 10;
/* 204 */         charval = Integer.valueOf(numRef, base).intValue();
/* 205 */       } catch (NumberFormatException numberFormatException) {}
/*     */       
/* 207 */       if (charval == -1 || (charval >= 55296 && charval <= 57343) || charval > 1114111) {
/* 208 */         characterReferenceError("character [%s] outside of valid range", new Object[] { Integer.valueOf(charval) });
/* 209 */         codeRef[0] = 65533;
/*     */       } else {
/*     */         
/* 212 */         if (charval >= 128 && charval < 128 + win1252Extensions.length) {
/* 213 */           characterReferenceError("character [%s] is not a valid unicode code point", new Object[] { Integer.valueOf(charval) });
/* 214 */           charval = win1252Extensions[charval - 128];
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 219 */         codeRef[0] = charval;
/*     */       } 
/* 221 */       return codeRef;
/*     */     } 
/*     */     
/* 224 */     String nameRef = this.reader.consumeLetterThenDigitSequence();
/* 225 */     boolean looksLegit = this.reader.matches(';');
/*     */     
/* 227 */     boolean found = (Entities.isBaseNamedEntity(nameRef) || (Entities.isNamedEntity(nameRef) && looksLegit));
/*     */     
/* 229 */     if (!found) {
/* 230 */       this.reader.rewindToMark();
/* 231 */       if (looksLegit)
/* 232 */         characterReferenceError("invalid named reference [%s]", new Object[] { nameRef }); 
/* 233 */       return null;
/*     */     } 
/* 235 */     if (inAttribute && (this.reader.matchesLetter() || this.reader.matchesDigit() || this.reader.matchesAny(new char[] { '=', '-', '_' }))) {
/*     */       
/* 237 */       this.reader.rewindToMark();
/* 238 */       return null;
/*     */     } 
/*     */     
/* 241 */     this.reader.unmark();
/* 242 */     if (!this.reader.matchConsume(";"))
/* 243 */       characterReferenceError("missing semicolon on [&%s]", new Object[] { nameRef }); 
/* 244 */     int numChars = Entities.codepointsForName(nameRef, this.multipointHolder);
/* 245 */     if (numChars == 1) {
/* 246 */       codeRef[0] = this.multipointHolder[0];
/* 247 */       return codeRef;
/* 248 */     }  if (numChars == 2) {
/* 249 */       return this.multipointHolder;
/*     */     }
/* 251 */     Validate.fail("Unexpected characters returned for " + nameRef);
/* 252 */     return this.multipointHolder; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Token.Tag createTagPending(boolean start) {
/* 258 */     this.tagPending = start ? this.startPending.reset() : this.endPending.reset();
/* 259 */     return this.tagPending;
/*     */   }
/*     */   
/*     */   void emitTagPending() {
/* 263 */     this.tagPending.finaliseTag();
/* 264 */     emit(this.tagPending);
/*     */   }
/*     */   
/*     */   void createCommentPending() {
/* 268 */     this.commentPending.reset();
/*     */   }
/*     */   
/*     */   void emitCommentPending() {
/* 272 */     emit(this.commentPending);
/*     */   }
/*     */   
/*     */   void createBogusCommentPending() {
/* 276 */     this.commentPending.reset();
/* 277 */     this.commentPending.bogus = true;
/*     */   }
/*     */   
/*     */   void createDoctypePending() {
/* 281 */     this.doctypePending.reset();
/*     */   }
/*     */   
/*     */   void emitDoctypePending() {
/* 285 */     emit(this.doctypePending);
/*     */   }
/*     */   
/*     */   void createTempBuffer() {
/* 289 */     Token.reset(this.dataBuffer);
/*     */   }
/*     */   
/*     */   boolean isAppropriateEndTagToken() {
/* 293 */     return (this.lastStartTag != null && this.tagPending.name().equalsIgnoreCase(this.lastStartTag));
/*     */   }
/*     */   @Nullable
/*     */   String appropriateEndTagName() {
/* 297 */     return this.lastStartTag;
/*     */   }
/*     */ 
/*     */   
/*     */   String appropriateEndTagSeq() {
/* 302 */     if (this.lastStartCloseSeq == null)
/* 303 */       this.lastStartCloseSeq = "</" + this.lastStartTag; 
/* 304 */     return this.lastStartCloseSeq;
/*     */   }
/*     */   
/*     */   void error(TokeniserState state) {
/* 308 */     if (this.errors.canAddError())
/* 309 */       this.errors.add(new ParseError(this.reader, "Unexpected character '%s' in input state [%s]", new Object[] { Character.valueOf(this.reader.current()), state })); 
/*     */   }
/*     */   
/*     */   void eofError(TokeniserState state) {
/* 313 */     if (this.errors.canAddError())
/* 314 */       this.errors.add(new ParseError(this.reader, "Unexpectedly reached end of file (EOF) in input state [%s]", new Object[] { state })); 
/*     */   }
/*     */   
/*     */   private void characterReferenceError(String message, Object... args) {
/* 318 */     if (this.errors.canAddError())
/* 319 */       this.errors.add(new ParseError(this.reader, String.format("Invalid character reference: " + message, args))); 
/*     */   }
/*     */   
/*     */   void error(String errorMsg) {
/* 323 */     if (this.errors.canAddError())
/* 324 */       this.errors.add(new ParseError(this.reader, errorMsg)); 
/*     */   }
/*     */   
/*     */   void error(String errorMsg, Object... args) {
/* 328 */     if (this.errors.canAddError()) {
/* 329 */       this.errors.add(new ParseError(this.reader, errorMsg, args));
/*     */     }
/*     */   }
/*     */   
/*     */   boolean currentNodeInHtmlNS() {
/* 334 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String unescapeEntities(boolean inAttribute) {
/* 345 */     StringBuilder builder = StringUtil.borrowBuilder();
/* 346 */     while (!this.reader.isEmpty()) {
/* 347 */       builder.append(this.reader.consumeTo('&'));
/* 348 */       if (this.reader.matches('&')) {
/* 349 */         this.reader.consume();
/* 350 */         int[] c = consumeCharacterReference(null, inAttribute);
/* 351 */         if (c == null || c.length == 0) {
/* 352 */           builder.append('&'); continue;
/*     */         } 
/* 354 */         builder.appendCodePoint(c[0]);
/* 355 */         if (c.length == 2) {
/* 356 */           builder.appendCodePoint(c[1]);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 361 */     return StringUtil.releaseBuilder(builder);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\Tokeniser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */