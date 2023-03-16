/*    */ package org.apache.commons.collections4.queue;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Queue;
/*    */ import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractQueueDecorator<E>
/*    */   extends AbstractCollectionDecorator<E>
/*    */   implements Queue<E>
/*    */ {
/*    */   private static final long serialVersionUID = -2629815475789577029L;
/*    */   
/*    */   protected AbstractQueueDecorator() {}
/*    */   
/*    */   protected AbstractQueueDecorator(Queue<E> queue) {
/* 58 */     super(queue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Queue<E> decorated() {
/* 68 */     return (Queue<E>)super.decorated();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean offer(E obj) {
/* 74 */     return decorated().offer(obj);
/*    */   }
/*    */   
/*    */   public E poll() {
/* 78 */     return decorated().poll();
/*    */   }
/*    */   
/*    */   public E peek() {
/* 82 */     return decorated().peek();
/*    */   }
/*    */   
/*    */   public E element() {
/* 86 */     return decorated().element();
/*    */   }
/*    */   
/*    */   public E remove() {
/* 90 */     return decorated().remove();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\queue\AbstractQueueDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */