/*     */ package org.jsoup.parser;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.List;
/*     */ import javax.annotation.ParametersAreNonnullByDefault;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.nodes.Attributes;
/*     */ import org.jsoup.nodes.CDataNode;
/*     */ import org.jsoup.nodes.Comment;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.DocumentType;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.nodes.Entities;
/*     */ import org.jsoup.nodes.Node;
/*     */ import org.jsoup.nodes.TextNode;
/*     */ import org.jsoup.nodes.XmlDeclaration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlTreeBuilder
/*     */   extends TreeBuilder
/*     */ {
/*     */   private static final int maxQueueDepth = 256;
/*     */   
/*     */   ParseSettings defaultSettings() {
/*  28 */     return ParseSettings.preserveCase;
/*     */   }
/*     */   
/*     */   @ParametersAreNonnullByDefault
/*     */   protected void initialiseParse(Reader input, String baseUri, Parser parser) {
/*  33 */     super.initialiseParse(input, baseUri, parser);
/*  34 */     this.stack.add(this.doc);
/*  35 */     this.doc.outputSettings()
/*  36 */       .syntax(Document.OutputSettings.Syntax.xml)
/*  37 */       .escapeMode(Entities.EscapeMode.xhtml)
/*  38 */       .prettyPrint(false);
/*     */   }
/*     */   
/*     */   Document parse(Reader input, String baseUri) {
/*  42 */     return parse(input, baseUri, new Parser(this));
/*     */   }
/*     */   
/*     */   Document parse(String input, String baseUri) {
/*  46 */     return parse(new StringReader(input), baseUri, new Parser(this));
/*     */   }
/*     */ 
/*     */   
/*     */   XmlTreeBuilder newInstance() {
/*  51 */     return new XmlTreeBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean process(Token token) {
/*  57 */     switch (token.type) {
/*     */       case StartTag:
/*  59 */         insert(token.asStartTag());
/*     */       
/*     */       case EndTag:
/*  62 */         popStackToClose(token.asEndTag());
/*     */       
/*     */       case Comment:
/*  65 */         insert(token.asComment());
/*     */       
/*     */       case Character:
/*  68 */         insert(token.asCharacter());
/*     */       
/*     */       case Doctype:
/*  71 */         insert(token.asDoctype());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case EOF:
/*  78 */         return true;
/*     */     } 
/*     */     Validate.fail("Unexpected token type: " + token.type);
/*     */   } protected void insertNode(Node node) {
/*  82 */     currentElement().appendChild(node);
/*  83 */     onNodeInserted(node, null);
/*     */   }
/*     */   
/*     */   protected void insertNode(Node node, Token token) {
/*  87 */     currentElement().appendChild(node);
/*  88 */     onNodeInserted(node, token);
/*     */   }
/*     */   
/*     */   Element insert(Token.StartTag startTag) {
/*  92 */     Tag tag = tagFor(startTag.name(), this.settings);
/*     */     
/*  94 */     if (startTag.hasAttributes()) {
/*  95 */       startTag.attributes.deduplicate(this.settings);
/*     */     }
/*  97 */     Element el = new Element(tag, null, this.settings.normalizeAttributes(startTag.attributes));
/*  98 */     insertNode((Node)el, startTag);
/*  99 */     if (startTag.isSelfClosing()) {
/* 100 */       if (!tag.isKnownTag())
/* 101 */         tag.setSelfClosing(); 
/*     */     } else {
/* 103 */       this.stack.add(el);
/*     */     } 
/* 105 */     return el;
/*     */   }
/*     */   void insert(Token.Comment commentToken) {
/*     */     XmlDeclaration xmlDeclaration;
/* 109 */     Comment comment = new Comment(commentToken.getData());
/* 110 */     Comment comment1 = comment;
/* 111 */     if (commentToken.bogus && comment.isXmlDeclaration()) {
/*     */ 
/*     */       
/* 114 */       XmlDeclaration decl = comment.asXmlDeclaration();
/* 115 */       if (decl != null)
/* 116 */         xmlDeclaration = decl; 
/*     */     } 
/* 118 */     insertNode((Node)xmlDeclaration, commentToken);
/*     */   }
/*     */   
/*     */   void insert(Token.Character token) {
/* 122 */     String data = token.getData();
/* 123 */     insertNode(token.isCData() ? (Node)new CDataNode(data) : (Node)new TextNode(data), token);
/*     */   }
/*     */   
/*     */   void insert(Token.Doctype d) {
/* 127 */     DocumentType doctypeNode = new DocumentType(this.settings.normalizeTag(d.getName()), d.getPublicIdentifier(), d.getSystemIdentifier());
/* 128 */     doctypeNode.setPubSysKey(d.getPubSysKey());
/* 129 */     insertNode((Node)doctypeNode, d);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void popStackToClose(Token.EndTag endTag) {
/* 140 */     String elName = this.settings.normalizeTag(endTag.tagName);
/* 141 */     Element firstFound = null;
/*     */     
/* 143 */     int bottom = this.stack.size() - 1;
/* 144 */     int upper = (bottom >= 256) ? (bottom - 256) : 0;
/*     */     int pos;
/* 146 */     for (pos = this.stack.size() - 1; pos >= upper; pos--) {
/* 147 */       Element next = this.stack.get(pos);
/* 148 */       if (next.nodeName().equals(elName)) {
/* 149 */         firstFound = next;
/*     */         break;
/*     */       } 
/*     */     } 
/* 153 */     if (firstFound == null) {
/*     */       return;
/*     */     }
/* 156 */     for (pos = this.stack.size() - 1; pos >= 0; pos--) {
/* 157 */       Element next = this.stack.get(pos);
/* 158 */       this.stack.remove(pos);
/* 159 */       if (next == firstFound) {
/* 160 */         onNodeClosed((Node)next, endTag);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List<Node> parseFragment(String inputFragment, String baseUri, Parser parser) {
/* 170 */     initialiseParse(new StringReader(inputFragment), baseUri, parser);
/* 171 */     runParser();
/* 172 */     return this.doc.childNodes();
/*     */   }
/*     */   
/*     */   List<Node> parseFragment(String inputFragment, Element context, String baseUri, Parser parser) {
/* 176 */     return parseFragment(inputFragment, baseUri, parser);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\XmlTreeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */