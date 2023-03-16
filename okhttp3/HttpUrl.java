/*      */ package okhttp3;
/*      */ 
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import javax.annotation.Nullable;
/*      */ import okhttp3.internal.Util;
/*      */ import okhttp3.internal.publicsuffix.PublicSuffixDatabase;
/*      */ import okio.Buffer;
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
/*      */ public final class HttpUrl
/*      */ {
/*  290 */   private static final char[] HEX_DIGITS = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*      */ 
/*      */   
/*      */   static final String USERNAME_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
/*      */ 
/*      */   
/*      */   static final String PASSWORD_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#";
/*      */ 
/*      */   
/*      */   static final String PATH_SEGMENT_ENCODE_SET = " \"<>^`{}|/\\?#";
/*      */ 
/*      */   
/*      */   static final String PATH_SEGMENT_ENCODE_SET_URI = "[]";
/*      */   
/*      */   static final String QUERY_ENCODE_SET = " \"'<>#";
/*      */   
/*      */   static final String QUERY_COMPONENT_REENCODE_SET = " \"'<>#&=";
/*      */   
/*      */   static final String QUERY_COMPONENT_ENCODE_SET = " !\"#$&'(),/:;<=>?@[]\\^`{|}~";
/*      */   
/*      */   static final String QUERY_COMPONENT_ENCODE_SET_URI = "\\^`{|}";
/*      */   
/*      */   static final String FORM_ENCODE_SET = " \"':;<=>@[]^`{}|/\\?#&!$(),~";
/*      */   
/*      */   static final String FRAGMENT_ENCODE_SET = "";
/*      */   
/*      */   static final String FRAGMENT_ENCODE_SET_URI = " \"#<>\\^`{|}";
/*      */   
/*      */   final String scheme;
/*      */   
/*      */   private final String username;
/*      */   
/*      */   private final String password;
/*      */   
/*      */   final String host;
/*      */   
/*      */   final int port;
/*      */   
/*      */   private final List<String> pathSegments;
/*      */   
/*      */   @Nullable
/*      */   private final List<String> queryNamesAndValues;
/*      */   
/*      */   @Nullable
/*      */   private final String fragment;
/*      */   
/*      */   private final String url;
/*      */ 
/*      */   
/*      */   HttpUrl(Builder builder) {
/*  340 */     this.scheme = builder.scheme;
/*  341 */     this.username = percentDecode(builder.encodedUsername, false);
/*  342 */     this.password = percentDecode(builder.encodedPassword, false);
/*  343 */     this.host = builder.host;
/*  344 */     this.port = builder.effectivePort();
/*  345 */     this.pathSegments = percentDecode(builder.encodedPathSegments, false);
/*  346 */     this
/*      */       
/*  348 */       .queryNamesAndValues = (builder.encodedQueryNamesAndValues != null) ? percentDecode(builder.encodedQueryNamesAndValues, true) : null;
/*  349 */     this
/*      */       
/*  351 */       .fragment = (builder.encodedFragment != null) ? percentDecode(builder.encodedFragment, false) : null;
/*  352 */     this.url = builder.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public URL url() {
/*      */     try {
/*  358 */       return new URL(this.url);
/*  359 */     } catch (MalformedURLException e) {
/*  360 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
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
/*      */   public URI uri() {
/*  378 */     String uri = newBuilder().reencodeForUri().toString();
/*      */     try {
/*  380 */       return new URI(uri);
/*  381 */     } catch (URISyntaxException e) {
/*      */       
/*      */       try {
/*  384 */         String stripped = uri.replaceAll("[\\u0000-\\u001F\\u007F-\\u009F\\p{javaWhitespace}]", "");
/*  385 */         return URI.create(stripped);
/*  386 */       } catch (Exception e1) {
/*  387 */         throw new RuntimeException(e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String scheme() {
/*  394 */     return this.scheme;
/*      */   }
/*      */   
/*      */   public boolean isHttps() {
/*  398 */     return this.scheme.equals("https");
/*      */   }
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
/*      */   public String encodedUsername() {
/*  413 */     if (this.username.isEmpty()) return ""; 
/*  414 */     int usernameStart = this.scheme.length() + 3;
/*  415 */     int usernameEnd = Util.delimiterOffset(this.url, usernameStart, this.url.length(), ":@");
/*  416 */     return this.url.substring(usernameStart, usernameEnd);
/*      */   }
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
/*      */   public String username() {
/*  431 */     return this.username;
/*      */   }
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
/*      */   public String encodedPassword() {
/*  446 */     if (this.password.isEmpty()) return ""; 
/*  447 */     int passwordStart = this.url.indexOf(':', this.scheme.length() + 3) + 1;
/*  448 */     int passwordEnd = this.url.indexOf('@');
/*  449 */     return this.url.substring(passwordStart, passwordEnd);
/*      */   }
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
/*      */   public String password() {
/*  464 */     return this.password;
/*      */   }
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
/*      */   public String host() {
/*  487 */     return this.host;
/*      */   }
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
/*      */   public int port() {
/*  503 */     return this.port;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int defaultPort(String scheme) {
/*  511 */     if (scheme.equals("http"))
/*  512 */       return 80; 
/*  513 */     if (scheme.equals("https")) {
/*  514 */       return 443;
/*      */     }
/*  516 */     return -1;
/*      */   }
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
/*      */   public int pathSize() {
/*  532 */     return this.pathSegments.size();
/*      */   }
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
/*      */   public String encodedPath() {
/*  547 */     int pathStart = this.url.indexOf('/', this.scheme.length() + 3);
/*  548 */     int pathEnd = Util.delimiterOffset(this.url, pathStart, this.url.length(), "?#");
/*  549 */     return this.url.substring(pathStart, pathEnd);
/*      */   }
/*      */   
/*      */   static void pathSegmentsToString(StringBuilder out, List<String> pathSegments) {
/*  553 */     for (int i = 0, size = pathSegments.size(); i < size; i++) {
/*  554 */       out.append('/');
/*  555 */       out.append(pathSegments.get(i));
/*      */     } 
/*      */   }
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
/*      */   public List<String> encodedPathSegments() {
/*  571 */     int pathStart = this.url.indexOf('/', this.scheme.length() + 3);
/*  572 */     int pathEnd = Util.delimiterOffset(this.url, pathStart, this.url.length(), "?#");
/*  573 */     List<String> result = new ArrayList<>(); int i;
/*  574 */     for (i = pathStart; i < pathEnd; ) {
/*  575 */       i++;
/*  576 */       int segmentEnd = Util.delimiterOffset(this.url, i, pathEnd, '/');
/*  577 */       result.add(this.url.substring(i, segmentEnd));
/*  578 */       i = segmentEnd;
/*      */     } 
/*  580 */     return result;
/*      */   }
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
/*      */   public List<String> pathSegments() {
/*  595 */     return this.pathSegments;
/*      */   }
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
/*      */   @Nullable
/*      */   public String encodedQuery() {
/*  614 */     if (this.queryNamesAndValues == null) return null; 
/*  615 */     int queryStart = this.url.indexOf('?') + 1;
/*  616 */     int queryEnd = Util.delimiterOffset(this.url, queryStart, this.url.length(), '#');
/*  617 */     return this.url.substring(queryStart, queryEnd);
/*      */   }
/*      */   
/*      */   static void namesAndValuesToQueryString(StringBuilder out, List<String> namesAndValues) {
/*  621 */     for (int i = 0, size = namesAndValues.size(); i < size; i += 2) {
/*  622 */       String name = namesAndValues.get(i);
/*  623 */       String value = namesAndValues.get(i + 1);
/*  624 */       if (i > 0) out.append('&'); 
/*  625 */       out.append(name);
/*  626 */       if (value != null) {
/*  627 */         out.append('=');
/*  628 */         out.append(value);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static List<String> queryStringToNamesAndValues(String encodedQuery) {
/*  640 */     List<String> result = new ArrayList<>();
/*  641 */     for (int pos = 0; pos <= encodedQuery.length(); ) {
/*  642 */       int ampersandOffset = encodedQuery.indexOf('&', pos);
/*  643 */       if (ampersandOffset == -1) ampersandOffset = encodedQuery.length();
/*      */       
/*  645 */       int equalsOffset = encodedQuery.indexOf('=', pos);
/*  646 */       if (equalsOffset == -1 || equalsOffset > ampersandOffset) {
/*  647 */         result.add(encodedQuery.substring(pos, ampersandOffset));
/*  648 */         result.add(null);
/*      */       } else {
/*  650 */         result.add(encodedQuery.substring(pos, equalsOffset));
/*  651 */         result.add(encodedQuery.substring(equalsOffset + 1, ampersandOffset));
/*      */       } 
/*  653 */       pos = ampersandOffset + 1;
/*      */     } 
/*  655 */     return result;
/*      */   }
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
/*      */   @Nullable
/*      */   public String query() {
/*  674 */     if (this.queryNamesAndValues == null) return null; 
/*  675 */     StringBuilder result = new StringBuilder();
/*  676 */     namesAndValuesToQueryString(result, this.queryNamesAndValues);
/*  677 */     return result.toString();
/*      */   }
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
/*      */   public int querySize() {
/*  695 */     return (this.queryNamesAndValues != null) ? (this.queryNamesAndValues.size() / 2) : 0;
/*      */   }
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
/*      */   @Nullable
/*      */   public String queryParameter(String name) {
/*  712 */     if (this.queryNamesAndValues == null) return null; 
/*  713 */     for (int i = 0, size = this.queryNamesAndValues.size(); i < size; i += 2) {
/*  714 */       if (name.equals(this.queryNamesAndValues.get(i))) {
/*  715 */         return this.queryNamesAndValues.get(i + 1);
/*      */       }
/*      */     } 
/*  718 */     return null;
/*      */   }
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
/*      */   public Set<String> queryParameterNames() {
/*  735 */     if (this.queryNamesAndValues == null) return Collections.emptySet(); 
/*  736 */     Set<String> result = new LinkedHashSet<>();
/*  737 */     for (int i = 0, size = this.queryNamesAndValues.size(); i < size; i += 2) {
/*  738 */       result.add(this.queryNamesAndValues.get(i));
/*      */     }
/*  740 */     return Collections.unmodifiableSet(result);
/*      */   }
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
/*      */   public List<String> queryParameterValues(String name) {
/*  762 */     if (this.queryNamesAndValues == null) return Collections.emptyList(); 
/*  763 */     List<String> result = new ArrayList<>();
/*  764 */     for (int i = 0, size = this.queryNamesAndValues.size(); i < size; i += 2) {
/*  765 */       if (name.equals(this.queryNamesAndValues.get(i))) {
/*  766 */         result.add(this.queryNamesAndValues.get(i + 1));
/*      */       }
/*      */     } 
/*  769 */     return Collections.unmodifiableList(result);
/*      */   }
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
/*      */   public String queryParameterName(int index) {
/*  790 */     if (this.queryNamesAndValues == null) throw new IndexOutOfBoundsException(); 
/*  791 */     return this.queryNamesAndValues.get(index * 2);
/*      */   }
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
/*      */   public String queryParameterValue(int index) {
/*  812 */     if (this.queryNamesAndValues == null) throw new IndexOutOfBoundsException(); 
/*  813 */     return this.queryNamesAndValues.get(index * 2 + 1);
/*      */   }
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
/*      */   @Nullable
/*      */   public String encodedFragment() {
/*  829 */     if (this.fragment == null) return null; 
/*  830 */     int fragmentStart = this.url.indexOf('#') + 1;
/*  831 */     return this.url.substring(fragmentStart);
/*      */   }
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
/*      */   @Nullable
/*      */   public String fragment() {
/*  847 */     return this.fragment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String redact() {
/*  856 */     return newBuilder("/...")
/*  857 */       .username("")
/*  858 */       .password("")
/*  859 */       .build()
/*  860 */       .toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public HttpUrl resolve(String link) {
/*  868 */     Builder builder = newBuilder(link);
/*  869 */     return (builder != null) ? builder.build() : null;
/*      */   }
/*      */   
/*      */   public Builder newBuilder() {
/*  873 */     Builder result = new Builder();
/*  874 */     result.scheme = this.scheme;
/*  875 */     result.encodedUsername = encodedUsername();
/*  876 */     result.encodedPassword = encodedPassword();
/*  877 */     result.host = this.host;
/*      */     
/*  879 */     result.port = (this.port != defaultPort(this.scheme)) ? this.port : -1;
/*  880 */     result.encodedPathSegments.clear();
/*  881 */     result.encodedPathSegments.addAll(encodedPathSegments());
/*  882 */     result.encodedQuery(encodedQuery());
/*  883 */     result.encodedFragment = encodedFragment();
/*  884 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public Builder newBuilder(String link) {
/*      */     try {
/*  893 */       return (new Builder()).parse(this, link);
/*  894 */     } catch (IllegalArgumentException ignored) {
/*  895 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static HttpUrl parse(String url) {
/*      */     try {
/*  905 */       return get(url);
/*  906 */     } catch (IllegalArgumentException ignored) {
/*  907 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static HttpUrl get(String url) {
/*  917 */     return (new Builder()).parse(null, url).build();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Nullable
/*      */   public static HttpUrl get(URL url) {
/*  925 */     return parse(url.toString());
/*      */   }
/*      */   @Nullable
/*      */   public static HttpUrl get(URI uri) {
/*  929 */     return parse(uri.toString());
/*      */   }
/*      */   
/*      */   public boolean equals(@Nullable Object other) {
/*  933 */     return (other instanceof HttpUrl && ((HttpUrl)other).url.equals(this.url));
/*      */   }
/*      */   
/*      */   public int hashCode() {
/*  937 */     return this.url.hashCode();
/*      */   }
/*      */   
/*      */   public String toString() {
/*  941 */     return this.url;
/*      */   }
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
/*      */   @Nullable
/*      */   public String topPrivateDomain() {
/*  964 */     if (Util.verifyAsIpAddress(this.host)) return null; 
/*  965 */     return PublicSuffixDatabase.get().getEffectiveTldPlusOne(this.host);
/*      */   }
/*      */   
/*      */   public static final class Builder { @Nullable
/*      */     String scheme;
/*  970 */     String encodedUsername = "";
/*  971 */     String encodedPassword = ""; @Nullable
/*      */     String host;
/*  973 */     int port = -1;
/*  974 */     final List<String> encodedPathSegments = new ArrayList<>();
/*      */     @Nullable
/*      */     List<String> encodedQueryNamesAndValues;
/*      */     
/*      */     public Builder() {
/*  979 */       this.encodedPathSegments.add("");
/*      */     } @Nullable
/*      */     String encodedFragment; static final String INVALID_HOST = "Invalid URL host";
/*      */     public Builder scheme(String scheme) {
/*  983 */       if (scheme == null)
/*  984 */         throw new NullPointerException("scheme == null"); 
/*  985 */       if (scheme.equalsIgnoreCase("http")) {
/*  986 */         this.scheme = "http";
/*  987 */       } else if (scheme.equalsIgnoreCase("https")) {
/*  988 */         this.scheme = "https";
/*      */       } else {
/*  990 */         throw new IllegalArgumentException("unexpected scheme: " + scheme);
/*      */       } 
/*  992 */       return this;
/*      */     }
/*      */     
/*      */     public Builder username(String username) {
/*  996 */       if (username == null) throw new NullPointerException("username == null"); 
/*  997 */       this.encodedUsername = HttpUrl.canonicalize(username, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
/*  998 */       return this;
/*      */     }
/*      */     
/*      */     public Builder encodedUsername(String encodedUsername) {
/* 1002 */       if (encodedUsername == null) throw new NullPointerException("encodedUsername == null"); 
/* 1003 */       this.encodedUsername = HttpUrl.canonicalize(encodedUsername, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
/*      */       
/* 1005 */       return this;
/*      */     }
/*      */     
/*      */     public Builder password(String password) {
/* 1009 */       if (password == null) throw new NullPointerException("password == null"); 
/* 1010 */       this.encodedPassword = HttpUrl.canonicalize(password, " \"':;<=>@[]^`{}|/\\?#", false, false, false, true);
/* 1011 */       return this;
/*      */     }
/*      */     
/*      */     public Builder encodedPassword(String encodedPassword) {
/* 1015 */       if (encodedPassword == null) throw new NullPointerException("encodedPassword == null"); 
/* 1016 */       this.encodedPassword = HttpUrl.canonicalize(encodedPassword, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true);
/*      */       
/* 1018 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder host(String host) {
/* 1026 */       if (host == null) throw new NullPointerException("host == null"); 
/* 1027 */       String encoded = canonicalizeHost(host, 0, host.length());
/* 1028 */       if (encoded == null) throw new IllegalArgumentException("unexpected host: " + host); 
/* 1029 */       this.host = encoded;
/* 1030 */       return this;
/*      */     }
/*      */     
/*      */     public Builder port(int port) {
/* 1034 */       if (port <= 0 || port > 65535) throw new IllegalArgumentException("unexpected port: " + port); 
/* 1035 */       this.port = port;
/* 1036 */       return this;
/*      */     }
/*      */     
/*      */     int effectivePort() {
/* 1040 */       return (this.port != -1) ? this.port : HttpUrl.defaultPort(this.scheme);
/*      */     }
/*      */     
/*      */     public Builder addPathSegment(String pathSegment) {
/* 1044 */       if (pathSegment == null) throw new NullPointerException("pathSegment == null"); 
/* 1045 */       push(pathSegment, 0, pathSegment.length(), false, false);
/* 1046 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder addPathSegments(String pathSegments) {
/* 1054 */       if (pathSegments == null) throw new NullPointerException("pathSegments == null"); 
/* 1055 */       return addPathSegments(pathSegments, false);
/*      */     }
/*      */     
/*      */     public Builder addEncodedPathSegment(String encodedPathSegment) {
/* 1059 */       if (encodedPathSegment == null) {
/* 1060 */         throw new NullPointerException("encodedPathSegment == null");
/*      */       }
/* 1062 */       push(encodedPathSegment, 0, encodedPathSegment.length(), false, true);
/* 1063 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Builder addEncodedPathSegments(String encodedPathSegments) {
/* 1072 */       if (encodedPathSegments == null) {
/* 1073 */         throw new NullPointerException("encodedPathSegments == null");
/*      */       }
/* 1075 */       return addPathSegments(encodedPathSegments, true);
/*      */     }
/*      */     
/*      */     private Builder addPathSegments(String pathSegments, boolean alreadyEncoded) {
/* 1079 */       int offset = 0;
/*      */       while (true) {
/* 1081 */         int segmentEnd = Util.delimiterOffset(pathSegments, offset, pathSegments.length(), "/\\");
/* 1082 */         boolean addTrailingSlash = (segmentEnd < pathSegments.length());
/* 1083 */         push(pathSegments, offset, segmentEnd, addTrailingSlash, alreadyEncoded);
/* 1084 */         offset = segmentEnd + 1;
/* 1085 */         if (offset > pathSegments.length())
/* 1086 */           return this; 
/*      */       } 
/*      */     }
/*      */     public Builder setPathSegment(int index, String pathSegment) {
/* 1090 */       if (pathSegment == null) throw new NullPointerException("pathSegment == null"); 
/* 1091 */       String canonicalPathSegment = HttpUrl.canonicalize(pathSegment, 0, pathSegment.length(), " \"<>^`{}|/\\?#", false, false, false, true, null);
/*      */       
/* 1093 */       if (isDot(canonicalPathSegment) || isDotDot(canonicalPathSegment)) {
/* 1094 */         throw new IllegalArgumentException("unexpected path segment: " + pathSegment);
/*      */       }
/* 1096 */       this.encodedPathSegments.set(index, canonicalPathSegment);
/* 1097 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setEncodedPathSegment(int index, String encodedPathSegment) {
/* 1101 */       if (encodedPathSegment == null) {
/* 1102 */         throw new NullPointerException("encodedPathSegment == null");
/*      */       }
/* 1104 */       String canonicalPathSegment = HttpUrl.canonicalize(encodedPathSegment, 0, encodedPathSegment.length(), " \"<>^`{}|/\\?#", true, false, false, true, null);
/*      */       
/* 1106 */       this.encodedPathSegments.set(index, canonicalPathSegment);
/* 1107 */       if (isDot(canonicalPathSegment) || isDotDot(canonicalPathSegment)) {
/* 1108 */         throw new IllegalArgumentException("unexpected path segment: " + encodedPathSegment);
/*      */       }
/* 1110 */       return this;
/*      */     }
/*      */     
/*      */     public Builder removePathSegment(int index) {
/* 1114 */       this.encodedPathSegments.remove(index);
/* 1115 */       if (this.encodedPathSegments.isEmpty()) {
/* 1116 */         this.encodedPathSegments.add("");
/*      */       }
/* 1118 */       return this;
/*      */     }
/*      */     
/*      */     public Builder encodedPath(String encodedPath) {
/* 1122 */       if (encodedPath == null) throw new NullPointerException("encodedPath == null"); 
/* 1123 */       if (!encodedPath.startsWith("/")) {
/* 1124 */         throw new IllegalArgumentException("unexpected encodedPath: " + encodedPath);
/*      */       }
/* 1126 */       resolvePath(encodedPath, 0, encodedPath.length());
/* 1127 */       return this;
/*      */     }
/*      */     
/*      */     public Builder query(@Nullable String query) {
/* 1131 */       this
/*      */ 
/*      */         
/* 1134 */         .encodedQueryNamesAndValues = (query != null) ? HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(query, " \"'<>#", false, false, true, true)) : null;
/* 1135 */       return this;
/*      */     }
/*      */     
/*      */     public Builder encodedQuery(@Nullable String encodedQuery) {
/* 1139 */       this
/*      */ 
/*      */         
/* 1142 */         .encodedQueryNamesAndValues = (encodedQuery != null) ? HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(encodedQuery, " \"'<>#", true, false, true, true)) : null;
/* 1143 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public Builder addQueryParameter(String name, @Nullable String value) {
/* 1148 */       if (name == null) throw new NullPointerException("name == null"); 
/* 1149 */       if (this.encodedQueryNamesAndValues == null) this.encodedQueryNamesAndValues = new ArrayList<>(); 
/* 1150 */       this.encodedQueryNamesAndValues.add(
/* 1151 */           HttpUrl.canonicalize(name, " !\"#$&'(),/:;<=>?@[]\\^`{|}~", false, false, true, true));
/* 1152 */       this.encodedQueryNamesAndValues.add((value != null) ? 
/* 1153 */           HttpUrl.canonicalize(value, " !\"#$&'(),/:;<=>?@[]\\^`{|}~", false, false, true, true) : 
/* 1154 */           null);
/* 1155 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public Builder addEncodedQueryParameter(String encodedName, @Nullable String encodedValue) {
/* 1160 */       if (encodedName == null) throw new NullPointerException("encodedName == null"); 
/* 1161 */       if (this.encodedQueryNamesAndValues == null) this.encodedQueryNamesAndValues = new ArrayList<>(); 
/* 1162 */       this.encodedQueryNamesAndValues.add(
/* 1163 */           HttpUrl.canonicalize(encodedName, " \"'<>#&=", true, false, true, true));
/* 1164 */       this.encodedQueryNamesAndValues.add((encodedValue != null) ? 
/* 1165 */           HttpUrl.canonicalize(encodedValue, " \"'<>#&=", true, false, true, true) : 
/* 1166 */           null);
/* 1167 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setQueryParameter(String name, @Nullable String value) {
/* 1171 */       removeAllQueryParameters(name);
/* 1172 */       addQueryParameter(name, value);
/* 1173 */       return this;
/*      */     }
/*      */     
/*      */     public Builder setEncodedQueryParameter(String encodedName, @Nullable String encodedValue) {
/* 1177 */       removeAllEncodedQueryParameters(encodedName);
/* 1178 */       addEncodedQueryParameter(encodedName, encodedValue);
/* 1179 */       return this;
/*      */     }
/*      */     
/*      */     public Builder removeAllQueryParameters(String name) {
/* 1183 */       if (name == null) throw new NullPointerException("name == null"); 
/* 1184 */       if (this.encodedQueryNamesAndValues == null) return this; 
/* 1185 */       String nameToRemove = HttpUrl.canonicalize(name, " !\"#$&'(),/:;<=>?@[]\\^`{|}~", false, false, true, true);
/*      */       
/* 1187 */       removeAllCanonicalQueryParameters(nameToRemove);
/* 1188 */       return this;
/*      */     }
/*      */     
/*      */     public Builder removeAllEncodedQueryParameters(String encodedName) {
/* 1192 */       if (encodedName == null) throw new NullPointerException("encodedName == null"); 
/* 1193 */       if (this.encodedQueryNamesAndValues == null) return this; 
/* 1194 */       removeAllCanonicalQueryParameters(
/* 1195 */           HttpUrl.canonicalize(encodedName, " \"'<>#&=", true, false, true, true));
/* 1196 */       return this;
/*      */     }
/*      */     
/*      */     private void removeAllCanonicalQueryParameters(String canonicalName) {
/* 1200 */       for (int i = this.encodedQueryNamesAndValues.size() - 2; i >= 0; i -= 2) {
/* 1201 */         if (canonicalName.equals(this.encodedQueryNamesAndValues.get(i))) {
/* 1202 */           this.encodedQueryNamesAndValues.remove(i + 1);
/* 1203 */           this.encodedQueryNamesAndValues.remove(i);
/* 1204 */           if (this.encodedQueryNamesAndValues.isEmpty()) {
/* 1205 */             this.encodedQueryNamesAndValues = null;
/*      */             return;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*      */     public Builder fragment(@Nullable String fragment) {
/* 1213 */       this
/*      */         
/* 1215 */         .encodedFragment = (fragment != null) ? HttpUrl.canonicalize(fragment, "", false, false, false, false) : null;
/* 1216 */       return this;
/*      */     }
/*      */     
/*      */     public Builder encodedFragment(@Nullable String encodedFragment) {
/* 1220 */       this
/*      */         
/* 1222 */         .encodedFragment = (encodedFragment != null) ? HttpUrl.canonicalize(encodedFragment, "", true, false, false, false) : null;
/* 1223 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Builder reencodeForUri() {
/*      */       int i;
/*      */       int size;
/* 1231 */       for (i = 0, size = this.encodedPathSegments.size(); i < size; i++) {
/* 1232 */         String pathSegment = this.encodedPathSegments.get(i);
/* 1233 */         this.encodedPathSegments.set(i, 
/* 1234 */             HttpUrl.canonicalize(pathSegment, "[]", true, true, false, true));
/*      */       } 
/* 1236 */       if (this.encodedQueryNamesAndValues != null) {
/* 1237 */         for (i = 0, size = this.encodedQueryNamesAndValues.size(); i < size; i++) {
/* 1238 */           String component = this.encodedQueryNamesAndValues.get(i);
/* 1239 */           if (component != null) {
/* 1240 */             this.encodedQueryNamesAndValues.set(i, 
/* 1241 */                 HttpUrl.canonicalize(component, "\\^`{|}", true, true, true, true));
/*      */           }
/*      */         } 
/*      */       }
/* 1245 */       if (this.encodedFragment != null) {
/* 1246 */         this.encodedFragment = HttpUrl.canonicalize(this.encodedFragment, " \"#<>\\^`{|}", true, true, false, false);
/*      */       }
/*      */       
/* 1249 */       return this;
/*      */     }
/*      */     
/*      */     public HttpUrl build() {
/* 1253 */       if (this.scheme == null) throw new IllegalStateException("scheme == null"); 
/* 1254 */       if (this.host == null) throw new IllegalStateException("host == null"); 
/* 1255 */       return new HttpUrl(this);
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1259 */       StringBuilder result = new StringBuilder();
/* 1260 */       if (this.scheme != null) {
/* 1261 */         result.append(this.scheme);
/* 1262 */         result.append("://");
/*      */       } else {
/* 1264 */         result.append("//");
/*      */       } 
/*      */       
/* 1267 */       if (!this.encodedUsername.isEmpty() || !this.encodedPassword.isEmpty()) {
/* 1268 */         result.append(this.encodedUsername);
/* 1269 */         if (!this.encodedPassword.isEmpty()) {
/* 1270 */           result.append(':');
/* 1271 */           result.append(this.encodedPassword);
/*      */         } 
/* 1273 */         result.append('@');
/*      */       } 
/*      */       
/* 1276 */       if (this.host != null) {
/* 1277 */         if (this.host.indexOf(':') != -1) {
/*      */           
/* 1279 */           result.append('[');
/* 1280 */           result.append(this.host);
/* 1281 */           result.append(']');
/*      */         } else {
/* 1283 */           result.append(this.host);
/*      */         } 
/*      */       }
/*      */       
/* 1287 */       if (this.port != -1 || this.scheme != null) {
/* 1288 */         int effectivePort = effectivePort();
/* 1289 */         if (this.scheme == null || effectivePort != HttpUrl.defaultPort(this.scheme)) {
/* 1290 */           result.append(':');
/* 1291 */           result.append(effectivePort);
/*      */         } 
/*      */       } 
/*      */       
/* 1295 */       HttpUrl.pathSegmentsToString(result, this.encodedPathSegments);
/*      */       
/* 1297 */       if (this.encodedQueryNamesAndValues != null) {
/* 1298 */         result.append('?');
/* 1299 */         HttpUrl.namesAndValuesToQueryString(result, this.encodedQueryNamesAndValues);
/*      */       } 
/*      */       
/* 1302 */       if (this.encodedFragment != null) {
/* 1303 */         result.append('#');
/* 1304 */         result.append(this.encodedFragment);
/*      */       } 
/*      */       
/* 1307 */       return result.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     Builder parse(@Nullable HttpUrl base, String input) {
/* 1313 */       int pos = Util.skipLeadingAsciiWhitespace(input, 0, input.length());
/* 1314 */       int limit = Util.skipTrailingAsciiWhitespace(input, pos, input.length());
/*      */ 
/*      */       
/* 1317 */       int schemeDelimiterOffset = schemeDelimiterOffset(input, pos, limit);
/* 1318 */       if (schemeDelimiterOffset != -1) {
/* 1319 */         if (input.regionMatches(true, pos, "https:", 0, 6)) {
/* 1320 */           this.scheme = "https";
/* 1321 */           pos += "https:".length();
/* 1322 */         } else if (input.regionMatches(true, pos, "http:", 0, 5)) {
/* 1323 */           this.scheme = "http";
/* 1324 */           pos += "http:".length();
/*      */         } else {
/* 1326 */           throw new IllegalArgumentException("Expected URL scheme 'http' or 'https' but was '" + input
/* 1327 */               .substring(0, schemeDelimiterOffset) + "'");
/*      */         } 
/* 1329 */       } else if (base != null) {
/* 1330 */         this.scheme = base.scheme;
/*      */       } else {
/* 1332 */         throw new IllegalArgumentException("Expected URL scheme 'http' or 'https' but no colon was found");
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 1337 */       boolean hasUsername = false;
/* 1338 */       boolean hasPassword = false;
/* 1339 */       int slashCount = slashCount(input, pos, limit);
/* 1340 */       if (slashCount >= 2 || base == null || !base.scheme.equals(this.scheme)) {
/*      */         int componentDelimiterOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1350 */         pos += slashCount;
/*      */         
/*      */         while (true) {
/* 1353 */           componentDelimiterOffset = Util.delimiterOffset(input, pos, limit, "@/\\?#");
/*      */ 
/*      */           
/* 1356 */           int c = (componentDelimiterOffset != limit) ? input.charAt(componentDelimiterOffset) : -1;
/* 1357 */           switch (c) {
/*      */             
/*      */             case 64:
/* 1360 */               if (!hasPassword) {
/* 1361 */                 int passwordColonOffset = Util.delimiterOffset(input, pos, componentDelimiterOffset, ':');
/*      */                 
/* 1363 */                 String canonicalUsername = HttpUrl.canonicalize(input, pos, passwordColonOffset, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true, null);
/*      */                 
/* 1365 */                 this
/*      */                   
/* 1367 */                   .encodedUsername = hasUsername ? (this.encodedUsername + "%40" + canonicalUsername) : canonicalUsername;
/* 1368 */                 if (passwordColonOffset != componentDelimiterOffset) {
/* 1369 */                   hasPassword = true;
/* 1370 */                   this.encodedPassword = HttpUrl.canonicalize(input, passwordColonOffset + 1, componentDelimiterOffset, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true, null);
/*      */                 } 
/*      */ 
/*      */                 
/* 1374 */                 hasUsername = true;
/*      */               } else {
/* 1376 */                 this.encodedPassword += "%40" + HttpUrl.canonicalize(input, pos, componentDelimiterOffset, " \"':;<=>@[]^`{}|/\\?#", true, false, false, true, null);
/*      */               } 
/*      */               
/* 1379 */               pos = componentDelimiterOffset + 1;
/*      */             case -1:
/*      */             case 35:
/*      */             case 47:
/*      */             case 63:
/*      */             case 92:
/*      */               break;
/*      */           } 
/*      */         } 
/* 1388 */         int portColonOffset = portColonOffset(input, pos, componentDelimiterOffset);
/* 1389 */         if (portColonOffset + 1 < componentDelimiterOffset) {
/* 1390 */           this.host = canonicalizeHost(input, pos, portColonOffset);
/* 1391 */           this.port = parsePort(input, portColonOffset + 1, componentDelimiterOffset);
/* 1392 */           if (this.port == -1) {
/* 1393 */             throw new IllegalArgumentException("Invalid URL port: \"" + input
/* 1394 */                 .substring(portColonOffset + 1, componentDelimiterOffset) + '"');
/*      */           }
/*      */         } else {
/* 1397 */           this.host = canonicalizeHost(input, pos, portColonOffset);
/* 1398 */           this.port = HttpUrl.defaultPort(this.scheme);
/*      */         } 
/* 1400 */         if (this.host == null) {
/* 1401 */           throw new IllegalArgumentException("Invalid URL host: \"" + input
/* 1402 */               .substring(pos, portColonOffset) + '"');
/*      */         }
/* 1404 */         pos = componentDelimiterOffset;
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */         
/* 1410 */         this.encodedUsername = base.encodedUsername();
/* 1411 */         this.encodedPassword = base.encodedPassword();
/* 1412 */         this.host = base.host;
/* 1413 */         this.port = base.port;
/* 1414 */         this.encodedPathSegments.clear();
/* 1415 */         this.encodedPathSegments.addAll(base.encodedPathSegments());
/* 1416 */         if (pos == limit || input.charAt(pos) == '#') {
/* 1417 */           encodedQuery(base.encodedQuery());
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1422 */       int pathDelimiterOffset = Util.delimiterOffset(input, pos, limit, "?#");
/* 1423 */       resolvePath(input, pos, pathDelimiterOffset);
/* 1424 */       pos = pathDelimiterOffset;
/*      */ 
/*      */       
/* 1427 */       if (pos < limit && input.charAt(pos) == '?') {
/* 1428 */         int queryDelimiterOffset = Util.delimiterOffset(input, pos, limit, '#');
/* 1429 */         this.encodedQueryNamesAndValues = HttpUrl.queryStringToNamesAndValues(HttpUrl.canonicalize(input, pos + 1, queryDelimiterOffset, " \"'<>#", true, false, true, true, null));
/*      */         
/* 1431 */         pos = queryDelimiterOffset;
/*      */       } 
/*      */ 
/*      */       
/* 1435 */       if (pos < limit && input.charAt(pos) == '#') {
/* 1436 */         this.encodedFragment = HttpUrl.canonicalize(input, pos + 1, limit, "", true, false, false, false, null);
/*      */       }
/*      */ 
/*      */       
/* 1440 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     private void resolvePath(String input, int pos, int limit) {
/* 1445 */       if (pos == limit) {
/*      */         return;
/*      */       }
/*      */       
/* 1449 */       char c = input.charAt(pos);
/* 1450 */       if (c == '/' || c == '\\') {
/*      */         
/* 1452 */         this.encodedPathSegments.clear();
/* 1453 */         this.encodedPathSegments.add("");
/* 1454 */         pos++;
/*      */       } else {
/*      */         
/* 1457 */         this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
/*      */       } 
/*      */ 
/*      */       
/* 1461 */       for (int i = pos; i < limit; ) {
/* 1462 */         int pathSegmentDelimiterOffset = Util.delimiterOffset(input, i, limit, "/\\");
/* 1463 */         boolean segmentHasTrailingSlash = (pathSegmentDelimiterOffset < limit);
/* 1464 */         push(input, i, pathSegmentDelimiterOffset, segmentHasTrailingSlash, true);
/* 1465 */         i = pathSegmentDelimiterOffset;
/* 1466 */         if (segmentHasTrailingSlash) i++;
/*      */       
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     private void push(String input, int pos, int limit, boolean addTrailingSlash, boolean alreadyEncoded) {
/* 1473 */       String segment = HttpUrl.canonicalize(input, pos, limit, " \"<>^`{}|/\\?#", alreadyEncoded, false, false, true, null);
/*      */       
/* 1475 */       if (isDot(segment)) {
/*      */         return;
/*      */       }
/* 1478 */       if (isDotDot(segment)) {
/* 1479 */         pop();
/*      */         return;
/*      */       } 
/* 1482 */       if (((String)this.encodedPathSegments.get(this.encodedPathSegments.size() - 1)).isEmpty()) {
/* 1483 */         this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, segment);
/*      */       } else {
/* 1485 */         this.encodedPathSegments.add(segment);
/*      */       } 
/* 1487 */       if (addTrailingSlash) {
/* 1488 */         this.encodedPathSegments.add("");
/*      */       }
/*      */     }
/*      */     
/*      */     private boolean isDot(String input) {
/* 1493 */       return (input.equals(".") || input.equalsIgnoreCase("%2e"));
/*      */     }
/*      */     
/*      */     private boolean isDotDot(String input) {
/* 1497 */       return (input.equals("..") || input
/* 1498 */         .equalsIgnoreCase("%2e.") || input
/* 1499 */         .equalsIgnoreCase(".%2e") || input
/* 1500 */         .equalsIgnoreCase("%2e%2e"));
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
/*      */     private void pop() {
/* 1514 */       String removed = this.encodedPathSegments.remove(this.encodedPathSegments.size() - 1);
/*      */ 
/*      */       
/* 1517 */       if (removed.isEmpty() && !this.encodedPathSegments.isEmpty()) {
/* 1518 */         this.encodedPathSegments.set(this.encodedPathSegments.size() - 1, "");
/*      */       } else {
/* 1520 */         this.encodedPathSegments.add("");
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static int schemeDelimiterOffset(String input, int pos, int limit) {
/* 1529 */       if (limit - pos < 2) return -1;
/*      */       
/* 1531 */       char c0 = input.charAt(pos);
/* 1532 */       if ((c0 < 'a' || c0 > 'z') && (c0 < 'A' || c0 > 'Z')) return -1;
/*      */       
/* 1534 */       for (int i = pos + 1; i < limit; ) {
/* 1535 */         char c = input.charAt(i);
/*      */         
/* 1537 */         if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '+' || c == '-' || c == '.') {
/*      */           i++;
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/* 1544 */         if (c == ':') {
/* 1545 */           return i;
/*      */         }
/* 1547 */         return -1;
/*      */       } 
/*      */ 
/*      */       
/* 1551 */       return -1;
/*      */     }
/*      */ 
/*      */     
/*      */     private static int slashCount(String input, int pos, int limit) {
/* 1556 */       int slashCount = 0;
/* 1557 */       while (pos < limit) {
/* 1558 */         char c = input.charAt(pos);
/* 1559 */         if (c == '\\' || c == '/') {
/* 1560 */           slashCount++;
/* 1561 */           pos++;
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/* 1566 */       return slashCount;
/*      */     }
/*      */ 
/*      */     
/*      */     private static int portColonOffset(String input, int pos, int limit) {
/* 1571 */       for (int i = pos; i < limit; i++) {
/* 1572 */         switch (input.charAt(i)) { case '[':
/*      */             do {  }
/* 1574 */             while (++i < limit && 
/* 1575 */               input.charAt(i) != ']');
/*      */             break;
/*      */           
/*      */           case ':':
/* 1579 */             return i; }
/*      */       
/*      */       } 
/* 1582 */       return limit;
/*      */     }
/*      */ 
/*      */     
/*      */     @Nullable
/*      */     private static String canonicalizeHost(String input, int pos, int limit) {
/* 1588 */       String percentDecoded = HttpUrl.percentDecode(input, pos, limit, false);
/* 1589 */       return Util.canonicalizeHost(percentDecoded);
/*      */     }
/*      */ 
/*      */     
/*      */     private static int parsePort(String input, int pos, int limit) {
/*      */       try {
/* 1595 */         String portString = HttpUrl.canonicalize(input, pos, limit, "", false, false, false, true, null);
/* 1596 */         int i = Integer.parseInt(portString);
/* 1597 */         if (i > 0 && i <= 65535) return i; 
/* 1598 */         return -1;
/* 1599 */       } catch (NumberFormatException e) {
/* 1600 */         return -1;
/*      */       } 
/*      */     } }
/*      */ 
/*      */   
/*      */   static String percentDecode(String encoded, boolean plusIsSpace) {
/* 1606 */     return percentDecode(encoded, 0, encoded.length(), plusIsSpace);
/*      */   }
/*      */   
/*      */   private List<String> percentDecode(List<String> list, boolean plusIsSpace) {
/* 1610 */     int size = list.size();
/* 1611 */     List<String> result = new ArrayList<>(size);
/* 1612 */     for (int i = 0; i < size; i++) {
/* 1613 */       String s = list.get(i);
/* 1614 */       result.add((s != null) ? percentDecode(s, plusIsSpace) : null);
/*      */     } 
/* 1616 */     return Collections.unmodifiableList(result);
/*      */   }
/*      */   
/*      */   static String percentDecode(String encoded, int pos, int limit, boolean plusIsSpace) {
/* 1620 */     for (int i = pos; i < limit; i++) {
/* 1621 */       char c = encoded.charAt(i);
/* 1622 */       if (c == '%' || (c == '+' && plusIsSpace)) {
/*      */         
/* 1624 */         Buffer out = new Buffer();
/* 1625 */         out.writeUtf8(encoded, pos, i);
/* 1626 */         percentDecode(out, encoded, i, limit, plusIsSpace);
/* 1627 */         return out.readUtf8();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1632 */     return encoded.substring(pos, limit);
/*      */   }
/*      */   
/*      */   static void percentDecode(Buffer out, String encoded, int pos, int limit, boolean plusIsSpace) {
/*      */     int i;
/* 1637 */     for (i = pos; i < limit; i += Character.charCount(SYNTHETIC_LOCAL_VARIABLE_5)) {
/* 1638 */       int codePoint = encoded.codePointAt(i);
/* 1639 */       if (codePoint == 37 && i + 2 < limit) {
/* 1640 */         int d1 = Util.decodeHexDigit(encoded.charAt(i + 1));
/* 1641 */         int d2 = Util.decodeHexDigit(encoded.charAt(i + 2));
/* 1642 */         if (d1 != -1 && d2 != -1) {
/* 1643 */           out.writeByte((d1 << 4) + d2);
/* 1644 */           i += 2;
/*      */           continue;
/*      */         } 
/* 1647 */       } else if (codePoint == 43 && plusIsSpace) {
/* 1648 */         out.writeByte(32);
/*      */         continue;
/*      */       } 
/* 1651 */       out.writeUtf8CodePoint(codePoint);
/*      */       continue;
/*      */     } 
/*      */   }
/*      */   static boolean percentEncoded(String encoded, int pos, int limit) {
/* 1656 */     return (pos + 2 < limit && encoded
/* 1657 */       .charAt(pos) == '%' && 
/* 1658 */       Util.decodeHexDigit(encoded.charAt(pos + 1)) != -1 && 
/* 1659 */       Util.decodeHexDigit(encoded.charAt(pos + 2)) != -1);
/*      */   }
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
/*      */   static String canonicalize(String input, int pos, int limit, String encodeSet, boolean alreadyEncoded, boolean strict, boolean plusIsSpace, boolean asciiOnly, @Nullable Charset charset) {
/*      */     int i;
/* 1683 */     for (i = pos; i < limit; i += Character.charCount(codePoint)) {
/* 1684 */       int codePoint = input.codePointAt(i);
/* 1685 */       if (codePoint < 32 || codePoint == 127 || (codePoint >= 128 && asciiOnly) || encodeSet
/*      */ 
/*      */         
/* 1688 */         .indexOf(codePoint) != -1 || (codePoint == 37 && (!alreadyEncoded || (strict && 
/* 1689 */         !percentEncoded(input, i, limit)))) || (codePoint == 43 && plusIsSpace)) {
/*      */ 
/*      */         
/* 1692 */         Buffer out = new Buffer();
/* 1693 */         out.writeUtf8(input, pos, i);
/* 1694 */         canonicalize(out, input, i, limit, encodeSet, alreadyEncoded, strict, plusIsSpace, asciiOnly, charset);
/*      */         
/* 1696 */         return out.readUtf8();
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 1701 */     return input.substring(pos, limit);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static void canonicalize(Buffer out, String input, int pos, int limit, String encodeSet, boolean alreadyEncoded, boolean strict, boolean plusIsSpace, boolean asciiOnly, @Nullable Charset charset) {
/* 1707 */     Buffer encodedCharBuffer = null;
/*      */     int i;
/* 1709 */     for (i = pos; i < limit; i += Character.charCount(codePoint)) {
/* 1710 */       int codePoint = input.codePointAt(i);
/* 1711 */       if (!alreadyEncoded || (codePoint != 9 && codePoint != 10 && codePoint != 12 && codePoint != 13))
/*      */       {
/*      */         
/* 1714 */         if (codePoint == 43 && plusIsSpace) {
/*      */           
/* 1716 */           out.writeUtf8(alreadyEncoded ? "+" : "%2B");
/* 1717 */         } else if (codePoint < 32 || codePoint == 127 || (codePoint >= 128 && asciiOnly) || encodeSet
/*      */ 
/*      */           
/* 1720 */           .indexOf(codePoint) != -1 || (codePoint == 37 && (!alreadyEncoded || (strict && 
/* 1721 */           !percentEncoded(input, i, limit))))) {
/*      */           
/* 1723 */           if (encodedCharBuffer == null) {
/* 1724 */             encodedCharBuffer = new Buffer();
/*      */           }
/*      */           
/* 1727 */           if (charset == null || charset.equals(StandardCharsets.UTF_8)) {
/* 1728 */             encodedCharBuffer.writeUtf8CodePoint(codePoint);
/*      */           } else {
/* 1730 */             encodedCharBuffer.writeString(input, i, i + Character.charCount(codePoint), charset);
/*      */           } 
/*      */           
/* 1733 */           while (!encodedCharBuffer.exhausted()) {
/* 1734 */             int b = encodedCharBuffer.readByte() & 0xFF;
/* 1735 */             out.writeByte(37);
/* 1736 */             out.writeByte(HEX_DIGITS[b >> 4 & 0xF]);
/* 1737 */             out.writeByte(HEX_DIGITS[b & 0xF]);
/*      */           } 
/*      */         } else {
/*      */           
/* 1741 */           out.writeUtf8CodePoint(codePoint);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   static String canonicalize(String input, String encodeSet, boolean alreadyEncoded, boolean strict, boolean plusIsSpace, boolean asciiOnly, @Nullable Charset charset) {
/* 1748 */     return canonicalize(input, 0, input.length(), encodeSet, alreadyEncoded, strict, plusIsSpace, asciiOnly, charset);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static String canonicalize(String input, String encodeSet, boolean alreadyEncoded, boolean strict, boolean plusIsSpace, boolean asciiOnly) {
/* 1754 */     return canonicalize(input, 0, input
/* 1755 */         .length(), encodeSet, alreadyEncoded, strict, plusIsSpace, asciiOnly, null);
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\HttpUrl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */