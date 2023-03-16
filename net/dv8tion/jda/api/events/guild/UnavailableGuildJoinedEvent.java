/*    */ package net.dv8tion.jda.api.events.guild;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
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
/*    */ 
/*    */ 
/*    */ public class UnavailableGuildJoinedEvent
/*    */   extends Event
/*    */ {
/*    */   private final long guildId;
/*    */   
/*    */   public UnavailableGuildJoinedEvent(@Nonnull JDA api, long responseNumber, long guildId) {
/* 37 */     super(api, responseNumber);
/* 38 */     this.guildId = guildId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getGuildId() {
/* 49 */     return Long.toUnsignedString(this.guildId);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getGuildIdLong() {
/* 59 */     return this.guildId;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\UnavailableGuildJoinedEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */