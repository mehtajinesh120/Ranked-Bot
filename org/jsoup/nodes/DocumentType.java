/*     */ package org.jsoup.nodes;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.StringUtil;
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
/*     */ public class DocumentType
/*     */   extends LeafNode
/*     */ {
/*     */   public static final String PUBLIC_KEY = "PUBLIC";
/*     */   public static final String SYSTEM_KEY = "SYSTEM";
/*     */   private static final String NAME = "name";
/*     */   private static final String PUB_SYS_KEY = "pubSysKey";
/*     */   private static final String PUBLIC_ID = "publicId";
/*     */   private static final String SYSTEM_ID = "systemId";
/*     */   
/*     */   public DocumentType(String name, String publicId, String systemId) {
/*  29 */     Validate.notNull(name);
/*  30 */     Validate.notNull(publicId);
/*  31 */     Validate.notNull(systemId);
/*  32 */     attr("name", name);
/*  33 */     attr("publicId", publicId);
/*  34 */     attr("systemId", systemId);
/*  35 */     updatePubSyskey();
/*     */   }
/*     */   
/*     */   public void setPubSysKey(String value) {
/*  39 */     if (value != null)
/*  40 */       attr("pubSysKey", value); 
/*     */   }
/*     */   
/*     */   private void updatePubSyskey() {
/*  44 */     if (has("publicId")) {
/*  45 */       attr("pubSysKey", "PUBLIC");
/*  46 */     } else if (has("systemId")) {
/*  47 */       attr("pubSysKey", "SYSTEM");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String name() {
/*  55 */     return attr("name");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String publicId() {
/*  63 */     return attr("publicId");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String systemId() {
/*  71 */     return attr("systemId");
/*     */   }
/*     */ 
/*     */   
/*     */   public String nodeName() {
/*  76 */     return "#doctype";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void outerHtmlHead(Appendable accum, int depth, Document.OutputSettings out) throws IOException {
/*  82 */     if (this.siblingIndex > 0 && out.prettyPrint()) {
/*  83 */       accum.append('\n');
/*     */     }
/*  85 */     if (out.syntax() == Document.OutputSettings.Syntax.html && !has("publicId") && !has("systemId")) {
/*     */       
/*  87 */       accum.append("<!doctype");
/*     */     } else {
/*  89 */       accum.append("<!DOCTYPE");
/*     */     } 
/*  91 */     if (has("name"))
/*  92 */       accum.append(" ").append(attr("name")); 
/*  93 */     if (has("pubSysKey"))
/*  94 */       accum.append(" ").append(attr("pubSysKey")); 
/*  95 */     if (has("publicId"))
/*  96 */       accum.append(" \"").append(attr("publicId")).append('"'); 
/*  97 */     if (has("systemId"))
/*  98 */       accum.append(" \"").append(attr("systemId")).append('"'); 
/*  99 */     accum.append('>');
/*     */   }
/*     */ 
/*     */   
/*     */   void outerHtmlTail(Appendable accum, int depth, Document.OutputSettings out) {}
/*     */ 
/*     */   
/*     */   private boolean has(String attribute) {
/* 107 */     return !StringUtil.isBlank(attr(attribute));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\DocumentType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */