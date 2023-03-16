/*     */ package net.dv8tion.jda.internal.handle;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.RoleIcon;
/*     */ import net.dv8tion.jda.api.events.GenericEvent;
/*     */ import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
/*     */ import net.dv8tion.jda.api.events.role.update.RoleUpdateHoistedEvent;
/*     */ import net.dv8tion.jda.api.events.role.update.RoleUpdateIconEvent;
/*     */ import net.dv8tion.jda.api.events.role.update.RoleUpdateMentionableEvent;
/*     */ import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
/*     */ import net.dv8tion.jda.api.events.role.update.RoleUpdatePermissionsEvent;
/*     */ import net.dv8tion.jda.api.events.role.update.RoleUpdatePositionEvent;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*     */ import net.dv8tion.jda.internal.entities.RoleImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuildRoleUpdateHandler
/*     */   extends SocketHandler
/*     */ {
/*     */   public GuildRoleUpdateHandler(JDAImpl api) {
/*  32 */     super(api);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Long handleInternally(DataObject content) {
/*  38 */     long guildId = content.getLong("guild_id");
/*  39 */     if (getJDA().getGuildSetupController().isLocked(guildId)) {
/*  40 */       return Long.valueOf(guildId);
/*     */     }
/*  42 */     DataObject rolejson = content.getObject("role");
/*  43 */     GuildImpl guild = (GuildImpl)getJDA().getGuildById(guildId);
/*  44 */     if (guild == null) {
/*     */       
/*  46 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/*  47 */       EventCache.LOG.debug("Received a Role Update for a Guild that is not yet cached: {}", content);
/*  48 */       return null;
/*     */     } 
/*     */     
/*  51 */     long roleId = rolejson.getLong("id");
/*  52 */     RoleImpl role = (RoleImpl)guild.getRolesView().get(roleId);
/*  53 */     if (role == null) {
/*     */       
/*  55 */       getJDA().getEventCache().cache(EventCache.Type.ROLE, roleId, this.responseNumber, this.allContent, this::handle);
/*  56 */       EventCache.LOG.debug("Received a Role Update for Role that is not yet cached: {}", content);
/*  57 */       return null;
/*     */     } 
/*     */     
/*  60 */     String name = rolejson.getString("name");
/*  61 */     int color = rolejson.getInt("color");
/*  62 */     if (color == 0)
/*  63 */       color = 536870911; 
/*  64 */     int position = rolejson.getInt("position");
/*  65 */     long permissions = rolejson.getLong("permissions");
/*  66 */     boolean hoisted = rolejson.getBoolean("hoist");
/*  67 */     boolean mentionable = rolejson.getBoolean("mentionable");
/*  68 */     String iconId = rolejson.getString("icon", null);
/*  69 */     String emoji = rolejson.getString("unicode_emoji", null);
/*     */     
/*  71 */     if (!Objects.equals(name, role.getName())) {
/*     */       
/*  73 */       String oldName = role.getName();
/*  74 */       role.setName(name);
/*  75 */       getJDA().handleEvent((GenericEvent)new RoleUpdateNameEvent((JDA)
/*     */             
/*  77 */             getJDA(), this.responseNumber, (Role)role, oldName));
/*     */     } 
/*     */     
/*  80 */     if (color != role.getColorRaw()) {
/*     */       
/*  82 */       int oldColor = role.getColorRaw();
/*  83 */       role.setColor(color);
/*  84 */       getJDA().handleEvent((GenericEvent)new RoleUpdateColorEvent((JDA)
/*     */             
/*  86 */             getJDA(), this.responseNumber, (Role)role, oldColor));
/*     */     } 
/*     */     
/*  89 */     if (!Objects.equals(Integer.valueOf(position), Integer.valueOf(role.getPositionRaw()))) {
/*     */       
/*  91 */       int oldPosition = role.getPosition();
/*  92 */       int oldPositionRaw = role.getPositionRaw();
/*  93 */       role.setRawPosition(position);
/*  94 */       getJDA().handleEvent((GenericEvent)new RoleUpdatePositionEvent((JDA)
/*     */             
/*  96 */             getJDA(), this.responseNumber, (Role)role, oldPosition, oldPositionRaw));
/*     */     } 
/*     */     
/*  99 */     if (!Objects.equals(Long.valueOf(permissions), Long.valueOf(role.getPermissionsRaw()))) {
/*     */       
/* 101 */       long oldPermissionsRaw = role.getPermissionsRaw();
/* 102 */       role.setRawPermissions(permissions);
/* 103 */       getJDA().handleEvent((GenericEvent)new RoleUpdatePermissionsEvent((JDA)
/*     */             
/* 105 */             getJDA(), this.responseNumber, (Role)role, oldPermissionsRaw));
/*     */     } 
/*     */ 
/*     */     
/* 109 */     if (hoisted != role.isHoisted()) {
/*     */       
/* 111 */       boolean wasHoisted = role.isHoisted();
/* 112 */       role.setHoisted(hoisted);
/* 113 */       getJDA().handleEvent((GenericEvent)new RoleUpdateHoistedEvent((JDA)
/*     */             
/* 115 */             getJDA(), this.responseNumber, (Role)role, wasHoisted));
/*     */     } 
/*     */     
/* 118 */     if (mentionable != role.isMentionable()) {
/*     */       
/* 120 */       boolean wasMentionable = role.isMentionable();
/* 121 */       role.setMentionable(mentionable);
/* 122 */       getJDA().handleEvent((GenericEvent)new RoleUpdateMentionableEvent((JDA)
/*     */             
/* 124 */             getJDA(), this.responseNumber, (Role)role, wasMentionable));
/*     */     } 
/*     */ 
/*     */     
/* 128 */     RoleIcon oldIcon = role.getIcon();
/*     */ 
/*     */     
/* 131 */     RoleIcon newIcon = (iconId == null && emoji == null) ? null : new RoleIcon(iconId, emoji, roleId);
/* 132 */     if (!Objects.equals(oldIcon, newIcon)) {
/*     */       
/* 134 */       role.setIcon(newIcon);
/* 135 */       getJDA().handleEvent((GenericEvent)new RoleUpdateIconEvent((JDA)
/*     */             
/* 137 */             getJDA(), this.responseNumber, (Role)role, oldIcon));
/*     */     } 
/*     */     
/* 140 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\GuildRoleUpdateHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */