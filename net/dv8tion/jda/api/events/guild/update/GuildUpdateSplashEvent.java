/*    */ package net.dv8tion.jda.api.events.guild.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
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
/*    */ public class GuildUpdateSplashEvent
/*    */   extends GenericGuildUpdateEvent<String>
/*    */ {
/*    */   public static final String IDENTIFIER = "splash";
/*    */   
/*    */   public GuildUpdateSplashEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nullable String oldSplashId) {
/* 38 */     super(api, responseNumber, guild, oldSplashId, guild.getSplashId(), "splash");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getOldSplashId() {
/* 49 */     return getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getOldSplashUrl() {
/* 60 */     return (this.previous == null) ? null : String.format("https://cdn.discordapp.com/splashes/%s/%s.png", new Object[] { this.guild.getId(), this.previous });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getNewSplashId() {
/* 71 */     return getNewValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getNewSplashUrl() {
/* 82 */     return (this.next == null) ? null : String.format("https://cdn.discordapp.com/splashes/%s/%s.png", new Object[] { this.guild.getId(), this.next });
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guil\\update\GuildUpdateSplashEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */