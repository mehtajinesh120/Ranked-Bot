/*     */ package net.dv8tion.jda.internal.managers;
/*     */ 
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.templates.Template;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.managers.Manager;
/*     */ import net.dv8tion.jda.api.managers.TemplateManager;
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
/*     */ public class TemplateManagerImpl
/*     */   extends ManagerBase<TemplateManager>
/*     */   implements TemplateManager
/*     */ {
/*     */   protected final Template template;
/*     */   protected final JDA api;
/*     */   protected String name;
/*     */   protected String description;
/*     */   
/*     */   public TemplateManagerImpl(Template template) {
/*  50 */     super(template.getJDA(), Route.Templates.MODIFY_TEMPLATE.compile(new String[] { template.getGuild().getId(), template.getCode() }));
/*  51 */     this.template = template;
/*  52 */     this.api = template.getJDA();
/*  53 */     if (isPermissionChecksEnabled()) {
/*  54 */       checkPermissions();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public TemplateManagerImpl reset(long fields) {
/*  62 */     super.reset(fields);
/*  63 */     if ((fields & 0x1L) == 1L)
/*  64 */       this.name = null; 
/*  65 */     if ((fields & 0x2L) == 2L)
/*  66 */       this.description = null; 
/*  67 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public TemplateManagerImpl reset(long... fields) {
/*  75 */     super.reset(fields);
/*  76 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public TemplateManagerImpl reset() {
/*  84 */     super.reset();
/*  85 */     this.name = null;
/*  86 */     this.description = null;
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public TemplateManagerImpl setName(@Nonnull String name) {
/*  95 */     Checks.notEmpty(name, "Name");
/*  96 */     Checks.notLonger(name, 100, "Name");
/*  97 */     this.name = name;
/*  98 */     this.set |= 0x1L;
/*  99 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public TemplateManagerImpl setDescription(@Nullable String description) {
/* 107 */     if (description != null)
/* 108 */       Checks.notLonger(this.name, 120, "Description"); 
/* 109 */     this.description = description;
/* 110 */     this.set |= 0x2L;
/* 111 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 117 */     DataObject body = DataObject.empty();
/* 118 */     if (shouldUpdate(1L))
/* 119 */       body.put("name", this.name); 
/* 120 */     if (shouldUpdate(2L)) {
/* 121 */       body.put("description", this.name);
/*     */     }
/* 123 */     reset();
/* 124 */     return getRequestBody(body);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkPermissions() {
/* 130 */     Guild guild = this.api.getGuildById(this.template.getGuild().getIdLong());
/*     */     
/* 132 */     if (guild == null)
/* 133 */       return true; 
/* 134 */     if (!guild.getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_SERVER }))
/* 135 */       throw new InsufficientPermissionException(guild, Permission.MANAGE_SERVER); 
/* 136 */     return super.checkPermissions();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\TemplateManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */