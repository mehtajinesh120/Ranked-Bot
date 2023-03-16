/*     */ package org.jsoup.select;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.IdentityHashMap;
/*     */ import javax.annotation.Nullable;
/*     */ import org.jsoup.helper.Validate;
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
/*     */ public class Selector
/*     */ {
/*     */   public static Elements select(String query, Element root) {
/*  97 */     Validate.notEmpty(query);
/*  98 */     return select(QueryParser.parse(query), root);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Elements select(Evaluator evaluator, Element root) {
/* 109 */     Validate.notNull(evaluator);
/* 110 */     Validate.notNull(root);
/* 111 */     return Collector.collect(evaluator, root);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Elements select(String query, Iterable<Element> roots) {
/* 122 */     Validate.notEmpty(query);
/* 123 */     Validate.notNull(roots);
/* 124 */     Evaluator evaluator = QueryParser.parse(query);
/* 125 */     Elements elements = new Elements();
/* 126 */     IdentityHashMap<Element, Boolean> seenElements = new IdentityHashMap<>();
/*     */ 
/*     */     
/* 129 */     for (Element root : roots) {
/* 130 */       Elements found = select(evaluator, root);
/* 131 */       for (Element el : found) {
/* 132 */         if (seenElements.put(el, Boolean.TRUE) == null) {
/* 133 */           elements.add(el);
/*     */         }
/*     */       } 
/*     */     } 
/* 137 */     return elements;
/*     */   }
/*     */ 
/*     */   
/*     */   static Elements filterOut(Collection<Element> elements, Collection<Element> outs) {
/* 142 */     Elements output = new Elements();
/* 143 */     for (Element el : elements) {
/* 144 */       boolean found = false;
/* 145 */       for (Element out : outs) {
/* 146 */         if (el.equals(out)) {
/* 147 */           found = true;
/*     */           break;
/*     */         } 
/*     */       } 
/* 151 */       if (!found)
/* 152 */         output.add(el); 
/*     */     } 
/* 154 */     return output;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public static Element selectFirst(String cssQuery, Element root) {
/* 164 */     Validate.notEmpty(cssQuery);
/* 165 */     return Collector.findFirst(QueryParser.parse(cssQuery), root);
/*     */   }
/*     */   
/*     */   public static class SelectorParseException extends IllegalStateException {
/*     */     public SelectorParseException(String msg) {
/* 170 */       super(msg);
/*     */     }
/*     */     
/*     */     public SelectorParseException(String msg, Object... params) {
/* 174 */       super(String.format(msg, params));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\select\Selector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */