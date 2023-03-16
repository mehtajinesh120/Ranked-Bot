/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import org.apache.commons.collections4.functors.AllPredicate;
/*     */ import org.apache.commons.collections4.functors.AndPredicate;
/*     */ import org.apache.commons.collections4.functors.AnyPredicate;
/*     */ import org.apache.commons.collections4.functors.EqualPredicate;
/*     */ import org.apache.commons.collections4.functors.ExceptionPredicate;
/*     */ import org.apache.commons.collections4.functors.FalsePredicate;
/*     */ import org.apache.commons.collections4.functors.IdentityPredicate;
/*     */ import org.apache.commons.collections4.functors.InstanceofPredicate;
/*     */ import org.apache.commons.collections4.functors.InvokerTransformer;
/*     */ import org.apache.commons.collections4.functors.NonePredicate;
/*     */ import org.apache.commons.collections4.functors.NotNullPredicate;
/*     */ import org.apache.commons.collections4.functors.NotPredicate;
/*     */ import org.apache.commons.collections4.functors.NullIsExceptionPredicate;
/*     */ import org.apache.commons.collections4.functors.NullIsFalsePredicate;
/*     */ import org.apache.commons.collections4.functors.NullIsTruePredicate;
/*     */ import org.apache.commons.collections4.functors.NullPredicate;
/*     */ import org.apache.commons.collections4.functors.OnePredicate;
/*     */ import org.apache.commons.collections4.functors.OrPredicate;
/*     */ import org.apache.commons.collections4.functors.TransformedPredicate;
/*     */ import org.apache.commons.collections4.functors.TransformerPredicate;
/*     */ import org.apache.commons.collections4.functors.TruePredicate;
/*     */ import org.apache.commons.collections4.functors.UniquePredicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PredicateUtils
/*     */ {
/*     */   public static <T> Predicate<T> exceptionPredicate() {
/*  91 */     return ExceptionPredicate.exceptionPredicate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> truePredicate() {
/* 102 */     return TruePredicate.truePredicate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> falsePredicate() {
/* 113 */     return FalsePredicate.falsePredicate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> nullPredicate() {
/* 124 */     return NullPredicate.nullPredicate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Predicate<T> notNullPredicate() {
/* 135 */     return NotNullPredicate.notNullPredicate();
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
/*     */   public static <T> Predicate<T> equalPredicate(T value) {
/* 148 */     return EqualPredicate.equalPredicate(value);
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
/*     */   public static <T> Predicate<T> identityPredicate(T value) {
/* 161 */     return IdentityPredicate.identityPredicate(value);
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
/*     */   public static Predicate<Object> instanceofPredicate(Class<?> type) {
/* 175 */     return InstanceofPredicate.instanceOfPredicate(type);
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
/*     */   public static <T> Predicate<T> uniquePredicate() {
/* 191 */     return UniquePredicate.uniquePredicate();
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
/*     */   public static <T> Predicate<T> invokerPredicate(String methodName) {
/* 213 */     return asPredicate(InvokerTransformer.invokerTransformer(methodName));
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
/*     */   public static <T> Predicate<T> invokerPredicate(String methodName, Class<?>[] paramTypes, Object[] args) {
/* 239 */     return asPredicate(InvokerTransformer.invokerTransformer(methodName, paramTypes, args));
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
/*     */   public static <T> Predicate<T> andPredicate(Predicate<? super T> predicate1, Predicate<? super T> predicate2) {
/* 258 */     return AndPredicate.andPredicate(predicate1, predicate2);
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
/*     */   public static <T> Predicate<T> allPredicate(Predicate<? super T>... predicates) {
/* 274 */     return AllPredicate.allPredicate((Predicate[])predicates);
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
/*     */   public static <T> Predicate<T> allPredicate(Collection<? extends Predicate<? super T>> predicates) {
/* 290 */     return AllPredicate.allPredicate(predicates);
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
/*     */   public static <T> Predicate<T> orPredicate(Predicate<? super T> predicate1, Predicate<? super T> predicate2) {
/* 306 */     return OrPredicate.orPredicate(predicate1, predicate2);
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
/*     */   public static <T> Predicate<T> anyPredicate(Predicate<? super T>... predicates) {
/* 322 */     return AnyPredicate.anyPredicate((Predicate[])predicates);
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
/*     */   public static <T> Predicate<T> anyPredicate(Collection<? extends Predicate<? super T>> predicates) {
/* 338 */     return AnyPredicate.anyPredicate(predicates);
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
/*     */   public static <T> Predicate<T> eitherPredicate(Predicate<? super T> predicate1, Predicate<? super T> predicate2) {
/* 355 */     Predicate<T> onePredicate = onePredicate((Predicate<? super T>[])new Predicate[] { predicate1, predicate2 });
/* 356 */     return onePredicate;
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
/*     */   public static <T> Predicate<T> onePredicate(Predicate<? super T>... predicates) {
/* 372 */     return OnePredicate.onePredicate((Predicate[])predicates);
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
/*     */   public static <T> Predicate<T> onePredicate(Collection<? extends Predicate<? super T>> predicates) {
/* 388 */     return OnePredicate.onePredicate(predicates);
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
/*     */   public static <T> Predicate<T> neitherPredicate(Predicate<? super T> predicate1, Predicate<? super T> predicate2) {
/* 405 */     Predicate<T> nonePredicate = nonePredicate((Predicate<? super T>[])new Predicate[] { predicate1, predicate2 });
/* 406 */     return nonePredicate;
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
/*     */   public static <T> Predicate<T> nonePredicate(Predicate<? super T>... predicates) {
/* 422 */     return NonePredicate.nonePredicate((Predicate[])predicates);
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
/*     */   public static <T> Predicate<T> nonePredicate(Collection<? extends Predicate<? super T>> predicates) {
/* 438 */     return NonePredicate.nonePredicate(predicates);
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
/*     */   public static <T> Predicate<T> notPredicate(Predicate<? super T> predicate) {
/* 452 */     return NotPredicate.notPredicate(predicate);
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
/*     */   public static <T> Predicate<T> asPredicate(Transformer<? super T, Boolean> transformer) {
/* 470 */     return TransformerPredicate.transformerPredicate(transformer);
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
/*     */   public static <T> Predicate<T> nullIsExceptionPredicate(Predicate<? super T> predicate) {
/* 488 */     return NullIsExceptionPredicate.nullIsExceptionPredicate(predicate);
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
/*     */   public static <T> Predicate<T> nullIsFalsePredicate(Predicate<? super T> predicate) {
/* 503 */     return NullIsFalsePredicate.nullIsFalsePredicate(predicate);
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
/*     */   public static <T> Predicate<T> nullIsTruePredicate(Predicate<? super T> predicate) {
/* 518 */     return NullIsTruePredicate.nullIsTruePredicate(predicate);
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
/*     */   public static <T> Predicate<T> transformedPredicate(Transformer<? super T, ? extends T> transformer, Predicate<? super T> predicate) {
/* 537 */     return TransformedPredicate.transformedPredicate(transformer, predicate);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\PredicateUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */