/*     */ package org.jsoup.select;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.nodes.Comment;
/*     */ import org.jsoup.nodes.DataNode;
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
/*     */ 
/*     */ 
/*     */ public class Elements
/*     */   extends ArrayList<Element>
/*     */ {
/*     */   public Elements() {}
/*     */   
/*     */   public Elements(int initialCapacity) {
/*  32 */     super(initialCapacity);
/*     */   }
/*     */   
/*     */   public Elements(Collection<Element> elements) {
/*  36 */     super(elements);
/*     */   }
/*     */   
/*     */   public Elements(List<Element> elements) {
/*  40 */     super(elements);
/*     */   }
/*     */   
/*     */   public Elements(Element... elements) {
/*  44 */     super(Arrays.asList(elements));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements clone() {
/*  53 */     Elements clone = new Elements(size());
/*     */     
/*  55 */     for (Element e : this) {
/*  56 */       clone.add(e.clone());
/*     */     }
/*  58 */     return clone;
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
/*     */   public String attr(String attributeKey) {
/*  70 */     for (Element element : this) {
/*  71 */       if (element.hasAttr(attributeKey))
/*  72 */         return element.attr(attributeKey); 
/*     */     } 
/*  74 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAttr(String attributeKey) {
/*  83 */     for (Element element : this) {
/*  84 */       if (element.hasAttr(attributeKey))
/*  85 */         return true; 
/*     */     } 
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> eachAttr(String attributeKey) {
/*  98 */     List<String> attrs = new ArrayList<>(size());
/*  99 */     for (Element element : this) {
/* 100 */       if (element.hasAttr(attributeKey))
/* 101 */         attrs.add(element.attr(attributeKey)); 
/*     */     } 
/* 103 */     return attrs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements attr(String attributeKey, String attributeValue) {
/* 113 */     for (Element element : this) {
/* 114 */       element.attr(attributeKey, attributeValue);
/*     */     }
/* 116 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements removeAttr(String attributeKey) {
/* 125 */     for (Element element : this) {
/* 126 */       element.removeAttr(attributeKey);
/*     */     }
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements addClass(String className) {
/* 137 */     for (Element element : this) {
/* 138 */       element.addClass(className);
/*     */     }
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements removeClass(String className) {
/* 149 */     for (Element element : this) {
/* 150 */       element.removeClass(className);
/*     */     }
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements toggleClass(String className) {
/* 161 */     for (Element element : this) {
/* 162 */       element.toggleClass(className);
/*     */     }
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasClass(String className) {
/* 173 */     for (Element element : this) {
/* 174 */       if (element.hasClass(className))
/* 175 */         return true; 
/*     */     } 
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String val() {
/* 186 */     if (size() > 0)
/*     */     {
/* 188 */       return first().val();
/*     */     }
/* 190 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements val(String value) {
/* 199 */     for (Element element : this)
/* 200 */       element.val(value); 
/* 201 */     return this;
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
/*     */   public String text() {
/* 214 */     StringBuilder sb = StringUtil.borrowBuilder();
/* 215 */     for (Element element : this) {
/* 216 */       if (sb.length() != 0)
/* 217 */         sb.append(" "); 
/* 218 */       sb.append(element.text());
/*     */     } 
/* 220 */     return StringUtil.releaseBuilder(sb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasText() {
/* 229 */     for (Element element : this) {
/* 230 */       if (element.hasText())
/* 231 */         return true; 
/*     */     } 
/* 233 */     return false;
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
/*     */   public List<String> eachText() {
/* 245 */     ArrayList<String> texts = new ArrayList<>(size());
/* 246 */     for (Element el : this) {
/* 247 */       if (el.hasText())
/* 248 */         texts.add(el.text()); 
/*     */     } 
/* 250 */     return texts;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String html() {
/* 260 */     StringBuilder sb = StringUtil.borrowBuilder();
/* 261 */     for (Element element : this) {
/* 262 */       if (sb.length() != 0)
/* 263 */         sb.append("\n"); 
/* 264 */       sb.append(element.html());
/*     */     } 
/* 266 */     return StringUtil.releaseBuilder(sb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String outerHtml() {
/* 276 */     StringBuilder sb = StringUtil.borrowBuilder();
/* 277 */     for (Element element : this) {
/* 278 */       if (sb.length() != 0)
/* 279 */         sb.append("\n"); 
/* 280 */       sb.append(element.outerHtml());
/*     */     } 
/* 282 */     return StringUtil.releaseBuilder(sb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 293 */     return outerHtml();
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
/*     */   public Elements tagName(String tagName) {
/* 305 */     for (Element element : this) {
/* 306 */       element.tagName(tagName);
/*     */     }
/* 308 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements html(String html) {
/* 318 */     for (Element element : this) {
/* 319 */       element.html(html);
/*     */     }
/* 321 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements prepend(String html) {
/* 331 */     for (Element element : this) {
/* 332 */       element.prepend(html);
/*     */     }
/* 334 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements append(String html) {
/* 344 */     for (Element element : this) {
/* 345 */       element.append(html);
/*     */     }
/* 347 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements before(String html) {
/* 357 */     for (Element element : this) {
/* 358 */       element.before(html);
/*     */     }
/* 360 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements after(String html) {
/* 370 */     for (Element element : this) {
/* 371 */       element.after(html);
/*     */     }
/* 373 */     return this;
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
/*     */   public Elements wrap(String html) {
/* 386 */     Validate.notEmpty(html);
/* 387 */     for (Element element : this) {
/* 388 */       element.wrap(html);
/*     */     }
/* 390 */     return this;
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
/*     */   public Elements unwrap() {
/* 408 */     for (Element element : this) {
/* 409 */       element.unwrap();
/*     */     }
/* 411 */     return this;
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
/*     */   public Elements empty() {
/* 426 */     for (Element element : this) {
/* 427 */       element.empty();
/*     */     }
/* 429 */     return this;
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
/*     */   public Elements remove() {
/* 445 */     for (Element element : this) {
/* 446 */       element.remove();
/*     */     }
/* 448 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements select(String query) {
/* 459 */     return Selector.select(query, this);
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
/*     */   public Elements not(String query) {
/* 473 */     Elements out = Selector.select(query, this);
/* 474 */     return Selector.filterOut(this, out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements eq(int index) {
/* 485 */     return (size() > index) ? new Elements(new Element[] { get(index) }) : new Elements();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean is(String query) {
/* 494 */     Evaluator eval = QueryParser.parse(query);
/* 495 */     for (Element e : this) {
/* 496 */       if (e.is(eval))
/* 497 */         return true; 
/*     */     } 
/* 499 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements next() {
/* 507 */     return siblings((String)null, true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements next(String query) {
/* 516 */     return siblings(query, true, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements nextAll() {
/* 524 */     return siblings((String)null, true, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements nextAll(String query) {
/* 533 */     return siblings(query, true, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements prev() {
/* 541 */     return siblings((String)null, false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements prev(String query) {
/* 550 */     return siblings(query, false, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements prevAll() {
/* 558 */     return siblings((String)null, false, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements prevAll(String query) {
/* 567 */     return siblings(query, false, true);
/*     */   }
/*     */   
/*     */   private Elements siblings(@Nullable String query, boolean next, boolean all) {
/* 571 */     Elements els = new Elements();
/* 572 */     Evaluator eval = (query != null) ? QueryParser.parse(query) : null;
/* 573 */     label26: for (Element e : this) {
/*     */       while (true)
/* 575 */       { Element sib = next ? e.nextElementSibling() : e.previousElementSibling();
/* 576 */         if (sib == null)
/* 577 */           continue label26;  if (eval == null) {
/* 578 */           els.add(sib);
/* 579 */         } else if (sib.is(eval)) {
/* 580 */           els.add(sib);
/* 581 */         }  e = sib;
/* 582 */         if (!all)
/*     */           continue label26;  } 
/* 584 */     }  return els;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements parents() {
/* 592 */     HashSet<Element> combo = new LinkedHashSet<>();
/* 593 */     for (Element e : this) {
/* 594 */       combo.addAll(e.parents());
/*     */     }
/* 596 */     return new Elements(combo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Element first() {
/* 605 */     return isEmpty() ? null : get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Element last() {
/* 613 */     return isEmpty() ? null : get(size() - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements traverse(NodeVisitor nodeVisitor) {
/* 622 */     NodeTraversor.traverse(nodeVisitor, this);
/* 623 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Elements filter(NodeFilter nodeFilter) {
/* 632 */     NodeTraversor.filter(nodeFilter, this);
/* 633 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FormElement> forms() {
/* 642 */     ArrayList<FormElement> forms = new ArrayList<>();
/* 643 */     for (Element el : this) {
/* 644 */       if (el instanceof FormElement)
/* 645 */         forms.add((FormElement)el); 
/* 646 */     }  return forms;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Comment> comments() {
/* 654 */     return childNodesOfType(Comment.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<TextNode> textNodes() {
/* 662 */     return childNodesOfType(TextNode.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<DataNode> dataNodes() {
/* 671 */     return childNodesOfType(DataNode.class);
/*     */   }
/*     */   
/*     */   private <T extends Node> List<T> childNodesOfType(Class<T> tClass) {
/* 675 */     ArrayList<T> nodes = new ArrayList<>();
/* 676 */     for (Element el : this) {
/* 677 */       for (int i = 0; i < el.childNodeSize(); i++) {
/* 678 */         Node node = el.childNode(i);
/* 679 */         if (tClass.isInstance(node))
/* 680 */           nodes.add(tClass.cast(node)); 
/*     */       } 
/*     */     } 
/* 683 */     return nodes;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\select\Elements.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */