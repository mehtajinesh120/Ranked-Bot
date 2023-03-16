/*    */ package okhttp3.internal.http;
/*    */ 
/*    */ import javax.annotation.Nullable;
/*    */ import okhttp3.MediaType;
/*    */ import okhttp3.ResponseBody;
/*    */ import okio.BufferedSource;
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
/*    */ public final class RealResponseBody
/*    */   extends ResponseBody
/*    */ {
/*    */   @Nullable
/*    */   private final String contentTypeString;
/*    */   private final long contentLength;
/*    */   private final BufferedSource source;
/*    */   
/*    */   public RealResponseBody(@Nullable String contentTypeString, long contentLength, BufferedSource source) {
/* 34 */     this.contentTypeString = contentTypeString;
/* 35 */     this.contentLength = contentLength;
/* 36 */     this.source = source;
/*    */   }
/*    */   
/*    */   public MediaType contentType() {
/* 40 */     return (this.contentTypeString != null) ? MediaType.parse(this.contentTypeString) : null;
/*    */   }
/*    */   
/*    */   public long contentLength() {
/* 44 */     return this.contentLength;
/*    */   }
/*    */   
/*    */   public BufferedSource source() {
/* 48 */     return this.source;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http\RealResponseBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */