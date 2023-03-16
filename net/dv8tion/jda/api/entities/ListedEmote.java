package net.dv8tion.jda.api.entities;

import javax.annotation.Nonnull;

public interface ListedEmote extends Emote {
  @Nonnull
  User getUser();
  
  boolean hasUser();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\ListedEmote.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */