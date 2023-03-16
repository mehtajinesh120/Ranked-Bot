/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Invite;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.InviteAction;
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
/*     */ public class InviteActionImpl
/*     */   extends AuditableRestActionImpl<Invite>
/*     */   implements InviteAction
/*     */ {
/*  37 */   private Integer maxAge = null;
/*  38 */   private Integer maxUses = null;
/*  39 */   private Boolean temporary = null;
/*  40 */   private Boolean unique = null;
/*  41 */   private Long targetApplication = null;
/*  42 */   private Long targetUser = null;
/*  43 */   private Invite.TargetType targetType = null;
/*     */ 
/*     */   
/*     */   public InviteActionImpl(JDA api, String channelId) {
/*  47 */     super(api, Route.Invites.CREATE_INVITE.compile(new String[] { channelId }));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public InviteActionImpl setCheck(BooleanSupplier checks) {
/*  54 */     return (InviteActionImpl)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public InviteActionImpl timeout(long timeout, @Nonnull TimeUnit unit) {
/*  61 */     return (InviteActionImpl)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public InviteActionImpl deadline(long timestamp) {
/*  68 */     return (InviteActionImpl)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public InviteActionImpl setMaxAge(Integer maxAge) {
/*  76 */     if (maxAge != null) {
/*  77 */       Checks.notNegative(maxAge.intValue(), "maxAge");
/*     */     }
/*  79 */     this.maxAge = maxAge;
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public InviteActionImpl setMaxAge(Long maxAge, @Nonnull TimeUnit timeUnit) {
/*  88 */     if (maxAge == null) {
/*  89 */       return setMaxAge((Integer)null);
/*     */     }
/*  91 */     Checks.notNegative(maxAge.longValue(), "maxAge");
/*  92 */     Checks.notNull(timeUnit, "timeUnit");
/*     */     
/*  94 */     return setMaxAge(Integer.valueOf(Math.toIntExact(timeUnit.toSeconds(maxAge.longValue()))));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public InviteActionImpl setMaxUses(Integer maxUses) {
/* 102 */     if (maxUses != null) {
/* 103 */       Checks.notNegative(maxUses.intValue(), "maxUses");
/*     */     }
/* 105 */     this.maxUses = maxUses;
/* 106 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public InviteActionImpl setTemporary(Boolean temporary) {
/* 114 */     this.temporary = temporary;
/* 115 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public InviteActionImpl setUnique(Boolean unique) {
/* 123 */     this.unique = unique;
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public InviteAction setTargetApplication(long applicationId) {
/* 131 */     if (applicationId == 0L) {
/*     */       
/* 133 */       this.targetType = null;
/* 134 */       this.targetApplication = null;
/* 135 */       return this;
/*     */     } 
/*     */     
/* 138 */     this.targetType = Invite.TargetType.EMBEDDED_APPLICATION;
/* 139 */     this.targetApplication = Long.valueOf(applicationId);
/* 140 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public InviteAction setTargetStream(long userId) {
/* 147 */     if (userId == 0L) {
/*     */       
/* 149 */       this.targetType = null;
/* 150 */       this.targetUser = null;
/* 151 */       return this;
/*     */     } 
/*     */     
/* 154 */     this.targetType = Invite.TargetType.STREAM;
/* 155 */     this.targetUser = Long.valueOf(userId);
/* 156 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 162 */     DataObject object = DataObject.empty();
/*     */     
/* 164 */     if (this.maxAge != null)
/* 165 */       object.put("max_age", this.maxAge); 
/* 166 */     if (this.maxUses != null)
/* 167 */       object.put("max_uses", this.maxUses); 
/* 168 */     if (this.temporary != null)
/* 169 */       object.put("temporary", this.temporary); 
/* 170 */     if (this.unique != null)
/* 171 */       object.put("unique", this.unique); 
/* 172 */     if (this.targetType != null)
/* 173 */       object.put("target_type", Integer.valueOf(this.targetType.getId())); 
/* 174 */     if (this.targetUser != null)
/* 175 */       object.put("target_user_id", this.targetUser); 
/* 176 */     if (this.targetApplication != null) {
/* 177 */       object.put("target_application_id", this.targetApplication);
/*     */     }
/* 179 */     return getRequestBody(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<Invite> request) {
/* 185 */     request.onSuccess(this.api.getEntityBuilder().createInvite(response.getObject()));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\InviteActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */