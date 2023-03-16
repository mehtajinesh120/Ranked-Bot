/*     */ package net.dv8tion.jda.api.interactions.commands.build;
/*     */ 
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
/*     */ public abstract class BaseCommand<T extends BaseCommand<T>>
/*     */   implements SerializableData
/*     */ {
/*  31 */   protected final DataArray options = DataArray.empty();
/*     */   protected String name;
/*     */   protected String description;
/*     */   
/*     */   public BaseCommand(@Nonnull String name, @Nonnull String description) {
/*  36 */     Checks.notEmpty(name, "Name");
/*  37 */     Checks.notEmpty(description, "Description");
/*  38 */     Checks.notLonger(name, 32, "Name");
/*  39 */     Checks.notLonger(description, 100, "Description");
/*  40 */     Checks.matches(name, Checks.ALPHANUMERIC_WITH_DASH, "Name");
/*  41 */     Checks.isLowercase(name, "Name");
/*  42 */     this.name = name;
/*  43 */     this.description = description;
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
/*     */   public T setName(@Nonnull String name) {
/*  61 */     Checks.notEmpty(name, "Name");
/*  62 */     Checks.notLonger(name, 32, "Name");
/*  63 */     Checks.isLowercase(name, "Name");
/*  64 */     Checks.matches(name, Checks.ALPHANUMERIC_WITH_DASH, "Name");
/*  65 */     this.name = name;
/*  66 */     return (T)this;
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
/*     */   public T setDescription(@Nonnull String description) {
/*  84 */     Checks.notEmpty(description, "Description");
/*  85 */     Checks.notLonger(description, 100, "Description");
/*  86 */     this.description = description;
/*  87 */     return (T)this;
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
/*  98 */     return this.name;
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
/* 109 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<OptionData> getOptions() {
/* 120 */     return (List<OptionData>)this.options.stream(DataArray::getObject)
/* 121 */       .map(OptionData::fromData)
/* 122 */       .filter(it -> (it.getType().getKey() > OptionType.SUB_COMMAND_GROUP.getKey()))
/* 123 */       .collect(Collectors.toList());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/* 130 */     return DataObject.empty()
/* 131 */       .put("name", this.name)
/* 132 */       .put("description", this.description)
/* 133 */       .put("options", this.options);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\commands\build\BaseCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */