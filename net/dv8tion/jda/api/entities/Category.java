package net.dv8tion.jda.api.entities;

import java.util.List;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;
import net.dv8tion.jda.api.requests.restaction.order.CategoryOrderAction;

public interface Category extends GuildChannel {
  @Nonnull
  List<GuildChannel> getChannels();
  
  @Nonnull
  List<StoreChannel> getStoreChannels();
  
  @Nonnull
  List<TextChannel> getTextChannels();
  
  @Nonnull
  List<VoiceChannel> getVoiceChannels();
  
  @Nonnull
  @CheckReturnValue
  ChannelAction<TextChannel> createTextChannel(@Nonnull String paramString);
  
  @Nonnull
  @CheckReturnValue
  ChannelAction<VoiceChannel> createVoiceChannel(@Nonnull String paramString);
  
  @Nonnull
  @CheckReturnValue
  ChannelAction<StageChannel> createStageChannel(@Nonnull String paramString);
  
  @Nonnull
  @CheckReturnValue
  CategoryOrderAction modifyTextChannelPositions();
  
  @Nonnull
  @CheckReturnValue
  CategoryOrderAction modifyVoiceChannelPositions();
  
  @Nonnull
  ChannelAction<Category> createCopy(@Nonnull Guild paramGuild);
  
  @Nonnull
  ChannelAction<Category> createCopy();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\Category.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */