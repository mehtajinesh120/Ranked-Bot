/*    */ package net.dv8tion.jda.api.events.self;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
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
/*    */ public class SelfUpdateAvatarEvent
/*    */   extends GenericSelfUpdateEvent<String>
/*    */ {
/*    */   public static final String IDENTIFIER = "avatar";
/*    */   private static final String AVATAR_URL = "https://cdn.discordapp.com/avatars/%s/%s%s";
/*    */   
/*    */   public SelfUpdateAvatarEvent(@Nonnull JDA api, long responseNumber, @Nullable String oldAvatarId) {
/* 38 */     super(api, responseNumber, oldAvatarId, api.getSelfUser().getAvatarId(), "avatar");
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
/* 49 */     return getOldValue();
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
/* 60 */     return (this.previous == null) ? null : String.format("https://cdn.discordapp.com/avatars/%s/%s%s", new Object[] { getSelfUser().getId(), this.previous, this.previous.startsWith("a_") ? ".gif" : ".png" });
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
/* 71 */     return getNewValue();
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
/* 82 */     return (this.next == null) ? null : String.format("https://cdn.discordapp.com/avatars/%s/%s%s", new Object[] { getSelfUser().getId(), this.next, this.next.startsWith("a_") ? ".gif" : ".png" });
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\self\SelfUpdateAvatarEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */