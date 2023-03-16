/*     */ package org.sqlite.core;
/*     */ 
/*     */ import java.sql.SQLException;
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
/*     */ public class SafeStmtPtr
/*     */ {
/*     */   private final DB db;
/*     */   private final long ptr;
/*     */   private volatile boolean closed = false;
/*     */   private int closedRC;
/*     */   private SQLException closeException;
/*     */   
/*     */   public SafeStmtPtr(DB db, long ptr) {
/*  31 */     this.db = db;
/*  32 */     this.ptr = ptr;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosed() {
/*  41 */     return this.closed;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int close() throws SQLException {
/*  52 */     synchronized (this.db) {
/*  53 */       return internalClose();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private int internalClose() throws SQLException {
/*     */     try {
/*  60 */       if (this.closed) {
/*  61 */         if (this.closeException != null) throw this.closeException; 
/*  62 */         return this.closedRC;
/*     */       } 
/*  64 */       this.closedRC = this.db.finalize(this, this.ptr);
/*  65 */       return this.closedRC;
/*  66 */     } catch (SQLException ex) {
/*  67 */       this.closeException = ex;
/*  68 */       throw ex;
/*     */     } finally {
/*  70 */       this.closed = true;
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
/*     */   public <E extends Throwable> int safeRunInt(SafePtrIntFunction<E> run) throws SQLException, E {
/*  82 */     synchronized (this.db) {
/*  83 */       ensureOpen();
/*  84 */       return run.run(this.db, this.ptr);
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
/*     */   public <E extends Throwable> long safeRunLong(SafePtrLongFunction<E> run) throws SQLException, E {
/*  97 */     synchronized (this.db) {
/*  98 */       ensureOpen();
/*  99 */       return run.run(this.db, this.ptr);
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
/*     */   public <E extends Throwable> double safeRunDouble(SafePtrDoubleFunction<E> run) throws SQLException, E {
/* 112 */     synchronized (this.db) {
/* 113 */       ensureOpen();
/* 114 */       return run.run(this.db, this.ptr);
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
/*     */   public <T, E extends Throwable> T safeRun(SafePtrFunction<T, E> run) throws SQLException, E {
/* 126 */     synchronized (this.db) {
/* 127 */       ensureOpen();
/* 128 */       return run.run(this.db, this.ptr);
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
/*     */   public <E extends Throwable> void safeRunConsume(SafePtrConsumer<E> run) throws SQLException, E {
/* 140 */     synchronized (this.db) {
/* 141 */       ensureOpen();
/* 142 */       run.run(this.db, this.ptr);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void ensureOpen() throws SQLException {
/* 147 */     if (this.closed) {
/* 148 */       throw new SQLException("stmt pointer is closed");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 154 */     if (this == o) return true; 
/* 155 */     if (o == null || getClass() != o.getClass()) return false; 
/* 156 */     SafeStmtPtr that = (SafeStmtPtr)o;
/* 157 */     return (this.ptr == that.ptr);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 162 */     return Long.hashCode(this.ptr);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SafePtrConsumer<E extends Throwable> {
/*     */     void run(DB param1DB, long param1Long) throws E;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SafePtrFunction<T, E extends Throwable> {
/*     */     T run(DB param1DB, long param1Long) throws E;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SafePtrDoubleFunction<E extends Throwable> {
/*     */     double run(DB param1DB, long param1Long) throws E;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SafePtrLongFunction<E extends Throwable> {
/*     */     long run(DB param1DB, long param1Long) throws E;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SafePtrIntFunction<E extends Throwable> {
/*     */     int run(DB param1DB, long param1Long) throws E;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\core\SafeStmtPtr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */