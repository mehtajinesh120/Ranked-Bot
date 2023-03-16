/*     */ package net.dv8tion.jda.internal.utils;
/*     */ 
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Properties;
/*     */ import org.slf4j.helpers.FormattingTuple;
/*     */ import org.slf4j.helpers.MarkerIgnoringBase;
/*     */ import org.slf4j.helpers.MessageFormatter;
/*     */ import org.slf4j.helpers.Util;
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
/*     */ class SimpleLogger
/*     */   extends MarkerIgnoringBase
/*     */ {
/*     */   private static final long serialVersionUID = -632788891211436180L;
/*     */   private static final String CONFIGURATION_FILE = "Loggerger.properties";
/*  41 */   private static long START_TIME = System.currentTimeMillis();
/*  42 */   private static final Properties SIMPLE_LOGGER_PROPS = new Properties();
/*     */   
/*     */   private static final int LOG_LEVEL_TRACE = 0;
/*     */   
/*     */   private static final int LOG_LEVEL_DEBUG = 10;
/*     */   
/*     */   private static final int LOG_LEVEL_INFO = 20;
/*     */   private static final int LOG_LEVEL_WARN = 30;
/*     */   private static final int LOG_LEVEL_ERROR = 40;
/*     */   private static boolean INITIALIZED = false;
/*  52 */   private static int DEFAULT_LOG_LEVEL = 20;
/*     */   private static boolean SHOW_DATE_TIME = false;
/*  54 */   private static String DATE_TIME_FORMAT_STR = null;
/*  55 */   private static DateFormat DATE_FORMATTER = null;
/*     */   private static boolean SHOW_THREAD_NAME = true;
/*     */   private static boolean SHOW_LOG_NAME = true;
/*     */   private static boolean SHOW_SHORT_LOG_NAME = false;
/*  59 */   private static String LOG_FILE = "System.err";
/*  60 */   private static PrintStream TARGET_STREAM = null;
/*     */   private static boolean LEVEL_IN_BRACKETS = false;
/*  62 */   private static String WARN_LEVEL_STRING = "WARN";
/*     */   
/*     */   public static final String SYSTEM_PREFIX = "org.slf4j.Loggerger.";
/*     */   
/*     */   public static final String DEFAULT_LOG_LEVEL_KEY = "org.slf4j.Loggerger.defaultLogLevel";
/*     */   
/*     */   public static final String SHOW_DATE_TIME_KEY = "org.slf4j.Loggerger.showDateTime";
/*     */   public static final String DATE_TIME_FORMAT_KEY = "org.slf4j.Loggerger.dateTimeFormat";
/*     */   public static final String SHOW_THREAD_NAME_KEY = "org.slf4j.Loggerger.showThreadName";
/*     */   public static final String SHOW_LOG_NAME_KEY = "org.slf4j.Loggerger.showLogName";
/*     */   public static final String SHOW_SHORT_LOG_NAME_KEY = "org.slf4j.Loggerger.showShortLogName";
/*     */   public static final String LOG_FILE_KEY = "org.slf4j.Loggerger.logFile";
/*     */   public static final String LEVEL_IN_BRACKETS_KEY = "org.slf4j.Loggerger.levelInBrackets";
/*     */   public static final String WARN_LEVEL_STRING_KEY = "org.slf4j.Loggerger.warnLevelString";
/*     */   public static final String LOG_KEY_PREFIX = "org.slf4j.Loggerger.log.";
/*     */   
/*     */   private static String getStringProperty(String name) {
/*  79 */     String prop = null;
/*     */     try {
/*  81 */       prop = System.getProperty(name);
/*  82 */     } catch (SecurityException securityException) {}
/*     */ 
/*     */     
/*  85 */     return (prop == null) ? SIMPLE_LOGGER_PROPS.getProperty(name) : prop;
/*     */   }
/*     */   
/*     */   private static String getStringProperty(String name, String defaultValue) {
/*  89 */     String prop = getStringProperty(name);
/*  90 */     return (prop == null) ? defaultValue : prop;
/*     */   }
/*     */   
/*     */   private static boolean getBooleanProperty(String name, boolean defaultValue) {
/*  94 */     String prop = getStringProperty(name);
/*  95 */     return (prop == null) ? defaultValue : "true".equalsIgnoreCase(prop);
/*     */   }
/*     */   
/*     */   static void init() {
/*  99 */     INITIALIZED = true;
/* 100 */     loadProperties();
/*     */     
/* 102 */     String defaultLogLevelString = getStringProperty("org.slf4j.Loggerger.defaultLogLevel", null);
/* 103 */     if (defaultLogLevelString != null) {
/* 104 */       DEFAULT_LOG_LEVEL = stringToLevel(defaultLogLevelString);
/*     */     }
/* 106 */     SHOW_LOG_NAME = getBooleanProperty("org.slf4j.Loggerger.showLogName", SHOW_LOG_NAME);
/* 107 */     SHOW_SHORT_LOG_NAME = getBooleanProperty("org.slf4j.Loggerger.showShortLogName", SHOW_SHORT_LOG_NAME);
/* 108 */     SHOW_DATE_TIME = getBooleanProperty("org.slf4j.Loggerger.showDateTime", SHOW_DATE_TIME);
/* 109 */     SHOW_THREAD_NAME = getBooleanProperty("org.slf4j.Loggerger.showThreadName", SHOW_THREAD_NAME);
/* 110 */     DATE_TIME_FORMAT_STR = getStringProperty("org.slf4j.Loggerger.dateTimeFormat", DATE_TIME_FORMAT_STR);
/* 111 */     LEVEL_IN_BRACKETS = getBooleanProperty("org.slf4j.Loggerger.levelInBrackets", LEVEL_IN_BRACKETS);
/* 112 */     WARN_LEVEL_STRING = getStringProperty("org.slf4j.Loggerger.warnLevelString", WARN_LEVEL_STRING);
/*     */     
/* 114 */     LOG_FILE = getStringProperty("org.slf4j.Loggerger.logFile", LOG_FILE);
/* 115 */     TARGET_STREAM = computeTargetStream(LOG_FILE);
/*     */     
/* 117 */     if (DATE_TIME_FORMAT_STR != null) {
/*     */       try {
/* 119 */         DATE_FORMATTER = new SimpleDateFormat(DATE_TIME_FORMAT_STR);
/* 120 */       } catch (IllegalArgumentException e) {
/* 121 */         Util.report("Bad date format in Loggerger.properties; will output relative time", e);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private static PrintStream computeTargetStream(String logFile) {
/* 127 */     if ("System.err".equalsIgnoreCase(logFile))
/* 128 */       return System.err; 
/* 129 */     if ("System.out".equalsIgnoreCase(logFile)) {
/* 130 */       return System.out;
/*     */     }
/*     */     try {
/* 133 */       FileOutputStream fos = new FileOutputStream(logFile);
/* 134 */       PrintStream printStream = new PrintStream(fos);
/* 135 */       return printStream;
/* 136 */     } catch (FileNotFoundException e) {
/* 137 */       Util.report("Could not open [" + logFile + "]. Defaulting to System.err", e);
/* 138 */       return System.err;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadProperties() {
/* 145 */     InputStream in = AccessController.<InputStream>doPrivileged(new PrivilegedAction<InputStream>() {
/*     */           public InputStream run() {
/* 147 */             ClassLoader threadCL = Thread.currentThread().getContextClassLoader();
/* 148 */             if (threadCL != null) {
/* 149 */               return threadCL.getResourceAsStream("Loggerger.properties");
/*     */             }
/* 151 */             return ClassLoader.getSystemResourceAsStream("Loggerger.properties");
/*     */           }
/*     */         });
/*     */     
/* 155 */     if (null != in) {
/*     */       try {
/* 157 */         SIMPLE_LOGGER_PROPS.load(in);
/* 158 */         in.close();
/* 159 */       } catch (IOException iOException) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 165 */   protected int currentLogLevel = 20;
/* 166 */   private transient String shortLogName = null;
/*     */   
/*     */   SimpleLogger(String name) {
/* 169 */     if (!INITIALIZED) {
/* 170 */       init();
/*     */     }
/* 172 */     this.name = name;
/*     */     
/* 174 */     String levelString = recursivelyComputeLevelString();
/* 175 */     if (levelString != null) {
/* 176 */       this.currentLogLevel = stringToLevel(levelString);
/*     */     } else {
/* 178 */       this.currentLogLevel = DEFAULT_LOG_LEVEL;
/*     */     } 
/*     */   }
/*     */   
/*     */   String recursivelyComputeLevelString() {
/* 183 */     String tempName = this.name;
/* 184 */     String levelString = null;
/* 185 */     int indexOfLastDot = tempName.length();
/* 186 */     while (levelString == null && indexOfLastDot > -1) {
/* 187 */       tempName = tempName.substring(0, indexOfLastDot);
/* 188 */       levelString = getStringProperty("org.slf4j.Loggerger.log." + tempName, null);
/* 189 */       indexOfLastDot = String.valueOf(tempName).lastIndexOf(".");
/*     */     } 
/* 191 */     return levelString;
/*     */   }
/*     */   
/*     */   private static int stringToLevel(String levelStr) {
/* 195 */     if ("trace".equalsIgnoreCase(levelStr))
/* 196 */       return 0; 
/* 197 */     if ("debug".equalsIgnoreCase(levelStr))
/* 198 */       return 10; 
/* 199 */     if ("info".equalsIgnoreCase(levelStr))
/* 200 */       return 20; 
/* 201 */     if ("warn".equalsIgnoreCase(levelStr))
/* 202 */       return 30; 
/* 203 */     if ("error".equalsIgnoreCase(levelStr)) {
/* 204 */       return 40;
/*     */     }
/*     */     
/* 207 */     return 20;
/*     */   }
/*     */   
/*     */   private void log(int level, String message, Throwable t) {
/* 211 */     if (!isLevelEnabled(level)) {
/*     */       return;
/*     */     }
/*     */     
/* 215 */     StringBuilder buf = new StringBuilder(32);
/*     */ 
/*     */     
/* 218 */     if (SHOW_DATE_TIME) {
/* 219 */       if (DATE_FORMATTER != null) {
/* 220 */         buf.append(getFormattedDate());
/* 221 */         buf.append(' ');
/*     */       } else {
/* 223 */         buf.append(System.currentTimeMillis() - START_TIME);
/* 224 */         buf.append(' ');
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 229 */     if (SHOW_THREAD_NAME) {
/* 230 */       buf.append('[');
/* 231 */       buf.append(Thread.currentThread().getName());
/* 232 */       buf.append("] ");
/*     */     } 
/*     */     
/* 235 */     if (LEVEL_IN_BRACKETS) {
/* 236 */       buf.append('[');
/*     */     }
/*     */     
/* 239 */     switch (level) {
/*     */       case 0:
/* 241 */         buf.append("TRACE");
/*     */         break;
/*     */       case 10:
/* 244 */         buf.append("DEBUG");
/*     */         break;
/*     */       case 20:
/* 247 */         buf.append("INFO");
/*     */         break;
/*     */       case 30:
/* 250 */         buf.append(WARN_LEVEL_STRING);
/*     */         break;
/*     */       case 40:
/* 253 */         buf.append("ERROR");
/*     */         break;
/*     */     } 
/* 256 */     if (LEVEL_IN_BRACKETS)
/* 257 */       buf.append(']'); 
/* 258 */     buf.append(' ');
/*     */ 
/*     */     
/* 261 */     if (SHOW_SHORT_LOG_NAME) {
/* 262 */       if (this.shortLogName == null)
/* 263 */         this.shortLogName = computeShortName(); 
/* 264 */       buf.append(String.valueOf(this.shortLogName)).append(" - ");
/* 265 */     } else if (SHOW_LOG_NAME) {
/* 266 */       buf.append(String.valueOf(this.name)).append(" - ");
/*     */     } 
/*     */ 
/*     */     
/* 270 */     buf.append(message);
/*     */     
/* 272 */     write(buf, t);
/*     */   }
/*     */ 
/*     */   
/*     */   void write(StringBuilder buf, Throwable t) {
/* 277 */     TARGET_STREAM.println(buf.toString());
/* 278 */     if (t != null) {
/* 279 */       t.printStackTrace(TARGET_STREAM);
/*     */     }
/* 281 */     TARGET_STREAM.flush();
/*     */   }
/*     */   private String getFormattedDate() {
/*     */     String dateText;
/* 285 */     Date now = new Date();
/*     */     
/* 287 */     synchronized (DATE_FORMATTER) {
/* 288 */       dateText = DATE_FORMATTER.format(now);
/*     */     } 
/* 290 */     return dateText;
/*     */   }
/*     */   
/*     */   private String computeShortName() {
/* 294 */     return this.name.substring(this.name.lastIndexOf(".") + 1);
/*     */   }
/*     */   
/*     */   private void formatAndLog(int level, String format, Object arg1, Object arg2) {
/* 298 */     if (!isLevelEnabled(level)) {
/*     */       return;
/*     */     }
/* 301 */     FormattingTuple tp = MessageFormatter.format(format, arg1, arg2);
/* 302 */     log(level, tp.getMessage(), tp.getThrowable());
/*     */   }
/*     */   
/*     */   private void formatAndLog(int level, String format, Object... arguments) {
/* 306 */     if (!isLevelEnabled(level)) {
/*     */       return;
/*     */     }
/* 309 */     FormattingTuple tp = MessageFormatter.arrayFormat(format, arguments);
/* 310 */     log(level, tp.getMessage(), tp.getThrowable());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isLevelEnabled(int logLevel) {
/* 316 */     return (logLevel >= this.currentLogLevel);
/*     */   }
/*     */   
/*     */   public boolean isTraceEnabled() {
/* 320 */     return isLevelEnabled(0);
/*     */   }
/*     */   
/*     */   public void trace(String msg) {
/* 324 */     log(0, msg, null);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object param1) {
/* 328 */     formatAndLog(0, format, param1, null);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object param1, Object param2) {
/* 332 */     formatAndLog(0, format, param1, param2);
/*     */   }
/*     */   
/*     */   public void trace(String format, Object... argArray) {
/* 336 */     formatAndLog(0, format, argArray);
/*     */   }
/*     */   
/*     */   public void trace(String msg, Throwable t) {
/* 340 */     log(0, msg, t);
/*     */   }
/*     */   
/*     */   public boolean isDebugEnabled() {
/* 344 */     return isLevelEnabled(10);
/*     */   }
/*     */   
/*     */   public void debug(String msg) {
/* 348 */     log(10, msg, null);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object param1) {
/* 352 */     formatAndLog(10, format, param1, null);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object param1, Object param2) {
/* 356 */     formatAndLog(10, format, param1, param2);
/*     */   }
/*     */   
/*     */   public void debug(String format, Object... argArray) {
/* 360 */     formatAndLog(10, format, argArray);
/*     */   }
/*     */   
/*     */   public void debug(String msg, Throwable t) {
/* 364 */     log(10, msg, t);
/*     */   }
/*     */   
/*     */   public boolean isInfoEnabled() {
/* 368 */     return isLevelEnabled(20);
/*     */   }
/*     */   
/*     */   public void info(String msg) {
/* 372 */     log(20, msg, null);
/*     */   }
/*     */   
/*     */   public void info(String format, Object arg) {
/* 376 */     formatAndLog(20, format, arg, null);
/*     */   }
/*     */   
/*     */   public void info(String format, Object arg1, Object arg2) {
/* 380 */     formatAndLog(20, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void info(String format, Object... argArray) {
/* 384 */     formatAndLog(20, format, argArray);
/*     */   }
/*     */   
/*     */   public void info(String msg, Throwable t) {
/* 388 */     log(20, msg, t);
/*     */   }
/*     */   
/*     */   public boolean isWarnEnabled() {
/* 392 */     return isLevelEnabled(30);
/*     */   }
/*     */   
/*     */   public void warn(String msg) {
/* 396 */     log(30, msg, null);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object arg) {
/* 400 */     formatAndLog(30, format, arg, null);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object arg1, Object arg2) {
/* 404 */     formatAndLog(30, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void warn(String format, Object... argArray) {
/* 408 */     formatAndLog(30, format, argArray);
/*     */   }
/*     */   
/*     */   public void warn(String msg, Throwable t) {
/* 412 */     log(30, msg, t);
/*     */   }
/*     */   
/*     */   public boolean isErrorEnabled() {
/* 416 */     return isLevelEnabled(40);
/*     */   }
/*     */   
/*     */   public void error(String msg) {
/* 420 */     log(40, msg, null);
/*     */   }
/*     */   
/*     */   public void error(String format, Object arg) {
/* 424 */     formatAndLog(40, format, arg, null);
/*     */   }
/*     */   
/*     */   public void error(String format, Object arg1, Object arg2) {
/* 428 */     formatAndLog(40, format, arg1, arg2);
/*     */   }
/*     */   
/*     */   public void error(String format, Object... argArray) {
/* 432 */     formatAndLog(40, format, argArray);
/*     */   }
/*     */   
/*     */   public void error(String msg, Throwable t) {
/* 436 */     log(40, msg, t);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\SimpleLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */