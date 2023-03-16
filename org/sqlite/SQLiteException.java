/*    */ package org.sqlite;
/*    */ 
/*    */ import java.sql.SQLException;
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
/*    */ public class SQLiteException
/*    */   extends SQLException
/*    */ {
/*    */   private SQLiteErrorCode resultCode;
/*    */   
/*    */   public SQLiteException(String message, SQLiteErrorCode resultCode) {
/* 33 */     super(message, (String)null, resultCode.code & 0xFF);
/* 34 */     this.resultCode = resultCode;
/*    */   }
/*    */   
/*    */   public SQLiteErrorCode getResultCode() {
/* 38 */     return this.resultCode;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\SQLiteException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */