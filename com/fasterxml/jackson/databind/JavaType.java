/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.core.type.ResolvedType;
/*     */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JavaType
/*     */   extends ResolvedType
/*     */   implements Serializable, Type
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Class<?> _class;
/*     */   protected final int _hash;
/*     */   protected final Object _valueHandler;
/*     */   protected final Object _typeHandler;
/*     */   protected final boolean _asStatic;
/*     */   
/*     */   protected JavaType(Class<?> raw, int additionalHash, Object valueHandler, Object typeHandler, boolean asStatic) {
/*  79 */     this._class = raw;
/*  80 */     this._hash = raw.getName().hashCode() + additionalHash;
/*  81 */     this._valueHandler = valueHandler;
/*  82 */     this._typeHandler = typeHandler;
/*  83 */     this._asStatic = asStatic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected JavaType(JavaType base) {
/*  93 */     this._class = base._class;
/*  94 */     this._hash = base._hash;
/*  95 */     this._valueHandler = base._valueHandler;
/*  96 */     this._typeHandler = base._typeHandler;
/*  97 */     this._asStatic = base._asStatic;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType withHandlersFrom(JavaType src) {
/* 141 */     JavaType type = this;
/* 142 */     Object h = src.getTypeHandler();
/* 143 */     if (h != this._typeHandler) {
/* 144 */       type = type.withTypeHandler(h);
/*     */     }
/* 146 */     h = src.getValueHandler();
/* 147 */     if (h != this._valueHandler) {
/* 148 */       type = type.withValueHandler(h);
/*     */     }
/* 150 */     return type;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public JavaType forcedNarrowBy(Class<?> subclass) {
/* 211 */     if (subclass == this._class) {
/* 212 */       return this;
/*     */     }
/* 214 */     return _narrow(subclass);
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
/*     */   public final Class<?> getRawClass() {
/* 227 */     return this._class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasRawClass(Class<?> clz) {
/* 235 */     return (this._class == clz);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasContentType() {
/* 245 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isTypeOrSubTypeOf(Class<?> clz) {
/* 252 */     return (this._class == clz || clz.isAssignableFrom(this._class));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isTypeOrSuperTypeOf(Class<?> clz) {
/* 259 */     return (this._class == clz || this._class.isAssignableFrom(clz));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAbstract() {
/* 264 */     return Modifier.isAbstract(this._class.getModifiers());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConcrete() {
/* 274 */     int mod = this._class.getModifiers();
/* 275 */     if ((mod & 0x600) == 0) {
/* 276 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 281 */     return this._class.isPrimitive();
/*     */   }
/*     */   
/*     */   public boolean isThrowable() {
/* 285 */     return Throwable.class.isAssignableFrom(this._class);
/*     */   }
/*     */   public boolean isArrayType() {
/* 288 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEnumType() {
/* 296 */     return this._class.isEnum();
/*     */   }
/*     */   
/*     */   public final boolean isInterface() {
/* 300 */     return this._class.isInterface();
/*     */   }
/*     */   public final boolean isPrimitive() {
/* 303 */     return this._class.isPrimitive();
/*     */   }
/*     */   public final boolean isFinal() {
/* 306 */     return Modifier.isFinal(this._class.getModifiers());
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
/*     */   public boolean isCollectionLikeType() {
/* 321 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMapLikeType() {
/* 329 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isJavaLangObject() {
/* 340 */     return (this._class == Object.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean useStaticType() {
/* 350 */     return this._asStatic;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasGenericTypes() {
/* 359 */     return (containedTypeCount() > 0);
/*     */   }
/*     */   public JavaType getKeyType() {
/* 362 */     return null;
/*     */   }
/*     */   public JavaType getContentType() {
/* 365 */     return null;
/*     */   }
/*     */   public JavaType getReferencedType() {
/* 368 */     return null;
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
/*     */   @Deprecated
/*     */   public Class<?> getParameterSource() {
/* 383 */     return null;
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
/*     */   public JavaType containedTypeOrUnknown(int index) {
/* 409 */     JavaType t = containedType(index);
/* 410 */     return (t == null) ? TypeFactory.unknownType() : t;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getValueHandler() {
/* 463 */     return (T)this._valueHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getTypeHandler() {
/* 469 */     return (T)this._typeHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getContentValueHandler() {
/* 474 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getContentTypeHandler() {
/* 479 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasValueHandler() {
/* 484 */     return (this._valueHandler != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasHandlers() {
/* 495 */     return (this._typeHandler != null || this._valueHandler != null);
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
/*     */   public String getGenericSignature() {
/* 515 */     StringBuilder sb = new StringBuilder(40);
/* 516 */     getGenericSignature(sb);
/* 517 */     return sb.toString();
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
/*     */   public String getErasedSignature() {
/* 536 */     StringBuilder sb = new StringBuilder(40);
/* 537 */     getErasedSignature(sb);
/* 538 */     return sb.toString();
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
/*     */ 
/*     */ 
/*     */   
/*     */   public final int hashCode() {
/* 567 */     return this._hash;
/*     */   }
/*     */   
/*     */   public abstract JavaType withTypeHandler(Object paramObject);
/*     */   
/*     */   public abstract JavaType withContentTypeHandler(Object paramObject);
/*     */   
/*     */   public abstract JavaType withValueHandler(Object paramObject);
/*     */   
/*     */   public abstract JavaType withContentValueHandler(Object paramObject);
/*     */   
/*     */   public abstract JavaType withContentType(JavaType paramJavaType);
/*     */   
/*     */   public abstract JavaType withStaticTyping();
/*     */   
/*     */   public abstract JavaType refine(Class<?> paramClass, TypeBindings paramTypeBindings, JavaType paramJavaType, JavaType[] paramArrayOfJavaType);
/*     */   
/*     */   @Deprecated
/*     */   protected abstract JavaType _narrow(Class<?> paramClass);
/*     */   
/*     */   public abstract boolean isContainerType();
/*     */   
/*     */   public abstract int containedTypeCount();
/*     */   
/*     */   public abstract JavaType containedType(int paramInt);
/*     */   
/*     */   @Deprecated
/*     */   public abstract String containedTypeName(int paramInt);
/*     */   
/*     */   public abstract TypeBindings getBindings();
/*     */   
/*     */   public abstract JavaType findSuperType(Class<?> paramClass);
/*     */   
/*     */   public abstract JavaType getSuperClass();
/*     */   
/*     */   public abstract List<JavaType> getInterfaces();
/*     */   
/*     */   public abstract JavaType[] findTypeParameters(Class<?> paramClass);
/*     */   
/*     */   public abstract StringBuilder getGenericSignature(StringBuilder paramStringBuilder);
/*     */   
/*     */   public abstract StringBuilder getErasedSignature(StringBuilder paramStringBuilder);
/*     */   
/*     */   public abstract String toString();
/*     */   
/*     */   public abstract boolean equals(Object paramObject);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\JavaType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */