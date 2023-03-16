/*     */ package org.yaml.snakeyaml;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.BeanAccess;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.introspector.PropertySubstitute;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TypeDescription
/*     */ {
/*  43 */   private static final Logger log = Logger.getLogger(TypeDescription.class.getPackage().getName());
/*     */   
/*     */   private final Class<? extends Object> type;
/*     */   
/*     */   private Class<?> impl;
/*     */   
/*     */   private Tag tag;
/*     */   
/*     */   private transient Set<Property> dumpProperties;
/*     */   
/*     */   private transient PropertyUtils propertyUtils;
/*     */   
/*     */   private transient boolean delegatesChecked;
/*     */   
/*  57 */   private Map<String, PropertySubstitute> properties = Collections.emptyMap();
/*     */   
/*  59 */   protected Set<String> excludes = Collections.emptySet();
/*  60 */   protected String[] includes = null;
/*     */   protected BeanAccess beanAccess;
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, Tag tag) {
/*  64 */     this(clazz, tag, null);
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, Tag tag, Class<?> impl) {
/*  68 */     this.type = clazz;
/*  69 */     this.tag = tag;
/*  70 */     this.impl = impl;
/*  71 */     this.beanAccess = null;
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, String tag) {
/*  75 */     this(clazz, new Tag(tag), null);
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz) {
/*  79 */     this(clazz, new Tag(clazz), null);
/*     */   }
/*     */   
/*     */   public TypeDescription(Class<? extends Object> clazz, Class<?> impl) {
/*  83 */     this(clazz, new Tag(clazz), impl);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag getTag() {
/*  93 */     return this.tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setTag(Tag tag) {
/* 104 */     this.tag = tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void setTag(String tag) {
/* 115 */     setTag(new Tag(tag));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Class<? extends Object> getType() {
/* 124 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public void putListPropertyType(String property, Class<? extends Object> type) {
/* 135 */     addPropertyParameters(property, new Class[] { type });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<? extends Object> getListPropertyType(String property) {
/* 146 */     if (this.properties.containsKey(property)) {
/* 147 */       Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
/* 148 */       if (typeArguments != null && typeArguments.length > 0) {
/* 149 */         return (Class)typeArguments[0];
/*     */       }
/*     */     } 
/* 152 */     return null;
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
/*     */   public void putMapPropertyType(String property, Class<? extends Object> key, Class<? extends Object> value) {
/* 165 */     addPropertyParameters(property, new Class[] { key, value });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<? extends Object> getMapKeyType(String property) {
/* 176 */     if (this.properties.containsKey(property)) {
/* 177 */       Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
/* 178 */       if (typeArguments != null && typeArguments.length > 0) {
/* 179 */         return (Class)typeArguments[0];
/*     */       }
/*     */     } 
/* 182 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Class<? extends Object> getMapValueType(String property) {
/* 193 */     if (this.properties.containsKey(property)) {
/* 194 */       Class<?>[] typeArguments = ((PropertySubstitute)this.properties.get(property)).getActualTypeArguments();
/* 195 */       if (typeArguments != null && typeArguments.length > 1) {
/* 196 */         return (Class)typeArguments[1];
/*     */       }
/*     */     } 
/* 199 */     return null;
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
/*     */   public void addPropertyParameters(String pName, Class<?>... classes) {
/* 211 */     if (!this.properties.containsKey(pName)) {
/* 212 */       substituteProperty(pName, null, null, null, classes);
/*     */     } else {
/* 214 */       PropertySubstitute pr = this.properties.get(pName);
/* 215 */       pr.setActualTypeArguments(classes);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 222 */     return "TypeDescription for " + getType() + " (tag='" + getTag() + "')";
/*     */   }
/*     */   
/*     */   private void checkDelegates() {
/* 226 */     Collection<PropertySubstitute> values = this.properties.values();
/* 227 */     for (PropertySubstitute p : values) {
/*     */       try {
/* 229 */         p.setDelegate(discoverProperty(p.getName()));
/* 230 */       } catch (YAMLException yAMLException) {}
/*     */     } 
/*     */     
/* 233 */     this.delegatesChecked = true;
/*     */   }
/*     */   
/*     */   private Property discoverProperty(String name) {
/* 237 */     if (this.propertyUtils != null) {
/* 238 */       if (this.beanAccess == null) {
/* 239 */         return this.propertyUtils.getProperty(this.type, name);
/*     */       }
/* 241 */       return this.propertyUtils.getProperty(this.type, name, this.beanAccess);
/*     */     } 
/* 243 */     return null;
/*     */   }
/*     */   
/*     */   public Property getProperty(String name) {
/* 247 */     if (!this.delegatesChecked) {
/* 248 */       checkDelegates();
/*     */     }
/* 250 */     return this.properties.containsKey(name) ? (Property)this.properties.get(name) : discoverProperty(name);
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
/*     */   public void substituteProperty(String pName, Class<?> pType, String getter, String setter, Class<?>... argParams) {
/* 264 */     substituteProperty(new PropertySubstitute(pName, pType, getter, setter, argParams));
/*     */   }
/*     */   
/*     */   public void substituteProperty(PropertySubstitute substitute) {
/* 268 */     if (Collections.EMPTY_MAP == this.properties) {
/* 269 */       this.properties = new LinkedHashMap<>();
/*     */     }
/* 271 */     substitute.setTargetType(this.type);
/* 272 */     this.properties.put(substitute.getName(), substitute);
/*     */   }
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 276 */     this.propertyUtils = propertyUtils;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIncludes(String... propNames) {
/* 281 */     this.includes = (propNames != null && propNames.length > 0) ? propNames : null;
/*     */   }
/*     */   
/*     */   public void setExcludes(String... propNames) {
/* 285 */     if (propNames != null && propNames.length > 0) {
/* 286 */       this.excludes = new HashSet<>();
/* 287 */       Collections.addAll(this.excludes, propNames);
/*     */     } else {
/* 289 */       this.excludes = Collections.emptySet();
/*     */     } 
/*     */   }
/*     */   
/*     */   public Set<Property> getProperties() {
/* 294 */     if (this.dumpProperties != null) {
/* 295 */       return this.dumpProperties;
/*     */     }
/*     */     
/* 298 */     if (this.propertyUtils != null) {
/* 299 */       if (this.includes != null) {
/* 300 */         this.dumpProperties = new LinkedHashSet<>();
/* 301 */         for (String propertyName : this.includes) {
/* 302 */           if (!this.excludes.contains(propertyName)) {
/* 303 */             this.dumpProperties.add(getProperty(propertyName));
/*     */           }
/*     */         } 
/* 306 */         return this.dumpProperties;
/*     */       } 
/*     */ 
/*     */       
/* 310 */       Set<Property> readableProps = (this.beanAccess == null) ? this.propertyUtils.getProperties(this.type) : this.propertyUtils.getProperties(this.type, this.beanAccess);
/*     */       
/* 312 */       if (this.properties.isEmpty()) {
/* 313 */         if (this.excludes.isEmpty()) {
/* 314 */           return this.dumpProperties = readableProps;
/*     */         }
/* 316 */         this.dumpProperties = new LinkedHashSet<>();
/* 317 */         for (Property property : readableProps) {
/* 318 */           if (!this.excludes.contains(property.getName())) {
/* 319 */             this.dumpProperties.add(property);
/*     */           }
/*     */         } 
/* 322 */         return this.dumpProperties;
/*     */       } 
/*     */       
/* 325 */       if (!this.delegatesChecked) {
/* 326 */         checkDelegates();
/*     */       }
/*     */       
/* 329 */       this.dumpProperties = new LinkedHashSet<>();
/*     */       
/* 331 */       for (Property property : this.properties.values()) {
/* 332 */         if (!this.excludes.contains(property.getName()) && property.isReadable()) {
/* 333 */           this.dumpProperties.add(property);
/*     */         }
/*     */       } 
/*     */       
/* 337 */       for (Property property : readableProps) {
/* 338 */         if (!this.excludes.contains(property.getName())) {
/* 339 */           this.dumpProperties.add(property);
/*     */         }
/*     */       } 
/*     */       
/* 343 */       return this.dumpProperties;
/*     */     } 
/* 345 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean setupPropertyType(String key, Node valueNode) {
/* 353 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setProperty(Object targetBean, String propertyName, Object value) throws Exception {
/* 358 */     return false;
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
/*     */   public Object newInstance(Node node) {
/* 371 */     if (this.impl != null) {
/*     */       try {
/* 373 */         Constructor<?> c = this.impl.getDeclaredConstructor(new Class[0]);
/* 374 */         c.setAccessible(true);
/* 375 */         return c.newInstance(new Object[0]);
/* 376 */       } catch (Exception e) {
/* 377 */         log.fine(e.getLocalizedMessage());
/* 378 */         this.impl = null;
/*     */       } 
/*     */     }
/* 381 */     return null;
/*     */   }
/*     */   
/*     */   public Object newInstance(String propertyName, Node node) {
/* 385 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object finalizeConstruction(Object obj) {
/* 395 */     return obj;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\TypeDescription.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */