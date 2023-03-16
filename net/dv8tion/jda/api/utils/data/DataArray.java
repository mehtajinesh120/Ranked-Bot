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
/*     */ import com.fasterxml.jackson.databind.type.CollectionType;
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
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.UnaryOperator;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.exceptions.ParsingException;
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
/*     */ 
/*     */ public class DataArray
/*     */   implements Iterable<Object>, SerializableArray
/*     */ {
/*  55 */   private static final Logger log = LoggerFactory.getLogger(DataObject.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   private static final ObjectMapper mapper = new ObjectMapper();
/*  63 */   private static final SimpleModule module = new SimpleModule(); static {
/*  64 */     module.addAbstractTypeMapping(Map.class, HashMap.class);
/*  65 */     module.addAbstractTypeMapping(List.class, ArrayList.class);
/*  66 */     mapper.registerModule((Module)module);
/*  67 */   } private static final CollectionType listType = mapper.getTypeFactory().constructRawCollectionType(ArrayList.class);
/*     */ 
/*     */   
/*     */   protected final List<Object> data;
/*     */ 
/*     */   
/*     */   protected DataArray(List<Object> data) {
/*  74 */     this.data = data;
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
/*     */   public static DataArray empty() {
/*  87 */     return new DataArray(new ArrayList());
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
/*     */   public static DataArray fromCollection(@Nonnull Collection<?> col) {
/* 102 */     return empty().addAll(col);
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
/*     */   public static DataArray fromJson(@Nonnull String json) {
/*     */     try {
/* 121 */       return new DataArray((List<Object>)mapper.readValue(json, (JavaType)listType));
/*     */     }
/* 123 */     catch (IOException e) {
/*     */       
/* 125 */       throw new ParsingException(e);
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
/*     */   public static DataArray fromJson(@Nonnull InputStream json) {
/*     */     try {
/* 145 */       return new DataArray((List<Object>)mapper.readValue(json, (JavaType)listType));
/*     */     }
/* 147 */     catch (IOException e) {
/*     */       
/* 149 */       throw new ParsingException(e);
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
/*     */   public static DataArray fromJson(@Nonnull Reader json) {
/*     */     try {
/* 169 */       return new DataArray((List<Object>)mapper.readValue(json, (JavaType)listType));
/*     */     }
/* 171 */     catch (IOException e) {
/*     */       
/* 173 */       throw new ParsingException(e);
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
/*     */   public static DataArray fromETF(@Nonnull byte[] data) {
/* 196 */     Checks.notNull(data, "Data");
/*     */     
/*     */     try {
/* 199 */       List<Object> list = ExTermDecoder.unpackList(ByteBuffer.wrap(data));
/* 200 */       return new DataArray(list);
/*     */     }
/* 202 */     catch (Exception ex) {
/*     */       
/* 204 */       log.error("Failed to parse ETF data {}", Arrays.toString(data), ex);
/* 205 */       throw new ParsingException(ex);
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
/*     */   public boolean isNull(int index) {
/* 219 */     return (this.data.get(index) == null);
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
/*     */   public boolean isType(int index, @Nonnull DataType type) {
/* 236 */     return type.isType(this.data.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int length() {
/* 246 */     return this.data.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 256 */     return this.data.isEmpty();
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
/*     */   public DataObject getObject(int index) {
/* 274 */     Map<String, Object> child = null;
/*     */     
/*     */     try {
/* 277 */       child = get((Class)Map.class, index);
/*     */     }
/* 279 */     catch (ClassCastException ex) {
/*     */       
/* 281 */       log.error("Unable to extract child data", ex);
/*     */     } 
/* 283 */     if (child == null)
/* 284 */       throw valueError(index, "DataObject"); 
/* 285 */     return new DataObject(child);
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
/*     */   public DataArray getArray(int index) {
/* 303 */     List<Object> child = null;
/*     */     
/*     */     try {
/* 306 */       child = get((Class)List.class, index);
/*     */     }
/* 308 */     catch (ClassCastException ex) {
/*     */       
/* 310 */       log.error("Unable to extract child data", ex);
/*     */     } 
/* 312 */     if (child == null)
/* 313 */       throw valueError(index, "DataArray"); 
/* 314 */     return new DataArray(child);
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
/*     */   public String getString(int index) {
/* 331 */     String value = get(String.class, index, UnaryOperator.identity(), String::valueOf);
/* 332 */     if (value == null)
/* 333 */       throw valueError(index, "String"); 
/* 334 */     return value;
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
/*     */   @Contract("_, !null -> !null")
/*     */   public String getString(int index, @Nullable String defaultValue) {
/* 353 */     String value = get(String.class, index, UnaryOperator.identity(), String::valueOf);
/* 354 */     return (value == null) ? defaultValue : value;
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
/*     */   public boolean getBoolean(int index) {
/* 370 */     return getBoolean(index, false);
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
/*     */   public boolean getBoolean(int index, boolean defaultValue) {
/* 388 */     Boolean value = get(Boolean.class, index, Boolean::parseBoolean, null);
/* 389 */     return (value == null) ? defaultValue : value.booleanValue();
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
/*     */   public int getInt(int index) {
/* 405 */     Integer value = get(Integer.class, index, Integer::parseInt, Number::intValue);
/* 406 */     if (value == null)
/* 407 */       throw valueError(index, "int"); 
/* 408 */     return value.intValue();
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
/*     */   public int getInt(int index, int defaultValue) {
/* 426 */     Integer value = get(Integer.class, index, Integer::parseInt, Number::intValue);
/* 427 */     return (value == null) ? defaultValue : value.intValue();
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
/*     */   public int getUnsignedInt(int index) {
/* 443 */     Integer value = get(Integer.class, index, Integer::parseUnsignedInt, Number::intValue);
/* 444 */     if (value == null)
/* 445 */       throw valueError(index, "unsigned int"); 
/* 446 */     return value.intValue();
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
/*     */   public int getUnsignedInt(int index, int defaultValue) {
/* 464 */     Integer value = get(Integer.class, index, Integer::parseUnsignedInt, Number::intValue);
/* 465 */     return (value == null) ? defaultValue : value.intValue();
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
/*     */   public long getLong(int index) {
/* 481 */     Long value = get(Long.class, index, Long::parseLong, Number::longValue);
/* 482 */     if (value == null)
/* 483 */       throw valueError(index, "long"); 
/* 484 */     return value.longValue();
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
/*     */   public long getLong(int index, long defaultValue) {
/* 502 */     Long value = get(Long.class, index, Long::parseLong, Number::longValue);
/* 503 */     return (value == null) ? defaultValue : value.longValue();
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
/*     */   public long getUnsignedLong(int index) {
/* 519 */     Long value = get(Long.class, index, Long::parseUnsignedLong, Number::longValue);
/* 520 */     if (value == null)
/* 521 */       throw valueError(index, "unsigned long"); 
/* 522 */     return value.longValue();
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
/*     */   public long getUnsignedLong(int index, long defaultValue) {
/* 540 */     Long value = get(Long.class, index, Long::parseUnsignedLong, Number::longValue);
/* 541 */     return (value == null) ? defaultValue : value.longValue();
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
/*     */   public DataArray add(@Nullable Object value) {
/* 555 */     if (value instanceof SerializableData) {
/* 556 */       this.data.add((((SerializableData)value).toData()).data);
/* 557 */     } else if (value instanceof SerializableArray) {
/* 558 */       this.data.add((((SerializableArray)value).toDataArray()).data);
/*     */     } else {
/* 560 */       this.data.add(value);
/* 561 */     }  return this;
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
/*     */   public DataArray addAll(@Nonnull Collection<?> values) {
/* 575 */     values.forEach(this::add);
/* 576 */     return this;
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
/*     */   public DataArray addAll(@Nonnull DataArray array) {
/* 590 */     return addAll(array.data);
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
/*     */   public DataArray insert(int index, @Nullable Object value) {
/* 606 */     if (value instanceof SerializableData) {
/* 607 */       this.data.add(index, (((SerializableData)value).toData()).data);
/* 608 */     } else if (value instanceof SerializableArray) {
/* 609 */       this.data.add(index, (((SerializableArray)value).toDataArray()).data);
/*     */     } else {
/* 611 */       this.data.add(index, value);
/* 612 */     }  return this;
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
/*     */   public DataArray remove(int index) {
/* 626 */     this.data.remove(index);
/* 627 */     return this;
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
/*     */   public DataArray remove(@Nullable Object value) {
/* 641 */     this.data.remove(value);
/* 642 */     return this;
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
/* 655 */       ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
/* 656 */       mapper.writeValue(outputStream, this.data);
/* 657 */       return outputStream.toByteArray();
/*     */     }
/* 659 */     catch (IOException e) {
/*     */       
/* 661 */       throw new UncheckedIOException(e);
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
/* 675 */     ByteBuffer buffer = ExTermEncoder.pack(this.data);
/* 676 */     return Arrays.copyOfRange(buffer.array(), buffer.arrayOffset(), buffer.arrayOffset() + buffer.limit());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*     */     try {
/* 684 */       return mapper.writeValueAsString(this.data);
/*     */     }
/* 686 */     catch (JsonProcessingException e) {
/*     */       
/* 688 */       throw new ParsingException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String toPrettyString() {
/* 695 */     DefaultIndenter defaultIndenter = new DefaultIndenter("    ", DefaultIndenter.SYS_LF);
/* 696 */     DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
/* 697 */     printer.withObjectIndenter((DefaultPrettyPrinter.Indenter)defaultIndenter).withArrayIndenter((DefaultPrettyPrinter.Indenter)defaultIndenter);
/*     */     
/*     */     try {
/* 700 */       return mapper.writer((PrettyPrinter)printer).writeValueAsString(this.data);
/*     */     }
/* 702 */     catch (JsonProcessingException e) {
/*     */       
/* 704 */       throw new ParsingException(e);
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
/*     */   public List<Object> toList() {
/* 716 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   private ParsingException valueError(int index, String expectedType) {
/* 721 */     return new ParsingException("Unable to resolve value at " + index + " to type " + expectedType + ": " + this.data.get(index));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private <T> T get(@Nonnull Class<T> type, int index) {
/* 727 */     return get(type, index, null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   private <T> T get(@Nonnull Class<T> type, int index, @Nullable Function<String, T> stringMapper, @Nullable Function<Number, T> numberMapper) {
/* 733 */     Object value = this.data.get(index);
/* 734 */     if (value == null)
/* 735 */       return null; 
/* 736 */     if (type.isInstance(value))
/* 737 */       return type.cast(value); 
/* 738 */     if (type == String.class) {
/* 739 */       return type.cast(value.toString());
/*     */     }
/* 741 */     if (stringMapper != null && value instanceof String)
/* 742 */       return stringMapper.apply((String)value); 
/* 743 */     if (numberMapper != null && value instanceof Number) {
/* 744 */       return numberMapper.apply((Number)value);
/*     */     }
/* 746 */     throw new ParsingException(Helpers.format("Cannot parse value for index %d into type %s: %s instance of %s", new Object[] {
/* 747 */             Integer.valueOf(index), type.getSimpleName(), value, value.getClass().getSimpleName()
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Iterator<Object> iterator() {
/* 754 */     return this.data.iterator();
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public <T> Stream<T> stream(BiFunction<? super DataArray, Integer, ? extends T> mapper) {
/* 760 */     return IntStream.range(0, length())
/* 761 */       .mapToObj(index -> mapper.apply(this, Integer.valueOf(index)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataArray toDataArray() {
/* 768 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\data\DataArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */