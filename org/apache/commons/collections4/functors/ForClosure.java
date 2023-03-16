/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import org.apache.commons.collections4.Closure;
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
/*     */ public class ForClosure<E>
/*     */   implements Closure<E>
/*     */ {
/*     */   private final int iCount;
/*     */   private final Closure<? super E> iClosure;
/*     */   
/*     */   public static <E> Closure<E> forClosure(int count, Closure<? super E> closure) {
/*  52 */     if (count <= 0 || closure == null) {
/*  53 */       return NOPClosure.nopClosure();
/*     */     }
/*  55 */     if (count == 1) {
/*  56 */       return (Closure)closure;
/*     */     }
/*  58 */     return new ForClosure<E>(count, closure);
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
/*     */   public ForClosure(int count, Closure<? super E> closure) {
/*  70 */     this.iCount = count;
/*  71 */     this.iClosure = closure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(E input) {
/*  81 */     for (int i = 0; i < this.iCount; i++) {
/*  82 */       this.iClosure.execute(input);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Closure<? super E> getClosure() {
/*  93 */     return this.iClosure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCount() {
/* 103 */     return this.iCount;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\ForClosure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */