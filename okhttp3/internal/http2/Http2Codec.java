/*     */ package okhttp3.internal.http2;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ProtocolException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import okhttp3.Headers;
/*     */ import okhttp3.Interceptor;
/*     */ import okhttp3.OkHttpClient;
/*     */ import okhttp3.Protocol;
/*     */ import okhttp3.Request;
/*     */ import okhttp3.Response;
/*     */ import okhttp3.ResponseBody;
/*     */ import okhttp3.internal.Internal;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.connection.StreamAllocation;
/*     */ import okhttp3.internal.http.HttpCodec;
/*     */ import okhttp3.internal.http.HttpHeaders;
/*     */ import okhttp3.internal.http.RealResponseBody;
/*     */ import okhttp3.internal.http.RequestLine;
/*     */ import okhttp3.internal.http.StatusLine;
/*     */ import okio.Buffer;
/*     */ import okio.ForwardingSource;
/*     */ import okio.Okio;
/*     */ import okio.Sink;
/*     */ import okio.Source;
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
/*     */ public final class Http2Codec
/*     */   implements HttpCodec
/*     */ {
/*     */   private static final String CONNECTION = "connection";
/*     */   private static final String HOST = "host";
/*     */   private static final String KEEP_ALIVE = "keep-alive";
/*     */   private static final String PROXY_CONNECTION = "proxy-connection";
/*     */   private static final String TRANSFER_ENCODING = "transfer-encoding";
/*     */   private static final String TE = "te";
/*     */   private static final String ENCODING = "encoding";
/*     */   private static final String UPGRADE = "upgrade";
/*  68 */   private static final List<String> HTTP_2_SKIPPED_REQUEST_HEADERS = Util.immutableList((Object[])new String[] { "connection", "host", "keep-alive", "proxy-connection", "te", "transfer-encoding", "encoding", "upgrade", ":method", ":path", ":scheme", ":authority" });
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
/*  81 */   private static final List<String> HTTP_2_SKIPPED_RESPONSE_HEADERS = Util.immutableList((Object[])new String[] { "connection", "host", "keep-alive", "proxy-connection", "te", "transfer-encoding", "encoding", "upgrade" });
/*     */ 
/*     */   
/*     */   private final Interceptor.Chain chain;
/*     */ 
/*     */   
/*     */   final StreamAllocation streamAllocation;
/*     */ 
/*     */   
/*     */   private final Http2Connection connection;
/*     */   
/*     */   private volatile Http2Stream stream;
/*     */   
/*     */   private final Protocol protocol;
/*     */   
/*     */   private volatile boolean canceled;
/*     */ 
/*     */   
/*     */   public Http2Codec(OkHttpClient client, Interceptor.Chain chain, StreamAllocation streamAllocation, Http2Connection connection) {
/* 100 */     this.chain = chain;
/* 101 */     this.streamAllocation = streamAllocation;
/* 102 */     this.connection = connection;
/* 103 */     this
/*     */       
/* 105 */       .protocol = client.protocols().contains(Protocol.H2_PRIOR_KNOWLEDGE) ? Protocol.H2_PRIOR_KNOWLEDGE : Protocol.HTTP_2;
/*     */   }
/*     */   
/*     */   public Sink createRequestBody(Request request, long contentLength) {
/* 109 */     return this.stream.getSink();
/*     */   }
/*     */   
/*     */   public void writeRequestHeaders(Request request) throws IOException {
/* 113 */     if (this.stream != null)
/*     */       return; 
/* 115 */     boolean hasRequestBody = (request.body() != null);
/* 116 */     List<Header> requestHeaders = http2HeadersList(request);
/* 117 */     this.stream = this.connection.newStream(requestHeaders, hasRequestBody);
/*     */ 
/*     */     
/* 120 */     if (this.canceled) {
/* 121 */       this.stream.closeLater(ErrorCode.CANCEL);
/* 122 */       throw new IOException("Canceled");
/*     */     } 
/* 124 */     this.stream.readTimeout().timeout(this.chain.readTimeoutMillis(), TimeUnit.MILLISECONDS);
/* 125 */     this.stream.writeTimeout().timeout(this.chain.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
/*     */   }
/*     */   
/*     */   public void flushRequest() throws IOException {
/* 129 */     this.connection.flush();
/*     */   }
/*     */   
/*     */   public void finishRequest() throws IOException {
/* 133 */     this.stream.getSink().close();
/*     */   }
/*     */   
/*     */   public Response.Builder readResponseHeaders(boolean expectContinue) throws IOException {
/* 137 */     Headers headers = this.stream.takeHeaders();
/* 138 */     Response.Builder responseBuilder = readHttp2HeadersList(headers, this.protocol);
/* 139 */     if (expectContinue && Internal.instance.code(responseBuilder) == 100) {
/* 140 */       return null;
/*     */     }
/* 142 */     return responseBuilder;
/*     */   }
/*     */   
/*     */   public static List<Header> http2HeadersList(Request request) {
/* 146 */     Headers headers = request.headers();
/* 147 */     List<Header> result = new ArrayList<>(headers.size() + 4);
/* 148 */     result.add(new Header(Header.TARGET_METHOD, request.method()));
/* 149 */     result.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(request.url())));
/* 150 */     String host = request.header("Host");
/* 151 */     if (host != null) {
/* 152 */       result.add(new Header(Header.TARGET_AUTHORITY, host));
/*     */     }
/* 154 */     result.add(new Header(Header.TARGET_SCHEME, request.url().scheme()));
/*     */     
/* 156 */     for (int i = 0, size = headers.size(); i < size; i++) {
/*     */       
/* 158 */       String name = headers.name(i).toLowerCase(Locale.US);
/* 159 */       if (!HTTP_2_SKIPPED_REQUEST_HEADERS.contains(name) || (name
/* 160 */         .equals("te") && headers.value(i).equals("trailers"))) {
/* 161 */         result.add(new Header(name, headers.value(i)));
/*     */       }
/*     */     } 
/* 164 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Response.Builder readHttp2HeadersList(Headers headerBlock, Protocol protocol) throws IOException {
/* 170 */     StatusLine statusLine = null;
/* 171 */     Headers.Builder headersBuilder = new Headers.Builder();
/* 172 */     for (int i = 0, size = headerBlock.size(); i < size; i++) {
/* 173 */       String name = headerBlock.name(i);
/* 174 */       String value = headerBlock.value(i);
/* 175 */       if (name.equals(":status")) {
/* 176 */         statusLine = StatusLine.parse("HTTP/1.1 " + value);
/* 177 */       } else if (!HTTP_2_SKIPPED_RESPONSE_HEADERS.contains(name)) {
/* 178 */         Internal.instance.addLenient(headersBuilder, name, value);
/*     */       } 
/*     */     } 
/* 181 */     if (statusLine == null) throw new ProtocolException("Expected ':status' header not present");
/*     */     
/* 183 */     return (new Response.Builder())
/* 184 */       .protocol(protocol)
/* 185 */       .code(statusLine.code)
/* 186 */       .message(statusLine.message)
/* 187 */       .headers(headersBuilder.build());
/*     */   }
/*     */   
/*     */   public ResponseBody openResponseBody(Response response) throws IOException {
/* 191 */     this.streamAllocation.eventListener.responseBodyStart(this.streamAllocation.call);
/* 192 */     String contentType = response.header("Content-Type");
/* 193 */     long contentLength = HttpHeaders.contentLength(response);
/* 194 */     StreamFinishingSource streamFinishingSource = new StreamFinishingSource(this.stream.getSource());
/* 195 */     return (ResponseBody)new RealResponseBody(contentType, contentLength, Okio.buffer((Source)streamFinishingSource));
/*     */   }
/*     */   
/*     */   public Headers trailers() throws IOException {
/* 199 */     return this.stream.trailers();
/*     */   }
/*     */   
/*     */   public void cancel() {
/* 203 */     this.canceled = true;
/* 204 */     if (this.stream != null) this.stream.closeLater(ErrorCode.CANCEL); 
/*     */   }
/*     */   
/*     */   class StreamFinishingSource extends ForwardingSource {
/*     */     boolean completed = false;
/* 209 */     long bytesRead = 0L;
/*     */     
/*     */     StreamFinishingSource(Source delegate) {
/* 212 */       super(delegate);
/*     */     }
/*     */     
/*     */     public long read(Buffer sink, long byteCount) throws IOException {
/*     */       try {
/* 217 */         long read = delegate().read(sink, byteCount);
/* 218 */         if (read > 0L) {
/* 219 */           this.bytesRead += read;
/*     */         }
/* 221 */         return read;
/* 222 */       } catch (IOException e) {
/* 223 */         endOfInput(e);
/* 224 */         throw e;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 229 */       super.close();
/* 230 */       endOfInput(null);
/*     */     }
/*     */     
/*     */     private void endOfInput(IOException e) {
/* 234 */       if (this.completed)
/* 235 */         return;  this.completed = true;
/* 236 */       Http2Codec.this.streamAllocation.streamFinished(false, Http2Codec.this, this.bytesRead, e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http2\Http2Codec.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */