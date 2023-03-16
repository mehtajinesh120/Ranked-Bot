/*     */ package org.apache.commons.collections4.set;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.CollectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompositeSet<E>
/*     */   implements Set<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5185069727540378940L;
/*     */   private SetMutator<E> mutator;
/*  58 */   private final List<Set<E>> all = new ArrayList<Set<E>>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeSet() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeSet(Set<E> set) {
/*  74 */     addComposited(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompositeSet(Set<E>... sets) {
/*  84 */     addComposited(sets);
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
/*     */   public int size() {
/*  96 */     int size = 0;
/*  97 */     for (Set<E> item : this.all) {
/*  98 */       size += item.size();
/*     */     }
/* 100 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 111 */     for (Set<E> item : this.all) {
/* 112 */       if (!item.isEmpty()) {
/* 113 */         return false;
/*     */       }
/*     */     } 
/* 116 */     return true;
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
/*     */   public boolean contains(Object obj) {
/* 128 */     for (Set<E> item : this.all) {
/* 129 */       if (item.contains(obj)) {
/* 130 */         return true;
/*     */       }
/*     */     } 
/* 133 */     return false;
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
/*     */   public Iterator<E> iterator() {
/* 147 */     if (this.all.isEmpty()) {
/* 148 */       return EmptyIterator.emptyIterator();
/*     */     }
/* 150 */     IteratorChain<E> chain = new IteratorChain();
/* 151 */     for (Set<E> item : this.all) {
/* 152 */       chain.addIterator(item.iterator());
/*     */     }
/* 154 */     return (Iterator<E>)chain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 163 */     Object[] result = new Object[size()];
/* 164 */     int i = 0;
/* 165 */     for (Iterator<E> it = iterator(); it.hasNext(); i++) {
/* 166 */       result[i] = it.next();
/*     */     }
/* 168 */     return result;
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
/*     */   public <T> T[] toArray(T[] array) {
/* 181 */     int size = size();
/* 182 */     Object[] result = null;
/* 183 */     if (array.length >= size) {
/* 184 */       T[] arrayOfT = array;
/*     */     } else {
/* 186 */       result = (Object[])Array.newInstance(array.getClass().getComponentType(), size);
/*     */     } 
/*     */     
/* 189 */     int offset = 0;
/* 190 */     for (Collection<E> item : this.all) {
/* 191 */       for (E e : item) {
/* 192 */         result[offset++] = e;
/*     */       }
/*     */     } 
/* 195 */     if (result.length > size) {
/* 196 */       result[size] = null;
/*     */     }
/* 198 */     return (T[])result;
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
/*     */   public boolean add(E obj) {
/* 213 */     if (this.mutator == null) {
/* 214 */       throw new UnsupportedOperationException("add() is not supported on CompositeSet without a SetMutator strategy");
/*     */     }
/*     */     
/* 217 */     return this.mutator.add(this, this.all, obj);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object obj) {
/* 228 */     for (Set<E> set : getSets()) {
/* 229 */       if (set.contains(obj)) {
/* 230 */         return set.remove(obj);
/*     */       }
/*     */     } 
/* 233 */     return false;
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
/*     */   public boolean containsAll(Collection<?> coll) {
/* 246 */     for (Object item : coll) {
/* 247 */       if (!contains(item)) {
/* 248 */         return false;
/*     */       }
/*     */     } 
/* 251 */     return true;
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
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 266 */     if (this.mutator == null) {
/* 267 */       throw new UnsupportedOperationException("addAll() is not supported on CompositeSet without a SetMutator strategy");
/*     */     }
/*     */     
/* 270 */     return this.mutator.addAll(this, this.all, coll);
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
/*     */   public boolean removeAll(Collection<?> coll) {
/* 283 */     if (coll.size() == 0) {
/* 284 */       return false;
/*     */     }
/* 286 */     boolean changed = false;
/* 287 */     for (Collection<E> item : this.all) {
/* 288 */       changed |= item.removeAll(coll);
/*     */     }
/* 290 */     return changed;
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
/*     */   public boolean retainAll(Collection<?> coll) {
/* 304 */     boolean changed = false;
/* 305 */     for (Collection<E> item : this.all) {
/* 306 */       changed |= item.retainAll(coll);
/*     */     }
/* 308 */     return changed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 319 */     for (Collection<E> coll : this.all) {
/* 320 */       coll.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMutator(SetMutator<E> mutator) {
/* 331 */     this.mutator = mutator;
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
/*     */   public synchronized void addComposited(Set<E> set) {
/* 344 */     for (Set<E> existingSet : getSets()) {
/* 345 */       Collection<E> intersects = CollectionUtils.intersection(existingSet, set);
/* 346 */       if (intersects.size() > 0) {
/* 347 */         if (this.mutator == null) {
/* 348 */           throw new UnsupportedOperationException("Collision adding composited set with no SetMutator set");
/*     */         }
/*     */         
/* 351 */         getMutator().resolveCollision(this, existingSet, set, intersects);
/* 352 */         if (CollectionUtils.intersection(existingSet, set).size() > 0) {
/* 353 */           throw new IllegalArgumentException("Attempt to add illegal entry unresolved by SetMutator.resolveCollision()");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 358 */     this.all.add(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComposited(Set<E> set1, Set<E> set2) {
/* 368 */     addComposited(set1);
/* 369 */     addComposited(set2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addComposited(Set<E>... sets) {
/* 378 */     for (Set<E> set : sets) {
/* 379 */       addComposited(set);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeComposited(Set<E> set) {
/* 389 */     this.all.remove(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<E> toSet() {
/* 400 */     return new HashSet<E>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Set<E>> getSets() {
/* 409 */     return UnmodifiableList.unmodifiableList(this.all);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SetMutator<E> getMutator() {
/* 417 */     return this.mutator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 426 */     if (obj instanceof Set) {
/* 427 */       Set<?> set = (Set)obj;
/* 428 */       return (set.size() == size() && set.containsAll(this));
/*     */     } 
/* 430 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 439 */     int code = 0;
/* 440 */     for (E e : this) {
/* 441 */       code += (e == null) ? 0 : e.hashCode();
/*     */     }
/* 443 */     return code;
/*     */   }
/*     */   
/*     */   public static interface SetMutator<E> extends Serializable {
/*     */     boolean add(CompositeSet<E> param1CompositeSet, List<Set<E>> param1List, E param1E);
/*     */     
/*     */     boolean addAll(CompositeSet<E> param1CompositeSet, List<Set<E>> param1List, Collection<? extends E> param1Collection);
/*     */     
/*     */     void resolveCollision(CompositeSet<E> param1CompositeSet, Set<E> param1Set1, Set<E> param1Set2, Collection<E> param1Collection);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\CompositeSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */