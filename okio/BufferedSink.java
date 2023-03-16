package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;

public interface BufferedSink extends Sink, WritableByteChannel {
  Buffer buffer();
  
  BufferedSink write(ByteString paramByteString) throws IOException;
  
  BufferedSink write(byte[] paramArrayOfbyte) throws IOException;
  
  BufferedSink write(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  long writeAll(Source paramSource) throws IOException;
  
  BufferedSink write(Source paramSource, long paramLong) throws IOException;
  
  BufferedSink writeUtf8(String paramString) throws IOException;
  
  BufferedSink writeUtf8(String paramString, int paramInt1, int paramInt2) throws IOException;
  
  BufferedSink writeUtf8CodePoint(int paramInt) throws IOException;
  
  BufferedSink writeString(String paramString, Charset paramCharset) throws IOException;
  
  BufferedSink writeString(String paramString, int paramInt1, int paramInt2, Charset paramCharset) throws IOException;
  
  BufferedSink writeByte(int paramInt) throws IOException;
  
  BufferedSink writeShort(int paramInt) throws IOException;
  
  BufferedSink writeShortLe(int paramInt) throws IOException;
  
  BufferedSink writeInt(int paramInt) throws IOException;
  
  BufferedSink writeIntLe(int paramInt) throws IOException;
  
  BufferedSink writeLong(long paramLong) throws IOException;
  
  BufferedSink writeLongLe(long paramLong) throws IOException;
  
  BufferedSink writeDecimalLong(long paramLong) throws IOException;
  
  BufferedSink writeHexadecimalUnsignedLong(long paramLong) throws IOException;
  
  void flush() throws IOException;
  
  BufferedSink emit() throws IOException;
  
  BufferedSink emitCompleteSegments() throws IOException;
  
  OutputStream outputStream();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okio\BufferedSink.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */