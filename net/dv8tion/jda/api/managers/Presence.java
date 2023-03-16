package net.dv8tion.jda.api.managers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

public interface Presence {
  @Nonnull
  JDA getJDA();
  
  @Nonnull
  OnlineStatus getStatus();
  
  @Nullable
  Activity getActivity();
  
  boolean isIdle();
  
  void setStatus(@Nullable OnlineStatus paramOnlineStatus);
  
  void setActivity(@Nullable Activity paramActivity);
  
  void setIdle(boolean paramBoolean);
  
  void setPresence(@Nullable OnlineStatus paramOnlineStatus, @Nullable Activity paramActivity, boolean paramBoolean);
  
  void setPresence(@Nullable OnlineStatus paramOnlineStatus, @Nullable Activity paramActivity);
  
  void setPresence(@Nullable OnlineStatus paramOnlineStatus, boolean paramBoolean);
  
  void setPresence(@Nullable Activity paramActivity, boolean paramBoolean);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\managers\Presence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */