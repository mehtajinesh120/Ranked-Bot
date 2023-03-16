/*     */ package org.jsoup.select;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.Normalizer;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.nodes.Node;
/*     */ import org.jsoup.nodes.PseudoTextElement;
/*     */ import org.jsoup.nodes.TextNode;
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
/*     */ public abstract class Evaluator
/*     */ {
/*     */   public abstract boolean matches(Element paramElement1, Element paramElement2);
/*     */   
/*     */   public static final class Tag
/*     */     extends Evaluator
/*     */   {
/*     */     private final String tagName;
/*     */     
/*     */     public Tag(String tagName) {
/*  47 */       this.tagName = tagName;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/*  52 */       return element.normalName().equals(this.tagName);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  57 */       return String.format("%s", new Object[] { this.tagName });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class TagEndsWith
/*     */     extends Evaluator
/*     */   {
/*     */     private final String tagName;
/*     */ 
/*     */     
/*     */     public TagEndsWith(String tagName) {
/*  69 */       this.tagName = tagName;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/*  74 */       return element.normalName().endsWith(this.tagName);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  79 */       return String.format("%s", new Object[] { this.tagName });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Id
/*     */     extends Evaluator
/*     */   {
/*     */     private final String id;
/*     */     
/*     */     public Id(String id) {
/*  90 */       this.id = id;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/*  95 */       return this.id.equals(element.id());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 100 */       return String.format("#%s", new Object[] { this.id });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Class
/*     */     extends Evaluator
/*     */   {
/*     */     private final String className;
/*     */ 
/*     */     
/*     */     public Class(String className) {
/* 112 */       this.className = className;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 117 */       return element.hasClass(this.className);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 122 */       return String.format(".%s", new Object[] { this.className });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Attribute
/*     */     extends Evaluator
/*     */   {
/*     */     private final String key;
/*     */ 
/*     */     
/*     */     public Attribute(String key) {
/* 134 */       this.key = key;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 139 */       return element.hasAttr(this.key);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 144 */       return String.format("[%s]", new Object[] { this.key });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class AttributeStarting
/*     */     extends Evaluator
/*     */   {
/*     */     private final String keyPrefix;
/*     */ 
/*     */     
/*     */     public AttributeStarting(String keyPrefix) {
/* 156 */       Validate.notEmpty(keyPrefix);
/* 157 */       this.keyPrefix = Normalizer.lowerCase(keyPrefix);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 162 */       List<org.jsoup.nodes.Attribute> values = element.attributes().asList();
/* 163 */       for (org.jsoup.nodes.Attribute attribute : values) {
/* 164 */         if (Normalizer.lowerCase(attribute.getKey()).startsWith(this.keyPrefix))
/* 165 */           return true; 
/*     */       } 
/* 167 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 172 */       return String.format("[^%s]", new Object[] { this.keyPrefix });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class AttributeWithValue
/*     */     extends AttributeKeyPair
/*     */   {
/*     */     public AttributeWithValue(String key, String value) {
/* 182 */       super(key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 187 */       return (element.hasAttr(this.key) && this.value.equalsIgnoreCase(element.attr(this.key).trim()));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 192 */       return String.format("[%s=%s]", new Object[] { this.key, this.value });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class AttributeWithValueNot
/*     */     extends AttributeKeyPair
/*     */   {
/*     */     public AttributeWithValueNot(String key, String value) {
/* 202 */       super(key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 207 */       return !this.value.equalsIgnoreCase(element.attr(this.key));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 212 */       return String.format("[%s!=%s]", new Object[] { this.key, this.value });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class AttributeWithValueStarting
/*     */     extends AttributeKeyPair
/*     */   {
/*     */     public AttributeWithValueStarting(String key, String value) {
/* 222 */       super(key, value, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 227 */       return (element.hasAttr(this.key) && Normalizer.lowerCase(element.attr(this.key)).startsWith(this.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 232 */       return String.format("[%s^=%s]", new Object[] { this.key, this.value });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class AttributeWithValueEnding
/*     */     extends AttributeKeyPair
/*     */   {
/*     */     public AttributeWithValueEnding(String key, String value) {
/* 242 */       super(key, value, false);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 247 */       return (element.hasAttr(this.key) && Normalizer.lowerCase(element.attr(this.key)).endsWith(this.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 252 */       return String.format("[%s$=%s]", new Object[] { this.key, this.value });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class AttributeWithValueContaining
/*     */     extends AttributeKeyPair
/*     */   {
/*     */     public AttributeWithValueContaining(String key, String value) {
/* 262 */       super(key, value);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 267 */       return (element.hasAttr(this.key) && Normalizer.lowerCase(element.attr(this.key)).contains(this.value));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 272 */       return String.format("[%s*=%s]", new Object[] { this.key, this.value });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class AttributeWithValueMatching
/*     */     extends Evaluator
/*     */   {
/*     */     String key;
/*     */     
/*     */     Pattern pattern;
/*     */     
/*     */     public AttributeWithValueMatching(String key, Pattern pattern) {
/* 285 */       this.key = Normalizer.normalize(key);
/* 286 */       this.pattern = pattern;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 291 */       return (element.hasAttr(this.key) && this.pattern.matcher(element.attr(this.key)).find());
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 296 */       return String.format("[%s~=%s]", new Object[] { this.key, this.pattern.toString() });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class AttributeKeyPair
/*     */     extends Evaluator
/*     */   {
/*     */     String key;
/*     */     
/*     */     String value;
/*     */     
/*     */     public AttributeKeyPair(String key, String value) {
/* 309 */       this(key, value, true);
/*     */     }
/*     */     
/*     */     public AttributeKeyPair(String key, String value, boolean trimValue) {
/* 313 */       Validate.notEmpty(key);
/* 314 */       Validate.notEmpty(value);
/*     */       
/* 316 */       this.key = Normalizer.normalize(key);
/*     */       
/* 318 */       boolean isStringLiteral = ((value.startsWith("'") && value.endsWith("'")) || (value.startsWith("\"") && value.endsWith("\"")));
/* 319 */       if (isStringLiteral) {
/* 320 */         value = value.substring(1, value.length() - 1);
/*     */       }
/*     */       
/* 323 */       this.value = trimValue ? Normalizer.normalize(value) : Normalizer.normalize(value, isStringLiteral);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class AllElements
/*     */     extends Evaluator
/*     */   {
/*     */     public boolean matches(Element root, Element element) {
/* 334 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 339 */       return "*";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class IndexLessThan
/*     */     extends IndexEvaluator
/*     */   {
/*     */     public IndexLessThan(int index) {
/* 348 */       super(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 353 */       return (root != element && element.elementSiblingIndex() < this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 358 */       return String.format(":lt(%d)", new Object[] { Integer.valueOf(this.index) });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class IndexGreaterThan
/*     */     extends IndexEvaluator
/*     */   {
/*     */     public IndexGreaterThan(int index) {
/* 368 */       super(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 373 */       return (element.elementSiblingIndex() > this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 378 */       return String.format(":gt(%d)", new Object[] { Integer.valueOf(this.index) });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class IndexEquals
/*     */     extends IndexEvaluator
/*     */   {
/*     */     public IndexEquals(int index) {
/* 388 */       super(index);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 393 */       return (element.elementSiblingIndex() == this.index);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 398 */       return String.format(":eq(%d)", new Object[] { Integer.valueOf(this.index) });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class IsLastChild
/*     */     extends Evaluator
/*     */   {
/*     */     public boolean matches(Element root, Element element) {
/* 409 */       Element p = element.parent();
/* 410 */       return (p != null && !(p instanceof org.jsoup.nodes.Document) && element.elementSiblingIndex() == p.children().size() - 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 415 */       return ":last-child";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class IsFirstOfType extends IsNthOfType {
/*     */     public IsFirstOfType() {
/* 421 */       super(0, 1);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 425 */       return ":first-of-type";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class IsLastOfType extends IsNthLastOfType {
/*     */     public IsLastOfType() {
/* 431 */       super(0, 1);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 435 */       return ":last-of-type";
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract class CssNthEvaluator extends Evaluator {
/*     */     protected final int a;
/*     */     protected final int b;
/*     */     
/*     */     public CssNthEvaluator(int a, int b) {
/* 444 */       this.a = a;
/* 445 */       this.b = b;
/*     */     }
/*     */     public CssNthEvaluator(int b) {
/* 448 */       this(0, b);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 453 */       Element p = element.parent();
/* 454 */       if (p == null || p instanceof org.jsoup.nodes.Document) return false;
/*     */       
/* 456 */       int pos = calculatePosition(root, element);
/* 457 */       if (this.a == 0) return (pos == this.b);
/*     */       
/* 459 */       return ((pos - this.b) * this.a >= 0 && (pos - this.b) % this.a == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 464 */       if (this.a == 0)
/* 465 */         return String.format(":%s(%d)", new Object[] { getPseudoClass(), Integer.valueOf(this.b) }); 
/* 466 */       if (this.b == 0)
/* 467 */         return String.format(":%s(%dn)", new Object[] { getPseudoClass(), Integer.valueOf(this.a) }); 
/* 468 */       return String.format(":%s(%dn%+d)", new Object[] { getPseudoClass(), Integer.valueOf(this.a), Integer.valueOf(this.b) });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected abstract String getPseudoClass();
/*     */ 
/*     */     
/*     */     protected abstract int calculatePosition(Element param1Element1, Element param1Element2);
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class IsNthChild
/*     */     extends CssNthEvaluator
/*     */   {
/*     */     public IsNthChild(int a, int b) {
/* 484 */       super(a, b);
/*     */     }
/*     */     
/*     */     protected int calculatePosition(Element root, Element element) {
/* 488 */       return element.elementSiblingIndex() + 1;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPseudoClass() {
/* 493 */       return "nth-child";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class IsNthLastChild
/*     */     extends CssNthEvaluator
/*     */   {
/*     */     public IsNthLastChild(int a, int b) {
/* 504 */       super(a, b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int calculatePosition(Element root, Element element) {
/* 509 */       if (element.parent() == null)
/* 510 */         return 0; 
/* 511 */       return element.parent().children().size() - element.elementSiblingIndex();
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPseudoClass() {
/* 516 */       return "nth-last-child";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class IsNthOfType
/*     */     extends CssNthEvaluator
/*     */   {
/*     */     public IsNthOfType(int a, int b) {
/* 526 */       super(a, b);
/*     */     }
/*     */     
/*     */     protected int calculatePosition(Element root, Element element) {
/* 530 */       int pos = 0;
/* 531 */       if (element.parent() == null)
/* 532 */         return 0; 
/* 533 */       Elements family = element.parent().children();
/* 534 */       for (Element el : family) {
/* 535 */         if (el.tag().equals(element.tag())) pos++; 
/* 536 */         if (el == element)
/*     */           break; 
/* 538 */       }  return pos;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPseudoClass() {
/* 543 */       return "nth-of-type";
/*     */     }
/*     */   }
/*     */   
/*     */   public static class IsNthLastOfType
/*     */     extends CssNthEvaluator {
/*     */     public IsNthLastOfType(int a, int b) {
/* 550 */       super(a, b);
/*     */     }
/*     */ 
/*     */     
/*     */     protected int calculatePosition(Element root, Element element) {
/* 555 */       int pos = 0;
/* 556 */       if (element.parent() == null)
/* 557 */         return 0; 
/* 558 */       Elements family = element.parent().children();
/* 559 */       for (int i = element.elementSiblingIndex(); i < family.size(); i++) {
/* 560 */         if (family.get(i).tag().equals(element.tag())) pos++; 
/*     */       } 
/* 562 */       return pos;
/*     */     }
/*     */ 
/*     */     
/*     */     protected String getPseudoClass() {
/* 567 */       return "nth-last-of-type";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class IsFirstChild
/*     */     extends Evaluator
/*     */   {
/*     */     public boolean matches(Element root, Element element) {
/* 577 */       Element p = element.parent();
/* 578 */       return (p != null && !(p instanceof org.jsoup.nodes.Document) && element.elementSiblingIndex() == 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 583 */       return ":first-child";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class IsRoot
/*     */     extends Evaluator
/*     */   {
/*     */     public boolean matches(Element root, Element element) {
/* 595 */       Element r = (root instanceof org.jsoup.nodes.Document) ? root.child(0) : root;
/* 596 */       return (element == r);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 600 */       return ":root";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class IsOnlyChild
/*     */     extends Evaluator {
/*     */     public boolean matches(Element root, Element element) {
/* 607 */       Element p = element.parent();
/* 608 */       return (p != null && !(p instanceof org.jsoup.nodes.Document) && element.siblingElements().isEmpty());
/*     */     }
/*     */     
/*     */     public String toString() {
/* 612 */       return ":only-child";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class IsOnlyOfType
/*     */     extends Evaluator {
/*     */     public boolean matches(Element root, Element element) {
/* 619 */       Element p = element.parent();
/* 620 */       if (p == null || p instanceof org.jsoup.nodes.Document) return false;
/*     */       
/* 622 */       int pos = 0;
/* 623 */       Elements family = p.children();
/* 624 */       for (Element el : family) {
/* 625 */         if (el.tag().equals(element.tag())) pos++; 
/*     */       } 
/* 627 */       return (pos == 1);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 631 */       return ":only-of-type";
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class IsEmpty
/*     */     extends Evaluator {
/*     */     public boolean matches(Element root, Element element) {
/* 638 */       List<Node> family = element.childNodes();
/* 639 */       for (Node n : family) {
/* 640 */         if (!(n instanceof org.jsoup.nodes.Comment) && !(n instanceof org.jsoup.nodes.XmlDeclaration) && !(n instanceof org.jsoup.nodes.DocumentType)) return false; 
/*     */       } 
/* 642 */       return true;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 646 */       return ":empty";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static abstract class IndexEvaluator
/*     */     extends Evaluator
/*     */   {
/*     */     int index;
/*     */ 
/*     */     
/*     */     public IndexEvaluator(int index) {
/* 659 */       this.index = index;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ContainsText
/*     */     extends Evaluator
/*     */   {
/*     */     private final String searchText;
/*     */     
/*     */     public ContainsText(String searchText) {
/* 670 */       this.searchText = Normalizer.lowerCase(StringUtil.normaliseWhitespace(searchText));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 675 */       return Normalizer.lowerCase(element.text()).contains(this.searchText);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 680 */       return String.format(":contains(%s)", new Object[] { this.searchText });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ContainsWholeText
/*     */     extends Evaluator
/*     */   {
/*     */     private final String searchText;
/*     */ 
/*     */     
/*     */     public ContainsWholeText(String searchText) {
/* 693 */       this.searchText = searchText;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 698 */       return element.wholeText().contains(this.searchText);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 703 */       return String.format(":containsWholeText(%s)", new Object[] { this.searchText });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class ContainsWholeOwnText
/*     */     extends Evaluator
/*     */   {
/*     */     private final String searchText;
/*     */ 
/*     */     
/*     */     public ContainsWholeOwnText(String searchText) {
/* 716 */       this.searchText = searchText;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 721 */       return element.wholeOwnText().contains(this.searchText);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 726 */       return String.format(":containsWholeOwnText(%s)", new Object[] { this.searchText });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ContainsData
/*     */     extends Evaluator
/*     */   {
/*     */     private final String searchText;
/*     */     
/*     */     public ContainsData(String searchText) {
/* 737 */       this.searchText = Normalizer.lowerCase(searchText);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 742 */       return Normalizer.lowerCase(element.data()).contains(this.searchText);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 747 */       return String.format(":containsData(%s)", new Object[] { this.searchText });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class ContainsOwnText
/*     */     extends Evaluator
/*     */   {
/*     */     private final String searchText;
/*     */     
/*     */     public ContainsOwnText(String searchText) {
/* 758 */       this.searchText = Normalizer.lowerCase(StringUtil.normaliseWhitespace(searchText));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 763 */       return Normalizer.lowerCase(element.ownText()).contains(this.searchText);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 768 */       return String.format(":containsOwn(%s)", new Object[] { this.searchText });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class Matches
/*     */     extends Evaluator
/*     */   {
/*     */     private final Pattern pattern;
/*     */     
/*     */     public Matches(Pattern pattern) {
/* 779 */       this.pattern = pattern;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 784 */       Matcher m = this.pattern.matcher(element.text());
/* 785 */       return m.find();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 790 */       return String.format(":matches(%s)", new Object[] { this.pattern });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class MatchesOwn
/*     */     extends Evaluator
/*     */   {
/*     */     private final Pattern pattern;
/*     */     
/*     */     public MatchesOwn(Pattern pattern) {
/* 801 */       this.pattern = pattern;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 806 */       Matcher m = this.pattern.matcher(element.ownText());
/* 807 */       return m.find();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 812 */       return String.format(":matchesOwn(%s)", new Object[] { this.pattern });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class MatchesWholeText
/*     */     extends Evaluator
/*     */   {
/*     */     private final Pattern pattern;
/*     */ 
/*     */     
/*     */     public MatchesWholeText(Pattern pattern) {
/* 824 */       this.pattern = pattern;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 829 */       Matcher m = this.pattern.matcher(element.wholeText());
/* 830 */       return m.find();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 835 */       return String.format(":matchesWholeText(%s)", new Object[] { this.pattern });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static final class MatchesWholeOwnText
/*     */     extends Evaluator
/*     */   {
/*     */     private final Pattern pattern;
/*     */ 
/*     */     
/*     */     public MatchesWholeOwnText(Pattern pattern) {
/* 847 */       this.pattern = pattern;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 852 */       Matcher m = this.pattern.matcher(element.wholeOwnText());
/* 853 */       return m.find();
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 858 */       return String.format(":matchesWholeOwnText(%s)", new Object[] { this.pattern });
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class MatchText
/*     */     extends Evaluator
/*     */   {
/*     */     public boolean matches(Element root, Element element) {
/* 866 */       if (element instanceof PseudoTextElement) {
/* 867 */         return true;
/*     */       }
/* 869 */       List<TextNode> textNodes = element.textNodes();
/* 870 */       for (TextNode textNode : textNodes) {
/*     */         
/* 872 */         PseudoTextElement pel = new PseudoTextElement(org.jsoup.parser.Tag.valueOf(element.tagName()), element.baseUri(), element.attributes());
/* 873 */         textNode.replaceWith((Node)pel);
/* 874 */         pel.appendChild((Node)textNode);
/*     */       } 
/* 876 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 881 */       return ":matchText";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\select\Evaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */