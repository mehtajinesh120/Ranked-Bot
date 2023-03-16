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
/*    */ public class GuildUpdateIconEvent
/*    */   extends GenericGuildUpdateEvent<String>
/*    */ {
/*    */   public static final String IDENTIFIER = "icon";
/*    */   
/*    */   public GuildUpdateIconEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nullable String oldIconId) {
/* 38 */     super(api, responseNumber, guild, oldIconId, guild.getIconId(), "icon");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getOldIconId() {
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
/*    */   public String getOldIconUrl() {
/* 60 */     return (this.previous == null) ? null : String.format("https://cdn.discordapp.com/icons/%s/%s.%s", new Object[] { this.guild.getId(), this.previous, this.previous.startsWith("a_") ? "gif" : "png" });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getNewIconId() {
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
/*    */   public String getNewIconUrl() {
/* 82 */     return (this.next == null) ? null : String.format("https://cdn.discordapp.com/icons/%s/%s.%s", new Object[] { this.guild.getId(), this.next, this.next.startsWith("a_") ? "gif" : "png" });
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guil\\update\GuildUpdateIconEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */