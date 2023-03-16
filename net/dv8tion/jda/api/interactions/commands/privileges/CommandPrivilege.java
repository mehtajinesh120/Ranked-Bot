/*     */ package net.dv8tion.jda.api.interactions.commands.privileges;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ import net.dv8tion.jda.api.entities.Role;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.utils.MiscUtil;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.api.utils.data.SerializableData;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandPrivilege
/*     */   implements ISnowflake, SerializableData
/*     */ {
/*     */   private final Type type;
/*     */   private final boolean enabled;
/*     */   private final long id;
/*     */   
/*     */   public CommandPrivilege(@Nonnull Type type, boolean enabled, long id) {
/*  52 */     Checks.notNull(type, "Type");
/*  53 */     this.type = type;
/*  54 */     this.enabled = enabled;
/*  55 */     this.id = id;
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege enable(@Nonnull Role role) {
/*  69 */     Checks.notNull(role, "Role");
/*  70 */     return new CommandPrivilege(Type.ROLE, true, role.getIdLong());
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege enable(@Nonnull User user) {
/*  84 */     Checks.notNull(user, "User");
/*  85 */     return new CommandPrivilege(Type.USER, true, user.getIdLong());
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege enableUser(@Nonnull String userId) {
/*  99 */     return enableUser(MiscUtil.parseSnowflake(userId));
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege enableUser(long userId) {
/* 113 */     return new CommandPrivilege(Type.USER, true, userId);
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege enableRole(@Nonnull String roleId) {
/* 127 */     return enableRole(MiscUtil.parseSnowflake(roleId));
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege enableRole(long roleId) {
/* 141 */     return new CommandPrivilege(Type.ROLE, true, roleId);
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege disable(@Nonnull Role role) {
/* 155 */     Checks.notNull(role, "Role");
/* 156 */     return new CommandPrivilege(Type.ROLE, false, role.getIdLong());
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege disable(@Nonnull User user) {
/* 170 */     Checks.notNull(user, "User");
/* 171 */     return new CommandPrivilege(Type.USER, false, user.getIdLong());
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege disableUser(@Nonnull String userId) {
/* 185 */     return disableUser(MiscUtil.parseSnowflake(userId));
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege disableUser(long userId) {
/* 199 */     return new CommandPrivilege(Type.USER, false, userId);
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege disableRole(@Nonnull String roleId) {
/* 213 */     return disableRole(MiscUtil.parseSnowflake(roleId));
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
/*     */   @Nonnull
/*     */   public static CommandPrivilege disableRole(long roleId) {
/* 227 */     return new CommandPrivilege(Type.ROLE, false, roleId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 234 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Type getType() {
/* 245 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEnabled() {
/* 255 */     return this.enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDisabled() {
/* 265 */     return !this.enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 271 */     return Long.hashCode(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 277 */     if (obj == this)
/* 278 */       return true; 
/* 279 */     if (!(obj instanceof CommandPrivilege))
/* 280 */       return false; 
/* 281 */     return (((CommandPrivilege)obj).id == this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject toData() {
/* 288 */     return DataObject.empty()
/* 289 */       .put("id", Long.valueOf(this.id))
/* 290 */       .put("type", Integer.valueOf(this.type.key))
/* 291 */       .put("permission", Boolean.valueOf(this.enabled));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/* 299 */     UNKNOWN(-1),
/* 300 */     ROLE(1),
/* 301 */     USER(2);
/*     */     
/*     */     private final int key;
/*     */ 
/*     */     
/*     */     Type(int key) {
/* 307 */       this.key = key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public static Type fromKey(int key) {
/* 321 */       for (Type type : values()) {
/*     */         
/* 323 */         if (type.key == key)
/* 324 */           return type; 
/*     */       } 
/* 326 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\commands\privileges\CommandPrivilege.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */