package net.dv8tion.jda.api.events.guild.voice;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.UpdateEvent;

public interface GuildVoiceUpdateEvent extends UpdateEvent<Member, VoiceChannel> {
  public static final String IDENTIFIER = "voice-channel";
  
  @Nonnull
  Member getMember();
  
  @Nonnull
  Guild getGuild();
  
  @Nullable
  VoiceChannel getChannelLeft();
  
  @Nullable
  VoiceChannel getChannelJoined();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\voice\GuildVoiceUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */