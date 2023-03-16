package net.dv8tion.jda.api.utils;

import java.io.IOException;

@FunctionalInterface
public interface IOConsumer<T> {
  void accept(T paramT) throws IOException;
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\IOConsumer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */