/*     */ package com.neovisionaries.ws.client;
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
/*     */ class Token
/*     */ {
/*     */   public static boolean isValid(String token) {
/*  29 */     if (token == null || token.length() == 0)
/*     */     {
/*  31 */       return false;
/*     */     }
/*     */     
/*  34 */     int len = token.length();
/*     */     
/*  36 */     for (int i = 0; i < len; i++) {
/*     */       
/*  38 */       if (isSeparator(token.charAt(i)))
/*     */       {
/*  40 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  44 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSeparator(char ch) {
/*  50 */     switch (ch) {
/*     */       
/*     */       case '\t':
/*     */       case ' ':
/*     */       case '"':
/*     */       case '(':
/*     */       case ')':
/*     */       case ',':
/*     */       case '/':
/*     */       case ':':
/*     */       case ';':
/*     */       case '<':
/*     */       case '=':
/*     */       case '>':
/*     */       case '?':
/*     */       case '@':
/*     */       case '[':
/*     */       case '\\':
/*     */       case ']':
/*     */       case '{':
/*     */       case '}':
/*  71 */         return true;
/*     */     } 
/*     */     
/*  74 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String unquote(String text) {
/*  81 */     if (text == null)
/*     */     {
/*  83 */       return null;
/*     */     }
/*     */     
/*  86 */     int len = text.length();
/*     */     
/*  88 */     if (len < 2 || text.charAt(0) != '"' || text.charAt(len - 1) != '"')
/*     */     {
/*  90 */       return text;
/*     */     }
/*     */     
/*  93 */     text = text.substring(1, len - 1);
/*     */     
/*  95 */     return unescape(text);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String unescape(String text) {
/* 101 */     if (text == null)
/*     */     {
/* 103 */       return null;
/*     */     }
/*     */     
/* 106 */     if (text.indexOf('\\') < 0)
/*     */     {
/* 108 */       return text;
/*     */     }
/*     */     
/* 111 */     int len = text.length();
/* 112 */     boolean escaped = false;
/* 113 */     StringBuilder builder = new StringBuilder();
/*     */ 
/*     */     
/* 116 */     for (int i = 0; i < len; i++) {
/*     */       
/* 118 */       char ch = text.charAt(i);
/*     */       
/* 120 */       if (ch == '\\' && !escaped) {
/*     */         
/* 122 */         escaped = true;
/*     */       }
/*     */       else {
/*     */         
/* 126 */         escaped = false;
/* 127 */         builder.append(ch);
/*     */       } 
/*     */     } 
/* 130 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\Token.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */