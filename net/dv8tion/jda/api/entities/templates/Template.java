/*     */ package net.dv8tion.jda.api.entities.templates;
/*     */ 
/*     */ import java.time.OffsetDateTime;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.managers.TemplateManager;
/*     */ import net.dv8tion.jda.api.requests.Request;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.managers.TemplateManagerImpl;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Template
/*     */ {
/*     */   private final JDAImpl api;
/*     */   private final String code;
/*     */   private final String name;
/*     */   private final String description;
/*     */   private final int uses;
/*     */   private final User creator;
/*     */   private final OffsetDateTime createdAt;
/*     */   private final OffsetDateTime updatedAt;
/*     */   private final TemplateGuild guild;
/*     */   private final boolean synced;
/*     */   protected TemplateManager manager;
/*     */   
/*     */   public Template(JDAImpl api, String code, String name, String description, int uses, User creator, OffsetDateTime createdAt, OffsetDateTime updatedAt, TemplateGuild guild, boolean synced) {
/*  64 */     this.api = api;
/*  65 */     this.code = code;
/*  66 */     this.name = name;
/*  67 */     this.description = description;
/*  68 */     this.uses = uses;
/*  69 */     this.creator = creator;
/*  70 */     this.createdAt = createdAt;
/*  71 */     this.updatedAt = updatedAt;
/*  72 */     this.guild = guild;
/*  73 */     this.synced = synced;
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
/*     */   public static RestAction<Template> resolve(JDA api, String code) {
/* 103 */     Checks.notEmpty(code, "code");
/* 104 */     Checks.noWhitespace(code, "code");
/* 105 */     Checks.notNull(api, "api");
/*     */     
/* 107 */     Route.CompiledRoute route = Route.Templates.GET_TEMPLATE.compile(new String[] { code });
/*     */     
/* 109 */     JDAImpl jda = (JDAImpl)api;
/* 110 */     return (RestAction<Template>)new RestActionImpl(api, route, (response, request) -> jda.getEntityBuilder().createTemplate(response.getObject()));
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
/*     */   @CheckReturnValue
/*     */   public RestAction<Template> sync() {
/* 131 */     checkInteraction();
/* 132 */     Route.CompiledRoute route = Route.Templates.SYNC_TEMPLATE.compile(new String[] { this.guild.getId(), this.code });
/* 133 */     return (RestAction<Template>)new RestActionImpl((JDA)this.api, route, (response, request) -> this.api.getEntityBuilder().createTemplate(response.getObject()));
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
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RestAction<Void> delete() {
/* 153 */     checkInteraction();
/* 154 */     Route.CompiledRoute route = Route.Templates.DELETE_TEMPLATE.compile(new String[] { this.guild.getId(), this.code });
/* 155 */     return (RestAction<Void>)new RestActionImpl((JDA)this.api, route);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getCode() {
/* 166 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/* 177 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getDescription() {
/* 188 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getUses() {
/* 198 */     return this.uses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public User getCreator() {
/* 209 */     return this.creator;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OffsetDateTime getTimeCreated() {
/* 220 */     return this.createdAt;
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
/*     */   public OffsetDateTime getTimeUpdated() {
/* 234 */     return this.updatedAt;
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
/*     */   public TemplateGuild getGuild() {
/* 248 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSynced() {
/* 258 */     return this.synced;
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
/*     */   @Nonnull
/*     */   public TemplateManager getManager() {
/* 279 */     checkInteraction();
/* 280 */     if (this.manager == null)
/* 281 */       return this.manager = (TemplateManager)new TemplateManagerImpl(this); 
/* 282 */     return this.manager;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkInteraction() {
/* 287 */     Guild guild = this.api.getGuildById(this.guild.getIdLong());
/*     */     
/* 289 */     if (guild == null)
/* 290 */       throw new IllegalStateException("Cannot interact with a template without shared guild"); 
/* 291 */     if (!guild.getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_SERVER })) {
/* 292 */       throw new InsufficientPermissionException(guild, Permission.MANAGE_SERVER);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/* 303 */     return (JDA)this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 309 */     return this.code.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 315 */     if (obj == this)
/* 316 */       return true; 
/* 317 */     if (!(obj instanceof Template))
/* 318 */       return false; 
/* 319 */     Template impl = (Template)obj;
/* 320 */     return impl.code.equals(this.code);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 326 */     return "Template(" + this.code + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\templates\Template.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */