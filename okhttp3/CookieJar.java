/*    */ package okhttp3;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public interface CookieJar
/*    */ {
/* 36 */   public static final CookieJar NO_COOKIES = new CookieJar()
/*    */     {
/*    */       public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {}
/*    */       
/*    */       public List<Cookie> loadForRequest(HttpUrl url) {
/* 41 */         return Collections.emptyList();
/*    */       }
/*    */     };
/*    */   
/*    */   void saveFromResponse(HttpUrl paramHttpUrl, List<Cookie> paramList);
/*    */   
/*    */   List<Cookie> loadForRequest(HttpUrl paramHttpUrl);
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\CookieJar.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */