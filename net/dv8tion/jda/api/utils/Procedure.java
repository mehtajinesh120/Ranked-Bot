package net.dv8tion.jda.api.utils;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface Procedure<T> {
  boolean execute(@Nonnull T paramT);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\Procedure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */