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
/*    */ public class GuildUpdateNSFWLevelEvent
/*    */   extends GenericGuildUpdateEvent<Guild.NSFWLevel>
/*    */ {
/*    */   public static final String IDENTIFIER = "nsfw_level";
/*    */   
/*    */   public GuildUpdateNSFWLevelEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nonnull Guild.NSFWLevel oldNSFWLevel) {
/* 37 */     super(api, responseNumber, guild, oldNSFWLevel, guild.getNSFWLevel(), "nsfw_level");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Guild.NSFWLevel getOldNSFWLevel() {
/* 48 */     return getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Guild.NSFWLevel getNewNSFWLevel() {
/* 59 */     return getNewValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Guild.NSFWLevel getOldValue() {
/* 66 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Guild.NSFWLevel getNewValue() {
/* 73 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guil\\update\GuildUpdateNSFWLevelEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */