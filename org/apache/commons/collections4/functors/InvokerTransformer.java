/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InvokerTransformer<I, O>
/*     */   implements Transformer<I, O>
/*     */ {
/*     */   private final String iMethodName;
/*     */   private final Class<?>[] iParamTypes;
/*     */   private final Object[] iArgs;
/*     */   
/*     */   public static <I, O> Transformer<I, O> invokerTransformer(String methodName) {
/*  56 */     if (methodName == null) {
/*  57 */       throw new NullPointerException("The method to invoke must not be null");
/*     */     }
/*  59 */     return new InvokerTransformer<I, O>(methodName);
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
/*     */   public static <I, O> Transformer<I, O> invokerTransformer(String methodName, Class<?>[] paramTypes, Object[] args) {
/*  76 */     if (methodName == null) {
/*  77 */       throw new NullPointerException("The method to invoke must not be null");
/*     */     }
/*  79 */     if ((paramTypes == null && args != null) || (paramTypes != null && args == null) || (paramTypes != null && args != null && paramTypes.length != args.length))
/*     */     {
/*     */       
/*  82 */       throw new IllegalArgumentException("The parameter types must match the arguments");
/*     */     }
/*  84 */     if (paramTypes == null || paramTypes.length == 0) {
/*  85 */       return new InvokerTransformer<I, O>(methodName);
/*     */     }
/*  87 */     return new InvokerTransformer<I, O>(methodName, paramTypes, args);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private InvokerTransformer(String methodName) {
/*  97 */     this.iMethodName = methodName;
/*  98 */     this.iParamTypes = null;
/*  99 */     this.iArgs = null;
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
/*     */   public InvokerTransformer(String methodName, Class<?>[] paramTypes, Object[] args) {
/* 114 */     this.iMethodName = methodName;
/* 115 */     this.iParamTypes = (paramTypes != null) ? (Class[])paramTypes.clone() : null;
/* 116 */     this.iArgs = (args != null) ? (Object[])args.clone() : null;
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
/*     */   public O transform(Object input) {
/* 128 */     if (input == null) {
/* 129 */       return null;
/*     */     }
/*     */     try {
/* 132 */       Class<?> cls = input.getClass();
/* 133 */       Method method = cls.getMethod(this.iMethodName, this.iParamTypes);
/* 134 */       return (O)method.invoke(input, this.iArgs);
/* 135 */     } catch (NoSuchMethodException ex) {
/* 136 */       throw new FunctorException("InvokerTransformer: The method '" + this.iMethodName + "' on '" + input.getClass() + "' does not exist");
/*     */     }
/* 138 */     catch (IllegalAccessException ex) {
/* 139 */       throw new FunctorException("InvokerTransformer: The method '" + this.iMethodName + "' on '" + input.getClass() + "' cannot be accessed");
/*     */     }
/* 141 */     catch (InvocationTargetException ex) {
/* 142 */       throw new FunctorException("InvokerTransformer: The method '" + this.iMethodName + "' on '" + input.getClass() + "' threw an exception", ex);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\InvokerTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */