package okhttp3;

import javax.annotation.Nullable;
import okio.ByteString;

public interface WebSocket {
  Request request();
  
  long queueSize();
  
  boolean send(String paramString);
  
  boolean send(ByteString paramByteString);
  
  boolean close(int paramInt, @Nullable String paramString);
  
  void cancel();
  
  public static interface Factory {
    WebSocket newWebSocket(Request param1Request, WebSocketListener param1WebSocketListener);
  }
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\WebSocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */