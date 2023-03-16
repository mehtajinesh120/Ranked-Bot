/*     */ package org.apache.commons.collections4.iterators;
/*     */ 
/*     */ import java.util.Iterator;
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
/*     */ public class TransformIterator<I, O>
/*     */   implements Iterator<O>
/*     */ {
/*     */   private Iterator<? extends I> iterator;
/*     */   private Transformer<? super I, ? extends O> transformer;
/*     */   
/*     */   public TransformIterator() {}
/*     */   
/*     */   public TransformIterator(Iterator<? extends I> iterator) {
/*  54 */     this.iterator = iterator;
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
/*     */   public TransformIterator(Iterator<? extends I> iterator, Transformer<? super I, ? extends O> transformer) {
/*  68 */     this.iterator = iterator;
/*  69 */     this.transformer = transformer;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  74 */     return this.iterator.hasNext();
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
/*     */   public O next() {
/*  86 */     return transform(this.iterator.next());
/*     */   }
/*     */   
/*     */   public void remove() {
/*  90 */     this.iterator.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator<? extends I> getIterator() {
/* 100 */     return this.iterator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setIterator(Iterator<? extends I> iterator) {
/* 110 */     this.iterator = iterator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Transformer<? super I, ? extends O> getTransformer() {
/* 120 */     return this.transformer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransformer(Transformer<? super I, ? extends O> transformer) {
/* 130 */     this.transformer = transformer;
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
/*     */   protected O transform(I source) {
/* 142 */     return (O)this.transformer.transform(source);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\TransformIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */