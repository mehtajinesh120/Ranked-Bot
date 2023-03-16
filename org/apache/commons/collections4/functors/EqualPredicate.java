/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.collections4.Equator;
/*     */ import org.apache.commons.collections4.Predicate;
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
/*     */ public final class EqualPredicate<T>
/*     */   implements Predicate<T>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5633766978029907089L;
/*     */   private final T iValue;
/*     */   private final Equator<T> equator;
/*     */   
/*     */   public static <T> Predicate<T> equalPredicate(T object) {
/*  50 */     if (object == null) {
/*  51 */       return NullPredicate.nullPredicate();
/*     */     }
/*  53 */     return new EqualPredicate<T>(object);
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
/*     */   public static <T> Predicate<T> equalPredicate(T object, Equator<T> equator) {
/*  66 */     if (object == null) {
/*  67 */       return NullPredicate.nullPredicate();
/*     */     }
/*  69 */     return new EqualPredicate<T>(object, equator);
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
/*     */   public EqualPredicate(T object) {
/*  81 */     this(object, null);
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
/*     */   public EqualPredicate(T object, Equator<T> equator) {
/*  94 */     this.iValue = object;
/*  95 */     this.equator = equator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean evaluate(T object) {
/* 105 */     if (this.equator != null) {
/* 106 */       return this.equator.equate(this.iValue, object);
/*     */     }
/* 108 */     return this.iValue.equals(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getValue() {
/* 119 */     return this.iValue;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\EqualPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */