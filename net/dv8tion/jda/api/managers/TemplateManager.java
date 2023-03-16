package net.dv8tion.jda.api.managers;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface TemplateManager extends Manager<TemplateManager> {
  public static final long NAME = 1L;
  
  public static final long DESCRIPTION = 2L;
  
  @Nonnull
  TemplateManager reset(long paramLong);
  
  @Nonnull
  TemplateManager reset(long... paramVarArgs);
  
  @Nonnull
  @CheckReturnValue
  TemplateManager setName(@Nonnull String paramString);
  
  @Nonnull
  @CheckReturnValue
  TemplateManager setDescription(@Nullable String paramString);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\managers\TemplateManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */