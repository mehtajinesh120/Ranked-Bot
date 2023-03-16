/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RoleIcon
/*     */ {
/*     */   public static final String ICON_URL = "https://cdn.discordapp.com/role-icons/%s/%s.png";
/*     */   private final String iconId;
/*     */   private final String emoji;
/*     */   private final long roleId;
/*     */   
/*     */   public RoleIcon(String iconId, String emoji, long roleId) {
/*  40 */     this.iconId = iconId;
/*  41 */     this.emoji = emoji;
/*  42 */     this.roleId = roleId;
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
/*     */   @Nullable
/*     */   public String getIconId() {
/*  57 */     return this.iconId;
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
/*     */   @Nullable
/*     */   public String getIconUrl() {
/*  72 */     String iconId = getIconId();
/*  73 */     return (iconId == null) ? null : String.format("https://cdn.discordapp.com/role-icons/%s/%s.png", new Object[] { Long.valueOf(this.roleId), iconId });
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
/*     */   @Nullable
/*     */   public String getEmoji() {
/*  88 */     return this.emoji;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmoji() {
/*  98 */     return (this.emoji != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 104 */     if (obj == this)
/* 105 */       return true; 
/* 106 */     if (!(obj instanceof RoleIcon))
/* 107 */       return false; 
/* 108 */     RoleIcon icon = (RoleIcon)obj;
/* 109 */     return (Objects.equals(icon.iconId, this.iconId) && 
/* 110 */       Objects.equals(icon.emoji, this.emoji));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 116 */     return Objects.hash(new Object[] { this.iconId, this.emoji });
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\RoleIcon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */