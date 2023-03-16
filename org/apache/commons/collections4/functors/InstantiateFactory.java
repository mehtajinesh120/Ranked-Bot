/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.apache.commons.collections4.Factory;
/*     */ import org.apache.commons.collections4.FunctorException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InstantiateFactory<T>
/*     */   implements Factory<T>
/*     */ {
/*     */   private final Class<T> iClassToInstantiate;
/*     */   private final Class<?>[] iParamTypes;
/*     */   private final Object[] iArgs;
/*  45 */   private transient Constructor<T> iConstructor = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*  61 */     if (classToInstantiate == null) {
/*  62 */       throw new NullPointerException("Class to instantiate must not be null");
/*     */     }
/*  64 */     if ((paramTypes == null && args != null) || (paramTypes != null && args == null) || (paramTypes != null && args != null && paramTypes.length != args.length))
/*     */     {
/*     */       
/*  67 */       throw new IllegalArgumentException("Parameter types must match the arguments");
/*     */     }
/*     */     
/*  70 */     if (paramTypes == null || paramTypes.length == 0) {
/*  71 */       return new InstantiateFactory<T>(classToInstantiate);
/*     */     }
/*  73 */     return new InstantiateFactory<T>(classToInstantiate, paramTypes, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InstantiateFactory(Class<T> classToInstantiate) {
/*  84 */     this.iClassToInstantiate = classToInstantiate;
/*  85 */     this.iParamTypes = null;
/*  86 */     this.iArgs = null;
/*  87 */     findConstructor();
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
/*     */   public InstantiateFactory(Class<T> classToInstantiate, Class<?>[] paramTypes, Object[] args) {
/* 100 */     this.iClassToInstantiate = classToInstantiate;
/* 101 */     this.iParamTypes = (Class[])paramTypes.clone();
/* 102 */     this.iArgs = (Object[])args.clone();
/* 103 */     findConstructor();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void findConstructor() {
/*     */     try {
/* 111 */       this.iConstructor = this.iClassToInstantiate.getConstructor(this.iParamTypes);
/* 112 */     } catch (NoSuchMethodException ex) {
/* 113 */       throw new IllegalArgumentException("InstantiateFactory: The constructor must exist and be public ");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T create() {
/* 125 */     if (this.iConstructor == null) {
/* 126 */       findConstructor();
/*     */     }
/*     */     
/*     */     try {
/* 130 */       return this.iConstructor.newInstance(this.iArgs);
/* 131 */     } catch (InstantiationException ex) {
/* 132 */       throw new FunctorException("InstantiateFactory: InstantiationException", ex);
/* 133 */     } catch (IllegalAccessException ex) {
/* 134 */       throw new FunctorException("InstantiateFactory: Constructor must be public", ex);
/* 135 */     } catch (InvocationTargetException ex) {
/* 136 */       throw new FunctorException("InstantiateFactory: Constructor threw an exception", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\InstantiateFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */