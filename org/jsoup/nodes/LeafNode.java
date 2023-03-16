/*     */ package org.jsoup.nodes;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ abstract class LeafNode extends Node {
/*     */   Object value;
/*     */   
/*     */   protected final boolean hasAttributes() {
/*   9 */     return this.value instanceof Attributes;
/*     */   }
/*     */ 
/*     */   
/*     */   public final Attributes attributes() {
/*  14 */     ensureAttributes();
/*  15 */     return (Attributes)this.value;
/*     */   }
/*     */   
/*     */   private void ensureAttributes() {
/*  19 */     if (!hasAttributes()) {
/*  20 */       Object coreValue = this.value;
/*  21 */       Attributes attributes = new Attributes();
/*  22 */       this.value = attributes;
/*  23 */       if (coreValue != null)
/*  24 */         attributes.put(nodeName(), (String)coreValue); 
/*     */     } 
/*     */   }
/*     */   
/*     */   String coreValue() {
/*  29 */     return attr(nodeName());
/*     */   }
/*     */   
/*     */   void coreValue(String value) {
/*  33 */     attr(nodeName(), value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String attr(String key) {
/*  38 */     if (!hasAttributes()) {
/*  39 */       return nodeName().equals(key) ? (String)this.value : "";
/*     */     }
/*  41 */     return super.attr(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Node attr(String key, String value) {
/*  46 */     if (!hasAttributes() && key.equals(nodeName())) {
/*  47 */       this.value = value;
/*     */     } else {
/*  49 */       ensureAttributes();
/*  50 */       super.attr(key, value);
/*     */     } 
/*  52 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasAttr(String key) {
/*  57 */     ensureAttributes();
/*  58 */     return super.hasAttr(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public Node removeAttr(String key) {
/*  63 */     ensureAttributes();
/*  64 */     return super.removeAttr(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public String absUrl(String key) {
/*  69 */     ensureAttributes();
/*  70 */     return super.absUrl(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public String baseUri() {
/*  75 */     return hasParent() ? parent().baseUri() : "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void doSetBaseUri(String baseUri) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int childNodeSize() {
/*  85 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node empty() {
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected List<Node> ensureChildNodes() {
/*  95 */     return EmptyNodes;
/*     */   }
/*     */ 
/*     */   
/*     */   protected LeafNode doClone(Node parent) {
/* 100 */     LeafNode clone = (LeafNode)super.doClone(parent);
/*     */ 
/*     */     
/* 103 */     if (hasAttributes()) {
/* 104 */       clone.value = ((Attributes)this.value).clone();
/*     */     }
/* 106 */     return clone;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\LeafNode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */