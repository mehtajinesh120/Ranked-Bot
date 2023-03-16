/*    */ package org.yaml.snakeyaml.events;
/*    */ 
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class CommentEvent
/*    */   extends Event
/*    */ {
/*    */   private final CommentType type;
/*    */   private final String value;
/*    */   
/*    */   public CommentEvent(CommentType type, String value, Mark startMark, Mark endMark) {
/* 28 */     super(startMark, endMark);
/* 29 */     if (type == null) {
/* 30 */       throw new NullPointerException("Event Type must be provided.");
/*    */     }
/* 32 */     this.type = type;
/* 33 */     if (value == null) {
/* 34 */       throw new NullPointerException("Value must be provided.");
/*    */     }
/* 36 */     this.value = value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 48 */     return this.value;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CommentType getCommentType() {
/* 57 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getArguments() {
/* 62 */     return super.getArguments() + "type=" + this.type + ", value=" + this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public Event.ID getEventId() {
/* 67 */     return Event.ID.Comment;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\events\CommentEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */