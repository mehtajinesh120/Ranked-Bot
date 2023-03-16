/*    */ package org.jsoup.parser;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParseError
/*    */ {
/*    */   private int pos;
/*    */   private String cursorPos;
/*    */   private String errorMsg;
/*    */   
/*    */   ParseError(CharacterReader reader, String errorMsg) {
/* 12 */     this.pos = reader.pos();
/* 13 */     this.cursorPos = reader.cursorPos();
/* 14 */     this.errorMsg = errorMsg;
/*    */   }
/*    */   
/*    */   ParseError(CharacterReader reader, String errorFormat, Object... args) {
/* 18 */     this.pos = reader.pos();
/* 19 */     this.cursorPos = reader.cursorPos();
/* 20 */     this.errorMsg = String.format(errorFormat, args);
/*    */   }
/*    */   
/*    */   ParseError(int pos, String errorMsg) {
/* 24 */     this.pos = pos;
/* 25 */     this.cursorPos = String.valueOf(pos);
/* 26 */     this.errorMsg = errorMsg;
/*    */   }
/*    */   
/*    */   ParseError(int pos, String errorFormat, Object... args) {
/* 30 */     this.pos = pos;
/* 31 */     this.cursorPos = String.valueOf(pos);
/* 32 */     this.errorMsg = String.format(errorFormat, args);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getErrorMessage() {
/* 40 */     return this.errorMsg;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getPosition() {
/* 48 */     return this.pos;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getCursorPos() {
/* 56 */     return this.cursorPos;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 61 */     return "<" + this.cursorPos + ">: " + this.errorMsg;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\ParseError.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */