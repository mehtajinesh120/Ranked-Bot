/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AtomicReferenceDeserializer
/*    */   extends ReferenceTypeDeserializer<AtomicReference<Object>>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public AtomicReferenceDeserializer(JavaType fullType, ValueInstantiator inst, TypeDeserializer typeDeser, JsonDeserializer<?> deser) {
/* 26 */     super(fullType, inst, typeDeser, deser);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AtomicReferenceDeserializer withResolved(TypeDeserializer typeDeser, JsonDeserializer<?> valueDeser) {
/* 37 */     return new AtomicReferenceDeserializer(this._fullType, this._valueInstantiator, typeDeser, valueDeser);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AtomicReference<Object> getNullValue(DeserializationContext ctxt) throws JsonMappingException {
/* 44 */     return new AtomicReference(this._valueDeserializer.getNullValue(ctxt));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException {
/* 52 */     return getNullValue(ctxt);
/*    */   }
/*    */ 
/*    */   
/*    */   public AtomicReference<Object> referenceValue(Object contents) {
/* 57 */     return new AtomicReference(contents);
/*    */   }
/*    */ 
/*    */   
/*    */   public Object getReferenced(AtomicReference<Object> reference) {
/* 62 */     return reference.get();
/*    */   }
/*    */ 
/*    */   
/*    */   public AtomicReference<Object> updateReference(AtomicReference<Object> reference, Object contents) {
/* 67 */     reference.set(contents);
/* 68 */     return reference;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Boolean supportsUpdate(DeserializationConfig config) {
/* 74 */     return Boolean.TRUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\deser\std\AtomicReferenceDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */