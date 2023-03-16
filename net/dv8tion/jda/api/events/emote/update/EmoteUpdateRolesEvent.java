/*    */ package net.dv8tion.jda.api.events.emote.update;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Emote;
/*    */ import net.dv8tion.jda.api.entities.Role;
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
/*    */ public class EmoteUpdateRolesEvent
/*    */   extends GenericEmoteUpdateEvent<List<Role>>
/*    */ {
/*    */   public static final String IDENTIFIER = "roles";
/*    */   
/*    */   public EmoteUpdateRolesEvent(@Nonnull JDA api, long responseNumber, @Nonnull Emote emote, @Nonnull List<Role> oldRoles) {
/* 46 */     super(api, responseNumber, emote, oldRoles, emote.getRoles(), "roles");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<Role> getOldRoles() {
/* 57 */     return getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<Role> getNewRoles() {
/* 68 */     return getNewValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<Role> getOldValue() {
/* 75 */     return super.getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<Role> getNewValue() {
/* 82 */     return super.getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\emot\\update\EmoteUpdateRolesEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */