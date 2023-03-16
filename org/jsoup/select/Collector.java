/*    */ package org.jsoup.select;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import org.jsoup.nodes.Element;
/*    */ import org.jsoup.nodes.Node;
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
/*    */ public class Collector
/*    */ {
/*    */   public static Elements collect(Evaluator eval, Element root) {
/* 27 */     Elements elements = new Elements();
/* 28 */     NodeTraversor.traverse((node, depth) -> { if (node instanceof Element) { Element el = (Element)node; if (eval.matches(root, el)) elements.add(el);  }  }(Node)root);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 35 */     return elements;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public static Element findFirst(Evaluator eval, Element root) {
/* 46 */     FirstFinder finder = new FirstFinder(eval);
/* 47 */     return finder.find(root, root);
/*    */   }
/*    */   
/*    */   static class FirstFinder implements NodeFilter { @Nullable
/* 51 */     private Element evalRoot = null; @Nullable
/* 52 */     private Element match = null;
/*    */     private final Evaluator eval;
/*    */     
/*    */     FirstFinder(Evaluator eval) {
/* 56 */       this.eval = eval;
/*    */     }
/*    */     @Nullable
/*    */     Element find(Element root, Element start) {
/* 60 */       this.evalRoot = root;
/* 61 */       this.match = null;
/* 62 */       NodeTraversor.filter(this, (Node)start);
/* 63 */       return this.match;
/*    */     }
/*    */ 
/*    */     
/*    */     public NodeFilter.FilterResult head(Node node, int depth) {
/* 68 */       if (node instanceof Element) {
/* 69 */         Element el = (Element)node;
/* 70 */         if (this.eval.matches(this.evalRoot, el)) {
/* 71 */           this.match = el;
/* 72 */           return NodeFilter.FilterResult.STOP;
/*    */         } 
/*    */       } 
/* 75 */       return NodeFilter.FilterResult.CONTINUE;
/*    */     }
/*    */ 
/*    */     
/*    */     public NodeFilter.FilterResult tail(Node node, int depth) {
/* 80 */       return NodeFilter.FilterResult.CONTINUE;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\select\Collector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */