/*    */ package org.sqlite;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SQLiteOpenMode
/*    */ {
/* 35 */   READONLY(1),
/* 36 */   READWRITE(2),
/* 37 */   CREATE(4),
/* 38 */   DELETEONCLOSE(8),
/* 39 */   EXCLUSIVE(16),
/* 40 */   OPEN_URI(64),
/* 41 */   OPEN_MEMORY(128),
/* 42 */   MAIN_DB(256),
/* 43 */   TEMP_DB(512),
/* 44 */   TRANSIENT_DB(1024),
/* 45 */   MAIN_JOURNAL(2048),
/* 46 */   TEMP_JOURNAL(4096),
/* 47 */   SUBJOURNAL(8192),
/* 48 */   MASTER_JOURNAL(16384),
/* 49 */   NOMUTEX(32768),
/* 50 */   FULLMUTEX(65536),
/* 51 */   SHAREDCACHE(131072),
/* 52 */   PRIVATECACHE(262144);
/*    */   
/*    */   public final int flag;
/*    */   
/*    */   SQLiteOpenMode(int flag) {
/* 57 */     this.flag = flag;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\SQLiteOpenMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */