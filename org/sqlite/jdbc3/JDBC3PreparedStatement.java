/*     */ package org.sqlite.jdbc3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.URL;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Date;
/*     */ import java.sql.ParameterMetaData;
/*     */ import java.sql.Ref;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.core.CorePreparedStatement;
/*     */ import org.sqlite.core.CoreStatement;
/*     */ import org.sqlite.core.DB;
/*     */ 
/*     */ public abstract class JDBC3PreparedStatement extends CorePreparedStatement {
/*     */   protected JDBC3PreparedStatement(SQLiteConnection conn, String sql) throws SQLException {
/*  31 */     super(conn, sql);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearParameters() throws SQLException {
/*  36 */     checkOpen();
/*  37 */     this.pointer.safeRunConsume(DB::clear_bindings);
/*  38 */     if (this.batch != null) for (int i = this.batchPos; i < this.batchPos + this.paramCount; ) { this.batch[i] = null; i++; }
/*     */        
/*     */   }
/*     */   
/*     */   public boolean execute() throws SQLException {
/*  43 */     checkOpen();
/*  44 */     this.rs.close();
/*  45 */     this.pointer.safeRunConsume(DB::reset);
/*     */     
/*  47 */     if (this.conn instanceof JDBC3Connection) {
/*  48 */       ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
/*     */     }
/*     */     
/*  51 */     return ((Boolean)withConnectionTimeout(() -> { boolean success = false; try { this.resultsWaiting = this.conn.getDatabase().execute((CoreStatement)this, this.batch); success = true; return Boolean.valueOf((0 != this.columnCount)); } finally { if (!success && !this.pointer.isClosed()) this.pointer.safeRunConsume(DB::reset);  }  })).booleanValue();
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
/*     */   public ResultSet executeQuery() throws SQLException {
/*  67 */     checkOpen();
/*     */     
/*  69 */     if (this.columnCount == 0) {
/*  70 */       throw new SQLException("Query does not return results");
/*     */     }
/*     */     
/*  73 */     this.rs.close();
/*  74 */     this.pointer.safeRunConsume(DB::reset);
/*     */     
/*  76 */     if (this.conn instanceof JDBC3Connection) {
/*  77 */       ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
/*     */     }
/*     */     
/*  80 */     return (ResultSet)withConnectionTimeout(() -> {
/*     */           boolean success = false;
/*     */           try {
/*     */             this.resultsWaiting = this.conn.getDatabase().execute((CoreStatement)this, this.batch);
/*     */             success = true;
/*     */           } finally {
/*     */             if (!success && !this.pointer.isClosed()) {
/*     */               this.pointer.safeRunInt(DB::reset);
/*     */             }
/*     */           } 
/*     */           return getResultSet();
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate() throws SQLException {
/*  98 */     return (int)executeLargeUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public long executeLargeUpdate() throws SQLException {
/* 103 */     checkOpen();
/*     */     
/* 105 */     if (this.columnCount != 0) {
/* 106 */       throw new SQLException("Query returns results");
/*     */     }
/*     */     
/* 109 */     this.rs.close();
/* 110 */     this.pointer.safeRunConsume(DB::reset);
/*     */     
/* 112 */     if (this.conn instanceof JDBC3Connection) {
/* 113 */       ((JDBC3Connection)this.conn).tryEnforceTransactionMode();
/*     */     }
/*     */     
/* 116 */     return ((Long)withConnectionTimeout(() -> Long.valueOf(this.conn.getDatabase().executeUpdate((CoreStatement)this, this.batch)))).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBatch() throws SQLException {
/* 122 */     checkOpen();
/* 123 */     this.batchPos += this.paramCount;
/* 124 */     this.batchQueryCount++;
/* 125 */     if (this.batch == null) {
/* 126 */       this.batch = new Object[this.paramCount];
/*     */     }
/* 128 */     if (this.batchPos + this.paramCount > this.batch.length) {
/* 129 */       Object[] nb = new Object[this.batch.length * 2];
/* 130 */       System.arraycopy(this.batch, 0, nb, 0, this.batch.length);
/* 131 */       this.batch = nb;
/*     */     } 
/* 133 */     System.arraycopy(this.batch, this.batchPos - this.paramCount, this.batch, this.batchPos, this.paramCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParameterMetaData getParameterMetaData() {
/* 140 */     return (ParameterMetaData)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParameterCount() throws SQLException {
/* 145 */     checkOpen();
/* 146 */     return this.paramCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParameterClassName(int param) throws SQLException {
/* 151 */     checkOpen();
/* 152 */     return "java.lang.String";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getParameterTypeName(int pos) {
/* 157 */     return "VARCHAR";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParameterType(int pos) {
/* 162 */     return 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getParameterMode(int pos) {
/* 167 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPrecision(int pos) {
/* 172 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getScale(int pos) {
/* 177 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int isNullable(int pos) {
/* 182 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSigned(int pos) {
/* 187 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Statement getStatement() {
/* 192 */     return (Statement)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBigDecimal(int pos, BigDecimal value) throws SQLException {
/* 197 */     batch(pos, (value == null) ? null : value.toString());
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
/*     */   private byte[] readBytes(InputStream istream, int length) throws SQLException {
/* 209 */     if (length < 0) {
/* 210 */       throw new SQLException("Error reading stream. Length should be non-negative");
/*     */     }
/*     */     
/* 213 */     byte[] bytes = new byte[length];
/*     */ 
/*     */     
/*     */     try {
/* 217 */       int totalBytesRead = 0;
/*     */       
/* 219 */       while (totalBytesRead < length) {
/* 220 */         int bytesRead = istream.read(bytes, totalBytesRead, length - totalBytesRead);
/* 221 */         if (bytesRead == -1) {
/* 222 */           throw new IOException("End of stream has been reached");
/*     */         }
/* 224 */         totalBytesRead += bytesRead;
/*     */       } 
/*     */       
/* 227 */       return bytes;
/* 228 */     } catch (IOException cause) {
/* 229 */       SQLException exception = new SQLException("Error reading stream");
/*     */       
/* 231 */       exception.initCause(cause);
/* 232 */       throw exception;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBinaryStream(int pos, InputStream istream, int length) throws SQLException {
/* 238 */     if (istream == null && length == 0) {
/* 239 */       setBytes(pos, (byte[])null);
/*     */     }
/*     */     
/* 242 */     setBytes(pos, readBytes(istream, length));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAsciiStream(int pos, InputStream istream, int length) throws SQLException {
/* 247 */     setUnicodeStream(pos, istream, length);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUnicodeStream(int pos, InputStream istream, int length) throws SQLException {
/* 252 */     if (istream == null && length == 0) {
/* 253 */       setString(pos, (String)null);
/*     */     }
/*     */     
/*     */     try {
/* 257 */       setString(pos, new String(readBytes(istream, length), "UTF-8"));
/* 258 */     } catch (UnsupportedEncodingException e) {
/* 259 */       SQLException exception = new SQLException("UTF-8 is not supported");
/*     */       
/* 261 */       exception.initCause(e);
/* 262 */       throw exception;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBoolean(int pos, boolean value) throws SQLException {
/* 268 */     setInt(pos, value ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setByte(int pos, byte value) throws SQLException {
/* 273 */     setInt(pos, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBytes(int pos, byte[] value) throws SQLException {
/* 278 */     batch(pos, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDouble(int pos, double value) throws SQLException {
/* 283 */     batch(pos, new Double(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFloat(int pos, float value) throws SQLException {
/* 288 */     batch(pos, new Float(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInt(int pos, int value) throws SQLException {
/* 293 */     batch(pos, new Integer(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLong(int pos, long value) throws SQLException {
/* 298 */     batch(pos, new Long(value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNull(int pos, int u1) throws SQLException {
/* 303 */     setNull(pos, u1, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNull(int pos, int u1, String u2) throws SQLException {
/* 308 */     batch(pos, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setObject(int pos, Object value) throws SQLException {
/* 313 */     if (value == null) {
/* 314 */       batch(pos, null);
/* 315 */     } else if (value instanceof Date) {
/* 316 */       setDateByMilliseconds(pos, Long.valueOf(((Date)value).getTime()), Calendar.getInstance());
/* 317 */     } else if (value instanceof Long) {
/* 318 */       batch(pos, value);
/* 319 */     } else if (value instanceof Integer) {
/* 320 */       batch(pos, value);
/* 321 */     } else if (value instanceof Short) {
/* 322 */       batch(pos, new Integer(((Short)value).intValue()));
/* 323 */     } else if (value instanceof Float) {
/* 324 */       batch(pos, value);
/* 325 */     } else if (value instanceof Double) {
/* 326 */       batch(pos, value);
/* 327 */     } else if (value instanceof Boolean) {
/* 328 */       setBoolean(pos, ((Boolean)value).booleanValue());
/* 329 */     } else if (value instanceof byte[]) {
/* 330 */       batch(pos, value);
/* 331 */     } else if (value instanceof BigDecimal) {
/* 332 */       setBigDecimal(pos, (BigDecimal)value);
/*     */     } else {
/* 334 */       batch(pos, value.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setObject(int p, Object v, int t) throws SQLException {
/* 340 */     setObject(p, v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setObject(int p, Object v, int t, int s) throws SQLException {
/* 345 */     setObject(p, v);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setShort(int pos, short value) throws SQLException {
/* 350 */     setInt(pos, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setString(int pos, String value) throws SQLException {
/* 355 */     batch(pos, value);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCharacterStream(int pos, Reader reader, int length) throws SQLException {
/*     */     try {
/* 362 */       StringBuffer sb = new StringBuffer();
/* 363 */       char[] cbuf = new char[8192];
/*     */       
/*     */       int cnt;
/* 366 */       while ((cnt = reader.read(cbuf)) > 0) {
/* 367 */         sb.append(cbuf, 0, cnt);
/*     */       }
/*     */ 
/*     */       
/* 371 */       setString(pos, sb.toString());
/* 372 */     } catch (IOException e) {
/* 373 */       throw new SQLException("Cannot read from character stream, exception message: " + e
/* 374 */           .getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDate(int pos, Date x) throws SQLException {
/* 380 */     setDate(pos, x, Calendar.getInstance());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDate(int pos, Date x, Calendar cal) throws SQLException {
/* 385 */     if (x == null) {
/* 386 */       setObject(pos, (Object)null);
/*     */     } else {
/* 388 */       setDateByMilliseconds(pos, Long.valueOf(x.getTime()), cal);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTime(int pos, Time x) throws SQLException {
/* 394 */     setTime(pos, x, Calendar.getInstance());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTime(int pos, Time x, Calendar cal) throws SQLException {
/* 399 */     if (x == null) {
/* 400 */       setObject(pos, (Object)null);
/*     */     } else {
/* 402 */       setDateByMilliseconds(pos, Long.valueOf(x.getTime()), cal);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTimestamp(int pos, Timestamp x) throws SQLException {
/* 408 */     setTimestamp(pos, x, Calendar.getInstance());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTimestamp(int pos, Timestamp x, Calendar cal) throws SQLException {
/* 413 */     if (x == null) {
/* 414 */       setObject(pos, (Object)null);
/*     */     } else {
/* 416 */       setDateByMilliseconds(pos, Long.valueOf(x.getTime()), cal);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ResultSetMetaData getMetaData() throws SQLException {
/* 422 */     checkOpen();
/* 423 */     return (ResultSetMetaData)this.rs;
/*     */   }
/*     */   
/*     */   protected SQLException unsupported() {
/* 427 */     return new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
/*     */   }
/*     */   
/*     */   protected SQLException invalid() {
/* 431 */     return new SQLException("method cannot be called on a PreparedStatement");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArray(int i, Array x) throws SQLException {
/* 437 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void setBlob(int i, Blob x) throws SQLException {
/* 441 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void setClob(int i, Clob x) throws SQLException {
/* 445 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void setRef(int i, Ref x) throws SQLException {
/* 449 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void setURL(int pos, URL x) throws SQLException {
/* 453 */     throw unsupported();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean execute(String sql) throws SQLException {
/* 459 */     throw invalid();
/*     */   }
/*     */   
/*     */   public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
/* 463 */     throw invalid();
/*     */   }
/*     */   
/*     */   public boolean execute(String sql, int[] colinds) throws SQLException {
/* 467 */     throw invalid();
/*     */   }
/*     */   
/*     */   public boolean execute(String sql, String[] colnames) throws SQLException {
/* 471 */     throw invalid();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int executeUpdate(String sql) throws SQLException {
/* 477 */     throw invalid();
/*     */   }
/*     */   
/*     */   public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
/* 481 */     throw invalid();
/*     */   }
/*     */   
/*     */   public int executeUpdate(String sql, int[] colinds) throws SQLException {
/* 485 */     throw invalid();
/*     */   }
/*     */   
/*     */   public int executeUpdate(String sql, String[] cols) throws SQLException {
/* 489 */     throw invalid();
/*     */   }
/*     */   
/*     */   public long executeLargeUpdate(String sql) throws SQLException {
/* 493 */     throw invalid();
/*     */   }
/*     */   
/*     */   public long executeLargeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
/* 497 */     throw invalid();
/*     */   }
/*     */   
/*     */   public long executeLargeUpdate(String sql, int[] colinds) throws SQLException {
/* 501 */     throw invalid();
/*     */   }
/*     */   
/*     */   public long executeLargeUpdate(String sql, String[] cols) throws SQLException {
/* 505 */     throw invalid();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSet executeQuery(String sql) throws SQLException {
/* 511 */     throw invalid();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addBatch(String sql) throws SQLException {
/* 517 */     throw invalid();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\jdbc3\JDBC3PreparedStatement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */