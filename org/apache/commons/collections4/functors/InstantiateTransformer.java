/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import org.apache.commons.collections4.FunctorException;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InstantiateTransformer<T>
/*     */   implements Transformer<Class<? extends T>, T>
/*     */ {
/*  40 */   private static final Transformer NO_ARG_INSTANCE = new InstantiateTransformer();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final Class<?>[] iParamTypes;
/*     */ 
/*     */ 
/*     */   
/*     */   private final Object[] iArgs;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> Transformer<Class<? extends T>, T> instantiateTransformer() {
/*  55 */     return NO_ARG_INSTANCE;
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
/*     */   public static <T> Transformer<Class<? extends T>, T> instantiateTransformer(Class<?>[] paramTypes, Object[] args) {
/*  69 */     if ((paramTypes == null && args != null) || (paramTypes != null && args == null) || (paramTypes != null && args != null && paramTypes.length != args.length))
/*     */     {
/*     */       
/*  72 */       throw new IllegalArgumentException("Parameter types must match the arguments");
/*     */     }
/*     */     
/*  75 */     if (paramTypes == null || paramTypes.length == 0) {
/*  76 */       return new InstantiateTransformer<T>();
/*     */     }
/*  78 */     return new InstantiateTransformer<T>(paramTypes, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InstantiateTransformer() {
/*  86 */     this.iParamTypes = null;
/*  87 */     this.iArgs = null;
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
/*     */   public InstantiateTransformer(Class<?>[] paramTypes, Object[] args) {
/* 101 */     this.iParamTypes = (paramTypes != null) ? (Class[])paramTypes.clone() : null;
/* 102 */     this.iArgs = (args != null) ? (Object[])args.clone() : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T transform(Class<? extends T> input) {
/*     */     try {
/* 114 */       if (input == null) {
/* 115 */         throw new FunctorException("InstantiateTransformer: Input object was not an instanceof Class, it was a null object");
/*     */       }
/*     */       
/* 118 */       Constructor<? extends T> con = input.getConstructor(this.iParamTypes);
/* 119 */       return con.newInstance(this.iArgs);
/* 120 */     } catch (NoSuchMethodException ex) {
/* 121 */       throw new FunctorException("InstantiateTransformer: The constructor must exist and be public ");
/* 122 */     } catch (InstantiationException ex) {
/* 123 */       throw new FunctorException("InstantiateTransformer: InstantiationException", ex);
/* 124 */     } catch (IllegalAccessException ex) {
/* 125 */       throw new FunctorException("InstantiateTransformer: Constructor must be public", ex);
/* 126 */     } catch (InvocationTargetException ex) {
/* 127 */       throw new FunctorException("InstantiateTransformer: Constructor threw an exception", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\InstantiateTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */