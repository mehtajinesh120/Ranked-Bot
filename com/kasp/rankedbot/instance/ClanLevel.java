/*    */ package com.kasp.rankedbot.instance;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.cache.ClanLevelCache;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.util.Map;
/*    */ import org.yaml.snakeyaml.Yaml;
/*    */ 
/*    */ 
/*    */ public class ClanLevel
/*    */ {
/*    */   private int level;
/*    */   private int neededXP;
/*    */   
/*    */   public ClanLevel(int level) {
/* 16 */     this.level = level;
/*    */     
/* 18 */     if (level == 0) {
/* 19 */       this.neededXP = 0;
/*    */     } else {
/*    */       
/* 22 */       Yaml yaml = new Yaml();
/*    */       try {
/* 24 */         Map<String, Object> data = (Map<String, Object>)yaml.load(new FileInputStream("RankedBot/clanlevels.yml"));
/*    */         
/* 26 */         this.neededXP = Integer.parseInt(data.get("l" + level).toString());
/* 27 */       } catch (FileNotFoundException e) {
/* 28 */         e.printStackTrace();
/*    */       } 
/*    */     } 
/*    */     
/* 32 */     ClanLevelCache.initializeLevel(level, this);
/*    */   }
/*    */   
/*    */   public int getNeededXP() {
/* 36 */     return this.neededXP;
/*    */   }
/*    */   
/*    */   public int getLevel() {
/* 40 */     return this.level;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\ClanLevel.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */