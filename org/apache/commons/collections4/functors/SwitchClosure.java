/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ public class SwitchClosure<E>
/*     */   implements Closure<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3518477308466486130L;
/*     */   private final Predicate<? super E>[] iPredicates;
/*     */   private final Closure<? super E>[] iClosures;
/*     */   private final Closure<? super E> iDefault;
/*     */   
/*     */   public static <E> Closure<E> switchClosure(Predicate<? super E>[] predicates, Closure<? super E>[] closures, Closure<? super E> defaultClosure) {
/*  60 */     FunctorUtils.validate((Predicate<?>[])predicates);
/*  61 */     FunctorUtils.validate((Closure<?>[])closures);
/*  62 */     if (predicates.length != closures.length) {
/*  63 */       throw new IllegalArgumentException("The predicate and closure arrays must be the same size");
/*     */     }
/*  65 */     if (predicates.length == 0) {
/*  66 */       return (defaultClosure == null) ? NOPClosure.<E>nopClosure() : (Closure)defaultClosure;
/*     */     }
/*  68 */     return new SwitchClosure<E>(predicates, closures, defaultClosure);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Closure<E> switchClosure(Map<Predicate<E>, Closure<E>> predicatesAndClosures) {
/*  91 */     if (predicatesAndClosures == null) {
/*  92 */       throw new NullPointerException("The predicate and closure map must not be null");
/*     */     }
/*     */     
/*  95 */     Closure<? super E> defaultClosure = predicatesAndClosures.remove(null);
/*  96 */     int size = predicatesAndClosures.size();
/*  97 */     if (size == 0) {
/*  98 */       return (defaultClosure == null) ? NOPClosure.<E>nopClosure() : (Closure)defaultClosure;
/*     */     }
/* 100 */     Closure[] arrayOfClosure = new Closure[size];
/* 101 */     Predicate[] arrayOfPredicate = new Predicate[size];
/* 102 */     int i = 0;
/* 103 */     for (Map.Entry<Predicate<E>, Closure<E>> entry : predicatesAndClosures.entrySet()) {
/* 104 */       arrayOfPredicate[i] = entry.getKey();
/* 105 */       arrayOfClosure[i] = entry.getValue();
/* 106 */       i++;
/*     */     } 
/* 108 */     return new SwitchClosure<E>(false, (Predicate<? super E>[])arrayOfPredicate, (Closure<? super E>[])arrayOfClosure, defaultClosure);
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
/*     */   private SwitchClosure(boolean clone, Predicate<? super E>[] predicates, Closure<? super E>[] closures, Closure<? super E> defaultClosure) {
/* 123 */     this.iPredicates = clone ? (Predicate<? super E>[])FunctorUtils.<E>copy(predicates) : predicates;
/* 124 */     this.iClosures = clone ? (Closure<? super E>[])FunctorUtils.<E>copy(closures) : closures;
/* 125 */     this.iDefault = (defaultClosure == null) ? NOPClosure.<E>nopClosure() : defaultClosure;
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
/*     */   public SwitchClosure(Predicate<? super E>[] predicates, Closure<? super E>[] closures, Closure<? super E> defaultClosure) {
/* 138 */     this(true, predicates, closures, defaultClosure);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(E input) {
/* 147 */     for (int i = 0; i < this.iPredicates.length; i++) {
/* 148 */       if (this.iPredicates[i].evaluate(input) == true) {
/* 149 */         this.iClosures[i].execute(input);
/*     */         return;
/*     */       } 
/*     */     } 
/* 153 */     this.iDefault.execute(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Predicate<? super E>[] getPredicates() {
/* 163 */     return (Predicate<? super E>[])FunctorUtils.copy(this.iPredicates);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Closure<? super E>[] getClosures() {
/* 173 */     return (Closure<? super E>[])FunctorUtils.copy(this.iClosures);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Closure<? super E> getDefaultClosure() {
/* 183 */     return this.iDefault;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\SwitchClosure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */