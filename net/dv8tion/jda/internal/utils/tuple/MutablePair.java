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
/*    */ public class MutablePair<L, R>
/*    */   extends Pair<L, R>
/*    */ {
/*    */   public L left;
/*    */   public R right;
/*    */   
/*    */   public static <L, R> MutablePair<L, R> of(L left, R right) {
/* 50 */     return new MutablePair<>(left, right);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MutablePair() {}
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MutablePair(L left, R right) {
/* 68 */     this.left = left;
/* 69 */     this.right = right;
/*    */   }
/*    */ 
/*    */   
/*    */   public L getLeft() {
/* 74 */     return this.left;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setLeft(L left) {
/* 83 */     this.left = left;
/*    */   }
/*    */ 
/*    */   
/*    */   public R getRight() {
/* 88 */     return this.right;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setRight(R right) {
/* 97 */     this.right = right;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\tuple\MutablePair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */