/*     */ package net.dv8tion.jda.api.events.guild.override;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.GuildChannel;
/*     */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PermissionOverrideUpdateEvent
/*     */   extends GenericPermissionOverrideEvent
/*     */ {
/*     */   private final long oldAllow;
/*     */   private final long oldDeny;
/*     */   
/*     */   public PermissionOverrideUpdateEvent(@Nonnull JDA api, long responseNumber, @Nonnull GuildChannel channel, @Nonnull PermissionOverride override, long oldAllow, long oldDeny) {
/*  38 */     super(api, responseNumber, channel, override);
/*  39 */     this.oldAllow = oldAllow;
/*  40 */     this.oldDeny = oldDeny;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getOldAllowRaw() {
/*  50 */     return this.oldAllow;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getOldDenyRaw() {
/*  60 */     return this.oldDeny;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getOldInheritedRaw() {
/*  70 */     return (this.oldAllow | this.oldDeny) ^ 0xFFFFFFFFFFFFFFFFL;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getOldAllow() {
/*  81 */     return Permission.getPermissions(this.oldAllow);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getOldDeny() {
/*  92 */     return Permission.getPermissions(this.oldDeny);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getOldInherited() {
/* 103 */     return Permission.getPermissions(getOldInheritedRaw());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guild\override\PermissionOverrideUpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */