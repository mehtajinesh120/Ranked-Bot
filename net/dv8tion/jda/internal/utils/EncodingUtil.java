/*    */ package net.dv8tion.jda.internal.utils;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URLEncoder;
/*    */ import java.util.stream.Collectors;
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
/*    */ public class EncodingUtil
/*    */ {
/*    */   public static String encodeUTF8(String chars) {
/*    */     try {
/* 29 */       return URLEncoder.encode(chars, "UTF-8");
/*    */     }
/* 31 */     catch (UnsupportedEncodingException e) {
/*    */       
/* 33 */       throw new AssertionError(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static String encodeCodepointsUTF8(String input) {
/* 39 */     if (!input.startsWith("U+"))
/* 40 */       throw new IllegalArgumentException("Invalid format"); 
/* 41 */     String[] codePoints = input.substring(2).split("\\s*U\\+\\s*");
/* 42 */     StringBuilder encoded = new StringBuilder();
/* 43 */     for (String part : codePoints) {
/*    */       
/* 45 */       String utf16 = decodeCodepoint(part, 16);
/* 46 */       String urlEncoded = encodeUTF8(utf16);
/* 47 */       encoded.append(urlEncoded);
/*    */     } 
/* 49 */     return encoded.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public static String decodeCodepoint(String codepoint) {
/* 54 */     if (!codepoint.startsWith("U+"))
/* 55 */       throw new IllegalArgumentException("Invalid format"); 
/* 56 */     return decodeCodepoint(codepoint.substring(2), 16);
/*    */   }
/*    */ 
/*    */   
/*    */   public static String encodeCodepoints(String unicode) {
/* 61 */     return unicode.codePoints()
/* 62 */       .<CharSequence>mapToObj(code -> "U+" + Integer.toHexString(code))
/* 63 */       .collect(Collectors.joining());
/*    */   }
/*    */ 
/*    */   
/*    */   private static String decodeCodepoint(String hex, int radix) {
/* 68 */     int codePoint = Integer.parseUnsignedInt(hex, radix);
/* 69 */     return String.valueOf(Character.toChars(codePoint));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String encodeReaction(String unicode) {
/* 79 */     if (unicode.startsWith("U+") || unicode.startsWith("u+")) {
/* 80 */       return encodeCodepointsUTF8(unicode);
/*    */     }
/* 82 */     return encodeUTF8(unicode);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\EncodingUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */