package net.dv8tion.jda.api.requests.restaction;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;

public interface MemberAction extends RestAction<Void> {
  @Nonnull
  MemberAction setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
  
  @Nonnull
  MemberAction timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
  
  @Nonnull
  MemberAction deadline(long paramLong);
  
  @Nonnull
  String getAccessToken();
  
  @Nonnull
  String getUserId();
  
  @Nullable
  User getUser();
  
  @Nonnull
  Guild getGuild();
  
  @Nonnull
  @CheckReturnValue
  MemberAction setNickname(@Nullable String paramString);
  
  @Nonnull
  @CheckReturnValue
  MemberAction setRoles(@Nullable Collection<Role> paramCollection);
  
  @Nonnull
  @CheckReturnValue
  MemberAction setRoles(@Nullable Role... paramVarArgs);
  
  @Nonnull
  @CheckReturnValue
  MemberAction setMute(boolean paramBoolean);
  
  @Nonnull
  @CheckReturnValue
  MemberAction setDeafen(boolean paramBoolean);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\MemberAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */