package net.dv8tion.jda.api.managers;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.SelfUser;

public interface AccountManager extends Manager<AccountManager> {
  public static final long NAME = 1L;
  
  public static final long AVATAR = 2L;
  
  @Nonnull
  SelfUser getSelfUser();
  
  @Nonnull
  @CheckReturnValue
  AccountManager reset(long paramLong);
  
  @Nonnull
  @CheckReturnValue
  AccountManager reset(long... paramVarArgs);
  
  @Nonnull
  @CheckReturnValue
  AccountManager setName(@Nonnull String paramString);
  
  @Nonnull
  @CheckReturnValue
  AccountManager setAvatar(@Nullable Icon paramIcon);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\managers\AccountManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */