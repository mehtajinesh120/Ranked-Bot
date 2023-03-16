/*    */ package net.dv8tion.jda.api.events.message.priv;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.User;
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
/*    */ public class PrivateMessageReceivedEvent
/*    */   extends GenericPrivateMessageEvent
/*    */ {
/*    */   private final Message message;
/*    */   
/*    */   public PrivateMessageReceivedEvent(@Nonnull JDA api, long responseNumber, @Nonnull Message message) {
/* 39 */     super(api, responseNumber, message.getIdLong(), message.getPrivateChannel());
/* 40 */     this.message = message;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Message getMessage() {
/* 51 */     return this.message;
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
/*    */   @Nonnull
/*    */   public User getAuthor() {
/* 64 */     return this.message.getAuthor();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\message\priv\PrivateMessageReceivedEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */