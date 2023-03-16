/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.composer.Composer;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.CollectionNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
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
/*     */ public abstract class BaseConstructor
/*     */ {
/*  53 */   protected static final Object NOT_INSTANTIATED_OBJECT = new Object();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   protected final Map<NodeId, Construct> yamlClassConstructors = new EnumMap<>(NodeId.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  66 */   protected final Map<Tag, Construct> yamlConstructors = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   protected final Map<String, Construct> yamlMultiConstructors = new HashMap<>();
/*     */   
/*     */   protected Composer composer;
/*     */   
/*     */   final Map<Node, Object> constructedObjects;
/*     */   
/*     */   private final Set<Node> recursiveObjects;
/*     */   
/*     */   private final ArrayList<RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>>> maps2fill;
/*     */   
/*     */   private final ArrayList<RecursiveTuple<Set<Object>, Object>> sets2fill;
/*     */   protected Tag rootTag;
/*     */   private PropertyUtils propertyUtils;
/*     */   private boolean explicitPropertyUtils;
/*     */   private boolean allowDuplicateKeys = true;
/*     */   private boolean wrappedToRootException = false;
/*     */   private boolean enumCaseSensitive = false;
/*     */   protected final Map<Class<? extends Object>, TypeDescription> typeDefinitions;
/*     */   protected final Map<Tag, Class<? extends Object>> typeTags;
/*     */   protected LoaderOptions loadingConfig;
/*     */   
/*     */   public BaseConstructor() {
/*  93 */     this(new LoaderOptions());
/*     */   }
/*     */   
/*     */   public BaseConstructor(LoaderOptions loadingConfig) {
/*  97 */     this.constructedObjects = new HashMap<>();
/*  98 */     this.recursiveObjects = new HashSet<>();
/*  99 */     this.maps2fill = new ArrayList<>();
/*     */     
/* 101 */     this.sets2fill = new ArrayList<>();
/* 102 */     this.typeDefinitions = new HashMap<>();
/* 103 */     this.typeTags = new HashMap<>();
/*     */     
/* 105 */     this.rootTag = null;
/* 106 */     this.explicitPropertyUtils = false;
/*     */     
/* 108 */     this.typeDefinitions.put(SortedMap.class, new TypeDescription(SortedMap.class, Tag.OMAP, TreeMap.class));
/*     */     
/* 110 */     this.typeDefinitions.put(SortedSet.class, new TypeDescription(SortedSet.class, Tag.SET, TreeSet.class));
/*     */     
/* 112 */     this.loadingConfig = loadingConfig;
/*     */   }
/*     */   
/*     */   public void setComposer(Composer composer) {
/* 116 */     this.composer = composer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkData() {
/* 126 */     return this.composer.checkNode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getData() throws NoSuchElementException {
/* 136 */     if (!this.composer.checkNode()) {
/* 137 */       throw new NoSuchElementException("No document is available.");
/*     */     }
/* 139 */     Node node = this.composer.getNode();
/* 140 */     if (this.rootTag != null) {
/* 141 */       node.setTag(this.rootTag);
/*     */     }
/* 143 */     return constructDocument(node);
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
/*     */   public Object getSingleData(Class<?> type) {
/* 155 */     Node node = this.composer.getSingleNode();
/* 156 */     if (node != null && !Tag.NULL.equals(node.getTag())) {
/* 157 */       if (Object.class != type) {
/* 158 */         node.setTag(new Tag(type));
/* 159 */       } else if (this.rootTag != null) {
/* 160 */         node.setTag(this.rootTag);
/*     */       } 
/* 162 */       return constructDocument(node);
/*     */     } 
/* 164 */     Construct construct = this.yamlConstructors.get(Tag.NULL);
/* 165 */     return construct.construct(node);
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
/*     */   protected final Object constructDocument(Node node) {
/*     */     try {
/* 178 */       Object data = constructObject(node);
/* 179 */       fillRecursive();
/* 180 */       return data;
/* 181 */     } catch (RuntimeException e) {
/* 182 */       if (this.wrappedToRootException && !(e instanceof YAMLException)) {
/* 183 */         throw new YAMLException(e);
/*     */       }
/* 185 */       throw e;
/*     */     }
/*     */     finally {
/*     */       
/* 189 */       this.constructedObjects.clear();
/* 190 */       this.recursiveObjects.clear();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void fillRecursive() {
/* 198 */     if (!this.maps2fill.isEmpty()) {
/* 199 */       for (RecursiveTuple<Map<Object, Object>, RecursiveTuple<Object, Object>> entry : this.maps2fill) {
/* 200 */         RecursiveTuple<Object, Object> key_value = entry._2();
/* 201 */         ((Map)entry._1()).put(key_value._1(), key_value._2());
/*     */       } 
/* 203 */       this.maps2fill.clear();
/*     */     } 
/* 205 */     if (!this.sets2fill.isEmpty()) {
/* 206 */       for (RecursiveTuple<Set<Object>, Object> value : this.sets2fill) {
/* 207 */         ((Set)value._1()).add(value._2());
/*     */       }
/* 209 */       this.sets2fill.clear();
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
/*     */   protected Object constructObject(Node node) {
/* 221 */     if (this.constructedObjects.containsKey(node)) {
/* 222 */       return this.constructedObjects.get(node);
/*     */     }
/* 224 */     return constructObjectNoCheck(node);
/*     */   }
/*     */   
/*     */   protected Object constructObjectNoCheck(Node node) {
/* 228 */     if (this.recursiveObjects.contains(node)) {
/* 229 */       throw new ConstructorException(null, null, "found unconstructable recursive node", node
/* 230 */           .getStartMark());
/*     */     }
/* 232 */     this.recursiveObjects.add(node);
/* 233 */     Construct constructor = getConstructor(node);
/*     */     
/* 235 */     Object data = this.constructedObjects.containsKey(node) ? this.constructedObjects.get(node) : constructor.construct(node);
/*     */     
/* 237 */     finalizeConstruction(node, data);
/* 238 */     this.constructedObjects.put(node, data);
/* 239 */     this.recursiveObjects.remove(node);
/* 240 */     if (node.isTwoStepsConstruction()) {
/* 241 */       constructor.construct2ndStep(node, data);
/*     */     }
/* 243 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Construct getConstructor(Node node) {
/* 254 */     if (node.useClassConstructor()) {
/* 255 */       return this.yamlClassConstructors.get(node.getNodeId());
/*     */     }
/* 257 */     Construct constructor = this.yamlConstructors.get(node.getTag());
/* 258 */     if (constructor == null) {
/* 259 */       for (String prefix : this.yamlMultiConstructors.keySet()) {
/* 260 */         if (node.getTag().startsWith(prefix)) {
/* 261 */           return this.yamlMultiConstructors.get(prefix);
/*     */         }
/*     */       } 
/* 264 */       return this.yamlConstructors.get(null);
/*     */     } 
/* 266 */     return constructor;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String constructScalar(ScalarNode node) {
/* 271 */     return node.getValue();
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<Object> createDefaultList(int initSize) {
/* 276 */     return new ArrayList(initSize);
/*     */   }
/*     */   
/*     */   protected Set<Object> createDefaultSet(int initSize) {
/* 280 */     return new LinkedHashSet(initSize);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Map<Object, Object> createDefaultMap(int initSize) {
/* 285 */     return new LinkedHashMap<>(initSize);
/*     */   }
/*     */   
/*     */   protected Object createArray(Class<?> type, int size) {
/* 289 */     return Array.newInstance(type.getComponentType(), size);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Object finalizeConstruction(Node node, Object data) {
/* 295 */     Class<? extends Object> type = node.getType();
/* 296 */     if (this.typeDefinitions.containsKey(type)) {
/* 297 */       return ((TypeDescription)this.typeDefinitions.get(type)).finalizeConstruction(data);
/*     */     }
/* 299 */     return data;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Object newInstance(Node node) {
/* 304 */     return newInstance(Object.class, node);
/*     */   }
/*     */   
/*     */   protected final Object newInstance(Class<?> ancestor, Node node) {
/* 308 */     return newInstance(ancestor, node, true);
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
/*     */   protected Object newInstance(Class<?> ancestor, Node node, boolean tryDefault) {
/*     */     try {
/* 326 */       Class<? extends Object> type = node.getType();
/* 327 */       if (this.typeDefinitions.containsKey(type)) {
/* 328 */         TypeDescription td = this.typeDefinitions.get(type);
/* 329 */         Object instance = td.newInstance(node);
/* 330 */         if (instance != null) {
/* 331 */           return instance;
/*     */         }
/*     */       } 
/*     */       
/* 335 */       if (tryDefault)
/*     */       {
/*     */ 
/*     */         
/* 339 */         if (ancestor.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers())) {
/* 340 */           Constructor<?> c = type.getDeclaredConstructor(new Class[0]);
/* 341 */           c.setAccessible(true);
/* 342 */           return c.newInstance(new Object[0]);
/*     */         } 
/*     */       }
/* 345 */     } catch (Exception e) {
/* 346 */       throw new YAMLException(e);
/*     */     } 
/*     */     
/* 349 */     return NOT_INSTANTIATED_OBJECT;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Set<Object> newSet(CollectionNode<?> node) {
/* 354 */     Object instance = newInstance(Set.class, (Node)node);
/* 355 */     if (instance != NOT_INSTANTIATED_OBJECT) {
/* 356 */       return (Set<Object>)instance;
/*     */     }
/* 358 */     return createDefaultSet(node.getValue().size());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<Object> newList(SequenceNode node) {
/* 364 */     Object instance = newInstance(List.class, (Node)node);
/* 365 */     if (instance != NOT_INSTANTIATED_OBJECT) {
/* 366 */       return (List<Object>)instance;
/*     */     }
/* 368 */     return createDefaultList(node.getValue().size());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<Object, Object> newMap(MappingNode node) {
/* 374 */     Object instance = newInstance(Map.class, (Node)node);
/* 375 */     if (instance != NOT_INSTANTIATED_OBJECT) {
/* 376 */       return (Map<Object, Object>)instance;
/*     */     }
/* 378 */     return createDefaultMap(node.getValue().size());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected List<? extends Object> constructSequence(SequenceNode node) {
/* 386 */     List<Object> result = newList(node);
/* 387 */     constructSequenceStep2(node, result);
/* 388 */     return result;
/*     */   }
/*     */   
/*     */   protected Set<? extends Object> constructSet(SequenceNode node) {
/* 392 */     Set<Object> result = newSet((CollectionNode<?>)node);
/* 393 */     constructSequenceStep2(node, result);
/* 394 */     return result;
/*     */   }
/*     */   
/*     */   protected Object constructArray(SequenceNode node) {
/* 398 */     return constructArrayStep2(node, createArray(node.getType(), node.getValue().size()));
/*     */   }
/*     */   
/*     */   protected void constructSequenceStep2(SequenceNode node, Collection<Object> collection) {
/* 402 */     for (Node child : node.getValue()) {
/* 403 */       collection.add(constructObject(child));
/*     */     }
/*     */   }
/*     */   
/*     */   protected Object constructArrayStep2(SequenceNode node, Object array) {
/* 408 */     Class<?> componentType = node.getType().getComponentType();
/*     */     
/* 410 */     int index = 0;
/* 411 */     for (Node child : node.getValue()) {
/*     */       
/* 413 */       if (child.getType() == Object.class) {
/* 414 */         child.setType(componentType);
/*     */       }
/*     */       
/* 417 */       Object value = constructObject(child);
/*     */       
/* 419 */       if (componentType.isPrimitive()) {
/*     */         
/* 421 */         if (value == null) {
/* 422 */           throw new NullPointerException("Unable to construct element value for " + child);
/*     */         }
/*     */ 
/*     */         
/* 426 */         if (byte.class.equals(componentType)) {
/* 427 */           Array.setByte(array, index, ((Number)value).byteValue());
/*     */         }
/* 429 */         else if (short.class.equals(componentType)) {
/* 430 */           Array.setShort(array, index, ((Number)value).shortValue());
/*     */         }
/* 432 */         else if (int.class.equals(componentType)) {
/* 433 */           Array.setInt(array, index, ((Number)value).intValue());
/*     */         }
/* 435 */         else if (long.class.equals(componentType)) {
/* 436 */           Array.setLong(array, index, ((Number)value).longValue());
/*     */         }
/* 438 */         else if (float.class.equals(componentType)) {
/* 439 */           Array.setFloat(array, index, ((Number)value).floatValue());
/*     */         }
/* 441 */         else if (double.class.equals(componentType)) {
/* 442 */           Array.setDouble(array, index, ((Number)value).doubleValue());
/*     */         }
/* 444 */         else if (char.class.equals(componentType)) {
/* 445 */           Array.setChar(array, index, ((Character)value).charValue());
/*     */         }
/* 447 */         else if (boolean.class.equals(componentType)) {
/* 448 */           Array.setBoolean(array, index, ((Boolean)value).booleanValue());
/*     */         } else {
/*     */           
/* 451 */           throw new YAMLException("unexpected primitive type");
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 456 */         Array.set(array, index, value);
/*     */       } 
/*     */       
/* 459 */       index++;
/*     */     } 
/* 461 */     return array;
/*     */   }
/*     */   
/*     */   protected Set<Object> constructSet(MappingNode node) {
/* 465 */     Set<Object> set = newSet((CollectionNode<?>)node);
/* 466 */     constructSet2ndStep(node, set);
/* 467 */     return set;
/*     */   }
/*     */   
/*     */   protected Map<Object, Object> constructMapping(MappingNode node) {
/* 471 */     Map<Object, Object> mapping = newMap(node);
/* 472 */     constructMapping2ndStep(node, mapping);
/* 473 */     return mapping;
/*     */   }
/*     */   
/*     */   protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
/* 477 */     List<NodeTuple> nodeValue = node.getValue();
/* 478 */     for (NodeTuple tuple : nodeValue) {
/* 479 */       Node keyNode = tuple.getKeyNode();
/* 480 */       Node valueNode = tuple.getValueNode();
/* 481 */       Object key = constructObject(keyNode);
/* 482 */       if (key != null) {
/*     */         try {
/* 484 */           key.hashCode();
/* 485 */         } catch (Exception e) {
/* 486 */           throw new ConstructorException("while constructing a mapping", node.getStartMark(), "found unacceptable key " + key, tuple
/* 487 */               .getKeyNode().getStartMark(), e);
/*     */         } 
/*     */       }
/* 490 */       Object value = constructObject(valueNode);
/* 491 */       if (keyNode.isTwoStepsConstruction()) {
/* 492 */         if (this.loadingConfig.getAllowRecursiveKeys()) {
/* 493 */           postponeMapFilling(mapping, key, value); continue;
/*     */         } 
/* 495 */         throw new YAMLException("Recursive key for mapping is detected but it is not configured to be allowed.");
/*     */       } 
/*     */ 
/*     */       
/* 499 */       mapping.put(key, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postponeMapFilling(Map<Object, Object> mapping, Object key, Object value) {
/* 510 */     this.maps2fill.add(0, new RecursiveTuple<>(mapping, new RecursiveTuple<>(key, value)));
/*     */   }
/*     */   
/*     */   protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
/* 514 */     List<NodeTuple> nodeValue = node.getValue();
/* 515 */     for (NodeTuple tuple : nodeValue) {
/* 516 */       Node keyNode = tuple.getKeyNode();
/* 517 */       Object key = constructObject(keyNode);
/* 518 */       if (key != null) {
/*     */         try {
/* 520 */           key.hashCode();
/* 521 */         } catch (Exception e) {
/* 522 */           throw new ConstructorException("while constructing a Set", node.getStartMark(), "found unacceptable key " + key, tuple
/* 523 */               .getKeyNode().getStartMark(), e);
/*     */         } 
/*     */       }
/* 526 */       if (keyNode.isTwoStepsConstruction()) {
/* 527 */         postponeSetFilling(set, key); continue;
/*     */       } 
/* 529 */       set.add(key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void postponeSetFilling(Set<Object> set, Object key) {
/* 540 */     this.sets2fill.add(0, new RecursiveTuple<>(set, key));
/*     */   }
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/* 544 */     this.propertyUtils = propertyUtils;
/* 545 */     this.explicitPropertyUtils = true;
/* 546 */     Collection<TypeDescription> tds = this.typeDefinitions.values();
/* 547 */     for (TypeDescription typeDescription : tds) {
/* 548 */       typeDescription.setPropertyUtils(propertyUtils);
/*     */     }
/*     */   }
/*     */   
/*     */   public final PropertyUtils getPropertyUtils() {
/* 553 */     if (this.propertyUtils == null) {
/* 554 */       this.propertyUtils = new PropertyUtils();
/*     */     }
/* 556 */     return this.propertyUtils;
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
/*     */   public TypeDescription addTypeDescription(TypeDescription definition) {
/* 568 */     if (definition == null) {
/* 569 */       throw new NullPointerException("TypeDescription is required.");
/*     */     }
/* 571 */     Tag tag = definition.getTag();
/* 572 */     this.typeTags.put(tag, definition.getType());
/* 573 */     definition.setPropertyUtils(getPropertyUtils());
/* 574 */     return this.typeDefinitions.put(definition.getType(), definition);
/*     */   }
/*     */   
/*     */   private static class RecursiveTuple<T, K>
/*     */   {
/*     */     private final T _1;
/*     */     private final K _2;
/*     */     
/*     */     public RecursiveTuple(T _1, K _2) {
/* 583 */       this._1 = _1;
/* 584 */       this._2 = _2;
/*     */     }
/*     */     
/*     */     public K _2() {
/* 588 */       return this._2;
/*     */     }
/*     */     
/*     */     public T _1() {
/* 592 */       return this._1;
/*     */     }
/*     */   }
/*     */   
/*     */   public final boolean isExplicitPropertyUtils() {
/* 597 */     return this.explicitPropertyUtils;
/*     */   }
/*     */   
/*     */   public boolean isAllowDuplicateKeys() {
/* 601 */     return this.allowDuplicateKeys;
/*     */   }
/*     */   
/*     */   public void setAllowDuplicateKeys(boolean allowDuplicateKeys) {
/* 605 */     this.allowDuplicateKeys = allowDuplicateKeys;
/*     */   }
/*     */   
/*     */   public boolean isWrappedToRootException() {
/* 609 */     return this.wrappedToRootException;
/*     */   }
/*     */   
/*     */   public void setWrappedToRootException(boolean wrappedToRootException) {
/* 613 */     this.wrappedToRootException = wrappedToRootException;
/*     */   }
/*     */   
/*     */   public boolean isEnumCaseSensitive() {
/* 617 */     return this.enumCaseSensitive;
/*     */   }
/*     */   
/*     */   public void setEnumCaseSensitive(boolean enumCaseSensitive) {
/* 621 */     this.enumCaseSensitive = enumCaseSensitive;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\constructor\BaseConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */