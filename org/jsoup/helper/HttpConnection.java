/*      */ package org.jsoup.helper;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.net.CookieManager;
/*      */ import java.net.CookieStore;
/*      */ import java.net.HttpURLConnection;
/*      */ import java.net.IDN;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.Proxy;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.net.URLEncoder;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.IllegalCharsetNameException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Pattern;
/*      */ import java.util.zip.GZIPInputStream;
/*      */ import java.util.zip.Inflater;
/*      */ import java.util.zip.InflaterInputStream;
/*      */ import javax.annotation.Nullable;
/*      */ import javax.net.ssl.HttpsURLConnection;
/*      */ import javax.net.ssl.SSLSocketFactory;
/*      */ import org.jsoup.Connection;
/*      */ import org.jsoup.HttpStatusException;
/*      */ import org.jsoup.UncheckedIOException;
/*      */ import org.jsoup.UnsupportedMimeTypeException;
/*      */ import org.jsoup.internal.ConstrainableInputStream;
/*      */ import org.jsoup.internal.Normalizer;
/*      */ import org.jsoup.internal.StringUtil;
/*      */ import org.jsoup.nodes.Document;
/*      */ import org.jsoup.parser.Parser;
/*      */ import org.jsoup.parser.TokenQueue;
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
/*      */ public class HttpConnection
/*      */   implements Connection
/*      */ {
/*      */   public static final String CONTENT_ENCODING = "Content-Encoding";
/*      */   public static final String DEFAULT_UA = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36";
/*      */   private static final String USER_AGENT = "User-Agent";
/*      */   public static final String CONTENT_TYPE = "Content-Type";
/*      */   public static final String MULTIPART_FORM_DATA = "multipart/form-data";
/*      */   public static final String FORM_URL_ENCODED = "application/x-www-form-urlencoded";
/*      */   private static final int HTTP_TEMP_REDIR = 307;
/*      */   private static final String DefaultUploadType = "application/octet-stream";
/*   71 */   private static final Charset UTF_8 = Charset.forName("UTF-8");
/*   72 */   private static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
/*      */   
/*      */   private Request req;
/*      */   
/*      */   @Nullable
/*      */   private Connection.Response res;
/*      */   
/*      */   public static Connection connect(String url) {
/*   80 */     Connection con = new HttpConnection();
/*   81 */     con.url(url);
/*   82 */     return con;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Connection connect(URL url) {
/*   91 */     Connection con = new HttpConnection();
/*   92 */     con.url(url);
/*   93 */     return con;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public HttpConnection() {
/*  100 */     this.req = new Request();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   HttpConnection(Request copy) {
/*  109 */     this.req = new Request(copy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static String encodeUrl(String url) {
/*      */     try {
/*  119 */       URL u = new URL(url);
/*  120 */       return encodeUrl(u).toExternalForm();
/*  121 */     } catch (Exception e) {
/*  122 */       return url;
/*      */     } 
/*      */   }
/*      */   
/*      */   static URL encodeUrl(URL u) {
/*  127 */     u = punyUrl(u);
/*      */     
/*      */     try {
/*  130 */       URI uri = new URI(u.getProtocol(), u.getUserInfo(), u.getHost(), u.getPort(), u.getPath(), u.getQuery(), u.getRef());
/*  131 */       return uri.toURL();
/*  132 */     } catch (URISyntaxException|MalformedURLException e) {
/*      */       
/*  134 */       return u;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static URL punyUrl(URL url) {
/*  144 */     if (!StringUtil.isAscii(url.getHost())) {
/*      */       try {
/*  146 */         String puny = IDN.toASCII(url.getHost());
/*  147 */         url = new URL(url.getProtocol(), puny, url.getPort(), url.getFile());
/*  148 */       } catch (MalformedURLException e) {
/*      */         
/*  150 */         throw new IllegalArgumentException(e);
/*      */       } 
/*      */     }
/*  153 */     return url;
/*      */   }
/*      */   
/*      */   private static String encodeMimeName(String val) {
/*  157 */     return val.replace("\"", "%22");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Connection newRequest() {
/*  166 */     return new HttpConnection(this.req);
/*      */   }
/*      */ 
/*      */   
/*      */   private HttpConnection(Request req, Response res) {
/*  171 */     this.req = req;
/*  172 */     this.res = res;
/*      */   }
/*      */   
/*      */   public Connection url(URL url) {
/*  176 */     this.req.url(url);
/*  177 */     return this;
/*      */   }
/*      */   
/*      */   public Connection url(String url) {
/*  181 */     Validate.notEmptyParam(url, "url");
/*      */     try {
/*  183 */       this.req.url(new URL(encodeUrl(url)));
/*  184 */     } catch (MalformedURLException e) {
/*  185 */       throw new IllegalArgumentException(String.format("The supplied URL, '%s', is malformed. Make sure it is an absolute URL, and starts with 'http://' or 'https://'. See https://jsoup.org/cookbook/extracting-data/working-with-urls", new Object[] { url }), e);
/*      */     } 
/*  187 */     return this;
/*      */   }
/*      */   
/*      */   public Connection proxy(@Nullable Proxy proxy) {
/*  191 */     this.req.proxy(proxy);
/*  192 */     return this;
/*      */   }
/*      */   
/*      */   public Connection proxy(String host, int port) {
/*  196 */     this.req.proxy(host, port);
/*  197 */     return this;
/*      */   }
/*      */   
/*      */   public Connection userAgent(String userAgent) {
/*  201 */     Validate.notNullParam(userAgent, "userAgent");
/*  202 */     this.req.header("User-Agent", userAgent);
/*  203 */     return this;
/*      */   }
/*      */   
/*      */   public Connection timeout(int millis) {
/*  207 */     this.req.timeout(millis);
/*  208 */     return this;
/*      */   }
/*      */   
/*      */   public Connection maxBodySize(int bytes) {
/*  212 */     this.req.maxBodySize(bytes);
/*  213 */     return this;
/*      */   }
/*      */   
/*      */   public Connection followRedirects(boolean followRedirects) {
/*  217 */     this.req.followRedirects(followRedirects);
/*  218 */     return this;
/*      */   }
/*      */   
/*      */   public Connection referrer(String referrer) {
/*  222 */     Validate.notNullParam(referrer, "referrer");
/*  223 */     this.req.header("Referer", referrer);
/*  224 */     return this;
/*      */   }
/*      */   
/*      */   public Connection method(Connection.Method method) {
/*  228 */     this.req.method(method);
/*  229 */     return this;
/*      */   }
/*      */   
/*      */   public Connection ignoreHttpErrors(boolean ignoreHttpErrors) {
/*  233 */     this.req.ignoreHttpErrors(ignoreHttpErrors);
/*  234 */     return this;
/*      */   }
/*      */   
/*      */   public Connection ignoreContentType(boolean ignoreContentType) {
/*  238 */     this.req.ignoreContentType(ignoreContentType);
/*  239 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Connection data(String key, String value) {
/*  244 */     this.req.data(KeyVal.create(key, value));
/*  245 */     return this;
/*      */   }
/*      */   
/*      */   public Connection sslSocketFactory(SSLSocketFactory sslSocketFactory) {
/*  249 */     this.req.sslSocketFactory(sslSocketFactory);
/*  250 */     return this;
/*      */   }
/*      */   
/*      */   public Connection data(String key, String filename, InputStream inputStream) {
/*  254 */     this.req.data(KeyVal.create(key, filename, inputStream));
/*  255 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public Connection data(String key, String filename, InputStream inputStream, String contentType) {
/*  260 */     this.req.data(KeyVal.create(key, filename, inputStream).contentType(contentType));
/*  261 */     return this;
/*      */   }
/*      */   
/*      */   public Connection data(Map<String, String> data) {
/*  265 */     Validate.notNullParam(data, "data");
/*  266 */     for (Map.Entry<String, String> entry : data.entrySet()) {
/*  267 */       this.req.data(KeyVal.create(entry.getKey(), entry.getValue()));
/*      */     }
/*  269 */     return this;
/*      */   }
/*      */   
/*      */   public Connection data(String... keyvals) {
/*  273 */     Validate.notNullParam(keyvals, "keyvals");
/*  274 */     Validate.isTrue((keyvals.length % 2 == 0), "Must supply an even number of key value pairs");
/*  275 */     for (int i = 0; i < keyvals.length; i += 2) {
/*  276 */       String key = keyvals[i];
/*  277 */       String value = keyvals[i + 1];
/*  278 */       Validate.notEmpty(key, "Data key must not be empty");
/*  279 */       Validate.notNull(value, "Data value must not be null");
/*  280 */       this.req.data(KeyVal.create(key, value));
/*      */     } 
/*  282 */     return this;
/*      */   }
/*      */   
/*      */   public Connection data(Collection<Connection.KeyVal> data) {
/*  286 */     Validate.notNullParam(data, "data");
/*  287 */     for (Connection.KeyVal entry : data) {
/*  288 */       this.req.data(entry);
/*      */     }
/*  290 */     return this;
/*      */   }
/*      */   
/*      */   public Connection.KeyVal data(String key) {
/*  294 */     Validate.notEmptyParam(key, "key");
/*  295 */     for (Connection.KeyVal keyVal : request().data()) {
/*  296 */       if (keyVal.key().equals(key))
/*  297 */         return keyVal; 
/*      */     } 
/*  299 */     return null;
/*      */   }
/*      */   
/*      */   public Connection requestBody(String body) {
/*  303 */     this.req.requestBody(body);
/*  304 */     return this;
/*      */   }
/*      */   
/*      */   public Connection header(String name, String value) {
/*  308 */     this.req.header(name, value);
/*  309 */     return this;
/*      */   }
/*      */   
/*      */   public Connection headers(Map<String, String> headers) {
/*  313 */     Validate.notNullParam(headers, "headers");
/*  314 */     for (Map.Entry<String, String> entry : headers.entrySet()) {
/*  315 */       this.req.header(entry.getKey(), entry.getValue());
/*      */     }
/*  317 */     return this;
/*      */   }
/*      */   
/*      */   public Connection cookie(String name, String value) {
/*  321 */     this.req.cookie(name, value);
/*  322 */     return this;
/*      */   }
/*      */   
/*      */   public Connection cookies(Map<String, String> cookies) {
/*  326 */     Validate.notNullParam(cookies, "cookies");
/*  327 */     for (Map.Entry<String, String> entry : cookies.entrySet()) {
/*  328 */       this.req.cookie(entry.getKey(), entry.getValue());
/*      */     }
/*  330 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Connection cookieStore(CookieStore cookieStore) {
/*  336 */     this.req.cookieManager = new CookieManager(cookieStore, null);
/*  337 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public CookieStore cookieStore() {
/*  342 */     return this.req.cookieManager.getCookieStore();
/*      */   }
/*      */   
/*      */   public Connection parser(Parser parser) {
/*  346 */     this.req.parser(parser);
/*  347 */     return this;
/*      */   }
/*      */   
/*      */   public Document get() throws IOException {
/*  351 */     this.req.method(Connection.Method.GET);
/*  352 */     execute();
/*  353 */     Validate.notNull(this.res);
/*  354 */     return this.res.parse();
/*      */   }
/*      */   
/*      */   public Document post() throws IOException {
/*  358 */     this.req.method(Connection.Method.POST);
/*  359 */     execute();
/*  360 */     Validate.notNull(this.res);
/*  361 */     return this.res.parse();
/*      */   }
/*      */   
/*      */   public Connection.Response execute() throws IOException {
/*  365 */     this.res = Response.execute(this.req);
/*  366 */     return this.res;
/*      */   }
/*      */   
/*      */   public Connection.Request request() {
/*  370 */     return this.req;
/*      */   }
/*      */   
/*      */   public Connection request(Connection.Request request) {
/*  374 */     this.req = (Request)request;
/*  375 */     return this;
/*      */   }
/*      */   
/*      */   public Connection.Response response() {
/*  379 */     if (this.res == null) {
/*  380 */       throw new IllegalArgumentException("You must execute the request before getting a response.");
/*      */     }
/*  382 */     return this.res;
/*      */   }
/*      */   
/*      */   public Connection response(Connection.Response response) {
/*  386 */     this.res = response;
/*  387 */     return this;
/*      */   }
/*      */   
/*      */   public Connection postDataCharset(String charset) {
/*  391 */     this.req.postDataCharset(charset);
/*  392 */     return this;
/*      */   }
/*      */   
/*      */   private static abstract class Base<T extends Connection.Base<T>>
/*      */     implements Connection.Base<T> {
/*      */     private static final URL UnsetUrl;
/*      */     
/*      */     static {
/*      */       try {
/*  401 */         UnsetUrl = new URL("http://undefined/");
/*  402 */       } catch (MalformedURLException e) {
/*  403 */         throw new IllegalStateException(e);
/*      */       } 
/*      */     }
/*      */     
/*  407 */     URL url = UnsetUrl;
/*  408 */     Connection.Method method = Connection.Method.GET;
/*      */     Map<String, List<String>> headers;
/*      */     Map<String, String> cookies;
/*      */     
/*      */     private Base() {
/*  413 */       this.headers = new LinkedHashMap<>();
/*  414 */       this.cookies = new LinkedHashMap<>();
/*      */     }
/*      */     
/*      */     private Base(Base<T> copy) {
/*  418 */       this.url = copy.url;
/*  419 */       this.method = copy.method;
/*  420 */       this.headers = new LinkedHashMap<>();
/*  421 */       for (Map.Entry<String, List<String>> entry : copy.headers.entrySet()) {
/*  422 */         this.headers.put(entry.getKey(), new ArrayList<>(entry.getValue()));
/*      */       }
/*  424 */       this.cookies = new LinkedHashMap<>(); this.cookies.putAll(copy.cookies);
/*      */     }
/*      */     
/*      */     public URL url() {
/*  428 */       if (this.url == UnsetUrl)
/*  429 */         throw new IllegalArgumentException("URL not set. Make sure to call #url(...) before executing the request."); 
/*  430 */       return this.url;
/*      */     }
/*      */     
/*      */     public T url(URL url) {
/*  434 */       Validate.notNullParam(url, "url");
/*  435 */       this.url = HttpConnection.punyUrl(url);
/*  436 */       return (T)this;
/*      */     }
/*      */     
/*      */     public Connection.Method method() {
/*  440 */       return this.method;
/*      */     }
/*      */     
/*      */     public T method(Connection.Method method) {
/*  444 */       Validate.notNullParam(method, "method");
/*  445 */       this.method = method;
/*  446 */       return (T)this;
/*      */     }
/*      */     
/*      */     public String header(String name) {
/*  450 */       Validate.notNullParam(name, "name");
/*  451 */       List<String> vals = getHeadersCaseInsensitive(name);
/*  452 */       if (vals.size() > 0)
/*      */       {
/*  454 */         return StringUtil.join(vals, ", ");
/*      */       }
/*      */       
/*  457 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     public T addHeader(String name, String value) {
/*  462 */       Validate.notEmptyParam(name, "name");
/*      */       
/*  464 */       value = (value == null) ? "" : value;
/*      */       
/*  466 */       List<String> values = headers(name);
/*  467 */       if (values.isEmpty()) {
/*  468 */         values = new ArrayList<>();
/*  469 */         this.headers.put(name, values);
/*      */       } 
/*  471 */       values.add(fixHeaderEncoding(value));
/*      */       
/*  473 */       return (T)this;
/*      */     }
/*      */ 
/*      */     
/*      */     public List<String> headers(String name) {
/*  478 */       Validate.notEmptyParam(name, "name");
/*  479 */       return getHeadersCaseInsensitive(name);
/*      */     }
/*      */     
/*      */     private static String fixHeaderEncoding(String val) {
/*  483 */       byte[] bytes = val.getBytes(HttpConnection.ISO_8859_1);
/*  484 */       if (!looksLikeUtf8(bytes))
/*  485 */         return val; 
/*  486 */       return new String(bytes, HttpConnection.UTF_8);
/*      */     }
/*      */     
/*      */     private static boolean looksLikeUtf8(byte[] input) {
/*  490 */       int i = 0;
/*      */       
/*  492 */       if (input.length >= 3 && (input[0] & 0xFF) == 239 && (input[1] & 0xFF) == 187 && (input[2] & 0xFF) == 191)
/*      */       {
/*      */ 
/*      */         
/*  496 */         i = 3;
/*      */       }
/*      */ 
/*      */       
/*  500 */       for (int j = input.length; i < j; i++) {
/*  501 */         int o = input[i];
/*  502 */         if ((o & 0x80) != 0) {
/*      */           int end;
/*      */ 
/*      */ 
/*      */           
/*  507 */           if ((o & 0xE0) == 192) {
/*  508 */             end = i + 1;
/*  509 */           } else if ((o & 0xF0) == 224) {
/*  510 */             end = i + 2;
/*  511 */           } else if ((o & 0xF8) == 240) {
/*  512 */             end = i + 3;
/*      */           } else {
/*  514 */             return false;
/*      */           } 
/*      */           
/*  517 */           if (end >= input.length) {
/*  518 */             return false;
/*      */           }
/*  520 */           while (i < end) {
/*  521 */             i++;
/*  522 */             o = input[i];
/*  523 */             if ((o & 0xC0) != 128)
/*  524 */               return false; 
/*      */           } 
/*      */         } 
/*      */       } 
/*  528 */       return true;
/*      */     }
/*      */     
/*      */     public T header(String name, String value) {
/*  532 */       Validate.notEmptyParam(name, "name");
/*  533 */       removeHeader(name);
/*  534 */       addHeader(name, value);
/*  535 */       return (T)this;
/*      */     }
/*      */     
/*      */     public boolean hasHeader(String name) {
/*  539 */       Validate.notEmptyParam(name, "name");
/*  540 */       return !getHeadersCaseInsensitive(name).isEmpty();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean hasHeaderWithValue(String name, String value) {
/*  547 */       Validate.notEmpty(name);
/*  548 */       Validate.notEmpty(value);
/*  549 */       List<String> values = headers(name);
/*  550 */       for (String candidate : values) {
/*  551 */         if (value.equalsIgnoreCase(candidate))
/*  552 */           return true; 
/*      */       } 
/*  554 */       return false;
/*      */     }
/*      */     
/*      */     public T removeHeader(String name) {
/*  558 */       Validate.notEmptyParam(name, "name");
/*  559 */       Map.Entry<String, List<String>> entry = scanHeaders(name);
/*  560 */       if (entry != null)
/*  561 */         this.headers.remove(entry.getKey()); 
/*  562 */       return (T)this;
/*      */     }
/*      */     
/*      */     public Map<String, String> headers() {
/*  566 */       LinkedHashMap<String, String> map = new LinkedHashMap<>(this.headers.size());
/*  567 */       for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
/*  568 */         String header = entry.getKey();
/*  569 */         List<String> values = entry.getValue();
/*  570 */         if (values.size() > 0)
/*  571 */           map.put(header, values.get(0)); 
/*      */       } 
/*  573 */       return map;
/*      */     }
/*      */ 
/*      */     
/*      */     public Map<String, List<String>> multiHeaders() {
/*  578 */       return this.headers;
/*      */     }
/*      */     
/*      */     private List<String> getHeadersCaseInsensitive(String name) {
/*  582 */       Validate.notNull(name);
/*      */       
/*  584 */       for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
/*  585 */         if (name.equalsIgnoreCase(entry.getKey())) {
/*  586 */           return entry.getValue();
/*      */         }
/*      */       } 
/*  589 */       return Collections.emptyList();
/*      */     }
/*      */     @Nullable
/*      */     private Map.Entry<String, List<String>> scanHeaders(String name) {
/*  593 */       String lc = Normalizer.lowerCase(name);
/*  594 */       for (Map.Entry<String, List<String>> entry : this.headers.entrySet()) {
/*  595 */         if (Normalizer.lowerCase(entry.getKey()).equals(lc))
/*  596 */           return entry; 
/*      */       } 
/*  598 */       return null;
/*      */     }
/*      */     
/*      */     public String cookie(String name) {
/*  602 */       Validate.notEmptyParam(name, "name");
/*  603 */       return this.cookies.get(name);
/*      */     }
/*      */     
/*      */     public T cookie(String name, String value) {
/*  607 */       Validate.notEmptyParam(name, "name");
/*  608 */       Validate.notNullParam(value, "value");
/*  609 */       this.cookies.put(name, value);
/*  610 */       return (T)this;
/*      */     }
/*      */     
/*      */     public boolean hasCookie(String name) {
/*  614 */       Validate.notEmptyParam(name, "name");
/*  615 */       return this.cookies.containsKey(name);
/*      */     }
/*      */     
/*      */     public T removeCookie(String name) {
/*  619 */       Validate.notEmptyParam(name, "name");
/*  620 */       this.cookies.remove(name);
/*  621 */       return (T)this;
/*      */     }
/*      */     
/*      */     public Map<String, String> cookies() {
/*  625 */       return this.cookies;
/*      */     } }
/*      */   public static class Request extends Base<Connection.Request> implements Connection.Request { @Nullable
/*      */     private Proxy proxy; private int timeoutMilliseconds;
/*      */     
/*      */     static {
/*  631 */       System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
/*      */     }
/*      */ 
/*      */     
/*      */     private int maxBodySizeBytes;
/*      */     
/*      */     private boolean followRedirects;
/*      */     private final Collection<Connection.KeyVal> data;
/*      */     @Nullable
/*  640 */     private String body = null;
/*      */     private boolean ignoreHttpErrors = false;
/*      */     private boolean ignoreContentType = false;
/*      */     private Parser parser;
/*      */     private boolean parserDefined = false;
/*  645 */     private String postDataCharset = DataUtil.defaultCharsetName;
/*      */     @Nullable
/*      */     private SSLSocketFactory sslSocketFactory;
/*      */     private CookieManager cookieManager;
/*      */     private volatile boolean executing = false;
/*      */     
/*      */     Request() {
/*  652 */       this.timeoutMilliseconds = 30000;
/*  653 */       this.maxBodySizeBytes = 2097152;
/*  654 */       this.followRedirects = true;
/*  655 */       this.data = new ArrayList<>();
/*  656 */       this.method = Connection.Method.GET;
/*  657 */       addHeader("Accept-Encoding", "gzip");
/*  658 */       addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
/*  659 */       this.parser = Parser.htmlParser();
/*  660 */       this.cookieManager = new CookieManager();
/*      */     }
/*      */     
/*      */     Request(Request copy) {
/*  664 */       super(copy);
/*  665 */       this.proxy = copy.proxy;
/*  666 */       this.postDataCharset = copy.postDataCharset;
/*  667 */       this.timeoutMilliseconds = copy.timeoutMilliseconds;
/*  668 */       this.maxBodySizeBytes = copy.maxBodySizeBytes;
/*  669 */       this.followRedirects = copy.followRedirects;
/*  670 */       this.data = new ArrayList<>();
/*      */       
/*  672 */       this.ignoreHttpErrors = copy.ignoreHttpErrors;
/*  673 */       this.ignoreContentType = copy.ignoreContentType;
/*  674 */       this.parser = copy.parser.newInstance();
/*  675 */       this.parserDefined = copy.parserDefined;
/*  676 */       this.sslSocketFactory = copy.sslSocketFactory;
/*  677 */       this.cookieManager = copy.cookieManager;
/*  678 */       this.executing = false;
/*      */     }
/*      */     
/*      */     public Proxy proxy() {
/*  682 */       return this.proxy;
/*      */     }
/*      */     
/*      */     public Request proxy(@Nullable Proxy proxy) {
/*  686 */       this.proxy = proxy;
/*  687 */       return this;
/*      */     }
/*      */     
/*      */     public Request proxy(String host, int port) {
/*  691 */       this.proxy = new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(host, port));
/*  692 */       return this;
/*      */     }
/*      */     
/*      */     public int timeout() {
/*  696 */       return this.timeoutMilliseconds;
/*      */     }
/*      */     
/*      */     public Request timeout(int millis) {
/*  700 */       Validate.isTrue((millis >= 0), "Timeout milliseconds must be 0 (infinite) or greater");
/*  701 */       this.timeoutMilliseconds = millis;
/*  702 */       return this;
/*      */     }
/*      */     
/*      */     public int maxBodySize() {
/*  706 */       return this.maxBodySizeBytes;
/*      */     }
/*      */     
/*      */     public Connection.Request maxBodySize(int bytes) {
/*  710 */       Validate.isTrue((bytes >= 0), "maxSize must be 0 (unlimited) or larger");
/*  711 */       this.maxBodySizeBytes = bytes;
/*  712 */       return this;
/*      */     }
/*      */     
/*      */     public boolean followRedirects() {
/*  716 */       return this.followRedirects;
/*      */     }
/*      */     
/*      */     public Connection.Request followRedirects(boolean followRedirects) {
/*  720 */       this.followRedirects = followRedirects;
/*  721 */       return this;
/*      */     }
/*      */     
/*      */     public boolean ignoreHttpErrors() {
/*  725 */       return this.ignoreHttpErrors;
/*      */     }
/*      */     
/*      */     public SSLSocketFactory sslSocketFactory() {
/*  729 */       return this.sslSocketFactory;
/*      */     }
/*      */     
/*      */     public void sslSocketFactory(SSLSocketFactory sslSocketFactory) {
/*  733 */       this.sslSocketFactory = sslSocketFactory;
/*      */     }
/*      */     
/*      */     public Connection.Request ignoreHttpErrors(boolean ignoreHttpErrors) {
/*  737 */       this.ignoreHttpErrors = ignoreHttpErrors;
/*  738 */       return this;
/*      */     }
/*      */     
/*      */     public boolean ignoreContentType() {
/*  742 */       return this.ignoreContentType;
/*      */     }
/*      */     
/*      */     public Connection.Request ignoreContentType(boolean ignoreContentType) {
/*  746 */       this.ignoreContentType = ignoreContentType;
/*  747 */       return this;
/*      */     }
/*      */     
/*      */     public Request data(Connection.KeyVal keyval) {
/*  751 */       Validate.notNullParam(keyval, "keyval");
/*  752 */       this.data.add(keyval);
/*  753 */       return this;
/*      */     }
/*      */     
/*      */     public Collection<Connection.KeyVal> data() {
/*  757 */       return this.data;
/*      */     }
/*      */     
/*      */     public Connection.Request requestBody(@Nullable String body) {
/*  761 */       this.body = body;
/*  762 */       return this;
/*      */     }
/*      */     
/*      */     public String requestBody() {
/*  766 */       return this.body;
/*      */     }
/*      */     
/*      */     public Request parser(Parser parser) {
/*  770 */       this.parser = parser;
/*  771 */       this.parserDefined = true;
/*  772 */       return this;
/*      */     }
/*      */     
/*      */     public Parser parser() {
/*  776 */       return this.parser;
/*      */     }
/*      */     
/*      */     public Connection.Request postDataCharset(String charset) {
/*  780 */       Validate.notNullParam(charset, "charset");
/*  781 */       if (!Charset.isSupported(charset)) throw new IllegalCharsetNameException(charset); 
/*  782 */       this.postDataCharset = charset;
/*  783 */       return this;
/*      */     }
/*      */     
/*      */     public String postDataCharset() {
/*  787 */       return this.postDataCharset;
/*      */     }
/*      */     
/*      */     CookieManager cookieManager() {
/*  791 */       return this.cookieManager;
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Response
/*      */     extends Base<Connection.Response>
/*      */     implements Connection.Response
/*      */   {
/*      */     private static final int MAX_REDIRECTS = 20;
/*      */     
/*      */     private static final String LOCATION = "Location";
/*      */     private final int statusCode;
/*      */     private final String statusMessage;
/*      */     @Nullable
/*      */     private ByteBuffer byteData;
/*  807 */     private int numRedirects = 0; @Nullable
/*      */     private InputStream bodyStream; @Nullable
/*      */     private HttpURLConnection conn; @Nullable
/*      */     private String charset; @Nullable
/*      */     private final String contentType; private boolean executed = false; private boolean inputStreamRead = false;
/*      */     private final HttpConnection.Request req;
/*  813 */     private static final Pattern xmlContentTypeRxp = Pattern.compile("(application|text)/\\w*\\+?xml.*");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Response() {
/*  821 */       this.statusCode = 400;
/*  822 */       this.statusMessage = "Request not made";
/*  823 */       this.req = new HttpConnection.Request();
/*  824 */       this.contentType = null;
/*      */     }
/*      */     
/*      */     static Response execute(HttpConnection.Request req) throws IOException {
/*  828 */       return execute(req, (Response)null);
/*      */     }
/*      */     
/*      */     static Response execute(HttpConnection.Request req, @Nullable Response previousResponse) throws IOException {
/*  832 */       synchronized (req) {
/*  833 */         Validate.isFalse(req.executing, "Multiple threads were detected trying to execute the same request concurrently. Make sure to use Connection#newRequest() and do not share an executing request between threads.");
/*  834 */         req.executing = true;
/*      */       } 
/*  836 */       Validate.notNullParam(req, "req");
/*  837 */       URL url = req.url();
/*  838 */       Validate.notNull(url, "URL must be specified to connect");
/*  839 */       String protocol = url.getProtocol();
/*  840 */       if (!protocol.equals("http") && !protocol.equals("https"))
/*  841 */         throw new MalformedURLException("Only http & https protocols supported"); 
/*  842 */       boolean methodHasBody = req.method().hasBody();
/*  843 */       boolean hasRequestBody = (req.requestBody() != null);
/*  844 */       if (!methodHasBody) {
/*  845 */         Validate.isFalse(hasRequestBody, "Cannot set a request body for HTTP method " + req.method());
/*      */       }
/*      */       
/*  848 */       String mimeBoundary = null;
/*  849 */       if (req.data().size() > 0 && (!methodHasBody || hasRequestBody)) {
/*  850 */         serialiseRequestUrl(req);
/*  851 */       } else if (methodHasBody) {
/*  852 */         mimeBoundary = setOutputContentType(req);
/*      */       } 
/*  854 */       long startTime = System.nanoTime();
/*  855 */       HttpURLConnection conn = createConnection(req);
/*  856 */       Response res = null;
/*      */       try {
/*  858 */         conn.connect();
/*  859 */         if (conn.getDoOutput()) {
/*  860 */           OutputStream out = conn.getOutputStream(); 
/*  861 */           try { writePost(req, out, mimeBoundary); }
/*  862 */           catch (IOException e) { conn.disconnect(); throw e; }
/*  863 */           finally { out.close(); }
/*      */         
/*      */         } 
/*  866 */         int status = conn.getResponseCode();
/*  867 */         res = new Response(conn, req, previousResponse);
/*      */ 
/*      */         
/*  870 */         if (res.hasHeader("Location") && req.followRedirects()) {
/*  871 */           if (status != 307) {
/*  872 */             req.method(Connection.Method.GET);
/*  873 */             req.data().clear();
/*  874 */             req.requestBody(null);
/*  875 */             req.removeHeader("Content-Type");
/*      */           } 
/*      */           
/*  878 */           String location = res.header("Location");
/*  879 */           Validate.notNull(location);
/*  880 */           if (location.startsWith("http:/") && location.charAt(6) != '/')
/*  881 */             location = location.substring(6); 
/*  882 */           URL redir = StringUtil.resolve(req.url(), location);
/*  883 */           req.url(HttpConnection.encodeUrl(redir));
/*      */           
/*  885 */           req.executing = false;
/*  886 */           return execute(req, res);
/*      */         } 
/*  888 */         if ((status < 200 || status >= 400) && !req.ignoreHttpErrors()) {
/*  889 */           throw new HttpStatusException("HTTP error fetching URL", status, req.url().toString());
/*      */         }
/*      */         
/*  892 */         String contentType = res.contentType();
/*  893 */         if (contentType != null && 
/*  894 */           !req.ignoreContentType() && 
/*  895 */           !contentType.startsWith("text/") && 
/*  896 */           !xmlContentTypeRxp.matcher(contentType).matches())
/*      */         {
/*  898 */           throw new UnsupportedMimeTypeException("Unhandled content type. Must be text/*, application/xml, or application/*+xml", contentType, req
/*  899 */               .url().toString());
/*      */         }
/*      */         
/*  902 */         if (contentType != null && xmlContentTypeRxp.matcher(contentType).matches() && 
/*  903 */           !req.parserDefined) req.parser(Parser.xmlParser());
/*      */ 
/*      */         
/*  906 */         res.charset = DataUtil.getCharsetFromContentType(res.contentType);
/*  907 */         if (conn.getContentLength() != 0 && req.method() != Connection.Method.HEAD) {
/*  908 */           res.bodyStream = (conn.getErrorStream() != null) ? conn.getErrorStream() : conn.getInputStream();
/*  909 */           Validate.notNull(res.bodyStream);
/*  910 */           if (res.hasHeaderWithValue("Content-Encoding", "gzip")) {
/*  911 */             res.bodyStream = new GZIPInputStream(res.bodyStream);
/*  912 */           } else if (res.hasHeaderWithValue("Content-Encoding", "deflate")) {
/*  913 */             res.bodyStream = new InflaterInputStream(res.bodyStream, new Inflater(true));
/*      */           } 
/*  915 */           res
/*      */             
/*  917 */             .bodyStream = (InputStream)ConstrainableInputStream.wrap(res.bodyStream, 32768, req.maxBodySize()).timeout(startTime, req.timeout());
/*      */         } else {
/*      */           
/*  920 */           res.byteData = DataUtil.emptyByteBuffer();
/*      */         } 
/*  922 */       } catch (IOException e) {
/*  923 */         if (res != null) res.safeClose(); 
/*  924 */         throw e;
/*      */       } finally {
/*  926 */         req.executing = false;
/*      */       } 
/*      */       
/*  929 */       res.executed = true;
/*  930 */       return res;
/*      */     }
/*      */     
/*      */     public int statusCode() {
/*  934 */       return this.statusCode;
/*      */     }
/*      */     
/*      */     public String statusMessage() {
/*  938 */       return this.statusMessage;
/*      */     }
/*      */     
/*      */     public String charset() {
/*  942 */       return this.charset;
/*      */     }
/*      */     
/*      */     public Response charset(String charset) {
/*  946 */       this.charset = charset;
/*  947 */       return this;
/*      */     }
/*      */     
/*      */     public String contentType() {
/*  951 */       return this.contentType;
/*      */     }
/*      */     
/*      */     public Document parse() throws IOException {
/*  955 */       Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before parsing response");
/*  956 */       if (this.byteData != null) {
/*  957 */         this.bodyStream = new ByteArrayInputStream(this.byteData.array());
/*  958 */         this.inputStreamRead = false;
/*      */       } 
/*  960 */       Validate.isFalse(this.inputStreamRead, "Input stream already read and parsed, cannot re-read.");
/*  961 */       Document doc = DataUtil.parseInputStream(this.bodyStream, this.charset, this.url.toExternalForm(), this.req.parser());
/*  962 */       doc.connection(new HttpConnection(this.req, this));
/*  963 */       this.charset = doc.outputSettings().charset().name();
/*  964 */       this.inputStreamRead = true;
/*  965 */       safeClose();
/*  966 */       return doc;
/*      */     }
/*      */     
/*      */     private void prepareByteData() {
/*  970 */       Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
/*  971 */       if (this.bodyStream != null && this.byteData == null) {
/*  972 */         Validate.isFalse(this.inputStreamRead, "Request has already been read (with .parse())");
/*      */         try {
/*  974 */           this.byteData = DataUtil.readToByteBuffer(this.bodyStream, this.req.maxBodySize());
/*  975 */         } catch (IOException e) {
/*  976 */           throw new UncheckedIOException(e);
/*      */         } finally {
/*  978 */           this.inputStreamRead = true;
/*  979 */           safeClose();
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     public String body() {
/*  985 */       prepareByteData();
/*  986 */       Validate.notNull(this.byteData);
/*      */ 
/*      */       
/*  989 */       String body = ((this.charset == null) ? DataUtil.UTF_8 : Charset.forName(this.charset)).decode(this.byteData).toString();
/*  990 */       this.byteData.rewind();
/*  991 */       return body;
/*      */     }
/*      */     
/*      */     public byte[] bodyAsBytes() {
/*  995 */       prepareByteData();
/*  996 */       Validate.notNull(this.byteData);
/*  997 */       return this.byteData.array();
/*      */     }
/*      */ 
/*      */     
/*      */     public Connection.Response bufferUp() {
/* 1002 */       prepareByteData();
/* 1003 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public BufferedInputStream bodyStream() {
/* 1008 */       Validate.isTrue(this.executed, "Request must be executed (with .execute(), .get(), or .post() before getting response body");
/* 1009 */       Validate.isFalse(this.inputStreamRead, "Request has already been read");
/* 1010 */       this.inputStreamRead = true;
/* 1011 */       return (BufferedInputStream)ConstrainableInputStream.wrap(this.bodyStream, 32768, this.req.maxBodySize());
/*      */     }
/*      */ 
/*      */     
/*      */     private static HttpURLConnection createConnection(HttpConnection.Request req) throws IOException {
/* 1016 */       Proxy proxy = req.proxy();
/*      */ 
/*      */ 
/*      */       
/* 1020 */       HttpURLConnection conn = (proxy == null) ? (HttpURLConnection)req.url().openConnection() : (HttpURLConnection)req.url().openConnection(proxy);
/*      */ 
/*      */       
/* 1023 */       conn.setRequestMethod(req.method().name());
/* 1024 */       conn.setInstanceFollowRedirects(false);
/* 1025 */       conn.setConnectTimeout(req.timeout());
/* 1026 */       conn.setReadTimeout(req.timeout() / 2);
/*      */       
/* 1028 */       if (req.sslSocketFactory() != null && conn instanceof HttpsURLConnection)
/* 1029 */         ((HttpsURLConnection)conn).setSSLSocketFactory(req.sslSocketFactory()); 
/* 1030 */       if (req.method().hasBody())
/* 1031 */         conn.setDoOutput(true); 
/* 1032 */       CookieUtil.applyCookiesToRequest(req, conn);
/* 1033 */       for (Map.Entry<String, List<String>> header : (Iterable<Map.Entry<String, List<String>>>)req.multiHeaders().entrySet()) {
/* 1034 */         for (String value : header.getValue()) {
/* 1035 */           conn.addRequestProperty(header.getKey(), value);
/*      */         }
/*      */       } 
/* 1038 */       return conn;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void safeClose() {
/* 1046 */       if (this.bodyStream != null) {
/*      */         try {
/* 1048 */           this.bodyStream.close();
/* 1049 */         } catch (IOException iOException) {
/*      */         
/*      */         } finally {
/* 1052 */           this.bodyStream = null;
/*      */         } 
/*      */       }
/* 1055 */       if (this.conn != null) {
/* 1056 */         this.conn.disconnect();
/* 1057 */         this.conn = null;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private Response(HttpURLConnection conn, HttpConnection.Request request, @Nullable Response previousResponse) throws IOException {
/* 1063 */       this.conn = conn;
/* 1064 */       this.req = request;
/* 1065 */       this.method = Connection.Method.valueOf(conn.getRequestMethod());
/* 1066 */       this.url = conn.getURL();
/* 1067 */       this.statusCode = conn.getResponseCode();
/* 1068 */       this.statusMessage = conn.getResponseMessage();
/* 1069 */       this.contentType = conn.getContentType();
/*      */       
/* 1071 */       Map<String, List<String>> resHeaders = createHeaderMap(conn);
/* 1072 */       processResponseHeaders(resHeaders);
/* 1073 */       CookieUtil.storeCookies(this.req, this.url, resHeaders);
/*      */       
/* 1075 */       if (previousResponse != null) {
/*      */         
/* 1077 */         for (Map.Entry<String, String> prevCookie : (Iterable<Map.Entry<String, String>>)previousResponse.cookies().entrySet()) {
/* 1078 */           if (!hasCookie(prevCookie.getKey()))
/* 1079 */             cookie(prevCookie.getKey(), prevCookie.getValue()); 
/*      */         } 
/* 1081 */         previousResponse.safeClose();
/*      */ 
/*      */         
/* 1084 */         previousResponse.numRedirects++;
/* 1085 */         if (this.numRedirects >= 20) {
/* 1086 */           throw new IOException(String.format("Too many redirects occurred trying to load URL %s", new Object[] { previousResponse.url() }));
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*      */     private static LinkedHashMap<String, List<String>> createHeaderMap(HttpURLConnection conn) {
/* 1092 */       LinkedHashMap<String, List<String>> headers = new LinkedHashMap<>();
/* 1093 */       int i = 0;
/*      */       while (true) {
/* 1095 */         String key = conn.getHeaderFieldKey(i);
/* 1096 */         String val = conn.getHeaderField(i);
/* 1097 */         if (key == null && val == null)
/*      */           break; 
/* 1099 */         i++;
/* 1100 */         if (key == null || val == null) {
/*      */           continue;
/*      */         }
/* 1103 */         if (headers.containsKey(key)) {
/* 1104 */           ((List<String>)headers.get(key)).add(val); continue;
/*      */         } 
/* 1106 */         ArrayList<String> vals = new ArrayList<>();
/* 1107 */         vals.add(val);
/* 1108 */         headers.put(key, vals);
/*      */       } 
/*      */       
/* 1111 */       return headers;
/*      */     }
/*      */     
/*      */     void processResponseHeaders(Map<String, List<String>> resHeaders) {
/* 1115 */       for (Map.Entry<String, List<String>> entry : resHeaders.entrySet()) {
/* 1116 */         String name = entry.getKey();
/* 1117 */         if (name == null) {
/*      */           continue;
/*      */         }
/* 1120 */         List<String> values = entry.getValue();
/* 1121 */         if (name.equalsIgnoreCase("Set-Cookie"))
/* 1122 */           for (String value : values) {
/* 1123 */             if (value == null)
/*      */               continue; 
/* 1125 */             TokenQueue cd = new TokenQueue(value);
/* 1126 */             String cookieName = cd.chompTo("=").trim();
/* 1127 */             String cookieVal = cd.consumeTo(";").trim();
/*      */ 
/*      */             
/* 1130 */             if (cookieName.length() > 0 && !this.cookies.containsKey(cookieName)) {
/* 1131 */               cookie(cookieName, cookieVal);
/*      */             }
/*      */           }  
/* 1134 */         for (String value : values)
/* 1135 */           addHeader(name, value); 
/*      */       } 
/*      */     }
/*      */     
/*      */     @Nullable
/*      */     private static String setOutputContentType(Connection.Request req) {
/* 1141 */       String contentType = req.header("Content-Type");
/* 1142 */       String bound = null;
/* 1143 */       if (contentType != null) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1148 */         if (contentType.contains("multipart/form-data") && !contentType.contains("boundary")) {
/* 1149 */           bound = DataUtil.mimeBoundary();
/* 1150 */           req.header("Content-Type", "multipart/form-data; boundary=" + bound);
/*      */         }
/*      */       
/*      */       }
/* 1154 */       else if (HttpConnection.needsMultipart(req)) {
/* 1155 */         bound = DataUtil.mimeBoundary();
/* 1156 */         req.header("Content-Type", "multipart/form-data; boundary=" + bound);
/*      */       } else {
/* 1158 */         req.header("Content-Type", "application/x-www-form-urlencoded; charset=" + req.postDataCharset());
/*      */       } 
/* 1160 */       return bound;
/*      */     }
/*      */     
/*      */     private static void writePost(Connection.Request req, OutputStream outputStream, @Nullable String boundary) throws IOException {
/* 1164 */       Collection<Connection.KeyVal> data = req.data();
/* 1165 */       BufferedWriter w = new BufferedWriter(new OutputStreamWriter(outputStream, Charset.forName(req.postDataCharset())));
/*      */       
/* 1167 */       if (boundary != null) {
/*      */         
/* 1169 */         for (Connection.KeyVal keyVal : data) {
/* 1170 */           w.write("--");
/* 1171 */           w.write(boundary);
/* 1172 */           w.write("\r\n");
/* 1173 */           w.write("Content-Disposition: form-data; name=\"");
/* 1174 */           w.write(HttpConnection.encodeMimeName(keyVal.key()));
/* 1175 */           w.write("\"");
/* 1176 */           InputStream input = keyVal.inputStream();
/* 1177 */           if (input != null) {
/* 1178 */             w.write("; filename=\"");
/* 1179 */             w.write(HttpConnection.encodeMimeName(keyVal.value()));
/* 1180 */             w.write("\"\r\nContent-Type: ");
/* 1181 */             String contentType = keyVal.contentType();
/* 1182 */             w.write((contentType != null) ? contentType : "application/octet-stream");
/* 1183 */             w.write("\r\n\r\n");
/* 1184 */             w.flush();
/* 1185 */             DataUtil.crossStreams(input, outputStream);
/* 1186 */             outputStream.flush();
/*      */           } else {
/* 1188 */             w.write("\r\n\r\n");
/* 1189 */             w.write(keyVal.value());
/*      */           } 
/* 1191 */           w.write("\r\n");
/*      */         } 
/* 1193 */         w.write("--");
/* 1194 */         w.write(boundary);
/* 1195 */         w.write("--");
/*      */       } else {
/* 1197 */         String body = req.requestBody();
/* 1198 */         if (body != null) {
/*      */           
/* 1200 */           w.write(body);
/*      */         }
/*      */         else {
/*      */           
/* 1204 */           boolean first = true;
/* 1205 */           for (Connection.KeyVal keyVal : data) {
/* 1206 */             if (!first) {
/* 1207 */               w.append('&');
/*      */             } else {
/* 1209 */               first = false;
/*      */             } 
/* 1211 */             w.write(URLEncoder.encode(keyVal.key(), req.postDataCharset()));
/* 1212 */             w.write(61);
/* 1213 */             w.write(URLEncoder.encode(keyVal.value(), req.postDataCharset()));
/*      */           } 
/*      */         } 
/*      */       } 
/* 1217 */       w.close();
/*      */     }
/*      */ 
/*      */     
/*      */     private static void serialiseRequestUrl(Connection.Request req) throws IOException {
/* 1222 */       URL in = req.url();
/* 1223 */       StringBuilder url = StringUtil.borrowBuilder();
/* 1224 */       boolean first = true;
/*      */       
/* 1226 */       url
/* 1227 */         .append(in.getProtocol())
/* 1228 */         .append("://")
/* 1229 */         .append(in.getAuthority())
/* 1230 */         .append(in.getPath())
/* 1231 */         .append("?");
/* 1232 */       if (in.getQuery() != null) {
/* 1233 */         url.append(in.getQuery());
/* 1234 */         first = false;
/*      */       } 
/* 1236 */       for (Connection.KeyVal keyVal : req.data()) {
/* 1237 */         Validate.isFalse(keyVal.hasInputStream(), "InputStream data not supported in URL query string.");
/* 1238 */         if (!first) {
/* 1239 */           url.append('&');
/*      */         } else {
/* 1241 */           first = false;
/* 1242 */         }  url
/* 1243 */           .append(URLEncoder.encode(keyVal.key(), DataUtil.defaultCharsetName))
/* 1244 */           .append('=')
/* 1245 */           .append(URLEncoder.encode(keyVal.value(), DataUtil.defaultCharsetName));
/*      */       } 
/* 1247 */       req.url(new URL(StringUtil.releaseBuilder(url)));
/* 1248 */       req.data().clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean needsMultipart(Connection.Request req) {
/* 1254 */     for (Connection.KeyVal keyVal : req.data()) {
/* 1255 */       if (keyVal.hasInputStream())
/* 1256 */         return true; 
/*      */     } 
/* 1258 */     return false;
/*      */   }
/*      */   public static class KeyVal implements Connection.KeyVal { private String key;
/*      */     private String value;
/*      */     @Nullable
/*      */     private InputStream stream;
/*      */     @Nullable
/*      */     private String contentType;
/*      */     
/*      */     public static KeyVal create(String key, String value) {
/* 1268 */       return new KeyVal(key, value);
/*      */     }
/*      */     
/*      */     public static KeyVal create(String key, String filename, InputStream stream) {
/* 1272 */       return (new KeyVal(key, filename))
/* 1273 */         .inputStream(stream);
/*      */     }
/*      */     
/*      */     private KeyVal(String key, String value) {
/* 1277 */       Validate.notEmptyParam(key, "key");
/* 1278 */       Validate.notNullParam(value, "value");
/* 1279 */       this.key = key;
/* 1280 */       this.value = value;
/*      */     }
/*      */     
/*      */     public KeyVal key(String key) {
/* 1284 */       Validate.notEmptyParam(key, "key");
/* 1285 */       this.key = key;
/* 1286 */       return this;
/*      */     }
/*      */     
/*      */     public String key() {
/* 1290 */       return this.key;
/*      */     }
/*      */     
/*      */     public KeyVal value(String value) {
/* 1294 */       Validate.notNullParam(value, "value");
/* 1295 */       this.value = value;
/* 1296 */       return this;
/*      */     }
/*      */     
/*      */     public String value() {
/* 1300 */       return this.value;
/*      */     }
/*      */     
/*      */     public KeyVal inputStream(InputStream inputStream) {
/* 1304 */       Validate.notNullParam(this.value, "inputStream");
/* 1305 */       this.stream = inputStream;
/* 1306 */       return this;
/*      */     }
/*      */     
/*      */     public InputStream inputStream() {
/* 1310 */       return this.stream;
/*      */     }
/*      */     
/*      */     public boolean hasInputStream() {
/* 1314 */       return (this.stream != null);
/*      */     }
/*      */ 
/*      */     
/*      */     public Connection.KeyVal contentType(String contentType) {
/* 1319 */       Validate.notEmpty(contentType);
/* 1320 */       this.contentType = contentType;
/* 1321 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public String contentType() {
/* 1326 */       return this.contentType;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/* 1331 */       return this.key + "=" + this.value;
/*      */     } }
/*      */ 
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\helper\HttpConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */