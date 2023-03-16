/*    */ package org.yaml.snakeyaml.util;
/*    */ 
/*    */ import java.util.ArrayList;
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
/*    */ public class ArrayStack<T>
/*    */ {
/*    */   private final ArrayList<T> stack;
/*    */   
/*    */   public ArrayStack(int initSize) {
/* 23 */     this.stack = new ArrayList<>(initSize);
/*    */   }
/*    */   
/*    */   public void push(T obj) {
/* 27 */     this.stack.add(obj);
/*    */   }
/*    */   
/*    */   public T pop() {
/* 31 */     return this.stack.remove(this.stack.size() - 1);
/*    */   }
/*    */   
/*    */   public boolean isEmpty() {
/* 35 */     return this.stack.isEmpty();
/*    */   }
/*    */   
/*    */   public void clear() {
/* 39 */     this.stack.clear();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyam\\util\ArrayStack.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */