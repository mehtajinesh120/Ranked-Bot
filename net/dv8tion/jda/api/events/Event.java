/*    */ package net.dv8tion.jda.api.events;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Event
/*    */   implements GenericEvent
/*    */ {
/*    */   protected final JDA api;
/*    */   protected final long responseNumber;
/*    */   
/*    */   public Event(@Nonnull JDA api, long responseNumber) {
/* 46 */     this.api = api;
/* 47 */     this.responseNumber = responseNumber;
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
/*    */   public Event(@Nonnull JDA api) {
/* 59 */     this(api, api.getResponseTotal());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public JDA getJDA() {
/* 66 */     return this.api;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public long getResponseNumber() {
/* 72 */     return this.responseNumber;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\Event.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */