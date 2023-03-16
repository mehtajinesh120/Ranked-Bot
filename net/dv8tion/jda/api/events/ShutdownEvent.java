/*    */ package net.dv8tion.jda.api.events;
/*    */ 
/*    */ import java.time.OffsetDateTime;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.requests.CloseCode;
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
/*    */ public class ShutdownEvent
/*    */   extends Event
/*    */ {
/*    */   protected final OffsetDateTime shutdownTime;
/*    */   protected final int code;
/*    */   
/*    */   public ShutdownEvent(@Nonnull JDA api, @Nonnull OffsetDateTime shutdownTime, int code) {
/* 36 */     super(api);
/* 37 */     this.shutdownTime = shutdownTime;
/* 38 */     this.code = code;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public OffsetDateTime getTimeShutdown() {
/* 50 */     return this.shutdownTime;
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
/*    */   @Nullable
/*    */   public CloseCode getCloseCode() {
/* 64 */     return CloseCode.from(this.code);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCode() {
/* 75 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\ShutdownEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */