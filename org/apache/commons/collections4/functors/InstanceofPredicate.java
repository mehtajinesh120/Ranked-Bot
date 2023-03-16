/*    */ package org.apache.commons.collections4.functors;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import org.apache.commons.collections4.Predicate;
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
/*    */ public final class InstanceofPredicate
/*    */   implements Predicate<Object>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -6682656911025165584L;
/*    */   private final Class<?> iType;
/*    */   
/*    */   public static Predicate<Object> instanceOfPredicate(Class<?> type) {
/* 46 */     if (type == null) {
/* 47 */       throw new NullPointerException("The type to check instanceof must not be null");
/*    */     }
/* 49 */     return new InstanceofPredicate(type);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public InstanceofPredicate(Class<?> type) {
/* 60 */     this.iType = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean evaluate(Object object) {
/* 70 */     return this.iType.isInstance(object);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Class<?> getType() {
/* 80 */     return this.iType;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\InstanceofPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */