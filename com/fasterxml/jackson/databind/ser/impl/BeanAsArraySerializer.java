/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanAsArraySerializer
/*     */   extends BeanSerializerBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final BeanSerializerBase _defaultSerializer;
/*     */   
/*     */   public BeanAsArraySerializer(BeanSerializerBase src) {
/*  64 */     super(src, (ObjectIdWriter)null);
/*  65 */     this._defaultSerializer = src;
/*     */   }
/*     */   
/*     */   protected BeanAsArraySerializer(BeanSerializerBase src, Set<String> toIgnore) {
/*  69 */     super(src, toIgnore);
/*  70 */     this._defaultSerializer = src;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanAsArraySerializer(BeanSerializerBase src, ObjectIdWriter oiw, Object filterId) {
/*  75 */     super(src, oiw, filterId);
/*  76 */     this._defaultSerializer = src;
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
/*     */   public JsonSerializer<Object> unwrappingSerializer(NameTransformer transformer) {
/*  90 */     return this._defaultSerializer.unwrappingSerializer(transformer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnwrappingSerializer() {
/*  95 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanSerializerBase withObjectIdWriter(ObjectIdWriter objectIdWriter) {
/* 101 */     return this._defaultSerializer.withObjectIdWriter(objectIdWriter);
/*     */   }
/*     */ 
/*     */   
/*     */   public BeanSerializerBase withFilterId(Object filterId) {
/* 106 */     return new BeanAsArraySerializer(this, this._objectIdWriter, filterId);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanAsArraySerializer withIgnorals(Set<String> toIgnore) {
/* 111 */     return new BeanAsArraySerializer(this, toIgnore);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase asArraySerializer() {
/* 117 */     return this;
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
/*     */ 
/*     */   
/*     */   public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 135 */     if (this._objectIdWriter != null) {
/* 136 */       _serializeWithObjectId(bean, gen, provider, typeSer);
/*     */       return;
/*     */     } 
/* 139 */     WritableTypeId typeIdDef = _typeIdDef(typeSer, bean, JsonToken.START_ARRAY);
/* 140 */     typeSer.writeTypePrefix(gen, typeIdDef);
/* 141 */     gen.setCurrentValue(bean);
/* 142 */     serializeAsArray(bean, gen, provider);
/* 143 */     typeSer.writeTypeSuffix(gen, typeIdDef);
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
/*     */   public final void serialize(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
/* 155 */     if (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED) && 
/* 156 */       hasSingleElement(provider)) {
/* 157 */       serializeAsArray(bean, gen, provider);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 164 */     gen.writeStartArray(bean);
/* 165 */     serializeAsArray(bean, gen, provider);
/* 166 */     gen.writeEndArray();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasSingleElement(SerializerProvider provider) {
/*     */     BeanPropertyWriter[] props;
/* 177 */     if (this._filteredProps != null && provider.getActiveView() != null) {
/* 178 */       props = this._filteredProps;
/*     */     } else {
/* 180 */       props = this._props;
/*     */     } 
/* 182 */     return (props.length == 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void serializeAsArray(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
/*     */     BeanPropertyWriter[] props;
/* 189 */     if (this._filteredProps != null && provider.getActiveView() != null) {
/* 190 */       props = this._filteredProps;
/*     */     } else {
/* 192 */       props = this._props;
/*     */     } 
/*     */     
/* 195 */     int i = 0;
/*     */     try {
/* 197 */       for (int len = props.length; i < len; i++) {
/* 198 */         BeanPropertyWriter prop = props[i];
/* 199 */         if (prop == null) {
/* 200 */           gen.writeNull();
/*     */         } else {
/* 202 */           prop.serializeAsElement(bean, gen, provider);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 209 */     catch (Exception e) {
/* 210 */       String name = (i == props.length) ? "[anySetter]" : props[i].getName();
/* 211 */       wrapAndThrow(provider, e, bean, name);
/* 212 */     } catch (StackOverflowError e) {
/* 213 */       JsonMappingException mapE = JsonMappingException.from(gen, "Infinite recursion (StackOverflowError)", e);
/* 214 */       String name = (i == props.length) ? "[anySetter]" : props[i].getName();
/* 215 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 216 */       throw mapE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 227 */     return "BeanAsArraySerializer for " + handledType().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\ser\impl\BeanAsArraySerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */