/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
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
/*     */ public class ChainedTransformer<T>
/*     */   implements Transformer<T, T>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 3514945074733160196L;
/*     */   private final Transformer<? super T, ? extends T>[] iTransformers;
/*     */   
/*     */   public static <T> Transformer<T, T> chainedTransformer(Transformer<? super T, ? extends T>... transformers) {
/*  51 */     FunctorUtils.validate((Transformer<?, ?>[])transformers);
/*  52 */     if (transformers.length == 0) {
/*  53 */       return NOPTransformer.nopTransformer();
/*     */     }
/*  55 */     return new ChainedTransformer<T>(transformers);
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
/*     */   public static <T> Transformer<T, T> chainedTransformer(Collection<? extends Transformer<? super T, ? extends T>> transformers) {
/*  72 */     if (transformers == null) {
/*  73 */       throw new NullPointerException("Transformer collection must not be null");
/*     */     }
/*  75 */     if (transformers.size() == 0) {
/*  76 */       return NOPTransformer.nopTransformer();
/*     */     }
/*     */     
/*  79 */     Transformer[] arrayOfTransformer = transformers.<Transformer>toArray(new Transformer[transformers.size()]);
/*  80 */     FunctorUtils.validate((Transformer<?, ?>[])arrayOfTransformer);
/*  81 */     return new ChainedTransformer<T>(false, (Transformer<? super T, ? extends T>[])arrayOfTransformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ChainedTransformer(boolean clone, Transformer<? super T, ? extends T>[] transformers) {
/*  92 */     this.iTransformers = clone ? (Transformer<? super T, ? extends T>[])FunctorUtils.<T, T>copy(transformers) : transformers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ChainedTransformer(Transformer<? super T, ? extends T>... transformers) {
/* 102 */     this(true, transformers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T transform(T object) {
/* 112 */     for (Transformer<? super T, ? extends T> iTransformer : this.iTransformers) {
/* 113 */       object = (T)iTransformer.transform(object);
/*     */     }
/* 115 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transformer<? super T, ? extends T>[] getTransformers() {
/* 125 */     return (Transformer<? super T, ? extends T>[])FunctorUtils.copy(this.iTransformers);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\ChainedTransformer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */