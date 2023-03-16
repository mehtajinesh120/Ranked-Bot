/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import org.apache.commons.collections4.functors.ConstantFactory;
/*     */ import org.apache.commons.collections4.functors.ExceptionFactory;
/*     */ import org.apache.commons.collections4.functors.InstantiateFactory;
/*     */ import org.apache.commons.collections4.functors.PrototypeFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FactoryUtils
/*     */ {
/*     */   public static <T> Factory<T> exceptionFactory() {
/*  62 */     return ExceptionFactory.exceptionFactory();
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
/*     */   public static <T> Factory<T> nullFactory() {
/*  74 */     return ConstantFactory.constantFactory(null);
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
/*     */   public static <T> Factory<T> constantFactory(T constantToReturn) {
/*  90 */     return ConstantFactory.constantFactory(constantToReturn);
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
/*     */   public static <T> Factory<T> prototypeFactory(T prototype) {
/* 112 */     return PrototypeFactory.prototypeFactory(prototype);
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
/*     */   public static <T> Factory<T> instantiateFactory(Class<T> classToInstantiate) {
/* 127 */     return InstantiateFactory.instantiateFactory(classToInstantiate, null, null);
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
/*     */   public static <T> Factory<T> instantiateFactory(Class<T> classToInstantiate, Class<?>[] paramTypes, Object[] args) {
/* 147 */     return InstantiateFactory.instantiateFactory(classToInstantiate, paramTypes, args);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\FactoryUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */