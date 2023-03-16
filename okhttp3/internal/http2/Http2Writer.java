/*     */ package okhttp3.internal.http2;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import okhttp3.internal.Util;
/*     */ import okio.Buffer;
/*     */ import okio.BufferedSink;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class Http2Writer
/*     */   implements Closeable
/*     */ {
/*  47 */   private static final Logger logger = Logger.getLogger(Http2.class.getName());
/*     */   
/*     */   private final BufferedSink sink;
/*     */   
/*     */   private final boolean client;
/*     */   private final Buffer hpackBuffer;
/*     */   private int maxFrameSize;
/*     */   private boolean closed;
/*     */   final Hpack.Writer hpackWriter;
/*     */   
/*     */   Http2Writer(BufferedSink sink, boolean client) {
/*  58 */     this.sink = sink;
/*  59 */     this.client = client;
/*  60 */     this.hpackBuffer = new Buffer();
/*  61 */     this.hpackWriter = new Hpack.Writer(this.hpackBuffer);
/*  62 */     this.maxFrameSize = 16384;
/*     */   }
/*     */   
/*     */   public synchronized void connectionPreface() throws IOException {
/*  66 */     if (this.closed) throw new IOException("closed"); 
/*  67 */     if (!this.client)
/*  68 */       return;  if (logger.isLoggable(Level.FINE)) {
/*  69 */       logger.fine(Util.format(">> CONNECTION %s", new Object[] { Http2.CONNECTION_PREFACE.hex() }));
/*     */     }
/*  71 */     this.sink.write(Http2.CONNECTION_PREFACE.toByteArray());
/*  72 */     this.sink.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void applyAndAckSettings(Settings peerSettings) throws IOException {
/*  77 */     if (this.closed) throw new IOException("closed"); 
/*  78 */     this.maxFrameSize = peerSettings.getMaxFrameSize(this.maxFrameSize);
/*  79 */     if (peerSettings.getHeaderTableSize() != -1) {
/*  80 */       this.hpackWriter.setHeaderTableSizeSetting(peerSettings.getHeaderTableSize());
/*     */     }
/*  82 */     int length = 0;
/*  83 */     byte type = 4;
/*  84 */     byte flags = 1;
/*  85 */     int streamId = 0;
/*  86 */     frameHeader(streamId, length, type, flags);
/*  87 */     this.sink.flush();
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
/*     */   public synchronized void pushPromise(int streamId, int promisedStreamId, List<Header> requestHeaders) throws IOException {
/* 105 */     if (this.closed) throw new IOException("closed"); 
/* 106 */     this.hpackWriter.writeHeaders(requestHeaders);
/*     */     
/* 108 */     long byteCount = this.hpackBuffer.size();
/* 109 */     int length = (int)Math.min((this.maxFrameSize - 4), byteCount);
/* 110 */     byte type = 5;
/* 111 */     byte flags = (byteCount == length) ? 4 : 0;
/* 112 */     frameHeader(streamId, length + 4, type, flags);
/* 113 */     this.sink.writeInt(promisedStreamId & Integer.MAX_VALUE);
/* 114 */     this.sink.write(this.hpackBuffer, length);
/*     */     
/* 116 */     if (byteCount > length) writeContinuationFrames(streamId, byteCount - length); 
/*     */   }
/*     */   
/*     */   public synchronized void flush() throws IOException {
/* 120 */     if (this.closed) throw new IOException("closed"); 
/* 121 */     this.sink.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void rstStream(int streamId, ErrorCode errorCode) throws IOException {
/* 126 */     if (this.closed) throw new IOException("closed"); 
/* 127 */     if (errorCode.httpCode == -1) throw new IllegalArgumentException();
/*     */     
/* 129 */     int length = 4;
/* 130 */     byte type = 3;
/* 131 */     byte flags = 0;
/* 132 */     frameHeader(streamId, length, type, flags);
/* 133 */     this.sink.writeInt(errorCode.httpCode);
/* 134 */     this.sink.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public int maxDataLength() {
/* 139 */     return this.maxFrameSize;
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
/*     */   public synchronized void data(boolean outFinished, int streamId, Buffer source, int byteCount) throws IOException {
/* 152 */     if (this.closed) throw new IOException("closed"); 
/* 153 */     byte flags = 0;
/* 154 */     if (outFinished) flags = (byte)(flags | 0x1); 
/* 155 */     dataFrame(streamId, flags, source, byteCount);
/*     */   }
/*     */   
/*     */   void dataFrame(int streamId, byte flags, Buffer buffer, int byteCount) throws IOException {
/* 159 */     byte type = 0;
/* 160 */     frameHeader(streamId, byteCount, type, flags);
/* 161 */     if (byteCount > 0) {
/* 162 */       this.sink.write(buffer, byteCount);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void settings(Settings settings) throws IOException {
/* 168 */     if (this.closed) throw new IOException("closed"); 
/* 169 */     int length = settings.size() * 6;
/* 170 */     byte type = 4;
/* 171 */     byte flags = 0;
/* 172 */     int streamId = 0;
/* 173 */     frameHeader(streamId, length, type, flags);
/* 174 */     for (int i = 0; i < 10; i++) {
/* 175 */       if (settings.isSet(i)) {
/* 176 */         int id = i;
/* 177 */         if (id == 4) {
/* 178 */           id = 3;
/* 179 */         } else if (id == 7) {
/* 180 */           id = 4;
/*     */         } 
/* 182 */         this.sink.writeShort(id);
/* 183 */         this.sink.writeInt(settings.get(i));
/*     */       } 
/* 185 */     }  this.sink.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void ping(boolean ack, int payload1, int payload2) throws IOException {
/* 193 */     if (this.closed) throw new IOException("closed"); 
/* 194 */     int length = 8;
/* 195 */     byte type = 6;
/* 196 */     byte flags = ack ? 1 : 0;
/* 197 */     int streamId = 0;
/* 198 */     frameHeader(streamId, length, type, flags);
/* 199 */     this.sink.writeInt(payload1);
/* 200 */     this.sink.writeInt(payload2);
/* 201 */     this.sink.flush();
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
/*     */   public synchronized void goAway(int lastGoodStreamId, ErrorCode errorCode, byte[] debugData) throws IOException {
/* 214 */     if (this.closed) throw new IOException("closed"); 
/* 215 */     if (errorCode.httpCode == -1) throw Http2.illegalArgument("errorCode.httpCode == -1", new Object[0]); 
/* 216 */     int length = 8 + debugData.length;
/* 217 */     byte type = 7;
/* 218 */     byte flags = 0;
/* 219 */     int streamId = 0;
/* 220 */     frameHeader(streamId, length, type, flags);
/* 221 */     this.sink.writeInt(lastGoodStreamId);
/* 222 */     this.sink.writeInt(errorCode.httpCode);
/* 223 */     if (debugData.length > 0) {
/* 224 */       this.sink.write(debugData);
/*     */     }
/* 226 */     this.sink.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void windowUpdate(int streamId, long windowSizeIncrement) throws IOException {
/* 234 */     if (this.closed) throw new IOException("closed"); 
/* 235 */     if (windowSizeIncrement == 0L || windowSizeIncrement > 2147483647L)
/* 236 */       throw Http2.illegalArgument("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s", new Object[] {
/* 237 */             Long.valueOf(windowSizeIncrement)
/*     */           }); 
/* 239 */     int length = 4;
/* 240 */     byte type = 8;
/* 241 */     byte flags = 0;
/* 242 */     frameHeader(streamId, length, type, flags);
/* 243 */     this.sink.writeInt((int)windowSizeIncrement);
/* 244 */     this.sink.flush();
/*     */   }
/*     */   
/*     */   public void frameHeader(int streamId, int length, byte type, byte flags) throws IOException {
/* 248 */     if (logger.isLoggable(Level.FINE)) logger.fine(Http2.frameLog(false, streamId, length, type, flags)); 
/* 249 */     if (length > this.maxFrameSize) {
/* 250 */       throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", new Object[] { Integer.valueOf(this.maxFrameSize), Integer.valueOf(length) });
/*     */     }
/* 252 */     if ((streamId & Integer.MIN_VALUE) != 0) throw Http2.illegalArgument("reserved bit set: %s", new Object[] { Integer.valueOf(streamId) }); 
/* 253 */     writeMedium(this.sink, length);
/* 254 */     this.sink.writeByte(type & 0xFF);
/* 255 */     this.sink.writeByte(flags & 0xFF);
/* 256 */     this.sink.writeInt(streamId & Integer.MAX_VALUE);
/*     */   }
/*     */   
/*     */   public synchronized void close() throws IOException {
/* 260 */     this.closed = true;
/* 261 */     this.sink.close();
/*     */   }
/*     */   
/*     */   private static void writeMedium(BufferedSink sink, int i) throws IOException {
/* 265 */     sink.writeByte(i >>> 16 & 0xFF);
/* 266 */     sink.writeByte(i >>> 8 & 0xFF);
/* 267 */     sink.writeByte(i & 0xFF);
/*     */   }
/*     */   
/*     */   private void writeContinuationFrames(int streamId, long byteCount) throws IOException {
/* 271 */     while (byteCount > 0L) {
/* 272 */       int length = (int)Math.min(this.maxFrameSize, byteCount);
/* 273 */       byteCount -= length;
/* 274 */       frameHeader(streamId, length, (byte)9, (byteCount == 0L) ? 4 : 0);
/* 275 */       this.sink.write(this.hpackBuffer, length);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void headers(boolean outFinished, int streamId, List<Header> headerBlock) throws IOException {
/* 281 */     if (this.closed) throw new IOException("closed"); 
/* 282 */     this.hpackWriter.writeHeaders(headerBlock);
/*     */     
/* 284 */     long byteCount = this.hpackBuffer.size();
/* 285 */     int length = (int)Math.min(this.maxFrameSize, byteCount);
/* 286 */     byte type = 1;
/* 287 */     byte flags = (byteCount == length) ? 4 : 0;
/* 288 */     if (outFinished) flags = (byte)(flags | 0x1); 
/* 289 */     frameHeader(streamId, length, type, flags);
/* 290 */     this.sink.write(this.hpackBuffer, length);
/*     */     
/* 292 */     if (byteCount > length) writeContinuationFrames(streamId, byteCount - length); 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http2\Http2Writer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */