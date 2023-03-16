/*    */ package org.jsoup.nodes;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class CDataNode
/*    */   extends TextNode
/*    */ {
/*    */   public CDataNode(String text) {
/* 10 */     super(text);
/*    */   }
/*    */ 
/*    */   
/*    */   public String nodeName() {
/* 15 */     return "#cdata";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String text() {
/* 24 */     return getWholeText();
/*    */   }
/*    */ 
/*    */   
/*    */   void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
/* 29 */     accum
/* 30 */       .append("<![CDATA[")
/* 31 */       .append(getWholeText());
/*    */   }
/*    */ 
/*    */   
/*    */   void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
/* 36 */     accum.append("]]>");
/*    */   }
/*    */ 
/*    */   
/*    */   public CDataNode clone() {
/* 41 */     return (CDataNode)super.clone();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\CDataNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */