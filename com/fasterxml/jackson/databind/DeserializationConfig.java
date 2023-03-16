/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.core.FormatFeature;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.json.JsonReadFeature;
/*     */ import com.fasterxml.jackson.databind.cfg.BaseSettings;
/*     */ import com.fasterxml.jackson.databind.cfg.ConfigOverrides;
/*     */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfigBase;
/*     */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.util.LinkedNode;
/*     */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ public final class DeserializationConfig
/*     */   extends MapperConfigBase<DeserializationFeature, DeserializationConfig>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 2L;
/*  32 */   private static final int DESER_FEATURE_DEFAULTS = collectFeatureDefaults(DeserializationFeature.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final LinkedNode<DeserializationProblemHandler> _problemHandlers;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonNodeFactory _nodeFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _deserFeatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _parserFeatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _parserFeaturesToChange;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _formatReadFeatures;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final int _formatReadFeaturesToChange;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeserializationConfig(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides) {
/* 106 */     super(base, str, mixins, rootNames, configOverrides);
/* 107 */     this._deserFeatures = DESER_FEATURE_DEFAULTS;
/* 108 */     this._nodeFactory = JsonNodeFactory.instance;
/* 109 */     this._problemHandlers = null;
/* 110 */     this._parserFeatures = 0;
/* 111 */     this._parserFeaturesToChange = 0;
/* 112 */     this._formatReadFeatures = 0;
/* 113 */     this._formatReadFeaturesToChange = 0;
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
/*     */   protected DeserializationConfig(DeserializationConfig src, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides) {
/* 125 */     super(src, mixins, rootNames, configOverrides);
/* 126 */     this._deserFeatures = src._deserFeatures;
/* 127 */     this._problemHandlers = src._problemHandlers;
/* 128 */     this._nodeFactory = src._nodeFactory;
/* 129 */     this._parserFeatures = src._parserFeatures;
/* 130 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 131 */     this._formatReadFeatures = src._formatReadFeatures;
/* 132 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
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
/*     */   private DeserializationConfig(DeserializationConfig src, int mapperFeatures, int deserFeatures, int parserFeatures, int parserFeatureMask, int formatFeatures, int formatFeatureMask) {
/* 147 */     super(src, mapperFeatures);
/* 148 */     this._deserFeatures = deserFeatures;
/* 149 */     this._nodeFactory = src._nodeFactory;
/* 150 */     this._problemHandlers = src._problemHandlers;
/* 151 */     this._parserFeatures = parserFeatures;
/* 152 */     this._parserFeaturesToChange = parserFeatureMask;
/* 153 */     this._formatReadFeatures = formatFeatures;
/* 154 */     this._formatReadFeaturesToChange = formatFeatureMask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, SubtypeResolver str) {
/* 163 */     super(src, str);
/* 164 */     this._deserFeatures = src._deserFeatures;
/* 165 */     this._nodeFactory = src._nodeFactory;
/* 166 */     this._problemHandlers = src._problemHandlers;
/* 167 */     this._parserFeatures = src._parserFeatures;
/* 168 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 169 */     this._formatReadFeatures = src._formatReadFeatures;
/* 170 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, BaseSettings base) {
/* 175 */     super(src, base);
/* 176 */     this._deserFeatures = src._deserFeatures;
/* 177 */     this._nodeFactory = src._nodeFactory;
/* 178 */     this._problemHandlers = src._problemHandlers;
/* 179 */     this._parserFeatures = src._parserFeatures;
/* 180 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 181 */     this._formatReadFeatures = src._formatReadFeatures;
/* 182 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, JsonNodeFactory f) {
/* 187 */     super(src);
/* 188 */     this._deserFeatures = src._deserFeatures;
/* 189 */     this._problemHandlers = src._problemHandlers;
/* 190 */     this._nodeFactory = f;
/* 191 */     this._parserFeatures = src._parserFeatures;
/* 192 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 193 */     this._formatReadFeatures = src._formatReadFeatures;
/* 194 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, LinkedNode<DeserializationProblemHandler> problemHandlers) {
/* 200 */     super(src);
/* 201 */     this._deserFeatures = src._deserFeatures;
/* 202 */     this._problemHandlers = problemHandlers;
/* 203 */     this._nodeFactory = src._nodeFactory;
/* 204 */     this._parserFeatures = src._parserFeatures;
/* 205 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 206 */     this._formatReadFeatures = src._formatReadFeatures;
/* 207 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, PropertyName rootName) {
/* 212 */     super(src, rootName);
/* 213 */     this._deserFeatures = src._deserFeatures;
/* 214 */     this._problemHandlers = src._problemHandlers;
/* 215 */     this._nodeFactory = src._nodeFactory;
/* 216 */     this._parserFeatures = src._parserFeatures;
/* 217 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 218 */     this._formatReadFeatures = src._formatReadFeatures;
/* 219 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, Class<?> view) {
/* 224 */     super(src, view);
/* 225 */     this._deserFeatures = src._deserFeatures;
/* 226 */     this._problemHandlers = src._problemHandlers;
/* 227 */     this._nodeFactory = src._nodeFactory;
/* 228 */     this._parserFeatures = src._parserFeatures;
/* 229 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 230 */     this._formatReadFeatures = src._formatReadFeatures;
/* 231 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DeserializationConfig(DeserializationConfig src, ContextAttributes attrs) {
/* 236 */     super(src, attrs);
/* 237 */     this._deserFeatures = src._deserFeatures;
/* 238 */     this._problemHandlers = src._problemHandlers;
/* 239 */     this._nodeFactory = src._nodeFactory;
/* 240 */     this._parserFeatures = src._parserFeatures;
/* 241 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 242 */     this._formatReadFeatures = src._formatReadFeatures;
/* 243 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */ 
/*     */   
/*     */   protected DeserializationConfig(DeserializationConfig src, SimpleMixInResolver mixins) {
/* 248 */     super(src, mixins);
/* 249 */     this._deserFeatures = src._deserFeatures;
/* 250 */     this._problemHandlers = src._problemHandlers;
/* 251 */     this._nodeFactory = src._nodeFactory;
/* 252 */     this._parserFeatures = src._parserFeatures;
/* 253 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 254 */     this._formatReadFeatures = src._formatReadFeatures;
/* 255 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */   
/*     */   protected BaseSettings getBaseSettings() {
/* 259 */     return this._base;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final DeserializationConfig _withBase(BaseSettings newBase) {
/* 269 */     return (this._base == newBase) ? this : new DeserializationConfig(this, newBase);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final DeserializationConfig _withMapperFeatures(int mapperFeatures) {
/* 274 */     return new DeserializationConfig(this, mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig with(SubtypeResolver str) {
/* 287 */     return (this._subtypeResolver == str) ? this : new DeserializationConfig(this, str);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeserializationConfig withRootName(PropertyName rootName) {
/* 292 */     if (rootName == null) {
/* 293 */       if (this._rootName == null) {
/* 294 */         return this;
/*     */       }
/* 296 */     } else if (rootName.equals(this._rootName)) {
/* 297 */       return this;
/*     */     } 
/* 299 */     return new DeserializationConfig(this, rootName);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeserializationConfig withView(Class<?> view) {
/* 304 */     return (this._view == view) ? this : new DeserializationConfig(this, view);
/*     */   }
/*     */ 
/*     */   
/*     */   public DeserializationConfig with(ContextAttributes attrs) {
/* 309 */     return (attrs == this._attributes) ? this : new DeserializationConfig(this, attrs);
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
/*     */   public DeserializationConfig with(DeserializationFeature feature) {
/* 324 */     int newDeserFeatures = this._deserFeatures | feature.getMask();
/* 325 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig with(DeserializationFeature first, DeserializationFeature... features) {
/* 338 */     int newDeserFeatures = this._deserFeatures | first.getMask();
/* 339 */     for (DeserializationFeature f : features) {
/* 340 */       newDeserFeatures |= f.getMask();
/*     */     }
/* 342 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig withFeatures(DeserializationFeature... features) {
/* 354 */     int newDeserFeatures = this._deserFeatures;
/* 355 */     for (DeserializationFeature f : features) {
/* 356 */       newDeserFeatures |= f.getMask();
/*     */     }
/* 358 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig without(DeserializationFeature feature) {
/* 370 */     int newDeserFeatures = this._deserFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 371 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig without(DeserializationFeature first, DeserializationFeature... features) {
/* 384 */     int newDeserFeatures = this._deserFeatures & (first.getMask() ^ 0xFFFFFFFF);
/* 385 */     for (DeserializationFeature f : features) {
/* 386 */       newDeserFeatures &= f.getMask() ^ 0xFFFFFFFF;
/*     */     }
/* 388 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig withoutFeatures(DeserializationFeature... features) {
/* 400 */     int newDeserFeatures = this._deserFeatures;
/* 401 */     for (DeserializationFeature f : features) {
/* 402 */       newDeserFeatures &= f.getMask() ^ 0xFFFFFFFF;
/*     */     }
/* 404 */     return (newDeserFeatures == this._deserFeatures) ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig with(JsonParser.Feature feature) {
/* 424 */     int newSet = this._parserFeatures | feature.getMask();
/* 425 */     int newMask = this._parserFeaturesToChange | feature.getMask();
/* 426 */     return (this._parserFeatures == newSet && this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig withFeatures(JsonParser.Feature... features) {
/* 440 */     int newSet = this._parserFeatures;
/* 441 */     int newMask = this._parserFeaturesToChange;
/* 442 */     for (JsonParser.Feature f : features) {
/* 443 */       int mask = f.getMask();
/* 444 */       newSet |= mask;
/* 445 */       newMask |= mask;
/*     */     } 
/* 447 */     return (this._parserFeatures == newSet && this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig without(JsonParser.Feature feature) {
/* 461 */     int newSet = this._parserFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 462 */     int newMask = this._parserFeaturesToChange | feature.getMask();
/* 463 */     return (this._parserFeatures == newSet && this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig withoutFeatures(JsonParser.Feature... features) {
/* 477 */     int newSet = this._parserFeatures;
/* 478 */     int newMask = this._parserFeaturesToChange;
/* 479 */     for (JsonParser.Feature f : features) {
/* 480 */       int mask = f.getMask();
/* 481 */       newSet &= mask ^ 0xFFFFFFFF;
/* 482 */       newMask |= mask;
/*     */     } 
/* 484 */     return (this._parserFeatures == newSet && this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig with(FormatFeature feature) {
/* 505 */     if (feature instanceof JsonReadFeature) {
/* 506 */       return _withJsonReadFeatures(new FormatFeature[] { feature });
/*     */     }
/* 508 */     int newSet = this._formatReadFeatures | feature.getMask();
/* 509 */     int newMask = this._formatReadFeaturesToChange | feature.getMask();
/* 510 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
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
/*     */   public DeserializationConfig withFeatures(FormatFeature... features) {
/* 525 */     if (features.length > 0 && features[0] instanceof JsonReadFeature) {
/* 526 */       return _withJsonReadFeatures(features);
/*     */     }
/* 528 */     int newSet = this._formatReadFeatures;
/* 529 */     int newMask = this._formatReadFeaturesToChange;
/* 530 */     for (FormatFeature f : features) {
/* 531 */       int mask = f.getMask();
/* 532 */       newSet |= mask;
/* 533 */       newMask |= mask;
/*     */     } 
/* 535 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
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
/*     */   public DeserializationConfig without(FormatFeature feature) {
/* 550 */     if (feature instanceof JsonReadFeature) {
/* 551 */       return _withoutJsonReadFeatures(new FormatFeature[] { feature });
/*     */     }
/* 553 */     int newSet = this._formatReadFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 554 */     int newMask = this._formatReadFeaturesToChange | feature.getMask();
/* 555 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
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
/*     */   public DeserializationConfig withoutFeatures(FormatFeature... features) {
/* 570 */     if (features.length > 0 && features[0] instanceof JsonReadFeature) {
/* 571 */       return _withoutJsonReadFeatures(features);
/*     */     }
/* 573 */     int newSet = this._formatReadFeatures;
/* 574 */     int newMask = this._formatReadFeaturesToChange;
/* 575 */     for (FormatFeature f : features) {
/* 576 */       int mask = f.getMask();
/* 577 */       newSet &= mask ^ 0xFFFFFFFF;
/* 578 */       newMask |= mask;
/*     */     } 
/* 580 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeserializationConfig _withJsonReadFeatures(FormatFeature... features) {
/* 588 */     int parserSet = this._parserFeatures;
/* 589 */     int parserMask = this._parserFeaturesToChange;
/* 590 */     int newSet = this._formatReadFeatures;
/* 591 */     int newMask = this._formatReadFeaturesToChange;
/* 592 */     for (FormatFeature f : features) {
/* 593 */       int mask = f.getMask();
/* 594 */       newSet |= mask;
/* 595 */       newMask |= mask;
/*     */       
/* 597 */       if (f instanceof JsonReadFeature) {
/* 598 */         JsonParser.Feature oldF = ((JsonReadFeature)f).mappedFeature();
/* 599 */         if (oldF != null) {
/* 600 */           int pmask = oldF.getMask();
/* 601 */           parserSet |= pmask;
/* 602 */           parserMask |= pmask;
/*     */         } 
/*     */       } 
/*     */     } 
/* 606 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask && this._parserFeatures == parserSet && this._parserFeaturesToChange == parserMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, parserSet, parserMask, newSet, newMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private DeserializationConfig _withoutJsonReadFeatures(FormatFeature... features) {
/* 615 */     int parserSet = this._parserFeatures;
/* 616 */     int parserMask = this._parserFeaturesToChange;
/* 617 */     int newSet = this._formatReadFeatures;
/* 618 */     int newMask = this._formatReadFeaturesToChange;
/* 619 */     for (FormatFeature f : features) {
/* 620 */       int mask = f.getMask();
/* 621 */       newSet &= mask ^ 0xFFFFFFFF;
/* 622 */       newMask |= mask;
/*     */       
/* 624 */       if (f instanceof JsonReadFeature) {
/* 625 */         JsonParser.Feature oldF = ((JsonReadFeature)f).mappedFeature();
/* 626 */         if (oldF != null) {
/* 627 */           int pmask = oldF.getMask();
/* 628 */           parserSet &= pmask ^ 0xFFFFFFFF;
/* 629 */           parserMask |= pmask;
/*     */         } 
/*     */       } 
/*     */     } 
/* 633 */     return (this._formatReadFeatures == newSet && this._formatReadFeaturesToChange == newMask && this._parserFeatures == parserSet && this._parserFeaturesToChange == parserMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, parserSet, parserMask, newSet, newMask);
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
/*     */   public DeserializationConfig with(JsonNodeFactory f) {
/* 651 */     if (this._nodeFactory == f) {
/* 652 */       return this;
/*     */     }
/* 654 */     return new DeserializationConfig(this, f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeserializationConfig withHandler(DeserializationProblemHandler h) {
/* 664 */     if (LinkedNode.contains(this._problemHandlers, h)) {
/* 665 */       return this;
/*     */     }
/* 667 */     return new DeserializationConfig(this, new LinkedNode(h, this._problemHandlers));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeserializationConfig withNoProblemHandlers() {
/* 676 */     if (this._problemHandlers == null) {
/* 677 */       return this;
/*     */     }
/* 679 */     return new DeserializationConfig(this, (LinkedNode<DeserializationProblemHandler>)null);
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
/*     */   public void initialize(JsonParser p) {
/* 697 */     if (this._parserFeaturesToChange != 0) {
/* 698 */       p.overrideStdFeatures(this._parserFeatures, this._parserFeaturesToChange);
/*     */     }
/* 700 */     if (this._formatReadFeaturesToChange != 0) {
/* 701 */       p.overrideFormatFeatures(this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public boolean useRootWrapping() {
/* 714 */     if (this._rootName != null) {
/* 715 */       return !this._rootName.isEmpty();
/*     */     }
/* 717 */     return isEnabled(DeserializationFeature.UNWRAP_ROOT_VALUE);
/*     */   }
/*     */   
/*     */   public final boolean isEnabled(DeserializationFeature f) {
/* 721 */     return ((this._deserFeatures & f.getMask()) != 0);
/*     */   }
/*     */   
/*     */   public final boolean isEnabled(JsonParser.Feature f, JsonFactory factory) {
/* 725 */     int mask = f.getMask();
/* 726 */     if ((this._parserFeaturesToChange & mask) != 0) {
/* 727 */       return ((this._parserFeatures & f.getMask()) != 0);
/*     */     }
/* 729 */     return factory.isEnabled(f);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasDeserializationFeatures(int featureMask) {
/* 739 */     return ((this._deserFeatures & featureMask) == featureMask);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasSomeOfFeatures(int featureMask) {
/* 749 */     return ((this._deserFeatures & featureMask) != 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getDeserializationFeatures() {
/* 757 */     return this._deserFeatures;
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
/*     */   public final boolean requiresFullValue() {
/* 769 */     return DeserializationFeature.FAIL_ON_TRAILING_TOKENS.enabledIn(this._deserFeatures);
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
/*     */   public LinkedNode<DeserializationProblemHandler> getProblemHandlers() {
/* 783 */     return this._problemHandlers;
/*     */   }
/*     */   
/*     */   public final JsonNodeFactory getNodeFactory() {
/* 787 */     return this._nodeFactory;
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
/*     */   public <T extends BeanDescription> T introspect(JavaType type) {
/* 804 */     return (T)getClassIntrospector().forDeserialization(this, type, (ClassIntrospector.MixInResolver)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends BeanDescription> T introspectForCreation(JavaType type) {
/* 813 */     return (T)getClassIntrospector().forCreation(this, type, (ClassIntrospector.MixInResolver)this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends BeanDescription> T introspectForBuilder(JavaType type) {
/* 821 */     return (T)getClassIntrospector().forDeserializationWithBuilder(this, type, (ClassIntrospector.MixInResolver)this);
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
/*     */   public TypeDeserializer findTypeDeserializer(JavaType baseType) throws JsonMappingException {
/* 840 */     BeanDescription bean = introspectClassAnnotations(baseType.getRawClass());
/* 841 */     AnnotatedClass ac = bean.getClassInfo();
/* 842 */     TypeResolverBuilder<?> b = getAnnotationIntrospector().findTypeResolver((MapperConfig<?>)this, ac, baseType);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 847 */     Collection<NamedType> subtypes = null;
/* 848 */     if (b == null) {
/* 849 */       b = getDefaultTyper(baseType);
/* 850 */       if (b == null) {
/* 851 */         return null;
/*     */       }
/*     */     } else {
/* 854 */       subtypes = getSubtypeResolver().collectAndResolveSubtypesByTypeId((MapperConfig)this, ac);
/*     */     } 
/* 856 */     return b.buildTypeDeserializer(this, baseType, subtypes);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\DeserializationConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */