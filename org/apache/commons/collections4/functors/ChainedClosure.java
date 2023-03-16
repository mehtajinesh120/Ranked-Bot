/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
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
/*     */ public class ChainedClosure<E>
/*     */   implements Closure<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -3520677225766901240L;
/*     */   private final Closure<? super E>[] iClosures;
/*     */   
/*     */   public static <E> Closure<E> chainedClosure(Closure<? super E>... closures) {
/*  48 */     FunctorUtils.validate((Closure<?>[])closures);
/*  49 */     if (closures.length == 0) {
/*  50 */       return NOPClosure.nopClosure();
/*     */     }
/*  52 */     return new ChainedClosure<E>(closures);
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
/*     */   public static <E> Closure<E> chainedClosure(Collection<? extends Closure<? super E>> closures) {
/*  68 */     if (closures == null) {
/*  69 */       throw new NullPointerException("Closure collection must not be null");
/*     */     }
/*  71 */     if (closures.size() == 0) {
/*  72 */       return NOPClosure.nopClosure();
/*     */     }
/*     */     
/*  75 */     Closure[] arrayOfClosure = new Closure[closures.size()];
/*  76 */     int i = 0;
/*  77 */     for (Closure<? super E> closure : closures) {
/*  78 */       arrayOfClosure[i++] = closure;
/*     */     }
/*  80 */     FunctorUtils.validate((Closure<?>[])arrayOfClosure);
/*  81 */     return new ChainedClosure<E>(false, (Closure<? super E>[])arrayOfClosure);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChainedClosure(boolean clone, Closure<? super E>... closures) {
/*  92 */     this.iClosures = clone ? (Closure<? super E>[])FunctorUtils.<E>copy(closures) : closures;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChainedClosure(Closure<? super E>... closures) {
/* 102 */     this(true, closures);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(E input) {
/* 111 */     for (Closure<? super E> iClosure : this.iClosures) {
/* 112 */       iClosure.execute(input);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Closure<? super E>[] getClosures() {
/* 123 */     return (Closure<? super E>[])FunctorUtils.copy(this.iClosures);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\ChainedClosure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */