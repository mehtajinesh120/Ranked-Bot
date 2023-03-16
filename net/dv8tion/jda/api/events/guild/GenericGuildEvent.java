/*    */ package net.dv8tion.jda.api.events.guild;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
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
/*    */ public abstract class GenericGuildEvent
/*    */   extends Event
/*    */ {
/*    */   protected final Guild guild;
/*    */   
/*    */   public GenericGuildEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild) {
/* 36 */     super(api, responseNumber);
/* 37 */     this.guild = guild;
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
/* 48 */     return this.guild;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\GenericGuildEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */