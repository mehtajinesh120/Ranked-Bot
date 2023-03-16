package net.dv8tion.jda.api.requests.restaction.order;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.api.requests.RestAction;

public interface OrderAction<T, M extends OrderAction<T, M>> extends RestAction<Void> {
  @Nonnull
  M setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
  
  @Nonnull
  M timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
  
  @Nonnull
  M deadline(long paramLong);
  
  boolean isAscendingOrder();
  
  @Nonnull
  List<T> getCurrentOrder();
  
  @Nonnull
  M selectPosition(int paramInt);
  
  @Nonnull
  M selectPosition(@Nonnull T paramT);
  
  int getSelectedPosition();
  
  @Nonnull
  T getSelectedEntity();
  
  @Nonnull
  M moveUp(int paramInt);
  
  @Nonnull
  M moveDown(int paramInt);
  
  @Nonnull
  M moveTo(int paramInt);
  
  @Nonnull
  M swapPosition(int paramInt);
  
  @Nonnull
  M swapPosition(@Nonnull T paramT);
  
  @Nonnull
  M reverseOrder();
  
  @Nonnull
  M shuffleOrder();
  
  @Nonnull
  M sortOrder(@Nonnull Comparator<T> paramComparator);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\order\OrderAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */