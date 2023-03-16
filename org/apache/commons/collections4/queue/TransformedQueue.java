/*     */ package org.apache.commons.collections4.queue;
/*     */ 
/*     */ import java.util.Queue;
/*     */ import org.apache.commons.collections4.Transformer;
/*     */ import org.apache.commons.collections4.collection.TransformedCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransformedQueue<E>
/*     */   extends TransformedCollection<E>
/*     */   implements Queue<E>
/*     */ {
/*     */   private static final long serialVersionUID = -7901091318986132033L;
/*     */   
/*     */   public static <E> TransformedQueue<E> transformingQueue(Queue<E> queue, Transformer<? super E, ? extends E> transformer) {
/*  55 */     return new TransformedQueue<E>(queue, transformer);
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
/*     */   public static <E> TransformedQueue<E> transformedQueue(Queue<E> queue, Transformer<? super E, ? extends E> transformer) {
/*  76 */     TransformedQueue<E> decorated = new TransformedQueue<E>(queue, transformer);
/*  77 */     if (queue.size() > 0) {
/*     */       
/*  79 */       E[] values = (E[])queue.toArray();
/*  80 */       queue.clear();
/*  81 */       for (E value : values) {
/*  82 */         decorated.decorated().add(transformer.transform(value));
/*     */       }
/*     */     } 
/*  85 */     return decorated;
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
/*     */   protected TransformedQueue(Queue<E> queue, Transformer<? super E, ? extends E> transformer) {
/* 100 */     super(queue, transformer);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Queue<E> getQueue() {
/* 109 */     return (Queue<E>)decorated();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean offer(E obj) {
/* 115 */     return getQueue().offer((E)transform(obj));
/*     */   }
/*     */   
/*     */   public E poll() {
/* 119 */     return getQueue().poll();
/*     */   }
/*     */   
/*     */   public E peek() {
/* 123 */     return getQueue().peek();
/*     */   }
/*     */   
/*     */   public E element() {
/* 127 */     return getQueue().element();
/*     */   }
/*     */   
/*     */   public E remove() {
/* 131 */     return getQueue().remove();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\queue\TransformedQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */