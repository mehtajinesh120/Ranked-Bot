package net.dv8tion.jda.api.entities;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.requests.RestAction;

public interface PrivateChannel extends MessageChannel {
  @Nonnull
  User getUser();
  
  @Nonnull
  @CheckReturnValue
  RestAction<Void> close();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\PrivateChannel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */