/*     */ package net.dv8tion.jda.api.interactions.components.selections;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Emoji;
/*     */ import net.dv8tion.jda.api.interactions.components.Component;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.interactions.SelectionMenuImpl;
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
/*     */ public interface SelectionMenu
/*     */   extends Component
/*     */ {
/*     */   @Nullable
/*     */   String getPlaceholder();
/*     */   
/*     */   int getMinValues();
/*     */   
/*     */   int getMaxValues();
/*     */   
/*     */   @Nonnull
/*     */   List<SelectOption> getOptions();
/*     */   
/*     */   boolean isDisabled();
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   default SelectionMenu asDisabled() {
/* 112 */     return withDisabled(true);
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
/*     */   @CheckReturnValue
/*     */   default SelectionMenu asEnabled() {
/* 126 */     return withDisabled(false);
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
/*     */   @CheckReturnValue
/*     */   default SelectionMenu withDisabled(boolean disabled) {
/* 143 */     return createCopy().setDisabled(disabled).build();
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
/*     */   @CheckReturnValue
/*     */   default Builder createCopy() {
/* 157 */     Builder builder = create(getId());
/* 158 */     builder.setRequiredRange(getMinValues(), getMaxValues());
/* 159 */     builder.setPlaceholder(getPlaceholder());
/* 160 */     builder.addOptions(getOptions());
/* 161 */     builder.setDisabled(isDisabled());
/* 162 */     return builder;
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
/*     */   @CheckReturnValue
/*     */   static Builder create(@Nonnull String customId) {
/* 180 */     return new Builder(customId);
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
/*     */   @CheckReturnValue
/*     */   static Builder fromData(@Nonnull DataObject data) {
/* 201 */     return (new SelectionMenuImpl(data)).createCopy();
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private String customId;
/*     */     
/*     */     private String placeholder;
/*     */     
/* 211 */     private int minValues = 1; private int maxValues = 1;
/*     */     private boolean disabled = false;
/* 213 */     private final List<SelectOption> options = new ArrayList<>();
/*     */ 
/*     */     
/*     */     protected Builder(@Nonnull String customId) {
/* 217 */       setId(customId);
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
/*     */     @Nonnull
/*     */     public Builder setId(@Nonnull String customId) {
/* 234 */       Checks.notEmpty(customId, "Component ID");
/* 235 */       Checks.notLonger(customId, 100, "Component ID");
/* 236 */       this.customId = customId;
/* 237 */       return this;
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
/*     */     @Nonnull
/*     */     public Builder setPlaceholder(@Nullable String placeholder) {
/* 254 */       if (placeholder != null) {
/*     */         
/* 256 */         Checks.notEmpty(placeholder, "Placeholder");
/* 257 */         Checks.notLonger(placeholder, 100, "Placeholder");
/*     */       } 
/* 259 */       this.placeholder = placeholder;
/* 260 */       return this;
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
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Builder setMinValues(int minValues) {
/* 280 */       Checks.notNegative(minValues, "Min Values");
/* 281 */       Checks.check((minValues <= 25), "Min Values may not be greater than 25! Provided: %d", Integer.valueOf(minValues));
/* 282 */       this.minValues = minValues;
/* 283 */       return this;
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
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Builder setMaxValues(int maxValues) {
/* 303 */       Checks.positive(maxValues, "Max Values");
/* 304 */       Checks.check((maxValues <= 25), "Min Values may not be greater than 25! Provided: %d", Integer.valueOf(maxValues));
/* 305 */       this.maxValues = maxValues;
/* 306 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Builder setRequiredRange(int min, int max) {
/* 328 */       Checks.check((min <= max), "Min Values should be less than or equal to Max Values! Provided: [%d, %d]", new Object[] { Integer.valueOf(min), Integer.valueOf(max) });
/* 329 */       return setMinValues(min).setMaxValues(max);
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
/*     */     @Nonnull
/*     */     public Builder setDisabled(boolean disabled) {
/* 344 */       this.disabled = disabled;
/* 345 */       return this;
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
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Builder addOptions(@Nonnull SelectOption... options) {
/* 364 */       Checks.noneNull((Object[])options, "Options");
/* 365 */       Checks.check((this.options.size() + options.length <= 25), "Cannot have more than 25 options for a selection menu!");
/* 366 */       Collections.addAll(this.options, options);
/* 367 */       return this;
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
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Builder addOptions(@Nonnull Collection<? extends SelectOption> options) {
/* 386 */       Checks.noneNull(options, "Options");
/* 387 */       Checks.check((this.options.size() + options.size() <= 25), "Cannot have more than 25 options for a selection menu!");
/* 388 */       this.options.addAll(options);
/* 389 */       return this;
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
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Builder addOption(@Nonnull String label, @Nonnull String value) {
/* 408 */       return addOptions(new SelectOption[] { new SelectOption(label, value) });
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Builder addOption(@Nonnull String label, @Nonnull String value, @Nonnull Emoji emoji) {
/* 429 */       return addOption(label, value, null, emoji);
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
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Builder addOption(@Nonnull String label, @Nonnull String value, @Nonnull String description) {
/* 450 */       return addOption(label, value, description, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public Builder addOption(@Nonnull String label, @Nonnull String value, @Nullable String description, @Nullable Emoji emoji) {
/* 473 */       return addOptions(new SelectOption[] { new SelectOption(label, value, description, false, emoji) });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<SelectOption> getOptions() {
/* 484 */       return this.options;
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
/*     */     @Nonnull
/*     */     public Builder setDefaultValues(@Nonnull Collection<String> values) {
/* 501 */       Checks.noneNull(values, "Values");
/* 502 */       Set<String> set = new HashSet<>(values);
/* 503 */       for (ListIterator<SelectOption> it = getOptions().listIterator(); it.hasNext(); ) {
/*     */         
/* 505 */         SelectOption option = it.next();
/* 506 */         it.set(option.withDefault(set.contains(option.getValue())));
/*     */       } 
/* 508 */       return this;
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
/*     */     
/*     */     @Nonnull
/*     */     public Builder setDefaultOptions(@Nonnull Collection<? extends SelectOption> values) {
/* 526 */       Checks.noneNull(values, "Values");
/* 527 */       return setDefaultValues((Collection<String>)values.stream().map(SelectOption::getValue).collect(Collectors.toSet()));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getId() {
/* 538 */       return this.customId;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getPlaceholder() {
/* 549 */       return this.placeholder;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getMinValues() {
/* 559 */       return this.minValues;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getMaxValues() {
/* 569 */       return this.maxValues;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean isDisabled() {
/* 579 */       return this.disabled;
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
/*     */     
/*     */     @Nonnull
/*     */     public SelectionMenu build() {
/* 597 */       Checks.check((this.minValues <= this.maxValues), "Min values cannot be greater than max values!");
/* 598 */       Checks.check((this.options.size() <= 25), "Cannot build a selection menu with more than 25 options.");
/* 599 */       int min = Math.min(this.minValues, this.options.size());
/* 600 */       int max = Math.min(this.maxValues, this.options.size());
/* 601 */       return (SelectionMenu)new SelectionMenuImpl(this.customId, this.placeholder, min, max, this.disabled, this.options);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\components\selections\SelectionMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */