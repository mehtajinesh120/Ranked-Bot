/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.collections4.Predicate;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SwitchTransformer<I, O>
/*     */   implements Transformer<I, O>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -6404460890903469332L;
/*     */   private final Predicate<? super I>[] iPredicates;
/*     */   private final Transformer<? super I, ? extends O>[] iTransformers;
/*     */   private final Transformer<? super I, ? extends O> iDefault;
/*     */   
/*     */   public static <I, O> Transformer<I, O> switchTransformer(Predicate<? super I>[] predicates, Transformer<? super I, ? extends O>[] transformers, Transformer<? super I, ? extends O> defaultTransformer) {
/*  60 */     FunctorUtils.validate((Predicate<?>[])predicates);
/*  61 */     FunctorUtils.validate((Transformer<?, ?>[])transformers);
/*  62 */     if (predicates.length != transformers.length) {
/*  63 */       throw new IllegalArgumentException("The predicate and transformer arrays must be the same size");
/*     */     }
/*  65 */     if (predicates.length == 0) {
/*  66 */       return (defaultTransformer == null) ? ConstantTransformer.<I, O>nullTransformer() : (Transformer)defaultTransformer;
/*     */     }
/*     */     
/*  69 */     return new SwitchTransformer<I, O>(predicates, transformers, defaultTransformer);
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
/*     */   public static <I, O> Transformer<I, O> switchTransformer(Map<? extends Predicate<? super I>, ? extends Transformer<? super I, ? extends O>> map) {
/*  95 */     if (map == null) {
/*  96 */       throw new NullPointerException("The predicate and transformer map must not be null");
/*     */     }
/*  98 */     if (map.size() == 0) {
/*  99 */       return ConstantTransformer.nullTransformer();
/*     */     }
/*     */     
/* 102 */     Transformer<? super I, ? extends O> defaultTransformer = map.remove(null);
/* 103 */     int size = map.size();
/* 104 */     if (size == 0) {
/* 105 */       return (defaultTransformer == null) ? ConstantTransformer.<I, O>nullTransformer() : (Transformer)defaultTransformer;
/*     */     }
/*     */     
/* 108 */     Transformer[] arrayOfTransformer = new Transformer[size];
/* 109 */     Predicate[] arrayOfPredicate = new Predicate[size];
/* 110 */     int i = 0;
/*     */     
/* 112 */     for (Map.Entry<? extends Predicate<? super I>, ? extends Transformer<? super I, ? extends O>> entry : map.entrySet()) {
/* 113 */       arrayOfPredicate[i] = entry.getKey();
/* 114 */       arrayOfTransformer[i] = entry.getValue();
/* 115 */       i++;
/*     */     } 
/* 117 */     return new SwitchTransformer<I, O>(false, (Predicate<? super I>[])arrayOfPredicate, (Transformer<? super I, ? extends O>[])arrayOfTransformer, defaultTransformer);
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
/*     */   private SwitchTransformer(boolean clone, Predicate<? super I>[] predicates, Transformer<? super I, ? extends O>[] transformers, Transformer<? super I, ? extends O> defaultTransformer) {
/* 133 */     this.iPredicates = clone ? (Predicate<? super I>[])FunctorUtils.<I>copy(predicates) : predicates;
/* 134 */     this.iTransformers = clone ? (Transformer<? super I, ? extends O>[])FunctorUtils.<I, O>copy(transformers) : transformers;
/* 135 */     this.iDefault = (defaultTransformer == null) ? ConstantTransformer.<I, O>nullTransformer() : defaultTransformer;
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
/*     */   public SwitchTransformer(Predicate<? super I>[] predicates, Transformer<? super I, ? extends O>[] transformers, Transformer<? super I, ? extends O> defaultTransformer) {
/* 150 */     this(true, predicates, transformers, defaultTransformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public O transform(I input) {
/* 161 */     for (int i = 0; i < this.iPredicates.length; i++) {
/* 162 */       if (this.iPredicates[i].evaluate(input) == true) {
/* 163 */         return (O)this.iTransformers[i].transform(input);
/*     */       }
/*     */     } 
/* 166 */     return (O)this.iDefault.transform(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Predicate<? super I>[] getPredicates() {
/* 176 */     return (Predicate<? super I>[])FunctorUtils.copy(this.iPredicates);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transformer<? super I, ? extends O>[] getTransformers() {
/* 186 */     return (Transformer<? super I, ? extends O>[])FunctorUtils.copy(this.iTransformers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transformer<? super I, ? extends O> getDefaultTransformer() {
/* 196 */     return this.iDefault;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\SwitchTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */