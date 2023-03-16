package net.dv8tion.jda.internal.utils;

import net.dv8tion.jda.api.utils.data.DataObject;

@FunctionalInterface
public interface CacheConsumer {
  void execute(long paramLong, DataObject paramDataObject);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\CacheConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */