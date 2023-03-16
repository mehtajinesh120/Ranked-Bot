/*     */ package org.jsoup.select;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import javax.annotation.Nullable;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.nodes.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CombiningEvaluator
/*     */   extends Evaluator
/*     */ {
/*     */   final ArrayList<Evaluator> evaluators;
/*  16 */   int num = 0;
/*     */ 
/*     */   
/*     */   CombiningEvaluator() {
/*  20 */     this.evaluators = new ArrayList<>();
/*     */   }
/*     */   
/*     */   CombiningEvaluator(Collection<Evaluator> evaluators) {
/*  24 */     this();
/*  25 */     this.evaluators.addAll(evaluators);
/*  26 */     updateNumEvaluators();
/*     */   }
/*     */   @Nullable
/*     */   Evaluator rightMostEvaluator() {
/*  30 */     return (this.num > 0) ? this.evaluators.get(this.num - 1) : null;
/*     */   }
/*     */   
/*     */   void replaceRightMostEvaluator(Evaluator replacement) {
/*  34 */     this.evaluators.set(this.num - 1, replacement);
/*     */   }
/*     */ 
/*     */   
/*     */   void updateNumEvaluators() {
/*  39 */     this.num = this.evaluators.size();
/*     */   }
/*     */   
/*     */   public static final class And extends CombiningEvaluator {
/*     */     And(Collection<Evaluator> evaluators) {
/*  44 */       super(evaluators);
/*     */     }
/*     */     
/*     */     And(Evaluator... evaluators) {
/*  48 */       this(Arrays.asList(evaluators));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element node) {
/*  53 */       for (int i = this.num - 1; i >= 0; i--) {
/*  54 */         Evaluator s = this.evaluators.get(i);
/*  55 */         if (!s.matches(root, node))
/*  56 */           return false; 
/*     */       } 
/*  58 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  63 */       return StringUtil.join(this.evaluators, "");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Or
/*     */     extends CombiningEvaluator
/*     */   {
/*     */     Or(Collection<Evaluator> evaluators) {
/*  74 */       if (this.num > 1) {
/*  75 */         this.evaluators.add(new CombiningEvaluator.And(evaluators));
/*     */       } else {
/*  77 */         this.evaluators.addAll(evaluators);
/*  78 */       }  updateNumEvaluators();
/*     */     }
/*     */     Or(Evaluator... evaluators) {
/*  81 */       this(Arrays.asList(evaluators));
/*     */     }
/*     */ 
/*     */     
/*     */     Or() {}
/*     */     
/*     */     public void add(Evaluator e) {
/*  88 */       this.evaluators.add(e);
/*  89 */       updateNumEvaluators();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean matches(Element root, Element node) {
/*  94 */       for (int i = 0; i < this.num; i++) {
/*  95 */         Evaluator s = this.evaluators.get(i);
/*  96 */         if (s.matches(root, node))
/*  97 */           return true; 
/*     */       } 
/*  99 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 104 */       return StringUtil.join(this.evaluators, ", ");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\select\CombiningEvaluator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */