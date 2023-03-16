/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.Predicate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PredicatedMap<K, V>
/*     */   extends AbstractInputCheckedMapDecorator<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 7412622456128415156L;
/*     */   protected final Predicate<? super K> keyPredicate;
/*     */   protected final Predicate<? super V> valuePredicate;
/*     */   
/*     */   public static <K, V> PredicatedMap<K, V> predicatedMap(Map<K, V> map, Predicate<? super K> keyPredicate, Predicate<? super V> valuePredicate) {
/*  81 */     return new PredicatedMap<K, V>(map, keyPredicate, valuePredicate);
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
/*     */   protected PredicatedMap(Map<K, V> map, Predicate<? super K> keyPredicate, Predicate<? super V> valuePredicate) {
/*  95 */     super(map);
/*  96 */     this.keyPredicate = keyPredicate;
/*  97 */     this.valuePredicate = valuePredicate;
/*     */     
/*  99 */     Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
/* 100 */     while (it.hasNext()) {
/* 101 */       Map.Entry<K, V> entry = it.next();
/* 102 */       validate(entry.getKey(), entry.getValue());
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
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 115 */     out.defaultWriteObject();
/* 116 */     out.writeObject(this.map);
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 129 */     in.defaultReadObject();
/* 130 */     this.map = (Map<K, V>)in.readObject();
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
/*     */   protected void validate(K key, V value) {
/* 142 */     if (this.keyPredicate != null && !this.keyPredicate.evaluate(key)) {
/* 143 */       throw new IllegalArgumentException("Cannot add key - Predicate rejected it");
/*     */     }
/* 145 */     if (this.valuePredicate != null && !this.valuePredicate.evaluate(value)) {
/* 146 */       throw new IllegalArgumentException("Cannot add value - Predicate rejected it");
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
/*     */   protected V checkSetValue(V value) {
/* 160 */     if (!this.valuePredicate.evaluate(value)) {
/* 161 */       throw new IllegalArgumentException("Cannot set value - Predicate rejected it");
/*     */     }
/* 163 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSetValueChecking() {
/* 174 */     return (this.valuePredicate != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public V put(K key, V value) {
/* 180 */     validate(key, value);
/* 181 */     return this.map.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> mapToCopy) {
/* 186 */     for (Map.Entry<? extends K, ? extends V> entry : mapToCopy.entrySet()) {
/* 187 */       validate(entry.getKey(), entry.getValue());
/*     */     }
/* 189 */     super.putAll(mapToCopy);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\PredicatedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */