/*    */ package net.dv8tion.jda.internal.utils;
/*    */ 
/*    */ import java.util.Deque;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.LinkedList;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Nonnull;
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
/*    */ public class ClassWalker
/*    */   implements Iterable<Class<?>>
/*    */ {
/*    */   private final Class<?> clazz;
/*    */   private final Class<?> end;
/*    */   
/*    */   private ClassWalker(Class<?> clazz) {
/* 29 */     this(clazz, Object.class);
/*    */   }
/*    */ 
/*    */   
/*    */   private ClassWalker(Class<?> clazz, Class<?> end) {
/* 34 */     this.clazz = clazz;
/* 35 */     this.end = end;
/*    */   }
/*    */ 
/*    */   
/*    */   public static ClassWalker range(Class<?> start, Class<?> end) {
/* 40 */     return new ClassWalker(start, end);
/*    */   }
/*    */ 
/*    */   
/*    */   public static ClassWalker walk(Class<?> start) {
/* 45 */     return new ClassWalker(start);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Iterator<Class<?>> iterator() {
/* 52 */     return new Iterator<Class<?>>()
/*    */       {
/*    */         private final Set<Class<?>> done;
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         private final Deque<Class<?>> work;
/*    */ 
/*    */ 
/*    */ 
/*    */         
/*    */         public boolean hasNext() {
/* 65 */           return !this.work.isEmpty();
/*    */         }
/*    */ 
/*    */ 
/*    */         
/*    */         public Class<?> next() {
/* 71 */           Class<?> current = this.work.removeFirst();
/* 72 */           this.done.add(current);
/* 73 */           for (Class<?> clazz : current.getInterfaces()) {
/*    */             
/* 75 */             if (!this.done.contains(clazz)) {
/* 76 */               this.work.addLast(clazz);
/*    */             }
/*    */           } 
/* 79 */           Class<?> parent = current.getSuperclass();
/* 80 */           if (parent != null && !this.done.contains(parent))
/* 81 */             this.work.addLast(parent); 
/* 82 */           return current;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\ClassWalker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */