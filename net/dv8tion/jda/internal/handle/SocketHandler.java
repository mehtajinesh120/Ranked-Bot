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
/*    */ public abstract class SocketHandler
/*    */ {
/*    */   protected final JDAImpl api;
/*    */   protected long responseNumber;
/*    */   protected DataObject allContent;
/*    */   
/*    */   public SocketHandler(JDAImpl api) {
/* 29 */     this.api = api;
/*    */   }
/*    */ 
/*    */   
/*    */   public final synchronized void handle(long responseTotal, DataObject o) {
/* 34 */     this.allContent = o;
/* 35 */     this.responseNumber = responseTotal;
/* 36 */     Long guildId = handleInternally(o.getObject("d"));
/* 37 */     if (guildId != null)
/* 38 */       getJDA().getGuildSetupController().cacheEvent(guildId.longValue(), o); 
/* 39 */     this.allContent = null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected JDAImpl getJDA() {
/* 44 */     return this.api;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected abstract Long handleInternally(DataObject paramDataObject);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static class NOPHandler
/*    */     extends SocketHandler
/*    */   {
/*    */     public NOPHandler(JDAImpl api) {
/* 60 */       super(api);
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     protected Long handleInternally(DataObject content) {
/* 66 */       return null;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\SocketHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */