/*     */ package org.yaml.snakeyaml.introspector;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PropertySubstitute
/*     */   extends Property
/*     */ {
/*  35 */   private static final Logger log = Logger.getLogger(PropertySubstitute.class.getPackage().getName());
/*     */   
/*     */   protected Class<?> targetType;
/*     */   
/*     */   private final String readMethod;
/*     */   private final String writeMethod;
/*     */   private transient Method read;
/*     */   private transient Method write;
/*     */   private Field field;
/*     */   protected Class<?>[] parameters;
/*     */   private Property delegate;
/*     */   private boolean filler;
/*     */   
/*     */   public PropertySubstitute(String name, Class<?> type, String readMethod, String writeMethod, Class<?>... params) {
/*  49 */     super(name, type);
/*  50 */     this.readMethod = readMethod;
/*  51 */     this.writeMethod = writeMethod;
/*  52 */     setActualTypeArguments(params);
/*  53 */     this.filler = false;
/*     */   }
/*     */   
/*     */   public PropertySubstitute(String name, Class<?> type, Class<?>... params) {
/*  57 */     this(name, type, null, null, params);
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?>[] getActualTypeArguments() {
/*  62 */     if (this.parameters == null && this.delegate != null) {
/*  63 */       return this.delegate.getActualTypeArguments();
/*     */     }
/*  65 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public void setActualTypeArguments(Class<?>... args) {
/*  69 */     if (args != null && args.length > 0) {
/*  70 */       this.parameters = args;
/*     */     } else {
/*  72 */       this.parameters = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void set(Object object, Object value) throws Exception {
/*  78 */     if (this.write != null) {
/*  79 */       if (!this.filler) {
/*  80 */         this.write.invoke(object, new Object[] { value });
/*  81 */       } else if (value != null) {
/*  82 */         if (value instanceof Collection) {
/*  83 */           Collection<?> collection = (Collection)value;
/*  84 */           for (Object val : collection) {
/*  85 */             this.write.invoke(object, new Object[] { val });
/*     */           } 
/*  87 */         } else if (value instanceof Map) {
/*  88 */           Map<?, ?> map = (Map<?, ?>)value;
/*  89 */           for (Map.Entry<?, ?> entry : map.entrySet()) {
/*  90 */             this.write.invoke(object, new Object[] { entry.getKey(), entry.getValue() });
/*     */           } 
/*  92 */         } else if (value.getClass().isArray()) {
/*     */           
/*  94 */           int len = Array.getLength(value);
/*  95 */           for (int i = 0; i < len; i++) {
/*  96 */             this.write.invoke(object, new Object[] { Array.get(value, i) });
/*     */           } 
/*     */         } 
/*     */       } 
/* 100 */     } else if (this.field != null) {
/* 101 */       this.field.set(object, value);
/* 102 */     } else if (this.delegate != null) {
/* 103 */       this.delegate.set(object, value);
/*     */     } else {
/* 105 */       log.warning("No setter/delegate for '" + getName() + "' on object " + object);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object get(Object object) {
/*     */     try {
/* 113 */       if (this.read != null)
/* 114 */         return this.read.invoke(object, new Object[0]); 
/* 115 */       if (this.field != null) {
/* 116 */         return this.field.get(object);
/*     */       }
/* 118 */     } catch (Exception e) {
/* 119 */       throw new YAMLException("Unable to find getter for property '" + 
/* 120 */           getName() + "' on object " + object + ":" + e);
/*     */     } 
/*     */     
/* 123 */     if (this.delegate != null) {
/* 124 */       return this.delegate.get(object);
/*     */     }
/* 126 */     throw new YAMLException("No getter or delegate for property '" + 
/* 127 */         getName() + "' on object " + object);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Annotation> getAnnotations() {
/* 132 */     Annotation[] annotations = null;
/* 133 */     if (this.read != null) {
/* 134 */       annotations = this.read.getAnnotations();
/* 135 */     } else if (this.field != null) {
/* 136 */       annotations = this.field.getAnnotations();
/*     */     } 
/* 138 */     return (annotations != null) ? Arrays.<Annotation>asList(annotations) : this.delegate.getAnnotations();
/*     */   }
/*     */ 
/*     */   
/*     */   public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
/*     */     A annotation;
/* 144 */     if (this.read != null) {
/* 145 */       annotation = this.read.getAnnotation(annotationType);
/* 146 */     } else if (this.field != null) {
/* 147 */       annotation = this.field.getAnnotation(annotationType);
/*     */     } else {
/* 149 */       annotation = this.delegate.getAnnotation(annotationType);
/*     */     } 
/* 151 */     return annotation;
/*     */   }
/*     */   
/*     */   public void setTargetType(Class<?> targetType) {
/* 155 */     if (this.targetType != targetType) {
/* 156 */       this.targetType = targetType;
/*     */       
/* 158 */       String name = getName();
/* 159 */       for (Class<?> c = targetType; c != null; c = c.getSuperclass()) {
/* 160 */         for (Field f : c.getDeclaredFields()) {
/* 161 */           if (f.getName().equals(name)) {
/* 162 */             int modifiers = f.getModifiers();
/* 163 */             if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
/* 164 */               f.setAccessible(true);
/* 165 */               this.field = f;
/*     */             } 
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/* 171 */       if (this.field == null && log.isLoggable(Level.FINE)) {
/* 172 */         log.fine(String.format("Failed to find field for %s.%s", new Object[] { targetType.getName(), getName() }));
/*     */       }
/*     */ 
/*     */       
/* 176 */       if (this.readMethod != null) {
/* 177 */         this.read = discoverMethod(targetType, this.readMethod, new Class[0]);
/*     */       }
/* 179 */       if (this.writeMethod != null) {
/* 180 */         this.filler = false;
/* 181 */         this.write = discoverMethod(targetType, this.writeMethod, new Class[] { getType() });
/* 182 */         if (this.write == null && this.parameters != null) {
/* 183 */           this.filler = true;
/* 184 */           this.write = discoverMethod(targetType, this.writeMethod, this.parameters);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Method discoverMethod(Class<?> type, String name, Class<?>... params) {
/* 191 */     for (Class<?> c = type; c != null; c = c.getSuperclass()) {
/* 192 */       for (Method method : c.getDeclaredMethods()) {
/* 193 */         if (name.equals(method.getName())) {
/* 194 */           Class<?>[] parameterTypes = method.getParameterTypes();
/* 195 */           if (parameterTypes.length == params.length) {
/*     */ 
/*     */             
/* 198 */             boolean found = true;
/* 199 */             for (int i = 0; i < parameterTypes.length; i++) {
/* 200 */               if (!parameterTypes[i].isAssignableFrom(params[i])) {
/* 201 */                 found = false;
/*     */               }
/*     */             } 
/* 204 */             if (found) {
/* 205 */               method.setAccessible(true);
/* 206 */               return method;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 211 */     }  if (log.isLoggable(Level.FINE)) {
/* 212 */       log.fine(String.format("Failed to find [%s(%d args)] for %s.%s", new Object[] { name, Integer.valueOf(params.length), this.targetType
/* 213 */               .getName(), getName() }));
/*     */     }
/* 215 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 220 */     String n = super.getName();
/* 221 */     if (n != null) {
/* 222 */       return n;
/*     */     }
/* 224 */     return (this.delegate != null) ? this.delegate.getName() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Class<?> getType() {
/* 229 */     Class<?> t = super.getType();
/* 230 */     if (t != null) {
/* 231 */       return t;
/*     */     }
/* 233 */     return (this.delegate != null) ? this.delegate.getType() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadable() {
/* 238 */     return (this.read != null || this.field != null || (this.delegate != null && this.delegate.isReadable()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable() {
/* 243 */     return (this.write != null || this.field != null || (this.delegate != null && this.delegate.isWritable()));
/*     */   }
/*     */   
/*     */   public void setDelegate(Property delegate) {
/* 247 */     this.delegate = delegate;
/* 248 */     if (this.writeMethod != null && this.write == null && !this.filler) {
/* 249 */       this.filler = true;
/* 250 */       this.write = discoverMethod(this.targetType, this.writeMethod, getActualTypeArguments());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\introspector\PropertySubstitute.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */