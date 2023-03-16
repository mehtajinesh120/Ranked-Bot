/*    */ package com.neovisionaries.ws.client;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.util.zip.Deflater;
/*    */ import java.util.zip.DeflaterOutputStream;
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
/*    */ class DeflateCompressor
/*    */ {
/*    */   public static byte[] compress(byte[] input) throws IOException {
/* 34 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/*    */ 
/*    */     
/* 37 */     Deflater deflater = createDeflater();
/* 38 */     DeflaterOutputStream dos = new DeflaterOutputStream(baos, deflater);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 46 */     dos.write(input, 0, input.length);
/* 47 */     dos.close();
/*    */ 
/*    */     
/* 50 */     deflater.end();
/*    */ 
/*    */     
/* 53 */     return baos.toByteArray();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Deflater createDeflater() {
/* 61 */     return new Deflater(-1, true);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\DeflateCompressor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */