/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyFilter;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.MapEntrySerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
/*     */ import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class BeanSerializerBase extends StdSerializer<Object> implements ContextualSerializer, ResolvableSerializer, JsonFormatVisitable, SchemaAware {
/*  42 */   protected static final PropertyName NAME_FOR_OBJECT_REF = new PropertyName("#object-ref");
/*     */   
/*  44 */   protected static final BeanPropertyWriter[] NO_PROPS = new BeanPropertyWriter[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JavaType _beanType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BeanPropertyWriter[] _props;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final BeanPropertyWriter[] _filteredProps;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnyGetterWriter _anyGetterWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final Object _propertyFilterId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final AnnotatedMember _typeId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final ObjectIdWriter _objectIdWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final JsonFormat.Shape _serializationShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
/* 113 */     super(type);
/* 114 */     this._beanType = type;
/* 115 */     this._props = properties;
/* 116 */     this._filteredProps = filteredProperties;
/* 117 */     if (builder == null) {
/*     */ 
/*     */       
/* 120 */       this._typeId = null;
/* 121 */       this._anyGetterWriter = null;
/* 122 */       this._propertyFilterId = null;
/* 123 */       this._objectIdWriter = null;
/* 124 */       this._serializationShape = null;
/*     */     } else {
/* 126 */       this._typeId = builder.getTypeId();
/* 127 */       this._anyGetterWriter = builder.getAnyGetter();
/* 128 */       this._propertyFilterId = builder.getFilterId();
/* 129 */       this._objectIdWriter = builder.getObjectIdWriter();
/* 130 */       JsonFormat.Value format = builder.getBeanDescription().findExpectedFormat(null);
/* 131 */       this._serializationShape = (format == null) ? null : format.getShape();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BeanSerializerBase(BeanSerializerBase src, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties) {
/* 138 */     super(src._handledType);
/* 139 */     this._beanType = src._beanType;
/* 140 */     this._props = properties;
/* 141 */     this._filteredProps = filteredProperties;
/*     */     
/* 143 */     this._typeId = src._typeId;
/* 144 */     this._anyGetterWriter = src._anyGetterWriter;
/* 145 */     this._objectIdWriter = src._objectIdWriter;
/* 146 */     this._propertyFilterId = src._propertyFilterId;
/* 147 */     this._serializationShape = src._serializationShape;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase(BeanSerializerBase src, ObjectIdWriter objectIdWriter) {
/* 153 */     this(src, objectIdWriter, src._propertyFilterId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase(BeanSerializerBase src, ObjectIdWriter objectIdWriter, Object filterId) {
/* 162 */     super(src._handledType);
/* 163 */     this._beanType = src._beanType;
/* 164 */     this._props = src._props;
/* 165 */     this._filteredProps = src._filteredProps;
/*     */     
/* 167 */     this._typeId = src._typeId;
/* 168 */     this._anyGetterWriter = src._anyGetterWriter;
/* 169 */     this._objectIdWriter = objectIdWriter;
/* 170 */     this._propertyFilterId = filterId;
/* 171 */     this._serializationShape = src._serializationShape;
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected BeanSerializerBase(BeanSerializerBase src, String[] toIgnore) {
/* 177 */     this(src, ArrayBuilders.arrayToSet((Object[])toIgnore));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase(BeanSerializerBase src, Set<String> toIgnore) {
/* 182 */     super(src._handledType);
/*     */     
/* 184 */     this._beanType = src._beanType;
/* 185 */     BeanPropertyWriter[] propsIn = src._props;
/* 186 */     BeanPropertyWriter[] fpropsIn = src._filteredProps;
/* 187 */     int len = propsIn.length;
/*     */     
/* 189 */     ArrayList<BeanPropertyWriter> propsOut = new ArrayList<>(len);
/* 190 */     ArrayList<BeanPropertyWriter> fpropsOut = (fpropsIn == null) ? null : new ArrayList<>(len);
/*     */     
/* 192 */     for (int i = 0; i < len; i++) {
/* 193 */       BeanPropertyWriter bpw = propsIn[i];
/*     */       
/* 195 */       if (toIgnore == null || !toIgnore.contains(bpw.getName())) {
/*     */ 
/*     */         
/* 198 */         propsOut.add(bpw);
/* 199 */         if (fpropsIn != null)
/* 200 */           fpropsOut.add(fpropsIn[i]); 
/*     */       } 
/*     */     } 
/* 203 */     this._props = propsOut.<BeanPropertyWriter>toArray(new BeanPropertyWriter[propsOut.size()]);
/* 204 */     this._filteredProps = (fpropsOut == null) ? null : fpropsOut.<BeanPropertyWriter>toArray(new BeanPropertyWriter[fpropsOut.size()]);
/*     */     
/* 206 */     this._typeId = src._typeId;
/* 207 */     this._anyGetterWriter = src._anyGetterWriter;
/* 208 */     this._objectIdWriter = src._objectIdWriter;
/* 209 */     this._propertyFilterId = src._propertyFilterId;
/* 210 */     this._serializationShape = src._serializationShape;
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
/*     */   @Deprecated
/*     */   protected BeanSerializerBase withIgnorals(String[] toIgnore) {
/* 237 */     return withIgnorals(ArrayBuilders.arrayToSet((Object[])toIgnore));
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
/*     */   protected BeanSerializerBase(BeanSerializerBase src) {
/* 263 */     this(src, src._props, src._filteredProps);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected BeanSerializerBase(BeanSerializerBase src, NameTransformer unwrapper) {
/* 271 */     this(src, rename(src._props, unwrapper), rename(src._filteredProps, unwrapper));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final BeanPropertyWriter[] rename(BeanPropertyWriter[] props, NameTransformer transformer) {
/* 277 */     if (props == null || props.length == 0 || transformer == null || transformer == NameTransformer.NOP) {
/* 278 */       return props;
/*     */     }
/* 280 */     int len = props.length;
/* 281 */     BeanPropertyWriter[] result = new BeanPropertyWriter[len];
/* 282 */     for (int i = 0; i < len; i++) {
/* 283 */       BeanPropertyWriter bpw = props[i];
/* 284 */       if (bpw != null) {
/* 285 */         result[i] = bpw.rename(transformer);
/*     */       }
/*     */     } 
/* 288 */     return result;
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
/*     */   public void resolve(SerializerProvider provider) throws JsonMappingException {
/* 305 */     int filteredCount = (this._filteredProps == null) ? 0 : this._filteredProps.length;
/* 306 */     for (int i = 0, len = this._props.length; i < len; i++) {
/* 307 */       ContainerSerializer containerSerializer; BeanPropertyWriter prop = this._props[i];
/*     */       
/* 309 */       if (!prop.willSuppressNulls() && !prop.hasNullSerializer()) {
/* 310 */         JsonSerializer<Object> nullSer = provider.findNullValueSerializer((BeanProperty)prop);
/* 311 */         if (nullSer != null) {
/* 312 */           prop.assignNullSerializer(nullSer);
/*     */           
/* 314 */           if (i < filteredCount) {
/* 315 */             BeanPropertyWriter w2 = this._filteredProps[i];
/* 316 */             if (w2 != null) {
/* 317 */               w2.assignNullSerializer(nullSer);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 323 */       if (prop.hasSerializer()) {
/*     */         continue;
/*     */       }
/*     */       
/* 327 */       JsonSerializer<Object> ser = findConvertingSerializer(provider, prop);
/* 328 */       if (ser == null) {
/*     */         
/* 330 */         JavaType type = prop.getSerializationType();
/*     */ 
/*     */ 
/*     */         
/* 334 */         if (type == null) {
/* 335 */           type = prop.getType();
/* 336 */           if (!type.isFinal()) {
/* 337 */             if (type.isContainerType() || type.containedTypeCount() > 0) {
/* 338 */               prop.setNonTrivialBaseType(type);
/*     */             }
/*     */             continue;
/*     */           } 
/*     */         } 
/* 343 */         ser = provider.findValueSerializer(type, (BeanProperty)prop);
/*     */ 
/*     */ 
/*     */         
/* 347 */         if (type.isContainerType()) {
/* 348 */           TypeSerializer typeSer = (TypeSerializer)type.getContentType().getTypeHandler();
/* 349 */           if (typeSer != null)
/*     */           {
/* 351 */             if (ser instanceof ContainerSerializer) {
/*     */ 
/*     */               
/* 354 */               ContainerSerializer containerSerializer1 = ((ContainerSerializer)ser).withValueTypeSerializer(typeSer);
/* 355 */               containerSerializer = containerSerializer1;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 361 */       if (i < filteredCount) {
/* 362 */         BeanPropertyWriter w2 = this._filteredProps[i];
/* 363 */         if (w2 != null) {
/* 364 */           w2.assignSerializer((JsonSerializer)containerSerializer);
/*     */ 
/*     */           
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */       
/* 371 */       prop.assignSerializer((JsonSerializer)containerSerializer);
/*     */       
/*     */       continue;
/*     */     } 
/* 375 */     if (this._anyGetterWriter != null)
/*     */     {
/* 377 */       this._anyGetterWriter.resolve(provider);
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
/*     */   protected JsonSerializer<Object> findConvertingSerializer(SerializerProvider provider, BeanPropertyWriter prop) throws JsonMappingException {
/* 392 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 393 */     if (intr != null) {
/* 394 */       AnnotatedMember m = prop.getMember();
/* 395 */       if (m != null) {
/* 396 */         Object convDef = intr.findSerializationConverter((Annotated)m);
/* 397 */         if (convDef != null) {
/* 398 */           Converter<Object, Object> conv = provider.converterInstance((Annotated)prop.getMember(), convDef);
/* 399 */           JavaType delegateType = conv.getOutputType(provider.getTypeFactory());
/*     */ 
/*     */           
/* 402 */           JsonSerializer<?> ser = delegateType.isJavaLangObject() ? null : provider.findValueSerializer(delegateType, (BeanProperty)prop);
/* 403 */           return new StdDelegatingSerializer(conv, delegateType, ser);
/*     */         } 
/*     */       } 
/*     */     } 
/* 407 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property) throws JsonMappingException {
/* 416 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/*     */     
/* 418 */     AnnotatedMember accessor = (property == null || intr == null) ? null : property.getMember();
/* 419 */     SerializationConfig config = provider.getConfig();
/*     */ 
/*     */ 
/*     */     
/* 423 */     JsonFormat.Value format = findFormatOverrides(provider, property, handledType());
/* 424 */     JsonFormat.Shape shape = null;
/* 425 */     if (format != null && format.hasShape()) {
/* 426 */       shape = format.getShape();
/*     */       
/* 428 */       if (shape != JsonFormat.Shape.ANY && shape != this._serializationShape) {
/* 429 */         if (ClassUtil.isEnumType(this._handledType)) {
/* 430 */           BeanDescription desc; JsonSerializer<?> ser; switch (shape) {
/*     */ 
/*     */             
/*     */             case STRING:
/*     */             case NUMBER:
/*     */             case NUMBER_INT:
/* 436 */               desc = config.introspectClassAnnotations(this._beanType);
/* 437 */               ser = EnumSerializer.construct(this._beanType.getRawClass(), provider
/* 438 */                   .getConfig(), desc, format);
/* 439 */               return provider.handlePrimaryContextualization(ser, property);
/*     */           } 
/*     */         
/* 442 */         } else if (shape == JsonFormat.Shape.NATURAL && (
/* 443 */           !this._beanType.isMapLikeType() || !Map.class.isAssignableFrom(this._handledType))) {
/*     */           
/* 445 */           if (Map.Entry.class.isAssignableFrom(this._handledType)) {
/* 446 */             JavaType mapEntryType = this._beanType.findSuperType(Map.Entry.class);
/*     */             
/* 448 */             JavaType kt = mapEntryType.containedTypeOrUnknown(0);
/* 449 */             JavaType vt = mapEntryType.containedTypeOrUnknown(1);
/*     */ 
/*     */ 
/*     */             
/* 453 */             MapEntrySerializer mapEntrySerializer = new MapEntrySerializer(this._beanType, kt, vt, false, null, property);
/*     */             
/* 455 */             return provider.handlePrimaryContextualization((JsonSerializer)mapEntrySerializer, property);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 461 */     ObjectIdWriter oiw = this._objectIdWriter;
/* 462 */     Set<String> ignoredProps = null;
/* 463 */     Object newFilterId = null;
/*     */ 
/*     */     
/* 466 */     if (accessor != null) {
/* 467 */       JsonIgnoreProperties.Value ignorals = intr.findPropertyIgnorals((Annotated)accessor);
/* 468 */       if (ignorals != null) {
/* 469 */         ignoredProps = ignorals.findIgnoredForSerialization();
/*     */       }
/* 471 */       ObjectIdInfo objectIdInfo = intr.findObjectIdInfo((Annotated)accessor);
/* 472 */       if (objectIdInfo == null) {
/*     */         
/* 474 */         if (oiw != null) {
/* 475 */           objectIdInfo = intr.findObjectReferenceInfo((Annotated)accessor, null);
/* 476 */           if (objectIdInfo != null) {
/* 477 */             oiw = this._objectIdWriter.withAlwaysAsId(objectIdInfo.getAlwaysAsId());
/*     */           
/*     */           }
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 485 */         objectIdInfo = intr.findObjectReferenceInfo((Annotated)accessor, objectIdInfo);
/*     */         
/* 487 */         Class<?> implClass = objectIdInfo.getGeneratorType();
/* 488 */         JavaType type = provider.constructType(implClass);
/* 489 */         JavaType idType = provider.getTypeFactory().findTypeParameters(type, ObjectIdGenerator.class)[0];
/*     */         
/* 491 */         if (implClass == ObjectIdGenerators.PropertyGenerator.class) {
/* 492 */           String propName = objectIdInfo.getPropertyName().getSimpleName();
/* 493 */           BeanPropertyWriter idProp = null;
/*     */           
/* 495 */           for (int i = 0, len = this._props.length;; i++) {
/* 496 */             if (i == len)
/* 497 */               provider.reportBadDefinition(this._beanType, String.format("Invalid Object Id definition for %s: cannot find property with name '%s'", new Object[] {
/*     */                       
/* 499 */                       handledType().getName(), propName
/*     */                     })); 
/* 501 */             BeanPropertyWriter prop = this._props[i];
/* 502 */             if (propName.equals(prop.getName())) {
/* 503 */               idProp = prop;
/*     */ 
/*     */               
/* 506 */               if (i > 0) {
/* 507 */                 System.arraycopy(this._props, 0, this._props, 1, i);
/* 508 */                 this._props[0] = idProp;
/* 509 */                 if (this._filteredProps != null) {
/* 510 */                   BeanPropertyWriter fp = this._filteredProps[i];
/* 511 */                   System.arraycopy(this._filteredProps, 0, this._filteredProps, 1, i);
/* 512 */                   this._filteredProps[0] = fp;
/*     */                 } 
/*     */               } 
/*     */               break;
/*     */             } 
/*     */           } 
/* 518 */           idType = idProp.getType();
/* 519 */           PropertyBasedObjectIdGenerator propertyBasedObjectIdGenerator = new PropertyBasedObjectIdGenerator(objectIdInfo, idProp);
/* 520 */           oiw = ObjectIdWriter.construct(idType, (PropertyName)null, (ObjectIdGenerator)propertyBasedObjectIdGenerator, objectIdInfo.getAlwaysAsId());
/*     */         } else {
/* 522 */           ObjectIdGenerator<?> gen = provider.objectIdGeneratorInstance((Annotated)accessor, objectIdInfo);
/* 523 */           oiw = ObjectIdWriter.construct(idType, objectIdInfo.getPropertyName(), gen, objectIdInfo
/* 524 */               .getAlwaysAsId());
/*     */         } 
/*     */       } 
/*     */       
/* 528 */       Object filterId = intr.findFilterId((Annotated)accessor);
/* 529 */       if (filterId != null)
/*     */       {
/* 531 */         if (this._propertyFilterId == null || !filterId.equals(this._propertyFilterId)) {
/* 532 */           newFilterId = filterId;
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/* 537 */     BeanSerializerBase contextual = this;
/* 538 */     if (oiw != null) {
/* 539 */       JsonSerializer<?> ser = provider.findValueSerializer(oiw.idType, property);
/* 540 */       oiw = oiw.withSerializer(ser);
/* 541 */       if (oiw != this._objectIdWriter) {
/* 542 */         contextual = contextual.withObjectIdWriter(oiw);
/*     */       }
/*     */     } 
/*     */     
/* 546 */     if (ignoredProps != null && !ignoredProps.isEmpty()) {
/* 547 */       contextual = contextual.withIgnorals(ignoredProps);
/*     */     }
/* 549 */     if (newFilterId != null) {
/* 550 */       contextual = contextual.withFilterId(newFilterId);
/*     */     }
/* 552 */     if (shape == null) {
/* 553 */       shape = this._serializationShape;
/*     */     }
/*     */     
/* 556 */     if (shape == JsonFormat.Shape.ARRAY) {
/* 557 */       return contextual.asArraySerializer();
/*     */     }
/* 559 */     return contextual;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<PropertyWriter> properties() {
/* 570 */     return Arrays.asList((Object[])this._props).iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean usesObjectId() {
/* 581 */     return (this._objectIdWriter != null);
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
/*     */   public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 595 */     if (this._objectIdWriter != null) {
/* 596 */       gen.setCurrentValue(bean);
/* 597 */       _serializeWithObjectId(bean, gen, provider, typeSer);
/*     */       
/*     */       return;
/*     */     } 
/* 601 */     gen.setCurrentValue(bean);
/* 602 */     WritableTypeId typeIdDef = _typeIdDef(typeSer, bean, JsonToken.START_OBJECT);
/* 603 */     typeSer.writeTypePrefix(gen, typeIdDef);
/* 604 */     if (this._propertyFilterId != null) {
/* 605 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 607 */       serializeFields(bean, gen, provider);
/*     */     } 
/* 609 */     typeSer.writeTypeSuffix(gen, typeIdDef);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void _serializeWithObjectId(Object bean, JsonGenerator gen, SerializerProvider provider, boolean startEndObject) throws IOException {
/* 615 */     ObjectIdWriter w = this._objectIdWriter;
/* 616 */     WritableObjectId objectId = provider.findObjectId(bean, w.generator);
/*     */     
/* 618 */     if (objectId.writeAsId(gen, provider, w)) {
/*     */       return;
/*     */     }
/*     */     
/* 622 */     Object id = objectId.generateId(bean);
/* 623 */     if (w.alwaysAsId) {
/* 624 */       w.serializer.serialize(id, gen, provider);
/*     */       return;
/*     */     } 
/* 627 */     if (startEndObject) {
/* 628 */       gen.writeStartObject(bean);
/*     */     }
/* 630 */     objectId.writeAsField(gen, provider, w);
/* 631 */     if (this._propertyFilterId != null) {
/* 632 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 634 */       serializeFields(bean, gen, provider);
/*     */     } 
/* 636 */     if (startEndObject) {
/* 637 */       gen.writeEndObject();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected final void _serializeWithObjectId(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/* 644 */     ObjectIdWriter w = this._objectIdWriter;
/* 645 */     WritableObjectId objectId = provider.findObjectId(bean, w.generator);
/*     */     
/* 647 */     if (objectId.writeAsId(gen, provider, w)) {
/*     */       return;
/*     */     }
/*     */     
/* 651 */     Object id = objectId.generateId(bean);
/* 652 */     if (w.alwaysAsId) {
/* 653 */       w.serializer.serialize(id, gen, provider);
/*     */       return;
/*     */     } 
/* 656 */     _serializeObjectId(bean, gen, provider, typeSer, objectId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void _serializeObjectId(Object bean, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer, WritableObjectId objectId) throws IOException {
/* 663 */     ObjectIdWriter w = this._objectIdWriter;
/* 664 */     WritableTypeId typeIdDef = _typeIdDef(typeSer, bean, JsonToken.START_OBJECT);
/*     */     
/* 666 */     typeSer.writeTypePrefix(g, typeIdDef);
/* 667 */     objectId.writeAsField(g, provider, w);
/* 668 */     if (this._propertyFilterId != null) {
/* 669 */       serializeFieldsFiltered(bean, g, provider);
/*     */     } else {
/* 671 */       serializeFields(bean, g, provider);
/*     */     } 
/* 673 */     typeSer.writeTypeSuffix(g, typeIdDef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected final WritableTypeId _typeIdDef(TypeSerializer typeSer, Object bean, JsonToken valueShape) {
/* 681 */     if (this._typeId == null) {
/* 682 */       return typeSer.typeId(bean, valueShape);
/*     */     }
/* 684 */     Object typeId = this._typeId.getValue(bean);
/* 685 */     if (typeId == null)
/*     */     {
/* 687 */       typeId = "";
/*     */     }
/* 689 */     return typeSer.typeId(bean, valueShape, typeId);
/*     */   }
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected final String _customTypeId(Object bean) {
/* 695 */     Object typeId = this._typeId.getValue(bean);
/* 696 */     if (typeId == null) {
/* 697 */       return "";
/*     */     }
/* 699 */     return (typeId instanceof String) ? (String)typeId : typeId.toString();
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
/*     */   protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException {
/*     */     BeanPropertyWriter[] props;
/* 712 */     if (this._filteredProps != null && provider.getActiveView() != null) {
/* 713 */       props = this._filteredProps;
/*     */     } else {
/* 715 */       props = this._props;
/*     */     } 
/* 717 */     int i = 0;
/*     */     try {
/* 719 */       for (int len = props.length; i < len; i++) {
/* 720 */         BeanPropertyWriter prop = props[i];
/* 721 */         if (prop != null) {
/* 722 */           prop.serializeAsField(bean, gen, provider);
/*     */         }
/*     */       } 
/* 725 */       if (this._anyGetterWriter != null) {
/* 726 */         this._anyGetterWriter.getAndSerialize(bean, gen, provider);
/*     */       }
/* 728 */     } catch (Exception e) {
/* 729 */       String name = (i == props.length) ? "[anySetter]" : props[i].getName();
/* 730 */       wrapAndThrow(provider, e, bean, name);
/* 731 */     } catch (StackOverflowError e) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 737 */       JsonMappingException mapE = new JsonMappingException((Closeable)gen, "Infinite recursion (StackOverflowError)", e);
/*     */       
/* 739 */       String name = (i == props.length) ? "[anySetter]" : props[i].getName();
/* 740 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 741 */       throw mapE;
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
/*     */   protected void serializeFieldsFiltered(Object bean, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonGenerationException {
/*     */     BeanPropertyWriter[] props;
/* 758 */     if (this._filteredProps != null && provider.getActiveView() != null) {
/* 759 */       props = this._filteredProps;
/*     */     } else {
/* 761 */       props = this._props;
/*     */     } 
/* 763 */     PropertyFilter filter = findPropertyFilter(provider, this._propertyFilterId, bean);
/*     */     
/* 765 */     if (filter == null) {
/* 766 */       serializeFields(bean, gen, provider);
/*     */       return;
/*     */     } 
/* 769 */     int i = 0;
/*     */     try {
/* 771 */       for (int len = props.length; i < len; i++) {
/* 772 */         BeanPropertyWriter prop = props[i];
/* 773 */         if (prop != null) {
/* 774 */           filter.serializeAsField(bean, gen, provider, (PropertyWriter)prop);
/*     */         }
/*     */       } 
/* 777 */       if (this._anyGetterWriter != null) {
/* 778 */         this._anyGetterWriter.getAndFilter(bean, gen, provider, filter);
/*     */       }
/* 780 */     } catch (Exception e) {
/* 781 */       String name = (i == props.length) ? "[anySetter]" : props[i].getName();
/* 782 */       wrapAndThrow(provider, e, bean, name);
/* 783 */     } catch (StackOverflowError e) {
/*     */ 
/*     */       
/* 786 */       JsonMappingException mapE = new JsonMappingException((Closeable)gen, "Infinite recursion (StackOverflowError)", e);
/* 787 */       String name = (i == props.length) ? "[anySetter]" : props[i].getName();
/* 788 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 789 */       throw mapE;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException {
/*     */     PropertyFilter filter;
/* 798 */     ObjectNode o = createSchemaNode("object", true);
/*     */ 
/*     */     
/* 801 */     JsonSerializableSchema ann = (JsonSerializableSchema)this._handledType.getAnnotation(JsonSerializableSchema.class);
/* 802 */     if (ann != null) {
/* 803 */       String id = ann.id();
/* 804 */       if (id != null && id.length() > 0) {
/* 805 */         o.put("id", id);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 811 */     ObjectNode propertiesNode = o.objectNode();
/*     */     
/* 813 */     if (this._propertyFilterId != null) {
/* 814 */       filter = findPropertyFilter(provider, this._propertyFilterId, null);
/*     */     } else {
/* 816 */       filter = null;
/*     */     } 
/*     */     
/* 819 */     for (int i = 0; i < this._props.length; i++) {
/* 820 */       BeanPropertyWriter prop = this._props[i];
/* 821 */       if (filter == null) {
/* 822 */         prop.depositSchemaProperty(propertiesNode, provider);
/*     */       } else {
/* 824 */         filter.depositSchemaProperty((PropertyWriter)prop, propertiesNode, provider);
/*     */       } 
/*     */     } 
/*     */     
/* 828 */     o.set("properties", (JsonNode)propertiesNode);
/* 829 */     return (JsonNode)o;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 837 */     if (visitor == null) {
/*     */       return;
/*     */     }
/* 840 */     JsonObjectFormatVisitor objectVisitor = visitor.expectObjectFormat(typeHint);
/* 841 */     if (objectVisitor == null) {
/*     */       return;
/*     */     }
/* 844 */     SerializerProvider provider = visitor.getProvider();
/* 845 */     if (this._propertyFilterId != null) {
/* 846 */       PropertyFilter filter = findPropertyFilter(visitor.getProvider(), this._propertyFilterId, null);
/*     */       
/* 848 */       for (int i = 0, end = this._props.length; i < end; i++) {
/* 849 */         filter.depositSchemaProperty((PropertyWriter)this._props[i], objectVisitor, provider);
/*     */       }
/*     */     } else {
/*     */       BeanPropertyWriter[] props;
/* 853 */       Class<?> view = (this._filteredProps == null || provider == null) ? null : provider.getActiveView();
/*     */       
/* 855 */       if (view != null) {
/* 856 */         props = this._filteredProps;
/*     */       } else {
/* 858 */         props = this._props;
/*     */       } 
/*     */       
/* 861 */       for (int i = 0, end = props.length; i < end; i++) {
/* 862 */         BeanPropertyWriter prop = props[i];
/* 863 */         if (prop != null)
/* 864 */           prop.depositSchemaProperty(objectVisitor, provider); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public abstract BeanSerializerBase withObjectIdWriter(ObjectIdWriter paramObjectIdWriter);
/*     */   
/*     */   protected abstract BeanSerializerBase withIgnorals(Set<String> paramSet);
/*     */   
/*     */   protected abstract BeanSerializerBase asArraySerializer();
/*     */   
/*     */   public abstract BeanSerializerBase withFilterId(Object paramObject);
/*     */   
/*     */   public abstract void serialize(Object paramObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider) throws IOException;
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\ser\std\BeanSerializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */