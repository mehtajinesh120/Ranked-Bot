package net.dv8tion.jda.api.utils;

import java.util.Iterator;

public interface ClosableIterator<T> extends Iterator<T>, AutoCloseable {
  void close();
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\ClosableIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */