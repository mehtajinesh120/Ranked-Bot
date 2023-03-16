/*     */ package net.dv8tion.jda.internal.interactions;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.interactions.components.Component;
/*     */ import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
/*     */ import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SelectionMenuImpl
/*     */   implements SelectionMenu
/*     */ {
/*     */   private final String id;
/*     */   private final String placeholder;
/*     */   private final int minValues;
/*     */   private final int maxValues;
/*     */   private final boolean disabled;
/*     */   private final List<SelectOption> options;
/*     */   
/*     */   public SelectionMenuImpl(DataObject data) {
/*  38 */     this(data
/*  39 */         .getString("custom_id"), data
/*  40 */         .getString("placeholder", null), data
/*  41 */         .getInt("min_values", 1), data
/*  42 */         .getInt("max_values", 1), data
/*  43 */         .getBoolean("disabled"), 
/*  44 */         parseOptions(data.getArray("options")));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SelectionMenuImpl(String id, String placeholder, int minValues, int maxValues, boolean disabled, List<SelectOption> options) {
/*  50 */     this.id = id;
/*  51 */     this.placeholder = placeholder;
/*  52 */     this.minValues = minValues;
/*  53 */     this.maxValues = maxValues;
/*  54 */     this.disabled = disabled;
/*  55 */     this.options = options;
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<SelectOption> parseOptions(DataArray array) {
/*  60 */     List<SelectOption> options = new ArrayList<>(array.length());
/*     */ 
/*     */     
/*  63 */     Objects.requireNonNull(options); array.stream(DataArray::getObject).map(SelectOption::fromData).forEach(options::add);
/*  64 */     return options;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Component.Type getType() {
/*  71 */     return Component.Type.SELECTION_MENU;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getId() {
/*  78 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getPlaceholder() {
/*  85 */     return this.placeholder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMinValues() {
/*  91 */     return this.minValues;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxValues() {
/*  97 */     return this.maxValues;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<SelectOption> getOptions() {
/* 104 */     return this.options;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisabled() {
/* 110 */     return this.disabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/* 117 */     DataObject data = DataObject.empty();
/* 118 */     data.put("type", Integer.valueOf(3));
/* 119 */     data.put("custom_id", this.id);
/* 120 */     data.put("min_values", Integer.valueOf(this.minValues));
/* 121 */     data.put("max_values", Integer.valueOf(this.maxValues));
/* 122 */     data.put("disabled", Boolean.valueOf(this.disabled));
/* 123 */     data.put("options", DataArray.fromCollection(this.options));
/* 124 */     if (this.placeholder != null)
/* 125 */       data.put("placeholder", this.placeholder); 
/* 126 */     return data;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\interactions\SelectionMenuImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */