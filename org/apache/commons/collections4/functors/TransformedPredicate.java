/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.collections4.Predicate;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TransformedPredicate<T>
/*     */   implements PredicateDecorator<T>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -5596090919668315834L;
/*     */   private final Transformer<? super T, ? extends T> iTransformer;
/*     */   private final Predicate<? super T> iPredicate;
/*     */   
/*     */   public static <T> Predicate<T> transformedPredicate(Transformer<? super T, ? extends T> transformer, Predicate<? super T> predicate) {
/*  53 */     if (transformer == null) {
/*  54 */       throw new NullPointerException("The transformer to call must not be null");
/*     */     }
/*  56 */     if (predicate == null) {
/*  57 */       throw new NullPointerException("The predicate to call must not be null");
/*     */     }
/*  59 */     return new TransformedPredicate<T>(transformer, predicate);
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
/*     */   public TransformedPredicate(Transformer<? super T, ? extends T> transformer, Predicate<? super T> predicate) {
/*  71 */     this.iTransformer = transformer;
/*  72 */     this.iPredicate = predicate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean evaluate(T object) {
/*  83 */     T result = (T)this.iTransformer.transform(object);
/*  84 */     return this.iPredicate.evaluate(result);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Predicate<? super T>[] getPredicates() {
/*  95 */     return (Predicate<? super T>[])new Predicate[] { this.iPredicate };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transformer<? super T, ? extends T> getTransformer() {
/* 104 */     return this.iTransformer;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\TransformedPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */