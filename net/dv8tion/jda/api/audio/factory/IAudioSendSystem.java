package net.dv8tion.jda.api.audio.factory;

import java.util.concurrent.ConcurrentMap;
import javax.annotation.CheckForNull;

public interface IAudioSendSystem {
  void start();
  
  void shutdown();
  
  default void setContextMap(@CheckForNull ConcurrentMap<String, String> contextMap) {}
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\factory\IAudioSendSystem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */