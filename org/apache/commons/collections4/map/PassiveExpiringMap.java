/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PassiveExpiringMap<K, V>
/*     */   extends AbstractMapDecorator<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public static interface ExpirationPolicy<K, V>
/*     */     extends Serializable
/*     */   {
/*     */     long expirationTime(K param1K, V param1V);
/*     */   }
/*     */   
/*     */   public static class ConstantTimeToLiveExpirationPolicy<K, V>
/*     */     implements ExpirationPolicy<K, V>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     private final long timeToLiveMillis;
/*     */     
/*     */     public ConstantTimeToLiveExpirationPolicy() {
/*  91 */       this(-1L);
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
/*     */     public ConstantTimeToLiveExpirationPolicy(long timeToLiveMillis) {
/* 107 */       this.timeToLiveMillis = timeToLiveMillis;
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
/*     */     
/*     */     public ConstantTimeToLiveExpirationPolicy(long timeToLive, TimeUnit timeUnit) {
/* 124 */       this(PassiveExpiringMap.validateAndConvertToMillis(timeToLive, timeUnit));
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
/*     */     public long expirationTime(K key, V value) {
/* 138 */       if (this.timeToLiveMillis >= 0L) {
/*     */         
/* 140 */         long now = System.currentTimeMillis();
/* 141 */         if (now > Long.MAX_VALUE - this.timeToLiveMillis)
/*     */         {
/*     */           
/* 144 */           return -1L;
/*     */         }
/*     */ 
/*     */         
/* 148 */         return now + this.timeToLiveMillis;
/*     */       } 
/*     */ 
/*     */       
/* 152 */       return -1L;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static long validateAndConvertToMillis(long timeToLive, TimeUnit timeUnit) {
/* 195 */     if (timeUnit == null) {
/* 196 */       throw new NullPointerException("Time unit must not be null");
/*     */     }
/* 198 */     return TimeUnit.MILLISECONDS.convert(timeToLive, timeUnit);
/*     */   }
/*     */ 
/*     */   
/* 202 */   private final Map<Object, Long> expirationMap = new HashMap<Object, Long>();
/*     */ 
/*     */ 
/*     */   
/*     */   private final ExpirationPolicy<K, V> expiringPolicy;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PassiveExpiringMap() {
/* 212 */     this(-1L);
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
/*     */   public PassiveExpiringMap(ExpirationPolicy<K, V> expiringPolicy) {
/* 224 */     this(expiringPolicy, new HashMap<K, V>());
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
/*     */   public PassiveExpiringMap(ExpirationPolicy<K, V> expiringPolicy, Map<K, V> map) {
/* 240 */     super(map);
/* 241 */     if (expiringPolicy == null) {
/* 242 */       throw new NullPointerException("Policy must not be null.");
/*     */     }
/* 244 */     this.expiringPolicy = expiringPolicy;
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
/*     */   public PassiveExpiringMap(long timeToLiveMillis) {
/* 258 */     this(new ConstantTimeToLiveExpirationPolicy<K, V>(timeToLiveMillis), new HashMap<K, V>());
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
/*     */   public PassiveExpiringMap(long timeToLiveMillis, Map<K, V> map) {
/* 277 */     this(new ConstantTimeToLiveExpirationPolicy<K, V>(timeToLiveMillis), map);
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
/*     */   public PassiveExpiringMap(long timeToLive, TimeUnit timeUnit) {
/* 294 */     this(validateAndConvertToMillis(timeToLive, timeUnit));
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
/*     */   public PassiveExpiringMap(long timeToLive, TimeUnit timeUnit, Map<K, V> map) {
/* 314 */     this(validateAndConvertToMillis(timeToLive, timeUnit), map);
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
/*     */   public PassiveExpiringMap(Map<K, V> map) {
/* 326 */     this(-1L, map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 335 */     super.clear();
/* 336 */     this.expirationMap.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 346 */     removeIfExpired(key, now());
/* 347 */     return super.containsKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object value) {
/* 357 */     removeAllExpired(now());
/* 358 */     return super.containsValue(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<K, V>> entrySet() {
/* 367 */     removeAllExpired(now());
/* 368 */     return super.entrySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 377 */     removeIfExpired(key, now());
/* 378 */     return super.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 387 */     removeAllExpired(now());
/* 388 */     return super.isEmpty();
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
/*     */   private boolean isExpired(long now, Long expirationTimeObject) {
/* 403 */     if (expirationTimeObject != null) {
/* 404 */       long expirationTime = expirationTimeObject.longValue();
/* 405 */       return (expirationTime >= 0L && now >= expirationTime);
/*     */     } 
/* 407 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<K> keySet() {
/* 416 */     removeAllExpired(now());
/* 417 */     return super.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long now() {
/* 424 */     return System.currentTimeMillis();
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
/*     */   public V put(K key, V value) {
/* 436 */     long expirationTime = this.expiringPolicy.expirationTime(key, value);
/* 437 */     this.expirationMap.put(key, Long.valueOf(expirationTime));
/*     */     
/* 439 */     return super.put(key, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void putAll(Map<? extends K, ? extends V> mapToCopy) {
/* 444 */     for (Map.Entry<? extends K, ? extends V> entry : mapToCopy.entrySet()) {
/* 445 */       put(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V remove(Object key) {
/* 456 */     this.expirationMap.remove(key);
/* 457 */     return super.remove(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeAllExpired(long now) {
/* 468 */     Iterator<Map.Entry<Object, Long>> iter = this.expirationMap.entrySet().iterator();
/* 469 */     while (iter.hasNext()) {
/* 470 */       Map.Entry<Object, Long> expirationEntry = iter.next();
/* 471 */       if (isExpired(now, expirationEntry.getValue())) {
/*     */         
/* 473 */         super.remove(expirationEntry.getKey());
/*     */         
/* 475 */         iter.remove();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeIfExpired(Object key, long now) {
/* 486 */     Long expirationTimeObject = this.expirationMap.get(key);
/* 487 */     if (isExpired(now, expirationTimeObject)) {
/* 488 */       remove(key);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 498 */     removeAllExpired(now());
/* 499 */     return super.size();
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 513 */     in.defaultReadObject();
/* 514 */     this.map = (Map<K, V>)in.readObject();
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
/* 525 */     out.defaultWriteObject();
/* 526 */     out.writeObject(this.map);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<V> values() {
/* 535 */     removeAllExpired(now());
/* 536 */     return super.values();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\PassiveExpiringMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */