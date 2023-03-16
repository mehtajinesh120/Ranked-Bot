/*    */ package net.dv8tion.jda.api.events.interaction;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.interactions.Interaction;
/*    */ import net.dv8tion.jda.api.interactions.components.Button;
/*    */ import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
/*    */ import net.dv8tion.jda.api.interactions.components.Component;
/*    */ import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
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
/*    */ public class ButtonClickEvent
/*    */   extends GenericComponentInteractionCreateEvent
/*    */   implements ButtonInteraction
/*    */ {
/*    */   private final ButtonInteraction interaction;
/*    */   
/*    */   public ButtonClickEvent(@Nonnull JDA api, long responseNumber, @Nonnull ButtonInteraction interaction) {
/* 41 */     super(api, responseNumber, (ComponentInteraction)interaction);
/* 42 */     this.interaction = interaction;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public ButtonInteraction getInteraction() {
/* 49 */     return this.interaction;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Button getComponent() {
/* 56 */     return this.interaction.getComponent();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Button getButton() {
/* 63 */     return this.interaction.getButton();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\interaction\ButtonClickEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */