/*    */ package net.dv8tion.jda.api.events.user;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.User;
/*    */ import net.dv8tion.jda.api.events.Event;
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
/*    */ public abstract class GenericUserEvent
/*    */   extends Event
/*    */ {
/*    */   private final User user;
/*    */   
/*    */   public GenericUserEvent(@Nonnull JDA api, long responseNumber, @Nonnull User user) {
/* 36 */     super(api, responseNumber);
/* 37 */     this.user = user;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public User getUser() {
/* 48 */     return this.user;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\event\\user\GenericUserEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */