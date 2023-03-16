/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PrototypeFactory
/*     */ {
/*     */   public static <T> Factory<T> prototypeFactory(T prototype) {
/*  65 */     if (prototype == null) {
/*  66 */       return ConstantFactory.constantFactory(null);
/*     */     }
/*     */     try {
/*  69 */       Method method = prototype.getClass().getMethod("clone", (Class[])null);
/*  70 */       return new PrototypeCloneFactory<T>(prototype, method);
/*     */     }
/*  72 */     catch (NoSuchMethodException ex) {
/*     */       try {
/*  74 */         prototype.getClass().getConstructor(new Class[] { prototype.getClass() });
/*  75 */         return new InstantiateFactory<T>((Class)prototype.getClass(), new Class[] { prototype.getClass() }, new Object[] { prototype });
/*     */ 
/*     */       
/*     */       }
/*  79 */       catch (NoSuchMethodException ex2) {
/*  80 */         if (prototype instanceof Serializable) {
/*  81 */           return (Factory)new PrototypeSerializationFactory<Serializable>((Serializable)prototype);
/*     */         }
/*     */ 
/*     */         
/*  85 */         throw new IllegalArgumentException("The prototype must be cloneable via a public clone method");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class PrototypeCloneFactory<T>
/*     */     implements Factory<T>
/*     */   {
/*     */     private final T iPrototype;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private transient Method iCloneMethod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private PrototypeCloneFactory(T prototype, Method method) {
/* 112 */       this.iPrototype = prototype;
/* 113 */       this.iCloneMethod = method;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void findCloneMethod() {
/*     */       try {
/* 121 */         this.iCloneMethod = this.iPrototype.getClass().getMethod("clone", (Class[])null);
/* 122 */       } catch (NoSuchMethodException ex) {
/* 123 */         throw new IllegalArgumentException("PrototypeCloneFactory: The clone method must exist and be public ");
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T create() {
/* 136 */       if (this.iCloneMethod == null) {
/* 137 */         findCloneMethod();
/*     */       }
/*     */       
/*     */       try {
/* 141 */         return (T)this.iCloneMethod.invoke(this.iPrototype, (Object[])null);
/* 142 */       } catch (IllegalAccessException ex) {
/* 143 */         throw new FunctorException("PrototypeCloneFactory: Clone method must be public", ex);
/* 144 */       } catch (InvocationTargetException ex) {
/* 145 */         throw new FunctorException("PrototypeCloneFactory: Clone method threw an exception", ex);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class PrototypeSerializationFactory<T extends Serializable>
/*     */     implements Factory<T>
/*     */   {
/*     */     private final T iPrototype;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private PrototypeSerializationFactory(T prototype) {
/* 165 */       this.iPrototype = prototype;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T create() {
/* 176 */       ByteArrayOutputStream baos = new ByteArrayOutputStream(512);
/* 177 */       ByteArrayInputStream bais = null;
/*     */       try {
/* 179 */         ObjectOutputStream out = new ObjectOutputStream(baos);
/* 180 */         out.writeObject(this.iPrototype);
/*     */         
/* 182 */         bais = new ByteArrayInputStream(baos.toByteArray());
/* 183 */         ObjectInputStream in = new ObjectInputStream(bais);
/* 184 */         return (T)in.readObject();
/*     */       }
/* 186 */       catch (ClassNotFoundException ex) {
/* 187 */         throw new FunctorException(ex);
/* 188 */       } catch (IOException ex) {
/* 189 */         throw new FunctorException(ex);
/*     */       } finally {
/*     */         try {
/* 192 */           if (bais != null) {
/* 193 */             bais.close();
/*     */           }
/* 195 */         } catch (IOException ex) {}
/*     */ 
/*     */         
/*     */         try {
/* 199 */           baos.close();
/* 200 */         } catch (IOException ex) {}
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\PrototypeFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */