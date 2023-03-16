/*     */ package net.dv8tion.jda.api.interactions.commands.build;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
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
/*     */ 
/*     */ public class CommandData
/*     */   extends BaseCommand<CommandData>
/*     */   implements SerializableData
/*     */ {
/*     */   private boolean allowSubcommands = true;
/*     */   private boolean allowGroups = true;
/*     */   private boolean allowOption = true;
/*     */   private boolean defaultPermissions = true;
/*     */   private boolean allowRequired = true;
/*     */   
/*     */   public CommandData(@Nonnull String name, @Nonnull String description) {
/*  58 */     super(name, description);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/*  65 */     return super.toData().put("default_permission", Boolean.valueOf(this.defaultPermissions));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<SubcommandData> getSubcommands() {
/*  76 */     return (List<SubcommandData>)this.options.stream(DataArray::getObject)
/*  77 */       .filter(obj -> {
/*     */           OptionType type = OptionType.fromKey(obj.getInt("type"));
/*     */ 
/*     */           
/*     */           return (type == OptionType.SUB_COMMAND);
/*  82 */         }).map(SubcommandData::fromData)
/*  83 */       .collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<SubcommandGroupData> getSubcommandGroups() {
/*  94 */     return (List<SubcommandGroupData>)this.options.stream(DataArray::getObject)
/*  95 */       .filter(obj -> {
/*     */           OptionType type = OptionType.fromKey(obj.getInt("type"));
/*     */ 
/*     */           
/*     */           return (type == OptionType.SUB_COMMAND_GROUP);
/* 100 */         }).map(SubcommandGroupData::fromData)
/* 101 */       .collect(Collectors.toList());
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
/*     */   @Nonnull
/*     */   public CommandData setDefaultEnabled(boolean enabled) {
/* 116 */     this.defaultPermissions = enabled;
/* 117 */     return this;
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
/*     */   @Nonnull
/*     */   public CommandData addOptions(@Nonnull OptionData... options) {
/* 142 */     Checks.noneNull((Object[])options, "Option");
/* 143 */     Checks.check((options.length + this.options.length() <= 25), "Cannot have more than 25 options for a command!");
/* 144 */     Checks.check(this.allowOption, "You cannot mix options with subcommands/groups.");
/* 145 */     this.allowSubcommands = this.allowGroups = false;
/* 146 */     for (OptionData option : options) {
/*     */       
/* 148 */       Checks.check((option.getType() != OptionType.SUB_COMMAND), "Cannot add a subcommand with addOptions(...). Use addSubcommands(...) instead!");
/* 149 */       Checks.check((option.getType() != OptionType.SUB_COMMAND_GROUP), "Cannot add a subcommand group with addOptions(...). Use addSubcommandGroups(...) instead!");
/* 150 */       Checks.check((this.allowRequired || !option.isRequired()), "Cannot add required options after non-required options!");
/* 151 */       this.allowRequired = option.isRequired();
/* 152 */       this.options.add(option);
/*     */     } 
/* 154 */     return this;
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
/*     */   @Nonnull
/*     */   public CommandData addOptions(@Nonnull Collection<? extends OptionData> options) {
/* 179 */     Checks.noneNull(options, "Option");
/* 180 */     return addOptions(options.<OptionData>toArray(new OptionData[0]));
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
/*     */   @Nonnull
/*     */   public CommandData addOption(@Nonnull OptionType type, @Nonnull String name, @Nonnull String description, boolean required) {
/* 211 */     return addOptions(new OptionData[] { (new OptionData(type, name, description)).setRequired(required) });
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
/*     */   public CommandData addOption(@Nonnull OptionType type, @Nonnull String name, @Nonnull String description) {
/* 241 */     return addOption(type, name, description, false);
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
/*     */   @Nonnull
/*     */   public CommandData addSubcommands(@Nonnull SubcommandData... subcommands) {
/* 259 */     Checks.noneNull((Object[])subcommands, "Subcommands");
/* 260 */     if (!this.allowSubcommands)
/* 261 */       throw new IllegalArgumentException("You cannot mix options with subcommands/groups."); 
/* 262 */     this.allowOption = false;
/* 263 */     Checks.check((subcommands.length + this.options.length() <= 25), "Cannot have more than 25 subcommands for a command!");
/* 264 */     for (SubcommandData data : subcommands)
/* 265 */       this.options.add(data); 
/* 266 */     return this;
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
/*     */   @Nonnull
/*     */   public CommandData addSubcommands(@Nonnull Collection<? extends SubcommandData> subcommands) {
/* 284 */     Checks.noneNull(subcommands, "Subcommands");
/* 285 */     return addSubcommands(subcommands.<SubcommandData>toArray(new SubcommandData[0]));
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
/*     */   @Nonnull
/*     */   public CommandData addSubcommandGroups(@Nonnull SubcommandGroupData... groups) {
/* 303 */     Checks.noneNull((Object[])groups, "SubcommandGroups");
/* 304 */     if (!this.allowGroups)
/* 305 */       throw new IllegalArgumentException("You cannot mix options with subcommands/groups."); 
/* 306 */     this.allowOption = false;
/* 307 */     Checks.check((groups.length + this.options.length() <= 25), "Cannot have more than 25 subcommand groups for a command!");
/* 308 */     for (SubcommandGroupData data : groups)
/* 309 */       this.options.add(data); 
/* 310 */     return this;
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
/*     */   @Nonnull
/*     */   public CommandData addSubcommandGroups(@Nonnull Collection<? extends SubcommandGroupData> groups) {
/* 328 */     Checks.noneNull(groups, "SubcommandGroups");
/* 329 */     return addSubcommandGroups(groups.<SubcommandGroupData>toArray(new SubcommandGroupData[0]));
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
/*     */   public static CommandData fromData(@Nonnull DataObject object) {
/* 349 */     Checks.notNull(object, "DataObject");
/* 350 */     String name = object.getString("name");
/* 351 */     String description = object.getString("description");
/* 352 */     DataArray options = object.optArray("options").orElseGet(DataArray::empty);
/* 353 */     CommandData command = new CommandData(name, description);
/* 354 */     options.stream(DataArray::getObject).forEach(opt -> {
/*     */           OptionType type = OptionType.fromKey(opt.getInt("type"));
/*     */           switch (type) {
/*     */             case SUB_COMMAND:
/*     */               command.addSubcommands(new SubcommandData[] { SubcommandData.fromData(opt) });
/*     */               return;
/*     */ 
/*     */             
/*     */             case SUB_COMMAND_GROUP:
/*     */               command.addSubcommandGroups(new SubcommandGroupData[] { SubcommandGroupData.fromData(opt) });
/*     */               return;
/*     */           } 
/*     */           
/*     */           command.addOptions(new OptionData[] { OptionData.fromData(opt) });
/*     */         });
/* 369 */     return command;
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
/*     */   public static List<CommandData> fromList(@Nonnull DataArray array) {
/* 389 */     Checks.notNull(array, "DataArray");
/* 390 */     return (List<CommandData>)array.stream(DataArray::getObject)
/* 391 */       .map(CommandData::fromData)
/* 392 */       .collect(Collectors.toList());
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
/*     */   public static List<CommandData> fromList(@Nonnull Collection<? extends DataObject> collection) {
/* 412 */     Checks.noneNull(collection, "CommandData");
/* 413 */     return fromList(DataArray.fromCollection(collection));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\commands\build\CommandData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */