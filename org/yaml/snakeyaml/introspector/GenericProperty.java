/*    */ package org.yaml.snakeyaml.introspector;
/*    */ 
/*    */ import java.lang.reflect.Array;
/*    */ import java.lang.reflect.GenericArrayType;
/*    */ import java.lang.reflect.ParameterizedType;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GenericProperty
/*    */   extends Property
/*    */ {
/*    */   private final Type genType;
/*    */   private boolean actualClassesChecked;
/*    */   private Class<?>[] actualClasses;
/*    */   
/*    */   public GenericProperty(String name, Class<?> aClass, Type aType) {
/* 26 */     super(name, aClass);
/* 27 */     this.genType = aType;
/* 28 */     this.actualClassesChecked = (aType == null);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?>[] getActualTypeArguments() {
/* 35 */     if (!this.actualClassesChecked) {
/* 36 */       if (this.genType instanceof ParameterizedType) {
/* 37 */         ParameterizedType parameterizedType = (ParameterizedType)this.genType;
/* 38 */         Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
/* 39 */         if (actualTypeArguments.length > 0) {
/* 40 */           this.actualClasses = new Class[actualTypeArguments.length];
/* 41 */           for (int i = 0; i < actualTypeArguments.length; i++) {
/* 42 */             if (actualTypeArguments[i] instanceof Class) {
/* 43 */               this.actualClasses[i] = (Class)actualTypeArguments[i];
/* 44 */             } else if (actualTypeArguments[i] instanceof ParameterizedType) {
/* 45 */               this.actualClasses[i] = (Class)((ParameterizedType)actualTypeArguments[i])
/* 46 */                 .getRawType();
/* 47 */             } else if (actualTypeArguments[i] instanceof GenericArrayType) {
/*    */               
/* 49 */               Type componentType = ((GenericArrayType)actualTypeArguments[i]).getGenericComponentType();
/* 50 */               if (componentType instanceof Class) {
/* 51 */                 this.actualClasses[i] = Array.newInstance((Class)componentType, 0).getClass();
/*    */               } else {
/* 53 */                 this.actualClasses = null;
/*    */                 break;
/*    */               } 
/*    */             } else {
/* 57 */               this.actualClasses = null;
/*    */               break;
/*    */             } 
/*    */           } 
/*    */         } 
/* 62 */       } else if (this.genType instanceof GenericArrayType) {
/* 63 */         Type componentType = ((GenericArrayType)this.genType).getGenericComponentType();
/* 64 */         if (componentType instanceof Class) {
/* 65 */           this.actualClasses = new Class[] { (Class)componentType };
/*    */         }
/* 67 */       } else if (this.genType instanceof Class) {
/*    */         
/* 69 */         Class<?> classType = (Class)this.genType;
/* 70 */         if (classType.isArray()) {
/* 71 */           this.actualClasses = new Class[1];
/* 72 */           this.actualClasses[0] = getType().getComponentType();
/*    */         } 
/*    */       } 
/* 75 */       this.actualClassesChecked = true;
/*    */     } 
/* 77 */     return this.actualClasses;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\introspector\GenericProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */