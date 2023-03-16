package net.dv8tion.jda.api.hooks;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.GenericEvent;

@FunctionalInterface
public interface EventListener {
  void onEvent(@Nonnull GenericEvent paramGenericEvent);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\hooks\EventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */