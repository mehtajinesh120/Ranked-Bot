/*    */ package org.jsoup;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class UncheckedIOException extends RuntimeException {
/*    */   public UncheckedIOException(IOException cause) {
/*  7 */     super(cause);
/*    */   }
/*    */   
/*    */   public UncheckedIOException(String message) {
/* 11 */     super(new IOException(message));
/*    */   }
/*    */   
/*    */   public IOException ioException() {
/* 15 */     return (IOException)getCause();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\UncheckedIOException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */