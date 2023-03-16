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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IfTransformer<I, O>
/*     */   implements Transformer<I, O>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8069309411242014252L;
/*     */   private final Predicate<? super I> iPredicate;
/*     */   private final Transformer<? super I, ? extends O> iTrueTransformer;
/*     */   private final Transformer<? super I, ? extends O> iFalseTransformer;
/*     */   
/*     */   public static <I, O> Transformer<I, O> ifTransformer(Predicate<? super I> predicate, Transformer<? super I, ? extends O> trueTransformer, Transformer<? super I, ? extends O> falseTransformer) {
/*  60 */     if (predicate == null) {
/*  61 */       throw new NullPointerException("Predicate must not be null");
/*     */     }
/*  63 */     if (trueTransformer == null || falseTransformer == null) {
/*  64 */       throw new NullPointerException("Transformers must not be null");
/*     */     }
/*     */     
/*  67 */     return new IfTransformer<I, O>(predicate, trueTransformer, falseTransformer);
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
/*     */   
/*     */   public static <T> Transformer<T, T> ifTransformer(Predicate<? super T> predicate, Transformer<? super T, ? extends T> trueTransformer) {
/*  86 */     if (predicate == null) {
/*  87 */       throw new NullPointerException("Predicate must not be null");
/*     */     }
/*  89 */     if (trueTransformer == null) {
/*  90 */       throw new NullPointerException("Transformer must not be null");
/*     */     }
/*     */     
/*  93 */     return new IfTransformer<T, T>(predicate, trueTransformer, (Transformer)NOPTransformer.nopTransformer());
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
/*     */   public IfTransformer(Predicate<? super I> predicate, Transformer<? super I, ? extends O> trueTransformer, Transformer<? super I, ? extends O> falseTransformer) {
/* 109 */     this.iPredicate = predicate;
/* 110 */     this.iTrueTransformer = trueTransformer;
/* 111 */     this.iFalseTransformer = falseTransformer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public O transform(I input) {
/* 121 */     if (this.iPredicate.evaluate(input)) {
/* 122 */       return (O)this.iTrueTransformer.transform(input);
/*     */     }
/* 124 */     return (O)this.iFalseTransformer.transform(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Predicate<? super I> getPredicate() {
/* 134 */     return this.iPredicate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transformer<? super I, ? extends O> getTrueTransformer() {
/* 143 */     return this.iTrueTransformer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transformer<? super I, ? extends O> getFalseTransformer() {
/* 152 */     return this.iFalseTransformer;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\IfTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */