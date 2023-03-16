/*     */ package org.apache.commons.collections4.multimap;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.ListUtils;
/*     */ import org.apache.commons.collections4.ListValuedMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractListValuedMap<K, V>
/*     */   extends AbstractMultiValuedMap<K, V>
/*     */   implements ListValuedMap<K, V>
/*     */ {
/*     */   protected AbstractListValuedMap() {}
/*     */   
/*     */   protected AbstractListValuedMap(Map<K, ? extends List<V>> map) {
/*  55 */     super((Map)map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Map<K, List<V>> getMap() {
/*  62 */     return (Map)super.getMap();
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
/*     */   public List<V> get(K key) {
/*  82 */     return wrappedCollection(key);
/*     */   }
/*     */ 
/*     */   
/*     */   List<V> wrappedCollection(K key) {
/*  87 */     return new WrappedList(key);
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
/*     */   public List<V> remove(Object key) {
/* 101 */     return ListUtils.emptyIfNull(getMap().remove(key));
/*     */   }
/*     */   
/*     */   protected abstract List<V> createCollection();
/*     */   
/*     */   private class WrappedList
/*     */     extends AbstractMultiValuedMap<K, V>.WrappedCollection
/*     */     implements List<V>
/*     */   {
/*     */     public WrappedList(K key) {
/* 111 */       super(key);
/*     */     }
/*     */ 
/*     */     
/*     */     protected List<V> getMapping() {
/* 116 */       return (List<V>)AbstractListValuedMap.this.getMap().get(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(int index, V value) {
/* 121 */       List<V> list = getMapping();
/* 122 */       if (list == null) {
/* 123 */         list = AbstractListValuedMap.this.createCollection();
/* 124 */         AbstractListValuedMap.this.getMap().put(this.key, list);
/*     */       } 
/* 126 */       list.add(index, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean addAll(int index, Collection<? extends V> c) {
/* 131 */       List<V> list = getMapping();
/* 132 */       if (list == null) {
/* 133 */         list = AbstractListValuedMap.this.createCollection();
/* 134 */         boolean changed = list.addAll(index, c);
/* 135 */         if (changed) {
/* 136 */           AbstractListValuedMap.this.getMap().put(this.key, list);
/*     */         }
/* 138 */         return changed;
/*     */       } 
/* 140 */       return list.addAll(index, c);
/*     */     }
/*     */ 
/*     */     
/*     */     public V get(int index) {
/* 145 */       List<V> list = ListUtils.emptyIfNull(getMapping());
/* 146 */       return list.get(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(Object o) {
/* 151 */       List<V> list = ListUtils.emptyIfNull(getMapping());
/* 152 */       return list.indexOf(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object o) {
/* 157 */       List<V> list = ListUtils.emptyIfNull(getMapping());
/* 158 */       return list.lastIndexOf(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public ListIterator<V> listIterator() {
/* 163 */       return new AbstractListValuedMap.ValuesListIterator(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public ListIterator<V> listIterator(int index) {
/* 168 */       return new AbstractListValuedMap.ValuesListIterator(this.key, index);
/*     */     }
/*     */ 
/*     */     
/*     */     public V remove(int index) {
/* 173 */       List<V> list = ListUtils.emptyIfNull(getMapping());
/* 174 */       V value = list.remove(index);
/* 175 */       if (list.isEmpty()) {
/* 176 */         AbstractListValuedMap.this.remove(this.key);
/*     */       }
/* 178 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public V set(int index, V value) {
/* 183 */       List<V> list = ListUtils.emptyIfNull(getMapping());
/* 184 */       return list.set(index, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<V> subList(int fromIndex, int toIndex) {
/* 189 */       List<V> list = ListUtils.emptyIfNull(getMapping());
/* 190 */       return list.subList(fromIndex, toIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object other) {
/* 195 */       List<V> list = getMapping();
/* 196 */       if (list == null) {
/* 197 */         return Collections.emptyList().equals(other);
/*     */       }
/* 199 */       if (!(other instanceof List)) {
/* 200 */         return false;
/*     */       }
/* 202 */       List<?> otherList = (List)other;
/* 203 */       return ListUtils.isEqualList(list, otherList);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 208 */       List<V> list = getMapping();
/* 209 */       return ListUtils.hashCodeForList(list);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private class ValuesListIterator
/*     */     implements ListIterator<V>
/*     */   {
/*     */     private final K key;
/*     */     private List<V> values;
/*     */     private ListIterator<V> iterator;
/*     */     
/*     */     public ValuesListIterator(K key) {
/* 222 */       this.key = key;
/* 223 */       this.values = ListUtils.emptyIfNull((List)AbstractListValuedMap.this.getMap().get(key));
/* 224 */       this.iterator = this.values.listIterator();
/*     */     }
/*     */     
/*     */     public ValuesListIterator(K key, int index) {
/* 228 */       this.key = key;
/* 229 */       this.values = ListUtils.emptyIfNull((List)AbstractListValuedMap.this.getMap().get(key));
/* 230 */       this.iterator = this.values.listIterator(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(V value) {
/* 235 */       if (AbstractListValuedMap.this.getMap().get(this.key) == null) {
/* 236 */         List<V> list = AbstractListValuedMap.this.createCollection();
/* 237 */         AbstractListValuedMap.this.getMap().put(this.key, list);
/* 238 */         this.values = list;
/* 239 */         this.iterator = list.listIterator();
/*     */       } 
/* 241 */       this.iterator.add(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 246 */       return this.iterator.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasPrevious() {
/* 251 */       return this.iterator.hasPrevious();
/*     */     }
/*     */ 
/*     */     
/*     */     public V next() {
/* 256 */       return this.iterator.next();
/*     */     }
/*     */ 
/*     */     
/*     */     public int nextIndex() {
/* 261 */       return this.iterator.nextIndex();
/*     */     }
/*     */ 
/*     */     
/*     */     public V previous() {
/* 266 */       return this.iterator.previous();
/*     */     }
/*     */ 
/*     */     
/*     */     public int previousIndex() {
/* 271 */       return this.iterator.previousIndex();
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 276 */       this.iterator.remove();
/* 277 */       if (this.values.isEmpty()) {
/* 278 */         AbstractListValuedMap.this.getMap().remove(this.key);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(V value) {
/* 284 */       this.iterator.set(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multimap\AbstractListValuedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */