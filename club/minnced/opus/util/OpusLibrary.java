/*     */ package club.minnced.opus.util;
/*     */ 
/*     */ import com.sun.jna.Platform;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public final class OpusLibrary
/*     */ {
/*     */   private static boolean initialized = false;
/*     */   private static final String SUPPORTED_SYSTEMS;
/*  32 */   private static final Map<String, String> platforms = new HashMap<>(10); static {
/*  33 */     platforms.put("darwin", "dylib");
/*  34 */     platforms.put("linux-arm", "so");
/*  35 */     platforms.put("linux-aarch64", "so");
/*  36 */     platforms.put("linux-x86", "so");
/*  37 */     platforms.put("linux-x86-64", "so");
/*  38 */     platforms.put("win32-x86", "dll");
/*  39 */     platforms.put("win32-x86-64", "dll");
/*  40 */     SUPPORTED_SYSTEMS = "Supported Systems: " + platforms.keySet() + "\nCurrent Operating system: " + Platform.RESOURCE_PREFIX;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<String> getSupportedPlatforms() {
/*  52 */     return Collections.unmodifiableList(new ArrayList<>(platforms.keySet()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isSupportedPlatform() {
/*  61 */     return platforms.containsKey(Platform.RESOURCE_PREFIX);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized boolean isInitialized() {
/*  71 */     return initialized;
/*     */   }
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
/*     */   public static synchronized boolean loadFrom(String absolutePath) {
/*  84 */     if (initialized)
/*  85 */       return false; 
/*  86 */     System.load(absolutePath);
/*  87 */     System.setProperty("opus.lib", absolutePath);
/*  88 */     return initialized = true;
/*     */   }
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
/*     */   public static synchronized boolean loadFromJar() throws IOException {
/* 103 */     if (initialized)
/* 104 */       return false; 
/* 105 */     String nativesRoot = "";
/*     */ 
/*     */     
/*     */     try {
/* 109 */       String platform = Platform.RESOURCE_PREFIX;
/* 110 */       String ext = platforms.get(platform);
/* 111 */       if (ext == null) {
/* 112 */         throw new UnsupportedOperationException(SUPPORTED_SYSTEMS);
/*     */       }
/* 114 */       String tmpRoot = String.format("/natives/%s/libopus.%s", new Object[] { platform, ext });
/* 115 */       NativeUtil.loadLibraryFromJar(tmpRoot);
/* 116 */       nativesRoot = tmpRoot;
/* 117 */       initialized = true;
/*     */     } finally {
/* 119 */       System.setProperty("opus.lib", nativesRoot);
/*     */     } 
/* 121 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\club\minnced\opu\\util\OpusLibrary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */