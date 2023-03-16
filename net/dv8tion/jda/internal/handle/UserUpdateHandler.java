/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.self.SelfUpdateAvatarEvent;
/*    */ import net.dv8tion.jda.api.events.self.SelfUpdateMFAEvent;
/*    */ import net.dv8tion.jda.api.events.self.SelfUpdateNameEvent;
/*    */ import net.dv8tion.jda.api.events.self.SelfUpdateVerifiedEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.SelfUserImpl;
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
/*    */ public class UserUpdateHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public UserUpdateHandler(JDAImpl api) {
/* 33 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 39 */     SelfUserImpl self = (SelfUserImpl)getJDA().getSelfUser();
/*    */     
/* 41 */     String name = content.getString("username");
/* 42 */     String discriminator = content.getString("discriminator");
/* 43 */     String avatarId = content.getString("avatar", null);
/* 44 */     Boolean verified = content.hasKey("verified") ? Boolean.valueOf(content.getBoolean("verified")) : null;
/* 45 */     Boolean mfaEnabled = content.hasKey("mfa_enabled") ? Boolean.valueOf(content.getBoolean("mfa_enabled")) : null;
/*    */ 
/*    */     
/* 48 */     String email = content.getString("email", null);
/* 49 */     Boolean mobile = content.hasKey("mobile") ? Boolean.valueOf(content.getBoolean("mobile")) : null;
/* 50 */     Boolean nitro = content.hasKey("premium") ? Boolean.valueOf(content.getBoolean("premium")) : null;
/* 51 */     String phoneNumber = content.getString("phone", null);
/*    */     
/* 53 */     if (!Objects.equals(name, self.getName()) || !Objects.equals(discriminator, self.getDiscriminator())) {
/*    */       
/* 55 */       String oldName = self.getName();
/* 56 */       self.setName(name);
/* 57 */       getJDA().handleEvent((GenericEvent)new SelfUpdateNameEvent((JDA)
/*    */             
/* 59 */             getJDA(), this.responseNumber, oldName));
/*    */     } 
/*    */ 
/*    */     
/* 63 */     if (!Objects.equals(avatarId, self.getAvatarId())) {
/*    */       
/* 65 */       String oldAvatarId = self.getAvatarId();
/* 66 */       self.setAvatarId(avatarId);
/* 67 */       getJDA().handleEvent((GenericEvent)new SelfUpdateAvatarEvent((JDA)
/*    */             
/* 69 */             getJDA(), this.responseNumber, oldAvatarId));
/*    */     } 
/*    */ 
/*    */     
/* 73 */     if (verified != null && verified.booleanValue() != self.isVerified()) {
/*    */       
/* 75 */       boolean wasVerified = self.isVerified();
/* 76 */       self.setVerified(verified.booleanValue());
/* 77 */       getJDA().handleEvent((GenericEvent)new SelfUpdateVerifiedEvent((JDA)
/*    */             
/* 79 */             getJDA(), this.responseNumber, wasVerified));
/*    */     } 
/*    */ 
/*    */     
/* 83 */     if (mfaEnabled != null && mfaEnabled.booleanValue() != self.isMfaEnabled()) {
/*    */       
/* 85 */       boolean wasMfaEnabled = self.isMfaEnabled();
/* 86 */       self.setMfaEnabled(mfaEnabled.booleanValue());
/* 87 */       getJDA().handleEvent((GenericEvent)new SelfUpdateMFAEvent((JDA)
/*    */             
/* 89 */             getJDA(), this.responseNumber, wasMfaEnabled));
/*    */     } 
/*    */     
/* 92 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\UserUpdateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */