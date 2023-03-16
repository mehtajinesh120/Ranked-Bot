/*    */ package okhttp3.internal.connection;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import okhttp3.internal.Util;
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
/*    */ public final class RouteException
/*    */   extends RuntimeException
/*    */ {
/*    */   private IOException firstException;
/*    */   private IOException lastException;
/*    */   
/*    */   public RouteException(IOException cause) {
/* 31 */     super(cause);
/* 32 */     this.firstException = cause;
/* 33 */     this.lastException = cause;
/*    */   }
/*    */   
/*    */   public IOException getFirstConnectException() {
/* 37 */     return this.firstException;
/*    */   }
/*    */   
/*    */   public IOException getLastConnectException() {
/* 41 */     return this.lastException;
/*    */   }
/*    */   
/*    */   public void addConnectException(IOException e) {
/* 45 */     Util.addSuppressedIfPossible(this.firstException, e);
/* 46 */     this.lastException = e;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\connection\RouteException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */