/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude;
/*     */ import com.fasterxml.jackson.core.FormatFeature;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.json.JsonWriteFeature;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.Instantiatable;
/*     */ import com.fasterxml.jackson.databind.cfg.BaseSettings;
/*     */ import com.fasterxml.jackson.databind.cfg.ConfigOverrides;
/*     */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfigBase;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SerializationConfig
/*     */   extends MapperConfigBase<SerializationFeature, SerializationConfig>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  38 */   protected static final PrettyPrinter DEFAULT_PRETTY_PRINTER = (PrettyPrinter)new DefaultPrettyPrinter();
/*     */ 
/*     */   
/*  41 */   private static final int SER_FEATURE_DEFAULTS = collectFeatureDefaults(SerializationFeature.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final FilterProvider _filterProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final PrettyPrinter _defaultPrettyPrinter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _serFeatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _generatorFeatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _generatorFeaturesToChange;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _formatWriteFeatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _formatWriteFeaturesToChange;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializationConfig(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides) {
/* 118 */     super(base, str, mixins, rootNames, configOverrides);
/* 119 */     this._serFeatures = SER_FEATURE_DEFAULTS;
/* 120 */     this._filterProvider = null;
/* 121 */     this._defaultPrettyPrinter = DEFAULT_PRETTY_PRINTER;
/* 122 */     this._generatorFeatures = 0;
/* 123 */     this._generatorFeaturesToChange = 0;
/* 124 */     this._formatWriteFeatures = 0;
/* 125 */     this._formatWriteFeaturesToChange = 0;
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
/*     */   protected SerializationConfig(SerializationConfig src, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides) {
/* 137 */     super(src, mixins, rootNames, configOverrides);
/* 138 */     this._serFeatures = src._serFeatures;
/* 139 */     this._filterProvider = src._filterProvider;
/* 140 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 141 */     this._generatorFeatures = src._generatorFeatures;
/* 142 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 143 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 144 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
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
/*     */   private SerializationConfig(SerializationConfig src, SubtypeResolver str) {
/* 156 */     super(src, str);
/* 157 */     this._serFeatures = src._serFeatures;
/* 158 */     this._filterProvider = src._filterProvider;
/* 159 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 160 */     this._generatorFeatures = src._generatorFeatures;
/* 161 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 162 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 163 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, int mapperFeatures, int serFeatures, int generatorFeatures, int generatorFeatureMask, int formatFeatures, int formatFeaturesMask) {
/* 171 */     super(src, mapperFeatures);
/* 172 */     this._serFeatures = serFeatures;
/* 173 */     this._filterProvider = src._filterProvider;
/* 174 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 175 */     this._generatorFeatures = generatorFeatures;
/* 176 */     this._generatorFeaturesToChange = generatorFeatureMask;
/* 177 */     this._formatWriteFeatures = formatFeatures;
/* 178 */     this._formatWriteFeaturesToChange = formatFeaturesMask;
/*     */   }
/*     */ 
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, BaseSettings base) {
/* 183 */     super(src, base);
/* 184 */     this._serFeatures = src._serFeatures;
/* 185 */     this._filterProvider = src._filterProvider;
/* 186 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 187 */     this._generatorFeatures = src._generatorFeatures;
/* 188 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 189 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 190 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, FilterProvider filters) {
/* 195 */     super(src);
/* 196 */     this._serFeatures = src._serFeatures;
/* 197 */     this._filterProvider = filters;
/* 198 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 199 */     this._generatorFeatures = src._generatorFeatures;
/* 200 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 201 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 202 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, Class<?> view) {
/* 207 */     super(src, view);
/* 208 */     this._serFeatures = src._serFeatures;
/* 209 */     this._filterProvider = src._filterProvider;
/* 210 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 211 */     this._generatorFeatures = src._generatorFeatures;
/* 212 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 213 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 214 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private SerializationConfig(SerializationConfig src, PropertyName rootName) {
/* 219 */     super(src, rootName);
/* 220 */     this._serFeatures = src._serFeatures;
/* 221 */     this._filterProvider = src._filterProvider;
/* 222 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 223 */     this._generatorFeatures = src._generatorFeatures;
/* 224 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 225 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 226 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SerializationConfig(SerializationConfig src, ContextAttributes attrs) {
/* 234 */     super(src, attrs);
/* 235 */     this._serFeatures = src._serFeatures;
/* 236 */     this._filterProvider = src._filterProvider;
/* 237 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 238 */     this._generatorFeatures = src._generatorFeatures;
/* 239 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 240 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 241 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SerializationConfig(SerializationConfig src, SimpleMixInResolver mixins) {
/* 249 */     super(src, mixins);
/* 250 */     this._serFeatures = src._serFeatures;
/* 251 */     this._filterProvider = src._filterProvider;
/* 252 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/* 253 */     this._generatorFeatures = src._generatorFeatures;
/* 254 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 255 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 256 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SerializationConfig(SerializationConfig src, PrettyPrinter defaultPP) {
/* 264 */     super(src);
/* 265 */     this._serFeatures = src._serFeatures;
/* 266 */     this._filterProvider = src._filterProvider;
/* 267 */     this._defaultPrettyPrinter = defaultPP;
/* 268 */     this._generatorFeatures = src._generatorFeatures;
/* 269 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/* 270 */     this._formatWriteFeatures = src._formatWriteFeatures;
/* 271 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final SerializationConfig _withBase(BaseSettings newBase) {
/* 282 */     return (this._base == newBase) ? this : new SerializationConfig(this, newBase);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final SerializationConfig _withMapperFeatures(int mapperFeatures) {
/* 287 */     return new SerializationConfig(this, mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializationConfig withRootName(PropertyName rootName) {
/* 294 */     if (rootName == null) {
/* 295 */       if (this._rootName == null) {
/* 296 */         return this;
/*     */       }
/* 298 */     } else if (rootName.equals(this._rootName)) {
/* 299 */       return this;
/*     */     } 
/* 301 */     return new SerializationConfig(this, rootName);
/*     */   }
/*     */ 
/*     */   
/*     */   public SerializationConfig with(SubtypeResolver str) {
/* 306 */     return (str == this._subtypeResolver) ? this : new SerializationConfig(this, str);
/*     */   }
/*     */ 
/*     */   
/*     */   public SerializationConfig withView(Class<?> view) {
/* 311 */     return (this._view == view) ? this : new SerializationConfig(this, view);
/*     */   }
/*     */ 
/*     */   
/*     */   public SerializationConfig with(ContextAttributes attrs) {
/* 316 */     return (attrs == this._attributes) ? this : new SerializationConfig(this, attrs);
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
/*     */   public SerializationConfig with(DateFormat df) {
/* 332 */     SerializationConfig cfg = (SerializationConfig)super.with(df);
/*     */     
/* 334 */     if (df == null) {
/* 335 */       return cfg.with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
/*     */     }
/* 337 */     return cfg.without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
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
/*     */   public SerializationConfig with(SerializationFeature feature) {
/* 352 */     int newSerFeatures = this._serFeatures | feature.getMask();
/* 353 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig with(SerializationFeature first, SerializationFeature... features) {
/* 365 */     int newSerFeatures = this._serFeatures | first.getMask();
/* 366 */     for (SerializationFeature f : features) {
/* 367 */       newSerFeatures |= f.getMask();
/*     */     }
/* 369 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig withFeatures(SerializationFeature... features) {
/* 381 */     int newSerFeatures = this._serFeatures;
/* 382 */     for (SerializationFeature f : features) {
/* 383 */       newSerFeatures |= f.getMask();
/*     */     }
/* 385 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig without(SerializationFeature feature) {
/* 397 */     int newSerFeatures = this._serFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 398 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig without(SerializationFeature first, SerializationFeature... features) {
/* 410 */     int newSerFeatures = this._serFeatures & (first.getMask() ^ 0xFFFFFFFF);
/* 411 */     for (SerializationFeature f : features) {
/* 412 */       newSerFeatures &= f.getMask() ^ 0xFFFFFFFF;
/*     */     }
/* 414 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig withoutFeatures(SerializationFeature... features) {
/* 426 */     int newSerFeatures = this._serFeatures;
/* 427 */     for (SerializationFeature f : features) {
/* 428 */       newSerFeatures &= f.getMask() ^ 0xFFFFFFFF;
/*     */     }
/* 430 */     return (newSerFeatures == this._serFeatures) ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig with(JsonGenerator.Feature feature) {
/* 449 */     int newSet = this._generatorFeatures | feature.getMask();
/* 450 */     int newMask = this._generatorFeaturesToChange | feature.getMask();
/* 451 */     return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig withFeatures(JsonGenerator.Feature... features) {
/* 465 */     int newSet = this._generatorFeatures;
/* 466 */     int newMask = this._generatorFeaturesToChange;
/* 467 */     for (JsonGenerator.Feature f : features) {
/* 468 */       int mask = f.getMask();
/* 469 */       newSet |= mask;
/* 470 */       newMask |= mask;
/*     */     } 
/* 472 */     return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig without(JsonGenerator.Feature feature) {
/* 486 */     int newSet = this._generatorFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 487 */     int newMask = this._generatorFeaturesToChange | feature.getMask();
/* 488 */     return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig withoutFeatures(JsonGenerator.Feature... features) {
/* 502 */     int newSet = this._generatorFeatures;
/* 503 */     int newMask = this._generatorFeaturesToChange;
/* 504 */     for (JsonGenerator.Feature f : features) {
/* 505 */       int mask = f.getMask();
/* 506 */       newSet &= mask ^ 0xFFFFFFFF;
/* 507 */       newMask |= mask;
/*     */     } 
/* 509 */     return (this._generatorFeatures == newSet && this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   public SerializationConfig with(FormatFeature feature) {
/* 529 */     if (feature instanceof JsonWriteFeature) {
/* 530 */       return _withJsonWriteFeatures(new FormatFeature[] { feature });
/*     */     }
/* 532 */     int newSet = this._formatWriteFeatures | feature.getMask();
/* 533 */     int newMask = this._formatWriteFeaturesToChange | feature.getMask();
/* 534 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
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
/*     */   public SerializationConfig withFeatures(FormatFeature... features) {
/* 549 */     if (features.length > 0 && features[0] instanceof JsonWriteFeature) {
/* 550 */       return _withJsonWriteFeatures(features);
/*     */     }
/* 552 */     int newSet = this._formatWriteFeatures;
/* 553 */     int newMask = this._formatWriteFeaturesToChange;
/* 554 */     for (FormatFeature f : features) {
/* 555 */       int mask = f.getMask();
/* 556 */       newSet |= mask;
/* 557 */       newMask |= mask;
/*     */     } 
/* 559 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
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
/*     */   public SerializationConfig without(FormatFeature feature) {
/* 574 */     if (feature instanceof JsonWriteFeature) {
/* 575 */       return _withoutJsonWriteFeatures(new FormatFeature[] { feature });
/*     */     }
/* 577 */     int newSet = this._formatWriteFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 578 */     int newMask = this._formatWriteFeaturesToChange | feature.getMask();
/* 579 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
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
/*     */   public SerializationConfig withoutFeatures(FormatFeature... features) {
/* 593 */     if (features.length > 0 && features[0] instanceof JsonWriteFeature) {
/* 594 */       return _withoutJsonWriteFeatures(features);
/*     */     }
/* 596 */     int newSet = this._formatWriteFeatures;
/* 597 */     int newMask = this._formatWriteFeaturesToChange;
/* 598 */     for (FormatFeature f : features) {
/* 599 */       int mask = f.getMask();
/* 600 */       newSet &= mask ^ 0xFFFFFFFF;
/* 601 */       newMask |= mask;
/*     */     } 
/* 603 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SerializationConfig _withJsonWriteFeatures(FormatFeature... features) {
/* 611 */     int parserSet = this._generatorFeatures;
/* 612 */     int parserMask = this._generatorFeaturesToChange;
/* 613 */     int newSet = this._formatWriteFeatures;
/* 614 */     int newMask = this._formatWriteFeaturesToChange;
/* 615 */     for (FormatFeature f : features) {
/* 616 */       int mask = f.getMask();
/* 617 */       newSet |= mask;
/* 618 */       newMask |= mask;
/*     */       
/* 620 */       if (f instanceof JsonWriteFeature) {
/* 621 */         JsonGenerator.Feature oldF = ((JsonWriteFeature)f).mappedFeature();
/* 622 */         if (oldF != null) {
/* 623 */           int pmask = oldF.getMask();
/* 624 */           parserSet |= pmask;
/* 625 */           parserMask |= pmask;
/*     */         } 
/*     */       } 
/*     */     } 
/* 629 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask && this._generatorFeatures == parserSet && this._generatorFeaturesToChange == parserMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, parserSet, parserMask, newSet, newMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SerializationConfig _withoutJsonWriteFeatures(FormatFeature... features) {
/* 638 */     int parserSet = this._generatorFeatures;
/* 639 */     int parserMask = this._generatorFeaturesToChange;
/* 640 */     int newSet = this._formatWriteFeatures;
/* 641 */     int newMask = this._formatWriteFeaturesToChange;
/* 642 */     for (FormatFeature f : features) {
/* 643 */       int mask = f.getMask();
/* 644 */       newSet &= mask ^ 0xFFFFFFFF;
/* 645 */       newMask |= mask;
/*     */       
/* 647 */       if (f instanceof JsonWriteFeature) {
/* 648 */         JsonGenerator.Feature oldF = ((JsonWriteFeature)f).mappedFeature();
/* 649 */         if (oldF != null) {
/* 650 */           int pmask = oldF.getMask();
/* 651 */           parserSet &= pmask ^ 0xFFFFFFFF;
/* 652 */           parserMask |= pmask;
/*     */         } 
/*     */       } 
/*     */     } 
/* 656 */     return (this._formatWriteFeatures == newSet && this._formatWriteFeaturesToChange == newMask && this._generatorFeatures == parserSet && this._generatorFeaturesToChange == parserMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, parserSet, parserMask, newSet, newMask);
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
/*     */   public SerializationConfig withFilters(FilterProvider filterProvider) {
/* 670 */     return (filterProvider == this._filterProvider) ? this : new SerializationConfig(this, filterProvider);
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
/*     */   @Deprecated
/*     */   public SerializationConfig withPropertyInclusion(JsonInclude.Value incl) {
/* 683 */     this._configOverrides.setDefaultInclusion(incl);
/* 684 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SerializationConfig withDefaultPrettyPrinter(PrettyPrinter pp) {
/* 691 */     return (this._defaultPrettyPrinter == pp) ? this : new SerializationConfig(this, pp);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrettyPrinter constructDefaultPrettyPrinter() {
/* 701 */     PrettyPrinter pp = this._defaultPrettyPrinter;
/* 702 */     if (pp instanceof Instantiatable) {
/* 703 */       pp = (PrettyPrinter)((Instantiatable)pp).createInstance();
/*     */     }
/* 705 */     return pp;
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
/*     */   public void initialize(JsonGenerator g) {
/* 723 */     if (SerializationFeature.INDENT_OUTPUT.enabledIn(this._serFeatures))
/*     */     {
/* 725 */       if (g.getPrettyPrinter() == null) {
/* 726 */         PrettyPrinter pp = constructDefaultPrettyPrinter();
/* 727 */         if (pp != null) {
/* 728 */           g.setPrettyPrinter(pp);
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 733 */     boolean useBigDec = SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN.enabledIn(this._serFeatures);
/*     */     
/* 735 */     int mask = this._generatorFeaturesToChange;
/* 736 */     if (mask != 0 || useBigDec) {
/* 737 */       int newFlags = this._generatorFeatures;
/*     */       
/* 739 */       if (useBigDec) {
/* 740 */         int f = JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN.getMask();
/* 741 */         newFlags |= f;
/* 742 */         mask |= f;
/*     */       } 
/* 744 */       g.overrideStdFeatures(newFlags, mask);
/*     */     } 
/* 746 */     if (this._formatWriteFeaturesToChange != 0) {
/* 747 */       g.overrideFormatFeatures(this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*     */   @Deprecated
/*     */   public JsonInclude.Include getSerializationInclusion() {
/* 763 */     JsonInclude.Include incl = getDefaultPropertyInclusion().getValueInclusion();
/* 764 */     return (incl == JsonInclude.Include.USE_DEFAULTS) ? JsonInclude.Include.ALWAYS : incl;
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
/*     */   public boolean useRootWrapping() {
/* 776 */     if (this._rootName != null) {
/* 777 */       return !this._rootName.isEmpty();
/*     */     }
/* 779 */     return isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/*     */   }
/*     */   
/*     */   public final boolean isEnabled(SerializationFeature f) {
/* 783 */     return ((this._serFeatures & f.getMask()) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isEnabled(JsonGenerator.Feature f, JsonFactory factory) {
/* 794 */     int mask = f.getMask();
/* 795 */     if ((this._generatorFeaturesToChange & mask) != 0) {
/* 796 */       return ((this._generatorFeatures & f.getMask()) != 0);
/*     */     }
/* 798 */     return factory.isEnabled(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasSerializationFeatures(int featureMask) {
/* 808 */     return ((this._serFeatures & featureMask) == featureMask);
/*     */   }
/*     */   
/*     */   public final int getSerializationFeatures() {
/* 812 */     return this._serFeatures;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FilterProvider getFilterProvider() {
/* 822 */     return this._filterProvider;
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
/*     */   public PrettyPrinter getDefaultPrettyPrinter() {
/* 836 */     return this._defaultPrettyPrinter;
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
/*     */   public <T extends BeanDescription> T introspect(JavaType type) {
/* 851 */     return (T)getClassIntrospector().forSerialization(this, type, (ClassIntrospector.MixInResolver)this);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\SerializationConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */