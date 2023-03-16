/*     */ package net.dv8tion.jda.api.entities.templates;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.ISnowflake;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TemplateRole
/*     */   implements ISnowflake
/*     */ {
/*     */   private final long id;
/*     */   private final String name;
/*     */   private final int color;
/*     */   private final boolean hoisted;
/*     */   private final boolean mentionable;
/*     */   private final long rawPermissions;
/*     */   
/*     */   public TemplateRole(long id, String name, int color, boolean hoisted, boolean mentionable, long rawPermissions) {
/*  44 */     this.id = id;
/*  45 */     this.name = name;
/*  46 */     this.color = color;
/*  47 */     this.hoisted = hoisted;
/*  48 */     this.mentionable = mentionable;
/*  49 */     this.rawPermissions = rawPermissions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/*  60 */     return this.id;
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
/*     */   public OffsetDateTime getTimeCreated() {
/*  72 */     throw new UnsupportedOperationException("The date of creation cannot be calculated");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/*  83 */     return this.name;
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
/*     */   @Nullable
/*     */   public Color getColor() {
/*  96 */     return (this.color == 536870911) ? null : new Color(this.color);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColorRaw() {
/* 107 */     return this.color;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isHoisted() {
/* 118 */     return this.hoisted;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMentionable() {
/* 128 */     return this.mentionable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<Permission> getPermissions() {
/* 140 */     return Permission.getPermissions(this.rawPermissions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getPermissionsRaw() {
/* 150 */     return this.rawPermissions;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\templates\TemplateRole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */