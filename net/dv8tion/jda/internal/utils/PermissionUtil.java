/*     */ package net.dv8tion.jda.internal.utils;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.ChannelType;
/*     */ import net.dv8tion.jda.api.entities.Emote;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.MessageChannel;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.TextChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import org.apache.commons.collections4.CollectionUtils;
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
/*     */ public class PermissionUtil
/*     */ {
/*     */   public static boolean canInteract(Member issuer, Member target) {
/*  45 */     Checks.notNull(issuer, "Issuer Member");
/*  46 */     Checks.notNull(target, "Target Member");
/*     */     
/*  48 */     Guild guild = issuer.getGuild();
/*  49 */     if (!guild.equals(target.getGuild()))
/*  50 */       throw new IllegalArgumentException("Provided members must both be Member objects of the same Guild!"); 
/*  51 */     if (issuer.isOwner())
/*  52 */       return true; 
/*  53 */     if (target.isOwner())
/*  54 */       return false; 
/*  55 */     List<Role> issuerRoles = issuer.getRoles();
/*  56 */     List<Role> targetRoles = target.getRoles();
/*  57 */     return (!issuerRoles.isEmpty() && (targetRoles.isEmpty() || canInteract(issuerRoles.get(0), targetRoles.get(0))));
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
/*     */   public static boolean canInteract(Member issuer, Role target) {
/*  77 */     Checks.notNull(issuer, "Issuer Member");
/*  78 */     Checks.notNull(target, "Target Role");
/*     */     
/*  80 */     Guild guild = issuer.getGuild();
/*  81 */     if (!guild.equals(target.getGuild()))
/*  82 */       throw new IllegalArgumentException("Provided Member issuer and Role target must be from the same Guild!"); 
/*  83 */     if (issuer.isOwner())
/*  84 */       return true; 
/*  85 */     List<Role> issuerRoles = issuer.getRoles();
/*  86 */     return (!issuerRoles.isEmpty() && canInteract(issuerRoles.get(0), target));
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
/*     */   public static boolean canInteract(Role issuer, Role target) {
/* 106 */     Checks.notNull(issuer, "Issuer Role");
/* 107 */     Checks.notNull(target, "Target Role");
/*     */     
/* 109 */     if (!issuer.getGuild().equals(target.getGuild()))
/* 110 */       throw new IllegalArgumentException("The 2 Roles are not from same Guild!"); 
/* 111 */     return (target.getPosition() < issuer.getPosition());
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
/*     */   
/*     */   public static boolean canInteract(Member issuer, Emote emote) {
/* 141 */     Checks.notNull(issuer, "Issuer Member");
/* 142 */     Checks.notNull(emote, "Target Emote");
/*     */     
/* 144 */     if (!issuer.getGuild().equals(emote.getGuild())) {
/* 145 */       throw new IllegalArgumentException("The issuer and target are not in the same Guild");
/*     */     }
/* 147 */     return (emote.canProvideRoles() && (emote.getRoles().isEmpty() || 
/* 148 */       CollectionUtils.containsAny(issuer.getRoles(), emote.getRoles())));
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
/*     */   public static boolean canInteract(User issuer, Emote emote, MessageChannel channel, boolean botOverride) {
/*     */     TextChannel text;
/* 172 */     Checks.notNull(issuer, "Issuer Member");
/* 173 */     Checks.notNull(emote, "Target Emote");
/* 174 */     Checks.notNull(channel, "Target Channel");
/*     */     
/* 176 */     if (emote.getGuild() == null || !emote.getGuild().isMember(issuer))
/* 177 */       return false; 
/* 178 */     Member member = emote.getGuild().getMemberById(issuer.getIdLong());
/* 179 */     if (!canInteract(member, emote)) {
/* 180 */       return false;
/*     */     }
/*     */     
/* 183 */     boolean external = (emote.isManaged() || (issuer.isBot() && botOverride));
/* 184 */     switch (channel.getType()) {
/*     */       
/*     */       case TEXT:
/* 187 */         text = (TextChannel)channel;
/* 188 */         member = text.getGuild().getMemberById(issuer.getIdLong());
/* 189 */         return (emote.getGuild().equals(text.getGuild()) || (external && member != null && member
/* 190 */           .hasPermission((GuildChannel)text, new Permission[] { Permission.MESSAGE_EXT_EMOJI })));
/*     */     } 
/* 192 */     return external;
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
/*     */   public static boolean canInteract(User issuer, Emote emote, MessageChannel channel) {
/* 215 */     return canInteract(issuer, emote, channel, true);
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
/*     */   public static boolean checkPermission(Member member, Permission... permissions) {
/* 240 */     Checks.notNull(member, "Member");
/* 241 */     Checks.notNull(permissions, "Permissions");
/*     */     
/* 243 */     long effectivePerms = getEffectivePermission(member);
/* 244 */     return (isApplied(effectivePerms, Permission.ADMINISTRATOR.getRawValue()) || 
/* 245 */       isApplied(effectivePerms, Permission.getRaw(permissions)));
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
/*     */   public static boolean checkPermission(GuildChannel channel, Member member, Permission... permissions) {
/* 274 */     Checks.notNull(channel, "Channel");
/* 275 */     Checks.notNull(member, "Member");
/* 276 */     Checks.notNull(permissions, "Permissions");
/*     */     
/* 278 */     GuildImpl guild = (GuildImpl)channel.getGuild();
/* 279 */     checkGuild((Guild)guild, member.getGuild(), "Member");
/*     */     
/* 281 */     long effectivePerms = getEffectivePermission(channel, member);
/* 282 */     return isApplied(effectivePerms, Permission.getRaw(permissions));
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
/*     */   public static long getEffectivePermission(Member member) {
/* 305 */     Checks.notNull(member, "Member");
/*     */     
/* 307 */     if (member.isOwner()) {
/* 308 */       return Permission.ALL_PERMISSIONS;
/*     */     }
/* 310 */     long permission = member.getGuild().getPublicRole().getPermissionsRaw();
/* 311 */     for (Role role : member.getRoles()) {
/*     */       
/* 313 */       permission |= role.getPermissionsRaw();
/* 314 */       if (isApplied(permission, Permission.ADMINISTRATOR.getRawValue())) {
/* 315 */         return Permission.ALL_PERMISSIONS;
/*     */       }
/*     */     } 
/* 318 */     return permission;
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
/*     */   public static long getEffectivePermission(GuildChannel channel, Member member) {
/* 342 */     Checks.notNull(channel, "Channel");
/* 343 */     Checks.notNull(member, "Member");
/*     */     
/* 345 */     Checks.check(channel.getGuild().equals(member.getGuild()), "Provided channel and provided member are not of the same guild!");
/*     */     
/* 347 */     if (member.isOwner())
/*     */     {
/*     */       
/* 350 */       return Permission.ALL_PERMISSIONS;
/*     */     }
/*     */     
/* 353 */     long permission = getEffectivePermission(member);
/* 354 */     long admin = Permission.ADMINISTRATOR.getRawValue();
/* 355 */     if (isApplied(permission, admin)) {
/* 356 */       return Permission.ALL_PERMISSIONS;
/*     */     }
/* 358 */     if (channel.getParent() != null && checkPermission((GuildChannel)channel.getParent(), member, new Permission[] { Permission.MANAGE_CHANNEL })) {
/* 359 */       permission |= Permission.MANAGE_CHANNEL.getRawValue();
/*     */     }
/*     */     
/* 362 */     AtomicLong allow = new AtomicLong(0L);
/* 363 */     AtomicLong deny = new AtomicLong(0L);
/* 364 */     getExplicitOverrides(channel, member, allow, deny);
/* 365 */     permission = apply(permission, allow.get(), deny.get());
/* 366 */     long viewChannel = Permission.VIEW_CHANNEL.getRawValue();
/* 367 */     long connectChannel = Permission.VOICE_CONNECT.getRawValue();
/*     */ 
/*     */ 
/*     */     
/* 371 */     boolean hasConnect = ((channel.getType() != ChannelType.VOICE && channel.getType() != ChannelType.STAGE) || isApplied(permission, connectChannel));
/* 372 */     boolean hasView = isApplied(permission, viewChannel);
/* 373 */     return (hasView && hasConnect) ? permission : 0L;
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
/*     */   public static long getEffectivePermission(GuildChannel channel, Role role) {
/* 396 */     Checks.notNull(channel, "Channel");
/* 397 */     Checks.notNull(role, "Role");
/*     */     
/* 399 */     Guild guild = channel.getGuild();
/* 400 */     if (!guild.equals(role.getGuild())) {
/* 401 */       throw new IllegalArgumentException("Provided channel and role are not of the same guild!");
/*     */     }
/* 403 */     long permissions = getExplicitPermission(channel, role);
/* 404 */     if (isApplied(permissions, Permission.ADMINISTRATOR.getRawValue()))
/* 405 */       return Permission.ALL_CHANNEL_PERMISSIONS; 
/* 406 */     if (!isApplied(permissions, Permission.VIEW_CHANNEL.getRawValue()))
/* 407 */       return 0L; 
/* 408 */     return permissions;
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
/*     */   public static long getExplicitPermission(Member member) {
/* 432 */     Checks.notNull(member, "Member");
/*     */     
/* 434 */     Guild guild = member.getGuild();
/* 435 */     long permission = guild.getPublicRole().getPermissionsRaw();
/*     */     
/* 437 */     for (Role role : member.getRoles()) {
/* 438 */       permission |= role.getPermissionsRaw();
/*     */     }
/* 440 */     return permission;
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
/*     */   
/*     */   public static long getExplicitPermission(GuildChannel channel, Member member) {
/* 470 */     return getExplicitPermission(channel, member, true);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static long getExplicitPermission(GuildChannel channel, Member member, boolean includeRoles) {
/* 502 */     Checks.notNull(channel, "Channel");
/* 503 */     Checks.notNull(member, "Member");
/*     */     
/* 505 */     Guild guild = member.getGuild();
/* 506 */     checkGuild(channel.getGuild(), guild, "Member");
/*     */     
/* 508 */     long permission = includeRoles ? getExplicitPermission(member) : 0L;
/*     */     
/* 510 */     AtomicLong allow = new AtomicLong(0L);
/* 511 */     AtomicLong deny = new AtomicLong(0L);
/*     */ 
/*     */     
/* 514 */     getExplicitOverrides(channel, member, allow, deny);
/*     */     
/* 516 */     return apply(permission, allow.get(), deny.get());
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
/*     */   public static long getExplicitPermission(GuildChannel channel, Role role) {
/* 544 */     return getExplicitPermission(channel, role, true);
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
/*     */   
/*     */   public static long getExplicitPermission(GuildChannel channel, Role role, boolean includeRoles) {
/* 574 */     Checks.notNull(channel, "Channel");
/* 575 */     Checks.notNull(role, "Role");
/*     */     
/* 577 */     Guild guild = role.getGuild();
/* 578 */     checkGuild(channel.getGuild(), guild, "Role");
/*     */     
/* 580 */     long permission = includeRoles ? (role.getPermissionsRaw() | guild.getPublicRole().getPermissionsRaw()) : 0L;
/* 581 */     PermissionOverride override = channel.getPermissionOverride((IPermissionHolder)guild.getPublicRole());
/* 582 */     if (override != null)
/* 583 */       permission = apply(permission, override.getAllowedRaw(), override.getDeniedRaw()); 
/* 584 */     if (role.isPublicRole()) {
/* 585 */       return permission;
/*     */     }
/* 587 */     override = channel.getPermissionOverride((IPermissionHolder)role);
/*     */     
/* 589 */     return (override == null) ? 
/* 590 */       permission : 
/* 591 */       apply(permission, override.getAllowedRaw(), override.getDeniedRaw());
/*     */   }
/*     */ 
/*     */   
/*     */   private static void getExplicitOverrides(GuildChannel channel, Member member, AtomicLong allow, AtomicLong deny) {
/* 596 */     PermissionOverride override = channel.getPermissionOverride((IPermissionHolder)member.getGuild().getPublicRole());
/* 597 */     long allowRaw = 0L;
/* 598 */     long denyRaw = 0L;
/* 599 */     if (override != null) {
/*     */       
/* 601 */       denyRaw = override.getDeniedRaw();
/* 602 */       allowRaw = override.getAllowedRaw();
/*     */     } 
/*     */     
/* 605 */     long allowRole = 0L;
/* 606 */     long denyRole = 0L;
/*     */     
/* 608 */     for (Role role : member.getRoles()) {
/*     */       
/* 610 */       override = channel.getPermissionOverride((IPermissionHolder)role);
/* 611 */       if (override != null) {
/*     */ 
/*     */         
/* 614 */         denyRole |= override.getDeniedRaw();
/* 615 */         allowRole |= override.getAllowedRaw();
/*     */       } 
/*     */     } 
/*     */     
/* 619 */     allowRaw = allowRaw & (denyRole ^ 0xFFFFFFFFFFFFFFFFL) | allowRole;
/* 620 */     denyRaw = denyRaw & (allowRole ^ 0xFFFFFFFFFFFFFFFFL) | denyRole;
/*     */     
/* 622 */     override = channel.getPermissionOverride((IPermissionHolder)member);
/* 623 */     if (override != null) {
/*     */ 
/*     */       
/* 626 */       long oDeny = override.getDeniedRaw();
/* 627 */       long oAllow = override.getAllowedRaw();
/* 628 */       allowRaw = allowRaw & (oDeny ^ 0xFFFFFFFFFFFFFFFFL) | oAllow;
/* 629 */       denyRaw = denyRaw & (oAllow ^ 0xFFFFFFFFFFFFFFFFL) | oDeny;
/*     */     } 
/*     */ 
/*     */     
/* 633 */     allow.set(allowRaw);
/* 634 */     deny.set(denyRaw);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isApplied(long permissions, long perms) {
/* 642 */     return ((permissions & perms) == perms);
/*     */   }
/*     */ 
/*     */   
/*     */   private static long apply(long permission, long allow, long deny) {
/* 647 */     permission &= deny ^ 0xFFFFFFFFFFFFFFFFL;
/* 648 */     permission |= allow;
/*     */     
/* 650 */     return permission;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void checkGuild(Guild o1, Guild o2, String name) {
/* 655 */     Checks.check(o1.equals(o2), "Specified %s is not in the same guild! (%s / %s)", new Object[] { name, o1, o2 });
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\PermissionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */