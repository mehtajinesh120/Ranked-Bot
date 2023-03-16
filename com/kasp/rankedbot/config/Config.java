/*    */ package com.kasp.rankedbot.config;
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
/*    */ public class Config {
/* 14 */   public static HashMap<String, String> configData = new HashMap<>();
/*    */   
/*    */   public static void loadConfig() {
/* 17 */     String filename = "config.yml";
/* 18 */     ClassLoader classLoader = RankedBot.class.getClassLoader();
/*    */     
/* 20 */     try { InputStream inputStream = classLoader.getResourceAsStream(filename); 
/* 21 */       try { String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
/*    */         
/* 23 */         File file = new File("RankedBot/" + filename);
/* 24 */         if (!file.exists()) {
/* 25 */           file.createNewFile();
/* 26 */           BufferedWriter bw = new BufferedWriter(new FileWriter("RankedBot/" + filename));
/* 27 */           bw.write(result);
/* 28 */           bw.close();
/*    */         } 
/*    */         
/* 31 */         Yaml yaml = new Yaml();
/* 32 */         Map<String, Object> data = (Map<String, Object>)yaml.load(new FileInputStream("RankedBot/config.yml"));
/* 33 */         for (String s : data.keySet()) {
/* 34 */           if (data.get(s) != null) {
/* 35 */             configData.put(s, data.get(s).toString());
/*    */           }
/*    */         } 
/* 38 */         if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 39 */     { e.printStackTrace(); }
/*    */ 
/*    */     
/* 42 */     System.out.println("Successfully loaded the config file into memory");
/*    */   }
/*    */   
/*    */   public static void reload() {
/* 46 */     configData.clear();
/*    */     
/* 48 */     loadConfig();
/*    */   }
/*    */   
/*    */   public static String getValue(String key) {
/* 52 */     return configData.get(key);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\config\Config.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */