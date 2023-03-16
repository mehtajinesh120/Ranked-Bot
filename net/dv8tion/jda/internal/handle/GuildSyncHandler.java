/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
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
/*    */ public class GuildSyncHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public GuildSyncHandler(JDAImpl api) {
/* 26 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 32 */     long guildId = content.getLong("id");
/* 33 */     getJDA().getGuildSetupController().onSync(guildId, content);
/* 34 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildSyncHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */