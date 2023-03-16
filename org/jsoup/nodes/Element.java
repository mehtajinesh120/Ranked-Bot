/*      */ package org.jsoup.nodes;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.regex.PatternSyntaxException;
/*      */ import javax.annotation.Nullable;
/*      */ import org.jsoup.helper.ChangeNotifyingArrayList;
/*      */ import org.jsoup.helper.Consumer;
/*      */ import org.jsoup.helper.Validate;
/*      */ import org.jsoup.internal.NonnullByDefault;
/*      */ import org.jsoup.internal.Normalizer;
/*      */ import org.jsoup.internal.StringUtil;
/*      */ import org.jsoup.parser.Tag;
/*      */ import org.jsoup.parser.TokenQueue;
/*      */ import org.jsoup.select.Collector;
/*      */ import org.jsoup.select.Elements;
/*      */ import org.jsoup.select.Evaluator;
/*      */ import org.jsoup.select.NodeFilter;
/*      */ import org.jsoup.select.NodeTraversor;
/*      */ import org.jsoup.select.NodeVisitor;
/*      */ import org.jsoup.select.QueryParser;
/*      */ import org.jsoup.select.Selector;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @NonnullByDefault
/*      */ public class Element
/*      */   extends Node
/*      */ {
/*   45 */   private static final List<Element> EmptyChildren = Collections.emptyList();
/*   46 */   private static final Pattern ClassSplit = Pattern.compile("\\s+");
/*   47 */   private static final String BaseUriKey = Attributes.internalKey("baseUri");
/*      */   
/*      */   private Tag tag;
/*      */   
/*      */   @Nullable
/*      */   private WeakReference<List<Element>> shadowChildrenRef;
/*      */   List<Node> childNodes;
/*      */   @Nullable
/*      */   Attributes attributes;
/*      */   
/*      */   public Element(String tag) {
/*   58 */     this(Tag.valueOf(tag), "", (Attributes)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element(Tag tag, @Nullable String baseUri, @Nullable Attributes attributes) {
/*   71 */     Validate.notNull(tag);
/*   72 */     this.childNodes = EmptyNodes;
/*   73 */     this.attributes = attributes;
/*   74 */     this.tag = tag;
/*   75 */     if (baseUri != null) {
/*   76 */       setBaseUri(baseUri);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element(Tag tag, @Nullable String baseUri) {
/*   87 */     this(tag, baseUri, (Attributes)null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean hasChildNodes() {
/*   94 */     return (this.childNodes != EmptyNodes);
/*      */   }
/*      */   
/*      */   protected List<Node> ensureChildNodes() {
/*   98 */     if (this.childNodes == EmptyNodes) {
/*   99 */       this.childNodes = (List<Node>)new NodeList(this, 4);
/*      */     }
/*  101 */     return this.childNodes;
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean hasAttributes() {
/*  106 */     return (this.attributes != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public Attributes attributes() {
/*  111 */     if (this.attributes == null)
/*  112 */       this.attributes = new Attributes(); 
/*  113 */     return this.attributes;
/*      */   }
/*      */ 
/*      */   
/*      */   public String baseUri() {
/*  118 */     return searchUpForAttribute(this, BaseUriKey);
/*      */   }
/*      */   
/*      */   private static String searchUpForAttribute(Element start, String key) {
/*  122 */     Element el = start;
/*  123 */     while (el != null) {
/*  124 */       if (el.attributes != null && el.attributes.hasKey(key))
/*  125 */         return el.attributes.get(key); 
/*  126 */       el = el.parent();
/*      */     } 
/*  128 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   protected void doSetBaseUri(String baseUri) {
/*  133 */     attributes().put(BaseUriKey, baseUri);
/*      */   }
/*      */ 
/*      */   
/*      */   public int childNodeSize() {
/*  138 */     return this.childNodes.size();
/*      */   }
/*      */ 
/*      */   
/*      */   public String nodeName() {
/*  143 */     return this.tag.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String tagName() {
/*  153 */     return this.tag.getName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String normalName() {
/*  163 */     return this.tag.normalName();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element tagName(String tagName) {
/*  175 */     Validate.notEmptyParam(tagName, "tagName");
/*  176 */     this.tag = Tag.valueOf(tagName, NodeUtils.parser(this).settings());
/*  177 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Tag tag() {
/*  186 */     return this.tag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBlock() {
/*  196 */     return this.tag.isBlock();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String id() {
/*  205 */     return (this.attributes != null) ? this.attributes.getIgnoreCase("id") : "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element id(String id) {
/*  214 */     Validate.notNull(id);
/*  215 */     attr("id", id);
/*  216 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element attr(String attributeKey, String attributeValue) {
/*  226 */     super.attr(attributeKey, attributeValue);
/*  227 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element attr(String attributeKey, boolean attributeValue) {
/*  241 */     attributes().put(attributeKey, attributeValue);
/*  242 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, String> dataset() {
/*  259 */     return attributes().dataset();
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   public final Element parent() {
/*  264 */     return (Element)this.parentNode;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements parents() {
/*  272 */     Elements parents = new Elements();
/*  273 */     Element parent = parent();
/*  274 */     while (parent != null && !parent.isNode("#root")) {
/*  275 */       parents.add(parent);
/*  276 */       parent = parent.parent();
/*      */     } 
/*  278 */     return parents;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element child(int index) {
/*  293 */     return childElementsList().get(index);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int childrenSize() {
/*  308 */     return childElementsList().size();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements children() {
/*  320 */     return new Elements(childElementsList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   List<Element> childElementsList() {
/*  329 */     if (childNodeSize() == 0) {
/*  330 */       return EmptyChildren;
/*      */     }
/*      */     List<Element> children;
/*  333 */     if (this.shadowChildrenRef == null || (children = this.shadowChildrenRef.get()) == null) {
/*  334 */       int size = this.childNodes.size();
/*  335 */       children = new ArrayList<>(size);
/*      */       
/*  337 */       for (int i = 0; i < size; i++) {
/*  338 */         Node node = this.childNodes.get(i);
/*  339 */         if (node instanceof Element)
/*  340 */           children.add((Element)node); 
/*      */       } 
/*  342 */       this.shadowChildrenRef = new WeakReference<>(children);
/*      */     } 
/*  344 */     return children;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void nodelistChanged() {
/*  352 */     super.nodelistChanged();
/*  353 */     this.shadowChildrenRef = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<TextNode> textNodes() {
/*  373 */     List<TextNode> textNodes = new ArrayList<>();
/*  374 */     for (Node node : this.childNodes) {
/*  375 */       if (node instanceof TextNode)
/*  376 */         textNodes.add((TextNode)node); 
/*      */     } 
/*  378 */     return Collections.unmodifiableList(textNodes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<DataNode> dataNodes() {
/*  391 */     List<DataNode> dataNodes = new ArrayList<>();
/*  392 */     for (Node node : this.childNodes) {
/*  393 */       if (node instanceof DataNode)
/*  394 */         dataNodes.add((DataNode)node); 
/*      */     } 
/*  396 */     return Collections.unmodifiableList(dataNodes);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements select(String cssQuery) {
/*  418 */     return Selector.select(cssQuery, this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements select(Evaluator evaluator) {
/*  429 */     return Selector.select(evaluator, this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Element selectFirst(String cssQuery) {
/*  443 */     return Selector.selectFirst(cssQuery, this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Element selectFirst(Evaluator evaluator) {
/*  455 */     return Collector.findFirst(evaluator, this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element expectFirst(String cssQuery) {
/*  467 */     return (Element)Validate.ensureNotNull(
/*  468 */         Selector.selectFirst(cssQuery, this), 
/*  469 */         (parent() != null) ? 
/*  470 */         "No elements matched the query '%s' on element '%s'." : 
/*  471 */         "No elements matched the query '%s' in the document.", new Object[] { cssQuery, 
/*  472 */           tagName() });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean is(String cssQuery) {
/*  484 */     return is(QueryParser.parse(cssQuery));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean is(Evaluator evaluator) {
/*  493 */     return evaluator.matches(root(), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Element closest(String cssQuery) {
/*  504 */     return closest(QueryParser.parse(cssQuery));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Element closest(Evaluator evaluator) {
/*  515 */     Validate.notNull(evaluator);
/*  516 */     Element el = this;
/*  517 */     Element root = root();
/*      */     while (true) {
/*  519 */       if (evaluator.matches(root, el))
/*  520 */         return el; 
/*  521 */       el = el.parent();
/*  522 */       if (el == null) {
/*  523 */         return null;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements selectXpath(String xpath) {
/*  545 */     return new Elements(NodeUtils.selectXpath(xpath, this, Element.class));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T extends Node> List<T> selectXpath(String xpath, Class<T> nodeType) {
/*  562 */     return NodeUtils.selectXpath(xpath, this, nodeType);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element appendChild(Node child) {
/*  574 */     Validate.notNull(child);
/*      */ 
/*      */     
/*  577 */     reparentChild(child);
/*  578 */     ensureChildNodes();
/*  579 */     this.childNodes.add(child);
/*  580 */     child.setSiblingIndex(this.childNodes.size() - 1);
/*  581 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element appendChildren(Collection<? extends Node> children) {
/*  592 */     insertChildren(-1, children);
/*  593 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element appendTo(Element parent) {
/*  603 */     Validate.notNull(parent);
/*  604 */     parent.appendChild(this);
/*  605 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element prependChild(Node child) {
/*  615 */     Validate.notNull(child);
/*      */     
/*  617 */     addChildren(0, new Node[] { child });
/*  618 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element prependChildren(Collection<? extends Node> children) {
/*  629 */     insertChildren(0, children);
/*  630 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element insertChildren(int index, Collection<? extends Node> children) {
/*  644 */     Validate.notNull(children, "Children collection to be inserted must not be null.");
/*  645 */     int currentSize = childNodeSize();
/*  646 */     if (index < 0) index += currentSize + 1; 
/*  647 */     Validate.isTrue((index >= 0 && index <= currentSize), "Insert position out of bounds.");
/*      */     
/*  649 */     ArrayList<Node> nodes = new ArrayList<>(children);
/*  650 */     Node[] nodeArray = nodes.<Node>toArray(new Node[0]);
/*  651 */     addChildren(index, nodeArray);
/*  652 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element insertChildren(int index, Node... children) {
/*  665 */     Validate.notNull(children, "Children collection to be inserted must not be null.");
/*  666 */     int currentSize = childNodeSize();
/*  667 */     if (index < 0) index += currentSize + 1; 
/*  668 */     Validate.isTrue((index >= 0 && index <= currentSize), "Insert position out of bounds.");
/*      */     
/*  670 */     addChildren(index, children);
/*  671 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element appendElement(String tagName) {
/*  682 */     Element child = new Element(Tag.valueOf(tagName, NodeUtils.parser(this).settings()), baseUri());
/*  683 */     appendChild(child);
/*  684 */     return child;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element prependElement(String tagName) {
/*  695 */     Element child = new Element(Tag.valueOf(tagName, NodeUtils.parser(this).settings()), baseUri());
/*  696 */     prependChild(child);
/*  697 */     return child;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element appendText(String text) {
/*  707 */     Validate.notNull(text);
/*  708 */     TextNode node = new TextNode(text);
/*  709 */     appendChild(node);
/*  710 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element prependText(String text) {
/*  720 */     Validate.notNull(text);
/*  721 */     TextNode node = new TextNode(text);
/*  722 */     prependChild(node);
/*  723 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element append(String html) {
/*  733 */     Validate.notNull(html);
/*  734 */     List<Node> nodes = NodeUtils.parser(this).parseFragmentInput(html, this, baseUri());
/*  735 */     addChildren(nodes.<Node>toArray(new Node[0]));
/*  736 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element prepend(String html) {
/*  746 */     Validate.notNull(html);
/*  747 */     List<Node> nodes = NodeUtils.parser(this).parseFragmentInput(html, this, baseUri());
/*  748 */     addChildren(0, nodes.<Node>toArray(new Node[0]));
/*  749 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element before(String html) {
/*  761 */     return (Element)super.before(html);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element before(Node node) {
/*  772 */     return (Element)super.before(node);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element after(String html) {
/*  784 */     return (Element)super.after(html);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element after(Node node) {
/*  795 */     return (Element)super.after(node);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element empty() {
/*  804 */     this.childNodes.clear();
/*  805 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element wrap(String html) {
/*  816 */     return (Element)super.wrap(html);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String cssSelector() {
/*  830 */     if (id().length() > 0) {
/*      */       
/*  832 */       String idSel = "#" + TokenQueue.escapeCssIdentifier(id());
/*  833 */       Document doc = ownerDocument();
/*  834 */       if (doc != null) {
/*  835 */         Elements els = doc.select(idSel);
/*  836 */         if (els.size() == 1 && els.get(0) == this)
/*  837 */           return idSel; 
/*      */       } else {
/*  839 */         return idSel;
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/*  844 */     String tagName = TokenQueue.escapeCssIdentifier(tagName()).replace("\\:", "|");
/*  845 */     StringBuilder selector = StringUtil.borrowBuilder().append(tagName);
/*      */ 
/*      */     
/*  848 */     StringUtil.StringJoiner escapedClasses = new StringUtil.StringJoiner(".");
/*  849 */     for (String name : classNames()) escapedClasses.add(TokenQueue.escapeCssIdentifier(name)); 
/*  850 */     String classes = escapedClasses.complete();
/*  851 */     if (classes.length() > 0) {
/*  852 */       selector.append('.').append(classes);
/*      */     }
/*  854 */     if (parent() == null || parent() instanceof Document) {
/*  855 */       return StringUtil.releaseBuilder(selector);
/*      */     }
/*  857 */     selector.insert(0, " > ");
/*  858 */     if (parent().select(selector.toString()).size() > 1)
/*  859 */       selector.append(String.format(":nth-child(%d)", new Object[] {
/*  860 */               Integer.valueOf(elementSiblingIndex() + 1)
/*      */             })); 
/*  862 */     return parent().cssSelector() + StringUtil.releaseBuilder(selector);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements siblingElements() {
/*  871 */     if (this.parentNode == null) {
/*  872 */       return new Elements(0);
/*      */     }
/*  874 */     List<Element> elements = parent().childElementsList();
/*  875 */     Elements siblings = new Elements(elements.size() - 1);
/*  876 */     for (Element el : elements) {
/*  877 */       if (el != this)
/*  878 */         siblings.add(el); 
/*  879 */     }  return siblings;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Element nextElementSibling() {
/*  892 */     if (this.parentNode == null) return null; 
/*  893 */     List<Element> siblings = parent().childElementsList();
/*  894 */     int index = indexInList(this, siblings);
/*  895 */     if (siblings.size() > index + 1) {
/*  896 */       return siblings.get(index + 1);
/*      */     }
/*  898 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements nextElementSiblings() {
/*  907 */     return nextElementSiblings(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Element previousElementSibling() {
/*  916 */     if (this.parentNode == null) return null; 
/*  917 */     List<Element> siblings = parent().childElementsList();
/*  918 */     int index = indexInList(this, siblings);
/*  919 */     if (index > 0) {
/*  920 */       return siblings.get(index - 1);
/*      */     }
/*  922 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements previousElementSiblings() {
/*  931 */     return nextElementSiblings(false);
/*      */   }
/*      */   
/*      */   private Elements nextElementSiblings(boolean next) {
/*  935 */     Elements els = new Elements();
/*  936 */     if (this.parentNode == null)
/*  937 */       return els; 
/*  938 */     els.add(this);
/*  939 */     return next ? els.nextAll() : els.prevAll();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element firstElementSibling() {
/*  947 */     if (parent() != null) {
/*  948 */       List<Element> siblings = parent().childElementsList();
/*  949 */       return (siblings.size() > 1) ? siblings.get(0) : this;
/*      */     } 
/*  951 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int elementSiblingIndex() {
/*  960 */     if (parent() == null) return 0; 
/*  961 */     return indexInList(this, parent().childElementsList());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element lastElementSibling() {
/*  969 */     if (parent() != null) {
/*  970 */       List<Element> siblings = parent().childElementsList();
/*  971 */       return (siblings.size() > 1) ? siblings.get(siblings.size() - 1) : this;
/*      */     } 
/*  973 */     return this;
/*      */   }
/*      */   
/*      */   private static <E extends Element> int indexInList(Element search, List<E> elements) {
/*  977 */     int size = elements.size();
/*  978 */     for (int i = 0; i < size; i++) {
/*  979 */       if (elements.get(i) == search)
/*  980 */         return i; 
/*      */     } 
/*  982 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Element firstElementChild() {
/*  993 */     int size = childNodeSize();
/*  994 */     if (size == 0) return null; 
/*  995 */     List<Node> children = ensureChildNodes();
/*  996 */     for (int i = 0; i < size; i++) {
/*  997 */       Node node = children.get(i);
/*  998 */       if (node instanceof Element) return (Element)node; 
/*      */     } 
/* 1000 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Element lastElementChild() {
/* 1011 */     int size = childNodeSize();
/* 1012 */     if (size == 0) return null; 
/* 1013 */     List<Node> children = ensureChildNodes();
/* 1014 */     for (int i = size - 1; i >= 0; i--) {
/* 1015 */       Node node = children.get(i);
/* 1016 */       if (node instanceof Element) return (Element)node; 
/*      */     } 
/* 1018 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByTag(String tagName) {
/* 1029 */     Validate.notEmpty(tagName);
/* 1030 */     tagName = Normalizer.normalize(tagName);
/*      */     
/* 1032 */     return Collector.collect((Evaluator)new Evaluator.Tag(tagName), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Element getElementById(String id) {
/* 1045 */     Validate.notEmpty(id);
/*      */     
/* 1047 */     Elements elements = Collector.collect((Evaluator)new Evaluator.Id(id), this);
/* 1048 */     if (elements.size() > 0) {
/* 1049 */       return (Element)elements.get(0);
/*      */     }
/* 1051 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByClass(String className) {
/* 1066 */     Validate.notEmpty(className);
/*      */     
/* 1068 */     return Collector.collect((Evaluator)new Evaluator.Class(className), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByAttribute(String key) {
/* 1078 */     Validate.notEmpty(key);
/* 1079 */     key = key.trim();
/*      */     
/* 1081 */     return Collector.collect((Evaluator)new Evaluator.Attribute(key), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByAttributeStarting(String keyPrefix) {
/* 1091 */     Validate.notEmpty(keyPrefix);
/* 1092 */     keyPrefix = keyPrefix.trim();
/*      */     
/* 1094 */     return Collector.collect((Evaluator)new Evaluator.AttributeStarting(keyPrefix), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByAttributeValue(String key, String value) {
/* 1105 */     return Collector.collect((Evaluator)new Evaluator.AttributeWithValue(key, value), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByAttributeValueNot(String key, String value) {
/* 1116 */     return Collector.collect((Evaluator)new Evaluator.AttributeWithValueNot(key, value), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByAttributeValueStarting(String key, String valuePrefix) {
/* 1127 */     return Collector.collect((Evaluator)new Evaluator.AttributeWithValueStarting(key, valuePrefix), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByAttributeValueEnding(String key, String valueSuffix) {
/* 1138 */     return Collector.collect((Evaluator)new Evaluator.AttributeWithValueEnding(key, valueSuffix), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByAttributeValueContaining(String key, String match) {
/* 1149 */     return Collector.collect((Evaluator)new Evaluator.AttributeWithValueContaining(key, match), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByAttributeValueMatching(String key, Pattern pattern) {
/* 1159 */     return Collector.collect((Evaluator)new Evaluator.AttributeWithValueMatching(key, pattern), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByAttributeValueMatching(String key, String regex) {
/*      */     Pattern pattern;
/*      */     try {
/* 1172 */       pattern = Pattern.compile(regex);
/* 1173 */     } catch (PatternSyntaxException e) {
/* 1174 */       throw new IllegalArgumentException("Pattern syntax error: " + regex, e);
/*      */     } 
/* 1176 */     return getElementsByAttributeValueMatching(key, pattern);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByIndexLessThan(int index) {
/* 1185 */     return Collector.collect((Evaluator)new Evaluator.IndexLessThan(index), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByIndexGreaterThan(int index) {
/* 1194 */     return Collector.collect((Evaluator)new Evaluator.IndexGreaterThan(index), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsByIndexEquals(int index) {
/* 1203 */     return Collector.collect((Evaluator)new Evaluator.IndexEquals(index), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsContainingText(String searchText) {
/* 1214 */     return Collector.collect((Evaluator)new Evaluator.ContainsText(searchText), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsContainingOwnText(String searchText) {
/* 1225 */     return Collector.collect((Evaluator)new Evaluator.ContainsOwnText(searchText), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsMatchingText(Pattern pattern) {
/* 1235 */     return Collector.collect((Evaluator)new Evaluator.Matches(pattern), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsMatchingText(String regex) {
/*      */     Pattern pattern;
/*      */     try {
/* 1247 */       pattern = Pattern.compile(regex);
/* 1248 */     } catch (PatternSyntaxException e) {
/* 1249 */       throw new IllegalArgumentException("Pattern syntax error: " + regex, e);
/*      */     } 
/* 1251 */     return getElementsMatchingText(pattern);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsMatchingOwnText(Pattern pattern) {
/* 1261 */     return Collector.collect((Evaluator)new Evaluator.MatchesOwn(pattern), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getElementsMatchingOwnText(String regex) {
/*      */     Pattern pattern;
/*      */     try {
/* 1273 */       pattern = Pattern.compile(regex);
/* 1274 */     } catch (PatternSyntaxException e) {
/* 1275 */       throw new IllegalArgumentException("Pattern syntax error: " + regex, e);
/*      */     } 
/* 1277 */     return getElementsMatchingOwnText(pattern);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Elements getAllElements() {
/* 1286 */     return Collector.collect((Evaluator)new Evaluator.AllElements(), this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String text() {
/* 1306 */     final StringBuilder accum = StringUtil.borrowBuilder();
/* 1307 */     NodeTraversor.traverse(new NodeVisitor() {
/*      */           public void head(Node node, int depth) {
/* 1309 */             if (node instanceof TextNode) {
/* 1310 */               TextNode textNode = (TextNode)node;
/* 1311 */               Element.appendNormalisedText(accum, textNode);
/* 1312 */             } else if (node instanceof Element) {
/* 1313 */               Element element = (Element)node;
/* 1314 */               if (accum.length() > 0 && (element
/* 1315 */                 .isBlock() || element.isNode("br")) && 
/* 1316 */                 !TextNode.lastCharIsWhitespace(accum)) {
/* 1317 */                 accum.append(' ');
/*      */               }
/*      */             } 
/*      */           }
/*      */           
/*      */           public void tail(Node node, int depth) {
/* 1323 */             if (node instanceof Element) {
/* 1324 */               Element element = (Element)node;
/* 1325 */               Node next = node.nextSibling();
/* 1326 */               if (element.isBlock() && (next instanceof TextNode || (next instanceof Element && !((Element)next).tag.formatAsBlock())) && !TextNode.lastCharIsWhitespace(accum)) {
/* 1327 */                 accum.append(' ');
/*      */               }
/*      */             } 
/*      */           }
/*      */         }this);
/*      */     
/* 1333 */     return StringUtil.releaseBuilder(accum).trim();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String wholeText() {
/* 1344 */     StringBuilder accum = StringUtil.borrowBuilder();
/* 1345 */     NodeTraversor.traverse((node, depth) -> appendWholeText(node, accum), this);
/* 1346 */     return StringUtil.releaseBuilder(accum);
/*      */   }
/*      */   
/*      */   private static void appendWholeText(Node node, StringBuilder accum) {
/* 1350 */     if (node instanceof TextNode) {
/* 1351 */       accum.append(((TextNode)node).getWholeText());
/* 1352 */     } else if (node.isNode("br")) {
/* 1353 */       accum.append("\n");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String wholeOwnText() {
/* 1367 */     StringBuilder accum = StringUtil.borrowBuilder();
/* 1368 */     int size = childNodeSize();
/* 1369 */     for (int i = 0; i < size; i++) {
/* 1370 */       Node node = this.childNodes.get(i);
/* 1371 */       appendWholeText(node, accum);
/*      */     } 
/*      */     
/* 1374 */     return StringUtil.releaseBuilder(accum);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String ownText() {
/* 1389 */     StringBuilder sb = StringUtil.borrowBuilder();
/* 1390 */     ownText(sb);
/* 1391 */     return StringUtil.releaseBuilder(sb).trim();
/*      */   }
/*      */   
/*      */   private void ownText(StringBuilder accum) {
/* 1395 */     for (int i = 0; i < childNodeSize(); i++) {
/* 1396 */       Node child = this.childNodes.get(i);
/* 1397 */       if (child instanceof TextNode) {
/* 1398 */         TextNode textNode = (TextNode)child;
/* 1399 */         appendNormalisedText(accum, textNode);
/* 1400 */       } else if (child.isNode("br") && !TextNode.lastCharIsWhitespace(accum)) {
/* 1401 */         accum.append(" ");
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private static void appendNormalisedText(StringBuilder accum, TextNode textNode) {
/* 1407 */     String text = textNode.getWholeText();
/* 1408 */     if (preserveWhitespace(textNode.parentNode) || textNode instanceof CDataNode) {
/* 1409 */       accum.append(text);
/*      */     } else {
/* 1411 */       StringUtil.appendNormalisedWhitespace(accum, text, TextNode.lastCharIsWhitespace(accum));
/*      */     } 
/*      */   }
/*      */   
/*      */   static boolean preserveWhitespace(@Nullable Node node) {
/* 1416 */     if (node instanceof Element) {
/* 1417 */       Element el = (Element)node;
/* 1418 */       int i = 0;
/*      */       do {
/* 1420 */         if (el.tag.preserveWhitespace())
/* 1421 */           return true; 
/* 1422 */         el = el.parent();
/* 1423 */         ++i;
/* 1424 */       } while (i < 6 && el != null);
/*      */     } 
/* 1426 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element text(String text) {
/* 1437 */     Validate.notNull(text);
/* 1438 */     empty();
/*      */     
/* 1440 */     Document owner = ownerDocument();
/*      */     
/* 1442 */     if (owner != null && owner.parser().isContentForTagData(normalName())) {
/* 1443 */       appendChild(new DataNode(text));
/*      */     } else {
/* 1445 */       appendChild(new TextNode(text));
/*      */     } 
/* 1447 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasText() {
/* 1455 */     AtomicBoolean hasText = new AtomicBoolean(false);
/* 1456 */     filter((node, depth) -> {
/*      */           if (node instanceof TextNode) {
/*      */             TextNode textNode = (TextNode)node;
/*      */             if (!textNode.isBlank()) {
/*      */               hasText.set(true);
/*      */               return NodeFilter.FilterResult.STOP;
/*      */             } 
/*      */           } 
/*      */           return NodeFilter.FilterResult.CONTINUE;
/*      */         });
/* 1466 */     return hasText.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String data() {
/* 1479 */     StringBuilder sb = StringUtil.borrowBuilder();
/* 1480 */     traverse((childNode, depth) -> {
/*      */           if (childNode instanceof DataNode) {
/*      */             DataNode data = (DataNode)childNode;
/*      */             
/*      */             sb.append(data.getWholeData());
/*      */           } else if (childNode instanceof Comment) {
/*      */             Comment comment = (Comment)childNode;
/*      */             
/*      */             sb.append(comment.getData());
/*      */           } else if (childNode instanceof CDataNode) {
/*      */             CDataNode cDataNode = (CDataNode)childNode;
/*      */             sb.append(cDataNode.getWholeText());
/*      */           } 
/*      */         });
/* 1494 */     return StringUtil.releaseBuilder(sb);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String className() {
/* 1503 */     return attr("class").trim();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Set<String> classNames() {
/* 1513 */     String[] names = ClassSplit.split(className());
/* 1514 */     Set<String> classNames = new LinkedHashSet<>(Arrays.asList(names));
/* 1515 */     classNames.remove("");
/*      */     
/* 1517 */     return classNames;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element classNames(Set<String> classNames) {
/* 1526 */     Validate.notNull(classNames);
/* 1527 */     if (classNames.isEmpty()) {
/* 1528 */       attributes().remove("class");
/*      */     } else {
/* 1530 */       attributes().put("class", StringUtil.join(classNames, " "));
/*      */     } 
/* 1532 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasClass(String className) {
/* 1542 */     if (this.attributes == null) {
/* 1543 */       return false;
/*      */     }
/* 1545 */     String classAttr = this.attributes.getIgnoreCase("class");
/* 1546 */     int len = classAttr.length();
/* 1547 */     int wantLen = className.length();
/*      */     
/* 1549 */     if (len == 0 || len < wantLen) {
/* 1550 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1554 */     if (len == wantLen) {
/* 1555 */       return className.equalsIgnoreCase(classAttr);
/*      */     }
/*      */ 
/*      */     
/* 1559 */     boolean inClass = false;
/* 1560 */     int start = 0;
/* 1561 */     for (int i = 0; i < len; i++) {
/* 1562 */       if (Character.isWhitespace(classAttr.charAt(i))) {
/* 1563 */         if (inClass)
/*      */         {
/* 1565 */           if (i - start == wantLen && classAttr.regionMatches(true, start, className, 0, wantLen)) {
/* 1566 */             return true;
/*      */           }
/* 1568 */           inClass = false;
/*      */         }
/*      */       
/* 1571 */       } else if (!inClass) {
/*      */         
/* 1573 */         inClass = true;
/* 1574 */         start = i;
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1580 */     if (inClass && len - start == wantLen) {
/* 1581 */       return classAttr.regionMatches(true, start, className, 0, wantLen);
/*      */     }
/*      */     
/* 1584 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element addClass(String className) {
/* 1593 */     Validate.notNull(className);
/*      */     
/* 1595 */     Set<String> classes = classNames();
/* 1596 */     classes.add(className);
/* 1597 */     classNames(classes);
/*      */     
/* 1599 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element removeClass(String className) {
/* 1608 */     Validate.notNull(className);
/*      */     
/* 1610 */     Set<String> classes = classNames();
/* 1611 */     classes.remove(className);
/* 1612 */     classNames(classes);
/*      */     
/* 1614 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element toggleClass(String className) {
/* 1623 */     Validate.notNull(className);
/*      */     
/* 1625 */     Set<String> classes = classNames();
/* 1626 */     if (classes.contains(className)) {
/* 1627 */       classes.remove(className);
/*      */     } else {
/* 1629 */       classes.add(className);
/* 1630 */     }  classNames(classes);
/*      */     
/* 1632 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String val() {
/* 1640 */     if (normalName().equals("textarea")) {
/* 1641 */       return text();
/*      */     }
/* 1643 */     return attr("value");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element val(String value) {
/* 1652 */     if (normalName().equals("textarea")) {
/* 1653 */       text(value);
/*      */     } else {
/* 1655 */       attr("value", value);
/* 1656 */     }  return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Range endSourceRange() {
/* 1669 */     return Range.of(this, false);
/*      */   }
/*      */   
/*      */   boolean shouldIndent(Document.OutputSettings out) {
/* 1673 */     return (out.prettyPrint() && isFormatAsBlock(out) && !isInlineable(out));
/*      */   }
/*      */ 
/*      */   
/*      */   void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
/* 1678 */     if (shouldIndent(out)) {
/* 1679 */       if (accum instanceof StringBuilder) {
/* 1680 */         if (((StringBuilder)accum).length() > 0)
/* 1681 */           indent(accum, depth, out); 
/*      */       } else {
/* 1683 */         indent(accum, depth, out);
/*      */       } 
/*      */     }
/* 1686 */     accum.append('<').append(tagName());
/* 1687 */     if (this.attributes != null) this.attributes.html(accum, out);
/*      */ 
/*      */     
/* 1690 */     if (this.childNodes.isEmpty() && this.tag.isSelfClosing()) {
/* 1691 */       if (out.syntax() == Document.OutputSettings.Syntax.html && this.tag.isEmpty()) {
/* 1692 */         accum.append('>');
/*      */       } else {
/* 1694 */         accum.append(" />");
/*      */       } 
/*      */     } else {
/* 1697 */       accum.append('>');
/*      */     } 
/*      */   }
/*      */   
/*      */   void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
/* 1702 */     if (!this.childNodes.isEmpty() || !this.tag.isSelfClosing()) {
/* 1703 */       if (out.prettyPrint() && !this.childNodes.isEmpty() && (this.tag
/* 1704 */         .formatAsBlock() || (out.outline() && (this.childNodes.size() > 1 || (this.childNodes.size() == 1 && this.childNodes.get(0) instanceof Element)))))
/*      */       {
/* 1706 */         indent(accum, depth, out); } 
/* 1707 */       accum.append("</").append(tagName()).append('>');
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String html() {
/* 1719 */     StringBuilder accum = StringUtil.borrowBuilder();
/* 1720 */     html(accum);
/* 1721 */     String html = StringUtil.releaseBuilder(accum);
/* 1722 */     return NodeUtils.outputSettings(this).prettyPrint() ? html.trim() : html;
/*      */   }
/*      */ 
/*      */   
/*      */   public <T extends Appendable> T html(T appendable) {
/* 1727 */     int size = this.childNodes.size();
/* 1728 */     for (int i = 0; i < size; i++) {
/* 1729 */       ((Node)this.childNodes.get(i)).outerHtml((Appendable)appendable);
/*      */     }
/* 1731 */     return appendable;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element html(String html) {
/* 1741 */     empty();
/* 1742 */     append(html);
/* 1743 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Element clone() {
/* 1748 */     return (Element)super.clone();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Element shallowClone() {
/* 1754 */     return new Element(this.tag, baseUri(), (this.attributes == null) ? null : this.attributes.clone());
/*      */   }
/*      */ 
/*      */   
/*      */   protected Element doClone(@Nullable Node parent) {
/* 1759 */     Element clone = (Element)super.doClone(parent);
/* 1760 */     clone.attributes = (this.attributes != null) ? this.attributes.clone() : null;
/* 1761 */     clone.childNodes = (List<Node>)new NodeList(clone, this.childNodes.size());
/* 1762 */     clone.childNodes.addAll(this.childNodes);
/*      */     
/* 1764 */     return clone;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Element clearAttributes() {
/* 1770 */     if (this.attributes != null) {
/* 1771 */       super.clearAttributes();
/* 1772 */       this.attributes = null;
/*      */     } 
/*      */     
/* 1775 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Element removeAttr(String attributeKey) {
/* 1780 */     return (Element)super.removeAttr(attributeKey);
/*      */   }
/*      */ 
/*      */   
/*      */   public Element root() {
/* 1785 */     return (Element)super.root();
/*      */   }
/*      */ 
/*      */   
/*      */   public Element traverse(NodeVisitor nodeVisitor) {
/* 1790 */     return (Element)super.traverse(nodeVisitor);
/*      */   }
/*      */ 
/*      */   
/*      */   public Element forEachNode(Consumer<? super Node> action) {
/* 1795 */     return (Element)super.forEachNode(action);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element forEach(Consumer<? super Element> action) {
/* 1806 */     Validate.notNull(action);
/* 1807 */     NodeTraversor.traverse((node, depth) -> { if (node instanceof Element) action.accept((Element)node);  }this);
/*      */ 
/*      */ 
/*      */     
/* 1811 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Element forEach(Consumer<? super Element> action) {
/* 1818 */     Validate.notNull(action);
/* 1819 */     NodeTraversor.traverse((node, depth) -> { if (node instanceof Element) action.accept(node);  }this);
/*      */ 
/*      */ 
/*      */     
/* 1823 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Element filter(NodeFilter nodeFilter) {
/* 1828 */     return (Element)super.filter(nodeFilter);
/*      */   }
/*      */   
/*      */   private static final class NodeList extends ChangeNotifyingArrayList<Node> {
/*      */     private final Element owner;
/*      */     
/*      */     NodeList(Element owner, int initialCapacity) {
/* 1835 */       super(initialCapacity);
/* 1836 */       this.owner = owner;
/*      */     }
/*      */     
/*      */     public void onContentsChanged() {
/* 1840 */       this.owner.nodelistChanged();
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean isFormatAsBlock(Document.OutputSettings out) {
/* 1845 */     return (this.tag.formatAsBlock() || (parent() != null && parent().tag().formatAsBlock()) || out.outline());
/*      */   }
/*      */   
/*      */   private boolean isInlineable(Document.OutputSettings out) {
/* 1849 */     return (tag().isInline() && (
/* 1850 */       parent() == null || parent().isBlock()) && 
/* 1851 */       previousSibling() != null && 
/* 1852 */       !out.outline());
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\Element.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */