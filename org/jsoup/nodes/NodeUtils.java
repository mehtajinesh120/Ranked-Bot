/*    */ package org.jsoup.nodes;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.jsoup.helper.Validate;
/*    */ import org.jsoup.helper.W3CDom;
/*    */ import org.jsoup.parser.HtmlTreeBuilder;
/*    */ import org.jsoup.parser.Parser;
/*    */ import org.jsoup.parser.TreeBuilder;
/*    */ import org.w3c.dom.Document;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class NodeUtils
/*    */ {
/*    */   static Document.OutputSettings outputSettings(Node node) {
/* 22 */     Document owner = node.ownerDocument();
/* 23 */     return (owner != null) ? owner.outputSettings() : (new Document("")).outputSettings();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static Parser parser(Node node) {
/* 30 */     Document doc = node.ownerDocument();
/* 31 */     return (doc != null && doc.parser() != null) ? doc.parser() : new Parser((TreeBuilder)new HtmlTreeBuilder());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T extends Node> List<T> selectXpath(String xpath, Element el, Class<T> nodeType) {
/* 41 */     Validate.notEmpty(xpath);
/* 42 */     Validate.notNull(el);
/* 43 */     Validate.notNull(nodeType);
/*    */     
/* 45 */     W3CDom w3c = (new W3CDom()).namespaceAware(false);
/* 46 */     Document wDoc = w3c.fromJsoup(el);
/* 47 */     Node contextNode = w3c.contextNode(wDoc);
/* 48 */     NodeList nodeList = w3c.selectXpath(xpath, contextNode);
/* 49 */     return w3c.sourceNodes(nodeList, nodeType);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\NodeUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */