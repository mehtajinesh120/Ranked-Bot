/*     */ package org.sqlite.jdbc3;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.Date;
/*     */ import java.sql.ResultSetMetaData;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.Statement;
/*     */ import java.sql.Time;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Calendar;
/*     */ import java.util.Locale;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.sqlite.core.CoreResultSet;
/*     */ import org.sqlite.core.CoreStatement;
/*     */ import org.sqlite.core.DB;
/*     */ import org.sqlite.date.FastDateFormat;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class JDBC3ResultSet
/*     */   extends CoreResultSet
/*     */ {
/*     */   protected JDBC3ResultSet(CoreStatement stmt) {
/*  31 */     super(stmt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int findColumn(String col) throws SQLException {
/*  40 */     checkOpen();
/*  41 */     Integer index = findColumnIndexInCache(col);
/*  42 */     if (index != null) {
/*  43 */       return index.intValue();
/*     */     }
/*  45 */     for (int i = 0; i < this.cols.length; i++) {
/*  46 */       if (col.equalsIgnoreCase(this.cols[i])) {
/*  47 */         return addColumnIndexInCache(col, i + 1);
/*     */       }
/*     */     } 
/*  50 */     throw new SQLException("no such column: '" + col + "'");
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean next() throws SQLException {
/*  55 */     if (!this.open || this.emptyResultSet) {
/*  56 */       return false;
/*     */     }
/*  58 */     this.lastCol = -1;
/*     */ 
/*     */     
/*  61 */     if (this.row == 0) {
/*  62 */       this.row++;
/*  63 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  67 */     if (this.maxRows != 0L && this.row == this.maxRows) {
/*  68 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  72 */     int statusCode = this.stmt.pointer.safeRunInt(DB::step);
/*  73 */     switch (statusCode) {
/*     */       case 101:
/*  75 */         close();
/*  76 */         return false;
/*     */       case 100:
/*  78 */         this.row++;
/*  79 */         return true;
/*     */     } 
/*     */     
/*  82 */     getDatabase().throwex(statusCode);
/*  83 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getType() {
/*  89 */     return 1003;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFetchSize() {
/*  94 */     return this.limitRows;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFetchSize(int rows) throws SQLException {
/*  99 */     if (0 > rows || (this.maxRows != 0L && rows > this.maxRows)) {
/* 100 */       throw new SQLException("fetch size " + rows + " out of bounds " + this.maxRows);
/*     */     }
/* 102 */     this.limitRows = rows;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFetchDirection() throws SQLException {
/* 107 */     checkOpen();
/* 108 */     return 1000;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFetchDirection(int d) throws SQLException {
/* 113 */     checkOpen();
/*     */     
/* 115 */     if (d != 1000)
/*     */     {
/*     */       
/* 118 */       throw new SQLException("only FETCH_FORWARD direction supported");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAfterLast() {
/* 124 */     return (!this.open && !this.emptyResultSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBeforeFirst() {
/* 129 */     return (!this.emptyResultSet && this.open && this.row == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFirst() {
/* 134 */     return (this.row == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLast() throws SQLException {
/* 139 */     throw new SQLException("function not yet implemented for SQLite");
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRow() {
/* 144 */     return this.row;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasNull() throws SQLException {
/* 149 */     return (safeGetColumnType(markCol(this.lastCol)) == 5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal getBigDecimal(int col) throws SQLException {
/* 156 */     int columnType = getColumnType(col);
/*     */     
/* 158 */     if (columnType == 4) {
/* 159 */       long decimal = getLong(col);
/* 160 */       return BigDecimal.valueOf(decimal);
/* 161 */     }  if (columnType == 6 || columnType == 8) {
/* 162 */       double decimal = getDouble(col);
/* 163 */       if (Double.isNaN(decimal)) {
/* 164 */         throw new SQLException("Bad value for type BigDecimal : Not a Number");
/*     */       }
/* 166 */       return BigDecimal.valueOf(decimal);
/*     */     } 
/*     */     
/* 169 */     String stringValue = getString(col);
/* 170 */     if (stringValue == null) {
/* 171 */       return null;
/*     */     }
/*     */     try {
/* 174 */       return new BigDecimal(stringValue);
/* 175 */     } catch (NumberFormatException e) {
/* 176 */       throw new SQLException("Bad value for type BigDecimal : " + stringValue);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal getBigDecimal(String col) throws SQLException {
/* 184 */     return getBigDecimal(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(int col) throws SQLException {
/* 189 */     return (getInt(col) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBoolean(String col) throws SQLException {
/* 194 */     return getBoolean(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public InputStream getBinaryStream(int col) throws SQLException {
/* 199 */     byte[] bytes = getBytes(col);
/* 200 */     if (bytes != null) {
/* 201 */       return new ByteArrayInputStream(bytes);
/*     */     }
/* 203 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public InputStream getBinaryStream(String col) throws SQLException {
/* 209 */     return getBinaryStream(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(int col) throws SQLException {
/* 214 */     return (byte)getInt(col);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getByte(String col) throws SQLException {
/* 219 */     return getByte(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getBytes(int col) throws SQLException {
/* 224 */     return (byte[])this.stmt.pointer.safeRun((db, ptr) -> db.column_blob(ptr, markCol(col)));
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getBytes(String col) throws SQLException {
/* 229 */     return getBytes(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public Reader getCharacterStream(int col) throws SQLException {
/* 234 */     String string = getString(col);
/* 235 */     return (string == null) ? null : new StringReader(string);
/*     */   }
/*     */ 
/*     */   
/*     */   public Reader getCharacterStream(String col) throws SQLException {
/* 240 */     return getCharacterStream(findColumn(col));
/*     */   }
/*     */   
/*     */   public Date getDate(int col) throws SQLException {
/*     */     String dateText;
/* 245 */     switch (safeGetColumnType(markCol(col))) {
/*     */       case 5:
/* 247 */         return null;
/*     */       
/*     */       case 3:
/* 250 */         dateText = safeGetColumnText(col);
/* 251 */         if ("".equals(dateText)) {
/* 252 */           return null;
/*     */         }
/*     */         try {
/* 255 */           return new Date(
/* 256 */               getConnectionConfig().getDateFormat().parse(dateText).getTime());
/* 257 */         } catch (Exception e) {
/* 258 */           throw new SQLException("Error parsing date", e);
/*     */         } 
/*     */       
/*     */       case 2:
/* 262 */         return new Date(julianDateToCalendar(Double.valueOf(safeGetDoubleCol(col))).getTimeInMillis());
/*     */     } 
/*     */     
/* 265 */     return new Date(safeGetLongCol(col) * getConnectionConfig().getDateMultiplier());
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getDate(int col, Calendar cal) throws SQLException {
/*     */     String dateText;
/* 271 */     requireCalendarNotNull(cal);
/* 272 */     switch (safeGetColumnType(markCol(col))) {
/*     */       case 5:
/* 274 */         return null;
/*     */       
/*     */       case 3:
/* 277 */         dateText = safeGetColumnText(col);
/* 278 */         if ("".equals(dateText)) {
/* 279 */           return null;
/*     */         }
/*     */         
/*     */         try {
/* 283 */           FastDateFormat dateFormat = FastDateFormat.getInstance(
/* 284 */               getConnectionConfig().getDateStringFormat(), cal.getTimeZone());
/*     */           
/* 286 */           return new Date(dateFormat.parse(dateText).getTime());
/* 287 */         } catch (Exception e) {
/* 288 */           throw new SQLException("Error parsing time stamp", e);
/*     */         } 
/*     */       
/*     */       case 2:
/* 292 */         return new Date(julianDateToCalendar(Double.valueOf(safeGetDoubleCol(col)), cal).getTimeInMillis());
/*     */     } 
/*     */     
/* 295 */     cal.setTimeInMillis(
/* 296 */         safeGetLongCol(col) * getConnectionConfig().getDateMultiplier());
/* 297 */     return new Date(cal.getTime().getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Date getDate(String col) throws SQLException {
/* 303 */     return getDate(findColumn(col), Calendar.getInstance());
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getDate(String col, Calendar cal) throws SQLException {
/* 308 */     return getDate(findColumn(col), cal);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDouble(int col) throws SQLException {
/* 313 */     if (safeGetColumnType(markCol(col)) == 5) {
/* 314 */       return 0.0D;
/*     */     }
/* 316 */     return safeGetDoubleCol(col);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDouble(String col) throws SQLException {
/* 321 */     return getDouble(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloat(int col) throws SQLException {
/* 326 */     if (safeGetColumnType(markCol(col)) == 5) {
/* 327 */       return 0.0F;
/*     */     }
/* 329 */     return (float)safeGetDoubleCol(col);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFloat(String col) throws SQLException {
/* 334 */     return getFloat(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(int col) throws SQLException {
/* 339 */     return this.stmt.pointer.safeRunInt((db, ptr) -> db.column_int(ptr, markCol(col)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInt(String col) throws SQLException {
/* 344 */     return getInt(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(int col) throws SQLException {
/* 349 */     return safeGetLongCol(col);
/*     */   }
/*     */ 
/*     */   
/*     */   public long getLong(String col) throws SQLException {
/* 354 */     return getLong(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShort(int col) throws SQLException {
/* 359 */     return (short)getInt(col);
/*     */   }
/*     */ 
/*     */   
/*     */   public short getShort(String col) throws SQLException {
/* 364 */     return getShort(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(int col) throws SQLException {
/* 369 */     return safeGetColumnText(col);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(String col) throws SQLException {
/* 374 */     return getString(findColumn(col));
/*     */   }
/*     */   
/*     */   public Time getTime(int col) throws SQLException {
/*     */     String dateText;
/* 379 */     switch (safeGetColumnType(markCol(col))) {
/*     */       case 5:
/* 381 */         return null;
/*     */       
/*     */       case 3:
/* 384 */         dateText = safeGetColumnText(col);
/* 385 */         if ("".equals(dateText)) {
/* 386 */           return null;
/*     */         }
/*     */         try {
/* 389 */           return new Time(
/* 390 */               getConnectionConfig().getDateFormat().parse(dateText).getTime());
/* 391 */         } catch (Exception e) {
/* 392 */           throw new SQLException("Error parsing time", e);
/*     */         } 
/*     */       
/*     */       case 2:
/* 396 */         return new Time(julianDateToCalendar(Double.valueOf(safeGetDoubleCol(col))).getTimeInMillis());
/*     */     } 
/*     */     
/* 399 */     return new Time(safeGetLongCol(col) * getConnectionConfig().getDateMultiplier());
/*     */   }
/*     */ 
/*     */   
/*     */   public Time getTime(int col, Calendar cal) throws SQLException {
/*     */     String dateText;
/* 405 */     requireCalendarNotNull(cal);
/* 406 */     switch (safeGetColumnType(markCol(col))) {
/*     */       case 5:
/* 408 */         return null;
/*     */       
/*     */       case 3:
/* 411 */         dateText = safeGetColumnText(col);
/* 412 */         if ("".equals(dateText)) {
/* 413 */           return null;
/*     */         }
/*     */         
/*     */         try {
/* 417 */           FastDateFormat dateFormat = FastDateFormat.getInstance(
/* 418 */               getConnectionConfig().getDateStringFormat(), cal.getTimeZone());
/*     */           
/* 420 */           return new Time(dateFormat.parse(dateText).getTime());
/* 421 */         } catch (Exception e) {
/* 422 */           throw new SQLException("Error parsing time", e);
/*     */         } 
/*     */       
/*     */       case 2:
/* 426 */         return new Time(julianDateToCalendar(Double.valueOf(safeGetDoubleCol(col)), cal).getTimeInMillis());
/*     */     } 
/*     */     
/* 429 */     cal.setTimeInMillis(
/* 430 */         safeGetLongCol(col) * getConnectionConfig().getDateMultiplier());
/* 431 */     return new Time(cal.getTime().getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Time getTime(String col) throws SQLException {
/* 437 */     return getTime(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public Time getTime(String col, Calendar cal) throws SQLException {
/* 442 */     return getTime(findColumn(col), cal);
/*     */   }
/*     */   
/*     */   public Timestamp getTimestamp(int col) throws SQLException {
/*     */     String dateText;
/* 447 */     switch (safeGetColumnType(markCol(col))) {
/*     */       case 5:
/* 449 */         return null;
/*     */       
/*     */       case 3:
/* 452 */         dateText = safeGetColumnText(col);
/* 453 */         if ("".equals(dateText)) {
/* 454 */           return null;
/*     */         }
/*     */         try {
/* 457 */           return new Timestamp(
/* 458 */               getConnectionConfig().getDateFormat().parse(dateText).getTime());
/* 459 */         } catch (Exception e) {
/* 460 */           throw new SQLException("Error parsing time stamp", e);
/*     */         } 
/*     */       
/*     */       case 2:
/* 464 */         return new Timestamp(julianDateToCalendar(Double.valueOf(safeGetDoubleCol(col))).getTimeInMillis());
/*     */     } 
/*     */     
/* 467 */     return new Timestamp(
/* 468 */         safeGetLongCol(col) * getConnectionConfig().getDateMultiplier());
/*     */   }
/*     */ 
/*     */   
/*     */   public Timestamp getTimestamp(int col, Calendar cal) throws SQLException {
/*     */     String dateText;
/* 474 */     requireCalendarNotNull(cal);
/* 475 */     switch (safeGetColumnType(markCol(col))) {
/*     */       case 5:
/* 477 */         return null;
/*     */       
/*     */       case 3:
/* 480 */         dateText = safeGetColumnText(col);
/* 481 */         if ("".equals(dateText)) {
/* 482 */           return null;
/*     */         }
/*     */         
/*     */         try {
/* 486 */           FastDateFormat dateFormat = FastDateFormat.getInstance(
/* 487 */               getConnectionConfig().getDateStringFormat(), cal.getTimeZone());
/*     */           
/* 489 */           return new Timestamp(dateFormat.parse(dateText).getTime());
/* 490 */         } catch (Exception e) {
/* 491 */           throw new SQLException("Error parsing time stamp", e);
/*     */         } 
/*     */       
/*     */       case 2:
/* 495 */         return new Timestamp(julianDateToCalendar(Double.valueOf(safeGetDoubleCol(col))).getTimeInMillis());
/*     */     } 
/*     */     
/* 498 */     cal.setTimeInMillis(
/* 499 */         safeGetLongCol(col) * getConnectionConfig().getDateMultiplier());
/*     */     
/* 501 */     return new Timestamp(cal.getTime().getTime());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Timestamp getTimestamp(String col) throws SQLException {
/* 507 */     return getTimestamp(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public Timestamp getTimestamp(String c, Calendar ca) throws SQLException {
/* 512 */     return getTimestamp(findColumn(c), ca);
/*     */   }
/*     */   
/*     */   public Object getObject(int col) throws SQLException {
/*     */     long val;
/* 517 */     switch (safeGetColumnType(markCol(col))) {
/*     */       case 1:
/* 519 */         val = getLong(col);
/* 520 */         if (val > 2147483647L || val < -2147483648L) {
/* 521 */           return new Long(val);
/*     */         }
/* 523 */         return new Integer((int)val);
/*     */       
/*     */       case 2:
/* 526 */         return new Double(getDouble(col));
/*     */       case 4:
/* 528 */         return getBytes(col);
/*     */       case 5:
/* 530 */         return null;
/*     */     } 
/*     */     
/* 533 */     return getString(col);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObject(String col) throws SQLException {
/* 539 */     return getObject(findColumn(col));
/*     */   }
/*     */ 
/*     */   
/*     */   public Statement getStatement() {
/* 544 */     return (Statement)this.stmt;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCursorName() {
/* 549 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public SQLWarning getWarnings() {
/* 554 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearWarnings() {}
/*     */ 
/*     */ 
/*     */   
/* 563 */   protected static final Pattern COLUMN_TYPENAME = Pattern.compile("([^\\(]*)");
/*     */ 
/*     */ 
/*     */   
/* 567 */   protected static final Pattern COLUMN_TYPECAST = Pattern.compile("cast\\(.*?\\s+as\\s+(.*?)\\s*\\)");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 572 */   protected static final Pattern COLUMN_PRECISION = Pattern.compile(".*?\\((.*?)\\)");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResultSetMetaData getMetaData() {
/* 579 */     return (ResultSetMetaData)this;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCatalogName(int col) throws SQLException {
/* 584 */     return safeGetColumnTableName(col);
/*     */   }
/*     */   
/*     */   public String getColumnClassName(int col) throws SQLException {
/*     */     long val;
/* 589 */     switch (safeGetColumnType(markCol(col))) {
/*     */       case 1:
/* 591 */         val = getLong(col);
/* 592 */         if (val > 2147483647L || val < -2147483648L) {
/* 593 */           return "java.lang.Long";
/*     */         }
/* 595 */         return "java.lang.Integer";
/*     */       
/*     */       case 2:
/* 598 */         return "java.lang.Double";
/*     */       case 4:
/*     */       case 5:
/* 601 */         return "java.lang.Object";
/*     */     } 
/*     */     
/* 604 */     return "java.lang.String";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getColumnCount() throws SQLException {
/* 610 */     checkCol(1);
/* 611 */     return this.colsMeta.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColumnDisplaySize(int col) {
/* 616 */     return Integer.MAX_VALUE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColumnLabel(int col) throws SQLException {
/* 621 */     return getColumnName(col);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColumnName(int col) throws SQLException {
/* 626 */     return safeGetColumnName(col);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColumnType(int col) throws SQLException {
/* 631 */     String typeName = getColumnTypeName(col);
/* 632 */     int valueType = safeGetColumnType(checkCol(col));
/*     */     
/* 634 */     if (valueType == 1 || valueType == 5) {
/* 635 */       if ("BOOLEAN".equals(typeName)) {
/* 636 */         return 16;
/*     */       }
/*     */       
/* 639 */       if ("TINYINT".equals(typeName)) {
/* 640 */         return -6;
/*     */       }
/*     */       
/* 643 */       if ("SMALLINT".equals(typeName) || "INT2".equals(typeName)) {
/* 644 */         return 5;
/*     */       }
/*     */       
/* 647 */       if ("BIGINT".equals(typeName) || "INT8"
/* 648 */         .equals(typeName) || "UNSIGNED BIG INT"
/* 649 */         .equals(typeName)) {
/* 650 */         return -5;
/*     */       }
/*     */       
/* 653 */       if ("DATE".equals(typeName) || "DATETIME".equals(typeName)) {
/* 654 */         return 91;
/*     */       }
/*     */       
/* 657 */       if ("TIMESTAMP".equals(typeName)) {
/* 658 */         return 93;
/*     */       }
/*     */       
/* 661 */       if (valueType == 1 || "INT"
/* 662 */         .equals(typeName) || "INTEGER"
/* 663 */         .equals(typeName) || "MEDIUMINT"
/* 664 */         .equals(typeName)) {
/* 665 */         long val = getLong(col);
/* 666 */         if (val > 2147483647L || val < -2147483648L) {
/* 667 */           return -5;
/*     */         }
/* 669 */         return 4;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 674 */     if (valueType == 2 || valueType == 5) {
/* 675 */       if ("DECIMAL".equals(typeName)) {
/* 676 */         return 3;
/*     */       }
/*     */       
/* 679 */       if ("DOUBLE".equals(typeName) || "DOUBLE PRECISION".equals(typeName)) {
/* 680 */         return 8;
/*     */       }
/*     */       
/* 683 */       if ("NUMERIC".equals(typeName)) {
/* 684 */         return 2;
/*     */       }
/*     */       
/* 687 */       if ("REAL".equals(typeName)) {
/* 688 */         return 7;
/*     */       }
/*     */       
/* 691 */       if (valueType == 2 || "FLOAT".equals(typeName)) {
/* 692 */         return 6;
/*     */       }
/*     */     } 
/*     */     
/* 696 */     if (valueType == 3 || valueType == 5) {
/* 697 */       if ("CHARACTER".equals(typeName) || "NCHAR"
/* 698 */         .equals(typeName) || "NATIVE CHARACTER"
/* 699 */         .equals(typeName) || "CHAR"
/* 700 */         .equals(typeName)) {
/* 701 */         return 1;
/*     */       }
/*     */       
/* 704 */       if ("CLOB".equals(typeName)) {
/* 705 */         return 2005;
/*     */       }
/*     */       
/* 708 */       if ("DATE".equals(typeName) || "DATETIME".equals(typeName)) {
/* 709 */         return 91;
/*     */       }
/*     */       
/* 712 */       if ("TIMESTAMP".equals(typeName)) {
/* 713 */         return 93;
/*     */       }
/*     */       
/* 716 */       if (valueType == 3 || "VARCHAR"
/* 717 */         .equals(typeName) || "VARYING CHARACTER"
/* 718 */         .equals(typeName) || "NVARCHAR"
/* 719 */         .equals(typeName) || "TEXT"
/* 720 */         .equals(typeName)) {
/* 721 */         return 12;
/*     */       }
/*     */     } 
/*     */     
/* 725 */     if (valueType == 4 || valueType == 5) {
/* 726 */       if ("BINARY".equals(typeName)) {
/* 727 */         return -2;
/*     */       }
/*     */       
/* 730 */       if (valueType == 4 || "BLOB".equals(typeName)) {
/* 731 */         return 2004;
/*     */       }
/*     */     } 
/*     */     
/* 735 */     return 2;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColumnTypeName(int col) throws SQLException {
/* 744 */     String declType = getColumnDeclType(col);
/*     */     
/* 746 */     if (declType != null) {
/* 747 */       Matcher matcher = COLUMN_TYPENAME.matcher(declType);
/*     */       
/* 749 */       matcher.find();
/* 750 */       return matcher.group(1).toUpperCase(Locale.ENGLISH);
/*     */     } 
/*     */     
/* 753 */     switch (safeGetColumnType(checkCol(col))) {
/*     */       case 1:
/* 755 */         return "INTEGER";
/*     */       case 2:
/* 757 */         return "FLOAT";
/*     */       case 4:
/* 759 */         return "BLOB";
/*     */       case 3:
/* 761 */         return "TEXT";
/*     */     } 
/*     */     
/* 764 */     return "NUMERIC";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPrecision(int col) throws SQLException {
/* 770 */     String declType = getColumnDeclType(col);
/*     */     
/* 772 */     if (declType != null) {
/* 773 */       Matcher matcher = COLUMN_PRECISION.matcher(declType);
/*     */       
/* 775 */       return matcher.find() ? Integer.parseInt(matcher.group(1).split(",")[0].trim()) : 0;
/*     */     } 
/*     */     
/* 778 */     return 0;
/*     */   }
/*     */   
/*     */   private String getColumnDeclType(int col) throws SQLException {
/* 782 */     String declType = (String)this.stmt.pointer.safeRun((db, ptr) -> db.column_decltype(ptr, checkCol(col)));
/*     */     
/* 784 */     if (declType == null) {
/* 785 */       Matcher matcher = COLUMN_TYPECAST.matcher(safeGetColumnName(col));
/* 786 */       declType = matcher.find() ? matcher.group(1) : null;
/*     */     } 
/*     */     
/* 789 */     return declType;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getScale(int col) throws SQLException {
/* 794 */     String declType = getColumnDeclType(col);
/*     */     
/* 796 */     if (declType != null) {
/* 797 */       Matcher matcher = COLUMN_PRECISION.matcher(declType);
/*     */       
/* 799 */       if (matcher.find()) {
/* 800 */         String[] array = matcher.group(1).split(",");
/*     */         
/* 802 */         if (array.length == 2) {
/* 803 */           return Integer.parseInt(array[1].trim());
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 808 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSchemaName(int col) {
/* 813 */     return "";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName(int col) throws SQLException {
/* 818 */     String tableName = safeGetColumnTableName(col);
/* 819 */     if (tableName == null)
/*     */     {
/* 821 */       return "";
/*     */     }
/* 823 */     return tableName;
/*     */   }
/*     */ 
/*     */   
/*     */   public int isNullable(int col) throws SQLException {
/* 828 */     checkMeta();
/* 829 */     return this.meta[checkCol(col)][1] ? 0 : 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoIncrement(int col) throws SQLException {
/* 836 */     checkMeta();
/* 837 */     return this.meta[checkCol(col)][2];
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCaseSensitive(int col) {
/* 842 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isCurrency(int col) {
/* 847 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDefinitelyWritable(int col) {
/* 852 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isReadOnly(int col) {
/* 857 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSearchable(int col) {
/* 862 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSigned(int col) throws SQLException {
/* 867 */     String typeName = getColumnTypeName(col);
/*     */     
/* 869 */     return ("NUMERIC".equals(typeName) || "INTEGER".equals(typeName) || "REAL".equals(typeName));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWritable(int col) {
/* 874 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getConcurrency() {
/* 879 */     return 1007;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean rowDeleted() {
/* 884 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean rowInserted() {
/* 889 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean rowUpdated() {
/* 894 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   private Calendar julianDateToCalendar(Double jd) {
/* 899 */     return julianDateToCalendar(jd, Calendar.getInstance());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Calendar julianDateToCalendar(Double jd, Calendar cal) {
/*     */     int A;
/* 908 */     if (jd == null) {
/* 909 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 914 */     double w = jd.doubleValue() + 0.5D;
/* 915 */     int Z = (int)w;
/* 916 */     double F = w - Z;
/*     */     
/* 918 */     if (Z < 2299161) {
/* 919 */       A = Z;
/*     */     } else {
/* 921 */       int alpha = (int)((Z - 1867216.25D) / 36524.25D);
/* 922 */       A = Z + 1 + alpha - (int)(alpha / 4.0D);
/*     */     } 
/*     */     
/* 925 */     int B = A + 1524;
/* 926 */     int C = (int)((B - 122.1D) / 365.25D);
/* 927 */     int D = (int)(365.25D * C);
/* 928 */     int E = (int)((B - D) / 30.6001D);
/*     */ 
/*     */     
/* 931 */     int mm = E - ((E < 13.5D) ? 1 : 13);
/*     */ 
/*     */     
/* 934 */     int yyyy = C - ((mm > 2.5D) ? 4716 : 4715);
/*     */ 
/*     */     
/* 937 */     double jjd = (B - D - (int)(30.6001D * E)) + F;
/* 938 */     int dd = (int)jjd;
/*     */ 
/*     */     
/* 941 */     double hhd = jjd - dd;
/* 942 */     int hh = (int)(24.0D * hhd);
/*     */ 
/*     */     
/* 945 */     double mnd = 24.0D * hhd - hh;
/* 946 */     int mn = (int)(60.0D * mnd);
/*     */ 
/*     */     
/* 949 */     double ssd = 60.0D * mnd - mn;
/* 950 */     int ss = (int)(60.0D * ssd);
/*     */ 
/*     */     
/* 953 */     double msd = 60.0D * ssd - ss;
/* 954 */     int ms = (int)(1000.0D * msd);
/*     */     
/* 956 */     cal.set(yyyy, mm - 1, dd, hh, mn, ss);
/* 957 */     cal.set(14, ms);
/*     */     
/* 959 */     if (yyyy < 1) {
/* 960 */       cal.set(0, 0);
/* 961 */       cal.set(1, -(yyyy - 1));
/*     */     } 
/*     */     
/* 964 */     return cal;
/*     */   }
/*     */   
/*     */   private void requireCalendarNotNull(Calendar cal) throws SQLException {
/* 968 */     if (cal == null) {
/* 969 */       throw new SQLException("Expected a calendar instance.", new IllegalArgumentException());
/*     */     }
/*     */   }
/*     */   
/*     */   private int safeGetColumnType(int col) throws SQLException {
/* 974 */     return this.stmt.pointer.safeRunInt((db, ptr) -> db.column_type(ptr, col));
/*     */   }
/*     */   
/*     */   private long safeGetLongCol(int col) throws SQLException {
/* 978 */     return this.stmt.pointer.safeRunLong((db, ptr) -> db.column_long(ptr, markCol(col)));
/*     */   }
/*     */   
/*     */   private double safeGetDoubleCol(int col) throws SQLException {
/* 982 */     return this.stmt.pointer.safeRunDouble((db, ptr) -> db.column_double(ptr, markCol(col)));
/*     */   }
/*     */   
/*     */   private String safeGetColumnText(int col) throws SQLException {
/* 986 */     return (String)this.stmt.pointer.safeRun((db, ptr) -> db.column_text(ptr, markCol(col)));
/*     */   }
/*     */   
/*     */   private String safeGetColumnTableName(int col) throws SQLException {
/* 990 */     return (String)this.stmt.pointer.safeRun((db, ptr) -> db.column_table_name(ptr, checkCol(col)));
/*     */   }
/*     */   
/*     */   private String safeGetColumnName(int col) throws SQLException {
/* 994 */     return (String)this.stmt.pointer.safeRun((db, ptr) -> db.column_name(ptr, checkCol(col)));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\jdbc3\JDBC3ResultSet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */