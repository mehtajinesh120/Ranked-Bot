/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.iterators.UnmodifiableIterator;
/*     */ import org.apache.commons.collections4.iterators.UnmodifiableListIterator;
/*     */ import org.apache.commons.collections4.list.UnmodifiableList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LinkedMap<K, V>
/*     */   extends AbstractLinkedMap<K, V>
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 9077234323521161066L;
/*     */   
/*     */   public LinkedMap() {
/*  72 */     super(16, 0.75F, 12);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedMap(int initialCapacity) {
/*  82 */     super(initialCapacity);
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
/*     */   public LinkedMap(int initialCapacity, float loadFactor) {
/*  95 */     super(initialCapacity, loadFactor);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedMap(Map<? extends K, ? extends V> map) {
/* 105 */     super(map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LinkedMap<K, V> clone() {
/* 116 */     return (LinkedMap<K, V>)super.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 123 */     out.defaultWriteObject();
/* 124 */     doWriteObject(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 131 */     in.defaultReadObject();
/* 132 */     doReadObject(in);
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
/*     */   public K get(int index) {
/* 144 */     return getEntry(index).getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V getValue(int index) {
/* 155 */     return getEntry(index).getValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int indexOf(Object key) {
/* 165 */     key = convertKey(key);
/* 166 */     int i = 0;
/* 167 */     for (AbstractLinkedMap.LinkEntry<K, V> entry = this.header.after; entry != this.header; entry = entry.after, i++) {
/* 168 */       if (isEqualKey(key, entry.key)) {
/* 169 */         return i;
/*     */       }
/*     */     } 
/* 172 */     return -1;
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
/*     */   public V remove(int index) {
/* 184 */     return remove(get(index));
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
/*     */   public List<K> asList() {
/* 203 */     return new LinkedMapList<K>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   static class LinkedMapList<K>
/*     */     extends AbstractList<K>
/*     */   {
/*     */     private final LinkedMap<K, ?> parent;
/*     */ 
/*     */     
/*     */     LinkedMapList(LinkedMap<K, ?> parent) {
/* 214 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 219 */       return this.parent.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public K get(int index) {
/* 224 */       return this.parent.get(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 229 */       return this.parent.containsKey(obj);
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(Object obj) {
/* 234 */       return this.parent.indexOf(obj);
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object obj) {
/* 239 */       return this.parent.indexOf(obj);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> coll) {
/* 244 */       return this.parent.keySet().containsAll(coll);
/*     */     }
/*     */ 
/*     */     
/*     */     public K remove(int index) {
/* 249 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 254 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeAll(Collection<?> coll) {
/* 259 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean retainAll(Collection<?> coll) {
/* 264 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 269 */       throw new UnsupportedOperationException();
/*     */     }
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 274 */       return this.parent.keySet().toArray();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 279 */       return (T[])this.parent.keySet().toArray((Object[])array);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<K> iterator() {
/* 284 */       return UnmodifiableIterator.unmodifiableIterator(this.parent.keySet().iterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public ListIterator<K> listIterator() {
/* 289 */       return UnmodifiableListIterator.umodifiableListIterator(super.listIterator());
/*     */     }
/*     */ 
/*     */     
/*     */     public ListIterator<K> listIterator(int fromIndex) {
/* 294 */       return UnmodifiableListIterator.umodifiableListIterator(super.listIterator(fromIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public List<K> subList(int fromIndexInclusive, int toIndexExclusive) {
/* 299 */       return UnmodifiableList.unmodifiableList(super.subList(fromIndexInclusive, toIndexExclusive));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\LinkedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */