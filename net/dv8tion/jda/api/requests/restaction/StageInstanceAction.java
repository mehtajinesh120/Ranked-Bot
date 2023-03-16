package net.dv8tion.jda.api.requests.restaction;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.entities.StageInstance;
import net.dv8tion.jda.api.requests.RestAction;

public interface StageInstanceAction extends RestAction<StageInstance> {
  @Nonnull
  StageInstanceAction setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
  
  @Nonnull
  StageInstanceAction timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
  
  @Nonnull
  StageInstanceAction deadline(long paramLong);
  
  @Nonnull
  @CheckReturnValue
  StageInstanceAction setTopic(@Nonnull String paramString);
  
  @Nonnull
  @CheckReturnValue
  StageInstanceAction setPrivacyLevel(@Nonnull StageInstance.PrivacyLevel paramPrivacyLevel);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\StageInstanceAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */