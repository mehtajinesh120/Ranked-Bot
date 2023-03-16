/*    */ package org.jsoup.helper;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.HttpURLConnection;
/*    */ import java.net.MalformedURLException;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.net.URL;
/*    */ import java.util.Collections;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.jsoup.Connection;
/*    */ import org.jsoup.internal.StringUtil;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CookieUtil
/*    */ {
/* 26 */   private static final Map<String, List<String>> EmptyRequestHeaders = Collections.unmodifiableMap(new HashMap<>());
/*    */ 
/*    */   
/*    */   private static final String Sep = "; ";
/*    */   
/*    */   private static final String CookieName = "Cookie";
/*    */   
/*    */   private static final String Cookie2Name = "Cookie2";
/*    */ 
/*    */   
/*    */   static void applyCookiesToRequest(HttpConnection.Request req, HttpURLConnection con) throws IOException {
/* 37 */     Set<String> cookieSet = requestCookieSet(req);
/* 38 */     Set<String> cookies2 = null;
/*    */ 
/*    */     
/* 41 */     Map<String, List<String>> storedCookies = req.cookieManager().get(asUri(req.url), EmptyRequestHeaders);
/* 42 */     for (Map.Entry<String, List<String>> entry : storedCookies.entrySet()) {
/*    */       Set<String> set;
/* 44 */       List<String> cookies = entry.getValue();
/* 45 */       if (cookies == null || cookies.size() == 0) {
/*    */         continue;
/*    */       }
/* 48 */       String key = entry.getKey();
/*    */       
/* 50 */       if ("Cookie".equals(key)) {
/* 51 */         set = cookieSet;
/* 52 */       } else if ("Cookie2".equals(key)) {
/* 53 */         set = new HashSet<>();
/* 54 */         cookies2 = set;
/*    */       } else {
/*    */         continue;
/*    */       } 
/* 58 */       set.addAll(cookies);
/*    */     } 
/*    */     
/* 61 */     if (cookieSet.size() > 0)
/* 62 */       con.addRequestProperty("Cookie", StringUtil.join(cookieSet, "; ")); 
/* 63 */     if (cookies2 != null && cookies2.size() > 0)
/* 64 */       con.addRequestProperty("Cookie2", StringUtil.join(cookies2, "; ")); 
/*    */   }
/*    */   
/*    */   private static LinkedHashSet<String> requestCookieSet(Connection.Request req) {
/* 68 */     LinkedHashSet<String> set = new LinkedHashSet<>();
/*    */     
/* 70 */     for (Map.Entry<String, String> cookie : (Iterable<Map.Entry<String, String>>)req.cookies().entrySet()) {
/* 71 */       set.add((String)cookie.getKey() + "=" + (String)cookie.getValue());
/*    */     }
/* 73 */     return set;
/*    */   }
/*    */   
/*    */   static URI asUri(URL url) throws IOException {
/*    */     try {
/* 78 */       return url.toURI();
/* 79 */     } catch (URISyntaxException e) {
/* 80 */       MalformedURLException ue = new MalformedURLException(e.getMessage());
/* 81 */       ue.initCause(e);
/* 82 */       throw ue;
/*    */     } 
/*    */   }
/*    */   
/*    */   static void storeCookies(HttpConnection.Request req, URL url, Map<String, List<String>> resHeaders) throws IOException {
/* 87 */     req.cookieManager().put(asUri(url), resHeaders);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\helper\CookieUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */