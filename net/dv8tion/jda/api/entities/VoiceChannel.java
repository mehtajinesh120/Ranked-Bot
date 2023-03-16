package net.dv8tion.jda.api.entities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

public interface VoiceChannel extends GuildChannel {
  int getUserLimit();
  
  int getBitrate();
  
  @Nonnull
  Region getRegion();
  
  @Nullable
  String getRegionRaw();
  
  @Nonnull
  ChannelAction<VoiceChannel> createCopy(@Nonnull Guild paramGuild);
  
  @Nonnull
  ChannelAction<VoiceChannel> createCopy();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\VoiceChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */