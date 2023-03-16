/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.annotation.JsonSetter;
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.StreamReadFeature;
/*     */ import com.fasterxml.jackson.core.StreamWriteFeature;
/*     */ import com.fasterxml.jackson.core.TokenStreamFactory;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.InjectableValues;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.Module;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.ser.SerializerFactory;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.DateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public abstract class MapperBuilder<M extends ObjectMapper, B extends MapperBuilder<M, B>> {
/*     */   protected final M _mapper;
/*     */   
/*     */   protected MapperBuilder(M mapper) {
/*  46 */     this._mapper = mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public M build() {
/*  57 */     return this._mapper;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled(MapperFeature f) {
/*  67 */     return this._mapper.isEnabled(f);
/*     */   }
/*     */   public boolean isEnabled(DeserializationFeature f) {
/*  70 */     return this._mapper.isEnabled(f);
/*     */   }
/*     */   public boolean isEnabled(SerializationFeature f) {
/*  73 */     return this._mapper.isEnabled(f);
/*     */   }
/*     */   
/*     */   public boolean isEnabled(JsonParser.Feature f) {
/*  77 */     return this._mapper.isEnabled(f);
/*     */   }
/*     */   public boolean isEnabled(JsonGenerator.Feature f) {
/*  80 */     return this._mapper.isEnabled(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenStreamFactory streamFactory() {
/*  90 */     return (TokenStreamFactory)this._mapper.tokenStreamFactory();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B enable(MapperFeature... features) {
/* 100 */     this._mapper.enable(features);
/* 101 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(MapperFeature... features) {
/* 105 */     this._mapper.disable(features);
/* 106 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(MapperFeature feature, boolean state) {
/* 110 */     this._mapper.configure(feature, state);
/* 111 */     return _this();
/*     */   }
/*     */   
/*     */   public B enable(SerializationFeature... features) {
/* 115 */     for (SerializationFeature f : features) {
/* 116 */       this._mapper.enable(f);
/*     */     }
/* 118 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(SerializationFeature... features) {
/* 122 */     for (SerializationFeature f : features) {
/* 123 */       this._mapper.disable(f);
/*     */     }
/* 125 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(SerializationFeature feature, boolean state) {
/* 129 */     this._mapper.configure(feature, state);
/* 130 */     return _this();
/*     */   }
/*     */   
/*     */   public B enable(DeserializationFeature... features) {
/* 134 */     for (DeserializationFeature f : features) {
/* 135 */       this._mapper.enable(f);
/*     */     }
/* 137 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(DeserializationFeature... features) {
/* 141 */     for (DeserializationFeature f : features) {
/* 142 */       this._mapper.disable(f);
/*     */     }
/* 144 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(DeserializationFeature feature, boolean state) {
/* 148 */     this._mapper.configure(feature, state);
/* 149 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B enable(JsonParser.Feature... features) {
/* 159 */     this._mapper.enable(features);
/* 160 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(JsonParser.Feature... features) {
/* 164 */     this._mapper.disable(features);
/* 165 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(JsonParser.Feature feature, boolean state) {
/* 169 */     this._mapper.configure(feature, state);
/* 170 */     return _this();
/*     */   }
/*     */   
/*     */   public B enable(JsonGenerator.Feature... features) {
/* 174 */     this._mapper.enable(features);
/* 175 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(JsonGenerator.Feature... features) {
/* 179 */     this._mapper.disable(features);
/* 180 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(JsonGenerator.Feature feature, boolean state) {
/* 184 */     this._mapper.configure(feature, state);
/* 185 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B enable(StreamReadFeature... features) {
/* 195 */     for (StreamReadFeature f : features) {
/* 196 */       this._mapper.enable(new JsonParser.Feature[] { f.mappedFeature() });
/*     */     } 
/* 198 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(StreamReadFeature... features) {
/* 202 */     for (StreamReadFeature f : features) {
/* 203 */       this._mapper.disable(new JsonParser.Feature[] { f.mappedFeature() });
/*     */     } 
/* 205 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(StreamReadFeature feature, boolean state) {
/* 209 */     this._mapper.configure(feature.mappedFeature(), state);
/* 210 */     return _this();
/*     */   }
/*     */   
/*     */   public B enable(StreamWriteFeature... features) {
/* 214 */     for (StreamWriteFeature f : features) {
/* 215 */       this._mapper.enable(new JsonGenerator.Feature[] { f.mappedFeature() });
/*     */     } 
/* 217 */     return _this();
/*     */   }
/*     */   
/*     */   public B disable(StreamWriteFeature... features) {
/* 221 */     for (StreamWriteFeature f : features) {
/* 222 */       this._mapper.disable(new JsonGenerator.Feature[] { f.mappedFeature() });
/*     */     } 
/* 224 */     return _this();
/*     */   }
/*     */   
/*     */   public B configure(StreamWriteFeature feature, boolean state) {
/* 228 */     this._mapper.configure(feature.mappedFeature(), state);
/* 229 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B addModule(Module module) {
/* 240 */     this._mapper.registerModule(module);
/* 241 */     return _this();
/*     */   }
/*     */ 
/*     */   
/*     */   public B addModules(Module... modules) {
/* 246 */     for (Module module : modules) {
/* 247 */       addModule(module);
/*     */     }
/* 249 */     return _this();
/*     */   }
/*     */ 
/*     */   
/*     */   public B addModules(Iterable<? extends Module> modules) {
/* 254 */     for (Module module : modules) {
/* 255 */       addModule(module);
/*     */     }
/* 257 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Module> findModules() {
/* 268 */     return findModules(null);
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
/*     */   public static List<Module> findModules(ClassLoader classLoader) {
/* 280 */     ArrayList<Module> modules = new ArrayList<>();
/* 281 */     ServiceLoader<Module> loader = secureGetServiceLoader(Module.class, classLoader);
/* 282 */     for (Module module : loader) {
/* 283 */       modules.add(module);
/*     */     }
/* 285 */     return modules;
/*     */   }
/*     */   
/*     */   private static <T> ServiceLoader<T> secureGetServiceLoader(final Class<T> clazz, final ClassLoader classLoader) {
/* 289 */     SecurityManager sm = System.getSecurityManager();
/* 290 */     if (sm == null) {
/* 291 */       return (classLoader == null) ? 
/* 292 */         ServiceLoader.<T>load(clazz) : ServiceLoader.<T>load(clazz, classLoader);
/*     */     }
/* 294 */     return AccessController.<ServiceLoader<T>>doPrivileged((PrivilegedAction)new PrivilegedAction<ServiceLoader<ServiceLoader<T>>>()
/*     */         {
/*     */           public ServiceLoader<T> run() {
/* 297 */             return (classLoader == null) ? 
/* 298 */               ServiceLoader.<T>load(clazz) : ServiceLoader.<T>load(clazz, classLoader);
/*     */           }
/*     */         });
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
/*     */   public B findAndAddModules() {
/* 314 */     return addModules(findModules());
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
/*     */   public B annotationIntrospector(AnnotationIntrospector intr) {
/* 334 */     this._mapper.setAnnotationIntrospector(intr);
/* 335 */     return _this();
/*     */   }
/*     */   
/*     */   public B nodeFactory(JsonNodeFactory f) {
/* 339 */     this._mapper.setNodeFactory(f);
/* 340 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B typeFactory(TypeFactory f) {
/* 350 */     this._mapper.setTypeFactory(f);
/* 351 */     return _this();
/*     */   }
/*     */   
/*     */   public B subtypeResolver(SubtypeResolver r) {
/* 355 */     this._mapper.setSubtypeResolver(r);
/* 356 */     return _this();
/*     */   }
/*     */   
/*     */   public B visibility(VisibilityChecker<?> vc) {
/* 360 */     this._mapper.setVisibility(vc);
/* 361 */     return _this();
/*     */   }
/*     */   
/*     */   public B visibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility) {
/* 365 */     this._mapper.setVisibility(forMethod, visibility);
/* 366 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B handlerInstantiator(HandlerInstantiator hi) {
/* 377 */     this._mapper.setHandlerInstantiator(hi);
/* 378 */     return _this();
/*     */   }
/*     */   
/*     */   public B propertyNamingStrategy(PropertyNamingStrategy s) {
/* 382 */     this._mapper.setPropertyNamingStrategy(s);
/* 383 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B serializerFactory(SerializerFactory f) {
/* 393 */     this._mapper.setSerializerFactory(f);
/* 394 */     return _this();
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
/*     */   public B filterProvider(FilterProvider prov) {
/* 406 */     this._mapper.setFilterProvider(prov);
/* 407 */     return _this();
/*     */   }
/*     */   
/*     */   public B defaultPrettyPrinter(PrettyPrinter pp) {
/* 411 */     this._mapper.setDefaultPrettyPrinter(pp);
/* 412 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B injectableValues(InjectableValues v) {
/* 422 */     this._mapper.setInjectableValues(v);
/* 423 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B addHandler(DeserializationProblemHandler h) {
/* 432 */     this._mapper.addHandler(h);
/* 433 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B clearProblemHandlers() {
/* 441 */     this._mapper.clearProblemHandlers();
/* 442 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B defaultSetterInfo(JsonSetter.Value v) {
/* 452 */     this._mapper.setDefaultSetterInfo(v);
/* 453 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B defaultMergeable(Boolean b) {
/* 462 */     this._mapper.setDefaultMergeable(b);
/* 463 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B defaultLeniency(Boolean b) {
/* 472 */     this._mapper.setDefaultLeniency(b);
/* 473 */     return _this();
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
/*     */   public B defaultDateFormat(DateFormat df) {
/* 489 */     this._mapper.setDateFormat(df);
/* 490 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B defaultTimeZone(TimeZone tz) {
/* 498 */     this._mapper.setTimeZone(tz);
/* 499 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B defaultLocale(Locale locale) {
/* 507 */     this._mapper.setLocale(locale);
/* 508 */     return _this();
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
/*     */   public B defaultBase64Variant(Base64Variant v) {
/* 526 */     this._mapper.setBase64Variant(v);
/* 527 */     return _this();
/*     */   }
/*     */   
/*     */   public B serializationInclusion(JsonInclude.Include incl) {
/* 531 */     this._mapper.setSerializationInclusion(incl);
/* 532 */     return _this();
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
/*     */   public B addMixIn(Class<?> target, Class<?> mixinSource) {
/* 557 */     this._mapper.addMixIn(target, mixinSource);
/* 558 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B registerSubtypes(Class<?>... subtypes) {
/* 568 */     this._mapper.registerSubtypes(subtypes);
/* 569 */     return _this();
/*     */   }
/*     */   
/*     */   public B registerSubtypes(NamedType... subtypes) {
/* 573 */     this._mapper.registerSubtypes(subtypes);
/* 574 */     return _this();
/*     */   }
/*     */   
/*     */   public B registerSubtypes(Collection<Class<?>> subtypes) {
/* 578 */     this._mapper.registerSubtypes(subtypes);
/* 579 */     return _this();
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
/*     */   public B polymorphicTypeValidator(PolymorphicTypeValidator ptv) {
/* 596 */     this._mapper.setPolymorphicTypeValidator(ptv);
/* 597 */     return _this();
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
/*     */   public B activateDefaultTyping(PolymorphicTypeValidator subtypeValidator) {
/* 616 */     this._mapper.activateDefaultTyping(subtypeValidator);
/* 617 */     return _this();
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
/*     */   public B activateDefaultTyping(PolymorphicTypeValidator subtypeValidator, ObjectMapper.DefaultTyping dti) {
/* 631 */     this._mapper.activateDefaultTyping(subtypeValidator, dti);
/* 632 */     return _this();
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
/*     */   public B activateDefaultTyping(PolymorphicTypeValidator subtypeValidator, ObjectMapper.DefaultTyping applicability, JsonTypeInfo.As includeAs) {
/* 653 */     this._mapper.activateDefaultTyping(subtypeValidator, applicability, includeAs);
/* 654 */     return _this();
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
/*     */   public B activateDefaultTypingAsProperty(PolymorphicTypeValidator subtypeValidator, ObjectMapper.DefaultTyping applicability, String propertyName) {
/* 671 */     this._mapper.activateDefaultTypingAsProperty(subtypeValidator, applicability, propertyName);
/* 672 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public B deactivateDefaultTyping() {
/* 682 */     this._mapper.deactivateDefaultTyping();
/* 683 */     return _this();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final B _this() {
/* 694 */     return (B)this;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\cfg\MapperBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */