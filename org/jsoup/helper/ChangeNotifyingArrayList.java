/*    */ package org.jsoup.helper;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ public abstract class ChangeNotifyingArrayList<E>
/*    */   extends ArrayList<E>
/*    */ {
/*    */   public ChangeNotifyingArrayList(int initialCapacity) {
/* 11 */     super(initialCapacity);
/*    */   }
/*    */ 
/*    */   
/*    */   public abstract void onContentsChanged();
/*    */   
/*    */   public E set(int index, E element) {
/* 18 */     onContentsChanged();
/* 19 */     return super.set(index, element);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E e) {
/* 24 */     onContentsChanged();
/* 25 */     return super.add(e);
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(int index, E element) {
/* 30 */     onContentsChanged();
/* 31 */     super.add(index, element);
/*    */   }
/*    */ 
/*    */   
/*    */   public E remove(int index) {
/* 36 */     onContentsChanged();
/* 37 */     return super.remove(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object o) {
/* 42 */     onContentsChanged();
/* 43 */     return super.remove(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 48 */     onContentsChanged();
/* 49 */     super.clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addAll(Collection<? extends E> c) {
/* 54 */     onContentsChanged();
/* 55 */     return super.addAll(c);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addAll(int index, Collection<? extends E> c) {
/* 60 */     onContentsChanged();
/* 61 */     return super.addAll(index, c);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void removeRange(int fromIndex, int toIndex) {
/* 66 */     onContentsChanged();
/* 67 */     super.removeRange(fromIndex, toIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeAll(Collection<?> c) {
/* 72 */     onContentsChanged();
/* 73 */     return super.removeAll(c);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean retainAll(Collection<?> c) {
/* 78 */     onContentsChanged();
/* 79 */     return super.retainAll(c);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\helper\ChangeNotifyingArrayList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */