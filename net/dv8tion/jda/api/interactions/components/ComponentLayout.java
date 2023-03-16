/*     */ package net.dv8tion.jda.api.interactions.components;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface ComponentLayout
/*     */   extends SerializableData, Iterable<Component>
/*     */ {
/*     */   @Nonnull
/*     */   List<Component> getComponents();
/*     */   
/*     */   @Nonnull
/*     */   List<Button> getButtons();
/*     */   
/*     */   @Nonnull
/*     */   Type getType();
/*     */   
/*     */   default boolean isEmpty() {
/*  69 */     return getComponents().isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default boolean isValid() {
/*  80 */     if (isEmpty())
/*  81 */       return false; 
/*  82 */     List<Component> components = getComponents();
/*  83 */     Map<Component.Type, List<Component>> groups = (Map<Component.Type, List<Component>>)components.stream().collect(Collectors.groupingBy(Component::getType));
/*  84 */     if (groups.size() > 1) {
/*  85 */       return false;
/*     */     }
/*  87 */     for (Map.Entry<Component.Type, List<Component>> entry : groups.entrySet()) {
/*     */       
/*  89 */       Component.Type type = entry.getKey();
/*  90 */       List<Component> list = entry.getValue();
/*  91 */       if (list.size() > type.getMaxPerRow()) {
/*  92 */         return false;
/*     */       }
/*     */     } 
/*  95 */     return true;
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
/*     */   @Nullable
/*     */   default Component updateComponent(@Nonnull String id, @Nullable Component newComponent) {
/* 115 */     Checks.notNull(id, "ID");
/* 116 */     List<Component> list = getComponents();
/* 117 */     for (ListIterator<Component> it = list.listIterator(); it.hasNext(); ) {
/*     */       
/* 119 */       Component component = it.next();
/* 120 */       if (id.equals(component.getId()) || (component instanceof Button && id.equals(((Button)component).getUrl()))) {
/*     */         
/* 122 */         if (newComponent == null) {
/* 123 */           it.remove();
/*     */         } else {
/* 125 */           it.set(newComponent);
/* 126 */         }  return component;
/*     */       } 
/*     */     } 
/* 129 */     return null;
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
/*     */   static boolean updateComponent(@Nonnull List<? extends ComponentLayout> layouts, @Nonnull String id, @Nullable Component newComponent) {
/* 155 */     Checks.notNull(layouts, "ComponentLayout");
/* 156 */     Checks.notEmpty(id, "ID or URL");
/* 157 */     for (Iterator<? extends ComponentLayout> it = layouts.iterator(); it.hasNext(); ) {
/*     */       
/* 159 */       ComponentLayout components = it.next();
/* 160 */       Component oldComponent = components.updateComponent(id, newComponent);
/* 161 */       if (oldComponent != null) {
/*     */         
/* 163 */         if (components.getComponents().isEmpty()) {
/* 164 */           it.remove();
/* 165 */         } else if (!components.isValid() && newComponent != null) {
/* 166 */           throw new IllegalArgumentException("Cannot replace " + oldComponent.getType() + " with " + newComponent.getType() + " due to a violation of the layout maximum. The resulting ComponentLayout is invalid!");
/* 167 */         }  return !Objects.equals(oldComponent, newComponent);
/*     */       } 
/*     */     } 
/* 170 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/* 178 */     UNKNOWN(-1),
/* 179 */     ACTION_ROW(1);
/*     */     
/*     */     private final int key;
/*     */ 
/*     */     
/*     */     Type(int key) {
/* 185 */       this.key = key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getKey() {
/* 195 */       return this.key;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\components\ComponentLayout.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */