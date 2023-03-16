/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.MapIterator;
/*     */ import org.apache.commons.collections4.keyvalue.MultiKey;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MultiKeyMap<K, V>
/*     */   extends AbstractMapDecorator<MultiKey<? extends K>, V>
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = -1788199231038721040L;
/*     */   
/*     */   public static <K, V> MultiKeyMap<K, V> multiKeyMap(AbstractHashedMap<MultiKey<? extends K>, V> map) {
/*  97 */     if (map == null) {
/*  98 */       throw new NullPointerException("Map must not be null");
/*     */     }
/* 100 */     if (map.size() > 0) {
/* 101 */       throw new IllegalArgumentException("Map must be empty");
/*     */     }
/* 103 */     return new MultiKeyMap<K, V>(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MultiKeyMap() {
/* 111 */     this(new HashedMap<MultiKey<? extends K>, V>());
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
/*     */   protected MultiKeyMap(AbstractHashedMap<MultiKey<? extends K>, V> map) {
/* 123 */     super(map);
/* 124 */     this.map = map;
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
/*     */   public V get(Object key1, Object key2) {
/* 136 */     int hashCode = hash(key1, key2);
/* 137 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[decorated().hashIndex(hashCode, (decorated()).data.length)];
/*     */     
/* 139 */     while (entry != null) {
/* 140 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
/* 141 */         return entry.getValue();
/*     */       }
/* 143 */       entry = entry.next;
/*     */     } 
/* 145 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key1, Object key2) {
/* 156 */     int hashCode = hash(key1, key2);
/* 157 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[decorated().hashIndex(hashCode, (decorated()).data.length)];
/*     */     
/* 159 */     while (entry != null) {
/* 160 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
/* 161 */         return true;
/*     */       }
/* 163 */       entry = entry.next;
/*     */     } 
/* 165 */     return false;
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
/*     */   public V put(K key1, K key2, V value) {
/* 177 */     int hashCode = hash(key1, key2);
/* 178 */     int index = decorated().hashIndex(hashCode, (decorated()).data.length);
/* 179 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[index];
/* 180 */     while (entry != null) {
/* 181 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
/* 182 */         V oldValue = entry.getValue();
/* 183 */         decorated().updateEntry(entry, value);
/* 184 */         return oldValue;
/*     */       } 
/* 186 */       entry = entry.next;
/*     */     } 
/* 188 */     decorated().addMapping(index, hashCode, new MultiKey(key1, key2), value);
/* 189 */     return null;
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
/*     */   public V removeMultiKey(Object key1, Object key2) {
/* 201 */     int hashCode = hash(key1, key2);
/* 202 */     int index = decorated().hashIndex(hashCode, (decorated()).data.length);
/* 203 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[index];
/* 204 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
/* 205 */     while (entry != null) {
/* 206 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2)) {
/* 207 */         V oldValue = entry.getValue();
/* 208 */         decorated().removeMapping(entry, index, previous);
/* 209 */         return oldValue;
/*     */       } 
/* 211 */       previous = entry;
/* 212 */       entry = entry.next;
/*     */     } 
/* 214 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int hash(Object key1, Object key2) {
/* 225 */     int h = 0;
/* 226 */     if (key1 != null) {
/* 227 */       h ^= key1.hashCode();
/*     */     }
/* 229 */     if (key2 != null) {
/* 230 */       h ^= key2.hashCode();
/*     */     }
/* 232 */     h += h << 9 ^ 0xFFFFFFFF;
/* 233 */     h ^= h >>> 14;
/* 234 */     h += h << 4;
/* 235 */     h ^= h >>> 10;
/* 236 */     return h;
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
/*     */   protected boolean isEqualKey(AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry, Object key1, Object key2) {
/* 249 */     MultiKey<? extends K> multi = entry.getKey();
/* 250 */     return (multi.size() == 2 && (key1 == multi.getKey(0) || (key1 != null && key1.equals(multi.getKey(0)))) && (key2 == multi.getKey(1) || (key2 != null && key2.equals(multi.getKey(1)))));
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
/*     */   public V get(Object key1, Object key2, Object key3) {
/* 266 */     int hashCode = hash(key1, key2, key3);
/* 267 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[decorated().hashIndex(hashCode, (decorated()).data.length)];
/*     */     
/* 269 */     while (entry != null) {
/* 270 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
/* 271 */         return entry.getValue();
/*     */       }
/* 273 */       entry = entry.next;
/*     */     } 
/* 275 */     return null;
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
/*     */   public boolean containsKey(Object key1, Object key2, Object key3) {
/* 287 */     int hashCode = hash(key1, key2, key3);
/* 288 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[decorated().hashIndex(hashCode, (decorated()).data.length)];
/*     */     
/* 290 */     while (entry != null) {
/* 291 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
/* 292 */         return true;
/*     */       }
/* 294 */       entry = entry.next;
/*     */     } 
/* 296 */     return false;
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
/*     */   public V put(K key1, K key2, K key3, V value) {
/* 309 */     int hashCode = hash(key1, key2, key3);
/* 310 */     int index = decorated().hashIndex(hashCode, (decorated()).data.length);
/* 311 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[index];
/* 312 */     while (entry != null) {
/* 313 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
/* 314 */         V oldValue = entry.getValue();
/* 315 */         decorated().updateEntry(entry, value);
/* 316 */         return oldValue;
/*     */       } 
/* 318 */       entry = entry.next;
/*     */     } 
/* 320 */     decorated().addMapping(index, hashCode, new MultiKey(key1, key2, key3), value);
/* 321 */     return null;
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
/*     */   public V removeMultiKey(Object key1, Object key2, Object key3) {
/* 334 */     int hashCode = hash(key1, key2, key3);
/* 335 */     int index = decorated().hashIndex(hashCode, (decorated()).data.length);
/* 336 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[index];
/* 337 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
/* 338 */     while (entry != null) {
/* 339 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3)) {
/* 340 */         V oldValue = entry.getValue();
/* 341 */         decorated().removeMapping(entry, index, previous);
/* 342 */         return oldValue;
/*     */       } 
/* 344 */       previous = entry;
/* 345 */       entry = entry.next;
/*     */     } 
/* 347 */     return null;
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
/*     */   protected int hash(Object key1, Object key2, Object key3) {
/* 359 */     int h = 0;
/* 360 */     if (key1 != null) {
/* 361 */       h ^= key1.hashCode();
/*     */     }
/* 363 */     if (key2 != null) {
/* 364 */       h ^= key2.hashCode();
/*     */     }
/* 366 */     if (key3 != null) {
/* 367 */       h ^= key3.hashCode();
/*     */     }
/* 369 */     h += h << 9 ^ 0xFFFFFFFF;
/* 370 */     h ^= h >>> 14;
/* 371 */     h += h << 4;
/* 372 */     h ^= h >>> 10;
/* 373 */     return h;
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
/*     */   protected boolean isEqualKey(AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry, Object key1, Object key2, Object key3) {
/* 387 */     MultiKey<? extends K> multi = entry.getKey();
/* 388 */     return (multi.size() == 3 && (key1 == multi.getKey(0) || (key1 != null && key1.equals(multi.getKey(0)))) && (key2 == multi.getKey(1) || (key2 != null && key2.equals(multi.getKey(1)))) && (key3 == multi.getKey(2) || (key3 != null && key3.equals(multi.getKey(2)))));
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
/*     */   public V get(Object key1, Object key2, Object key3, Object key4) {
/* 406 */     int hashCode = hash(key1, key2, key3, key4);
/* 407 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[decorated().hashIndex(hashCode, (decorated()).data.length)];
/*     */     
/* 409 */     while (entry != null) {
/* 410 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
/* 411 */         return entry.getValue();
/*     */       }
/* 413 */       entry = entry.next;
/*     */     } 
/* 415 */     return null;
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
/*     */   public boolean containsKey(Object key1, Object key2, Object key3, Object key4) {
/* 428 */     int hashCode = hash(key1, key2, key3, key4);
/* 429 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[decorated().hashIndex(hashCode, (decorated()).data.length)];
/*     */     
/* 431 */     while (entry != null) {
/* 432 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
/* 433 */         return true;
/*     */       }
/* 435 */       entry = entry.next;
/*     */     } 
/* 437 */     return false;
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
/*     */   public V put(K key1, K key2, K key3, K key4, V value) {
/* 451 */     int hashCode = hash(key1, key2, key3, key4);
/* 452 */     int index = decorated().hashIndex(hashCode, (decorated()).data.length);
/* 453 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[index];
/* 454 */     while (entry != null) {
/* 455 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
/* 456 */         V oldValue = entry.getValue();
/* 457 */         decorated().updateEntry(entry, value);
/* 458 */         return oldValue;
/*     */       } 
/* 460 */       entry = entry.next;
/*     */     } 
/* 462 */     decorated().addMapping(index, hashCode, new MultiKey(key1, key2, key3, key4), value);
/* 463 */     return null;
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
/*     */   public V removeMultiKey(Object key1, Object key2, Object key3, Object key4) {
/* 477 */     int hashCode = hash(key1, key2, key3, key4);
/* 478 */     int index = decorated().hashIndex(hashCode, (decorated()).data.length);
/* 479 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[index];
/* 480 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
/* 481 */     while (entry != null) {
/* 482 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4)) {
/* 483 */         V oldValue = entry.getValue();
/* 484 */         decorated().removeMapping(entry, index, previous);
/* 485 */         return oldValue;
/*     */       } 
/* 487 */       previous = entry;
/* 488 */       entry = entry.next;
/*     */     } 
/* 490 */     return null;
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
/*     */   protected int hash(Object key1, Object key2, Object key3, Object key4) {
/* 503 */     int h = 0;
/* 504 */     if (key1 != null) {
/* 505 */       h ^= key1.hashCode();
/*     */     }
/* 507 */     if (key2 != null) {
/* 508 */       h ^= key2.hashCode();
/*     */     }
/* 510 */     if (key3 != null) {
/* 511 */       h ^= key3.hashCode();
/*     */     }
/* 513 */     if (key4 != null) {
/* 514 */       h ^= key4.hashCode();
/*     */     }
/* 516 */     h += h << 9 ^ 0xFFFFFFFF;
/* 517 */     h ^= h >>> 14;
/* 518 */     h += h << 4;
/* 519 */     h ^= h >>> 10;
/* 520 */     return h;
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
/*     */   protected boolean isEqualKey(AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry, Object key1, Object key2, Object key3, Object key4) {
/* 535 */     MultiKey<? extends K> multi = entry.getKey();
/* 536 */     return (multi.size() == 4 && (key1 == multi.getKey(0) || (key1 != null && key1.equals(multi.getKey(0)))) && (key2 == multi.getKey(1) || (key2 != null && key2.equals(multi.getKey(1)))) && (key3 == multi.getKey(2) || (key3 != null && key3.equals(multi.getKey(2)))) && (key4 == multi.getKey(3) || (key4 != null && key4.equals(multi.getKey(3)))));
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
/*     */   public V get(Object key1, Object key2, Object key3, Object key4, Object key5) {
/* 556 */     int hashCode = hash(key1, key2, key3, key4, key5);
/* 557 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[decorated().hashIndex(hashCode, (decorated()).data.length)];
/*     */     
/* 559 */     while (entry != null) {
/* 560 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
/* 561 */         return entry.getValue();
/*     */       }
/* 563 */       entry = entry.next;
/*     */     } 
/* 565 */     return null;
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
/*     */   public boolean containsKey(Object key1, Object key2, Object key3, Object key4, Object key5) {
/* 580 */     int hashCode = hash(key1, key2, key3, key4, key5);
/* 581 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[decorated().hashIndex(hashCode, (decorated()).data.length)];
/*     */     
/* 583 */     while (entry != null) {
/* 584 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
/* 585 */         return true;
/*     */       }
/* 587 */       entry = entry.next;
/*     */     } 
/* 589 */     return false;
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
/*     */   public V put(K key1, K key2, K key3, K key4, K key5, V value) {
/* 604 */     int hashCode = hash(key1, key2, key3, key4, key5);
/* 605 */     int index = decorated().hashIndex(hashCode, (decorated()).data.length);
/* 606 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[index];
/* 607 */     while (entry != null) {
/* 608 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
/* 609 */         V oldValue = entry.getValue();
/* 610 */         decorated().updateEntry(entry, value);
/* 611 */         return oldValue;
/*     */       } 
/* 613 */       entry = entry.next;
/*     */     } 
/* 615 */     decorated().addMapping(index, hashCode, new MultiKey(key1, key2, key3, key4, key5), value);
/* 616 */     return null;
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
/*     */   public V removeMultiKey(Object key1, Object key2, Object key3, Object key4, Object key5) {
/* 632 */     int hashCode = hash(key1, key2, key3, key4, key5);
/* 633 */     int index = decorated().hashIndex(hashCode, (decorated()).data.length);
/* 634 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry = (decorated()).data[index];
/* 635 */     AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> previous = null;
/* 636 */     while (entry != null) {
/* 637 */       if (entry.hashCode == hashCode && isEqualKey(entry, key1, key2, key3, key4, key5)) {
/* 638 */         V oldValue = entry.getValue();
/* 639 */         decorated().removeMapping(entry, index, previous);
/* 640 */         return oldValue;
/*     */       } 
/* 642 */       previous = entry;
/* 643 */       entry = entry.next;
/*     */     } 
/* 645 */     return null;
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
/*     */   protected int hash(Object key1, Object key2, Object key3, Object key4, Object key5) {
/* 659 */     int h = 0;
/* 660 */     if (key1 != null) {
/* 661 */       h ^= key1.hashCode();
/*     */     }
/* 663 */     if (key2 != null) {
/* 664 */       h ^= key2.hashCode();
/*     */     }
/* 666 */     if (key3 != null) {
/* 667 */       h ^= key3.hashCode();
/*     */     }
/* 669 */     if (key4 != null) {
/* 670 */       h ^= key4.hashCode();
/*     */     }
/* 672 */     if (key5 != null) {
/* 673 */       h ^= key5.hashCode();
/*     */     }
/* 675 */     h += h << 9 ^ 0xFFFFFFFF;
/* 676 */     h ^= h >>> 14;
/* 677 */     h += h << 4;
/* 678 */     h ^= h >>> 10;
/* 679 */     return h;
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
/*     */   protected boolean isEqualKey(AbstractHashedMap.HashEntry<MultiKey<? extends K>, V> entry, Object key1, Object key2, Object key3, Object key4, Object key5) {
/* 695 */     MultiKey<? extends K> multi = entry.getKey();
/* 696 */     return (multi.size() == 5 && (key1 == multi.getKey(0) || (key1 != null && key1.equals(multi.getKey(0)))) && (key2 == multi.getKey(1) || (key2 != null && key2.equals(multi.getKey(1)))) && (key3 == multi.getKey(2) || (key3 != null && key3.equals(multi.getKey(2)))) && (key4 == multi.getKey(3) || (key4 != null && key4.equals(multi.getKey(3)))) && (key5 == multi.getKey(4) || (key5 != null && key5.equals(multi.getKey(4)))));
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
/*     */   public boolean removeAll(Object key1) {
/* 716 */     boolean modified = false;
/* 717 */     MapIterator<MultiKey<? extends K>, V> it = mapIterator();
/* 718 */     while (it.hasNext()) {
/* 719 */       MultiKey<? extends K> multi = (MultiKey<? extends K>)it.next();
/* 720 */       if (multi.size() >= 1 && ((key1 == null) ? (multi.getKey(0) == null) : key1.equals(multi.getKey(0)))) {
/*     */         
/* 722 */         it.remove();
/* 723 */         modified = true;
/*     */       } 
/*     */     } 
/* 726 */     return modified;
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
/*     */   public boolean removeAll(Object key1, Object key2) {
/* 740 */     boolean modified = false;
/* 741 */     MapIterator<MultiKey<? extends K>, V> it = mapIterator();
/* 742 */     while (it.hasNext()) {
/* 743 */       MultiKey<? extends K> multi = (MultiKey<? extends K>)it.next();
/* 744 */       if (multi.size() >= 2 && ((key1 == null) ? (multi.getKey(0) == null) : key1.equals(multi.getKey(0))) && ((key2 == null) ? (multi.getKey(1) == null) : key2.equals(multi.getKey(1)))) {
/*     */ 
/*     */         
/* 747 */         it.remove();
/* 748 */         modified = true;
/*     */       } 
/*     */     } 
/* 751 */     return modified;
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
/*     */   public boolean removeAll(Object key1, Object key2, Object key3) {
/* 766 */     boolean modified = false;
/* 767 */     MapIterator<MultiKey<? extends K>, V> it = mapIterator();
/* 768 */     while (it.hasNext()) {
/* 769 */       MultiKey<? extends K> multi = (MultiKey<? extends K>)it.next();
/* 770 */       if (multi.size() >= 3 && ((key1 == null) ? (multi.getKey(0) == null) : key1.equals(multi.getKey(0))) && ((key2 == null) ? (multi.getKey(1) == null) : key2.equals(multi.getKey(1))) && ((key3 == null) ? (multi.getKey(2) == null) : key3.equals(multi.getKey(2)))) {
/*     */ 
/*     */ 
/*     */         
/* 774 */         it.remove();
/* 775 */         modified = true;
/*     */       } 
/*     */     } 
/* 778 */     return modified;
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
/*     */   public boolean removeAll(Object key1, Object key2, Object key3, Object key4) {
/* 794 */     boolean modified = false;
/* 795 */     MapIterator<MultiKey<? extends K>, V> it = mapIterator();
/* 796 */     while (it.hasNext()) {
/* 797 */       MultiKey<? extends K> multi = (MultiKey<? extends K>)it.next();
/* 798 */       if (multi.size() >= 4 && ((key1 == null) ? (multi.getKey(0) == null) : key1.equals(multi.getKey(0))) && ((key2 == null) ? (multi.getKey(1) == null) : key2.equals(multi.getKey(1))) && ((key3 == null) ? (multi.getKey(2) == null) : key3.equals(multi.getKey(2))) && ((key4 == null) ? (multi.getKey(3) == null) : key4.equals(multi.getKey(3)))) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 803 */         it.remove();
/* 804 */         modified = true;
/*     */       } 
/*     */     } 
/* 807 */     return modified;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkKey(MultiKey<?> key) {
/* 817 */     if (key == null) {
/* 818 */       throw new NullPointerException("Key must not be null");
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
/*     */   public MultiKeyMap<K, V> clone() {
/*     */     try {
/* 831 */       return (MultiKeyMap<K, V>)super.clone();
/* 832 */     } catch (CloneNotSupportedException e) {
/* 833 */       throw new InternalError();
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
/*     */   public V put(MultiKey<? extends K> key, V value) {
/* 849 */     checkKey(key);
/* 850 */     return super.put(key, value);
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
/*     */   public void putAll(Map<? extends MultiKey<? extends K>, ? extends V> mapToCopy) {
/* 863 */     for (MultiKey<? extends K> key : mapToCopy.keySet()) {
/* 864 */       checkKey(key);
/*     */     }
/* 866 */     super.putAll(mapToCopy);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public MapIterator<MultiKey<? extends K>, V> mapIterator() {
/* 872 */     return decorated().mapIterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected AbstractHashedMap<MultiKey<? extends K>, V> decorated() {
/* 880 */     return (AbstractHashedMap)super.decorated();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 891 */     out.defaultWriteObject();
/* 892 */     out.writeObject(this.map);
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 904 */     in.defaultReadObject();
/* 905 */     this.map = (Map<MultiKey<? extends K>, V>)in.readObject();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\MultiKeyMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */