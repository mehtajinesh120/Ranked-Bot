/*     */ package net.dv8tion.jda.api.interactions.commands.build;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.interactions.commands.OptionType;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.api.utils.data.SerializableData;
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
/*     */ public class SubcommandData
/*     */   extends BaseCommand<CommandData>
/*     */   implements SerializableData
/*     */ {
/*     */   private boolean allowRequired = true;
/*     */   
/*     */   public SubcommandData(@Nonnull String name, @Nonnull String description) {
/*  52 */     super(name, description);
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
/*     */   @Nonnull
/*     */   public SubcommandData addOptions(@Nonnull OptionData... options) {
/*  75 */     Checks.noneNull((Object[])options, "Option");
/*  76 */     Checks.check((options.length + this.options.length() <= 25), "Cannot have more than 25 options for a subcommand!");
/*  77 */     for (OptionData option : options) {
/*     */       
/*  79 */       Checks.check((option.getType() != OptionType.SUB_COMMAND), "Cannot add a subcommand to a subcommand!");
/*  80 */       Checks.check((option.getType() != OptionType.SUB_COMMAND_GROUP), "Cannot add a subcommand group to a subcommand!");
/*  81 */       Checks.check((this.allowRequired || !option.isRequired()), "Cannot add required options after non-required options!");
/*  82 */       this.allowRequired = option.isRequired();
/*  83 */       this.options.add(option);
/*     */     } 
/*  85 */     return this;
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
/*     */   @Nonnull
/*     */   public SubcommandData addOptions(@Nonnull Collection<? extends OptionData> options) {
/* 108 */     Checks.noneNull(options, "Options");
/* 109 */     return addOptions(options.<OptionData>toArray(new OptionData[0]));
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
/*     */   @Nonnull
/*     */   public SubcommandData addOption(@Nonnull OptionType type, @Nonnull String name, @Nonnull String description, boolean required) {
/* 138 */     return addOptions(new OptionData[] { (new OptionData(type, name, description)).setRequired(required) });
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
/*     */   @Nonnull
/*     */   public SubcommandData addOption(@Nonnull OptionType type, @Nonnull String name, @Nonnull String description) {
/* 166 */     return addOption(type, name, description, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/* 173 */     return super.toData().put("type", Integer.valueOf(OptionType.SUB_COMMAND.getKey()));
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
/*     */   @Nonnull
/*     */   public static SubcommandData fromData(@Nonnull DataObject json) {
/* 193 */     String name = json.getString("name");
/* 194 */     String description = json.getString("description");
/* 195 */     SubcommandData sub = new SubcommandData(name, description);
/* 196 */     json.optArray("options").ifPresent(arr -> {
/*     */           Objects.requireNonNull(sub);
/*     */           
/*     */           arr.stream(DataArray::getObject).map(OptionData::fromData).forEach(());
/*     */         });
/* 201 */     return sub;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\commands\build\SubcommandData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */