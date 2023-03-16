/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.nodes.CollectionNode;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.util.EnumUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Constructor
/*     */   extends SafeConstructor
/*     */ {
/*     */   public Constructor() {
/*  45 */     this(Object.class);
/*     */   }
/*     */   
/*     */   public Constructor(LoaderOptions loadingConfig) {
/*  49 */     this(Object.class, loadingConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Constructor(Class<? extends Object> theRoot) {
/*  58 */     this(new TypeDescription(checkRoot(theRoot)));
/*     */   }
/*     */   
/*     */   public Constructor(Class<? extends Object> theRoot, LoaderOptions loadingConfig) {
/*  62 */     this(new TypeDescription(checkRoot(theRoot)), loadingConfig);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Class<? extends Object> checkRoot(Class<? extends Object> theRoot) {
/*  69 */     if (theRoot == null) {
/*  70 */       throw new NullPointerException("Root class must be provided.");
/*     */     }
/*  72 */     return theRoot;
/*     */   }
/*     */ 
/*     */   
/*     */   public Constructor(TypeDescription theRoot) {
/*  77 */     this(theRoot, (Collection<TypeDescription>)null, new LoaderOptions());
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot, LoaderOptions loadingConfig) {
/*  81 */     this(theRoot, (Collection<TypeDescription>)null, loadingConfig);
/*     */   }
/*     */   
/*     */   public Constructor(TypeDescription theRoot, Collection<TypeDescription> moreTDs) {
/*  85 */     this(theRoot, moreTDs, new LoaderOptions());
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
/*     */   public Constructor(TypeDescription theRoot, Collection<TypeDescription> moreTDs, LoaderOptions loadingConfig) {
/*  97 */     super(loadingConfig);
/*  98 */     if (theRoot == null) {
/*  99 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/* 101 */     this.yamlConstructors.put(null, new ConstructYamlObject());
/* 102 */     if (!Object.class.equals(theRoot.getType())) {
/* 103 */       this.rootTag = new Tag(theRoot.getType());
/*     */     }
/* 105 */     this.yamlClassConstructors.put(NodeId.scalar, new ConstructScalar());
/* 106 */     this.yamlClassConstructors.put(NodeId.mapping, new ConstructMapping());
/* 107 */     this.yamlClassConstructors.put(NodeId.sequence, new ConstructSequence());
/* 108 */     addTypeDescription(theRoot);
/* 109 */     if (moreTDs != null) {
/* 110 */       for (TypeDescription td : moreTDs) {
/* 111 */         addTypeDescription(td);
/*     */       }
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
/*     */   public Constructor(String theRoot) throws ClassNotFoundException {
/* 124 */     this((Class)Class.forName(check(theRoot)));
/*     */   }
/*     */   
/*     */   public Constructor(String theRoot, LoaderOptions loadingConfig) throws ClassNotFoundException {
/* 128 */     this((Class)Class.forName(check(theRoot)), loadingConfig);
/*     */   }
/*     */   
/*     */   private static final String check(String s) {
/* 132 */     if (s == null) {
/* 133 */       throw new NullPointerException("Root type must be provided.");
/*     */     }
/* 135 */     if (s.trim().length() == 0) {
/* 136 */       throw new YAMLException("Root type must be provided.");
/*     */     }
/* 138 */     return s;
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
/*     */   protected class ConstructMapping
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 155 */       MappingNode mnode = (MappingNode)node;
/* 156 */       if (Map.class.isAssignableFrom(node.getType())) {
/* 157 */         if (node.isTwoStepsConstruction()) {
/* 158 */           return Constructor.this.newMap(mnode);
/*     */         }
/* 160 */         return Constructor.this.constructMapping(mnode);
/*     */       } 
/* 162 */       if (Collection.class.isAssignableFrom(node.getType())) {
/* 163 */         if (node.isTwoStepsConstruction()) {
/* 164 */           return Constructor.this.newSet((CollectionNode<?>)mnode);
/*     */         }
/* 166 */         return Constructor.this.constructSet(mnode);
/*     */       } 
/*     */       
/* 169 */       Object obj = Constructor.this.newInstance((Node)mnode);
/* 170 */       if (obj != BaseConstructor.NOT_INSTANTIATED_OBJECT) {
/* 171 */         if (node.isTwoStepsConstruction()) {
/* 172 */           return obj;
/*     */         }
/* 174 */         return constructJavaBean2ndStep(mnode, obj);
/*     */       } 
/*     */       
/* 177 */       throw new ConstructorException(null, null, "Can't create an instance for " + mnode
/* 178 */           .getTag(), node.getStartMark());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 185 */       if (Map.class.isAssignableFrom(node.getType())) {
/* 186 */         Constructor.this.constructMapping2ndStep((MappingNode)node, (Map<Object, Object>)object);
/* 187 */       } else if (Set.class.isAssignableFrom(node.getType())) {
/* 188 */         Constructor.this.constructSet2ndStep((MappingNode)node, (Set<Object>)object);
/*     */       } else {
/* 190 */         constructJavaBean2ndStep((MappingNode)node, object);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected Object constructJavaBean2ndStep(MappingNode node, Object object) {
/* 219 */       Constructor.this.flattenMapping(node, true);
/* 220 */       Class<? extends Object> beanType = node.getType();
/* 221 */       List<NodeTuple> nodeValue = node.getValue();
/* 222 */       for (NodeTuple tuple : nodeValue) {
/* 223 */         Node valueNode = tuple.getValueNode();
/*     */         
/* 225 */         String key = (String)Constructor.this.constructObject(tuple.getKeyNode());
/*     */         try {
/* 227 */           TypeDescription memberDescription = Constructor.this.typeDefinitions.get(beanType);
/*     */           
/* 229 */           Property property = (memberDescription == null) ? getProperty(beanType, key) : memberDescription.getProperty(key);
/*     */           
/* 231 */           if (!property.isWritable()) {
/* 232 */             throw new YAMLException("No writable property '" + key + "' on class: " + beanType
/* 233 */                 .getName());
/*     */           }
/*     */           
/* 236 */           valueNode.setType(property.getType());
/*     */           
/* 238 */           boolean typeDetected = (memberDescription != null && memberDescription.setupPropertyType(key, valueNode));
/* 239 */           if (!typeDetected && valueNode.getNodeId() != NodeId.scalar) {
/*     */             
/* 241 */             Class<?>[] arguments = property.getActualTypeArguments();
/* 242 */             if (arguments != null && arguments.length > 0)
/*     */             {
/*     */               
/* 245 */               if (valueNode.getNodeId() == NodeId.sequence) {
/* 246 */                 Class<?> t = arguments[0];
/* 247 */                 SequenceNode snode = (SequenceNode)valueNode;
/* 248 */                 snode.setListType(t);
/* 249 */               } else if (Map.class.isAssignableFrom(valueNode.getType())) {
/* 250 */                 Class<?> keyType = arguments[0];
/* 251 */                 Class<?> valueType = arguments[1];
/* 252 */                 MappingNode mnode = (MappingNode)valueNode;
/* 253 */                 mnode.setTypes(keyType, valueType);
/* 254 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/* 255 */               } else if (Collection.class.isAssignableFrom(valueNode.getType())) {
/* 256 */                 Class<?> t = arguments[0];
/* 257 */                 MappingNode mnode = (MappingNode)valueNode;
/* 258 */                 mnode.setOnlyKeyType(t);
/* 259 */                 mnode.setUseClassConstructor(Boolean.valueOf(true));
/*     */               } 
/*     */             }
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 266 */           Object value = (memberDescription != null) ? newInstance(memberDescription, key, valueNode) : Constructor.this.constructObject(valueNode);
/*     */ 
/*     */           
/* 269 */           if ((property.getType() == float.class || property.getType() == Float.class) && 
/* 270 */             value instanceof Double) {
/* 271 */             value = Float.valueOf(((Double)value).floatValue());
/*     */           }
/*     */ 
/*     */           
/* 275 */           if (property.getType() == String.class && Tag.BINARY.equals(valueNode.getTag()) && value instanceof byte[])
/*     */           {
/* 277 */             value = new String((byte[])value);
/*     */           }
/*     */           
/* 280 */           if (memberDescription == null || !memberDescription.setProperty(object, key, value)) {
/* 281 */             property.set(object, value);
/*     */           }
/* 283 */         } catch (DuplicateKeyException e) {
/* 284 */           throw e;
/* 285 */         } catch (Exception e) {
/* 286 */           throw new ConstructorException("Cannot create property=" + key + " for JavaBean=" + object, node
/* 287 */               .getStartMark(), e
/* 288 */               .getMessage(), valueNode.getStartMark(), e);
/*     */         } 
/*     */       } 
/* 291 */       return object;
/*     */     }
/*     */     
/*     */     private Object newInstance(TypeDescription memberDescription, String propertyName, Node node) {
/* 295 */       Object newInstance = memberDescription.newInstance(propertyName, node);
/* 296 */       if (newInstance != null) {
/* 297 */         Constructor.this.constructedObjects.put(node, newInstance);
/* 298 */         return Constructor.this.constructObjectNoCheck(node);
/*     */       } 
/* 300 */       return Constructor.this.constructObject(node);
/*     */     }
/*     */     
/*     */     protected Property getProperty(Class<? extends Object> type, String name) {
/* 304 */       return Constructor.this.getPropertyUtils().getProperty(type, name);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructYamlObject
/*     */     implements Construct
/*     */   {
/*     */     private Construct getConstructor(Node node) {
/* 316 */       Class<?> cl = Constructor.this.getClassForNode(node);
/* 317 */       node.setType(cl);
/*     */       
/* 319 */       Construct constructor = Constructor.this.yamlClassConstructors.get(node.getNodeId());
/* 320 */       return constructor;
/*     */     }
/*     */     
/*     */     public Object construct(Node node) {
/*     */       try {
/* 325 */         return getConstructor(node).construct(node);
/* 326 */       } catch (ConstructorException e) {
/* 327 */         throw e;
/* 328 */       } catch (Exception e) {
/* 329 */         throw new ConstructorException(null, null, "Can't construct a java object for " + node
/* 330 */             .getTag() + "; exception=" + e.getMessage(), node
/* 331 */             .getStartMark(), e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/*     */       try {
/* 337 */         getConstructor(node).construct2ndStep(node, object);
/* 338 */       } catch (Exception e) {
/* 339 */         throw new ConstructorException(null, null, "Can't construct a second step for a java object for " + node
/* 340 */             .getTag() + "; exception=" + e
/* 341 */             .getMessage(), node
/* 342 */             .getStartMark(), e);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructScalar
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node nnode) {
/*     */       Object result;
/* 354 */       ScalarNode node = (ScalarNode)nnode;
/* 355 */       Class<?> type = node.getType();
/*     */ 
/*     */       
/* 358 */       Object instance = Constructor.this.newInstance(type, (Node)node, false);
/* 359 */       if (instance != BaseConstructor.NOT_INSTANTIATED_OBJECT) {
/* 360 */         return instance;
/*     */       }
/*     */ 
/*     */       
/* 364 */       if (type.isPrimitive() || type == String.class || Number.class.isAssignableFrom(type) || type == Boolean.class || Date.class
/* 365 */         .isAssignableFrom(type) || type == Character.class || type == BigInteger.class || type == BigDecimal.class || Enum.class
/*     */         
/* 367 */         .isAssignableFrom(type) || Tag.BINARY.equals(node.getTag()) || Calendar.class
/* 368 */         .isAssignableFrom(type) || type == UUID.class) {
/*     */         
/* 370 */         result = constructStandardJavaInstance(type, node);
/*     */       } else {
/*     */         Object argument;
/* 373 */         Constructor[] arrayOfConstructor = (Constructor[])type.getDeclaredConstructors();
/* 374 */         int oneArgCount = 0;
/* 375 */         Constructor<?> javaConstructor = null;
/* 376 */         for (Constructor<?> c : arrayOfConstructor) {
/* 377 */           if ((c.getParameterTypes()).length == 1) {
/* 378 */             oneArgCount++;
/* 379 */             javaConstructor = c;
/*     */           } 
/*     */         } 
/*     */         
/* 383 */         if (javaConstructor == null)
/* 384 */           throw new YAMLException("No single argument constructor found for " + type); 
/* 385 */         if (oneArgCount == 1) {
/* 386 */           argument = constructStandardJavaInstance(javaConstructor.getParameterTypes()[0], node);
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */ 
/*     */           
/* 394 */           argument = Constructor.this.constructScalar(node);
/*     */           try {
/* 396 */             javaConstructor = type.getDeclaredConstructor(new Class[] { String.class });
/* 397 */           } catch (Exception e) {
/* 398 */             throw new YAMLException("Can't construct a java object for scalar " + node.getTag() + "; No String constructor found. Exception=" + e
/* 399 */                 .getMessage(), e);
/*     */           } 
/*     */         } 
/*     */         try {
/* 403 */           javaConstructor.setAccessible(true);
/* 404 */           result = javaConstructor.newInstance(new Object[] { argument });
/* 405 */         } catch (Exception e) {
/* 406 */           throw new ConstructorException(null, null, "Can't construct a java object for scalar " + node
/* 407 */               .getTag() + "; exception=" + e.getMessage(), node.getStartMark(), e);
/*     */         } 
/*     */       } 
/* 410 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private Object constructStandardJavaInstance(Class<String> type, ScalarNode node) {
/*     */       Object result;
/* 417 */       if (type == String.class) {
/* 418 */         Construct stringConstructor = Constructor.this.yamlConstructors.get(Tag.STR);
/* 419 */         result = stringConstructor.construct((Node)node);
/* 420 */       } else if (type == Boolean.class || type == boolean.class) {
/* 421 */         Construct boolConstructor = Constructor.this.yamlConstructors.get(Tag.BOOL);
/* 422 */         result = boolConstructor.construct((Node)node);
/* 423 */       } else if (type == Character.class || type == char.class) {
/* 424 */         Construct charConstructor = Constructor.this.yamlConstructors.get(Tag.STR);
/* 425 */         String ch = (String)charConstructor.construct((Node)node);
/* 426 */         if (ch.length() == 0)
/* 427 */         { result = null; }
/* 428 */         else { if (ch.length() != 1) {
/* 429 */             throw new YAMLException("Invalid node Character: '" + ch + "'; length: " + ch.length());
/*     */           }
/* 431 */           result = Character.valueOf(ch.charAt(0)); }
/*     */       
/* 433 */       } else if (Date.class.isAssignableFrom(type)) {
/* 434 */         Construct dateConstructor = Constructor.this.yamlConstructors.get(Tag.TIMESTAMP);
/* 435 */         Date date = (Date)dateConstructor.construct((Node)node);
/* 436 */         if (type == Date.class) {
/* 437 */           result = date;
/*     */         } else {
/*     */           try {
/* 440 */             Constructor<?> constr = type.getConstructor(new Class[] { long.class });
/* 441 */             result = constr.newInstance(new Object[] { Long.valueOf(date.getTime()) });
/* 442 */           } catch (RuntimeException e) {
/* 443 */             throw e;
/* 444 */           } catch (Exception e) {
/* 445 */             throw new YAMLException("Cannot construct: '" + type + "'");
/*     */           } 
/*     */         } 
/* 448 */       } else if (type == Float.class || type == Double.class || type == float.class || type == double.class || type == BigDecimal.class) {
/*     */         
/* 450 */         if (type == BigDecimal.class) {
/* 451 */           result = new BigDecimal(node.getValue());
/*     */         } else {
/* 453 */           Construct doubleConstructor = Constructor.this.yamlConstructors.get(Tag.FLOAT);
/* 454 */           result = doubleConstructor.construct((Node)node);
/* 455 */           if (type == Float.class || type == float.class) {
/* 456 */             result = Float.valueOf(((Double)result).floatValue());
/*     */           }
/*     */         } 
/* 459 */       } else if (type == Byte.class || type == Short.class || type == Integer.class || type == Long.class || type == BigInteger.class || type == byte.class || type == short.class || type == int.class || type == long.class) {
/*     */ 
/*     */         
/* 462 */         Construct intConstructor = Constructor.this.yamlConstructors.get(Tag.INT);
/* 463 */         result = intConstructor.construct((Node)node);
/* 464 */         if (type == Byte.class || type == byte.class) {
/* 465 */           result = Byte.valueOf(Integer.valueOf(result.toString()).byteValue());
/* 466 */         } else if (type == Short.class || type == short.class) {
/* 467 */           result = Short.valueOf(Integer.valueOf(result.toString()).shortValue());
/* 468 */         } else if (type == Integer.class || type == int.class) {
/* 469 */           result = Integer.valueOf(Integer.parseInt(result.toString()));
/* 470 */         } else if (type == Long.class || type == long.class) {
/* 471 */           result = Long.valueOf(result.toString());
/*     */         } else {
/*     */           
/* 474 */           result = new BigInteger(result.toString());
/*     */         } 
/* 476 */       } else if (Enum.class.isAssignableFrom(type)) {
/* 477 */         String enumValueName = node.getValue();
/*     */         try {
/* 479 */           if (Constructor.this.loadingConfig.isEnumCaseSensitive()) {
/* 480 */             result = Enum.valueOf(type, enumValueName);
/*     */           } else {
/* 482 */             result = EnumUtils.findEnumInsensitiveCase(type, enumValueName);
/*     */           } 
/* 484 */         } catch (Exception ex) {
/* 485 */           throw new YAMLException("Unable to find enum value '" + enumValueName + "' for enum class: " + type
/* 486 */               .getName());
/*     */         } 
/* 488 */       } else if (Calendar.class.isAssignableFrom(type)) {
/* 489 */         SafeConstructor.ConstructYamlTimestamp contr = new SafeConstructor.ConstructYamlTimestamp();
/* 490 */         contr.construct((Node)node);
/* 491 */         result = contr.getCalendar();
/* 492 */       } else if (Number.class.isAssignableFrom(type)) {
/*     */         
/* 494 */         SafeConstructor.ConstructYamlFloat contr = new SafeConstructor.ConstructYamlFloat(Constructor.this);
/* 495 */         result = contr.construct((Node)node);
/* 496 */       } else if (UUID.class == type) {
/* 497 */         result = UUID.fromString(node.getValue());
/*     */       }
/* 499 */       else if (Constructor.this.yamlConstructors.containsKey(node.getTag())) {
/* 500 */         result = ((Construct)Constructor.this.yamlConstructors.get(node.getTag())).construct((Node)node);
/*     */       } else {
/* 502 */         throw new YAMLException("Unsupported class: " + type);
/*     */       } 
/*     */       
/* 505 */       return result;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class ConstructSequence
/*     */     implements Construct
/*     */   {
/*     */     public Object construct(Node node) {
/* 516 */       SequenceNode snode = (SequenceNode)node;
/* 517 */       if (Set.class.isAssignableFrom(node.getType())) {
/* 518 */         if (node.isTwoStepsConstruction()) {
/* 519 */           throw new YAMLException("Set cannot be recursive.");
/*     */         }
/* 521 */         return Constructor.this.constructSet(snode);
/*     */       } 
/* 523 */       if (Collection.class.isAssignableFrom(node.getType())) {
/* 524 */         if (node.isTwoStepsConstruction()) {
/* 525 */           return Constructor.this.newList(snode);
/*     */         }
/* 527 */         return Constructor.this.constructSequence(snode);
/*     */       } 
/* 529 */       if (node.getType().isArray()) {
/* 530 */         if (node.isTwoStepsConstruction()) {
/* 531 */           return Constructor.this.createArray(node.getType(), snode.getValue().size());
/*     */         }
/* 533 */         return Constructor.this.constructArray(snode);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 538 */       List<Constructor<?>> possibleConstructors = new ArrayList<>(snode.getValue().size());
/* 539 */       for (Constructor<?> constructor : node.getType()
/* 540 */         .getDeclaredConstructors()) {
/* 541 */         if (snode.getValue().size() == (constructor.getParameterTypes()).length) {
/* 542 */           possibleConstructors.add(constructor);
/*     */         }
/*     */       } 
/* 545 */       if (!possibleConstructors.isEmpty()) {
/* 546 */         if (possibleConstructors.size() == 1) {
/* 547 */           Object[] arrayOfObject = new Object[snode.getValue().size()];
/* 548 */           Constructor<?> c = possibleConstructors.get(0);
/* 549 */           int i = 0;
/* 550 */           for (Node argumentNode : snode.getValue()) {
/* 551 */             Class<?> type = c.getParameterTypes()[i];
/*     */             
/* 553 */             argumentNode.setType(type);
/* 554 */             arrayOfObject[i++] = Constructor.this.constructObject(argumentNode);
/*     */           } 
/*     */           
/*     */           try {
/* 558 */             c.setAccessible(true);
/* 559 */             return c.newInstance(arrayOfObject);
/* 560 */           } catch (Exception e) {
/* 561 */             throw new YAMLException(e);
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 566 */         List<Object> argumentList = (List)Constructor.this.constructSequence(snode);
/* 567 */         Class<?>[] parameterTypes = new Class[argumentList.size()];
/* 568 */         int index = 0;
/* 569 */         for (Object parameter : argumentList) {
/* 570 */           parameterTypes[index] = parameter.getClass();
/* 571 */           index++;
/*     */         } 
/*     */         
/* 574 */         for (Constructor<?> c : possibleConstructors) {
/* 575 */           Class<?>[] argTypes = c.getParameterTypes();
/* 576 */           boolean foundConstructor = true;
/* 577 */           for (int i = 0; i < argTypes.length; i++) {
/* 578 */             if (!wrapIfPrimitive(argTypes[i]).isAssignableFrom(parameterTypes[i])) {
/* 579 */               foundConstructor = false;
/*     */               break;
/*     */             } 
/*     */           } 
/* 583 */           if (foundConstructor) {
/*     */             try {
/* 585 */               c.setAccessible(true);
/* 586 */               return c.newInstance(argumentList.toArray());
/* 587 */             } catch (Exception e) {
/* 588 */               throw new YAMLException(e);
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/* 593 */       throw new YAMLException("No suitable constructor with " + snode.getValue().size() + " arguments found for " + node
/* 594 */           .getType());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private final Class<? extends Object> wrapIfPrimitive(Class<?> clazz) {
/* 600 */       if (!clazz.isPrimitive()) {
/* 601 */         return (Class)clazz;
/*     */       }
/* 603 */       if (clazz == int.class) {
/* 604 */         return (Class)Integer.class;
/*     */       }
/* 606 */       if (clazz == float.class) {
/* 607 */         return (Class)Float.class;
/*     */       }
/* 609 */       if (clazz == double.class) {
/* 610 */         return (Class)Double.class;
/*     */       }
/* 612 */       if (clazz == boolean.class) {
/* 613 */         return (Class)Boolean.class;
/*     */       }
/* 615 */       if (clazz == long.class) {
/* 616 */         return (Class)Long.class;
/*     */       }
/* 618 */       if (clazz == char.class) {
/* 619 */         return (Class)Character.class;
/*     */       }
/* 621 */       if (clazz == short.class) {
/* 622 */         return (Class)Short.class;
/*     */       }
/* 624 */       if (clazz == byte.class) {
/* 625 */         return (Class)Byte.class;
/*     */       }
/* 627 */       throw new YAMLException("Unexpected primitive " + clazz);
/*     */     }
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 632 */       SequenceNode snode = (SequenceNode)node;
/* 633 */       if (List.class.isAssignableFrom(node.getType())) {
/* 634 */         List<Object> list = (List<Object>)object;
/* 635 */         Constructor.this.constructSequenceStep2(snode, list);
/* 636 */       } else if (node.getType().isArray()) {
/* 637 */         Constructor.this.constructArrayStep2(snode, object);
/*     */       } else {
/* 639 */         throw new YAMLException("Immutable objects cannot be recursive.");
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   protected Class<?> getClassForNode(Node node) {
/* 645 */     Class<? extends Object> classForTag = this.typeTags.get(node.getTag());
/* 646 */     if (classForTag == null) {
/* 647 */       Class<?> cl; String name = node.getTag().getClassName();
/*     */       
/*     */       try {
/* 650 */         cl = getClassForName(name);
/* 651 */       } catch (ClassNotFoundException e) {
/* 652 */         throw new YAMLException("Class not found: " + name);
/*     */       } 
/* 654 */       this.typeTags.put(node.getTag(), cl);
/* 655 */       return cl;
/*     */     } 
/* 657 */     return classForTag;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Class<?> getClassForName(String name) throws ClassNotFoundException {
/*     */     try {
/* 663 */       return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
/* 664 */     } catch (ClassNotFoundException e) {
/* 665 */       return Class.forName(name);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\constructor\Constructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */