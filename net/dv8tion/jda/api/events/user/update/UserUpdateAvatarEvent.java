/*    */ package net.dv8tion.jda.api.events.user.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
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
/*    */ 
/*    */ public class UserUpdateAvatarEvent
/*    */   extends GenericUserUpdateEvent<String>
/*    */ {
/*    */   public static final String IDENTIFIER = "avatar";
/*    */   
/*    */   public UserUpdateAvatarEvent(@Nonnull JDA api, long responseNumber, @Nonnull User user, @Nullable String oldAvatar) {
/* 49 */     super(api, responseNumber, user, oldAvatar, user.getAvatarId(), "avatar");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getOldAvatarId() {
/* 60 */     return getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getOldAvatarUrl() {
/* 71 */     return (this.previous == null) ? null : String.format("https://cdn.discordapp.com/avatars/%s/%s.%s", new Object[] { getUser().getId(), this.previous, this.previous.startsWith("a_") ? "gif" : "png" });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getNewAvatarId() {
/* 82 */     return getNewValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public String getNewAvatarUrl() {
/* 93 */     return (this.next == null) ? null : String.format("https://cdn.discordapp.com/avatars/%s/%s.%s", new Object[] { getUser().getId(), this.next, this.next.startsWith("a_") ? "gif" : "png" });
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\event\\use\\update\UserUpdateAvatarEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */