/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.collections4.Transformer;
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
/*     */ public class ConstantTransformer<I, O>
/*     */   implements Transformer<I, O>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6374440726369055124L;
/*  40 */   public static final Transformer NULL_INSTANCE = new ConstantTransformer(null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final O iConstant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <I, O> Transformer<I, O> nullTransformer() {
/*  54 */     return NULL_INSTANCE;
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
/*     */   public static <I, O> Transformer<I, O> constantTransformer(O constantToReturn) {
/*  66 */     if (constantToReturn == null) {
/*  67 */       return nullTransformer();
/*     */     }
/*  69 */     return new ConstantTransformer<I, O>(constantToReturn);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ConstantTransformer(O constantToReturn) {
/*  80 */     this.iConstant = constantToReturn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public O transform(I input) {
/*  90 */     return this.iConstant;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public O getConstant() {
/* 100 */     return this.iConstant;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 108 */     if (obj == this) {
/* 109 */       return true;
/*     */     }
/* 111 */     if (!(obj instanceof ConstantTransformer)) {
/* 112 */       return false;
/*     */     }
/* 114 */     Object otherConstant = ((ConstantTransformer)obj).getConstant();
/* 115 */     return (otherConstant == getConstant() || (otherConstant != null && otherConstant.equals(getConstant())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 123 */     int result = "ConstantTransformer".hashCode() << 2;
/* 124 */     if (getConstant() != null) {
/* 125 */       result |= getConstant().hashCode();
/*     */     }
/* 127 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\ConstantTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */