/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*     */ import net.dv8tion.jda.api.entities.SelfUser;
/*     */ import net.dv8tion.jda.api.managers.AccountManager;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.managers.AccountManagerImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SelfUserImpl
/*     */   extends UserImpl
/*     */   implements SelfUser
/*     */ {
/*     */   protected AccountManager manager;
/*     */   private boolean verified;
/*     */   private boolean mfaEnabled;
/*     */   private long applicationId;
/*     */   private String email;
/*     */   private String phoneNumber;
/*     */   private boolean mobile;
/*     */   private boolean nitro;
/*     */   
/*     */   public SelfUserImpl(long id, JDAImpl api) {
/*  44 */     super(id, api);
/*  45 */     this.applicationId = id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPrivateChannel() {
/*  51 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PrivateChannel getPrivateChannel() {
/*  57 */     throw new UnsupportedOperationException("You cannot get a PrivateChannel with yourself (SelfUser)");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<PrivateChannel> openPrivateChannel() {
/*  64 */     throw new UnsupportedOperationException("You cannot open a PrivateChannel with yourself (SelfUser)");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getApplicationIdLong() {
/*  70 */     return this.applicationId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isVerified() {
/*  76 */     return this.verified;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMfaEnabled() {
/*  82 */     return this.mfaEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getAllowedFileSize() {
/*  88 */     if (this.nitro) {
/*  89 */       return 52428800L;
/*     */     }
/*  91 */     return 8388608L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AccountManager getManager() {
/*  98 */     if (this.manager == null)
/*  99 */       return this.manager = (AccountManager)new AccountManagerImpl(this); 
/* 100 */     return this.manager;
/*     */   }
/*     */ 
/*     */   
/*     */   public SelfUserImpl setVerified(boolean verified) {
/* 105 */     this.verified = verified;
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SelfUserImpl setMfaEnabled(boolean enabled) {
/* 111 */     this.mfaEnabled = enabled;
/* 112 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SelfUserImpl setEmail(String email) {
/* 117 */     this.email = email;
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SelfUserImpl setPhoneNumber(String phoneNumber) {
/* 123 */     this.phoneNumber = phoneNumber;
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SelfUserImpl setMobile(boolean mobile) {
/* 129 */     this.mobile = mobile;
/* 130 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SelfUserImpl setNitro(boolean nitro) {
/* 135 */     this.nitro = nitro;
/* 136 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public SelfUserImpl setApplicationId(long id) {
/* 141 */     this.applicationId = id;
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public static SelfUserImpl copyOf(SelfUserImpl other, JDAImpl jda) {
/* 147 */     SelfUserImpl selfUser = new SelfUserImpl(other.id, jda);
/* 148 */     selfUser.setName(other.name)
/* 149 */       .setAvatarId(other.avatarId)
/* 150 */       .setDiscriminator(other.getDiscriminator())
/* 151 */       .setBot(other.bot);
/* 152 */     return selfUser
/* 153 */       .setVerified(other.verified)
/* 154 */       .setMfaEnabled(other.mfaEnabled)
/* 155 */       .setEmail(other.email)
/* 156 */       .setPhoneNumber(other.phoneNumber)
/* 157 */       .setMobile(other.mobile)
/* 158 */       .setNitro(other.nitro)
/* 159 */       .setApplicationId(other.applicationId);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\SelfUserImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */