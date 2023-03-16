/*      */ package com.fasterxml.jackson.databind.util;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonLocation;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonStreamContext;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.TreeNode;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.base.ParserMinimalBase;
/*      */ import com.fasterxml.jackson.core.json.JsonWriteContext;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*      */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ 
/*      */ public class TokenBuffer
/*      */   extends JsonGenerator
/*      */ {
/*   32 */   protected static final int DEFAULT_GENERATOR_FEATURES = JsonGenerator.Feature.collectDefaults();
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
/*      */   protected JsonStreamContext _parentContext;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int _generatorFeatures;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _closed;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _hasNativeTypeIds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _hasNativeObjectIds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _mayHaveNativeIds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _forceBigDecimal;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Segment _first;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Segment _last;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int _appendAt;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _typeId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _objectId;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _hasNativeId = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected JsonWriteContext _writeContext;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TokenBuffer(ObjectCodec codec, boolean hasNativeIds) {
/*  151 */     this._objectCodec = codec;
/*  152 */     this._generatorFeatures = DEFAULT_GENERATOR_FEATURES;
/*  153 */     this._writeContext = JsonWriteContext.createRootContext(null);
/*      */     
/*  155 */     this._first = this._last = new Segment();
/*  156 */     this._appendAt = 0;
/*  157 */     this._hasNativeTypeIds = hasNativeIds;
/*  158 */     this._hasNativeObjectIds = hasNativeIds;
/*      */     
/*  160 */     this._mayHaveNativeIds = this._hasNativeTypeIds | this._hasNativeObjectIds;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TokenBuffer(JsonParser p) {
/*  167 */     this(p, (DeserializationContext)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TokenBuffer(JsonParser p, DeserializationContext ctxt) {
/*  175 */     this._objectCodec = p.getCodec();
/*  176 */     this._parentContext = p.getParsingContext();
/*  177 */     this._generatorFeatures = DEFAULT_GENERATOR_FEATURES;
/*  178 */     this._writeContext = JsonWriteContext.createRootContext(null);
/*      */     
/*  180 */     this._first = this._last = new Segment();
/*  181 */     this._appendAt = 0;
/*  182 */     this._hasNativeTypeIds = p.canReadTypeId();
/*  183 */     this._hasNativeObjectIds = p.canReadObjectId();
/*  184 */     this._mayHaveNativeIds = this._hasNativeTypeIds | this._hasNativeObjectIds;
/*  185 */     this
/*  186 */       ._forceBigDecimal = (ctxt == null) ? false : ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);
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
/*      */   public static TokenBuffer asCopyOfValue(JsonParser p) throws IOException {
/*  200 */     TokenBuffer b = new TokenBuffer(p);
/*  201 */     b.copyCurrentStructure(p);
/*  202 */     return b;
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
/*      */   public TokenBuffer overrideParentContext(JsonStreamContext ctxt) {
/*  214 */     this._parentContext = ctxt;
/*  215 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TokenBuffer forceUseOfBigDecimal(boolean b) {
/*  222 */     this._forceBigDecimal = b;
/*  223 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Version version() {
/*  228 */     return PackageVersion.VERSION;
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
/*      */   public JsonParser asParser() {
/*  242 */     return asParser(this._objectCodec);
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
/*      */   public JsonParser asParserOnFirstToken() throws IOException {
/*  256 */     JsonParser p = asParser(this._objectCodec);
/*  257 */     p.nextToken();
/*  258 */     return p;
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
/*      */   public JsonParser asParser(ObjectCodec codec) {
/*  276 */     return (JsonParser)new Parser(this._first, codec, this._hasNativeTypeIds, this._hasNativeObjectIds, this._parentContext);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonParser asParser(JsonParser src) {
/*  285 */     Parser p = new Parser(this._first, src.getCodec(), this._hasNativeTypeIds, this._hasNativeObjectIds, this._parentContext);
/*  286 */     p.setLocation(src.getTokenLocation());
/*  287 */     return (JsonParser)p;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonToken firstToken() {
/*  298 */     return this._first.type(0);
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
/*      */   public TokenBuffer append(TokenBuffer other) throws IOException {
/*  318 */     if (!this._hasNativeTypeIds) {
/*  319 */       this._hasNativeTypeIds = other.canWriteTypeId();
/*      */     }
/*  321 */     if (!this._hasNativeObjectIds) {
/*  322 */       this._hasNativeObjectIds = other.canWriteObjectId();
/*      */     }
/*  324 */     this._mayHaveNativeIds = this._hasNativeTypeIds | this._hasNativeObjectIds;
/*      */     
/*  326 */     JsonParser p = other.asParser();
/*  327 */     while (p.nextToken() != null) {
/*  328 */       copyCurrentStructure(p);
/*      */     }
/*  330 */     return this;
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
/*      */   public void serialize(JsonGenerator gen) throws IOException {
/*  345 */     Segment segment = this._first;
/*  346 */     int ptr = -1;
/*      */     
/*  348 */     boolean checkIds = this._mayHaveNativeIds;
/*  349 */     boolean hasIds = (checkIds && segment.hasIds());
/*      */     while (true) {
/*      */       Object ob, n, value;
/*  352 */       if (++ptr >= 16) {
/*  353 */         ptr = 0;
/*  354 */         segment = segment.next();
/*  355 */         if (segment == null)
/*  356 */           break;  hasIds = (checkIds && segment.hasIds());
/*      */       } 
/*  358 */       JsonToken t = segment.type(ptr);
/*  359 */       if (t == null)
/*      */         break; 
/*  361 */       if (hasIds) {
/*  362 */         Object id = segment.findObjectId(ptr);
/*  363 */         if (id != null) {
/*  364 */           gen.writeObjectId(id);
/*      */         }
/*  366 */         id = segment.findTypeId(ptr);
/*  367 */         if (id != null) {
/*  368 */           gen.writeTypeId(id);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/*  373 */       switch (t) {
/*      */         case INT:
/*  375 */           gen.writeStartObject();
/*      */           continue;
/*      */         case BIG_INTEGER:
/*  378 */           gen.writeEndObject();
/*      */           continue;
/*      */         case BIG_DECIMAL:
/*  381 */           gen.writeStartArray();
/*      */           continue;
/*      */         case FLOAT:
/*  384 */           gen.writeEndArray();
/*      */           continue;
/*      */ 
/*      */         
/*      */         case LONG:
/*  389 */           ob = segment.get(ptr);
/*  390 */           if (ob instanceof SerializableString) {
/*  391 */             gen.writeFieldName((SerializableString)ob); continue;
/*      */           } 
/*  393 */           gen.writeFieldName((String)ob);
/*      */           continue;
/*      */ 
/*      */ 
/*      */         
/*      */         case null:
/*  399 */           ob = segment.get(ptr);
/*  400 */           if (ob instanceof SerializableString) {
/*  401 */             gen.writeString((SerializableString)ob); continue;
/*      */           } 
/*  403 */           gen.writeString((String)ob);
/*      */           continue;
/*      */ 
/*      */ 
/*      */         
/*      */         case null:
/*  409 */           n = segment.get(ptr);
/*  410 */           if (n instanceof Integer) {
/*  411 */             gen.writeNumber(((Integer)n).intValue()); continue;
/*  412 */           }  if (n instanceof BigInteger) {
/*  413 */             gen.writeNumber((BigInteger)n); continue;
/*  414 */           }  if (n instanceof Long) {
/*  415 */             gen.writeNumber(((Long)n).longValue()); continue;
/*  416 */           }  if (n instanceof Short) {
/*  417 */             gen.writeNumber(((Short)n).shortValue()); continue;
/*      */           } 
/*  419 */           gen.writeNumber(((Number)n).intValue());
/*      */           continue;
/*      */ 
/*      */ 
/*      */         
/*      */         case null:
/*  425 */           n = segment.get(ptr);
/*  426 */           if (n instanceof Double) {
/*  427 */             gen.writeNumber(((Double)n).doubleValue()); continue;
/*  428 */           }  if (n instanceof BigDecimal) {
/*  429 */             gen.writeNumber((BigDecimal)n); continue;
/*  430 */           }  if (n instanceof Float) {
/*  431 */             gen.writeNumber(((Float)n).floatValue()); continue;
/*  432 */           }  if (n == null) {
/*  433 */             gen.writeNull(); continue;
/*  434 */           }  if (n instanceof String) {
/*  435 */             gen.writeNumber((String)n); continue;
/*      */           } 
/*  437 */           throw new JsonGenerationException(String.format("Unrecognized value type for VALUE_NUMBER_FLOAT: %s, cannot serialize", new Object[] { n
/*      */                   
/*  439 */                   .getClass().getName() }), gen);
/*      */ 
/*      */ 
/*      */         
/*      */         case null:
/*  444 */           gen.writeBoolean(true);
/*      */           continue;
/*      */         case null:
/*  447 */           gen.writeBoolean(false);
/*      */           continue;
/*      */         case null:
/*  450 */           gen.writeNull();
/*      */           continue;
/*      */         
/*      */         case null:
/*  454 */           value = segment.get(ptr);
/*      */ 
/*      */ 
/*      */           
/*  458 */           if (value instanceof RawValue) {
/*  459 */             ((RawValue)value).serialize(gen); continue;
/*  460 */           }  if (value instanceof com.fasterxml.jackson.databind.JsonSerializable) {
/*  461 */             gen.writeObject(value); continue;
/*      */           } 
/*  463 */           gen.writeEmbeddedObject(value);
/*      */           continue;
/*      */       } 
/*      */ 
/*      */       
/*  468 */       throw new RuntimeException("Internal error: should never end up through this code path");
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
/*      */   public TokenBuffer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  480 */     if (!p.hasToken(JsonToken.FIELD_NAME)) {
/*  481 */       copyCurrentStructure(p);
/*  482 */       return this;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  489 */     writeStartObject(); JsonToken t;
/*      */     do {
/*  491 */       copyCurrentStructure(p);
/*  492 */     } while ((t = p.nextToken()) == JsonToken.FIELD_NAME);
/*  493 */     if (t != JsonToken.END_OBJECT) {
/*  494 */       ctxt.reportWrongTokenException(TokenBuffer.class, JsonToken.END_OBJECT, "Expected END_OBJECT after copying contents of a JsonParser into TokenBuffer, got " + t, new Object[0]);
/*      */     }
/*      */ 
/*      */     
/*  498 */     writeEndObject();
/*  499 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  507 */     int MAX_COUNT = 100;
/*      */     
/*  509 */     StringBuilder sb = new StringBuilder();
/*  510 */     sb.append("[TokenBuffer: ");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  517 */     JsonParser jp = asParser();
/*  518 */     int count = 0;
/*  519 */     boolean hasNativeIds = (this._hasNativeTypeIds || this._hasNativeObjectIds);
/*      */ 
/*      */     
/*      */     while (true) {
/*      */       try {
/*  524 */         JsonToken t = jp.nextToken();
/*  525 */         if (t == null)
/*      */           break; 
/*  527 */         if (hasNativeIds) {
/*  528 */           _appendNativeIds(sb);
/*      */         }
/*      */         
/*  531 */         if (count < 100) {
/*  532 */           if (count > 0) {
/*  533 */             sb.append(", ");
/*      */           }
/*  535 */           sb.append(t.toString());
/*  536 */           if (t == JsonToken.FIELD_NAME) {
/*  537 */             sb.append('(');
/*  538 */             sb.append(jp.getCurrentName());
/*  539 */             sb.append(')');
/*      */           } 
/*      */         } 
/*  542 */       } catch (IOException ioe) {
/*  543 */         throw new IllegalStateException(ioe);
/*      */       } 
/*  545 */       count++;
/*      */     } 
/*      */     
/*  548 */     if (count >= 100) {
/*  549 */       sb.append(" ... (truncated ").append(count - 100).append(" entries)");
/*      */     }
/*  551 */     sb.append(']');
/*  552 */     return sb.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private final void _appendNativeIds(StringBuilder sb) {
/*  557 */     Object objectId = this._last.findObjectId(this._appendAt - 1);
/*  558 */     if (objectId != null) {
/*  559 */       sb.append("[objectId=").append(String.valueOf(objectId)).append(']');
/*      */     }
/*  561 */     Object typeId = this._last.findTypeId(this._appendAt - 1);
/*  562 */     if (typeId != null) {
/*  563 */       sb.append("[typeId=").append(String.valueOf(typeId)).append(']');
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
/*      */   public JsonGenerator enable(JsonGenerator.Feature f) {
/*  575 */     this._generatorFeatures |= f.getMask();
/*  576 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonGenerator disable(JsonGenerator.Feature f) {
/*  581 */     this._generatorFeatures &= f.getMask() ^ 0xFFFFFFFF;
/*  582 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEnabled(JsonGenerator.Feature f) {
/*  589 */     return ((this._generatorFeatures & f.getMask()) != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getFeatureMask() {
/*  594 */     return this._generatorFeatures;
/*      */   }
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public JsonGenerator setFeatureMask(int mask) {
/*  600 */     this._generatorFeatures = mask;
/*  601 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonGenerator overrideStdFeatures(int values, int mask) {
/*  606 */     int oldState = getFeatureMask();
/*  607 */     this._generatorFeatures = oldState & (mask ^ 0xFFFFFFFF) | values & mask;
/*  608 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public JsonGenerator useDefaultPrettyPrinter() {
/*  614 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public JsonGenerator setCodec(ObjectCodec oc) {
/*  619 */     this._objectCodec = oc;
/*  620 */     return this;
/*      */   }
/*      */   
/*      */   public ObjectCodec getCodec() {
/*  624 */     return this._objectCodec;
/*      */   }
/*      */   public final JsonWriteContext getOutputContext() {
/*  627 */     return this._writeContext;
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
/*      */   public boolean canWriteBinaryNatively() {
/*  640 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void flush() throws IOException {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws IOException {
/*  654 */     this._closed = true;
/*      */   }
/*      */   
/*      */   public boolean isClosed() {
/*  658 */     return this._closed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeStartArray() throws IOException {
/*  669 */     this._writeContext.writeValue();
/*  670 */     _appendStartMarker(JsonToken.START_ARRAY);
/*  671 */     this._writeContext = this._writeContext.createChildArrayContext();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeStartArray(int size) throws IOException {
/*  677 */     this._writeContext.writeValue();
/*  678 */     _appendStartMarker(JsonToken.START_ARRAY);
/*  679 */     this._writeContext = this._writeContext.createChildArrayContext();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeStartArray(Object forValue) throws IOException {
/*  684 */     this._writeContext.writeValue();
/*  685 */     _appendStartMarker(JsonToken.START_ARRAY);
/*  686 */     this._writeContext = this._writeContext.createChildArrayContext();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeStartArray(Object forValue, int size) throws IOException {
/*  691 */     this._writeContext.writeValue();
/*  692 */     _appendStartMarker(JsonToken.START_ARRAY);
/*  693 */     this._writeContext = this._writeContext.createChildArrayContext(forValue);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeEndArray() throws IOException {
/*  699 */     _appendEndMarker(JsonToken.END_ARRAY);
/*      */     
/*  701 */     JsonWriteContext c = this._writeContext.getParent();
/*  702 */     if (c != null) {
/*  703 */       this._writeContext = c;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeStartObject() throws IOException {
/*  710 */     this._writeContext.writeValue();
/*  711 */     _appendStartMarker(JsonToken.START_OBJECT);
/*  712 */     this._writeContext = this._writeContext.createChildObjectContext();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeStartObject(Object forValue) throws IOException {
/*  718 */     this._writeContext.writeValue();
/*  719 */     _appendStartMarker(JsonToken.START_OBJECT);
/*  720 */     JsonWriteContext ctxt = this._writeContext.createChildObjectContext(forValue);
/*  721 */     this._writeContext = ctxt;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeStartObject(Object forValue, int size) throws IOException {
/*  727 */     this._writeContext.writeValue();
/*  728 */     _appendStartMarker(JsonToken.START_OBJECT);
/*  729 */     JsonWriteContext ctxt = this._writeContext.createChildObjectContext(forValue);
/*  730 */     this._writeContext = ctxt;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeEndObject() throws IOException {
/*  736 */     _appendEndMarker(JsonToken.END_OBJECT);
/*      */     
/*  738 */     JsonWriteContext c = this._writeContext.getParent();
/*  739 */     if (c != null) {
/*  740 */       this._writeContext = c;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public final void writeFieldName(String name) throws IOException {
/*  747 */     this._writeContext.writeFieldName(name);
/*  748 */     _appendFieldName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeFieldName(SerializableString name) throws IOException {
/*  754 */     this._writeContext.writeFieldName(name.getValue());
/*  755 */     _appendFieldName(name);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeString(String text) throws IOException {
/*  766 */     if (text == null) {
/*  767 */       writeNull();
/*      */     } else {
/*  769 */       _appendValue(JsonToken.VALUE_STRING, text);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeString(char[] text, int offset, int len) throws IOException {
/*  775 */     writeString(new String(text, offset, len));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeString(SerializableString text) throws IOException {
/*  780 */     if (text == null) {
/*  781 */       writeNull();
/*      */     } else {
/*  783 */       _appendValue(JsonToken.VALUE_STRING, text);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException {
/*  791 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
/*  798 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRaw(String text) throws IOException {
/*  803 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRaw(String text, int offset, int len) throws IOException {
/*  808 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRaw(SerializableString text) throws IOException {
/*  813 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRaw(char[] text, int offset, int len) throws IOException {
/*  818 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRaw(char c) throws IOException {
/*  823 */     _reportUnsupportedOperation();
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRawValue(String text) throws IOException {
/*  828 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new RawValue(text));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRawValue(String text, int offset, int len) throws IOException {
/*  833 */     if (offset > 0 || len != text.length()) {
/*  834 */       text = text.substring(offset, offset + len);
/*      */     }
/*  836 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new RawValue(text));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeRawValue(char[] text, int offset, int len) throws IOException {
/*  841 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, new String(text, offset, len));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeNumber(short i) throws IOException {
/*  852 */     _appendValue(JsonToken.VALUE_NUMBER_INT, Short.valueOf(i));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(int i) throws IOException {
/*  857 */     _appendValue(JsonToken.VALUE_NUMBER_INT, Integer.valueOf(i));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(long l) throws IOException {
/*  862 */     _appendValue(JsonToken.VALUE_NUMBER_INT, Long.valueOf(l));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(double d) throws IOException {
/*  867 */     _appendValue(JsonToken.VALUE_NUMBER_FLOAT, Double.valueOf(d));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(float f) throws IOException {
/*  872 */     _appendValue(JsonToken.VALUE_NUMBER_FLOAT, Float.valueOf(f));
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(BigDecimal dec) throws IOException {
/*  877 */     if (dec == null) {
/*  878 */       writeNull();
/*      */     } else {
/*  880 */       _appendValue(JsonToken.VALUE_NUMBER_FLOAT, dec);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNumber(BigInteger v) throws IOException {
/*  886 */     if (v == null) {
/*  887 */       writeNull();
/*      */     } else {
/*  889 */       _appendValue(JsonToken.VALUE_NUMBER_INT, v);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeNumber(String encodedValue) throws IOException {
/*  898 */     _appendValue(JsonToken.VALUE_NUMBER_FLOAT, encodedValue);
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeBoolean(boolean state) throws IOException {
/*  903 */     _appendValue(state ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE);
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeNull() throws IOException {
/*  908 */     _appendValue(JsonToken.VALUE_NULL);
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
/*      */   public void writeObject(Object value) throws IOException {
/*  920 */     if (value == null) {
/*  921 */       writeNull();
/*      */       return;
/*      */     } 
/*  924 */     Class<?> raw = value.getClass();
/*  925 */     if (raw == byte[].class || value instanceof RawValue) {
/*  926 */       _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, value);
/*      */       return;
/*      */     } 
/*  929 */     if (this._objectCodec == null) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  934 */       _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, value);
/*      */     } else {
/*  936 */       this._objectCodec.writeValue(this, value);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeTree(TreeNode node) throws IOException {
/*  943 */     if (node == null) {
/*  944 */       writeNull();
/*      */       
/*      */       return;
/*      */     } 
/*  948 */     if (this._objectCodec == null) {
/*      */       
/*  950 */       _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, node);
/*      */     } else {
/*  952 */       this._objectCodec.writeTree(this, node);
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
/*      */ 
/*      */   
/*      */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
/*  971 */     byte[] copy = new byte[len];
/*  972 */     System.arraycopy(data, offset, copy, 0, len);
/*  973 */     writeObject(copy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) {
/*  984 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canWriteTypeId() {
/*  995 */     return this._hasNativeTypeIds;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canWriteObjectId() {
/* 1000 */     return this._hasNativeObjectIds;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeTypeId(Object id) {
/* 1005 */     this._typeId = id;
/* 1006 */     this._hasNativeId = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeObjectId(Object id) {
/* 1011 */     this._objectId = id;
/* 1012 */     this._hasNativeId = true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeEmbeddedObject(Object object) throws IOException {
/* 1017 */     _appendValue(JsonToken.VALUE_EMBEDDED_OBJECT, object);
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
/*      */   public void copyCurrentEvent(JsonParser p) throws IOException {
/* 1029 */     if (this._mayHaveNativeIds) {
/* 1030 */       _checkNativeIds(p);
/*      */     }
/* 1032 */     switch (p.currentToken()) {
/*      */       case INT:
/* 1034 */         writeStartObject();
/*      */         return;
/*      */       case BIG_INTEGER:
/* 1037 */         writeEndObject();
/*      */         return;
/*      */       case BIG_DECIMAL:
/* 1040 */         writeStartArray();
/*      */         return;
/*      */       case FLOAT:
/* 1043 */         writeEndArray();
/*      */         return;
/*      */       case LONG:
/* 1046 */         writeFieldName(p.getCurrentName());
/*      */         return;
/*      */       case null:
/* 1049 */         if (p.hasTextCharacters()) {
/* 1050 */           writeString(p.getTextCharacters(), p.getTextOffset(), p.getTextLength());
/*      */         } else {
/* 1052 */           writeString(p.getText());
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1056 */         switch (p.getNumberType()) {
/*      */           case INT:
/* 1058 */             writeNumber(p.getIntValue());
/*      */             return;
/*      */           case BIG_INTEGER:
/* 1061 */             writeNumber(p.getBigIntegerValue());
/*      */             return;
/*      */         } 
/* 1064 */         writeNumber(p.getLongValue());
/*      */         return;
/*      */       
/*      */       case null:
/* 1068 */         if (this._forceBigDecimal) {
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1073 */           writeNumber(p.getDecimalValue());
/*      */         } else {
/* 1075 */           switch (p.getNumberType()) {
/*      */             case BIG_DECIMAL:
/* 1077 */               writeNumber(p.getDecimalValue());
/*      */               return;
/*      */             case FLOAT:
/* 1080 */               writeNumber(p.getFloatValue());
/*      */               return;
/*      */           } 
/* 1083 */           writeNumber(p.getDoubleValue());
/*      */         } 
/*      */         return;
/*      */       
/*      */       case null:
/* 1088 */         writeBoolean(true);
/*      */         return;
/*      */       case null:
/* 1091 */         writeBoolean(false);
/*      */         return;
/*      */       case null:
/* 1094 */         writeNull();
/*      */         return;
/*      */       case null:
/* 1097 */         writeObject(p.getEmbeddedObject());
/*      */         return;
/*      */     } 
/* 1100 */     throw new RuntimeException("Internal error: unexpected token: " + p.currentToken());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void copyCurrentStructure(JsonParser p) throws IOException {
/* 1107 */     JsonToken t = p.currentToken();
/*      */ 
/*      */     
/* 1110 */     if (t == JsonToken.FIELD_NAME) {
/* 1111 */       if (this._mayHaveNativeIds) {
/* 1112 */         _checkNativeIds(p);
/*      */       }
/* 1114 */       writeFieldName(p.getCurrentName());
/* 1115 */       t = p.nextToken();
/*      */     }
/* 1117 */     else if (t == null) {
/* 1118 */       throw new IllegalStateException("No token available from argument `JsonParser`");
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1124 */     switch (t) {
/*      */       case BIG_DECIMAL:
/* 1126 */         if (this._mayHaveNativeIds) {
/* 1127 */           _checkNativeIds(p);
/*      */         }
/* 1129 */         writeStartArray();
/* 1130 */         _copyBufferContents(p);
/*      */         return;
/*      */       case INT:
/* 1133 */         if (this._mayHaveNativeIds) {
/* 1134 */           _checkNativeIds(p);
/*      */         }
/* 1136 */         writeStartObject();
/* 1137 */         _copyBufferContents(p);
/*      */         return;
/*      */       case FLOAT:
/* 1140 */         writeEndArray();
/*      */         return;
/*      */       case BIG_INTEGER:
/* 1143 */         writeEndObject();
/*      */         return;
/*      */     } 
/* 1146 */     _copyBufferValue(p, t);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _copyBufferContents(JsonParser p) throws IOException {
/* 1152 */     int depth = 1;
/*      */     
/*      */     JsonToken t;
/* 1155 */     while ((t = p.nextToken()) != null) {
/* 1156 */       switch (t) {
/*      */         case LONG:
/* 1158 */           if (this._mayHaveNativeIds) {
/* 1159 */             _checkNativeIds(p);
/*      */           }
/* 1161 */           writeFieldName(p.getCurrentName());
/*      */           continue;
/*      */         
/*      */         case BIG_DECIMAL:
/* 1165 */           if (this._mayHaveNativeIds) {
/* 1166 */             _checkNativeIds(p);
/*      */           }
/* 1168 */           writeStartArray();
/* 1169 */           depth++;
/*      */           continue;
/*      */         
/*      */         case INT:
/* 1173 */           if (this._mayHaveNativeIds) {
/* 1174 */             _checkNativeIds(p);
/*      */           }
/* 1176 */           writeStartObject();
/* 1177 */           depth++;
/*      */           continue;
/*      */         
/*      */         case FLOAT:
/* 1181 */           writeEndArray();
/* 1182 */           if (--depth == 0) {
/*      */             return;
/*      */           }
/*      */           continue;
/*      */         case BIG_INTEGER:
/* 1187 */           writeEndObject();
/* 1188 */           if (--depth == 0) {
/*      */             return;
/*      */           }
/*      */           continue;
/*      */       } 
/*      */       
/* 1194 */       _copyBufferValue(p, t);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void _copyBufferValue(JsonParser p, JsonToken t) throws IOException {
/* 1202 */     if (this._mayHaveNativeIds) {
/* 1203 */       _checkNativeIds(p);
/*      */     }
/* 1205 */     switch (t) {
/*      */       case null:
/* 1207 */         if (p.hasTextCharacters()) {
/* 1208 */           writeString(p.getTextCharacters(), p.getTextOffset(), p.getTextLength());
/*      */         } else {
/* 1210 */           writeString(p.getText());
/*      */         } 
/*      */         return;
/*      */       case null:
/* 1214 */         switch (p.getNumberType()) {
/*      */           case INT:
/* 1216 */             writeNumber(p.getIntValue());
/*      */             return;
/*      */           case BIG_INTEGER:
/* 1219 */             writeNumber(p.getBigIntegerValue());
/*      */             return;
/*      */         } 
/* 1222 */         writeNumber(p.getLongValue());
/*      */         return;
/*      */       
/*      */       case null:
/* 1226 */         if (this._forceBigDecimal) {
/* 1227 */           writeNumber(p.getDecimalValue());
/*      */         } else {
/* 1229 */           switch (p.getNumberType()) {
/*      */             case BIG_DECIMAL:
/* 1231 */               writeNumber(p.getDecimalValue());
/*      */               return;
/*      */             case FLOAT:
/* 1234 */               writeNumber(p.getFloatValue());
/*      */               return;
/*      */           } 
/* 1237 */           writeNumber(p.getDoubleValue());
/*      */         } 
/*      */         return;
/*      */       
/*      */       case null:
/* 1242 */         writeBoolean(true);
/*      */         return;
/*      */       case null:
/* 1245 */         writeBoolean(false);
/*      */         return;
/*      */       case null:
/* 1248 */         writeNull();
/*      */         return;
/*      */       case null:
/* 1251 */         writeObject(p.getEmbeddedObject());
/*      */         return;
/*      */     } 
/* 1254 */     throw new RuntimeException("Internal error: unexpected token: " + t);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void _checkNativeIds(JsonParser p) throws IOException {
/* 1260 */     if ((this._typeId = p.getTypeId()) != null) {
/* 1261 */       this._hasNativeId = true;
/*      */     }
/* 1263 */     if ((this._objectId = p.getObjectId()) != null) {
/* 1264 */       this._hasNativeId = true;
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
/*      */   protected final void _appendValue(JsonToken type) {
/*      */     Segment next;
/* 1317 */     this._writeContext.writeValue();
/*      */     
/* 1319 */     if (this._hasNativeId) {
/* 1320 */       next = this._last.append(this._appendAt, type, this._objectId, this._typeId);
/*      */     } else {
/* 1322 */       next = this._last.append(this._appendAt, type);
/*      */     } 
/* 1324 */     if (next == null) {
/* 1325 */       this._appendAt++;
/*      */     } else {
/* 1327 */       this._last = next;
/* 1328 */       this._appendAt = 1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _appendValue(JsonToken type, Object value) {
/*      */     Segment next;
/* 1340 */     this._writeContext.writeValue();
/*      */     
/* 1342 */     if (this._hasNativeId) {
/* 1343 */       next = this._last.append(this._appendAt, type, value, this._objectId, this._typeId);
/*      */     } else {
/* 1345 */       next = this._last.append(this._appendAt, type, value);
/*      */     } 
/* 1347 */     if (next == null) {
/* 1348 */       this._appendAt++;
/*      */     } else {
/* 1350 */       this._last = next;
/* 1351 */       this._appendAt = 1;
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
/*      */   protected final void _appendFieldName(Object value) {
/*      */     Segment next;
/* 1365 */     if (this._hasNativeId) {
/* 1366 */       next = this._last.append(this._appendAt, JsonToken.FIELD_NAME, value, this._objectId, this._typeId);
/*      */     } else {
/* 1368 */       next = this._last.append(this._appendAt, JsonToken.FIELD_NAME, value);
/*      */     } 
/* 1370 */     if (next == null) {
/* 1371 */       this._appendAt++;
/*      */     } else {
/* 1373 */       this._last = next;
/* 1374 */       this._appendAt = 1;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _appendStartMarker(JsonToken type) {
/*      */     Segment next;
/* 1386 */     if (this._hasNativeId) {
/* 1387 */       next = this._last.append(this._appendAt, type, this._objectId, this._typeId);
/*      */     } else {
/* 1389 */       next = this._last.append(this._appendAt, type);
/*      */     } 
/* 1391 */     if (next == null) {
/* 1392 */       this._appendAt++;
/*      */     } else {
/* 1394 */       this._last = next;
/* 1395 */       this._appendAt = 1;
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
/*      */   protected final void _appendEndMarker(JsonToken type) {
/* 1407 */     Segment next = this._last.append(this._appendAt, type);
/* 1408 */     if (next == null) {
/* 1409 */       this._appendAt++;
/*      */     } else {
/* 1411 */       this._last = next;
/* 1412 */       this._appendAt = 1;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void _reportUnsupportedOperation() {
/* 1418 */     throw new UnsupportedOperationException("Called operation not supported for TokenBuffer");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final class Parser
/*      */     extends ParserMinimalBase
/*      */   {
/*      */     protected ObjectCodec _codec;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final boolean _hasNativeTypeIds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final boolean _hasNativeObjectIds;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final boolean _hasNativeIds;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected TokenBuffer.Segment _segment;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int _segmentPtr;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected TokenBufferReadContext _parsingContext;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean _closed;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected transient ByteArrayBuilder _byteBuilder;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1476 */     protected JsonLocation _location = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public Parser(TokenBuffer.Segment firstSeg, ObjectCodec codec, boolean hasNativeTypeIds, boolean hasNativeObjectIds) {
/* 1488 */       this(firstSeg, codec, hasNativeTypeIds, hasNativeObjectIds, (JsonStreamContext)null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Parser(TokenBuffer.Segment firstSeg, ObjectCodec codec, boolean hasNativeTypeIds, boolean hasNativeObjectIds, JsonStreamContext parentContext) {
/* 1495 */       super(0);
/* 1496 */       this._segment = firstSeg;
/* 1497 */       this._segmentPtr = -1;
/* 1498 */       this._codec = codec;
/* 1499 */       this._parsingContext = TokenBufferReadContext.createRootContext(parentContext);
/* 1500 */       this._hasNativeTypeIds = hasNativeTypeIds;
/* 1501 */       this._hasNativeObjectIds = hasNativeObjectIds;
/* 1502 */       this._hasNativeIds = hasNativeTypeIds | hasNativeObjectIds;
/*      */     }
/*      */     
/*      */     public void setLocation(JsonLocation l) {
/* 1506 */       this._location = l;
/*      */     }
/*      */     
/*      */     public ObjectCodec getCodec() {
/* 1510 */       return this._codec;
/*      */     }
/*      */     public void setCodec(ObjectCodec c) {
/* 1513 */       this._codec = c;
/*      */     }
/*      */     
/*      */     public Version version() {
/* 1517 */       return PackageVersion.VERSION;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public JsonToken peekNextToken() throws IOException {
/* 1529 */       if (this._closed) return null; 
/* 1530 */       TokenBuffer.Segment seg = this._segment;
/* 1531 */       int ptr = this._segmentPtr + 1;
/* 1532 */       if (ptr >= 16) {
/* 1533 */         ptr = 0;
/* 1534 */         seg = (seg == null) ? null : seg.next();
/*      */       } 
/* 1536 */       return (seg == null) ? null : seg.type(ptr);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void close() throws IOException {
/* 1547 */       if (!this._closed) {
/* 1548 */         this._closed = true;
/*      */       }
/*      */     }
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
/*      */     public JsonToken nextToken() throws IOException {
/* 1562 */       if (this._closed || this._segment == null) return null;
/*      */ 
/*      */       
/* 1565 */       if (++this._segmentPtr >= 16) {
/* 1566 */         this._segmentPtr = 0;
/* 1567 */         this._segment = this._segment.next();
/* 1568 */         if (this._segment == null) {
/* 1569 */           return null;
/*      */         }
/*      */       } 
/* 1572 */       this._currToken = this._segment.type(this._segmentPtr);
/*      */       
/* 1574 */       if (this._currToken == JsonToken.FIELD_NAME) {
/* 1575 */         Object ob = _currentObject();
/* 1576 */         String name = (ob instanceof String) ? (String)ob : ob.toString();
/* 1577 */         this._parsingContext.setCurrentName(name);
/* 1578 */       } else if (this._currToken == JsonToken.START_OBJECT) {
/* 1579 */         this._parsingContext = this._parsingContext.createChildObjectContext();
/* 1580 */       } else if (this._currToken == JsonToken.START_ARRAY) {
/* 1581 */         this._parsingContext = this._parsingContext.createChildArrayContext();
/* 1582 */       } else if (this._currToken == JsonToken.END_OBJECT || this._currToken == JsonToken.END_ARRAY) {
/*      */ 
/*      */         
/* 1585 */         this._parsingContext = this._parsingContext.parentOrCopy();
/*      */       } else {
/* 1587 */         this._parsingContext.updateForValue();
/*      */       } 
/* 1589 */       return this._currToken;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String nextFieldName() throws IOException {
/* 1596 */       if (this._closed || this._segment == null) {
/* 1597 */         return null;
/*      */       }
/*      */       
/* 1600 */       int ptr = this._segmentPtr + 1;
/* 1601 */       if (ptr < 16 && this._segment.type(ptr) == JsonToken.FIELD_NAME) {
/* 1602 */         this._segmentPtr = ptr;
/* 1603 */         this._currToken = JsonToken.FIELD_NAME;
/* 1604 */         Object ob = this._segment.get(ptr);
/* 1605 */         String name = (ob instanceof String) ? (String)ob : ob.toString();
/* 1606 */         this._parsingContext.setCurrentName(name);
/* 1607 */         return name;
/*      */       } 
/* 1609 */       return (nextToken() == JsonToken.FIELD_NAME) ? getCurrentName() : null;
/*      */     }
/*      */     
/*      */     public boolean isClosed() {
/* 1613 */       return this._closed;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public JsonStreamContext getParsingContext() {
/* 1622 */       return this._parsingContext;
/*      */     }
/*      */     public JsonLocation getTokenLocation() {
/* 1625 */       return getCurrentLocation();
/*      */     }
/*      */     
/*      */     public JsonLocation getCurrentLocation() {
/* 1629 */       return (this._location == null) ? JsonLocation.NA : this._location;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public String getCurrentName() {
/* 1635 */       if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
/* 1636 */         JsonStreamContext parent = this._parsingContext.getParent();
/* 1637 */         return parent.getCurrentName();
/*      */       } 
/* 1639 */       return this._parsingContext.getCurrentName();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public void overrideCurrentName(String name) {
/* 1646 */       JsonStreamContext ctxt = this._parsingContext;
/* 1647 */       if (this._currToken == JsonToken.START_OBJECT || this._currToken == JsonToken.START_ARRAY) {
/* 1648 */         ctxt = ctxt.getParent();
/*      */       }
/* 1650 */       if (ctxt instanceof TokenBufferReadContext) {
/*      */         try {
/* 1652 */           ((TokenBufferReadContext)ctxt).setCurrentName(name);
/* 1653 */         } catch (IOException e) {
/* 1654 */           throw new RuntimeException(e);
/*      */         } 
/*      */       }
/*      */     }
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
/*      */     public String getText() {
/* 1669 */       if (this._currToken == JsonToken.VALUE_STRING || this._currToken == JsonToken.FIELD_NAME) {
/*      */         
/* 1671 */         Object ob = _currentObject();
/* 1672 */         if (ob instanceof String) {
/* 1673 */           return (String)ob;
/*      */         }
/* 1675 */         return ClassUtil.nullOrToString(ob);
/*      */       } 
/* 1677 */       if (this._currToken == null) {
/* 1678 */         return null;
/*      */       }
/* 1680 */       switch (this._currToken) {
/*      */         case null:
/*      */         case null:
/* 1683 */           return ClassUtil.nullOrToString(_currentObject());
/*      */       } 
/* 1685 */       return this._currToken.asString();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public char[] getTextCharacters() {
/* 1691 */       String str = getText();
/* 1692 */       return (str == null) ? null : str.toCharArray();
/*      */     }
/*      */ 
/*      */     
/*      */     public int getTextLength() {
/* 1697 */       String str = getText();
/* 1698 */       return (str == null) ? 0 : str.length();
/*      */     }
/*      */     
/*      */     public int getTextOffset() {
/* 1702 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean hasTextCharacters() {
/* 1707 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isNaN() {
/* 1719 */       if (this._currToken == JsonToken.VALUE_NUMBER_FLOAT) {
/* 1720 */         Object value = _currentObject();
/* 1721 */         if (value instanceof Double) {
/* 1722 */           Double v = (Double)value;
/* 1723 */           return (v.isNaN() || v.isInfinite());
/*      */         } 
/* 1725 */         if (value instanceof Float) {
/* 1726 */           Float v = (Float)value;
/* 1727 */           return (v.isNaN() || v.isInfinite());
/*      */         } 
/*      */       } 
/* 1730 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public BigInteger getBigIntegerValue() throws IOException {
/* 1736 */       Number n = getNumberValue();
/* 1737 */       if (n instanceof BigInteger) {
/* 1738 */         return (BigInteger)n;
/*      */       }
/* 1740 */       if (getNumberType() == JsonParser.NumberType.BIG_DECIMAL) {
/* 1741 */         return ((BigDecimal)n).toBigInteger();
/*      */       }
/*      */       
/* 1744 */       return BigInteger.valueOf(n.longValue());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public BigDecimal getDecimalValue() throws IOException {
/* 1750 */       Number n = getNumberValue();
/* 1751 */       if (n instanceof BigDecimal) {
/* 1752 */         return (BigDecimal)n;
/*      */       }
/* 1754 */       switch (getNumberType()) {
/*      */         case INT:
/*      */         case LONG:
/* 1757 */           return BigDecimal.valueOf(n.longValue());
/*      */         case BIG_INTEGER:
/* 1759 */           return new BigDecimal((BigInteger)n);
/*      */       } 
/*      */ 
/*      */       
/* 1763 */       return BigDecimal.valueOf(n.doubleValue());
/*      */     }
/*      */ 
/*      */     
/*      */     public double getDoubleValue() throws IOException {
/* 1768 */       return getNumberValue().doubleValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public float getFloatValue() throws IOException {
/* 1773 */       return getNumberValue().floatValue();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int getIntValue() throws IOException {
/* 1780 */       Number n = (this._currToken == JsonToken.VALUE_NUMBER_INT) ? (Number)_currentObject() : getNumberValue();
/* 1781 */       if (n instanceof Integer || _smallerThanInt(n)) {
/* 1782 */         return n.intValue();
/*      */       }
/* 1784 */       return _convertNumberToInt(n);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public long getLongValue() throws IOException {
/* 1790 */       Number n = (this._currToken == JsonToken.VALUE_NUMBER_INT) ? (Number)_currentObject() : getNumberValue();
/* 1791 */       if (n instanceof Long || _smallerThanLong(n)) {
/* 1792 */         return n.longValue();
/*      */       }
/* 1794 */       return _convertNumberToLong(n);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public JsonParser.NumberType getNumberType() throws IOException {
/* 1800 */       Number n = getNumberValue();
/* 1801 */       if (n instanceof Integer) return JsonParser.NumberType.INT; 
/* 1802 */       if (n instanceof Long) return JsonParser.NumberType.LONG; 
/* 1803 */       if (n instanceof Double) return JsonParser.NumberType.DOUBLE; 
/* 1804 */       if (n instanceof BigDecimal) return JsonParser.NumberType.BIG_DECIMAL; 
/* 1805 */       if (n instanceof BigInteger) return JsonParser.NumberType.BIG_INTEGER; 
/* 1806 */       if (n instanceof Float) return JsonParser.NumberType.FLOAT; 
/* 1807 */       if (n instanceof Short) return JsonParser.NumberType.INT; 
/* 1808 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public final Number getNumberValue() throws IOException {
/* 1813 */       _checkIsNumber();
/* 1814 */       Object value = _currentObject();
/* 1815 */       if (value instanceof Number) {
/* 1816 */         return (Number)value;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1821 */       if (value instanceof String) {
/* 1822 */         String str = (String)value;
/* 1823 */         if (str.indexOf('.') >= 0) {
/* 1824 */           return Double.valueOf(Double.parseDouble(str));
/*      */         }
/* 1826 */         return Long.valueOf(Long.parseLong(str));
/*      */       } 
/* 1828 */       if (value == null) {
/* 1829 */         return null;
/*      */       }
/* 1831 */       throw new IllegalStateException("Internal error: entry should be a Number, but is of type " + value
/* 1832 */           .getClass().getName());
/*      */     }
/*      */     
/*      */     private final boolean _smallerThanInt(Number n) {
/* 1836 */       return (n instanceof Short || n instanceof Byte);
/*      */     }
/*      */     
/*      */     private final boolean _smallerThanLong(Number n) {
/* 1840 */       return (n instanceof Integer || n instanceof Short || n instanceof Byte);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected int _convertNumberToInt(Number n) throws IOException {
/* 1847 */       if (n instanceof Long) {
/* 1848 */         long l = n.longValue();
/* 1849 */         int result = (int)l;
/* 1850 */         if (result != l) {
/* 1851 */           reportOverflowInt();
/*      */         }
/* 1853 */         return result;
/*      */       } 
/* 1855 */       if (n instanceof BigInteger) {
/* 1856 */         BigInteger big = (BigInteger)n;
/* 1857 */         if (BI_MIN_INT.compareTo(big) > 0 || BI_MAX_INT
/* 1858 */           .compareTo(big) < 0)
/* 1859 */           reportOverflowInt(); 
/*      */       } else {
/* 1861 */         if (n instanceof Double || n instanceof Float) {
/* 1862 */           double d = n.doubleValue();
/*      */           
/* 1864 */           if (d < -2.147483648E9D || d > 2.147483647E9D) {
/* 1865 */             reportOverflowInt();
/*      */           }
/* 1867 */           return (int)d;
/* 1868 */         }  if (n instanceof BigDecimal) {
/* 1869 */           BigDecimal big = (BigDecimal)n;
/* 1870 */           if (BD_MIN_INT.compareTo(big) > 0 || BD_MAX_INT
/* 1871 */             .compareTo(big) < 0) {
/* 1872 */             reportOverflowInt();
/*      */           }
/*      */         } else {
/* 1875 */           _throwInternal();
/*      */         } 
/* 1877 */       }  return n.intValue();
/*      */     }
/*      */ 
/*      */     
/*      */     protected long _convertNumberToLong(Number n) throws IOException {
/* 1882 */       if (n instanceof BigInteger) {
/* 1883 */         BigInteger big = (BigInteger)n;
/* 1884 */         if (BI_MIN_LONG.compareTo(big) > 0 || BI_MAX_LONG
/* 1885 */           .compareTo(big) < 0)
/* 1886 */           reportOverflowLong(); 
/*      */       } else {
/* 1888 */         if (n instanceof Double || n instanceof Float) {
/* 1889 */           double d = n.doubleValue();
/*      */           
/* 1891 */           if (d < -9.223372036854776E18D || d > 9.223372036854776E18D) {
/* 1892 */             reportOverflowLong();
/*      */           }
/* 1894 */           return (long)d;
/* 1895 */         }  if (n instanceof BigDecimal) {
/* 1896 */           BigDecimal big = (BigDecimal)n;
/* 1897 */           if (BD_MIN_LONG.compareTo(big) > 0 || BD_MAX_LONG
/* 1898 */             .compareTo(big) < 0) {
/* 1899 */             reportOverflowLong();
/*      */           }
/*      */         } else {
/* 1902 */           _throwInternal();
/*      */         } 
/* 1904 */       }  return n.longValue();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Object getEmbeddedObject() {
/* 1916 */       if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 1917 */         return _currentObject();
/*      */       }
/* 1919 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] getBinaryValue(Base64Variant b64variant) throws IOException, JsonParseException {
/* 1927 */       if (this._currToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
/*      */         
/* 1929 */         Object ob = _currentObject();
/* 1930 */         if (ob instanceof byte[]) {
/* 1931 */           return (byte[])ob;
/*      */         }
/*      */       } 
/*      */       
/* 1935 */       if (this._currToken != JsonToken.VALUE_STRING) {
/* 1936 */         throw _constructError("Current token (" + this._currToken + ") not VALUE_STRING (or VALUE_EMBEDDED_OBJECT with byte[]), cannot access as binary");
/*      */       }
/* 1938 */       String str = getText();
/* 1939 */       if (str == null) {
/* 1940 */         return null;
/*      */       }
/* 1942 */       ByteArrayBuilder builder = this._byteBuilder;
/* 1943 */       if (builder == null) {
/* 1944 */         this._byteBuilder = builder = new ByteArrayBuilder(100);
/*      */       } else {
/* 1946 */         this._byteBuilder.reset();
/*      */       } 
/* 1948 */       _decodeBase64(str, builder, b64variant);
/* 1949 */       return builder.toByteArray();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public int readBinaryValue(Base64Variant b64variant, OutputStream out) throws IOException {
/* 1955 */       byte[] data = getBinaryValue(b64variant);
/* 1956 */       if (data != null) {
/* 1957 */         out.write(data, 0, data.length);
/* 1958 */         return data.length;
/*      */       } 
/* 1960 */       return 0;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canReadObjectId() {
/* 1971 */       return this._hasNativeObjectIds;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean canReadTypeId() {
/* 1976 */       return this._hasNativeTypeIds;
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getTypeId() {
/* 1981 */       return this._segment.findTypeId(this._segmentPtr);
/*      */     }
/*      */ 
/*      */     
/*      */     public Object getObjectId() {
/* 1986 */       return this._segment.findObjectId(this._segmentPtr);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected final Object _currentObject() {
/* 1996 */       return this._segment.get(this._segmentPtr);
/*      */     }
/*      */ 
/*      */     
/*      */     protected final void _checkIsNumber() throws JsonParseException {
/* 2001 */       if (this._currToken == null || !this._currToken.isNumeric()) {
/* 2002 */         throw _constructError("Current token (" + this._currToken + ") not numeric, cannot use numeric value accessors");
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     protected void _handleEOF() throws JsonParseException {
/* 2008 */       _throwInternal();
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
/*      */   protected static final class Segment
/*      */   {
/*      */     public static final int TOKENS_PER_SEGMENT = 16;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2030 */     private static final JsonToken[] TOKEN_TYPES_BY_INDEX = new JsonToken[16]; static {
/* 2031 */       JsonToken[] t = JsonToken.values();
/*      */       
/* 2033 */       System.arraycopy(t, 1, TOKEN_TYPES_BY_INDEX, 1, Math.min(15, t.length - 1));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected Segment _next;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected long _tokenTypes;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2051 */     protected final Object[] _tokens = new Object[16];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected TreeMap<Integer, Object> _nativeIds;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public JsonToken type(int index) {
/* 2064 */       long l = this._tokenTypes;
/* 2065 */       if (index > 0) {
/* 2066 */         l >>= index << 2;
/*      */       }
/* 2068 */       int ix = (int)l & 0xF;
/* 2069 */       return TOKEN_TYPES_BY_INDEX[ix];
/*      */     }
/*      */ 
/*      */     
/*      */     public int rawType(int index) {
/* 2074 */       long l = this._tokenTypes;
/* 2075 */       if (index > 0) {
/* 2076 */         l >>= index << 2;
/*      */       }
/* 2078 */       int ix = (int)l & 0xF;
/* 2079 */       return ix;
/*      */     }
/*      */     
/*      */     public Object get(int index) {
/* 2083 */       return this._tokens[index];
/*      */     }
/*      */     public Segment next() {
/* 2086 */       return this._next;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean hasIds() {
/* 2093 */       return (this._nativeIds != null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Segment append(int index, JsonToken tokenType) {
/* 2100 */       if (index < 16) {
/* 2101 */         set(index, tokenType);
/* 2102 */         return null;
/*      */       } 
/* 2104 */       this._next = new Segment();
/* 2105 */       this._next.set(0, tokenType);
/* 2106 */       return this._next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Segment append(int index, JsonToken tokenType, Object objectId, Object typeId) {
/* 2112 */       if (index < 16) {
/* 2113 */         set(index, tokenType, objectId, typeId);
/* 2114 */         return null;
/*      */       } 
/* 2116 */       this._next = new Segment();
/* 2117 */       this._next.set(0, tokenType, objectId, typeId);
/* 2118 */       return this._next;
/*      */     }
/*      */ 
/*      */     
/*      */     public Segment append(int index, JsonToken tokenType, Object value) {
/* 2123 */       if (index < 16) {
/* 2124 */         set(index, tokenType, value);
/* 2125 */         return null;
/*      */       } 
/* 2127 */       this._next = new Segment();
/* 2128 */       this._next.set(0, tokenType, value);
/* 2129 */       return this._next;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Segment append(int index, JsonToken tokenType, Object value, Object objectId, Object typeId) {
/* 2135 */       if (index < 16) {
/* 2136 */         set(index, tokenType, value, objectId, typeId);
/* 2137 */         return null;
/*      */       } 
/* 2139 */       this._next = new Segment();
/* 2140 */       this._next.set(0, tokenType, value, objectId, typeId);
/* 2141 */       return this._next;
/*      */     }
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
/*      */     private void set(int index, JsonToken tokenType) {
/* 2195 */       long typeCode = tokenType.ordinal();
/* 2196 */       if (index > 0) {
/* 2197 */         typeCode <<= index << 2;
/*      */       }
/* 2199 */       this._tokenTypes |= typeCode;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void set(int index, JsonToken tokenType, Object objectId, Object typeId) {
/* 2205 */       long typeCode = tokenType.ordinal();
/* 2206 */       if (index > 0) {
/* 2207 */         typeCode <<= index << 2;
/*      */       }
/* 2209 */       this._tokenTypes |= typeCode;
/* 2210 */       assignNativeIds(index, objectId, typeId);
/*      */     }
/*      */ 
/*      */     
/*      */     private void set(int index, JsonToken tokenType, Object value) {
/* 2215 */       this._tokens[index] = value;
/* 2216 */       long typeCode = tokenType.ordinal();
/* 2217 */       if (index > 0) {
/* 2218 */         typeCode <<= index << 2;
/*      */       }
/* 2220 */       this._tokenTypes |= typeCode;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private void set(int index, JsonToken tokenType, Object value, Object objectId, Object typeId) {
/* 2226 */       this._tokens[index] = value;
/* 2227 */       long typeCode = tokenType.ordinal();
/* 2228 */       if (index > 0) {
/* 2229 */         typeCode <<= index << 2;
/*      */       }
/* 2231 */       this._tokenTypes |= typeCode;
/* 2232 */       assignNativeIds(index, objectId, typeId);
/*      */     }
/*      */ 
/*      */     
/*      */     private final void assignNativeIds(int index, Object objectId, Object typeId) {
/* 2237 */       if (this._nativeIds == null) {
/* 2238 */         this._nativeIds = new TreeMap<>();
/*      */       }
/* 2240 */       if (objectId != null) {
/* 2241 */         this._nativeIds.put(Integer.valueOf(_objectIdIndex(index)), objectId);
/*      */       }
/* 2243 */       if (typeId != null) {
/* 2244 */         this._nativeIds.put(Integer.valueOf(_typeIdIndex(index)), typeId);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object findObjectId(int index) {
/* 2252 */       return (this._nativeIds == null) ? null : this._nativeIds.get(Integer.valueOf(_objectIdIndex(index)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Object findTypeId(int index) {
/* 2259 */       return (this._nativeIds == null) ? null : this._nativeIds.get(Integer.valueOf(_typeIdIndex(index)));
/*      */     }
/*      */     
/* 2262 */     private final int _typeIdIndex(int i) { return i + i; } private final int _objectIdIndex(int i) {
/* 2263 */       return i + i + 1;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databin\\util\TokenBuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */