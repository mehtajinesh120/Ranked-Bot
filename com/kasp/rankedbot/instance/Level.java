/*    */ package com.kasp.rankedbot.instance;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.cache.LevelCache;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.yaml.snakeyaml.Yaml;
/*    */ 
/*    */ 
/*    */ public class Level
/*    */ {
/*    */   private int level;
/*    */   private int neededXP;
/*    */   private List<String> rewards;
/*    */   
/*    */   public Level(int level) {
/* 20 */     this.level = level;
/* 21 */     this.rewards = new ArrayList<>();
/*    */     
/* 23 */     if (level == 0) {
/* 24 */       this.neededXP = 0;
/*    */     } else {
/*    */       
/* 27 */       Yaml yaml = new Yaml();
/*    */       try {
/* 29 */         Map<String, Object> data = (Map<String, Object>)yaml.load(new FileInputStream("RankedBot/levels.yml"));
/*    */         
/* 31 */         String levelData = data.get("l" + level).toString();
/*    */         
/* 33 */         if (levelData.contains(";;;")) {
/* 34 */           this.neededXP = Integer.parseInt(levelData.split(";;;")[0]);
/*    */           
/* 36 */           String rewards = levelData.split(";;;")[1];
/*    */           
/* 38 */           if (rewards.contains(",")) {
/* 39 */             this.rewards.addAll(Arrays.asList(rewards.split(",")));
/*    */           } else {
/*    */             
/* 42 */             this.rewards.add(rewards);
/*    */           } 
/*    */         } else {
/*    */           
/* 46 */           this.neededXP = Integer.parseInt(levelData);
/*    */         } 
/* 48 */       } catch (FileNotFoundException e) {
/* 49 */         throw new RuntimeException(e);
/*    */       } 
/*    */     } 
/*    */     
/* 53 */     LevelCache.initializeLevel(level, this);
/*    */   }
/*    */   
/*    */   public int getNeededXP() {
/* 57 */     return this.neededXP;
/*    */   }
/*    */   
/*    */   public int getLevel() {
/* 61 */     return this.level;
/*    */   }
/*    */   
/*    */   public List<String> getRewards() {
/* 65 */     return this.rewards;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\Level.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */