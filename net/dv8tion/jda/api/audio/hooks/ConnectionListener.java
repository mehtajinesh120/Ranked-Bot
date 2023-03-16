package net.dv8tion.jda.api.audio.hooks;

import java.util.EnumSet;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.audio.SpeakingMode;
import net.dv8tion.jda.api.entities.User;

public interface ConnectionListener {
  void onPing(long paramLong);
  
  void onStatusChange(@Nonnull ConnectionStatus paramConnectionStatus);
  
  void onUserSpeaking(@Nonnull User paramUser, boolean paramBoolean);
  
  default void onUserSpeaking(@Nonnull User user, @Nonnull EnumSet<SpeakingMode> modes) {}
  
  default void onUserSpeaking(@Nonnull User user, boolean speaking, boolean soundshare) {}
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\hooks\ConnectionListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */