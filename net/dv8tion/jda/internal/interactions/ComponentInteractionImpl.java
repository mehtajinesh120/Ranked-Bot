/*    */ package net.dv8tion.jda.internal.interactions;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.entities.AbstractChannel;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.MessageChannel;
/*    */ import net.dv8tion.jda.api.interactions.components.ComponentInteraction;
/*    */ import net.dv8tion.jda.api.requests.restaction.interactions.UpdateInteractionAction;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.requests.restaction.interactions.UpdateInteractionActionImpl;
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
/*    */ public abstract class ComponentInteractionImpl
/*    */   extends InteractionImpl
/*    */   implements ComponentInteraction
/*    */ {
/*    */   protected final String customId;
/*    */   protected final Message message;
/*    */   protected final long messageId;
/*    */   
/*    */   public ComponentInteractionImpl(JDAImpl jda, DataObject data) {
/* 36 */     super(jda, data);
/* 37 */     this.customId = data.getObject("data").getString("custom_id");
/*    */     
/* 39 */     DataObject messageJson = data.getObject("message");
/* 40 */     this.messageId = messageJson.getUnsignedLong("id");
/*    */     
/* 42 */     this.message = messageJson.isNull("type") ? null : jda.getEntityBuilder().createMessage(messageJson);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public MessageChannel getChannel() {
/* 50 */     return (MessageChannel)super.getChannel();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getComponentId() {
/* 57 */     return this.customId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Message getMessage() {
/* 64 */     return this.message;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getMessageIdLong() {
/* 70 */     return this.messageId;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public UpdateInteractionActionImpl deferEdit() {
/* 77 */     return new UpdateInteractionActionImpl(this.hook);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\interactions\ComponentInteractionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */