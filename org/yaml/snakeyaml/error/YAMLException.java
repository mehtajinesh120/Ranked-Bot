/*    */ package org.yaml.snakeyaml.error;
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
/*    */ public class YAMLException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -4738336175050337570L;
/*    */   
/*    */   public YAMLException(String message) {
/* 21 */     super(message);
/*    */   }
/*    */   
/*    */   public YAMLException(Throwable cause) {
/* 25 */     super(cause);
/*    */   }
/*    */   
/*    */   public YAMLException(String message, Throwable cause) {
/* 29 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\error\YAMLException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */