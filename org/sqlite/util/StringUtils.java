/*    */ package org.sqlite.util;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class StringUtils {
/*    */   public static String join(List<String> list, String separator) {
/*  7 */     StringBuilder sb = new StringBuilder();
/*  8 */     boolean first = true;
/*  9 */     for (String item : list) {
/* 10 */       if (first) { first = false; }
/* 11 */       else { sb.append(separator); }
/*    */       
/* 13 */       sb.append(item);
/*    */     } 
/* 15 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlit\\util\StringUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */