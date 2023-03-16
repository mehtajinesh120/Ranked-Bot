/*    */ package net.dv8tion.jda.api.events.channel.text.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
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
/*    */ public class TextChannelUpdateSlowmodeEvent
/*    */   extends GenericTextChannelUpdateEvent<Integer>
/*    */ {
/*    */   public static final String IDENTIFIER = "slowmode";
/*    */   
/*    */   public TextChannelUpdateSlowmodeEvent(@Nonnull JDA api, long responseNumber, @Nonnull TextChannel channel, int oldSlowmode) {
/* 36 */     super(api, responseNumber, channel, Integer.valueOf(oldSlowmode), Integer.valueOf(channel.getSlowmode()), "slowmode");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOldSlowmode() {
/* 46 */     return getOldValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNewSlowmode() {
/* 56 */     return getNewValue().intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\tex\\update\TextChannelUpdateSlowmodeEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */