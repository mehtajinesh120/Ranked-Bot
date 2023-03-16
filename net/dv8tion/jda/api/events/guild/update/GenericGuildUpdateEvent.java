/*    */ package net.dv8tion.jda.api.events.guild.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.events.UpdateEvent;
/*    */ import net.dv8tion.jda.api.events.guild.GenericGuildEvent;
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
/*    */ public abstract class GenericGuildUpdateEvent<T>
/*    */   extends GenericGuildEvent
/*    */   implements UpdateEvent<Guild, T>
/*    */ {
/*    */   protected final T previous;
/*    */   protected final T next;
/*    */   protected final String identifier;
/*    */   
/*    */   public GenericGuildUpdateEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nullable T previous, @Nullable T next, @Nonnull String identifier) {
/* 41 */     super(api, responseNumber, guild);
/* 42 */     this.previous = previous;
/* 43 */     this.next = next;
/* 44 */     this.identifier = identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Guild getEntity() {
/* 51 */     return getGuild();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getPropertyIdentifier() {
/* 58 */     return this.identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getOldValue() {
/* 65 */     return this.previous;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getNewValue() {
/* 72 */     return this.next;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 78 */     return "GuildUpdate[" + getPropertyIdentifier() + "](" + getOldValue() + "->" + getNewValue() + ')';
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guil\\update\GenericGuildUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */