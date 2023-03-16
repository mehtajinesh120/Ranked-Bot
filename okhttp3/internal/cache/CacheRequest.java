package okhttp3.internal.cache;

import java.io.IOException;
import okio.Sink;

public interface CacheRequest {
  Sink body() throws IOException;
  
  void abort();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\cache\CacheRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */