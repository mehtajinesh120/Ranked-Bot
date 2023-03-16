package net.dv8tion.jda.api.managers;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.dv8tion.jda.annotations.DeprecatedSince;
import net.dv8tion.jda.annotations.ForRemoval;
import net.dv8tion.jda.annotations.ReplaceWith;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public interface GuildManager extends Manager<GuildManager> {
  public static final long NAME = 1L;
  
  public static final long REGION = 2L;
  
  public static final long ICON = 4L;
  
  public static final long SPLASH = 8L;
  
  public static final long AFK_CHANNEL = 16L;
  
  public static final long AFK_TIMEOUT = 32L;
  
  public static final long SYSTEM_CHANNEL = 64L;
  
  public static final long MFA_LEVEL = 128L;
  
  public static final long NOTIFICATION_LEVEL = 256L;
  
  public static final long EXPLICIT_CONTENT_LEVEL = 512L;
  
  public static final long VERIFICATION_LEVEL = 1024L;
  
  public static final long BANNER = 2048L;
  
  public static final long VANITY_URL = 4096L;
  
  public static final long DESCRIPTION = 8192L;
  
  public static final long RULES_CHANNEL = 16384L;
  
  public static final long COMMUNITY_UPDATES_CHANNEL = 32768L;
  
  @Nonnull
  GuildManager reset(long paramLong);
  
  @Nonnull
  GuildManager reset(long... paramVarArgs);
  
  @Nonnull
  Guild getGuild();
  
  @Nonnull
  @CheckReturnValue
  GuildManager setName(@Nonnull String paramString);
  
  @Nonnull
  @CheckReturnValue
  @Deprecated
  @ForRemoval(deadline = "5.0.0")
  @ReplaceWith("ChannelManager.setRegion()")
  @DeprecatedSince("4.3.0")
  GuildManager setRegion(@Nonnull Region paramRegion);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setIcon(@Nullable Icon paramIcon);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setSplash(@Nullable Icon paramIcon);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setAfkChannel(@Nullable VoiceChannel paramVoiceChannel);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setSystemChannel(@Nullable TextChannel paramTextChannel);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setRulesChannel(@Nullable TextChannel paramTextChannel);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setCommunityUpdatesChannel(@Nullable TextChannel paramTextChannel);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setAfkTimeout(@Nonnull Guild.Timeout paramTimeout);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setVerificationLevel(@Nonnull Guild.VerificationLevel paramVerificationLevel);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setDefaultNotificationLevel(@Nonnull Guild.NotificationLevel paramNotificationLevel);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setRequiredMFALevel(@Nonnull Guild.MFALevel paramMFALevel);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setExplicitContentLevel(@Nonnull Guild.ExplicitContentLevel paramExplicitContentLevel);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setBanner(@Nullable Icon paramIcon);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setVanityCode(@Nullable String paramString);
  
  @Nonnull
  @CheckReturnValue
  GuildManager setDescription(@Nullable String paramString);
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\managers\GuildManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */