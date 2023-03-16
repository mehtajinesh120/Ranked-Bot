/*     */ package net.dv8tion.jda.api.utils.cache;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.requests.GatewayIntent;
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
/*     */ public enum CacheFlag
/*     */ {
/*  40 */   ACTIVITY(GatewayIntent.GUILD_PRESENCES),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   VOICE_STATE(GatewayIntent.GUILD_VOICE_STATES),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   EMOTE(GatewayIntent.GUILD_EMOJIS),
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   CLIENT_STATUS(GatewayIntent.GUILD_PRESENCES),
/*     */ 
/*     */ 
/*     */   
/*  63 */   MEMBER_OVERRIDES,
/*     */ 
/*     */ 
/*     */   
/*  67 */   ROLE_TAGS,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  76 */   ONLINE_STATUS(GatewayIntent.GUILD_PRESENCES);
/*     */   
/*     */   static {
/*  79 */     privileged = EnumSet.of(ACTIVITY, CLIENT_STATUS, ONLINE_STATUS);
/*     */   }
/*     */ 
/*     */   
/*     */   private static final EnumSet<CacheFlag> privileged;
/*     */   
/*     */   private final GatewayIntent requiredIntent;
/*     */ 
/*     */   
/*     */   CacheFlag(GatewayIntent requiredIntent) {
/*  89 */     this.requiredIntent = requiredIntent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public GatewayIntent getRequiredIntent() {
/* 100 */     return this.requiredIntent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPresence() {
/* 110 */     return (this.requiredIntent == GatewayIntent.GUILD_PRESENCES);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static EnumSet<CacheFlag> getPrivileged() {
/* 121 */     return EnumSet.copyOf(privileged);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\cache\CacheFlag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */