/*     */ package net.dv8tion.jda.api.interactions.commands.build;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
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
/*     */ public class SubcommandGroupData
/*     */   implements SerializableData
/*     */ {
/*  35 */   private final DataArray options = DataArray.empty();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String name;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String description;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SubcommandGroupData(@Nonnull String name, @Nonnull String description) {
/*  55 */     Checks.notEmpty(name, "Name");
/*  56 */     Checks.notEmpty(description, "Description");
/*  57 */     Checks.notLonger(name, 32, "Name");
/*  58 */     Checks.notLonger(description, 100, "Description");
/*  59 */     Checks.matches(name, Checks.ALPHANUMERIC_WITH_DASH, "Name");
/*  60 */     Checks.isLowercase(name, "Name");
/*  61 */     this.name = name;
/*  62 */     this.description = description;
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
/*     */   public SubcommandGroupData setName(@Nonnull String name) {
/*  79 */     Checks.notEmpty(name, "Name");
/*  80 */     Checks.notLonger(name, 32, "Name");
/*  81 */     Checks.isLowercase(name, "Name");
/*  82 */     Checks.matches(name, Checks.ALPHANUMERIC_WITH_DASH, "Name");
/*  83 */     this.name = name;
/*  84 */     return this;
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
/*     */   public SubcommandGroupData setDescription(@Nonnull String description) {
/* 101 */     Checks.notEmpty(description, "Description");
/* 102 */     Checks.notLonger(description, 100, "Description");
/* 103 */     this.description = description;
/* 104 */     return this;
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
/* 115 */     return this.name;
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
/* 126 */     return this.description;
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
/* 137 */     return (List<SubcommandData>)this.options.stream(DataArray::getObject)
/* 138 */       .map(SubcommandData::fromData)
/* 139 */       .collect(Collectors.toList());
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
/*     */   public SubcommandGroupData addSubcommands(@Nonnull SubcommandData... subcommands) {
/* 156 */     Checks.noneNull((Object[])subcommands, "Subcommand");
/* 157 */     Checks.check((subcommands.length + this.options.length() <= 25), "Cannot have more than 25 subcommands in one group!");
/* 158 */     for (SubcommandData subcommand : subcommands)
/* 159 */       this.options.add(subcommand); 
/* 160 */     return this;
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
/*     */   public SubcommandGroupData addSubcommands(@Nonnull Collection<? extends SubcommandData> subcommands) {
/* 177 */     Checks.noneNull(subcommands, "Subcommands");
/* 178 */     return addSubcommands(subcommands.<SubcommandData>toArray(new SubcommandData[0]));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/* 185 */     return DataObject.empty()
/* 186 */       .put("type", Integer.valueOf(OptionType.SUB_COMMAND_GROUP.getKey()))
/* 187 */       .put("name", this.name)
/* 188 */       .put("description", this.description)
/* 189 */       .put("options", this.options);
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
/*     */   public static SubcommandGroupData fromData(@Nonnull DataObject json) {
/* 209 */     String name = json.getString("name");
/* 210 */     String description = json.getString("description");
/* 211 */     SubcommandGroupData group = new SubcommandGroupData(name, description);
/* 212 */     json.optArray("options").ifPresent(arr -> {
/*     */           Objects.requireNonNull(group);
/*     */           
/*     */           arr.stream(DataArray::getObject).map(SubcommandData::fromData).forEach(());
/*     */         });
/* 217 */     return group;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\commands\build\SubcommandGroupData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */