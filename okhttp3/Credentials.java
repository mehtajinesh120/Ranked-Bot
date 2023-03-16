/*    */ package okhttp3;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import okio.ByteString;
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
/*    */ public final class Credentials
/*    */ {
/*    */   public static String basic(String username, String password) {
/* 30 */     return basic(username, password, StandardCharsets.ISO_8859_1);
/*    */   }
/*    */   
/*    */   public static String basic(String username, String password, Charset charset) {
/* 34 */     String usernameAndPassword = username + ":" + password;
/* 35 */     String encoded = ByteString.encodeString(usernameAndPassword, charset).base64();
/* 36 */     return "Basic " + encoded;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Credentials.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */