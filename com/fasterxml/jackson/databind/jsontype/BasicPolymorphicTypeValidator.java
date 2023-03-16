/*     */ package com.fasterxml.jackson.databind.jsontype;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicPolymorphicTypeValidator
/*     */   extends PolymorphicTypeValidator.Base
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Set<Class<?>> _invalidBaseTypes;
/*     */   protected final TypeMatcher[] _baseTypeMatchers;
/*     */   protected final NameMatcher[] _subTypeNameMatchers;
/*     */   protected final TypeMatcher[] _subClassMatchers;
/*     */   
/*     */   protected static abstract class TypeMatcher
/*     */   {
/*     */     public abstract boolean match(Class<?> param1Class);
/*     */   }
/*     */   
/*     */   protected static abstract class NameMatcher
/*     */   {
/*     */     public abstract boolean match(String param1String);
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     protected Set<Class<?>> _invalidBaseTypes;
/*     */     protected List<BasicPolymorphicTypeValidator.TypeMatcher> _baseTypeMatchers;
/*     */     protected List<BasicPolymorphicTypeValidator.NameMatcher> _subTypeNameMatchers;
/*     */     protected List<BasicPolymorphicTypeValidator.TypeMatcher> _subTypeClassMatchers;
/*     */     
/*     */     public Builder allowIfBaseType(final Class<?> baseOfBase) {
/* 100 */       return _appendBaseMatcher(new BasicPolymorphicTypeValidator.TypeMatcher()
/*     */           {
/*     */             public boolean match(Class<?> clazz) {
/* 103 */               return baseOfBase.isAssignableFrom(clazz);
/*     */             }
/*     */           });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder allowIfBaseType(final Pattern patternForBase) {
/* 126 */       return _appendBaseMatcher(new BasicPolymorphicTypeValidator.TypeMatcher()
/*     */           {
/*     */             public boolean match(Class<?> clazz) {
/* 129 */               return patternForBase.matcher(clazz.getName()).matches();
/*     */             }
/*     */           });
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
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder allowIfBaseType(final String prefixForBase) {
/* 146 */       return _appendBaseMatcher(new BasicPolymorphicTypeValidator.TypeMatcher()
/*     */           {
/*     */             public boolean match(Class<?> clazz) {
/* 149 */               return clazz.getName().startsWith(prefixForBase);
/*     */             }
/*     */           });
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder denyForExactBaseType(Class<?> baseTypeToDeny) {
/* 167 */       if (this._invalidBaseTypes == null) {
/* 168 */         this._invalidBaseTypes = new HashSet<>();
/*     */       }
/* 170 */       this._invalidBaseTypes.add(baseTypeToDeny);
/* 171 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder allowIfSubType(final Class<?> subTypeBase) {
/* 188 */       return _appendSubClassMatcher(new BasicPolymorphicTypeValidator.TypeMatcher()
/*     */           {
/*     */             public boolean match(Class<?> clazz) {
/* 191 */               return subTypeBase.isAssignableFrom(clazz);
/*     */             }
/*     */           });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder allowIfSubType(final Pattern patternForSubType) {
/* 213 */       return _appendSubNameMatcher(new BasicPolymorphicTypeValidator.NameMatcher()
/*     */           {
/*     */             public boolean match(String clazzName) {
/* 216 */               return patternForSubType.matcher(clazzName).matches();
/*     */             }
/*     */           });
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
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder allowIfSubType(final String prefixForSubType) {
/* 233 */       return _appendSubNameMatcher(new BasicPolymorphicTypeValidator.NameMatcher()
/*     */           {
/*     */             public boolean match(String clazzName) {
/* 236 */               return clazzName.startsWith(prefixForSubType);
/*     */             }
/*     */           });
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder allowIfSubTypeIsArray() {
/* 256 */       return _appendSubClassMatcher(new BasicPolymorphicTypeValidator.TypeMatcher()
/*     */           {
/*     */             public boolean match(Class<?> clazz) {
/* 259 */               return clazz.isArray();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public BasicPolymorphicTypeValidator build() {
/* 265 */       return new BasicPolymorphicTypeValidator(this._invalidBaseTypes, (this._baseTypeMatchers == null) ? null : this._baseTypeMatchers
/* 266 */           .<BasicPolymorphicTypeValidator.TypeMatcher>toArray(new BasicPolymorphicTypeValidator.TypeMatcher[0]), (this._subTypeNameMatchers == null) ? null : this._subTypeNameMatchers
/* 267 */           .<BasicPolymorphicTypeValidator.NameMatcher>toArray(new BasicPolymorphicTypeValidator.NameMatcher[0]), (this._subTypeClassMatchers == null) ? null : this._subTypeClassMatchers
/* 268 */           .<BasicPolymorphicTypeValidator.TypeMatcher>toArray(new BasicPolymorphicTypeValidator.TypeMatcher[0]));
/*     */     }
/*     */ 
/*     */     
/*     */     protected Builder _appendBaseMatcher(BasicPolymorphicTypeValidator.TypeMatcher matcher) {
/* 273 */       if (this._baseTypeMatchers == null) {
/* 274 */         this._baseTypeMatchers = new ArrayList<>();
/*     */       }
/* 276 */       this._baseTypeMatchers.add(matcher);
/* 277 */       return this;
/*     */     }
/*     */     
/*     */     protected Builder _appendSubNameMatcher(BasicPolymorphicTypeValidator.NameMatcher matcher) {
/* 281 */       if (this._subTypeNameMatchers == null) {
/* 282 */         this._subTypeNameMatchers = new ArrayList<>();
/*     */       }
/* 284 */       this._subTypeNameMatchers.add(matcher);
/* 285 */       return this;
/*     */     }
/*     */     
/*     */     protected Builder _appendSubClassMatcher(BasicPolymorphicTypeValidator.TypeMatcher matcher) {
/* 289 */       if (this._subTypeClassMatchers == null) {
/* 290 */         this._subTypeClassMatchers = new ArrayList<>();
/*     */       }
/* 292 */       this._subTypeClassMatchers.add(matcher);
/* 293 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BasicPolymorphicTypeValidator(Set<Class<?>> invalidBaseTypes, TypeMatcher[] baseTypeMatchers, NameMatcher[] subTypeNameMatchers, TypeMatcher[] subClassMatchers) {
/* 331 */     this._invalidBaseTypes = invalidBaseTypes;
/* 332 */     this._baseTypeMatchers = baseTypeMatchers;
/* 333 */     this._subTypeNameMatchers = subTypeNameMatchers;
/* 334 */     this._subClassMatchers = subClassMatchers;
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/* 338 */     return new Builder();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PolymorphicTypeValidator.Validity validateBaseType(MapperConfig<?> ctxt, JavaType baseType) {
/* 344 */     Class<?> rawBase = baseType.getRawClass();
/* 345 */     if (this._invalidBaseTypes != null && 
/* 346 */       this._invalidBaseTypes.contains(rawBase)) {
/* 347 */       return PolymorphicTypeValidator.Validity.DENIED;
/*     */     }
/*     */     
/* 350 */     if (this._baseTypeMatchers != null) {
/* 351 */       for (TypeMatcher m : this._baseTypeMatchers) {
/* 352 */         if (m.match(rawBase)) {
/* 353 */           return PolymorphicTypeValidator.Validity.ALLOWED;
/*     */         }
/*     */       } 
/*     */     }
/* 357 */     return PolymorphicTypeValidator.Validity.INDETERMINATE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolymorphicTypeValidator.Validity validateSubClassName(MapperConfig<?> ctxt, JavaType baseType, String subClassName) throws JsonMappingException {
/* 366 */     if (this._subTypeNameMatchers != null) {
/* 367 */       for (NameMatcher m : this._subTypeNameMatchers) {
/* 368 */         if (m.match(subClassName)) {
/* 369 */           return PolymorphicTypeValidator.Validity.ALLOWED;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 374 */     return PolymorphicTypeValidator.Validity.INDETERMINATE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PolymorphicTypeValidator.Validity validateSubType(MapperConfig<?> ctxt, JavaType baseType, JavaType subType) throws JsonMappingException {
/* 382 */     if (this._subClassMatchers != null) {
/* 383 */       Class<?> subClass = subType.getRawClass();
/* 384 */       for (TypeMatcher m : this._subClassMatchers) {
/* 385 */         if (m.match(subClass)) {
/* 386 */           return PolymorphicTypeValidator.Validity.ALLOWED;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 391 */     return PolymorphicTypeValidator.Validity.INDETERMINATE;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\jsontype\BasicPolymorphicTypeValidator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */