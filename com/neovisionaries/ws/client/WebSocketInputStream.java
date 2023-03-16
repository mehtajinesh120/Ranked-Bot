/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ class WebSocketInputStream
/*     */   extends FilterInputStream
/*     */ {
/*     */   public WebSocketInputStream(InputStream in) {
/*  28 */     super(in);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String readLine() throws IOException {
/*  34 */     return Misc.readLine(this, "UTF-8");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocketFrame readFrame() throws IOException, WebSocketException {
/*  41 */     byte[] buffer = new byte[8];
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  46 */       readBytes(buffer, 2);
/*     */     }
/*  48 */     catch (InsufficientDataException e) {
/*     */       
/*  50 */       if (e.getReadByteCount() == 0)
/*     */       {
/*     */ 
/*     */         
/*  54 */         throw new NoMoreFrameException();
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  59 */       throw e;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  64 */     boolean fin = ((buffer[0] & 0x80) != 0);
/*     */ 
/*     */     
/*  67 */     boolean rsv1 = ((buffer[0] & 0x40) != 0);
/*  68 */     boolean rsv2 = ((buffer[0] & 0x20) != 0);
/*  69 */     boolean rsv3 = ((buffer[0] & 0x10) != 0);
/*     */ 
/*     */     
/*  72 */     int opcode = buffer[0] & 0xF;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     boolean mask = ((buffer[1] & 0x80) != 0);
/*     */ 
/*     */     
/*  82 */     long payloadLength = (buffer[1] & Byte.MAX_VALUE);
/*     */     
/*  84 */     if (payloadLength == 126L) {
/*     */ 
/*     */ 
/*     */       
/*  88 */       readBytes(buffer, 2);
/*     */ 
/*     */       
/*  91 */       payloadLength = ((buffer[0] & 0xFF) << 8 | buffer[1] & 0xFF);
/*     */     
/*     */     }
/*  94 */     else if (payloadLength == 127L) {
/*     */ 
/*     */ 
/*     */       
/*  98 */       readBytes(buffer, 8);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 104 */       if ((buffer[0] & 0x80) != 0)
/*     */       {
/*     */         
/* 107 */         throw new WebSocketException(WebSocketError.INVALID_PAYLOAD_LENGTH, "The payload length of a frame is invalid.");
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 113 */       payloadLength = ((buffer[0] & 0xFF) << 56 | (buffer[1] & 0xFF) << 48 | (buffer[2] & 0xFF) << 40 | (buffer[3] & 0xFF) << 32 | (buffer[4] & 0xFF) << 24 | (buffer[5] & 0xFF) << 16 | (buffer[6] & 0xFF) << 8 | buffer[7] & 0xFF);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     byte[] maskingKey = null;
/*     */     
/* 126 */     if (mask) {
/*     */ 
/*     */       
/* 129 */       maskingKey = new byte[4];
/* 130 */       readBytes(maskingKey, 4);
/*     */     } 
/*     */     
/* 133 */     if (2147483647L < payloadLength) {
/*     */ 
/*     */ 
/*     */       
/* 137 */       skipQuietly(payloadLength);
/* 138 */       throw new WebSocketException(WebSocketError.TOO_LONG_PAYLOAD, "The payload length of a frame exceeds the maximum array size in Java.");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 144 */     byte[] payload = readPayload(payloadLength, mask, maskingKey);
/*     */ 
/*     */     
/* 147 */     return (new WebSocketFrame())
/* 148 */       .setFin(fin)
/* 149 */       .setRsv1(rsv1)
/* 150 */       .setRsv2(rsv2)
/* 151 */       .setRsv3(rsv3)
/* 152 */       .setOpcode(opcode)
/* 153 */       .setMask(mask)
/* 154 */       .setPayload(payload);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void readBytes(byte[] buffer, int length) throws IOException, WebSocketException {
/* 161 */     int total = 0;
/*     */     
/* 163 */     while (total < length) {
/*     */       
/* 165 */       int count = read(buffer, total, length - total);
/*     */       
/* 167 */       if (count <= 0)
/*     */       {
/*     */         
/* 170 */         throw new InsufficientDataException(length, total);
/*     */       }
/*     */       
/* 173 */       total += count;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void skipQuietly(long length) {
/*     */     try {
/* 182 */       skip(length);
/*     */     }
/* 184 */     catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private byte[] readPayload(long payloadLength, boolean mask, byte[] maskingKey) throws IOException, WebSocketException {
/*     */     byte[] payload;
/* 192 */     if (payloadLength == 0L)
/*     */     {
/* 194 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 202 */       payload = new byte[(int)payloadLength];
/*     */     }
/* 204 */     catch (OutOfMemoryError e) {
/*     */ 
/*     */ 
/*     */       
/* 208 */       skipQuietly(payloadLength);
/* 209 */       throw new WebSocketException(WebSocketError.INSUFFICIENT_MEMORY_FOR_PAYLOAD, "OutOfMemoryError occurred during a trial to allocate a memory area for a frame's payload: " + e
/*     */           
/* 211 */           .getMessage(), e);
/*     */     } 
/*     */ 
/*     */     
/* 215 */     readBytes(payload, payload.length);
/*     */ 
/*     */     
/* 218 */     if (mask)
/*     */     {
/*     */       
/* 221 */       WebSocketFrame.mask(maskingKey, payload);
/*     */     }
/*     */     
/* 224 */     return payload;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\WebSocketInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */