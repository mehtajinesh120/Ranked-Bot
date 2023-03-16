/*     */ package org.apache.commons.collections4.map;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.Factory;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ import org.apache.commons.collections4.functors.ConstantTransformer;
/*     */ import org.apache.commons.collections4.functors.FactoryTransformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultedMap<K, V>
/*     */   extends AbstractMapDecorator<K, V>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 19698628745827L;
/*     */   private final Transformer<? super K, ? extends V> value;
/*     */   
/*     */   public static <K, V> DefaultedMap<K, V> defaultedMap(Map<K, V> map, V defaultValue) {
/*  87 */     return new DefaultedMap<K, V>(map, ConstantTransformer.constantTransformer(defaultValue));
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
/*     */   public static <K, V> DefaultedMap<K, V> defaultedMap(Map<K, V> map, Factory<? extends V> factory) {
/* 105 */     if (factory == null) {
/* 106 */       throw new IllegalArgumentException("Factory must not be null");
/*     */     }
/* 108 */     return new DefaultedMap<K, V>(map, FactoryTransformer.factoryTransformer(factory));
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
/*     */   public static <K, V> Map<K, V> defaultedMap(Map<K, V> map, Transformer<? super K, ? extends V> transformer) {
/* 128 */     if (transformer == null) {
/* 129 */       throw new IllegalArgumentException("Transformer must not be null");
/*     */     }
/* 131 */     return (Map<K, V>)new DefaultedMap<K, V>(map, transformer);
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
/*     */   public DefaultedMap(V defaultValue) {
/* 145 */     this(ConstantTransformer.constantTransformer(defaultValue));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultedMap(Transformer<? super K, ? extends V> defaultValueTransformer) {
/* 154 */     this(new HashMap<K, V>(), defaultValueTransformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected DefaultedMap(Map<K, V> map, Transformer<? super K, ? extends V> defaultValueTransformer) {
/* 165 */     super(map);
/* 166 */     if (defaultValueTransformer == null) {
/* 167 */       throw new NullPointerException("Transformer must not be null.");
/*     */     }
/* 169 */     this.value = defaultValueTransformer;
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
/* 180 */     out.defaultWriteObject();
/* 181 */     out.writeObject(this.map);
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
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 193 */     in.defaultReadObject();
/* 194 */     this.map = (Map<K, V>)in.readObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public V get(Object key) {
/* 202 */     if (!this.map.containsKey(key)) {
/* 203 */       return (V)this.value.transform(key);
/*     */     }
/* 205 */     return this.map.get(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\map\DefaultedMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */