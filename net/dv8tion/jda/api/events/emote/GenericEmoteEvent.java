/*    */ package net.dv8tion.jda.api.events.emote;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Emote;
/*    */ import net.dv8tion.jda.api.entities.Guild;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GenericEmoteEvent
/*    */   extends Event
/*    */ {
/*    */   protected final Emote emote;
/*    */   
/*    */   public GenericEmoteEvent(@Nonnull JDA api, long responseNumber, @Nonnull Emote emote) {
/* 42 */     super(api, responseNumber);
/* 43 */     this.emote = emote;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Guild getGuild() {
/* 54 */     return this.emote.getGuild();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Emote getEmote() {
/* 65 */     return this.emote;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isManaged() {
/* 75 */     return this.emote.isManaged();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\emote\GenericEmoteEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */