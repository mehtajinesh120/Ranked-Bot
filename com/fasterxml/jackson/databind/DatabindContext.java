/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DatabindContext
/*     */ {
/*     */   private static final int MAX_ERROR_STR_LEN = 500;
/*     */   
/*     */   public abstract MapperConfig<?> getConfig();
/*     */   
/*     */   public abstract AnnotationIntrospector getAnnotationIntrospector();
/*     */   
/*     */   public abstract boolean isEnabled(MapperFeature paramMapperFeature);
/*     */   
/*     */   public abstract boolean canOverrideAccessModifiers();
/*     */   
/*     */   public abstract Class<?> getActiveView();
/*     */   
/*     */   public abstract Locale getLocale();
/*     */   
/*     */   public abstract TimeZone getTimeZone();
/*     */   
/*     */   public abstract JsonFormat.Value getDefaultPropertyFormat(Class<?> paramClass);
/*     */   
/*     */   public abstract Object getAttribute(Object paramObject);
/*     */   
/*     */   public abstract DatabindContext setAttribute(Object paramObject1, Object paramObject2);
/*     */   
/*     */   public JavaType constructType(Type type) {
/* 148 */     if (type == null) {
/* 149 */       return null;
/*     */     }
/* 151 */     return getTypeFactory().constructType(type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
/* 160 */     if (baseType.getRawClass() == subclass) {
/* 161 */       return baseType;
/*     */     }
/* 163 */     return getConfig().constructSpecializedType(baseType, subclass);
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
/*     */   public JavaType resolveSubType(JavaType baseType, String subClassName) throws JsonMappingException {
/* 181 */     if (subClassName.indexOf('<') > 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 186 */       JavaType t = getTypeFactory().constructFromCanonical(subClassName);
/* 187 */       if (t.isTypeOrSubTypeOf(baseType.getRawClass())) {
/* 188 */         return t;
/*     */       }
/*     */     } else {
/*     */       Class<?> cls;
/*     */       try {
/* 193 */         cls = getTypeFactory().findClass(subClassName);
/* 194 */       } catch (ClassNotFoundException e) {
/* 195 */         return null;
/* 196 */       } catch (Exception e) {
/* 197 */         throw invalidTypeIdException(baseType, subClassName, String.format("problem: (%s) %s", new Object[] { e
/*     */                 
/* 199 */                 .getClass().getName(), 
/* 200 */                 ClassUtil.exceptionMessage(e) }));
/*     */       } 
/* 202 */       if (baseType.isTypeOrSuperTypeOf(cls)) {
/* 203 */         return getTypeFactory().constructSpecializedType(baseType, cls);
/*     */       }
/*     */     } 
/* 206 */     throw invalidTypeIdException(baseType, subClassName, "Not a subtype");
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
/*     */   public JavaType resolveAndValidateSubType(JavaType baseType, String subClass, PolymorphicTypeValidator ptv) throws JsonMappingException {
/*     */     Class<?> cls;
/* 220 */     int ltIndex = subClass.indexOf('<');
/* 221 */     if (ltIndex > 0) {
/* 222 */       return _resolveAndValidateGeneric(baseType, subClass, ptv, ltIndex);
/*     */     }
/* 224 */     MapperConfig<?> config = getConfig();
/* 225 */     PolymorphicTypeValidator.Validity vld = ptv.validateSubClassName(config, baseType, subClass);
/* 226 */     if (vld == PolymorphicTypeValidator.Validity.DENIED) {
/* 227 */       return _throwSubtypeNameNotAllowed(baseType, subClass, ptv);
/*     */     }
/*     */     
/*     */     try {
/* 231 */       cls = getTypeFactory().findClass(subClass);
/* 232 */     } catch (ClassNotFoundException e) {
/* 233 */       return null;
/* 234 */     } catch (Exception e) {
/* 235 */       throw invalidTypeIdException(baseType, subClass, String.format("problem: (%s) %s", new Object[] { e
/*     */               
/* 237 */               .getClass().getName(), 
/* 238 */               ClassUtil.exceptionMessage(e) }));
/*     */     } 
/* 240 */     if (!baseType.isTypeOrSuperTypeOf(cls)) {
/* 241 */       return _throwNotASubtype(baseType, subClass);
/*     */     }
/* 243 */     JavaType subType = config.getTypeFactory().constructSpecializedType(baseType, cls);
/*     */     
/* 245 */     if (vld == PolymorphicTypeValidator.Validity.INDETERMINATE) {
/* 246 */       vld = ptv.validateSubType(config, baseType, subType);
/* 247 */       if (vld != PolymorphicTypeValidator.Validity.ALLOWED) {
/* 248 */         return _throwSubtypeClassNotAllowed(baseType, subClass, ptv);
/*     */       }
/*     */     } 
/* 251 */     return subType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JavaType _resolveAndValidateGeneric(JavaType baseType, String subClass, PolymorphicTypeValidator ptv, int ltIndex) throws JsonMappingException {
/* 258 */     MapperConfig<?> config = getConfig();
/*     */ 
/*     */ 
/*     */     
/* 262 */     PolymorphicTypeValidator.Validity vld = ptv.validateSubClassName(config, baseType, subClass.substring(0, ltIndex));
/* 263 */     if (vld == PolymorphicTypeValidator.Validity.DENIED) {
/* 264 */       return _throwSubtypeNameNotAllowed(baseType, subClass, ptv);
/*     */     }
/* 266 */     JavaType subType = getTypeFactory().constructFromCanonical(subClass);
/* 267 */     if (!subType.isTypeOrSubTypeOf(baseType.getRawClass())) {
/* 268 */       return _throwNotASubtype(baseType, subClass);
/*     */     }
/*     */     
/* 271 */     if (vld != PolymorphicTypeValidator.Validity.ALLOWED && 
/* 272 */       ptv.validateSubType(config, baseType, subType) != PolymorphicTypeValidator.Validity.ALLOWED) {
/* 273 */       return _throwSubtypeClassNotAllowed(baseType, subClass, ptv);
/*     */     }
/*     */     
/* 276 */     return subType;
/*     */   }
/*     */   
/*     */   protected <T> T _throwNotASubtype(JavaType baseType, String subType) throws JsonMappingException {
/* 280 */     throw invalidTypeIdException(baseType, subType, "Not a subtype");
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> T _throwSubtypeNameNotAllowed(JavaType baseType, String subType, PolymorphicTypeValidator ptv) throws JsonMappingException {
/* 285 */     throw invalidTypeIdException(baseType, subType, "Configured `PolymorphicTypeValidator` (of type " + 
/* 286 */         ClassUtil.classNameOf(ptv) + ") denied resolution");
/*     */   }
/*     */ 
/*     */   
/*     */   protected <T> T _throwSubtypeClassNotAllowed(JavaType baseType, String subType, PolymorphicTypeValidator ptv) throws JsonMappingException {
/* 291 */     throw invalidTypeIdException(baseType, subType, "Configured `PolymorphicTypeValidator` (of type " + 
/* 292 */         ClassUtil.classNameOf(ptv) + ") denied resolution");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract JsonMappingException invalidTypeIdException(JavaType paramJavaType, String paramString1, String paramString2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract TypeFactory getTypeFactory();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo) throws JsonMappingException {
/* 321 */     Class<?> implClass = objectIdInfo.getGeneratorType();
/* 322 */     MapperConfig<?> config = getConfig();
/* 323 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 324 */     ObjectIdGenerator<?> gen = (hi == null) ? null : hi.objectIdGeneratorInstance(config, annotated, implClass);
/* 325 */     if (gen == null) {
/* 326 */       gen = (ObjectIdGenerator)ClassUtil.createInstance(implClass, config
/* 327 */           .canOverrideAccessModifiers());
/*     */     }
/* 329 */     return gen.forScope(objectIdInfo.getScope());
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectIdResolver objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo) {
/* 334 */     Class<? extends ObjectIdResolver> implClass = objectIdInfo.getResolverType();
/* 335 */     MapperConfig<?> config = getConfig();
/* 336 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 337 */     ObjectIdResolver resolver = (hi == null) ? null : hi.resolverIdGeneratorInstance(config, annotated, implClass);
/* 338 */     if (resolver == null) {
/* 339 */       resolver = (ObjectIdResolver)ClassUtil.createInstance(implClass, config.canOverrideAccessModifiers());
/*     */     }
/*     */     
/* 342 */     return resolver;
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
/*     */   public Converter<Object, Object> converterInstance(Annotated annotated, Object converterDef) throws JsonMappingException {
/* 356 */     if (converterDef == null) {
/* 357 */       return null;
/*     */     }
/* 359 */     if (converterDef instanceof Converter) {
/* 360 */       return (Converter<Object, Object>)converterDef;
/*     */     }
/* 362 */     if (!(converterDef instanceof Class)) {
/* 363 */       throw new IllegalStateException("AnnotationIntrospector returned Converter definition of type " + converterDef
/* 364 */           .getClass().getName() + "; expected type Converter or Class<Converter> instead");
/*     */     }
/* 366 */     Class<?> converterClass = (Class)converterDef;
/*     */     
/* 368 */     if (converterClass == Converter.None.class || ClassUtil.isBogusClass(converterClass)) {
/* 369 */       return null;
/*     */     }
/* 371 */     if (!Converter.class.isAssignableFrom(converterClass)) {
/* 372 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + converterClass
/* 373 */           .getName() + "; expected Class<Converter>");
/*     */     }
/* 375 */     MapperConfig<?> config = getConfig();
/* 376 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 377 */     Converter<?, ?> conv = (hi == null) ? null : hi.converterInstance(config, annotated, converterClass);
/* 378 */     if (conv == null) {
/* 379 */       conv = (Converter<?, ?>)ClassUtil.createInstance(converterClass, config
/* 380 */           .canOverrideAccessModifiers());
/*     */     }
/* 382 */     return (Converter)conv;
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
/*     */   public abstract <T> T reportBadDefinition(JavaType paramJavaType, String paramString) throws JsonMappingException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T reportBadDefinition(Class<?> type, String msg) throws JsonMappingException {
/* 404 */     return reportBadDefinition(constructType(type), msg);
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
/*     */   protected final String _format(String msg, Object... msgArgs) {
/* 417 */     if (msgArgs.length > 0) {
/* 418 */       return String.format(msg, msgArgs);
/*     */     }
/* 420 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final String _truncate(String desc) {
/* 427 */     if (desc == null) {
/* 428 */       return "";
/*     */     }
/* 430 */     if (desc.length() <= 500) {
/* 431 */       return desc;
/*     */     }
/* 433 */     return desc.substring(0, 500) + "]...[" + desc.substring(desc.length() - 500);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String _quotedString(String desc) {
/* 440 */     if (desc == null) {
/* 441 */       return "[N/A]";
/*     */     }
/*     */     
/* 444 */     return String.format("\"%s\"", new Object[] { _truncate(desc) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String _colonConcat(String msgBase, String extra) {
/* 451 */     if (extra == null) {
/* 452 */       return msgBase;
/*     */     }
/* 454 */     return msgBase + ": " + extra;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String _desc(String desc) {
/* 461 */     if (desc == null) {
/* 462 */       return "[N/A]";
/*     */     }
/*     */     
/* 465 */     return _truncate(desc);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\DatabindContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */