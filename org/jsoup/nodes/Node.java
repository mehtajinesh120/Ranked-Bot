/*     */ package org.jsoup.nodes;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nullable;
/*     */ import org.jsoup.SerializationException;
/*     */ import org.jsoup.helper.Consumer;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.select.NodeFilter;
/*     */ import org.jsoup.select.NodeTraversor;
/*     */ import org.jsoup.select.NodeVisitor;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Node
/*     */   implements Cloneable
/*     */ {
/*  25 */   static final List<Node> EmptyNodes = Collections.emptyList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final String EmptyString = "";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   Node parentNode;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int siblingIndex;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String normalName() {
/*  49 */     return nodeName();
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
/*     */   public boolean hasParent() {
/*  63 */     return (this.parentNode != null);
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
/*     */   public String attr(String attributeKey) {
/*  82 */     Validate.notNull(attributeKey);
/*  83 */     if (!hasAttributes()) {
/*  84 */       return "";
/*     */     }
/*  86 */     String val = attributes().getIgnoreCase(attributeKey);
/*  87 */     if (val.length() > 0)
/*  88 */       return val; 
/*  89 */     if (attributeKey.startsWith("abs:"))
/*  90 */       return absUrl(attributeKey.substring("abs:".length())); 
/*  91 */     return "";
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
/*     */   public int attributesSize() {
/* 107 */     return hasAttributes() ? attributes().size() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node attr(String attributeKey, String attributeValue) {
/* 118 */     attributeKey = NodeUtils.parser(this).settings().normalizeAttribute(attributeKey);
/* 119 */     attributes().putIgnoreCase(attributeKey, attributeValue);
/* 120 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAttr(String attributeKey) {
/* 129 */     Validate.notNull(attributeKey);
/* 130 */     if (!hasAttributes()) {
/* 131 */       return false;
/*     */     }
/* 133 */     if (attributeKey.startsWith("abs:")) {
/* 134 */       String key = attributeKey.substring("abs:".length());
/* 135 */       if (attributes().hasKeyIgnoreCase(key) && !absUrl(key).isEmpty())
/* 136 */         return true; 
/*     */     } 
/* 138 */     return attributes().hasKeyIgnoreCase(attributeKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node removeAttr(String attributeKey) {
/* 147 */     Validate.notNull(attributeKey);
/* 148 */     if (hasAttributes())
/* 149 */       attributes().removeIgnoreCase(attributeKey); 
/* 150 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node clearAttributes() {
/* 158 */     if (hasAttributes()) {
/* 159 */       Iterator<Attribute> it = attributes().iterator();
/* 160 */       while (it.hasNext()) {
/* 161 */         it.next();
/* 162 */         it.remove();
/*     */       } 
/*     */     } 
/* 165 */     return this;
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
/*     */   public void setBaseUri(String baseUri) {
/* 188 */     Validate.notNull(baseUri);
/* 189 */     doSetBaseUri(baseUri);
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
/*     */   public String absUrl(String attributeKey) {
/* 216 */     Validate.notEmpty(attributeKey);
/* 217 */     if (!hasAttributes() || !attributes().hasKeyIgnoreCase(attributeKey)) {
/* 218 */       return "";
/*     */     }
/* 220 */     return StringUtil.resolve(baseUri(), attributes().getIgnoreCase(attributeKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node childNode(int index) {
/* 231 */     return ensureChildNodes().get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Node> childNodes() {
/* 240 */     if (childNodeSize() == 0) {
/* 241 */       return EmptyNodes;
/*     */     }
/* 243 */     List<Node> children = ensureChildNodes();
/* 244 */     List<Node> rewrap = new ArrayList<>(children.size());
/* 245 */     rewrap.addAll(children);
/* 246 */     return Collections.unmodifiableList(rewrap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Node> childNodesCopy() {
/* 255 */     List<Node> nodes = ensureChildNodes();
/* 256 */     ArrayList<Node> children = new ArrayList<>(nodes.size());
/* 257 */     for (Node node : nodes) {
/* 258 */       children.add(node.clone());
/*     */     }
/* 260 */     return children;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Node[] childNodesAsArray() {
/* 270 */     return ensureChildNodes().<Node>toArray(new Node[0]);
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
/*     */   @Nullable
/*     */   public Node parent() {
/* 286 */     return this.parentNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public final Node parentNode() {
/* 294 */     return this.parentNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node root() {
/* 302 */     Node node = this;
/* 303 */     while (node.parentNode != null)
/* 304 */       node = node.parentNode; 
/* 305 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Document ownerDocument() {
/* 313 */     Node root = root();
/* 314 */     return (root instanceof Document) ? (Document)root : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {
/* 321 */     Validate.notNull(this.parentNode);
/* 322 */     this.parentNode.removeChild(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node before(String html) {
/* 332 */     addSiblingHtml(this.siblingIndex, html);
/* 333 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node before(Node node) {
/* 343 */     Validate.notNull(node);
/* 344 */     Validate.notNull(this.parentNode);
/*     */     
/* 346 */     this.parentNode.addChildren(this.siblingIndex, new Node[] { node });
/* 347 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node after(String html) {
/* 357 */     addSiblingHtml(this.siblingIndex + 1, html);
/* 358 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node after(Node node) {
/* 368 */     Validate.notNull(node);
/* 369 */     Validate.notNull(this.parentNode);
/*     */     
/* 371 */     this.parentNode.addChildren(this.siblingIndex + 1, new Node[] { node });
/* 372 */     return this;
/*     */   }
/*     */   
/*     */   private void addSiblingHtml(int index, String html) {
/* 376 */     Validate.notNull(html);
/* 377 */     Validate.notNull(this.parentNode);
/*     */     
/* 379 */     Element context = (parent() instanceof Element) ? (Element)parent() : null;
/* 380 */     List<Node> nodes = NodeUtils.parser(this).parseFragmentInput(html, context, baseUri());
/* 381 */     this.parentNode.addChildren(index, nodes.<Node>toArray(new Node[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node wrap(String html) {
/* 392 */     Validate.notEmpty(html);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 398 */     Element context = (this.parentNode != null && this.parentNode instanceof Element) ? (Element)this.parentNode : ((this instanceof Element) ? (Element)this : null);
/* 399 */     List<Node> wrapChildren = NodeUtils.parser(this).parseFragmentInput(html, context, baseUri());
/* 400 */     Node wrapNode = wrapChildren.get(0);
/* 401 */     if (!(wrapNode instanceof Element)) {
/* 402 */       return this;
/*     */     }
/* 404 */     Element wrap = (Element)wrapNode;
/* 405 */     Element deepest = getDeepChild(wrap);
/* 406 */     if (this.parentNode != null)
/* 407 */       this.parentNode.replaceChild(this, wrap); 
/* 408 */     deepest.addChildren(new Node[] { this });
/*     */ 
/*     */     
/* 411 */     if (wrapChildren.size() > 0)
/*     */     {
/* 413 */       for (int i = 0; i < wrapChildren.size(); i++) {
/* 414 */         Node remainder = wrapChildren.get(i);
/*     */         
/* 416 */         if (wrap != remainder) {
/*     */ 
/*     */           
/* 419 */           if (remainder.parentNode != null)
/* 420 */             remainder.parentNode.removeChild(remainder); 
/* 421 */           wrap.after(remainder);
/*     */         } 
/*     */       }  } 
/* 424 */     return this;
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
/*     */   @Nullable
/*     */   public Node unwrap() {
/* 443 */     Validate.notNull(this.parentNode);
/* 444 */     Node firstChild = firstChild();
/* 445 */     this.parentNode.addChildren(this.siblingIndex, childNodesAsArray());
/* 446 */     remove();
/*     */     
/* 448 */     return firstChild;
/*     */   }
/*     */   
/*     */   private Element getDeepChild(Element el) {
/* 452 */     while (el.childrenSize() > 0) {
/* 453 */       el = el.childElementsList().get(0);
/*     */     }
/* 455 */     return el;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void nodelistChanged() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replaceWith(Node in) {
/* 467 */     Validate.notNull(in);
/* 468 */     Validate.notNull(this.parentNode);
/* 469 */     this.parentNode.replaceChild(this, in);
/*     */   }
/*     */   
/*     */   protected void setParentNode(Node parentNode) {
/* 473 */     Validate.notNull(parentNode);
/* 474 */     if (this.parentNode != null)
/* 475 */       this.parentNode.removeChild(this); 
/* 476 */     this.parentNode = parentNode;
/*     */   }
/*     */   
/*     */   protected void replaceChild(Node out, Node in) {
/* 480 */     Validate.isTrue((out.parentNode == this));
/* 481 */     Validate.notNull(in);
/* 482 */     if (out == in)
/*     */       return; 
/* 484 */     if (in.parentNode != null) {
/* 485 */       in.parentNode.removeChild(in);
/*     */     }
/* 487 */     int index = out.siblingIndex;
/* 488 */     ensureChildNodes().set(index, in);
/* 489 */     in.parentNode = this;
/* 490 */     in.setSiblingIndex(index);
/* 491 */     out.parentNode = null;
/*     */   }
/*     */   
/*     */   protected void removeChild(Node out) {
/* 495 */     Validate.isTrue((out.parentNode == this));
/* 496 */     int index = out.siblingIndex;
/* 497 */     ensureChildNodes().remove(index);
/* 498 */     reindexChildren(index);
/* 499 */     out.parentNode = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addChildren(Node... children) {
/* 504 */     List<Node> nodes = ensureChildNodes();
/*     */     
/* 506 */     for (Node child : children) {
/* 507 */       reparentChild(child);
/* 508 */       nodes.add(child);
/* 509 */       child.setSiblingIndex(nodes.size() - 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void addChildren(int index, Node... children) {
/* 514 */     Validate.notNull(children);
/* 515 */     if (children.length == 0) {
/*     */       return;
/*     */     }
/* 518 */     List<Node> nodes = ensureChildNodes();
/*     */ 
/*     */     
/* 521 */     Node firstParent = children[0].parent();
/* 522 */     if (firstParent != null && firstParent.childNodeSize() == children.length) {
/* 523 */       boolean sameList = true;
/* 524 */       List<Node> firstParentNodes = firstParent.ensureChildNodes();
/*     */       
/* 526 */       int i = children.length;
/* 527 */       while (i-- > 0) {
/* 528 */         if (children[i] != firstParentNodes.get(i)) {
/* 529 */           sameList = false;
/*     */           break;
/*     */         } 
/*     */       } 
/* 533 */       if (sameList) {
/* 534 */         boolean wasEmpty = (childNodeSize() == 0);
/* 535 */         firstParent.empty();
/* 536 */         nodes.addAll(index, Arrays.asList(children));
/* 537 */         i = children.length;
/* 538 */         while (i-- > 0) {
/* 539 */           (children[i]).parentNode = this;
/*     */         }
/* 541 */         if (!wasEmpty || (children[0]).siblingIndex != 0) {
/* 542 */           reindexChildren(index);
/*     */         }
/*     */         return;
/*     */       } 
/*     */     } 
/* 547 */     Validate.noNullElements((Object[])children);
/* 548 */     for (Node child : children) {
/* 549 */       reparentChild(child);
/*     */     }
/* 551 */     nodes.addAll(index, Arrays.asList(children));
/* 552 */     reindexChildren(index);
/*     */   }
/*     */   
/*     */   protected void reparentChild(Node child) {
/* 556 */     child.setParentNode(this);
/*     */   }
/*     */   
/*     */   private void reindexChildren(int start) {
/* 560 */     int size = childNodeSize();
/* 561 */     if (size == 0)
/* 562 */       return;  List<Node> childNodes = ensureChildNodes();
/* 563 */     for (int i = start; i < size; i++) {
/* 564 */       ((Node)childNodes.get(i)).setSiblingIndex(i);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Node> siblingNodes() {
/* 574 */     if (this.parentNode == null) {
/* 575 */       return Collections.emptyList();
/*     */     }
/* 577 */     List<Node> nodes = this.parentNode.ensureChildNodes();
/* 578 */     List<Node> siblings = new ArrayList<>(nodes.size() - 1);
/* 579 */     for (Node node : nodes) {
/* 580 */       if (node != this)
/* 581 */         siblings.add(node); 
/* 582 */     }  return siblings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Node nextSibling() {
/* 590 */     if (this.parentNode == null) {
/* 591 */       return null;
/*     */     }
/* 593 */     List<Node> siblings = this.parentNode.ensureChildNodes();
/* 594 */     int index = this.siblingIndex + 1;
/* 595 */     if (siblings.size() > index) {
/* 596 */       return siblings.get(index);
/*     */     }
/* 598 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Node previousSibling() {
/* 606 */     if (this.parentNode == null) {
/* 607 */       return null;
/*     */     }
/* 609 */     if (this.siblingIndex > 0) {
/* 610 */       return this.parentNode.ensureChildNodes().get(this.siblingIndex - 1);
/*     */     }
/* 612 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int siblingIndex() {
/* 622 */     return this.siblingIndex;
/*     */   }
/*     */   
/*     */   protected void setSiblingIndex(int siblingIndex) {
/* 626 */     this.siblingIndex = siblingIndex;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Node firstChild() {
/* 638 */     if (childNodeSize() == 0) return null; 
/* 639 */     return ensureChildNodes().get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Node lastChild() {
/* 650 */     int size = childNodeSize();
/* 651 */     if (size == 0) return null; 
/* 652 */     List<Node> children = ensureChildNodes();
/* 653 */     return children.get(size - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node traverse(NodeVisitor nodeVisitor) {
/* 662 */     Validate.notNull(nodeVisitor);
/* 663 */     NodeTraversor.traverse(nodeVisitor, this);
/* 664 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node forEachNode(Consumer<? super Node> action) {
/* 675 */     Validate.notNull(action);
/* 676 */     NodeTraversor.traverse((node, depth) -> action.accept(node), this);
/* 677 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node forEachNode(Consumer<? super Node> action) {
/* 684 */     Validate.notNull(action);
/* 685 */     NodeTraversor.traverse((node, depth) -> action.accept(node), this);
/* 686 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node filter(NodeFilter nodeFilter) {
/* 695 */     Validate.notNull(nodeFilter);
/* 696 */     NodeTraversor.filter(nodeFilter, this);
/* 697 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String outerHtml() {
/* 707 */     StringBuilder accum = StringUtil.borrowBuilder();
/* 708 */     outerHtml(accum);
/* 709 */     return StringUtil.releaseBuilder(accum);
/*     */   }
/*     */   
/*     */   protected void outerHtml(Appendable accum) {
/* 713 */     NodeTraversor.traverse(new OuterHtmlVisitor(accum, NodeUtils.outputSettings(this)), this);
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
/*     */   public <T extends Appendable> T html(T appendable) {
/* 732 */     outerHtml((Appendable)appendable);
/* 733 */     return appendable;
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
/*     */   public Range sourceRange() {
/* 745 */     return Range.of(this, true);
/*     */   }
/*     */ 
/*     */   
/*     */   static boolean isNode(@Nullable Node node, String normalName) {
/* 750 */     return (node != null && node.normalName().equals(normalName));
/*     */   }
/*     */ 
/*     */   
/*     */   final boolean isNode(String normalName) {
/* 755 */     return normalName().equals(normalName);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 764 */     return outerHtml();
/*     */   }
/*     */   
/*     */   protected void indent(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
/* 768 */     accum.append('\n').append(StringUtil.padding(depth * out.indentAmount(), out.maxPaddingWidth()));
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
/*     */   public boolean equals(@Nullable Object o) {
/* 781 */     return (this == o);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 792 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasSameValue(@Nullable Object o) {
/* 802 */     if (this == o) return true; 
/* 803 */     if (o == null || getClass() != o.getClass()) return false;
/*     */     
/* 805 */     return outerHtml().equals(((Node)o).outerHtml());
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
/*     */   public Node clone() {
/* 820 */     Node thisClone = doClone(null);
/*     */ 
/*     */     
/* 823 */     LinkedList<Node> nodesToProcess = new LinkedList<>();
/* 824 */     nodesToProcess.add(thisClone);
/*     */     
/* 826 */     while (!nodesToProcess.isEmpty()) {
/* 827 */       Node currParent = nodesToProcess.remove();
/*     */       
/* 829 */       int size = currParent.childNodeSize();
/* 830 */       for (int i = 0; i < size; i++) {
/* 831 */         List<Node> childNodes = currParent.ensureChildNodes();
/* 832 */         Node childClone = ((Node)childNodes.get(i)).doClone(currParent);
/* 833 */         childNodes.set(i, childClone);
/* 834 */         nodesToProcess.add(childClone);
/*     */       } 
/*     */     } 
/*     */     
/* 838 */     return thisClone;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Node shallowClone() {
/* 848 */     return doClone(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Node doClone(@Nullable Node parent) {
/*     */     Node clone;
/*     */     try {
/* 859 */       clone = (Node)super.clone();
/* 860 */     } catch (CloneNotSupportedException e) {
/* 861 */       throw new RuntimeException(e);
/*     */     } 
/*     */     
/* 864 */     clone.parentNode = parent;
/* 865 */     clone.siblingIndex = (parent == null) ? 0 : this.siblingIndex;
/*     */     
/* 867 */     if (parent == null && !(this instanceof Document)) {
/* 868 */       Document doc = ownerDocument();
/* 869 */       if (doc != null) {
/* 870 */         Document docClone = doc.shallowClone();
/* 871 */         clone.parentNode = docClone;
/* 872 */         docClone.ensureChildNodes().add(clone);
/*     */       } 
/*     */     } 
/*     */     
/* 876 */     return clone;
/*     */   } public abstract String nodeName(); protected abstract boolean hasAttributes(); public abstract Attributes attributes(); public abstract String baseUri(); protected abstract void doSetBaseUri(String paramString); protected abstract List<Node> ensureChildNodes();
/*     */   public abstract int childNodeSize();
/*     */   public abstract Node empty();
/*     */   abstract void outerHtmlHead(Appendable paramAppendable, int paramInt, Document.OutputSettings paramOutputSettings) throws IOException;
/*     */   abstract void outerHtmlTail(Appendable paramAppendable, int paramInt, Document.OutputSettings paramOutputSettings) throws IOException;
/*     */   private static class OuterHtmlVisitor implements NodeVisitor { private final Appendable accum;
/*     */     OuterHtmlVisitor(Appendable accum, Document.OutputSettings out) {
/* 884 */       this.accum = accum;
/* 885 */       this.out = out;
/* 886 */       out.prepareEncoder();
/*     */     }
/*     */     private final Document.OutputSettings out;
/*     */     public void head(Node node, int depth) {
/*     */       try {
/* 891 */         node.outerHtmlHead(this.accum, depth, this.out);
/* 892 */       } catch (IOException exception) {
/* 893 */         throw new SerializationException(exception);
/*     */       } 
/*     */     }
/*     */     
/*     */     public void tail(Node node, int depth) {
/* 898 */       if (!node.nodeName().equals("#text"))
/*     */         try {
/* 900 */           node.outerHtmlTail(this.accum, depth, this.out);
/* 901 */         } catch (IOException exception) {
/* 902 */           throw new SerializationException(exception);
/*     */         }  
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\Node.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */