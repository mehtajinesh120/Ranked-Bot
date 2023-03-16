package okio;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;

public interface Sink extends Closeable, Flushable {
  void write(Buffer paramBuffer, long paramLong) throws IOException;
  
  void flush() throws IOException;
  
  Timeout timeout();
  
  void close() throws IOException;
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okio\Sink.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */