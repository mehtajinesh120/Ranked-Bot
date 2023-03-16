/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.collections4.Closure;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IfClosure<E>
/*     */   implements Closure<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3518477308466486130L;
/*     */   private final Predicate<? super E> iPredicate;
/*     */   private final Closure<? super E> iTrueClosure;
/*     */   private final Closure<? super E> iFalseClosure;
/*     */   
/*     */   public static <E> Closure<E> ifClosure(Predicate<? super E> predicate, Closure<? super E> trueClosure) {
/*  57 */     return ifClosure(predicate, trueClosure, NOPClosure.nopClosure());
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
/*     */   public static <E> Closure<E> ifClosure(Predicate<? super E> predicate, Closure<? super E> trueClosure, Closure<? super E> falseClosure) {
/*  73 */     if (predicate == null) {
/*  74 */       throw new NullPointerException("Predicate must not be null");
/*     */     }
/*  76 */     if (trueClosure == null || falseClosure == null) {
/*  77 */       throw new NullPointerException("Closures must not be null");
/*     */     }
/*  79 */     return new IfClosure<E>(predicate, trueClosure, falseClosure);
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
/*     */   public IfClosure(Predicate<? super E> predicate, Closure<? super E> trueClosure) {
/*  94 */     this(predicate, trueClosure, NOPClosure.nopClosure());
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
/*     */   public IfClosure(Predicate<? super E> predicate, Closure<? super E> trueClosure, Closure<? super E> falseClosure) {
/* 108 */     this.iPredicate = predicate;
/* 109 */     this.iTrueClosure = trueClosure;
/* 110 */     this.iFalseClosure = falseClosure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(E input) {
/* 119 */     if (this.iPredicate.evaluate(input)) {
/* 120 */       this.iTrueClosure.execute(input);
/*     */     } else {
/* 122 */       this.iFalseClosure.execute(input);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Predicate<? super E> getPredicate() {
/* 133 */     return this.iPredicate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Closure<? super E> getTrueClosure() {
/* 143 */     return this.iTrueClosure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Closure<? super E> getFalseClosure() {
/* 153 */     return this.iFalseClosure;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\IfClosure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */