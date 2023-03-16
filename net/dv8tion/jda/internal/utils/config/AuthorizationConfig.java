/*    */ package net.dv8tion.jda.internal.utils.config;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.AccountType;
/*    */ import net.dv8tion.jda.internal.utils.Checks;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class AuthorizationConfig
/*    */ {
/*    */   private String token;
/*    */   
/*    */   public AuthorizationConfig(@Nonnull String token) {
/* 30 */     Checks.notNull(token, "Token");
/* 31 */     setToken(token);
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public AccountType getAccountType() {
/* 37 */     return AccountType.BOT;
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getToken() {
/* 43 */     return this.token;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setToken(@Nonnull String token) {
/* 48 */     this.token = "Bot " + token;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\config\AuthorizationConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */