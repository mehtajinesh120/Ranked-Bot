package okhttp3;

import java.io.IOException;
import okio.Timeout;

public interface Call extends Cloneable {
  Request request();
  
  Response execute() throws IOException;
  
  void enqueue(Callback paramCallback);
  
  void cancel();
  
  boolean isExecuted();
  
  boolean isCanceled();
  
  Timeout timeout();
  
  Call clone();
  
  public static interface Factory {
    Call newCall(Request param1Request);
  }
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Call.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */