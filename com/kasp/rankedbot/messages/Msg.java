/*    */ package com.kasp.rankedbot.messages;
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
/*    */ public class Msg {
/* 14 */   public static HashMap<String, String> msgData = new HashMap<>();
/*    */   
/*    */   public static void loadMsg() {
/* 17 */     String filename = "messages.yml";
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
/* 32 */         Map<String, Object> data = (Map<String, Object>)yaml.load(new FileInputStream("RankedBot/messages.yml"));
/* 33 */         for (String s : data.keySet()) {
/* 34 */           msgData.put(s, data.get(s).toString());
/*    */         }
/* 36 */         if (inputStream != null) inputStream.close();  } catch (Throwable throwable) { if (inputStream != null) try { inputStream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 37 */     { e.printStackTrace(); }
/*    */ 
/*    */     
/* 40 */     System.out.println("Successfully loaded the messages file into memory");
/*    */   }
/*    */   
/*    */   public static void reload() {
/* 44 */     msgData.clear();
/*    */     
/* 46 */     loadMsg();
/*    */   }
/*    */   
/*    */   public static String getMsg(String msg) {
/* 50 */     return msgData.get(msg);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\messages\Msg.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */