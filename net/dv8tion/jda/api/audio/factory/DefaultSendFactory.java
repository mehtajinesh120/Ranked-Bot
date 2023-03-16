/*    */ package net.dv8tion.jda.api.audio.factory;
/*    */ 
/*    */ import javax.annotation.Nonnull;
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
/*    */ public class DefaultSendFactory
/*    */   implements IAudioSendFactory
/*    */ {
/*    */   @Nonnull
/*    */   public IAudioSendSystem createSendSystem(@Nonnull IPacketProvider packetProvider) {
/* 30 */     return new DefaultSendSystem(packetProvider);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\factory\DefaultSendFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */