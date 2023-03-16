/*     */ package org.apache.commons.collections4.functors;
/*     */ 
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
/*     */ public class WhileClosure<E>
/*     */   implements Closure<E>
/*     */ {
/*     */   private final Predicate<? super E> iPredicate;
/*     */   private final Closure<? super E> iClosure;
/*     */   private final boolean iDoLoop;
/*     */   
/*     */   public static <E> Closure<E> whileClosure(Predicate<? super E> predicate, Closure<? super E> closure, boolean doLoop) {
/*  55 */     if (predicate == null) {
/*  56 */       throw new NullPointerException("Predicate must not be null");
/*     */     }
/*  58 */     if (closure == null) {
/*  59 */       throw new NullPointerException("Closure must not be null");
/*     */     }
/*  61 */     return new WhileClosure<E>(predicate, closure, doLoop);
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
/*     */   public WhileClosure(Predicate<? super E> predicate, Closure<? super E> closure, boolean doLoop) {
/*  74 */     this.iPredicate = predicate;
/*  75 */     this.iClosure = closure;
/*  76 */     this.iDoLoop = doLoop;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(E input) {
/*  86 */     if (this.iDoLoop) {
/*  87 */       this.iClosure.execute(input);
/*     */     }
/*  89 */     while (this.iPredicate.evaluate(input)) {
/*  90 */       this.iClosure.execute(input);
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
/* 101 */     return this.iPredicate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Closure<? super E> getClosure() {
/* 111 */     return this.iClosure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDoLoop() {
/* 121 */     return this.iDoLoop;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\WhileClosure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */