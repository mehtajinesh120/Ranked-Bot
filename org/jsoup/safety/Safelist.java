/*     */ package org.jsoup.safety;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.Normalizer;
/*     */ import org.jsoup.nodes.Attribute;
/*     */ import org.jsoup.nodes.Attributes;
/*     */ import org.jsoup.nodes.Element;
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
/*     */ public class Safelist
/*     */ {
/*     */   private static final String All = ":all";
/*     */   private final Set<TagName> tagNames;
/*     */   private final Map<TagName, Set<AttributeKey>> attributes;
/*     */   private final Map<TagName, Map<AttributeKey, AttributeValue>> enforcedAttributes;
/*     */   private final Map<TagName, Map<AttributeKey, Set<Protocol>>> protocols;
/*     */   private boolean preserveRelativeLinks;
/*     */   
/*     */   public static Safelist none() {
/*  96 */     return new Safelist();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Safelist simpleText() {
/* 106 */     return (new Safelist())
/* 107 */       .addTags(new String[] { "b", "em", "i", "strong", "u" });
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
/*     */   public static Safelist basic() {
/* 127 */     return (new Safelist())
/* 128 */       .addTags(new String[] { 
/*     */           "a", "b", "blockquote", "br", "cite", "code", "dd", "dl", "dt", "em",
/*     */           
/*     */           "i", "li", "ol", "p", "pre", "q", "small", "span", "strike", "strong", 
/*     */           "sub", "sup", "u", "ul"
/* 133 */         }).addAttributes("a", new String[] { "href"
/* 134 */         }).addAttributes("blockquote", new String[] { "cite"
/* 135 */         }).addAttributes("q", new String[] { "cite"
/*     */         
/* 137 */         }).addProtocols("a", "href", new String[] { "ftp", "http", "https", "mailto"
/* 138 */         }).addProtocols("blockquote", "cite", new String[] { "http", "https"
/* 139 */         }).addProtocols("cite", "cite", new String[] { "http", "https"
/*     */         
/* 141 */         }).addEnforcedAttribute("a", "rel", "nofollow");
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
/*     */   public static Safelist basicWithImages() {
/* 153 */     return basic()
/* 154 */       .addTags(new String[] { "img"
/* 155 */         }).addAttributes("img", new String[] { "align", "alt", "height", "src", "title", "width"
/* 156 */         }).addProtocols("img", "src", new String[] { "http", "https" });
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
/*     */   public static Safelist relaxed() {
/* 171 */     return (new Safelist())
/* 172 */       .addTags(new String[] { 
/*     */           "a", "b", "blockquote", "br", "caption", "cite", "code", "col", "colgroup", "dd",
/*     */           
/*     */           "div", "dl", "dt", "em", "h1", "h2", "h3", "h4", "h5", "h6", 
/*     */           "i", "img", "li", "ol", "p", "pre", "q", "small", "span", "strike", 
/*     */           "strong", "sub", "sup", "table", "tbody", "td", "tfoot", "th", "thead", "tr", 
/*     */           "u", "ul"
/* 179 */         }).addAttributes("a", new String[] { "href", "title"
/* 180 */         }).addAttributes("blockquote", new String[] { "cite"
/* 181 */         }).addAttributes("col", new String[] { "span", "width"
/* 182 */         }).addAttributes("colgroup", new String[] { "span", "width"
/* 183 */         }).addAttributes("img", new String[] { "align", "alt", "height", "src", "title", "width"
/* 184 */         }).addAttributes("ol", new String[] { "start", "type"
/* 185 */         }).addAttributes("q", new String[] { "cite"
/* 186 */         }).addAttributes("table", new String[] { "summary", "width"
/* 187 */         }).addAttributes("td", new String[] { "abbr", "axis", "colspan", "rowspan", "width"
/* 188 */         }).addAttributes("th", new String[] {
/*     */           
/*     */           "abbr", "axis", "colspan", "rowspan", "scope", "width"
/* 191 */         }).addAttributes("ul", new String[] { "type"
/*     */         
/* 193 */         }).addProtocols("a", "href", new String[] { "ftp", "http", "https", "mailto"
/* 194 */         }).addProtocols("blockquote", "cite", new String[] { "http", "https"
/* 195 */         }).addProtocols("cite", "cite", new String[] { "http", "https"
/* 196 */         }).addProtocols("img", "src", new String[] { "http", "https"
/* 197 */         }).addProtocols("q", "cite", new String[] { "http", "https" });
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
/*     */   public Safelist() {
/* 210 */     this.tagNames = new HashSet<>();
/* 211 */     this.attributes = new HashMap<>();
/* 212 */     this.enforcedAttributes = new HashMap<>();
/* 213 */     this.protocols = new HashMap<>();
/* 214 */     this.preserveRelativeLinks = false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Safelist(Safelist copy) {
/* 222 */     this();
/* 223 */     this.tagNames.addAll(copy.tagNames);
/* 224 */     for (Map.Entry<TagName, Set<AttributeKey>> copyTagAttributes : copy.attributes.entrySet()) {
/* 225 */       this.attributes.put(copyTagAttributes.getKey(), new HashSet<>(copyTagAttributes.getValue()));
/*     */     }
/* 227 */     for (Map.Entry<TagName, Map<AttributeKey, AttributeValue>> enforcedEntry : copy.enforcedAttributes.entrySet()) {
/* 228 */       this.enforcedAttributes.put(enforcedEntry.getKey(), new HashMap<>(enforcedEntry.getValue()));
/*     */     }
/* 230 */     for (Map.Entry<TagName, Map<AttributeKey, Set<Protocol>>> protocolsEntry : copy.protocols.entrySet()) {
/* 231 */       Map<AttributeKey, Set<Protocol>> attributeProtocolsCopy = new HashMap<>();
/* 232 */       for (Map.Entry<AttributeKey, Set<Protocol>> attributeProtocols : (Iterable<Map.Entry<AttributeKey, Set<Protocol>>>)((Map)protocolsEntry.getValue()).entrySet()) {
/* 233 */         attributeProtocolsCopy.put(attributeProtocols.getKey(), new HashSet<>(attributeProtocols.getValue()));
/*     */       }
/* 235 */       this.protocols.put(protocolsEntry.getKey(), attributeProtocolsCopy);
/*     */     } 
/* 237 */     this.preserveRelativeLinks = copy.preserveRelativeLinks;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Safelist addTags(String... tags) {
/* 247 */     Validate.notNull(tags);
/*     */     
/* 249 */     for (String tagName : tags) {
/* 250 */       Validate.notEmpty(tagName);
/* 251 */       this.tagNames.add(TagName.valueOf(tagName));
/*     */     } 
/* 253 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Safelist removeTags(String... tags) {
/* 263 */     Validate.notNull(tags);
/*     */     
/* 265 */     for (String tag : tags) {
/* 266 */       Validate.notEmpty(tag);
/* 267 */       TagName tagName = TagName.valueOf(tag);
/*     */       
/* 269 */       if (this.tagNames.remove(tagName)) {
/* 270 */         this.attributes.remove(tagName);
/* 271 */         this.enforcedAttributes.remove(tagName);
/* 272 */         this.protocols.remove(tagName);
/*     */       } 
/*     */     } 
/* 275 */     return this;
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
/*     */   public Safelist addAttributes(String tag, String... attributes) {
/* 294 */     Validate.notEmpty(tag);
/* 295 */     Validate.notNull(attributes);
/* 296 */     Validate.isTrue((attributes.length > 0), "No attribute names supplied.");
/*     */     
/* 298 */     TagName tagName = TagName.valueOf(tag);
/* 299 */     this.tagNames.add(tagName);
/* 300 */     Set<AttributeKey> attributeSet = new HashSet<>();
/* 301 */     for (String key : attributes) {
/* 302 */       Validate.notEmpty(key);
/* 303 */       attributeSet.add(AttributeKey.valueOf(key));
/*     */     } 
/* 305 */     if (this.attributes.containsKey(tagName)) {
/* 306 */       Set<AttributeKey> currentSet = this.attributes.get(tagName);
/* 307 */       currentSet.addAll(attributeSet);
/*     */     } else {
/* 309 */       this.attributes.put(tagName, attributeSet);
/*     */     } 
/* 311 */     return this;
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
/*     */   public Safelist removeAttributes(String tag, String... attributes) {
/* 330 */     Validate.notEmpty(tag);
/* 331 */     Validate.notNull(attributes);
/* 332 */     Validate.isTrue((attributes.length > 0), "No attribute names supplied.");
/*     */     
/* 334 */     TagName tagName = TagName.valueOf(tag);
/* 335 */     Set<AttributeKey> attributeSet = new HashSet<>();
/* 336 */     for (String key : attributes) {
/* 337 */       Validate.notEmpty(key);
/* 338 */       attributeSet.add(AttributeKey.valueOf(key));
/*     */     } 
/* 340 */     if (this.tagNames.contains(tagName) && this.attributes.containsKey(tagName)) {
/* 341 */       Set<AttributeKey> currentSet = this.attributes.get(tagName);
/* 342 */       currentSet.removeAll(attributeSet);
/*     */       
/* 344 */       if (currentSet.isEmpty())
/* 345 */         this.attributes.remove(tagName); 
/*     */     } 
/* 347 */     if (tag.equals(":all")) {
/* 348 */       Iterator<Map.Entry<TagName, Set<AttributeKey>>> it = this.attributes.entrySet().iterator();
/* 349 */       while (it.hasNext()) {
/* 350 */         Map.Entry<TagName, Set<AttributeKey>> entry = it.next();
/* 351 */         Set<AttributeKey> currentSet = entry.getValue();
/* 352 */         currentSet.removeAll(attributeSet);
/* 353 */         if (currentSet.isEmpty())
/* 354 */           it.remove(); 
/*     */       } 
/*     */     } 
/* 357 */     return this;
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
/*     */   public Safelist addEnforcedAttribute(String tag, String attribute, String value) {
/* 374 */     Validate.notEmpty(tag);
/* 375 */     Validate.notEmpty(attribute);
/* 376 */     Validate.notEmpty(value);
/*     */     
/* 378 */     TagName tagName = TagName.valueOf(tag);
/* 379 */     this.tagNames.add(tagName);
/* 380 */     AttributeKey attrKey = AttributeKey.valueOf(attribute);
/* 381 */     AttributeValue attrVal = AttributeValue.valueOf(value);
/*     */     
/* 383 */     if (this.enforcedAttributes.containsKey(tagName)) {
/* 384 */       ((Map<AttributeKey, AttributeValue>)this.enforcedAttributes.get(tagName)).put(attrKey, attrVal);
/*     */     } else {
/* 386 */       Map<AttributeKey, AttributeValue> attrMap = new HashMap<>();
/* 387 */       attrMap.put(attrKey, attrVal);
/* 388 */       this.enforcedAttributes.put(tagName, attrMap);
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
/*     */   public Safelist removeEnforcedAttribute(String tag, String attribute) {
/* 401 */     Validate.notEmpty(tag);
/* 402 */     Validate.notEmpty(attribute);
/*     */     
/* 404 */     TagName tagName = TagName.valueOf(tag);
/* 405 */     if (this.tagNames.contains(tagName) && this.enforcedAttributes.containsKey(tagName)) {
/* 406 */       AttributeKey attrKey = AttributeKey.valueOf(attribute);
/* 407 */       Map<AttributeKey, AttributeValue> attrMap = this.enforcedAttributes.get(tagName);
/* 408 */       attrMap.remove(attrKey);
/*     */       
/* 410 */       if (attrMap.isEmpty())
/* 411 */         this.enforcedAttributes.remove(tagName); 
/*     */     } 
/* 413 */     return this;
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
/*     */   public Safelist preserveRelativeLinks(boolean preserve) {
/* 432 */     this.preserveRelativeLinks = preserve;
/* 433 */     return this;
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
/*     */   public Safelist addProtocols(String tag, String attribute, String... protocols) {
/*     */     Map<AttributeKey, Set<Protocol>> attrMap;
/*     */     Set<Protocol> protSet;
/* 453 */     Validate.notEmpty(tag);
/* 454 */     Validate.notEmpty(attribute);
/* 455 */     Validate.notNull(protocols);
/*     */     
/* 457 */     TagName tagName = TagName.valueOf(tag);
/* 458 */     AttributeKey attrKey = AttributeKey.valueOf(attribute);
/*     */ 
/*     */ 
/*     */     
/* 462 */     if (this.protocols.containsKey(tagName)) {
/* 463 */       attrMap = this.protocols.get(tagName);
/*     */     } else {
/* 465 */       attrMap = new HashMap<>();
/* 466 */       this.protocols.put(tagName, attrMap);
/*     */     } 
/* 468 */     if (attrMap.containsKey(attrKey)) {
/* 469 */       protSet = attrMap.get(attrKey);
/*     */     } else {
/* 471 */       protSet = new HashSet<>();
/* 472 */       attrMap.put(attrKey, protSet);
/*     */     } 
/* 474 */     for (String protocol : protocols) {
/* 475 */       Validate.notEmpty(protocol);
/* 476 */       Protocol prot = Protocol.valueOf(protocol);
/* 477 */       protSet.add(prot);
/*     */     } 
/* 479 */     return this;
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
/*     */   public Safelist removeProtocols(String tag, String attribute, String... removeProtocols) {
/* 495 */     Validate.notEmpty(tag);
/* 496 */     Validate.notEmpty(attribute);
/* 497 */     Validate.notNull(removeProtocols);
/*     */     
/* 499 */     TagName tagName = TagName.valueOf(tag);
/* 500 */     AttributeKey attr = AttributeKey.valueOf(attribute);
/*     */ 
/*     */ 
/*     */     
/* 504 */     Validate.isTrue(this.protocols.containsKey(tagName), "Cannot remove a protocol that is not set.");
/* 505 */     Map<AttributeKey, Set<Protocol>> tagProtocols = this.protocols.get(tagName);
/* 506 */     Validate.isTrue(tagProtocols.containsKey(attr), "Cannot remove a protocol that is not set.");
/*     */     
/* 508 */     Set<Protocol> attrProtocols = tagProtocols.get(attr);
/* 509 */     for (String protocol : removeProtocols) {
/* 510 */       Validate.notEmpty(protocol);
/* 511 */       attrProtocols.remove(Protocol.valueOf(protocol));
/*     */     } 
/*     */     
/* 514 */     if (attrProtocols.isEmpty()) {
/* 515 */       tagProtocols.remove(attr);
/* 516 */       if (tagProtocols.isEmpty())
/* 517 */         this.protocols.remove(tagName); 
/*     */     } 
/* 519 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSafeTag(String tag) {
/* 528 */     return this.tagNames.contains(TagName.valueOf(tag));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isSafeAttribute(String tagName, Element el, Attribute attr) {
/* 539 */     TagName tag = TagName.valueOf(tagName);
/* 540 */     AttributeKey key = AttributeKey.valueOf(attr.getKey());
/*     */     
/* 542 */     Set<AttributeKey> okSet = this.attributes.get(tag);
/* 543 */     if (okSet != null && okSet.contains(key)) {
/* 544 */       if (this.protocols.containsKey(tag)) {
/* 545 */         Map<AttributeKey, Set<Protocol>> attrProts = this.protocols.get(tag);
/*     */         
/* 547 */         return (!attrProts.containsKey(key) || testValidProtocol(el, attr, attrProts.get(key)));
/*     */       } 
/* 549 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 553 */     Map<AttributeKey, AttributeValue> enforcedSet = this.enforcedAttributes.get(tag);
/* 554 */     if (enforcedSet != null) {
/* 555 */       Attributes expect = getEnforcedAttributes(tagName);
/* 556 */       String attrKey = attr.getKey();
/* 557 */       if (expect.hasKeyIgnoreCase(attrKey)) {
/* 558 */         return expect.getIgnoreCase(attrKey).equals(attr.getValue());
/*     */       }
/*     */     } 
/*     */     
/* 562 */     return (!tagName.equals(":all") && isSafeAttribute(":all", el, attr));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean testValidProtocol(Element el, Attribute attr, Set<Protocol> protocols) {
/* 568 */     String value = el.absUrl(attr.getKey());
/* 569 */     if (value.length() == 0)
/* 570 */       value = attr.getValue(); 
/* 571 */     if (!this.preserveRelativeLinks) {
/* 572 */       attr.setValue(value);
/*     */     }
/* 574 */     for (Protocol protocol : protocols) {
/* 575 */       String prot = protocol.toString();
/*     */       
/* 577 */       if (prot.equals("#")) {
/* 578 */         if (isValidAnchor(value)) {
/* 579 */           return true;
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 585 */       prot = prot + ":";
/*     */       
/* 587 */       if (Normalizer.lowerCase(value).startsWith(prot)) {
/* 588 */         return true;
/*     */       }
/*     */     } 
/* 591 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isValidAnchor(String value) {
/* 595 */     return (value.startsWith("#") && !value.matches(".*\\s.*"));
/*     */   }
/*     */   
/*     */   Attributes getEnforcedAttributes(String tagName) {
/* 599 */     Attributes attrs = new Attributes();
/* 600 */     TagName tag = TagName.valueOf(tagName);
/* 601 */     if (this.enforcedAttributes.containsKey(tag)) {
/* 602 */       Map<AttributeKey, AttributeValue> keyVals = this.enforcedAttributes.get(tag);
/* 603 */       for (Map.Entry<AttributeKey, AttributeValue> entry : keyVals.entrySet()) {
/* 604 */         attrs.put(((AttributeKey)entry.getKey()).toString(), ((AttributeValue)entry.getValue()).toString());
/*     */       }
/*     */     } 
/* 607 */     return attrs;
/*     */   }
/*     */   
/*     */   static class TagName
/*     */     extends TypedValue
/*     */   {
/*     */     TagName(String value) {
/* 614 */       super(value);
/*     */     }
/*     */     
/*     */     static TagName valueOf(String value) {
/* 618 */       return new TagName(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AttributeKey extends TypedValue {
/*     */     AttributeKey(String value) {
/* 624 */       super(value);
/*     */     }
/*     */     
/*     */     static AttributeKey valueOf(String value) {
/* 628 */       return new AttributeKey(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AttributeValue extends TypedValue {
/*     */     AttributeValue(String value) {
/* 634 */       super(value);
/*     */     }
/*     */     
/*     */     static AttributeValue valueOf(String value) {
/* 638 */       return new AttributeValue(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static class Protocol extends TypedValue {
/*     */     Protocol(String value) {
/* 644 */       super(value);
/*     */     }
/*     */     
/*     */     static Protocol valueOf(String value) {
/* 648 */       return new Protocol(value);
/*     */     }
/*     */   }
/*     */   
/*     */   static abstract class TypedValue {
/*     */     private final String value;
/*     */     
/*     */     TypedValue(String value) {
/* 656 */       Validate.notNull(value);
/* 657 */       this.value = value;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 662 */       int prime = 31;
/* 663 */       int result = 1;
/* 664 */       result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
/* 665 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 670 */       if (this == obj) return true; 
/* 671 */       if (obj == null) return false; 
/* 672 */       if (getClass() != obj.getClass()) return false; 
/* 673 */       TypedValue other = (TypedValue)obj;
/* 674 */       if (this.value == null)
/* 675 */         return (other.value == null); 
/* 676 */       return this.value.equals(other.value);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 681 */       return this.value;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\safety\Safelist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */