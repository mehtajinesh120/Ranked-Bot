package net.dv8tion.jda.api.hooks;

import java.util.List;
import javax.annotation.Nonnull;
import net.dv8tion.jda.api.events.GenericEvent;

public interface IEventManager {
  void register(@Nonnull Object paramObject);
  
  void unregister(@Nonnull Object paramObject);
  
  void handle(@Nonnull GenericEvent paramGenericEvent);
  
  @Nonnull
  List<Object> getRegisteredListeners();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\hooks\IEventManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */