/*    */ package net.dv8tion.jda.internal.utils.tuple;
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
/*    */ public class MutableTriple<LEFT, MIDDLE, RIGHT>
/*    */   extends MutablePair<LEFT, RIGHT>
/*    */ {
/*    */   public MIDDLE middle;
/*    */   
/*    */   private MutableTriple(LEFT left, MIDDLE middle, RIGHT right) {
/* 26 */     super(left, right);
/* 27 */     this.middle = middle;
/*    */   }
/*    */ 
/*    */   
/*    */   public static <LEFT, MIDDLE, RIGHT> MutableTriple<LEFT, MIDDLE, RIGHT> of(LEFT left, MIDDLE middle, RIGHT right) {
/* 32 */     return new MutableTriple<>(left, middle, right);
/*    */   }
/*    */ 
/*    */   
/*    */   public MIDDLE getMiddle() {
/* 37 */     return this.middle;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setMiddle(MIDDLE middle) {
/* 42 */     this.middle = middle;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\tuple\MutableTriple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */