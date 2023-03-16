/*     */ package net.dv8tion.jda.api.utils.data.etf;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.zip.InflaterOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExTermDecoder
/*     */ {
/*     */   public static Object unpack(ByteBuffer buffer) {
/*  65 */     if (buffer.get() != -125) {
/*  66 */       throw new IllegalArgumentException("Failed header check");
/*     */     }
/*  68 */     return unpack0(buffer);
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
/*     */ 
/*     */   
/*     */   public static Map<String, Object> unpackMap(ByteBuffer buffer) {
/*  96 */     byte tag = buffer.get(1);
/*  97 */     if (tag != 116)
/*  98 */       throw new IllegalArgumentException("Cannot unpack map from tag " + tag); 
/*  99 */     return (Map<String, Object>)unpack(buffer);
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
/*     */ 
/*     */   
/*     */   public static List<Object> unpackList(ByteBuffer buffer) {
/* 127 */     byte tag = buffer.get(1);
/* 128 */     if (tag != 108) {
/* 129 */       throw new IllegalArgumentException("Cannot unpack list from tag " + tag);
/*     */     }
/* 131 */     return (List<Object>)unpack(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object unpack0(ByteBuffer buffer) {
/* 136 */     int tag = buffer.get();
/* 137 */     switch (tag) { case 80:
/* 138 */         return unpackCompressed(buffer);
/* 139 */       case 97: return Integer.valueOf(unpackSmallInt(buffer));
/* 140 */       case 110: return Long.valueOf(unpackSmallBigint(buffer));
/* 141 */       case 98: return Integer.valueOf(unpackInt(buffer));
/*     */       case 99:
/* 143 */         return Double.valueOf(unpackOldFloat(buffer));
/* 144 */       case 70: return Double.valueOf(unpackFloat(buffer));
/*     */       case 119:
/* 146 */         return unpackSmallAtom(buffer, StandardCharsets.UTF_8);
/* 147 */       case 115: return unpackSmallAtom(buffer, StandardCharsets.ISO_8859_1);
/* 148 */       case 118: return unpackAtom(buffer, StandardCharsets.UTF_8);
/* 149 */       case 100: return unpackAtom(buffer, StandardCharsets.ISO_8859_1);
/*     */       case 116:
/* 151 */         return unpackMap0(buffer);
/* 152 */       case 108: return unpackList0(buffer);
/* 153 */       case 106: return Collections.emptyList();
/*     */       case 107:
/* 155 */         return unpackString(buffer);
/* 156 */       case 109: return unpackBinary(buffer); }
/*     */     
/* 158 */     throw new IllegalArgumentException("Unknown tag " + tag);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object unpackCompressed(ByteBuffer buffer) {
/* 164 */     int size = buffer.getInt();
/* 165 */     ByteArrayOutputStream decompressed = new ByteArrayOutputStream(size); try {
/* 166 */       InflaterOutputStream inflater = new InflaterOutputStream(decompressed);
/*     */       
/* 168 */       try { inflater.write(buffer.array(), buffer.position(), buffer.remaining());
/* 169 */         inflater.close(); } catch (Throwable throwable) { try { inflater.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; } 
/* 170 */     } catch (IOException e) {
/*     */       
/* 172 */       throw new UncheckedIOException(e);
/*     */     } 
/*     */     
/* 175 */     buffer = ByteBuffer.wrap(decompressed.toByteArray());
/* 176 */     return unpack0(buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   private static double unpackOldFloat(ByteBuffer buffer) {
/* 181 */     String bytes = getString(buffer, StandardCharsets.ISO_8859_1, 31);
/* 182 */     return Double.parseDouble(bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   private static double unpackFloat(ByteBuffer buffer) {
/* 187 */     return buffer.getDouble();
/*     */   }
/*     */ 
/*     */   
/*     */   private static long unpackSmallBigint(ByteBuffer buffer) {
/* 192 */     int arity = Byte.toUnsignedInt(buffer.get());
/* 193 */     int sign = Byte.toUnsignedInt(buffer.get());
/* 194 */     long sum = 0L;
/* 195 */     long offset = 0L;
/* 196 */     while (arity-- > 0) {
/*     */       
/* 198 */       sum += Byte.toUnsignedLong(buffer.get()) << (int)offset;
/* 199 */       offset += 8L;
/*     */     } 
/*     */     
/* 202 */     return (sign == 0) ? sum : -sum;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int unpackSmallInt(ByteBuffer buffer) {
/* 207 */     return Byte.toUnsignedInt(buffer.get());
/*     */   }
/*     */ 
/*     */   
/*     */   private static int unpackInt(ByteBuffer buffer) {
/* 212 */     return buffer.getInt();
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Object> unpackString(ByteBuffer buffer) {
/* 217 */     int length = Short.toUnsignedInt(buffer.getShort());
/* 218 */     List<Object> bytes = new ArrayList(length);
/* 219 */     while (length-- > 0)
/* 220 */       bytes.add(Byte.valueOf(buffer.get())); 
/* 221 */     return bytes;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String unpackBinary(ByteBuffer buffer) {
/* 226 */     int length = buffer.getInt();
/* 227 */     return getString(buffer, StandardCharsets.UTF_8, length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object unpackSmallAtom(ByteBuffer buffer, Charset charset) {
/* 232 */     int length = Byte.toUnsignedInt(buffer.get());
/* 233 */     return unpackAtom(buffer, charset, length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object unpackAtom(ByteBuffer buffer, Charset charset) {
/* 238 */     int length = Short.toUnsignedInt(buffer.getShort());
/* 239 */     return unpackAtom(buffer, charset, length);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Object unpackAtom(ByteBuffer buffer, Charset charset, int length) {
/* 244 */     String value = getString(buffer, charset, length);
/* 245 */     switch (value) {
/*     */       case "true":
/* 247 */         return Boolean.valueOf(true);
/* 248 */       case "false": return Boolean.valueOf(false);
/* 249 */       case "nil": return null;
/* 250 */     }  return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getString(ByteBuffer buffer, Charset charset, int length) {
/* 256 */     byte[] array = new byte[length];
/* 257 */     buffer.get(array);
/* 258 */     return new String(array, charset);
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Object> unpackList0(ByteBuffer buffer) {
/* 263 */     int length = buffer.getInt();
/* 264 */     List<Object> list = new ArrayList(length);
/* 265 */     while (length-- > 0)
/*     */     {
/* 267 */       list.add(unpack0(buffer));
/*     */     }
/* 269 */     Object tail = unpack0(buffer);
/* 270 */     if (tail != Collections.emptyList())
/* 271 */       throw new IllegalArgumentException("Unexpected tail " + tail); 
/* 272 */     return list;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map<String, Object> unpackMap0(ByteBuffer buffer) {
/* 277 */     Map<String, Object> map = new HashMap<>();
/* 278 */     int arity = buffer.getInt();
/* 279 */     while (arity-- > 0) {
/*     */       
/* 281 */       String key = (String)unpack0(buffer);
/* 282 */       Object value = unpack0(buffer);
/* 283 */       map.put(key, value);
/*     */     } 
/* 285 */     return map;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\data\etf\ExTermDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */