/*    */ package org.sqlite.util;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ 
/*    */ public class ProcessRunner {
/*    */   String runAndWaitFor(String command) throws IOException, InterruptedException {
/* 10 */     Process p = Runtime.getRuntime().exec(command);
/* 11 */     p.waitFor();
/*    */     
/* 13 */     return getProcessOutput(p);
/*    */   }
/*    */ 
/*    */   
/*    */   String runAndWaitFor(String command, long timeout, TimeUnit unit) throws IOException, InterruptedException {
/* 18 */     Process p = Runtime.getRuntime().exec(command);
/* 19 */     p.waitFor(timeout, unit);
/*    */     
/* 21 */     return getProcessOutput(p);
/*    */   }
/*    */   
/*    */   static String getProcessOutput(Process process) throws IOException {
/* 25 */     try (InputStream in = process.getInputStream()) {
/*    */       
/* 27 */       ByteArrayOutputStream b = new ByteArrayOutputStream();
/* 28 */       byte[] buf = new byte[32]; int readLen;
/* 29 */       while ((readLen = in.read(buf, 0, buf.length)) >= 0) {
/* 30 */         b.write(buf, 0, readLen);
/*    */       }
/* 32 */       return b.toString();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlit\\util\ProcessRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */