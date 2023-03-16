/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.exceptions.MissingAccessException;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.PermissionOverrideAction;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.restaction.PermissionOverrideActionImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PermissionOverrideImpl
/*     */   implements PermissionOverride
/*     */ {
/*     */   private final long id;
/*     */   private final boolean isRole;
/*     */   private final JDAImpl api;
/*     */   private GuildChannel channel;
/*     */   protected PermissionOverrideAction manager;
/*     */   private long allow;
/*     */   private long deny;
/*     */   
/*     */   public PermissionOverrideImpl(GuildChannel channel, long id, boolean isRole) {
/*  49 */     this.isRole = isRole;
/*  50 */     this.api = (JDAImpl)channel.getJDA();
/*  51 */     this.channel = channel;
/*  52 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getAllowedRaw() {
/*  58 */     return this.allow;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getInheritRaw() {
/*  64 */     return (this.allow | this.deny) ^ 0xFFFFFFFFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDeniedRaw() {
/*  70 */     return this.deny;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getAllowed() {
/*  77 */     return Permission.getPermissions(this.allow);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getInherit() {
/*  84 */     return Permission.getPermissions(getInheritRaw());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getDenied() {
/*  91 */     return Permission.getPermissions(this.deny);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  98 */     return (JDA)this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public IPermissionHolder getPermissionHolder() {
/* 104 */     return this.isRole ? (IPermissionHolder)getRole() : (IPermissionHolder)getMember();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Member getMember() {
/* 110 */     return getGuild().getMemberById(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Role getRole() {
/* 116 */     return getGuild().getRoleById(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public GuildChannel getChannel() {
/* 123 */     GuildChannel realChannel = this.api.getGuildChannelById(this.channel.getType(), this.channel.getIdLong());
/* 124 */     if (realChannel != null)
/* 125 */       this.channel = realChannel; 
/* 126 */     return this.channel;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/* 133 */     return getChannel().getGuild();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMemberOverride() {
/* 139 */     return !this.isRole;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRoleOverride() {
/* 145 */     return this.isRole;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverrideAction getManager() {
/* 152 */     Member selfMember = getGuild().getSelfMember();
/* 153 */     GuildChannel channel = getChannel();
/* 154 */     if (!selfMember.hasPermission(channel, new Permission[] { Permission.VIEW_CHANNEL }))
/* 155 */       throw new MissingAccessException(channel, Permission.VIEW_CHANNEL); 
/* 156 */     if (!selfMember.hasAccess(channel))
/* 157 */       throw new MissingAccessException(channel, Permission.VOICE_CONNECT); 
/* 158 */     if (!selfMember.hasPermission(channel, new Permission[] { Permission.MANAGE_PERMISSIONS }))
/* 159 */       throw new InsufficientPermissionException(channel, Permission.MANAGE_PERMISSIONS); 
/* 160 */     if (this.manager == null)
/* 161 */       return this.manager = (PermissionOverrideAction)(new PermissionOverrideActionImpl(this)).setOverride(false); 
/* 162 */     return this.manager;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<Void> delete() {
/* 170 */     Member selfMember = getGuild().getSelfMember();
/* 171 */     GuildChannel channel = getChannel();
/* 172 */     if (!selfMember.hasPermission(channel, new Permission[] { Permission.VIEW_CHANNEL }))
/* 173 */       throw new MissingAccessException(channel, Permission.VIEW_CHANNEL); 
/* 174 */     if (!selfMember.hasAccess(channel))
/* 175 */       throw new MissingAccessException(channel, Permission.VOICE_CONNECT); 
/* 176 */     if (!selfMember.hasPermission(channel, new Permission[] { Permission.MANAGE_PERMISSIONS })) {
/* 177 */       throw new InsufficientPermissionException(channel, Permission.MANAGE_PERMISSIONS);
/*     */     }
/* 179 */     Route.CompiledRoute route = Route.Channels.DELETE_PERM_OVERRIDE.compile(new String[] { this.channel.getId(), getId() });
/* 180 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl(getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 186 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public PermissionOverrideImpl setAllow(long allow) {
/* 191 */     this.allow = allow;
/* 192 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public PermissionOverrideImpl setDeny(long deny) {
/* 197 */     this.deny = deny;
/* 198 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 204 */     if (o == this)
/* 205 */       return true; 
/* 206 */     if (!(o instanceof PermissionOverrideImpl))
/* 207 */       return false; 
/* 208 */     PermissionOverrideImpl oPerm = (PermissionOverrideImpl)o;
/* 209 */     return (this.id == oPerm.id && this.channel.getIdLong() == oPerm.channel.getIdLong());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 215 */     return Objects.hash(new Object[] { Long.valueOf(this.id), Long.valueOf(this.channel.getIdLong()) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 221 */     return "PermOver:(" + (isMemberOverride() ? "M" : "R") + ")(" + this.channel.getId() + " | " + getId() + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\PermissionOverrideImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */