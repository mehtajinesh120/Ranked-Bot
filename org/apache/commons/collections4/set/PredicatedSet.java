/*    */ package org.apache.commons.collections4.set;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Set;
/*    */ import org.apache.commons.collections4.Predicate;
/*    */ import org.apache.commons.collections4.collection.PredicatedCollection;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PredicatedSet<E>
/*    */   extends PredicatedCollection<E>
/*    */   implements Set<E>
/*    */ {
/*    */   private static final long serialVersionUID = -684521469108685117L;
/*    */   
/*    */   public static <E> PredicatedSet<E> predicatedSet(Set<E> set, Predicate<? super E> predicate) {
/* 60 */     return new PredicatedSet<E>(set, predicate);
/*    */   }
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
/*    */   protected PredicatedSet(Set<E> set, Predicate<? super E> predicate) {
/* 76 */     super(set, predicate);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Set<E> decorated() {
/* 86 */     return (Set<E>)super.decorated();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object object) {
/* 91 */     return (object == this || decorated().equals(object));
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 96 */     return decorated().hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\set\PredicatedSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */