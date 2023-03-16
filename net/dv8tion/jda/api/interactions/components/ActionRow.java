/*     */ package net.dv8tion.jda.api.interactions.components;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.interactions.ButtonImpl;
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
/*     */ public class ActionRow
/*     */   implements ComponentLayout, Iterable<Component>
/*     */ {
/*  36 */   private final List<Component> components = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public static ActionRow fromData(@Nonnull DataObject data) {
/*  57 */     Checks.notNull(data, "Data");
/*  58 */     ActionRow row = new ActionRow();
/*  59 */     if (data.getInt("type", 0) != 1) {
/*  60 */       throw new IllegalArgumentException("Data has incorrect type. Expected: 1 Found: " + data.getInt("type"));
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
/*  75 */     Objects.requireNonNull(row.components); data.getArray("components").stream(DataArray::getObject).map(obj -> { switch (Component.Type.fromKey(obj.getInt("type"))) { case BUTTON: return (Component)new ButtonImpl(obj);case SELECTION_MENU: return (Component)new SelectionMenuImpl(obj); }  return null; }).filter(Objects::nonNull).forEach(row.components::add);
/*  76 */     return row;
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
/*     */   public static ActionRow of(@Nonnull Collection<? extends Component> components) {
/*  94 */     Checks.noneNull(components, "Components");
/*  95 */     return of(components.<Component>toArray(new Component[0]));
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
/*     */   public static ActionRow of(@Nonnull Component... components) {
/* 113 */     Checks.noneNull((Object[])components, "Components");
/* 114 */     Checks.check((components.length > 0), "Cannot have empty row!");
/* 115 */     ActionRow row = new ActionRow();
/* 116 */     Collections.addAll(row.components, components);
/* 117 */     if (!row.isValid()) {
/*     */       
/* 119 */       Map<Component.Type, List<Component>> grouped = (Map<Component.Type, List<Component>>)Arrays.<Component>stream(components).collect(Collectors.groupingBy(Component::getType));
/*     */ 
/*     */ 
/*     */       
/* 123 */       String provided = grouped.entrySet().stream().map(entry -> ((List)entry.getValue()).size() + "/" + ((Component.Type)entry.getKey()).getMaxPerRow() + " of " + entry.getKey()).collect(Collectors.joining(", "));
/* 124 */       throw new IllegalArgumentException("Cannot create action row with invalid component combinations. Provided: " + provided);
/*     */     } 
/* 126 */     return row;
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
/*     */   public List<Component> getComponents() {
/* 139 */     return this.components;
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
/*     */   public List<Button> getButtons() {
/* 153 */     Objects.requireNonNull(Button.class);
/* 154 */     Objects.requireNonNull(Button.class); return Collections.unmodifiableList((List<? extends Button>)getComponents().stream().filter(Button.class::isInstance).map(Button.class::cast)
/* 155 */         .collect(Collectors.toList()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ComponentLayout.Type getType() {
/* 162 */     return ComponentLayout.Type.ACTION_ROW;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/* 169 */     return DataObject.empty()
/* 170 */       .put("type", Integer.valueOf(1))
/* 171 */       .put("components", DataArray.fromCollection(this.components));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Iterator<Component> iterator() {
/* 178 */     return this.components.iterator();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\components\ActionRow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */