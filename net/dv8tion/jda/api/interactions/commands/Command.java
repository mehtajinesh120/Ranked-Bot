/*     */ package net.dv8tion.jda.api.interactions.commands;
/*     */ 
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.interactions.commands.privileges.CommandPrivilege;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.CommandEditAction;
/*     */ import net.dv8tion.jda.api.utils.TimeUtil;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.api.utils.data.DataType;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.CommandEditActionImpl;
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
/*     */ public class Command
/*     */   implements ISnowflake
/*     */ {
/*  56 */   private static final EnumSet<OptionType> OPTIONS = EnumSet.complementOf(EnumSet.of(OptionType.SUB_COMMAND, OptionType.SUB_COMMAND_GROUP)); private static final Predicate<DataObject> OPTION_TEST; private static final Predicate<DataObject> SUBCOMMAND_TEST; private static final Predicate<DataObject> GROUP_TEST; static {
/*  57 */     OPTION_TEST = (it -> OPTIONS.contains(OptionType.fromKey(it.getInt("type"))));
/*  58 */     SUBCOMMAND_TEST = (it -> (OptionType.fromKey(it.getInt("type")) == OptionType.SUB_COMMAND));
/*  59 */     GROUP_TEST = (it -> (OptionType.fromKey(it.getInt("type")) == OptionType.SUB_COMMAND_GROUP));
/*     */   }
/*     */   private final JDAImpl api; private final Guild guild; private final String name; private final String description;
/*     */   private final List<Option> options;
/*     */   private final List<SubcommandGroup> groups;
/*     */   private final List<Subcommand> subcommands;
/*     */   private final long id;
/*     */   private final long guildId;
/*     */   private final long applicationId;
/*     */   private final long version;
/*     */   private final boolean defaultEnabled;
/*     */   
/*     */   public Command(JDAImpl api, Guild guild, DataObject json) {
/*  72 */     this.api = api;
/*  73 */     this.guild = guild;
/*  74 */     this.name = json.getString("name");
/*  75 */     this.description = json.getString("description");
/*  76 */     this.id = json.getUnsignedLong("id");
/*  77 */     this.defaultEnabled = json.getBoolean("default_permission");
/*  78 */     this.guildId = (guild != null) ? guild.getIdLong() : 0L;
/*  79 */     this.applicationId = json.getUnsignedLong("application_id", api.getSelfUser().getApplicationIdLong());
/*  80 */     this.options = parseOptions(json, OPTION_TEST, Option::new);
/*  81 */     this.groups = parseOptions(json, GROUP_TEST, SubcommandGroup::new);
/*  82 */     this.subcommands = parseOptions(json, SUBCOMMAND_TEST, Subcommand::new);
/*  83 */     this.version = json.getUnsignedLong("version", this.id);
/*     */   }
/*     */ 
/*     */   
/*     */   protected static <T> List<T> parseOptions(DataObject json, Predicate<DataObject> test, Function<DataObject, T> transform) {
/*  88 */     return json.optArray("options").map(arr -> (List)arr.stream(DataArray::getObject).filter(test).map(transform).collect(Collectors.toList()))
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  93 */       .orElse(Collections.emptyList());
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RestAction<Void> delete() {
/*     */     Route.CompiledRoute route;
/* 109 */     if (this.applicationId != this.api.getSelfUser().getApplicationIdLong()) {
/* 110 */       throw new IllegalStateException("Cannot delete a command from another bot!");
/*     */     }
/* 112 */     String appId = getJDA().getSelfUser().getApplicationId();
/* 113 */     if (this.guildId != 0L) {
/* 114 */       route = Route.Interactions.DELETE_GUILD_COMMAND.compile(new String[] { appId, Long.toUnsignedString(this.guildId), getId() });
/*     */     } else {
/* 116 */       route = Route.Interactions.DELETE_COMMAND.compile(new String[] { appId, getId() });
/* 117 */     }  return (RestAction<Void>)new RestActionImpl((JDA)this.api, route);
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
/*     */   @CheckReturnValue
/*     */   public CommandEditAction editCommand() {
/* 133 */     if (this.applicationId != this.api.getSelfUser().getApplicationIdLong())
/* 134 */       throw new IllegalStateException("Cannot edit a command from another bot!"); 
/* 135 */     return (this.guild == null) ? (CommandEditAction)new CommandEditActionImpl((JDA)this.api, getId()) : (CommandEditAction)new CommandEditActionImpl(this.guild, getId());
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
/*     */   @CheckReturnValue
/*     */   public RestAction<List<CommandPrivilege>> retrievePrivileges(@Nonnull Guild guild) {
/* 159 */     Checks.notNull(guild, "Guild");
/* 160 */     return guild.retrieveCommandPrivilegesById(this.id);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RestAction<List<CommandPrivilege>> updatePrivileges(@Nonnull Guild guild, @Nonnull Collection<? extends CommandPrivilege> privileges) {
/* 188 */     if (this.applicationId != this.api.getSelfUser().getApplicationIdLong())
/* 189 */       throw new IllegalStateException("Cannot update privileges for a command from another bot!"); 
/* 190 */     Checks.notNull(guild, "Guild");
/* 191 */     return guild.updateCommandPrivilegesById(this.id, privileges);
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RestAction<List<CommandPrivilege>> updatePrivileges(@Nonnull Guild guild, @Nonnull CommandPrivilege... privileges) {
/* 219 */     Checks.noneNull((Object[])privileges, "CommandPrivileges");
/* 220 */     return updatePrivileges(guild, Arrays.asList(privileges));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/* 231 */     return (JDA)this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/* 242 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getDescription() {
/* 253 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDefaultEnabled() {
/* 263 */     return this.defaultEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Option> getOptions() {
/* 274 */     return this.options;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Subcommand> getSubcommands() {
/* 285 */     return this.subcommands;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<SubcommandGroup> getSubcommandGroups() {
/* 296 */     return this.groups;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getApplicationIdLong() {
/* 306 */     return this.applicationId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getApplicationId() {
/* 317 */     return Long.toUnsignedString(this.applicationId);
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
/*     */   public long getVersion() {
/* 331 */     return this.version;
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
/*     */   @Nonnull
/*     */   public OffsetDateTime getTimeModified() {
/* 344 */     return TimeUtil.getTimeCreated(getVersion());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 350 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 356 */     return "C:" + getName() + "(" + getId() + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 362 */     if (obj == this)
/* 363 */       return true; 
/* 364 */     if (!(obj instanceof Command))
/* 365 */       return false; 
/* 366 */     return (this.id == ((Command)obj).id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 372 */     return Long.hashCode(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class Choice
/*     */   {
/*     */     private final String name;
/*     */ 
/*     */ 
/*     */     
/* 384 */     private long intValue = 0L;
/* 385 */     private double doubleValue = Double.NaN;
/* 386 */     private String stringValue = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Choice(@Nonnull String name, long value) {
/* 398 */       this.name = name;
/* 399 */       setIntValue(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Choice(@Nonnull String name, double value) {
/* 412 */       this.name = name;
/* 413 */       setDoubleValue(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Choice(@Nonnull String name, @Nonnull String value) {
/* 426 */       this.name = name;
/* 427 */       setStringValue(value);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Choice(@Nonnull DataObject json) {
/* 443 */       Checks.notNull(json, "DataObject");
/* 444 */       this.name = json.getString("name");
/* 445 */       if (json.isType("value", DataType.INT)) {
/*     */         
/* 447 */         setIntValue(json.getLong("value"));
/*     */       }
/* 449 */       else if (json.isType("value", DataType.FLOAT)) {
/*     */         
/* 451 */         setDoubleValue(json.getDouble("value"));
/*     */       }
/*     */       else {
/*     */         
/* 455 */         setStringValue(json.getString("value"));
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getName() {
/* 468 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public double getAsDouble() {
/* 478 */       return this.doubleValue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getAsLong() {
/* 488 */       return this.intValue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getAsString() {
/* 499 */       return this.stringValue;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 505 */       return Objects.hash(new Object[] { this.name, this.stringValue });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 511 */       if (obj == this) return true; 
/* 512 */       if (!(obj instanceof Choice)) return false; 
/* 513 */       Choice other = (Choice)obj;
/* 514 */       return (Objects.equals(other.name, this.name) && Objects.equals(other.stringValue, this.stringValue));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 520 */       return "Choice(" + this.name + "," + this.stringValue + ")";
/*     */     }
/*     */ 
/*     */     
/*     */     private void setIntValue(long value) {
/* 525 */       this.doubleValue = value;
/* 526 */       this.intValue = value;
/* 527 */       this.stringValue = Long.toString(value);
/*     */     }
/*     */ 
/*     */     
/*     */     private void setDoubleValue(double value) {
/* 532 */       this.doubleValue = value;
/* 533 */       this.intValue = (long)value;
/* 534 */       this.stringValue = Double.toString(value);
/*     */     }
/*     */ 
/*     */     
/*     */     private void setStringValue(@Nonnull String value) {
/* 539 */       this.doubleValue = Double.NaN;
/* 540 */       this.intValue = 0L;
/* 541 */       this.stringValue = value;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Option
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final String description;
/*     */     
/*     */     private final int type;
/*     */     private final boolean required;
/*     */     private final Set<ChannelType> channelTypes;
/*     */     private final List<Command.Choice> choices;
/*     */     private Number minValue;
/*     */     private Number maxValue;
/*     */     
/*     */     public Option(@Nonnull DataObject json) {
/* 560 */       this.name = json.getString("name");
/* 561 */       this.description = json.getString("description");
/* 562 */       this.type = json.getInt("type");
/* 563 */       this.required = json.getBoolean("required");
/* 564 */       this.channelTypes = Collections.unmodifiableSet(json.optArray("channel_types")
/* 565 */           .map(it -> (Set)it.stream(DataArray::getInt).map(ChannelType::fromId).collect(Collectors.toSet()))
/* 566 */           .orElse(Collections.emptySet()));
/* 567 */       this
/*     */         
/* 569 */         .choices = json.optArray("choices").map(it -> (List)it.stream(DataArray::getObject).map(Choice::new).collect(Collectors.toList())).orElse(Collections.emptyList());
/* 570 */       if (!json.isNull("min_value"))
/* 571 */         this.minValue = Double.valueOf(json.getDouble("min_value")); 
/* 572 */       if (!json.isNull("max_value")) {
/* 573 */         this.maxValue = Double.valueOf(json.getDouble("max_value"));
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getName() {
/* 584 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getDescription() {
/* 595 */       return this.description;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getTypeRaw() {
/* 605 */       return this.type;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isRequired() {
/* 615 */       return this.required;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public OptionType getType() {
/* 626 */       return OptionType.fromKey(this.type);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Set<ChannelType> getChannelTypes() {
/* 638 */       return this.channelTypes;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Number getMinValue() {
/* 651 */       return this.minValue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public Number getMaxValue() {
/* 664 */       return this.maxValue;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<Command.Choice> getChoices() {
/* 676 */       return this.choices;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 682 */       return Objects.hash(new Object[] { this.name, this.description, Integer.valueOf(this.type), this.choices, this.channelTypes, this.minValue, this.maxValue });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 688 */       if (obj == this) return true; 
/* 689 */       if (!(obj instanceof Option)) return false; 
/* 690 */       Option other = (Option)obj;
/* 691 */       return (Objects.equals(other.name, this.name) && 
/* 692 */         Objects.equals(other.description, this.description) && 
/* 693 */         Objects.equals(other.choices, this.choices) && 
/* 694 */         Objects.equals(other.channelTypes, this.channelTypes) && 
/* 695 */         Objects.equals(other.minValue, this.minValue) && 
/* 696 */         Objects.equals(other.maxValue, this.maxValue) && other.type == this.type);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 703 */       return "Option[" + getType() + "](" + this.name + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Subcommand
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final String description;
/*     */     
/*     */     private final List<Command.Option> options;
/*     */     
/*     */     public Subcommand(DataObject json) {
/* 717 */       this.name = json.getString("name");
/* 718 */       this.description = json.getString("description");
/* 719 */       this.options = Command.parseOptions(json, Command.OPTION_TEST, Option::new);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getName() {
/* 730 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getDescription() {
/* 741 */       return this.description;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<Command.Option> getOptions() {
/* 752 */       return this.options;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 758 */       return Objects.hash(new Object[] { this.name, this.description, this.options });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 764 */       if (obj == this) return true; 
/* 765 */       if (!(obj instanceof Subcommand)) return false; 
/* 766 */       Subcommand other = (Subcommand)obj;
/* 767 */       return (Objects.equals(other.name, this.name) && 
/* 768 */         Objects.equals(other.description, this.description) && 
/* 769 */         Objects.equals(other.options, this.options));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 775 */       return "Subcommand(" + this.name + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class SubcommandGroup
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final String description;
/*     */     
/*     */     private final List<Command.Subcommand> subcommands;
/*     */     
/*     */     public SubcommandGroup(DataObject json) {
/* 789 */       this.name = json.getString("name");
/* 790 */       this.description = json.getString("description");
/* 791 */       this.subcommands = Command.parseOptions(json, Command.SUBCOMMAND_TEST, Subcommand::new);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getName() {
/* 802 */       return this.name;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getDescription() {
/* 813 */       return this.description;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<Command.Subcommand> getSubcommands() {
/* 824 */       return this.subcommands;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 830 */       return Objects.hash(new Object[] { this.name, this.description, this.subcommands });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 836 */       if (obj == this) return true; 
/* 837 */       if (!(obj instanceof SubcommandGroup)) return false; 
/* 838 */       SubcommandGroup other = (SubcommandGroup)obj;
/* 839 */       return (Objects.equals(other.name, this.name) && 
/* 840 */         Objects.equals(other.description, this.description) && 
/* 841 */         Objects.equals(other.subcommands, this.subcommands));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 847 */       return "SubcommandGroup(" + this.name + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\commands\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */