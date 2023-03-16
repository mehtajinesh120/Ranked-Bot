/*    */ package okhttp3.internal.http;
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
/*    */ public final class HttpMethod
/*    */ {
/*    */   public static boolean invalidatesCache(String method) {
/* 20 */     return (method.equals("POST") || method
/* 21 */       .equals("PATCH") || method
/* 22 */       .equals("PUT") || method
/* 23 */       .equals("DELETE") || method
/* 24 */       .equals("MOVE"));
/*    */   }
/*    */   
/*    */   public static boolean requiresRequestBody(String method) {
/* 28 */     return (method.equals("POST") || method
/* 29 */       .equals("PUT") || method
/* 30 */       .equals("PATCH") || method
/* 31 */       .equals("PROPPATCH") || method
/* 32 */       .equals("REPORT"));
/*    */   }
/*    */   
/*    */   public static boolean permitsRequestBody(String method) {
/* 36 */     return (!method.equals("GET") && !method.equals("HEAD"));
/*    */   }
/*    */   
/*    */   public static boolean redirectsWithBody(String method) {
/* 40 */     return method.equals("PROPFIND");
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean redirectsToGet(String method) {
/* 45 */     return !method.equals("PROPFIND");
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http\HttpMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */