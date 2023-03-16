/*     */ package org.sqlite;
/*     */ 
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.sqlite.date.FastDateFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SQLiteConnectionConfig
/*     */   implements Cloneable
/*     */ {
/*  13 */   private SQLiteConfig.DateClass dateClass = SQLiteConfig.DateClass.INTEGER;
/*  14 */   private SQLiteConfig.DatePrecision datePrecision = SQLiteConfig.DatePrecision.MILLISECONDS;
/*     */   
/*  16 */   private String dateStringFormat = "yyyy-MM-dd HH:mm:ss.SSS";
/*  17 */   private FastDateFormat dateFormat = FastDateFormat.getInstance(this.dateStringFormat);
/*     */   
/*  19 */   private int transactionIsolation = 8;
/*  20 */   private SQLiteConfig.TransactionMode transactionMode = SQLiteConfig.TransactionMode.DEFERRED;
/*     */   private boolean autoCommit = true;
/*     */   
/*     */   public static SQLiteConnectionConfig fromPragmaTable(Properties pragmaTable) {
/*  24 */     return new SQLiteConnectionConfig(
/*  25 */         SQLiteConfig.DateClass.getDateClass(pragmaTable
/*  26 */           .getProperty(SQLiteConfig.Pragma.DATE_CLASS.pragmaName, SQLiteConfig.DateClass.INTEGER
/*     */             
/*  28 */             .name())), 
/*  29 */         SQLiteConfig.DatePrecision.getPrecision(pragmaTable
/*  30 */           .getProperty(SQLiteConfig.Pragma.DATE_PRECISION.pragmaName, SQLiteConfig.DatePrecision.MILLISECONDS
/*     */             
/*  32 */             .name())), pragmaTable
/*  33 */         .getProperty(SQLiteConfig.Pragma.DATE_STRING_FORMAT.pragmaName, "yyyy-MM-dd HH:mm:ss.SSS"), 8, 
/*     */ 
/*     */ 
/*     */         
/*  37 */         SQLiteConfig.TransactionMode.getMode(pragmaTable
/*  38 */           .getProperty(SQLiteConfig.Pragma.TRANSACTION_MODE.pragmaName, SQLiteConfig.TransactionMode.DEFERRED
/*     */             
/*  40 */             .name())), true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SQLiteConnectionConfig(SQLiteConfig.DateClass dateClass, SQLiteConfig.DatePrecision datePrecision, String dateStringFormat, int transactionIsolation, SQLiteConfig.TransactionMode transactionMode, boolean autoCommit) {
/*  51 */     setDateClass(dateClass);
/*  52 */     setDatePrecision(datePrecision);
/*  53 */     setDateStringFormat(dateStringFormat);
/*  54 */     setTransactionIsolation(transactionIsolation);
/*  55 */     setTransactionMode(transactionMode);
/*  56 */     setAutoCommit(autoCommit);
/*     */   }
/*     */   
/*     */   public SQLiteConnectionConfig copyConfig() {
/*  60 */     return new SQLiteConnectionConfig(this.dateClass, this.datePrecision, this.dateStringFormat, this.transactionIsolation, this.transactionMode, this.autoCommit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getDateMultiplier() {
/*  70 */     return (this.datePrecision == SQLiteConfig.DatePrecision.MILLISECONDS) ? 1L : 1000L;
/*     */   }
/*     */   
/*     */   public SQLiteConfig.DateClass getDateClass() {
/*  74 */     return this.dateClass;
/*     */   }
/*     */   
/*     */   public void setDateClass(SQLiteConfig.DateClass dateClass) {
/*  78 */     this.dateClass = dateClass;
/*     */   }
/*     */   
/*     */   public SQLiteConfig.DatePrecision getDatePrecision() {
/*  82 */     return this.datePrecision;
/*     */   }
/*     */   
/*     */   public void setDatePrecision(SQLiteConfig.DatePrecision datePrecision) {
/*  86 */     this.datePrecision = datePrecision;
/*     */   }
/*     */   
/*     */   public String getDateStringFormat() {
/*  90 */     return this.dateStringFormat;
/*     */   }
/*     */   
/*     */   public void setDateStringFormat(String dateStringFormat) {
/*  94 */     this.dateStringFormat = dateStringFormat;
/*  95 */     this.dateFormat = FastDateFormat.getInstance(dateStringFormat);
/*     */   }
/*     */   
/*     */   public FastDateFormat getDateFormat() {
/*  99 */     return this.dateFormat;
/*     */   }
/*     */   
/*     */   public boolean isAutoCommit() {
/* 103 */     return this.autoCommit;
/*     */   }
/*     */   
/*     */   public void setAutoCommit(boolean autoCommit) {
/* 107 */     this.autoCommit = autoCommit;
/*     */   }
/*     */   
/*     */   public int getTransactionIsolation() {
/* 111 */     return this.transactionIsolation;
/*     */   }
/*     */   
/*     */   public void setTransactionIsolation(int transactionIsolation) {
/* 115 */     this.transactionIsolation = transactionIsolation;
/*     */   }
/*     */   
/*     */   public SQLiteConfig.TransactionMode getTransactionMode() {
/* 119 */     return this.transactionMode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransactionMode(SQLiteConfig.TransactionMode transactionMode) {
/* 124 */     if (transactionMode == SQLiteConfig.TransactionMode.DEFFERED) {
/* 125 */       transactionMode = SQLiteConfig.TransactionMode.DEFERRED;
/*     */     }
/* 127 */     this.transactionMode = transactionMode;
/*     */   }
/*     */   
/* 130 */   private static final Map<SQLiteConfig.TransactionMode, String> beginCommandMap = new EnumMap<>(SQLiteConfig.TransactionMode.class);
/*     */ 
/*     */   
/*     */   static {
/* 134 */     beginCommandMap.put(SQLiteConfig.TransactionMode.DEFERRED, "begin;");
/* 135 */     beginCommandMap.put(SQLiteConfig.TransactionMode.IMMEDIATE, "begin immediate;");
/* 136 */     beginCommandMap.put(SQLiteConfig.TransactionMode.EXCLUSIVE, "begin exclusive;");
/*     */   }
/*     */   
/*     */   String transactionPrefix() {
/* 140 */     return beginCommandMap.get(this.transactionMode);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\SQLiteConnectionConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */