/*     */ package org.jsoup.nodes;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TextNode
/*     */   extends LeafNode
/*     */ {
/*     */   public TextNode(String text) {
/*  20 */     this.value = text;
/*     */   }
/*     */   
/*     */   public String nodeName() {
/*  24 */     return "#text";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String text() {
/*  33 */     return StringUtil.normaliseWhitespace(getWholeText());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextNode text(String text) {
/*  42 */     coreValue(text);
/*  43 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWholeText() {
/*  51 */     return coreValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBlank() {
/*  59 */     return StringUtil.isBlank(coreValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TextNode splitText(int offset) {
/*  69 */     String text = coreValue();
/*  70 */     Validate.isTrue((offset >= 0), "Split offset must be not be negative");
/*  71 */     Validate.isTrue((offset < text.length()), "Split offset must not be greater than current text length");
/*     */     
/*  73 */     String head = text.substring(0, offset);
/*  74 */     String tail = text.substring(offset);
/*  75 */     text(head);
/*  76 */     TextNode tailNode = new TextNode(tail);
/*  77 */     if (this.parentNode != null) {
/*  78 */       this.parentNode.addChildren(siblingIndex() + 1, new Node[] { tailNode });
/*     */     }
/*  80 */     return tailNode;
/*     */   }
/*     */ 
/*     */   
/*     */   void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
/*  85 */     boolean prettyPrint = out.prettyPrint();
/*  86 */     Element parent = (this.parentNode instanceof Element) ? (Element)this.parentNode : null;
/*  87 */     boolean normaliseWhite = (prettyPrint && !Element.preserveWhitespace(this.parentNode));
/*  88 */     boolean trimLikeBlock = (parent != null && (parent.tag().isBlock() || parent.tag().formatAsBlock()));
/*  89 */     boolean trimLeading = false, trimTrailing = false;
/*     */     
/*  91 */     if (normaliseWhite) {
/*  92 */       trimLeading = ((trimLikeBlock && this.siblingIndex == 0) || this.parentNode instanceof Document);
/*  93 */       trimTrailing = (trimLikeBlock && nextSibling() == null);
/*     */ 
/*     */       
/*  96 */       Node next = nextSibling();
/*  97 */       Node prev = previousSibling();
/*  98 */       boolean isBlank = isBlank();
/*     */ 
/*     */       
/* 101 */       boolean couldSkip = ((next instanceof Element && ((Element)next).shouldIndent(out)) || (next instanceof TextNode && ((TextNode)next).isBlank()) || (prev instanceof Element && (((Element)prev).isBlock() || prev.isNode("br"))));
/*     */       
/* 103 */       if (couldSkip && isBlank)
/*     */         return; 
/* 105 */       if ((this.siblingIndex == 0 && parent != null && parent
/* 106 */         .tag().formatAsBlock() && !isBlank) || (out
/* 107 */         .outline() && siblingNodes().size() > 0 && !isBlank) || (this.siblingIndex > 0 && 
/* 108 */         isNode(prev, "br")))
/*     */       {
/* 110 */         indent(accum, depth, out);
/*     */       }
/*     */     } 
/* 113 */     Entities.escape(accum, coreValue(), out, false, normaliseWhite, trimLeading, trimTrailing);
/*     */   }
/*     */ 
/*     */   
/*     */   void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) throws IOException {}
/*     */ 
/*     */   
/*     */   public String toString() {
/* 121 */     return outerHtml();
/*     */   }
/*     */ 
/*     */   
/*     */   public TextNode clone() {
/* 126 */     return (TextNode)super.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TextNode createFromEncoded(String encodedText) {
/* 135 */     String text = Entities.unescape(encodedText);
/* 136 */     return new TextNode(text);
/*     */   }
/*     */   
/*     */   static String normaliseWhitespace(String text) {
/* 140 */     text = StringUtil.normaliseWhitespace(text);
/* 141 */     return text;
/*     */   }
/*     */   
/*     */   static String stripLeadingWhitespace(String text) {
/* 145 */     return text.replaceFirst("^\\s+", "");
/*     */   }
/*     */   
/*     */   static boolean lastCharIsWhitespace(StringBuilder sb) {
/* 149 */     return (sb.length() != 0 && sb.charAt(sb.length() - 1) == ' ');
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\TextNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */