/*    */ package net.dv8tion.jda.internal.interactions;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.interactions.components.ActionRow;
/*    */ import net.dv8tion.jda.api.interactions.components.Component;
/*    */ import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
/*    */ import net.dv8tion.jda.api.interactions.components.selections.SelectionMenuInteraction;
/*    */ import net.dv8tion.jda.api.utils.data.DataArray;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SelectionMenuInteractionImpl
/*    */   extends ComponentInteractionImpl
/*    */   implements SelectionMenuInteraction
/*    */ {
/*    */   private final List<String> values;
/*    */   private final SelectionMenu menu;
/*    */   
/*    */   public SelectionMenuInteractionImpl(JDAImpl jda, DataObject data) {
/* 39 */     super(jda, data);
/* 40 */     this.values = Collections.unmodifiableList((List<? extends String>)data.getObject("data").getArray("values")
/* 41 */         .stream(DataArray::getString)
/* 42 */         .collect(Collectors.toList()));
/* 43 */     if (this.message != null) {
/*    */       
/* 45 */       this
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 50 */         .menu = this.message.getActionRows().stream().flatMap(row -> row.getComponents().stream()).filter(c -> (c.getType() == Component.Type.SELECTION_MENU && this.customId.equals(c.getId()))).findFirst().orElse(null);
/*    */     }
/*    */     else {
/*    */       
/* 54 */       this.menu = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public SelectionMenu getComponent() {
/* 62 */     return this.menu;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Component.Type getComponentType() {
/* 69 */     return Component.Type.SELECTION_MENU;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<String> getValues() {
/* 76 */     return this.values;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\interactions\SelectionMenuInteractionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */