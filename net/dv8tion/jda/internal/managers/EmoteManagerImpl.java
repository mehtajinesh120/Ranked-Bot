/*     */ package net.dv8tion.jda.internal.managers;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.Emote;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.managers.EmoteManager;
/*     */ import net.dv8tion.jda.api.managers.Manager;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.entities.EmoteImpl;
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
/*     */ public class EmoteManagerImpl
/*     */   extends ManagerBase<EmoteManager>
/*     */   implements EmoteManager
/*     */ {
/*     */   protected final EmoteImpl emote;
/*  42 */   protected final List<String> roles = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String name;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EmoteManagerImpl(EmoteImpl emote) {
/*  53 */     super((JDA)emote.getJDA(), Route.Emotes.MODIFY_EMOTE.compile(new String[] { notNullGuild(emote).getId(), emote.getId() }));
/*  54 */     this.emote = emote;
/*  55 */     if (isPermissionChecksEnabled()) {
/*  56 */       checkPermissions();
/*     */     }
/*     */   }
/*     */   
/*     */   private static Guild notNullGuild(EmoteImpl emote) {
/*  61 */     GuildImpl guildImpl = emote.getGuild();
/*  62 */     if (guildImpl == null)
/*  63 */       throw new IllegalStateException("Cannot modify an emote without shared guild"); 
/*  64 */     return (Guild)guildImpl;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Emote getEmote() {
/*  71 */     return (Emote)this.emote;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public EmoteManagerImpl reset(long fields) {
/*  79 */     super.reset(fields);
/*  80 */     if ((fields & 0x2L) == 2L)
/*  81 */       withLock(this.roles, List::clear); 
/*  82 */     if ((fields & 0x1L) == 1L)
/*  83 */       this.name = null; 
/*  84 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public EmoteManagerImpl reset(long... fields) {
/*  92 */     super.reset(fields);
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public EmoteManagerImpl reset() {
/* 101 */     super.reset();
/* 102 */     withLock(this.roles, List::clear);
/* 103 */     this.name = null;
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public EmoteManagerImpl setName(@Nonnull String name) {
/* 112 */     Checks.notBlank(name, "Name");
/* 113 */     name = name.trim();
/* 114 */     Checks.inRange(name, 2, 32, "Name");
/* 115 */     this.name = name;
/* 116 */     this.set |= 0x1L;
/* 117 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public EmoteManagerImpl setRoles(Set<Role> roles) {
/* 125 */     if (roles == null) {
/*     */       
/* 127 */       withLock(this.roles, List::clear);
/*     */     }
/*     */     else {
/*     */       
/* 131 */       Checks.notNull(roles, "Roles");
/* 132 */       roles.forEach(role -> {
/*     */             Checks.notNull(role, "Roles");
/*     */             
/*     */             Checks.check(role.getGuild().equals(getGuild()), "Roles must all be from the same guild");
/*     */           });
/* 137 */       withLock(this.roles, list -> {
/*     */             list.clear();
/*     */             Objects.requireNonNull(list);
/*     */             roles.stream().map(ISnowflake::getId).forEach(list::add);
/*     */           });
/*     */     } 
/* 143 */     this.set |= 0x2L;
/* 144 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 150 */     DataObject object = DataObject.empty();
/* 151 */     if (shouldUpdate(1L))
/* 152 */       object.put("name", this.name); 
/* 153 */     withLock(this.roles, list -> {
/*     */           if (shouldUpdate(2L)) {
/*     */             object.put("roles", DataArray.fromCollection(list));
/*     */           }
/*     */         });
/* 158 */     reset();
/* 159 */     return getRequestBody(object);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkPermissions() {
/* 165 */     if (!getGuild().getSelfMember().hasPermission(new Permission[] { Permission.MANAGE_EMOTES }))
/* 166 */       throw new InsufficientPermissionException(getGuild(), Permission.MANAGE_EMOTES); 
/* 167 */     return super.checkPermissions();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\EmoteManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */