/*     */ package net.dv8tion.jda.api.interactions.components.selections;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.entities.Message;
/*     */ import net.dv8tion.jda.api.interactions.InteractionHook;
/*     */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*     */ import net.dv8tion.jda.api.interactions.components.Component;
/*     */ import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
/*     */ import net.dv8tion.jda.api.interactions.components.ComponentLayout;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface SelectionMenuInteraction
/*     */   extends ComponentInteraction
/*     */ {
/*     */   @Nullable
/*     */   default SelectionMenu getSelectionMenu() {
/*  55 */     return getComponent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default List<SelectOption> getSelectedOptions() {
/*  67 */     SelectionMenu menu = getComponent();
/*  68 */     if (menu == null) {
/*  69 */       return null;
/*     */     }
/*  71 */     List<String> values = getValues();
/*  72 */     return (List<SelectOption>)menu.getOptions()
/*  73 */       .stream()
/*  74 */       .filter(it -> values.contains(it.getValue()))
/*  75 */       .collect(Collectors.toList());
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
/*     */   @CheckReturnValue
/*     */   default RestAction<Void> editSelectionMenu(@Nullable SelectionMenu newMenu) {
/* 107 */     Message message = getMessage();
/* 108 */     List<ActionRow> components = new ArrayList<>(message.getActionRows());
/* 109 */     ComponentLayout.updateComponent(components, getComponentId(), newMenu);
/*     */     
/* 111 */     if (isAcknowledged()) {
/* 112 */       return getHook().editMessageComponentsById(message.getId(), components).map(it -> null);
/*     */     }
/* 114 */     return editComponents(components).map(it -> null);
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   SelectionMenu getComponent();
/*     */   
/*     */   @Nonnull
/*     */   List<String> getValues();
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\components\selections\SelectionMenuInteraction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */