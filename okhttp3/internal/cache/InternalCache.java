package okhttp3.internal.cache;

import java.io.IOException;
import javax.annotation.Nullable;
import okhttp3.Request;
import okhttp3.Response;

public interface InternalCache {
  @Nullable
  Response get(Request paramRequest) throws IOException;
  
  @Nullable
  CacheRequest put(Response paramResponse) throws IOException;
  
  void remove(Request paramRequest) throws IOException;
  
  void update(Response paramResponse1, Response paramResponse2);
  
  void trackConditionalCacheHit();
  
  void trackResponse(CacheStrategy paramCacheStrategy);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\cache\InternalCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */