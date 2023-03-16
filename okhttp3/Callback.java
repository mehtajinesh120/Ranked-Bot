package okhttp3;

import java.io.IOException;

public interface Callback {
  void onFailure(Call paramCall, IOException paramIOException);
  
  void onResponse(Call paramCall, Response paramResponse) throws IOException;
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Callback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */