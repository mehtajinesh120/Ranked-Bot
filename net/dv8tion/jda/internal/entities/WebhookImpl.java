/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.entities.Webhook;
/*     */ import net.dv8tion.jda.api.entities.WebhookType;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.managers.WebhookManager;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.managers.WebhookManagerImpl;
/*     */ import net.dv8tion.jda.internal.requests.Requester;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.restaction.WebhookMessageActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.restaction.WebhookMessageUpdateActionImpl;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
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
/*     */ public class WebhookImpl
/*     */   extends AbstractWebhookClient<Void>
/*     */   implements Webhook
/*     */ {
/*     */   private final TextChannel channel;
/*     */   private final WebhookType type;
/*     */   private WebhookManager manager;
/*     */   private Member owner;
/*     */   private User user;
/*     */   private User ownerUser;
/*     */   private Webhook.ChannelReference sourceChannel;
/*     */   private Webhook.GuildReference sourceGuild;
/*     */   
/*     */   public WebhookImpl(TextChannel channel, long id, WebhookType type) {
/*  54 */     this(channel, channel.getJDA(), id, type);
/*     */   }
/*     */ 
/*     */   
/*     */   public WebhookImpl(TextChannel channel, JDA api, long id, WebhookType type) {
/*  59 */     super(id, (String)null, api);
/*  60 */     this.channel = channel;
/*  61 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookType getType() {
/*  68 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPartial() {
/*  74 */     return (this.channel == null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  81 */     return this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/*  88 */     if (this.channel == null)
/*  89 */       throw new IllegalStateException("Cannot provide guild for this Webhook instance because it does not belong to this shard"); 
/*  90 */     return getChannel().getGuild();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public TextChannel getChannel() {
/*  97 */     if (this.channel == null)
/*  98 */       throw new IllegalStateException("Cannot provide channel for this Webhook instance because it does not belong to this shard"); 
/*  99 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getOwner() {
/* 105 */     if (this.owner == null && this.channel != null && this.ownerUser != null)
/* 106 */       return getGuild().getMember(this.ownerUser); 
/* 107 */     return this.owner;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public User getOwnerAsUser() {
/* 113 */     return this.ownerUser;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public User getDefaultUser() {
/* 120 */     return this.user;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/* 127 */     return this.user.getName();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getToken() {
/* 133 */     return this.token;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getUrl() {
/* 140 */     return Requester.DISCORD_API_PREFIX + "webhooks/" + getId() + ((getToken() == null) ? "" : ("/" + getToken()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Webhook.ChannelReference getSourceChannel() {
/* 146 */     return this.sourceChannel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Webhook.GuildReference getSourceGuild() {
/* 152 */     return this.sourceGuild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<Void> delete() {
/* 159 */     if (this.token != null) {
/* 160 */       return delete(this.token);
/*     */     }
/* 162 */     if (!getGuild().getSelfMember().hasPermission((GuildChannel)getChannel(), new Permission[] { Permission.MANAGE_WEBHOOKS })) {
/* 163 */       throw new InsufficientPermissionException(getChannel(), Permission.MANAGE_WEBHOOKS);
/*     */     }
/* 165 */     Route.CompiledRoute route = Route.Webhooks.DELETE_WEBHOOK.compile(new String[] { getId() });
/* 166 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl(getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<Void> delete(@Nonnull String token) {
/* 173 */     Checks.notNull(token, "Token");
/* 174 */     Route.CompiledRoute route = Route.Webhooks.DELETE_TOKEN_WEBHOOK.compile(new String[] { getId(), token });
/* 175 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl(getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public WebhookManager getManager() {
/* 182 */     if (this.manager == null)
/* 183 */       return this.manager = (WebhookManager)new WebhookManagerImpl(this); 
/* 184 */     return this.manager;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 190 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebhookImpl setOwner(Member member, User user) {
/* 197 */     this.owner = member;
/* 198 */     this.ownerUser = user;
/* 199 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WebhookImpl setToken(String token) {
/* 204 */     this.token = token;
/* 205 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WebhookImpl setUser(User user) {
/* 210 */     this.user = user;
/* 211 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WebhookImpl setSourceGuild(Webhook.GuildReference reference) {
/* 216 */     this.sourceGuild = reference;
/* 217 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public WebhookImpl setSourceChannel(Webhook.ChannelReference reference) {
/* 222 */     this.sourceChannel = reference;
/* 223 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 231 */     return Long.hashCode(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 237 */     if (obj == this)
/* 238 */       return true; 
/* 239 */     if (!(obj instanceof WebhookImpl))
/* 240 */       return false; 
/* 241 */     WebhookImpl impl = (WebhookImpl)obj;
/* 242 */     return (impl.id == this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 248 */     return "WH:" + getName() + "(" + this.id + ")";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WebhookMessageActionImpl<Void> sendRequest() {
/* 256 */     checkToken();
/* 257 */     Route.CompiledRoute route = Route.Webhooks.EXECUTE_WEBHOOK.compile(new String[] { getId(), this.token });
/* 258 */     WebhookMessageActionImpl<Void> action = new WebhookMessageActionImpl(this.api, (MessageChannel)this.channel, route, json -> null);
/* 259 */     action.run();
/* 260 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public WebhookMessageUpdateActionImpl<Void> editRequest(String messageId) {
/* 266 */     checkToken();
/* 267 */     Checks.isSnowflake(messageId);
/* 268 */     Route.CompiledRoute route = Route.Webhooks.EXECUTE_WEBHOOK_EDIT.compile(new String[] { getId(), this.token, messageId });
/* 269 */     WebhookMessageUpdateActionImpl<Void> action = new WebhookMessageUpdateActionImpl(this.api, route, json -> null);
/* 270 */     action.run();
/* 271 */     return action;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<Void> deleteMessageById(@Nonnull String messageId) {
/* 278 */     checkToken();
/* 279 */     return super.deleteMessageById(messageId);
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkToken() {
/* 284 */     if (this.token == null)
/* 285 */       throw new UnsupportedOperationException("Cannot execute webhook without a token!"); 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\WebhookImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */