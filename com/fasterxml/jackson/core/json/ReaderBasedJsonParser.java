/*      */ package com.fasterxml.jackson.core.json;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonLocation;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.base.ParserBase;
/*      */ import com.fasterxml.jackson.core.io.CharTypes;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.core.util.TextBuffer;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ 
/*      */ public class ReaderBasedJsonParser
/*      */   extends ParserBase {
/*   23 */   private static final int FEAT_MASK_TRAILING_COMMA = JsonParser.Feature.ALLOW_TRAILING_COMMA.getMask();
/*      */ 
/*      */   
/*   26 */   private static final int FEAT_MASK_LEADING_ZEROS = JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS.getMask();
/*      */ 
/*      */   
/*   29 */   private static final int FEAT_MASK_NON_NUM_NUMBERS = JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS.getMask();
/*      */ 
/*      */   
/*   32 */   private static final int FEAT_MASK_ALLOW_MISSING = JsonParser.Feature.ALLOW_MISSING_VALUES.getMask();
/*   33 */   private static final int FEAT_MASK_ALLOW_SINGLE_QUOTES = JsonParser.Feature.ALLOW_SINGLE_QUOTES.getMask();
/*   34 */   private static final int FEAT_MASK_ALLOW_UNQUOTED_NAMES = JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES.getMask();
/*      */   
/*   36 */   private static final int FEAT_MASK_ALLOW_JAVA_COMMENTS = JsonParser.Feature.ALLOW_COMMENTS.getMask();
/*   37 */   private static final int FEAT_MASK_ALLOW_YAML_COMMENTS = JsonParser.Feature.ALLOW_YAML_COMMENTS.getMask();
/*      */ 
/*      */ 
/*      */   
/*   41 */   protected static final int[] _icLatin1 = CharTypes.getInputCodeLatin1();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Reader _reader;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected char[] _inputBuffer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _bufferRecyclable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected ObjectCodec _objectCodec;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final CharsToNameCanonicalizer _symbols;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final int _hashSeed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _tokenIncomplete;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected long _nameStartOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int _nameStartRow;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int _nameStartCol;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReaderBasedJsonParser(IOContext ctxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st, char[] inputBuffer, int start, int end, boolean bufferRecyclable) {
/*  133 */     super(ctxt, features);
/*  134 */     this._reader = r;
/*  135 */     this._inputBuffer = inputBuffer;
/*  136 */     this._inputPtr = start;
/*  137 */     this._inputEnd = end;
/*  138 */     this._objectCodec = codec;
/*  139 */     this._symbols = st;
/*  140 */     this._hashSeed = st.hashSeed();
/*  141 */     this._bufferRecyclable = bufferRecyclable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ReaderBasedJsonParser(IOContext ctxt, int features, Reader r, ObjectCodec codec, CharsToNameCanonicalizer st) {
/*  151 */     super(ctxt, features);
/*  152 */     this._reader = r;
/*  153 */     this._inputBuffer = ctxt.allocTokenBuffer();
/*  154 */     this._inputPtr = 0;
/*  155 */     this._inputEnd = 0;
/*  156 */     this._objectCodec = codec;
/*  157 */     this._symbols = st;
/*  158 */     this._hashSeed = st.hashSeed();
/*  159 */     this._bufferRecyclable = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ObjectCodec getCodec() {
/*  168 */     return this._objectCodec; } public void setCodec(ObjectCodec c) {
/*  169 */     this._objectCodec = c;
/*      */   }
/*      */   
/*      */   public int releaseBuffered(Writer w) throws IOException {
/*  173 */     int count = this._inputEnd - this._inputPtr;
/*  174 */     if (count < 1) return 0;
/*      */     
/*  176 */     int origPtr = this._inputPtr;
/*  177 */     w.write(this._inputBuffer, origPtr, count);
/*  178 */     return count;
/*      */   }
/*      */   public Object getInputSource() {
/*  181 */     return this._reader;
/*      */   }
/*      */   @Deprecated
/*      */   protected char getNextChar(String eofMsg) throws IOException {
/*  185 */     return getNextChar(eofMsg, (JsonToken)null);
/*      */   }
/*      */   
/*      */   protected char getNextChar(String eofMsg, JsonToken forToken) throws IOException {
/*  189 */     if (this._inputPtr >= this._inputEnd && 
/*  190 */       !_loadMore()) {
/*  191 */       _reportInvalidEOF(eofMsg, forToken);
/*      */     }
/*      */     
/*  194 */     return this._inputBuffer[this._inputPtr++];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _closeInput() throws IOException {
/*  206 */     if (this._reader != null) {
/*  207 */       if (this._ioContext.isResourceManaged() || isEnabled(JsonParser.Feature.AUTO_CLOSE_SOURCE)) {
/*  208 */         this._reader.close();
/*      */       }
/*  210 */       this._reader = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _releaseBuffers() throws IOException {
/*  222 */     super._releaseBuffers();
/*      */     
/*  224 */     this._symbols.release();
/*      */     
/*  226 */     if (this._bufferRecyclable) {
/*  227 */       char[] buf = this._inputBuffer;
/*  228 */       if (buf != null) {
/*  229 */         this._inputBuffer = null;
/*  230 */         this._ioContext.releaseTokenBuffer(buf);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _loadMoreGuaranteed() throws IOException {
/*  242 */     if (!_loadMore()) _reportInvalidEOF();
/*      */   
/*      */   }
/*      */   
/*      */   protected boolean _loadMore() throws IOException {
/*  247 */     int bufSize = this._inputEnd;
/*      */     
/*  249 */     if (this._reader != null) {
/*  250 */       int count = this._reader.read(this._inputBuffer, 0, this._inputBuffer.length);
/*  251 */       if (count > 0) {
/*  252 */         this._inputPtr = 0;
/*  253 */         this._inputEnd = count;
/*      */         
/*  255 */         this._currInputProcessed += bufSize;
/*  256 */         this._currInputRowStart -= bufSize;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  261 */         this._nameStartOffset -= bufSize;
/*      */         
/*  263 */         return true;
/*      */       } 
/*      */       
/*  266 */       _closeInput();
/*      */       
/*  268 */       if (count == 0) {
/*  269 */         throw new IOException("Reader returned 0 characters when trying to read " + this._inputEnd);
/*      */       }
/*      */     } 
/*  272 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getText() throws IOException {
/*  290 */     JsonToken t = this._currToken;
/*  291 */     if (t == JsonToken.VALUE_STRING) {
/*  292 */       if (this._tokenIncomplete) {
/*  293 */         this._tokenIncomplete = false;
/*  294 */         _finishString();
/*      */       } 
/*  296 */       return this._textBuffer.contentsAsString();
/*      */     } 
/*  298 */     return _getText2(t);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getText(Writer writer) throws IOException {
/*  304 */     JsonToken t = this._currToken;
/*  305 */     if (t == JsonToken.VALUE_STRING) {
/*  306 */       if (this._tokenIncomplete) {
/*  307 */         this._tokenIncomplete = false;
/*  308 */         _finishString();
/*      */       } 
/*  310 */       return this._textBuffer.contentsToWriter(writer);
/*      */     } 
/*  312 */     if (t == JsonToken.FIELD_NAME) {
/*  313 */       String n = this._parsingContext.getCurrentName();
/*  314 */       writer.write(n);
/*  315 */       return n.length();
/*      */     } 
/*  317 */     if (t != null) {
/*  318 */       if (t.isNumeric()) {
/*  319 */         return this._textBuffer.contentsToWriter(writer);
/*      */       }
/*  321 */       char[] ch = t.asCharArray();
/*  322 */       writer.write(ch);
/*  323 */       return ch.length;
/*      */     } 
/*  325 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getValueAsString() throws IOException {
/*  334 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  335 */       if (this._tokenIncomplete) {
/*  336 */         this._tokenIncomplete = false;
/*  337 */         _finishString();
/*      */       } 
/*  339 */       return this._textBuffer.contentsAsString();
/*      */     } 
/*  341 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  342 */       return getCurrentName();
/*      */     }
/*  344 */     return super.getValueAsString(null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String getValueAsString(String defValue) throws IOException {
/*  350 */     if (this._currToken == JsonToken.VALUE_STRING) {
/*  351 */       if (this._tokenIncomplete) {
/*  352 */         this._tokenIncomplete = false;
/*  353 */         _finishString();
/*      */       } 
/*  355 */       return this._textBuffer.contentsAsString();
/*      */     } 
/*  357 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  358 */       return getCurrentName();
/*      */     }
/*  360 */     return super.getValueAsString(defValue);
/*      */   }
/*      */   
/*      */   protected final String _getText2(JsonToken t) {
/*  364 */     if (t == null) {
/*  365 */       return null;
/*      */     }
/*  367 */     switch (t.id()) {
/*      */       case 5:
/*  369 */         return this._parsingContext.getCurrentName();
/*      */ 
/*      */       
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*  375 */         return this._textBuffer.contentsAsString();
/*      */     } 
/*  377 */     return t.asString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final char[] getTextCharacters() throws IOException {
/*  384 */     if (this._currToken != null) {
/*  385 */       switch (this._currToken.id()) {
/*      */         case 5:
/*  387 */           if (!this._nameCopied) {
/*  388 */             String name = this._parsingContext.getCurrentName();
/*  389 */             int nameLen = name.length();
/*  390 */             if (this._nameCopyBuffer == null) {
/*  391 */               this._nameCopyBuffer = this._ioContext.allocNameCopyBuffer(nameLen);
/*  392 */             } else if (this._nameCopyBuffer.length < nameLen) {
/*  393 */               this._nameCopyBuffer = new char[nameLen];
/*      */             } 
/*  395 */             name.getChars(0, nameLen, this._nameCopyBuffer, 0);
/*  396 */             this._nameCopied = true;
/*      */           } 
/*  398 */           return this._nameCopyBuffer;
/*      */         case 6:
/*  400 */           if (this._tokenIncomplete) {
/*  401 */             this._tokenIncomplete = false;
/*  402 */             _finishString();
/*      */           } 
/*      */         
/*      */         case 7:
/*      */         case 8:
/*  407 */           return this._textBuffer.getTextBuffer();
/*      */       } 
/*  409 */       return this._currToken.asCharArray();
/*      */     } 
/*      */     
/*  412 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getTextLength() throws IOException {
/*  418 */     if (this._currToken != null) {
/*  419 */       switch (this._currToken.id()) {
/*      */         case 5:
/*  421 */           return this._parsingContext.getCurrentName().length();
/*      */         case 6:
/*  423 */           if (this._tokenIncomplete) {
/*  424 */             this._tokenIncomplete = false;
/*  425 */             _finishString();
/*      */           } 
/*      */         
/*      */         case 7:
/*      */         case 8:
/*  430 */           return this._textBuffer.size();
/*      */       } 
/*  432 */       return (this._currToken.asCharArray()).length;
/*      */     } 
/*      */     
/*  435 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int getTextOffset() throws IOException {
/*  442 */     if (this._currToken != null) {
/*  443 */       switch (this._currToken.id()) {
/*      */         case 5:
/*  445 */           return 0;
/*      */         case 6:
/*  447 */           if (this._tokenIncomplete) {
/*  448 */             this._tokenIncomplete = false;
/*  449 */             _finishString();
/*      */           } 
/*      */         
/*      */         case 7:
/*      */         case 8:
/*  454 */           return this._textBuffer.getTextOffset();
/*      */       } 
/*      */     
/*      */     }
/*  458 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBinaryValue(Base64Variant b64variant) throws IOException {
/*  464 */     if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT && this._binaryValue != null) {
/*  465 */       return this._binaryValue;
/*      */     }
/*  467 */     if (this._currToken != JsonToken.VALUE_STRING) {
/*  468 */       _reportError("Current token (" + this._currToken + ") not VALUE_STRING or VALUE_EMBEDDED_OBJECT, can not access as binary");
/*      */     }
/*      */     
/*  471 */     if (this._tokenIncomplete) {
/*      */       try {
/*  473 */         this._binaryValue = _decodeBase64(b64variant);
/*  474 */       } catch (IllegalArgumentException iae) {
/*  475 */         throw _constructError("Failed to decode VALUE_STRING as base64 (" + b64variant + "): " + iae.getMessage());
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  480 */       this._tokenIncomplete = false;
/*      */     }
/*  482 */     else if (this._binaryValue == null) {
/*      */       
/*  484 */       ByteArrayBuilder builder = _getByteArrayBuilder();
/*  485 */       _decodeBase64(getText(), builder, b64variant);
/*  486 */       this._binaryValue = builder.toByteArray();
/*      */     } 
/*      */     
/*  489 */     return this._binaryValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int readBinaryValue(Base64Variant b64variant, OutputStream out) throws IOException {
/*  496 */     if (!this._tokenIncomplete || this._currToken != JsonToken.VALUE_STRING) {
/*  497 */       byte[] b = getBinaryValue(b64variant);
/*  498 */       out.write(b);
/*  499 */       return b.length;
/*      */     } 
/*      */     
/*  502 */     byte[] buf = this._ioContext.allocBase64Buffer();
/*      */     try {
/*  504 */       return _readBinary(b64variant, out, buf);
/*      */     } finally {
/*  506 */       this._ioContext.releaseBase64Buffer(buf);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected int _readBinary(Base64Variant b64variant, OutputStream out, byte[] buffer) throws IOException {
/*  512 */     int outputPtr = 0;
/*  513 */     int outputEnd = buffer.length - 3;
/*  514 */     int outputCount = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  520 */       if (this._inputPtr >= this._inputEnd) {
/*  521 */         _loadMoreGuaranteed();
/*      */       }
/*  523 */       char ch = this._inputBuffer[this._inputPtr++];
/*  524 */       if (ch > ' ') {
/*  525 */         int bits = b64variant.decodeBase64Char(ch);
/*  526 */         if (bits < 0) {
/*  527 */           if (ch == '"') {
/*      */             break;
/*      */           }
/*  530 */           bits = _decodeBase64Escape(b64variant, ch, 0);
/*  531 */           if (bits < 0) {
/*      */             continue;
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/*  537 */         if (outputPtr > outputEnd) {
/*  538 */           outputCount += outputPtr;
/*  539 */           out.write(buffer, 0, outputPtr);
/*  540 */           outputPtr = 0;
/*      */         } 
/*      */         
/*  543 */         int decodedData = bits;
/*      */ 
/*      */ 
/*      */         
/*  547 */         if (this._inputPtr >= this._inputEnd) {
/*  548 */           _loadMoreGuaranteed();
/*      */         }
/*  550 */         ch = this._inputBuffer[this._inputPtr++];
/*  551 */         bits = b64variant.decodeBase64Char(ch);
/*  552 */         if (bits < 0) {
/*  553 */           bits = _decodeBase64Escape(b64variant, ch, 1);
/*      */         }
/*  555 */         decodedData = decodedData << 6 | bits;
/*      */ 
/*      */         
/*  558 */         if (this._inputPtr >= this._inputEnd) {
/*  559 */           _loadMoreGuaranteed();
/*      */         }
/*  561 */         ch = this._inputBuffer[this._inputPtr++];
/*  562 */         bits = b64variant.decodeBase64Char(ch);
/*      */ 
/*      */         
/*  565 */         if (bits < 0) {
/*  566 */           if (bits != -2) {
/*      */             
/*  568 */             if (ch == '"') {
/*  569 */               decodedData >>= 4;
/*  570 */               buffer[outputPtr++] = (byte)decodedData;
/*  571 */               if (b64variant.usesPadding()) {
/*  572 */                 this._inputPtr--;
/*  573 */                 _handleBase64MissingPadding(b64variant);
/*      */               } 
/*      */               break;
/*      */             } 
/*  577 */             bits = _decodeBase64Escape(b64variant, ch, 2);
/*      */           } 
/*  579 */           if (bits == -2) {
/*      */             
/*  581 */             if (this._inputPtr >= this._inputEnd) {
/*  582 */               _loadMoreGuaranteed();
/*      */             }
/*  584 */             ch = this._inputBuffer[this._inputPtr++];
/*  585 */             if (!b64variant.usesPaddingChar(ch) && 
/*  586 */               _decodeBase64Escape(b64variant, ch, 3) != -2) {
/*  587 */               throw reportInvalidBase64Char(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */             }
/*      */ 
/*      */             
/*  591 */             decodedData >>= 4;
/*  592 */             buffer[outputPtr++] = (byte)decodedData;
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  597 */         decodedData = decodedData << 6 | bits;
/*      */         
/*  599 */         if (this._inputPtr >= this._inputEnd) {
/*  600 */           _loadMoreGuaranteed();
/*      */         }
/*  602 */         ch = this._inputBuffer[this._inputPtr++];
/*  603 */         bits = b64variant.decodeBase64Char(ch);
/*  604 */         if (bits < 0) {
/*  605 */           if (bits != -2) {
/*      */             
/*  607 */             if (ch == '"') {
/*  608 */               decodedData >>= 2;
/*  609 */               buffer[outputPtr++] = (byte)(decodedData >> 8);
/*  610 */               buffer[outputPtr++] = (byte)decodedData;
/*  611 */               if (b64variant.usesPadding()) {
/*  612 */                 this._inputPtr--;
/*  613 */                 _handleBase64MissingPadding(b64variant);
/*      */               } 
/*      */               break;
/*      */             } 
/*  617 */             bits = _decodeBase64Escape(b64variant, ch, 3);
/*      */           } 
/*  619 */           if (bits == -2) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  626 */             decodedData >>= 2;
/*  627 */             buffer[outputPtr++] = (byte)(decodedData >> 8);
/*  628 */             buffer[outputPtr++] = (byte)decodedData;
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*  633 */         decodedData = decodedData << 6 | bits;
/*  634 */         buffer[outputPtr++] = (byte)(decodedData >> 16);
/*  635 */         buffer[outputPtr++] = (byte)(decodedData >> 8);
/*  636 */         buffer[outputPtr++] = (byte)decodedData;
/*      */       } 
/*  638 */     }  this._tokenIncomplete = false;
/*  639 */     if (outputPtr > 0) {
/*  640 */       outputCount += outputPtr;
/*  641 */       out.write(buffer, 0, outputPtr);
/*      */     } 
/*  643 */     return outputCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final JsonToken nextToken() throws IOException {
/*      */     JsonToken t;
/*  663 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  664 */       return _nextAfterName();
/*      */     }
/*      */ 
/*      */     
/*  668 */     this._numTypesValid = 0;
/*  669 */     if (this._tokenIncomplete) {
/*  670 */       _skipString();
/*      */     }
/*  672 */     int i = _skipWSOrEnd();
/*  673 */     if (i < 0) {
/*      */ 
/*      */       
/*  676 */       close();
/*  677 */       return this._currToken = null;
/*      */     } 
/*      */     
/*  680 */     this._binaryValue = null;
/*      */ 
/*      */     
/*  683 */     if (i == 93 || i == 125) {
/*  684 */       _closeScope(i);
/*  685 */       return this._currToken;
/*      */     } 
/*      */ 
/*      */     
/*  689 */     if (this._parsingContext.expectComma()) {
/*  690 */       i = _skipComma(i);
/*      */ 
/*      */       
/*  693 */       if ((this._features & FEAT_MASK_TRAILING_COMMA) != 0 && (
/*  694 */         i == 93 || i == 125)) {
/*  695 */         _closeScope(i);
/*  696 */         return this._currToken;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  704 */     boolean inObject = this._parsingContext.inObject();
/*  705 */     if (inObject) {
/*      */       
/*  707 */       _updateNameLocation();
/*  708 */       String name = (i == 34) ? _parseName() : _handleOddName(i);
/*  709 */       this._parsingContext.setCurrentName(name);
/*  710 */       this._currToken = JsonToken.FIELD_NAME;
/*  711 */       i = _skipColon();
/*      */     } 
/*  713 */     _updateLocation();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  719 */     switch (i) {
/*      */       case 34:
/*  721 */         this._tokenIncomplete = true;
/*  722 */         t = JsonToken.VALUE_STRING;
/*      */         break;
/*      */       case 91:
/*  725 */         if (!inObject) {
/*  726 */           this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*      */         }
/*  728 */         t = JsonToken.START_ARRAY;
/*      */         break;
/*      */       case 123:
/*  731 */         if (!inObject) {
/*  732 */           this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */         }
/*  734 */         t = JsonToken.START_OBJECT;
/*      */         break;
/*      */ 
/*      */       
/*      */       case 125:
/*  739 */         _reportUnexpectedChar(i, "expected a value");
/*      */       case 116:
/*  741 */         _matchTrue();
/*  742 */         t = JsonToken.VALUE_TRUE;
/*      */         break;
/*      */       case 102:
/*  745 */         _matchFalse();
/*  746 */         t = JsonToken.VALUE_FALSE;
/*      */         break;
/*      */       case 110:
/*  749 */         _matchNull();
/*  750 */         t = JsonToken.VALUE_NULL;
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 45:
/*  758 */         t = _parseNegNumber();
/*      */         break;
/*      */       case 48:
/*      */       case 49:
/*      */       case 50:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 54:
/*      */       case 55:
/*      */       case 56:
/*      */       case 57:
/*  770 */         t = _parsePosNumber(i);
/*      */         break;
/*      */       default:
/*  773 */         t = _handleOddValue(i);
/*      */         break;
/*      */     } 
/*      */     
/*  777 */     if (inObject) {
/*  778 */       this._nextToken = t;
/*  779 */       return this._currToken;
/*      */     } 
/*  781 */     this._currToken = t;
/*  782 */     return t;
/*      */   }
/*      */ 
/*      */   
/*      */   private final JsonToken _nextAfterName() {
/*  787 */     this._nameCopied = false;
/*  788 */     JsonToken t = this._nextToken;
/*  789 */     this._nextToken = null;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  794 */     if (t == JsonToken.START_ARRAY) {
/*  795 */       this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/*  796 */     } else if (t == JsonToken.START_OBJECT) {
/*  797 */       this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */     } 
/*  799 */     return this._currToken = t;
/*      */   }
/*      */ 
/*      */   
/*      */   public void finishToken() throws IOException {
/*  804 */     if (this._tokenIncomplete) {
/*  805 */       this._tokenIncomplete = false;
/*  806 */       _finishString();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean nextFieldName(SerializableString sstr) throws IOException {
/*  822 */     this._numTypesValid = 0;
/*  823 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  824 */       _nextAfterName();
/*  825 */       return false;
/*      */     } 
/*  827 */     if (this._tokenIncomplete) {
/*  828 */       _skipString();
/*      */     }
/*  830 */     int i = _skipWSOrEnd();
/*  831 */     if (i < 0) {
/*  832 */       close();
/*  833 */       this._currToken = null;
/*  834 */       return false;
/*      */     } 
/*  836 */     this._binaryValue = null;
/*      */ 
/*      */     
/*  839 */     if (i == 93 || i == 125) {
/*  840 */       _closeScope(i);
/*  841 */       return false;
/*      */     } 
/*      */     
/*  844 */     if (this._parsingContext.expectComma()) {
/*  845 */       i = _skipComma(i);
/*      */ 
/*      */       
/*  848 */       if ((this._features & FEAT_MASK_TRAILING_COMMA) != 0 && (
/*  849 */         i == 93 || i == 125)) {
/*  850 */         _closeScope(i);
/*  851 */         return false;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  856 */     if (!this._parsingContext.inObject()) {
/*  857 */       _updateLocation();
/*  858 */       _nextTokenNotInObject(i);
/*  859 */       return false;
/*      */     } 
/*      */     
/*  862 */     _updateNameLocation();
/*  863 */     if (i == 34) {
/*      */       
/*  865 */       char[] nameChars = sstr.asQuotedChars();
/*  866 */       int len = nameChars.length;
/*      */ 
/*      */       
/*  869 */       if (this._inputPtr + len + 4 < this._inputEnd) {
/*      */         
/*  871 */         int end = this._inputPtr + len;
/*  872 */         if (this._inputBuffer[end] == '"') {
/*  873 */           int offset = 0;
/*  874 */           int ptr = this._inputPtr;
/*      */           while (true) {
/*  876 */             if (ptr == end) {
/*  877 */               this._parsingContext.setCurrentName(sstr.getValue());
/*  878 */               _isNextTokenNameYes(_skipColonFast(ptr + 1));
/*  879 */               return true;
/*      */             } 
/*  881 */             if (nameChars[offset] != this._inputBuffer[ptr]) {
/*      */               break;
/*      */             }
/*  884 */             offset++;
/*  885 */             ptr++;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*  890 */     return _isNextTokenNameMaybe(i, sstr.getValue());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String nextFieldName() throws IOException {
/*  898 */     this._numTypesValid = 0;
/*  899 */     if (this._currToken == JsonToken.FIELD_NAME) {
/*  900 */       _nextAfterName();
/*  901 */       return null;
/*      */     } 
/*  903 */     if (this._tokenIncomplete) {
/*  904 */       _skipString();
/*      */     }
/*  906 */     int i = _skipWSOrEnd();
/*  907 */     if (i < 0) {
/*  908 */       close();
/*  909 */       this._currToken = null;
/*  910 */       return null;
/*      */     } 
/*  912 */     this._binaryValue = null;
/*  913 */     if (i == 93 || i == 125) {
/*  914 */       _closeScope(i);
/*  915 */       return null;
/*      */     } 
/*  917 */     if (this._parsingContext.expectComma()) {
/*  918 */       i = _skipComma(i);
/*  919 */       if ((this._features & FEAT_MASK_TRAILING_COMMA) != 0 && (
/*  920 */         i == 93 || i == 125)) {
/*  921 */         _closeScope(i);
/*  922 */         return null;
/*      */       } 
/*      */     } 
/*      */     
/*  926 */     if (!this._parsingContext.inObject()) {
/*  927 */       _updateLocation();
/*  928 */       _nextTokenNotInObject(i);
/*  929 */       return null;
/*      */     } 
/*      */     
/*  932 */     _updateNameLocation();
/*  933 */     String name = (i == 34) ? _parseName() : _handleOddName(i);
/*  934 */     this._parsingContext.setCurrentName(name);
/*  935 */     this._currToken = JsonToken.FIELD_NAME;
/*  936 */     i = _skipColon();
/*      */     
/*  938 */     _updateLocation();
/*  939 */     if (i == 34) {
/*  940 */       this._tokenIncomplete = true;
/*  941 */       this._nextToken = JsonToken.VALUE_STRING;
/*  942 */       return name;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  949 */     switch (i)
/*      */     { case 45:
/*  951 */         t = _parseNegNumber();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  987 */         this._nextToken = t;
/*  988 */         return name;case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57: t = _parsePosNumber(i); this._nextToken = t; return name;case 102: _matchFalse(); t = JsonToken.VALUE_FALSE; this._nextToken = t; return name;case 110: _matchNull(); t = JsonToken.VALUE_NULL; this._nextToken = t; return name;case 116: _matchTrue(); t = JsonToken.VALUE_TRUE; this._nextToken = t; return name;case 91: t = JsonToken.START_ARRAY; this._nextToken = t; return name;case 123: t = JsonToken.START_OBJECT; this._nextToken = t; return name; }  JsonToken t = _handleOddValue(i); this._nextToken = t; return name;
/*      */   }
/*      */ 
/*      */   
/*      */   private final void _isNextTokenNameYes(int i) throws IOException {
/*  993 */     this._currToken = JsonToken.FIELD_NAME;
/*  994 */     _updateLocation();
/*      */     
/*  996 */     switch (i) {
/*      */       case 34:
/*  998 */         this._tokenIncomplete = true;
/*  999 */         this._nextToken = JsonToken.VALUE_STRING;
/*      */         return;
/*      */       case 91:
/* 1002 */         this._nextToken = JsonToken.START_ARRAY;
/*      */         return;
/*      */       case 123:
/* 1005 */         this._nextToken = JsonToken.START_OBJECT;
/*      */         return;
/*      */       case 116:
/* 1008 */         _matchToken("true", 1);
/* 1009 */         this._nextToken = JsonToken.VALUE_TRUE;
/*      */         return;
/*      */       case 102:
/* 1012 */         _matchToken("false", 1);
/* 1013 */         this._nextToken = JsonToken.VALUE_FALSE;
/*      */         return;
/*      */       case 110:
/* 1016 */         _matchToken("null", 1);
/* 1017 */         this._nextToken = JsonToken.VALUE_NULL;
/*      */         return;
/*      */       case 45:
/* 1020 */         this._nextToken = _parseNegNumber();
/*      */         return;
/*      */       case 48:
/*      */       case 49:
/*      */       case 50:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 54:
/*      */       case 55:
/*      */       case 56:
/*      */       case 57:
/* 1032 */         this._nextToken = _parsePosNumber(i);
/*      */         return;
/*      */     } 
/* 1035 */     this._nextToken = _handleOddValue(i);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _isNextTokenNameMaybe(int i, String nameToMatch) throws IOException {
/* 1041 */     String name = (i == 34) ? _parseName() : _handleOddName(i);
/* 1042 */     this._parsingContext.setCurrentName(name);
/* 1043 */     this._currToken = JsonToken.FIELD_NAME;
/* 1044 */     i = _skipColon();
/* 1045 */     _updateLocation();
/* 1046 */     if (i == 34) {
/* 1047 */       this._tokenIncomplete = true;
/* 1048 */       this._nextToken = JsonToken.VALUE_STRING;
/* 1049 */       return nameToMatch.equals(name);
/*      */     } 
/*      */ 
/*      */     
/* 1053 */     switch (i)
/*      */     { case 45:
/* 1055 */         t = _parseNegNumber();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1091 */         this._nextToken = t;
/* 1092 */         return nameToMatch.equals(name);case 48: case 49: case 50: case 51: case 52: case 53: case 54: case 55: case 56: case 57: t = _parsePosNumber(i); this._nextToken = t; return nameToMatch.equals(name);case 102: _matchFalse(); t = JsonToken.VALUE_FALSE; this._nextToken = t; return nameToMatch.equals(name);case 110: _matchNull(); t = JsonToken.VALUE_NULL; this._nextToken = t; return nameToMatch.equals(name);case 116: _matchTrue(); t = JsonToken.VALUE_TRUE; this._nextToken = t; return nameToMatch.equals(name);case 91: t = JsonToken.START_ARRAY; this._nextToken = t; return nameToMatch.equals(name);case 123: t = JsonToken.START_OBJECT; this._nextToken = t; return nameToMatch.equals(name); }  JsonToken t = _handleOddValue(i); this._nextToken = t; return nameToMatch.equals(name);
/*      */   }
/*      */ 
/*      */   
/*      */   private final JsonToken _nextTokenNotInObject(int i) throws IOException {
/* 1097 */     if (i == 34) {
/* 1098 */       this._tokenIncomplete = true;
/* 1099 */       return this._currToken = JsonToken.VALUE_STRING;
/*      */     } 
/* 1101 */     switch (i) {
/*      */       case 91:
/* 1103 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1104 */         return this._currToken = JsonToken.START_ARRAY;
/*      */       case 123:
/* 1106 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/* 1107 */         return this._currToken = JsonToken.START_OBJECT;
/*      */       case 116:
/* 1109 */         _matchToken("true", 1);
/* 1110 */         return this._currToken = JsonToken.VALUE_TRUE;
/*      */       case 102:
/* 1112 */         _matchToken("false", 1);
/* 1113 */         return this._currToken = JsonToken.VALUE_FALSE;
/*      */       case 110:
/* 1115 */         _matchToken("null", 1);
/* 1116 */         return this._currToken = JsonToken.VALUE_NULL;
/*      */       case 45:
/* 1118 */         return this._currToken = _parseNegNumber();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 48:
/*      */       case 49:
/*      */       case 50:
/*      */       case 51:
/*      */       case 52:
/*      */       case 53:
/*      */       case 54:
/*      */       case 55:
/*      */       case 56:
/*      */       case 57:
/* 1133 */         return this._currToken = _parsePosNumber(i);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 44:
/*      */       case 93:
/* 1144 */         if ((this._features & FEAT_MASK_ALLOW_MISSING) != 0) {
/* 1145 */           this._inputPtr--;
/* 1146 */           return this._currToken = JsonToken.VALUE_NULL;
/*      */         }  break;
/*      */     } 
/* 1149 */     return this._currToken = _handleOddValue(i);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final String nextTextValue() throws IOException {
/* 1155 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1156 */       this._nameCopied = false;
/* 1157 */       JsonToken t = this._nextToken;
/* 1158 */       this._nextToken = null;
/* 1159 */       this._currToken = t;
/* 1160 */       if (t == JsonToken.VALUE_STRING) {
/* 1161 */         if (this._tokenIncomplete) {
/* 1162 */           this._tokenIncomplete = false;
/* 1163 */           _finishString();
/*      */         } 
/* 1165 */         return this._textBuffer.contentsAsString();
/*      */       } 
/* 1167 */       if (t == JsonToken.START_ARRAY) {
/* 1168 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1169 */       } else if (t == JsonToken.START_OBJECT) {
/* 1170 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       } 
/* 1172 */       return null;
/*      */     } 
/*      */     
/* 1175 */     return (nextToken() == JsonToken.VALUE_STRING) ? getText() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final int nextIntValue(int defaultValue) throws IOException {
/* 1182 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1183 */       this._nameCopied = false;
/* 1184 */       JsonToken t = this._nextToken;
/* 1185 */       this._nextToken = null;
/* 1186 */       this._currToken = t;
/* 1187 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 1188 */         return getIntValue();
/*      */       }
/* 1190 */       if (t == JsonToken.START_ARRAY) {
/* 1191 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1192 */       } else if (t == JsonToken.START_OBJECT) {
/* 1193 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       } 
/* 1195 */       return defaultValue;
/*      */     } 
/*      */     
/* 1198 */     return (nextToken() == JsonToken.VALUE_NUMBER_INT) ? getIntValue() : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final long nextLongValue(long defaultValue) throws IOException {
/* 1205 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1206 */       this._nameCopied = false;
/* 1207 */       JsonToken t = this._nextToken;
/* 1208 */       this._nextToken = null;
/* 1209 */       this._currToken = t;
/* 1210 */       if (t == JsonToken.VALUE_NUMBER_INT) {
/* 1211 */         return getLongValue();
/*      */       }
/* 1213 */       if (t == JsonToken.START_ARRAY) {
/* 1214 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1215 */       } else if (t == JsonToken.START_OBJECT) {
/* 1216 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       } 
/* 1218 */       return defaultValue;
/*      */     } 
/*      */     
/* 1221 */     return (nextToken() == JsonToken.VALUE_NUMBER_INT) ? getLongValue() : defaultValue;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final Boolean nextBooleanValue() throws IOException {
/* 1228 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 1229 */       this._nameCopied = false;
/* 1230 */       JsonToken jsonToken = this._nextToken;
/* 1231 */       this._nextToken = null;
/* 1232 */       this._currToken = jsonToken;
/* 1233 */       if (jsonToken == JsonToken.VALUE_TRUE) {
/* 1234 */         return Boolean.TRUE;
/*      */       }
/* 1236 */       if (jsonToken == JsonToken.VALUE_FALSE) {
/* 1237 */         return Boolean.FALSE;
/*      */       }
/* 1239 */       if (jsonToken == JsonToken.START_ARRAY) {
/* 1240 */         this._parsingContext = this._parsingContext.createChildArrayContext(this._tokenInputRow, this._tokenInputCol);
/* 1241 */       } else if (jsonToken == JsonToken.START_OBJECT) {
/* 1242 */         this._parsingContext = this._parsingContext.createChildObjectContext(this._tokenInputRow, this._tokenInputCol);
/*      */       } 
/* 1244 */       return null;
/*      */     } 
/* 1246 */     JsonToken t = nextToken();
/* 1247 */     if (t != null) {
/* 1248 */       int id = t.id();
/* 1249 */       if (id == 9) return Boolean.TRUE; 
/* 1250 */       if (id == 10) return Boolean.FALSE; 
/*      */     } 
/* 1252 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JsonToken _parsePosNumber(int ch) throws IOException {
/* 1283 */     int ptr = this._inputPtr;
/* 1284 */     int startPtr = ptr - 1;
/* 1285 */     int inputLen = this._inputEnd;
/*      */ 
/*      */     
/* 1288 */     if (ch == 48) {
/* 1289 */       return _parseNumber2(false, startPtr);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1298 */     int intLen = 1;
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/* 1303 */       if (ptr >= inputLen) {
/* 1304 */         this._inputPtr = startPtr;
/* 1305 */         return _parseNumber2(false, startPtr);
/*      */       } 
/* 1307 */       ch = this._inputBuffer[ptr++];
/* 1308 */       if (ch < 48 || ch > 57) {
/*      */         break;
/*      */       }
/* 1311 */       intLen++;
/*      */     } 
/* 1313 */     if (ch == 46 || ch == 101 || ch == 69) {
/* 1314 */       this._inputPtr = ptr;
/* 1315 */       return _parseFloat(ch, startPtr, ptr, false, intLen);
/*      */     } 
/*      */ 
/*      */     
/* 1319 */     this._inputPtr = --ptr;
/*      */     
/* 1321 */     if (this._parsingContext.inRoot()) {
/* 1322 */       _verifyRootSpace(ch);
/*      */     }
/* 1324 */     int len = ptr - startPtr;
/* 1325 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, len);
/* 1326 */     return resetInt(false, intLen);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final JsonToken _parseFloat(int ch, int startPtr, int ptr, boolean neg, int intLen) throws IOException {
/* 1332 */     int inputLen = this._inputEnd;
/* 1333 */     int fractLen = 0;
/*      */ 
/*      */     
/* 1336 */     if (ch == 46) {
/*      */       
/*      */       while (true) {
/* 1339 */         if (ptr >= inputLen) {
/* 1340 */           return _parseNumber2(neg, startPtr);
/*      */         }
/* 1342 */         ch = this._inputBuffer[ptr++];
/* 1343 */         if (ch < 48 || ch > 57) {
/*      */           break;
/*      */         }
/* 1346 */         fractLen++;
/*      */       } 
/*      */       
/* 1349 */       if (fractLen == 0) {
/* 1350 */         reportUnexpectedNumberChar(ch, "Decimal point not followed by a digit");
/*      */       }
/*      */     } 
/* 1353 */     int expLen = 0;
/* 1354 */     if (ch == 101 || ch == 69) {
/* 1355 */       if (ptr >= inputLen) {
/* 1356 */         this._inputPtr = startPtr;
/* 1357 */         return _parseNumber2(neg, startPtr);
/*      */       } 
/*      */       
/* 1360 */       ch = this._inputBuffer[ptr++];
/* 1361 */       if (ch == 45 || ch == 43) {
/* 1362 */         if (ptr >= inputLen) {
/* 1363 */           this._inputPtr = startPtr;
/* 1364 */           return _parseNumber2(neg, startPtr);
/*      */         } 
/* 1366 */         ch = this._inputBuffer[ptr++];
/*      */       } 
/* 1368 */       while (ch <= 57 && ch >= 48) {
/* 1369 */         expLen++;
/* 1370 */         if (ptr >= inputLen) {
/* 1371 */           this._inputPtr = startPtr;
/* 1372 */           return _parseNumber2(neg, startPtr);
/*      */         } 
/* 1374 */         ch = this._inputBuffer[ptr++];
/*      */       } 
/*      */       
/* 1377 */       if (expLen == 0) {
/* 1378 */         reportUnexpectedNumberChar(ch, "Exponent indicator not followed by a digit");
/*      */       }
/*      */     } 
/*      */     
/* 1382 */     this._inputPtr = --ptr;
/*      */     
/* 1384 */     if (this._parsingContext.inRoot()) {
/* 1385 */       _verifyRootSpace(ch);
/*      */     }
/* 1387 */     int len = ptr - startPtr;
/* 1388 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, len);
/*      */     
/* 1390 */     return resetFloat(neg, intLen, fractLen, expLen);
/*      */   }
/*      */ 
/*      */   
/*      */   protected final JsonToken _parseNegNumber() throws IOException {
/* 1395 */     int ptr = this._inputPtr;
/* 1396 */     int startPtr = ptr - 1;
/* 1397 */     int inputLen = this._inputEnd;
/*      */     
/* 1399 */     if (ptr >= inputLen) {
/* 1400 */       return _parseNumber2(true, startPtr);
/*      */     }
/* 1402 */     int ch = this._inputBuffer[ptr++];
/*      */     
/* 1404 */     if (ch > 57 || ch < 48) {
/* 1405 */       this._inputPtr = ptr;
/* 1406 */       return _handleInvalidNumberStart(ch, true);
/*      */     } 
/*      */     
/* 1409 */     if (ch == 48) {
/* 1410 */       return _parseNumber2(true, startPtr);
/*      */     }
/* 1412 */     int intLen = 1;
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/* 1417 */       if (ptr >= inputLen) {
/* 1418 */         return _parseNumber2(true, startPtr);
/*      */       }
/* 1420 */       ch = this._inputBuffer[ptr++];
/* 1421 */       if (ch < 48 || ch > 57) {
/*      */         break;
/*      */       }
/* 1424 */       intLen++;
/*      */     } 
/*      */     
/* 1427 */     if (ch == 46 || ch == 101 || ch == 69) {
/* 1428 */       this._inputPtr = ptr;
/* 1429 */       return _parseFloat(ch, startPtr, ptr, true, intLen);
/*      */     } 
/*      */     
/* 1432 */     this._inputPtr = --ptr;
/* 1433 */     if (this._parsingContext.inRoot()) {
/* 1434 */       _verifyRootSpace(ch);
/*      */     }
/* 1436 */     int len = ptr - startPtr;
/* 1437 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, len);
/* 1438 */     return resetInt(true, intLen);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final JsonToken _parseNumber2(boolean neg, int startPtr) throws IOException {
/* 1450 */     this._inputPtr = neg ? (startPtr + 1) : startPtr;
/* 1451 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 1452 */     int outPtr = 0;
/*      */ 
/*      */     
/* 1455 */     if (neg) {
/* 1456 */       outBuf[outPtr++] = '-';
/*      */     }
/*      */ 
/*      */     
/* 1460 */     int intLen = 0;
/*      */     
/* 1462 */     char c = (this._inputPtr < this._inputEnd) ? this._inputBuffer[this._inputPtr++] : getNextChar("No digit following minus sign", JsonToken.VALUE_NUMBER_INT);
/* 1463 */     if (c == '0') {
/* 1464 */       c = _verifyNoLeadingZeroes();
/*      */     }
/* 1466 */     boolean eof = false;
/*      */ 
/*      */ 
/*      */     
/* 1470 */     while (c >= '0' && c <= '9') {
/* 1471 */       intLen++;
/* 1472 */       if (outPtr >= outBuf.length) {
/* 1473 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1474 */         outPtr = 0;
/*      */       } 
/* 1476 */       outBuf[outPtr++] = c;
/* 1477 */       if (this._inputPtr >= this._inputEnd && !_loadMore()) {
/*      */         
/* 1479 */         c = Character.MIN_VALUE;
/* 1480 */         eof = true;
/*      */         break;
/*      */       } 
/* 1483 */       c = this._inputBuffer[this._inputPtr++];
/*      */     } 
/*      */     
/* 1486 */     if (intLen == 0) {
/* 1487 */       return _handleInvalidNumberStart(c, neg);
/*      */     }
/*      */     
/* 1490 */     int fractLen = 0;
/*      */     
/* 1492 */     if (c == '.') {
/* 1493 */       if (outPtr >= outBuf.length) {
/* 1494 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1495 */         outPtr = 0;
/*      */       } 
/* 1497 */       outBuf[outPtr++] = c;
/*      */ 
/*      */       
/*      */       while (true) {
/* 1501 */         if (this._inputPtr >= this._inputEnd && !_loadMore()) {
/* 1502 */           eof = true;
/*      */           break;
/*      */         } 
/* 1505 */         c = this._inputBuffer[this._inputPtr++];
/* 1506 */         if (c < '0' || c > '9') {
/*      */           break;
/*      */         }
/* 1509 */         fractLen++;
/* 1510 */         if (outPtr >= outBuf.length) {
/* 1511 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1512 */           outPtr = 0;
/*      */         } 
/* 1514 */         outBuf[outPtr++] = c;
/*      */       } 
/*      */       
/* 1517 */       if (fractLen == 0) {
/* 1518 */         reportUnexpectedNumberChar(c, "Decimal point not followed by a digit");
/*      */       }
/*      */     } 
/*      */     
/* 1522 */     int expLen = 0;
/* 1523 */     if (c == 'e' || c == 'E') {
/* 1524 */       if (outPtr >= outBuf.length) {
/* 1525 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1526 */         outPtr = 0;
/*      */       } 
/* 1528 */       outBuf[outPtr++] = c;
/*      */ 
/*      */       
/* 1531 */       c = (this._inputPtr < this._inputEnd) ? this._inputBuffer[this._inputPtr++] : getNextChar("expected a digit for number exponent");
/*      */       
/* 1533 */       if (c == '-' || c == '+') {
/* 1534 */         if (outPtr >= outBuf.length) {
/* 1535 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1536 */           outPtr = 0;
/*      */         } 
/* 1538 */         outBuf[outPtr++] = c;
/*      */ 
/*      */         
/* 1541 */         c = (this._inputPtr < this._inputEnd) ? this._inputBuffer[this._inputPtr++] : getNextChar("expected a digit for number exponent");
/*      */       } 
/*      */ 
/*      */       
/* 1545 */       while (c <= '9' && c >= '0') {
/* 1546 */         expLen++;
/* 1547 */         if (outPtr >= outBuf.length) {
/* 1548 */           outBuf = this._textBuffer.finishCurrentSegment();
/* 1549 */           outPtr = 0;
/*      */         } 
/* 1551 */         outBuf[outPtr++] = c;
/* 1552 */         if (this._inputPtr >= this._inputEnd && !_loadMore()) {
/* 1553 */           eof = true;
/*      */           break;
/*      */         } 
/* 1556 */         c = this._inputBuffer[this._inputPtr++];
/*      */       } 
/*      */       
/* 1559 */       if (expLen == 0) {
/* 1560 */         reportUnexpectedNumberChar(c, "Exponent indicator not followed by a digit");
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1565 */     if (!eof) {
/* 1566 */       this._inputPtr--;
/* 1567 */       if (this._parsingContext.inRoot()) {
/* 1568 */         _verifyRootSpace(c);
/*      */       }
/*      */     } 
/* 1571 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1573 */     return reset(neg, intLen, fractLen, expLen);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final char _verifyNoLeadingZeroes() throws IOException {
/* 1583 */     if (this._inputPtr < this._inputEnd) {
/* 1584 */       char ch = this._inputBuffer[this._inputPtr];
/*      */       
/* 1586 */       if (ch < '0' || ch > '9') {
/* 1587 */         return '0';
/*      */       }
/*      */     } 
/*      */     
/* 1591 */     return _verifyNLZ2();
/*      */   }
/*      */ 
/*      */   
/*      */   private char _verifyNLZ2() throws IOException {
/* 1596 */     if (this._inputPtr >= this._inputEnd && !_loadMore()) {
/* 1597 */       return '0';
/*      */     }
/* 1599 */     char ch = this._inputBuffer[this._inputPtr];
/* 1600 */     if (ch < '0' || ch > '9') {
/* 1601 */       return '0';
/*      */     }
/* 1603 */     if ((this._features & FEAT_MASK_LEADING_ZEROS) == 0) {
/* 1604 */       reportInvalidNumber("Leading zeroes not allowed");
/*      */     }
/*      */     
/* 1607 */     this._inputPtr++;
/* 1608 */     if (ch == '0') {
/* 1609 */       while (this._inputPtr < this._inputEnd || _loadMore()) {
/* 1610 */         ch = this._inputBuffer[this._inputPtr];
/* 1611 */         if (ch < '0' || ch > '9') {
/* 1612 */           return '0';
/*      */         }
/* 1614 */         this._inputPtr++;
/* 1615 */         if (ch != '0') {
/*      */           break;
/*      */         }
/*      */       } 
/*      */     }
/* 1620 */     return ch;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonToken _handleInvalidNumberStart(int ch, boolean negative) throws IOException {
/* 1629 */     if (ch == 73) {
/* 1630 */       if (this._inputPtr >= this._inputEnd && 
/* 1631 */         !_loadMore()) {
/* 1632 */         _reportInvalidEOFInValue(JsonToken.VALUE_NUMBER_INT);
/*      */       }
/*      */       
/* 1635 */       ch = this._inputBuffer[this._inputPtr++];
/* 1636 */       if (ch == 78) {
/* 1637 */         String match = negative ? "-INF" : "+INF";
/* 1638 */         _matchToken(match, 3);
/* 1639 */         if ((this._features & FEAT_MASK_NON_NUM_NUMBERS) != 0) {
/* 1640 */           return resetAsNaN(match, negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
/*      */         }
/* 1642 */         _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/* 1643 */       } else if (ch == 110) {
/* 1644 */         String match = negative ? "-Infinity" : "+Infinity";
/* 1645 */         _matchToken(match, 3);
/* 1646 */         if ((this._features & FEAT_MASK_NON_NUM_NUMBERS) != 0) {
/* 1647 */           return resetAsNaN(match, negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
/*      */         }
/* 1649 */         _reportError("Non-standard token '" + match + "': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/*      */       } 
/*      */     } 
/* 1652 */     reportUnexpectedNumberChar(ch, "expected digit (0-9) to follow minus sign, for valid numeric value");
/* 1653 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void _verifyRootSpace(int ch) throws IOException {
/* 1666 */     this._inputPtr++;
/* 1667 */     switch (ch) {
/*      */       case 9:
/*      */       case 32:
/*      */         return;
/*      */       case 13:
/* 1672 */         _skipCR();
/*      */         return;
/*      */       case 10:
/* 1675 */         this._currInputRow++;
/* 1676 */         this._currInputRowStart = this._inputPtr;
/*      */         return;
/*      */     } 
/* 1679 */     _reportMissingRootWS(ch);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final String _parseName() throws IOException {
/* 1692 */     int ptr = this._inputPtr;
/* 1693 */     int hash = this._hashSeed;
/* 1694 */     int[] codes = _icLatin1;
/*      */     
/* 1696 */     while (ptr < this._inputEnd) {
/* 1697 */       int ch = this._inputBuffer[ptr];
/* 1698 */       if (ch < codes.length && codes[ch] != 0) {
/* 1699 */         if (ch == 34) {
/* 1700 */           int i = this._inputPtr;
/* 1701 */           this._inputPtr = ptr + 1;
/* 1702 */           return this._symbols.findSymbol(this._inputBuffer, i, ptr - i, hash);
/*      */         } 
/*      */         break;
/*      */       } 
/* 1706 */       hash = hash * 33 + ch;
/* 1707 */       ptr++;
/*      */     } 
/* 1709 */     int start = this._inputPtr;
/* 1710 */     this._inputPtr = ptr;
/* 1711 */     return _parseName2(start, hash, 34);
/*      */   }
/*      */ 
/*      */   
/*      */   private String _parseName2(int startPtr, int hash, int endChar) throws IOException {
/* 1716 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1721 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/* 1722 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/*      */     
/*      */     while (true) {
/* 1725 */       if (this._inputPtr >= this._inputEnd && 
/* 1726 */         !_loadMore()) {
/* 1727 */         _reportInvalidEOF(" in field name", JsonToken.FIELD_NAME);
/*      */       }
/*      */       
/* 1730 */       char c = this._inputBuffer[this._inputPtr++];
/* 1731 */       int i = c;
/* 1732 */       if (i <= 92) {
/* 1733 */         if (i == 92) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1738 */           c = _decodeEscaped();
/* 1739 */         } else if (i <= endChar) {
/* 1740 */           if (i == endChar) {
/*      */             break;
/*      */           }
/* 1743 */           if (i < 32) {
/* 1744 */             _throwUnquotedSpace(i, "name");
/*      */           }
/*      */         } 
/*      */       }
/* 1748 */       hash = hash * 33 + c;
/*      */       
/* 1750 */       outBuf[outPtr++] = c;
/*      */ 
/*      */       
/* 1753 */       if (outPtr >= outBuf.length) {
/* 1754 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1755 */         outPtr = 0;
/*      */       } 
/*      */     } 
/* 1758 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1760 */     TextBuffer tb = this._textBuffer;
/* 1761 */     char[] buf = tb.getTextBuffer();
/* 1762 */     int start = tb.getTextOffset();
/* 1763 */     int len = tb.size();
/* 1764 */     return this._symbols.findSymbol(buf, start, len, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String _handleOddName(int i) throws IOException {
/*      */     boolean firstOk;
/* 1777 */     if (i == 39 && (this._features & FEAT_MASK_ALLOW_SINGLE_QUOTES) != 0) {
/* 1778 */       return _parseAposName();
/*      */     }
/*      */     
/* 1781 */     if ((this._features & FEAT_MASK_ALLOW_UNQUOTED_NAMES) == 0) {
/* 1782 */       _reportUnexpectedChar(i, "was expecting double-quote to start field name");
/*      */     }
/* 1784 */     int[] codes = CharTypes.getInputCodeLatin1JsNames();
/* 1785 */     int maxCode = codes.length;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1790 */     if (i < maxCode) {
/* 1791 */       firstOk = (codes[i] == 0);
/*      */     } else {
/* 1793 */       firstOk = Character.isJavaIdentifierPart((char)i);
/*      */     } 
/* 1795 */     if (!firstOk) {
/* 1796 */       _reportUnexpectedChar(i, "was expecting either valid name character (for unquoted name) or double-quote (for quoted) to start field name");
/*      */     }
/* 1798 */     int ptr = this._inputPtr;
/* 1799 */     int hash = this._hashSeed;
/* 1800 */     int inputLen = this._inputEnd;
/*      */     
/* 1802 */     if (ptr < inputLen) {
/*      */       do {
/* 1804 */         int ch = this._inputBuffer[ptr];
/* 1805 */         if (ch < maxCode) {
/* 1806 */           if (codes[ch] != 0) {
/* 1807 */             int j = this._inputPtr - 1;
/* 1808 */             this._inputPtr = ptr;
/* 1809 */             return this._symbols.findSymbol(this._inputBuffer, j, ptr - j, hash);
/*      */           } 
/* 1811 */         } else if (!Character.isJavaIdentifierPart((char)ch)) {
/* 1812 */           int j = this._inputPtr - 1;
/* 1813 */           this._inputPtr = ptr;
/* 1814 */           return this._symbols.findSymbol(this._inputBuffer, j, ptr - j, hash);
/*      */         } 
/* 1816 */         hash = hash * 33 + ch;
/* 1817 */         ++ptr;
/* 1818 */       } while (ptr < inputLen);
/*      */     }
/* 1820 */     int start = this._inputPtr - 1;
/* 1821 */     this._inputPtr = ptr;
/* 1822 */     return _handleOddName2(start, hash, codes);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected String _parseAposName() throws IOException {
/* 1828 */     int ptr = this._inputPtr;
/* 1829 */     int hash = this._hashSeed;
/* 1830 */     int inputLen = this._inputEnd;
/*      */     
/* 1832 */     if (ptr < inputLen) {
/* 1833 */       int[] codes = _icLatin1;
/* 1834 */       int maxCode = codes.length;
/*      */       
/*      */       do {
/* 1837 */         int ch = this._inputBuffer[ptr];
/* 1838 */         if (ch == 39) {
/* 1839 */           int i = this._inputPtr;
/* 1840 */           this._inputPtr = ptr + 1;
/* 1841 */           return this._symbols.findSymbol(this._inputBuffer, i, ptr - i, hash);
/*      */         } 
/* 1843 */         if (ch < maxCode && codes[ch] != 0) {
/*      */           break;
/*      */         }
/* 1846 */         hash = hash * 33 + ch;
/* 1847 */         ++ptr;
/* 1848 */       } while (ptr < inputLen);
/*      */     } 
/*      */     
/* 1851 */     int start = this._inputPtr;
/* 1852 */     this._inputPtr = ptr;
/*      */     
/* 1854 */     return _parseName2(start, hash, 39);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonToken _handleOddValue(int i) throws IOException {
/* 1864 */     switch (i) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 39:
/* 1871 */         if ((this._features & FEAT_MASK_ALLOW_SINGLE_QUOTES) != 0) {
/* 1872 */           return _handleApos();
/*      */         }
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 93:
/* 1880 */         if (!this._parsingContext.inArray()) {
/*      */           break;
/*      */         }
/*      */       
/*      */       case 44:
/* 1885 */         if ((this._features & FEAT_MASK_ALLOW_MISSING) != 0) {
/* 1886 */           this._inputPtr--;
/* 1887 */           return JsonToken.VALUE_NULL;
/*      */         } 
/*      */         break;
/*      */       case 78:
/* 1891 */         _matchToken("NaN", 1);
/* 1892 */         if ((this._features & FEAT_MASK_NON_NUM_NUMBERS) != 0) {
/* 1893 */           return resetAsNaN("NaN", Double.NaN);
/*      */         }
/* 1895 */         _reportError("Non-standard token 'NaN': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/*      */         break;
/*      */       case 73:
/* 1898 */         _matchToken("Infinity", 1);
/* 1899 */         if ((this._features & FEAT_MASK_NON_NUM_NUMBERS) != 0) {
/* 1900 */           return resetAsNaN("Infinity", Double.POSITIVE_INFINITY);
/*      */         }
/* 1902 */         _reportError("Non-standard token 'Infinity': enable JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS to allow");
/*      */         break;
/*      */       case 43:
/* 1905 */         if (this._inputPtr >= this._inputEnd && 
/* 1906 */           !_loadMore()) {
/* 1907 */           _reportInvalidEOFInValue(JsonToken.VALUE_NUMBER_INT);
/*      */         }
/*      */         
/* 1910 */         return _handleInvalidNumberStart(this._inputBuffer[this._inputPtr++], false);
/*      */     } 
/*      */     
/* 1913 */     if (Character.isJavaIdentifierStart(i)) {
/* 1914 */       _reportInvalidToken("" + (char)i, _validJsonTokenList());
/*      */     }
/*      */     
/* 1917 */     _reportUnexpectedChar(i, "expected a valid value " + _validJsonValueList());
/* 1918 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected JsonToken _handleApos() throws IOException {
/* 1923 */     char[] outBuf = this._textBuffer.emptyAndGetCurrentSegment();
/* 1924 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/*      */     
/*      */     while (true) {
/* 1927 */       if (this._inputPtr >= this._inputEnd && 
/* 1928 */         !_loadMore()) {
/* 1929 */         _reportInvalidEOF(": was expecting closing quote for a string value", JsonToken.VALUE_STRING);
/*      */       }
/*      */ 
/*      */       
/* 1933 */       char c = this._inputBuffer[this._inputPtr++];
/* 1934 */       int i = c;
/* 1935 */       if (i <= 92) {
/* 1936 */         if (i == 92) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1941 */           c = _decodeEscaped();
/* 1942 */         } else if (i <= 39) {
/* 1943 */           if (i == 39) {
/*      */             break;
/*      */           }
/* 1946 */           if (i < 32) {
/* 1947 */             _throwUnquotedSpace(i, "string value");
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/* 1952 */       if (outPtr >= outBuf.length) {
/* 1953 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1954 */         outPtr = 0;
/*      */       } 
/*      */       
/* 1957 */       outBuf[outPtr++] = c;
/*      */     } 
/* 1959 */     this._textBuffer.setCurrentLength(outPtr);
/* 1960 */     return JsonToken.VALUE_STRING;
/*      */   }
/*      */ 
/*      */   
/*      */   private String _handleOddName2(int startPtr, int hash, int[] codes) throws IOException {
/* 1965 */     this._textBuffer.resetWithShared(this._inputBuffer, startPtr, this._inputPtr - startPtr);
/* 1966 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/* 1967 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/* 1968 */     int maxCode = codes.length;
/*      */ 
/*      */     
/* 1971 */     while (this._inputPtr < this._inputEnd || 
/* 1972 */       _loadMore()) {
/*      */ 
/*      */ 
/*      */       
/* 1976 */       char c = this._inputBuffer[this._inputPtr];
/* 1977 */       int i = c;
/* 1978 */       if ((i < maxCode) ? (
/* 1979 */         codes[i] != 0) : 
/*      */ 
/*      */         
/* 1982 */         !Character.isJavaIdentifierPart(c)) {
/*      */         break;
/*      */       }
/* 1985 */       this._inputPtr++;
/* 1986 */       hash = hash * 33 + i;
/*      */       
/* 1988 */       outBuf[outPtr++] = c;
/*      */ 
/*      */       
/* 1991 */       if (outPtr >= outBuf.length) {
/* 1992 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 1993 */         outPtr = 0;
/*      */       } 
/*      */     } 
/* 1996 */     this._textBuffer.setCurrentLength(outPtr);
/*      */     
/* 1998 */     TextBuffer tb = this._textBuffer;
/* 1999 */     char[] buf = tb.getTextBuffer();
/* 2000 */     int start = tb.getTextOffset();
/* 2001 */     int len = tb.size();
/*      */     
/* 2003 */     return this._symbols.findSymbol(buf, start, len, hash);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _finishString() throws IOException {
/* 2014 */     int ptr = this._inputPtr;
/* 2015 */     int inputLen = this._inputEnd;
/*      */     
/* 2017 */     if (ptr < inputLen) {
/* 2018 */       int[] codes = _icLatin1;
/* 2019 */       int maxCode = codes.length;
/*      */       
/*      */       do {
/* 2022 */         int ch = this._inputBuffer[ptr];
/* 2023 */         if (ch < maxCode && codes[ch] != 0) {
/* 2024 */           if (ch == 34) {
/* 2025 */             this._textBuffer.resetWithShared(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
/* 2026 */             this._inputPtr = ptr + 1;
/*      */             
/*      */             return;
/*      */           } 
/*      */           break;
/*      */         } 
/* 2032 */         ++ptr;
/* 2033 */       } while (ptr < inputLen);
/*      */     } 
/*      */ 
/*      */     
/* 2037 */     this._textBuffer.resetWithCopy(this._inputBuffer, this._inputPtr, ptr - this._inputPtr);
/* 2038 */     this._inputPtr = ptr;
/* 2039 */     _finishString2();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _finishString2() throws IOException {
/* 2044 */     char[] outBuf = this._textBuffer.getCurrentSegment();
/* 2045 */     int outPtr = this._textBuffer.getCurrentSegmentSize();
/* 2046 */     int[] codes = _icLatin1;
/* 2047 */     int maxCode = codes.length;
/*      */     
/*      */     while (true) {
/* 2050 */       if (this._inputPtr >= this._inputEnd && 
/* 2051 */         !_loadMore()) {
/* 2052 */         _reportInvalidEOF(": was expecting closing quote for a string value", JsonToken.VALUE_STRING);
/*      */       }
/*      */ 
/*      */       
/* 2056 */       char c = this._inputBuffer[this._inputPtr++];
/* 2057 */       int i = c;
/* 2058 */       if (i < maxCode && codes[i] != 0) {
/* 2059 */         if (i == 34)
/*      */           break; 
/* 2061 */         if (i == 92) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2066 */           c = _decodeEscaped();
/* 2067 */         } else if (i < 32) {
/* 2068 */           _throwUnquotedSpace(i, "string value");
/*      */         } 
/*      */       } 
/*      */       
/* 2072 */       if (outPtr >= outBuf.length) {
/* 2073 */         outBuf = this._textBuffer.finishCurrentSegment();
/* 2074 */         outPtr = 0;
/*      */       } 
/*      */       
/* 2077 */       outBuf[outPtr++] = c;
/*      */     } 
/* 2079 */     this._textBuffer.setCurrentLength(outPtr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _skipString() throws IOException {
/* 2089 */     this._tokenIncomplete = false;
/*      */     
/* 2091 */     int inPtr = this._inputPtr;
/* 2092 */     int inLen = this._inputEnd;
/* 2093 */     char[] inBuf = this._inputBuffer;
/*      */     
/*      */     while (true) {
/* 2096 */       if (inPtr >= inLen) {
/* 2097 */         this._inputPtr = inPtr;
/* 2098 */         if (!_loadMore()) {
/* 2099 */           _reportInvalidEOF(": was expecting closing quote for a string value", JsonToken.VALUE_STRING);
/*      */         }
/*      */         
/* 2102 */         inPtr = this._inputPtr;
/* 2103 */         inLen = this._inputEnd;
/*      */       } 
/* 2105 */       char c = inBuf[inPtr++];
/* 2106 */       int i = c;
/* 2107 */       if (i <= 92) {
/* 2108 */         if (i == 92) {
/*      */ 
/*      */           
/* 2111 */           this._inputPtr = inPtr;
/* 2112 */           _decodeEscaped();
/* 2113 */           inPtr = this._inputPtr;
/* 2114 */           inLen = this._inputEnd; continue;
/* 2115 */         }  if (i <= 34) {
/* 2116 */           if (i == 34) {
/* 2117 */             this._inputPtr = inPtr;
/*      */             break;
/*      */           } 
/* 2120 */           if (i < 32) {
/* 2121 */             this._inputPtr = inPtr;
/* 2122 */             _throwUnquotedSpace(i, "string value");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _skipCR() throws IOException {
/* 2140 */     if ((this._inputPtr < this._inputEnd || _loadMore()) && 
/* 2141 */       this._inputBuffer[this._inputPtr] == '\n') {
/* 2142 */       this._inputPtr++;
/*      */     }
/*      */     
/* 2145 */     this._currInputRow++;
/* 2146 */     this._currInputRowStart = this._inputPtr;
/*      */   }
/*      */ 
/*      */   
/*      */   private final int _skipColon() throws IOException {
/* 2151 */     if (this._inputPtr + 4 >= this._inputEnd) {
/* 2152 */       return _skipColon2(false);
/*      */     }
/* 2154 */     char c = this._inputBuffer[this._inputPtr];
/* 2155 */     if (c == ':') {
/* 2156 */       int i = this._inputBuffer[++this._inputPtr];
/* 2157 */       if (i > 32) {
/* 2158 */         if (i == 47 || i == 35) {
/* 2159 */           return _skipColon2(true);
/*      */         }
/* 2161 */         this._inputPtr++;
/* 2162 */         return i;
/*      */       } 
/* 2164 */       if (i == 32 || i == 9) {
/* 2165 */         i = this._inputBuffer[++this._inputPtr];
/* 2166 */         if (i > 32) {
/* 2167 */           if (i == 47 || i == 35) {
/* 2168 */             return _skipColon2(true);
/*      */           }
/* 2170 */           this._inputPtr++;
/* 2171 */           return i;
/*      */         } 
/*      */       } 
/* 2174 */       return _skipColon2(true);
/*      */     } 
/* 2176 */     if (c == ' ' || c == '\t') {
/* 2177 */       c = this._inputBuffer[++this._inputPtr];
/*      */     }
/* 2179 */     if (c == ':') {
/* 2180 */       int i = this._inputBuffer[++this._inputPtr];
/* 2181 */       if (i > 32) {
/* 2182 */         if (i == 47 || i == 35) {
/* 2183 */           return _skipColon2(true);
/*      */         }
/* 2185 */         this._inputPtr++;
/* 2186 */         return i;
/*      */       } 
/* 2188 */       if (i == 32 || i == 9) {
/* 2189 */         i = this._inputBuffer[++this._inputPtr];
/* 2190 */         if (i > 32) {
/* 2191 */           if (i == 47 || i == 35) {
/* 2192 */             return _skipColon2(true);
/*      */           }
/* 2194 */           this._inputPtr++;
/* 2195 */           return i;
/*      */         } 
/*      */       } 
/* 2198 */       return _skipColon2(true);
/*      */     } 
/* 2200 */     return _skipColon2(false);
/*      */   }
/*      */ 
/*      */   
/*      */   private final int _skipColon2(boolean gotColon) throws IOException {
/* 2205 */     while (this._inputPtr < this._inputEnd || _loadMore()) {
/* 2206 */       int i = this._inputBuffer[this._inputPtr++];
/* 2207 */       if (i > 32) {
/* 2208 */         if (i == 47) {
/* 2209 */           _skipComment();
/*      */           continue;
/*      */         } 
/* 2212 */         if (i == 35 && 
/* 2213 */           _skipYAMLComment()) {
/*      */           continue;
/*      */         }
/*      */         
/* 2217 */         if (gotColon) {
/* 2218 */           return i;
/*      */         }
/* 2220 */         if (i != 58) {
/* 2221 */           _reportUnexpectedChar(i, "was expecting a colon to separate field name and value");
/*      */         }
/* 2223 */         gotColon = true;
/*      */         continue;
/*      */       } 
/* 2226 */       if (i < 32) {
/* 2227 */         if (i == 10) {
/* 2228 */           this._currInputRow++;
/* 2229 */           this._currInputRowStart = this._inputPtr; continue;
/* 2230 */         }  if (i == 13) {
/* 2231 */           _skipCR(); continue;
/* 2232 */         }  if (i != 9) {
/* 2233 */           _throwInvalidSpace(i);
/*      */         }
/*      */       } 
/*      */     } 
/* 2237 */     _reportInvalidEOF(" within/between " + this._parsingContext.typeDesc() + " entries", null);
/*      */     
/* 2239 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final int _skipColonFast(int ptr) throws IOException {
/* 2245 */     int i = this._inputBuffer[ptr++];
/* 2246 */     if (i == 58) {
/* 2247 */       i = this._inputBuffer[ptr++];
/* 2248 */       if (i > 32) {
/* 2249 */         if (i != 47 && i != 35) {
/* 2250 */           this._inputPtr = ptr;
/* 2251 */           return i;
/*      */         } 
/* 2253 */       } else if (i == 32 || i == 9) {
/* 2254 */         i = this._inputBuffer[ptr++];
/* 2255 */         if (i > 32 && 
/* 2256 */           i != 47 && i != 35) {
/* 2257 */           this._inputPtr = ptr;
/* 2258 */           return i;
/*      */         } 
/*      */       } 
/*      */       
/* 2262 */       this._inputPtr = ptr - 1;
/* 2263 */       return _skipColon2(true);
/*      */     } 
/* 2265 */     if (i == 32 || i == 9) {
/* 2266 */       i = this._inputBuffer[ptr++];
/*      */     }
/* 2268 */     boolean gotColon = (i == 58);
/* 2269 */     if (gotColon) {
/* 2270 */       i = this._inputBuffer[ptr++];
/* 2271 */       if (i > 32) {
/* 2272 */         if (i != 47 && i != 35) {
/* 2273 */           this._inputPtr = ptr;
/* 2274 */           return i;
/*      */         } 
/* 2276 */       } else if (i == 32 || i == 9) {
/* 2277 */         i = this._inputBuffer[ptr++];
/* 2278 */         if (i > 32 && 
/* 2279 */           i != 47 && i != 35) {
/* 2280 */           this._inputPtr = ptr;
/* 2281 */           return i;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 2286 */     this._inputPtr = ptr - 1;
/* 2287 */     return _skipColon2(gotColon);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final int _skipComma(int i) throws IOException {
/* 2293 */     if (i != 44) {
/* 2294 */       _reportUnexpectedChar(i, "was expecting comma to separate " + this._parsingContext.typeDesc() + " entries");
/*      */     }
/* 2296 */     while (this._inputPtr < this._inputEnd) {
/* 2297 */       i = this._inputBuffer[this._inputPtr++];
/* 2298 */       if (i > 32) {
/* 2299 */         if (i == 47 || i == 35) {
/* 2300 */           this._inputPtr--;
/* 2301 */           return _skipAfterComma2();
/*      */         } 
/* 2303 */         return i;
/*      */       } 
/* 2305 */       if (i < 32) {
/* 2306 */         if (i == 10) {
/* 2307 */           this._currInputRow++;
/* 2308 */           this._currInputRowStart = this._inputPtr; continue;
/* 2309 */         }  if (i == 13) {
/* 2310 */           _skipCR(); continue;
/* 2311 */         }  if (i != 9) {
/* 2312 */           _throwInvalidSpace(i);
/*      */         }
/*      */       } 
/*      */     } 
/* 2316 */     return _skipAfterComma2();
/*      */   }
/*      */ 
/*      */   
/*      */   private final int _skipAfterComma2() throws IOException {
/* 2321 */     while (this._inputPtr < this._inputEnd || _loadMore()) {
/* 2322 */       int i = this._inputBuffer[this._inputPtr++];
/* 2323 */       if (i > 32) {
/* 2324 */         if (i == 47) {
/* 2325 */           _skipComment();
/*      */           continue;
/*      */         } 
/* 2328 */         if (i == 35 && 
/* 2329 */           _skipYAMLComment()) {
/*      */           continue;
/*      */         }
/*      */         
/* 2333 */         return i;
/*      */       } 
/* 2335 */       if (i < 32) {
/* 2336 */         if (i == 10) {
/* 2337 */           this._currInputRow++;
/* 2338 */           this._currInputRowStart = this._inputPtr; continue;
/* 2339 */         }  if (i == 13) {
/* 2340 */           _skipCR(); continue;
/* 2341 */         }  if (i != 9) {
/* 2342 */           _throwInvalidSpace(i);
/*      */         }
/*      */       } 
/*      */     } 
/* 2346 */     throw _constructError("Unexpected end-of-input within/between " + this._parsingContext.typeDesc() + " entries");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final int _skipWSOrEnd() throws IOException {
/* 2353 */     if (this._inputPtr >= this._inputEnd && 
/* 2354 */       !_loadMore()) {
/* 2355 */       return _eofAsNextChar();
/*      */     }
/*      */     
/* 2358 */     int i = this._inputBuffer[this._inputPtr++];
/* 2359 */     if (i > 32) {
/* 2360 */       if (i == 47 || i == 35) {
/* 2361 */         this._inputPtr--;
/* 2362 */         return _skipWSOrEnd2();
/*      */       } 
/* 2364 */       return i;
/*      */     } 
/* 2366 */     if (i != 32) {
/* 2367 */       if (i == 10) {
/* 2368 */         this._currInputRow++;
/* 2369 */         this._currInputRowStart = this._inputPtr;
/* 2370 */       } else if (i == 13) {
/* 2371 */         _skipCR();
/* 2372 */       } else if (i != 9) {
/* 2373 */         _throwInvalidSpace(i);
/*      */       } 
/*      */     }
/*      */     
/* 2377 */     while (this._inputPtr < this._inputEnd) {
/* 2378 */       i = this._inputBuffer[this._inputPtr++];
/* 2379 */       if (i > 32) {
/* 2380 */         if (i == 47 || i == 35) {
/* 2381 */           this._inputPtr--;
/* 2382 */           return _skipWSOrEnd2();
/*      */         } 
/* 2384 */         return i;
/*      */       } 
/* 2386 */       if (i != 32) {
/* 2387 */         if (i == 10) {
/* 2388 */           this._currInputRow++;
/* 2389 */           this._currInputRowStart = this._inputPtr; continue;
/* 2390 */         }  if (i == 13) {
/* 2391 */           _skipCR(); continue;
/* 2392 */         }  if (i != 9) {
/* 2393 */           _throwInvalidSpace(i);
/*      */         }
/*      */       } 
/*      */     } 
/* 2397 */     return _skipWSOrEnd2();
/*      */   }
/*      */ 
/*      */   
/*      */   private int _skipWSOrEnd2() throws IOException {
/*      */     while (true) {
/* 2403 */       if (this._inputPtr >= this._inputEnd && 
/* 2404 */         !_loadMore()) {
/* 2405 */         return _eofAsNextChar();
/*      */       }
/*      */       
/* 2408 */       int i = this._inputBuffer[this._inputPtr++];
/* 2409 */       if (i > 32) {
/* 2410 */         if (i == 47) {
/* 2411 */           _skipComment();
/*      */           continue;
/*      */         } 
/* 2414 */         if (i == 35 && 
/* 2415 */           _skipYAMLComment()) {
/*      */           continue;
/*      */         }
/*      */         
/* 2419 */         return i;
/* 2420 */       }  if (i != 32) {
/* 2421 */         if (i == 10) {
/* 2422 */           this._currInputRow++;
/* 2423 */           this._currInputRowStart = this._inputPtr; continue;
/* 2424 */         }  if (i == 13) {
/* 2425 */           _skipCR(); continue;
/* 2426 */         }  if (i != 9) {
/* 2427 */           _throwInvalidSpace(i);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void _skipComment() throws IOException {
/* 2435 */     if ((this._features & FEAT_MASK_ALLOW_JAVA_COMMENTS) == 0) {
/* 2436 */       _reportUnexpectedChar(47, "maybe a (non-standard) comment? (not recognized as one since Feature 'ALLOW_COMMENTS' not enabled for parser)");
/*      */     }
/*      */     
/* 2439 */     if (this._inputPtr >= this._inputEnd && !_loadMore()) {
/* 2440 */       _reportInvalidEOF(" in a comment", null);
/*      */     }
/* 2442 */     char c = this._inputBuffer[this._inputPtr++];
/* 2443 */     if (c == '/') {
/* 2444 */       _skipLine();
/* 2445 */     } else if (c == '*') {
/* 2446 */       _skipCComment();
/*      */     } else {
/* 2448 */       _reportUnexpectedChar(c, "was expecting either '*' or '/' for a comment");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void _skipCComment() throws IOException {
/* 2455 */     while (this._inputPtr < this._inputEnd || _loadMore()) {
/* 2456 */       int i = this._inputBuffer[this._inputPtr++];
/* 2457 */       if (i <= 42) {
/* 2458 */         if (i == 42) {
/* 2459 */           if (this._inputPtr >= this._inputEnd && !_loadMore()) {
/*      */             break;
/*      */           }
/* 2462 */           if (this._inputBuffer[this._inputPtr] == '/') {
/* 2463 */             this._inputPtr++;
/*      */             return;
/*      */           } 
/*      */           continue;
/*      */         } 
/* 2468 */         if (i < 32) {
/* 2469 */           if (i == 10) {
/* 2470 */             this._currInputRow++;
/* 2471 */             this._currInputRowStart = this._inputPtr; continue;
/* 2472 */           }  if (i == 13) {
/* 2473 */             _skipCR(); continue;
/* 2474 */           }  if (i != 9) {
/* 2475 */             _throwInvalidSpace(i);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 2480 */     _reportInvalidEOF(" in a comment", null);
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean _skipYAMLComment() throws IOException {
/* 2485 */     if ((this._features & FEAT_MASK_ALLOW_YAML_COMMENTS) == 0) {
/* 2486 */       return false;
/*      */     }
/* 2488 */     _skipLine();
/* 2489 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void _skipLine() throws IOException {
/* 2495 */     while (this._inputPtr < this._inputEnd || _loadMore()) {
/* 2496 */       int i = this._inputBuffer[this._inputPtr++];
/* 2497 */       if (i < 32) {
/* 2498 */         if (i == 10) {
/* 2499 */           this._currInputRow++;
/* 2500 */           this._currInputRowStart = this._inputPtr; break;
/*      */         } 
/* 2502 */         if (i == 13) {
/* 2503 */           _skipCR(); break;
/*      */         } 
/* 2505 */         if (i != 9) {
/* 2506 */           _throwInvalidSpace(i);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected char _decodeEscaped() throws IOException {
/* 2515 */     if (this._inputPtr >= this._inputEnd && 
/* 2516 */       !_loadMore()) {
/* 2517 */       _reportInvalidEOF(" in character escape sequence", JsonToken.VALUE_STRING);
/*      */     }
/*      */     
/* 2520 */     char c = this._inputBuffer[this._inputPtr++];
/*      */     
/* 2522 */     switch (c) {
/*      */       
/*      */       case 'b':
/* 2525 */         return '\b';
/*      */       case 't':
/* 2527 */         return '\t';
/*      */       case 'n':
/* 2529 */         return '\n';
/*      */       case 'f':
/* 2531 */         return '\f';
/*      */       case 'r':
/* 2533 */         return '\r';
/*      */ 
/*      */       
/*      */       case '"':
/*      */       case '/':
/*      */       case '\\':
/* 2539 */         return c;
/*      */       
/*      */       case 'u':
/*      */         break;
/*      */       
/*      */       default:
/* 2545 */         return _handleUnrecognizedCharacterEscape(c);
/*      */     } 
/*      */ 
/*      */     
/* 2549 */     int value = 0;
/* 2550 */     for (int i = 0; i < 4; i++) {
/* 2551 */       if (this._inputPtr >= this._inputEnd && 
/* 2552 */         !_loadMore()) {
/* 2553 */         _reportInvalidEOF(" in character escape sequence", JsonToken.VALUE_STRING);
/*      */       }
/*      */       
/* 2556 */       int ch = this._inputBuffer[this._inputPtr++];
/* 2557 */       int digit = CharTypes.charToHex(ch);
/* 2558 */       if (digit < 0) {
/* 2559 */         _reportUnexpectedChar(ch, "expected a hex-digit for character escape sequence");
/*      */       }
/* 2561 */       value = value << 4 | digit;
/*      */     } 
/* 2563 */     return (char)value;
/*      */   }
/*      */   
/*      */   private final void _matchTrue() throws IOException {
/* 2567 */     int ptr = this._inputPtr;
/* 2568 */     if (ptr + 3 < this._inputEnd) {
/* 2569 */       char[] b = this._inputBuffer;
/* 2570 */       if (b[ptr] == 'r' && b[++ptr] == 'u' && b[++ptr] == 'e') {
/* 2571 */         char c = b[++ptr];
/* 2572 */         if (c < '0' || c == ']' || c == '}') {
/* 2573 */           this._inputPtr = ptr;
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/* 2579 */     _matchToken("true", 1);
/*      */   }
/*      */   
/*      */   private final void _matchFalse() throws IOException {
/* 2583 */     int ptr = this._inputPtr;
/* 2584 */     if (ptr + 4 < this._inputEnd) {
/* 2585 */       char[] b = this._inputBuffer;
/* 2586 */       if (b[ptr] == 'a' && b[++ptr] == 'l' && b[++ptr] == 's' && b[++ptr] == 'e') {
/* 2587 */         char c = b[++ptr];
/* 2588 */         if (c < '0' || c == ']' || c == '}') {
/* 2589 */           this._inputPtr = ptr;
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/* 2595 */     _matchToken("false", 1);
/*      */   }
/*      */   
/*      */   private final void _matchNull() throws IOException {
/* 2599 */     int ptr = this._inputPtr;
/* 2600 */     if (ptr + 3 < this._inputEnd) {
/* 2601 */       char[] b = this._inputBuffer;
/* 2602 */       if (b[ptr] == 'u' && b[++ptr] == 'l' && b[++ptr] == 'l') {
/* 2603 */         char c = b[++ptr];
/* 2604 */         if (c < '0' || c == ']' || c == '}') {
/* 2605 */           this._inputPtr = ptr;
/*      */           
/*      */           return;
/*      */         } 
/*      */       } 
/*      */     } 
/* 2611 */     _matchToken("null", 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _matchToken(String matchStr, int i) throws IOException {
/* 2619 */     int len = matchStr.length();
/* 2620 */     if (this._inputPtr + len >= this._inputEnd) {
/* 2621 */       _matchToken2(matchStr, i);
/*      */       
/*      */       return;
/*      */     } 
/*      */     while (true) {
/* 2626 */       if (this._inputBuffer[this._inputPtr] != matchStr.charAt(i)) {
/* 2627 */         _reportInvalidToken(matchStr.substring(0, i));
/*      */       }
/* 2629 */       this._inputPtr++;
/* 2630 */       if (++i >= len) {
/* 2631 */         int ch = this._inputBuffer[this._inputPtr];
/* 2632 */         if (ch >= 48 && ch != 93 && ch != 125)
/* 2633 */           _checkMatchEnd(matchStr, i, ch); 
/*      */         return;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   private final void _matchToken2(String matchStr, int i) throws IOException {
/* 2639 */     int len = matchStr.length();
/*      */     do {
/* 2641 */       if ((this._inputPtr >= this._inputEnd && !_loadMore()) || this._inputBuffer[this._inputPtr] != matchStr
/* 2642 */         .charAt(i)) {
/* 2643 */         _reportInvalidToken(matchStr.substring(0, i));
/*      */       }
/* 2645 */       this._inputPtr++;
/* 2646 */     } while (++i < len);
/*      */ 
/*      */     
/* 2649 */     if (this._inputPtr >= this._inputEnd && !_loadMore()) {
/*      */       return;
/*      */     }
/* 2652 */     int ch = this._inputBuffer[this._inputPtr];
/* 2653 */     if (ch >= 48 && ch != 93 && ch != 125) {
/* 2654 */       _checkMatchEnd(matchStr, i, ch);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private final void _checkMatchEnd(String matchStr, int i, int c) throws IOException {
/* 2660 */     char ch = (char)c;
/* 2661 */     if (Character.isJavaIdentifierPart(ch)) {
/* 2662 */       _reportInvalidToken(matchStr.substring(0, i));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected byte[] _decodeBase64(Base64Variant b64variant) throws IOException {
/* 2679 */     ByteArrayBuilder builder = _getByteArrayBuilder();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/* 2686 */       if (this._inputPtr >= this._inputEnd) {
/* 2687 */         _loadMoreGuaranteed();
/*      */       }
/* 2689 */       char ch = this._inputBuffer[this._inputPtr++];
/* 2690 */       if (ch > ' ') {
/* 2691 */         int bits = b64variant.decodeBase64Char(ch);
/* 2692 */         if (bits < 0) {
/* 2693 */           if (ch == '"') {
/* 2694 */             return builder.toByteArray();
/*      */           }
/* 2696 */           bits = _decodeBase64Escape(b64variant, ch, 0);
/* 2697 */           if (bits < 0) {
/*      */             continue;
/*      */           }
/*      */         } 
/* 2701 */         int decodedData = bits;
/*      */ 
/*      */ 
/*      */         
/* 2705 */         if (this._inputPtr >= this._inputEnd) {
/* 2706 */           _loadMoreGuaranteed();
/*      */         }
/* 2708 */         ch = this._inputBuffer[this._inputPtr++];
/* 2709 */         bits = b64variant.decodeBase64Char(ch);
/* 2710 */         if (bits < 0) {
/* 2711 */           bits = _decodeBase64Escape(b64variant, ch, 1);
/*      */         }
/* 2713 */         decodedData = decodedData << 6 | bits;
/*      */ 
/*      */         
/* 2716 */         if (this._inputPtr >= this._inputEnd) {
/* 2717 */           _loadMoreGuaranteed();
/*      */         }
/* 2719 */         ch = this._inputBuffer[this._inputPtr++];
/* 2720 */         bits = b64variant.decodeBase64Char(ch);
/*      */ 
/*      */         
/* 2723 */         if (bits < 0) {
/* 2724 */           if (bits != -2) {
/*      */             
/* 2726 */             if (ch == '"') {
/* 2727 */               decodedData >>= 4;
/* 2728 */               builder.append(decodedData);
/* 2729 */               if (b64variant.usesPadding()) {
/* 2730 */                 this._inputPtr--;
/* 2731 */                 _handleBase64MissingPadding(b64variant);
/*      */               } 
/* 2733 */               return builder.toByteArray();
/*      */             } 
/* 2735 */             bits = _decodeBase64Escape(b64variant, ch, 2);
/*      */           } 
/* 2737 */           if (bits == -2) {
/*      */             
/* 2739 */             if (this._inputPtr >= this._inputEnd) {
/* 2740 */               _loadMoreGuaranteed();
/*      */             }
/* 2742 */             ch = this._inputBuffer[this._inputPtr++];
/* 2743 */             if (!b64variant.usesPaddingChar(ch) && 
/* 2744 */               _decodeBase64Escape(b64variant, ch, 3) != -2) {
/* 2745 */               throw reportInvalidBase64Char(b64variant, ch, 3, "expected padding character '" + b64variant.getPaddingChar() + "'");
/*      */             }
/*      */ 
/*      */             
/* 2749 */             decodedData >>= 4;
/* 2750 */             builder.append(decodedData);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*      */         
/* 2756 */         decodedData = decodedData << 6 | bits;
/*      */         
/* 2758 */         if (this._inputPtr >= this._inputEnd) {
/* 2759 */           _loadMoreGuaranteed();
/*      */         }
/* 2761 */         ch = this._inputBuffer[this._inputPtr++];
/* 2762 */         bits = b64variant.decodeBase64Char(ch);
/* 2763 */         if (bits < 0) {
/* 2764 */           if (bits != -2) {
/*      */             
/* 2766 */             if (ch == '"') {
/* 2767 */               decodedData >>= 2;
/* 2768 */               builder.appendTwoBytes(decodedData);
/* 2769 */               if (b64variant.usesPadding()) {
/* 2770 */                 this._inputPtr--;
/* 2771 */                 _handleBase64MissingPadding(b64variant);
/*      */               } 
/* 2773 */               return builder.toByteArray();
/*      */             } 
/* 2775 */             bits = _decodeBase64Escape(b64variant, ch, 3);
/*      */           } 
/* 2777 */           if (bits == -2) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 2783 */             decodedData >>= 2;
/* 2784 */             builder.appendTwoBytes(decodedData);
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } 
/*      */         
/* 2790 */         decodedData = decodedData << 6 | bits;
/* 2791 */         builder.appendThreeBytes(decodedData);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonLocation getTokenLocation() {
/* 2804 */     if (this._currToken == JsonToken.FIELD_NAME) {
/* 2805 */       long total = this._currInputProcessed + this._nameStartOffset - 1L;
/* 2806 */       return new JsonLocation(_getSourceReference(), -1L, total, this._nameStartRow, this._nameStartCol);
/*      */     } 
/*      */     
/* 2809 */     return new JsonLocation(_getSourceReference(), -1L, this._tokenInputTotal - 1L, this._tokenInputRow, this._tokenInputCol);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonLocation getCurrentLocation() {
/* 2815 */     int col = this._inputPtr - this._currInputRowStart + 1;
/* 2816 */     return new JsonLocation(_getSourceReference(), -1L, this._currInputProcessed + this._inputPtr, this._currInputRow, col);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void _updateLocation() {
/* 2824 */     int ptr = this._inputPtr;
/* 2825 */     this._tokenInputTotal = this._currInputProcessed + ptr;
/* 2826 */     this._tokenInputRow = this._currInputRow;
/* 2827 */     this._tokenInputCol = ptr - this._currInputRowStart;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void _updateNameLocation() {
/* 2833 */     int ptr = this._inputPtr;
/* 2834 */     this._nameStartOffset = ptr;
/* 2835 */     this._nameStartRow = this._currInputRow;
/* 2836 */     this._nameStartCol = ptr - this._currInputRowStart;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _reportInvalidToken(String matchedPart) throws IOException {
/* 2846 */     _reportInvalidToken(matchedPart, _validJsonTokenList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _reportInvalidToken(String matchedPart, String msg) throws IOException {
/* 2855 */     StringBuilder sb = new StringBuilder(matchedPart);
/* 2856 */     while (this._inputPtr < this._inputEnd || _loadMore()) {
/* 2857 */       char c = this._inputBuffer[this._inputPtr];
/* 2858 */       if (!Character.isJavaIdentifierPart(c)) {
/*      */         break;
/*      */       }
/* 2861 */       this._inputPtr++;
/* 2862 */       sb.append(c);
/* 2863 */       if (sb.length() >= 256) {
/* 2864 */         sb.append("...");
/*      */         break;
/*      */       } 
/*      */     } 
/* 2868 */     _reportError("Unrecognized token '%s': was expecting %s", sb, msg);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void _closeScope(int i) throws JsonParseException {
/* 2878 */     if (i == 93) {
/* 2879 */       _updateLocation();
/* 2880 */       if (!this._parsingContext.inArray()) {
/* 2881 */         _reportMismatchedEndMarker(i, '}');
/*      */       }
/* 2883 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/* 2884 */       this._currToken = JsonToken.END_ARRAY;
/*      */     } 
/* 2886 */     if (i == 125) {
/* 2887 */       _updateLocation();
/* 2888 */       if (!this._parsingContext.inObject()) {
/* 2889 */         _reportMismatchedEndMarker(i, ']');
/*      */       }
/* 2891 */       this._parsingContext = this._parsingContext.clearAndGetParent();
/* 2892 */       this._currToken = JsonToken.END_OBJECT;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\core\json\ReaderBasedJsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */