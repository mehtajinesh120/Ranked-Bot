/*    */ package net.dv8tion.jda.api.events.channel.priv;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*    */ import net.dv8tion.jda.annotations.ForRemoval;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*    */ import net.dv8tion.jda.api.entities.User;
/*    */ import net.dv8tion.jda.api.events.Event;
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
/*    */ @Deprecated
/*    */ @ForRemoval(deadline = "4.4.0")
/*    */ @DeprecatedSince("4.3.0")
/*    */ public class PrivateChannelCreateEvent
/*    */   extends Event
/*    */ {
/*    */   private final PrivateChannel channel;
/*    */   
/*    */   public PrivateChannelCreateEvent(@Nonnull JDA api, long responseNumber, @Nonnull PrivateChannel channel) {
/* 43 */     super(api, responseNumber);
/* 44 */     this.channel = channel;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public User getUser() {
/* 56 */     return this.channel.getUser();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public PrivateChannel getChannel() {
/* 67 */     return this.channel;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\priv\PrivateChannelCreateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */