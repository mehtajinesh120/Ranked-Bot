package net.dv8tion.jda.api.managers;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.entities.StageInstance;

public interface StageInstanceManager extends Manager<StageInstanceManager> {
  public static final long TOPIC = 1L;
  
  public static final long PRIVACY_LEVEL = 2L;
  
  @Nonnull
  StageInstanceManager reset(long paramLong);
  
  @Nonnull
  StageInstanceManager reset(long... paramVarArgs);
  
  @Nonnull
  StageInstance getStageInstance();
  
  @Nonnull
  @CheckReturnValue
  StageInstanceManager setTopic(@Nullable String paramString);
  
  @Nonnull
  @CheckReturnValue
  StageInstanceManager setPrivacyLevel(@Nonnull StageInstance.PrivacyLevel paramPrivacyLevel);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\managers\StageInstanceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */