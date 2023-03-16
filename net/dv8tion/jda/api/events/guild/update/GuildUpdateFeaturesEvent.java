/*    */ package net.dv8tion.jda.api.events.guild.update;
/*    */ 
/*    */ import java.util.Set;
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
/*    */ public class GuildUpdateFeaturesEvent
/*    */   extends GenericGuildUpdateEvent<Set<String>>
/*    */ {
/*    */   public static final String IDENTIFIER = "features";
/*    */   
/*    */   public GuildUpdateFeaturesEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nonnull Set<String> oldFeatures) {
/* 38 */     super(api, responseNumber, guild, oldFeatures, guild.getFeatures(), "features");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Set<String> getOldFeatures() {
/* 49 */     return getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Set<String> getNewFeatures() {
/* 60 */     return getNewValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Set<String> getOldValue() {
/* 67 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Set<String> getNewValue() {
/* 74 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guil\\update\GuildUpdateFeaturesEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */