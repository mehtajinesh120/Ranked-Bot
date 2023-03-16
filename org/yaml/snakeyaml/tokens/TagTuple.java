/*    */ package org.yaml.snakeyaml.tokens;
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
/*    */ public final class TagTuple
/*    */ {
/*    */   private final String handle;
/*    */   private final String suffix;
/*    */   
/*    */   public TagTuple(String handle, String suffix) {
/* 22 */     if (suffix == null) {
/* 23 */       throw new NullPointerException("Suffix must be provided.");
/*    */     }
/* 25 */     this.handle = handle;
/* 26 */     this.suffix = suffix;
/*    */   }
/*    */   
/*    */   public String getHandle() {
/* 30 */     return this.handle;
/*    */   }
/*    */   
/*    */   public String getSuffix() {
/* 34 */     return this.suffix;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\tokens\TagTuple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */