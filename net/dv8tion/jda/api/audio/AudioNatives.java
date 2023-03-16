/*     */ package net.dv8tion.jda.api.audio;
/*     */ 
/*     */ import club.minnced.opus.util.OpusLibrary;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
/*     */ import org.slf4j.Logger;
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
/*     */ public final class AudioNatives
/*     */ {
/*  33 */   private static final Logger LOG = JDALogger.getLog(AudioNatives.class);
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean initialized;
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean audioSupported;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isAudioSupported() {
/*  47 */     return audioSupported;
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
/*     */   public static boolean isInitialized() {
/*  59 */     return initialized;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static synchronized boolean ensureOpus() {
/*  70 */     if (initialized)
/*  71 */       return audioSupported; 
/*  72 */     initialized = true;
/*     */     
/*     */     try {
/*  75 */       if (OpusLibrary.isInitialized())
/*  76 */         return audioSupported = true; 
/*  77 */       audioSupported = OpusLibrary.loadFromJar();
/*     */     }
/*  79 */     catch (Throwable e) {
/*     */       
/*  81 */       handleException(e);
/*     */     }
/*     */     finally {
/*     */       
/*  85 */       if (audioSupported) {
/*  86 */         LOG.info("Audio System successfully setup!");
/*     */       } else {
/*  88 */         LOG.info("Audio System encountered problems while loading, thus, is disabled.");
/*     */       } 
/*  90 */     }  return audioSupported;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void handleException(Throwable e) {
/*  95 */     if (e instanceof UnsupportedOperationException) {
/*     */       
/*  97 */       LOG.error("Sorry, JDA's audio system doesn't support this system.\n{}", e.getMessage());
/*     */     }
/*  99 */     else if (e instanceof NoClassDefFoundError) {
/*     */       
/* 101 */       LOG.error("Missing opus dependency, unable to initialize audio!");
/*     */     }
/* 103 */     else if (e instanceof java.io.IOException) {
/*     */       
/* 105 */       LOG.error("There was an IO Exception when setting up the temp files for audio.", e);
/*     */     }
/* 107 */     else if (e instanceof UnsatisfiedLinkError) {
/*     */       
/* 109 */       LOG.error("JDA encountered a problem when attempting to load the Native libraries. Contact a DEV.", e);
/*     */     } else {
/* 111 */       if (e instanceof Error)
/*     */       {
/* 113 */         throw (Error)e;
/*     */       }
/*     */ 
/*     */       
/* 117 */       LOG.error("An unknown exception occurred while attempting to setup JDA's audio system!", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\AudioNatives.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */