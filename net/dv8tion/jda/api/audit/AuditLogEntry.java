/*     */ package net.dv8tion.jda.api.audit;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.entities.Webhook;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.entities.UserImpl;
/*     */ import net.dv8tion.jda.internal.entities.WebhookImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AuditLogEntry
/*     */   implements ISnowflake
/*     */ {
/*     */   protected final long id;
/*     */   protected final long targetId;
/*     */   protected final GuildImpl guild;
/*     */   protected final UserImpl user;
/*     */   protected final WebhookImpl webhook;
/*     */   protected final String reason;
/*     */   protected final Map<String, AuditLogChange> changes;
/*     */   protected final Map<String, Object> options;
/*     */   protected final ActionType type;
/*     */   protected final int rawType;
/*     */   
/*     */   public AuditLogEntry(ActionType type, int rawType, long id, long targetId, GuildImpl guild, UserImpl user, WebhookImpl webhook, String reason, Map<String, AuditLogChange> changes, Map<String, Object> options) {
/*  58 */     this.rawType = rawType;
/*  59 */     this.type = type;
/*  60 */     this.id = id;
/*  61 */     this.targetId = targetId;
/*  62 */     this.guild = guild;
/*  63 */     this.user = user;
/*  64 */     this.webhook = webhook;
/*  65 */     this.reason = reason;
/*  66 */     this
/*     */       
/*  68 */       .changes = (changes != null && !changes.isEmpty()) ? Collections.<String, AuditLogChange>unmodifiableMap(changes) : Collections.<String, AuditLogChange>emptyMap();
/*  69 */     this
/*     */       
/*  71 */       .options = (options != null && !options.isEmpty()) ? Collections.<String, Object>unmodifiableMap(options) : Collections.<String, Object>emptyMap();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/*  77 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getTargetIdLong() {
/*  89 */     return this.targetId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getTargetId() {
/* 102 */     return Long.toUnsignedString(this.targetId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Webhook getWebhook() {
/* 113 */     return (Webhook)this.webhook;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/* 124 */     return (Guild)this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public User getUser() {
/* 136 */     return (User)this.user;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getReason() {
/* 147 */     return this.reason;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/* 158 */     return (JDA)this.guild.getJDA();
/*     */   }
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
/*     */   @Nonnull
/*     */   public Map<String, AuditLogChange> getChanges() {
/* 172 */     return this.changes;
/*     */   }
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
/*     */   @Nullable
/*     */   public AuditLogChange getChangeByKey(@Nullable AuditLogKey key) {
/* 187 */     return (key == null) ? null : getChangeByKey(key.getKey());
/*     */   }
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
/*     */   @Nullable
/*     */   public AuditLogChange getChangeByKey(@Nullable String key) {
/* 202 */     return this.changes.get(key);
/*     */   }
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
/*     */   @Nonnull
/*     */   public List<AuditLogChange> getChangesForKeys(@Nonnull AuditLogKey... keys) {
/* 219 */     Checks.notNull(keys, "Keys");
/* 220 */     List<AuditLogChange> changes = new ArrayList<>(keys.length);
/* 221 */     for (AuditLogKey key : keys) {
/*     */       
/* 223 */       AuditLogChange change = getChangeByKey(key);
/* 224 */       if (change != null)
/* 225 */         changes.add(change); 
/*     */     } 
/* 227 */     return Collections.unmodifiableList(changes);
/*     */   }
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
/*     */   @Nonnull
/*     */   public Map<String, Object> getOptions() {
/* 245 */     return this.options;
/*     */   }
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
/*     */   @Nullable
/*     */   public <T> T getOptionByName(@Nullable String name) {
/* 266 */     return (T)this.options.get(name);
/*     */   }
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
/*     */   @Nullable
/*     */   public <T> T getOption(@Nonnull AuditLogOption option) {
/* 287 */     Checks.notNull(option, "Option");
/* 288 */     return getOptionByName(option.getKey());
/*     */   }
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
/*     */   @Nonnull
/*     */   public List<Object> getOptions(@Nonnull AuditLogOption... options) {
/* 308 */     Checks.notNull(options, "Options");
/* 309 */     List<Object> items = new ArrayList(options.length);
/* 310 */     for (AuditLogOption option : options) {
/*     */       
/* 312 */       Object obj = getOption(option);
/* 313 */       if (obj != null)
/* 314 */         items.add(obj); 
/*     */     } 
/* 316 */     return Collections.unmodifiableList(items);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ActionType getType() {
/* 328 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTypeRaw() {
/* 339 */     return this.rawType;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public TargetType getTargetType() {
/* 352 */     return this.type.getTargetType();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 358 */     return Long.hashCode(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 364 */     if (!(obj instanceof AuditLogEntry))
/* 365 */       return false; 
/* 366 */     AuditLogEntry other = (AuditLogEntry)obj;
/* 367 */     return (other.id == this.id && other.targetId == this.targetId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 373 */     return "ALE:" + this.type + "(ID:" + this.id + " / TID:" + this.targetId + " / " + this.guild + ')';
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audit\AuditLogEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */