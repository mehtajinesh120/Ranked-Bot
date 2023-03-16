/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.functors.ChainedTransformer;
/*     */ import org.apache.commons.collections4.functors.CloneTransformer;
/*     */ import org.apache.commons.collections4.functors.ClosureTransformer;
/*     */ import org.apache.commons.collections4.functors.ConstantTransformer;
/*     */ import org.apache.commons.collections4.functors.EqualPredicate;
/*     */ import org.apache.commons.collections4.functors.ExceptionTransformer;
/*     */ import org.apache.commons.collections4.functors.FactoryTransformer;
/*     */ import org.apache.commons.collections4.functors.IfTransformer;
/*     */ import org.apache.commons.collections4.functors.InstantiateTransformer;
/*     */ import org.apache.commons.collections4.functors.InvokerTransformer;
/*     */ import org.apache.commons.collections4.functors.MapTransformer;
/*     */ import org.apache.commons.collections4.functors.NOPTransformer;
/*     */ import org.apache.commons.collections4.functors.PredicateTransformer;
/*     */ import org.apache.commons.collections4.functors.StringValueTransformer;
/*     */ import org.apache.commons.collections4.functors.SwitchTransformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransformerUtils
/*     */ {
/*     */   public static <I, O> Transformer<I, O> exceptionTransformer() {
/*  88 */     return ExceptionTransformer.exceptionTransformer();
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
/*     */   public static <I, O> Transformer<I, O> nullTransformer() {
/* 100 */     return ConstantTransformer.nullTransformer();
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
/*     */   public static <T> Transformer<T, T> nopTransformer() {
/* 113 */     return NOPTransformer.nopTransformer();
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
/*     */   public static <T> Transformer<T, T> cloneTransformer() {
/* 130 */     return CloneTransformer.cloneTransformer();
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
/*     */   public static <I, O> Transformer<I, O> constantTransformer(O constantToReturn) {
/* 144 */     return ConstantTransformer.constantTransformer(constantToReturn);
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
/*     */   public static <T> Transformer<T, T> asTransformer(Closure<? super T> closure) {
/* 158 */     return ClosureTransformer.closureTransformer(closure);
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
/*     */   public static <T> Transformer<T, Boolean> asTransformer(Predicate<? super T> predicate) {
/* 172 */     return PredicateTransformer.predicateTransformer(predicate);
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
/*     */   public static <I, O> Transformer<I, O> asTransformer(Factory<? extends O> factory) {
/* 187 */     return FactoryTransformer.factoryTransformer(factory);
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
/*     */   public static <T> Transformer<T, T> chainedTransformer(Transformer<? super T, ? extends T>... transformers) {
/* 202 */     return ChainedTransformer.chainedTransformer((Transformer[])transformers);
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
/*     */   public static <T> Transformer<T, T> chainedTransformer(Collection<? extends Transformer<? super T, ? extends T>> transformers) {
/* 218 */     return ChainedTransformer.chainedTransformer(transformers);
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
/*     */   public static <T> Transformer<T, T> ifTransformer(Predicate<? super T> predicate, Transformer<? super T, ? extends T> trueTransformer) {
/* 235 */     return IfTransformer.ifTransformer(predicate, trueTransformer);
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
/*     */   public static <I, O> Transformer<I, O> ifTransformer(Predicate<? super I> predicate, Transformer<? super I, ? extends O> trueTransformer, Transformer<? super I, ? extends O> falseTransformer) {
/* 255 */     return IfTransformer.ifTransformer(predicate, trueTransformer, falseTransformer);
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
/*     */   @Deprecated
/*     */   public static <I, O> Transformer<I, O> switchTransformer(Predicate<? super I> predicate, Transformer<? super I, ? extends O> trueTransformer, Transformer<? super I, ? extends O> falseTransformer) {
/* 277 */     return SwitchTransformer.switchTransformer(new Predicate[] { predicate }, new Transformer[] { trueTransformer }, falseTransformer);
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
/*     */   public static <I, O> Transformer<I, O> switchTransformer(Predicate<? super I>[] predicates, Transformer<? super I, ? extends O>[] transformers) {
/* 299 */     return SwitchTransformer.switchTransformer((Predicate[])predicates, (Transformer[])transformers, null);
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
/*     */   public static <I, O> Transformer<I, O> switchTransformer(Predicate<? super I>[] predicates, Transformer<? super I, ? extends O>[] transformers, Transformer<? super I, ? extends O> defaultTransformer) {
/* 323 */     return SwitchTransformer.switchTransformer((Predicate[])predicates, (Transformer[])transformers, defaultTransformer);
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
/*     */   public static <I, O> Transformer<I, O> switchTransformer(Map<Predicate<I>, Transformer<I, O>> predicatesAndTransformers) {
/* 349 */     return SwitchTransformer.switchTransformer(predicatesAndTransformers);
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
/*     */   public static <I, O> Transformer<I, O> switchMapTransformer(Map<I, Transformer<I, O>> objectsAndTransformers) {
/* 373 */     if (objectsAndTransformers == null) {
/* 374 */       throw new NullPointerException("The object and transformer map must not be null");
/*     */     }
/* 376 */     Transformer<? super I, ? extends O> def = objectsAndTransformers.remove(null);
/* 377 */     int size = objectsAndTransformers.size();
/* 378 */     Transformer[] arrayOfTransformer = new Transformer[size];
/* 379 */     Predicate[] arrayOfPredicate = new Predicate[size];
/* 380 */     int i = 0;
/* 381 */     for (Map.Entry<I, Transformer<I, O>> entry : objectsAndTransformers.entrySet()) {
/* 382 */       arrayOfPredicate[i] = EqualPredicate.equalPredicate(entry.getKey());
/* 383 */       arrayOfTransformer[i++] = entry.getValue();
/*     */     } 
/* 385 */     return switchTransformer((Predicate<? super I>[])arrayOfPredicate, (Transformer<? super I, ? extends O>[])arrayOfTransformer, def);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Transformer<Class<? extends T>, T> instantiateTransformer() {
/* 396 */     return InstantiateTransformer.instantiateTransformer();
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
/*     */   public static <T> Transformer<Class<? extends T>, T> instantiateTransformer(Class<?>[] paramTypes, Object[] args) {
/* 413 */     return InstantiateTransformer.instantiateTransformer(paramTypes, args);
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
/*     */   public static <I, O> Transformer<I, O> mapTransformer(Map<? super I, ? extends O> map) {
/* 428 */     return MapTransformer.mapTransformer(map);
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
/*     */   public static <I, O> Transformer<I, O> invokerTransformer(String methodName) {
/* 448 */     return InvokerTransformer.invokerTransformer(methodName, null, null);
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
/*     */   public static <I, O> Transformer<I, O> invokerTransformer(String methodName, Class<?>[] paramTypes, Object[] args) {
/* 468 */     return InvokerTransformer.invokerTransformer(methodName, paramTypes, args);
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
/*     */   public static <T> Transformer<T, String> stringValueTransformer() {
/* 481 */     return StringValueTransformer.stringValueTransformer();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\TransformerUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */