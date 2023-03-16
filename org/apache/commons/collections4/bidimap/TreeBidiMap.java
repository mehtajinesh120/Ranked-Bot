/*      */ package org.apache.commons.collections4.bidimap;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.AbstractSet;
/*      */ import java.util.Collection;
/*      */ import java.util.ConcurrentModificationException;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ import org.apache.commons.collections4.BidiMap;
/*      */ import org.apache.commons.collections4.KeyValue;
/*      */ import org.apache.commons.collections4.MapIterator;
/*      */ import org.apache.commons.collections4.OrderedBidiMap;
/*      */ import org.apache.commons.collections4.OrderedIterator;
/*      */ import org.apache.commons.collections4.OrderedMapIterator;
/*      */ import org.apache.commons.collections4.iterators.EmptyOrderedMapIterator;
/*      */ import org.apache.commons.collections4.keyvalue.UnmodifiableMapEntry;
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
/*      */ public class TreeBidiMap<K extends Comparable<K>, V extends Comparable<V>>
/*      */   implements OrderedBidiMap<K, V>, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 721969328361807L;
/*      */   private transient Node<K, V>[] rootNode;
/*      */   
/*      */   enum DataElement
/*      */   {
/*   82 */     KEY("key"), VALUE("value");
/*      */ 
/*      */ 
/*      */     
/*      */     private final String description;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     DataElement(String description) {
/*   92 */       this.description = description;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*   97 */       return this.description;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  104 */   private transient int nodeCount = 0;
/*  105 */   private transient int modifications = 0;
/*      */   private transient Set<K> keySet;
/*      */   private transient Set<V> valuesSet;
/*      */   private transient Set<Map.Entry<K, V>> entrySet;
/*  109 */   private transient Inverse inverse = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public TreeBidiMap() {
/*  118 */     this.rootNode = (Node<K, V>[])new Node[2];
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
/*      */   public TreeBidiMap(Map<? extends K, ? extends V> map) {
/*  130 */     this();
/*  131 */     putAll(map);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  142 */     return this.nodeCount;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEmpty() {
/*  152 */     return (this.nodeCount == 0);
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
/*      */   public boolean containsKey(Object key) {
/*  167 */     checkKey(key);
/*  168 */     return (lookupKey(key) != null);
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
/*      */   public boolean containsValue(Object value) {
/*  183 */     checkValue(value);
/*  184 */     return (lookupValue(value) != null);
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
/*      */   public V get(Object key) {
/*  201 */     checkKey(key);
/*  202 */     Node<K, V> node = lookupKey(key);
/*  203 */     return (node == null) ? null : node.getValue();
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
/*      */   public V put(K key, V value) {
/*  232 */     V result = get(key);
/*  233 */     doPut(key, value);
/*  234 */     return result;
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
/*      */   public void putAll(Map<? extends K, ? extends V> map) {
/*  246 */     for (Map.Entry<? extends K, ? extends V> e : map.entrySet()) {
/*  247 */       put(e.getKey(), e.getValue());
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
/*      */ 
/*      */   
/*      */   public V remove(Object key) {
/*  264 */     return doRemoveKey(key);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clear() {
/*  272 */     modify();
/*      */     
/*  274 */     this.nodeCount = 0;
/*  275 */     this.rootNode[DataElement.KEY.ordinal()] = null;
/*  276 */     this.rootNode[DataElement.VALUE.ordinal()] = null;
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
/*      */   public K getKey(Object value) {
/*  294 */     checkValue(value);
/*  295 */     Node<K, V> node = lookupValue(value);
/*  296 */     return (node == null) ? null : node.getKey();
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
/*      */   public K removeValue(Object value) {
/*  312 */     return doRemoveValue(value);
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
/*      */   public K firstKey() {
/*  324 */     if (this.nodeCount == 0) {
/*  325 */       throw new NoSuchElementException("Map is empty");
/*      */     }
/*  327 */     return leastNode(this.rootNode[DataElement.KEY.ordinal()], DataElement.KEY).getKey();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public K lastKey() {
/*  338 */     if (this.nodeCount == 0) {
/*  339 */       throw new NoSuchElementException("Map is empty");
/*      */     }
/*  341 */     return greatestNode(this.rootNode[DataElement.KEY.ordinal()], DataElement.KEY).getKey();
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
/*      */   public K nextKey(K key) {
/*  354 */     checkKey(key);
/*  355 */     Node<K, V> node = nextGreater(lookupKey(key), DataElement.KEY);
/*  356 */     return (node == null) ? null : node.getKey();
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
/*      */   public K previousKey(K key) {
/*  369 */     checkKey(key);
/*  370 */     Node<K, V> node = nextSmaller(lookupKey(key), DataElement.KEY);
/*  371 */     return (node == null) ? null : node.getKey();
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
/*      */   public Set<K> keySet() {
/*  389 */     if (this.keySet == null) {
/*  390 */       this.keySet = new KeyView(DataElement.KEY);
/*      */     }
/*  392 */     return this.keySet;
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
/*      */   public Set<V> values() {
/*  411 */     if (this.valuesSet == null) {
/*  412 */       this.valuesSet = new ValueView(DataElement.KEY);
/*      */     }
/*  414 */     return this.valuesSet;
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
/*      */   public Set<Map.Entry<K, V>> entrySet() {
/*  434 */     if (this.entrySet == null) {
/*  435 */       this.entrySet = new EntryView();
/*      */     }
/*  437 */     return this.entrySet;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public OrderedMapIterator<K, V> mapIterator() {
/*  443 */     if (isEmpty()) {
/*  444 */       return EmptyOrderedMapIterator.emptyOrderedMapIterator();
/*      */     }
/*  446 */     return new ViewMapIterator(DataElement.KEY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public OrderedBidiMap<V, K> inverseBidiMap() {
/*  457 */     if (this.inverse == null) {
/*  458 */       this.inverse = new Inverse();
/*      */     }
/*  460 */     return this.inverse;
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
/*      */   public boolean equals(Object obj) {
/*  472 */     return doEquals(obj, DataElement.KEY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  482 */     return doHashCode(DataElement.KEY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  492 */     return doToString(DataElement.KEY);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void doPut(K key, V value) {
/*  503 */     checkKeyAndValue(key, value);
/*      */ 
/*      */     
/*  506 */     doRemoveKey(key);
/*  507 */     doRemoveValue(value);
/*      */     
/*  509 */     Node<K, V> node = this.rootNode[DataElement.KEY.ordinal()];
/*  510 */     if (node == null) {
/*      */       
/*  512 */       Node<K, V> root = new Node<K, V>(key, value);
/*  513 */       this.rootNode[DataElement.KEY.ordinal()] = root;
/*  514 */       this.rootNode[DataElement.VALUE.ordinal()] = root;
/*  515 */       grow();
/*      */     } else {
/*      */       
/*      */       while (true) {
/*      */         
/*  520 */         int cmp = compare(key, node.getKey());
/*      */         
/*  522 */         if (cmp == 0)
/*      */         {
/*  524 */           throw new IllegalArgumentException("Cannot store a duplicate key (\"" + key + "\") in this Map"); } 
/*  525 */         if (cmp < 0) {
/*  526 */           if (node.getLeft(DataElement.KEY) != null) {
/*  527 */             node = node.getLeft(DataElement.KEY); continue;
/*      */           } 
/*  529 */           Node<K, V> node1 = new Node<K, V>(key, value);
/*      */           
/*  531 */           insertValue(node1);
/*  532 */           node.setLeft(node1, DataElement.KEY);
/*  533 */           node1.setParent(node, DataElement.KEY);
/*  534 */           doRedBlackInsert(node1, DataElement.KEY);
/*  535 */           grow();
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/*  540 */         if (node.getRight(DataElement.KEY) != null) {
/*  541 */           node = node.getRight(DataElement.KEY); continue;
/*      */         } 
/*  543 */         Node<K, V> newNode = new Node<K, V>(key, value);
/*      */         
/*  545 */         insertValue(newNode);
/*  546 */         node.setRight(newNode, DataElement.KEY);
/*  547 */         newNode.setParent(node, DataElement.KEY);
/*  548 */         doRedBlackInsert(newNode, DataElement.KEY);
/*  549 */         grow();
/*      */         break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private V doRemoveKey(Object key) {
/*  559 */     Node<K, V> node = lookupKey(key);
/*  560 */     if (node == null) {
/*  561 */       return null;
/*      */     }
/*  563 */     doRedBlackDelete(node);
/*  564 */     return node.getValue();
/*      */   }
/*      */   
/*      */   private K doRemoveValue(Object value) {
/*  568 */     Node<K, V> node = lookupValue(value);
/*  569 */     if (node == null) {
/*  570 */       return null;
/*      */     }
/*  572 */     doRedBlackDelete(node);
/*  573 */     return node.getKey();
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
/*      */   private <T extends Comparable<T>> Node<K, V> lookup(Object data, DataElement dataElement) {
/*  586 */     Node<K, V> rval = null;
/*  587 */     Node<K, V> node = this.rootNode[dataElement.ordinal()];
/*      */     
/*  589 */     while (node != null) {
/*  590 */       int cmp = compare((Comparable)data, (Comparable)node.getData(dataElement));
/*  591 */       if (cmp == 0) {
/*  592 */         rval = node;
/*      */         break;
/*      */       } 
/*  595 */       node = (cmp < 0) ? node.getLeft(dataElement) : node.getRight(dataElement);
/*      */     } 
/*      */ 
/*      */     
/*  599 */     return rval;
/*      */   }
/*      */   
/*      */   private Node<K, V> lookupKey(Object key) {
/*  603 */     return lookup(key, DataElement.KEY);
/*      */   }
/*      */   
/*      */   private Node<K, V> lookupValue(Object value) {
/*  607 */     return lookup(value, DataElement.VALUE);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Node<K, V> nextGreater(Node<K, V> node, DataElement dataElement) {
/*      */     Node<K, V> rval;
/*  619 */     if (node == null) {
/*  620 */       rval = null;
/*  621 */     } else if (node.getRight(dataElement) != null) {
/*      */ 
/*      */       
/*  624 */       rval = leastNode(node.getRight(dataElement), dataElement);
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */       
/*  632 */       Node<K, V> parent = node.getParent(dataElement);
/*  633 */       Node<K, V> child = node;
/*      */       
/*  635 */       while (parent != null && child == parent.getRight(dataElement)) {
/*  636 */         child = parent;
/*  637 */         parent = parent.getParent(dataElement);
/*      */       } 
/*  639 */       rval = parent;
/*      */     } 
/*  641 */     return rval;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Node<K, V> nextSmaller(Node<K, V> node, DataElement dataElement) {
/*      */     Node<K, V> rval;
/*  653 */     if (node == null) {
/*  654 */       rval = null;
/*  655 */     } else if (node.getLeft(dataElement) != null) {
/*      */ 
/*      */       
/*  658 */       rval = greatestNode(node.getLeft(dataElement), dataElement);
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */       
/*  666 */       Node<K, V> parent = node.getParent(dataElement);
/*  667 */       Node<K, V> child = node;
/*      */       
/*  669 */       while (parent != null && child == parent.getLeft(dataElement)) {
/*  670 */         child = parent;
/*  671 */         parent = parent.getParent(dataElement);
/*      */       } 
/*  673 */       rval = parent;
/*      */     } 
/*  675 */     return rval;
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
/*      */   private static <T extends Comparable<T>> int compare(T o1, T o2) {
/*  690 */     return o1.compareTo(o2);
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
/*      */   private Node<K, V> leastNode(Node<K, V> node, DataElement dataElement) {
/*  702 */     Node<K, V> rval = node;
/*  703 */     if (rval != null) {
/*  704 */       while (rval.getLeft(dataElement) != null) {
/*  705 */         rval = rval.getLeft(dataElement);
/*      */       }
/*      */     }
/*  708 */     return rval;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Node<K, V> greatestNode(Node<K, V> node, DataElement dataElement) {
/*  719 */     Node<K, V> rval = node;
/*  720 */     if (rval != null) {
/*  721 */       while (rval.getRight(dataElement) != null) {
/*  722 */         rval = rval.getRight(dataElement);
/*      */       }
/*      */     }
/*  725 */     return rval;
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
/*      */   private void copyColor(Node<K, V> from, Node<K, V> to, DataElement dataElement) {
/*  737 */     if (to != null) {
/*  738 */       if (from == null) {
/*      */         
/*  740 */         to.setBlack(dataElement);
/*      */       } else {
/*  742 */         to.copyColor(from, dataElement);
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
/*      */   
/*      */   private static boolean isRed(Node<?, ?> node, DataElement dataElement) {
/*  755 */     return (node != null && node.isRed(dataElement));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean isBlack(Node<?, ?> node, DataElement dataElement) {
/*  766 */     return (node == null || node.isBlack(dataElement));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void makeRed(Node<?, ?> node, DataElement dataElement) {
/*  776 */     if (node != null) {
/*  777 */       node.setRed(dataElement);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void makeBlack(Node<?, ?> node, DataElement dataElement) {
/*  788 */     if (node != null) {
/*  789 */       node.setBlack(dataElement);
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
/*      */   private Node<K, V> getGrandParent(Node<K, V> node, DataElement dataElement) {
/*  801 */     return getParent(getParent(node, dataElement), dataElement);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Node<K, V> getParent(Node<K, V> node, DataElement dataElement) {
/*  812 */     return (node == null) ? null : node.getParent(dataElement);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Node<K, V> getRightChild(Node<K, V> node, DataElement dataElement) {
/*  823 */     return (node == null) ? null : node.getRight(dataElement);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Node<K, V> getLeftChild(Node<K, V> node, DataElement dataElement) {
/*  834 */     return (node == null) ? null : node.getLeft(dataElement);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void rotateLeft(Node<K, V> node, DataElement dataElement) {
/*  844 */     Node<K, V> rightChild = node.getRight(dataElement);
/*  845 */     node.setRight(rightChild.getLeft(dataElement), dataElement);
/*      */     
/*  847 */     if (rightChild.getLeft(dataElement) != null) {
/*  848 */       rightChild.getLeft(dataElement).setParent(node, dataElement);
/*      */     }
/*  850 */     rightChild.setParent(node.getParent(dataElement), dataElement);
/*      */     
/*  852 */     if (node.getParent(dataElement) == null) {
/*      */       
/*  854 */       this.rootNode[dataElement.ordinal()] = rightChild;
/*  855 */     } else if (node.getParent(dataElement).getLeft(dataElement) == node) {
/*  856 */       node.getParent(dataElement).setLeft(rightChild, dataElement);
/*      */     } else {
/*  858 */       node.getParent(dataElement).setRight(rightChild, dataElement);
/*      */     } 
/*      */     
/*  861 */     rightChild.setLeft(node, dataElement);
/*  862 */     node.setParent(rightChild, dataElement);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void rotateRight(Node<K, V> node, DataElement dataElement) {
/*  872 */     Node<K, V> leftChild = node.getLeft(dataElement);
/*  873 */     node.setLeft(leftChild.getRight(dataElement), dataElement);
/*  874 */     if (leftChild.getRight(dataElement) != null) {
/*  875 */       leftChild.getRight(dataElement).setParent(node, dataElement);
/*      */     }
/*  877 */     leftChild.setParent(node.getParent(dataElement), dataElement);
/*      */     
/*  879 */     if (node.getParent(dataElement) == null) {
/*      */       
/*  881 */       this.rootNode[dataElement.ordinal()] = leftChild;
/*  882 */     } else if (node.getParent(dataElement).getRight(dataElement) == node) {
/*  883 */       node.getParent(dataElement).setRight(leftChild, dataElement);
/*      */     } else {
/*  885 */       node.getParent(dataElement).setLeft(leftChild, dataElement);
/*      */     } 
/*      */     
/*  888 */     leftChild.setRight(node, dataElement);
/*  889 */     node.setParent(leftChild, dataElement);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void doRedBlackInsert(Node<K, V> insertedNode, DataElement dataElement) {
/*  900 */     Node<K, V> currentNode = insertedNode;
/*  901 */     makeRed(currentNode, dataElement);
/*      */     
/*  903 */     while (currentNode != null && currentNode != this.rootNode[dataElement.ordinal()] && isRed(currentNode.getParent(dataElement), dataElement)) {
/*      */ 
/*      */       
/*  906 */       if (currentNode.isLeftChild(dataElement)) {
/*  907 */         Node<K, V> node = getRightChild(getGrandParent(currentNode, dataElement), dataElement);
/*      */         
/*  909 */         if (isRed(node, dataElement)) {
/*  910 */           makeBlack(getParent(currentNode, dataElement), dataElement);
/*  911 */           makeBlack(node, dataElement);
/*  912 */           makeRed(getGrandParent(currentNode, dataElement), dataElement);
/*      */           
/*  914 */           currentNode = getGrandParent(currentNode, dataElement);
/*      */           continue;
/*      */         } 
/*  917 */         if (currentNode.isRightChild(dataElement)) {
/*  918 */           currentNode = getParent(currentNode, dataElement);
/*      */           
/*  920 */           rotateLeft(currentNode, dataElement);
/*      */         } 
/*      */         
/*  923 */         makeBlack(getParent(currentNode, dataElement), dataElement);
/*  924 */         makeRed(getGrandParent(currentNode, dataElement), dataElement);
/*      */         
/*  926 */         if (getGrandParent(currentNode, dataElement) != null) {
/*  927 */           rotateRight(getGrandParent(currentNode, dataElement), dataElement);
/*      */         }
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/*  933 */       Node<K, V> y = getLeftChild(getGrandParent(currentNode, dataElement), dataElement);
/*      */       
/*  935 */       if (isRed(y, dataElement)) {
/*  936 */         makeBlack(getParent(currentNode, dataElement), dataElement);
/*  937 */         makeBlack(y, dataElement);
/*  938 */         makeRed(getGrandParent(currentNode, dataElement), dataElement);
/*      */         
/*  940 */         currentNode = getGrandParent(currentNode, dataElement);
/*      */         continue;
/*      */       } 
/*  943 */       if (currentNode.isLeftChild(dataElement)) {
/*  944 */         currentNode = getParent(currentNode, dataElement);
/*      */         
/*  946 */         rotateRight(currentNode, dataElement);
/*      */       } 
/*      */       
/*  949 */       makeBlack(getParent(currentNode, dataElement), dataElement);
/*  950 */       makeRed(getGrandParent(currentNode, dataElement), dataElement);
/*      */       
/*  952 */       if (getGrandParent(currentNode, dataElement) != null) {
/*  953 */         rotateLeft(getGrandParent(currentNode, dataElement), dataElement);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  959 */     makeBlack(this.rootNode[dataElement.ordinal()], dataElement);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void doRedBlackDelete(Node<K, V> deletedNode) {
/*  969 */     for (DataElement dataElement : DataElement.values()) {
/*      */ 
/*      */       
/*  972 */       if (deletedNode.getLeft(dataElement) != null && deletedNode.getRight(dataElement) != null) {
/*  973 */         swapPosition(nextGreater(deletedNode, dataElement), deletedNode, dataElement);
/*      */       }
/*      */       
/*  976 */       Node<K, V> replacement = (deletedNode.getLeft(dataElement) != null) ? deletedNode.getLeft(dataElement) : deletedNode.getRight(dataElement);
/*      */ 
/*      */       
/*  979 */       if (replacement != null) {
/*  980 */         replacement.setParent(deletedNode.getParent(dataElement), dataElement);
/*      */         
/*  982 */         if (deletedNode.getParent(dataElement) == null) {
/*  983 */           this.rootNode[dataElement.ordinal()] = replacement;
/*  984 */         } else if (deletedNode == deletedNode.getParent(dataElement).getLeft(dataElement)) {
/*  985 */           deletedNode.getParent(dataElement).setLeft(replacement, dataElement);
/*      */         } else {
/*  987 */           deletedNode.getParent(dataElement).setRight(replacement, dataElement);
/*      */         } 
/*      */         
/*  990 */         deletedNode.setLeft(null, dataElement);
/*  991 */         deletedNode.setRight(null, dataElement);
/*  992 */         deletedNode.setParent(null, dataElement);
/*      */         
/*  994 */         if (isBlack(deletedNode, dataElement)) {
/*  995 */           doRedBlackDeleteFixup(replacement, dataElement);
/*      */         
/*      */         }
/*      */       
/*      */       }
/* 1000 */       else if (deletedNode.getParent(dataElement) == null) {
/*      */ 
/*      */         
/* 1003 */         this.rootNode[dataElement.ordinal()] = null;
/*      */       }
/*      */       else {
/*      */         
/* 1007 */         if (isBlack(deletedNode, dataElement)) {
/* 1008 */           doRedBlackDeleteFixup(deletedNode, dataElement);
/*      */         }
/*      */         
/* 1011 */         if (deletedNode.getParent(dataElement) != null) {
/* 1012 */           if (deletedNode == deletedNode.getParent(dataElement).getLeft(dataElement)) {
/* 1013 */             deletedNode.getParent(dataElement).setLeft(null, dataElement);
/*      */           } else {
/* 1015 */             deletedNode.getParent(dataElement).setRight(null, dataElement);
/*      */           } 
/*      */           
/* 1018 */           deletedNode.setParent(null, dataElement);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1023 */     shrink();
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
/*      */   private void doRedBlackDeleteFixup(Node<K, V> replacementNode, DataElement dataElement) {
/* 1036 */     Node<K, V> currentNode = replacementNode;
/*      */     
/* 1038 */     while (currentNode != this.rootNode[dataElement.ordinal()] && isBlack(currentNode, dataElement)) {
/* 1039 */       if (currentNode.isLeftChild(dataElement)) {
/* 1040 */         Node<K, V> node = getRightChild(getParent(currentNode, dataElement), dataElement);
/*      */         
/* 1042 */         if (isRed(node, dataElement)) {
/* 1043 */           makeBlack(node, dataElement);
/* 1044 */           makeRed(getParent(currentNode, dataElement), dataElement);
/* 1045 */           rotateLeft(getParent(currentNode, dataElement), dataElement);
/*      */           
/* 1047 */           node = getRightChild(getParent(currentNode, dataElement), dataElement);
/*      */         } 
/*      */         
/* 1050 */         if (isBlack(getLeftChild(node, dataElement), dataElement) && isBlack(getRightChild(node, dataElement), dataElement)) {
/*      */           
/* 1052 */           makeRed(node, dataElement);
/*      */           
/* 1054 */           currentNode = getParent(currentNode, dataElement); continue;
/*      */         } 
/* 1056 */         if (isBlack(getRightChild(node, dataElement), dataElement)) {
/* 1057 */           makeBlack(getLeftChild(node, dataElement), dataElement);
/* 1058 */           makeRed(node, dataElement);
/* 1059 */           rotateRight(node, dataElement);
/*      */           
/* 1061 */           node = getRightChild(getParent(currentNode, dataElement), dataElement);
/*      */         } 
/*      */         
/* 1064 */         copyColor(getParent(currentNode, dataElement), node, dataElement);
/* 1065 */         makeBlack(getParent(currentNode, dataElement), dataElement);
/* 1066 */         makeBlack(getRightChild(node, dataElement), dataElement);
/* 1067 */         rotateLeft(getParent(currentNode, dataElement), dataElement);
/*      */         
/* 1069 */         currentNode = this.rootNode[dataElement.ordinal()];
/*      */         continue;
/*      */       } 
/* 1072 */       Node<K, V> siblingNode = getLeftChild(getParent(currentNode, dataElement), dataElement);
/*      */       
/* 1074 */       if (isRed(siblingNode, dataElement)) {
/* 1075 */         makeBlack(siblingNode, dataElement);
/* 1076 */         makeRed(getParent(currentNode, dataElement), dataElement);
/* 1077 */         rotateRight(getParent(currentNode, dataElement), dataElement);
/*      */         
/* 1079 */         siblingNode = getLeftChild(getParent(currentNode, dataElement), dataElement);
/*      */       } 
/*      */       
/* 1082 */       if (isBlack(getRightChild(siblingNode, dataElement), dataElement) && isBlack(getLeftChild(siblingNode, dataElement), dataElement)) {
/*      */         
/* 1084 */         makeRed(siblingNode, dataElement);
/*      */         
/* 1086 */         currentNode = getParent(currentNode, dataElement); continue;
/*      */       } 
/* 1088 */       if (isBlack(getLeftChild(siblingNode, dataElement), dataElement)) {
/* 1089 */         makeBlack(getRightChild(siblingNode, dataElement), dataElement);
/* 1090 */         makeRed(siblingNode, dataElement);
/* 1091 */         rotateLeft(siblingNode, dataElement);
/*      */         
/* 1093 */         siblingNode = getLeftChild(getParent(currentNode, dataElement), dataElement);
/*      */       } 
/*      */       
/* 1096 */       copyColor(getParent(currentNode, dataElement), siblingNode, dataElement);
/* 1097 */       makeBlack(getParent(currentNode, dataElement), dataElement);
/* 1098 */       makeBlack(getLeftChild(siblingNode, dataElement), dataElement);
/* 1099 */       rotateRight(getParent(currentNode, dataElement), dataElement);
/*      */       
/* 1101 */       currentNode = this.rootNode[dataElement.ordinal()];
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1106 */     makeBlack(currentNode, dataElement);
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
/*      */   private void swapPosition(Node<K, V> x, Node<K, V> y, DataElement dataElement) {
/* 1120 */     Node<K, V> xFormerParent = x.getParent(dataElement);
/* 1121 */     Node<K, V> xFormerLeftChild = x.getLeft(dataElement);
/* 1122 */     Node<K, V> xFormerRightChild = x.getRight(dataElement);
/* 1123 */     Node<K, V> yFormerParent = y.getParent(dataElement);
/* 1124 */     Node<K, V> yFormerLeftChild = y.getLeft(dataElement);
/* 1125 */     Node<K, V> yFormerRightChild = y.getRight(dataElement);
/* 1126 */     boolean xWasLeftChild = (x.getParent(dataElement) != null && x == x.getParent(dataElement).getLeft(dataElement));
/*      */     
/* 1128 */     boolean yWasLeftChild = (y.getParent(dataElement) != null && y == y.getParent(dataElement).getLeft(dataElement));
/*      */ 
/*      */ 
/*      */     
/* 1132 */     if (x == yFormerParent) {
/* 1133 */       x.setParent(y, dataElement);
/*      */       
/* 1135 */       if (yWasLeftChild) {
/* 1136 */         y.setLeft(x, dataElement);
/* 1137 */         y.setRight(xFormerRightChild, dataElement);
/*      */       } else {
/* 1139 */         y.setRight(x, dataElement);
/* 1140 */         y.setLeft(xFormerLeftChild, dataElement);
/*      */       } 
/*      */     } else {
/* 1143 */       x.setParent(yFormerParent, dataElement);
/*      */       
/* 1145 */       if (yFormerParent != null) {
/* 1146 */         if (yWasLeftChild) {
/* 1147 */           yFormerParent.setLeft(x, dataElement);
/*      */         } else {
/* 1149 */           yFormerParent.setRight(x, dataElement);
/*      */         } 
/*      */       }
/*      */       
/* 1153 */       y.setLeft(xFormerLeftChild, dataElement);
/* 1154 */       y.setRight(xFormerRightChild, dataElement);
/*      */     } 
/*      */     
/* 1157 */     if (y == xFormerParent) {
/* 1158 */       y.setParent(x, dataElement);
/*      */       
/* 1160 */       if (xWasLeftChild) {
/* 1161 */         x.setLeft(y, dataElement);
/* 1162 */         x.setRight(yFormerRightChild, dataElement);
/*      */       } else {
/* 1164 */         x.setRight(y, dataElement);
/* 1165 */         x.setLeft(yFormerLeftChild, dataElement);
/*      */       } 
/*      */     } else {
/* 1168 */       y.setParent(xFormerParent, dataElement);
/*      */       
/* 1170 */       if (xFormerParent != null) {
/* 1171 */         if (xWasLeftChild) {
/* 1172 */           xFormerParent.setLeft(y, dataElement);
/*      */         } else {
/* 1174 */           xFormerParent.setRight(y, dataElement);
/*      */         } 
/*      */       }
/*      */       
/* 1178 */       x.setLeft(yFormerLeftChild, dataElement);
/* 1179 */       x.setRight(yFormerRightChild, dataElement);
/*      */     } 
/*      */ 
/*      */     
/* 1183 */     if (x.getLeft(dataElement) != null) {
/* 1184 */       x.getLeft(dataElement).setParent(x, dataElement);
/*      */     }
/*      */     
/* 1187 */     if (x.getRight(dataElement) != null) {
/* 1188 */       x.getRight(dataElement).setParent(x, dataElement);
/*      */     }
/*      */     
/* 1191 */     if (y.getLeft(dataElement) != null) {
/* 1192 */       y.getLeft(dataElement).setParent(y, dataElement);
/*      */     }
/*      */     
/* 1195 */     if (y.getRight(dataElement) != null) {
/* 1196 */       y.getRight(dataElement).setParent(y, dataElement);
/*      */     }
/*      */     
/* 1199 */     x.swapColors(y, dataElement);
/*      */ 
/*      */     
/* 1202 */     if (this.rootNode[dataElement.ordinal()] == x) {
/* 1203 */       this.rootNode[dataElement.ordinal()] = y;
/* 1204 */     } else if (this.rootNode[dataElement.ordinal()] == y) {
/* 1205 */       this.rootNode[dataElement.ordinal()] = x;
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
/*      */   
/*      */   private static void checkNonNullComparable(Object o, DataElement dataElement) {
/* 1221 */     if (o == null) {
/* 1222 */       throw new NullPointerException(dataElement + " cannot be null");
/*      */     }
/* 1224 */     if (!(o instanceof Comparable)) {
/* 1225 */       throw new ClassCastException(dataElement + " must be Comparable");
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
/*      */   private static void checkKey(Object key) {
/* 1238 */     checkNonNullComparable(key, DataElement.KEY);
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
/*      */   private static void checkValue(Object value) {
/* 1250 */     checkNonNullComparable(value, DataElement.VALUE);
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
/*      */   private static void checkKeyAndValue(Object key, Object value) {
/* 1264 */     checkKey(key);
/* 1265 */     checkValue(value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void modify() {
/* 1274 */     this.modifications++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void grow() {
/* 1281 */     modify();
/* 1282 */     this.nodeCount++;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void shrink() {
/* 1289 */     modify();
/* 1290 */     this.nodeCount--;
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
/*      */   private void insertValue(Node<K, V> newNode) throws IllegalArgumentException {
/* 1302 */     Node<K, V> node = this.rootNode[DataElement.VALUE.ordinal()];
/*      */     
/*      */     while (true) {
/* 1305 */       int cmp = compare((Comparable)newNode.getValue(), (Comparable)node.getValue());
/*      */       
/* 1307 */       if (cmp == 0) {
/* 1308 */         throw new IllegalArgumentException("Cannot store a duplicate value (\"" + newNode.getData(DataElement.VALUE) + "\") in this Map");
/*      */       }
/* 1310 */       if (cmp < 0) {
/* 1311 */         if (node.getLeft(DataElement.VALUE) != null) {
/* 1312 */           node = node.getLeft(DataElement.VALUE); continue;
/*      */         } 
/* 1314 */         node.setLeft(newNode, DataElement.VALUE);
/* 1315 */         newNode.setParent(node, DataElement.VALUE);
/* 1316 */         doRedBlackInsert(newNode, DataElement.VALUE);
/*      */         
/*      */         break;
/*      */       } 
/*      */       
/* 1321 */       if (node.getRight(DataElement.VALUE) != null) {
/* 1322 */         node = node.getRight(DataElement.VALUE); continue;
/*      */       } 
/* 1324 */       node.setRight(newNode, DataElement.VALUE);
/* 1325 */       newNode.setParent(node, DataElement.VALUE);
/* 1326 */       doRedBlackInsert(newNode, DataElement.VALUE);
/*      */       break;
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
/*      */   
/*      */   private boolean doEquals(Object obj, DataElement dataElement) {
/* 1343 */     if (obj == this) {
/* 1344 */       return true;
/*      */     }
/* 1346 */     if (!(obj instanceof Map)) {
/* 1347 */       return false;
/*      */     }
/* 1349 */     Map<?, ?> other = (Map<?, ?>)obj;
/* 1350 */     if (other.size() != size()) {
/* 1351 */       return false;
/*      */     }
/*      */     
/* 1354 */     if (this.nodeCount > 0) {
/*      */       try {
/* 1356 */         for (MapIterator<?, ?> it = getMapIterator(dataElement); it.hasNext(); ) {
/* 1357 */           Object key = it.next();
/* 1358 */           Object value = it.getValue();
/* 1359 */           if (!value.equals(other.get(key))) {
/* 1360 */             return false;
/*      */           }
/*      */         } 
/* 1363 */       } catch (ClassCastException ex) {
/* 1364 */         return false;
/* 1365 */       } catch (NullPointerException ex) {
/* 1366 */         return false;
/*      */       } 
/*      */     }
/* 1369 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int doHashCode(DataElement dataElement) {
/* 1379 */     int total = 0;
/* 1380 */     if (this.nodeCount > 0) {
/* 1381 */       for (MapIterator<?, ?> it = getMapIterator(dataElement); it.hasNext(); ) {
/* 1382 */         Object key = it.next();
/* 1383 */         Object value = it.getValue();
/* 1384 */         total += key.hashCode() ^ value.hashCode();
/*      */       } 
/*      */     }
/* 1387 */     return total;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String doToString(DataElement dataElement) {
/* 1397 */     if (this.nodeCount == 0) {
/* 1398 */       return "{}";
/*      */     }
/* 1400 */     StringBuilder buf = new StringBuilder(this.nodeCount * 32);
/* 1401 */     buf.append('{');
/* 1402 */     MapIterator<?, ?> it = getMapIterator(dataElement);
/* 1403 */     boolean hasNext = it.hasNext();
/* 1404 */     while (hasNext) {
/* 1405 */       Object key = it.next();
/* 1406 */       Object value = it.getValue();
/* 1407 */       buf.append((key == this) ? "(this Map)" : key).append('=').append((value == this) ? "(this Map)" : value);
/*      */ 
/*      */ 
/*      */       
/* 1411 */       hasNext = it.hasNext();
/* 1412 */       if (hasNext) {
/* 1413 */         buf.append(", ");
/*      */       }
/*      */     } 
/*      */     
/* 1417 */     buf.append('}');
/* 1418 */     return buf.toString();
/*      */   }
/*      */   
/*      */   private MapIterator<?, ?> getMapIterator(DataElement dataElement) {
/* 1422 */     switch (dataElement) {
/*      */       case KEY:
/* 1424 */         return (MapIterator<?, ?>)new ViewMapIterator(DataElement.KEY);
/*      */       case VALUE:
/* 1426 */         return (MapIterator<?, ?>)new InverseViewMapIterator(DataElement.VALUE);
/*      */     } 
/* 1428 */     throw new IllegalArgumentException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 1437 */     stream.defaultReadObject();
/* 1438 */     this.rootNode = (Node<K, V>[])new Node[2];
/* 1439 */     int size = stream.readInt();
/* 1440 */     for (int i = 0; i < size; i++) {
/* 1441 */       Comparable comparable1 = (Comparable)stream.readObject();
/* 1442 */       Comparable comparable2 = (Comparable)stream.readObject();
/* 1443 */       put((K)comparable1, (V)comparable2);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeObject(ObjectOutputStream stream) throws IOException {
/* 1451 */     stream.defaultWriteObject();
/* 1452 */     stream.writeInt(size());
/* 1453 */     for (Map.Entry<K, V> entry : entrySet()) {
/* 1454 */       stream.writeObject(entry.getKey());
/* 1455 */       stream.writeObject(entry.getValue());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract class View<E>
/*      */     extends AbstractSet<E>
/*      */   {
/*      */     final TreeBidiMap.DataElement orderType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     View(TreeBidiMap.DataElement orderType) {
/* 1475 */       this.orderType = orderType;
/*      */     }
/*      */ 
/*      */     
/*      */     public int size() {
/* 1480 */       return TreeBidiMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 1485 */       TreeBidiMap.this.clear();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   class KeyView
/*      */     extends View<K>
/*      */   {
/*      */     public KeyView(TreeBidiMap.DataElement orderType) {
/* 1495 */       super(orderType);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<K> iterator() {
/* 1500 */       return (Iterator<K>)new TreeBidiMap.ViewMapIterator(this.orderType);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object obj) {
/* 1505 */       TreeBidiMap.checkNonNullComparable(obj, TreeBidiMap.DataElement.KEY);
/* 1506 */       return (TreeBidiMap.this.lookupKey(obj) != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 1511 */       return (TreeBidiMap.this.doRemoveKey(o) != null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class ValueView
/*      */     extends View<V>
/*      */   {
/*      */     public ValueView(TreeBidiMap.DataElement orderType) {
/* 1522 */       super(orderType);
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<V> iterator() {
/* 1527 */       return (Iterator<V>)new TreeBidiMap.InverseViewMapIterator(this.orderType);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object obj) {
/* 1532 */       TreeBidiMap.checkNonNullComparable(obj, TreeBidiMap.DataElement.VALUE);
/* 1533 */       return (TreeBidiMap.this.lookupValue(obj) != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object o) {
/* 1538 */       return (TreeBidiMap.this.doRemoveValue(o) != null);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class EntryView
/*      */     extends View<Map.Entry<K, V>>
/*      */   {
/*      */     EntryView() {
/* 1549 */       super(TreeBidiMap.DataElement.KEY);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object obj) {
/* 1554 */       if (!(obj instanceof Map.Entry)) {
/* 1555 */         return false;
/*      */       }
/* 1557 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 1558 */       Object value = entry.getValue();
/* 1559 */       TreeBidiMap.Node<K, V> node = TreeBidiMap.this.lookupKey(entry.getKey());
/* 1560 */       return (node != null && node.getValue().equals(value));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object obj) {
/* 1565 */       if (!(obj instanceof Map.Entry)) {
/* 1566 */         return false;
/*      */       }
/* 1568 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 1569 */       Object value = entry.getValue();
/* 1570 */       TreeBidiMap.Node<K, V> node = TreeBidiMap.this.lookupKey(entry.getKey());
/* 1571 */       if (node != null && node.getValue().equals(value)) {
/* 1572 */         TreeBidiMap.this.doRedBlackDelete(node);
/* 1573 */         return true;
/*      */       } 
/* 1575 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<K, V>> iterator() {
/* 1580 */       return (Iterator<Map.Entry<K, V>>)new TreeBidiMap.ViewMapEntryIterator();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   class InverseEntryView
/*      */     extends View<Map.Entry<V, K>>
/*      */   {
/*      */     InverseEntryView() {
/* 1590 */       super(TreeBidiMap.DataElement.VALUE);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean contains(Object obj) {
/* 1595 */       if (!(obj instanceof Map.Entry)) {
/* 1596 */         return false;
/*      */       }
/* 1598 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 1599 */       Object value = entry.getValue();
/* 1600 */       TreeBidiMap.Node<K, V> node = TreeBidiMap.this.lookupValue(entry.getKey());
/* 1601 */       return (node != null && node.getKey().equals(value));
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean remove(Object obj) {
/* 1606 */       if (!(obj instanceof Map.Entry)) {
/* 1607 */         return false;
/*      */       }
/* 1609 */       Map.Entry<?, ?> entry = (Map.Entry<?, ?>)obj;
/* 1610 */       Object value = entry.getValue();
/* 1611 */       TreeBidiMap.Node<K, V> node = TreeBidiMap.this.lookupValue(entry.getKey());
/* 1612 */       if (node != null && node.getKey().equals(value)) {
/* 1613 */         TreeBidiMap.this.doRedBlackDelete(node);
/* 1614 */         return true;
/*      */       } 
/* 1616 */       return false;
/*      */     }
/*      */ 
/*      */     
/*      */     public Iterator<Map.Entry<V, K>> iterator() {
/* 1621 */       return (Iterator<Map.Entry<V, K>>)new TreeBidiMap.InverseViewMapEntryIterator();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   abstract class ViewIterator
/*      */   {
/*      */     private final TreeBidiMap.DataElement orderType;
/*      */ 
/*      */ 
/*      */     
/*      */     TreeBidiMap.Node<K, V> lastReturnedNode;
/*      */ 
/*      */ 
/*      */     
/*      */     private TreeBidiMap.Node<K, V> nextNode;
/*      */ 
/*      */     
/*      */     private TreeBidiMap.Node<K, V> previousNode;
/*      */ 
/*      */     
/*      */     private int expectedModifications;
/*      */ 
/*      */ 
/*      */     
/*      */     ViewIterator(TreeBidiMap.DataElement orderType) {
/* 1649 */       this.orderType = orderType;
/* 1650 */       this.expectedModifications = TreeBidiMap.this.modifications;
/* 1651 */       this.nextNode = TreeBidiMap.this.leastNode(TreeBidiMap.this.rootNode[orderType.ordinal()], orderType);
/* 1652 */       this.lastReturnedNode = null;
/* 1653 */       this.previousNode = null;
/*      */     }
/*      */     
/*      */     public final boolean hasNext() {
/* 1657 */       return (this.nextNode != null);
/*      */     }
/*      */     
/*      */     protected TreeBidiMap.Node<K, V> navigateNext() {
/* 1661 */       if (this.nextNode == null) {
/* 1662 */         throw new NoSuchElementException();
/*      */       }
/* 1664 */       if (TreeBidiMap.this.modifications != this.expectedModifications) {
/* 1665 */         throw new ConcurrentModificationException();
/*      */       }
/* 1667 */       this.lastReturnedNode = this.nextNode;
/* 1668 */       this.previousNode = this.nextNode;
/* 1669 */       this.nextNode = TreeBidiMap.this.nextGreater(this.nextNode, this.orderType);
/* 1670 */       return this.lastReturnedNode;
/*      */     }
/*      */     
/*      */     public boolean hasPrevious() {
/* 1674 */       return (this.previousNode != null);
/*      */     }
/*      */     
/*      */     protected TreeBidiMap.Node<K, V> navigatePrevious() {
/* 1678 */       if (this.previousNode == null) {
/* 1679 */         throw new NoSuchElementException();
/*      */       }
/* 1681 */       if (TreeBidiMap.this.modifications != this.expectedModifications) {
/* 1682 */         throw new ConcurrentModificationException();
/*      */       }
/* 1684 */       this.nextNode = this.lastReturnedNode;
/* 1685 */       if (this.nextNode == null) {
/* 1686 */         this.nextNode = TreeBidiMap.this.nextGreater(this.previousNode, this.orderType);
/*      */       }
/* 1688 */       this.lastReturnedNode = this.previousNode;
/* 1689 */       this.previousNode = TreeBidiMap.this.nextSmaller(this.previousNode, this.orderType);
/* 1690 */       return this.lastReturnedNode;
/*      */     }
/*      */     
/*      */     public final void remove() {
/* 1694 */       if (this.lastReturnedNode == null) {
/* 1695 */         throw new IllegalStateException();
/*      */       }
/* 1697 */       if (TreeBidiMap.this.modifications != this.expectedModifications) {
/* 1698 */         throw new ConcurrentModificationException();
/*      */       }
/* 1700 */       TreeBidiMap.this.doRedBlackDelete(this.lastReturnedNode);
/* 1701 */       this.expectedModifications++;
/* 1702 */       this.lastReturnedNode = null;
/* 1703 */       if (this.nextNode == null) {
/* 1704 */         this.previousNode = TreeBidiMap.this.greatestNode(TreeBidiMap.this.rootNode[this.orderType.ordinal()], this.orderType);
/*      */       } else {
/* 1706 */         this.previousNode = TreeBidiMap.this.nextSmaller(this.nextNode, this.orderType);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class ViewMapIterator
/*      */     extends ViewIterator
/*      */     implements OrderedMapIterator<K, V>
/*      */   {
/*      */     ViewMapIterator(TreeBidiMap.DataElement orderType) {
/* 1721 */       super(orderType);
/*      */     }
/*      */ 
/*      */     
/*      */     public K getKey() {
/* 1726 */       if (this.lastReturnedNode == null) {
/* 1727 */         throw new IllegalStateException("Iterator getKey() can only be called after next() and before remove()");
/*      */       }
/*      */       
/* 1730 */       return this.lastReturnedNode.getKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 1735 */       if (this.lastReturnedNode == null) {
/* 1736 */         throw new IllegalStateException("Iterator getValue() can only be called after next() and before remove()");
/*      */       }
/*      */       
/* 1739 */       return this.lastReturnedNode.getValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public V setValue(V obj) {
/* 1744 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public K next() {
/* 1749 */       return navigateNext().getKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K previous() {
/* 1754 */       return navigatePrevious().getKey();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class InverseViewMapIterator
/*      */     extends ViewIterator
/*      */     implements OrderedMapIterator<V, K>
/*      */   {
/*      */     public InverseViewMapIterator(TreeBidiMap.DataElement orderType) {
/* 1767 */       super(orderType);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getKey() {
/* 1772 */       if (this.lastReturnedNode == null) {
/* 1773 */         throw new IllegalStateException("Iterator getKey() can only be called after next() and before remove()");
/*      */       }
/*      */       
/* 1776 */       return this.lastReturnedNode.getValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public K getValue() {
/* 1781 */       if (this.lastReturnedNode == null) {
/* 1782 */         throw new IllegalStateException("Iterator getValue() can only be called after next() and before remove()");
/*      */       }
/*      */       
/* 1785 */       return this.lastReturnedNode.getKey();
/*      */     }
/*      */ 
/*      */     
/*      */     public K setValue(K obj) {
/* 1790 */       throw new UnsupportedOperationException();
/*      */     }
/*      */ 
/*      */     
/*      */     public V next() {
/* 1795 */       return navigateNext().getValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public V previous() {
/* 1800 */       return navigatePrevious().getValue();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class ViewMapEntryIterator
/*      */     extends ViewIterator
/*      */     implements OrderedIterator<Map.Entry<K, V>>
/*      */   {
/*      */     ViewMapEntryIterator() {
/* 1813 */       super(TreeBidiMap.DataElement.KEY);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> next() {
/* 1818 */       return navigateNext();
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<K, V> previous() {
/* 1823 */       return navigatePrevious();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class InverseViewMapEntryIterator
/*      */     extends ViewIterator
/*      */     implements OrderedIterator<Map.Entry<V, K>>
/*      */   {
/*      */     InverseViewMapEntryIterator() {
/* 1836 */       super(TreeBidiMap.DataElement.VALUE);
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<V, K> next() {
/* 1841 */       return createEntry(navigateNext());
/*      */     }
/*      */ 
/*      */     
/*      */     public Map.Entry<V, K> previous() {
/* 1846 */       return createEntry(navigatePrevious());
/*      */     }
/*      */     
/*      */     private Map.Entry<V, K> createEntry(TreeBidiMap.Node<K, V> node) {
/* 1850 */       return (Map.Entry<V, K>)new UnmodifiableMapEntry(node.getValue(), node.getKey());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static class Node<K extends Comparable<K>, V extends Comparable<V>>
/*      */     implements Map.Entry<K, V>, KeyValue<K, V>
/*      */   {
/*      */     private final K key;
/*      */ 
/*      */     
/*      */     private final V value;
/*      */ 
/*      */     
/*      */     private final Node<K, V>[] leftNode;
/*      */ 
/*      */     
/*      */     private final Node<K, V>[] rightNode;
/*      */     
/*      */     private final Node<K, V>[] parentNode;
/*      */     
/*      */     private final boolean[] blackColor;
/*      */     
/*      */     private int hashcodeValue;
/*      */     
/*      */     private boolean calculatedHashCode;
/*      */ 
/*      */     
/*      */     Node(K key, V value) {
/* 1880 */       this.key = key;
/* 1881 */       this.value = value;
/* 1882 */       this.leftNode = (Node<K, V>[])new Node[2];
/* 1883 */       this.rightNode = (Node<K, V>[])new Node[2];
/* 1884 */       this.parentNode = (Node<K, V>[])new Node[2];
/* 1885 */       this.blackColor = new boolean[] { true, true };
/* 1886 */       this.calculatedHashCode = false;
/*      */     }
/*      */     
/*      */     private Object getData(TreeBidiMap.DataElement dataElement) {
/* 1890 */       switch (dataElement) {
/*      */         case KEY:
/* 1892 */           return getKey();
/*      */         case VALUE:
/* 1894 */           return getValue();
/*      */       } 
/* 1896 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/*      */     
/*      */     private void setLeft(Node<K, V> node, TreeBidiMap.DataElement dataElement) {
/* 1901 */       this.leftNode[dataElement.ordinal()] = node;
/*      */     }
/*      */     
/*      */     private Node<K, V> getLeft(TreeBidiMap.DataElement dataElement) {
/* 1905 */       return this.leftNode[dataElement.ordinal()];
/*      */     }
/*      */     
/*      */     private void setRight(Node<K, V> node, TreeBidiMap.DataElement dataElement) {
/* 1909 */       this.rightNode[dataElement.ordinal()] = node;
/*      */     }
/*      */     
/*      */     private Node<K, V> getRight(TreeBidiMap.DataElement dataElement) {
/* 1913 */       return this.rightNode[dataElement.ordinal()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void setParent(Node<K, V> node, TreeBidiMap.DataElement dataElement) {
/* 1923 */       this.parentNode[dataElement.ordinal()] = node;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Node<K, V> getParent(TreeBidiMap.DataElement dataElement) {
/* 1933 */       return this.parentNode[dataElement.ordinal()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void swapColors(Node<K, V> node, TreeBidiMap.DataElement dataElement) {
/* 1944 */       this.blackColor[dataElement.ordinal()] = this.blackColor[dataElement.ordinal()] ^ node.blackColor[dataElement.ordinal()];
/* 1945 */       node.blackColor[dataElement.ordinal()] = node.blackColor[dataElement.ordinal()] ^ this.blackColor[dataElement.ordinal()];
/* 1946 */       this.blackColor[dataElement.ordinal()] = this.blackColor[dataElement.ordinal()] ^ node.blackColor[dataElement.ordinal()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean isBlack(TreeBidiMap.DataElement dataElement) {
/* 1956 */       return this.blackColor[dataElement.ordinal()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean isRed(TreeBidiMap.DataElement dataElement) {
/* 1966 */       return !this.blackColor[dataElement.ordinal()];
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void setBlack(TreeBidiMap.DataElement dataElement) {
/* 1975 */       this.blackColor[dataElement.ordinal()] = true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void setRed(TreeBidiMap.DataElement dataElement) {
/* 1984 */       this.blackColor[dataElement.ordinal()] = false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void copyColor(Node<K, V> node, TreeBidiMap.DataElement dataElement) {
/* 1994 */       this.blackColor[dataElement.ordinal()] = node.blackColor[dataElement.ordinal()];
/*      */     }
/*      */     
/*      */     private boolean isLeftChild(TreeBidiMap.DataElement dataElement) {
/* 1998 */       return (this.parentNode[dataElement.ordinal()] != null && (this.parentNode[dataElement.ordinal()]).leftNode[dataElement.ordinal()] == this);
/*      */     }
/*      */ 
/*      */     
/*      */     private boolean isRightChild(TreeBidiMap.DataElement dataElement) {
/* 2003 */       return (this.parentNode[dataElement.ordinal()] != null && (this.parentNode[dataElement.ordinal()]).rightNode[dataElement.ordinal()] == this);
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
/*      */     public K getKey() {
/* 2015 */       return this.key;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public V getValue() {
/* 2025 */       return this.value;
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
/*      */     public V setValue(V ignored) throws UnsupportedOperationException {
/* 2037 */       throw new UnsupportedOperationException("Map.Entry.setValue is not supported");
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
/*      */     public boolean equals(Object obj) {
/* 2050 */       if (obj == this) {
/* 2051 */         return true;
/*      */       }
/* 2053 */       if (!(obj instanceof Map.Entry)) {
/* 2054 */         return false;
/*      */       }
/* 2056 */       Map.Entry<?, ?> e = (Map.Entry<?, ?>)obj;
/* 2057 */       return (getKey().equals(e.getKey()) && getValue().equals(e.getValue()));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2065 */       if (!this.calculatedHashCode) {
/* 2066 */         this.hashcodeValue = getKey().hashCode() ^ getValue().hashCode();
/* 2067 */         this.calculatedHashCode = true;
/*      */       } 
/* 2069 */       return this.hashcodeValue;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   class Inverse
/*      */     implements OrderedBidiMap<V, K>
/*      */   {
/*      */     private Set<V> inverseKeySet;
/*      */ 
/*      */     
/*      */     private Set<K> inverseValuesSet;
/*      */ 
/*      */     
/*      */     private Set<Map.Entry<V, K>> inverseEntrySet;
/*      */ 
/*      */     
/*      */     public int size() {
/* 2088 */       return TreeBidiMap.this.size();
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean isEmpty() {
/* 2093 */       return TreeBidiMap.this.isEmpty();
/*      */     }
/*      */ 
/*      */     
/*      */     public K get(Object key) {
/* 2098 */       return (K)TreeBidiMap.this.getKey(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V getKey(Object value) {
/* 2103 */       return (V)TreeBidiMap.this.get(value);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsKey(Object key) {
/* 2108 */       return TreeBidiMap.this.containsValue(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean containsValue(Object value) {
/* 2113 */       return TreeBidiMap.this.containsKey(value);
/*      */     }
/*      */ 
/*      */     
/*      */     public V firstKey() {
/* 2118 */       if (TreeBidiMap.this.nodeCount == 0) {
/* 2119 */         throw new NoSuchElementException("Map is empty");
/*      */       }
/* 2121 */       return (V)TreeBidiMap.this.leastNode(TreeBidiMap.this.rootNode[TreeBidiMap.DataElement.VALUE.ordinal()], TreeBidiMap.DataElement.VALUE).getValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public V lastKey() {
/* 2126 */       if (TreeBidiMap.this.nodeCount == 0) {
/* 2127 */         throw new NoSuchElementException("Map is empty");
/*      */       }
/* 2129 */       return (V)TreeBidiMap.this.greatestNode(TreeBidiMap.this.rootNode[TreeBidiMap.DataElement.VALUE.ordinal()], TreeBidiMap.DataElement.VALUE).getValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public V nextKey(V key) {
/* 2134 */       TreeBidiMap.checkKey(key);
/* 2135 */       TreeBidiMap.Node<K, V> node = TreeBidiMap.this.nextGreater(TreeBidiMap.this.lookup(key, TreeBidiMap.DataElement.VALUE), TreeBidiMap.DataElement.VALUE);
/* 2136 */       return (node == null) ? null : node.getValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public V previousKey(V key) {
/* 2141 */       TreeBidiMap.checkKey(key);
/* 2142 */       TreeBidiMap.Node<K, V> node = TreeBidiMap.this.nextSmaller(TreeBidiMap.this.lookup(key, TreeBidiMap.DataElement.VALUE), TreeBidiMap.DataElement.VALUE);
/* 2143 */       return (node == null) ? null : node.getValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public K put(V key, K value) {
/* 2148 */       K result = get(key);
/* 2149 */       TreeBidiMap.this.doPut(value, key);
/* 2150 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     public void putAll(Map<? extends V, ? extends K> map) {
/* 2155 */       for (Map.Entry<? extends V, ? extends K> e : map.entrySet()) {
/* 2156 */         put(e.getKey(), e.getValue());
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public K remove(Object key) {
/* 2162 */       return (K)TreeBidiMap.this.removeValue(key);
/*      */     }
/*      */ 
/*      */     
/*      */     public V removeValue(Object value) {
/* 2167 */       return (V)TreeBidiMap.this.remove(value);
/*      */     }
/*      */ 
/*      */     
/*      */     public void clear() {
/* 2172 */       TreeBidiMap.this.clear();
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<V> keySet() {
/* 2177 */       if (this.inverseKeySet == null) {
/* 2178 */         this.inverseKeySet = new TreeBidiMap.ValueView(TreeBidiMap.DataElement.VALUE);
/*      */       }
/* 2180 */       return this.inverseKeySet;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<K> values() {
/* 2185 */       if (this.inverseValuesSet == null) {
/* 2186 */         this.inverseValuesSet = new TreeBidiMap.KeyView(TreeBidiMap.DataElement.VALUE);
/*      */       }
/* 2188 */       return this.inverseValuesSet;
/*      */     }
/*      */ 
/*      */     
/*      */     public Set<Map.Entry<V, K>> entrySet() {
/* 2193 */       if (this.inverseEntrySet == null) {
/* 2194 */         this.inverseEntrySet = new TreeBidiMap.InverseEntryView();
/*      */       }
/* 2196 */       return this.inverseEntrySet;
/*      */     }
/*      */ 
/*      */     
/*      */     public OrderedMapIterator<V, K> mapIterator() {
/* 2201 */       if (isEmpty()) {
/* 2202 */         return EmptyOrderedMapIterator.emptyOrderedMapIterator();
/*      */       }
/* 2204 */       return new TreeBidiMap.InverseViewMapIterator(TreeBidiMap.DataElement.VALUE);
/*      */     }
/*      */ 
/*      */     
/*      */     public OrderedBidiMap<K, V> inverseBidiMap() {
/* 2209 */       return TreeBidiMap.this;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean equals(Object obj) {
/* 2214 */       return TreeBidiMap.this.doEquals(obj, TreeBidiMap.DataElement.VALUE);
/*      */     }
/*      */ 
/*      */     
/*      */     public int hashCode() {
/* 2219 */       return TreeBidiMap.this.doHashCode(TreeBidiMap.DataElement.VALUE);
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 2224 */       return TreeBidiMap.this.doToString(TreeBidiMap.DataElement.VALUE);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bidimap\TreeBidiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */