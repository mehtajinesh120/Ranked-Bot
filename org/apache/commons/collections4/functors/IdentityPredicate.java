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
/*    */ public final class IdentityPredicate<T>
/*    */   implements Predicate<T>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -89901658494523293L;
/*    */   private final T iValue;
/*    */   
/*    */   public static <T> Predicate<T> identityPredicate(T object) {
/* 46 */     if (object == null) {
/* 47 */       return NullPredicate.nullPredicate();
/*    */     }
/* 49 */     return new IdentityPredicate<T>(object);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public IdentityPredicate(T object) {
/* 60 */     this.iValue = object;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean evaluate(T object) {
/* 71 */     return (this.iValue == object);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T getValue() {
/* 81 */     return this.iValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\IdentityPredicate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */