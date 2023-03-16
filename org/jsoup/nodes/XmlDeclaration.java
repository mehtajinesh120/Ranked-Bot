/*    */ package org.jsoup.nodes;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import org.jsoup.SerializationException;
/*    */ import org.jsoup.helper.Validate;
/*    */ import org.jsoup.internal.StringUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlDeclaration
/*    */   extends LeafNode
/*    */ {
/*    */   private final boolean isProcessingInstruction;
/*    */   
/*    */   public XmlDeclaration(String name, boolean isProcessingInstruction) {
/* 22 */     Validate.notNull(name);
/* 23 */     this.value = name;
/* 24 */     this.isProcessingInstruction = isProcessingInstruction;
/*    */   }
/*    */   
/*    */   public String nodeName() {
/* 28 */     return "#declaration";
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String name() {
/* 36 */     return coreValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getWholeDeclaration() {
/* 44 */     StringBuilder sb = StringUtil.borrowBuilder();
/*    */     try {
/* 46 */       getWholeDeclaration(sb, new Document.OutputSettings());
/* 47 */     } catch (IOException e) {
/* 48 */       throw new SerializationException(e);
/*    */     } 
/* 50 */     return StringUtil.releaseBuilder(sb).trim();
/*    */   }
/*    */   
/*    */   private void getWholeDeclaration(Appendable accum, Document.OutputSettings out) throws IOException {
/* 54 */     for (Attribute attribute : attributes()) {
/* 55 */       String key = attribute.getKey();
/* 56 */       String val = attribute.getValue();
/* 57 */       if (!key.equals(nodeName())) {
/* 58 */         accum.append(' ');
/*    */         
/* 60 */         accum.append(key);
/* 61 */         if (!val.isEmpty()) {
/* 62 */           accum.append("=\"");
/* 63 */           Entities.escape(accum, val, out, true, false, false, false);
/* 64 */           accum.append('"');
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
/* 72 */     accum
/* 73 */       .append("<")
/* 74 */       .append(this.isProcessingInstruction ? "!" : "?")
/* 75 */       .append(coreValue());
/* 76 */     getWholeDeclaration(accum, out);
/* 77 */     accum
/* 78 */       .append(this.isProcessingInstruction ? "!" : "?")
/* 79 */       .append(">");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {}
/*    */ 
/*    */   
/*    */   public String toString() {
/* 88 */     return outerHtml();
/*    */   }
/*    */ 
/*    */   
/*    */   public XmlDeclaration clone() {
/* 93 */     return (XmlDeclaration)super.clone();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\XmlDeclaration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */