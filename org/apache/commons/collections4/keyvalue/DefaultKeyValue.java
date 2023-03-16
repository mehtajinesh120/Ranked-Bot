/*     */ package org.apache.commons.collections4.keyvalue;
/*     */ 
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.KeyValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultKeyValue<K, V>
/*     */   extends AbstractKeyValue<K, V>
/*     */ {
/*     */   public DefaultKeyValue() {
/*  39 */     super(null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultKeyValue(K key, V value) {
/*  49 */     super(key, value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultKeyValue(KeyValue<? extends K, ? extends V> pair) {
/*  59 */     super((K)pair.getKey(), (V)pair.getValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultKeyValue(Map.Entry<? extends K, ? extends V> entry) {
/*  69 */     super(entry.getKey(), entry.getValue());
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
/*     */   public K setKey(K key) {
/*  82 */     if (key == this) {
/*  83 */       throw new IllegalArgumentException("DefaultKeyValue may not contain itself as a key.");
/*     */     }
/*     */     
/*  86 */     return super.setKey(key);
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
/*     */   public V setValue(V value) {
/*  98 */     if (value == this) {
/*  99 */       throw new IllegalArgumentException("DefaultKeyValue may not contain itself as a value.");
/*     */     }
/*     */     
/* 102 */     return super.setValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map.Entry<K, V> toMapEntry() {
/* 112 */     return new DefaultMapEntry<K, V>(this);
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
/*     */   public boolean equals(Object obj) {
/* 127 */     if (obj == this) {
/* 128 */       return true;
/*     */     }
/* 130 */     if (!(obj instanceof DefaultKeyValue)) {
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     DefaultKeyValue<?, ?> other = (DefaultKeyValue<?, ?>)obj;
/* 135 */     return (((getKey() == null) ? (other.getKey() == null) : getKey().equals(other.getKey())) && ((getValue() == null) ? (other.getValue() == null) : getValue().equals(other.getValue())));
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
/*     */   public int hashCode() {
/* 150 */     return ((getKey() == null) ? 0 : getKey().hashCode()) ^ ((getValue() == null) ? 0 : getValue().hashCode());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\keyvalue\DefaultKeyValue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */