/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Icon;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.RoleAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
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
/*     */ public class RoleActionImpl
/*     */   extends AuditableRestActionImpl<Role>
/*     */   implements RoleAction
/*     */ {
/*     */   protected final Guild guild;
/*     */   protected Long permissions;
/*  43 */   protected String name = null;
/*  44 */   protected Integer color = null;
/*  45 */   protected Boolean hoisted = null;
/*  46 */   protected Boolean mentionable = null;
/*  47 */   protected Icon icon = null;
/*  48 */   protected String emoji = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RoleActionImpl(Guild guild) {
/*  58 */     super(guild.getJDA(), Route.Roles.CREATE_ROLE.compile(new String[] { guild.getId() }));
/*  59 */     this.guild = guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RoleActionImpl setCheck(BooleanSupplier checks) {
/*  66 */     return (RoleActionImpl)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RoleActionImpl timeout(long timeout, @Nonnull TimeUnit unit) {
/*  73 */     return (RoleActionImpl)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RoleActionImpl deadline(long timestamp) {
/*  80 */     return (RoleActionImpl)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/*  87 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleActionImpl setName(String name) {
/*  95 */     if (name != null) {
/*     */       
/*  97 */       Checks.notEmpty(name, "Name");
/*  98 */       Checks.notLonger(name, 100, "Name");
/*     */     } 
/* 100 */     this.name = name;
/* 101 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleActionImpl setHoisted(Boolean hoisted) {
/* 109 */     this.hoisted = hoisted;
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleActionImpl setMentionable(Boolean mentionable) {
/* 118 */     this.mentionable = mentionable;
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleActionImpl setColor(Integer rgb) {
/* 127 */     this.color = rgb;
/* 128 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleActionImpl setPermissions(Long permissions) {
/* 136 */     if (permissions != null)
/*     */     {
/* 138 */       for (Permission p : Permission.getPermissions(permissions.longValue()))
/* 139 */         checkPermission(p); 
/*     */     }
/* 141 */     this.permissions = permissions;
/* 142 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleActionImpl setIcon(Icon icon) {
/* 150 */     this.icon = icon;
/* 151 */     this.emoji = null;
/* 152 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleActionImpl setIcon(String emoji) {
/* 160 */     this.emoji = emoji;
/* 161 */     this.icon = null;
/* 162 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 168 */     DataObject object = DataObject.empty();
/* 169 */     if (this.name != null)
/* 170 */       object.put("name", this.name); 
/* 171 */     if (this.color != null)
/* 172 */       object.put("color", Integer.valueOf(this.color.intValue() & 0xFFFFFF)); 
/* 173 */     if (this.permissions != null)
/* 174 */       object.put("permissions", this.permissions); 
/* 175 */     if (this.hoisted != null)
/* 176 */       object.put("hoist", this.hoisted); 
/* 177 */     if (this.mentionable != null)
/* 178 */       object.put("mentionable", this.mentionable); 
/* 179 */     if (this.icon != null)
/* 180 */       object.put("icon", this.icon.getEncoding()); 
/* 181 */     if (this.emoji != null) {
/* 182 */       object.put("unicode_emoji", this.emoji);
/*     */     }
/* 184 */     return getRequestBody(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleSuccess(Response response, Request<Role> request) {
/* 190 */     request.onSuccess(this.api.getEntityBuilder().createRole((GuildImpl)this.guild, response.getObject(), this.guild.getIdLong()));
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkPermission(Permission permission) {
/* 195 */     if (!this.guild.getSelfMember().hasPermission(new Permission[] { permission }))
/* 196 */       throw new InsufficientPermissionException(this.guild, permission); 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\RoleActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */