/*     */ package org.apache.commons.collections4.list;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.ListUtils;
/*     */ import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
/*     */ import org.apache.commons.collections4.iterators.AbstractListIteratorDecorator;
/*     */ import org.apache.commons.collections4.set.UnmodifiableSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SetUniqueList<E>
/*     */   extends AbstractSerializableListDecorator<E>
/*     */ {
/*     */   private static final long serialVersionUID = 7196982186153478694L;
/*     */   private final Set<E> set;
/*     */   
/*     */   public static <E> SetUniqueList<E> setUniqueList(List<E> list) {
/*  72 */     if (list == null) {
/*  73 */       throw new NullPointerException("List must not be null");
/*     */     }
/*  75 */     if (list.isEmpty()) {
/*  76 */       return new SetUniqueList<E>(list, new HashSet<E>());
/*     */     }
/*  78 */     List<E> temp = new ArrayList<E>(list);
/*  79 */     list.clear();
/*  80 */     SetUniqueList<E> sl = new SetUniqueList<E>(list, new HashSet<E>());
/*  81 */     sl.addAll(temp);
/*  82 */     return sl;
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
/*     */   protected SetUniqueList(List<E> list, Set<E> set) {
/*  96 */     super(list);
/*  97 */     if (set == null) {
/*  98 */       throw new NullPointerException("Set must not be null");
/*     */     }
/* 100 */     this.set = set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<E> asSet() {
/* 110 */     return UnmodifiableSet.unmodifiableSet(this.set);
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
/*     */   public boolean add(E object) {
/* 127 */     int sizeBefore = size();
/*     */ 
/*     */     
/* 130 */     add(size(), object);
/*     */ 
/*     */     
/* 133 */     return (sizeBefore != size());
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
/*     */   public void add(int index, E object) {
/* 150 */     if (!this.set.contains(object)) {
/* 151 */       super.add(index, object);
/* 152 */       this.set.add(object);
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
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 171 */     return addAll(size(), coll);
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
/*     */   public boolean addAll(int index, Collection<? extends E> coll) {
/* 191 */     List<E> temp = new ArrayList<E>();
/* 192 */     for (E e : coll) {
/* 193 */       if (this.set.add(e)) {
/* 194 */         temp.add(e);
/*     */       }
/*     */     } 
/* 197 */     return super.addAll(index, temp);
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
/*     */   public E set(int index, E object) {
/* 214 */     int pos = indexOf(object);
/* 215 */     E removed = super.set(index, object);
/*     */     
/* 217 */     if (pos != -1 && pos != index)
/*     */     {
/*     */       
/* 220 */       super.remove(pos);
/*     */     }
/*     */     
/* 223 */     this.set.remove(removed);
/* 224 */     this.set.add(object);
/*     */     
/* 226 */     return removed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/* 231 */     boolean result = this.set.remove(object);
/* 232 */     if (result) {
/* 233 */       super.remove(object);
/*     */     }
/* 235 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public E remove(int index) {
/* 240 */     E result = super.remove(index);
/* 241 */     this.set.remove(result);
/* 242 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/* 247 */     boolean result = false;
/* 248 */     for (Object name : coll) {
/* 249 */       result |= remove(name);
/*     */     }
/* 251 */     return result;
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
/* 265 */     boolean result = this.set.retainAll(coll);
/* 266 */     if (!result) {
/* 267 */       return false;
/*     */     }
/* 269 */     if (this.set.size() == 0) {
/* 270 */       super.clear();
/*     */     } else {
/*     */       
/* 273 */       super.retainAll(this.set);
/*     */     } 
/* 275 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 280 */     super.clear();
/* 281 */     this.set.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object object) {
/* 286 */     return this.set.contains(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> coll) {
/* 291 */     return this.set.containsAll(coll);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 296 */     return (Iterator<E>)new SetListIterator<E>(super.iterator(), this.set);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator() {
/* 301 */     return (ListIterator<E>)new SetListListIterator<E>(super.listIterator(), this.set);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListIterator<E> listIterator(int index) {
/* 306 */     return (ListIterator<E>)new SetListListIterator<E>(super.listIterator(index), this.set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 317 */     List<E> superSubList = super.subList(fromIndex, toIndex);
/* 318 */     Set<E> subSet = createSetBasedOnList(this.set, superSubList);
/* 319 */     return ListUtils.unmodifiableList(new SetUniqueList(superSubList, subSet));
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
/*     */   protected Set<E> createSetBasedOnList(Set<E> set, List<E> list) {
/*     */     Set<E> set1;
/* 334 */     if (set.getClass().equals(HashSet.class)) {
/* 335 */       set1 = new HashSet<E>(list.size());
/*     */     } else {
/*     */       try {
/* 338 */         set1 = (Set<E>)set.getClass().newInstance();
/* 339 */       } catch (InstantiationException ie) {
/* 340 */         set1 = new HashSet();
/* 341 */       } catch (IllegalAccessException iae) {
/* 342 */         set1 = new HashSet();
/*     */       } 
/*     */     } 
/* 345 */     set1.addAll(list);
/* 346 */     return set1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class SetListIterator<E>
/*     */     extends AbstractIteratorDecorator<E>
/*     */   {
/*     */     private final Set<E> set;
/*     */     
/* 356 */     private E last = null;
/*     */     
/*     */     protected SetListIterator(Iterator<E> it, Set<E> set) {
/* 359 */       super(it);
/* 360 */       this.set = set;
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 365 */       this.last = (E)super.next();
/* 366 */       return this.last;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 371 */       super.remove();
/* 372 */       this.set.remove(this.last);
/* 373 */       this.last = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class SetListListIterator<E>
/*     */     extends AbstractListIteratorDecorator<E>
/*     */   {
/*     */     private final Set<E> set;
/*     */     
/* 384 */     private E last = null;
/*     */     
/*     */     protected SetListListIterator(ListIterator<E> it, Set<E> set) {
/* 387 */       super(it);
/* 388 */       this.set = set;
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/* 393 */       this.last = (E)super.next();
/* 394 */       return this.last;
/*     */     }
/*     */ 
/*     */     
/*     */     public E previous() {
/* 399 */       this.last = (E)super.previous();
/* 400 */       return this.last;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 405 */       super.remove();
/* 406 */       this.set.remove(this.last);
/* 407 */       this.last = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void add(E object) {
/* 412 */       if (!this.set.contains(object)) {
/* 413 */         super.add(object);
/* 414 */         this.set.add(object);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void set(E object) {
/* 420 */       throw new UnsupportedOperationException("ListIterator does not support set");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\list\SetUniqueList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */