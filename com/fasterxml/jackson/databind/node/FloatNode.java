/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.io.NumberOutput;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
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
/*     */ public class FloatNode
/*     */   extends NumericNode
/*     */ {
/*     */   protected final float _value;
/*     */   
/*     */   public FloatNode(float v) {
/*  28 */     this._value = v;
/*     */   } public static FloatNode valueOf(float v) {
/*  30 */     return new FloatNode(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonToken asToken() {
/*  38 */     return JsonToken.VALUE_NUMBER_FLOAT;
/*     */   }
/*     */   public JsonParser.NumberType numberType() {
/*  41 */     return JsonParser.NumberType.FLOAT;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFloatingPointNumber() {
/*  50 */     return true;
/*     */   }
/*     */   public boolean isFloat() {
/*  53 */     return true;
/*     */   }
/*     */   public boolean canConvertToInt() {
/*  56 */     return (this._value >= -2.14748365E9F && this._value <= 2.14748365E9F);
/*     */   }
/*     */   
/*     */   public boolean canConvertToLong() {
/*  60 */     return (this._value >= -9.223372E18F && this._value <= 9.223372E18F);
/*     */   }
/*     */ 
/*     */   
/*     */   public Number numberValue() {
/*  65 */     return Float.valueOf(this._value);
/*     */   }
/*     */   
/*     */   public short shortValue() {
/*  69 */     return (short)(int)this._value;
/*     */   }
/*     */   public int intValue() {
/*  72 */     return (int)this._value;
/*     */   }
/*     */   public long longValue() {
/*  75 */     return (long)this._value;
/*     */   }
/*     */   public float floatValue() {
/*  78 */     return this._value;
/*     */   }
/*     */   public double doubleValue() {
/*  81 */     return this._value;
/*     */   }
/*     */   public BigDecimal decimalValue() {
/*  84 */     return BigDecimal.valueOf(this._value);
/*     */   }
/*     */   
/*     */   public BigInteger bigIntegerValue() {
/*  88 */     return decimalValue().toBigInteger();
/*     */   }
/*     */ 
/*     */   
/*     */   public String asText() {
/*  93 */     return NumberOutput.toString(this._value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNaN() {
/*  99 */     return (Float.isNaN(this._value) || Float.isInfinite(this._value));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void serialize(JsonGenerator g, SerializerProvider provider) throws IOException {
/* 104 */     g.writeNumber(this._value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 110 */     if (o == this) return true; 
/* 111 */     if (o == null) return false; 
/* 112 */     if (o instanceof FloatNode) {
/*     */ 
/*     */       
/* 115 */       float otherValue = ((FloatNode)o)._value;
/* 116 */       return (Float.compare(this._value, otherValue) == 0);
/*     */     } 
/* 118 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 123 */     return Float.floatToIntBits(this._value);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\node\FloatNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */