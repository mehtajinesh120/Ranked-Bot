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
/*    */ public class GatewayPingEvent
/*    */   extends Event
/*    */   implements UpdateEvent<JDA, Long>
/*    */ {
/*    */   public static final String IDENTIFIER = "gateway-ping";
/*    */   private final long next;
/*    */   private final long prev;
/*    */   
/*    */   public GatewayPingEvent(@Nonnull JDA api, long old) {
/* 38 */     super(api);
/* 39 */     this.next = api.getGatewayPing();
/* 40 */     this.prev = old;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getNewPing() {
/* 50 */     return this.next;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getOldPing() {
/* 60 */     return this.prev;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getPropertyIdentifier() {
/* 67 */     return "gateway-ping";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public JDA getEntity() {
/* 74 */     return getJDA();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Long getOldValue() {
/* 81 */     return Long.valueOf(this.prev);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Long getNewValue() {
/* 88 */     return Long.valueOf(this.next);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return "GatewayUpdate[ping](" + getOldValue() + "->" + getNewValue() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\GatewayPingEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */