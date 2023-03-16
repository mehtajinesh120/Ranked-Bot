/*    */ package net.dv8tion.jda.internal.interactions;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.interactions.components.Button;
/*    */ import net.dv8tion.jda.api.interactions.components.ButtonInteraction;
/*    */ import net.dv8tion.jda.api.interactions.components.Component;
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
/*    */ 
/*    */ 
/*    */ public class ButtonInteractionImpl
/*    */   extends ComponentInteractionImpl
/*    */   implements ButtonInteraction
/*    */ {
/*    */   private final Button button;
/*    */   
/*    */   public ButtonInteractionImpl(JDAImpl jda, DataObject data) {
/* 33 */     super(jda, data);
/* 34 */     this.button = (this.message != null) ? this.message.getButtonById(this.customId) : null;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Component.Type getComponentType() {
/* 41 */     return Component.Type.BUTTON;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Button getButton() {
/* 48 */     return this.button;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\interactions\ButtonInteractionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */