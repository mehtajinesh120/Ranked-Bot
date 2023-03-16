/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.Socket;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLParameters;
/*     */ import javax.net.ssl.SSLSocket;
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
/*     */ class SNIHelper
/*     */ {
/*     */   private static Constructor<?> sSNIHostNameConstructor;
/*     */   private static Method sSetServerNamesMethod;
/*     */   
/*     */   static {
/*     */     try {
/*  38 */       initialize();
/*     */     }
/*  40 */     catch (Exception e) {
/*     */       
/*  42 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void initialize() throws Exception {
/*  51 */     sSNIHostNameConstructor = Misc.getConstructor("javax.net.ssl.SNIHostName", new Class[] { String.class });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  56 */     sSetServerNamesMethod = Misc.getMethod("javax.net.ssl.SSLParameters", "setServerNames", new Class[] { List.class });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Object createSNIHostName(String hostname) {
/*  64 */     return Misc.newInstance(sSNIHostNameConstructor, new Object[] { hostname });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<Object> createSNIHostNames(String[] hostnames) {
/*  70 */     List<Object> list = new ArrayList(hostnames.length);
/*     */ 
/*     */     
/*  73 */     for (String hostname : hostnames)
/*     */     {
/*     */       
/*  76 */       list.add(createSNIHostName(hostname));
/*     */     }
/*     */     
/*  79 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void setServerNames(SSLParameters parameters, String[] hostnames) {
/*  86 */     Misc.invoke(sSetServerNamesMethod, parameters, new Object[] { createSNIHostNames(hostnames) });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void setServerNames(Socket socket, String[] hostnames) {
/*  92 */     if (!(socket instanceof SSLSocket)) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  97 */     if (hostnames == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 104 */     int androidSDKVersion = getAndroidSDKVersion();
/* 105 */     if (androidSDKVersion > 0 && androidSDKVersion < 24) {
/*     */ 
/*     */       
/*     */       try {
/* 109 */         Method method = socket.getClass().getMethod("setHostname", new Class[] { String.class });
/* 110 */         method.invoke(socket, new Object[] { hostnames[0] });
/*     */       }
/* 112 */       catch (Exception e) {
/*     */         
/* 114 */         System.err.println("SNI configuration failed: " + e.getMessage());
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 119 */     SSLParameters parameters = ((SSLSocket)socket).getSSLParameters();
/* 120 */     if (parameters == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 126 */     setServerNames(parameters, hostnames);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getAndroidSDKVersion() {
/*     */     try {
/* 133 */       return Class.forName("android.os.Build$VERSION").getField("SDK_INT").getInt(null);
/*     */     }
/* 135 */     catch (Exception ex) {
/*     */ 
/*     */       
/*     */       try {
/* 139 */         return Integer.parseInt((String)Class.forName("android.os.Build$VERSION").getField("SDK").get(null));
/*     */       }
/* 141 */       catch (Exception ex1) {
/*     */         
/* 143 */         return 0;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\SNIHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */