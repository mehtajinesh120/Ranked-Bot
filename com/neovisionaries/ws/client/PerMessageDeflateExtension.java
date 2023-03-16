/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class PerMessageDeflateExtension
/*     */   extends PerMessageCompressionExtension
/*     */ {
/*     */   private static final String SERVER_NO_CONTEXT_TAKEOVER = "server_no_context_takeover";
/*     */   private static final String CLIENT_NO_CONTEXT_TAKEOVER = "client_no_context_takeover";
/*     */   private static final String SERVER_MAX_WINDOW_BITS = "server_max_window_bits";
/*     */   private static final String CLIENT_MAX_WINDOW_BITS = "client_max_window_bits";
/*  36 */   private static final byte[] COMPRESSION_TERMINATOR = new byte[] { 0, 0, -1, -1 };
/*     */   
/*     */   private static final int MIN_BITS = 8;
/*     */   
/*     */   private static final int MAX_BITS = 15;
/*     */   private static final int MIN_WINDOW_SIZE = 256;
/*     */   private static final int MAX_WINDOW_SIZE = 32768;
/*     */   private static final int INCOMING_SLIDING_WINDOW_MARGIN = 1024;
/*     */   private boolean mServerNoContextTakeover;
/*     */   private boolean mClientNoContextTakeover;
/*  46 */   private int mServerWindowSize = 32768;
/*  47 */   private int mClientWindowSize = 32768;
/*     */   
/*     */   private int mIncomingSlidingWindowBufferSize;
/*     */   
/*     */   private ByteArray mIncomingSlidingWindow;
/*     */   
/*     */   public PerMessageDeflateExtension() {
/*  54 */     super("permessage-deflate");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PerMessageDeflateExtension(String name) {
/*  60 */     super(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void validate() throws WebSocketException {
/*  68 */     for (Map.Entry<String, String> entry : getParameters().entrySet())
/*     */     {
/*  70 */       validateParameter(entry.getKey(), entry.getValue());
/*     */     }
/*     */     
/*  73 */     this.mIncomingSlidingWindowBufferSize = this.mServerWindowSize + 1024;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isServerNoContextTakeover() {
/*  79 */     return this.mServerNoContextTakeover;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClientNoContextTakeover() {
/*  85 */     return this.mClientNoContextTakeover;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getServerWindowSize() {
/*  91 */     return this.mServerWindowSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getClientWindowSize() {
/*  97 */     return this.mClientWindowSize;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateParameter(String key, String value) throws WebSocketException {
/* 103 */     if ("server_no_context_takeover".equals(key)) {
/*     */       
/* 105 */       this.mServerNoContextTakeover = true;
/*     */     }
/* 107 */     else if ("client_no_context_takeover".equals(key)) {
/*     */       
/* 109 */       this.mClientNoContextTakeover = true;
/*     */     }
/* 111 */     else if ("server_max_window_bits".equals(key)) {
/*     */       
/* 113 */       this.mServerWindowSize = computeWindowSize(key, value);
/*     */     }
/* 115 */     else if ("client_max_window_bits".equals(key)) {
/*     */       
/* 117 */       this.mClientWindowSize = computeWindowSize(key, value);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 122 */       throw new WebSocketException(WebSocketError.PERMESSAGE_DEFLATE_UNSUPPORTED_PARAMETER, "permessage-deflate extension contains an unsupported parameter: " + key);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int computeWindowSize(String key, String value) throws WebSocketException {
/* 131 */     int bits = extractMaxWindowBits(key, value);
/* 132 */     int size = 256;
/*     */     
/* 134 */     for (int i = 8; i < bits; i++)
/*     */     {
/* 136 */       size *= 2;
/*     */     }
/*     */     
/* 139 */     return size;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int extractMaxWindowBits(String key, String value) throws WebSocketException {
/* 145 */     int bits = parseMaxWindowBits(value);
/*     */     
/* 147 */     if (bits < 0) {
/*     */       
/* 149 */       String message = String.format("The value of %s parameter of permessage-deflate extension is invalid: %s", new Object[] { key, value });
/*     */ 
/*     */ 
/*     */       
/* 153 */       throw new WebSocketException(WebSocketError.PERMESSAGE_DEFLATE_INVALID_MAX_WINDOW_BITS, message);
/*     */     } 
/*     */ 
/*     */     
/* 157 */     return bits;
/*     */   }
/*     */ 
/*     */   
/*     */   private int parseMaxWindowBits(String value) {
/*     */     int bits;
/* 163 */     if (value == null)
/*     */     {
/* 165 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 172 */       bits = Integer.parseInt(value);
/*     */     }
/* 174 */     catch (NumberFormatException e) {
/*     */       
/* 176 */       return -1;
/*     */     } 
/*     */     
/* 179 */     if (bits < 8 || 15 < bits)
/*     */     {
/* 181 */       return -1;
/*     */     }
/*     */     
/* 184 */     return bits;
/*     */   }
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
/*     */   protected byte[] decompress(byte[] compressed) throws WebSocketException {
/* 216 */     int inputLen = compressed.length + COMPRESSION_TERMINATOR.length;
/*     */ 
/*     */     
/* 219 */     ByteArray input = new ByteArray(inputLen);
/* 220 */     input.put(compressed);
/* 221 */     input.put(COMPRESSION_TERMINATOR);
/*     */     
/* 223 */     if (this.mIncomingSlidingWindow == null)
/*     */     {
/* 225 */       this.mIncomingSlidingWindow = new ByteArray(this.mIncomingSlidingWindowBufferSize);
/*     */     }
/*     */ 
/*     */     
/* 229 */     int outPos = this.mIncomingSlidingWindow.length();
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 234 */       DeflateDecompressor.decompress(input, this.mIncomingSlidingWindow);
/*     */     }
/* 236 */     catch (Exception e) {
/*     */ 
/*     */       
/* 239 */       throw new WebSocketException(WebSocketError.DECOMPRESSION_ERROR, 
/*     */           
/* 241 */           String.format("Failed to decompress the message: %s", new Object[] { e.getMessage() }), e);
/*     */     } 
/*     */     
/* 244 */     byte[] output = this.mIncomingSlidingWindow.toBytes(outPos);
/*     */ 
/*     */     
/* 247 */     this.mIncomingSlidingWindow.shrink(this.mIncomingSlidingWindowBufferSize);
/*     */     
/* 249 */     if (this.mServerNoContextTakeover)
/*     */     {
/*     */       
/* 252 */       this.mIncomingSlidingWindow.clear();
/*     */     }
/*     */     
/* 255 */     return output;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] compress(byte[] plain) throws WebSocketException {
/* 262 */     if (!canCompress(plain))
/*     */     {
/*     */       
/* 265 */       return plain;
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
/*     */     try {
/* 287 */       byte[] compressed = DeflateCompressor.compress(plain);
/*     */ 
/*     */       
/* 290 */       return adjustCompressedData(compressed);
/*     */     }
/* 292 */     catch (Exception e) {
/*     */ 
/*     */       
/* 295 */       throw new WebSocketException(WebSocketError.COMPRESSION_ERROR, 
/*     */           
/* 297 */           String.format("Failed to compress the message: %s", new Object[] { e.getMessage() }), e);
/*     */     } 
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean canCompress(byte[] plain) {
/* 324 */     if (this.mClientWindowSize == 32768)
/*     */     {
/*     */       
/* 327 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 336 */     if (plain.length < this.mClientWindowSize)
/*     */     {
/*     */       
/* 339 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 345 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] adjustCompressedData(byte[] compressed) throws FormatException {
/* 353 */     ByteArray data = new ByteArray(compressed.length + 1);
/* 354 */     data.put(compressed);
/*     */ 
/*     */     
/* 357 */     int[] bitIndex = new int[1];
/*     */ 
/*     */ 
/*     */     
/* 361 */     boolean[] hasEmptyBlock = new boolean[1];
/*     */ 
/*     */ 
/*     */     
/* 365 */     while (skipBlock(data, bitIndex, hasEmptyBlock));
/*     */ 
/*     */     
/* 368 */     if (hasEmptyBlock[0])
/*     */     {
/*     */ 
/*     */       
/* 372 */       return data.toBytes(0, (bitIndex[0] - 1) / 8 + 1 - 4);
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
/*     */     
/* 384 */     appendEmptyBlock(data, bitIndex);
/*     */ 
/*     */     
/* 387 */     return data.toBytes(0, (bitIndex[0] - 1) / 8 + 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendEmptyBlock(ByteArray data, int[] bitIndex) {
/* 393 */     int shift = bitIndex[0] % 8;
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
/* 408 */     switch (shift) {
/*     */       
/*     */       case 0:
/*     */       case 6:
/*     */       case 7:
/* 413 */         data.put(0);
/*     */         break;
/*     */     } 
/*     */     
/* 417 */     bitIndex[0] = bitIndex[0] + 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean skipBlock(ByteArray input, int[] bitIndex, boolean[] hasEmptyBlock) throws FormatException {
/*     */     String message;
/* 428 */     boolean last = input.readBit(bitIndex);
/*     */     
/* 430 */     if (last)
/*     */     {
/*     */ 
/*     */       
/* 434 */       input.clearBit(bitIndex[0] - 1);
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
/* 445 */     int type = input.readBits(bitIndex, 2);
/*     */ 
/*     */     
/* 448 */     boolean plain0 = false;
/*     */     
/* 450 */     switch (type) {
/*     */ 
/*     */ 
/*     */       
/*     */       case 0:
/* 455 */         plain0 = (skipPlainBlock(input, bitIndex) == 0);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 1:
/* 460 */         skipFixedBlock(input, bitIndex);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 465 */         skipDynamicBlock(input, bitIndex);
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/* 471 */         message = String.format("[%s] Bad compression type '11' at the bit index '%d'.", new Object[] { PerMessageDeflateExtension.class
/*     */               
/* 473 */               .getSimpleName(), Integer.valueOf(bitIndex[0]) });
/*     */         
/* 475 */         throw new FormatException(message);
/*     */     } 
/*     */ 
/*     */     
/* 479 */     if (input.length() <= bitIndex[0] / 8)
/*     */     {
/*     */       
/* 482 */       last = true;
/*     */     }
/*     */     
/* 485 */     if (last && plain0)
/*     */     {
/*     */       
/* 488 */       hasEmptyBlock[0] = true;
/*     */     }
/*     */ 
/*     */     
/* 492 */     return !last;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int skipPlainBlock(ByteArray input, int[] bitIndex) {
/* 501 */     int bi = bitIndex[0] + 7 & 0xFFFFFFF8;
/*     */ 
/*     */ 
/*     */     
/* 505 */     int index = bi / 8;
/*     */ 
/*     */     
/* 508 */     int len = (input.get(index) & 0xFF) + (input.get(index + 1) & 0xFF) * 256;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 513 */     index += 4;
/*     */ 
/*     */ 
/*     */     
/* 517 */     bitIndex[0] = (index + len) * 8;
/*     */     
/* 519 */     return len;
/*     */   }
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
/*     */   private static void skipFixedBlock(ByteArray input, int[] bitIndex) throws FormatException {
/* 537 */     skipData(input, bitIndex, 
/* 538 */         FixedLiteralLengthHuffman.getInstance(), 
/* 539 */         FixedDistanceHuffman.getInstance());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void skipDynamicBlock(ByteArray input, int[] bitIndex) throws FormatException {
/* 550 */     Huffman[] tables = new Huffman[2];
/* 551 */     DeflateUtil.readDynamicTables(input, bitIndex, tables);
/*     */     
/* 553 */     skipData(input, bitIndex, tables[0], tables[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void skipData(ByteArray input, int[] bitIndex, Huffman literalLengthHuffman, Huffman distanceHuffman) throws FormatException {
/*     */     while (true) {
/* 566 */       int literalLength = literalLengthHuffman.readSym(input, bitIndex);
/*     */ 
/*     */       
/* 569 */       if (literalLength == 256) {
/*     */         break;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 576 */       if (0 <= literalLength && literalLength <= 255) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 585 */       DeflateUtil.readLength(input, bitIndex, literalLength);
/*     */ 
/*     */       
/* 588 */       DeflateUtil.readDistance(input, bitIndex, distanceHuffman);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\PerMessageDeflateExtension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */