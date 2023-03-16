/*    */ package okio;
/*    */ 
/*    */ import java.io.IOException;
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
/*    */ final class PeekSource
/*    */   implements Source
/*    */ {
/*    */   private final BufferedSource upstream;
/*    */   private final Buffer buffer;
/*    */   private Segment expectedSegment;
/*    */   private int expectedPos;
/*    */   private boolean closed;
/*    */   private long pos;
/*    */   
/*    */   PeekSource(BufferedSource upstream) {
/* 40 */     this.upstream = upstream;
/* 41 */     this.buffer = upstream.buffer();
/* 42 */     this.expectedSegment = this.buffer.head;
/* 43 */     this.expectedPos = (this.expectedSegment != null) ? this.expectedSegment.pos : -1;
/*    */   }
/*    */   
/*    */   public long read(Buffer sink, long byteCount) throws IOException {
/* 47 */     if (this.closed) throw new IllegalStateException("closed");
/*    */ 
/*    */ 
/*    */     
/* 51 */     if (this.expectedSegment != null && (this.expectedSegment != this.buffer.head || this.expectedPos != this.buffer.head.pos))
/*    */     {
/* 53 */       throw new IllegalStateException("Peek source is invalid because upstream source was used");
/*    */     }
/*    */     
/* 56 */     this.upstream.request(this.pos + byteCount);
/* 57 */     if (this.expectedSegment == null && this.buffer.head != null) {
/*    */ 
/*    */ 
/*    */       
/* 61 */       this.expectedSegment = this.buffer.head;
/* 62 */       this.expectedPos = this.buffer.head.pos;
/*    */     } 
/*    */     
/* 65 */     long toCopy = Math.min(byteCount, this.buffer.size - this.pos);
/* 66 */     if (toCopy <= 0L) return -1L;
/*    */     
/* 68 */     this.buffer.copyTo(sink, this.pos, toCopy);
/* 69 */     this.pos += toCopy;
/* 70 */     return toCopy;
/*    */   }
/*    */   
/*    */   public Timeout timeout() {
/* 74 */     return this.upstream.timeout();
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 78 */     this.closed = true;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okio\PeekSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */