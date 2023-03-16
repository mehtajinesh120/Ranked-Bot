/*     */ package okhttp3;
/*     */ 
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import javax.annotation.Nullable;
/*     */ import okhttp3.internal.Util;
/*     */ import okio.Buffer;
/*     */ import okio.BufferedSource;
/*     */ import okio.ByteString;
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
/*     */ public abstract class ResponseBody
/*     */   implements Closeable
/*     */ {
/*     */   @Nullable
/*     */   private Reader reader;
/*     */   
/*     */   public final InputStream byteStream() {
/* 116 */     return source().inputStream();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final byte[] bytes() throws IOException {
/*     */     byte[] bytes;
/* 129 */     long contentLength = contentLength();
/* 130 */     if (contentLength > 2147483647L) {
/* 131 */       throw new IOException("Cannot buffer entire body for content length: " + contentLength);
/*     */     }
/*     */ 
/*     */     
/* 135 */     BufferedSource source = source(); Throwable throwable = null; 
/* 136 */     try { bytes = source.readByteArray(); } catch (Throwable throwable1) { throwable = throwable1 = null; throw throwable1; }
/* 137 */     finally { if (source != null) $closeResource(throwable, (AutoCloseable)source);  }
/* 138 */      if (contentLength != -1L && contentLength != bytes.length) {
/* 139 */       throw new IOException("Content-Length (" + contentLength + ") and stream length (" + bytes.length + ") disagree");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 145 */     return bytes;
/*     */   }
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
/*     */   public final Reader charStream() {
/* 160 */     Reader r = this.reader;
/* 161 */     return (r != null) ? r : (this.reader = new BomAwareReader(source(), charset()));
/*     */   }
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
/*     */   public final String string() throws IOException {
/* 180 */     BufferedSource source = source(); Throwable throwable = null; 
/* 181 */     try { Charset charset = Util.bomAwareCharset(source, charset());
/* 182 */       return source.readString(charset); } catch (Throwable throwable1) { throwable = throwable1 = null; throw throwable1; }
/* 183 */     finally { if (source != null) $closeResource(throwable, (AutoCloseable)source);  }
/*     */   
/*     */   }
/*     */   private Charset charset() {
/* 187 */     MediaType contentType = contentType();
/* 188 */     return (contentType != null) ? contentType.charset(StandardCharsets.UTF_8) : StandardCharsets.UTF_8;
/*     */   }
/*     */   
/*     */   public void close() {
/* 192 */     Util.closeQuietly((Closeable)source());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ResponseBody create(@Nullable MediaType contentType, String content) {
/* 200 */     Charset charset = StandardCharsets.UTF_8;
/* 201 */     if (contentType != null) {
/* 202 */       charset = contentType.charset();
/* 203 */       if (charset == null) {
/* 204 */         charset = StandardCharsets.UTF_8;
/* 205 */         contentType = MediaType.parse(contentType + "; charset=utf-8");
/*     */       } 
/*     */     } 
/* 208 */     Buffer buffer = (new Buffer()).writeString(content, charset);
/* 209 */     return create(contentType, buffer.size(), (BufferedSource)buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ResponseBody create(@Nullable MediaType contentType, byte[] content) {
/* 214 */     Buffer buffer = (new Buffer()).write(content);
/* 215 */     return create(contentType, content.length, (BufferedSource)buffer);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ResponseBody create(@Nullable MediaType contentType, ByteString content) {
/* 220 */     Buffer buffer = (new Buffer()).write(content);
/* 221 */     return create(contentType, content.size(), (BufferedSource)buffer);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ResponseBody create(@Nullable final MediaType contentType, final long contentLength, final BufferedSource content) {
/* 227 */     if (content == null) throw new NullPointerException("source == null"); 
/* 228 */     return new ResponseBody() { @Nullable
/*     */         public MediaType contentType() {
/* 230 */           return contentType;
/*     */         }
/*     */         
/*     */         public long contentLength() {
/* 234 */           return contentLength;
/*     */         }
/*     */         
/*     */         public BufferedSource source() {
/* 238 */           return content;
/*     */         } }
/*     */       ;
/*     */   }
/*     */   @Nullable
/*     */   public abstract MediaType contentType();
/*     */   public abstract long contentLength();
/*     */   public abstract BufferedSource source();
/*     */   
/*     */   static final class BomAwareReader extends Reader { private final BufferedSource source;
/*     */     private final Charset charset;
/*     */     
/*     */     BomAwareReader(BufferedSource source, Charset charset) {
/* 251 */       this.source = source;
/* 252 */       this.charset = charset;
/*     */     } private boolean closed; @Nullable
/*     */     private Reader delegate;
/*     */     public int read(char[] cbuf, int off, int len) throws IOException {
/* 256 */       if (this.closed) throw new IOException("Stream closed");
/*     */       
/* 258 */       Reader delegate = this.delegate;
/* 259 */       if (delegate == null) {
/* 260 */         Charset charset = Util.bomAwareCharset(this.source, this.charset);
/* 261 */         delegate = this.delegate = new InputStreamReader(this.source.inputStream(), charset);
/*     */       } 
/* 263 */       return delegate.read(cbuf, off, len);
/*     */     }
/*     */     
/*     */     public void close() throws IOException {
/* 267 */       this.closed = true;
/* 268 */       if (this.delegate != null) {
/* 269 */         this.delegate.close();
/*     */       } else {
/* 271 */         this.source.close();
/*     */       } 
/*     */     } }
/*     */ 
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\ResponseBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */