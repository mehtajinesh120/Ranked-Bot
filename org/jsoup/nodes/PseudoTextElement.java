/*    */ package org.jsoup.nodes;
/*    */ 
/*    */ import org.jsoup.parser.Tag;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PseudoTextElement
/*    */   extends Element
/*    */ {
/*    */   public PseudoTextElement(Tag tag, String baseUri, Attributes attributes) {
/* 12 */     super(tag, baseUri, attributes);
/*    */   }
/*    */   
/*    */   void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) {}
/*    */   
/*    */   void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {}
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\PseudoTextElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */