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
/*    */ public class TextChannelUpdateNSFWEvent
/*    */   extends GenericTextChannelUpdateEvent<Boolean>
/*    */ {
/*    */   public static final String IDENTIFIER = "nsfw";
/*    */   
/*    */   public TextChannelUpdateNSFWEvent(@Nonnull JDA api, long responseNumber, @Nonnull TextChannel channel, boolean oldNsfw) {
/* 36 */     super(api, responseNumber, channel, Boolean.valueOf(oldNsfw), Boolean.valueOf(channel.isNSFW()), "nsfw");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getOldNSFW() {
/* 46 */     return getOldValue().booleanValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean getNewNSFW() {
/* 56 */     return getNewValue().booleanValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\tex\\update\TextChannelUpdateNSFWEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */