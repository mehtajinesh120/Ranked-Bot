/*    */ package org.yaml.snakeyaml.comments;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
/*    */ import org.yaml.snakeyaml.events.CommentEvent;
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
/*    */ public class CommentLine
/*    */ {
/*    */   private final Mark startMark;
/*    */   private final Mark endMark;
/*    */   private final String value;
/*    */   private final CommentType commentType;
/*    */   
/*    */   public CommentLine(CommentEvent event) {
/* 30 */     this(event.getStartMark(), event.getEndMark(), event.getValue(), event.getCommentType());
/*    */   }
/*    */   
/*    */   public CommentLine(Mark startMark, Mark endMark, String value, CommentType commentType) {
/* 34 */     this.startMark = startMark;
/* 35 */     this.endMark = endMark;
/* 36 */     this.value = value;
/* 37 */     this.commentType = commentType;
/*    */   }
/*    */   
/*    */   public Mark getEndMark() {
/* 41 */     return this.endMark;
/*    */   }
/*    */   
/*    */   public Mark getStartMark() {
/* 45 */     return this.startMark;
/*    */   }
/*    */   
/*    */   public CommentType getCommentType() {
/* 49 */     return this.commentType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 58 */     return this.value;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 62 */     return "<" + getClass().getName() + " (type=" + getCommentType() + ", value=" + getValue() + ")>";
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\comments\CommentLine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */