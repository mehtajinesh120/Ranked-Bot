/*    */ package net.dv8tion.jda.api.events.user.update;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.User;
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
/*    */ public class UserUpdateFlagsEvent
/*    */   extends GenericUserUpdateEvent<EnumSet<User.UserFlag>>
/*    */ {
/*    */   public static final String IDENTIFIER = "public_flags";
/*    */   
/*    */   public UserUpdateFlagsEvent(@Nonnull JDA api, long responseNumber, @Nonnull User user, @Nonnull EnumSet<User.UserFlag> oldFlags) {
/* 48 */     super(api, responseNumber, user, oldFlags, user.getFlags(), "public_flags");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public EnumSet<User.UserFlag> getOldFlags() {
/* 59 */     return getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOldFlagsRaw() {
/* 69 */     return User.UserFlag.getRaw(this.previous);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public EnumSet<User.UserFlag> getNewFlags() {
/* 80 */     return getNewValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNewFlagsRaw() {
/* 90 */     return User.UserFlag.getRaw(this.next);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\event\\use\\update\UserUpdateFlagsEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */