/*    */ package net.dv8tion.jda.api.events;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.handle.GuildSetupController;
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
/*    */ public class ReadyEvent
/*    */   extends Event
/*    */ {
/*    */   private final int availableGuilds;
/*    */   private final int unavailableGuilds;
/*    */   
/*    */   public ReadyEvent(@Nonnull JDA api, long responseNumber) {
/* 38 */     super(api, responseNumber);
/* 39 */     this.availableGuilds = (int)getJDA().getGuildCache().size();
/* 40 */     GuildSetupController setupController = ((JDAImpl)getJDA()).getGuildSetupController();
/* 41 */     this.unavailableGuilds = setupController.getSetupNodes(GuildSetupController.Status.UNAVAILABLE).size() + setupController.getUnavailableGuilds().size();
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
/*    */ 
/*    */   
/*    */   public int getGuildAvailableCount() {
/* 56 */     return this.availableGuilds;
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
/*    */   public int getGuildUnavailableCount() {
/* 68 */     return this.unavailableGuilds;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getGuildTotalCount() {
/* 78 */     return getGuildAvailableCount() + getGuildUnavailableCount();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\ReadyEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */