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
/*    */ public final class BlockMappingStartToken
/*    */   extends Token
/*    */ {
/*    */   public BlockMappingStartToken(Mark startMark, Mark endMark) {
/* 21 */     super(startMark, endMark);
/*    */   }
/*    */ 
/*    */   
/*    */   public Token.ID getTokenId() {
/* 26 */     return Token.ID.BlockMappingStart;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\tokens\BlockMappingStartToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */