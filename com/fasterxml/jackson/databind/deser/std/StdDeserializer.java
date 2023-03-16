/*      */ package com.fasterxml.jackson.databind.deser.std;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonFormat;
/*      */ import com.fasterxml.jackson.annotation.Nulls;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.io.NumberInput;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanProperty;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*      */ import com.fasterxml.jackson.databind.MapperFeature;
/*      */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.deser.BeanDeserializerBase;
/*      */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*      */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*      */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*      */ import com.fasterxml.jackson.databind.deser.impl.NullsAsEmptyProvider;
/*      */ import com.fasterxml.jackson.databind.deser.impl.NullsConstantProvider;
/*      */ import com.fasterxml.jackson.databind.deser.impl.NullsFailProvider;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.util.AccessPattern;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.Converter;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Map;
/*      */ 
/*      */ public abstract class StdDeserializer<T>
/*      */   extends JsonDeserializer<T>
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   44 */   protected static final int F_MASK_INT_COERCIONS = DeserializationFeature.USE_BIG_INTEGER_FOR_INTS
/*   45 */     .getMask() | DeserializationFeature.USE_LONG_FOR_INTS
/*   46 */     .getMask();
/*      */ 
/*      */   
/*   49 */   protected static final int F_MASK_ACCEPT_ARRAYS = DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS
/*   50 */     .getMask() | DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT
/*   51 */     .getMask();
/*      */ 
/*      */ 
/*      */   
/*      */   protected final Class<?> _valueClass;
/*      */ 
/*      */ 
/*      */   
/*      */   protected final JavaType _valueType;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected StdDeserializer(Class<?> vc) {
/*   65 */     this._valueClass = vc;
/*   66 */     this._valueType = null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected StdDeserializer(JavaType valueType) {
/*   71 */     this._valueClass = (valueType == null) ? Object.class : valueType.getRawClass();
/*   72 */     this._valueType = valueType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected StdDeserializer(StdDeserializer<?> src) {
/*   82 */     this._valueClass = src._valueClass;
/*   83 */     this._valueType = src._valueType;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Class<?> handledType() {
/*   93 */     return this._valueClass;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public final Class<?> getValueClass() {
/*  105 */     return this._valueClass;
/*      */   }
/*      */ 
/*      */   
/*      */   public JavaType getValueType() {
/*  110 */     return this._valueType;
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
/*      */   public JavaType getValueType(DeserializationContext ctxt) {
/*  126 */     if (this._valueType != null) {
/*  127 */       return this._valueType;
/*      */     }
/*  129 */     return ctxt.constructType(this._valueClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isDefaultDeserializer(JsonDeserializer<?> deserializer) {
/*  139 */     return ClassUtil.isJacksonStdImpl(deserializer);
/*      */   }
/*      */   
/*      */   protected boolean isDefaultKeyDeserializer(KeyDeserializer keyDeser) {
/*  143 */     return ClassUtil.isJacksonStdImpl(keyDeser);
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
/*      */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/*  160 */     return typeDeserializer.deserializeTypedFromAny(p, ctxt);
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
/*      */   protected final boolean _parseBooleanPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  173 */     JsonToken t = p.getCurrentToken();
/*  174 */     if (t == JsonToken.VALUE_TRUE) return true; 
/*  175 */     if (t == JsonToken.VALUE_FALSE) return false; 
/*  176 */     if (t == JsonToken.VALUE_NULL) {
/*  177 */       _verifyNullForPrimitive(ctxt);
/*  178 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  182 */     if (t == JsonToken.VALUE_NUMBER_INT) {
/*  183 */       return _parseBooleanFromInt(p, ctxt);
/*      */     }
/*      */     
/*  186 */     if (t == JsonToken.VALUE_STRING) {
/*  187 */       String text = p.getText().trim();
/*      */       
/*  189 */       if ("true".equals(text) || "True".equals(text)) {
/*  190 */         return true;
/*      */       }
/*  192 */       if ("false".equals(text) || "False".equals(text)) {
/*  193 */         return false;
/*      */       }
/*  195 */       if (_isEmptyOrTextualNull(text)) {
/*  196 */         _verifyNullForPrimitiveCoercion(ctxt, text);
/*  197 */         return false;
/*      */       } 
/*  199 */       Boolean b = (Boolean)ctxt.handleWeirdStringValue(this._valueClass, text, "only \"true\" or \"false\" recognized", new Object[0]);
/*      */       
/*  201 */       return Boolean.TRUE.equals(b);
/*      */     } 
/*      */     
/*  204 */     if (t == JsonToken.START_ARRAY && ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  205 */       p.nextToken();
/*  206 */       boolean parsed = _parseBooleanPrimitive(p, ctxt);
/*  207 */       _verifyEndArrayForSingle(p, ctxt);
/*  208 */       return parsed;
/*      */     } 
/*      */     
/*  211 */     return ((Boolean)ctxt.handleUnexpectedToken(this._valueClass, p)).booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _parseBooleanFromInt(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  221 */     _verifyNumberForScalarCoercion(ctxt, p);
/*      */ 
/*      */     
/*  224 */     return !"0".equals(p.getText());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final byte _parseBytePrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  230 */     int value = _parseIntPrimitive(p, ctxt);
/*      */     
/*  232 */     if (_byteOverflow(value)) {
/*  233 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, String.valueOf(value), "overflow, value cannot be represented as 8-bit value", new Object[0]);
/*      */       
/*  235 */       return _nonNullNumber(v).byteValue();
/*      */     } 
/*  237 */     return (byte)value;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final short _parseShortPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  243 */     int value = _parseIntPrimitive(p, ctxt);
/*      */     
/*  245 */     if (_shortOverflow(value)) {
/*  246 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, String.valueOf(value), "overflow, value cannot be represented as 16-bit value", new Object[0]);
/*      */       
/*  248 */       return _nonNullNumber(v).shortValue();
/*      */     } 
/*  250 */     return (short)value;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final int _parseIntPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     String text;
/*  256 */     if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
/*  257 */       return p.getIntValue();
/*      */     }
/*  259 */     switch (p.getCurrentTokenId()) {
/*      */       case 6:
/*  261 */         text = p.getText().trim();
/*  262 */         if (_isEmptyOrTextualNull(text)) {
/*  263 */           _verifyNullForPrimitiveCoercion(ctxt, text);
/*  264 */           return 0;
/*      */         } 
/*  266 */         return _parseIntPrimitive(ctxt, text);
/*      */       case 8:
/*  268 */         if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
/*  269 */           _failDoubleToIntCoercion(p, ctxt, "int");
/*      */         }
/*  271 */         return p.getValueAsInt();
/*      */       case 11:
/*  273 */         _verifyNullForPrimitive(ctxt);
/*  274 */         return 0;
/*      */       case 3:
/*  276 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  277 */           p.nextToken();
/*  278 */           int parsed = _parseIntPrimitive(p, ctxt);
/*  279 */           _verifyEndArrayForSingle(p, ctxt);
/*  280 */           return parsed;
/*      */         } 
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*  286 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final int _parseIntPrimitive(DeserializationContext ctxt, String text) throws IOException {
/*      */     try {
/*  295 */       if (text.length() > 9) {
/*  296 */         long l = Long.parseLong(text);
/*  297 */         if (_intOverflow(l)) {
/*  298 */           Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "Overflow: numeric value (%s) out of range of int (%d -%d)", new Object[] { text, 
/*      */                 
/*  300 */                 Integer.valueOf(-2147483648), Integer.valueOf(2147483647) });
/*  301 */           return _nonNullNumber(v).intValue();
/*      */         } 
/*  303 */         return (int)l;
/*      */       } 
/*  305 */       return NumberInput.parseInt(text);
/*  306 */     } catch (IllegalArgumentException iae) {
/*  307 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid int value", new Object[0]);
/*      */       
/*  309 */       return _nonNullNumber(v).intValue();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected final long _parseLongPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     String text;
/*  316 */     if (p.hasToken(JsonToken.VALUE_NUMBER_INT)) {
/*  317 */       return p.getLongValue();
/*      */     }
/*  319 */     switch (p.getCurrentTokenId()) {
/*      */       case 6:
/*  321 */         text = p.getText().trim();
/*  322 */         if (_isEmptyOrTextualNull(text)) {
/*  323 */           _verifyNullForPrimitiveCoercion(ctxt, text);
/*  324 */           return 0L;
/*      */         } 
/*  326 */         return _parseLongPrimitive(ctxt, text);
/*      */       case 8:
/*  328 */         if (!ctxt.isEnabled(DeserializationFeature.ACCEPT_FLOAT_AS_INT)) {
/*  329 */           _failDoubleToIntCoercion(p, ctxt, "long");
/*      */         }
/*  331 */         return p.getValueAsLong();
/*      */       case 11:
/*  333 */         _verifyNullForPrimitive(ctxt);
/*  334 */         return 0L;
/*      */       case 3:
/*  336 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  337 */           p.nextToken();
/*  338 */           long parsed = _parseLongPrimitive(p, ctxt);
/*  339 */           _verifyEndArrayForSingle(p, ctxt);
/*  340 */           return parsed;
/*      */         } 
/*      */         break;
/*      */     } 
/*  344 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).longValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final long _parseLongPrimitive(DeserializationContext ctxt, String text) throws IOException {
/*      */     try {
/*  353 */       return NumberInput.parseLong(text);
/*  354 */     } catch (IllegalArgumentException illegalArgumentException) {
/*      */       
/*  356 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid long value", new Object[0]);
/*      */       
/*  358 */       return _nonNullNumber(v).longValue();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected final float _parseFloatPrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     String text;
/*  365 */     if (p.hasToken(JsonToken.VALUE_NUMBER_FLOAT)) {
/*  366 */       return p.getFloatValue();
/*      */     }
/*  368 */     switch (p.getCurrentTokenId()) {
/*      */       case 6:
/*  370 */         text = p.getText().trim();
/*  371 */         if (_isEmptyOrTextualNull(text)) {
/*  372 */           _verifyNullForPrimitiveCoercion(ctxt, text);
/*  373 */           return 0.0F;
/*      */         } 
/*  375 */         return _parseFloatPrimitive(ctxt, text);
/*      */       case 7:
/*  377 */         return p.getFloatValue();
/*      */       case 11:
/*  379 */         _verifyNullForPrimitive(ctxt);
/*  380 */         return 0.0F;
/*      */       case 3:
/*  382 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  383 */           p.nextToken();
/*  384 */           float parsed = _parseFloatPrimitive(p, ctxt);
/*  385 */           _verifyEndArrayForSingle(p, ctxt);
/*  386 */           return parsed;
/*      */         } 
/*      */         break;
/*      */     } 
/*      */     
/*  391 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).floatValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final float _parseFloatPrimitive(DeserializationContext ctxt, String text) throws IOException {
/*  400 */     switch (text.charAt(0)) {
/*      */       case 'I':
/*  402 */         if (_isPosInf(text)) {
/*  403 */           return Float.POSITIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */       case 'N':
/*  407 */         if (_isNaN(text)) return Float.NaN; 
/*      */         break;
/*      */       case '-':
/*  410 */         if (_isNegInf(text)) {
/*  411 */           return Float.NEGATIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */     } 
/*      */     try {
/*  416 */       return Float.parseFloat(text);
/*  417 */     } catch (IllegalArgumentException illegalArgumentException) {
/*  418 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid float value", new Object[0]);
/*      */       
/*  420 */       return _nonNullNumber(v).floatValue();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected final double _parseDoublePrimitive(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     String text;
/*  426 */     if (p.hasToken(JsonToken.VALUE_NUMBER_FLOAT)) {
/*  427 */       return p.getDoubleValue();
/*      */     }
/*  429 */     switch (p.getCurrentTokenId()) {
/*      */       case 6:
/*  431 */         text = p.getText().trim();
/*  432 */         if (_isEmptyOrTextualNull(text)) {
/*  433 */           _verifyNullForPrimitiveCoercion(ctxt, text);
/*  434 */           return 0.0D;
/*      */         } 
/*  436 */         return _parseDoublePrimitive(ctxt, text);
/*      */       case 7:
/*  438 */         return p.getDoubleValue();
/*      */       case 11:
/*  440 */         _verifyNullForPrimitive(ctxt);
/*  441 */         return 0.0D;
/*      */       case 3:
/*  443 */         if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  444 */           p.nextToken();
/*  445 */           double parsed = _parseDoublePrimitive(p, ctxt);
/*  446 */           _verifyEndArrayForSingle(p, ctxt);
/*  447 */           return parsed;
/*      */         } 
/*      */         break;
/*      */     } 
/*      */     
/*  452 */     return ((Number)ctxt.handleUnexpectedToken(this._valueClass, p)).doubleValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final double _parseDoublePrimitive(DeserializationContext ctxt, String text) throws IOException {
/*  461 */     switch (text.charAt(0)) {
/*      */       case 'I':
/*  463 */         if (_isPosInf(text)) {
/*  464 */           return Double.POSITIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */       case 'N':
/*  468 */         if (_isNaN(text)) {
/*  469 */           return Double.NaN;
/*      */         }
/*      */         break;
/*      */       case '-':
/*  473 */         if (_isNegInf(text)) {
/*  474 */           return Double.NEGATIVE_INFINITY;
/*      */         }
/*      */         break;
/*      */     } 
/*      */     try {
/*  479 */       return parseDouble(text);
/*  480 */     } catch (IllegalArgumentException illegalArgumentException) {
/*  481 */       Number v = (Number)ctxt.handleWeirdStringValue(this._valueClass, text, "not a valid double value (as String to convert)", new Object[0]);
/*      */       
/*  483 */       return _nonNullNumber(v).doubleValue();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Date _parseDate(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     long ts;
/*  489 */     switch (p.getCurrentTokenId()) {
/*      */       case 6:
/*  491 */         return _parseDate(p.getText().trim(), ctxt);
/*      */ 
/*      */       
/*      */       case 7:
/*      */         try {
/*  496 */           ts = p.getLongValue();
/*      */         
/*      */         }
/*  499 */         catch (JsonParseException|com.fasterxml.jackson.core.exc.InputCoercionException e) {
/*  500 */           Number v = (Number)ctxt.handleWeirdNumberValue(this._valueClass, p.getNumberValue(), "not a valid 64-bit long for creating `java.util.Date`", new Object[0]);
/*      */           
/*  502 */           ts = v.longValue();
/*      */         } 
/*  504 */         return new Date(ts);
/*      */       
/*      */       case 11:
/*  507 */         return (Date)getNullValue(ctxt);
/*      */       case 3:
/*  509 */         return _parseDateFromArray(p, ctxt);
/*      */     } 
/*  511 */     return (Date)ctxt.handleUnexpectedToken(this._valueClass, p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Date _parseDateFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
/*      */     JsonToken t;
/*  519 */     if (ctxt.hasSomeOfFeatures(F_MASK_ACCEPT_ARRAYS)) {
/*  520 */       t = p.nextToken();
/*  521 */       if (t == JsonToken.END_ARRAY && 
/*  522 */         ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/*  523 */         return (Date)getNullValue(ctxt);
/*      */       }
/*      */       
/*  526 */       if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  527 */         Date parsed = _parseDate(p, ctxt);
/*  528 */         _verifyEndArrayForSingle(p, ctxt);
/*  529 */         return parsed;
/*      */       } 
/*      */     } else {
/*  532 */       t = p.getCurrentToken();
/*      */     } 
/*  534 */     return (Date)ctxt.handleUnexpectedToken(this._valueClass, t, p, null, new Object[0]);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Date _parseDate(String value, DeserializationContext ctxt) throws IOException {
/*      */     try {
/*  545 */       if (_isEmptyOrTextualNull(value)) {
/*  546 */         return (Date)getNullValue(ctxt);
/*      */       }
/*  548 */       return ctxt.parseDate(value);
/*  549 */     } catch (IllegalArgumentException iae) {
/*  550 */       return (Date)ctxt.handleWeirdStringValue(this._valueClass, value, "not a valid representation (error: %s)", new Object[] {
/*      */             
/*  552 */             ClassUtil.exceptionMessage(iae)
/*      */           });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static final double parseDouble(String numStr) throws NumberFormatException {
/*  563 */     if ("2.2250738585072012e-308".equals(numStr)) {
/*  564 */       return 2.2250738585072014E-308D;
/*      */     }
/*  566 */     return Double.parseDouble(numStr);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final String _parseString(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  577 */     JsonToken t = p.getCurrentToken();
/*  578 */     if (t == JsonToken.VALUE_STRING) {
/*  579 */       return p.getText();
/*      */     }
/*      */     
/*  582 */     if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
/*  583 */       Object ob = p.getEmbeddedObject();
/*  584 */       if (ob instanceof byte[]) {
/*  585 */         return ctxt.getBase64Variant().encode((byte[])ob, false);
/*      */       }
/*  587 */       if (ob == null) {
/*  588 */         return null;
/*      */       }
/*      */       
/*  591 */       return ob.toString();
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
/*  605 */     String value = p.getValueAsString();
/*  606 */     if (value != null) {
/*  607 */       return value;
/*      */     }
/*  609 */     return (String)ctxt.handleUnexpectedToken(String.class, p);
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
/*      */   protected T _deserializeFromEmpty(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  622 */     JsonToken t = p.getCurrentToken();
/*  623 */     if (t == JsonToken.START_ARRAY) {
/*  624 */       if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/*  625 */         t = p.nextToken();
/*  626 */         if (t == JsonToken.END_ARRAY) {
/*  627 */           return null;
/*      */         }
/*  629 */         return (T)ctxt.handleUnexpectedToken(handledType(), p);
/*      */       } 
/*  631 */     } else if (t == JsonToken.VALUE_STRING && 
/*  632 */       ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
/*  633 */       String str = p.getText().trim();
/*  634 */       if (str.isEmpty()) {
/*  635 */         return null;
/*      */       }
/*      */     } 
/*      */     
/*  639 */     return (T)ctxt.handleUnexpectedToken(handledType(), p);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _hasTextualNull(String value) {
/*  650 */     return "null".equals(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean _isEmptyOrTextualNull(String value) {
/*  657 */     return (value.isEmpty() || "null".equals(value));
/*      */   }
/*      */   
/*      */   protected final boolean _isNegInf(String text) {
/*  661 */     return ("-Infinity".equals(text) || "-INF".equals(text));
/*      */   }
/*      */   
/*      */   protected final boolean _isPosInf(String text) {
/*  665 */     return ("Infinity".equals(text) || "INF".equals(text));
/*      */   }
/*      */   protected final boolean _isNaN(String text) {
/*  668 */     return "NaN".equals(text);
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
/*      */   protected T _deserializeFromArray(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  696 */     if (ctxt.hasSomeOfFeatures(F_MASK_ACCEPT_ARRAYS)) {
/*  697 */       JsonToken t = p.nextToken();
/*  698 */       if (t == JsonToken.END_ARRAY && 
/*  699 */         ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/*  700 */         return (T)getNullValue(ctxt);
/*      */       }
/*      */       
/*  703 */       if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/*  704 */         T parsed = (T)deserialize(p, ctxt);
/*  705 */         if (p.nextToken() != JsonToken.END_ARRAY) {
/*  706 */           handleMissingEndArrayForSingle(p, ctxt);
/*      */         }
/*  708 */         return parsed;
/*      */       } 
/*      */     } else {
/*  711 */       JsonToken t = p.getCurrentToken();
/*      */     } 
/*      */     
/*  714 */     T result = (T)ctxt.handleUnexpectedToken(getValueType(ctxt), p.getCurrentToken(), p, null, new Object[0]);
/*  715 */     return result;
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
/*      */   protected T _deserializeWrappedValue(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  730 */     if (p.hasToken(JsonToken.START_ARRAY)) {
/*  731 */       String msg = String.format("Cannot deserialize instance of %s out of %s token: nested Arrays not allowed with %s", new Object[] {
/*      */             
/*  733 */             ClassUtil.nameOf(this._valueClass), JsonToken.START_ARRAY, "DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS"
/*      */           });
/*      */       
/*  736 */       T result = (T)ctxt.handleUnexpectedToken(getValueType(ctxt), p.getCurrentToken(), p, msg, new Object[0]);
/*  737 */       return result;
/*      */     } 
/*  739 */     return (T)deserialize(p, ctxt);
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
/*      */   protected void _failDoubleToIntCoercion(JsonParser p, DeserializationContext ctxt, String type) throws IOException {
/*  751 */     ctxt.reportInputMismatch(handledType(), "Cannot coerce a floating-point value ('%s') into %s (enable `DeserializationFeature.ACCEPT_FLOAT_AS_INT` to allow)", new Object[] { p
/*      */           
/*  753 */           .getValueAsString(), type });
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
/*      */   protected Object _coerceIntegral(JsonParser p, DeserializationContext ctxt) throws IOException {
/*  769 */     int feats = ctxt.getDeserializationFeatures();
/*  770 */     if (DeserializationFeature.USE_BIG_INTEGER_FOR_INTS.enabledIn(feats)) {
/*  771 */       return p.getBigIntegerValue();
/*      */     }
/*  773 */     if (DeserializationFeature.USE_LONG_FOR_INTS.enabledIn(feats)) {
/*  774 */       return Long.valueOf(p.getLongValue());
/*      */     }
/*  776 */     return p.getBigIntegerValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _coerceNullToken(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
/*  787 */     if (isPrimitive) {
/*  788 */       _verifyNullForPrimitive(ctxt);
/*      */     }
/*  790 */     return getNullValue(ctxt);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _coerceTextualNull(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
/*      */     DeserializationFeature deserializationFeature;
/*      */     boolean enable;
/*  803 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/*  804 */       MapperFeature mapperFeature = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/*  805 */       enable = true;
/*  806 */     } else if (isPrimitive && ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
/*  807 */       deserializationFeature = DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
/*  808 */       enable = false;
/*      */     } else {
/*  810 */       return getNullValue(ctxt);
/*      */     } 
/*  812 */     _reportFailedNullCoerce(ctxt, enable, (Enum<?>)deserializationFeature, "String \"null\"");
/*  813 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object _coerceEmptyString(DeserializationContext ctxt, boolean isPrimitive) throws JsonMappingException {
/*      */     DeserializationFeature deserializationFeature;
/*      */     boolean enable;
/*  826 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/*  827 */       MapperFeature mapperFeature = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/*  828 */       enable = true;
/*  829 */     } else if (isPrimitive && ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
/*  830 */       deserializationFeature = DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
/*  831 */       enable = false;
/*      */     } else {
/*  833 */       return getNullValue(ctxt);
/*      */     } 
/*  835 */     _reportFailedNullCoerce(ctxt, enable, (Enum<?>)deserializationFeature, "empty String (\"\")");
/*  836 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _verifyNullForPrimitive(DeserializationContext ctxt) throws JsonMappingException {
/*  842 */     if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
/*  843 */       ctxt.reportInputMismatch(this, "Cannot coerce `null` %s (disable `DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES` to allow)", new Object[] {
/*      */             
/*  845 */             _coercedTypeDesc()
/*      */           });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _verifyNullForPrimitiveCoercion(DeserializationContext ctxt, String str) throws JsonMappingException {
/*      */     DeserializationFeature deserializationFeature;
/*      */     boolean enable;
/*  856 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/*  857 */       MapperFeature mapperFeature = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/*  858 */       enable = true;
/*  859 */     } else if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)) {
/*  860 */       deserializationFeature = DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES;
/*  861 */       enable = false;
/*      */     } else {
/*      */       return;
/*      */     } 
/*  865 */     String strDesc = str.isEmpty() ? "empty String (\"\")" : String.format("String \"%s\"", new Object[] { str });
/*  866 */     _reportFailedNullCoerce(ctxt, enable, (Enum<?>)deserializationFeature, strDesc);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void _verifyNullForScalarCoercion(DeserializationContext ctxt, String str) throws JsonMappingException {
/*  873 */     if (!ctxt.isEnabled(MapperFeature.ALLOW_COERCION_OF_SCALARS)) {
/*  874 */       String strDesc = str.isEmpty() ? "empty String (\"\")" : String.format("String \"%s\"", new Object[] { str });
/*  875 */       _reportFailedNullCoerce(ctxt, true, (Enum<?>)MapperFeature.ALLOW_COERCION_OF_SCALARS, strDesc);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _verifyStringForScalarCoercion(DeserializationContext ctxt, String str) throws JsonMappingException {
/*  882 */     MapperFeature feat = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/*  883 */     if (!ctxt.isEnabled(feat)) {
/*  884 */       ctxt.reportInputMismatch(this, "Cannot coerce String \"%s\" %s (enable `%s.%s` to allow)", new Object[] { str, 
/*  885 */             _coercedTypeDesc(), feat.getClass().getSimpleName(), feat.name() });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _verifyNumberForScalarCoercion(DeserializationContext ctxt, JsonParser p) throws IOException {
/*  892 */     MapperFeature feat = MapperFeature.ALLOW_COERCION_OF_SCALARS;
/*  893 */     if (!ctxt.isEnabled(feat)) {
/*      */ 
/*      */       
/*  896 */       String valueDesc = p.getText();
/*  897 */       ctxt.reportInputMismatch(this, "Cannot coerce Number (%s) %s (enable `%s.%s` to allow)", new Object[] { valueDesc, 
/*  898 */             _coercedTypeDesc(), feat.getClass().getSimpleName(), feat.name() });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _reportFailedNullCoerce(DeserializationContext ctxt, boolean state, Enum<?> feature, String inputDesc) throws JsonMappingException {
/*  905 */     String enableDesc = state ? "enable" : "disable";
/*  906 */     ctxt.reportInputMismatch(this, "Cannot coerce %s to Null value %s (%s `%s.%s` to allow)", new Object[] { inputDesc, 
/*  907 */           _coercedTypeDesc(), enableDesc, feature.getClass().getSimpleName(), feature.name() });
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
/*      */   protected String _coercedTypeDesc() {
/*      */     boolean structured;
/*      */     String typeDesc;
/*  923 */     JavaType t = getValueType();
/*  924 */     if (t != null && !t.isPrimitive()) {
/*  925 */       structured = (t.isContainerType() || t.isReferenceType());
/*      */       
/*  927 */       typeDesc = "'" + t.toString() + "'";
/*      */     } else {
/*  929 */       Class<?> cls = handledType();
/*      */       
/*  931 */       structured = (cls.isArray() || Collection.class.isAssignableFrom(cls) || Map.class.isAssignableFrom(cls));
/*  932 */       typeDesc = ClassUtil.nameOf(cls);
/*      */     } 
/*  934 */     if (structured) {
/*  935 */       return "as content of type " + typeDesc;
/*      */     }
/*  937 */     return "for type " + typeDesc;
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
/*      */   protected JsonDeserializer<Object> findDeserializer(DeserializationContext ctxt, JavaType type, BeanProperty property) throws JsonMappingException {
/*  959 */     return ctxt.findContextualValueDeserializer(type, property);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _isIntNumber(String text) {
/*  968 */     int len = text.length();
/*  969 */     if (len > 0) {
/*  970 */       char c = text.charAt(0);
/*      */       
/*  972 */       int i = (c == '-' || c == '+') ? 1 : 0;
/*  973 */       for (; i < len; i++) {
/*  974 */         int ch = text.charAt(i);
/*  975 */         if (ch > 57 || ch < 48) {
/*  976 */           return false;
/*      */         }
/*      */       } 
/*  979 */       return true;
/*      */     } 
/*  981 */     return false;
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
/*      */   protected JsonDeserializer<?> findConvertingContentDeserializer(DeserializationContext ctxt, BeanProperty prop, JsonDeserializer<?> existingDeserializer) throws JsonMappingException {
/* 1004 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 1005 */     if (_neitherNull(intr, prop)) {
/* 1006 */       AnnotatedMember member = prop.getMember();
/* 1007 */       if (member != null) {
/* 1008 */         Object convDef = intr.findDeserializationContentConverter(member);
/* 1009 */         if (convDef != null) {
/* 1010 */           Converter<Object, Object> conv = ctxt.converterInstance((Annotated)prop.getMember(), convDef);
/* 1011 */           JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/* 1012 */           if (existingDeserializer == null) {
/* 1013 */             existingDeserializer = ctxt.findContextualValueDeserializer(delegateType, prop);
/*      */           }
/* 1015 */           return new StdDelegatingDeserializer(conv, delegateType, existingDeserializer);
/*      */         } 
/*      */       } 
/*      */     } 
/* 1019 */     return existingDeserializer;
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
/*      */   protected JsonFormat.Value findFormatOverrides(DeserializationContext ctxt, BeanProperty prop, Class<?> typeForDefaults) {
/* 1040 */     if (prop != null) {
/* 1041 */       return prop.findPropertyFormat((MapperConfig)ctxt.getConfig(), typeForDefaults);
/*      */     }
/*      */     
/* 1044 */     return ctxt.getDefaultPropertyFormat(typeForDefaults);
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
/*      */   protected Boolean findFormatFeature(DeserializationContext ctxt, BeanProperty prop, Class<?> typeForDefaults, JsonFormat.Feature feat) {
/* 1060 */     JsonFormat.Value format = findFormatOverrides(ctxt, prop, typeForDefaults);
/* 1061 */     if (format != null) {
/* 1062 */       return format.getFeature(feat);
/*      */     }
/* 1064 */     return null;
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
/*      */   protected final NullValueProvider findValueNullProvider(DeserializationContext ctxt, SettableBeanProperty prop, PropertyMetadata propMetadata) throws JsonMappingException {
/* 1078 */     if (prop != null) {
/* 1079 */       return _findNullProvider(ctxt, (BeanProperty)prop, propMetadata.getValueNulls(), prop
/* 1080 */           .getValueDeserializer());
/*      */     }
/* 1082 */     return null;
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
/*      */   protected NullValueProvider findContentNullProvider(DeserializationContext ctxt, BeanProperty prop, JsonDeserializer<?> valueDeser) throws JsonMappingException {
/* 1097 */     Nulls nulls = findContentNullStyle(ctxt, prop);
/* 1098 */     if (nulls == Nulls.SKIP) {
/* 1099 */       return (NullValueProvider)NullsConstantProvider.skipper();
/*      */     }
/* 1101 */     NullValueProvider prov = _findNullProvider(ctxt, prop, nulls, valueDeser);
/* 1102 */     if (prov != null) {
/* 1103 */       return prov;
/*      */     }
/* 1105 */     return (NullValueProvider)valueDeser;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected Nulls findContentNullStyle(DeserializationContext ctxt, BeanProperty prop) throws JsonMappingException {
/* 1111 */     if (prop != null) {
/* 1112 */       return prop.getMetadata().getContentNulls();
/*      */     }
/* 1114 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final NullValueProvider _findNullProvider(DeserializationContext ctxt, BeanProperty prop, Nulls nulls, JsonDeserializer<?> valueDeser) throws JsonMappingException {
/* 1122 */     if (nulls == Nulls.FAIL) {
/* 1123 */       if (prop == null) {
/* 1124 */         return (NullValueProvider)NullsFailProvider.constructForRootValue(ctxt.constructType(valueDeser.handledType()));
/*      */       }
/* 1126 */       return (NullValueProvider)NullsFailProvider.constructForProperty(prop);
/*      */     } 
/* 1128 */     if (nulls == Nulls.AS_EMPTY) {
/*      */ 
/*      */       
/* 1131 */       if (valueDeser == null) {
/* 1132 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1138 */       if (valueDeser instanceof BeanDeserializerBase) {
/* 1139 */         ValueInstantiator vi = ((BeanDeserializerBase)valueDeser).getValueInstantiator();
/* 1140 */         if (!vi.canCreateUsingDefault()) {
/* 1141 */           JavaType type = prop.getType();
/* 1142 */           ctxt.reportBadDefinition(type, 
/* 1143 */               String.format("Cannot create empty instance of %s, no default Creator", new Object[] { type }));
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1148 */       AccessPattern access = valueDeser.getEmptyAccessPattern();
/* 1149 */       if (access == AccessPattern.ALWAYS_NULL) {
/* 1150 */         return (NullValueProvider)NullsConstantProvider.nuller();
/*      */       }
/* 1152 */       if (access == AccessPattern.CONSTANT) {
/* 1153 */         return (NullValueProvider)NullsConstantProvider.forValue(valueDeser.getEmptyValue(ctxt));
/*      */       }
/*      */       
/* 1156 */       return (NullValueProvider)new NullsAsEmptyProvider(valueDeser);
/*      */     } 
/* 1158 */     if (nulls == Nulls.SKIP) {
/* 1159 */       return (NullValueProvider)NullsConstantProvider.skipper();
/*      */     }
/* 1161 */     return null;
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
/*      */   protected void handleUnknownProperty(JsonParser p, DeserializationContext ctxt, Object<?> instanceOrClass, String propName) throws IOException {
/* 1188 */     if (instanceOrClass == null) {
/* 1189 */       instanceOrClass = (Object<?>)handledType();
/*      */     }
/*      */     
/* 1192 */     if (ctxt.handleUnknownProperty(p, this, instanceOrClass, propName)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1198 */     p.skipChildren();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void handleMissingEndArrayForSingle(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1204 */     ctxt.reportWrongTokenException(this, JsonToken.END_ARRAY, "Attempted to unwrap '%s' value from an array (with `DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS`) but it contains more than one value", new Object[] {
/*      */           
/* 1206 */           handledType().getName()
/*      */         });
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void _verifyEndArrayForSingle(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 1213 */     JsonToken t = p.nextToken();
/* 1214 */     if (t != JsonToken.END_ARRAY) {
/* 1215 */       handleMissingEndArrayForSingle(p, ctxt);
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
/*      */   protected static final boolean _neitherNull(Object a, Object b) {
/* 1229 */     return (a != null && b != null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _byteOverflow(int value) {
/* 1238 */     return (value < -128 || value > 255);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _shortOverflow(int value) {
/* 1245 */     return (value < -32768 || value > 32767);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean _intOverflow(long value) {
/* 1252 */     return (value < -2147483648L || value > 2147483647L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Number _nonNullNumber(Number n) {
/* 1259 */     if (n == null) {
/* 1260 */       n = Integer.valueOf(0);
/*      */     }
/* 1262 */     return n;
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\deser\std\StdDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */