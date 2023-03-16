/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
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
/*     */ public class SettableAnyProperty
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final BeanProperty _property;
/*     */   protected final AnnotatedMember _setter;
/*     */   final boolean _setterIsField;
/*     */   protected final JavaType _type;
/*     */   protected JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   protected final KeyDeserializer _keyDeserializer;
/*     */   
/*     */   public SettableAnyProperty(BeanProperty property, AnnotatedMember setter, JavaType type, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer typeDeser) {
/*  62 */     this._property = property;
/*  63 */     this._setter = setter;
/*  64 */     this._type = type;
/*  65 */     this._valueDeserializer = valueDeser;
/*  66 */     this._valueTypeDeserializer = typeDeser;
/*  67 */     this._keyDeserializer = keyDeser;
/*  68 */     this._setterIsField = setter instanceof AnnotatedField;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public SettableAnyProperty(BeanProperty property, AnnotatedMember setter, JavaType type, JsonDeserializer<Object> valueDeser, TypeDeserializer typeDeser) {
/*  75 */     this(property, setter, type, null, valueDeser, typeDeser);
/*     */   }
/*     */   
/*     */   public SettableAnyProperty withValueDeserializer(JsonDeserializer<Object> deser) {
/*  79 */     return new SettableAnyProperty(this._property, this._setter, this._type, this._keyDeserializer, deser, this._valueTypeDeserializer);
/*     */   }
/*     */ 
/*     */   
/*     */   public void fixAccess(DeserializationConfig config) {
/*  84 */     this._setter.fixAccess(config
/*  85 */         .isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
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
/*     */   Object readResolve() {
/*  99 */     if (this._setter == null || this._setter.getAnnotated() == null) {
/* 100 */       throw new IllegalArgumentException("Missing method (broken JDK (de)serialization?)");
/*     */     }
/* 102 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanProperty getProperty() {
/* 111 */     return this._property;
/*     */   } public boolean hasValueDeserializer() {
/* 113 */     return (this._valueDeserializer != null);
/*     */   } public JavaType getType() {
/* 115 */     return this._type;
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
/*     */   public final void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance, String propName) throws IOException {
/*     */     try {
/* 133 */       Object key = (this._keyDeserializer == null) ? propName : this._keyDeserializer.deserializeKey(propName, ctxt);
/* 134 */       set(instance, key, deserialize(p, ctxt));
/* 135 */     } catch (UnresolvedForwardReference reference) {
/* 136 */       if (this._valueDeserializer.getObjectIdReader() == null) {
/* 137 */         throw JsonMappingException.from(p, "Unresolved forward reference but no identity info.", reference);
/*     */       }
/*     */       
/* 140 */       AnySetterReferring referring = new AnySetterReferring(this, reference, this._type.getRawClass(), instance, propName);
/* 141 */       reference.getRoid().appendReferring(referring);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
/* 147 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/* 148 */       return this._valueDeserializer.getNullValue(ctxt);
/*     */     }
/* 150 */     if (this._valueTypeDeserializer != null) {
/* 151 */       return this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
/*     */     }
/* 153 */     return this._valueDeserializer.deserialize(p, ctxt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void set(Object instance, Object propName, Object value) throws IOException {
/*     */     try {
/* 161 */       if (this._setterIsField) {
/* 162 */         AnnotatedField field = (AnnotatedField)this._setter;
/* 163 */         Map<Object, Object> val = (Map<Object, Object>)field.getValue(instance);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 169 */         if (val != null)
/*     */         {
/* 171 */           val.put(propName, value);
/*     */         }
/*     */       } else {
/*     */         
/* 175 */         ((AnnotatedMethod)this._setter).callOnWith(instance, new Object[] { propName, value });
/*     */       } 
/* 177 */     } catch (Exception e) {
/* 178 */       _throwAsIOE(e, propName, value);
/*     */     } 
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
/*     */   protected void _throwAsIOE(Exception e, Object propName, Object value) throws IOException {
/* 196 */     if (e instanceof IllegalArgumentException) {
/* 197 */       String actType = ClassUtil.classNameOf(value);
/* 198 */       StringBuilder msg = (new StringBuilder("Problem deserializing \"any\" property '")).append(propName);
/* 199 */       msg.append("' of class " + getClassName() + " (expected type: ").append(this._type);
/* 200 */       msg.append("; actual type: ").append(actType).append(")");
/* 201 */       String origMsg = ClassUtil.exceptionMessage(e);
/* 202 */       if (origMsg != null) {
/* 203 */         msg.append(", problem: ").append(origMsg);
/*     */       } else {
/* 205 */         msg.append(" (no error message provided)");
/*     */       } 
/* 207 */       throw new JsonMappingException(null, msg.toString(), e);
/*     */     } 
/* 209 */     ClassUtil.throwIfIOE(e);
/* 210 */     ClassUtil.throwIfRTE(e);
/*     */     
/* 212 */     Throwable t = ClassUtil.getRootCause(e);
/* 213 */     throw new JsonMappingException(null, ClassUtil.exceptionMessage(t), t);
/*     */   }
/*     */   private String getClassName() {
/* 216 */     return this._setter.getDeclaringClass().getName();
/*     */   } public String toString() {
/* 218 */     return "[any property on class " + getClassName() + "]";
/*     */   }
/*     */   
/*     */   private static class AnySetterReferring
/*     */     extends ReadableObjectId.Referring {
/*     */     private final SettableAnyProperty _parent;
/*     */     private final Object _pojo;
/*     */     private final String _propName;
/*     */     
/*     */     public AnySetterReferring(SettableAnyProperty parent, UnresolvedForwardReference reference, Class<?> type, Object instance, String propName) {
/* 228 */       super(reference, type);
/* 229 */       this._parent = parent;
/* 230 */       this._pojo = instance;
/* 231 */       this._propName = propName;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void handleResolvedForwardReference(Object id, Object value) throws IOException {
/* 238 */       if (!hasId(id)) {
/* 239 */         throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id.toString() + "] that wasn't previously registered.");
/*     */       }
/*     */       
/* 242 */       this._parent.set(this._pojo, this._propName, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\deser\SettableAnyProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */