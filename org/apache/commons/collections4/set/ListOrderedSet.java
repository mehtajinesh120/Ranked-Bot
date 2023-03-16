/*     */ package org.apache.commons.collections4.set;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.CollectionUtils;
/*     */ import org.apache.commons.collections4.OrderedIterator;
/*     */ import org.apache.commons.collections4.functors.UniquePredicate;
/*     */ import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ListOrderedSet<E>
/*     */   extends AbstractSerializableSetDecorator<E>
/*     */ {
/*     */   private static final long serialVersionUID = -228664372470420141L;
/*     */   private final List<E> setOrder;
/*     */   
/*     */   public static <E> ListOrderedSet<E> listOrderedSet(Set<E> set, List<E> list) {
/*  78 */     if (set == null) {
/*  79 */       throw new NullPointerException("Set must not be null");
/*     */     }
/*  81 */     if (list == null) {
/*  82 */       throw new NullPointerException("List must not be null");
/*     */     }
/*  84 */     if (set.size() > 0 || list.size() > 0) {
/*  85 */       throw new IllegalArgumentException("Set and List must be empty");
/*     */     }
/*  87 */     return new ListOrderedSet<E>(set, list);
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
/*     */   public static <E> ListOrderedSet<E> listOrderedSet(Set<E> set) {
/* 102 */     return new ListOrderedSet<E>(set);
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
/*     */   public static <E> ListOrderedSet<E> listOrderedSet(List<E> list) {
/* 120 */     if (list == null) {
/* 121 */       throw new NullPointerException("List must not be null");
/*     */     }
/* 123 */     CollectionUtils.filter(list, UniquePredicate.uniquePredicate());
/* 124 */     Set<E> set = new HashSet<E>(list);
/*     */     
/* 126 */     return new ListOrderedSet<E>(set, list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ListOrderedSet() {
/* 137 */     super(new HashSet<E>());
/* 138 */     this.setOrder = new ArrayList<E>();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ListOrderedSet(Set<E> set) {
/* 148 */     super(set);
/* 149 */     this.setOrder = new ArrayList<E>(set);
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
/*     */   protected ListOrderedSet(Set<E> set, List<E> list) {
/* 163 */     super(set);
/* 164 */     if (list == null) {
/* 165 */       throw new NullPointerException("List must not be null");
/*     */     }
/* 167 */     this.setOrder = list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<E> asList() {
/* 177 */     return UnmodifiableList.unmodifiableList(this.setOrder);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 183 */     decorated().clear();
/* 184 */     this.setOrder.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public OrderedIterator<E> iterator() {
/* 189 */     return new OrderedSetIterator<E>(this.setOrder.listIterator(), decorated());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E object) {
/* 194 */     if (decorated().add(object)) {
/* 195 */       this.setOrder.add(object);
/* 196 */       return true;
/*     */     } 
/* 198 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 203 */     boolean result = false;
/* 204 */     for (E e : coll) {
/* 205 */       result |= add(e);
/*     */     }
/* 207 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/* 212 */     boolean result = decorated().remove(object);
/* 213 */     if (result) {
/* 214 */       this.setOrder.remove(object);
/*     */     }
/* 216 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/* 221 */     boolean result = false;
/* 222 */     for (Object name : coll) {
/* 223 */       result |= remove(name);
/*     */     }
/* 225 */     return result;
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
/* 239 */     boolean result = decorated().retainAll(coll);
/* 240 */     if (!result) {
/* 241 */       return false;
/*     */     }
/* 243 */     if (decorated().size() == 0) {
/* 244 */       this.setOrder.clear();
/*     */     } else {
/* 246 */       for (Iterator<E> it = this.setOrder.iterator(); it.hasNext();) {
/* 247 */         if (!decorated().contains(it.next())) {
/* 248 */           it.remove();
/*     */         }
/*     */       } 
/*     */     } 
/* 252 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 257 */     return this.setOrder.toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 262 */     return this.setOrder.toArray(a);
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
/*     */   public E get(int index) {
/* 277 */     return this.setOrder.get(index);
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
/*     */   public int indexOf(Object object) {
/* 290 */     return this.setOrder.indexOf(object);
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
/*     */   public void add(int index, E object) {
/* 303 */     if (!contains(object)) {
/* 304 */       decorated().add(object);
/* 305 */       this.setOrder.add(index, object);
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
/*     */   public boolean addAll(int index, Collection<? extends E> coll) {
/* 321 */     boolean changed = false;
/*     */     
/* 323 */     List<E> toAdd = new ArrayList<E>();
/* 324 */     for (E e : coll) {
/* 325 */       if (contains(e)) {
/*     */         continue;
/*     */       }
/* 328 */       decorated().add(e);
/* 329 */       toAdd.add(e);
/* 330 */       changed = true;
/*     */     } 
/*     */     
/* 333 */     if (changed) {
/* 334 */       this.setOrder.addAll(index, toAdd);
/*     */     }
/*     */     
/* 337 */     return changed;
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
/*     */   public E remove(int index) {
/* 349 */     E obj = this.setOrder.remove(index);
/* 350 */     remove(obj);
/* 351 */     return obj;
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
/*     */   public String toString() {
/* 364 */     return this.setOrder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class OrderedSetIterator<E>
/*     */     extends AbstractIteratorDecorator<E>
/*     */     implements OrderedIterator<E>
/*     */   {
/*     */     private final Collection<E> set;
/*     */ 
/*     */     
/*     */     private E last;
/*     */ 
/*     */ 
/*     */     
/*     */     private OrderedSetIterator(ListIterator<E> iterator, Collection<E> set) {
/* 382 */       super(iterator);
/* 383 */       this.set = set;
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 388 */       this.last = getIterator().next();
/* 389 */       return this.last;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 394 */       this.set.remove(this.last);
/* 395 */       getIterator().remove();
/* 396 */       this.last = null;
/*     */     }
/*     */     
/*     */     public boolean hasPrevious() {
/* 400 */       return ((ListIterator)getIterator()).hasPrevious();
/*     */     }
/*     */     
/*     */     public E previous() {
/* 404 */       this.last = ((ListIterator<E>)getIterator()).previous();
/* 405 */       return this.last;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\ListOrderedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */