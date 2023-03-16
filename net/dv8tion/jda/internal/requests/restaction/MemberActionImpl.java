/*     */ package net.dv8tion.jda.internal.requests.restaction;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.MemberAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
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
/*     */ public class MemberActionImpl
/*     */   extends RestActionImpl<Void>
/*     */   implements MemberAction
/*     */ {
/*     */   private final String accessToken;
/*     */   private final String userId;
/*     */   private final Guild guild;
/*     */   private String nick;
/*     */   private Set<Role> roles;
/*     */   private boolean mute;
/*     */   private boolean deaf;
/*     */   
/*     */   public MemberActionImpl(JDA api, Guild guild, String userId, String accessToken) {
/*  54 */     super(api, Route.Guilds.ADD_MEMBER.compile(new String[] { guild.getId(), userId }));
/*  55 */     this.accessToken = accessToken;
/*  56 */     this.userId = userId;
/*  57 */     this.guild = guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MemberAction setCheck(BooleanSupplier checks) {
/*  64 */     return (MemberAction)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MemberAction timeout(long timeout, @Nonnull TimeUnit unit) {
/*  71 */     return (MemberAction)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public MemberAction deadline(long timestamp) {
/*  78 */     return (MemberAction)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getAccessToken() {
/*  85 */     return this.accessToken;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getUserId() {
/*  92 */     return this.userId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public User getUser() {
/*  99 */     return getJDA().getUserById(this.userId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Guild getGuild() {
/* 106 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MemberActionImpl setNickname(String nick) {
/* 114 */     if (nick != null) {
/*     */       
/* 116 */       if (Helpers.isBlank(nick)) {
/*     */         
/* 118 */         this.nick = null;
/* 119 */         return this;
/*     */       } 
/* 121 */       Checks.notLonger(nick, 32, "Nickname");
/*     */     } 
/* 123 */     this.nick = nick;
/* 124 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MemberActionImpl setRoles(Collection<Role> roles) {
/* 132 */     if (roles == null) {
/*     */       
/* 134 */       this.roles = null;
/* 135 */       return this;
/*     */     } 
/* 137 */     Set<Role> newRoles = new HashSet<>(roles.size());
/* 138 */     for (Role role : roles)
/* 139 */       checkAndAdd(newRoles, role); 
/* 140 */     this.roles = newRoles;
/* 141 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MemberActionImpl setRoles(Role... roles) {
/* 149 */     if (roles == null) {
/*     */       
/* 151 */       this.roles = null;
/* 152 */       return this;
/*     */     } 
/* 154 */     Set<Role> newRoles = new HashSet<>(roles.length);
/* 155 */     for (Role role : roles)
/* 156 */       checkAndAdd(newRoles, role); 
/* 157 */     this.roles = newRoles;
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MemberActionImpl setMute(boolean mute) {
/* 166 */     this.mute = mute;
/* 167 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public MemberActionImpl setDeafen(boolean deaf) {
/* 175 */     this.deaf = deaf;
/* 176 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 182 */     DataObject obj = DataObject.empty();
/* 183 */     obj.put("access_token", this.accessToken);
/* 184 */     if (this.nick != null)
/* 185 */       obj.put("nick", this.nick); 
/* 186 */     if (this.roles != null && !this.roles.isEmpty())
/* 187 */       obj.put("roles", this.roles.stream().map(ISnowflake::getId).collect(Collectors.toList())); 
/* 188 */     obj.put("mute", Boolean.valueOf(this.mute));
/* 189 */     obj.put("deaf", Boolean.valueOf(this.deaf));
/* 190 */     return getRequestBody(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkAndAdd(Set<Role> newRoles, Role role) {
/* 195 */     Checks.notNull(role, "Role");
/* 196 */     Checks.check(role.getGuild().equals(getGuild()), "Roles must all be from the same guild");
/* 197 */     newRoles.add(role);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\MemberActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */