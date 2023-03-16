/*     */ package net.dv8tion.jda.internal.managers;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Icon;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.exceptions.HierarchyException;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.managers.Manager;
/*     */ import net.dv8tion.jda.api.managers.RoleManager;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.PermissionUtil;
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
/*     */ public class RoleManagerImpl
/*     */   extends ManagerBase<RoleManager>
/*     */   implements RoleManager
/*     */ {
/*     */   protected Role role;
/*     */   protected String name;
/*     */   protected int color;
/*     */   protected long permissions;
/*     */   protected boolean hoist;
/*     */   protected boolean mentionable;
/*     */   protected Icon icon;
/*     */   protected String emoji;
/*     */   
/*     */   public RoleManagerImpl(Role role) {
/*  58 */     super(role.getJDA(), Route.Roles.MODIFY_ROLE.compile(new String[] { role.getGuild().getId(), role.getId() }));
/*  59 */     JDA api = role.getJDA();
/*  60 */     this.role = role;
/*  61 */     if (isPermissionChecksEnabled()) {
/*  62 */       checkPermissions();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Role getRole() {
/*  69 */     Role realRole = this.role.getGuild().getRoleById(this.role.getIdLong());
/*  70 */     if (realRole != null)
/*  71 */       this.role = realRole; 
/*  72 */     return this.role;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl reset(long fields) {
/*  80 */     super.reset(fields);
/*  81 */     if ((fields & 0x1L) == 1L)
/*  82 */       this.name = null; 
/*  83 */     if ((fields & 0x2L) == 2L)
/*  84 */       this.color = 536870911; 
/*  85 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl reset(long... fields) {
/*  93 */     super.reset(fields);
/*  94 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl reset() {
/* 102 */     super.reset();
/* 103 */     this.name = null;
/* 104 */     this.color = 536870911;
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl setName(@Nonnull String name) {
/* 113 */     Checks.notBlank(name, "Name");
/* 114 */     name = name.trim();
/* 115 */     Checks.notEmpty(name, "Name");
/* 116 */     Checks.notLonger(name, 100, "Name");
/* 117 */     this.name = name;
/* 118 */     this.set |= 0x1L;
/* 119 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl setPermissions(long perms) {
/* 127 */     long selfPermissions = PermissionUtil.getEffectivePermission(getGuild().getSelfMember());
/* 128 */     setupPermissions();
/* 129 */     long missingPerms = perms;
/* 130 */     missingPerms &= selfPermissions ^ 0xFFFFFFFFFFFFFFFFL;
/* 131 */     missingPerms &= this.permissions ^ 0xFFFFFFFFFFFFFFFFL;
/*     */     
/* 133 */     if (missingPerms != 0L && isPermissionChecksEnabled()) {
/*     */       
/* 135 */       EnumSet<Permission> permissionList = Permission.getPermissions(missingPerms);
/* 136 */       if (!permissionList.isEmpty())
/* 137 */         throw new InsufficientPermissionException(getGuild(), (Permission)permissionList.iterator().next()); 
/*     */     } 
/* 139 */     this.permissions = perms;
/* 140 */     this.set |= 0x4L;
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl setColor(int rgb) {
/* 149 */     this.color = rgb;
/* 150 */     this.set |= 0x2L;
/* 151 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl setHoisted(boolean hoisted) {
/* 159 */     this.hoist = hoisted;
/* 160 */     this.set |= 0x8L;
/* 161 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl setMentionable(boolean mentionable) {
/* 169 */     this.mentionable = mentionable;
/* 170 */     this.set |= 0x10L;
/* 171 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl setIcon(Icon icon) {
/* 179 */     this.icon = icon;
/* 180 */     this.emoji = null;
/* 181 */     this.set |= 0x20L;
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl setIcon(String emoji) {
/* 190 */     this.emoji = emoji;
/* 191 */     this.icon = null;
/* 192 */     this.set |= 0x20L;
/* 193 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl givePermissions(@Nonnull Collection<Permission> perms) {
/* 201 */     Checks.noneNull(perms, "Permissions");
/* 202 */     setupPermissions();
/* 203 */     return setPermissions(this.permissions | Permission.getRaw(perms));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public RoleManagerImpl revokePermissions(@Nonnull Collection<Permission> perms) {
/* 211 */     Checks.noneNull(perms, "Permissions");
/* 212 */     setupPermissions();
/* 213 */     return setPermissions(this.permissions & (Permission.getRaw(perms) ^ 0xFFFFFFFFFFFFFFFFL));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 219 */     DataObject object = DataObject.empty().put("name", getRole().getName());
/* 220 */     if (shouldUpdate(1L))
/* 221 */       object.put("name", this.name); 
/* 222 */     if (shouldUpdate(4L))
/* 223 */       object.put("permissions", Long.valueOf(this.permissions)); 
/* 224 */     if (shouldUpdate(8L))
/* 225 */       object.put("hoist", Boolean.valueOf(this.hoist)); 
/* 226 */     if (shouldUpdate(16L))
/* 227 */       object.put("mentionable", Boolean.valueOf(this.mentionable)); 
/* 228 */     if (shouldUpdate(2L))
/* 229 */       object.put("color", Integer.valueOf((this.color == 536870911) ? 0 : (this.color & 0xFFFFFF))); 
/* 230 */     if (shouldUpdate(32L)) {
/*     */       
/* 232 */       object.put("icon", (this.icon == null) ? null : this.icon.getEncoding());
/* 233 */       object.put("unicode_emoji", this.emoji);
/*     */     } 
/* 235 */     reset();
/* 236 */     return getRequestBody(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkPermissions() {
/* 242 */     Member selfMember = getGuild().getSelfMember();
/* 243 */     if (!selfMember.hasPermission(new Permission[] { Permission.MANAGE_ROLES }))
/* 244 */       throw new InsufficientPermissionException(getGuild(), Permission.MANAGE_ROLES); 
/* 245 */     if (!selfMember.canInteract(getRole()))
/* 246 */       throw new HierarchyException("Cannot modify a role that is higher or equal in hierarchy"); 
/* 247 */     return super.checkPermissions();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setupPermissions() {
/* 252 */     if (!shouldUpdate(4L))
/* 253 */       this.permissions = getRole().getPermissionsRaw(); 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\RoleManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */