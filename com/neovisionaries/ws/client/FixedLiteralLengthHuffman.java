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
/*    */ class FixedLiteralLengthHuffman
/*    */   extends Huffman
/*    */ {
/* 21 */   private static final FixedLiteralLengthHuffman INSTANCE = new FixedLiteralLengthHuffman();
/*    */ 
/*    */ 
/*    */   
/*    */   private FixedLiteralLengthHuffman() {
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static int[] buildCodeLensFromSym() {
/* 41 */     int[] codeLengths = new int[288];
/*    */ 
/*    */     
/*    */     int symbol;
/*    */     
/* 46 */     for (symbol = 0; symbol < 144; symbol++)
/*    */     {
/* 48 */       codeLengths[symbol] = 8;
/*    */     }
/*    */ 
/*    */     
/* 52 */     for (; symbol < 256; symbol++)
/*    */     {
/* 54 */       codeLengths[symbol] = 9;
/*    */     }
/*    */ 
/*    */     
/* 58 */     for (; symbol < 280; symbol++)
/*    */     {
/* 60 */       codeLengths[symbol] = 7;
/*    */     }
/*    */ 
/*    */     
/* 64 */     for (; symbol < 288; symbol++)
/*    */     {
/* 66 */       codeLengths[symbol] = 8;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 72 */     return codeLengths;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static FixedLiteralLengthHuffman getInstance() {
/* 78 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\FixedLiteralLengthHuffman.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */