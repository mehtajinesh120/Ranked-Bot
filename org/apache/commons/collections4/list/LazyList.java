/*     */ package org.apache.commons.collections4.list;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.apache.commons.collections4.Factory;
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
/*     */ public class LazyList<E>
/*     */   extends AbstractSerializableListDecorator<E>
/*     */ {
/*     */   private static final long serialVersionUID = -1708388017160694542L;
/*     */   private final Factory<? extends E> factory;
/*     */   
/*     */   public static <E> LazyList<E> lazyList(List<E> list, Factory<? extends E> factory) {
/*  79 */     return new LazyList<E>(list, factory);
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
/*     */   protected LazyList(List<E> list, Factory<? extends E> factory) {
/*  91 */     super(list);
/*  92 */     if (factory == null) {
/*  93 */       throw new IllegalArgumentException("Factory must not be null");
/*     */     }
/*  95 */     this.factory = factory;
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
/*     */   public E get(int index) {
/* 112 */     int size = decorated().size();
/* 113 */     if (index < size) {
/*     */       
/* 115 */       E e = decorated().get(index);
/* 116 */       if (e == null) {
/*     */         
/* 118 */         e = (E)this.factory.create();
/* 119 */         decorated().set(index, e);
/* 120 */         return e;
/*     */       } 
/*     */       
/* 123 */       return e;
/*     */     } 
/*     */     
/* 126 */     for (int i = size; i < index; i++) {
/* 127 */       decorated().add(null);
/*     */     }
/*     */     
/* 130 */     E object = (E)this.factory.create();
/* 131 */     decorated().add(object);
/* 132 */     return object;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<E> subList(int fromIndex, int toIndex) {
/* 137 */     List<E> sub = decorated().subList(fromIndex, toIndex);
/* 138 */     return new LazyList(sub, this.factory);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\list\LazyList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */