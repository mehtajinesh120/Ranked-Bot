/*     */ package okhttp3;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import javax.annotation.Nullable;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.http.HttpDate;
/*     */ import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Headers
/*     */ {
/*     */   private final String[] namesAndValues;
/*     */   
/*     */   Headers(Builder builder) {
/*  56 */     this.namesAndValues = builder.namesAndValues.<String>toArray(new String[builder.namesAndValues.size()]);
/*     */   }
/*     */   
/*     */   private Headers(String[] namesAndValues) {
/*  60 */     this.namesAndValues = namesAndValues;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public String get(String name) {
/*  65 */     return get(this.namesAndValues, name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Date getDate(String name) {
/*  73 */     String value = get(name);
/*  74 */     return (value != null) ? HttpDate.parse(value) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   @IgnoreJRERequirement
/*     */   public Instant getInstant(String name) {
/*  83 */     Date value = getDate(name);
/*  84 */     return (value != null) ? value.toInstant() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  89 */     return this.namesAndValues.length / 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String name(int index) {
/*  94 */     return this.namesAndValues[index * 2];
/*     */   }
/*     */ 
/*     */   
/*     */   public String value(int index) {
/*  99 */     return this.namesAndValues[index * 2 + 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> names() {
/* 104 */     TreeSet<String> result = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
/* 105 */     for (int i = 0, size = size(); i < size; i++) {
/* 106 */       result.add(name(i));
/*     */     }
/* 108 */     return Collections.unmodifiableSet(result);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> values(String name) {
/* 113 */     List<String> result = null;
/* 114 */     for (int i = 0, size = size(); i < size; i++) {
/* 115 */       if (name.equalsIgnoreCase(name(i))) {
/* 116 */         if (result == null) result = new ArrayList<>(2); 
/* 117 */         result.add(value(i));
/*     */       } 
/*     */     } 
/* 120 */     return (result != null) ? 
/* 121 */       Collections.<String>unmodifiableList(result) : 
/* 122 */       Collections.<String>emptyList();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long byteCount() {
/* 133 */     long result = (this.namesAndValues.length * 2);
/*     */     
/* 135 */     for (int i = 0, size = this.namesAndValues.length; i < size; i++) {
/* 136 */       result += this.namesAndValues[i].length();
/*     */     }
/*     */     
/* 139 */     return result;
/*     */   }
/*     */   
/*     */   public Builder newBuilder() {
/* 143 */     Builder result = new Builder();
/* 144 */     Collections.addAll(result.namesAndValues, this.namesAndValues);
/* 145 */     return result;
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
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/* 175 */     return (other instanceof Headers && 
/* 176 */       Arrays.equals((Object[])((Headers)other).namesAndValues, (Object[])this.namesAndValues));
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 180 */     return Arrays.hashCode((Object[])this.namesAndValues);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 184 */     StringBuilder result = new StringBuilder();
/* 185 */     for (int i = 0, size = size(); i < size; i++) {
/* 186 */       result.append(name(i)).append(": ").append(value(i)).append("\n");
/*     */     }
/* 188 */     return result.toString();
/*     */   }
/*     */   
/*     */   public Map<String, List<String>> toMultimap() {
/* 192 */     Map<String, List<String>> result = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
/* 193 */     for (int i = 0, size = size(); i < size; i++) {
/* 194 */       String name = name(i).toLowerCase(Locale.US);
/* 195 */       List<String> values = result.get(name);
/* 196 */       if (values == null) {
/* 197 */         values = new ArrayList<>(2);
/* 198 */         result.put(name, values);
/*     */       } 
/* 200 */       values.add(value(i));
/*     */     } 
/* 202 */     return result;
/*     */   }
/*     */   @Nullable
/*     */   private static String get(String[] namesAndValues, String name) {
/* 206 */     for (int i = namesAndValues.length - 2; i >= 0; i -= 2) {
/* 207 */       if (name.equalsIgnoreCase(namesAndValues[i])) {
/* 208 */         return namesAndValues[i + 1];
/*     */       }
/*     */     } 
/* 211 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Headers of(String... namesAndValues) {
/* 219 */     if (namesAndValues == null) throw new NullPointerException("namesAndValues == null"); 
/* 220 */     if (namesAndValues.length % 2 != 0) {
/* 221 */       throw new IllegalArgumentException("Expected alternating header names and values");
/*     */     }
/*     */ 
/*     */     
/* 225 */     namesAndValues = (String[])namesAndValues.clone(); int i;
/* 226 */     for (i = 0; i < namesAndValues.length; i++) {
/* 227 */       if (namesAndValues[i] == null) throw new IllegalArgumentException("Headers cannot be null"); 
/* 228 */       namesAndValues[i] = namesAndValues[i].trim();
/*     */     } 
/*     */ 
/*     */     
/* 232 */     for (i = 0; i < namesAndValues.length; i += 2) {
/* 233 */       String name = namesAndValues[i];
/* 234 */       String value = namesAndValues[i + 1];
/* 235 */       checkName(name);
/* 236 */       checkValue(value, name);
/*     */     } 
/*     */     
/* 239 */     return new Headers(namesAndValues);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Headers of(Map<String, String> headers) {
/* 246 */     if (headers == null) throw new NullPointerException("headers == null");
/*     */ 
/*     */     
/* 249 */     String[] namesAndValues = new String[headers.size() * 2];
/* 250 */     int i = 0;
/* 251 */     for (Map.Entry<String, String> header : headers.entrySet()) {
/* 252 */       if (header.getKey() == null || header.getValue() == null) {
/* 253 */         throw new IllegalArgumentException("Headers cannot be null");
/*     */       }
/* 255 */       String name = ((String)header.getKey()).trim();
/* 256 */       String value = ((String)header.getValue()).trim();
/* 257 */       checkName(name);
/* 258 */       checkValue(value, name);
/* 259 */       namesAndValues[i] = name;
/* 260 */       namesAndValues[i + 1] = value;
/* 261 */       i += 2;
/*     */     } 
/*     */     
/* 264 */     return new Headers(namesAndValues);
/*     */   }
/*     */   
/*     */   static void checkName(String name) {
/* 268 */     if (name == null) throw new NullPointerException("name == null"); 
/* 269 */     if (name.isEmpty()) throw new IllegalArgumentException("name is empty"); 
/* 270 */     for (int i = 0, length = name.length(); i < length; i++) {
/* 271 */       char c = name.charAt(i);
/* 272 */       if (c <= ' ' || c >= '')
/* 273 */         throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in header name: %s", new Object[] {
/* 274 */                 Integer.valueOf(c), Integer.valueOf(i), name
/*     */               })); 
/*     */     } 
/*     */   }
/*     */   
/*     */   static void checkValue(String value, String name) {
/* 280 */     if (value == null) throw new NullPointerException("value for name " + name + " == null"); 
/* 281 */     for (int i = 0, length = value.length(); i < length; i++) {
/* 282 */       char c = value.charAt(i);
/* 283 */       if ((c <= '\037' && c != '\t') || c >= '')
/* 284 */         throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in %s value: %s", new Object[] {
/* 285 */                 Integer.valueOf(c), Integer.valueOf(i), name, value
/*     */               })); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static final class Builder {
/* 291 */     final List<String> namesAndValues = new ArrayList<>(20);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder addLenient(String line) {
/* 298 */       int index = line.indexOf(":", 1);
/* 299 */       if (index != -1)
/* 300 */         return addLenient(line.substring(0, index), line.substring(index + 1)); 
/* 301 */       if (line.startsWith(":"))
/*     */       {
/*     */         
/* 304 */         return addLenient("", line.substring(1));
/*     */       }
/* 306 */       return addLenient("", line);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder add(String line) {
/* 312 */       int index = line.indexOf(":");
/* 313 */       if (index == -1) {
/* 314 */         throw new IllegalArgumentException("Unexpected header: " + line);
/*     */       }
/* 316 */       return add(line.substring(0, index).trim(), line.substring(index + 1));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder add(String name, String value) {
/* 323 */       Headers.checkName(name);
/* 324 */       Headers.checkValue(value, name);
/* 325 */       return addLenient(name, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addUnsafeNonAscii(String name, String value) {
/* 333 */       Headers.checkName(name);
/* 334 */       return addLenient(name, value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder addAll(Headers headers) {
/* 341 */       for (int i = 0, size = headers.size(); i < size; i++) {
/* 342 */         addLenient(headers.name(i), headers.value(i));
/*     */       }
/*     */       
/* 345 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder add(String name, Date value) {
/* 353 */       if (value == null) throw new NullPointerException("value for name " + name + " == null"); 
/* 354 */       add(name, HttpDate.format(value));
/* 355 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @IgnoreJRERequirement
/*     */     public Builder add(String name, Instant value) {
/* 364 */       if (value == null) throw new NullPointerException("value for name " + name + " == null"); 
/* 365 */       return add(name, new Date(value.toEpochMilli()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder set(String name, Date value) {
/* 373 */       if (value == null) throw new NullPointerException("value for name " + name + " == null"); 
/* 374 */       set(name, HttpDate.format(value));
/* 375 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @IgnoreJRERequirement
/*     */     public Builder set(String name, Instant value) {
/* 384 */       if (value == null) throw new NullPointerException("value for name " + name + " == null"); 
/* 385 */       return set(name, new Date(value.toEpochMilli()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Builder addLenient(String name, String value) {
/* 393 */       this.namesAndValues.add(name);
/* 394 */       this.namesAndValues.add(value.trim());
/* 395 */       return this;
/*     */     }
/*     */     
/*     */     public Builder removeAll(String name) {
/* 399 */       for (int i = 0; i < this.namesAndValues.size(); i += 2) {
/* 400 */         if (name.equalsIgnoreCase(this.namesAndValues.get(i))) {
/* 401 */           this.namesAndValues.remove(i);
/* 402 */           this.namesAndValues.remove(i);
/* 403 */           i -= 2;
/*     */         } 
/*     */       } 
/* 406 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder set(String name, String value) {
/* 414 */       Headers.checkName(name);
/* 415 */       Headers.checkValue(value, name);
/* 416 */       removeAll(name);
/* 417 */       addLenient(name, value);
/* 418 */       return this;
/*     */     }
/*     */     
/*     */     @Nullable
/*     */     public String get(String name) {
/* 423 */       for (int i = this.namesAndValues.size() - 2; i >= 0; i -= 2) {
/* 424 */         if (name.equalsIgnoreCase(this.namesAndValues.get(i))) {
/* 425 */           return this.namesAndValues.get(i + 1);
/*     */         }
/*     */       } 
/* 428 */       return null;
/*     */     }
/*     */     
/*     */     public Headers build() {
/* 432 */       return new Headers(this);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Headers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */