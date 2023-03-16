/*    */ package okhttp3.internal.cache2;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.FileChannel;
/*    */ import java.nio.channels.ReadableByteChannel;
/*    */ import java.nio.channels.WritableByteChannel;
/*    */ import okio.Buffer;
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
/*    */ final class FileOperator
/*    */ {
/*    */   private final FileChannel fileChannel;
/*    */   
/*    */   FileOperator(FileChannel fileChannel) {
/* 40 */     this.fileChannel = fileChannel;
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(long pos, Buffer source, long byteCount) throws IOException {
/* 45 */     if (byteCount < 0L || byteCount > source.size()) throw new IndexOutOfBoundsException();
/*    */     
/* 47 */     while (byteCount > 0L) {
/* 48 */       long bytesWritten = this.fileChannel.transferFrom((ReadableByteChannel)source, pos, byteCount);
/* 49 */       pos += bytesWritten;
/* 50 */       byteCount -= bytesWritten;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void read(long pos, Buffer sink, long byteCount) throws IOException {
/* 60 */     if (byteCount < 0L) throw new IndexOutOfBoundsException();
/*    */     
/* 62 */     while (byteCount > 0L) {
/* 63 */       long bytesRead = this.fileChannel.transferTo(pos, byteCount, (WritableByteChannel)sink);
/* 64 */       pos += bytesRead;
/* 65 */       byteCount -= bytesRead;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\cache2\FileOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */