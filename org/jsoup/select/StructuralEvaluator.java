/*     */ package org.jsoup.select;
/*     */ 
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.nodes.Node;
/*     */ 
/*     */ abstract class StructuralEvaluator
/*     */   extends Evaluator
/*     */ {
/*     */   Evaluator evaluator;
/*     */   
/*     */   static class Root
/*     */     extends Evaluator
/*     */   {
/*     */     public boolean matches(Element root, Element element) {
/*  15 */       return (root == element);
/*     */     }
/*     */   }
/*     */   
/*     */   static class Has extends StructuralEvaluator {
/*     */     final Collector.FirstFinder finder;
/*     */     
/*     */     public Has(Evaluator evaluator) {
/*  23 */       this.evaluator = evaluator;
/*  24 */       this.finder = new Collector.FirstFinder(evaluator);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/*  30 */       for (int i = 0; i < element.childNodeSize(); i++) {
/*  31 */         Node node = element.childNode(i);
/*  32 */         if (node instanceof Element) {
/*  33 */           Element match = this.finder.find(element, (Element)node);
/*  34 */           if (match != null)
/*  35 */             return true; 
/*     */         } 
/*     */       } 
/*  38 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  43 */       return String.format(":has(%s)", new Object[] { this.evaluator });
/*     */     }
/*     */   }
/*     */   
/*     */   static class Not extends StructuralEvaluator {
/*     */     public Not(Evaluator evaluator) {
/*  49 */       this.evaluator = evaluator;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element node) {
/*  54 */       return !this.evaluator.matches(root, node);
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  59 */       return String.format(":not(%s)", new Object[] { this.evaluator });
/*     */     }
/*     */   }
/*     */   
/*     */   static class Parent extends StructuralEvaluator {
/*     */     public Parent(Evaluator evaluator) {
/*  65 */       this.evaluator = evaluator;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/*  70 */       if (root == element) {
/*  71 */         return false;
/*     */       }
/*  73 */       Element parent = element.parent();
/*  74 */       while (parent != null) {
/*  75 */         if (this.evaluator.matches(root, parent))
/*  76 */           return true; 
/*  77 */         if (parent == root)
/*     */           break; 
/*  79 */         parent = parent.parent();
/*     */       } 
/*  81 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  86 */       return String.format("%s ", new Object[] { this.evaluator });
/*     */     }
/*     */   }
/*     */   
/*     */   static class ImmediateParent extends StructuralEvaluator {
/*     */     public ImmediateParent(Evaluator evaluator) {
/*  92 */       this.evaluator = evaluator;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/*  97 */       if (root == element) {
/*  98 */         return false;
/*     */       }
/* 100 */       Element parent = element.parent();
/* 101 */       return (parent != null && this.evaluator.matches(root, parent));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 106 */       return String.format("%s > ", new Object[] { this.evaluator });
/*     */     }
/*     */   }
/*     */   
/*     */   static class PreviousSibling extends StructuralEvaluator {
/*     */     public PreviousSibling(Evaluator evaluator) {
/* 112 */       this.evaluator = evaluator;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 117 */       if (root == element) {
/* 118 */         return false;
/*     */       }
/* 120 */       Element prev = element.previousElementSibling();
/*     */       
/* 122 */       while (prev != null) {
/* 123 */         if (this.evaluator.matches(root, prev)) {
/* 124 */           return true;
/*     */         }
/* 126 */         prev = prev.previousElementSibling();
/*     */       } 
/* 128 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 133 */       return String.format("%s ~ ", new Object[] { this.evaluator });
/*     */     }
/*     */   }
/*     */   
/*     */   static class ImmediatePreviousSibling extends StructuralEvaluator {
/*     */     public ImmediatePreviousSibling(Evaluator evaluator) {
/* 139 */       this.evaluator = evaluator;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element element) {
/* 144 */       if (root == element) {
/* 145 */         return false;
/*     */       }
/* 147 */       Element prev = element.previousElementSibling();
/* 148 */       return (prev != null && this.evaluator.matches(root, prev));
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 153 */       return String.format("%s + ", new Object[] { this.evaluator });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\select\StructuralEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */