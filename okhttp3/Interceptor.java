package okhttp3;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;

public interface Interceptor {
  Response intercept(Chain paramChain) throws IOException;
  
  public static interface Chain {
    Request request();
    
    Response proceed(Request param1Request) throws IOException;
    
    @Nullable
    Connection connection();
    
    Call call();
    
    int connectTimeoutMillis();
    
    Chain withConnectTimeout(int param1Int, TimeUnit param1TimeUnit);
    
    int readTimeoutMillis();
    
    Chain withReadTimeout(int param1Int, TimeUnit param1TimeUnit);
    
    int writeTimeoutMillis();
    
    Chain withWriteTimeout(int param1Int, TimeUnit param1TimeUnit);
  }
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Interceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */