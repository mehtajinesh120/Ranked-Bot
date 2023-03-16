/*     */ package org.jsoup.parser;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.List;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.nodes.Node;
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
/*     */ public class Parser
/*     */ {
/*     */   private TreeBuilder treeBuilder;
/*     */   private ParseErrorList errors;
/*     */   private ParseSettings settings;
/*     */   private boolean trackPosition = false;
/*     */   
/*     */   public Parser(TreeBuilder treeBuilder) {
/*  27 */     this.treeBuilder = treeBuilder;
/*  28 */     this.settings = treeBuilder.defaultSettings();
/*  29 */     this.errors = ParseErrorList.noTracking();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser newInstance() {
/*  37 */     return new Parser(this);
/*     */   }
/*     */   
/*     */   private Parser(Parser copy) {
/*  41 */     this.treeBuilder = copy.treeBuilder.newInstance();
/*  42 */     this.errors = new ParseErrorList(copy.errors);
/*  43 */     this.settings = new ParseSettings(copy.settings);
/*  44 */     this.trackPosition = copy.trackPosition;
/*     */   }
/*     */   
/*     */   public Document parseInput(String html, String baseUri) {
/*  48 */     return this.treeBuilder.parse(new StringReader(html), baseUri, this);
/*     */   }
/*     */   
/*     */   public Document parseInput(Reader inputHtml, String baseUri) {
/*  52 */     return this.treeBuilder.parse(inputHtml, baseUri, this);
/*     */   }
/*     */   
/*     */   public List<Node> parseFragmentInput(String fragment, Element context, String baseUri) {
/*  56 */     return this.treeBuilder.parseFragment(fragment, context, baseUri, this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TreeBuilder getTreeBuilder() {
/*  64 */     return this.treeBuilder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser setTreeBuilder(TreeBuilder treeBuilder) {
/*  73 */     this.treeBuilder = treeBuilder;
/*  74 */     treeBuilder.parser = this;
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTrackErrors() {
/*  83 */     return (this.errors.getMaxSize() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser setTrackErrors(int maxErrors) {
/*  92 */     this.errors = (maxErrors > 0) ? ParseErrorList.tracking(maxErrors) : ParseErrorList.noTracking();
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseErrorList getErrors() {
/* 102 */     return this.errors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isTrackPosition() {
/* 111 */     return this.trackPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser setTrackPosition(boolean trackPosition) {
/* 121 */     this.trackPosition = trackPosition;
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Parser settings(ParseSettings settings) {
/* 131 */     this.settings = settings;
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseSettings settings() {
/* 140 */     return this.settings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isContentForTagData(String normalName) {
/* 148 */     return getTreeBuilder().isContentForTagData(normalName);
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
/*     */   public static Document parse(String html, String baseUri) {
/* 161 */     TreeBuilder treeBuilder = new HtmlTreeBuilder();
/* 162 */     return treeBuilder.parse(new StringReader(html), baseUri, new Parser(treeBuilder));
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
/*     */   public static List<Node> parseFragment(String fragmentHtml, Element context, String baseUri) {
/* 176 */     HtmlTreeBuilder treeBuilder = new HtmlTreeBuilder();
/* 177 */     return treeBuilder.parseFragment(fragmentHtml, context, baseUri, new Parser(treeBuilder));
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
/*     */   public static List<Node> parseFragment(String fragmentHtml, Element context, String baseUri, ParseErrorList errorList) {
/* 192 */     HtmlTreeBuilder treeBuilder = new HtmlTreeBuilder();
/* 193 */     Parser parser = new Parser(treeBuilder);
/* 194 */     parser.errors = errorList;
/* 195 */     return treeBuilder.parseFragment(fragmentHtml, context, baseUri, parser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Node> parseXmlFragment(String fragmentXml, String baseUri) {
/* 206 */     XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
/* 207 */     return treeBuilder.parseFragment(fragmentXml, baseUri, new Parser(treeBuilder));
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
/*     */   public static Document parseBodyFragment(String bodyHtml, String baseUri) {
/* 219 */     Document doc = Document.createShell(baseUri);
/* 220 */     Element body = doc.body();
/* 221 */     List<Node> nodeList = parseFragment(bodyHtml, body, baseUri);
/* 222 */     Node[] nodes = nodeList.<Node>toArray(new Node[0]);
/* 223 */     for (int i = nodes.length - 1; i > 0; i--) {
/* 224 */       nodes[i].remove();
/*     */     }
/* 226 */     for (Node node : nodes) {
/* 227 */       body.appendChild(node);
/*     */     }
/* 229 */     return doc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String unescapeEntities(String string, boolean inAttribute) {
/* 239 */     Tokeniser tokeniser = new Tokeniser(new CharacterReader(string), ParseErrorList.noTracking());
/* 240 */     return tokeniser.unescapeEntities(inAttribute);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Parser htmlParser() {
/* 251 */     return new Parser(new HtmlTreeBuilder());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Parser xmlParser() {
/* 260 */     return new Parser(new XmlTreeBuilder());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\Parser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */