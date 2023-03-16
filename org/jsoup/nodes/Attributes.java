/*     */ package org.jsoup.nodes;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Nullable;
/*     */ import org.jsoup.SerializationException;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.Normalizer;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.parser.ParseSettings;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Attributes
/*     */   implements Iterable<Attribute>, Cloneable
/*     */ {
/*     */   protected static final String dataPrefix = "data-";
/*     */   static final char InternalPrefix = '/';
/*     */   private static final int InitialCapacity = 3;
/*     */   private static final int GrowthFactor = 2;
/*     */   static final int NotFound = -1;
/*     */   private static final String EmptyString = "";
/*  50 */   private int size = 0;
/*  51 */   String[] keys = new String[3];
/*  52 */   Object[] vals = new Object[3];
/*     */ 
/*     */   
/*     */   private void checkCapacity(int minNewSize) {
/*  56 */     Validate.isTrue((minNewSize >= this.size));
/*  57 */     int curCap = this.keys.length;
/*  58 */     if (curCap >= minNewSize)
/*     */       return; 
/*  60 */     int newCap = (curCap >= 3) ? (this.size * 2) : 3;
/*  61 */     if (minNewSize > newCap) {
/*  62 */       newCap = minNewSize;
/*     */     }
/*  64 */     this.keys = Arrays.<String>copyOf(this.keys, newCap);
/*  65 */     this.vals = Arrays.copyOf(this.vals, newCap);
/*     */   }
/*     */   
/*     */   int indexOfKey(String key) {
/*  69 */     Validate.notNull(key);
/*  70 */     for (int i = 0; i < this.size; i++) {
/*  71 */       if (key.equals(this.keys[i]))
/*  72 */         return i; 
/*     */     } 
/*  74 */     return -1;
/*     */   }
/*     */   
/*     */   private int indexOfKeyIgnoreCase(String key) {
/*  78 */     Validate.notNull(key);
/*  79 */     for (int i = 0; i < this.size; i++) {
/*  80 */       if (key.equalsIgnoreCase(this.keys[i]))
/*  81 */         return i; 
/*     */     } 
/*  83 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static String checkNotNull(@Nullable Object val) {
/*  89 */     return (val == null) ? "" : (String)val;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String get(String key) {
/*  99 */     int i = indexOfKey(key);
/* 100 */     return (i == -1) ? "" : checkNotNull(this.vals[i]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIgnoreCase(String key) {
/* 109 */     int i = indexOfKeyIgnoreCase(key);
/* 110 */     return (i == -1) ? "" : checkNotNull(this.vals[i]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Object getUserData(String key) {
/* 120 */     Validate.notNull(key);
/* 121 */     if (!isInternalKey(key)) key = internalKey(key); 
/* 122 */     int i = indexOfKeyIgnoreCase(key);
/* 123 */     return (i == -1) ? null : this.vals[i];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Attributes add(String key, @Nullable String value) {
/* 131 */     addObject(key, value);
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   private void addObject(String key, @Nullable Object value) {
/* 136 */     checkCapacity(this.size + 1);
/* 137 */     this.keys[this.size] = key;
/* 138 */     this.vals[this.size] = value;
/* 139 */     this.size++;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Attributes put(String key, @Nullable String value) {
/* 149 */     Validate.notNull(key);
/* 150 */     int i = indexOfKey(key);
/* 151 */     if (i != -1) {
/* 152 */       this.vals[i] = value;
/*     */     } else {
/* 154 */       add(key, value);
/* 155 */     }  return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Attributes putUserData(String key, Object value) {
/* 166 */     Validate.notNull(key);
/* 167 */     if (!isInternalKey(key)) key = internalKey(key); 
/* 168 */     Validate.notNull(value);
/* 169 */     int i = indexOfKey(key);
/* 170 */     if (i != -1) {
/* 171 */       this.vals[i] = value;
/*     */     } else {
/* 173 */       addObject(key, value);
/* 174 */     }  return this;
/*     */   }
/*     */   
/*     */   void putIgnoreCase(String key, @Nullable String value) {
/* 178 */     int i = indexOfKeyIgnoreCase(key);
/* 179 */     if (i != -1) {
/* 180 */       this.vals[i] = value;
/* 181 */       if (!this.keys[i].equals(key)) {
/* 182 */         this.keys[i] = key;
/*     */       }
/*     */     } else {
/* 185 */       add(key, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Attributes put(String key, boolean value) {
/* 195 */     if (value) {
/* 196 */       putIgnoreCase(key, null);
/*     */     } else {
/* 198 */       remove(key);
/* 199 */     }  return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Attributes put(Attribute attribute) {
/* 208 */     Validate.notNull(attribute);
/* 209 */     put(attribute.getKey(), attribute.getValue());
/* 210 */     attribute.parent = this;
/* 211 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void remove(int index) {
/* 217 */     Validate.isFalse((index >= this.size));
/* 218 */     int shifted = this.size - index - 1;
/* 219 */     if (shifted > 0) {
/* 220 */       System.arraycopy(this.keys, index + 1, this.keys, index, shifted);
/* 221 */       System.arraycopy(this.vals, index + 1, this.vals, index, shifted);
/*     */     } 
/* 223 */     this.size--;
/* 224 */     this.keys[this.size] = null;
/* 225 */     this.vals[this.size] = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove(String key) {
/* 233 */     int i = indexOfKey(key);
/* 234 */     if (i != -1) {
/* 235 */       remove(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeIgnoreCase(String key) {
/* 243 */     int i = indexOfKeyIgnoreCase(key);
/* 244 */     if (i != -1) {
/* 245 */       remove(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasKey(String key) {
/* 254 */     return (indexOfKey(key) != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasKeyIgnoreCase(String key) {
/* 263 */     return (indexOfKeyIgnoreCase(key) != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasDeclaredValueForKey(String key) {
/* 272 */     int i = indexOfKey(key);
/* 273 */     return (i != -1 && this.vals[i] != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasDeclaredValueForKeyIgnoreCase(String key) {
/* 282 */     int i = indexOfKeyIgnoreCase(key);
/* 283 */     return (i != -1 && this.vals[i] != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 292 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 299 */     return (this.size == 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addAll(Attributes incoming) {
/* 307 */     if (incoming.size() == 0)
/*     */       return; 
/* 309 */     checkCapacity(this.size + incoming.size);
/*     */     
/* 311 */     boolean needsPut = (this.size != 0);
/*     */     
/* 313 */     for (Attribute attr : incoming) {
/* 314 */       if (needsPut) {
/* 315 */         put(attr); continue;
/*     */       } 
/* 317 */       add(attr.getKey(), attr.getValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Iterator<Attribute> iterator() {
/* 322 */     return new Iterator<Attribute>() {
/* 323 */         int i = 0;
/*     */ 
/*     */         
/*     */         public boolean hasNext() {
/* 327 */           while (this.i < Attributes.this.size && 
/* 328 */             Attributes.this.isInternalKey(Attributes.this.keys[this.i])) {
/* 329 */             this.i++;
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 334 */           return (this.i < Attributes.this.size);
/*     */         }
/*     */ 
/*     */         
/*     */         public Attribute next() {
/* 339 */           Attribute attr = new Attribute(Attributes.this.keys[this.i], (String)Attributes.this.vals[this.i], Attributes.this);
/* 340 */           this.i++;
/* 341 */           return attr;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/* 346 */           Attributes.this.remove(--this.i);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Attribute> asList() {
/* 356 */     ArrayList<Attribute> list = new ArrayList<>(this.size);
/* 357 */     for (int i = 0; i < this.size; i++) {
/* 358 */       if (!isInternalKey(this.keys[i])) {
/*     */         
/* 360 */         Attribute attr = new Attribute(this.keys[i], (String)this.vals[i], this);
/* 361 */         list.add(attr);
/*     */       } 
/* 363 */     }  return Collections.unmodifiableList(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, String> dataset() {
/* 372 */     return new Dataset(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String html() {
/* 380 */     StringBuilder sb = StringUtil.borrowBuilder();
/*     */     try {
/* 382 */       html(sb, (new Document("")).outputSettings());
/* 383 */     } catch (IOException e) {
/* 384 */       throw new SerializationException(e);
/*     */     } 
/* 386 */     return StringUtil.releaseBuilder(sb);
/*     */   }
/*     */   
/*     */   final void html(Appendable accum, Document.OutputSettings out) throws IOException {
/* 390 */     int sz = this.size;
/* 391 */     for (int i = 0; i < sz; i++) {
/* 392 */       if (!isInternalKey(this.keys[i])) {
/*     */         
/* 394 */         String key = Attribute.getValidKey(this.keys[i], out.syntax());
/* 395 */         if (key != null)
/* 396 */           Attribute.htmlNoValidate(key, (String)this.vals[i], accum.append(' '), out); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 402 */     return html();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object o) {
/* 413 */     if (this == o) return true; 
/* 414 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 416 */     Attributes that = (Attributes)o;
/* 417 */     if (this.size != that.size) return false; 
/* 418 */     for (int i = 0; i < this.size; i++) {
/* 419 */       String key = this.keys[i];
/* 420 */       int thatI = that.indexOfKey(key);
/* 421 */       if (thatI == -1)
/* 422 */         return false; 
/* 423 */       Object val = this.vals[i];
/* 424 */       Object thatVal = that.vals[thatI];
/* 425 */       if (val == null) {
/* 426 */         if (thatVal != null)
/* 427 */           return false; 
/* 428 */       } else if (!val.equals(thatVal)) {
/* 429 */         return false;
/*     */       } 
/* 431 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 440 */     int result = this.size;
/* 441 */     result = 31 * result + Arrays.hashCode((Object[])this.keys);
/* 442 */     result = 31 * result + Arrays.hashCode(this.vals);
/* 443 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Attributes clone() {
/*     */     Attributes clone;
/*     */     try {
/* 450 */       clone = (Attributes)super.clone();
/* 451 */     } catch (CloneNotSupportedException e) {
/* 452 */       throw new RuntimeException(e);
/*     */     } 
/* 454 */     clone.size = this.size;
/* 455 */     clone.keys = Arrays.<String>copyOf(this.keys, this.size);
/* 456 */     clone.vals = Arrays.copyOf(this.vals, this.size);
/* 457 */     return clone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void normalize() {
/* 464 */     for (int i = 0; i < this.size; i++) {
/* 465 */       this.keys[i] = Normalizer.lowerCase(this.keys[i]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int deduplicate(ParseSettings settings) {
/* 475 */     if (isEmpty())
/* 476 */       return 0; 
/* 477 */     boolean preserve = settings.preserveAttributeCase();
/* 478 */     int dupes = 0;
/* 479 */     for (int i = 0; i < this.keys.length; i++) {
/* 480 */       for (int j = i + 1; j < this.keys.length && 
/* 481 */         this.keys[j] != null; j++) {
/*     */         
/* 483 */         if ((preserve && this.keys[i].equals(this.keys[j])) || (!preserve && this.keys[i].equalsIgnoreCase(this.keys[j]))) {
/* 484 */           dupes++;
/* 485 */           remove(j);
/* 486 */           j--;
/*     */         } 
/*     */       } 
/*     */     } 
/* 490 */     return dupes;
/*     */   }
/*     */   
/*     */   private static class Dataset extends AbstractMap<String, String> {
/*     */     private final Attributes attributes;
/*     */     
/*     */     private Dataset(Attributes attributes) {
/* 497 */       this.attributes = attributes;
/*     */     }
/*     */ 
/*     */     
/*     */     public Set<Map.Entry<String, String>> entrySet() {
/* 502 */       return new EntrySet();
/*     */     }
/*     */ 
/*     */     
/*     */     public String put(String key, String value) {
/* 507 */       String dataKey = Attributes.dataKey(key);
/* 508 */       String oldValue = this.attributes.hasKey(dataKey) ? this.attributes.get(dataKey) : null;
/* 509 */       this.attributes.put(dataKey, value);
/* 510 */       return oldValue;
/*     */     }
/*     */     
/*     */     private class EntrySet extends AbstractSet<Map.Entry<String, String>> {
/*     */       private EntrySet() {}
/*     */       
/*     */       public Iterator<Map.Entry<String, String>> iterator() {
/* 517 */         return new Attributes.Dataset.DatasetIterator();
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/* 522 */         int count = 0;
/* 523 */         Iterator iter = new Attributes.Dataset.DatasetIterator();
/* 524 */         while (iter.hasNext())
/* 525 */           count++; 
/* 526 */         return count;
/*     */       } }
/*     */     private class DatasetIterator implements Iterator<Map.Entry<String, String>> { private Iterator<Attribute> attrIter;
/*     */       
/*     */       private DatasetIterator() {
/* 531 */         this.attrIter = Attributes.Dataset.this.attributes.iterator();
/*     */       } private Attribute attr;
/*     */       public boolean hasNext() {
/* 534 */         while (this.attrIter.hasNext()) {
/* 535 */           this.attr = this.attrIter.next();
/* 536 */           if (this.attr.isDataAttribute()) return true; 
/*     */         } 
/* 538 */         return false;
/*     */       }
/*     */       
/*     */       public Map.Entry<String, String> next() {
/* 542 */         return new Attribute(this.attr.getKey().substring("data-".length()), this.attr.getValue());
/*     */       }
/*     */       
/*     */       public void remove() {
/* 546 */         Attributes.Dataset.this.attributes.remove(this.attr.getKey());
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   private static String dataKey(String key) {
/* 552 */     return "data-" + key;
/*     */   }
/*     */   
/*     */   static String internalKey(String key) {
/* 556 */     return '/' + key;
/*     */   }
/*     */   
/*     */   private boolean isInternalKey(String key) {
/* 560 */     return (key != null && key.length() > 1 && key.charAt(0) == '/');
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\Attributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */