/*     */ package org.jsoup.safety;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.nodes.Attribute;
/*     */ import org.jsoup.nodes.Attributes;
/*     */ import org.jsoup.nodes.DataNode;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.nodes.Node;
/*     */ import org.jsoup.nodes.TextNode;
/*     */ import org.jsoup.parser.ParseErrorList;
/*     */ import org.jsoup.parser.Parser;
/*     */ import org.jsoup.parser.Tag;
/*     */ import org.jsoup.select.NodeTraversor;
/*     */ import org.jsoup.select.NodeVisitor;
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
/*     */ 
/*     */ public class Cleaner
/*     */ {
/*     */   private final Safelist safelist;
/*     */   
/*     */   public Cleaner(Safelist safelist) {
/*  43 */     Validate.notNull(safelist);
/*  44 */     this.safelist = safelist;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document clean(Document dirtyDocument) {
/*  55 */     Validate.notNull(dirtyDocument);
/*     */     
/*  57 */     Document clean = Document.createShell(dirtyDocument.baseUri());
/*  58 */     copySafeNodes(dirtyDocument.body(), clean.body());
/*  59 */     clean.outputSettings(dirtyDocument.outputSettings().clone());
/*     */     
/*  61 */     return clean;
/*     */   }
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
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isValid(Document dirtyDocument) {
/*  87 */     Validate.notNull(dirtyDocument);
/*     */     
/*  89 */     Document clean = Document.createShell(dirtyDocument.baseUri());
/*  90 */     int numDiscarded = copySafeNodes(dirtyDocument.body(), clean.body());
/*  91 */     return (numDiscarded == 0 && dirtyDocument
/*  92 */       .head().childNodes().isEmpty());
/*     */   }
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
/*     */ 
/*     */   
/*     */   public boolean isValidBodyHtml(String bodyHtml) {
/* 117 */     Document clean = Document.createShell("");
/* 118 */     Document dirty = Document.createShell("");
/* 119 */     ParseErrorList errorList = ParseErrorList.tracking(1);
/* 120 */     List<Node> nodes = Parser.parseFragment(bodyHtml, dirty.body(), "", errorList);
/* 121 */     dirty.body().insertChildren(0, nodes);
/* 122 */     int numDiscarded = copySafeNodes(dirty.body(), clean.body());
/* 123 */     return (numDiscarded == 0 && errorList.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   private final class CleaningVisitor
/*     */     implements NodeVisitor
/*     */   {
/* 130 */     private int numDiscarded = 0;
/*     */     private final Element root;
/*     */     private Element destination;
/*     */     
/*     */     private CleaningVisitor(Element root, Element destination) {
/* 135 */       this.root = root;
/* 136 */       this.destination = destination;
/*     */     }
/*     */     
/*     */     public void head(Node source, int depth) {
/* 140 */       if (source instanceof Element) {
/* 141 */         Element sourceEl = (Element)source;
/*     */         
/* 143 */         if (Cleaner.this.safelist.isSafeTag(sourceEl.normalName())) {
/* 144 */           Cleaner.ElementMeta meta = Cleaner.this.createSafeElement(sourceEl);
/* 145 */           Element destChild = meta.el;
/* 146 */           this.destination.appendChild((Node)destChild);
/*     */           
/* 148 */           this.numDiscarded += meta.numAttribsDiscarded;
/* 149 */           this.destination = destChild;
/* 150 */         } else if (source != this.root) {
/* 151 */           this.numDiscarded++;
/*     */         } 
/* 153 */       } else if (source instanceof TextNode) {
/* 154 */         TextNode sourceText = (TextNode)source;
/* 155 */         TextNode destText = new TextNode(sourceText.getWholeText());
/* 156 */         this.destination.appendChild((Node)destText);
/* 157 */       } else if (source instanceof DataNode && Cleaner.this.safelist.isSafeTag(source.parent().nodeName())) {
/* 158 */         DataNode sourceData = (DataNode)source;
/* 159 */         DataNode destData = new DataNode(sourceData.getWholeData());
/* 160 */         this.destination.appendChild((Node)destData);
/*     */       } else {
/* 162 */         this.numDiscarded++;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void tail(Node source, int depth) {
/* 167 */       if (source instanceof Element && Cleaner.this.safelist.isSafeTag(source.nodeName())) {
/* 168 */         this.destination = this.destination.parent();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private int copySafeNodes(Element source, Element dest) {
/* 174 */     CleaningVisitor cleaningVisitor = new CleaningVisitor(source, dest);
/* 175 */     NodeTraversor.traverse(cleaningVisitor, (Node)source);
/* 176 */     return cleaningVisitor.numDiscarded;
/*     */   }
/*     */   
/*     */   private ElementMeta createSafeElement(Element sourceEl) {
/* 180 */     String sourceTag = sourceEl.tagName();
/* 181 */     Attributes destAttrs = new Attributes();
/* 182 */     Element dest = new Element(Tag.valueOf(sourceTag), sourceEl.baseUri(), destAttrs);
/* 183 */     int numDiscarded = 0;
/*     */     
/* 185 */     Attributes sourceAttrs = sourceEl.attributes();
/* 186 */     for (Attribute sourceAttr : sourceAttrs) {
/* 187 */       if (this.safelist.isSafeAttribute(sourceTag, sourceEl, sourceAttr)) {
/* 188 */         destAttrs.put(sourceAttr); continue;
/*     */       } 
/* 190 */       numDiscarded++;
/*     */     } 
/* 192 */     Attributes enforcedAttrs = this.safelist.getEnforcedAttributes(sourceTag);
/* 193 */     destAttrs.addAll(enforcedAttrs);
/*     */ 
/*     */ 
/*     */     
/* 197 */     if (sourceEl.sourceRange().isTracked())
/* 198 */       sourceEl.sourceRange().track((Node)dest, true); 
/* 199 */     if (sourceEl.endSourceRange().isTracked()) {
/* 200 */       sourceEl.endSourceRange().track((Node)dest, false);
/*     */     }
/* 202 */     return new ElementMeta(dest, numDiscarded);
/*     */   }
/*     */   
/*     */   private static class ElementMeta {
/*     */     Element el;
/*     */     int numAttribsDiscarded;
/*     */     
/*     */     ElementMeta(Element el, int numAttribsDiscarded) {
/* 210 */       this.el = el;
/* 211 */       this.numAttribsDiscarded = numAttribsDiscarded;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\safety\Cleaner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */