/*    */ package org.apache.commons.collections4.bag;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Set;
/*    */ import org.apache.commons.collections4.Bag;
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
/*    */ public abstract class AbstractBagDecorator<E>
/*    */   extends AbstractCollectionDecorator<E>
/*    */   implements Bag<E>
/*    */ {
/*    */   private static final long serialVersionUID = -3768146017343785417L;
/*    */   
/*    */   protected AbstractBagDecorator() {}
/*    */   
/*    */   protected AbstractBagDecorator(Bag<E> bag) {
/* 53 */     super((Collection)bag);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Bag<E> decorated() {
/* 63 */     return (Bag<E>)super.decorated();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 68 */     return (object == this || decorated().equals(object));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 73 */     return decorated().hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCount(Object object) {
/* 80 */     return decorated().getCount(object);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E object, int count) {
/* 85 */     return decorated().add(object, count);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object object, int count) {
/* 90 */     return decorated().remove(object, count);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<E> uniqueSet() {
/* 95 */     return decorated().uniqueSet();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\bag\AbstractBagDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */