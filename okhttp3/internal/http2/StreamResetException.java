/*    */ package okhttp3.internal.http2;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ public final class StreamResetException
/*    */   extends IOException
/*    */ {
/*    */   public final ErrorCode errorCode;
/*    */   
/*    */   public StreamResetException(ErrorCode errorCode) {
/* 25 */     super("stream was reset: " + errorCode);
/* 26 */     this.errorCode = errorCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http2\StreamResetException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */