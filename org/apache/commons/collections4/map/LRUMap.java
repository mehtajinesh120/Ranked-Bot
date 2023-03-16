/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.BoundedMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LRUMap<K, V>
/*     */   extends AbstractLinkedMap<K, V>
/*     */   implements BoundedMap<K, V>, Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -612114643488955218L;
/*     */   protected static final int DEFAULT_MAX_SIZE = 100;
/*     */   private transient int maxSize;
/*     */   private boolean scanUntilRemovable;
/*     */   
/*     */   public LRUMap() {
/*  77 */     this(100, 0.75F, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LRUMap(int maxSize) {
/*  87 */     this(maxSize, 0.75F);
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
/*     */   public LRUMap(int maxSize, int initialSize) {
/* 100 */     this(maxSize, initialSize, 0.75F);
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
/*     */   public LRUMap(int maxSize, boolean scanUntilRemovable) {
/* 112 */     this(maxSize, 0.75F, scanUntilRemovable);
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
/*     */   public LRUMap(int maxSize, float loadFactor) {
/* 125 */     this(maxSize, loadFactor, false);
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
/*     */   public LRUMap(int maxSize, int initialSize, float loadFactor) {
/* 141 */     this(maxSize, initialSize, loadFactor, false);
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
/*     */   public LRUMap(int maxSize, float loadFactor, boolean scanUntilRemovable) {
/* 155 */     this(maxSize, maxSize, loadFactor, scanUntilRemovable);
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
/*     */   public LRUMap(int maxSize, int initialSize, float loadFactor, boolean scanUntilRemovable) {
/* 175 */     super(initialSize, loadFactor);
/* 176 */     if (maxSize < 1) {
/* 177 */       throw new IllegalArgumentException("LRUMap max size must be greater than 0");
/*     */     }
/* 179 */     if (initialSize > maxSize) {
/* 180 */       throw new IllegalArgumentException("LRUMap initial size must not be greather than max size");
/*     */     }
/* 182 */     this.maxSize = maxSize;
/* 183 */     this.scanUntilRemovable = scanUntilRemovable;
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
/*     */   public LRUMap(Map<? extends K, ? extends V> map) {
/* 196 */     this(map, false);
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
/*     */   public LRUMap(Map<? extends K, ? extends V> map, boolean scanUntilRemovable) {
/* 211 */     this(map.size(), 0.75F, scanUntilRemovable);
/* 212 */     putAll(map);
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
/*     */   public V get(Object key) {
/* 227 */     return get(key, true);
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
/*     */   public V get(Object key, boolean updateToMRU) {
/* 244 */     AbstractLinkedMap.LinkEntry<K, V> entry = getEntry(key);
/* 245 */     if (entry == null) {
/* 246 */       return null;
/*     */     }
/* 248 */     if (updateToMRU) {
/* 249 */       moveToMRU(entry);
/*     */     }
/* 251 */     return entry.getValue();
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
/*     */   protected void moveToMRU(AbstractLinkedMap.LinkEntry<K, V> entry) {
/* 263 */     if (entry.after != this.header) {
/* 264 */       this.modCount++;
/*     */       
/* 266 */       if (entry.before == null) {
/* 267 */         throw new IllegalStateException("Entry.before is null. Please check that your keys are immutable, and that you have used synchronization properly. If so, then please report this to dev@commons.apache.org as a bug.");
/*     */       }
/*     */ 
/*     */       
/* 271 */       entry.before.after = entry.after;
/* 272 */       entry.after.before = entry.before;
/*     */       
/* 274 */       entry.after = this.header;
/* 275 */       entry.before = this.header.before;
/* 276 */       this.header.before.after = entry;
/* 277 */       this.header.before = entry;
/* 278 */     } else if (entry == this.header) {
/* 279 */       throw new IllegalStateException("Can't move header to MRU (please report this to dev@commons.apache.org)");
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
/*     */   protected void updateEntry(AbstractHashedMap.HashEntry<K, V> entry, V newValue) {
/* 295 */     moveToMRU((AbstractLinkedMap.LinkEntry<K, V>)entry);
/* 296 */     entry.setValue(newValue);
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
/*     */   protected void addMapping(int hashIndex, int hashCode, K key, V value) {
/* 316 */     if (isFull()) {
/* 317 */       AbstractLinkedMap.LinkEntry<K, V> reuse = this.header.after;
/* 318 */       boolean removeLRUEntry = false;
/* 319 */       if (this.scanUntilRemovable) {
/* 320 */         while (reuse != this.header && reuse != null) {
/* 321 */           if (removeLRU(reuse)) {
/* 322 */             removeLRUEntry = true;
/*     */             break;
/*     */           } 
/* 325 */           reuse = reuse.after;
/*     */         } 
/* 327 */         if (reuse == null) {
/* 328 */           throw new IllegalStateException("Entry.after=null, header.after" + this.header.after + " header.before" + this.header.before + " key=" + key + " value=" + value + " size=" + this.size + " maxSize=" + this.maxSize + " Please check that your keys are immutable, and that you have used synchronization properly." + " If so, then please report this to dev@commons.apache.org as a bug.");
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 335 */         removeLRUEntry = removeLRU(reuse);
/*     */       } 
/*     */       
/* 338 */       if (removeLRUEntry) {
/* 339 */         if (reuse == null) {
/* 340 */           throw new IllegalStateException("reuse=null, header.after=" + this.header.after + " header.before" + this.header.before + " key=" + key + " value=" + value + " size=" + this.size + " maxSize=" + this.maxSize + " Please check that your keys are immutable, and that you have used synchronization properly." + " If so, then please report this to dev@commons.apache.org as a bug.");
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 346 */         reuseMapping(reuse, hashIndex, hashCode, key, value);
/*     */       } else {
/* 348 */         super.addMapping(hashIndex, hashCode, key, value);
/*     */       } 
/*     */     } else {
/* 351 */       super.addMapping(hashIndex, hashCode, key, value);
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
/*     */   protected void reuseMapping(AbstractLinkedMap.LinkEntry<K, V> entry, int hashIndex, int hashCode, K key, V value) {
/*     */     try {
/* 372 */       int removeIndex = hashIndex(entry.hashCode, this.data.length);
/* 373 */       AbstractHashedMap.HashEntry<K, V>[] tmp = this.data;
/* 374 */       AbstractHashedMap.HashEntry<K, V> loop = tmp[removeIndex];
/* 375 */       AbstractHashedMap.HashEntry<K, V> previous = null;
/* 376 */       while (loop != entry && loop != null) {
/* 377 */         previous = loop;
/* 378 */         loop = loop.next;
/*     */       } 
/* 380 */       if (loop == null) {
/* 381 */         throw new IllegalStateException("Entry.next=null, data[removeIndex]=" + this.data[removeIndex] + " previous=" + previous + " key=" + key + " value=" + value + " size=" + this.size + " maxSize=" + this.maxSize + " Please check that your keys are immutable, and that you have used synchronization properly." + " If so, then please report this to dev@commons.apache.org as a bug.");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 389 */       this.modCount++;
/* 390 */       removeEntry(entry, removeIndex, previous);
/* 391 */       reuseEntry(entry, hashIndex, hashCode, key, value);
/* 392 */       addEntry(entry, hashIndex);
/* 393 */     } catch (NullPointerException ex) {
/* 394 */       throw new IllegalStateException("NPE, entry=" + entry + " entryIsHeader=" + ((entry == this.header) ? 1 : 0) + " key=" + key + " value=" + value + " size=" + this.size + " maxSize=" + this.maxSize + " Please check that your keys are immutable, and that you have used synchronization properly." + " If so, then please report this to dev@commons.apache.org as a bug.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean removeLRU(AbstractLinkedMap.LinkEntry<K, V> entry) {
/* 437 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFull() {
/* 447 */     return (this.size >= this.maxSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int maxSize() {
/* 456 */     return this.maxSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isScanUntilRemovable() {
/* 467 */     return this.scanUntilRemovable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LRUMap<K, V> clone() {
/* 478 */     return (LRUMap<K, V>)super.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 485 */     out.defaultWriteObject();
/* 486 */     doWriteObject(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 493 */     in.defaultReadObject();
/* 494 */     doReadObject(in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doWriteObject(ObjectOutputStream out) throws IOException {
/* 505 */     out.writeInt(this.maxSize);
/* 506 */     super.doWriteObject(out);
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
/*     */   protected void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 518 */     this.maxSize = in.readInt();
/* 519 */     super.doReadObject(in);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\LRUMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */