/*     */ package net.dv8tion.jda.api.requests.restaction;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.interactions.commands.Command;
/*     */ import net.dv8tion.jda.api.interactions.commands.OptionType;
/*     */ import net.dv8tion.jda.api.interactions.commands.build.CommandData;
/*     */ import net.dv8tion.jda.api.interactions.commands.build.OptionData;
/*     */ import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
/*     */ import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface CommandEditAction
/*     */   extends RestAction<Command>
/*     */ {
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default CommandEditAction addOptions(@Nonnull Collection<? extends OptionData> options) {
/* 175 */     Checks.noneNull(options, "Options");
/* 176 */     return addOptions(options.<OptionData>toArray(new OptionData[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default CommandEditAction addOption(@Nonnull OptionType type, @Nonnull String name, @Nonnull String description, boolean required) {
/* 209 */     return addOptions(new OptionData[] { (new OptionData(type, name, description)).setRequired(required) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default CommandEditAction addOption(@Nonnull OptionType type, @Nonnull String name, @Nonnull String description) {
/* 240 */     return addOption(type, name, description, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default CommandEditAction addSubcommands(@Nonnull Collection<? extends SubcommandData> subcommands) {
/* 277 */     Checks.noneNull(subcommands, "Subcommands");
/* 278 */     return addSubcommands(subcommands.<SubcommandData>toArray(new SubcommandData[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default CommandEditAction addSubcommandGroups(@Nonnull Collection<? extends SubcommandGroupData> groups) {
/* 315 */     Checks.noneNull(groups, "SubcommandGroups");
/* 316 */     return addSubcommandGroups(groups.<SubcommandGroupData>toArray(new SubcommandGroupData[0]));
/*     */   }
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction setCheck(@Nullable BooleanSupplier paramBooleanSupplier);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction addCheck(@Nonnull BooleanSupplier paramBooleanSupplier);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction timeout(long paramLong, @Nonnull TimeUnit paramTimeUnit);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction deadline(long paramLong);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction apply(@Nonnull CommandData paramCommandData);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction setDefaultEnabled(boolean paramBoolean);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction setName(@Nullable String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction setDescription(@Nullable String paramString);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction clearOptions();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction addOptions(@Nonnull OptionData... paramVarArgs);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction addSubcommands(@Nonnull SubcommandData... paramVarArgs);
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   CommandEditAction addSubcommandGroups(@Nonnull SubcommandGroupData... paramVarArgs);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\restaction\CommandEditAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */