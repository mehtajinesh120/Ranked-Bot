/*    */ package com.kasp.rankedbot.instance;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.cache.ThemeCache;
/*    */ 
/*    */ public class Theme
/*    */ {
/*    */   private String name;
/*    */   
/*    */   public Theme(String name) {
/* 10 */     this.name = name;
/*    */     
/* 12 */     ThemeCache.initializeTheme(name, this);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 16 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\Theme.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */