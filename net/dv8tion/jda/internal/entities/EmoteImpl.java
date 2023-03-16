/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.ListedEmote;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.managers.EmoteManager;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.internal.JDAImpl;
/*     */ import net.dv8tion.jda.internal.managers.EmoteManagerImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EmoteImpl
/*     */   implements ListedEmote
/*     */ {
/*     */   private final long id;
/*     */   private final JDAImpl api;
/*     */   private final Set<Role> roles;
/*     */   private EmoteManager manager;
/*     */   private GuildImpl guild;
/*     */   private boolean managed = false;
/*     */   private boolean available = true;
/*     */   private boolean animated = false;
/*     */   private String name;
/*     */   private User user;
/*     */   
/*     */   public EmoteImpl(long id, GuildImpl guild) {
/*  60 */     this.id = id;
/*  61 */     this.api = guild.getJDA();
/*  62 */     this.guild = guild;
/*  63 */     this.roles = ConcurrentHashMap.newKeySet();
/*     */   }
/*     */ 
/*     */   
/*     */   public EmoteImpl(long id, JDAImpl api) {
/*  68 */     this.id = id;
/*  69 */     this.api = api;
/*  70 */     this.guild = null;
/*  71 */     this.roles = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public GuildImpl getGuild() {
/*  77 */     if (this.guild == null)
/*  78 */       return null; 
/*  79 */     GuildImpl realGuild = (GuildImpl)this.api.getGuildById(this.guild.getIdLong());
/*  80 */     if (realGuild != null)
/*  81 */       this.guild = realGuild; 
/*  82 */     return this.guild;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Role> getRoles() {
/*  89 */     if (!canProvideRoles())
/*  90 */       throw new IllegalStateException("Unable to return roles because this emote is from a message. (We do not know the origin Guild of this emote)"); 
/*  91 */     return Collections.unmodifiableList(new LinkedList<>(this.roles));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canProvideRoles() {
/*  97 */     return (this.roles != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/* 104 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isManaged() {
/* 110 */     return this.managed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAvailable() {
/* 116 */     return this.available;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 122 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDAImpl getJDA() {
/* 129 */     return this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public User getUser() {
/* 136 */     if (!hasUser())
/* 137 */       throw new IllegalStateException("This emote does not have a user"); 
/* 138 */     return this.user;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasUser() {
/* 144 */     return (this.user != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EmoteManager getManager() {
/* 151 */     if (this.manager == null)
/* 152 */       return this.manager = (EmoteManager)new EmoteManagerImpl(this); 
/* 153 */     return this.manager;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAnimated() {
/* 159 */     return this.animated;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<Void> delete() {
/* 166 */     if (getGuild() == null)
/* 167 */       throw new IllegalStateException("The emote you are trying to delete is not an actual emote we have access to (it is from a message)!"); 
/* 168 */     if (this.managed)
/* 169 */       throw new UnsupportedOperationException("You cannot delete a managed emote!"); 
/* 170 */     if (!getGuild().getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_EMOTES })) {
/* 171 */       throw new InsufficientPermissionException(getGuild(), Permission.MANAGE_EMOTES);
/*     */     }
/* 173 */     Route.CompiledRoute route = Route.Emotes.DELETE_EMOTE.compile(new String[] { getGuild().getId(), getId() });
/* 174 */     return (AuditableRestAction<Void>)new AuditableRestActionImpl((JDA)getJDA(), route);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EmoteImpl setName(String name) {
/* 181 */     this.name = name;
/* 182 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EmoteImpl setAnimated(boolean animated) {
/* 187 */     this.animated = animated;
/* 188 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EmoteImpl setManaged(boolean val) {
/* 193 */     this.managed = val;
/* 194 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EmoteImpl setAvailable(boolean available) {
/* 199 */     this.available = available;
/* 200 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EmoteImpl setUser(User user) {
/* 205 */     this.user = user;
/* 206 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Role> getRoleSet() {
/* 213 */     return this.roles;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 221 */     if (obj == this)
/* 222 */       return true; 
/* 223 */     if (!(obj instanceof EmoteImpl)) {
/* 224 */       return false;
/*     */     }
/* 226 */     EmoteImpl oEmote = (EmoteImpl)obj;
/* 227 */     return (this.id == oEmote.id && getName().equals(oEmote.getName()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 234 */     return Long.hashCode(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 240 */     return "E:" + getName() + '(' + getIdLong() + ')';
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EmoteImpl clone() {
/* 246 */     EmoteImpl copy = (new EmoteImpl(this.id, getGuild())).setUser(this.user).setManaged(this.managed).setAnimated(this.animated).setName(this.name);
/* 247 */     copy.roles.addAll(this.roles);
/* 248 */     return copy;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\EmoteImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */