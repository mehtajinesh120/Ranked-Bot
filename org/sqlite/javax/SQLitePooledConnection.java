/*     */ package org.sqlite.javax;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.sql.ConnectionEvent;
/*     */ import javax.sql.ConnectionEventListener;
/*     */ import javax.sql.PooledConnection;
/*     */ import org.sqlite.SQLiteConnection;
/*     */ import org.sqlite.jdbc4.JDBC4PooledConnection;
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
/*     */ 
/*     */ 
/*     */ public class SQLitePooledConnection
/*     */   extends JDBC4PooledConnection
/*     */ {
/*     */   protected SQLiteConnection physicalConn;
/*     */   protected volatile Connection handleConn;
/*  54 */   protected List<ConnectionEventListener> listeners = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected SQLitePooledConnection(SQLiteConnection physicalConn) {
/*  62 */     this.physicalConn = physicalConn;
/*     */   }
/*     */   
/*     */   public SQLiteConnection getPhysicalConn() {
/*  66 */     return this.physicalConn;
/*     */   }
/*     */   
/*     */   public void close() throws SQLException {
/*  70 */     if (this.handleConn != null) {
/*  71 */       this.listeners.clear();
/*  72 */       this.handleConn.close();
/*     */     } 
/*     */     
/*  75 */     if (this.physicalConn != null) {
/*     */       try {
/*  77 */         this.physicalConn.close();
/*     */       } finally {
/*  79 */         this.physicalConn = null;
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/*  86 */     if (this.handleConn != null) this.handleConn.close();
/*     */     
/*  88 */     this
/*     */       
/*  90 */       .handleConn = (Connection)Proxy.newProxyInstance(
/*  91 */         getClass().getClassLoader(), new Class[] { Connection.class }, new InvocationHandler()
/*     */         {
/*     */           boolean isClosed;
/*     */ 
/*     */ 
/*     */           
/*     */           public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/*     */             try {
/*  99 */               String name = method.getName();
/* 100 */               if ("close".equals(name)) {
/* 101 */                 ConnectionEvent event = new ConnectionEvent((PooledConnection)SQLitePooledConnection.this);
/*     */ 
/*     */ 
/*     */                 
/* 105 */                 for (int i = SQLitePooledConnection.this.listeners.size() - 1; i >= 0; i--) {
/* 106 */                   ((ConnectionEventListener)SQLitePooledConnection.this.listeners.get(i)).connectionClosed(event);
/*     */                 }
/*     */                 
/* 109 */                 if (!SQLitePooledConnection.this.physicalConn.getAutoCommit()) {
/* 110 */                   SQLitePooledConnection.this.physicalConn.rollback();
/*     */                 }
/* 112 */                 SQLitePooledConnection.this.physicalConn.setAutoCommit(true);
/* 113 */                 this.isClosed = true;
/*     */                 
/* 115 */                 return null;
/* 116 */               }  if ("isClosed".equals(name)) {
/* 117 */                 if (!this.isClosed) {
/* 118 */                   this
/*     */ 
/*     */ 
/*     */ 
/*     */                     
/* 123 */                     .isClosed = ((Boolean)method.invoke(SQLitePooledConnection.this.physicalConn, args)).booleanValue();
/*     */                 }
/* 125 */                 return Boolean.valueOf(this.isClosed);
/*     */               } 
/*     */               
/* 128 */               if (this.isClosed) {
/* 129 */                 throw new SQLException("Connection is closed");
/*     */               }
/*     */               
/* 132 */               return method.invoke(SQLitePooledConnection.this.physicalConn, args);
/* 133 */             } catch (SQLException e) {
/* 134 */               if ("database connection closed"
/* 135 */                 .equals(e.getMessage())) {
/* 136 */                 ConnectionEvent event = new ConnectionEvent((PooledConnection)SQLitePooledConnection.this, e);
/*     */ 
/*     */ 
/*     */                 
/* 140 */                 for (int i = SQLitePooledConnection.this.listeners.size() - 1; i >= 0; i--) {
/* 141 */                   ((ConnectionEventListener)SQLitePooledConnection.this.listeners.get(i)).connectionErrorOccurred(event);
/*     */                 }
/*     */               } 
/*     */               
/* 145 */               throw e;
/* 146 */             } catch (InvocationTargetException ex) {
/* 147 */               throw ex.getCause();
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 152 */     return this.handleConn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addConnectionEventListener(ConnectionEventListener listener) {
/* 159 */     this.listeners.add(listener);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeConnectionEventListener(ConnectionEventListener listener) {
/* 167 */     this.listeners.remove(listener);
/*     */   }
/*     */   
/*     */   public List<ConnectionEventListener> getListeners() {
/* 171 */     return this.listeners;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\javax\SQLitePooledConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */