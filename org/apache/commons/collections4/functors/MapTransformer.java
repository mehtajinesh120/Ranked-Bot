/*    */ package org.apache.commons.collections4.functors;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Map;
/*    */ import org.apache.commons.collections4.Transformer;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MapTransformer<I, O>
/*    */   implements Transformer<I, O>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 862391807045468939L;
/*    */   private final Map<? super I, ? extends O> iMap;
/*    */   
/*    */   public static <I, O> Transformer<I, O> mapTransformer(Map<? super I, ? extends O> map) {
/* 50 */     if (map == null) {
/* 51 */       return ConstantTransformer.nullTransformer();
/*    */     }
/* 53 */     return new MapTransformer<I, O>(map);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private MapTransformer(Map<? super I, ? extends O> map) {
/* 64 */     this.iMap = map;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public O transform(I input) {
/* 74 */     return this.iMap.get(input);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<? super I, ? extends O> getMap() {
/* 84 */     return this.iMap;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\MapTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */