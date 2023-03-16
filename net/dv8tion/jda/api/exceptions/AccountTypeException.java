/*    */ package net.dv8tion.jda.api.exceptions;
/*    */ 
/*    */ import net.dv8tion.jda.api.AccountType;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AccountTypeException
/*    */   extends RuntimeException
/*    */ {
/*    */   private final AccountType requiredType;
/*    */   
/*    */   public AccountTypeException(AccountType requiredType) {
/* 37 */     this(requiredType, "The current AccountType is not valid for the attempted action. Required AccountType: " + requiredType);
/*    */   }
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
/*    */   public AccountTypeException(AccountType requiredType, String message) {
/* 50 */     super(message);
/* 51 */     this.requiredType = requiredType;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public AccountType getRequiredType() {
/* 61 */     return this.requiredType;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void check(AccountType actualType, AccountType requiredType) {
/* 66 */     if (actualType != requiredType)
/* 67 */       throw new AccountTypeException(requiredType); 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\exceptions\AccountTypeException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */