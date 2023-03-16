/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.Unmodifiable;
/*     */ import org.apache.commons.collections4.iterators.AbstractIteratorDecorator;
/*     */ import org.apache.commons.collections4.keyvalue.AbstractMapEntryDecorator;
/*     */ import org.apache.commons.collections4.set.AbstractSetDecorator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class UnmodifiableEntrySet<K, V>
/*     */   extends AbstractSetDecorator<Map.Entry<K, V>>
/*     */   implements Unmodifiable
/*     */ {
/*     */   private static final long serialVersionUID = 1678353579659253473L;
/*     */   
/*     */   public static <K, V> Set<Map.Entry<K, V>> unmodifiableEntrySet(Set<Map.Entry<K, V>> set) {
/*  55 */     if (set instanceof Unmodifiable) {
/*  56 */       return set;
/*     */     }
/*  58 */     return (Set)new UnmodifiableEntrySet<K, V>(set);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private UnmodifiableEntrySet(Set<Map.Entry<K, V>> set) {
/*  69 */     super(set);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(Map.Entry<K, V> object) {
/*  75 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends Map.Entry<K, V>> coll) {
/*  80 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  85 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object object) {
/*  90 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> coll) {
/*  95 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> coll) {
/* 100 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<Map.Entry<K, V>> iterator() {
/* 106 */     return (Iterator<Map.Entry<K, V>>)new UnmodifiableEntrySetIterator(decorated().iterator());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 112 */     Object[] array = decorated().toArray();
/* 113 */     for (int i = 0; i < array.length; i++) {
/* 114 */       array[i] = new UnmodifiableEntry((Map.Entry<K, V>)array[i]);
/*     */     }
/* 116 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] array) {
/* 122 */     T[] arrayOfT = array;
/* 123 */     if (array.length > 0)
/*     */     {
/*     */       
/* 126 */       arrayOfT = (T[])Array.newInstance(array.getClass().getComponentType(), 0);
/*     */     }
/* 128 */     arrayOfT = decorated().toArray((Object[])arrayOfT);
/* 129 */     for (int i = 0; i < arrayOfT.length; i++) {
/* 130 */       arrayOfT[i] = (T)new UnmodifiableEntry((Map.Entry<K, V>)arrayOfT[i]);
/*     */     }
/*     */ 
/*     */     
/* 134 */     if (arrayOfT.length > array.length) {
/* 135 */       return arrayOfT;
/*     */     }
/*     */ 
/*     */     
/* 139 */     System.arraycopy(arrayOfT, 0, array, 0, arrayOfT.length);
/* 140 */     if (array.length > arrayOfT.length) {
/* 141 */       array[arrayOfT.length] = null;
/*     */     }
/* 143 */     return array;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class UnmodifiableEntrySetIterator
/*     */     extends AbstractIteratorDecorator<Map.Entry<K, V>>
/*     */   {
/*     */     protected UnmodifiableEntrySetIterator(Iterator<Map.Entry<K, V>> iterator) {
/* 153 */       super(iterator);
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<K, V> next() {
/* 158 */       return (Map.Entry<K, V>)new UnmodifiableEntrySet.UnmodifiableEntry(getIterator().next());
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 163 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class UnmodifiableEntry
/*     */     extends AbstractMapEntryDecorator<K, V>
/*     */   {
/*     */     protected UnmodifiableEntry(Map.Entry<K, V> entry) {
/* 174 */       super(entry);
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V obj) {
/* 179 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\UnmodifiableEntrySet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */