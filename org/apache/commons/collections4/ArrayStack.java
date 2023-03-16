/*     */ package org.apache.commons.collections4;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.EmptyStackException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class ArrayStack<E>
/*     */   extends ArrayList<E>
/*     */ {
/*     */   private static final long serialVersionUID = 2130079159931574599L;
/*     */   
/*     */   public ArrayStack() {}
/*     */   
/*     */   public ArrayStack(int initialSize) {
/*  66 */     super(initialSize);
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
/*     */   public boolean empty() {
/*  78 */     return isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E peek() throws EmptyStackException {
/*  88 */     int n = size();
/*  89 */     if (n <= 0) {
/*  90 */       throw new EmptyStackException();
/*     */     }
/*  92 */     return get(n - 1);
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
/*     */   public E peek(int n) throws EmptyStackException {
/* 106 */     int m = size() - n - 1;
/* 107 */     if (m < 0) {
/* 108 */       throw new EmptyStackException();
/*     */     }
/* 110 */     return get(m);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public E pop() throws EmptyStackException {
/* 121 */     int n = size();
/* 122 */     if (n <= 0) {
/* 123 */       throw new EmptyStackException();
/*     */     }
/* 125 */     return remove(n - 1);
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
/*     */   public E push(E item) {
/* 137 */     add(item);
/* 138 */     return item;
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
/*     */   public int search(Object object) {
/* 153 */     int i = size() - 1;
/* 154 */     int n = 1;
/* 155 */     while (i >= 0) {
/* 156 */       Object current = get(i);
/* 157 */       if ((object == null && current == null) || (object != null && object.equals(current)))
/*     */       {
/* 159 */         return n;
/*     */       }
/* 161 */       i--;
/* 162 */       n++;
/*     */     } 
/* 164 */     return -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\ArrayStack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */