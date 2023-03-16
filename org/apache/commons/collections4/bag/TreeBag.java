/*     */ package org.apache.commons.collections4.bag;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import org.apache.commons.collections4.SortedBag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TreeBag<E>
/*     */   extends AbstractMapBag<E>
/*     */   implements SortedBag<E>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7740146511091606676L;
/*     */   
/*     */   public TreeBag() {
/*  53 */     super(new TreeMap<E, AbstractMapBag.MutableInteger>());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TreeBag(Comparator<? super E> comparator) {
/*  63 */     super(new TreeMap<E, AbstractMapBag.MutableInteger>(comparator));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TreeBag(Collection<? extends E> coll) {
/*  73 */     this();
/*  74 */     addAll(coll);
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
/*     */   public boolean add(E object) {
/*  88 */     if (comparator() == null && !(object instanceof Comparable)) {
/*  89 */       if (object == null) {
/*  90 */         throw new NullPointerException();
/*     */       }
/*  92 */       throw new IllegalArgumentException("Objects of type " + object.getClass() + " cannot be added to " + "a naturally ordered TreeBag as it does not implement Comparable");
/*     */     } 
/*     */     
/*  95 */     return super.add(object);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E first() {
/* 102 */     return getMap().firstKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/* 107 */     return getMap().lastKey();
/*     */   }
/*     */ 
/*     */   
/*     */   public Comparator<? super E> comparator() {
/* 112 */     return getMap().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   protected SortedMap<E, AbstractMapBag.MutableInteger> getMap() {
/* 117 */     return (SortedMap<E, AbstractMapBag.MutableInteger>)super.getMap();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 125 */     out.defaultWriteObject();
/* 126 */     out.writeObject(comparator());
/* 127 */     doWriteObject(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 134 */     in.defaultReadObject();
/*     */     
/* 136 */     Comparator<? super E> comp = (Comparator<? super E>)in.readObject();
/* 137 */     doReadObject(new TreeMap<E, AbstractMapBag.MutableInteger>(comp), in);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bag\TreeBag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */