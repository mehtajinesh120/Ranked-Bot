/*    */ package net.dv8tion.jda.api.interactions.components;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import javax.annotation.CheckReturnValue;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.interactions.InteractionHook;
/*    */ import net.dv8tion.jda.api.requests.RestAction;
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
/*    */ public interface ButtonInteraction
/*    */   extends ComponentInteraction
/*    */ {
/*    */   @Nullable
/*    */   default Button getComponent() {
/* 40 */     return getButton();
/*    */   }
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
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   @CheckReturnValue
/*    */   default RestAction<Void> editButton(@Nullable Button newButton) {
/* 72 */     Message message = getMessage();
/* 73 */     List<ActionRow> components = new ArrayList<>(message.getActionRows());
/* 74 */     ComponentLayout.updateComponent((List)components, getComponentId(), newButton);
/*    */     
/* 76 */     if (isAcknowledged()) {
/* 77 */       return getHook().editMessageComponentsById(message.getId(), components).map(it -> null);
/*    */     }
/* 79 */     return editComponents((Collection)components).map(it -> null);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   Button getButton();
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\components\ButtonInteraction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */