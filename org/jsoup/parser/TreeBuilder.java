/*     */ package org.jsoup.parser;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.ParametersAreNonnullByDefault;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.nodes.Attributes;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.nodes.Node;
/*     */ import org.jsoup.nodes.Range;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ abstract class TreeBuilder
/*     */ {
/*     */   protected Parser parser;
/*     */   CharacterReader reader;
/*     */   Tokeniser tokeniser;
/*     */   protected Document doc;
/*     */   protected ArrayList<Element> stack;
/*     */   protected String baseUri;
/*     */   protected Token currentToken;
/*     */   protected ParseSettings settings;
/*     */   protected Map<String, Tag> seenTags;
/*  32 */   private Token.StartTag start = new Token.StartTag();
/*  33 */   private Token.EndTag end = new Token.EndTag();
/*     */   private boolean trackSourceRange;
/*     */   
/*     */   abstract ParseSettings defaultSettings();
/*     */   
/*     */   @ParametersAreNonnullByDefault
/*     */   protected void initialiseParse(Reader input, String baseUri, Parser parser) {
/*  40 */     Validate.notNullParam(input, "input");
/*  41 */     Validate.notNullParam(baseUri, "baseUri");
/*  42 */     Validate.notNull(parser);
/*     */     
/*  44 */     this.doc = new Document(baseUri);
/*  45 */     this.doc.parser(parser);
/*  46 */     this.parser = parser;
/*  47 */     this.settings = parser.settings();
/*  48 */     this.reader = new CharacterReader(input);
/*  49 */     this.trackSourceRange = parser.isTrackPosition();
/*  50 */     this.reader.trackNewlines((parser.isTrackErrors() || this.trackSourceRange));
/*  51 */     this.currentToken = null;
/*  52 */     this.tokeniser = new Tokeniser(this.reader, parser.getErrors());
/*  53 */     this.stack = new ArrayList<>(32);
/*  54 */     this.seenTags = new HashMap<>();
/*  55 */     this.baseUri = baseUri;
/*     */   }
/*     */   
/*     */   @ParametersAreNonnullByDefault
/*     */   Document parse(Reader input, String baseUri, Parser parser) {
/*  60 */     initialiseParse(input, baseUri, parser);
/*  61 */     runParser();
/*     */ 
/*     */     
/*  64 */     this.reader.close();
/*  65 */     this.reader = null;
/*  66 */     this.tokeniser = null;
/*  67 */     this.stack = null;
/*  68 */     this.seenTags = null;
/*     */     
/*  70 */     return this.doc;
/*     */   }
/*     */ 
/*     */   
/*     */   abstract TreeBuilder newInstance();
/*     */ 
/*     */   
/*     */   abstract List<Node> parseFragment(String paramString1, Element paramElement, String paramString2, Parser paramParser);
/*     */ 
/*     */   
/*     */   protected void runParser() {
/*     */     Token token;
/*  82 */     Tokeniser tokeniser = this.tokeniser;
/*  83 */     Token.TokenType eof = Token.TokenType.EOF;
/*     */     
/*     */     do {
/*  86 */       token = tokeniser.read();
/*  87 */       process(token);
/*  88 */       token.reset();
/*     */     }
/*  90 */     while (token.type != eof);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract boolean process(Token paramToken);
/*     */ 
/*     */   
/*     */   protected boolean processStartTag(String name) {
/*  99 */     Token.StartTag start = this.start;
/* 100 */     if (this.currentToken == start) {
/* 101 */       return process((new Token.StartTag()).name(name));
/*     */     }
/* 103 */     return process(start.reset().name(name));
/*     */   }
/*     */   
/*     */   public boolean processStartTag(String name, Attributes attrs) {
/* 107 */     Token.StartTag start = this.start;
/* 108 */     if (this.currentToken == start) {
/* 109 */       return process((new Token.StartTag()).nameAttr(name, attrs));
/*     */     }
/* 111 */     start.reset();
/* 112 */     start.nameAttr(name, attrs);
/* 113 */     return process(start);
/*     */   }
/*     */   
/*     */   protected boolean processEndTag(String name) {
/* 117 */     if (this.currentToken == this.end) {
/* 118 */       return process((new Token.EndTag()).name(name));
/*     */     }
/* 120 */     return process(this.end.reset().name(name));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Element currentElement() {
/* 130 */     int size = this.stack.size();
/* 131 */     return (size > 0) ? this.stack.get(size - 1) : (Element)this.doc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean currentElementIs(String normalName) {
/* 140 */     if (this.stack.size() == 0)
/* 141 */       return false; 
/* 142 */     Element current = currentElement();
/* 143 */     return (current != null && current.normalName().equals(normalName));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void error(String msg) {
/* 151 */     error(msg, (Object[])null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void error(String msg, Object... args) {
/* 160 */     ParseErrorList errors = this.parser.getErrors();
/* 161 */     if (errors.canAddError()) {
/* 162 */       errors.add(new ParseError(this.reader, msg, args));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isContentForTagData(String normalName) {
/* 170 */     return false;
/*     */   }
/*     */   
/*     */   protected Tag tagFor(String tagName, ParseSettings settings) {
/* 174 */     Tag tag = this.seenTags.get(tagName);
/* 175 */     if (tag == null) {
/* 176 */       tag = Tag.valueOf(tagName, settings);
/* 177 */       this.seenTags.put(tagName, tag);
/*     */     } 
/* 179 */     return tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onNodeInserted(Node node, @Nullable Token token) {
/* 189 */     trackNodePosition(node, token, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onNodeClosed(Node node, Token token) {
/* 199 */     trackNodePosition(node, token, false);
/*     */   }
/*     */   
/*     */   private void trackNodePosition(Node node, @Nullable Token token, boolean start) {
/* 203 */     if (this.trackSourceRange && token != null) {
/* 204 */       int startPos = token.startPos();
/* 205 */       if (startPos == -1)
/*     */         return; 
/* 207 */       Range.Position startRange = new Range.Position(startPos, this.reader.lineNumber(startPos), this.reader.columnNumber(startPos));
/* 208 */       int endPos = token.endPos();
/* 209 */       Range.Position endRange = new Range.Position(endPos, this.reader.lineNumber(endPos), this.reader.columnNumber(endPos));
/* 210 */       Range range = new Range(startRange, endRange);
/* 211 */       range.track(node, start);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\TreeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */