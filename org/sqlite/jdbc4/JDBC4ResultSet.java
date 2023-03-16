/*     */ package org.sqlite.jdbc4;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.io.Writer;
/*     */ import java.math.BigDecimal;
/*     */ import java.net.URL;
/*     */ import java.sql.Array;
/*     */ import java.sql.Blob;
/*     */ import java.sql.Clob;
/*     */ import java.sql.Date;
/*     */ import java.sql.NClob;
/*     */ import java.sql.Ref;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.RowId;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.sql.SQLXML;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Map;
/*     */ import org.sqlite.core.CoreStatement;
/*     */ import org.sqlite.jdbc3.JDBC3ResultSet;
/*     */ 
/*     */ public class JDBC4ResultSet
/*     */   extends JDBC3ResultSet implements ResultSet, ResultSetMetaData {
/*     */   public JDBC4ResultSet(CoreStatement stmt) {
/*  33 */     super(stmt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {
/*  38 */     boolean wasOpen = isOpen();
/*  39 */     super.close();
/*     */     
/*  41 */     if (wasOpen && this.stmt instanceof JDBC4Statement) {
/*  42 */       JDBC4Statement stat = (JDBC4Statement)this.stmt;
/*     */       
/*  44 */       if (stat.closeOnCompletion && !stat.isClosed()) {
/*  45 */         stat.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws ClassCastException {
/*  52 */     return iface.cast(this);
/*     */   }
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) {
/*  56 */     return iface.isInstance(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public RowId getRowId(int columnIndex) throws SQLException {
/*  61 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public RowId getRowId(String columnLabel) throws SQLException {
/*  66 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateRowId(int columnIndex, RowId x) throws SQLException {
/*  71 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateRowId(String columnLabel, RowId x) throws SQLException {
/*  76 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHoldability() throws SQLException {
/*  81 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean isClosed() throws SQLException {
/*  85 */     return !isOpen();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNString(int columnIndex, String nString) throws SQLException {
/*  90 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNString(String columnLabel, String nString) throws SQLException {
/*  95 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNClob(int columnIndex, NClob nClob) throws SQLException {
/* 100 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNClob(String columnLabel, NClob nClob) throws SQLException {
/* 105 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NClob getNClob(int columnIndex) throws SQLException {
/* 110 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public NClob getNClob(String columnLabel) throws SQLException {
/* 115 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLXML getSQLXML(int columnIndex) throws SQLException {
/* 120 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLXML getSQLXML(String columnLabel) throws SQLException {
/* 125 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
/* 130 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
/* 135 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNString(int columnIndex) throws SQLException {
/* 140 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getNString(String columnLabel) throws SQLException {
/* 145 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */   
/*     */   public Reader getNCharacterStream(int col) throws SQLException {
/* 149 */     String data = getString(col);
/* 150 */     return getNCharacterStreamInternal(data);
/*     */   }
/*     */   
/*     */   private Reader getNCharacterStreamInternal(String data) {
/* 154 */     if (data == null) {
/* 155 */       return null;
/*     */     }
/* 157 */     Reader reader = new StringReader(data);
/* 158 */     return reader;
/*     */   }
/*     */   
/*     */   public Reader getNCharacterStream(String col) throws SQLException {
/* 162 */     String data = getString(col);
/* 163 */     return getNCharacterStreamInternal(data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
/* 168 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
/* 174 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
/* 179 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
/* 185 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
/* 190 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
/* 196 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
/* 202 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
/* 208 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
/* 214 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
/* 220 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
/* 225 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
/* 230 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
/* 235 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
/* 240 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
/* 245 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
/* 250 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
/* 255 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
/* 260 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
/* 265 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
/* 270 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
/* 275 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
/* 280 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
/* 285 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
/* 290 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateClob(int columnIndex, Reader reader) throws SQLException {
/* 295 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateClob(String columnLabel, Reader reader) throws SQLException {
/* 300 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNClob(int columnIndex, Reader reader) throws SQLException {
/* 305 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateNClob(String columnLabel, Reader reader) throws SQLException {
/* 310 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
/* 315 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
/* 320 */     throw new SQLFeatureNotSupportedException();
/*     */   }
/*     */   
/*     */   protected SQLException unsupported() {
/* 324 */     return new SQLFeatureNotSupportedException("not implemented by SQLite JDBC driver");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Array getArray(int i) throws SQLException {
/* 330 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public Array getArray(String col) throws SQLException {
/* 334 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public InputStream getAsciiStream(int col) throws SQLException {
/* 338 */     String data = getString(col);
/* 339 */     return getAsciiStreamInternal(data);
/*     */   }
/*     */   
/*     */   public InputStream getAsciiStream(String col) throws SQLException {
/* 343 */     String data = getString(col);
/* 344 */     return getAsciiStreamInternal(data);
/*     */   }
/*     */   private InputStream getAsciiStreamInternal(String data) {
/*     */     InputStream inputStream;
/* 348 */     if (data == null) {
/* 349 */       return null;
/*     */     }
/*     */     
/*     */     try {
/* 353 */       inputStream = new ByteArrayInputStream(data.getBytes("ASCII"));
/* 354 */     } catch (UnsupportedEncodingException e) {
/* 355 */       return null;
/*     */     } 
/* 357 */     return inputStream;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public BigDecimal getBigDecimal(int col, int s) throws SQLException {
/* 362 */     throw unsupported();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public BigDecimal getBigDecimal(String col, int s) throws SQLException {
/* 367 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public Blob getBlob(int col) throws SQLException {
/* 371 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public Blob getBlob(String col) throws SQLException {
/* 375 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public Clob getClob(int col) throws SQLException {
/* 379 */     String clob = getString(col);
/* 380 */     return (clob == null) ? null : new SqliteClob(clob);
/*     */   }
/*     */   
/*     */   public Clob getClob(String col) throws SQLException {
/* 384 */     String clob = getString(col);
/* 385 */     return (clob == null) ? null : new SqliteClob(clob);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getObject(int col, Map map) throws SQLException {
/* 390 */     throw unsupported();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getObject(String col, Map map) throws SQLException {
/* 395 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public Ref getRef(int i) throws SQLException {
/* 399 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public Ref getRef(String col) throws SQLException {
/* 403 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public InputStream getUnicodeStream(int col) throws SQLException {
/* 407 */     return getAsciiStream(col);
/*     */   }
/*     */   
/*     */   public InputStream getUnicodeStream(String col) throws SQLException {
/* 411 */     return getAsciiStream(col);
/*     */   }
/*     */   
/*     */   public URL getURL(int col) throws SQLException {
/* 415 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public URL getURL(String col) throws SQLException {
/* 419 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void insertRow() throws SQLException {
/* 423 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   }
/*     */   
/*     */   public void moveToCurrentRow() throws SQLException {
/* 427 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   }
/*     */   
/*     */   public void moveToInsertRow() throws SQLException {
/* 431 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   }
/*     */   
/*     */   public boolean last() throws SQLException {
/* 435 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   }
/*     */   
/*     */   public boolean previous() throws SQLException {
/* 439 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   }
/*     */   
/*     */   public boolean relative(int rows) throws SQLException {
/* 443 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   }
/*     */   
/*     */   public boolean absolute(int row) throws SQLException {
/* 447 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   }
/*     */   
/*     */   public void afterLast() throws SQLException {
/* 451 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   }
/*     */   
/*     */   public void beforeFirst() throws SQLException {
/* 455 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   }
/*     */   
/*     */   public boolean first() throws SQLException {
/* 459 */     throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
/*     */   }
/*     */   
/*     */   public void cancelRowUpdates() throws SQLException {
/* 463 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void deleteRow() throws SQLException {
/* 467 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateArray(int col, Array x) throws SQLException {
/* 471 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateArray(String col, Array x) throws SQLException {
/* 475 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateAsciiStream(int col, InputStream x, int l) throws SQLException {
/* 479 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateAsciiStream(String col, InputStream x, int l) throws SQLException {
/* 483 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateBigDecimal(int col, BigDecimal x) throws SQLException {
/* 487 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateBigDecimal(String col, BigDecimal x) throws SQLException {
/* 491 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateBinaryStream(int c, InputStream x, int l) throws SQLException {
/* 495 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateBinaryStream(String c, InputStream x, int l) throws SQLException {
/* 499 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateBlob(int col, Blob x) throws SQLException {
/* 503 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateBlob(String col, Blob x) throws SQLException {
/* 507 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateBoolean(int col, boolean x) throws SQLException {
/* 511 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateBoolean(String col, boolean x) throws SQLException {
/* 515 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateByte(int col, byte x) throws SQLException {
/* 519 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateByte(String col, byte x) throws SQLException {
/* 523 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateBytes(int col, byte[] x) throws SQLException {
/* 527 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateBytes(String col, byte[] x) throws SQLException {
/* 531 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateCharacterStream(int c, Reader x, int l) throws SQLException {
/* 535 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateCharacterStream(String c, Reader r, int l) throws SQLException {
/* 539 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateClob(int col, Clob x) throws SQLException {
/* 543 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateClob(String col, Clob x) throws SQLException {
/* 547 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateDate(int col, Date x) throws SQLException {
/* 551 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateDate(String col, Date x) throws SQLException {
/* 555 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateDouble(int col, double x) throws SQLException {
/* 559 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateDouble(String col, double x) throws SQLException {
/* 563 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateFloat(int col, float x) throws SQLException {
/* 567 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateFloat(String col, float x) throws SQLException {
/* 571 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateInt(int col, int x) throws SQLException {
/* 575 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateInt(String col, int x) throws SQLException {
/* 579 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateLong(int col, long x) throws SQLException {
/* 583 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateLong(String col, long x) throws SQLException {
/* 587 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateNull(int col) throws SQLException {
/* 591 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateNull(String col) throws SQLException {
/* 595 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateObject(int c, Object x) throws SQLException {
/* 599 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateObject(int c, Object x, int s) throws SQLException {
/* 603 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateObject(String col, Object x) throws SQLException {
/* 607 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateObject(String c, Object x, int s) throws SQLException {
/* 611 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateRef(int col, Ref x) throws SQLException {
/* 615 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateRef(String c, Ref x) throws SQLException {
/* 619 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateRow() throws SQLException {
/* 623 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateShort(int c, short x) throws SQLException {
/* 627 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateShort(String c, short x) throws SQLException {
/* 631 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateString(int c, String x) throws SQLException {
/* 635 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateString(String c, String x) throws SQLException {
/* 639 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateTime(int c, Time x) throws SQLException {
/* 643 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateTime(String c, Time x) throws SQLException {
/* 647 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateTimestamp(int c, Timestamp x) throws SQLException {
/* 651 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void updateTimestamp(String c, Timestamp x) throws SQLException {
/* 655 */     throw unsupported();
/*     */   }
/*     */   
/*     */   public void refreshRow() throws SQLException {
/* 659 */     throw unsupported();
/*     */   }
/*     */   
/*     */   class SqliteClob
/*     */     implements NClob {
/*     */     private String data;
/*     */     
/*     */     protected SqliteClob(String data) {
/* 667 */       this.data = data;
/*     */     }
/*     */     
/*     */     public void free() throws SQLException {
/* 671 */       this.data = null;
/*     */     }
/*     */     
/*     */     public InputStream getAsciiStream() throws SQLException {
/* 675 */       return JDBC4ResultSet.this.getAsciiStreamInternal(this.data);
/*     */     }
/*     */     
/*     */     public Reader getCharacterStream() throws SQLException {
/* 679 */       return JDBC4ResultSet.this.getNCharacterStreamInternal(this.data);
/*     */     }
/*     */     
/*     */     public Reader getCharacterStream(long arg0, long arg1) throws SQLException {
/* 683 */       return JDBC4ResultSet.this.getNCharacterStreamInternal(this.data);
/*     */     }
/*     */     
/*     */     public String getSubString(long position, int length) throws SQLException {
/* 687 */       if (this.data == null) {
/* 688 */         throw new SQLException("no data");
/*     */       }
/* 690 */       if (position < 1L) {
/* 691 */         throw new SQLException("Position must be greater than or equal to 1");
/*     */       }
/* 693 */       if (length < 0) {
/* 694 */         throw new SQLException("Length must be greater than or equal to 0");
/*     */       }
/* 696 */       int start = (int)position - 1;
/* 697 */       return this.data.substring(start, Math.min(start + length, this.data.length()));
/*     */     }
/*     */     
/*     */     public long length() throws SQLException {
/* 701 */       if (this.data == null) {
/* 702 */         throw new SQLException("no data");
/*     */       }
/* 704 */       return this.data.length();
/*     */     }
/*     */     
/*     */     public long position(String arg0, long arg1) throws SQLException {
/* 708 */       JDBC4ResultSet.this.unsupported();
/* 709 */       return -1L;
/*     */     }
/*     */     
/*     */     public long position(Clob arg0, long arg1) throws SQLException {
/* 713 */       JDBC4ResultSet.this.unsupported();
/* 714 */       return -1L;
/*     */     }
/*     */     
/*     */     public OutputStream setAsciiStream(long arg0) throws SQLException {
/* 718 */       JDBC4ResultSet.this.unsupported();
/* 719 */       return null;
/*     */     }
/*     */     
/*     */     public Writer setCharacterStream(long arg0) throws SQLException {
/* 723 */       JDBC4ResultSet.this.unsupported();
/* 724 */       return null;
/*     */     }
/*     */     
/*     */     public int setString(long arg0, String arg1) throws SQLException {
/* 728 */       JDBC4ResultSet.this.unsupported();
/* 729 */       return -1;
/*     */     }
/*     */     
/*     */     public int setString(long arg0, String arg1, int arg2, int arg3) throws SQLException {
/* 733 */       JDBC4ResultSet.this.unsupported();
/* 734 */       return -1;
/*     */     }
/*     */     
/*     */     public void truncate(long arg0) throws SQLException {
/* 738 */       JDBC4ResultSet.this.unsupported();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\jdbc4\JDBC4ResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */