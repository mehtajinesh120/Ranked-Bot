/*     */ package org.apache.commons.collections4.collection;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.apache.commons.collections4.iterators.EmptyIterator;
/*     */ import org.apache.commons.collections4.iterators.IteratorChain;
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
/*     */ public class CompositeCollection<E>
/*     */   implements Collection<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 8417515734108306801L;
/*     */   private CollectionMutator<E> mutator;
/*  51 */   private final List<Collection<E>> all = new ArrayList<Collection<E>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeCollection() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeCollection(Collection<E> compositeCollection) {
/*  67 */     addComposited(compositeCollection);
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
/*     */   public CompositeCollection(Collection<E> compositeCollection1, Collection<E> compositeCollection2) {
/*  79 */     addComposited(compositeCollection1, compositeCollection2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeCollection(Collection<E>... compositeCollections) {
/*  89 */     addComposited(compositeCollections);
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
/*     */   public int size() {
/* 102 */     int size = 0;
/* 103 */     for (Collection<E> item : this.all) {
/* 104 */       size += item.size();
/*     */     }
/* 106 */     return size;
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
/*     */   public boolean isEmpty() {
/* 118 */     for (Collection<E> item : this.all) {
/* 119 */       if (!item.isEmpty()) {
/* 120 */         return false;
/*     */       }
/*     */     } 
/* 123 */     return true;
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
/*     */   public boolean contains(Object obj) {
/* 136 */     for (Collection<E> item : this.all) {
/* 137 */       if (item.contains(obj)) {
/* 138 */         return true;
/*     */       }
/*     */     } 
/* 141 */     return false;
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
/*     */   public Iterator<E> iterator() {
/* 156 */     if (this.all.isEmpty()) {
/* 157 */       return EmptyIterator.emptyIterator();
/*     */     }
/* 159 */     IteratorChain<E> chain = new IteratorChain();
/* 160 */     for (Collection<E> item : this.all) {
/* 161 */       chain.addIterator(item.iterator());
/*     */     }
/* 163 */     return (Iterator<E>)chain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 173 */     Object[] result = new Object[size()];
/* 174 */     int i = 0;
/* 175 */     for (Iterator<E> it = iterator(); it.hasNext(); i++) {
/* 176 */       result[i] = it.next();
/*     */     }
/* 178 */     return result;
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
/*     */   public <T> T[] toArray(T[] array) {
/* 192 */     int size = size();
/* 193 */     Object[] result = null;
/* 194 */     if (array.length >= size) {
/* 195 */       T[] arrayOfT = array;
/*     */     } else {
/* 197 */       result = (Object[])Array.newInstance(array.getClass().getComponentType(), size);
/*     */     } 
/*     */     
/* 200 */     int offset = 0;
/* 201 */     for (Collection<E> item : this.all) {
/* 202 */       for (E e : item) {
/* 203 */         result[offset++] = e;
/*     */       }
/*     */     } 
/* 206 */     if (result.length > size) {
/* 207 */       result[size] = null;
/*     */     }
/* 209 */     return (T[])result;
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
/*     */   public boolean add(E obj) {
/* 226 */     if (this.mutator == null) {
/* 227 */       throw new UnsupportedOperationException("add() is not supported on CompositeCollection without a CollectionMutator strategy");
/*     */     }
/*     */     
/* 230 */     return this.mutator.add(this, this.all, obj);
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
/*     */   public boolean remove(Object obj) {
/* 246 */     if (this.mutator == null) {
/* 247 */       throw new UnsupportedOperationException("remove() is not supported on CompositeCollection without a CollectionMutator strategy");
/*     */     }
/*     */     
/* 250 */     return this.mutator.remove(this, this.all, obj);
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
/*     */   public boolean containsAll(Collection<?> coll) {
/* 264 */     for (Object item : coll) {
/* 265 */       if (!contains(item)) {
/* 266 */         return false;
/*     */       }
/*     */     } 
/* 269 */     return true;
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
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 286 */     if (this.mutator == null) {
/* 287 */       throw new UnsupportedOperationException("addAll() is not supported on CompositeCollection without a CollectionMutator strategy");
/*     */     }
/*     */     
/* 290 */     return this.mutator.addAll(this, this.all, coll);
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
/*     */   public boolean removeAll(Collection<?> coll) {
/* 304 */     if (coll.size() == 0) {
/* 305 */       return false;
/*     */     }
/* 307 */     boolean changed = false;
/* 308 */     for (Collection<E> item : this.all) {
/* 309 */       changed |= item.removeAll(coll);
/*     */     }
/* 311 */     return changed;
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
/*     */   public boolean retainAll(Collection<?> coll) {
/* 326 */     boolean changed = false;
/* 327 */     for (Collection<E> item : this.all) {
/* 328 */       changed |= item.retainAll(coll);
/*     */     }
/* 330 */     return changed;
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
/*     */   public void clear() {
/* 342 */     for (Collection<E> coll : this.all) {
/* 343 */       coll.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMutator(CollectionMutator<E> mutator) {
/* 354 */     this.mutator = mutator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComposited(Collection<E> compositeCollection) {
/* 363 */     this.all.add(compositeCollection);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComposited(Collection<E> compositeCollection1, Collection<E> compositeCollection2) {
/* 374 */     this.all.add(compositeCollection1);
/* 375 */     this.all.add(compositeCollection2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComposited(Collection<E>... compositeCollections) {
/* 384 */     this.all.addAll(Arrays.asList(compositeCollections));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeComposited(Collection<E> coll) {
/* 393 */     this.all.remove(coll);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<E> toCollection() {
/* 404 */     return new ArrayList<E>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Collection<E>> getCollections() {
/* 413 */     return UnmodifiableList.unmodifiableList(this.all);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CollectionMutator<E> getMutator() {
/* 421 */     return this.mutator;
/*     */   }
/*     */   
/*     */   public static interface CollectionMutator<E> extends Serializable {
/*     */     boolean add(CompositeCollection<E> param1CompositeCollection, List<Collection<E>> param1List, E param1E);
/*     */     
/*     */     boolean addAll(CompositeCollection<E> param1CompositeCollection, List<Collection<E>> param1List, Collection<? extends E> param1Collection);
/*     */     
/*     */     boolean remove(CompositeCollection<E> param1CompositeCollection, List<Collection<E>> param1List, Object param1Object);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\collection\CompositeCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */