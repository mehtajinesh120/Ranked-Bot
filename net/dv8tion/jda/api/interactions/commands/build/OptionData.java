/*     */ package net.dv8tion.jda.api.interactions.commands.build;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.interactions.commands.Command;
/*     */ import net.dv8tion.jda.api.interactions.commands.OptionType;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.api.utils.data.DataType;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OptionData
/*     */   implements SerializableData
/*     */ {
/*     */   public static final double MAX_POSITIVE_NUMBER = 9.007199254740991E15D;
/*     */   public static final double MIN_NEGATIVE_NUMBER = -9.007199254740991E15D;
/*     */   public static final int MAX_NAME_LENGTH = 32;
/*     */   public static final int MAX_CHOICE_NAME_LENGTH = 100;
/*     */   public static final int MAX_DESCRIPTION_LENGTH = 100;
/*     */   public static final int MAX_CHOICE_VALUE_LENGTH = 100;
/*     */   public static final int MAX_CHOICES = 25;
/*     */   private final OptionType type;
/*     */   private String name;
/*     */   private String description;
/*     */   private boolean isRequired;
/*  76 */   private final EnumSet<ChannelType> channelTypes = EnumSet.noneOf(ChannelType.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Number minValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Number maxValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, Object> choices;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OptionData(@Nonnull OptionType type, @Nonnull String name, @Nonnull String description) {
/* 103 */     this(type, name, description, false);
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
/*     */   public OptionData(@Nonnull OptionType type, @Nonnull String name, @Nonnull String description, boolean isRequired) {
/* 129 */     Checks.notNull(type, "Type");
/* 130 */     this.type = type;
/*     */     
/* 132 */     setName(name);
/* 133 */     setDescription(description);
/* 134 */     setRequired(isRequired);
/* 135 */     if (type.canSupportChoices()) {
/* 136 */       this.choices = new LinkedHashMap<>();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OptionType getType() {
/* 147 */     return this.type;
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
/* 158 */     return this.name;
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
/* 169 */     return this.description;
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
/*     */   public boolean isRequired() {
/* 182 */     return this.isRequired;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<ChannelType> getChannelTypes() {
/* 194 */     return this.channelTypes;
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
/*     */   @Nullable
/*     */   public Number getMinValue() {
/* 207 */     return this.minValue;
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
/*     */   @Nullable
/*     */   public Number getMaxValue() {
/* 220 */     return this.maxValue;
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
/*     */   @Nonnull
/*     */   public List<Command.Choice> getChoices() {
/* 236 */     if (this.choices == null || this.choices.isEmpty())
/* 237 */       return Collections.emptyList(); 
/* 238 */     return (List<Command.Choice>)this.choices.entrySet().stream()
/* 239 */       .map(entry -> (entry.getValue() instanceof String) ? new Command.Choice((String)entry.getKey(), entry.getValue().toString()) : ((entry.getValue() instanceof Double) ? new Command.Choice((String)entry.getKey(), ((Number)entry.getValue()).doubleValue()) : new Command.Choice((String)entry.getKey(), ((Number)entry.getValue()).longValue())))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 247 */       .collect(Collectors.toList());
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
/*     */   public OptionData setName(@Nonnull String name) {
/* 265 */     Checks.notEmpty(name, "Name");
/* 266 */     Checks.notLonger(name, 32, "Name");
/* 267 */     Checks.isLowercase(name, "Name");
/* 268 */     Checks.matches(name, Checks.ALPHANUMERIC_WITH_DASH, "Name");
/* 269 */     this.name = name;
/* 270 */     return this;
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
/*     */   @Nonnull
/*     */   public OptionData setDescription(@Nonnull String description) {
/* 287 */     Checks.notEmpty(description, "Description");
/* 288 */     Checks.notLonger(description, 100, "Description");
/* 289 */     this.description = description;
/* 290 */     return this;
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
/*     */   public OptionData setRequired(boolean required) {
/* 305 */     this.isRequired = required;
/* 306 */     return this;
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
/*     */   @Nonnull
/*     */   public OptionData setChannelTypes(@Nonnull ChannelType... channelTypes) {
/* 330 */     Checks.noneNull((Object[])channelTypes, "ChannelTypes");
/* 331 */     return setChannelTypes(Arrays.asList(channelTypes));
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
/*     */   public OptionData setChannelTypes(@Nonnull Collection<ChannelType> channelTypes) {
/* 356 */     if (this.type != OptionType.CHANNEL)
/* 357 */       throw new IllegalArgumentException("Can only apply channel type restriction to options of type CHANNEL"); 
/* 358 */     Checks.notNull(channelTypes, "ChannelType collection");
/* 359 */     Checks.noneNull(channelTypes, "ChannelType");
/*     */     
/* 361 */     for (ChannelType channelType : channelTypes) {
/*     */       
/* 363 */       if (!channelType.isGuild())
/* 364 */         throw new IllegalArgumentException("Provided channel type is not a guild channel type. Provided: " + channelType); 
/*     */     } 
/* 366 */     this.channelTypes.clear();
/* 367 */     this.channelTypes.addAll(channelTypes);
/* 368 */     return this;
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
/*     */   public OptionData setMinValue(long value) {
/* 388 */     if (this.type != OptionType.INTEGER && this.type != OptionType.NUMBER)
/* 389 */       throw new IllegalArgumentException("Can only set min and max long value for options of type INTEGER or NUMBER"); 
/* 390 */     Checks.check((value >= -9.007199254740991E15D), "Long value may not be lower than %f", Double.valueOf(-9.007199254740991E15D));
/* 391 */     this.minValue = Long.valueOf(value);
/* 392 */     return this;
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
/*     */   public OptionData setMinValue(double value) {
/* 412 */     if (this.type != OptionType.NUMBER)
/* 413 */       throw new IllegalArgumentException("Can only set min double value for options of type NUMBER"); 
/* 414 */     Checks.check((value >= -9.007199254740991E15D), "Double value may not be lower than %f", Double.valueOf(-9.007199254740991E15D));
/* 415 */     this.minValue = Double.valueOf(value);
/* 416 */     return this;
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
/*     */   public OptionData setMaxValue(long value) {
/* 436 */     if (this.type != OptionType.INTEGER && this.type != OptionType.NUMBER)
/* 437 */       throw new IllegalArgumentException("Can only set min and max long value for options of type INTEGER or NUMBER"); 
/* 438 */     Checks.check((value <= 9.007199254740991E15D), "Long value may not be larger than %f", Double.valueOf(9.007199254740991E15D));
/* 439 */     this.maxValue = Long.valueOf(value);
/* 440 */     return this;
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
/*     */   public OptionData setMaxValue(double value) {
/* 460 */     if (this.type != OptionType.NUMBER)
/* 461 */       throw new IllegalArgumentException("Can only set max double value for options of type NUMBER"); 
/* 462 */     Checks.check((value <= 9.007199254740991E15D), "Double value may not be larger than %f", Double.valueOf(9.007199254740991E15D));
/* 463 */     this.maxValue = Double.valueOf(value);
/* 464 */     return this;
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
/*     */   public OptionData setRequiredRange(long minValue, long maxValue) {
/* 487 */     if (this.type != OptionType.INTEGER && this.type != OptionType.NUMBER)
/* 488 */       throw new IllegalArgumentException("Can only set min and max long value for options of type INTEGER or NUMBER"); 
/* 489 */     Checks.check((minValue >= -9.007199254740991E15D), "Long value may not be lower than %f", Double.valueOf(-9.007199254740991E15D));
/* 490 */     Checks.check((maxValue <= 9.007199254740991E15D), "Long value may not be larger than %f", Double.valueOf(9.007199254740991E15D));
/* 491 */     this.minValue = Long.valueOf(minValue);
/* 492 */     this.maxValue = Long.valueOf(maxValue);
/* 493 */     return this;
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
/*     */   public OptionData setRequiredRange(double minValue, double maxValue) {
/* 516 */     if (this.type != OptionType.NUMBER)
/* 517 */       throw new IllegalArgumentException("Can only set min and max double value for options of type NUMBER"); 
/* 518 */     Checks.check((minValue >= -9.007199254740991E15D), "Double value may not be lower than %f", Double.valueOf(-9.007199254740991E15D));
/* 519 */     Checks.check((maxValue <= 9.007199254740991E15D), "Double value may not be larger than %f", Double.valueOf(9.007199254740991E15D));
/* 520 */     this.minValue = Double.valueOf(minValue);
/* 521 */     this.maxValue = Double.valueOf(maxValue);
/* 522 */     return this;
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
/*     */   public OptionData addChoice(@Nonnull String name, double value) {
/* 549 */     Checks.notEmpty(name, "Name");
/* 550 */     Checks.notLonger(name, 100, "Name");
/* 551 */     Checks.check((value >= -9.007199254740991E15D), "Double value may not be lower than %f", Double.valueOf(-9.007199254740991E15D));
/* 552 */     Checks.check((value <= 9.007199254740991E15D), "Double value may not be larger than %f", Double.valueOf(9.007199254740991E15D));
/* 553 */     Checks.check((this.choices.size() < 25), "Cannot have more than 25 choices for an option!");
/* 554 */     if (this.type != OptionType.NUMBER)
/* 555 */       throw new IllegalArgumentException("Cannot add double choice for OptionType." + this.type); 
/* 556 */     this.choices.put(name, Double.valueOf(value));
/* 557 */     return this;
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
/*     */   public OptionData addChoice(@Nonnull String name, int value) {
/* 582 */     Checks.notEmpty(name, "Name");
/* 583 */     Checks.notLonger(name, 100, "Name");
/* 584 */     Checks.check((this.choices.size() < 25), "Cannot have more than 25 choices for an option!");
/* 585 */     if (this.type != OptionType.INTEGER)
/* 586 */       throw new IllegalArgumentException("Cannot add int choice for OptionType." + this.type); 
/* 587 */     this.choices.put(name, Integer.valueOf(value));
/* 588 */     return this;
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
/*     */   @Nonnull
/*     */   public OptionData addChoice(@Nonnull String name, long value) {
/* 614 */     Checks.notEmpty(name, "Name");
/* 615 */     Checks.notLonger(name, 32, "Name");
/* 616 */     Checks.check((value >= -9.007199254740991E15D), "Long value may not be lower than %f", Double.valueOf(-9.007199254740991E15D));
/* 617 */     Checks.check((value <= 9.007199254740991E15D), "Long value may not be larger than %f", Double.valueOf(9.007199254740991E15D));
/* 618 */     Checks.check((this.choices.size() < 25), "Cannot have more than 25 choices for an option!");
/* 619 */     if (this.type != OptionType.INTEGER)
/* 620 */       throw new IllegalArgumentException("Cannot add long choice for OptionType." + this.type); 
/* 621 */     this.choices.put(name, Long.valueOf(value));
/* 622 */     return this;
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
/*     */   @Nonnull
/*     */   public OptionData addChoice(@Nonnull String name, @Nonnull String value) {
/* 648 */     Checks.notEmpty(name, "Name");
/* 649 */     Checks.notEmpty(value, "Value");
/* 650 */     Checks.notLonger(name, 100, "Name");
/* 651 */     Checks.notLonger(value, 100, "Value");
/* 652 */     Checks.check((this.choices.size() < 25), "Cannot have more than 25 choices for an option!");
/* 653 */     if (this.type != OptionType.STRING)
/* 654 */       throw new IllegalArgumentException("Cannot add string choice for OptionType." + this.type); 
/* 655 */     this.choices.put(name, value);
/* 656 */     return this;
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
/*     */   @Nonnull
/*     */   public OptionData addChoices(@Nonnull Command.Choice... choices) {
/* 680 */     if (this.choices == null)
/* 681 */       throw new IllegalStateException("Cannot add choices for an option of type " + this.type); 
/* 682 */     Checks.noneNull((Object[])choices, "Choices");
/* 683 */     Checks.check((choices.length + this.choices.size() <= 25), "Cannot have more than 25 choices for one option!");
/* 684 */     for (Command.Choice choice : choices) {
/*     */       
/* 686 */       if (this.type == OptionType.INTEGER) {
/* 687 */         addChoice(choice.getName(), choice.getAsLong());
/* 688 */       } else if (this.type == OptionType.STRING) {
/* 689 */         addChoice(choice.getName(), choice.getAsString());
/* 690 */       } else if (this.type == OptionType.NUMBER) {
/* 691 */         addChoice(choice.getName(), choice.getAsDouble());
/*     */       } else {
/* 693 */         throw new IllegalArgumentException("Cannot add choice for type " + this.type);
/*     */       } 
/* 695 */     }  return this;
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
/*     */   @Nonnull
/*     */   public OptionData addChoices(@Nonnull Collection<? extends Command.Choice> choices) {
/* 719 */     Checks.noneNull(choices, "Choices");
/* 720 */     return addChoices(choices.<Command.Choice>toArray(new Command.Choice[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/* 730 */     DataObject json = DataObject.empty().put("type", Integer.valueOf(this.type.getKey())).put("name", this.name).put("description", this.description);
/* 731 */     if (this.type != OptionType.SUB_COMMAND && this.type != OptionType.SUB_COMMAND_GROUP)
/* 732 */       json.put("required", Boolean.valueOf(this.isRequired)); 
/* 733 */     if (this.choices != null && !this.choices.isEmpty())
/*     */     {
/* 735 */       json.put("choices", DataArray.fromCollection((Collection)this.choices.entrySet()
/* 736 */             .stream()
/* 737 */             .map(entry -> DataObject.empty().put("name", entry.getKey()).put("value", entry.getValue()))
/* 738 */             .collect(Collectors.toList())));
/*     */     }
/* 740 */     if (this.type == OptionType.CHANNEL && !this.channelTypes.isEmpty())
/* 741 */       json.put("channel_types", this.channelTypes.stream().map(ChannelType::getId).collect(Collectors.toList())); 
/* 742 */     if (this.type == OptionType.INTEGER || this.type == OptionType.NUMBER) {
/*     */       
/* 744 */       if (this.minValue != null)
/* 745 */         json.put("min_value", this.minValue); 
/* 746 */       if (this.maxValue != null)
/* 747 */         json.put("max_value", this.maxValue); 
/*     */     } 
/* 749 */     return json;
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
/*     */   public static OptionData fromData(@Nonnull DataObject json) {
/* 769 */     String name = json.getString("name");
/* 770 */     String description = json.getString("description");
/* 771 */     OptionType type = OptionType.fromKey(json.getInt("type"));
/* 772 */     OptionData option = new OptionData(type, name, description);
/* 773 */     option.setRequired(json.getBoolean("required"));
/* 774 */     if (type == OptionType.INTEGER || type == OptionType.NUMBER) {
/*     */       
/* 776 */       if (!json.isNull("min_value"))
/*     */       {
/* 778 */         if (json.isType("min_value", DataType.INT)) {
/* 779 */           option.setMinValue(json.getLong("min_value"));
/* 780 */         } else if (json.isType("min_value", DataType.FLOAT)) {
/* 781 */           option.setMinValue(json.getDouble("min_value"));
/*     */         }  } 
/* 783 */       if (!json.isNull("max_value"))
/*     */       {
/* 785 */         if (json.isType("max_value", DataType.INT)) {
/* 786 */           option.setMaxValue(json.getLong("max_value"));
/* 787 */         } else if (json.isType("max_value", DataType.FLOAT)) {
/* 788 */           option.setMaxValue(json.getDouble("max_value"));
/*     */         }  } 
/*     */     } 
/* 791 */     if (type == OptionType.CHANNEL)
/*     */     {
/* 793 */       option.setChannelTypes(json.optArray("channel_types")
/* 794 */           .map(it -> (Set)it.stream(DataArray::getInt).map(ChannelType::fromId).collect(Collectors.toSet()))
/* 795 */           .orElse(Collections.emptySet()));
/*     */     }
/* 797 */     json.optArray("choices").ifPresent(choices1 -> choices1.stream(DataArray::getObject).forEach(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 808 */     return option;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\commands\build\OptionData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */