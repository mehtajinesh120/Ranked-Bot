/*    */ package org.apache.commons.collections4.bidimap;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.collections4.BidiMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DualLinkedHashBidiMap<K, V>
/*    */   extends AbstractDualBidiMap<K, V>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 721969328361810L;
/*    */   
/*    */   public DualLinkedHashBidiMap() {
/* 46 */     super(new LinkedHashMap<K, V>(), new LinkedHashMap<V, K>());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DualLinkedHashBidiMap(Map<? extends K, ? extends V> map) {
/* 56 */     super(new LinkedHashMap<K, V>(), new LinkedHashMap<V, K>());
/* 57 */     putAll(map);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected DualLinkedHashBidiMap(Map<K, V> normalMap, Map<V, K> reverseMap, BidiMap<V, K> inverseBidiMap) {
/* 69 */     super(normalMap, reverseMap, inverseBidiMap);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected BidiMap<V, K> createBidiMap(Map<V, K> normalMap, Map<K, V> reverseMap, BidiMap<K, V> inverseBidiMap) {
/* 83 */     return new DualLinkedHashBidiMap(normalMap, reverseMap, inverseBidiMap);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 89 */     out.defaultWriteObject();
/* 90 */     out.writeObject(this.normalMap);
/*    */   }
/*    */   
/*    */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 94 */     in.defaultReadObject();
/* 95 */     this.normalMap = new LinkedHashMap<K, V>();
/* 96 */     this.reverseMap = new LinkedHashMap<V, K>();
/*    */     
/* 98 */     Map<K, V> map = (Map<K, V>)in.readObject();
/* 99 */     putAll(map);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bidimap\DualLinkedHashBidiMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */