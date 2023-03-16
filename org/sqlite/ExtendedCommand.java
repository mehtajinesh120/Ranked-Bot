/*     */ package org.sqlite;
/*     */ 
/*     */ import java.sql.SQLException;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.sqlite.core.DB;
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
/*     */ public class ExtendedCommand
/*     */ {
/*     */   public static SQLExtension parse(String sql) throws SQLException {
/*  38 */     if (sql == null) return null; 
/*  39 */     if (sql.length() > 5 && sql.substring(0, 6).toLowerCase().equals("backup"))
/*  40 */       return BackupCommand.parse(sql); 
/*  41 */     if (sql.length() > 6 && sql.substring(0, 7).toLowerCase().equals("restore")) {
/*  42 */       return RestoreCommand.parse(sql);
/*     */     }
/*  44 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String removeQuotation(String s) {
/*  54 */     if (s == null) return s;
/*     */     
/*  56 */     if ((s.startsWith("\"") && s.endsWith("\"")) || (s.startsWith("'") && s.endsWith("'")))
/*  57 */       return s.substring(1, s.length() - 1); 
/*  58 */     return s;
/*     */   }
/*     */   
/*     */   public static interface SQLExtension
/*     */   {
/*     */     void execute(DB param1DB) throws SQLException;
/*     */   }
/*     */   
/*     */   public static class BackupCommand
/*     */     implements SQLExtension {
/*     */     public final String srcDB;
/*     */     public final String destFile;
/*     */     
/*     */     public BackupCommand(String srcDB, String destFile) {
/*  72 */       this.srcDB = srcDB;
/*  73 */       this.destFile = destFile;
/*     */     }
/*     */ 
/*     */     
/*  77 */     private static Pattern backupCmd = Pattern.compile("backup(\\s+(\"[^\"]*\"|'[^']*'|\\S+))?\\s+to\\s+(\"[^\"]*\"|'[^']*'|\\S+)", 2);
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
/*     */     public static BackupCommand parse(String sql) throws SQLException {
/*  89 */       if (sql != null) {
/*  90 */         Matcher m = backupCmd.matcher(sql);
/*  91 */         if (m.matches()) {
/*  92 */           String dbName = ExtendedCommand.removeQuotation(m.group(2));
/*  93 */           String dest = ExtendedCommand.removeQuotation(m.group(3));
/*  94 */           if (dbName == null || dbName.length() == 0) dbName = "main";
/*     */           
/*  96 */           return new BackupCommand(dbName, dest);
/*     */         } 
/*     */       } 
/*  99 */       throw new SQLException("syntax error: " + sql);
/*     */     }
/*     */     
/*     */     public void execute(DB db) throws SQLException {
/* 103 */       db.backup(this.srcDB, this.destFile, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RestoreCommand
/*     */     implements SQLExtension {
/*     */     public final String targetDB;
/*     */     public final String srcFile;
/* 111 */     private static Pattern restoreCmd = Pattern.compile("restore(\\s+(\"[^\"]*\"|'[^']*'|\\S+))?\\s+from\\s+(\"[^\"]*\"|'[^']*'|\\S+)", 2);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public RestoreCommand(String targetDB, String srcFile) {
/* 122 */       this.targetDB = targetDB;
/* 123 */       this.srcFile = srcFile;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static RestoreCommand parse(String sql) throws SQLException {
/* 134 */       if (sql != null) {
/* 135 */         Matcher m = restoreCmd.matcher(sql);
/* 136 */         if (m.matches()) {
/* 137 */           String dbName = ExtendedCommand.removeQuotation(m.group(2));
/* 138 */           String dest = ExtendedCommand.removeQuotation(m.group(3));
/* 139 */           if (dbName == null || dbName.length() == 0) dbName = "main"; 
/* 140 */           return new RestoreCommand(dbName, dest);
/*     */         } 
/*     */       } 
/* 143 */       throw new SQLException("syntax error: " + sql);
/*     */     }
/*     */ 
/*     */     
/*     */     public void execute(DB db) throws SQLException {
/* 148 */       db.restore(this.targetDB, this.srcFile, null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\ExtendedCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */