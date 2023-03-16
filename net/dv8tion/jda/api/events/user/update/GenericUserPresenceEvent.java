package net.dv8tion.jda.api.events.user.update;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.GenericEvent;

public interface GenericUserPresenceEvent extends GenericEvent {
  @Nonnull
  Guild getGuild();
  
  @Nonnull
  Member getMember();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\event\\use\\update\GenericUserPresenceEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */