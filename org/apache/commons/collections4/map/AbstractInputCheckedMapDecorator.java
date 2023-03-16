/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class AbstractInputCheckedMapDecorator<K, V>
/*     */   extends AbstractMapDecorator<K, V>
/*     */ {
/*     */   protected AbstractInputCheckedMapDecorator() {}
/*     */   
/*     */   protected AbstractInputCheckedMapDecorator(Map<K, V> map) {
/*  62 */     super(map);
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
/*     */   protected abstract V checkSetValue(V paramV);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSetValueChecking() {
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/*  99 */     if (isSetValueChecking()) {
/* 100 */       return (Set<Map.Entry<K, V>>)new EntrySet(this.map.entrySet(), this);
/*     */     }
/* 102 */     return this.map.entrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class EntrySet
/*     */     extends AbstractSetDecorator<Map.Entry<K, V>>
/*     */   {
/*     */     private static final long serialVersionUID = 4354731610923110264L;
/*     */ 
/*     */     
/*     */     private final AbstractInputCheckedMapDecorator<K, V> parent;
/*     */ 
/*     */ 
/*     */     
/*     */     protected EntrySet(Set<Map.Entry<K, V>> set, AbstractInputCheckedMapDecorator<K, V> parent) {
/* 118 */       super(set);
/* 119 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<Map.Entry<K, V>> iterator() {
/* 124 */       return (Iterator<Map.Entry<K, V>>)new AbstractInputCheckedMapDecorator.EntrySetIterator(decorated().iterator(), this.parent);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object[] toArray() {
/* 130 */       Object[] array = decorated().toArray();
/* 131 */       for (int i = 0; i < array.length; i++) {
/* 132 */         array[i] = new AbstractInputCheckedMapDecorator.MapEntry((Map.Entry<K, V>)array[i], this.parent);
/*     */       }
/* 134 */       return array;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T[] toArray(T[] array) {
/* 140 */       T[] arrayOfT = array;
/* 141 */       if (array.length > 0)
/*     */       {
/*     */         
/* 144 */         arrayOfT = (T[])Array.newInstance(array.getClass().getComponentType(), 0);
/*     */       }
/* 146 */       arrayOfT = decorated().toArray((Object[])arrayOfT);
/* 147 */       for (int i = 0; i < arrayOfT.length; i++) {
/* 148 */         arrayOfT[i] = (T)new AbstractInputCheckedMapDecorator.MapEntry((Map.Entry<K, V>)arrayOfT[i], this.parent);
/*     */       }
/*     */ 
/*     */       
/* 152 */       if (arrayOfT.length > array.length) {
/* 153 */         return arrayOfT;
/*     */       }
/*     */ 
/*     */       
/* 157 */       System.arraycopy(arrayOfT, 0, array, 0, arrayOfT.length);
/* 158 */       if (array.length > arrayOfT.length) {
/* 159 */         array[arrayOfT.length] = null;
/*     */       }
/* 161 */       return array;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class EntrySetIterator
/*     */     extends AbstractIteratorDecorator<Map.Entry<K, V>>
/*     */   {
/*     */     private final AbstractInputCheckedMapDecorator<K, V> parent;
/*     */ 
/*     */ 
/*     */     
/*     */     protected EntrySetIterator(Iterator<Map.Entry<K, V>> iterator, AbstractInputCheckedMapDecorator<K, V> parent) {
/* 175 */       super(iterator);
/* 176 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public Map.Entry<K, V> next() {
/* 181 */       Map.Entry<K, V> entry = getIterator().next();
/* 182 */       return (Map.Entry<K, V>)new AbstractInputCheckedMapDecorator.MapEntry(entry, this.parent);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class MapEntry
/*     */     extends AbstractMapEntryDecorator<K, V>
/*     */   {
/*     */     private final AbstractInputCheckedMapDecorator<K, V> parent;
/*     */ 
/*     */     
/*     */     protected MapEntry(Map.Entry<K, V> entry, AbstractInputCheckedMapDecorator<K, V> parent) {
/* 195 */       super(entry);
/* 196 */       this.parent = parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public V setValue(V value) {
/* 201 */       value = this.parent.checkSetValue(value);
/* 202 */       return (V)getMapEntry().setValue(value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\AbstractInputCheckedMapDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */