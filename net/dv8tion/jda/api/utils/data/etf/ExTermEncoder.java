/*     */ package net.dv8tion.jda.api.utils.data.etf;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.SerializableData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExTermEncoder
/*     */ {
/*     */   public static ByteBuffer pack(Object data) {
/*  66 */     ByteBuffer buffer = ByteBuffer.allocate(1024);
/*  67 */     buffer.put((byte)-125);
/*     */     
/*  69 */     ByteBuffer packed = pack(buffer, data);
/*     */     
/*  71 */     packed.flip();
/*  72 */     return packed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static ByteBuffer pack(ByteBuffer buffer, Object value) {
/*  78 */     if (value instanceof String)
/*  79 */       return packBinary(buffer, (String)value); 
/*  80 */     if (value instanceof Map)
/*  81 */       return packMap(buffer, (Map<String, Object>)value); 
/*  82 */     if (value instanceof SerializableData)
/*  83 */       return packMap(buffer, ((SerializableData)value).toData().toMap()); 
/*  84 */     if (value instanceof Collection)
/*  85 */       return packList(buffer, (Collection<Object>)value); 
/*  86 */     if (value instanceof DataArray)
/*  87 */       return packList(buffer, ((DataArray)value).toList()); 
/*  88 */     if (value instanceof Byte)
/*  89 */       return packSmallInt(buffer, ((Byte)value).byteValue()); 
/*  90 */     if (value instanceof Integer || value instanceof Short)
/*  91 */       return packInt(buffer, ((Integer)value).intValue()); 
/*  92 */     if (value instanceof Long)
/*  93 */       return packLong(buffer, ((Long)value).longValue()); 
/*  94 */     if (value instanceof Float || value instanceof Double)
/*  95 */       return packFloat(buffer, ((Double)value).doubleValue()); 
/*  96 */     if (value instanceof Boolean)
/*  97 */       return packAtom(buffer, String.valueOf(value)); 
/*  98 */     if (value == null) {
/*  99 */       return packAtom(buffer, "nil");
/*     */     }
/* 101 */     if (value instanceof long[])
/* 102 */       return packArray(buffer, (long[])value); 
/* 103 */     if (value instanceof int[])
/* 104 */       return packArray(buffer, (int[])value); 
/* 105 */     if (value instanceof short[])
/* 106 */       return packArray(buffer, (short[])value); 
/* 107 */     if (value instanceof byte[]) {
/* 108 */       return packArray(buffer, (byte[])value);
/*     */     }
/* 110 */     if (value instanceof Object[]) {
/* 111 */       return packList(buffer, Arrays.asList((Object[])value));
/*     */     }
/* 113 */     throw new UnsupportedOperationException("Cannot pack value of type " + value.getClass().getName());
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer realloc(ByteBuffer buffer, int length) {
/* 118 */     if (buffer.remaining() >= length) {
/* 119 */       return buffer;
/*     */     }
/* 121 */     ByteBuffer allocated = ByteBuffer.allocate(buffer.position() + length << 1);
/*     */     
/* 123 */     buffer.flip();
/* 124 */     allocated.put(buffer);
/* 125 */     return allocated;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packMap(ByteBuffer buffer, Map<String, Object> data) {
/* 130 */     buffer = realloc(buffer, data.size() + 5);
/* 131 */     buffer.put((byte)116);
/* 132 */     buffer.putInt(data.size());
/*     */     
/* 134 */     for (Map.Entry<String, Object> entry : data.entrySet()) {
/*     */       
/* 136 */       buffer = packBinary(buffer, entry.getKey());
/* 137 */       buffer = pack(buffer, entry.getValue());
/*     */     } 
/*     */     
/* 140 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packList(ByteBuffer buffer, Collection<Object> data) {
/* 145 */     if (data.isEmpty())
/*     */     {
/*     */       
/* 148 */       return packNil(buffer);
/*     */     }
/*     */     
/* 151 */     buffer = realloc(buffer, data.size() + 6);
/* 152 */     buffer.put((byte)108);
/* 153 */     buffer.putInt(data.size());
/* 154 */     for (Object element : data)
/* 155 */       buffer = pack(buffer, element); 
/* 156 */     return packNil(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packBinary(ByteBuffer buffer, String value) {
/* 161 */     byte[] encoded = value.getBytes(StandardCharsets.UTF_8);
/* 162 */     buffer = realloc(buffer, encoded.length * 4 + 5);
/* 163 */     buffer.put((byte)109);
/* 164 */     buffer.putInt(encoded.length);
/* 165 */     buffer.put(encoded);
/* 166 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packSmallInt(ByteBuffer buffer, byte value) {
/* 171 */     buffer = realloc(buffer, 2);
/* 172 */     buffer.put((byte)97);
/* 173 */     buffer.put(value);
/* 174 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packInt(ByteBuffer buffer, int value) {
/* 179 */     if (countBytes(value) <= 1 && value >= 0)
/* 180 */       return packSmallInt(buffer, (byte)value); 
/* 181 */     buffer = realloc(buffer, 5);
/* 182 */     buffer.put((byte)98);
/* 183 */     buffer.putInt(value);
/* 184 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packLong(ByteBuffer buffer, long value) {
/* 189 */     byte bytes = countBytes(value);
/* 190 */     if (bytes <= 1)
/* 191 */       return packSmallInt(buffer, (byte)(int)value); 
/* 192 */     if (bytes <= 4 && value >= 0L) {
/*     */ 
/*     */       
/* 195 */       buffer = realloc(buffer, 5);
/* 196 */       buffer.put((byte)98);
/* 197 */       buffer.putInt((int)value);
/* 198 */       return buffer;
/*     */     } 
/*     */     
/* 201 */     buffer = realloc(buffer, 3 + bytes);
/* 202 */     buffer.put((byte)110);
/* 203 */     buffer.put(bytes);
/*     */     
/* 205 */     buffer.put((byte)0);
/* 206 */     while (value > 0L) {
/*     */       
/* 208 */       buffer.put((byte)(int)value);
/* 209 */       value >>>= 8L;
/*     */     } 
/*     */     
/* 212 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packFloat(ByteBuffer buffer, double value) {
/* 217 */     buffer = realloc(buffer, 9);
/* 218 */     buffer.put((byte)70);
/* 219 */     buffer.putDouble(value);
/* 220 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packAtom(ByteBuffer buffer, String value) {
/* 225 */     byte[] array = value.getBytes(StandardCharsets.ISO_8859_1);
/* 226 */     buffer = realloc(buffer, array.length + 3);
/* 227 */     buffer.put((byte)100);
/* 228 */     buffer.putShort((short)array.length);
/* 229 */     buffer.put(array);
/* 230 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packArray(ByteBuffer buffer, long[] array) {
/* 235 */     if (array.length == 0) {
/* 236 */       return packNil(buffer);
/*     */     }
/* 238 */     buffer = realloc(buffer, array.length * 8 + 6);
/* 239 */     buffer.put((byte)108);
/* 240 */     buffer.putInt(array.length);
/* 241 */     for (long it : array)
/* 242 */       buffer = packLong(buffer, it); 
/* 243 */     return packNil(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packArray(ByteBuffer buffer, int[] array) {
/* 248 */     if (array.length == 0) {
/* 249 */       return packNil(buffer);
/*     */     }
/* 251 */     buffer = realloc(buffer, array.length * 4 + 6);
/* 252 */     buffer.put((byte)108);
/* 253 */     buffer.putInt(array.length);
/* 254 */     for (int it : array)
/* 255 */       buffer = packInt(buffer, it); 
/* 256 */     return packNil(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packArray(ByteBuffer buffer, short[] array) {
/* 261 */     if (array.length == 0) {
/* 262 */       return packNil(buffer);
/*     */     }
/* 264 */     buffer = realloc(buffer, array.length * 2 + 6);
/* 265 */     buffer.put((byte)108);
/* 266 */     buffer.putInt(array.length);
/* 267 */     for (short it : array)
/* 268 */       buffer = packInt(buffer, it); 
/* 269 */     return packNil(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packArray(ByteBuffer buffer, byte[] array) {
/* 274 */     if (array.length == 0) {
/* 275 */       return packNil(buffer);
/*     */     }
/* 277 */     buffer = realloc(buffer, array.length + 6);
/* 278 */     buffer.put((byte)108);
/* 279 */     buffer.putInt(array.length);
/* 280 */     for (byte it : array)
/* 281 */       buffer = packSmallInt(buffer, it); 
/* 282 */     return packNil(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   private static ByteBuffer packNil(ByteBuffer buffer) {
/* 287 */     buffer = realloc(buffer, 1);
/* 288 */     buffer.put((byte)106);
/* 289 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte countBytes(long value) {
/* 294 */     int leadingZeros = Long.numberOfLeadingZeros(value);
/* 295 */     return (byte)(int)Math.ceil((64 - leadingZeros) / 8.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\data\etf\ExTermEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */