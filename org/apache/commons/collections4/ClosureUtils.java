/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.functors.ChainedClosure;
/*     */ import org.apache.commons.collections4.functors.EqualPredicate;
/*     */ import org.apache.commons.collections4.functors.ExceptionClosure;
/*     */ import org.apache.commons.collections4.functors.ForClosure;
/*     */ import org.apache.commons.collections4.functors.IfClosure;
/*     */ import org.apache.commons.collections4.functors.InvokerTransformer;
/*     */ import org.apache.commons.collections4.functors.NOPClosure;
/*     */ import org.apache.commons.collections4.functors.SwitchClosure;
/*     */ import org.apache.commons.collections4.functors.TransformerClosure;
/*     */ import org.apache.commons.collections4.functors.WhileClosure;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClosureUtils
/*     */ {
/*     */   public static <E> Closure<E> exceptionClosure() {
/*  77 */     return ExceptionClosure.exceptionClosure();
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
/*     */   public static <E> Closure<E> nopClosure() {
/*  90 */     return NOPClosure.nopClosure();
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
/*     */   public static <E> Closure<E> asClosure(Transformer<? super E, ?> transformer) {
/* 105 */     return TransformerClosure.transformerClosure(transformer);
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
/*     */   public static <E> Closure<E> forClosure(int count, Closure<? super E> closure) {
/* 121 */     return ForClosure.forClosure(count, closure);
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
/*     */   public static <E> Closure<E> whileClosure(Predicate<? super E> predicate, Closure<? super E> closure) {
/* 137 */     return WhileClosure.whileClosure(predicate, closure, false);
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
/*     */   public static <E> Closure<E> doWhileClosure(Closure<? super E> closure, Predicate<? super E> predicate) {
/* 154 */     return WhileClosure.whileClosure(predicate, closure, true);
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
/*     */   public static <E> Closure<E> invokerClosure(String methodName) {
/* 171 */     return asClosure(InvokerTransformer.invokerTransformer(methodName));
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
/*     */   public static <E> Closure<E> invokerClosure(String methodName, Class<?>[] paramTypes, Object[] args) {
/* 192 */     return asClosure(InvokerTransformer.invokerTransformer(methodName, paramTypes, args));
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
/*     */   public static <E> Closure<E> chainedClosure(Closure<? super E>... closures) {
/* 208 */     return ChainedClosure.chainedClosure((Closure[])closures);
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
/*     */   public static <E> Closure<E> chainedClosure(Collection<? extends Closure<? super E>> closures) {
/* 226 */     return ChainedClosure.chainedClosure(closures);
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
/*     */   public static <E> Closure<E> ifClosure(Predicate<? super E> predicate, Closure<? super E> trueClosure) {
/* 244 */     return IfClosure.ifClosure(predicate, trueClosure);
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
/*     */   public static <E> Closure<E> ifClosure(Predicate<? super E> predicate, Closure<? super E> trueClosure, Closure<? super E> falseClosure) {
/* 263 */     return IfClosure.ifClosure(predicate, trueClosure, falseClosure);
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
/*     */   public static <E> Closure<E> switchClosure(Predicate<? super E>[] predicates, Closure<? super E>[] closures) {
/* 286 */     return SwitchClosure.switchClosure((Predicate[])predicates, (Closure[])closures, null);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Closure<E> switchClosure(Predicate<? super E>[] predicates, Closure<? super E>[] closures, Closure<? super E> defaultClosure) {
/* 312 */     return SwitchClosure.switchClosure((Predicate[])predicates, (Closure[])closures, defaultClosure);
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
/*     */ 
/*     */   
/*     */   public static <E> Closure<E> switchClosure(Map<Predicate<E>, Closure<E>> predicatesAndClosures) {
/* 337 */     return SwitchClosure.switchClosure(predicatesAndClosures);
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
/*     */   public static <E> Closure<E> switchMapClosure(Map<? extends E, Closure<E>> objectsAndClosures) {
/* 360 */     if (objectsAndClosures == null) {
/* 361 */       throw new NullPointerException("The object and closure map must not be null");
/*     */     }
/* 363 */     Closure<? super E> def = objectsAndClosures.remove(null);
/* 364 */     int size = objectsAndClosures.size();
/* 365 */     Closure[] arrayOfClosure = new Closure[size];
/* 366 */     Predicate[] arrayOfPredicate = new Predicate[size];
/* 367 */     int i = 0;
/* 368 */     for (Map.Entry<? extends E, Closure<E>> entry : objectsAndClosures.entrySet()) {
/* 369 */       arrayOfPredicate[i] = EqualPredicate.equalPredicate(entry.getKey());
/* 370 */       arrayOfClosure[i] = entry.getValue();
/* 371 */       i++;
/*     */     } 
/* 373 */     return switchClosure((Predicate<? super E>[])arrayOfPredicate, (Closure<? super E>[])arrayOfClosure, def);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\ClosureUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */