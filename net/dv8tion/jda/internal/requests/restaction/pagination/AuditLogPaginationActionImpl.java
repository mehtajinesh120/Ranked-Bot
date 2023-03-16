/*     */ package net.dv8tion.jda.internal.requests.restaction.pagination;
/*     */ 
/*     */ import gnu.trove.map.hash.TLongObjectHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.audit.ActionType;
/*     */ import net.dv8tion.jda.api.audit.AuditLogEntry;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.exceptions.ParsingException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.restaction.pagination.AuditLogPaginationAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AuditLogPaginationActionImpl
/*     */   extends PaginationActionImpl<AuditLogEntry, AuditLogPaginationAction>
/*     */   implements AuditLogPaginationAction
/*     */ {
/*     */   protected final Guild guild;
/*  48 */   protected ActionType type = null;
/*  49 */   protected String userId = null;
/*     */ 
/*     */   
/*     */   public AuditLogPaginationActionImpl(Guild guild) {
/*  53 */     super(guild.getJDA(), Route.Guilds.GET_AUDIT_LOGS.compile(new String[] { guild.getId() }, ), 1, 100, 100);
/*  54 */     if (!guild.getSelfMember().hasPermission(new Permission[] { Permission.VIEW_AUDIT_LOGS }))
/*  55 */       throw new InsufficientPermissionException(guild, Permission.VIEW_AUDIT_LOGS); 
/*  56 */     this.guild = guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditLogPaginationActionImpl type(ActionType type) {
/*  63 */     this.type = type;
/*  64 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditLogPaginationActionImpl user(User user) {
/*  71 */     return user((user == null) ? null : user.getId());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditLogPaginationActionImpl user(String userId) {
/*  78 */     if (userId != null)
/*  79 */       Checks.isSnowflake(userId, "User ID"); 
/*  80 */     this.userId = userId;
/*  81 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditLogPaginationActionImpl user(long userId) {
/*  88 */     return user(Long.toUnsignedString(userId));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/*  95 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Route.CompiledRoute finalizeRoute() {
/* 101 */     Route.CompiledRoute route = super.finalizeRoute();
/*     */     
/* 103 */     String limit = String.valueOf(this.limit.get());
/* 104 */     long last = this.lastKey;
/*     */     
/* 106 */     route = route.withQueryParams(new String[] { "limit", limit });
/*     */     
/* 108 */     if (this.type != null) {
/* 109 */       route = route.withQueryParams(new String[] { "action_type", String.valueOf(this.type.getKey()) });
/*     */     }
/* 111 */     if (this.userId != null) {
/* 112 */       route = route.withQueryParams(new String[] { "user_id", this.userId });
/*     */     }
/* 114 */     if (last != 0L) {
/* 115 */       route = route.withQueryParams(new String[] { "before", Long.toUnsignedString(last) });
/*     */     }
/* 117 */     return route;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<List<AuditLogEntry>> request) {
/* 123 */     DataObject obj = response.getObject();
/* 124 */     DataArray users = obj.getArray("users");
/* 125 */     DataArray webhooks = obj.getArray("webhooks");
/* 126 */     DataArray entries = obj.getArray("audit_log_entries");
/*     */     
/* 128 */     List<AuditLogEntry> list = new ArrayList<>(entries.length());
/* 129 */     EntityBuilder builder = this.api.getEntityBuilder();
/*     */     
/* 131 */     TLongObjectHashMap tLongObjectHashMap1 = new TLongObjectHashMap();
/* 132 */     for (int i = 0; i < users.length(); i++) {
/*     */       
/* 134 */       DataObject user = users.getObject(i);
/* 135 */       tLongObjectHashMap1.put(user.getLong("id"), user);
/*     */     } 
/*     */     
/* 138 */     TLongObjectHashMap tLongObjectHashMap2 = new TLongObjectHashMap(); int j;
/* 139 */     for (j = 0; j < webhooks.length(); j++) {
/*     */       
/* 141 */       DataObject webhook = webhooks.getObject(j);
/* 142 */       tLongObjectHashMap2.put(webhook.getLong("id"), webhook);
/*     */     } 
/*     */     
/* 145 */     for (j = 0; j < entries.length(); j++) {
/*     */ 
/*     */       
/*     */       try {
/* 149 */         DataObject entry = entries.getObject(j);
/* 150 */         DataObject user = (DataObject)tLongObjectHashMap1.get(entry.getLong("user_id", 0L));
/* 151 */         DataObject webhook = (DataObject)tLongObjectHashMap2.get(entry.getLong("target_id", 0L));
/* 152 */         AuditLogEntry result = builder.createAuditLogEntry((GuildImpl)this.guild, entry, user, webhook);
/* 153 */         list.add(result);
/* 154 */         if (this.useCache)
/* 155 */           this.cached.add(result); 
/* 156 */         this.last = result;
/* 157 */         this.lastKey = this.last.getIdLong();
/*     */       }
/* 159 */       catch (ParsingException|NullPointerException e) {
/*     */         
/* 161 */         LOG.warn("Encountered exception in AuditLogPagination", e);
/*     */       } 
/*     */     } 
/*     */     
/* 165 */     request.onSuccess(list);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected long getKey(AuditLogEntry it) {
/* 171 */     return it.getIdLong();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\pagination\AuditLogPaginationActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */