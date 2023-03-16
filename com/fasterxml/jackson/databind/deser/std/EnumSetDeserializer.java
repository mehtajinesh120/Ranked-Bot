/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.NullValueProvider;
/*     */ import com.fasterxml.jackson.databind.deser.impl.NullsConstantProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.AccessPattern;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.util.EnumSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnumSetDeserializer
/*     */   extends StdDeserializer<EnumSet<?>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _enumType;
/*     */   protected final Class<Enum> _enumClass;
/*     */   protected JsonDeserializer<Enum<?>> _enumDeserializer;
/*     */   protected final NullValueProvider _nullProvider;
/*     */   protected final boolean _skipNullValues;
/*     */   protected final Boolean _unwrapSingle;
/*     */   
/*     */   public EnumSetDeserializer(JavaType enumType, JsonDeserializer<?> deser) {
/*  68 */     super(EnumSet.class);
/*  69 */     this._enumType = enumType;
/*  70 */     this._enumClass = enumType.getRawClass();
/*     */     
/*  72 */     if (!ClassUtil.isEnumType(this._enumClass)) {
/*  73 */       throw new IllegalArgumentException("Type " + enumType + " not Java Enum type");
/*     */     }
/*  75 */     this._enumDeserializer = (JsonDeserializer)deser;
/*  76 */     this._unwrapSingle = null;
/*  77 */     this._nullProvider = null;
/*  78 */     this._skipNullValues = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected EnumSetDeserializer(EnumSetDeserializer base, JsonDeserializer<?> deser, Boolean unwrapSingle) {
/*  88 */     this(base, deser, base._nullProvider, unwrapSingle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EnumSetDeserializer(EnumSetDeserializer base, JsonDeserializer<?> deser, NullValueProvider nuller, Boolean unwrapSingle) {
/*  97 */     super(base);
/*  98 */     this._enumType = base._enumType;
/*  99 */     this._enumClass = base._enumClass;
/* 100 */     this._enumDeserializer = (JsonDeserializer)deser;
/* 101 */     this._nullProvider = nuller;
/* 102 */     this._skipNullValues = NullsConstantProvider.isSkipper(nuller);
/* 103 */     this._unwrapSingle = unwrapSingle;
/*     */   }
/*     */   
/*     */   public EnumSetDeserializer withDeserializer(JsonDeserializer<?> deser) {
/* 107 */     if (this._enumDeserializer == deser) {
/* 108 */       return this;
/*     */     }
/* 110 */     return new EnumSetDeserializer(this, deser, this._nullProvider, this._unwrapSingle);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public EnumSetDeserializer withResolved(JsonDeserializer<?> deser, Boolean unwrapSingle) {
/* 115 */     return withResolved(deser, this._nullProvider, unwrapSingle);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumSetDeserializer withResolved(JsonDeserializer<?> deser, NullValueProvider nuller, Boolean unwrapSingle) {
/* 123 */     if (this._unwrapSingle == unwrapSingle && this._enumDeserializer == deser && this._nullProvider == deser) {
/* 124 */       return this;
/*     */     }
/* 126 */     return new EnumSetDeserializer(this, deser, nuller, unwrapSingle);
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
/*     */   public boolean isCachable() {
/* 142 */     if (this._enumType.getValueHandler() != null) {
/* 143 */       return false;
/*     */     }
/* 145 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean supportsUpdate(DeserializationConfig config) {
/* 150 */     return Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
/* 155 */     return constructSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public AccessPattern getEmptyAccessPattern() {
/* 160 */     return AccessPattern.DYNAMIC;
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
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) throws JsonMappingException {
/* 173 */     Boolean unwrapSingle = findFormatFeature(ctxt, property, EnumSet.class, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
/*     */     
/* 175 */     JsonDeserializer<?> deser = this._enumDeserializer;
/* 176 */     if (deser == null) {
/* 177 */       deser = ctxt.findContextualValueDeserializer(this._enumType, property);
/*     */     } else {
/* 179 */       deser = ctxt.handleSecondaryContextualization(deser, property, this._enumType);
/*     */     } 
/* 181 */     return withResolved(deser, findContentNullProvider(ctxt, property, deser), unwrapSingle);
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
/*     */   public EnumSet<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 193 */     EnumSet result = constructSet();
/*     */     
/* 195 */     if (!p.isExpectedStartArrayToken()) {
/* 196 */       return handleNonArray(p, ctxt, result);
/*     */     }
/* 198 */     return _deserialize(p, ctxt, result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumSet<?> deserialize(JsonParser p, DeserializationContext ctxt, EnumSet<?> result) throws IOException {
/* 206 */     if (!p.isExpectedStartArrayToken()) {
/* 207 */       return handleNonArray(p, ctxt, result);
/*     */     }
/* 209 */     return _deserialize(p, ctxt, result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final EnumSet<?> _deserialize(JsonParser p, DeserializationContext ctxt, EnumSet<Enum<?>> result) throws IOException {
/*     */     try {
/*     */       JsonToken t;
/* 219 */       while ((t = p.nextToken()) != JsonToken.END_ARRAY) {
/*     */         Enum<?> value;
/*     */ 
/*     */ 
/*     */         
/* 224 */         if (t == JsonToken.VALUE_NULL) {
/* 225 */           if (this._skipNullValues) {
/*     */             continue;
/*     */           }
/* 228 */           value = (Enum)this._nullProvider.getNullValue(ctxt);
/*     */         } else {
/* 230 */           value = (Enum)this._enumDeserializer.deserialize(p, ctxt);
/*     */         } 
/* 232 */         if (value != null) {
/* 233 */           result.add(value);
/*     */         }
/*     */       } 
/* 236 */     } catch (Exception e) {
/* 237 */       throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */     } 
/* 239 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException, JsonProcessingException {
/* 247 */     return typeDeserializer.deserializeTypedFromArray(p, ctxt);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private EnumSet constructSet() {
/* 253 */     return EnumSet.noneOf(this._enumClass);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected EnumSet<?> handleNonArray(JsonParser p, DeserializationContext ctxt, EnumSet<Enum<?>> result) throws IOException {
/* 263 */     boolean canWrap = (this._unwrapSingle == Boolean.TRUE || (this._unwrapSingle == null && ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)));
/*     */     
/* 265 */     if (!canWrap) {
/* 266 */       return (EnumSet)ctxt.handleUnexpectedToken(EnumSet.class, p);
/*     */     }
/*     */     
/* 269 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/* 270 */       return (EnumSet)ctxt.handleUnexpectedToken(this._enumClass, p);
/*     */     }
/*     */     try {
/* 273 */       Enum<?> value = (Enum)this._enumDeserializer.deserialize(p, ctxt);
/* 274 */       if (value != null) {
/* 275 */         result.add(value);
/*     */       }
/* 277 */     } catch (Exception e) {
/* 278 */       throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */     } 
/* 280 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\deser\std\EnumSetDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */