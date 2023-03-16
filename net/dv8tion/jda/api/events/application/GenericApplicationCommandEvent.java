/*    */ package net.dv8tion.jda.api.events.application;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.events.Event;
/*    */ import net.dv8tion.jda.api.interactions.commands.Command;
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
/*    */ public abstract class GenericApplicationCommandEvent
/*    */   extends Event
/*    */ {
/*    */   private final Command command;
/*    */   private final Guild guild;
/*    */   
/*    */   public GenericApplicationCommandEvent(@Nonnull JDA api, long responseNumber, @Nonnull Command command, @Nullable Guild guild) {
/* 42 */     super(api, responseNumber);
/* 43 */     this.command = command;
/* 44 */     this.guild = guild;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Command getCommand() {
/* 55 */     return this.command;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public Guild getGuild() {
/* 66 */     return this.guild;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\application\GenericApplicationCommandEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */