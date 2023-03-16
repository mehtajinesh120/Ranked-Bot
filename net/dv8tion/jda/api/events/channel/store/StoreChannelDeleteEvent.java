/*    */ package net.dv8tion.jda.api.events.channel.store;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.StoreChannel;
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
/*    */ public class StoreChannelDeleteEvent
/*    */   extends GenericStoreChannelEvent
/*    */ {
/*    */   public StoreChannelDeleteEvent(@Nonnull JDA api, long responseNumber, @Nonnull StoreChannel channel) {
/* 33 */     super(api, responseNumber, channel);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\store\StoreChannelDeleteEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */