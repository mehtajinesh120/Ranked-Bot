/*     */ package net.dv8tion.jda.internal.managers;
/*     */ 
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.Member;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*     */ import net.dv8tion.jda.api.exceptions.MissingAccessException;
/*     */ import net.dv8tion.jda.api.managers.Manager;
/*     */ import net.dv8tion.jda.api.managers.PermOverrideManager;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.entities.AbstractChannelImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
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
/*     */ public class PermOverrideManagerImpl
/*     */   extends ManagerBase<PermOverrideManager>
/*     */   implements PermOverrideManager
/*     */ {
/*     */   protected final boolean role;
/*     */   protected PermissionOverride override;
/*     */   protected long allowed;
/*     */   protected long denied;
/*     */   
/*     */   public PermOverrideManagerImpl(PermissionOverride override) {
/*  50 */     super(override.getJDA(), Route.Channels.MODIFY_PERM_OVERRIDE
/*  51 */         .compile(new String[] {
/*  52 */             override.getChannel().getId(), override.getId() }));
/*  53 */     this.override = override;
/*  54 */     this.role = override.isRoleOverride();
/*  55 */     this.allowed = override.getAllowedRaw();
/*  56 */     this.denied = override.getDeniedRaw();
/*  57 */     if (isPermissionChecksEnabled()) {
/*  58 */       checkPermissions();
/*     */     }
/*     */   }
/*     */   
/*     */   private void setupValues() {
/*  63 */     if (!shouldUpdate(2L))
/*  64 */       this.allowed = getPermissionOverride().getAllowedRaw(); 
/*  65 */     if (!shouldUpdate(1L)) {
/*  66 */       this.denied = getPermissionOverride().getDeniedRaw();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public PermissionOverride getPermissionOverride() {
/*  73 */     AbstractChannelImpl<?, ?> channel = (AbstractChannelImpl<?, ?>)this.override.getChannel();
/*  74 */     PermissionOverride realOverride = (PermissionOverride)channel.getOverrideMap().get(this.override.getIdLong());
/*  75 */     if (realOverride != null)
/*  76 */       this.override = realOverride; 
/*  77 */     return this.override;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public PermOverrideManagerImpl reset(long fields) {
/*  85 */     super.reset(fields);
/*  86 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public PermOverrideManagerImpl reset(long... fields) {
/*  94 */     super.reset(fields);
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public PermOverrideManagerImpl reset() {
/* 103 */     super.reset();
/* 104 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public PermOverrideManagerImpl grant(long permissions) {
/* 112 */     if (permissions == 0L)
/* 113 */       return this; 
/* 114 */     setupValues();
/* 115 */     this.allowed |= permissions;
/* 116 */     this.denied &= permissions ^ 0xFFFFFFFFFFFFFFFFL;
/* 117 */     this.set |= 0x3L;
/* 118 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public PermOverrideManagerImpl deny(long permissions) {
/* 126 */     if (permissions == 0L)
/* 127 */       return this; 
/* 128 */     setupValues();
/* 129 */     this.denied |= permissions;
/* 130 */     this.allowed &= permissions ^ 0xFFFFFFFFFFFFFFFFL;
/* 131 */     this.set |= 0x3L;
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public PermOverrideManagerImpl clear(long permissions) {
/* 140 */     setupValues();
/* 141 */     if ((this.allowed & permissions) != 0L) {
/*     */       
/* 143 */       this.allowed &= permissions ^ 0xFFFFFFFFFFFFFFFFL;
/* 144 */       this.set |= 0x2L;
/*     */     } 
/*     */     
/* 147 */     if ((this.denied & permissions) != 0L) {
/*     */       
/* 149 */       this.denied &= permissions ^ 0xFFFFFFFFFFFFFFFFL;
/* 150 */       this.set |= 0x1L;
/*     */     } 
/*     */     
/* 153 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected RequestBody finalizeData() {
/* 159 */     String targetId = this.override.getId();
/*     */     
/* 161 */     setupValues();
/* 162 */     RequestBody data = getRequestBody(
/* 163 */         DataObject.empty()
/* 164 */         .put("id", targetId)
/* 165 */         .put("type", this.role ? "role" : "member")
/* 166 */         .put("allow", Long.valueOf(this.allowed))
/* 167 */         .put("deny", Long.valueOf(this.denied)));
/* 168 */     reset();
/* 169 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean checkPermissions() {
/* 175 */     Member selfMember = getGuild().getSelfMember();
/* 176 */     GuildChannel channel = getChannel();
/* 177 */     if (!selfMember.hasPermission(channel, new Permission[] { Permission.VIEW_CHANNEL }))
/* 178 */       throw new MissingAccessException(channel, Permission.VIEW_CHANNEL); 
/* 179 */     if (!selfMember.hasAccess(channel))
/* 180 */       throw new MissingAccessException(channel, Permission.VOICE_CONNECT); 
/* 181 */     if (!selfMember.hasPermission(channel, new Permission[] { Permission.MANAGE_PERMISSIONS }))
/* 182 */       throw new InsufficientPermissionException(channel, Permission.MANAGE_PERMISSIONS); 
/* 183 */     return super.checkPermissions();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\PermOverrideManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */