package net.dv8tion.jda.api.utils.cache;

import java.util.NavigableSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

public interface SortedSnowflakeCacheView<T extends Comparable<? super T> & net.dv8tion.jda.api.entities.ISnowflake> extends SnowflakeCacheView<T> {
  void forEachUnordered(@Nonnull Consumer<? super T> paramConsumer);
  
  @Nonnull
  NavigableSet<T> asSet();
  
  @Nonnull
  Stream<T> streamUnordered();
  
  @Nonnull
  Stream<T> parallelStreamUnordered();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\cache\SortedSnowflakeCacheView.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */