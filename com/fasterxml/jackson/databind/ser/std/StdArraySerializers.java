/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.type.WritableTypeId;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class StdArraySerializers {
/*  25 */   protected static final HashMap<String, JsonSerializer<?>> _arraySerializers = new HashMap<>();
/*     */ 
/*     */   
/*     */   static {
/*  29 */     _arraySerializers.put(boolean[].class.getName(), new BooleanArraySerializer());
/*  30 */     _arraySerializers.put(byte[].class.getName(), new ByteArraySerializer());
/*  31 */     _arraySerializers.put(char[].class.getName(), new CharArraySerializer());
/*  32 */     _arraySerializers.put(short[].class.getName(), new ShortArraySerializer());
/*  33 */     _arraySerializers.put(int[].class.getName(), new IntArraySerializer());
/*  34 */     _arraySerializers.put(long[].class.getName(), new LongArraySerializer());
/*  35 */     _arraySerializers.put(float[].class.getName(), new FloatArraySerializer());
/*  36 */     _arraySerializers.put(double[].class.getName(), new DoubleArraySerializer());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static JsonSerializer<?> findStandardImpl(Class<?> cls) {
/*  46 */     return _arraySerializers.get(cls.getName());
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
/*     */   protected static abstract class TypedPrimitiveArraySerializer<T>
/*     */     extends ArraySerializerBase<T>
/*     */   {
/*     */     protected TypedPrimitiveArraySerializer(Class<T> cls) {
/*  63 */       super(cls);
/*     */     }
/*     */ 
/*     */     
/*     */     protected TypedPrimitiveArraySerializer(TypedPrimitiveArraySerializer<T> src, BeanProperty prop, Boolean unwrapSingle) {
/*  68 */       super(src, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public final ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
/*  76 */       return this;
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
/*     */   @JacksonStdImpl
/*     */   public static class BooleanArraySerializer
/*     */     extends ArraySerializerBase<boolean[]>
/*     */   {
/*  92 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Boolean.class);
/*     */     public BooleanArraySerializer() {
/*  94 */       super((Class)boolean[].class);
/*     */     }
/*     */     
/*     */     protected BooleanArraySerializer(BooleanArraySerializer src, BeanProperty prop, Boolean unwrapSingle) {
/*  98 */       super(src, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
/* 103 */       return (JsonSerializer<?>)new BooleanArraySerializer(this, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
/* 112 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public JavaType getContentType() {
/* 117 */       return VALUE_TYPE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> getContentSerializer() {
/* 123 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, boolean[] value) {
/* 128 */       return (value.length == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasSingleElement(boolean[] value) {
/* 133 */       return (value.length == 1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void serialize(boolean[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 139 */       int len = value.length;
/* 140 */       if (len == 1 && _shouldUnwrapSingle(provider)) {
/* 141 */         serializeContents(value, g, provider);
/*     */         return;
/*     */       } 
/* 144 */       g.writeStartArray(value, len);
/* 145 */       serializeContents(value, g, provider);
/* 146 */       g.writeEndArray();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeContents(boolean[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 153 */       for (int i = 0, len = value.length; i < len; i++) {
/* 154 */         g.writeBoolean(value[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 161 */       ObjectNode o = createSchemaNode("array", true);
/* 162 */       o.set("items", (JsonNode)createSchemaNode("boolean"));
/* 163 */       return (JsonNode)o;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 170 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.BOOLEAN);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class ShortArraySerializer
/*     */     extends TypedPrimitiveArraySerializer<short[]>
/*     */   {
/* 179 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(short.class);
/*     */     public ShortArraySerializer() {
/* 181 */       super((Class)short[].class);
/*     */     }
/*     */     public ShortArraySerializer(ShortArraySerializer src, BeanProperty prop, Boolean unwrapSingle) {
/* 184 */       super(src, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
/* 189 */       return (JsonSerializer<?>)new ShortArraySerializer(this, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */     
/*     */     public JavaType getContentType() {
/* 194 */       return VALUE_TYPE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> getContentSerializer() {
/* 200 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, short[] value) {
/* 205 */       return (value.length == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasSingleElement(short[] value) {
/* 210 */       return (value.length == 1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void serialize(short[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 216 */       int len = value.length;
/* 217 */       if (len == 1 && _shouldUnwrapSingle(provider)) {
/* 218 */         serializeContents(value, g, provider);
/*     */         return;
/*     */       } 
/* 221 */       g.writeStartArray(value, len);
/* 222 */       serializeContents(value, g, provider);
/* 223 */       g.writeEndArray();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeContents(short[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 231 */       for (int i = 0, len = value.length; i < len; i++) {
/* 232 */         g.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 240 */       ObjectNode o = createSchemaNode("array", true);
/* 241 */       return o.set("items", (JsonNode)createSchemaNode("integer"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 248 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.INTEGER);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class CharArraySerializer
/*     */     extends StdSerializer<char[]>
/*     */   {
/*     */     public CharArraySerializer() {
/* 262 */       super((Class)char[].class);
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, char[] value) {
/* 266 */       return (value.length == 0);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void serialize(char[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 274 */       if (provider.isEnabled(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
/* 275 */         g.writeStartArray(value, value.length);
/* 276 */         _writeArrayContents(g, value);
/* 277 */         g.writeEndArray();
/*     */       } else {
/* 279 */         g.writeString(value, 0, value.length);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeWithType(char[] value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer) throws IOException {
/*     */       WritableTypeId typeIdDef;
/* 289 */       boolean asArray = provider.isEnabled(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS);
/*     */       
/* 291 */       if (asArray) {
/* 292 */         typeIdDef = typeSer.writeTypePrefix(g, typeSer
/* 293 */             .typeId(value, JsonToken.START_ARRAY));
/* 294 */         _writeArrayContents(g, value);
/*     */       } else {
/* 296 */         typeIdDef = typeSer.writeTypePrefix(g, typeSer
/* 297 */             .typeId(value, JsonToken.VALUE_STRING));
/* 298 */         g.writeString(value, 0, value.length);
/*     */       } 
/* 300 */       typeSer.writeTypeSuffix(g, typeIdDef);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private final void _writeArrayContents(JsonGenerator g, char[] value) throws IOException {
/* 306 */       for (int i = 0, len = value.length; i < len; i++) {
/* 307 */         g.writeString(value, i, 1);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 314 */       ObjectNode o = createSchemaNode("array", true);
/* 315 */       ObjectNode itemSchema = createSchemaNode("string");
/* 316 */       itemSchema.put("type", "string");
/* 317 */       return o.set("items", (JsonNode)itemSchema);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 324 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.STRING);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class IntArraySerializer
/*     */     extends ArraySerializerBase<int[]>
/*     */   {
/* 333 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(int.class);
/*     */     public IntArraySerializer() {
/* 335 */       super((Class)int[].class);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected IntArraySerializer(IntArraySerializer src, BeanProperty prop, Boolean unwrapSingle) {
/* 342 */       super(src, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
/* 347 */       return (JsonSerializer<?>)new IntArraySerializer(this, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
/* 356 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public JavaType getContentType() {
/* 361 */       return VALUE_TYPE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> getContentSerializer() {
/* 367 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, int[] value) {
/* 372 */       return (value.length == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasSingleElement(int[] value) {
/* 377 */       return (value.length == 1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void serialize(int[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 383 */       int len = value.length;
/* 384 */       if (len == 1 && _shouldUnwrapSingle(provider)) {
/* 385 */         serializeContents(value, g, provider);
/*     */         
/*     */         return;
/*     */       } 
/* 389 */       g.writeArray(value, 0, value.length);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeContents(int[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 396 */       for (int i = 0, len = value.length; i < len; i++) {
/* 397 */         g.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 403 */       return createSchemaNode("array", true).set("items", (JsonNode)createSchemaNode("integer"));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 409 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.INTEGER);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class LongArraySerializer
/*     */     extends TypedPrimitiveArraySerializer<long[]>
/*     */   {
/* 418 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(long.class);
/*     */     public LongArraySerializer() {
/* 420 */       super((Class)long[].class);
/*     */     }
/*     */     public LongArraySerializer(LongArraySerializer src, BeanProperty prop, Boolean unwrapSingle) {
/* 423 */       super(src, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
/* 428 */       return (JsonSerializer<?>)new LongArraySerializer(this, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */     
/*     */     public JavaType getContentType() {
/* 433 */       return VALUE_TYPE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> getContentSerializer() {
/* 439 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, long[] value) {
/* 444 */       return (value.length == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasSingleElement(long[] value) {
/* 449 */       return (value.length == 1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void serialize(long[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 455 */       int len = value.length;
/* 456 */       if (len == 1 && _shouldUnwrapSingle(provider)) {
/* 457 */         serializeContents(value, g, provider);
/*     */         
/*     */         return;
/*     */       } 
/* 461 */       g.writeArray(value, 0, value.length);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeContents(long[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 468 */       for (int i = 0, len = value.length; i < len; i++) {
/* 469 */         g.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 476 */       return createSchemaNode("array", true)
/* 477 */         .set("items", (JsonNode)createSchemaNode("number", true));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 484 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.NUMBER);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class FloatArraySerializer
/*     */     extends TypedPrimitiveArraySerializer<float[]>
/*     */   {
/* 493 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(float.class);
/*     */     
/*     */     public FloatArraySerializer() {
/* 496 */       super((Class)float[].class);
/*     */     }
/*     */     
/*     */     public FloatArraySerializer(FloatArraySerializer src, BeanProperty prop, Boolean unwrapSingle) {
/* 500 */       super(src, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
/* 505 */       return (JsonSerializer<?>)new FloatArraySerializer(this, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */     
/*     */     public JavaType getContentType() {
/* 510 */       return VALUE_TYPE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> getContentSerializer() {
/* 516 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, float[] value) {
/* 521 */       return (value.length == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasSingleElement(float[] value) {
/* 526 */       return (value.length == 1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void serialize(float[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 532 */       int len = value.length;
/* 533 */       if (len == 1 && _shouldUnwrapSingle(provider)) {
/* 534 */         serializeContents(value, g, provider);
/*     */         return;
/*     */       } 
/* 537 */       g.writeStartArray(value, len);
/* 538 */       serializeContents(value, g, provider);
/* 539 */       g.writeEndArray();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeContents(float[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 546 */       for (int i = 0, len = value.length; i < len; i++) {
/* 547 */         g.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 553 */       return createSchemaNode("array", true).set("items", (JsonNode)createSchemaNode("number"));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 559 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.NUMBER);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static class DoubleArraySerializer
/*     */     extends ArraySerializerBase<double[]>
/*     */   {
/* 568 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(double.class);
/*     */     public DoubleArraySerializer() {
/* 570 */       super((Class)double[].class);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected DoubleArraySerializer(DoubleArraySerializer src, BeanProperty prop, Boolean unwrapSingle) {
/* 577 */       super(src, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle) {
/* 582 */       return (JsonSerializer<?>)new DoubleArraySerializer(this, prop, unwrapSingle);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts) {
/* 591 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public JavaType getContentType() {
/* 596 */       return VALUE_TYPE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonSerializer<?> getContentSerializer() {
/* 602 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, double[] value) {
/* 607 */       return (value.length == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasSingleElement(double[] value) {
/* 612 */       return (value.length == 1);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public final void serialize(double[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 618 */       int len = value.length;
/* 619 */       if (len == 1 && _shouldUnwrapSingle(provider)) {
/* 620 */         serializeContents(value, g, provider);
/*     */         
/*     */         return;
/*     */       } 
/* 624 */       g.writeArray(value, 0, value.length);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void serializeContents(double[] value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 630 */       for (int i = 0, len = value.length; i < len; i++) {
/* 631 */         g.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 637 */       return createSchemaNode("array", true).set("items", (JsonNode)createSchemaNode("number"));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 644 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.NUMBER);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\ser\std\StdArraySerializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */