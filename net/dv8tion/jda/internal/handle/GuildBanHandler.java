/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.User;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.guild.GuildBanEvent;
/*    */ import net.dv8tion.jda.api.events.guild.GuildUnbanEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*    */ import net.dv8tion.jda.internal.entities.UserImpl;
/*    */ import net.dv8tion.jda.internal.utils.JDALogger;
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
/*    */ public class GuildBanHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   private final boolean banned;
/*    */   
/*    */   public GuildBanHandler(JDAImpl api, boolean banned) {
/* 32 */     super(api);
/* 33 */     this.banned = banned;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 39 */     long id = content.getLong("guild_id");
/* 40 */     if (getJDA().getGuildSetupController().isLocked(id)) {
/* 41 */       return Long.valueOf(id);
/*    */     }
/* 43 */     DataObject userJson = content.getObject("user");
/* 44 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(id);
/* 45 */     if (guild == null) {
/*    */       
/* 47 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, id, this.responseNumber, this.allContent, this::handle);
/* 48 */       EventCache.LOG.debug("Received Guild Member {} event for a Guild not yet cached.", JDALogger.getLazyString(() -> this.banned ? "Ban" : "Unban"));
/* 49 */       return null;
/*    */     } 
/*    */     
/* 52 */     UserImpl userImpl = getJDA().getEntityBuilder().createUser(userJson);
/*    */     
/* 54 */     if (this.banned) {
/*    */       
/* 56 */       getJDA().handleEvent((GenericEvent)new GuildBanEvent((JDA)
/*    */             
/* 58 */             getJDA(), this.responseNumber, (Guild)guild, (User)userImpl));
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 63 */       getJDA().handleEvent((GenericEvent)new GuildUnbanEvent((JDA)
/*    */             
/* 65 */             getJDA(), this.responseNumber, (Guild)guild, (User)userImpl));
/*    */     } 
/*    */     
/* 68 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildBanHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */