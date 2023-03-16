package net.dv8tion.jda.api.entities;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.JDA;

public interface AbstractChannel extends ISnowflake {
  @Nonnull
  String getName();
  
  @Nonnull
  ChannelType getType();
  
  @Nonnull
  JDA getJDA();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\AbstractChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */