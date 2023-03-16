/*      */ package org.sqlite.jdbc3;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.sql.Connection;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Struct;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.sqlite.SQLiteConnection;
/*      */ import org.sqlite.core.CoreDatabaseMetaData;
/*      */ import org.sqlite.core.CoreStatement;
/*      */ import org.sqlite.util.StringUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class JDBC3DatabaseMetaData
/*      */   extends CoreDatabaseMetaData
/*      */ {
/*      */   private static String driverName;
/*      */   private static String driverVersion;
/*      */   
/*      */   static {
/*   35 */     try (InputStream sqliteJdbcPropStream = JDBC3DatabaseMetaData.class.getClassLoader().getResourceAsStream("sqlite-jdbc.properties")) {
/*   36 */       if (sqliteJdbcPropStream == null) {
/*   37 */         throw new IOException("Cannot load sqlite-jdbc.properties from jar");
/*      */       }
/*   39 */       Properties sqliteJdbcProp = new Properties();
/*   40 */       sqliteJdbcProp.load(sqliteJdbcPropStream);
/*   41 */       driverName = sqliteJdbcProp.getProperty("name");
/*   42 */       driverVersion = sqliteJdbcProp.getProperty("version");
/*   43 */     } catch (Exception e) {
/*      */       
/*   45 */       driverName = "SQLite JDBC";
/*   46 */       driverVersion = "3.0.0-UNKNOWN";
/*      */     } 
/*      */   }
/*      */   
/*      */   protected JDBC3DatabaseMetaData(SQLiteConnection conn) {
/*   51 */     super(conn);
/*      */   }
/*      */ 
/*      */   
/*      */   public Connection getConnection() {
/*   56 */     return (Connection)this.conn;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDatabaseMajorVersion() throws SQLException {
/*   61 */     return Integer.parseInt(this.conn.libversion().split("\\.")[0]);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDatabaseMinorVersion() throws SQLException {
/*   66 */     return Integer.parseInt(this.conn.libversion().split("\\.")[1]);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDriverMajorVersion() {
/*   71 */     return Integer.parseInt(driverVersion.split("\\.")[0]);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDriverMinorVersion() {
/*   76 */     return Integer.parseInt(driverVersion.split("\\.")[1]);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getJDBCMajorVersion() {
/*   81 */     return 4;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getJDBCMinorVersion() {
/*   86 */     return 2;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getDefaultTransactionIsolation() {
/*   91 */     return 8;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxBinaryLiteralLength() {
/*   96 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxCatalogNameLength() {
/*  101 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxCharLiteralLength() {
/*  106 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxColumnNameLength() {
/*  111 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInGroupBy() {
/*  116 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInIndex() {
/*  121 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInOrderBy() {
/*  126 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInSelect() {
/*  131 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxColumnsInTable() {
/*  136 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxConnections() {
/*  141 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxCursorNameLength() {
/*  146 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxIndexLength() {
/*  151 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxProcedureNameLength() {
/*  156 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxRowSize() {
/*  161 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxSchemaNameLength() {
/*  166 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxStatementLength() {
/*  171 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxStatements() {
/*  176 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxTableNameLength() {
/*  181 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxTablesInSelect() {
/*  186 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxUserNameLength() {
/*  191 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getResultSetHoldability() {
/*  196 */     return 2;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSQLStateType() {
/*  201 */     return 2;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDatabaseProductName() {
/*  206 */     return "SQLite";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDatabaseProductVersion() throws SQLException {
/*  211 */     return this.conn.libversion();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDriverName() {
/*  216 */     return driverName;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDriverVersion() {
/*  221 */     return driverVersion;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getExtraNameCharacters() {
/*  226 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getCatalogSeparator() {
/*  231 */     return ".";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getCatalogTerm() {
/*  236 */     return "catalog";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getSchemaTerm() {
/*  241 */     return "schema";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getProcedureTerm() {
/*  246 */     return "not_implemented";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getSearchStringEscape() {
/*  251 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getIdentifierQuoteString() {
/*  256 */     return "\"";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSQLKeywords() {
/*  264 */     return "ABORT,ACTION,AFTER,ANALYZE,ATTACH,AUTOINCREMENT,BEFORE,CASCADE,CONFLICT,DATABASE,DEFERRABLE,DEFERRED,DESC,DETACH,EXCLUSIVE,EXPLAIN,FAIL,GLOB,IGNORE,INDEX,INDEXED,INITIALLY,INSTEAD,ISNULL,KEY,LIMIT,NOTNULL,OFFSET,PLAN,PRAGMA,QUERY,RAISE,REGEXP,REINDEX,RENAME,REPLACE,RESTRICT,TEMP,TEMPORARY,TRANSACTION,VACUUM,VIEW,VIRTUAL";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getNumericFunctions() {
/*  274 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getStringFunctions() {
/*  279 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getSystemFunctions() {
/*  284 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getTimeDateFunctions() {
/*  289 */     return "DATE,TIME,DATETIME,JULIANDAY,STRFTIME";
/*      */   }
/*      */ 
/*      */   
/*      */   public String getURL() {
/*  294 */     return this.conn.getUrl();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getUserName() {
/*  299 */     return null;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean allProceduresAreCallable() {
/*  304 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean allTablesAreSelectable() {
/*  309 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean dataDefinitionCausesTransactionCommit() {
/*  314 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean dataDefinitionIgnoredInTransactions() {
/*  319 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean doesMaxRowSizeIncludeBlobs() {
/*  324 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean deletesAreDetected(int type) {
/*  329 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean insertsAreDetected(int type) {
/*  334 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isCatalogAtStart() {
/*  339 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean locatorsUpdateCopy() {
/*  344 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean nullPlusNonNullIsNull() {
/*  349 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean nullsAreSortedAtEnd() {
/*  354 */     return !nullsAreSortedAtStart();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean nullsAreSortedAtStart() {
/*  359 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean nullsAreSortedHigh() {
/*  364 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean nullsAreSortedLow() {
/*  369 */     return !nullsAreSortedHigh();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean othersDeletesAreVisible(int type) {
/*  374 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean othersInsertsAreVisible(int type) {
/*  379 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean othersUpdatesAreVisible(int type) {
/*  384 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean ownDeletesAreVisible(int type) {
/*  389 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean ownInsertsAreVisible(int type) {
/*  394 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean ownUpdatesAreVisible(int type) {
/*  399 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean storesLowerCaseIdentifiers() {
/*  404 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean storesLowerCaseQuotedIdentifiers() {
/*  409 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean storesMixedCaseIdentifiers() {
/*  414 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean storesMixedCaseQuotedIdentifiers() {
/*  419 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean storesUpperCaseIdentifiers() {
/*  424 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean storesUpperCaseQuotedIdentifiers() {
/*  429 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsAlterTableWithAddColumn() {
/*  434 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsAlterTableWithDropColumn() {
/*  439 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsANSI92EntryLevelSQL() {
/*  444 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsANSI92FullSQL() {
/*  449 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsANSI92IntermediateSQL() {
/*  454 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsBatchUpdates() {
/*  459 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsCatalogsInDataManipulation() {
/*  464 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsCatalogsInIndexDefinitions() {
/*  469 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsCatalogsInPrivilegeDefinitions() {
/*  474 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsCatalogsInProcedureCalls() {
/*  479 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsCatalogsInTableDefinitions() {
/*  484 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsColumnAliasing() {
/*  489 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsConvert() {
/*  494 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsConvert(int fromType, int toType) {
/*  499 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsCorrelatedSubqueries() {
/*  504 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsDataDefinitionAndDataManipulationTransactions() {
/*  509 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsDataManipulationTransactionsOnly() {
/*  514 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsDifferentTableCorrelationNames() {
/*  519 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsExpressionsInOrderBy() {
/*  524 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsMinimumSQLGrammar() {
/*  529 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsCoreSQLGrammar() {
/*  534 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsExtendedSQLGrammar() {
/*  539 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsLimitedOuterJoins() {
/*  544 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsFullOuterJoins() throws SQLException {
/*  549 */     String[] version = this.conn.libversion().split("\\.");
/*  550 */     return (Integer.parseInt(version[0]) >= 3 && Integer.parseInt(version[1]) >= 39);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsGetGeneratedKeys() {
/*  555 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsGroupBy() {
/*  560 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsGroupByBeyondSelect() {
/*  565 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsGroupByUnrelated() {
/*  570 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsIntegrityEnhancementFacility() {
/*  575 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsLikeEscapeClause() {
/*  580 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsMixedCaseIdentifiers() {
/*  585 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsMixedCaseQuotedIdentifiers() {
/*  590 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsMultipleOpenResults() {
/*  595 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsMultipleResultSets() {
/*  600 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsMultipleTransactions() {
/*  605 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsNamedParameters() {
/*  610 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsNonNullableColumns() {
/*  615 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsOpenCursorsAcrossCommit() {
/*  620 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsOpenCursorsAcrossRollback() {
/*  625 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsOpenStatementsAcrossCommit() {
/*  630 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsOpenStatementsAcrossRollback() {
/*  635 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsOrderByUnrelated() {
/*  640 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsOuterJoins() {
/*  645 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsPositionedDelete() {
/*  650 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsPositionedUpdate() {
/*  655 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsResultSetConcurrency(int t, int c) {
/*  660 */     return (t == 1003 && c == 1007);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsResultSetHoldability(int h) {
/*  665 */     return (h == 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsResultSetType(int t) {
/*  670 */     return (t == 1003);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSavepoints() {
/*  675 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInDataManipulation() {
/*  680 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInIndexDefinitions() {
/*  685 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInPrivilegeDefinitions() {
/*  690 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInProcedureCalls() {
/*  695 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSchemasInTableDefinitions() {
/*  700 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSelectForUpdate() {
/*  705 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsStatementPooling() {
/*  710 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsStoredProcedures() {
/*  715 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSubqueriesInComparisons() {
/*  720 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSubqueriesInExists() {
/*  725 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSubqueriesInIns() {
/*  730 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsSubqueriesInQuantifieds() {
/*  735 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsTableCorrelationNames() {
/*  740 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsTransactionIsolationLevel(int level) {
/*  745 */     return (level == 8);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsTransactions() {
/*  750 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsUnion() {
/*  755 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsUnionAll() {
/*  760 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean updatesAreDetected(int type) {
/*  765 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean usesLocalFilePerTable() {
/*  770 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean usesLocalFiles() {
/*  775 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() throws SQLException {
/*  780 */     return this.conn.isReadOnly();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getAttributes(String c, String s, String t, String a) throws SQLException {
/*  788 */     if (this.getAttributes == null) {
/*  789 */       this
/*  790 */         .getAttributes = this.conn.prepareStatement("select null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as ATTR_NAME, null as DATA_TYPE, null as ATTR_TYPE_NAME, null as ATTR_SIZE, null as DECIMAL_DIGITS, null as NUM_PREC_RADIX, null as NULLABLE, null as REMARKS, null as ATTR_DEF, null as SQL_DATA_TYPE, null as SQL_DATETIME_SUB, null as CHAR_OCTET_LENGTH, null as ORDINAL_POSITION, null as IS_NULLABLE, null as SCOPE_CATALOG, null as SCOPE_SCHEMA, null as SCOPE_TABLE, null as SOURCE_DATA_TYPE limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  800 */     return this.getAttributes.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getBestRowIdentifier(String c, String s, String t, int scope, boolean n) throws SQLException {
/*  809 */     if (this.getBestRowIdentifier == null) {
/*  810 */       this
/*  811 */         .getBestRowIdentifier = this.conn.prepareStatement("select null as SCOPE, null as COLUMN_NAME, null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  817 */     return this.getBestRowIdentifier.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getColumnPrivileges(String c, String s, String t, String colPat) throws SQLException {
/*  826 */     if (this.getColumnPrivileges == null) {
/*  827 */       this
/*  828 */         .getColumnPrivileges = this.conn.prepareStatement("select null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as COLUMN_NAME, null as GRANTOR, null as GRANTEE, null as PRIVILEGE, null as IS_GRANTABLE limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  834 */     return this.getColumnPrivileges.executeQuery();
/*      */   }
/*      */ 
/*      */   
/*  838 */   protected static final Pattern TYPE_INTEGER = Pattern.compile(".*(INT|BOOL).*");
/*  839 */   protected static final Pattern TYPE_VARCHAR = Pattern.compile(".*(CHAR|CLOB|TEXT|BLOB).*");
/*  840 */   protected static final Pattern TYPE_FLOAT = Pattern.compile(".*(REAL|FLOA|DOUB|DEC|NUM).*");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getColumns(String c, String s, String tblNamePattern, String colNamePattern) throws SQLException {
/*  911 */     checkOpen();
/*      */     
/*  913 */     StringBuilder sql = new StringBuilder(700);
/*  914 */     sql.append("select null as TABLE_CAT, null as TABLE_SCHEM, tblname as TABLE_NAME, ")
/*  915 */       .append("cn as COLUMN_NAME, ct as DATA_TYPE, tn as TYPE_NAME, colSize as COLUMN_SIZE, ")
/*      */       
/*  917 */       .append("2000000000 as BUFFER_LENGTH, colDecimalDigits as DECIMAL_DIGITS, 10   as NUM_PREC_RADIX, ")
/*      */       
/*  919 */       .append("colnullable as NULLABLE, null as REMARKS, colDefault as COLUMN_DEF, ")
/*  920 */       .append("0    as SQL_DATA_TYPE, 0    as SQL_DATETIME_SUB, 2000000000 as CHAR_OCTET_LENGTH, ")
/*      */       
/*  922 */       .append("ordpos as ORDINAL_POSITION, (case colnullable when 0 then 'NO' when 1 then 'YES' else '' end)")
/*      */       
/*  924 */       .append("    as IS_NULLABLE, null as SCOPE_CATLOG, null as SCOPE_SCHEMA, ")
/*  925 */       .append("null as SCOPE_TABLE, null as SOURCE_DATA_TYPE, ")
/*  926 */       .append("(case colautoincrement when 0 then 'NO' when 1 then 'YES' else '' end) as IS_AUTOINCREMENT, ")
/*      */       
/*  928 */       .append("(case colgenerated when 0 then 'NO' when 1 then 'YES' else '' end) as IS_GENERATEDCOLUMN from (");
/*      */ 
/*      */     
/*  931 */     boolean colFound = false;
/*      */     
/*  933 */     ResultSet rs = null;
/*      */     
/*      */     try {
/*  936 */       String[] types = { "TABLE", "VIEW" };
/*  937 */       rs = getTables(c, s, tblNamePattern, types);
/*  938 */       while (rs.next()) {
/*  939 */         boolean isAutoIncrement; String tableName = rs.getString(3);
/*      */ 
/*      */ 
/*      */         
/*  943 */         Statement statColAutoinc = this.conn.createStatement();
/*  944 */         ResultSet rsColAutoinc = null;
/*      */         try {
/*  946 */           statColAutoinc = this.conn.createStatement();
/*      */           
/*  948 */           rsColAutoinc = statColAutoinc.executeQuery("SELECT LIKE('%autoincrement%', LOWER(sql)) FROM sqlite_master WHERE LOWER(name) = LOWER('" + 
/*      */ 
/*      */               
/*  951 */               escape(tableName) + "') AND TYPE IN ('table', 'view')");
/*      */           
/*  953 */           rsColAutoinc.next();
/*  954 */           isAutoIncrement = (rsColAutoinc.getInt(1) == 1);
/*      */         } finally {
/*  956 */           if (rsColAutoinc != null) {
/*      */             try {
/*  958 */               rsColAutoinc.close();
/*  959 */             } catch (Exception e) {
/*  960 */               e.printStackTrace();
/*      */             } 
/*      */           }
/*  963 */           if (statColAutoinc != null) {
/*      */             try {
/*  965 */               statColAutoinc.close();
/*  966 */             } catch (Exception e) {
/*  967 */               e.printStackTrace();
/*      */             } 
/*      */           }
/*      */         } 
/*      */ 
/*      */         
/*  973 */         String pragmaStatement = "PRAGMA table_xinfo('" + escape(tableName) + "')";
/*  974 */         try(Statement colstat = this.conn.createStatement(); 
/*  975 */             ResultSet rscol = colstat.executeQuery(pragmaStatement)) {
/*      */           
/*  977 */           for (int i = 0; rscol.next(); i++) {
/*  978 */             int colJavaType; String colName = rscol.getString(2);
/*  979 */             String colType = rscol.getString(3);
/*  980 */             String colNotNull = rscol.getString(4);
/*  981 */             String colDefault = rscol.getString(5);
/*  982 */             boolean isPk = "1".equals(rscol.getString(6));
/*  983 */             String colHidden = rscol.getString(7);
/*      */             
/*  985 */             int colNullable = 2;
/*  986 */             if (colNotNull != null) {
/*  987 */               colNullable = colNotNull.equals("0") ? 1 : 0;
/*      */             }
/*      */             
/*  990 */             if (colFound) {
/*  991 */               sql.append(" union all ");
/*      */             }
/*  993 */             colFound = true;
/*      */ 
/*      */             
/*  996 */             int iColumnSize = 2000000000;
/*  997 */             int iDecimalDigits = 10;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1004 */             colType = (colType == null) ? "TEXT" : colType.toUpperCase();
/*      */             
/* 1006 */             int colAutoIncrement = 0;
/* 1007 */             if (isPk && isAutoIncrement) {
/* 1008 */               colAutoIncrement = 1;
/*      */             }
/*      */ 
/*      */             
/* 1012 */             if (TYPE_INTEGER.matcher(colType).find()) {
/* 1013 */               colJavaType = 4;
/*      */               
/* 1015 */               iDecimalDigits = 0;
/* 1016 */             } else if (TYPE_VARCHAR.matcher(colType).find()) {
/* 1017 */               colJavaType = 12;
/*      */               
/* 1019 */               iDecimalDigits = 0;
/* 1020 */             } else if (TYPE_FLOAT.matcher(colType).find()) {
/* 1021 */               colJavaType = 6;
/*      */             } else {
/*      */               
/* 1024 */               colJavaType = 12;
/*      */             } 
/*      */             
/* 1027 */             int iStartOfDimension = colType.indexOf('(');
/* 1028 */             if (iStartOfDimension > 0) {
/*      */               
/* 1030 */               int iEndOfDimension = colType.indexOf(')', iStartOfDimension);
/* 1031 */               if (iEndOfDimension > 0) {
/*      */                 String sInteger, sDecimal;
/*      */ 
/*      */                 
/* 1035 */                 int iDimensionSeparator = colType.indexOf(',', iStartOfDimension);
/* 1036 */                 if (iDimensionSeparator > 0) {
/*      */                   
/* 1038 */                   sInteger = colType.substring(iStartOfDimension + 1, iDimensionSeparator);
/*      */ 
/*      */                   
/* 1041 */                   sDecimal = colType.substring(iDimensionSeparator + 1, iEndOfDimension);
/*      */                 
/*      */                 }
/*      */                 else {
/*      */ 
/*      */                   
/* 1047 */                   sInteger = colType.substring(iStartOfDimension + 1, iEndOfDimension);
/*      */                   
/* 1049 */                   sDecimal = null;
/*      */                 } 
/*      */                 
/*      */                 try {
/* 1053 */                   int iInteger = Integer.parseUnsignedInt(sInteger);
/*      */                   
/* 1055 */                   if (sDecimal != null) {
/* 1056 */                     iDecimalDigits = Integer.parseUnsignedInt(sDecimal);
/*      */ 
/*      */                     
/* 1059 */                     iColumnSize = iInteger + iDecimalDigits;
/*      */                   } else {
/*      */                     
/* 1062 */                     iDecimalDigits = 0;
/*      */                     
/* 1064 */                     iColumnSize = iInteger;
/*      */                   } 
/* 1066 */                 } catch (NumberFormatException numberFormatException) {}
/*      */               } 
/*      */             } 
/*      */ 
/*      */ 
/*      */             
/* 1072 */             int colGenerated = "2".equals(colHidden) ? 1 : 0;
/*      */             
/* 1074 */             sql.append("select ")
/* 1075 */               .append(i + 1)
/* 1076 */               .append(" as ordpos, ")
/* 1077 */               .append(colNullable)
/* 1078 */               .append(" as colnullable,")
/* 1079 */               .append("'")
/* 1080 */               .append(colJavaType)
/* 1081 */               .append("' as ct, ")
/* 1082 */               .append(iColumnSize)
/* 1083 */               .append(" as colSize, ")
/* 1084 */               .append(iDecimalDigits)
/* 1085 */               .append(" as colDecimalDigits, ")
/* 1086 */               .append("'")
/* 1087 */               .append(tableName)
/* 1088 */               .append("' as tblname, ")
/* 1089 */               .append("'")
/* 1090 */               .append(escape(colName))
/* 1091 */               .append("' as cn, ")
/* 1092 */               .append("'")
/* 1093 */               .append(escape(colType))
/* 1094 */               .append("' as tn, ")
/* 1095 */               .append(quote((colDefault == null) ? null : escape(colDefault)))
/* 1096 */               .append(" as colDefault,")
/* 1097 */               .append(colAutoIncrement)
/* 1098 */               .append(" as colautoincrement,")
/* 1099 */               .append(colGenerated)
/* 1100 */               .append(" as colgenerated");
/*      */             
/* 1102 */             if (colNamePattern != null) {
/* 1103 */               sql.append(" where upper(cn) like upper('")
/* 1104 */                 .append(escape(colNamePattern))
/* 1105 */                 .append("')");
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } finally {
/* 1111 */       if (rs != null) {
/*      */         try {
/* 1113 */           rs.close();
/* 1114 */         } catch (Exception e) {
/* 1115 */           e.printStackTrace();
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/* 1120 */     if (colFound) {
/* 1121 */       sql.append(") order by TABLE_SCHEM, TABLE_NAME, ORDINAL_POSITION;");
/*      */     } else {
/* 1123 */       sql.append("select null as ordpos, null as colnullable, null as ct, null as colsize, null as colDecimalDigits, null as tblname, null as cn, null as tn, null as colDefault, null as colautoincrement, null as colgenerated) limit 0;");
/*      */     } 
/*      */ 
/*      */     
/* 1127 */     Statement stat = this.conn.createStatement();
/* 1128 */     return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getCrossReference(String pc, String ps, String pt, String fc, String fs, String ft) throws SQLException {
/* 1137 */     if (pt == null) {
/* 1138 */       return getExportedKeys(fc, fs, ft);
/*      */     }
/*      */     
/* 1141 */     if (ft == null) {
/* 1142 */       return getImportedKeys(pc, ps, pt);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1158 */     String query = "select " + quote(pc) + " as PKTABLE_CAT, " + quote(ps) + " as PKTABLE_SCHEM, " + quote(pt) + " as PKTABLE_NAME, '' as PKCOLUMN_NAME, " + quote(fc) + " as FKTABLE_CAT, " + quote(fs) + " as FKTABLE_SCHEM, " + quote(ft) + " as FKTABLE_NAME, '' as FKCOLUMN_NAME, -1 as KEY_SEQ, 3 as UPDATE_RULE, 3 as DELETE_RULE, '' as FK_NAME, '' as PK_NAME, " + '\005' + " as DEFERRABILITY limit 0 ";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1164 */     return ((CoreStatement)this.conn.createStatement()).executeQuery(query, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet getSchemas() throws SQLException {
/* 1169 */     if (this.getSchemas == null) {
/* 1170 */       this
/* 1171 */         .getSchemas = this.conn.prepareStatement("select null as TABLE_SCHEM, null as TABLE_CATALOG limit 0;");
/*      */     }
/*      */ 
/*      */     
/* 1175 */     return this.getSchemas.executeQuery();
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet getCatalogs() throws SQLException {
/* 1180 */     if (this.getCatalogs == null) {
/* 1181 */       this.getCatalogs = this.conn.prepareStatement("select null as TABLE_CAT limit 0;");
/*      */     }
/*      */     
/* 1184 */     return this.getCatalogs.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getPrimaryKeys(String c, String s, String table) throws SQLException {
/* 1192 */     PrimaryKeyFinder pkFinder = new PrimaryKeyFinder(table);
/* 1193 */     String[] columns = pkFinder.getColumns();
/*      */     
/* 1195 */     Statement stat = this.conn.createStatement();
/* 1196 */     StringBuilder sql = new StringBuilder(512);
/* 1197 */     sql.append("select null as TABLE_CAT, null as TABLE_SCHEM, '")
/* 1198 */       .append(escape(table))
/* 1199 */       .append("' as TABLE_NAME, cn as COLUMN_NAME, ks as KEY_SEQ, pk as PK_NAME from (");
/*      */     
/* 1201 */     if (columns == null) {
/* 1202 */       sql.append("select null as cn, null as pk, 0 as ks) limit 0;");
/*      */       
/* 1204 */       return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */     } 
/*      */     
/* 1207 */     String pkName = pkFinder.getName();
/* 1208 */     if (pkName != null) {
/* 1209 */       pkName = "'" + pkName + "'";
/*      */     }
/*      */     
/* 1212 */     for (int i = 0; i < columns.length; i++) {
/* 1213 */       if (i > 0) sql.append(" union "); 
/* 1214 */       sql.append("select ")
/* 1215 */         .append(pkName)
/* 1216 */         .append(" as pk, '")
/* 1217 */         .append(escape(unquoteIdentifier(columns[i])))
/* 1218 */         .append("' as cn, ")
/* 1219 */         .append(i + 1)
/* 1220 */         .append(" as ks");
/*      */     } 
/*      */     
/* 1223 */     return ((CoreStatement)stat).executeQuery(sql.append(") order by cn;").toString(), true);
/*      */   }
/*      */   
/* 1226 */   private static final Map<String, Integer> RULE_MAP = new HashMap<>();
/*      */   
/*      */   static {
/* 1229 */     RULE_MAP.put("NO ACTION", Integer.valueOf(3));
/* 1230 */     RULE_MAP.put("CASCADE", Integer.valueOf(0));
/* 1231 */     RULE_MAP.put("RESTRICT", Integer.valueOf(1));
/* 1232 */     RULE_MAP.put("SET NULL", Integer.valueOf(2));
/* 1233 */     RULE_MAP.put("SET DEFAULT", Integer.valueOf(4));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
/* 1242 */     PrimaryKeyFinder pkFinder = new PrimaryKeyFinder(table);
/* 1243 */     String[] pkColumns = pkFinder.getColumns();
/* 1244 */     Statement stat = this.conn.createStatement();
/*      */     
/* 1246 */     catalog = (catalog != null) ? quote(catalog) : null;
/* 1247 */     schema = (schema != null) ? quote(schema) : null;
/*      */     
/* 1249 */     StringBuilder exportedKeysQuery = new StringBuilder(512);
/*      */     
/* 1251 */     String target = null;
/* 1252 */     int count = 0;
/* 1253 */     if (pkColumns != null) {
/*      */       ArrayList<String> tableList;
/*      */ 
/*      */       
/* 1257 */       try (ResultSet rs = stat.executeQuery("select name from sqlite_master where type = 'table'")) {
/* 1258 */         tableList = new ArrayList<>();
/*      */         
/* 1260 */         while (rs.next()) {
/* 1261 */           String tblname = rs.getString(1);
/* 1262 */           tableList.add(tblname);
/* 1263 */           if (tblname.equalsIgnoreCase(table))
/*      */           {
/*      */             
/* 1266 */             target = tblname;
/*      */           }
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1272 */       for (String tbl : tableList) {
/* 1273 */         ImportedKeyFinder impFkFinder = new ImportedKeyFinder(tbl);
/* 1274 */         List<ImportedKeyFinder.ForeignKey> fkNames = impFkFinder.getFkList();
/*      */         
/* 1276 */         for (ImportedKeyFinder.ForeignKey foreignKey : fkNames) {
/* 1277 */           String PKTabName = foreignKey.getPkTableName();
/*      */           
/* 1279 */           if (PKTabName == null || !PKTabName.equalsIgnoreCase(target)) {
/*      */             continue;
/*      */           }
/*      */           
/* 1283 */           for (int j = 0; j < foreignKey.getColumnMappingCount(); j++) {
/* 1284 */             int keySeq = j + 1;
/* 1285 */             String[] columnMapping = foreignKey.getColumnMapping(j);
/* 1286 */             String PKColName = columnMapping[1];
/* 1287 */             PKColName = (PKColName == null) ? "" : PKColName;
/* 1288 */             String FKColName = columnMapping[0];
/* 1289 */             FKColName = (FKColName == null) ? "" : FKColName;
/*      */             
/* 1291 */             boolean usePkName = false;
/* 1292 */             for (String pkColumn : pkColumns) {
/* 1293 */               if (pkColumn != null && pkColumn.equalsIgnoreCase(PKColName)) {
/* 1294 */                 usePkName = true;
/*      */                 
/*      */                 break;
/*      */               } 
/*      */             } 
/* 1299 */             String pkName = (usePkName && pkFinder.getName() != null) ? pkFinder.getName() : "";
/*      */             
/* 1301 */             exportedKeysQuery
/* 1302 */               .append((count > 0) ? " union all select " : "select ")
/* 1303 */               .append(keySeq)
/* 1304 */               .append(" as ks, '")
/* 1305 */               .append(escape(tbl))
/* 1306 */               .append("' as fkt, '")
/* 1307 */               .append(escape(FKColName))
/* 1308 */               .append("' as fcn, '")
/* 1309 */               .append(escape(PKColName))
/* 1310 */               .append("' as pcn, '")
/* 1311 */               .append(escape(pkName))
/* 1312 */               .append("' as pkn, ")
/* 1313 */               .append(RULE_MAP.get(foreignKey.getOnUpdate()))
/* 1314 */               .append(" as ur, ")
/* 1315 */               .append(RULE_MAP.get(foreignKey.getOnDelete()))
/* 1316 */               .append(" as dr, ");
/*      */             
/* 1318 */             String fkName = foreignKey.getFkName();
/*      */             
/* 1320 */             if (fkName != null) {
/* 1321 */               exportedKeysQuery.append("'").append(escape(fkName)).append("' as fkn");
/*      */             } else {
/* 1323 */               exportedKeysQuery.append("'' as fkn");
/*      */             } 
/*      */             
/* 1326 */             count++;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1332 */     boolean hasImportedKey = (count > 0);
/* 1333 */     StringBuilder sql = new StringBuilder(512);
/* 1334 */     sql.append("select ")
/* 1335 */       .append(catalog)
/* 1336 */       .append(" as PKTABLE_CAT, ")
/* 1337 */       .append(schema)
/* 1338 */       .append(" as PKTABLE_SCHEM, ")
/* 1339 */       .append(quote(target))
/* 1340 */       .append(" as PKTABLE_NAME, ")
/* 1341 */       .append(hasImportedKey ? "pcn" : "''")
/* 1342 */       .append(" as PKCOLUMN_NAME, ")
/* 1343 */       .append(catalog)
/* 1344 */       .append(" as FKTABLE_CAT, ")
/* 1345 */       .append(schema)
/* 1346 */       .append(" as FKTABLE_SCHEM, ")
/* 1347 */       .append(hasImportedKey ? "fkt" : "''")
/* 1348 */       .append(" as FKTABLE_NAME, ")
/* 1349 */       .append(hasImportedKey ? "fcn" : "''")
/* 1350 */       .append(" as FKCOLUMN_NAME, ")
/* 1351 */       .append(hasImportedKey ? "ks" : "-1")
/* 1352 */       .append(" as KEY_SEQ, ")
/* 1353 */       .append(hasImportedKey ? "ur" : "3")
/* 1354 */       .append(" as UPDATE_RULE, ")
/* 1355 */       .append(hasImportedKey ? "dr" : "3")
/* 1356 */       .append(" as DELETE_RULE, ")
/* 1357 */       .append(hasImportedKey ? "fkn" : "''")
/* 1358 */       .append(" as FK_NAME, ")
/* 1359 */       .append(hasImportedKey ? "pkn" : "''")
/* 1360 */       .append(" as PK_NAME, ")
/* 1361 */       .append(5)
/*      */       
/* 1363 */       .append(" as DEFERRABILITY ");
/*      */     
/* 1365 */     if (hasImportedKey) {
/* 1366 */       sql.append("from (")
/* 1367 */         .append(exportedKeysQuery)
/* 1368 */         .append(") ORDER BY FKTABLE_CAT, FKTABLE_SCHEM, FKTABLE_NAME, KEY_SEQ");
/*      */     } else {
/* 1370 */       sql.append("limit 0");
/*      */     } 
/*      */     
/* 1373 */     return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */   }
/*      */   
/*      */   private StringBuilder appendDummyForeignKeyList(StringBuilder sql) {
/* 1377 */     sql.append("select -1 as ks, '' as ptn, '' as fcn, '' as pcn, ")
/* 1378 */       .append(3)
/* 1379 */       .append(" as ur, ")
/* 1380 */       .append(3)
/* 1381 */       .append(" as dr, ")
/* 1382 */       .append(" '' as fkn, ")
/* 1383 */       .append(" '' as pkn ")
/* 1384 */       .append(") limit 0;");
/* 1385 */     return sql;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
/*      */     ResultSet rs;
/* 1395 */     Statement stat = this.conn.createStatement();
/* 1396 */     StringBuilder sql = new StringBuilder(700);
/*      */     
/* 1398 */     sql.append("select ")
/* 1399 */       .append(quote(catalog))
/* 1400 */       .append(" as PKTABLE_CAT, ")
/* 1401 */       .append(quote(schema))
/* 1402 */       .append(" as PKTABLE_SCHEM, ")
/* 1403 */       .append("ptn as PKTABLE_NAME, pcn as PKCOLUMN_NAME, ")
/* 1404 */       .append(quote(catalog))
/* 1405 */       .append(" as FKTABLE_CAT, ")
/* 1406 */       .append(quote(schema))
/* 1407 */       .append(" as FKTABLE_SCHEM, ")
/* 1408 */       .append(quote(table))
/* 1409 */       .append(" as FKTABLE_NAME, ")
/* 1410 */       .append("fcn as FKCOLUMN_NAME, ks as KEY_SEQ, ur as UPDATE_RULE, dr as DELETE_RULE, fkn as FK_NAME, pkn as PK_NAME, ")
/*      */       
/* 1412 */       .append(5)
/* 1413 */       .append(" as DEFERRABILITY from (");
/*      */ 
/*      */     
/*      */     try {
/* 1417 */       rs = stat.executeQuery("pragma foreign_key_list('" + escape(table) + "');");
/* 1418 */     } catch (SQLException e) {
/* 1419 */       sql = appendDummyForeignKeyList(sql);
/* 1420 */       return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */     } 
/*      */     
/* 1423 */     ImportedKeyFinder impFkFinder = new ImportedKeyFinder(table);
/* 1424 */     List<ImportedKeyFinder.ForeignKey> fkNames = impFkFinder.getFkList();
/*      */     
/* 1426 */     int i = 0;
/* 1427 */     for (; rs.next(); i++) {
/* 1428 */       int keySeq = rs.getInt(2) + 1;
/* 1429 */       int keyId = rs.getInt(1);
/* 1430 */       String PKTabName = rs.getString(3);
/* 1431 */       String FKColName = rs.getString(4);
/* 1432 */       String PKColName = rs.getString(5);
/*      */       
/* 1434 */       String pkName = null;
/*      */       try {
/* 1436 */         PrimaryKeyFinder pkFinder = new PrimaryKeyFinder(PKTabName);
/* 1437 */         pkName = pkFinder.getName();
/* 1438 */         if (PKColName == null) {
/* 1439 */           PKColName = pkFinder.getColumns()[0];
/*      */         }
/* 1441 */       } catch (SQLException sQLException) {}
/*      */ 
/*      */       
/* 1444 */       String updateRule = rs.getString(6);
/* 1445 */       String deleteRule = rs.getString(7);
/*      */       
/* 1447 */       if (i > 0) {
/* 1448 */         sql.append(" union all ");
/*      */       }
/*      */       
/* 1451 */       String fkName = null;
/* 1452 */       if (fkNames.size() > keyId) fkName = ((ImportedKeyFinder.ForeignKey)fkNames.get(keyId)).getFkName();
/*      */       
/* 1454 */       sql.append("select ")
/* 1455 */         .append(keySeq)
/* 1456 */         .append(" as ks,")
/* 1457 */         .append("'")
/* 1458 */         .append(escape(PKTabName))
/* 1459 */         .append("' as ptn, '")
/* 1460 */         .append(escape(FKColName))
/* 1461 */         .append("' as fcn, '")
/* 1462 */         .append(escape(PKColName))
/* 1463 */         .append("' as pcn,")
/* 1464 */         .append("case '")
/* 1465 */         .append(escape(updateRule))
/* 1466 */         .append("'")
/* 1467 */         .append(" when 'NO ACTION' then ")
/* 1468 */         .append(3)
/* 1469 */         .append(" when 'CASCADE' then ")
/* 1470 */         .append(0)
/* 1471 */         .append(" when 'RESTRICT' then ")
/* 1472 */         .append(1)
/* 1473 */         .append(" when 'SET NULL' then ")
/* 1474 */         .append(2)
/* 1475 */         .append(" when 'SET DEFAULT' then ")
/* 1476 */         .append(4)
/* 1477 */         .append(" end as ur, ")
/* 1478 */         .append("case '")
/* 1479 */         .append(escape(deleteRule))
/* 1480 */         .append("'")
/* 1481 */         .append(" when 'NO ACTION' then ")
/* 1482 */         .append(3)
/* 1483 */         .append(" when 'CASCADE' then ")
/* 1484 */         .append(0)
/* 1485 */         .append(" when 'RESTRICT' then ")
/* 1486 */         .append(1)
/* 1487 */         .append(" when 'SET NULL' then ")
/* 1488 */         .append(2)
/* 1489 */         .append(" when 'SET DEFAULT' then ")
/* 1490 */         .append(4)
/* 1491 */         .append(" end as dr, ")
/* 1492 */         .append((fkName == null) ? "''" : quote(fkName))
/* 1493 */         .append(" as fkn, ")
/* 1494 */         .append((pkName == null) ? "''" : quote(pkName))
/* 1495 */         .append(" as pkn");
/*      */     } 
/* 1497 */     rs.close();
/*      */     
/* 1499 */     if (i == 0) {
/* 1500 */       sql = appendDummyForeignKeyList(sql);
/*      */     }
/* 1502 */     sql.append(") ORDER BY PKTABLE_CAT, PKTABLE_SCHEM, PKTABLE_NAME, KEY_SEQ;");
/*      */     
/* 1504 */     return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getIndexInfo(String c, String s, String table, boolean u, boolean approximate) throws SQLException {
/* 1514 */     Statement stat = this.conn.createStatement();
/* 1515 */     StringBuilder sql = new StringBuilder(500);
/*      */ 
/*      */ 
/*      */     
/* 1519 */     sql.append("select null as TABLE_CAT, null as TABLE_SCHEM, '")
/* 1520 */       .append(escape(table))
/* 1521 */       .append("' as TABLE_NAME, un as NON_UNIQUE, null as INDEX_QUALIFIER, n as INDEX_NAME, ")
/*      */       
/* 1523 */       .append(Integer.toString(3))
/* 1524 */       .append(" as TYPE, op as ORDINAL_POSITION, ")
/* 1525 */       .append("cn as COLUMN_NAME, null as ASC_OR_DESC, 0 as CARDINALITY, 0 as PAGES, null as FILTER_CONDITION from (");
/*      */ 
/*      */ 
/*      */     
/* 1529 */     ResultSet rs = stat.executeQuery("pragma index_list('" + escape(table) + "');");
/*      */     
/* 1531 */     ArrayList<ArrayList<Object>> indexList = new ArrayList<>();
/* 1532 */     while (rs.next()) {
/* 1533 */       indexList.add(new ArrayList());
/* 1534 */       ((ArrayList<String>)indexList.get(indexList.size() - 1)).add(rs.getString(2));
/* 1535 */       ((ArrayList<Integer>)indexList.get(indexList.size() - 1)).add(Integer.valueOf(rs.getInt(3)));
/*      */     } 
/* 1537 */     rs.close();
/* 1538 */     if (indexList.size() == 0) {
/*      */       
/* 1540 */       sql.append("select null as un, null as n, null as op, null as cn) limit 0;");
/* 1541 */       return ((CoreStatement)stat).executeQuery(sql.toString(), true);
/*      */     } 
/*      */ 
/*      */     
/* 1545 */     Iterator<ArrayList<Object>> indexIterator = indexList.iterator();
/*      */ 
/*      */     
/* 1548 */     ArrayList<String> unionAll = new ArrayList<>();
/*      */     
/* 1550 */     while (indexIterator.hasNext()) {
/* 1551 */       ArrayList<Object> currentIndex = indexIterator.next();
/* 1552 */       String indexName = currentIndex.get(0).toString();
/* 1553 */       rs = stat.executeQuery("pragma index_info('" + escape(indexName) + "');");
/*      */       
/* 1555 */       while (rs.next()) {
/*      */         
/* 1557 */         StringBuilder sqlRow = new StringBuilder();
/*      */         
/* 1559 */         String colName = rs.getString(3);
/* 1560 */         sqlRow.append("select ")
/* 1561 */           .append(1 - ((Integer)currentIndex.get(1)).intValue())
/* 1562 */           .append(" as un,'")
/* 1563 */           .append(escape(indexName))
/* 1564 */           .append("' as n,")
/* 1565 */           .append(rs.getInt(1) + 1)
/* 1566 */           .append(" as op,");
/* 1567 */         if (colName == null) {
/* 1568 */           sqlRow.append("null");
/*      */         } else {
/* 1570 */           sqlRow.append("'").append(escape(colName)).append("'");
/*      */         } 
/* 1572 */         sqlRow.append(" as cn");
/*      */         
/* 1574 */         unionAll.add(sqlRow.toString());
/*      */       } 
/*      */       
/* 1577 */       rs.close();
/*      */     } 
/*      */     
/* 1580 */     String sqlBlock = StringUtils.join(unionAll, " union all ");
/*      */     
/* 1582 */     return ((CoreStatement)stat)
/* 1583 */       .executeQuery(sql.append(sqlBlock).append(");").toString(), true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getProcedureColumns(String c, String s, String p, String colPat) throws SQLException {
/* 1593 */     if (this.getProcedureColumns == null) {
/* 1594 */       this
/* 1595 */         .getProcedureColumns = this.conn.prepareStatement("select null as PROCEDURE_CAT, null as PROCEDURE_SCHEM, null as PROCEDURE_NAME, null as COLUMN_NAME, null as COLUMN_TYPE, null as DATA_TYPE, null as TYPE_NAME, null as PRECISION, null as LENGTH, null as SCALE, null as RADIX, null as NULLABLE, null as REMARKS limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1602 */     return this.getProcedureColumns.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getProcedures(String c, String s, String p) throws SQLException {
/* 1610 */     if (this.getProcedures == null) {
/* 1611 */       this
/* 1612 */         .getProcedures = this.conn.prepareStatement("select null as PROCEDURE_CAT, null as PROCEDURE_SCHEM, null as PROCEDURE_NAME, null as UNDEF1, null as UNDEF2, null as UNDEF3, null as REMARKS, null as PROCEDURE_TYPE limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1617 */     return this.getProcedures.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getSuperTables(String c, String s, String t) throws SQLException {
/* 1625 */     if (this.getSuperTables == null) {
/* 1626 */       this
/* 1627 */         .getSuperTables = this.conn.prepareStatement("select null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as SUPERTABLE_NAME limit 0;");
/*      */     }
/*      */ 
/*      */     
/* 1631 */     return this.getSuperTables.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getSuperTypes(String c, String s, String t) throws SQLException {
/* 1639 */     if (this.getSuperTypes == null) {
/* 1640 */       this
/* 1641 */         .getSuperTypes = this.conn.prepareStatement("select null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME, null as SUPERTYPE_CAT, null as SUPERTYPE_SCHEM, null as SUPERTYPE_NAME limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1646 */     return this.getSuperTypes.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getTablePrivileges(String c, String s, String t) throws SQLException {
/* 1654 */     if (this.getTablePrivileges == null) {
/* 1655 */       this
/* 1656 */         .getTablePrivileges = this.conn.prepareStatement("select  null as TABLE_CAT, null as TABLE_SCHEM, null as TABLE_NAME, null as GRANTOR, null GRANTEE,  null as PRIVILEGE, null as IS_GRANTABLE limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1661 */     return this.getTablePrivileges.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public synchronized ResultSet getTables(String c, String s, String tblNamePattern, String[] types) throws SQLException {
/* 1671 */     checkOpen();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1676 */     tblNamePattern = (tblNamePattern == null || "".equals(tblNamePattern)) ? "%" : escape(tblNamePattern);
/*      */     
/* 1678 */     StringBuilder sql = new StringBuilder();
/* 1679 */     sql.append("SELECT").append("\n");
/* 1680 */     sql.append("  NULL AS TABLE_CAT,").append("\n");
/* 1681 */     sql.append("  NULL AS TABLE_SCHEM,").append("\n");
/* 1682 */     sql.append("  NAME AS TABLE_NAME,").append("\n");
/* 1683 */     sql.append("  TYPE AS TABLE_TYPE,").append("\n");
/* 1684 */     sql.append("  NULL AS REMARKS,").append("\n");
/* 1685 */     sql.append("  NULL AS TYPE_CAT,").append("\n");
/* 1686 */     sql.append("  NULL AS TYPE_SCHEM,").append("\n");
/* 1687 */     sql.append("  NULL AS TYPE_NAME,").append("\n");
/* 1688 */     sql.append("  NULL AS SELF_REFERENCING_COL_NAME,").append("\n");
/* 1689 */     sql.append("  NULL AS REF_GENERATION").append("\n");
/* 1690 */     sql.append("FROM").append("\n");
/* 1691 */     sql.append("  (").append("\n");
/* 1692 */     sql.append("    SELECT").append("\n");
/* 1693 */     sql.append("      NAME,").append("\n");
/* 1694 */     sql.append("      UPPER(TYPE) AS TYPE").append("\n");
/* 1695 */     sql.append("    FROM").append("\n");
/* 1696 */     sql.append("      sqlite_master").append("\n");
/* 1697 */     sql.append("    WHERE").append("\n");
/* 1698 */     sql.append("      NAME NOT LIKE 'sqlite\\_%' ESCAPE '\\'").append("\n");
/* 1699 */     sql.append("      AND UPPER(TYPE) IN ('TABLE', 'VIEW')").append("\n");
/* 1700 */     sql.append("    UNION ALL").append("\n");
/* 1701 */     sql.append("    SELECT").append("\n");
/* 1702 */     sql.append("      NAME,").append("\n");
/* 1703 */     sql.append("      'GLOBAL TEMPORARY' AS TYPE").append("\n");
/* 1704 */     sql.append("    FROM").append("\n");
/* 1705 */     sql.append("      sqlite_temp_master").append("\n");
/* 1706 */     sql.append("    UNION ALL").append("\n");
/* 1707 */     sql.append("    SELECT").append("\n");
/* 1708 */     sql.append("      NAME,").append("\n");
/* 1709 */     sql.append("      'SYSTEM TABLE' AS TYPE").append("\n");
/* 1710 */     sql.append("    FROM").append("\n");
/* 1711 */     sql.append("      sqlite_master").append("\n");
/* 1712 */     sql.append("    WHERE").append("\n");
/* 1713 */     sql.append("      NAME LIKE 'sqlite\\_%' ESCAPE '\\'").append("\n");
/* 1714 */     sql.append("  )").append("\n");
/* 1715 */     sql.append(" WHERE TABLE_NAME LIKE '")
/* 1716 */       .append(tblNamePattern)
/* 1717 */       .append("' AND TABLE_TYPE IN (");
/*      */     
/* 1719 */     if (types == null || types.length == 0) {
/* 1720 */       sql.append("'TABLE','VIEW'");
/*      */     } else {
/* 1722 */       sql.append("'").append(types[0].toUpperCase()).append("'");
/*      */       
/* 1724 */       for (int i = 1; i < types.length; i++) {
/* 1725 */         sql.append(",'").append(types[i].toUpperCase()).append("'");
/*      */       }
/*      */     } 
/*      */     
/* 1729 */     sql.append(") ORDER BY TABLE_TYPE, TABLE_NAME;");
/*      */     
/* 1731 */     return ((CoreStatement)this.conn.createStatement()).executeQuery(sql.toString(), true);
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet getTableTypes() throws SQLException {
/* 1736 */     checkOpen();
/*      */     
/* 1738 */     String sql = "SELECT 'TABLE' AS TABLE_TYPE UNION SELECT 'VIEW' AS TABLE_TYPE UNION SELECT 'SYSTEM TABLE' AS TABLE_TYPE UNION SELECT 'GLOBAL TEMPORARY' AS TABLE_TYPE;";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1747 */     if (this.getTableTypes == null) {
/* 1748 */       this.getTableTypes = this.conn.prepareStatement(sql);
/*      */     }
/* 1750 */     this.getTableTypes.clearParameters();
/* 1751 */     return this.getTableTypes.executeQuery();
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet getTypeInfo() throws SQLException {
/* 1756 */     if (this.getTypeInfo == null) {
/* 1757 */       this
/* 1758 */         .getTypeInfo = this.conn.prepareStatement("select tn as TYPE_NAME, dt as DATA_TYPE, 0 as PRECISION, null as LITERAL_PREFIX, null as LITERAL_SUFFIX, null as CREATE_PARAMS, 1 as NULLABLE, 1 as CASE_SENSITIVE, 3 as SEARCHABLE, 0 as UNSIGNED_ATTRIBUTE, 0 as FIXED_PREC_SCALE, 0 as AUTO_INCREMENT, null as LOCAL_TYPE_NAME, 0 as MINIMUM_SCALE, 0 as MAXIMUM_SCALE, 0 as SQL_DATA_TYPE, 0 as SQL_DATETIME_SUB, 10 as NUM_PREC_RADIX from (    select 'BLOB' as tn, 2004 as dt union    select 'NULL' as tn, 0 as dt union    select 'REAL' as tn, 7 as dt union    select 'TEXT' as tn, 12 as dt union    select 'INTEGER' as tn, 4 as dt) order by TYPE_NAME;");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1798 */     this.getTypeInfo.clearParameters();
/* 1799 */     return this.getTypeInfo.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getUDTs(String c, String s, String t, int[] types) throws SQLException {
/* 1807 */     if (this.getUDTs == null) {
/* 1808 */       this
/* 1809 */         .getUDTs = this.conn.prepareStatement("select  null as TYPE_CAT, null as TYPE_SCHEM, null as TYPE_NAME,  null as CLASS_NAME,  null as DATA_TYPE, null as REMARKS, null as BASE_TYPE limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1816 */     this.getUDTs.clearParameters();
/* 1817 */     return this.getUDTs.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getVersionColumns(String c, String s, String t) throws SQLException {
/* 1825 */     if (this.getVersionColumns == null) {
/* 1826 */       this
/* 1827 */         .getVersionColumns = this.conn.prepareStatement("select null as SCOPE, null as COLUMN_NAME, null as DATA_TYPE, null as TYPE_NAME, null as COLUMN_SIZE, null as BUFFER_LENGTH, null as DECIMAL_DIGITS, null as PSEUDO_COLUMN limit 0;");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1832 */     return this.getVersionColumns.executeQuery();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getGeneratedKeys() throws SQLException {
/* 1840 */     if (this.getGeneratedKeys == null) {
/* 1841 */       this.getGeneratedKeys = this.conn.prepareStatement("select last_insert_rowid();");
/*      */     }
/*      */     
/* 1844 */     return this.getGeneratedKeys.executeQuery();
/*      */   }
/*      */ 
/*      */   
/*      */   public Struct createStruct(String t, Object[] attr) throws SQLException {
/* 1849 */     throw new SQLException("Not yet implemented by SQLite JDBC driver");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public ResultSet getFunctionColumns(String a, String b, String c, String d) throws SQLException {
/* 1855 */     throw new SQLException("Not yet implemented by SQLite JDBC driver");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1862 */   protected static final Pattern PK_UNNAMED_PATTERN = Pattern.compile(".*PRIMARY\\s+KEY\\s*\\((.*?)\\).*", 34);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 1867 */   protected static final Pattern PK_NAMED_PATTERN = Pattern.compile(".*CONSTRAINT\\s*(.*?)\\s*PRIMARY\\s+KEY\\s*\\((.*?)\\).*", 34);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   class PrimaryKeyFinder
/*      */   {
/*      */     String table;
/*      */ 
/*      */     
/* 1877 */     String pkName = null;
/*      */ 
/*      */     
/* 1880 */     String[] pkColumns = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public PrimaryKeyFinder(String table) throws SQLException {
/* 1889 */       this.table = table;
/*      */       
/* 1891 */       if (table == null || table.trim().length() == 0) {
/* 1892 */         throw new SQLException("Invalid table name: '" + this.table + "'");
/*      */       }
/*      */       
/* 1895 */       try(Statement stat = JDBC3DatabaseMetaData.this.conn.createStatement(); 
/*      */ 
/*      */           
/* 1898 */           ResultSet rs = stat.executeQuery("select sql from sqlite_master where lower(name) = lower('" + JDBC3DatabaseMetaData.this
/*      */ 
/*      */             
/* 1901 */             .escape(table) + "') and type in ('table', 'view')")) {
/*      */ 
/*      */         
/* 1904 */         if (!rs.next()) throw new SQLException("Table not found: '" + table + "'");
/*      */         
/* 1906 */         Matcher matcher = JDBC3DatabaseMetaData.PK_NAMED_PATTERN.matcher(rs.getString(1));
/* 1907 */         if (matcher.find()) {
/* 1908 */           this.pkName = JDBC3DatabaseMetaData.this.unquoteIdentifier(JDBC3DatabaseMetaData.this.escape(matcher.group(1)));
/* 1909 */           this.pkColumns = matcher.group(2).split(",");
/*      */         } else {
/* 1911 */           matcher = JDBC3DatabaseMetaData.PK_UNNAMED_PATTERN.matcher(rs.getString(1));
/* 1912 */           if (matcher.find()) {
/* 1913 */             this.pkColumns = matcher.group(1).split(",");
/*      */           }
/*      */         } 
/*      */         
/* 1917 */         if (this.pkColumns == null)
/*      */         {
/* 1919 */           try (ResultSet rs2 = stat.executeQuery("pragma table_info('" + JDBC3DatabaseMetaData.this.escape(table) + "');")) {
/* 1920 */             while (rs2.next()) {
/* 1921 */               if (rs2.getBoolean(6)) this.pkColumns = new String[] { rs2.getString(2) };
/*      */             
/*      */             } 
/*      */           } 
/*      */         }
/* 1926 */         if (this.pkColumns != null) {
/* 1927 */           for (int i = 0; i < this.pkColumns.length; i++) {
/* 1928 */             this.pkColumns[i] = JDBC3DatabaseMetaData.this.unquoteIdentifier(this.pkColumns[i]);
/*      */           }
/*      */         }
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public String getName() {
/* 1936 */       return this.pkName;
/*      */     }
/*      */ 
/*      */     
/*      */     public String[] getColumns() {
/* 1941 */       return this.pkColumns;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   class ImportedKeyFinder
/*      */   {
/* 1949 */     private final Pattern FK_NAMED_PATTERN = Pattern.compile("CONSTRAINT\\s*([A-Za-z_][A-Za-z\\d_]*)?\\s*FOREIGN\\s+KEY\\s*\\((.*?)\\)", 34);
/*      */ 
/*      */     
/*      */     private final String fkTableName;
/*      */     
/* 1954 */     private final List<ForeignKey> fkList = new ArrayList<>();
/*      */ 
/*      */     
/*      */     public ImportedKeyFinder(String table) throws SQLException {
/* 1958 */       if (table == null || table.trim().length() == 0) {
/* 1959 */         throw new SQLException("Invalid table name: '" + table + "'");
/*      */       }
/*      */       
/* 1962 */       this.fkTableName = table;
/*      */       
/* 1964 */       List<String> fkNames = getForeignKeyNames(this.fkTableName);
/*      */       
/* 1966 */       try(Statement stat = JDBC3DatabaseMetaData.this.conn.createStatement(); 
/*      */           
/* 1968 */           ResultSet rs = stat.executeQuery("pragma foreign_key_list('" + JDBC3DatabaseMetaData.this
/*      */             
/* 1970 */             .escape(this.fkTableName.toLowerCase()) + "')")) {
/*      */ 
/*      */         
/* 1973 */         int prevFkId = -1;
/* 1974 */         int count = 0;
/* 1975 */         ForeignKey fk = null;
/* 1976 */         while (rs.next()) {
/* 1977 */           int fkId = rs.getInt(1);
/* 1978 */           String pkTableName = rs.getString(3);
/* 1979 */           String fkColName = rs.getString(4);
/* 1980 */           String pkColName = rs.getString(5);
/* 1981 */           String onUpdate = rs.getString(6);
/* 1982 */           String onDelete = rs.getString(7);
/* 1983 */           String match = rs.getString(8);
/*      */           
/* 1985 */           String fkName = null;
/* 1986 */           if (fkNames.size() > count) fkName = fkNames.get(count);
/*      */           
/* 1988 */           if (fkId != prevFkId) {
/* 1989 */             fk = new ForeignKey(fkName, pkTableName, this.fkTableName, onUpdate, onDelete, match);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1997 */             this.fkList.add(fk);
/* 1998 */             prevFkId = fkId;
/* 1999 */             count++;
/*      */           } 
/* 2001 */           if (fk != null) {
/* 2002 */             fk.addColumnMapping(fkColName, pkColName);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     private List<String> getForeignKeyNames(String tbl) throws SQLException {
/* 2009 */       List<String> fkNames = new ArrayList<>();
/* 2010 */       if (tbl == null) {
/* 2011 */         return fkNames;
/*      */       }
/* 2013 */       try(Statement stat2 = JDBC3DatabaseMetaData.this.conn.createStatement(); 
/*      */           
/* 2015 */           ResultSet rs = stat2.executeQuery("select sql from sqlite_master where lower(name) = lower('" + JDBC3DatabaseMetaData.this
/*      */ 
/*      */             
/* 2018 */             .escape(tbl) + "')")) {
/*      */ 
/*      */         
/* 2021 */         if (rs.next()) {
/* 2022 */           Matcher matcher = this.FK_NAMED_PATTERN.matcher(rs.getString(1));
/*      */           
/* 2024 */           while (matcher.find()) {
/* 2025 */             fkNames.add(matcher.group(1));
/*      */           }
/*      */         } 
/*      */       } 
/* 2029 */       Collections.reverse(fkNames);
/* 2030 */       return fkNames;
/*      */     }
/*      */     
/*      */     public String getFkTableName() {
/* 2034 */       return this.fkTableName;
/*      */     }
/*      */     
/*      */     public List<ForeignKey> getFkList() {
/* 2038 */       return this.fkList;
/*      */     }
/*      */     
/*      */     class ForeignKey
/*      */     {
/*      */       private final String fkName;
/*      */       private final String pkTableName;
/*      */       private final String fkTableName;
/* 2046 */       private final List<String> fkColNames = new ArrayList<>();
/* 2047 */       private final List<String> pkColNames = new ArrayList<>();
/*      */ 
/*      */       
/*      */       private final String onUpdate;
/*      */ 
/*      */       
/*      */       private final String onDelete;
/*      */       
/*      */       private final String match;
/*      */ 
/*      */       
/*      */       ForeignKey(String fkName, String pkTableName, String fkTableName, String onUpdate, String onDelete, String match) {
/* 2059 */         this.fkName = fkName;
/* 2060 */         this.pkTableName = pkTableName;
/* 2061 */         this.fkTableName = fkTableName;
/* 2062 */         this.onUpdate = onUpdate;
/* 2063 */         this.onDelete = onDelete;
/* 2064 */         this.match = match;
/*      */       }
/*      */       
/*      */       public String getFkName() {
/* 2068 */         return this.fkName;
/*      */       }
/*      */       
/*      */       void addColumnMapping(String fkColName, String pkColName) {
/* 2072 */         this.fkColNames.add(fkColName);
/* 2073 */         this.pkColNames.add(pkColName);
/*      */       }
/*      */       
/*      */       public String[] getColumnMapping(int colSeq) {
/* 2077 */         return new String[] { this.fkColNames.get(colSeq), this.pkColNames.get(colSeq) };
/*      */       }
/*      */       
/*      */       public int getColumnMappingCount() {
/* 2081 */         return this.fkColNames.size();
/*      */       }
/*      */       
/*      */       public String getPkTableName() {
/* 2085 */         return this.pkTableName;
/*      */       }
/*      */       
/*      */       public String getFkTableName() {
/* 2089 */         return this.fkTableName;
/*      */       }
/*      */       
/*      */       public String getOnUpdate() {
/* 2093 */         return this.onUpdate;
/*      */       }
/*      */       
/*      */       public String getOnDelete() {
/* 2097 */         return this.onDelete;
/*      */       }
/*      */       
/*      */       public String getMatch() {
/* 2101 */         return this.match;
/*      */       }
/*      */       
/*      */       public String toString()
/*      */       {
/* 2106 */         return "ForeignKey [fkName=" + this.fkName + ", pkTableName=" + this.pkTableName + ", fkTableName=" + this.fkTableName + ", pkColNames=" + this.pkColNames + ", fkColNames=" + this.fkColNames + "]"; } } } class ForeignKey { private final String fkName; private final String pkTableName; private final String fkTableName; private final List<String> fkColNames = new ArrayList<>(); private final List<String> pkColNames = new ArrayList<>(); private final String onUpdate; private final String onDelete; private final String match; ForeignKey(String fkName, String pkTableName, String fkTableName, String onUpdate, String onDelete, String match) { this.fkName = fkName; this.pkTableName = pkTableName; this.fkTableName = fkTableName; this.onUpdate = onUpdate; this.onDelete = onDelete; this.match = match; } public String getFkName() { return this.fkName; } void addColumnMapping(String fkColName, String pkColName) { this.fkColNames.add(fkColName); this.pkColNames.add(pkColName); } public String[] getColumnMapping(int colSeq) { return new String[] { this.fkColNames.get(colSeq), this.pkColNames.get(colSeq) }; } public int getColumnMappingCount() { return this.fkColNames.size(); } public String getPkTableName() { return this.pkTableName; } public String toString() { return "ForeignKey [fkName=" + this.fkName + ", pkTableName=" + this.pkTableName + ", fkTableName=" + this.fkTableName + ", pkColNames=" + this.pkColNames + ", fkColNames=" + this.fkColNames + "]"; }
/*      */     
/*      */     public String getFkTableName() {
/*      */       return this.fkTableName;
/*      */     }
/*      */     public String getOnUpdate() {
/*      */       return this.onUpdate;
/*      */     }
/*      */     public String getOnDelete() {
/*      */       return this.onDelete;
/*      */     }
/*      */     
/*      */     public String getMatch() {
/*      */       return this.match;
/*      */     } }
/*      */   
/*      */   protected void finalize() throws Throwable {
/* 2123 */     close();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String unquoteIdentifier(String name) {
/* 2133 */     if (name == null) return name; 
/* 2134 */     name = name.trim();
/* 2135 */     if (name.length() > 2 && ((name
/* 2136 */       .startsWith("`") && name.endsWith("`")) || (name
/* 2137 */       .startsWith("\"") && name.endsWith("\"")) || (name
/* 2138 */       .startsWith("[") && name.endsWith("]"))))
/*      */     {
/* 2140 */       name = name.substring(1, name.length() - 1);
/*      */     }
/* 2142 */     return name;
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\jdbc3\JDBC3DatabaseMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */