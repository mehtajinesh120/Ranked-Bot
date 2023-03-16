/*    */ package net.dv8tion.jda.api.events.guild.member.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.events.UpdateEvent;
/*    */ import net.dv8tion.jda.api.events.guild.member.GenericGuildMemberEvent;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GenericGuildMemberUpdateEvent<T>
/*    */   extends GenericGuildMemberEvent
/*    */   implements UpdateEvent<Member, T>
/*    */ {
/*    */   protected final T previous;
/*    */   protected final T next;
/*    */   protected final String identifier;
/*    */   
/*    */   public GenericGuildMemberUpdateEvent(@Nonnull JDA api, long responseNumber, @Nonnull Member member, @Nullable T previous, @Nullable T next, @Nonnull String identifier) {
/* 53 */     super(api, responseNumber, member);
/* 54 */     this.previous = previous;
/* 55 */     this.next = next;
/* 56 */     this.identifier = identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getPropertyIdentifier() {
/* 63 */     return this.identifier;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Member getEntity() {
/* 70 */     return getMember();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getOldValue() {
/* 77 */     return this.previous;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public T getNewValue() {
/* 84 */     return this.next;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 90 */     return "GenericGuildMemberUpdateEvent[" + getPropertyIdentifier() + "](" + getOldValue() + "->" + getNewValue() + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\membe\\update\GenericGuildMemberUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */