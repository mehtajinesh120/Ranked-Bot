/*    */ package net.dv8tion.jda.api.events.interaction;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.AbstractChannel;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.MessageChannel;
/*    */ import net.dv8tion.jda.api.interactions.Interaction;
/*    */ import net.dv8tion.jda.api.interactions.components.Component;
/*    */ import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
/*    */ import net.dv8tion.jda.api.requests.restaction.interactions.UpdateInteractionAction;
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
/*    */ public class GenericComponentInteractionCreateEvent
/*    */   extends GenericInteractionCreateEvent
/*    */   implements ComponentInteraction
/*    */ {
/*    */   private final ComponentInteraction interaction;
/*    */   
/*    */   public GenericComponentInteractionCreateEvent(@Nonnull JDA api, long responseNumber, @Nonnull ComponentInteraction interaction) {
/* 43 */     super(api, responseNumber, (Interaction)interaction);
/* 44 */     this.interaction = interaction;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public ComponentInteraction getInteraction() {
/* 51 */     return this.interaction;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public MessageChannel getChannel() {
/* 58 */     return this.interaction.getChannel();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getComponentId() {
/* 65 */     return this.interaction.getComponentId();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Component getComponent() {
/* 72 */     return this.interaction.getComponent();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Message getMessage() {
/* 79 */     return this.interaction.getMessage();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getMessageIdLong() {
/* 85 */     return this.interaction.getMessageIdLong();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Component.Type getComponentType() {
/* 92 */     return this.interaction.getComponentType();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public UpdateInteractionAction deferEdit() {
/* 99 */     return this.interaction.deferEdit();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\interaction\GenericComponentInteractionCreateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */