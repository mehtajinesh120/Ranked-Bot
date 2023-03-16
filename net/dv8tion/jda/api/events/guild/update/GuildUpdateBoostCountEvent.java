/*    */ package net.dv8tion.jda.api.events.guild.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Guild;
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
/*    */ public class GuildUpdateBoostCountEvent
/*    */   extends GenericGuildUpdateEvent<Integer>
/*    */ {
/*    */   public static final String IDENTIFIER = "boost_count";
/*    */   
/*    */   public GuildUpdateBoostCountEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, int previous) {
/* 37 */     super(api, responseNumber, guild, Integer.valueOf(previous), Integer.valueOf(guild.getBoostCount()), "boost_count");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOldBoostCount() {
/* 47 */     return getOldValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNewBoostCount() {
/* 57 */     return getNewValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Integer getOldValue() {
/* 64 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Integer getNewValue() {
/* 71 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guil\\update\GuildUpdateBoostCountEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */