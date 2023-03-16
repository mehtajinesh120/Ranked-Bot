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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuildTimeoutEvent
/*    */   extends Event
/*    */ {
/*    */   private final long guildId;
/*    */   
/*    */   public GuildTimeoutEvent(@Nonnull JDA api, long guildId) {
/* 43 */     super(api);
/* 44 */     this.guildId = guildId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public long getGuildIdLong() {
/* 54 */     return this.guildId;
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
/* 65 */     return Long.toUnsignedString(this.guildId);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\GuildTimeoutEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */