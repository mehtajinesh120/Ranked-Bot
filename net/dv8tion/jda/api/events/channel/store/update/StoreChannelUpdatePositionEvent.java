/*    */ package net.dv8tion.jda.api.events.channel.store.update;
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
/*    */ 
/*    */ 
/*    */ public class StoreChannelUpdatePositionEvent
/*    */   extends GenericStoreChannelUpdateEvent<Integer>
/*    */ {
/*    */   public static final String IDENTIFIER = "position";
/*    */   
/*    */   public StoreChannelUpdatePositionEvent(@Nonnull JDA api, long responseNumber, @Nonnull StoreChannel channel, int prev) {
/* 37 */     super(api, responseNumber, channel, Integer.valueOf(prev), Integer.valueOf(channel.getPositionRaw()), "position");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOldPosition() {
/* 47 */     return getOldValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNewPosition() {
/* 57 */     return getNewValue().intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\stor\\update\StoreChannelUpdatePositionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */