package okio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import javax.annotation.Nullable;

public interface BufferedSource extends Source, ReadableByteChannel {
  @Deprecated
  Buffer buffer();
  
  Buffer getBuffer();
  
  boolean exhausted() throws IOException;
  
  void require(long paramLong) throws IOException;
  
  boolean request(long paramLong) throws IOException;
  
  byte readByte() throws IOException;
  
  short readShort() throws IOException;
  
  short readShortLe() throws IOException;
  
  int readInt() throws IOException;
  
  int readIntLe() throws IOException;
  
  long readLong() throws IOException;
  
  long readLongLe() throws IOException;
  
  long readDecimalLong() throws IOException;
  
  long readHexadecimalUnsignedLong() throws IOException;
  
  void skip(long paramLong) throws IOException;
  
  ByteString readByteString() throws IOException;
  
  ByteString readByteString(long paramLong) throws IOException;
  
  int select(Options paramOptions) throws IOException;
  
  byte[] readByteArray() throws IOException;
  
  byte[] readByteArray(long paramLong) throws IOException;
  
  int read(byte[] paramArrayOfbyte) throws IOException;
  
  void readFully(byte[] paramArrayOfbyte) throws IOException;
  
  int read(byte[] paramArrayOfbyte, int paramInt1, int paramInt2) throws IOException;
  
  void readFully(Buffer paramBuffer, long paramLong) throws IOException;
  
  long readAll(Sink paramSink) throws IOException;
  
  String readUtf8() throws IOException;
  
  String readUtf8(long paramLong) throws IOException;
  
  @Nullable
  String readUtf8Line() throws IOException;
  
  String readUtf8LineStrict() throws IOException;
  
  String readUtf8LineStrict(long paramLong) throws IOException;
  
  int readUtf8CodePoint() throws IOException;
  
  String readString(Charset paramCharset) throws IOException;
  
  String readString(long paramLong, Charset paramCharset) throws IOException;
  
  long indexOf(byte paramByte) throws IOException;
  
  long indexOf(byte paramByte, long paramLong) throws IOException;
  
  long indexOf(byte paramByte, long paramLong1, long paramLong2) throws IOException;
  
  long indexOf(ByteString paramByteString) throws IOException;
  
  long indexOf(ByteString paramByteString, long paramLong) throws IOException;
  
  long indexOfElement(ByteString paramByteString) throws IOException;
  
  long indexOfElement(ByteString paramByteString, long paramLong) throws IOException;
  
  boolean rangeEquals(long paramLong, ByteString paramByteString) throws IOException;
  
  boolean rangeEquals(long paramLong, ByteString paramByteString, int paramInt1, int paramInt2) throws IOException;
  
  BufferedSource peek();
  
  InputStream inputStream();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okio\BufferedSource.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */