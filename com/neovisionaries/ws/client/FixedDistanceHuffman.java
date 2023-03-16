/*    */ package com.neovisionaries.ws.client;
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
/*    */ class FixedDistanceHuffman
/*    */   extends Huffman
/*    */ {
/* 21 */   private static final FixedDistanceHuffman INSTANCE = new FixedDistanceHuffman();
/*    */ 
/*    */ 
/*    */   
/*    */   private FixedDistanceHuffman() {
/* 26 */     super(buildCodeLensFromSym());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int[] buildCodeLensFromSym() {
/* 37 */     int[] codeLengths = new int[32];
/*    */     
/* 39 */     for (int symbol = 0; symbol < 32; symbol++)
/*    */     {
/* 41 */       codeLengths[symbol] = 5;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 47 */     return codeLengths;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static FixedDistanceHuffman getInstance() {
/* 53 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\FixedDistanceHuffman.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */