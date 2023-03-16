/*    */ package com.kasp.rankedbot.levelsfile;
/*    */ import com.kasp.rankedbot.RankedBot;
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.yaml.snakeyaml.Yaml;
/*    */ 
/*    */ public class Levels {
/* 14 */   public static HashMap<String, String> levelsData = new HashMap<>();
/* 15 */   public static HashMap<String, String> clanLevelsData = new HashMap<>();
/*    */   
/*    */   public static void loadLevels() {
/* 18 */     String filename = "levels.yml";
/* 19 */     ClassLoader classLoader = RankedBot.class.getClassLoader();
/*    */     
/* 21 */     try { InputStream inputStream = classLoader.getResourceAsStream(filename); 
/* 22 */       try { String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
/*    */         
/* 24 */         File file = new File("RankedBot/" + filename);
/* 25 */         if (!file.exists()) {
/* 26 */           file.createNewFile();
/* 27 */           BufferedWriter bw = new BufferedWriter(new FileWriter("RankedBot/" + filename));
/* 28 */           bw.write(result);
/* 29 */           bw.close();
/*    */         } 
/*    */         
/* 32 */         Yaml yaml = new Yaml();
/* 33 */         Map<String, Object> data = (Map<String, Object>)yaml.load(new FileInputStream("RankedBot/" + filename));
/* 34 */         for (String s : data.keySet()) {
/* 35 */           levelsData.put(s, data.get(s).toString());
/*    */         }
/* 37 */         if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 38 */     { e.printStackTrace(); }
/*    */ 
/*    */     
/* 41 */     System.out.println("Successfully loaded the levels file into memory");
/*    */   }
/*    */   
/*    */   public static String getLevel(String key) {
/* 45 */     return levelsData.get(key);
/*    */   }
/*    */   
/*    */   public static void loadClanLevels() {
/* 49 */     String filename = "clanlevels.yml";
/* 50 */     ClassLoader classLoader = RankedBot.class.getClassLoader();
/*    */     
/* 52 */     try { InputStream inputStream = classLoader.getResourceAsStream(filename); 
/* 53 */       try { String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
/*    */         
/* 55 */         File file = new File("RankedBot/" + filename);
/* 56 */         if (!file.exists()) {
/* 57 */           file.createNewFile();
/* 58 */           BufferedWriter bw = new BufferedWriter(new FileWriter("RankedBot/" + filename));
/* 59 */           bw.write(result);
/* 60 */           bw.close();
/*    */         } 
/*    */         
/* 63 */         Yaml yaml = new Yaml();
/* 64 */         Map<String, Object> data = (Map<String, Object>)yaml.load(new FileInputStream("RankedBot/" + filename));
/* 65 */         for (String s : data.keySet()) {
/* 66 */           clanLevelsData.put(s, data.get(s).toString());
/*    */         }
/* 68 */         if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 69 */     { e.printStackTrace(); }
/*    */ 
/*    */     
/* 72 */     System.out.println("Successfully loaded the clan levels file into memory");
/*    */   }
/*    */   
/*    */   public static String getClanLevel(String key) {
/* 76 */     return clanLevelsData.get(key);
/*    */   }
/*    */   
/*    */   public static void reload() {
/* 80 */     levelsData.clear();
/* 81 */     clanLevelsData.clear();
/*    */     
/* 83 */     loadLevels();
/* 84 */     loadClanLevels();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\levelsfile\Levels.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */