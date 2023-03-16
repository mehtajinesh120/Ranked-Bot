/*    */ package org.jsoup.internal;
/*    */ 
/*    */ import java.util.Locale;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Normalizer
/*    */ {
/*    */   public static String lowerCase(String input) {
/* 12 */     return (input != null) ? input.toLowerCase(Locale.ENGLISH) : "";
/*    */   }
/*    */ 
/*    */   
/*    */   public static String normalize(String input) {
/* 17 */     return lowerCase(input).trim();
/*    */   }
/*    */ 
/*    */   
/*    */   public static String normalize(String input, boolean isStringLiteral) {
/* 22 */     return isStringLiteral ? lowerCase(input) : normalize(input);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\internal\Normalizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */