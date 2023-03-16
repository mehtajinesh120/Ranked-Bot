package okhttp3;

import java.net.Socket;
import javax.annotation.Nullable;

public interface Connection {
  Route route();
  
  Socket socket();
  
  @Nullable
  Handshake handshake();
  
  Protocol protocol();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Connection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */