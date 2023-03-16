/*    */ package org.yaml.snakeyaml.tokens;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ public final class AnchorToken
/*    */   extends Token
/*    */ {
/*    */   private final String value;
/*    */   
/*    */   public AnchorToken(String value, Mark startMark, Mark endMark) {
/* 23 */     super(startMark, endMark);
/* 24 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 28 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Token.ID getTokenId() {
/* 33 */     return Token.ID.Anchor;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\tokens\AnchorToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */