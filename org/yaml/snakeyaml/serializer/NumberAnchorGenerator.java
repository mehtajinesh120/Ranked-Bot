/*    */ package org.yaml.snakeyaml.serializer;
/*    */ 
/*    */ import java.text.NumberFormat;
/*    */ import org.yaml.snakeyaml.nodes.Node;
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
/*    */ public class NumberAnchorGenerator
/*    */   implements AnchorGenerator
/*    */ {
/* 21 */   private int lastAnchorId = 0;
/*    */   
/*    */   public NumberAnchorGenerator(int lastAnchorId) {
/* 24 */     this.lastAnchorId = lastAnchorId;
/*    */   }
/*    */   
/*    */   public String nextAnchor(Node node) {
/* 28 */     this.lastAnchorId++;
/* 29 */     NumberFormat format = NumberFormat.getNumberInstance();
/* 30 */     format.setMinimumIntegerDigits(3);
/* 31 */     format.setMaximumFractionDigits(0);
/* 32 */     format.setGroupingUsed(false);
/* 33 */     String anchorId = format.format(this.lastAnchorId);
/* 34 */     return "id" + anchorId;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\serializer\NumberAnchorGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */