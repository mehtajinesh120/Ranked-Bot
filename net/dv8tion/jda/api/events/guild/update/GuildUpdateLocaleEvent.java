/*    */ package net.dv8tion.jda.api.events.guild.update;
/*    */ 
/*    */ import java.util.Locale;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuildUpdateLocaleEvent
/*    */   extends GenericGuildUpdateEvent<Locale>
/*    */ {
/*    */   public static final String IDENTIFIER = "locale";
/*    */   
/*    */   public GuildUpdateLocaleEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nonnull Locale previous) {
/* 41 */     super(api, responseNumber, guild, previous, guild.getLocale(), "locale");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Locale getOldValue() {
/* 48 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Locale getNewValue() {
/* 55 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guil\\update\GuildUpdateLocaleEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */