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
/*     */ public class DoubleNode
/*     */   extends NumericNode
/*     */ {
/*     */   protected final double _value;
/*     */   
/*     */   public DoubleNode(double v) {
/*  28 */     this._value = v;
/*     */   } public static DoubleNode valueOf(double v) {
/*  30 */     return new DoubleNode(v);
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
/*  41 */     return JsonParser.NumberType.DOUBLE;
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
/*     */   public boolean isDouble() {
/*  53 */     return true;
/*     */   }
/*     */   public boolean canConvertToInt() {
/*  56 */     return (this._value >= -2.147483648E9D && this._value <= 2.147483647E9D);
/*     */   }
/*     */   public boolean canConvertToLong() {
/*  59 */     return (this._value >= -9.223372036854776E18D && this._value <= 9.223372036854776E18D);
/*     */   }
/*     */ 
/*     */   
/*     */   public Number numberValue() {
/*  64 */     return Double.valueOf(this._value);
/*     */   }
/*     */   
/*     */   public short shortValue() {
/*  68 */     return (short)(int)this._value;
/*     */   }
/*     */   public int intValue() {
/*  71 */     return (int)this._value;
/*     */   }
/*     */   public long longValue() {
/*  74 */     return (long)this._value;
/*     */   }
/*     */   public float floatValue() {
/*  77 */     return (float)this._value;
/*     */   }
/*     */   public double doubleValue() {
/*  80 */     return this._value;
/*     */   }
/*     */   public BigDecimal decimalValue() {
/*  83 */     return BigDecimal.valueOf(this._value);
/*     */   }
/*     */   
/*     */   public BigInteger bigIntegerValue() {
/*  87 */     return decimalValue().toBigInteger();
/*     */   }
/*     */ 
/*     */   
/*     */   public String asText() {
/*  92 */     return NumberOutput.toString(this._value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isNaN() {
/*  98 */     return (Double.isNaN(this._value) || Double.isInfinite(this._value));
/*     */   }
/*     */ 
/*     */   
/*     */   public final void serialize(JsonGenerator g, SerializerProvider provider) throws IOException {
/* 103 */     g.writeNumber(this._value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 109 */     if (o == this) return true; 
/* 110 */     if (o == null) return false; 
/* 111 */     if (o instanceof DoubleNode) {
/*     */ 
/*     */       
/* 114 */       double otherValue = ((DoubleNode)o)._value;
/* 115 */       return (Double.compare(this._value, otherValue) == 0);
/*     */     } 
/* 117 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 124 */     long l = Double.doubleToLongBits(this._value);
/* 125 */     return (int)l ^ (int)(l >> 32L);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\node\DoubleNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */