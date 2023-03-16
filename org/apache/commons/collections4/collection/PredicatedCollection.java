/*     */ package org.apache.commons.collections4.collection;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.Bag;
/*     */ import org.apache.commons.collections4.MultiSet;
/*     */ import org.apache.commons.collections4.Predicate;
/*     */ import org.apache.commons.collections4.bag.HashBag;
/*     */ import org.apache.commons.collections4.bag.PredicatedBag;
/*     */ import org.apache.commons.collections4.functors.NotNullPredicate;
/*     */ import org.apache.commons.collections4.list.PredicatedList;
/*     */ import org.apache.commons.collections4.multiset.HashMultiSet;
/*     */ import org.apache.commons.collections4.multiset.PredicatedMultiSet;
/*     */ import org.apache.commons.collections4.queue.PredicatedQueue;
/*     */ import org.apache.commons.collections4.set.PredicatedSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PredicatedCollection<E>
/*     */   extends AbstractCollectionDecorator<E>
/*     */ {
/*     */   private static final long serialVersionUID = -5259182142076705162L;
/*     */   protected final Predicate<? super E> predicate;
/*     */   
/*     */   public static <E> Builder<E> builder(Predicate<? super E> predicate) {
/*  76 */     return new Builder<E>(predicate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <E> Builder<E> notNullBuilder() {
/*  87 */     return new Builder<E>(NotNullPredicate.notNullPredicate());
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
/*     */   public static <T> PredicatedCollection<T> predicatedCollection(Collection<T> coll, Predicate<? super T> predicate) {
/* 106 */     return new PredicatedCollection<T>(coll, predicate);
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
/*     */   protected PredicatedCollection(Collection<E> coll, Predicate<? super E> predicate) {
/* 122 */     super(coll);
/* 123 */     if (predicate == null) {
/* 124 */       throw new NullPointerException("Predicate must not be null.");
/*     */     }
/* 126 */     this.predicate = predicate;
/* 127 */     for (E item : coll) {
/* 128 */       validate(item);
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
/*     */   protected void validate(E object) {
/* 142 */     if (!this.predicate.evaluate(object)) {
/* 143 */       throw new IllegalArgumentException("Cannot add Object '" + object + "' - Predicate '" + this.predicate + "' rejected it");
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
/*     */   public boolean add(E object) {
/* 159 */     validate(object);
/* 160 */     return decorated().add(object);
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
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 174 */     for (E item : coll) {
/* 175 */       validate(item);
/*     */     }
/* 177 */     return decorated().addAll(coll);
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
/*     */   public static class Builder<E>
/*     */   {
/*     */     private final Predicate<? super E> predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 212 */     private final List<E> accepted = new ArrayList<E>();
/*     */ 
/*     */     
/* 215 */     private final List<E> rejected = new ArrayList<E>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder(Predicate<? super E> predicate) {
/* 225 */       if (predicate == null) {
/* 226 */         throw new NullPointerException("Predicate must not be null");
/*     */       }
/* 228 */       this.predicate = predicate;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> add(E item) {
/* 241 */       if (this.predicate.evaluate(item)) {
/* 242 */         this.accepted.add(item);
/*     */       } else {
/* 244 */         this.rejected.add(item);
/*     */       } 
/* 246 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder<E> addAll(Collection<? extends E> items) {
/* 259 */       if (items != null) {
/* 260 */         for (E item : items) {
/* 261 */           add(item);
/*     */         }
/*     */       }
/* 264 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<E> createPredicatedList() {
/* 276 */       return createPredicatedList(new ArrayList<E>());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public List<E> createPredicatedList(List<E> list) {
/* 292 */       if (list == null) {
/* 293 */         throw new NullPointerException("List must not be null.");
/*     */       }
/* 295 */       PredicatedList<E> predicatedList = PredicatedList.predicatedList(list, this.predicate);
/* 296 */       predicatedList.addAll(this.accepted);
/* 297 */       return (List<E>)predicatedList;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<E> createPredicatedSet() {
/* 309 */       return createPredicatedSet(new HashSet<E>());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Set<E> createPredicatedSet(Set<E> set) {
/* 325 */       if (set == null) {
/* 326 */         throw new NullPointerException("Set must not be null.");
/*     */       }
/* 328 */       PredicatedSet<E> predicatedSet = PredicatedSet.predicatedSet(set, this.predicate);
/* 329 */       predicatedSet.addAll(this.accepted);
/* 330 */       return (Set<E>)predicatedSet;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultiSet<E> createPredicatedMultiSet() {
/* 342 */       return createPredicatedMultiSet((MultiSet<E>)new HashMultiSet());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public MultiSet<E> createPredicatedMultiSet(MultiSet<E> multiset) {
/* 358 */       if (multiset == null) {
/* 359 */         throw new NullPointerException("MultiSet must not be null.");
/*     */       }
/* 361 */       PredicatedMultiSet<E> predicatedMultiSet = PredicatedMultiSet.predicatedMultiSet(multiset, this.predicate);
/*     */       
/* 363 */       predicatedMultiSet.addAll(this.accepted);
/* 364 */       return (MultiSet<E>)predicatedMultiSet;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Bag<E> createPredicatedBag() {
/* 376 */       return createPredicatedBag((Bag<E>)new HashBag());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Bag<E> createPredicatedBag(Bag<E> bag) {
/* 392 */       if (bag == null) {
/* 393 */         throw new NullPointerException("Bag must not be null.");
/*     */       }
/* 395 */       PredicatedBag<E> predicatedBag = PredicatedBag.predicatedBag(bag, this.predicate);
/* 396 */       predicatedBag.addAll(this.accepted);
/* 397 */       return (Bag<E>)predicatedBag;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Queue<E> createPredicatedQueue() {
/* 409 */       return createPredicatedQueue(new LinkedList<E>());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Queue<E> createPredicatedQueue(Queue<E> queue) {
/* 425 */       if (queue == null) {
/* 426 */         throw new NullPointerException("queue must not be null");
/*     */       }
/* 428 */       PredicatedQueue<E> predicatedQueue = PredicatedQueue.predicatedQueue(queue, this.predicate);
/* 429 */       predicatedQueue.addAll(this.accepted);
/* 430 */       return (Queue<E>)predicatedQueue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Collection<E> rejectedElements() {
/* 439 */       return Collections.unmodifiableCollection(this.rejected);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\collection\PredicatedCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */