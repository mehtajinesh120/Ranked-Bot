/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class WebSocketOutputStream
/*     */   extends BufferedOutputStream
/*     */ {
/*     */   public WebSocketOutputStream(OutputStream out) {
/*  28 */     super(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(String string) throws IOException {
/*  35 */     byte[] bytes = Misc.getBytesUTF8(string);
/*     */     
/*  37 */     write(bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void write(WebSocketFrame frame) throws IOException {
/*  43 */     writeFrame0(frame);
/*  44 */     writeFrame1(frame);
/*  45 */     writeFrameExtendedPayloadLength(frame);
/*     */ 
/*     */     
/*  48 */     byte[] maskingKey = Misc.nextBytes(4);
/*     */ 
/*     */     
/*  51 */     write(maskingKey);
/*     */ 
/*     */     
/*  54 */     writeFramePayload(frame, maskingKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeFrame0(WebSocketFrame frame) throws IOException {
/*  64 */     int b = (frame.getFin() ? 128 : 0) | (frame.getRsv1() ? 64 : 0) | (frame.getRsv2() ? 32 : 0) | (frame.getRsv3() ? 16 : 0) | frame.getOpcode() & 0xF;
/*     */     
/*  66 */     write(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeFrame1(WebSocketFrame frame) throws IOException {
/*  73 */     int b = 128;
/*     */     
/*  75 */     int len = frame.getPayloadLength();
/*     */     
/*  77 */     if (len <= 125) {
/*     */       
/*  79 */       b |= len;
/*     */     }
/*  81 */     else if (len <= 65535) {
/*     */       
/*  83 */       b |= 0x7E;
/*     */     }
/*     */     else {
/*     */       
/*  87 */       b |= 0x7F;
/*     */     } 
/*     */     
/*  90 */     write(b);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writeFrameExtendedPayloadLength(WebSocketFrame frame) throws IOException {
/*     */     byte[] buf;
/*  96 */     int len = frame.getPayloadLength();
/*     */ 
/*     */     
/*  99 */     if (len <= 125) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 104 */     if (len <= 65535) {
/*     */       
/* 106 */       buf = new byte[2];
/*     */       
/* 108 */       buf[1] = (byte)(len & 0xFF);
/* 109 */       buf[0] = (byte)(len >> 8 & 0xFF);
/*     */     } else {
/* 111 */       buf = new byte[8];
/* 112 */       for (int i = 7; i >= 0; i--) {
/* 113 */         buf[i] = (byte)(len & 0xFF);
/* 114 */         len >>>= 8;
/*     */       } 
/*     */     } 
/* 117 */     write(buf);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeFramePayload(WebSocketFrame frame, byte[] maskingKey) throws IOException {
/* 123 */     byte[] payload = frame.getPayload();
/*     */     
/* 125 */     if (payload == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 130 */     byte[] masked = new byte[payload.length];
/*     */     
/* 132 */     for (int i = 0; i < payload.length; i++)
/*     */     {
/*     */       
/* 135 */       masked[i] = (byte)((payload[i] ^ maskingKey[i % 4]) & 0xFF);
/*     */     }
/*     */     
/* 138 */     write(masked);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\WebSocketOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */