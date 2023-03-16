/*     */ package org.apache.commons.collections4.multiset;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.collections4.MultiSet;
/*     */ import org.apache.commons.collections4.collection.AbstractCollectionDecorator;
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
/*     */ public abstract class AbstractMultiSetDecorator<E>
/*     */   extends AbstractCollectionDecorator<E>
/*     */   implements MultiSet<E>
/*     */ {
/*     */   private static final long serialVersionUID = 20150610L;
/*     */   
/*     */   protected AbstractMultiSetDecorator() {}
/*     */   
/*     */   protected AbstractMultiSetDecorator(MultiSet<E> multiset) {
/*  52 */     super((Collection)multiset);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MultiSet<E> decorated() {
/*  62 */     return (MultiSet<E>)super.decorated();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object object) {
/*  67 */     return (object == this || decorated().equals(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  72 */     return decorated().hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCount(Object object) {
/*  79 */     return decorated().getCount(object);
/*     */   }
/*     */ 
/*     */   
/*     */   public int setCount(E object, int count) {
/*  84 */     return decorated().setCount(object, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public int add(E object, int count) {
/*  89 */     return decorated().add(object, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public int remove(Object object, int count) {
/*  94 */     return decorated().remove(object, count);
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<E> uniqueSet() {
/*  99 */     return decorated().uniqueSet();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<MultiSet.Entry<E>> entrySet() {
/* 104 */     return decorated().entrySet();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multiset\AbstractMultiSetDecorator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */