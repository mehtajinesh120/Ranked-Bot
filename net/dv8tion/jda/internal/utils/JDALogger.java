/*     */ package net.dv8tion.jda.internal.utils;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Map;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.function.Function;
/*     */ import org.apache.commons.collections4.map.CaseInsensitiveMap;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class JDALogger
/*     */ {
/*     */   public static final boolean SLF4J_ENABLED;
/*     */   
/*     */   static {
/*  45 */     boolean tmp = false;
/*     */ 
/*     */     
/*     */     try {
/*  49 */       Class.forName("org.slf4j.impl.StaticLoggerBinder");
/*     */       
/*  51 */       tmp = true;
/*     */     }
/*  53 */     catch (ClassNotFoundException eStatic) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */         
/*  59 */         Class<?> serviceProviderInterface = Class.forName("org.slf4j.spi.SLF4JServiceProvider");
/*     */ 
/*     */         
/*  62 */         tmp = ServiceLoader.load(serviceProviderInterface).iterator().hasNext();
/*     */       }
/*  64 */       catch (ClassNotFoundException eService) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  69 */         LoggerFactory.getLogger(JDALogger.class);
/*     */         
/*  71 */         tmp = false;
/*     */       } 
/*     */     } 
/*     */     
/*  75 */     SLF4J_ENABLED = tmp;
/*     */   }
/*     */   
/*  78 */   private static final Map<String, Logger> LOGS = (Map<String, Logger>)new CaseInsensitiveMap();
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
/*     */   public static Logger getLog(String name) {
/*  95 */     synchronized (LOGS) {
/*     */       
/*  97 */       if (SLF4J_ENABLED)
/*  98 */         return LoggerFactory.getLogger(name); 
/*  99 */       return LOGS.computeIfAbsent(name, SimpleLogger::new);
/*     */     } 
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
/*     */   
/*     */   public static Logger getLog(Class<?> clazz) {
/* 116 */     synchronized (LOGS) {
/*     */       
/* 118 */       if (SLF4J_ENABLED)
/* 119 */         return LoggerFactory.getLogger(clazz); 
/* 120 */       return LOGS.computeIfAbsent(clazz.getName(), n -> new SimpleLogger(clazz.getSimpleName()));
/*     */     } 
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
/*     */   public static Object getLazyString(final LazyEvaluation lazyLambda) {
/* 134 */     return new Object()
/*     */       {
/*     */ 
/*     */         
/*     */         public String toString()
/*     */         {
/*     */           try {
/* 141 */             return lazyLambda.getString();
/*     */           }
/* 143 */           catch (Exception ex) {
/*     */             
/* 145 */             StringWriter sw = new StringWriter();
/* 146 */             ex.printStackTrace(new PrintWriter(sw));
/* 147 */             return "Error while evaluating lazy String... " + sw.toString();
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface LazyEvaluation {
/*     */     String getString() throws Exception;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\JDALogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */