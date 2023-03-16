/*    */ package net.dv8tion.jda.api.events.interaction;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.interactions.Interaction;
/*    */ import net.dv8tion.jda.api.interactions.components.Component;
/*    */ import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
/*    */ import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
/*    */ import net.dv8tion.jda.api.interactions.components.selections.SelectionMenuInteraction;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SelectionMenuEvent
/*    */   extends GenericComponentInteractionCreateEvent
/*    */   implements SelectionMenuInteraction
/*    */ {
/*    */   private final SelectionMenuInteraction menuInteraction;
/*    */   
/*    */   public SelectionMenuEvent(@Nonnull JDA api, long responseNumber, @Nonnull SelectionMenuInteraction interaction) {
/* 42 */     super(api, responseNumber, (ComponentInteraction)interaction);
/* 43 */     this.menuInteraction = interaction;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public SelectionMenuInteraction getInteraction() {
/* 50 */     return this.menuInteraction;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public SelectionMenu getComponent() {
/* 57 */     return this.menuInteraction.getComponent();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<String> getValues() {
/* 64 */     return this.menuInteraction.getValues();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\interaction\SelectionMenuEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */