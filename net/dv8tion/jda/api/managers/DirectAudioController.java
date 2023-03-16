package net.dv8tion.jda.api.managers;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;

public interface DirectAudioController {
  @Nonnull
  JDA getJDA();
  
  void connect(@Nonnull VoiceChannel paramVoiceChannel);
  
  void disconnect(@Nonnull Guild paramGuild);
  
  void reconnect(@Nonnull VoiceChannel paramVoiceChannel);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\managers\DirectAudioController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */