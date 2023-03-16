/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.Permission;
/*     */ import net.dv8tion.jda.api.entities.ApplicationInfo;
/*     */ import net.dv8tion.jda.api.entities.ApplicationTeam;
/*     */ import net.dv8tion.jda.api.entities.User;
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
/*     */ public class ApplicationInfoImpl
/*     */   implements ApplicationInfo
/*     */ {
/*     */   private final JDA api;
/*     */   private final boolean doesBotRequireCodeGrant;
/*     */   private final boolean isBotPublic;
/*     */   private final long id;
/*     */   private final String iconId;
/*     */   private final String description;
/*     */   private final String termsOfServiceUrl;
/*     */   private final String privacyPolicyUrl;
/*     */   private final String name;
/*     */   private final User owner;
/*     */   private final ApplicationTeam team;
/*  43 */   private String scopes = "bot";
/*     */ 
/*     */ 
/*     */   
/*     */   public ApplicationInfoImpl(JDA api, String description, boolean doesBotRequireCodeGrant, String iconId, long id, boolean isBotPublic, String name, String termsOfServiceUrl, String privacyPolicyUrl, User owner, ApplicationTeam team) {
/*  48 */     this.api = api;
/*  49 */     this.description = description;
/*  50 */     this.doesBotRequireCodeGrant = doesBotRequireCodeGrant;
/*  51 */     this.iconId = iconId;
/*  52 */     this.id = id;
/*  53 */     this.isBotPublic = isBotPublic;
/*  54 */     this.name = name;
/*  55 */     this.termsOfServiceUrl = termsOfServiceUrl;
/*  56 */     this.privacyPolicyUrl = privacyPolicyUrl;
/*  57 */     this.owner = owner;
/*  58 */     this.team = team;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean doesBotRequireCodeGrant() {
/*  64 */     return this.doesBotRequireCodeGrant;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  70 */     return (obj instanceof ApplicationInfoImpl && this.id == ((ApplicationInfoImpl)obj).id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getDescription() {
/*  77 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTermsOfServiceUrl() {
/*  83 */     return this.termsOfServiceUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPrivacyPolicyUrl() {
/*  89 */     return this.privacyPolicyUrl;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIconId() {
/*  95 */     return this.iconId;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIconUrl() {
/* 101 */     return (this.iconId == null) ? null : (
/* 102 */       "https://cdn.discordapp.com/app-icons/" + this.id + '/' + this.iconId + ".png");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ApplicationTeam getTeam() {
/* 109 */     return this.team;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public ApplicationInfo setRequiredScopes(@Nonnull Collection<String> scopes) {
/* 116 */     Checks.noneNull(scopes, "Scopes");
/* 117 */     this.scopes = String.join("+", (Iterable)scopes);
/* 118 */     if (!this.scopes.contains("bot"))
/*     */     {
/* 120 */       if (this.scopes.isEmpty()) {
/* 121 */         this.scopes = "bot";
/*     */       } else {
/* 123 */         this.scopes += "+bot";
/*     */       }  } 
/* 125 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/* 131 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getInviteUrl(String guildId, Collection<Permission> permissions) {
/* 138 */     StringBuilder builder = new StringBuilder("https://discord.com/oauth2/authorize?client_id=");
/* 139 */     builder.append(getId());
/* 140 */     builder.append("&scope=").append(this.scopes);
/* 141 */     if (permissions != null && !permissions.isEmpty()) {
/*     */       
/* 143 */       builder.append("&permissions=");
/* 144 */       builder.append(Permission.getRaw(permissions));
/*     */     } 
/* 146 */     if (guildId != null) {
/*     */       
/* 148 */       builder.append("&guild_id=");
/* 149 */       builder.append(guildId);
/*     */     } 
/* 151 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/* 158 */     return this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/* 165 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public User getOwner() {
/* 172 */     return this.owner;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 178 */     return Long.hashCode(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isBotPublic() {
/* 184 */     return this.isBotPublic;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 190 */     return "ApplicationInfo(" + this.id + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\ApplicationInfoImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */