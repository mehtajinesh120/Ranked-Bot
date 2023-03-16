/*     */ package club.minnced.opus.util;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
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
/*     */ public class NativeUtil
/*     */ {
/*     */   public static void loadLibraryFromJar(String path) throws IOException {
/*  55 */     if (!path.startsWith("/")) {
/*  56 */       throw new IllegalArgumentException("The path has to be absolute (start with '/').");
/*     */     }
/*     */ 
/*     */     
/*  60 */     String[] parts = path.split("/");
/*  61 */     String filename = (parts.length > 1) ? parts[parts.length - 1] : null;
/*     */ 
/*     */     
/*  64 */     String prefix = "";
/*  65 */     String suffix = null;
/*  66 */     if (filename != null) {
/*  67 */       parts = filename.split("\\.", 2);
/*  68 */       prefix = parts[0];
/*  69 */       suffix = (parts.length > 1) ? ("." + parts[parts.length - 1]) : null;
/*     */     } 
/*     */ 
/*     */     
/*  73 */     if (filename == null || prefix.length() < 3) {
/*  74 */       throw new IllegalArgumentException("The filename has to be at least 3 characters long.");
/*     */     }
/*     */ 
/*     */     
/*  78 */     File temp = File.createTempFile(prefix, suffix);
/*  79 */     temp.deleteOnExit();
/*     */     
/*  81 */     if (!temp.exists()) {
/*  82 */       throw new FileNotFoundException("File " + temp.getAbsolutePath() + " does not exist.");
/*     */     }
/*     */ 
/*     */     
/*  86 */     byte[] buffer = new byte[1024];
/*     */ 
/*     */ 
/*     */     
/*  90 */     InputStream is = NativeUtil.class.getResourceAsStream(path);
/*  91 */     if (is == null) {
/*  92 */       throw new FileNotFoundException("File " + path + " was not found inside JAR.");
/*     */     }
/*     */ 
/*     */     
/*  96 */     OutputStream os = new FileOutputStream(temp); try {
/*     */       int readBytes;
/*  98 */       while ((readBytes = is.read(buffer)) != -1) {
/*  99 */         os.write(buffer, 0, readBytes);
/*     */       }
/*     */     } finally {
/*     */       
/* 103 */       os.close();
/* 104 */       is.close();
/*     */     } 
/*     */ 
/*     */     
/* 108 */     System.load(temp.getAbsolutePath());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\club\minnced\opu\\util\NativeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */