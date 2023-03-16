package net.dv8tion.jda.api.events;

import javax.annotation.Nonnull;
import net.dv8tion.jda.api.JDA;

public interface GenericEvent {
  @Nonnull
  JDA getJDA();
  
  long getResponseNumber();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\GenericEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */