/*     */ package org.apache.commons.collections4.splitmap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.Put;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ import org.apache.commons.collections4.map.LinkedMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransformedSplitMap<J, K, U, V>
/*     */   extends AbstractIterableGetMapDecorator<K, V>
/*     */   implements Put<J, U>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 5966875321133456994L;
/*     */   private final Transformer<? super J, ? extends K> keyTransformer;
/*     */   private final Transformer<? super U, ? extends V> valueTransformer;
/*     */   
/*     */   public static <J, K, U, V> TransformedSplitMap<J, K, U, V> transformingMap(Map<K, V> map, Transformer<? super J, ? extends K> keyTransformer, Transformer<? super U, ? extends V> valueTransformer) {
/*  91 */     return new TransformedSplitMap<J, K, U, V>(map, keyTransformer, valueTransformer);
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
/*     */   protected TransformedSplitMap(Map<K, V> map, Transformer<? super J, ? extends K> keyTransformer, Transformer<? super U, ? extends V> valueTransformer) {
/* 108 */     super(map);
/* 109 */     if (keyTransformer == null) {
/* 110 */       throw new NullPointerException("KeyTransformer must not be null.");
/*     */     }
/* 112 */     this.keyTransformer = keyTransformer;
/* 113 */     if (valueTransformer == null) {
/* 114 */       throw new NullPointerException("ValueTransformer must not be null.");
/*     */     }
/* 116 */     this.valueTransformer = valueTransformer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 127 */     out.defaultWriteObject();
/* 128 */     out.writeObject(decorated());
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
/* 141 */     in.defaultReadObject();
/* 142 */     this.map = (Map<K, V>)in.readObject();
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
/*     */   protected K transformKey(J object) {
/* 155 */     return (K)this.keyTransformer.transform(object);
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
/*     */   protected V transformValue(U object) {
/* 167 */     return (V)this.valueTransformer.transform(object);
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
/*     */   protected Map<K, V> transformMap(Map<? extends J, ? extends U> map) {
/* 180 */     if (map.isEmpty()) {
/* 181 */       return (Map)map;
/*     */     }
/* 183 */     LinkedMap<K, V> linkedMap = new LinkedMap(map.size());
/*     */     
/* 185 */     for (Map.Entry<? extends J, ? extends U> entry : map.entrySet()) {
/* 186 */       linkedMap.put(transformKey(entry.getKey()), transformValue(entry.getValue()));
/*     */     }
/* 188 */     return (Map<K, V>)linkedMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected V checkSetValue(U value) {
/* 198 */     return (V)this.valueTransformer.transform(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public V put(J key, U value) {
/* 203 */     return decorated().put(transformKey(key), transformValue(value));
/*     */   }
/*     */   
/*     */   public void putAll(Map<? extends J, ? extends U> mapToCopy) {
/* 207 */     decorated().putAll(transformMap(mapToCopy));
/*     */   }
/*     */   
/*     */   public void clear() {
/* 211 */     decorated().clear();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\splitmap\TransformedSplitMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */