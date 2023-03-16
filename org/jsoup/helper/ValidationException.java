/*    */ package org.jsoup.helper;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValidationException
/*    */   extends IllegalArgumentException
/*    */ {
/* 11 */   public static final String Validator = Validate.class.getName();
/*    */   
/*    */   public ValidationException(String msg) {
/* 14 */     super(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public synchronized Throwable fillInStackTrace() {
/* 21 */     super.fillInStackTrace();
/*    */     
/* 23 */     StackTraceElement[] stackTrace = getStackTrace();
/* 24 */     List<StackTraceElement> filteredTrace = new ArrayList<>();
/* 25 */     for (StackTraceElement trace : stackTrace) {
/* 26 */       if (!trace.getClassName().equals(Validator)) {
/* 27 */         filteredTrace.add(trace);
/*    */       }
/*    */     } 
/* 30 */     setStackTrace(filteredTrace.<StackTraceElement>toArray(new StackTraceElement[0]));
/*    */     
/* 32 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\helper\ValidationException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */