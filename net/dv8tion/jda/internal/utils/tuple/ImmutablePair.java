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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ImmutablePair<L, R>
/*    */   extends Pair<L, R>
/*    */ {
/*    */   public final L left;
/*    */   public final R right;
/*    */   
/*    */   public static <L, R> ImmutablePair<L, R> of(L left, R right) {
/* 55 */     return new ImmutablePair<>(left, right);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ImmutablePair(L left, R right) {
/* 66 */     this.left = left;
/* 67 */     this.right = right;
/*    */   }
/*    */ 
/*    */   
/*    */   public L getLeft() {
/* 72 */     return this.left;
/*    */   }
/*    */ 
/*    */   
/*    */   public R getRight() {
/* 77 */     return this.right;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\tuple\ImmutablePair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */