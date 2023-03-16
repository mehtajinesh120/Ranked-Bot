/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.GuildImpl;
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
/*    */ public class GuildCreateHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public GuildCreateHandler(JDAImpl api) {
/* 27 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 33 */     long id = content.getLong("id");
/* 34 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(id);
/* 35 */     if (guild == null)
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 44 */       getJDA().getGuildSetupController().onCreate(id, content);
/*    */     }
/*    */ 
/*    */     
/* 48 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildCreateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */