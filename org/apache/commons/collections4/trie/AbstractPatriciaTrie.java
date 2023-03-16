/*      */ package org.apache.commons.collections4.trie;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.AbstractMap;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import org.apache.commons.collections4.MapIterator;
/*      */ import org.apache.commons.collections4.OrderedMapIterator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ abstract class AbstractPatriciaTrie<K, V>
/*      */   extends AbstractBitwiseTrie<K, V>
/*      */ {
/*      */   private static final long serialVersionUID = 5155253417231339498L;
/*   49 */   private transient TrieEntry<K, V> root = new TrieEntry<K, V>(null, null, -1);
/*      */ 
/*      */   
/*      */   private volatile transient Set<K> keySet;
/*      */ 
/*      */   
/*      */   private volatile transient Collection<V> values;
/*      */ 
/*      */   
/*      */   private volatile transient Set<Map.Entry<K, V>> entrySet;
/*      */ 
/*      */   
/*   61 */   private transient int size = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   67 */   protected transient int modCount = 0;
/*      */   
/*      */   protected AbstractPatriciaTrie(KeyAnalyzer<? super K> keyAnalyzer) {
/*   70 */     super(keyAnalyzer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected AbstractPatriciaTrie(KeyAnalyzer<? super K> keyAnalyzer, Map<? extends K, ? extends V> map) {
/*   80 */     super(keyAnalyzer);
/*   81 */     putAll(map);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*   87 */     this.root.key = null;
/*   88 */     this.root.bitIndex = -1;
/*   89 */     this.root.value = null;
/*      */     
/*   91 */     this.root.parent = null;
/*   92 */     this.root.left = this.root;
/*   93 */     this.root.right = null;
/*   94 */     this.root.predecessor = this.root;
/*      */     
/*   96 */     this.size = 0;
/*   97 */     incrementModCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public int size() {
/*  102 */     return this.size;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void incrementSize() {
/*  109 */     this.size++;
/*  110 */     incrementModCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void decrementSize() {
/*  117 */     this.size--;
/*  118 */     incrementModCount();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void incrementModCount() {
/*  125 */     this.modCount++;
/*      */   }
/*      */ 
/*      */   
/*      */   public V put(K key, V value) {
/*  130 */     if (key == null) {
/*  131 */       throw new NullPointerException("Key cannot be null");
/*      */     }
/*      */     
/*  134 */     int lengthInBits = lengthInBits(key);
/*      */ 
/*      */ 
/*      */     
/*  138 */     if (lengthInBits == 0) {
/*  139 */       if (this.root.isEmpty()) {
/*  140 */         incrementSize();
/*      */       } else {
/*  142 */         incrementModCount();
/*      */       } 
/*  144 */       return this.root.setKeyValue(key, value);
/*      */     } 
/*      */     
/*  147 */     TrieEntry<K, V> found = getNearestEntryForKey(key, lengthInBits);
/*  148 */     if (compareKeys(key, found.key)) {
/*  149 */       if (found.isEmpty()) {
/*  150 */         incrementSize();
/*      */       } else {
/*  152 */         incrementModCount();
/*      */       } 
/*  154 */       return found.setKeyValue(key, value);
/*      */     } 
/*      */     
/*  157 */     int bitIndex = bitIndex(key, found.key);
/*  158 */     if (!KeyAnalyzer.isOutOfBoundsIndex(bitIndex)) {
/*  159 */       if (KeyAnalyzer.isValidBitIndex(bitIndex)) {
/*      */         
/*  161 */         TrieEntry<K, V> t = new TrieEntry<K, V>(key, value, bitIndex);
/*  162 */         addEntry(t, lengthInBits);
/*  163 */         incrementSize();
/*  164 */         return null;
/*  165 */       }  if (KeyAnalyzer.isNullBitKey(bitIndex)) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  170 */         if (this.root.isEmpty()) {
/*  171 */           incrementSize();
/*      */         } else {
/*  173 */           incrementModCount();
/*      */         } 
/*  175 */         return this.root.setKeyValue(key, value);
/*      */       } 
/*  177 */       if (KeyAnalyzer.isEqualBitKey(bitIndex))
/*      */       {
/*      */ 
/*      */         
/*  181 */         if (found != this.root) {
/*  182 */           incrementModCount();
/*  183 */           return found.setKeyValue(key, value);
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  188 */     throw new IllegalArgumentException("Failed to put: " + key + " -> " + value + ", " + bitIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> addEntry(TrieEntry<K, V> entry, int lengthInBits) {
/*  195 */     TrieEntry<K, V> current = this.root.left;
/*  196 */     TrieEntry<K, V> path = this.root;
/*      */     while (true) {
/*  198 */       if (current.bitIndex >= entry.bitIndex || current.bitIndex <= path.bitIndex) {
/*      */         
/*  200 */         entry.predecessor = entry;
/*      */         
/*  202 */         if (!isBitSet(entry.key, entry.bitIndex, lengthInBits)) {
/*  203 */           entry.left = entry;
/*  204 */           entry.right = current;
/*      */         } else {
/*  206 */           entry.left = current;
/*  207 */           entry.right = entry;
/*      */         } 
/*      */         
/*  210 */         entry.parent = path;
/*  211 */         if (current.bitIndex >= entry.bitIndex) {
/*  212 */           current.parent = entry;
/*      */         }
/*      */ 
/*      */         
/*  216 */         if (current.bitIndex <= path.bitIndex) {
/*  217 */           current.predecessor = entry;
/*      */         }
/*      */         
/*  220 */         if (path == this.root || !isBitSet(entry.key, path.bitIndex, lengthInBits)) {
/*  221 */           path.left = entry;
/*      */         } else {
/*  223 */           path.right = entry;
/*      */         } 
/*      */         
/*  226 */         return entry;
/*      */       } 
/*      */       
/*  229 */       path = current;
/*      */       
/*  231 */       if (!isBitSet(entry.key, current.bitIndex, lengthInBits)) {
/*  232 */         current = current.left; continue;
/*      */       } 
/*  234 */       current = current.right;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public V get(Object k) {
/*  241 */     TrieEntry<K, V> entry = getEntry(k);
/*  242 */     return (entry != null) ? entry.getValue() : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> getEntry(Object k) {
/*  253 */     K key = castKey(k);
/*  254 */     if (key == null) {
/*  255 */       return null;
/*      */     }
/*      */     
/*  258 */     int lengthInBits = lengthInBits(key);
/*  259 */     TrieEntry<K, V> entry = getNearestEntryForKey(key, lengthInBits);
/*  260 */     return (!entry.isEmpty() && compareKeys(key, entry.key)) ? entry : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map.Entry<K, V> select(K key) {
/*  283 */     int lengthInBits = lengthInBits(key);
/*  284 */     Reference<Map.Entry<K, V>> reference = new Reference<Map.Entry<K, V>>();
/*  285 */     if (!selectR(this.root.left, -1, key, lengthInBits, reference)) {
/*  286 */       return reference.get();
/*      */     }
/*  288 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K selectKey(K key) {
/*  311 */     Map.Entry<K, V> entry = select(key);
/*  312 */     if (entry == null) {
/*  313 */       return null;
/*      */     }
/*  315 */     return entry.getKey();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V selectValue(K key) {
/*  339 */     Map.Entry<K, V> entry = select(key);
/*  340 */     if (entry == null) {
/*  341 */       return null;
/*      */     }
/*  343 */     return entry.getValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean selectR(TrieEntry<K, V> h, int bitIndex, K key, int lengthInBits, Reference<Map.Entry<K, V>> reference) {
/*  354 */     if (h.bitIndex <= bitIndex) {
/*      */ 
/*      */ 
/*      */       
/*  358 */       if (!h.isEmpty()) {
/*  359 */         reference.set(h);
/*  360 */         return false;
/*      */       } 
/*  362 */       return true;
/*      */     } 
/*      */     
/*  365 */     if (!isBitSet(key, h.bitIndex, lengthInBits)) {
/*  366 */       if (selectR(h.left, h.bitIndex, key, lengthInBits, reference)) {
/*  367 */         return selectR(h.right, h.bitIndex, key, lengthInBits, reference);
/*      */       }
/*      */     }
/*  370 */     else if (selectR(h.right, h.bitIndex, key, lengthInBits, reference)) {
/*  371 */       return selectR(h.left, h.bitIndex, key, lengthInBits, reference);
/*      */     } 
/*      */     
/*  374 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean containsKey(Object k) {
/*  379 */     if (k == null) {
/*  380 */       return false;
/*      */     }
/*      */     
/*  383 */     K key = castKey(k);
/*  384 */     int lengthInBits = lengthInBits(key);
/*  385 */     TrieEntry<K, V> entry = getNearestEntryForKey(key, lengthInBits);
/*  386 */     return (!entry.isEmpty() && compareKeys(key, entry.key));
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/*  391 */     if (this.entrySet == null) {
/*  392 */       this.entrySet = new EntrySet();
/*      */     }
/*  394 */     return this.entrySet;
/*      */   }
/*      */ 
/*      */   
/*      */   public Set<K> keySet() {
/*  399 */     if (this.keySet == null) {
/*  400 */       this.keySet = new KeySet();
/*      */     }
/*  402 */     return this.keySet;
/*      */   }
/*      */ 
/*      */   
/*      */   public Collection<V> values() {
/*  407 */     if (this.values == null) {
/*  408 */       this.values = new Values();
/*      */     }
/*  410 */     return this.values;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public V remove(Object k) {
/*  420 */     if (k == null) {
/*  421 */       return null;
/*      */     }
/*      */     
/*  424 */     K key = castKey(k);
/*  425 */     int lengthInBits = lengthInBits(key);
/*  426 */     TrieEntry<K, V> current = this.root.left;
/*  427 */     TrieEntry<K, V> path = this.root;
/*      */     while (true) {
/*  429 */       if (current.bitIndex <= path.bitIndex) {
/*  430 */         if (!current.isEmpty() && compareKeys(key, current.key)) {
/*  431 */           return removeEntry(current);
/*      */         }
/*  433 */         return null;
/*      */       } 
/*      */       
/*  436 */       path = current;
/*      */       
/*  438 */       if (!isBitSet(key, current.bitIndex, lengthInBits)) {
/*  439 */         current = current.left; continue;
/*      */       } 
/*  441 */       current = current.right;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> getNearestEntryForKey(K key, int lengthInBits) {
/*  456 */     TrieEntry<K, V> current = this.root.left;
/*  457 */     TrieEntry<K, V> path = this.root;
/*      */     while (true) {
/*  459 */       if (current.bitIndex <= path.bitIndex) {
/*  460 */         return current;
/*      */       }
/*      */       
/*  463 */       path = current;
/*  464 */       if (!isBitSet(key, current.bitIndex, lengthInBits)) {
/*  465 */         current = current.left; continue;
/*      */       } 
/*  467 */       current = current.right;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   V removeEntry(TrieEntry<K, V> h) {
/*  480 */     if (h != this.root) {
/*  481 */       if (h.isInternalNode()) {
/*  482 */         removeInternalEntry(h);
/*      */       } else {
/*  484 */         removeExternalEntry(h);
/*      */       } 
/*      */     }
/*      */     
/*  488 */     decrementSize();
/*  489 */     return h.setKeyValue(null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeExternalEntry(TrieEntry<K, V> h) {
/*  499 */     if (h == this.root)
/*  500 */       throw new IllegalArgumentException("Cannot delete root Entry!"); 
/*  501 */     if (!h.isExternalNode()) {
/*  502 */       throw new IllegalArgumentException(h + " is not an external Entry!");
/*      */     }
/*      */     
/*  505 */     TrieEntry<K, V> parent = h.parent;
/*  506 */     TrieEntry<K, V> child = (h.left == h) ? h.right : h.left;
/*      */     
/*  508 */     if (parent.left == h) {
/*  509 */       parent.left = child;
/*      */     } else {
/*  511 */       parent.right = child;
/*      */     } 
/*      */ 
/*      */     
/*  515 */     if (child.bitIndex > parent.bitIndex) {
/*  516 */       child.parent = parent;
/*      */     } else {
/*  518 */       child.predecessor = parent;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void removeInternalEntry(TrieEntry<K, V> h) {
/*  531 */     if (h == this.root)
/*  532 */       throw new IllegalArgumentException("Cannot delete root Entry!"); 
/*  533 */     if (!h.isInternalNode()) {
/*  534 */       throw new IllegalArgumentException(h + " is not an internal Entry!");
/*      */     }
/*      */     
/*  537 */     TrieEntry<K, V> p = h.predecessor;
/*      */ 
/*      */     
/*  540 */     p.bitIndex = h.bitIndex;
/*      */ 
/*      */ 
/*      */     
/*  544 */     TrieEntry<K, V> parent = p.parent;
/*  545 */     TrieEntry<K, V> child = (p.left == h) ? p.right : p.left;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  553 */     if (p.predecessor == p && p.parent != h) {
/*  554 */       p.predecessor = p.parent;
/*      */     }
/*      */     
/*  557 */     if (parent.left == p) {
/*  558 */       parent.left = child;
/*      */     } else {
/*  560 */       parent.right = child;
/*      */     } 
/*      */     
/*  563 */     if (child.bitIndex > parent.bitIndex) {
/*  564 */       child.parent = parent;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  572 */     if (h.left.parent == h) {
/*  573 */       h.left.parent = p;
/*      */     }
/*      */     
/*  576 */     if (h.right.parent == h) {
/*  577 */       h.right.parent = p;
/*      */     }
/*      */ 
/*      */     
/*  581 */     if (h.parent.left == h) {
/*  582 */       h.parent.left = p;
/*      */     } else {
/*  584 */       h.parent.right = p;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  590 */     p.parent = h.parent;
/*  591 */     p.left = h.left;
/*  592 */     p.right = h.right;
/*      */ 
/*      */ 
/*      */     
/*  596 */     if (isValidUplink(p.left, p)) {
/*  597 */       p.left.predecessor = p;
/*      */     }
/*      */     
/*  600 */     if (isValidUplink(p.right, p)) {
/*  601 */       p.right.predecessor = p;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> nextEntry(TrieEntry<K, V> node) {
/*  610 */     if (node == null) {
/*  611 */       return firstEntry();
/*      */     }
/*  613 */     return nextEntryImpl(node.predecessor, node, (TrieEntry<K, V>)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> nextEntryImpl(TrieEntry<K, V> start, TrieEntry<K, V> previous, TrieEntry<K, V> tree) {
/*  652 */     TrieEntry<K, V> current = start;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  657 */     if (previous == null || start != previous.predecessor) {
/*  658 */       while (!current.left.isEmpty()) {
/*      */ 
/*      */         
/*  661 */         if (previous == current.left) {
/*      */           break;
/*      */         }
/*      */         
/*  665 */         if (isValidUplink(current.left, current)) {
/*  666 */           return current.left;
/*      */         }
/*      */         
/*  669 */         current = current.left;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  674 */     if (current.isEmpty()) {
/*  675 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  687 */     if (current.right == null) {
/*  688 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  692 */     if (previous != current.right) {
/*      */       
/*  694 */       if (isValidUplink(current.right, current)) {
/*  695 */         return current.right;
/*      */       }
/*      */ 
/*      */       
/*  699 */       return nextEntryImpl(current.right, previous, tree);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  704 */     while (current == current.parent.right) {
/*      */       
/*  706 */       if (current == tree) {
/*  707 */         return null;
/*      */       }
/*      */       
/*  710 */       current = current.parent;
/*      */     } 
/*      */ 
/*      */     
/*  714 */     if (current == tree) {
/*  715 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  719 */     if (current.parent.right == null) {
/*  720 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  724 */     if (previous != current.parent.right && isValidUplink(current.parent.right, current.parent))
/*      */     {
/*  726 */       return current.parent.right;
/*      */     }
/*      */ 
/*      */     
/*  730 */     if (current.parent.right == current.parent) {
/*  731 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  735 */     return nextEntryImpl(current.parent.right, previous, tree);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> firstEntry() {
/*  746 */     if (isEmpty()) {
/*  747 */       return null;
/*      */     }
/*      */     
/*  750 */     return followLeft(this.root);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> followLeft(TrieEntry<K, V> node) {
/*      */     while (true) {
/*  758 */       TrieEntry<K, V> child = node.left;
/*      */       
/*  760 */       if (child.isEmpty()) {
/*  761 */         child = node.right;
/*      */       }
/*      */       
/*  764 */       if (child.bitIndex <= node.bitIndex) {
/*  765 */         return child;
/*      */       }
/*      */       
/*  768 */       node = child;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Comparator<? super K> comparator() {
/*  775 */     return getKeyAnalyzer();
/*      */   }
/*      */   
/*      */   public K firstKey() {
/*  779 */     if (size() == 0) {
/*  780 */       throw new NoSuchElementException();
/*      */     }
/*  782 */     return firstEntry().getKey();
/*      */   }
/*      */   
/*      */   public K lastKey() {
/*  786 */     TrieEntry<K, V> entry = lastEntry();
/*  787 */     if (entry != null) {
/*  788 */       return entry.getKey();
/*      */     }
/*  790 */     throw new NoSuchElementException();
/*      */   }
/*      */   
/*      */   public K nextKey(K key) {
/*  794 */     if (key == null) {
/*  795 */       throw new NullPointerException();
/*      */     }
/*  797 */     TrieEntry<K, V> entry = getEntry(key);
/*  798 */     if (entry != null) {
/*  799 */       TrieEntry<K, V> nextEntry = nextEntry(entry);
/*  800 */       return (nextEntry != null) ? nextEntry.getKey() : null;
/*      */     } 
/*  802 */     return null;
/*      */   }
/*      */   
/*      */   public K previousKey(K key) {
/*  806 */     if (key == null) {
/*  807 */       throw new NullPointerException();
/*      */     }
/*  809 */     TrieEntry<K, V> entry = getEntry(key);
/*  810 */     if (entry != null) {
/*  811 */       TrieEntry<K, V> prevEntry = previousEntry(entry);
/*  812 */       return (prevEntry != null) ? prevEntry.getKey() : null;
/*      */     } 
/*  814 */     return null;
/*      */   }
/*      */   
/*      */   public OrderedMapIterator<K, V> mapIterator() {
/*  818 */     return new TrieMapIterator();
/*      */   }
/*      */   
/*      */   public SortedMap<K, V> prefixMap(K key) {
/*  822 */     return getPrefixMapByBits(key, 0, lengthInBits(key));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private SortedMap<K, V> getPrefixMapByBits(K key, int offsetInBits, int lengthInBits) {
/*  848 */     int offsetLength = offsetInBits + lengthInBits;
/*  849 */     if (offsetLength > lengthInBits(key)) {
/*  850 */       throw new IllegalArgumentException(offsetInBits + " + " + lengthInBits + " > " + lengthInBits(key));
/*      */     }
/*      */ 
/*      */     
/*  854 */     if (offsetLength == 0) {
/*  855 */       return (SortedMap<K, V>)this;
/*      */     }
/*      */     
/*  858 */     return new PrefixRangeMap(key, offsetInBits, lengthInBits);
/*      */   }
/*      */   
/*      */   public SortedMap<K, V> headMap(K toKey) {
/*  862 */     return new RangeEntryMap(null, toKey);
/*      */   }
/*      */   
/*      */   public SortedMap<K, V> subMap(K fromKey, K toKey) {
/*  866 */     return new RangeEntryMap(fromKey, toKey);
/*      */   }
/*      */   
/*      */   public SortedMap<K, V> tailMap(K fromKey) {
/*  870 */     return new RangeEntryMap(fromKey, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> higherEntry(K key) {
/*  881 */     int lengthInBits = lengthInBits(key);
/*      */     
/*  883 */     if (lengthInBits == 0) {
/*  884 */       if (!this.root.isEmpty()) {
/*      */         
/*  886 */         if (size() > 1) {
/*  887 */           return nextEntry(this.root);
/*      */         }
/*      */         
/*  890 */         return null;
/*      */       } 
/*      */       
/*  893 */       return firstEntry();
/*      */     } 
/*      */     
/*  896 */     TrieEntry<K, V> found = getNearestEntryForKey(key, lengthInBits);
/*  897 */     if (compareKeys(key, found.key)) {
/*  898 */       return nextEntry(found);
/*      */     }
/*      */     
/*  901 */     int bitIndex = bitIndex(key, found.key);
/*  902 */     if (KeyAnalyzer.isValidBitIndex(bitIndex)) {
/*  903 */       TrieEntry<K, V> added = new TrieEntry<K, V>(key, null, bitIndex);
/*  904 */       addEntry(added, lengthInBits);
/*  905 */       incrementSize();
/*  906 */       TrieEntry<K, V> ceil = nextEntry(added);
/*  907 */       removeEntry(added);
/*  908 */       this.modCount -= 2;
/*  909 */       return ceil;
/*  910 */     }  if (KeyAnalyzer.isNullBitKey(bitIndex)) {
/*  911 */       if (!this.root.isEmpty())
/*  912 */         return firstEntry(); 
/*  913 */       if (size() > 1) {
/*  914 */         return nextEntry(firstEntry());
/*      */       }
/*  916 */       return null;
/*      */     } 
/*  918 */     if (KeyAnalyzer.isEqualBitKey(bitIndex)) {
/*  919 */       return nextEntry(found);
/*      */     }
/*      */ 
/*      */     
/*  923 */     throw new IllegalStateException("invalid lookup: " + key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> ceilingEntry(K key) {
/*  949 */     int lengthInBits = lengthInBits(key);
/*      */     
/*  951 */     if (lengthInBits == 0) {
/*  952 */       if (!this.root.isEmpty()) {
/*  953 */         return this.root;
/*      */       }
/*  955 */       return firstEntry();
/*      */     } 
/*      */     
/*  958 */     TrieEntry<K, V> found = getNearestEntryForKey(key, lengthInBits);
/*  959 */     if (compareKeys(key, found.key)) {
/*  960 */       return found;
/*      */     }
/*      */     
/*  963 */     int bitIndex = bitIndex(key, found.key);
/*  964 */     if (KeyAnalyzer.isValidBitIndex(bitIndex)) {
/*  965 */       TrieEntry<K, V> added = new TrieEntry<K, V>(key, null, bitIndex);
/*  966 */       addEntry(added, lengthInBits);
/*  967 */       incrementSize();
/*  968 */       TrieEntry<K, V> ceil = nextEntry(added);
/*  969 */       removeEntry(added);
/*  970 */       this.modCount -= 2;
/*  971 */       return ceil;
/*  972 */     }  if (KeyAnalyzer.isNullBitKey(bitIndex)) {
/*  973 */       if (!this.root.isEmpty()) {
/*  974 */         return this.root;
/*      */       }
/*  976 */       return firstEntry();
/*  977 */     }  if (KeyAnalyzer.isEqualBitKey(bitIndex)) {
/*  978 */       return found;
/*      */     }
/*      */ 
/*      */     
/*  982 */     throw new IllegalStateException("invalid lookup: " + key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> lowerEntry(K key) {
/* 1007 */     int lengthInBits = lengthInBits(key);
/*      */     
/* 1009 */     if (lengthInBits == 0) {
/* 1010 */       return null;
/*      */     }
/*      */     
/* 1013 */     TrieEntry<K, V> found = getNearestEntryForKey(key, lengthInBits);
/* 1014 */     if (compareKeys(key, found.key)) {
/* 1015 */       return previousEntry(found);
/*      */     }
/*      */     
/* 1018 */     int bitIndex = bitIndex(key, found.key);
/* 1019 */     if (KeyAnalyzer.isValidBitIndex(bitIndex)) {
/* 1020 */       TrieEntry<K, V> added = new TrieEntry<K, V>(key, null, bitIndex);
/* 1021 */       addEntry(added, lengthInBits);
/* 1022 */       incrementSize();
/* 1023 */       TrieEntry<K, V> prior = previousEntry(added);
/* 1024 */       removeEntry(added);
/* 1025 */       this.modCount -= 2;
/* 1026 */       return prior;
/* 1027 */     }  if (KeyAnalyzer.isNullBitKey(bitIndex))
/* 1028 */       return null; 
/* 1029 */     if (KeyAnalyzer.isEqualBitKey(bitIndex)) {
/* 1030 */       return previousEntry(found);
/*      */     }
/*      */ 
/*      */     
/* 1034 */     throw new IllegalStateException("invalid lookup: " + key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> floorEntry(K key) {
/* 1045 */     int lengthInBits = lengthInBits(key);
/*      */     
/* 1047 */     if (lengthInBits == 0) {
/* 1048 */       if (!this.root.isEmpty()) {
/* 1049 */         return this.root;
/*      */       }
/* 1051 */       return null;
/*      */     } 
/*      */     
/* 1054 */     TrieEntry<K, V> found = getNearestEntryForKey(key, lengthInBits);
/* 1055 */     if (compareKeys(key, found.key)) {
/* 1056 */       return found;
/*      */     }
/*      */     
/* 1059 */     int bitIndex = bitIndex(key, found.key);
/* 1060 */     if (KeyAnalyzer.isValidBitIndex(bitIndex)) {
/* 1061 */       TrieEntry<K, V> added = new TrieEntry<K, V>(key, null, bitIndex);
/* 1062 */       addEntry(added, lengthInBits);
/* 1063 */       incrementSize();
/* 1064 */       TrieEntry<K, V> floor = previousEntry(added);
/* 1065 */       removeEntry(added);
/* 1066 */       this.modCount -= 2;
/* 1067 */       return floor;
/* 1068 */     }  if (KeyAnalyzer.isNullBitKey(bitIndex)) {
/* 1069 */       if (!this.root.isEmpty()) {
/* 1070 */         return this.root;
/*      */       }
/* 1072 */       return null;
/* 1073 */     }  if (KeyAnalyzer.isEqualBitKey(bitIndex)) {
/* 1074 */       return found;
/*      */     }
/*      */ 
/*      */     
/* 1078 */     throw new IllegalStateException("invalid lookup: " + key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> subtree(K prefix, int offsetInBits, int lengthInBits) {
/* 1088 */     TrieEntry<K, V> current = this.root.left;
/* 1089 */     TrieEntry<K, V> path = this.root;
/*      */     
/* 1091 */     while (current.bitIndex > path.bitIndex && lengthInBits > current.bitIndex) {
/*      */ 
/*      */ 
/*      */       
/* 1095 */       path = current;
/* 1096 */       if (!isBitSet(prefix, offsetInBits + current.bitIndex, offsetInBits + lengthInBits)) {
/* 1097 */         current = current.left; continue;
/*      */       } 
/* 1099 */       current = current.right;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1104 */     TrieEntry<K, V> entry = current.isEmpty() ? path : current;
/*      */ 
/*      */     
/* 1107 */     if (entry.isEmpty()) {
/* 1108 */       return null;
/*      */     }
/*      */     
/* 1111 */     int endIndexInBits = offsetInBits + lengthInBits;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1117 */     if (entry == this.root && lengthInBits(entry.getKey()) < endIndexInBits) {
/* 1118 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1123 */     if (isBitSet(prefix, endIndexInBits - 1, endIndexInBits) != isBitSet(entry.key, lengthInBits - 1, lengthInBits(entry.key)))
/*      */     {
/* 1125 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1129 */     int bitIndex = getKeyAnalyzer().bitIndex(prefix, offsetInBits, lengthInBits, entry.key, 0, lengthInBits(entry.getKey()));
/*      */ 
/*      */     
/* 1132 */     if (bitIndex >= 0 && bitIndex < lengthInBits) {
/* 1133 */       return null;
/*      */     }
/*      */     
/* 1136 */     return entry;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> lastEntry() {
/* 1146 */     return followRight(this.root.left);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> followRight(TrieEntry<K, V> node) {
/* 1154 */     if (node.right == null) {
/* 1155 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1159 */     while (node.right.bitIndex > node.bitIndex) {
/* 1160 */       node = node.right;
/*      */     }
/*      */     
/* 1163 */     return node.right;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> previousEntry(TrieEntry<K, V> start) {
/* 1186 */     if (start.predecessor == null) {
/* 1187 */       throw new IllegalArgumentException("must have come from somewhere!");
/*      */     }
/*      */     
/* 1190 */     if (start.predecessor.right == start) {
/* 1191 */       if (isValidUplink(start.predecessor.left, start.predecessor)) {
/* 1192 */         return start.predecessor.left;
/*      */       }
/* 1194 */       return followRight(start.predecessor.left);
/*      */     } 
/* 1196 */     TrieEntry<K, V> node = start.predecessor;
/* 1197 */     while (node.parent != null && node == node.parent.left) {
/* 1198 */       node = node.parent;
/*      */     }
/*      */     
/* 1201 */     if (node.parent == null) {
/* 1202 */       return null;
/*      */     }
/*      */     
/* 1205 */     if (isValidUplink(node.parent.left, node.parent)) {
/* 1206 */       if (node.parent.left == this.root) {
/* 1207 */         if (this.root.isEmpty()) {
/* 1208 */           return null;
/*      */         }
/* 1210 */         return this.root;
/*      */       } 
/*      */       
/* 1213 */       return node.parent.left;
/*      */     } 
/* 1215 */     return followRight(node.parent.left);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TrieEntry<K, V> nextEntryInSubtree(TrieEntry<K, V> node, TrieEntry<K, V> parentOfSubtree) {
/* 1227 */     if (node == null) {
/* 1228 */       return firstEntry();
/*      */     }
/* 1230 */     return nextEntryImpl(node.predecessor, node, parentOfSubtree);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isValidUplink(TrieEntry<?, ?> next, TrieEntry<?, ?> from) {
/* 1237 */     return (next != null && next.bitIndex <= from.bitIndex && !next.isEmpty());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static class Reference<E>
/*      */   {
/*      */     private E item;
/*      */ 
/*      */ 
/*      */     
/*      */     private Reference() {}
/*      */ 
/*      */     
/*      */     public void set(E item) {
/* 1252 */       this.item = item;
/*      */     }
/*      */     
/*      */     public E get() {
/* 1256 */       return this.item;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected static class TrieEntry<K, V>
/*      */     extends AbstractBitwiseTrie.BasicEntry<K, V>
/*      */   {
/*      */     private static final long serialVersionUID = 4596023148184140013L;
/*      */ 
/*      */     
/*      */     protected int bitIndex;
/*      */ 
/*      */     
/*      */     protected TrieEntry<K, V> parent;
/*      */ 
/*      */     
/*      */     protected TrieEntry<K, V> left;
/*      */ 
/*      */     
/*      */     protected TrieEntry<K, V> right;
/*      */     
/*      */     protected TrieEntry<K, V> predecessor;
/*      */ 
/*      */     
/*      */     public TrieEntry(K key, V value, int bitIndex) {
/* 1283 */       super(key, value);
/*      */       
/* 1285 */       this.bitIndex = bitIndex;
/*      */       
/* 1287 */       this.parent = null;
/* 1288 */       this.left = this;
/* 1289 */       this.right = null;
/* 1290 */       this.predecessor = this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1299 */       return (this.key == null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isInternalNode() {
/* 1306 */       return (this.left != this && this.right != this);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isExternalNode() {
/* 1313 */       return !isInternalNode();
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1318 */       StringBuilder buffer = new StringBuilder();
/*      */       
/* 1320 */       if (this.bitIndex == -1) {
/* 1321 */         buffer.append("RootEntry(");
/*      */       } else {
/* 1323 */         buffer.append("Entry(");
/*      */       } 
/*      */       
/* 1326 */       buffer.append("key=").append(getKey()).append(" [").append(this.bitIndex).append("], ");
/* 1327 */       buffer.append("value=").append(getValue()).append(", ");
/*      */ 
/*      */       
/* 1330 */       if (this.parent != null) {
/* 1331 */         if (this.parent.bitIndex == -1) {
/* 1332 */           buffer.append("parent=").append("ROOT");
/*      */         } else {
/* 1334 */           buffer.append("parent=").append(this.parent.getKey()).append(" [").append(this.parent.bitIndex).append("]");
/*      */         } 
/*      */       } else {
/* 1337 */         buffer.append("parent=").append("null");
/*      */       } 
/* 1339 */       buffer.append(", ");
/*      */       
/* 1341 */       if (this.left != null) {
/* 1342 */         if (this.left.bitIndex == -1) {
/* 1343 */           buffer.append("left=").append("ROOT");
/*      */         } else {
/* 1345 */           buffer.append("left=").append(this.left.getKey()).append(" [").append(this.left.bitIndex).append("]");
/*      */         } 
/*      */       } else {
/* 1348 */         buffer.append("left=").append("null");
/*      */       } 
/* 1350 */       buffer.append(", ");
/*      */       
/* 1352 */       if (this.right != null) {
/* 1353 */         if (this.right.bitIndex == -1) {
/* 1354 */           buffer.append("right=").append("ROOT");
/*      */         } else {
/* 1356 */           buffer.append("right=").append(this.right.getKey()).append(" [").append(this.right.bitIndex).append("]");
/*      */         } 
/*      */       } else {
/* 1359 */         buffer.append("right=").append("null");
/*      */       } 
/* 1361 */       buffer.append(", ");
/*      */       
/* 1363 */       if (this.predecessor != null) {
/* 1364 */         if (this.predecessor.bitIndex == -1) {
/* 1365 */           buffer.append("predecessor=").append("ROOT");
/*      */         } else {
/* 1367 */           buffer.append("predecessor=").append(this.predecessor.getKey()).append(" [").append(this.predecessor.bitIndex).append("]");
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 1372 */       buffer.append(")");
/* 1373 */       return buffer.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class EntrySet
/*      */     extends AbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     private EntrySet() {}
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 1385 */       return new EntryIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1390 */       if (!(o instanceof Map.Entry)) {
/* 1391 */         return false;
/*      */       }
/*      */       
/* 1394 */       AbstractPatriciaTrie.TrieEntry<K, V> candidate = AbstractPatriciaTrie.this.getEntry(((Map.Entry)o).getKey());
/* 1395 */       return (candidate != null && candidate.equals(o));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object obj) {
/* 1400 */       if (!(obj instanceof Map.Entry)) {
/* 1401 */         return false;
/*      */       }
/* 1403 */       if (!contains(obj)) {
/* 1404 */         return false;
/*      */       }
/* 1406 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 1407 */       AbstractPatriciaTrie.this.remove(entry.getKey());
/* 1408 */       return true;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1413 */       return AbstractPatriciaTrie.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1418 */       AbstractPatriciaTrie.this.clear();
/*      */     }
/*      */     
/*      */     private class EntryIterator
/*      */       extends AbstractPatriciaTrie<K, V>.TrieIterator<Map.Entry<K, V>> {
/*      */       private EntryIterator() {}
/*      */       
/*      */       public Map.Entry<K, V> next() {
/* 1426 */         return nextEntry();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class KeySet
/*      */     extends AbstractSet<K>
/*      */   {
/*      */     private KeySet() {}
/*      */     
/*      */     public Iterator<K> iterator() {
/* 1438 */       return new KeyIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1443 */       return AbstractPatriciaTrie.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1448 */       return AbstractPatriciaTrie.this.containsKey(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 1453 */       int size = size();
/* 1454 */       AbstractPatriciaTrie.this.remove(o);
/* 1455 */       return (size != size());
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1460 */       AbstractPatriciaTrie.this.clear();
/*      */     }
/*      */     
/*      */     private class KeyIterator
/*      */       extends AbstractPatriciaTrie<K, V>.TrieIterator<K> {
/*      */       private KeyIterator() {}
/*      */       
/*      */       public K next() {
/* 1468 */         return (K)nextEntry().getKey();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private class Values
/*      */     extends AbstractCollection<V>
/*      */   {
/*      */     private Values() {}
/*      */     
/*      */     public Iterator<V> iterator() {
/* 1480 */       return new ValueIterator();
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1485 */       return AbstractPatriciaTrie.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1490 */       return AbstractPatriciaTrie.this.containsValue(o);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1495 */       AbstractPatriciaTrie.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 1500 */       for (Iterator<V> it = iterator(); it.hasNext(); ) {
/* 1501 */         V value = it.next();
/* 1502 */         if (AbstractBitwiseTrie.compare(value, o)) {
/* 1503 */           it.remove();
/* 1504 */           return true;
/*      */         } 
/*      */       } 
/* 1507 */       return false;
/*      */     }
/*      */     
/*      */     private class ValueIterator
/*      */       extends AbstractPatriciaTrie<K, V>.TrieIterator<V> {
/*      */       private ValueIterator() {}
/*      */       
/*      */       public V next() {
/* 1515 */         return (V)nextEntry().getValue();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract class TrieIterator<E>
/*      */     implements Iterator<E>
/*      */   {
/* 1526 */     protected int expectedModCount = AbstractPatriciaTrie.this.modCount;
/*      */ 
/*      */     
/*      */     protected AbstractPatriciaTrie.TrieEntry<K, V> next;
/*      */     
/*      */     protected AbstractPatriciaTrie.TrieEntry<K, V> current;
/*      */ 
/*      */     
/*      */     protected TrieIterator() {
/* 1535 */       this.next = AbstractPatriciaTrie.this.nextEntry((AbstractPatriciaTrie.TrieEntry<K, V>)null);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected TrieIterator(AbstractPatriciaTrie.TrieEntry<K, V> firstEntry) {
/* 1542 */       this.next = firstEntry;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected AbstractPatriciaTrie.TrieEntry<K, V> nextEntry() {
/* 1549 */       if (this.expectedModCount != AbstractPatriciaTrie.this.modCount) {
/* 1550 */         throw new ConcurrentModificationException();
/*      */       }
/*      */       
/* 1553 */       AbstractPatriciaTrie.TrieEntry<K, V> e = this.next;
/* 1554 */       if (e == null) {
/* 1555 */         throw new NoSuchElementException();
/*      */       }
/*      */       
/* 1558 */       this.next = findNext(e);
/* 1559 */       this.current = e;
/* 1560 */       return e;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected AbstractPatriciaTrie.TrieEntry<K, V> findNext(AbstractPatriciaTrie.TrieEntry<K, V> prior) {
/* 1567 */       return AbstractPatriciaTrie.this.nextEntry(prior);
/*      */     }
/*      */     
/*      */     public boolean hasNext() {
/* 1571 */       return (this.next != null);
/*      */     }
/*      */     
/*      */     public void remove() {
/* 1575 */       if (this.current == null) {
/* 1576 */         throw new IllegalStateException();
/*      */       }
/*      */       
/* 1579 */       if (this.expectedModCount != AbstractPatriciaTrie.this.modCount) {
/* 1580 */         throw new ConcurrentModificationException();
/*      */       }
/*      */       
/* 1583 */       AbstractPatriciaTrie.TrieEntry<K, V> node = this.current;
/* 1584 */       this.current = null;
/* 1585 */       AbstractPatriciaTrie.this.removeEntry(node);
/*      */       
/* 1587 */       this.expectedModCount = AbstractPatriciaTrie.this.modCount;
/*      */     }
/*      */   }
/*      */   
/*      */   private class TrieMapIterator
/*      */     extends TrieIterator<K>
/*      */     implements OrderedMapIterator<K, V> {
/*      */     protected AbstractPatriciaTrie.TrieEntry<K, V> previous;
/*      */     
/*      */     private TrieMapIterator() {}
/*      */     
/*      */     public K next() {
/* 1599 */       return nextEntry().getKey();
/*      */     }
/*      */     
/*      */     public K getKey() {
/* 1603 */       if (this.current == null) {
/* 1604 */         throw new IllegalStateException();
/*      */       }
/* 1606 */       return (K)this.current.getKey();
/*      */     }
/*      */     
/*      */     public V getValue() {
/* 1610 */       if (this.current == null) {
/* 1611 */         throw new IllegalStateException();
/*      */       }
/* 1613 */       return (V)this.current.getValue();
/*      */     }
/*      */     
/*      */     public V setValue(V value) {
/* 1617 */       if (this.current == null) {
/* 1618 */         throw new IllegalStateException();
/*      */       }
/* 1620 */       return (V)this.current.setValue(value);
/*      */     }
/*      */     
/*      */     public boolean hasPrevious() {
/* 1624 */       return (this.previous != null);
/*      */     }
/*      */     
/*      */     public K previous() {
/* 1628 */       return previousEntry().getKey();
/*      */     }
/*      */ 
/*      */     
/*      */     protected AbstractPatriciaTrie.TrieEntry<K, V> nextEntry() {
/* 1633 */       AbstractPatriciaTrie.TrieEntry<K, V> nextEntry = super.nextEntry();
/* 1634 */       this.previous = nextEntry;
/* 1635 */       return nextEntry;
/*      */     }
/*      */     
/*      */     protected AbstractPatriciaTrie.TrieEntry<K, V> previousEntry() {
/* 1639 */       if (this.expectedModCount != AbstractPatriciaTrie.this.modCount) {
/* 1640 */         throw new ConcurrentModificationException();
/*      */       }
/*      */       
/* 1643 */       AbstractPatriciaTrie.TrieEntry<K, V> e = this.previous;
/* 1644 */       if (e == null) {
/* 1645 */         throw new NoSuchElementException();
/*      */       }
/*      */       
/* 1648 */       this.previous = AbstractPatriciaTrie.this.previousEntry(e);
/* 1649 */       this.next = this.current;
/* 1650 */       this.current = e;
/* 1651 */       return this.current;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private abstract class RangeMap
/*      */     extends AbstractMap<K, V>
/*      */     implements SortedMap<K, V>
/*      */   {
/*      */     private volatile transient Set<Map.Entry<K, V>> entrySet;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private RangeMap() {}
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Comparator<? super K> comparator() {
/* 1691 */       return AbstractPatriciaTrie.this.comparator();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 1696 */       if (!inRange((K)AbstractPatriciaTrie.this.castKey(key))) {
/* 1697 */         return false;
/*      */       }
/*      */       
/* 1700 */       return AbstractPatriciaTrie.this.containsKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V remove(Object key) {
/* 1705 */       if (!inRange((K)AbstractPatriciaTrie.this.castKey(key))) {
/* 1706 */         return null;
/*      */       }
/*      */       
/* 1709 */       return (V)AbstractPatriciaTrie.this.remove(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V get(Object key) {
/* 1714 */       if (!inRange((K)AbstractPatriciaTrie.this.castKey(key))) {
/* 1715 */         return null;
/*      */       }
/*      */       
/* 1718 */       return (V)AbstractPatriciaTrie.this.get(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V put(K key, V value) {
/* 1723 */       if (!inRange(key)) {
/* 1724 */         throw new IllegalArgumentException("Key is out of range: " + key);
/*      */       }
/* 1726 */       return AbstractPatriciaTrie.this.put(key, value);
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<K, V>> entrySet() {
/* 1731 */       if (this.entrySet == null) {
/* 1732 */         this.entrySet = createEntrySet();
/*      */       }
/* 1734 */       return this.entrySet;
/*      */     }
/*      */     
/*      */     public SortedMap<K, V> subMap(K fromKey, K toKey) {
/* 1738 */       if (!inRange2(fromKey)) {
/* 1739 */         throw new IllegalArgumentException("FromKey is out of range: " + fromKey);
/*      */       }
/*      */       
/* 1742 */       if (!inRange2(toKey)) {
/* 1743 */         throw new IllegalArgumentException("ToKey is out of range: " + toKey);
/*      */       }
/*      */       
/* 1746 */       return createRangeMap(fromKey, isFromInclusive(), toKey, isToInclusive());
/*      */     }
/*      */     
/*      */     public SortedMap<K, V> headMap(K toKey) {
/* 1750 */       if (!inRange2(toKey)) {
/* 1751 */         throw new IllegalArgumentException("ToKey is out of range: " + toKey);
/*      */       }
/* 1753 */       return createRangeMap(getFromKey(), isFromInclusive(), toKey, isToInclusive());
/*      */     }
/*      */     
/*      */     public SortedMap<K, V> tailMap(K fromKey) {
/* 1757 */       if (!inRange2(fromKey)) {
/* 1758 */         throw new IllegalArgumentException("FromKey is out of range: " + fromKey);
/*      */       }
/* 1760 */       return createRangeMap(fromKey, isFromInclusive(), getToKey(), isToInclusive());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean inRange(K key) {
/* 1767 */       K fromKey = getFromKey();
/* 1768 */       K toKey = getToKey();
/*      */       
/* 1770 */       return ((fromKey == null || inFromRange(key, false)) && (toKey == null || inToRange(key, false)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean inRange2(K key) {
/* 1777 */       K fromKey = getFromKey();
/* 1778 */       K toKey = getToKey();
/*      */       
/* 1780 */       return ((fromKey == null || inFromRange(key, false)) && (toKey == null || inToRange(key, true)));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean inFromRange(K key, boolean forceInclusive) {
/* 1787 */       K fromKey = getFromKey();
/* 1788 */       boolean fromInclusive = isFromInclusive();
/*      */       
/* 1790 */       int ret = AbstractPatriciaTrie.this.getKeyAnalyzer().compare(key, fromKey);
/* 1791 */       if (fromInclusive || forceInclusive) {
/* 1792 */         return (ret >= 0);
/*      */       }
/* 1794 */       return (ret > 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean inToRange(K key, boolean forceInclusive) {
/* 1801 */       K toKey = getToKey();
/* 1802 */       boolean toInclusive = isToInclusive();
/*      */       
/* 1804 */       int ret = AbstractPatriciaTrie.this.getKeyAnalyzer().compare(key, toKey);
/* 1805 */       if (toInclusive || forceInclusive) {
/* 1806 */         return (ret <= 0);
/*      */       }
/* 1808 */       return (ret < 0);
/*      */     }
/*      */ 
/*      */     
/*      */     protected abstract Set<Map.Entry<K, V>> createEntrySet();
/*      */ 
/*      */     
/*      */     protected abstract K getFromKey();
/*      */ 
/*      */     
/*      */     protected abstract boolean isFromInclusive();
/*      */ 
/*      */     
/*      */     protected abstract K getToKey();
/*      */ 
/*      */     
/*      */     protected abstract boolean isToInclusive();
/*      */ 
/*      */     
/*      */     protected abstract SortedMap<K, V> createRangeMap(K param1K1, boolean param1Boolean1, K param1K2, boolean param1Boolean2);
/*      */   }
/*      */ 
/*      */   
/*      */   private class RangeEntryMap
/*      */     extends RangeMap
/*      */   {
/*      */     private final K fromKey;
/*      */     private final K toKey;
/*      */     private final boolean fromInclusive;
/*      */     private final boolean toInclusive;
/*      */     
/*      */     protected RangeEntryMap(K fromKey, K toKey) {
/* 1840 */       this(fromKey, true, toKey, false);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected RangeEntryMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1849 */       if (fromKey == null && toKey == null) {
/* 1850 */         throw new IllegalArgumentException("must have a from or to!");
/*      */       }
/*      */       
/* 1853 */       if (fromKey != null && toKey != null && AbstractPatriciaTrie.this.getKeyAnalyzer().compare(fromKey, toKey) > 0) {
/* 1854 */         throw new IllegalArgumentException("fromKey > toKey");
/*      */       }
/*      */       
/* 1857 */       this.fromKey = fromKey;
/* 1858 */       this.fromInclusive = fromInclusive;
/* 1859 */       this.toKey = toKey;
/* 1860 */       this.toInclusive = toInclusive;
/*      */     }
/*      */     
/*      */     public K firstKey() {
/* 1864 */       Map.Entry<K, V> e = null;
/* 1865 */       if (this.fromKey == null) {
/* 1866 */         e = AbstractPatriciaTrie.this.firstEntry();
/*      */       }
/* 1868 */       else if (this.fromInclusive) {
/* 1869 */         e = AbstractPatriciaTrie.this.ceilingEntry(this.fromKey);
/*      */       } else {
/* 1871 */         e = AbstractPatriciaTrie.this.higherEntry(this.fromKey);
/*      */       } 
/*      */ 
/*      */       
/* 1875 */       K first = (e != null) ? e.getKey() : null;
/* 1876 */       if (e == null || (this.toKey != null && !inToRange(first, false))) {
/* 1877 */         throw new NoSuchElementException();
/*      */       }
/* 1879 */       return first;
/*      */     }
/*      */     
/*      */     public K lastKey() {
/*      */       Map.Entry<K, V> e;
/* 1884 */       if (this.toKey == null) {
/* 1885 */         e = AbstractPatriciaTrie.this.lastEntry();
/*      */       }
/* 1887 */       else if (this.toInclusive) {
/* 1888 */         e = AbstractPatriciaTrie.this.floorEntry(this.toKey);
/*      */       } else {
/* 1890 */         e = AbstractPatriciaTrie.this.lowerEntry(this.toKey);
/*      */       } 
/*      */ 
/*      */       
/* 1894 */       K last = (e != null) ? e.getKey() : null;
/* 1895 */       if (e == null || (this.fromKey != null && !inFromRange(last, false))) {
/* 1896 */         throw new NoSuchElementException();
/*      */       }
/* 1898 */       return last;
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 1903 */       return new AbstractPatriciaTrie.RangeEntrySet(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public K getFromKey() {
/* 1908 */       return this.fromKey;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getToKey() {
/* 1913 */       return this.toKey;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isFromInclusive() {
/* 1918 */       return this.fromInclusive;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isToInclusive() {
/* 1923 */       return this.toInclusive;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected SortedMap<K, V> createRangeMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 1929 */       return new RangeEntryMap(fromKey, fromInclusive, toKey, toInclusive);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class RangeEntrySet
/*      */     extends AbstractSet<Map.Entry<K, V>>
/*      */   {
/*      */     private final AbstractPatriciaTrie<K, V>.RangeMap delegate;
/*      */     
/* 1940 */     private transient int size = -1;
/*      */ 
/*      */     
/*      */     private transient int expectedModCount;
/*      */ 
/*      */ 
/*      */     
/*      */     public RangeEntrySet(AbstractPatriciaTrie<K, V>.RangeMap delegate) {
/* 1948 */       if (delegate == null) {
/* 1949 */         throw new NullPointerException("delegate");
/*      */       }
/*      */       
/* 1952 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 1957 */       K fromKey = this.delegate.getFromKey();
/* 1958 */       K toKey = this.delegate.getToKey();
/*      */       
/* 1960 */       AbstractPatriciaTrie.TrieEntry<K, V> first = null;
/* 1961 */       if (fromKey == null) {
/* 1962 */         first = AbstractPatriciaTrie.this.firstEntry();
/*      */       } else {
/* 1964 */         first = AbstractPatriciaTrie.this.ceilingEntry(fromKey);
/*      */       } 
/*      */       
/* 1967 */       AbstractPatriciaTrie.TrieEntry<K, V> last = null;
/* 1968 */       if (toKey != null) {
/* 1969 */         last = AbstractPatriciaTrie.this.ceilingEntry(toKey);
/*      */       }
/*      */       
/* 1972 */       return new EntryIterator(first, last);
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1977 */       if (this.size == -1 || this.expectedModCount != AbstractPatriciaTrie.this.modCount) {
/* 1978 */         this.size = 0;
/*      */         
/* 1980 */         for (Iterator<?> it = iterator(); it.hasNext(); it.next()) {
/* 1981 */           this.size++;
/*      */         }
/*      */         
/* 1984 */         this.expectedModCount = AbstractPatriciaTrie.this.modCount;
/*      */       } 
/* 1986 */       return this.size;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 1991 */       return !iterator().hasNext();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(Object o) {
/* 1997 */       if (!(o instanceof Map.Entry)) {
/* 1998 */         return false;
/*      */       }
/*      */       
/* 2001 */       Map.Entry<K, V> entry = (Map.Entry<K, V>)o;
/* 2002 */       K key = entry.getKey();
/* 2003 */       if (!this.delegate.inRange(key)) {
/* 2004 */         return false;
/*      */       }
/*      */       
/* 2007 */       AbstractPatriciaTrie.TrieEntry<K, V> node = AbstractPatriciaTrie.this.getEntry(key);
/* 2008 */       return (node != null && AbstractBitwiseTrie.compare(node.getValue(), entry.getValue()));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 2014 */       if (!(o instanceof Map.Entry)) {
/* 2015 */         return false;
/*      */       }
/*      */       
/* 2018 */       Map.Entry<K, V> entry = (Map.Entry<K, V>)o;
/* 2019 */       K key = entry.getKey();
/* 2020 */       if (!this.delegate.inRange(key)) {
/* 2021 */         return false;
/*      */       }
/*      */       
/* 2024 */       AbstractPatriciaTrie.TrieEntry<K, V> node = AbstractPatriciaTrie.this.getEntry(key);
/* 2025 */       if (node != null && AbstractBitwiseTrie.compare(node.getValue(), entry.getValue())) {
/* 2026 */         AbstractPatriciaTrie.this.removeEntry(node);
/* 2027 */         return true;
/*      */       } 
/* 2029 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final class EntryIterator
/*      */       extends AbstractPatriciaTrie<K, V>.TrieIterator<Map.Entry<K, V>>
/*      */     {
/*      */       private final K excludedKey;
/*      */ 
/*      */ 
/*      */       
/*      */       private EntryIterator(AbstractPatriciaTrie.TrieEntry<K, V> first, AbstractPatriciaTrie.TrieEntry<K, V> last) {
/* 2043 */         super(first);
/* 2044 */         this.excludedKey = (last != null) ? last.getKey() : null;
/*      */       }
/*      */ 
/*      */       
/*      */       public boolean hasNext() {
/* 2049 */         return (this.next != null && !AbstractBitwiseTrie.compare(this.next.key, this.excludedKey));
/*      */       }
/*      */       
/*      */       public Map.Entry<K, V> next() {
/* 2053 */         if (this.next == null || AbstractBitwiseTrie.compare(this.next.key, this.excludedKey)) {
/* 2054 */           throw new NoSuchElementException();
/*      */         }
/* 2056 */         return nextEntry();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private class PrefixRangeMap
/*      */     extends RangeMap
/*      */   {
/*      */     private final K prefix;
/*      */     
/*      */     private final int offsetInBits;
/*      */     
/*      */     private final int lengthInBits;
/*      */     
/* 2072 */     private K fromKey = null;
/*      */     
/* 2074 */     private K toKey = null;
/*      */     
/* 2076 */     private transient int expectedModCount = 0;
/*      */     
/* 2078 */     private int size = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private PrefixRangeMap(K prefix, int offsetInBits, int lengthInBits) {
/* 2084 */       this.prefix = prefix;
/* 2085 */       this.offsetInBits = offsetInBits;
/* 2086 */       this.lengthInBits = lengthInBits;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int fixup() {
/* 2097 */       if (this.size == -1 || AbstractPatriciaTrie.this.modCount != this.expectedModCount) {
/* 2098 */         Iterator<Map.Entry<K, V>> it = entrySet().iterator();
/* 2099 */         this.size = 0;
/*      */         
/* 2101 */         Map.Entry<K, V> entry = null;
/* 2102 */         if (it.hasNext()) {
/* 2103 */           entry = it.next();
/* 2104 */           this.size = 1;
/*      */         } 
/*      */         
/* 2107 */         this.fromKey = (entry == null) ? null : entry.getKey();
/* 2108 */         if (this.fromKey != null) {
/* 2109 */           AbstractPatriciaTrie.TrieEntry<K, V> prior = AbstractPatriciaTrie.this.previousEntry((AbstractPatriciaTrie.TrieEntry<K, V>)entry);
/* 2110 */           this.fromKey = (prior == null) ? null : prior.getKey();
/*      */         } 
/*      */         
/* 2113 */         this.toKey = this.fromKey;
/*      */         
/* 2115 */         while (it.hasNext()) {
/* 2116 */           this.size++;
/* 2117 */           entry = it.next();
/*      */         } 
/*      */         
/* 2120 */         this.toKey = (entry == null) ? null : entry.getKey();
/*      */         
/* 2122 */         if (this.toKey != null) {
/* 2123 */           entry = AbstractPatriciaTrie.this.nextEntry((AbstractPatriciaTrie.TrieEntry<K, V>)entry);
/* 2124 */           this.toKey = (entry == null) ? null : entry.getKey();
/*      */         } 
/*      */         
/* 2127 */         this.expectedModCount = AbstractPatriciaTrie.this.modCount;
/*      */       } 
/*      */       
/* 2130 */       return this.size;
/*      */     }
/*      */     
/*      */     public K firstKey() {
/* 2134 */       fixup();
/*      */       
/* 2136 */       Map.Entry<K, V> e = null;
/* 2137 */       if (this.fromKey == null) {
/* 2138 */         e = AbstractPatriciaTrie.this.firstEntry();
/*      */       } else {
/* 2140 */         e = AbstractPatriciaTrie.this.higherEntry(this.fromKey);
/*      */       } 
/*      */       
/* 2143 */       K first = (e != null) ? e.getKey() : null;
/* 2144 */       if (e == null || !AbstractPatriciaTrie.this.getKeyAnalyzer().isPrefix(this.prefix, this.offsetInBits, this.lengthInBits, first)) {
/* 2145 */         throw new NoSuchElementException();
/*      */       }
/*      */       
/* 2148 */       return first;
/*      */     }
/*      */     
/*      */     public K lastKey() {
/* 2152 */       fixup();
/*      */       
/* 2154 */       Map.Entry<K, V> e = null;
/* 2155 */       if (this.toKey == null) {
/* 2156 */         e = AbstractPatriciaTrie.this.lastEntry();
/*      */       } else {
/* 2158 */         e = AbstractPatriciaTrie.this.lowerEntry(this.toKey);
/*      */       } 
/*      */       
/* 2161 */       K last = (e != null) ? e.getKey() : null;
/* 2162 */       if (e == null || !AbstractPatriciaTrie.this.getKeyAnalyzer().isPrefix(this.prefix, this.offsetInBits, this.lengthInBits, last)) {
/* 2163 */         throw new NoSuchElementException();
/*      */       }
/*      */       
/* 2166 */       return last;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean inRange(K key) {
/* 2174 */       return AbstractPatriciaTrie.this.getKeyAnalyzer().isPrefix(this.prefix, this.offsetInBits, this.lengthInBits, key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean inRange2(K key) {
/* 2182 */       return inRange(key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean inFromRange(K key, boolean forceInclusive) {
/* 2190 */       return AbstractPatriciaTrie.this.getKeyAnalyzer().isPrefix(this.prefix, this.offsetInBits, this.lengthInBits, key);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected boolean inToRange(K key, boolean forceInclusive) {
/* 2198 */       return AbstractPatriciaTrie.this.getKeyAnalyzer().isPrefix(this.prefix, this.offsetInBits, this.lengthInBits, key);
/*      */     }
/*      */ 
/*      */     
/*      */     protected Set<Map.Entry<K, V>> createEntrySet() {
/* 2203 */       return new AbstractPatriciaTrie.PrefixRangeEntrySet(this);
/*      */     }
/*      */ 
/*      */     
/*      */     public K getFromKey() {
/* 2208 */       return this.fromKey;
/*      */     }
/*      */ 
/*      */     
/*      */     public K getToKey() {
/* 2213 */       return this.toKey;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isFromInclusive() {
/* 2218 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isToInclusive() {
/* 2223 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     protected SortedMap<K, V> createRangeMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
/* 2229 */       return new AbstractPatriciaTrie.RangeEntryMap(fromKey, fromInclusive, toKey, toInclusive);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final class PrefixRangeEntrySet
/*      */     extends RangeEntrySet
/*      */   {
/*      */     private final AbstractPatriciaTrie<K, V>.PrefixRangeMap delegate;
/*      */     
/*      */     private AbstractPatriciaTrie.TrieEntry<K, V> prefixStart;
/*      */     
/* 2242 */     private int expectedModCount = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PrefixRangeEntrySet(AbstractPatriciaTrie<K, V>.PrefixRangeMap delegate) {
/* 2248 */       super(delegate);
/* 2249 */       this.delegate = delegate;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 2254 */       return this.delegate.fixup();
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 2259 */       if (AbstractPatriciaTrie.this.modCount != this.expectedModCount) {
/* 2260 */         this.prefixStart = AbstractPatriciaTrie.this.subtree(this.delegate.prefix, this.delegate.offsetInBits, this.delegate.lengthInBits);
/* 2261 */         this.expectedModCount = AbstractPatriciaTrie.this.modCount;
/*      */       } 
/*      */       
/* 2264 */       if (this.prefixStart == null) {
/* 2265 */         Set<Map.Entry<K, V>> empty = Collections.emptySet();
/* 2266 */         return empty.iterator();
/* 2267 */       }  if (this.delegate.lengthInBits > this.prefixStart.bitIndex) {
/* 2268 */         return new SingletonIterator(this.prefixStart);
/*      */       }
/* 2270 */       return new EntryIterator(this.prefixStart, this.delegate.prefix, this.delegate.offsetInBits, this.delegate.lengthInBits);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private final class SingletonIterator
/*      */       implements Iterator<Map.Entry<K, V>>
/*      */     {
/*      */       private final AbstractPatriciaTrie.TrieEntry<K, V> entry;
/*      */ 
/*      */       
/* 2281 */       private int hit = 0;
/*      */       
/*      */       public SingletonIterator(AbstractPatriciaTrie.TrieEntry<K, V> entry) {
/* 2284 */         this.entry = entry;
/*      */       }
/*      */       
/*      */       public boolean hasNext() {
/* 2288 */         return (this.hit == 0);
/*      */       }
/*      */       
/*      */       public Map.Entry<K, V> next() {
/* 2292 */         if (this.hit != 0) {
/* 2293 */           throw new NoSuchElementException();
/*      */         }
/*      */         
/* 2296 */         this.hit++;
/* 2297 */         return this.entry;
/*      */       }
/*      */       
/*      */       public void remove() {
/* 2301 */         if (this.hit != 1) {
/* 2302 */           throw new IllegalStateException();
/*      */         }
/*      */         
/* 2305 */         this.hit++;
/* 2306 */         AbstractPatriciaTrie.this.removeEntry(this.entry);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private final class EntryIterator
/*      */       extends AbstractPatriciaTrie<K, V>.TrieIterator<Map.Entry<K, V>>
/*      */     {
/*      */       private final K prefix;
/*      */ 
/*      */       
/*      */       private final int offset;
/*      */ 
/*      */       
/*      */       private final int lengthInBits;
/*      */       
/*      */       private boolean lastOne;
/*      */       
/*      */       private AbstractPatriciaTrie.TrieEntry<K, V> subtree;
/*      */ 
/*      */       
/*      */       EntryIterator(AbstractPatriciaTrie.TrieEntry<K, V> startScan, K prefix, int offset, int lengthInBits) {
/* 2329 */         this.subtree = startScan;
/* 2330 */         this.next = AbstractPatriciaTrie.this.followLeft(startScan);
/* 2331 */         this.prefix = prefix;
/* 2332 */         this.offset = offset;
/* 2333 */         this.lengthInBits = lengthInBits;
/*      */       }
/*      */       
/*      */       public Map.Entry<K, V> next() {
/* 2337 */         Map.Entry<K, V> entry = nextEntry();
/* 2338 */         if (this.lastOne) {
/* 2339 */           this.next = null;
/*      */         }
/* 2341 */         return entry;
/*      */       }
/*      */ 
/*      */       
/*      */       protected AbstractPatriciaTrie.TrieEntry<K, V> findNext(AbstractPatriciaTrie.TrieEntry<K, V> prior) {
/* 2346 */         return AbstractPatriciaTrie.this.nextEntryInSubtree(prior, this.subtree);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void remove() {
/* 2353 */         boolean needsFixing = false;
/* 2354 */         int bitIdx = this.subtree.bitIndex;
/* 2355 */         if (this.current == this.subtree) {
/* 2356 */           needsFixing = true;
/*      */         }
/*      */         
/* 2359 */         super.remove();
/*      */ 
/*      */ 
/*      */         
/* 2363 */         if (bitIdx != this.subtree.bitIndex || needsFixing) {
/* 2364 */           this.subtree = AbstractPatriciaTrie.this.subtree(this.prefix, this.offset, this.lengthInBits);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2370 */         if (this.lengthInBits >= this.subtree.bitIndex) {
/* 2371 */           this.lastOne = true;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 2384 */     stream.defaultReadObject();
/* 2385 */     this.root = new TrieEntry<K, V>(null, null, -1);
/* 2386 */     int size = stream.readInt();
/* 2387 */     for (int i = 0; i < size; i++) {
/* 2388 */       K k = (K)stream.readObject();
/* 2389 */       V v = (V)stream.readObject();
/* 2390 */       put(k, v);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 2398 */     stream.defaultWriteObject();
/* 2399 */     stream.writeInt(size());
/* 2400 */     for (Map.Entry<K, V> entry : entrySet()) {
/* 2401 */       stream.writeObject(entry.getKey());
/* 2402 */       stream.writeObject(entry.getValue());
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\trie\AbstractPatriciaTrie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */