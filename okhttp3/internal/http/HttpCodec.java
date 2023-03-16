package okhttp3.internal.http;

import java.io.IOException;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Sink;

public interface HttpCodec {
  public static final int DISCARD_STREAM_TIMEOUT_MILLIS = 100;
  
  Sink createRequestBody(Request paramRequest, long paramLong);
  
  void writeRequestHeaders(Request paramRequest) throws IOException;
  
  void flushRequest() throws IOException;
  
  void finishRequest() throws IOException;
  
  Response.Builder readResponseHeaders(boolean paramBoolean) throws IOException;
  
  ResponseBody openResponseBody(Response paramResponse) throws IOException;
  
  Headers trailers() throws IOException;
  
  void cancel();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http\HttpCodec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */