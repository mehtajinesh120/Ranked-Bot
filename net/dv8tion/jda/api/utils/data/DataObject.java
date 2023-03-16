/*     */ package net.dv8tion.jda.api.utils.data;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.util.DefaultIndenter;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.Module;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.module.SimpleModule;
/*     */ import com.fasterxml.jackson.databind.type.MapType;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.UnaryOperator;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.exceptions.ParsingException;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.data.etf.ExTermDecoder;
/*     */ import net.dv8tion.jda.api.utils.data.etf.ExTermEncoder;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
/*     */ import org.jetbrains.annotations.Contract;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataObject
/*     */   implements SerializableData
/*     */ {
/*  53 */   private static final Logger log = LoggerFactory.getLogger(DataObject.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   private static final ObjectMapper mapper = new ObjectMapper();
/*  61 */   private static final SimpleModule module = new SimpleModule(); static {
/*  62 */     module.addAbstractTypeMapping(Map.class, HashMap.class);
/*  63 */     module.addAbstractTypeMapping(List.class, ArrayList.class);
/*  64 */     mapper.registerModule((Module)module);
/*  65 */   } private static final MapType mapType = mapper.getTypeFactory().constructRawMapType(HashMap.class);
/*     */ 
/*     */   
/*     */   protected final Map<String, Object> data;
/*     */ 
/*     */   
/*     */   protected DataObject(@Nonnull Map<String, Object> data) {
/*  72 */     this.data = data;
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
/*     */   @Nonnull
/*     */   public static DataObject empty() {
/*  85 */     return new DataObject(new HashMap<>());
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
/*     */   @Nonnull
/*     */   public static DataObject fromJson(@Nonnull byte[] data) {
/*     */     try {
/* 104 */       Map<String, Object> map = (Map<String, Object>)mapper.readValue(data, (JavaType)mapType);
/* 105 */       return new DataObject(map);
/*     */     }
/* 107 */     catch (IOException ex) {
/*     */       
/* 109 */       throw new ParsingException(ex);
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
/*     */   @Nonnull
/*     */   public static DataObject fromJson(@Nonnull String json) {
/*     */     try {
/* 129 */       Map<String, Object> map = (Map<String, Object>)mapper.readValue(json, (JavaType)mapType);
/* 130 */       return new DataObject(map);
/*     */     }
/* 132 */     catch (IOException ex) {
/*     */       
/* 134 */       throw new ParsingException(ex);
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
/*     */   @Nonnull
/*     */   public static DataObject fromJson(@Nonnull InputStream stream) {
/*     */     try {
/* 154 */       Map<String, Object> map = (Map<String, Object>)mapper.readValue(stream, (JavaType)mapType);
/* 155 */       return new DataObject(map);
/*     */     }
/* 157 */     catch (IOException ex) {
/*     */       
/* 159 */       throw new ParsingException(ex);
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
/*     */   @Nonnull
/*     */   public static DataObject fromJson(@Nonnull Reader stream) {
/*     */     try {
/* 179 */       Map<String, Object> map = (Map<String, Object>)mapper.readValue(stream, (JavaType)mapType);
/* 180 */       return new DataObject(map);
/*     */     }
/* 182 */     catch (IOException ex) {
/*     */       
/* 184 */       throw new ParsingException(ex);
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
/*     */   @Nonnull
/*     */   public static DataObject fromETF(@Nonnull byte[] data) {
/* 207 */     Checks.notNull(data, "Data");
/*     */     
/*     */     try {
/* 210 */       Map<String, Object> map = ExTermDecoder.unpackMap(ByteBuffer.wrap(data));
/* 211 */       return new DataObject(map);
/*     */     }
/* 213 */     catch (Exception ex) {
/*     */       
/* 215 */       log.error("Failed to parse ETF data {}", Arrays.toString(data), ex);
/* 216 */       throw new ParsingException(ex);
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
/*     */   public boolean hasKey(@Nonnull String key) {
/* 230 */     return this.data.containsKey(key);
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
/*     */   public boolean isNull(@Nonnull String key) {
/* 243 */     return (this.data.get(key) == null);
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
/*     */   public boolean isType(@Nonnull String key, @Nonnull DataType type) {
/* 260 */     return type.isType(this.data.get(key));
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
/*     */   @Nonnull
/*     */   public DataObject getObject(@Nonnull String key) {
/* 277 */     return optObject(key).<Throwable>orElseThrow(() -> valueError(key, "DataObject"));
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
/*     */   @Nonnull
/*     */   public Optional<DataObject> optObject(@Nonnull String key) {
/* 295 */     Map<String, Object> child = null;
/*     */     
/*     */     try {
/* 298 */       child = get((Class)Map.class, key);
/*     */     }
/* 300 */     catch (ClassCastException ex) {
/*     */       
/* 302 */       log.error("Unable to extract child data", ex);
/*     */     } 
/* 304 */     return (child == null) ? Optional.<DataObject>empty() : Optional.<DataObject>of(new DataObject(child));
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
/*     */   @Nonnull
/*     */   public DataArray getArray(@Nonnull String key) {
/* 321 */     return optArray(key).<Throwable>orElseThrow(() -> valueError(key, "DataArray"));
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
/*     */   @Nonnull
/*     */   public Optional<DataArray> optArray(@Nonnull String key) {
/* 339 */     List<Object> child = null;
/*     */     
/*     */     try {
/* 342 */       child = get((Class)List.class, key);
/*     */     }
/* 344 */     catch (ClassCastException ex) {
/*     */       
/* 346 */       log.error("Unable to extract child data", ex);
/*     */     } 
/* 348 */     return (child == null) ? Optional.<DataArray>empty() : Optional.<DataArray>of(new DataArray(child));
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
/*     */   @Nonnull
/*     */   public Optional<Object> opt(@Nonnull String key) {
/* 362 */     return Optional.ofNullable(this.data.get(key));
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
/*     */   @Nonnull
/*     */   public Object get(@Nonnull String key) {
/* 381 */     Object value = this.data.get(key);
/* 382 */     if (value == null)
/* 383 */       throw valueError(key, "any"); 
/* 384 */     return value;
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
/*     */   @Nonnull
/*     */   public String getString(@Nonnull String key) {
/* 401 */     String value = getString(key, null);
/* 402 */     if (value == null)
/* 403 */       throw valueError(key, "String"); 
/* 404 */     return value;
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
/*     */   @Contract("_, !null -> !null")
/*     */   public String getString(@Nonnull String key, @Nullable String defaultValue) {
/* 420 */     String value = get(String.class, key, UnaryOperator.identity(), String::valueOf);
/* 421 */     return (value == null) ? defaultValue : value;
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
/*     */   public boolean getBoolean(@Nonnull String key) {
/* 437 */     return getBoolean(key, false);
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
/*     */   public boolean getBoolean(@Nonnull String key, boolean defaultValue) {
/* 455 */     Boolean value = get(Boolean.class, key, Boolean::parseBoolean, null);
/* 456 */     return (value == null) ? defaultValue : value.booleanValue();
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
/*     */   public long getLong(@Nonnull String key) {
/* 472 */     Long value = get(Long.class, key, MiscUtil::parseLong, Number::longValue);
/* 473 */     if (value == null)
/* 474 */       throw valueError(key, "long"); 
/* 475 */     return value.longValue();
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
/*     */   public long getLong(@Nonnull String key, long defaultValue) {
/* 493 */     Long value = get(Long.class, key, Long::parseLong, Number::longValue);
/* 494 */     return (value == null) ? defaultValue : value.longValue();
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
/*     */   public long getUnsignedLong(@Nonnull String key) {
/* 510 */     Long value = get(Long.class, key, Long::parseUnsignedLong, Number::longValue);
/* 511 */     if (value == null)
/* 512 */       throw valueError(key, "unsigned long"); 
/* 513 */     return value.longValue();
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
/*     */   public long getUnsignedLong(@Nonnull String key, long defaultValue) {
/* 531 */     Long value = get(Long.class, key, Long::parseUnsignedLong, Number::longValue);
/* 532 */     return (value == null) ? defaultValue : value.longValue();
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
/*     */   public int getInt(@Nonnull String key) {
/* 548 */     Integer value = get(Integer.class, key, Integer::parseInt, Number::intValue);
/* 549 */     if (value == null)
/* 550 */       throw valueError(key, "int"); 
/* 551 */     return value.intValue();
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
/*     */   public int getInt(@Nonnull String key, int defaultValue) {
/* 569 */     Integer value = get(Integer.class, key, Integer::parseInt, Number::intValue);
/* 570 */     return (value == null) ? defaultValue : value.intValue();
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
/*     */   public int getUnsignedInt(@Nonnull String key) {
/* 586 */     Integer value = get(Integer.class, key, Integer::parseUnsignedInt, Number::intValue);
/* 587 */     if (value == null)
/* 588 */       throw valueError(key, "unsigned int"); 
/* 589 */     return value.intValue();
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
/*     */   public int getUnsignedInt(@Nonnull String key, int defaultValue) {
/* 607 */     Integer value = get(Integer.class, key, Integer::parseUnsignedInt, Number::intValue);
/* 608 */     return (value == null) ? defaultValue : value.intValue();
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
/*     */   public double getDouble(@Nonnull String key) {
/* 624 */     Double value = get(Double.class, key, Double::parseDouble, Number::doubleValue);
/* 625 */     if (value == null)
/* 626 */       throw valueError(key, "double"); 
/* 627 */     return value.doubleValue();
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
/*     */   public double getDouble(@Nonnull String key, double defaultValue) {
/* 645 */     Double value = get(Double.class, key, Double::parseDouble, Number::doubleValue);
/* 646 */     return (value == null) ? defaultValue : value.doubleValue();
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
/*     */   @Nonnull
/*     */   public DataObject remove(@Nonnull String key) {
/* 661 */     this.data.remove(key);
/* 662 */     return this;
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
/*     */   @Nonnull
/*     */   public DataObject putNull(@Nonnull String key) {
/* 676 */     this.data.put(key, null);
/* 677 */     return this;
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
/*     */   @Nonnull
/*     */   public DataObject put(@Nonnull String key, @Nullable Object value) {
/* 693 */     if (value instanceof SerializableData) {
/* 694 */       this.data.put(key, (((SerializableData)value).toData()).data);
/* 695 */     } else if (value instanceof SerializableArray) {
/* 696 */       this.data.put(key, (((SerializableArray)value).toDataArray()).data);
/*     */     } else {
/* 698 */       this.data.put(key, value);
/* 699 */     }  return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Collection<Object> values() {
/* 710 */     return this.data.values();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Set<String> keys() {
/* 721 */     return this.data.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public byte[] toJson() {
/*     */     try {
/* 734 */       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
/* 735 */       mapper.writeValue(outputStream, this.data);
/* 736 */       return outputStream.toByteArray();
/*     */     }
/* 738 */     catch (IOException e) {
/*     */       
/* 740 */       throw new UncheckedIOException(e);
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
/*     */   @Nonnull
/*     */   public byte[] toETF() {
/* 754 */     ByteBuffer buffer = ExTermEncoder.pack(this.data);
/* 755 */     return Arrays.copyOfRange(buffer.array(), buffer.arrayOffset(), buffer.arrayOffset() + buffer.limit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     try {
/* 763 */       return mapper.writeValueAsString(this.data);
/*     */     }
/* 765 */     catch (JsonProcessingException e) {
/*     */       
/* 767 */       throw new ParsingException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String toPrettyString() {
/* 774 */     DefaultIndenter defaultIndenter = new DefaultIndenter("    ", DefaultIndenter.SYS_LF);
/* 775 */     DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
/* 776 */     printer.withObjectIndenter((DefaultPrettyPrinter.Indenter)defaultIndenter).withArrayIndenter((DefaultPrettyPrinter.Indenter)defaultIndenter);
/*     */     
/*     */     try {
/* 779 */       return mapper.writer((PrettyPrinter)printer).writeValueAsString(this.data);
/*     */     }
/* 781 */     catch (JsonProcessingException e) {
/*     */       
/* 783 */       throw new ParsingException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Map<String, Object> toMap() {
/* 795 */     return this.data;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/* 802 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private ParsingException valueError(String key, String expectedType) {
/* 807 */     return new ParsingException("Unable to resolve value with key " + key + " to type " + expectedType + ": " + this.data.get(key));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private <T> T get(@Nonnull Class<T> type, @Nonnull String key) {
/* 813 */     return get(type, key, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private <T> T get(@Nonnull Class<T> type, @Nonnull String key, @Nullable Function<String, T> stringParse, @Nullable Function<Number, T> numberParse) {
/* 819 */     Object value = this.data.get(key);
/* 820 */     if (value == null)
/* 821 */       return null; 
/* 822 */     if (type.isInstance(value))
/* 823 */       return type.cast(value); 
/* 824 */     if (type == String.class) {
/* 825 */       return type.cast(value.toString());
/*     */     }
/* 827 */     if (value instanceof Number && numberParse != null)
/* 828 */       return numberParse.apply((Number)value); 
/* 829 */     if (value instanceof String && stringParse != null) {
/* 830 */       return stringParse.apply((String)value);
/*     */     }
/* 832 */     throw new ParsingException(Helpers.format("Cannot parse value for %s into type %s: %s instance of %s", new Object[] { key, type
/* 833 */             .getSimpleName(), value, value.getClass().getSimpleName() }));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\data\DataObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */