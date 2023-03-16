/*    */ package org.jsoup;
/*    */ import java.io.InputStream;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Nullable;
/*    */ public interface Connection { Connection newRequest(); Connection url(URL paramURL); Connection url(String paramString); Connection proxy(@Nullable Proxy paramProxy); Connection proxy(String paramString, int paramInt); Connection userAgent(String paramString); Connection timeout(int paramInt); Connection maxBodySize(int paramInt); Connection referrer(String paramString); Connection followRedirects(boolean paramBoolean); Connection method(Method paramMethod); Connection ignoreHttpErrors(boolean paramBoolean); Connection ignoreContentType(boolean paramBoolean); Connection sslSocketFactory(SSLSocketFactory paramSSLSocketFactory); Connection data(String paramString1, String paramString2); Connection data(String paramString1, String paramString2, InputStream paramInputStream); Connection data(String paramString1, String paramString2, InputStream paramInputStream, String paramString3); Connection data(Collection<KeyVal> paramCollection); Connection data(Map<String, String> paramMap); Connection data(String... paramVarArgs); @Nullable
/*    */   KeyVal data(String paramString); Connection requestBody(String paramString); Connection header(String paramString1, String paramString2); Connection headers(Map<String, String> paramMap); Connection cookie(String paramString1, String paramString2); Connection cookies(Map<String, String> paramMap); Connection cookieStore(CookieStore paramCookieStore); CookieStore cookieStore(); Connection parser(Parser paramParser); Connection postDataCharset(String paramString); Document get() throws IOException; Document post() throws IOException; Response execute() throws IOException; Request request(); Connection request(Request paramRequest); Response response(); Connection response(Response paramResponse); public static interface KeyVal { KeyVal key(String param1String); String key(); KeyVal value(String param1String); String value(); KeyVal inputStream(InputStream param1InputStream); @Nullable
/*    */     InputStream inputStream(); boolean hasInputStream(); KeyVal contentType(String param1String); @Nullable
/*    */     String contentType(); } public static interface Response extends Base<Response> { int statusCode(); String statusMessage(); @Nullable
/*    */     String charset(); Response charset(String param1String); @Nullable
/*    */     String contentType(); Document parse() throws IOException; String body(); byte[] bodyAsBytes(); Response bufferUp(); BufferedInputStream bodyStream(); } public static interface Request extends Base<Request> { @Nullable
/*    */     Proxy proxy(); Request proxy(@Nullable Proxy param1Proxy); Request proxy(String param1String, int param1Int); int timeout(); Request timeout(int param1Int); int maxBodySize(); Request maxBodySize(int param1Int); boolean followRedirects(); Request followRedirects(boolean param1Boolean); boolean ignoreHttpErrors(); Request ignoreHttpErrors(boolean param1Boolean);
/*    */     boolean ignoreContentType();
/*    */     Request ignoreContentType(boolean param1Boolean);
/*    */     @Nullable
/*    */     SSLSocketFactory sslSocketFactory();
/*    */     void sslSocketFactory(SSLSocketFactory param1SSLSocketFactory);
/*    */     Request data(Connection.KeyVal param1KeyVal);
/*    */     Collection<Connection.KeyVal> data();
/*    */     Request requestBody(@Nullable String param1String);
/*    */     @Nullable
/*    */     String requestBody();
/*    */     Request parser(Parser param1Parser);
/*    */     Parser parser();
/*    */     Request postDataCharset(String param1String);
/*    */     String postDataCharset(); }
/*    */   public static interface Base<T extends Base<T>> { URL url();
/*    */     T url(URL param1URL);
/*    */     Connection.Method method();
/*    */     T method(Connection.Method param1Method);
/*    */     @Nullable
/*    */     String header(String param1String);
/*    */     List<String> headers(String param1String);
/*    */     T header(String param1String1, String param1String2);
/*    */     T addHeader(String param1String1, String param1String2);
/*    */     boolean hasHeader(String param1String);
/*    */     boolean hasHeaderWithValue(String param1String1, String param1String2);
/*    */     T removeHeader(String param1String);
/*    */     Map<String, String> headers();
/*    */     Map<String, List<String>> multiHeaders();
/*    */     @Nullable
/*    */     String cookie(String param1String);
/*    */     T cookie(String param1String1, String param1String2);
/*    */     boolean hasCookie(String param1String);
/*    */     T removeCookie(String param1String);
/*    */     Map<String, String> cookies(); }
/* 46 */   public enum Method { GET(false), POST(true), PUT(true), DELETE(false), PATCH(true), HEAD(false), OPTIONS(false), TRACE(false);
/*    */     
/*    */     private final boolean hasBody;
/*    */     
/*    */     Method(boolean hasBody) {
/* 51 */       this.hasBody = hasBody;
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public final boolean hasBody() {
/* 59 */       return this.hasBody;
/*    */     } }
/*    */    }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\Connection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */