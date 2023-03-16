package net.dv8tion.jda.api.utils.concurrent;

import java.util.function.Consumer;
import javax.annotation.Nonnull;

public interface Task<T> {
  boolean isStarted();
  
  @Nonnull
  Task<T> onError(@Nonnull Consumer<? super Throwable> paramConsumer);
  
  @Nonnull
  Task<T> onSuccess(@Nonnull Consumer<? super T> paramConsumer);
  
  @Nonnull
  T get();
  
  void cancel();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\concurrent\Task.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */