/*    */ package net.dv8tion.jda.api.events.emote;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Emote;
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
/*    */ public class EmoteAddedEvent
/*    */   extends GenericEmoteEvent
/*    */ {
/*    */   public EmoteAddedEvent(@Nonnull JDA api, long responseNumber, @Nonnull Emote emote) {
/* 38 */     super(api, responseNumber, emote);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\emote\EmoteAddedEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */