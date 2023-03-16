/*     */ package net.dv8tion.jda.internal.managers;
/*     */ 
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.entities.Icon;
/*     */ import net.dv8tion.jda.api.entities.SelfUser;
/*     */ import net.dv8tion.jda.api.managers.AccountManager;
/*     */ import net.dv8tion.jda.api.managers.Manager;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import okhttp3.RequestBody;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AccountManagerImpl
/*     */   extends ManagerBase<AccountManager>
/*     */   implements AccountManager
/*     */ {
/*     */   protected final SelfUser selfUser;
/*     */   protected String name;
/*     */   protected Icon avatar;
/*     */   
/*     */   public AccountManagerImpl(SelfUser selfUser) {
/*  47 */     super(selfUser.getJDA(), Route.Self.MODIFY_SELF.compile(new String[0]));
/*  48 */     this.selfUser = selfUser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public SelfUser getSelfUser() {
/*  55 */     return this.selfUser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public AccountManagerImpl reset(long fields) {
/*  63 */     super.reset(fields);
/*  64 */     if ((fields & 0x2L) == 2L)
/*  65 */       this.avatar = null; 
/*  66 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public AccountManagerImpl reset(long... fields) {
/*  74 */     super.reset(fields);
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public AccountManagerImpl reset() {
/*  83 */     super.reset();
/*  84 */     this.avatar = null;
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public AccountManagerImpl setName(@Nonnull String name) {
/*  93 */     Checks.notBlank(name, "Name");
/*  94 */     name = name.trim();
/*  95 */     Checks.notEmpty(name, "Name");
/*  96 */     Checks.notLonger(name, 32, "Name");
/*  97 */     this.name = name;
/*  98 */     this.set |= 0x1L;
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public AccountManagerImpl setAvatar(Icon avatar) {
/* 107 */     this.avatar = avatar;
/* 108 */     this.set |= 0x2L;
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 115 */     DataObject body = DataObject.empty();
/*     */ 
/*     */     
/* 118 */     body.put("username", getSelfUser().getName());
/* 119 */     body.put("avatar", getSelfUser().getAvatarId());
/*     */     
/* 121 */     if (shouldUpdate(1L))
/* 122 */       body.put("username", this.name); 
/* 123 */     if (shouldUpdate(2L)) {
/* 124 */       body.put("avatar", (this.avatar == null) ? null : this.avatar.getEncoding());
/*     */     }
/* 126 */     reset();
/* 127 */     return getRequestBody(body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<Void> request) {
/* 133 */     String newToken = response.getObject().getString("token").replace("Bot ", "");
/* 134 */     this.api.setToken(newToken);
/* 135 */     request.onSuccess(null);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\AccountManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */