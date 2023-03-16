/*    */ package org.jsoup.nodes;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nullable;
/*    */ import org.jsoup.parser.ParseSettings;
/*    */ import org.jsoup.parser.Parser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Comment
/*    */   extends LeafNode
/*    */ {
/*    */   public Comment(String data) {
/* 19 */     this.value = data;
/*    */   }
/*    */   
/*    */   public String nodeName() {
/* 23 */     return "#comment";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getData() {
/* 31 */     return coreValue();
/*    */   }
/*    */   
/*    */   public Comment setData(String data) {
/* 35 */     coreValue(data);
/* 36 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
/* 41 */     if (out.prettyPrint() && ((siblingIndex() == 0 && this.parentNode instanceof Element && ((Element)this.parentNode).tag().formatAsBlock()) || out.outline()))
/* 42 */       indent(accum, depth, out); 
/* 43 */     accum
/* 44 */       .append("<!--")
/* 45 */       .append(getData())
/* 46 */       .append("-->");
/*    */   }
/*    */ 
/*    */   
/*    */   void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {}
/*    */ 
/*    */   
/*    */   public String toString() {
/* 54 */     return outerHtml();
/*    */   }
/*    */ 
/*    */   
/*    */   public Comment clone() {
/* 59 */     return (Comment)super.clone();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isXmlDeclaration() {
/* 67 */     String data = getData();
/* 68 */     return isXmlDeclarationData(data);
/*    */   }
/*    */   
/*    */   private static boolean isXmlDeclarationData(String data) {
/* 72 */     return (data.length() > 1 && (data.startsWith("!") || data.startsWith("?")));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public XmlDeclaration asXmlDeclaration() {
/* 80 */     String data = getData();
/*    */     
/* 82 */     XmlDeclaration decl = null;
/* 83 */     String declContent = data.substring(1, data.length() - 1);
/*    */     
/* 85 */     if (isXmlDeclarationData(declContent)) {
/* 86 */       return null;
/*    */     }
/* 88 */     String fragment = "<" + declContent + ">";
/*    */     
/* 90 */     Document doc = Parser.htmlParser().settings(ParseSettings.preserveCase).parseInput(fragment, baseUri());
/* 91 */     if (doc.body().children().size() > 0) {
/* 92 */       Element el = doc.body().child(0);
/* 93 */       decl = new XmlDeclaration(NodeUtils.parser(doc).settings().normalizeTag(el.tagName()), data.startsWith("!"));
/* 94 */       decl.attributes().addAll(el.attributes());
/*    */     } 
/* 96 */     return decl;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\Comment.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */