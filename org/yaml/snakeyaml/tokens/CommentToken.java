/*    */ package org.yaml.snakeyaml.tokens;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import org.yaml.snakeyaml.comments.CommentType;
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
/*    */ public final class CommentToken
/*    */   extends Token
/*    */ {
/*    */   private final CommentType type;
/*    */   private final String value;
/*    */   
/*    */   public CommentToken(CommentType type, String value, Mark startMark, Mark endMark) {
/* 26 */     super(startMark, endMark);
/* 27 */     Objects.requireNonNull(type);
/* 28 */     this.type = type;
/* 29 */     Objects.requireNonNull(value);
/* 30 */     this.value = value;
/*    */   }
/*    */   
/*    */   public CommentType getCommentType() {
/* 34 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 38 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Token.ID getTokenId() {
/* 43 */     return Token.ID.Comment;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\tokens\CommentToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */