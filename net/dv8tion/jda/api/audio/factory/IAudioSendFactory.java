package net.dv8tion.jda.api.audio.factory;

import javax.annotation.Nonnull;

public interface IAudioSendFactory {
  @Nonnull
  IAudioSendSystem createSendSystem(@Nonnull IPacketProvider paramIPacketProvider);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\factory\IAudioSendFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */