/*    */ package org.jsoup.nodes;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DataNode
/*    */   extends LeafNode
/*    */ {
/*    */   public DataNode(String data) {
/* 16 */     this.value = data;
/*    */   }
/*    */   
/*    */   public String nodeName() {
/* 20 */     return "#data";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getWholeData() {
/* 28 */     return coreValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DataNode setWholeData(String data) {
/* 37 */     coreValue(data);
/* 38 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
/* 43 */     accum.append(getWholeData());
/*    */   }
/*    */ 
/*    */   
/*    */   void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {}
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     return outerHtml();
/*    */   }
/*    */ 
/*    */   
/*    */   public DataNode clone() {
/* 56 */     return (DataNode)super.clone();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\DataNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */