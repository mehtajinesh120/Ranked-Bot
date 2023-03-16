/*    */ package org.jsoup.parser;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import org.jsoup.internal.Normalizer;
/*    */ import org.jsoup.nodes.Attributes;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParseSettings
/*    */ {
/* 21 */   public static final ParseSettings htmlDefault = new ParseSettings(false, false);
/* 22 */   public static final ParseSettings preserveCase = new ParseSettings(true, true);
/*    */ 
/*    */   
/*    */   private final boolean preserveTagCase;
/*    */ 
/*    */   
/*    */   private final boolean preserveAttributeCase;
/*    */ 
/*    */   
/*    */   public boolean preserveTagCase() {
/* 32 */     return this.preserveTagCase;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean preserveAttributeCase() {
/* 39 */     return this.preserveAttributeCase;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ParseSettings(boolean tag, boolean attribute) {
/* 48 */     this.preserveTagCase = tag;
/* 49 */     this.preserveAttributeCase = attribute;
/*    */   }
/*    */   
/*    */   ParseSettings(ParseSettings copy) {
/* 53 */     this(copy.preserveTagCase, copy.preserveAttributeCase);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String normalizeTag(String name) {
/* 60 */     name = name.trim();
/* 61 */     if (!this.preserveTagCase)
/* 62 */       name = Normalizer.lowerCase(name); 
/* 63 */     return name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String normalizeAttribute(String name) {
/* 70 */     name = name.trim();
/* 71 */     if (!this.preserveAttributeCase)
/* 72 */       name = Normalizer.lowerCase(name); 
/* 73 */     return name;
/*    */   }
/*    */   @Nullable
/*    */   Attributes normalizeAttributes(@Nullable Attributes attributes) {
/* 77 */     if (attributes != null && !this.preserveAttributeCase) {
/* 78 */       attributes.normalize();
/*    */     }
/* 80 */     return attributes;
/*    */   }
/*    */ 
/*    */   
/*    */   static String normalName(String name) {
/* 85 */     return Normalizer.lowerCase(name.trim());
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\ParseSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */