/*     */ package org.jsoup.nodes;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import org.jsoup.Connection;
/*     */ import org.jsoup.Jsoup;
/*     */ import org.jsoup.helper.DataUtil;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.parser.ParseSettings;
/*     */ import org.jsoup.parser.Parser;
/*     */ import org.jsoup.parser.Tag;
/*     */ import org.jsoup.select.Elements;
/*     */ import org.jsoup.select.Evaluator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Document
/*     */   extends Element
/*     */ {
/*     */   @Nullable
/*     */   private Connection connection;
/*  27 */   private OutputSettings outputSettings = new OutputSettings();
/*     */   private Parser parser;
/*  29 */   private QuirksMode quirksMode = QuirksMode.noQuirks;
/*     */ 
/*     */   
/*     */   private final String location;
/*     */ 
/*     */   
/*     */   private boolean updateMetaCharset = false;
/*     */ 
/*     */ 
/*     */   
/*     */   public Document(String baseUri) {
/*  40 */     super(Tag.valueOf("#root", ParseSettings.htmlDefault), baseUri);
/*  41 */     this.location = baseUri;
/*  42 */     this.parser = Parser.htmlParser();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Document createShell(String baseUri) {
/*  51 */     Validate.notNull(baseUri);
/*     */     
/*  53 */     Document doc = new Document(baseUri);
/*  54 */     doc.parser = doc.parser();
/*  55 */     Element html = doc.appendElement("html");
/*  56 */     html.appendElement("head");
/*  57 */     html.appendElement("body");
/*     */     
/*  59 */     return doc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String location() {
/*  69 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection connection() {
/*  79 */     if (this.connection == null) {
/*  80 */       return Jsoup.newSession();
/*     */     }
/*  82 */     return this.connection;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public DocumentType documentType() {
/*  90 */     for (Node node : this.childNodes) {
/*  91 */       if (node instanceof DocumentType)
/*  92 */         return (DocumentType)node; 
/*  93 */       if (!(node instanceof LeafNode))
/*     */         break; 
/*     */     } 
/*  96 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Element htmlEl() {
/* 105 */     for (Element el : childElementsList()) {
/* 106 */       if (el.normalName().equals("html"))
/* 107 */         return el; 
/*     */     } 
/* 109 */     return appendElement("html");
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
/*     */   public Element head() {
/* 121 */     Element html = htmlEl();
/* 122 */     for (Element el : html.childElementsList()) {
/* 123 */       if (el.normalName().equals("head"))
/* 124 */         return el; 
/*     */     } 
/* 126 */     return html.prependElement("head");
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
/*     */   public Element body() {
/* 139 */     Element html = htmlEl();
/* 140 */     for (Element el : html.childElementsList()) {
/* 141 */       if ("body".equals(el.normalName()) || "frameset".equals(el.normalName()))
/* 142 */         return el; 
/*     */     } 
/* 144 */     return html.appendElement("body");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FormElement> forms() {
/* 155 */     return select("form").forms();
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
/*     */   public FormElement expectForm(String cssQuery) {
/* 167 */     Elements els = select(cssQuery);
/* 168 */     for (Element el : els) {
/* 169 */       if (el instanceof FormElement) return (FormElement)el; 
/*     */     } 
/* 171 */     Validate.fail("No form elements matched the query '%s' in the document.", new Object[] { cssQuery });
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String title() {
/* 181 */     Element titleEl = head().selectFirst(titleEval);
/* 182 */     return (titleEl != null) ? StringUtil.normaliseWhitespace(titleEl.text()).trim() : "";
/*     */   }
/* 184 */   private static final Evaluator titleEval = (Evaluator)new Evaluator.Tag("title");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void title(String title) {
/* 192 */     Validate.notNull(title);
/* 193 */     Element titleEl = head().selectFirst(titleEval);
/* 194 */     if (titleEl == null)
/* 195 */       titleEl = head().appendElement("title"); 
/* 196 */     titleEl.text(title);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Element createElement(String tagName) {
/* 205 */     return new Element(Tag.valueOf(tagName, ParseSettings.preserveCase), baseUri());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Document normalise() {
/* 217 */     Element htmlEl = htmlEl();
/* 218 */     Element head = head();
/* 219 */     body();
/*     */ 
/*     */ 
/*     */     
/* 223 */     normaliseTextNodes(head);
/* 224 */     normaliseTextNodes(htmlEl);
/* 225 */     normaliseTextNodes(this);
/*     */     
/* 227 */     normaliseStructure("head", htmlEl);
/* 228 */     normaliseStructure("body", htmlEl);
/*     */     
/* 230 */     ensureMetaCharsetElement();
/*     */     
/* 232 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private void normaliseTextNodes(Element element) {
/* 237 */     List<Node> toMove = new ArrayList<>();
/* 238 */     for (Node node : element.childNodes) {
/* 239 */       if (node instanceof TextNode) {
/* 240 */         TextNode tn = (TextNode)node;
/* 241 */         if (!tn.isBlank()) {
/* 242 */           toMove.add(tn);
/*     */         }
/*     */       } 
/*     */     } 
/* 246 */     for (int i = toMove.size() - 1; i >= 0; i--) {
/* 247 */       Node node = toMove.get(i);
/* 248 */       element.removeChild(node);
/* 249 */       body().prependChild(new TextNode(" "));
/* 250 */       body().prependChild(node);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void normaliseStructure(String tag, Element htmlEl) {
/* 256 */     Elements elements = getElementsByTag(tag);
/* 257 */     Element master = elements.first();
/* 258 */     if (elements.size() > 1) {
/* 259 */       List<Node> toMove = new ArrayList<>();
/* 260 */       for (int i = 1; i < elements.size(); i++) {
/* 261 */         Node dupe = (Node)elements.get(i);
/* 262 */         toMove.addAll(dupe.ensureChildNodes());
/* 263 */         dupe.remove();
/*     */       } 
/*     */       
/* 266 */       for (Node dupe : toMove) {
/* 267 */         master.appendChild(dupe);
/*     */       }
/*     */     } 
/* 270 */     if (master.parent() != null && !master.parent().equals(htmlEl)) {
/* 271 */       htmlEl.appendChild(master);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public String outerHtml() {
/* 277 */     return html();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Element text(String text) {
/* 287 */     body().text(text);
/* 288 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String nodeName() {
/* 293 */     return "#document";
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
/*     */ 
/*     */   
/*     */   public void charset(Charset charset) {
/* 321 */     updateMetaCharsetElement(true);
/* 322 */     this.outputSettings.charset(charset);
/* 323 */     ensureMetaCharsetElement();
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
/*     */   public Charset charset() {
/* 335 */     return this.outputSettings.charset();
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
/*     */   public void updateMetaCharsetElement(boolean update) {
/* 352 */     this.updateMetaCharset = update;
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
/*     */   public boolean updateMetaCharsetElement() {
/* 364 */     return this.updateMetaCharset;
/*     */   }
/*     */ 
/*     */   
/*     */   public Document clone() {
/* 369 */     Document clone = (Document)super.clone();
/* 370 */     clone.outputSettings = this.outputSettings.clone();
/* 371 */     return clone;
/*     */   }
/*     */ 
/*     */   
/*     */   public Document shallowClone() {
/* 376 */     Document clone = new Document(baseUri());
/* 377 */     if (this.attributes != null)
/* 378 */       clone.attributes = this.attributes.clone(); 
/* 379 */     clone.outputSettings = this.outputSettings.clone();
/* 380 */     return clone;
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
/*     */   private void ensureMetaCharsetElement() {
/* 403 */     if (this.updateMetaCharset) {
/* 404 */       OutputSettings.Syntax syntax = outputSettings().syntax();
/*     */       
/* 406 */       if (syntax == OutputSettings.Syntax.html) {
/* 407 */         Element metaCharset = selectFirst("meta[charset]");
/* 408 */         if (metaCharset != null) {
/* 409 */           metaCharset.attr("charset", charset().displayName());
/*     */         } else {
/* 411 */           head().appendElement("meta").attr("charset", charset().displayName());
/*     */         } 
/* 413 */         select("meta[name=charset]").remove();
/* 414 */       } else if (syntax == OutputSettings.Syntax.xml) {
/* 415 */         Node node = ensureChildNodes().get(0);
/* 416 */         if (node instanceof XmlDeclaration) {
/* 417 */           XmlDeclaration decl = (XmlDeclaration)node;
/* 418 */           if (decl.name().equals("xml")) {
/* 419 */             decl.attr("encoding", charset().displayName());
/* 420 */             if (decl.hasAttr("version"))
/* 421 */               decl.attr("version", "1.0"); 
/*     */           } else {
/* 423 */             decl = new XmlDeclaration("xml", false);
/* 424 */             decl.attr("version", "1.0");
/* 425 */             decl.attr("encoding", charset().displayName());
/* 426 */             prependChild(decl);
/*     */           } 
/*     */         } else {
/* 429 */           XmlDeclaration decl = new XmlDeclaration("xml", false);
/* 430 */           decl.attr("version", "1.0");
/* 431 */           decl.attr("encoding", charset().displayName());
/* 432 */           prependChild(decl);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class OutputSettings
/*     */     implements Cloneable
/*     */   {
/*     */     public enum Syntax
/*     */     {
/* 446 */       html, xml;
/*     */     }
/* 448 */     private Entities.EscapeMode escapeMode = Entities.EscapeMode.base;
/* 449 */     private Charset charset = DataUtil.UTF_8;
/* 450 */     private final ThreadLocal<CharsetEncoder> encoderThreadLocal = new ThreadLocal<>();
/*     */     @Nullable
/*     */     Entities.CoreCharset coreCharset;
/*     */     private boolean prettyPrint = true;
/*     */     private boolean outline = false;
/* 455 */     private int indentAmount = 1;
/* 456 */     private int maxPaddingWidth = 30;
/* 457 */     private Syntax syntax = Syntax.html;
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
/*     */     public Entities.EscapeMode escapeMode() {
/* 470 */       return this.escapeMode;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputSettings escapeMode(Entities.EscapeMode escapeMode) {
/* 480 */       this.escapeMode = escapeMode;
/* 481 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Charset charset() {
/* 493 */       return this.charset;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputSettings charset(Charset charset) {
/* 502 */       this.charset = charset;
/* 503 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputSettings charset(String charset) {
/* 512 */       charset(Charset.forName(charset));
/* 513 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     CharsetEncoder prepareEncoder() {
/* 518 */       CharsetEncoder encoder = this.charset.newEncoder();
/* 519 */       this.encoderThreadLocal.set(encoder);
/* 520 */       this.coreCharset = Entities.CoreCharset.byName(encoder.charset().name());
/* 521 */       return encoder;
/*     */     }
/*     */     
/*     */     CharsetEncoder encoder() {
/* 525 */       CharsetEncoder encoder = this.encoderThreadLocal.get();
/* 526 */       return (encoder != null) ? encoder : prepareEncoder();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Syntax syntax() {
/* 534 */       return this.syntax;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputSettings syntax(Syntax syntax) {
/* 544 */       this.syntax = syntax;
/* 545 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean prettyPrint() {
/* 554 */       return this.prettyPrint;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputSettings prettyPrint(boolean pretty) {
/* 563 */       this.prettyPrint = pretty;
/* 564 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean outline() {
/* 573 */       return this.outline;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputSettings outline(boolean outlineMode) {
/* 582 */       this.outline = outlineMode;
/* 583 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int indentAmount() {
/* 591 */       return this.indentAmount;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputSettings indentAmount(int indentAmount) {
/* 600 */       Validate.isTrue((indentAmount >= 0));
/* 601 */       this.indentAmount = indentAmount;
/* 602 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int maxPaddingWidth() {
/* 611 */       return this.maxPaddingWidth;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public OutputSettings maxPaddingWidth(int maxPaddingWidth) {
/* 621 */       Validate.isTrue((maxPaddingWidth >= -1));
/* 622 */       this.maxPaddingWidth = maxPaddingWidth;
/* 623 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public OutputSettings clone() {
/*     */       OutputSettings clone;
/*     */       try {
/* 630 */         clone = (OutputSettings)super.clone();
/* 631 */       } catch (CloneNotSupportedException e) {
/* 632 */         throw new RuntimeException(e);
/*     */       } 
/* 634 */       clone.charset(this.charset.name());
/* 635 */       clone.escapeMode = Entities.EscapeMode.valueOf(this.escapeMode.name());
/*     */       
/* 637 */       return clone;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Syntax {
/*     */     html, xml;
/*     */   }
/*     */   
/*     */   public OutputSettings outputSettings() {
/* 646 */     return this.outputSettings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document outputSettings(OutputSettings outputSettings) {
/* 655 */     Validate.notNull(outputSettings);
/* 656 */     this.outputSettings = outputSettings;
/* 657 */     return this;
/*     */   }
/*     */   
/*     */   public enum QuirksMode {
/* 661 */     noQuirks, quirks, limitedQuirks;
/*     */   }
/*     */   
/*     */   public QuirksMode quirksMode() {
/* 665 */     return this.quirksMode;
/*     */   }
/*     */   
/*     */   public Document quirksMode(QuirksMode quirksMode) {
/* 669 */     this.quirksMode = quirksMode;
/* 670 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser parser() {
/* 678 */     return this.parser;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Document parser(Parser parser) {
/* 688 */     this.parser = parser;
/* 689 */     return this;
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
/*     */   public Document connection(Connection connection) {
/* 702 */     Validate.notNull(connection);
/* 703 */     this.connection = connection;
/* 704 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\Document.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */