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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TextChannelUpdateNewsEvent
/*    */   extends GenericTextChannelUpdateEvent<Boolean>
/*    */ {
/*    */   public static final String IDENTIFIER = "news";
/*    */   
/*    */   public TextChannelUpdateNewsEvent(@Nonnull JDA api, long responseNumber, @Nonnull TextChannel channel) {
/* 41 */     super(api, responseNumber, channel, Boolean.valueOf(!channel.isNews()), Boolean.valueOf(channel.isNews()), "news");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Boolean getOldValue() {
/* 48 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Boolean getNewValue() {
/* 55 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\tex\\update\TextChannelUpdateNewsEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */