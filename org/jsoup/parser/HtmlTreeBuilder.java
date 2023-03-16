/*     */ package org.jsoup.parser;
/*     */ 
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.ParametersAreNonnullByDefault;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.nodes.Attributes;
/*     */ import org.jsoup.nodes.CDataNode;
/*     */ import org.jsoup.nodes.Comment;
/*     */ import org.jsoup.nodes.DataNode;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.nodes.FormElement;
/*     */ import org.jsoup.nodes.Node;
/*     */ import org.jsoup.nodes.TextNode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HtmlTreeBuilder
/*     */   extends TreeBuilder
/*     */ {
/*  29 */   static final String[] TagsSearchInScope = new String[] { "applet", "caption", "html", "marquee", "object", "table", "td", "th" };
/*  30 */   static final String[] TagSearchList = new String[] { "ol", "ul" };
/*  31 */   static final String[] TagSearchButton = new String[] { "button" };
/*  32 */   static final String[] TagSearchTableScope = new String[] { "html", "table" };
/*  33 */   static final String[] TagSearchSelectScope = new String[] { "optgroup", "option" };
/*  34 */   static final String[] TagSearchEndTags = new String[] { "dd", "dt", "li", "optgroup", "option", "p", "rb", "rp", "rt", "rtc" };
/*  35 */   static final String[] TagThoroughSearchEndTags = new String[] { "caption", "colgroup", "dd", "dt", "li", "optgroup", "option", "p", "rb", "rp", "rt", "rtc", "tbody", "td", "tfoot", "th", "thead", "tr" };
/*  36 */   static final String[] TagSearchSpecial = new String[] { "address", "applet", "area", "article", "aside", "base", "basefont", "bgsound", "blockquote", "body", "br", "button", "caption", "center", "col", "colgroup", "command", "dd", "details", "dir", "div", "dl", "dt", "embed", "fieldset", "figcaption", "figure", "footer", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hgroup", "hr", "html", "iframe", "img", "input", "isindex", "li", "link", "listing", "marquee", "menu", "meta", "nav", "noembed", "noframes", "noscript", "object", "ol", "p", "param", "plaintext", "pre", "script", "section", "select", "style", "summary", "table", "tbody", "td", "textarea", "tfoot", "th", "thead", "title", "tr", "ul", "wbr", "xmp" };
/*     */   
/*     */   public static final int MaxScopeSearchDepth = 100;
/*     */   
/*     */   private HtmlTreeBuilderState state;
/*     */   
/*     */   private HtmlTreeBuilderState originalState;
/*     */   
/*     */   private boolean baseUriSetFromDoc;
/*     */   
/*     */   @Nullable
/*     */   private Element headElement;
/*     */   
/*     */   @Nullable
/*     */   private FormElement formElement;
/*     */   
/*     */   @Nullable
/*     */   private Element contextElement;
/*     */   private ArrayList<Element> formattingElements;
/*     */   private ArrayList<HtmlTreeBuilderState> tmplInsertMode;
/*     */   private List<String> pendingTableCharacters;
/*     */   private Token.EndTag emptyEnd;
/*     */   private boolean framesetOk;
/*     */   private boolean fosterInserts;
/*     */   private boolean fragmentParsing;
/*     */   private static final int maxQueueDepth = 256;
/*     */   
/*     */   ParseSettings defaultSettings() {
/*  64 */     return ParseSettings.htmlDefault;
/*     */   }
/*     */ 
/*     */   
/*     */   HtmlTreeBuilder newInstance() {
/*  69 */     return new HtmlTreeBuilder();
/*     */   }
/*     */   
/*     */   @ParametersAreNonnullByDefault
/*     */   protected void initialiseParse(Reader input, String baseUri, Parser parser) {
/*  74 */     super.initialiseParse(input, baseUri, parser);
/*     */ 
/*     */     
/*  77 */     this.state = HtmlTreeBuilderState.Initial;
/*  78 */     this.originalState = null;
/*  79 */     this.baseUriSetFromDoc = false;
/*  80 */     this.headElement = null;
/*  81 */     this.formElement = null;
/*  82 */     this.contextElement = null;
/*  83 */     this.formattingElements = new ArrayList<>();
/*  84 */     this.tmplInsertMode = new ArrayList<>();
/*  85 */     this.pendingTableCharacters = new ArrayList<>();
/*  86 */     this.emptyEnd = new Token.EndTag();
/*  87 */     this.framesetOk = true;
/*  88 */     this.fosterInserts = false;
/*  89 */     this.fragmentParsing = false;
/*     */   }
/*     */ 
/*     */   
/*     */   List<Node> parseFragment(String inputFragment, @Nullable Element context, String baseUri, Parser parser) {
/*  94 */     this.state = HtmlTreeBuilderState.Initial;
/*  95 */     initialiseParse(new StringReader(inputFragment), baseUri, parser);
/*  96 */     this.contextElement = context;
/*  97 */     this.fragmentParsing = true;
/*  98 */     Element root = null;
/*     */     
/* 100 */     if (context != null) {
/* 101 */       if (context.ownerDocument() != null) {
/* 102 */         this.doc.quirksMode(context.ownerDocument().quirksMode());
/*     */       }
/*     */       
/* 105 */       String contextTag = context.normalName();
/* 106 */       switch (contextTag) {
/*     */         case "title":
/*     */         case "textarea":
/* 109 */           this.tokeniser.transition(TokeniserState.Rcdata);
/*     */           break;
/*     */         case "iframe":
/*     */         case "noembed":
/*     */         case "noframes":
/*     */         case "style":
/*     */         case "xml":
/* 116 */           this.tokeniser.transition(TokeniserState.Rawtext);
/*     */           break;
/*     */         case "script":
/* 119 */           this.tokeniser.transition(TokeniserState.ScriptData);
/*     */           break;
/*     */         case "noscript":
/* 122 */           this.tokeniser.transition(TokeniserState.Data);
/*     */           break;
/*     */         case "plaintext":
/* 125 */           this.tokeniser.transition(TokeniserState.PLAINTEXT);
/*     */           break;
/*     */         case "template":
/* 128 */           this.tokeniser.transition(TokeniserState.Data);
/* 129 */           pushTemplateMode(HtmlTreeBuilderState.InTemplate);
/*     */           break;
/*     */         default:
/* 132 */           this.tokeniser.transition(TokeniserState.Data); break;
/*     */       } 
/* 134 */       root = new Element(tagFor(contextTag, this.settings), baseUri);
/* 135 */       this.doc.appendChild((Node)root);
/* 136 */       this.stack.add(root);
/* 137 */       resetInsertionMode();
/*     */ 
/*     */ 
/*     */       
/* 141 */       Element formSearch = context;
/* 142 */       while (formSearch != null) {
/* 143 */         if (formSearch instanceof FormElement) {
/* 144 */           this.formElement = (FormElement)formSearch;
/*     */           break;
/*     */         } 
/* 147 */         formSearch = formSearch.parent();
/*     */       } 
/*     */     } 
/*     */     
/* 151 */     runParser();
/* 152 */     if (context != null) {
/*     */ 
/*     */       
/* 155 */       List<Node> nodes = root.siblingNodes();
/* 156 */       if (!nodes.isEmpty())
/* 157 */         root.insertChildren(-1, nodes); 
/* 158 */       return root.childNodes();
/*     */     } 
/*     */     
/* 161 */     return this.doc.childNodes();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean process(Token token) {
/* 166 */     this.currentToken = token;
/* 167 */     return this.state.process(token, this);
/*     */   }
/*     */   
/*     */   boolean process(Token token, HtmlTreeBuilderState state) {
/* 171 */     this.currentToken = token;
/* 172 */     return state.process(token, this);
/*     */   }
/*     */   
/*     */   void transition(HtmlTreeBuilderState state) {
/* 176 */     this.state = state;
/*     */   }
/*     */   
/*     */   HtmlTreeBuilderState state() {
/* 180 */     return this.state;
/*     */   }
/*     */   
/*     */   void markInsertionMode() {
/* 184 */     this.originalState = this.state;
/*     */   }
/*     */   
/*     */   HtmlTreeBuilderState originalState() {
/* 188 */     return this.originalState;
/*     */   }
/*     */   
/*     */   void framesetOk(boolean framesetOk) {
/* 192 */     this.framesetOk = framesetOk;
/*     */   }
/*     */   
/*     */   boolean framesetOk() {
/* 196 */     return this.framesetOk;
/*     */   }
/*     */   
/*     */   Document getDocument() {
/* 200 */     return this.doc;
/*     */   }
/*     */   
/*     */   String getBaseUri() {
/* 204 */     return this.baseUri;
/*     */   }
/*     */   
/*     */   void maybeSetBaseUri(Element base) {
/* 208 */     if (this.baseUriSetFromDoc) {
/*     */       return;
/*     */     }
/* 211 */     String href = base.absUrl("href");
/* 212 */     if (href.length() != 0) {
/* 213 */       this.baseUri = href;
/* 214 */       this.baseUriSetFromDoc = true;
/* 215 */       this.doc.setBaseUri(href);
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean isFragmentParsing() {
/* 220 */     return this.fragmentParsing;
/*     */   }
/*     */   
/*     */   void error(HtmlTreeBuilderState state) {
/* 224 */     if (this.parser.getErrors().canAddError()) {
/* 225 */       this.parser.getErrors().add(new ParseError(this.reader, "Unexpected %s token [%s] when in state [%s]", new Object[] { this.currentToken
/* 226 */               .tokenType(), this.currentToken, state }));
/*     */     }
/*     */   }
/*     */   
/*     */   Element insert(Token.StartTag startTag) {
/* 231 */     if (startTag.hasAttributes() && !startTag.attributes.isEmpty()) {
/* 232 */       int dupes = startTag.attributes.deduplicate(this.settings);
/* 233 */       if (dupes > 0) {
/* 234 */         error("Dropped duplicate attribute(s) in tag [%s]", new Object[] { startTag.normalName });
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 240 */     if (startTag.isSelfClosing()) {
/* 241 */       Element element = insertEmpty(startTag);
/* 242 */       this.stack.add(element);
/* 243 */       this.tokeniser.transition(TokeniserState.Data);
/* 244 */       this.tokeniser.emit(this.emptyEnd.reset().name(element.tagName()));
/* 245 */       return element;
/*     */     } 
/*     */     
/* 248 */     Element el = new Element(tagFor(startTag.name(), this.settings), null, this.settings.normalizeAttributes(startTag.attributes));
/* 249 */     insert(el, startTag);
/* 250 */     return el;
/*     */   }
/*     */   
/*     */   Element insertStartTag(String startTagName) {
/* 254 */     Element el = new Element(tagFor(startTagName, this.settings), null);
/* 255 */     insert(el);
/* 256 */     return el;
/*     */   }
/*     */   
/*     */   void insert(Element el) {
/* 260 */     insertNode((Node)el, (Token)null);
/* 261 */     this.stack.add(el);
/*     */   }
/*     */   
/*     */   private void insert(Element el, @Nullable Token token) {
/* 265 */     insertNode((Node)el, token);
/* 266 */     this.stack.add(el);
/*     */   }
/*     */   
/*     */   Element insertEmpty(Token.StartTag startTag) {
/* 270 */     Tag tag = tagFor(startTag.name(), this.settings);
/* 271 */     Element el = new Element(tag, null, this.settings.normalizeAttributes(startTag.attributes));
/* 272 */     insertNode((Node)el, startTag);
/* 273 */     if (startTag.isSelfClosing())
/* 274 */       if (tag.isKnownTag()) {
/* 275 */         if (!tag.isEmpty()) {
/* 276 */           this.tokeniser.error("Tag [%s] cannot be self closing; not a void tag", new Object[] { tag.normalName() });
/*     */         }
/*     */       } else {
/* 279 */         tag.setSelfClosing();
/*     */       }  
/* 281 */     return el;
/*     */   }
/*     */   
/*     */   FormElement insertForm(Token.StartTag startTag, boolean onStack, boolean checkTemplateStack) {
/* 285 */     Tag tag = tagFor(startTag.name(), this.settings);
/* 286 */     FormElement el = new FormElement(tag, null, this.settings.normalizeAttributes(startTag.attributes));
/* 287 */     if (checkTemplateStack) {
/* 288 */       if (!onStack("template"))
/* 289 */         setFormElement(el); 
/*     */     } else {
/* 291 */       setFormElement(el);
/*     */     } 
/* 293 */     insertNode((Node)el, startTag);
/* 294 */     if (onStack)
/* 295 */       this.stack.add(el); 
/* 296 */     return el;
/*     */   }
/*     */   
/*     */   void insert(Token.Comment commentToken) {
/* 300 */     Comment comment = new Comment(commentToken.getData());
/* 301 */     insertNode((Node)comment, commentToken);
/*     */   }
/*     */   
/*     */   void insert(Token.Character characterToken) {
/*     */     TextNode textNode;
/* 306 */     Element el = currentElement();
/* 307 */     String tagName = el.normalName();
/* 308 */     String data = characterToken.getData();
/*     */     
/* 310 */     if (characterToken.isCData()) {
/* 311 */       CDataNode cDataNode = new CDataNode(data);
/* 312 */     } else if (isContentForTagData(tagName)) {
/* 313 */       DataNode dataNode = new DataNode(data);
/*     */     } else {
/* 315 */       textNode = new TextNode(data);
/* 316 */     }  el.appendChild((Node)textNode);
/* 317 */     onNodeInserted((Node)textNode, characterToken);
/*     */   }
/*     */ 
/*     */   
/*     */   private void insertNode(Node node, @Nullable Token token) {
/* 322 */     if (this.stack.isEmpty()) {
/* 323 */       this.doc.appendChild(node);
/* 324 */     } else if (isFosterInserts() && StringUtil.inSorted(currentElement().normalName(), HtmlTreeBuilderState.Constants.InTableFoster)) {
/* 325 */       insertInFosterParent(node);
/*     */     } else {
/* 327 */       currentElement().appendChild(node);
/*     */     } 
/*     */     
/* 330 */     if (node instanceof Element && ((Element)node).tag().isFormListed() && 
/* 331 */       this.formElement != null) {
/* 332 */       this.formElement.addElement((Element)node);
/*     */     }
/* 334 */     onNodeInserted(node, token);
/*     */   }
/*     */   
/*     */   Element pop() {
/* 338 */     int size = this.stack.size();
/* 339 */     return this.stack.remove(size - 1);
/*     */   }
/*     */   
/*     */   void push(Element element) {
/* 343 */     this.stack.add(element);
/*     */   }
/*     */   
/*     */   ArrayList<Element> getStack() {
/* 347 */     return this.stack;
/*     */   }
/*     */   
/*     */   boolean onStack(Element el) {
/* 351 */     return onStack(this.stack, el);
/*     */   }
/*     */   
/*     */   boolean onStack(String elName) {
/* 355 */     return (getFromStack(elName) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean onStack(ArrayList<Element> queue, Element element) {
/* 360 */     int bottom = queue.size() - 1;
/* 361 */     int upper = (bottom >= 256) ? (bottom - 256) : 0;
/* 362 */     for (int pos = bottom; pos >= upper; pos--) {
/* 363 */       Element next = queue.get(pos);
/* 364 */       if (next == element) {
/* 365 */         return true;
/*     */       }
/*     */     } 
/* 368 */     return false;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   Element getFromStack(String elName) {
/* 373 */     int bottom = this.stack.size() - 1;
/* 374 */     int upper = (bottom >= 256) ? (bottom - 256) : 0;
/* 375 */     for (int pos = bottom; pos >= upper; pos--) {
/* 376 */       Element next = this.stack.get(pos);
/* 377 */       if (next.normalName().equals(elName)) {
/* 378 */         return next;
/*     */       }
/*     */     } 
/* 381 */     return null;
/*     */   }
/*     */   
/*     */   boolean removeFromStack(Element el) {
/* 385 */     for (int pos = this.stack.size() - 1; pos >= 0; pos--) {
/* 386 */       Element next = this.stack.get(pos);
/* 387 */       if (next == el) {
/* 388 */         this.stack.remove(pos);
/* 389 */         return true;
/*     */       } 
/*     */     } 
/* 392 */     return false;
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   Element popStackToClose(String elName) {
/* 397 */     for (int pos = this.stack.size() - 1; pos >= 0; pos--) {
/* 398 */       Element el = this.stack.get(pos);
/* 399 */       this.stack.remove(pos);
/* 400 */       if (el.normalName().equals(elName)) {
/* 401 */         if (this.currentToken instanceof Token.EndTag)
/* 402 */           onNodeClosed((Node)el, this.currentToken); 
/* 403 */         return el;
/*     */       } 
/*     */     } 
/* 406 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   void popStackToClose(String... elNames) {
/* 411 */     for (int pos = this.stack.size() - 1; pos >= 0; pos--) {
/* 412 */       Element next = this.stack.get(pos);
/* 413 */       this.stack.remove(pos);
/* 414 */       if (StringUtil.inSorted(next.normalName(), elNames))
/*     */         break; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void popStackToBefore(String elName) {
/* 420 */     for (int pos = this.stack.size() - 1; pos >= 0; pos--) {
/* 421 */       Element next = this.stack.get(pos);
/* 422 */       if (next.normalName().equals(elName)) {
/*     */         break;
/*     */       }
/* 425 */       this.stack.remove(pos);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   void clearStackToTableContext() {
/* 431 */     clearStackToContext(new String[] { "table", "template" });
/*     */   }
/*     */   
/*     */   void clearStackToTableBodyContext() {
/* 435 */     clearStackToContext(new String[] { "tbody", "tfoot", "thead", "template" });
/*     */   }
/*     */   
/*     */   void clearStackToTableRowContext() {
/* 439 */     clearStackToContext(new String[] { "tr", "template" });
/*     */   }
/*     */   
/*     */   private void clearStackToContext(String... nodeNames) {
/* 443 */     for (int pos = this.stack.size() - 1; pos >= 0; pos--) {
/* 444 */       Element next = this.stack.get(pos);
/* 445 */       if (StringUtil.in(next.normalName(), nodeNames) || next.normalName().equals("html")) {
/*     */         break;
/*     */       }
/* 448 */       this.stack.remove(pos);
/*     */     } 
/*     */   }
/*     */   @Nullable
/*     */   Element aboveOnStack(Element el) {
/* 453 */     assert onStack(el);
/* 454 */     for (int pos = this.stack.size() - 1; pos >= 0; pos--) {
/* 455 */       Element next = this.stack.get(pos);
/* 456 */       if (next == el) {
/* 457 */         return this.stack.get(pos - 1);
/*     */       }
/*     */     } 
/* 460 */     return null;
/*     */   }
/*     */   
/*     */   void insertOnStackAfter(Element after, Element in) {
/* 464 */     int i = this.stack.lastIndexOf(after);
/* 465 */     Validate.isTrue((i != -1));
/* 466 */     this.stack.add(i + 1, in);
/*     */   }
/*     */   
/*     */   void replaceOnStack(Element out, Element in) {
/* 470 */     replaceInQueue(this.stack, out, in);
/*     */   }
/*     */   
/*     */   private void replaceInQueue(ArrayList<Element> queue, Element out, Element in) {
/* 474 */     int i = queue.lastIndexOf(out);
/* 475 */     Validate.isTrue((i != -1));
/* 476 */     queue.set(i, in);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   boolean resetInsertionMode() {
/* 486 */     boolean last = false;
/* 487 */     int bottom = this.stack.size() - 1;
/* 488 */     int upper = (bottom >= 256) ? (bottom - 256) : 0;
/* 489 */     HtmlTreeBuilderState origState = this.state;
/*     */     
/* 491 */     if (this.stack.size() == 0) {
/* 492 */       transition(HtmlTreeBuilderState.InBody);
/*     */     }
/*     */     
/* 495 */     for (int pos = bottom; pos >= upper; pos--) {
/* 496 */       HtmlTreeBuilderState tmplState; Element node = this.stack.get(pos);
/* 497 */       if (pos == upper) {
/* 498 */         last = true;
/* 499 */         if (this.fragmentParsing)
/* 500 */           node = this.contextElement; 
/*     */       } 
/* 502 */       String name = (node != null) ? node.normalName() : "";
/* 503 */       switch (name) {
/*     */         case "select":
/* 505 */           transition(HtmlTreeBuilderState.InSelect);
/*     */           break;
/*     */         
/*     */         case "td":
/*     */         case "th":
/* 510 */           if (!last) {
/* 511 */             transition(HtmlTreeBuilderState.InCell);
/*     */             break;
/*     */           } 
/*     */           break;
/*     */         case "tr":
/* 516 */           transition(HtmlTreeBuilderState.InRow);
/*     */           break;
/*     */         case "tbody":
/*     */         case "thead":
/*     */         case "tfoot":
/* 521 */           transition(HtmlTreeBuilderState.InTableBody);
/*     */           break;
/*     */         case "caption":
/* 524 */           transition(HtmlTreeBuilderState.InCaption);
/*     */           break;
/*     */         case "colgroup":
/* 527 */           transition(HtmlTreeBuilderState.InColumnGroup);
/*     */           break;
/*     */         case "table":
/* 530 */           transition(HtmlTreeBuilderState.InTable);
/*     */           break;
/*     */         case "template":
/* 533 */           tmplState = currentTemplateMode();
/* 534 */           Validate.notNull(tmplState, "Bug: no template insertion mode on stack!");
/* 535 */           transition(tmplState);
/*     */           break;
/*     */         case "head":
/* 538 */           if (!last) {
/* 539 */             transition(HtmlTreeBuilderState.InHead);
/*     */             break;
/*     */           } 
/*     */           break;
/*     */         case "body":
/* 544 */           transition(HtmlTreeBuilderState.InBody);
/*     */           break;
/*     */         case "frameset":
/* 547 */           transition(HtmlTreeBuilderState.InFrameset);
/*     */           break;
/*     */         case "html":
/* 550 */           transition((this.headElement == null) ? HtmlTreeBuilderState.BeforeHead : HtmlTreeBuilderState.AfterHead);
/*     */           break;
/*     */       } 
/* 553 */       if (last) {
/* 554 */         transition(HtmlTreeBuilderState.InBody);
/*     */         break;
/*     */       } 
/*     */     } 
/* 558 */     return (this.state != origState);
/*     */   }
/*     */ 
/*     */   
/*     */   void resetBody() {
/* 563 */     if (!onStack("body")) {
/* 564 */       this.stack.add(this.doc.body());
/*     */     }
/* 566 */     transition(HtmlTreeBuilderState.InBody);
/*     */   }
/*     */ 
/*     */   
/* 570 */   private String[] specificScopeTarget = new String[] { null }; private static final int maxUsedFormattingElements = 12;
/*     */   
/*     */   private boolean inSpecificScope(String targetName, String[] baseTypes, String[] extraTypes) {
/* 573 */     this.specificScopeTarget[0] = targetName;
/* 574 */     return inSpecificScope(this.specificScopeTarget, baseTypes, extraTypes);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean inSpecificScope(String[] targetNames, String[] baseTypes, String[] extraTypes) {
/* 579 */     int bottom = this.stack.size() - 1;
/* 580 */     int top = (bottom > 100) ? (bottom - 100) : 0;
/*     */ 
/*     */     
/* 583 */     for (int pos = bottom; pos >= top; pos--) {
/* 584 */       String elName = ((Element)this.stack.get(pos)).normalName();
/* 585 */       if (StringUtil.inSorted(elName, targetNames))
/* 586 */         return true; 
/* 587 */       if (StringUtil.inSorted(elName, baseTypes))
/* 588 */         return false; 
/* 589 */       if (extraTypes != null && StringUtil.inSorted(elName, extraTypes)) {
/* 590 */         return false;
/*     */       }
/*     */     } 
/* 593 */     return false;
/*     */   }
/*     */   
/*     */   boolean inScope(String[] targetNames) {
/* 597 */     return inSpecificScope(targetNames, TagsSearchInScope, (String[])null);
/*     */   }
/*     */   
/*     */   boolean inScope(String targetName) {
/* 601 */     return inScope(targetName, (String[])null);
/*     */   }
/*     */   
/*     */   boolean inScope(String targetName, String[] extras) {
/* 605 */     return inSpecificScope(targetName, TagsSearchInScope, extras);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean inListItemScope(String targetName) {
/* 611 */     return inScope(targetName, TagSearchList);
/*     */   }
/*     */   
/*     */   boolean inButtonScope(String targetName) {
/* 615 */     return inScope(targetName, TagSearchButton);
/*     */   }
/*     */   
/*     */   boolean inTableScope(String targetName) {
/* 619 */     return inSpecificScope(targetName, TagSearchTableScope, (String[])null);
/*     */   }
/*     */   
/*     */   boolean inSelectScope(String targetName) {
/* 623 */     for (int pos = this.stack.size() - 1; pos >= 0; pos--) {
/* 624 */       Element el = this.stack.get(pos);
/* 625 */       String elName = el.normalName();
/* 626 */       if (elName.equals(targetName))
/* 627 */         return true; 
/* 628 */       if (!StringUtil.inSorted(elName, TagSearchSelectScope))
/* 629 */         return false; 
/*     */     } 
/* 631 */     Validate.fail("Should not be reachable");
/* 632 */     return false;
/*     */   }
/*     */   
/*     */   void setHeadElement(Element headElement) {
/* 636 */     this.headElement = headElement;
/*     */   }
/*     */   
/*     */   Element getHeadElement() {
/* 640 */     return this.headElement;
/*     */   }
/*     */   
/*     */   boolean isFosterInserts() {
/* 644 */     return this.fosterInserts;
/*     */   }
/*     */   
/*     */   void setFosterInserts(boolean fosterInserts) {
/* 648 */     this.fosterInserts = fosterInserts;
/*     */   }
/*     */   @Nullable
/*     */   FormElement getFormElement() {
/* 652 */     return this.formElement;
/*     */   }
/*     */   
/*     */   void setFormElement(FormElement formElement) {
/* 656 */     this.formElement = formElement;
/*     */   }
/*     */   
/*     */   void newPendingTableCharacters() {
/* 660 */     this.pendingTableCharacters = new ArrayList<>();
/*     */   }
/*     */   
/*     */   List<String> getPendingTableCharacters() {
/* 664 */     return this.pendingTableCharacters;
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
/*     */   void generateImpliedEndTags(String excludeTag) {
/* 679 */     while (StringUtil.inSorted(currentElement().normalName(), TagSearchEndTags) && (
/* 680 */       excludeTag == null || !currentElementIs(excludeTag)))
/*     */     {
/* 682 */       pop();
/*     */     }
/*     */   }
/*     */   
/*     */   void generateImpliedEndTags() {
/* 687 */     generateImpliedEndTags(false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void generateImpliedEndTags(boolean thorough) {
/* 695 */     String[] search = thorough ? TagThoroughSearchEndTags : TagSearchEndTags;
/* 696 */     while (StringUtil.inSorted(currentElement().normalName(), search)) {
/* 697 */       pop();
/*     */     }
/*     */   }
/*     */   
/*     */   void closeElement(String name) {
/* 702 */     generateImpliedEndTags(name);
/* 703 */     if (!name.equals(currentElement().normalName())) error(state()); 
/* 704 */     popStackToClose(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean isSpecial(Element el) {
/* 710 */     String name = el.normalName();
/* 711 */     return StringUtil.inSorted(name, TagSearchSpecial);
/*     */   }
/*     */   
/*     */   Element lastFormattingElement() {
/* 715 */     return (this.formattingElements.size() > 0) ? this.formattingElements.get(this.formattingElements.size() - 1) : null;
/*     */   }
/*     */   
/*     */   int positionOfElement(Element el) {
/* 719 */     for (int i = 0; i < this.formattingElements.size(); i++) {
/* 720 */       if (el == this.formattingElements.get(i))
/* 721 */         return i; 
/*     */     } 
/* 723 */     return -1;
/*     */   }
/*     */   
/*     */   Element removeLastFormattingElement() {
/* 727 */     int size = this.formattingElements.size();
/* 728 */     if (size > 0) {
/* 729 */       return this.formattingElements.remove(size - 1);
/*     */     }
/* 731 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   void pushActiveFormattingElements(Element in) {
/* 736 */     checkActiveFormattingElements(in);
/* 737 */     this.formattingElements.add(in);
/*     */   }
/*     */   
/*     */   void pushWithBookmark(Element in, int bookmark) {
/* 741 */     checkActiveFormattingElements(in);
/*     */     
/*     */     try {
/* 744 */       this.formattingElements.add(bookmark, in);
/* 745 */     } catch (IndexOutOfBoundsException e) {
/* 746 */       this.formattingElements.add(in);
/*     */     } 
/*     */   }
/*     */   
/*     */   void checkActiveFormattingElements(Element in) {
/* 751 */     int numSeen = 0;
/* 752 */     int size = this.formattingElements.size() - 1;
/* 753 */     int ceil = size - 12; if (ceil < 0) ceil = 0;
/*     */     
/* 755 */     for (int pos = size; pos >= ceil; pos--) {
/* 756 */       Element el = this.formattingElements.get(pos);
/* 757 */       if (el == null) {
/*     */         break;
/*     */       }
/* 760 */       if (isSameFormattingElement(in, el)) {
/* 761 */         numSeen++;
/*     */       }
/* 763 */       if (numSeen == 3) {
/* 764 */         this.formattingElements.remove(pos);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isSameFormattingElement(Element a, Element b) {
/* 772 */     return (a.normalName().equals(b.normalName()) && a
/*     */       
/* 774 */       .attributes().equals(b.attributes()));
/*     */   }
/*     */ 
/*     */   
/*     */   void reconstructFormattingElements() {
/* 779 */     if (this.stack.size() > 256)
/*     */       return; 
/* 781 */     Element last = lastFormattingElement();
/* 782 */     if (last == null || onStack(last)) {
/*     */       return;
/*     */     }
/* 785 */     Element entry = last;
/* 786 */     int size = this.formattingElements.size();
/* 787 */     int ceil = size - 12; if (ceil < 0) ceil = 0; 
/* 788 */     int pos = size - 1;
/* 789 */     boolean skip = false;
/*     */     do {
/* 791 */       if (pos == ceil) {
/* 792 */         skip = true;
/*     */         break;
/*     */       } 
/* 795 */       entry = this.formattingElements.get(--pos);
/* 796 */     } while (entry != null && !onStack(entry));
/*     */ 
/*     */     
/*     */     do {
/* 800 */       if (!skip)
/* 801 */         entry = this.formattingElements.get(++pos); 
/* 802 */       Validate.notNull(entry);
/*     */ 
/*     */       
/* 805 */       skip = false;
/* 806 */       Element newEl = new Element(tagFor(entry.normalName(), this.settings), null, entry.attributes().clone());
/* 807 */       insert(newEl);
/*     */ 
/*     */       
/* 810 */       this.formattingElements.set(pos, newEl);
/*     */     
/*     */     }
/* 813 */     while (pos != size - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void clearFormattingElementsToLastMarker() {
/* 820 */     while (!this.formattingElements.isEmpty()) {
/* 821 */       Element el = removeLastFormattingElement();
/* 822 */       if (el == null)
/*     */         break; 
/*     */     } 
/*     */   }
/*     */   
/*     */   void removeFromActiveFormattingElements(Element el) {
/* 828 */     for (int pos = this.formattingElements.size() - 1; pos >= 0; pos--) {
/* 829 */       Element next = this.formattingElements.get(pos);
/* 830 */       if (next == el) {
/* 831 */         this.formattingElements.remove(pos);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   boolean isInActiveFormattingElements(Element el) {
/* 838 */     return onStack(this.formattingElements, el);
/*     */   }
/*     */   
/*     */   Element getActiveFormattingElement(String nodeName) {
/* 842 */     for (int pos = this.formattingElements.size() - 1; pos >= 0; pos--) {
/* 843 */       Element next = this.formattingElements.get(pos);
/* 844 */       if (next == null)
/*     */         break; 
/* 846 */       if (next.normalName().equals(nodeName))
/* 847 */         return next; 
/*     */     } 
/* 849 */     return null;
/*     */   }
/*     */   
/*     */   void replaceActiveFormattingElement(Element out, Element in) {
/* 853 */     replaceInQueue(this.formattingElements, out, in);
/*     */   }
/*     */   
/*     */   void insertMarkerToFormattingElements() {
/* 857 */     this.formattingElements.add(null);
/*     */   }
/*     */ 
/*     */   
/*     */   void insertInFosterParent(Node in) {
/* 862 */     Element fosterParent, lastTable = getFromStack("table");
/* 863 */     boolean isLastTableParent = false;
/* 864 */     if (lastTable != null)
/* 865 */     { if (lastTable.parent() != null) {
/* 866 */         fosterParent = lastTable.parent();
/* 867 */         isLastTableParent = true;
/*     */       } else {
/* 869 */         fosterParent = aboveOnStack(lastTable);
/*     */       }  }
/* 871 */     else { fosterParent = this.stack.get(0); }
/*     */ 
/*     */     
/* 874 */     if (isLastTableParent) {
/* 875 */       Validate.notNull(lastTable);
/* 876 */       lastTable.before(in);
/*     */     } else {
/*     */       
/* 879 */       fosterParent.appendChild(in);
/*     */     } 
/*     */   }
/*     */   
/*     */   void pushTemplateMode(HtmlTreeBuilderState state) {
/* 884 */     this.tmplInsertMode.add(state);
/*     */   }
/*     */   @Nullable
/*     */   HtmlTreeBuilderState popTemplateMode() {
/* 888 */     if (this.tmplInsertMode.size() > 0) {
/* 889 */       return this.tmplInsertMode.remove(this.tmplInsertMode.size() - 1);
/*     */     }
/* 891 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   int templateModeSize() {
/* 896 */     return this.tmplInsertMode.size();
/*     */   }
/*     */   @Nullable
/*     */   HtmlTreeBuilderState currentTemplateMode() {
/* 900 */     return (this.tmplInsertMode.size() > 0) ? this.tmplInsertMode.get(this.tmplInsertMode.size() - 1) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 905 */     return "TreeBuilder{currentToken=" + this.currentToken + ", state=" + this.state + ", currentElement=" + 
/*     */ 
/*     */       
/* 908 */       currentElement() + '}';
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isContentForTagData(String normalName) {
/* 913 */     return (normalName.equals("script") || normalName.equals("style"));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\HtmlTreeBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */