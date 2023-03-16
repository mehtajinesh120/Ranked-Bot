/*     */ package org.apache.commons.collections4.multiset;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.AbstractCollection;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.IteratorUtils;
/*     */ import org.apache.commons.collections4.MultiSet;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractMultiSet<E>
/*     */   extends AbstractCollection<E>
/*     */   implements MultiSet<E>
/*     */ {
/*     */   private transient Set<E> uniqueSet;
/*     */   private transient Set<MultiSet.Entry<E>> entrySet;
/*     */   
/*     */   public int size() {
/*  61 */     int totalSize = 0;
/*  62 */     for (MultiSet.Entry<E> entry : entrySet()) {
/*  63 */       totalSize += entry.getCount();
/*     */     }
/*  65 */     return totalSize;
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
/*     */   public int getCount(Object object) {
/*  77 */     for (MultiSet.Entry<E> entry : entrySet()) {
/*  78 */       E element = (E)entry.getElement();
/*  79 */       if (element == object || (element != null && element.equals(object)))
/*     */       {
/*  81 */         return entry.getCount();
/*     */       }
/*     */     } 
/*  84 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int setCount(E object, int count) {
/*  89 */     if (count < 0) {
/*  90 */       throw new IllegalArgumentException("Count must not be negative.");
/*     */     }
/*     */     
/*  93 */     int oldCount = getCount(object);
/*  94 */     if (oldCount < count) {
/*  95 */       add(object, count - oldCount);
/*     */     } else {
/*  97 */       remove(object, oldCount - count);
/*     */     } 
/*  99 */     return oldCount;
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
/*     */   public boolean contains(Object object) {
/* 111 */     return (getCount(object) > 0);
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
/*     */   public Iterator<E> iterator() {
/* 123 */     return new MultiSetIterator<E>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class MultiSetIterator<E>
/*     */     implements Iterator<E>
/*     */   {
/*     */     private final AbstractMultiSet<E> parent;
/*     */     
/*     */     private final Iterator<MultiSet.Entry<E>> entryIterator;
/*     */     
/*     */     private MultiSet.Entry<E> current;
/*     */     
/*     */     private int itemCount;
/*     */     
/*     */     private boolean canRemove;
/*     */ 
/*     */     
/*     */     public MultiSetIterator(AbstractMultiSet<E> parent) {
/* 142 */       this.parent = parent;
/* 143 */       this.entryIterator = parent.entrySet().iterator();
/* 144 */       this.current = null;
/* 145 */       this.canRemove = false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 151 */       return (this.itemCount > 0 || this.entryIterator.hasNext());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E next() {
/* 157 */       if (this.itemCount == 0) {
/* 158 */         this.current = this.entryIterator.next();
/* 159 */         this.itemCount = this.current.getCount();
/*     */       } 
/* 161 */       this.canRemove = true;
/* 162 */       this.itemCount--;
/* 163 */       return (E)this.current.getElement();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void remove() {
/* 169 */       if (!this.canRemove) {
/* 170 */         throw new IllegalStateException();
/*     */       }
/* 172 */       int count = this.current.getCount();
/* 173 */       if (count > 1) {
/* 174 */         this.parent.remove(this.current.getElement());
/*     */       } else {
/* 176 */         this.entryIterator.remove();
/*     */       } 
/* 178 */       this.canRemove = false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E object) {
/* 185 */     add(object, 1);
/* 186 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int add(E object, int occurrences) {
/* 191 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 200 */     Iterator<MultiSet.Entry<E>> it = entrySet().iterator();
/* 201 */     while (it.hasNext()) {
/* 202 */       it.next();
/* 203 */       it.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/* 209 */     return (remove(object, 1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public int remove(Object object, int occurrences) {
/* 214 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/* 219 */     boolean result = false;
/* 220 */     Iterator<?> i = coll.iterator();
/* 221 */     while (i.hasNext()) {
/* 222 */       Object obj = i.next();
/* 223 */       boolean changed = (remove(obj, getCount(obj)) != 0);
/* 224 */       result = (result || changed);
/*     */     } 
/* 226 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<E> uniqueSet() {
/* 237 */     if (this.uniqueSet == null) {
/* 238 */       this.uniqueSet = createUniqueSet();
/*     */     }
/* 240 */     return this.uniqueSet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<E> createUniqueSet() {
/* 249 */     return new UniqueSet<E>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Iterator<E> createUniqueSetIterator() {
/* 259 */     Transformer<MultiSet.Entry<E>, E> transformer = new Transformer<MultiSet.Entry<E>, E>()
/*     */       {
/*     */         public E transform(MultiSet.Entry<E> entry) {
/* 262 */           return (E)entry.getElement();
/*     */         }
/*     */       };
/* 265 */     return IteratorUtils.transformedIterator(entrySet().iterator(), transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<MultiSet.Entry<E>> entrySet() {
/* 275 */     if (this.entrySet == null) {
/* 276 */       this.entrySet = createEntrySet();
/*     */     }
/* 278 */     return this.entrySet;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<MultiSet.Entry<E>> createEntrySet() {
/* 287 */     return new EntrySet<E>(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract int uniqueElements();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Iterator<MultiSet.Entry<E>> createEntrySetIterator();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class UniqueSet<E>
/*     */     extends AbstractSet<E>
/*     */   {
/*     */     protected final AbstractMultiSet<E> parent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected UniqueSet(AbstractMultiSet<E> parent) {
/* 320 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<E> iterator() {
/* 325 */       return this.parent.createUniqueSetIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object key) {
/* 330 */       return this.parent.contains(key);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean containsAll(Collection<?> coll) {
/* 335 */       return this.parent.containsAll(coll);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object key) {
/* 340 */       return (this.parent.remove(key, this.parent.getCount(key)) != 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 345 */       return this.parent.uniqueElements();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 350 */       this.parent.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static class EntrySet<E>
/*     */     extends AbstractSet<MultiSet.Entry<E>>
/*     */   {
/*     */     private final AbstractMultiSet<E> parent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     protected EntrySet(AbstractMultiSet<E> parent) {
/* 368 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 373 */       return this.parent.uniqueElements();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<MultiSet.Entry<E>> iterator() {
/* 378 */       return this.parent.createEntrySetIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object obj) {
/* 383 */       if (!(obj instanceof MultiSet.Entry)) {
/* 384 */         return false;
/*     */       }
/* 386 */       MultiSet.Entry<?> entry = (MultiSet.Entry)obj;
/* 387 */       Object element = entry.getElement();
/* 388 */       return (this.parent.getCount(element) == entry.getCount());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean remove(Object obj) {
/* 393 */       if (!(obj instanceof MultiSet.Entry)) {
/* 394 */         return false;
/*     */       }
/* 396 */       MultiSet.Entry<?> entry = (MultiSet.Entry)obj;
/* 397 */       Object element = entry.getElement();
/* 398 */       if (this.parent.contains(element)) {
/* 399 */         int count = this.parent.getCount(element);
/* 400 */         if (entry.getCount() == count) {
/* 401 */           this.parent.remove(element, count);
/* 402 */           return true;
/*     */         } 
/*     */       } 
/* 405 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static abstract class AbstractEntry<E>
/*     */     implements MultiSet.Entry<E>
/*     */   {
/*     */     public boolean equals(Object object) {
/* 416 */       if (object instanceof MultiSet.Entry) {
/* 417 */         MultiSet.Entry<?> other = (MultiSet.Entry)object;
/* 418 */         E element = (E)getElement();
/* 419 */         Object otherElement = other.getElement();
/*     */         
/* 421 */         return (getCount() == other.getCount() && (element == otherElement || (element != null && element.equals(otherElement))));
/*     */       } 
/*     */ 
/*     */       
/* 425 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 430 */       E element = (E)getElement();
/* 431 */       return ((element == null) ? 0 : element.hashCode()) ^ getCount();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 436 */       return String.format("%s:%d", new Object[] { getElement(), Integer.valueOf(getCount()) });
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
/*     */   protected void doWriteObject(ObjectOutputStream out) throws IOException {
/* 448 */     out.writeInt(entrySet().size());
/* 449 */     for (MultiSet.Entry<E> entry : entrySet()) {
/* 450 */       out.writeObject(entry.getElement());
/* 451 */       out.writeInt(entry.getCount());
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
/*     */   protected void doReadObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 464 */     int entrySize = in.readInt();
/* 465 */     for (int i = 0; i < entrySize; i++) {
/*     */       
/* 467 */       E obj = (E)in.readObject();
/* 468 */       int count = in.readInt();
/* 469 */       setCount(obj, count);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/* 476 */     if (object == this) {
/* 477 */       return true;
/*     */     }
/* 479 */     if (!(object instanceof MultiSet)) {
/* 480 */       return false;
/*     */     }
/* 482 */     MultiSet<?> other = (MultiSet)object;
/* 483 */     if (other.size() != size()) {
/* 484 */       return false;
/*     */     }
/* 486 */     for (MultiSet.Entry<E> entry : entrySet()) {
/* 487 */       if (other.getCount(entry.getElement()) != getCount(entry.getElement())) {
/* 488 */         return false;
/*     */       }
/*     */     } 
/* 491 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 496 */     return entrySet().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 506 */     return entrySet().toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multiset\AbstractMultiSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */