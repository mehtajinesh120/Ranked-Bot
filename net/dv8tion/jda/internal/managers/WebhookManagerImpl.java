/*     */ package net.dv8tion.jda.internal.managers;
/*     */ 
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.Icon;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.Webhook;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.exceptions.MissingAccessException;
/*     */ import net.dv8tion.jda.api.managers.Manager;
/*     */ import net.dv8tion.jda.api.managers.WebhookManager;
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
/*     */ public class WebhookManagerImpl
/*     */   extends ManagerBase<WebhookManager>
/*     */   implements WebhookManager
/*     */ {
/*     */   protected final Webhook webhook;
/*     */   protected String name;
/*     */   protected String channel;
/*     */   protected Icon avatar;
/*     */   
/*     */   public WebhookManagerImpl(Webhook webhook) {
/*  50 */     super(webhook.getJDA(), Route.Webhooks.MODIFY_WEBHOOK.compile(new String[] { webhook.getId() }));
/*  51 */     this.webhook = webhook;
/*  52 */     if (isPermissionChecksEnabled()) {
/*  53 */       checkPermissions();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Webhook getWebhook() {
/*  60 */     return this.webhook;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public WebhookManagerImpl reset(long fields) {
/*  68 */     super.reset(fields);
/*  69 */     if ((fields & 0x1L) == 1L)
/*  70 */       this.name = null; 
/*  71 */     if ((fields & 0x2L) == 2L)
/*  72 */       this.channel = null; 
/*  73 */     if ((fields & 0x4L) == 4L)
/*  74 */       this.avatar = null; 
/*  75 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public WebhookManagerImpl reset(long... fields) {
/*  83 */     super.reset(fields);
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public WebhookManagerImpl reset() {
/*  92 */     super.reset();
/*  93 */     this.name = null;
/*  94 */     this.channel = null;
/*  95 */     this.avatar = null;
/*  96 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public WebhookManagerImpl setName(@Nonnull String name) {
/* 104 */     Checks.notBlank(name, "Name");
/* 105 */     this.name = name;
/* 106 */     this.set |= 0x1L;
/* 107 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public WebhookManagerImpl setAvatar(Icon icon) {
/* 115 */     this.avatar = icon;
/* 116 */     this.set |= 0x4L;
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public WebhookManagerImpl setChannel(@Nonnull TextChannel channel) {
/* 125 */     Checks.notNull(channel, "Channel");
/* 126 */     Checks.check(channel.getGuild().equals(getGuild()), "Channel is not from the same guild");
/* 127 */     this.channel = channel.getId();
/* 128 */     this.set |= 0x2L;
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 135 */     DataObject data = DataObject.empty();
/* 136 */     if (shouldUpdate(1L))
/* 137 */       data.put("name", this.name); 
/* 138 */     if (shouldUpdate(2L))
/* 139 */       data.put("channel_id", this.channel); 
/* 140 */     if (shouldUpdate(4L)) {
/* 141 */       data.put("avatar", (this.avatar == null) ? null : this.avatar.getEncoding());
/*     */     }
/* 143 */     return getRequestBody(data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkPermissions() {
/* 149 */     Member selfMember = getGuild().getSelfMember();
/* 150 */     TextChannel channel = getChannel();
/* 151 */     if (!selfMember.hasAccess((GuildChannel)channel))
/* 152 */       throw new MissingAccessException(channel, Permission.VIEW_CHANNEL); 
/* 153 */     if (!selfMember.hasPermission((GuildChannel)channel, new Permission[] { Permission.MANAGE_WEBHOOKS }))
/* 154 */       throw new InsufficientPermissionException(channel, Permission.MANAGE_WEBHOOKS); 
/* 155 */     return super.checkPermissions();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\WebhookManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */