package okio;

import java.io.Closeable;
import java.io.IOException;

public interface Source extends Closeable {
  long read(Buffer paramBuffer, long paramLong) throws IOException;
  
  Timeout timeout();
  
  void close() throws IOException;
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okio\Source.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */