/*     */ package org.sqlite;
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
/*     */ 
/*     */ 
/*     */ public enum SQLiteErrorCode
/*     */ {
/*  35 */   UNKNOWN_ERROR(-1, "unknown error"),
/*  36 */   SQLITE_OK(0, "Successful result"),
/*     */   
/*  38 */   SQLITE_ERROR(1, "SQL error or missing database"),
/*  39 */   SQLITE_INTERNAL(2, "Internal logic error in SQLite"),
/*  40 */   SQLITE_PERM(3, "Access permission denied"),
/*  41 */   SQLITE_ABORT(4, "Callback routine requested an abort"),
/*  42 */   SQLITE_BUSY(5, "The database file is locked"),
/*  43 */   SQLITE_LOCKED(6, "A table in the database is locked"),
/*  44 */   SQLITE_NOMEM(7, "A malloc() failed"),
/*  45 */   SQLITE_READONLY(8, "Attempt to write a readonly database"),
/*  46 */   SQLITE_INTERRUPT(9, "Operation terminated by sqlite3_interrupt()"),
/*  47 */   SQLITE_IOERR(10, "Some kind of disk I/O error occurred"),
/*  48 */   SQLITE_CORRUPT(11, "The database disk image is malformed"),
/*  49 */   SQLITE_NOTFOUND(12, "NOT USED. Table or record not found"),
/*  50 */   SQLITE_FULL(13, "Insertion failed because database is full"),
/*  51 */   SQLITE_CANTOPEN(14, "Unable to open the database file"),
/*  52 */   SQLITE_PROTOCOL(15, "NOT USED. Database lock protocol error"),
/*  53 */   SQLITE_EMPTY(16, "Database is empty"),
/*  54 */   SQLITE_SCHEMA(17, "The database schema changed"),
/*  55 */   SQLITE_TOOBIG(18, "String or BLOB exceeds size limit"),
/*  56 */   SQLITE_CONSTRAINT(19, "Abort due to constraint violation"),
/*  57 */   SQLITE_MISMATCH(20, "Data type mismatch"),
/*  58 */   SQLITE_MISUSE(21, "Library used incorrectly"),
/*  59 */   SQLITE_NOLFS(22, "Uses OS features not supported on host"),
/*  60 */   SQLITE_AUTH(23, "Authorization denied"),
/*  61 */   SQLITE_FORMAT(24, "Auxiliary database format error"),
/*  62 */   SQLITE_RANGE(25, "2nd parameter to sqlite3_bind out of range"),
/*  63 */   SQLITE_NOTADB(26, "File opened that is not a database file"),
/*  64 */   SQLITE_NOTICE(27, "Notifications from sqlite3_log()"),
/*  65 */   SQLITE_WARNING(28, "Warnings from sqlite3_log()"),
/*  66 */   SQLITE_ROW(100, "sqlite3_step() has another row ready"),
/*  67 */   SQLITE_DONE(101, "sqlite3_step() has finished executing"),
/*     */   
/*  69 */   SQLITE_ABORT_ROLLBACK(516, "The transaction that was active when the SQL statement first started was rolled back"),
/*     */ 
/*     */   
/*  72 */   SQLITE_AUTH_USER(279, "An operation was attempted on a database for which the logged in user lacks sufficient authorization"),
/*     */ 
/*     */   
/*  75 */   SQLITE_BUSY_RECOVERY(261, "Another process is busy recovering a WAL mode database file following a crash"),
/*     */   
/*  77 */   SQLITE_BUSY_SNAPSHOT(517, "Another database connection has already written to the database"),
/*  78 */   SQLITE_BUSY_TIMEOUT(773, "A blocking Posix advisory file lock request in the VFS layer failed due to a timeout"),
/*     */ 
/*     */   
/*  81 */   SQLITE_CANTOPEN_CONVPATH(1038, "cygwin_conv_path() system call failed while trying to open a file"),
/*     */   
/*  83 */   SQLITE_CANTOPEN_DIRTYWAL(1294, "Not used"),
/*  84 */   SQLITE_CANTOPEN_FULLPATH(782, "The operating system was unable to convert the filename into a full pathname"),
/*     */   
/*  86 */   SQLITE_CANTOPEN_ISDIR(526, "The file is really a directory"),
/*  87 */   SQLITE_CANTOPEN_NOTEMPDIR(270, "No longer used"),
/*  88 */   SQLITE_CANTOPEN_SYMLINK(1550, "The file is a symbolic link but SQLITE_OPEN_NOFOLLOW flag is used"),
/*     */   
/*  90 */   SQLITE_CONSTRAINT_CHECK(275, "A CHECK constraint failed"),
/*  91 */   SQLITE_CONSTRAINT_COMMITHOOK(531, "A commit hook callback returned non-zero"),
/*  92 */   SQLITE_CONSTRAINT_DATATYPE(3091, "An insert or update attempted to store a value inconsistent with the column's declared type in a table defined as STRICT"),
/*     */ 
/*     */   
/*  95 */   SQLITE_CONSTRAINT_FOREIGNKEY(787, "A foreign key constraint failed"),
/*  96 */   SQLITE_CONSTRAINT_FUNCTION(1043, "Error reported by extension function"),
/*  97 */   SQLITE_CONSTRAINT_NOTNULL(1299, "A NOT NULL constraint failed"),
/*  98 */   SQLITE_CONSTRAINT_PINNED(2835, "An UPDATE trigger attempted to delete the row that was being updated in the middle of the update"),
/*     */ 
/*     */   
/* 101 */   SQLITE_CONSTRAINT_PRIMARYKEY(1555, "A PRIMARY KEY constraint failed"),
/* 102 */   SQLITE_CONSTRAINT_ROWID(2579, "rowid is not unique"),
/* 103 */   SQLITE_CONSTRAINT_TRIGGER(1811, "A RAISE function within a trigger fired, causing the SQL statement to abort"),
/*     */   
/* 105 */   SQLITE_CONSTRAINT_UNIQUE(2067, "A UNIQUE constraint failed"),
/* 106 */   SQLITE_CONSTRAINT_VTAB(2323, "Error reported by application-defined virtual table"),
/* 107 */   SQLITE_CORRUPT_INDEX(779, "SQLite detected an entry is or was missing from an index"),
/* 108 */   SQLITE_CORRUPT_SEQUENCE(523, "the schema of the sqlite_sequence table is corrupt"),
/* 109 */   SQLITE_CORRUPT_VTAB(267, "Content in the virtual table is corrupt"),
/* 110 */   SQLITE_ERROR_MISSING_COLLSEQ(257, "An SQL statement could not be prepared because a collating sequence named in that SQL statement could not be located"),
/*     */ 
/*     */   
/* 113 */   SQLITE_ERROR_RETRY(513, "used internally"),
/* 114 */   SQLITE_ERROR_SNAPSHOT(769, "the historical snapshot is no longer available"),
/* 115 */   SQLITE_IOERR_ACCESS(3338, "I/O error within the xAccess"),
/* 116 */   SQLITE_IOERR_AUTH(7178, "reserved for use by extensions"),
/* 117 */   SQLITE_IOERR_BEGIN_ATOMIC(7434, "the underlying operating system reported and error on the SQLITE_FCNTL_BEGIN_ATOMIC_WRITE file-control"),
/*     */ 
/*     */   
/* 120 */   SQLITE_IOERR_BLOCKED(2826, "no longer used"),
/* 121 */   SQLITE_IOERR_CHECKRESERVEDLOCK(3594, "I/O error within xCheckReservedLock"),
/* 122 */   SQLITE_IOERR_CLOSE(4106, "I/O error within xClose"),
/* 123 */   SQLITE_IOERR_COMMIT_ATOMIC(7690, "the underlying operating system reported and error on the SQLITE_FCNTL_COMMIT_ATOMIC_WRITE file-control"),
/*     */ 
/*     */   
/* 126 */   SQLITE_IOERR_CONVPATH(6666, "cygwin_conv_path() system call failed"),
/* 127 */   SQLITE_IOERR_CORRUPTFS(8458, "I/O error in the VFS layer, a seek or read failure was due to the request not falling within the file's boundary rather than an ordinary device failure"),
/*     */ 
/*     */   
/* 130 */   SQLITE_IOERR_DATA(8202, "I/O error in the VFS shim, the checksum on a page of the database file is incorrect"),
/*     */ 
/*     */   
/* 133 */   SQLITE_IOERR_DELETE(2570, "I/O error within xDelete"),
/* 134 */   SQLITE_IOERR_DELETE_NOENT(5898, "The file being deleted does not exist"),
/* 135 */   SQLITE_IOERR_DIR_CLOSE(4362, "no longer used"),
/* 136 */   SQLITE_IOERR_DIR_FSYNC(1290, "I/O error in the VFS layer while trying to invoke fsync() on a directory"),
/*     */   
/* 138 */   SQLITE_IOERR_FSTAT(1802, "I/O error in the VFS layer while trying to invoke fstat()"),
/* 139 */   SQLITE_IOERR_FSYNC(1034, "I/O error in the VFS layer while trying to flush previously written content"),
/*     */   
/* 141 */   SQLITE_IOERR_GETTEMPPATH(6410, "Unable to determine a suitable directory in which to place temporary files"),
/*     */   
/* 143 */   SQLITE_IOERR_LOCK(3850, "I/O error in the advisory file locking logic"),
/* 144 */   SQLITE_IOERR_MMAP(6154, "I/O error while trying to map or unmap part of the database file"),
/* 145 */   SQLITE_IOERR_NOMEM(3082, "Unable to allocate sufficient memory"),
/* 146 */   SQLITE_IOERR_RDLOCK(2314, "I/O error within xLock"),
/* 147 */   SQLITE_IOERR_READ(266, "I/O error in the VFS layer while trying to read from a file on disk"),
/* 148 */   SQLITE_IOERR_ROLLBACK_ATOMIC(7946, "the underlying operating system reported and error on the SQLITE_FCNTL_ROLLBACK_ATOMIC_WRITE file-control"),
/*     */ 
/*     */   
/* 151 */   SQLITE_IOERR_SEEK(5642, "I/O error while trying to seek a file descriptor"),
/* 152 */   SQLITE_IOERR_SHMLOCK(5130, "no longer used"),
/* 153 */   SQLITE_IOERR_SHMMAP(5386, "I/O error within xShmMap while trying to map a shared memory segment"),
/*     */   
/* 155 */   SQLITE_IOERR_SHMOPEN(4618, "I/O error within xShmMap while trying to open a new shared memory segment"),
/*     */   
/* 157 */   SQLITE_IOERR_SHMSIZE(4874, "I/O error within xShmMap while trying to resize an existing shared memory segment"),
/*     */ 
/*     */   
/* 160 */   SQLITE_IOERR_SHORT_READ(522, "The VFS layer was unable to obtain as many bytes as was requested"),
/*     */   
/* 162 */   SQLITE_IOERR_TRUNCATE(1546, "I/O error in the VFS layer while trying to truncate a file to a smaller size"),
/*     */   
/* 164 */   SQLITE_IOERR_UNLOCK(2058, "I/O error within xUnlock"),
/* 165 */   SQLITE_IOERR_VNODE(6922, "reserved for use by extensions"),
/* 166 */   SQLITE_IOERR_WRITE(778, "I/O error in the VFS layer while trying to write to a file on disk"),
/* 167 */   SQLITE_LOCKED_SHAREDCACHE(262, "Contention with a different database connection that shares the cache"),
/*     */   
/* 169 */   SQLITE_LOCKED_VTAB(518, "reserved for use by extensions"),
/* 170 */   SQLITE_NOTICE_RECOVER_ROLLBACK(539, "a hot journal is rolled back"),
/* 171 */   SQLITE_NOTICE_RECOVER_WAL(283, "a WAL mode database file is recovered"),
/* 172 */   SQLITE_OK_LOAD_PERMANENTLY(256, "the extension remains loaded into the process address space after the database connection closes"),
/*     */ 
/*     */   
/* 175 */   SQLITE_READONLY_CANTINIT(1288, "the current process does not have write permission on the shared memory region"),
/*     */   
/* 177 */   SQLITE_READONLY_CANTLOCK(520, "The shared-memory file associated with that database is read-only"),
/*     */   
/* 179 */   SQLITE_READONLY_DBMOVED(1032, "The database file has been moved since it was opened"),
/* 180 */   SQLITE_READONLY_DIRECTORY(1544, "Process does not have permission to create a journal file in the same directory as the database and the creation of a journal file is a prerequisite for writing"),
/*     */ 
/*     */   
/* 183 */   SQLITE_READONLY_RECOVERY(264, "The database file needs to be recovered"),
/* 184 */   SQLITE_READONLY_ROLLBACK(776, "Hot journal needs to be rolled back"),
/* 185 */   SQLITE_WARNING_AUTOINDEX(284, "automatic indexing is used");
/*     */ 
/*     */ 
/*     */   
/*     */   public final int code;
/*     */ 
/*     */   
/*     */   public final String message;
/*     */ 
/*     */ 
/*     */   
/*     */   SQLiteErrorCode(int code, String message) {
/* 197 */     this.code = code;
/* 198 */     this.message = message;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SQLiteErrorCode getErrorCode(int errorCode) {
/* 206 */     for (SQLiteErrorCode each : values()) {
/* 207 */       if (errorCode == each.code) return each; 
/*     */     } 
/* 209 */     return UNKNOWN_ERROR;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 215 */     return String.format("[%s] %s", new Object[] { name(), this.message });
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\SQLiteErrorCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */