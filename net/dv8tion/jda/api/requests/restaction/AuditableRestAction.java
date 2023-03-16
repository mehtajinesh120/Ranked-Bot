package net.dv8tion.jda.api.requests.restaction;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.requests.RestAction;

public interface AuditableRestAction<T> extends RestAction<T> {
  @Nonnull
  AuditableRestAction<T> reason(@Nullable String paramString);
  
  @Nonnull
  AuditableRestAction<T> setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
  
  @Nonnull
  AuditableRestAction<T> timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
  
  @Nonnull
  AuditableRestAction<T> deadline(long paramLong);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\AuditableRestAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */