/*     */ package okhttp3;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import javax.annotation.Nullable;
/*     */ import okhttp3.internal.Util;
/*     */ import okio.BufferedSink;
/*     */ import okio.ByteString;
/*     */ import okio.Okio;
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
/*     */ public abstract class RequestBody
/*     */ {
/*     */   @Nullable
/*     */   public abstract MediaType contentType();
/*     */   
/*     */   public long contentLength() throws IOException {
/*  39 */     return -1L;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void writeTo(BufferedSink paramBufferedSink) throws IOException;
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBody create(@Nullable MediaType contentType, String content) {
/*  50 */     Charset charset = StandardCharsets.UTF_8;
/*  51 */     if (contentType != null) {
/*  52 */       charset = contentType.charset();
/*  53 */       if (charset == null) {
/*  54 */         charset = StandardCharsets.UTF_8;
/*  55 */         contentType = MediaType.parse(contentType + "; charset=utf-8");
/*     */       } 
/*     */     } 
/*  58 */     byte[] bytes = content.getBytes(charset);
/*  59 */     return create(contentType, bytes);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBody create(@Nullable final MediaType contentType, final ByteString content) {
/*  65 */     return new RequestBody() { @Nullable
/*     */         public MediaType contentType() {
/*  67 */           return contentType;
/*     */         }
/*     */         
/*     */         public long contentLength() throws IOException {
/*  71 */           return content.size();
/*     */         }
/*     */         
/*     */         public void writeTo(BufferedSink sink) throws IOException {
/*  75 */           sink.write(content);
/*     */         } }
/*     */       ;
/*     */   }
/*     */ 
/*     */   
/*     */   public static RequestBody create(@Nullable MediaType contentType, byte[] content) {
/*  82 */     return create(contentType, content, 0, content.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBody create(@Nullable final MediaType contentType, final byte[] content, final int offset, final int byteCount) {
/*  88 */     if (content == null) throw new NullPointerException("content == null"); 
/*  89 */     Util.checkOffsetAndCount(content.length, offset, byteCount);
/*  90 */     return new RequestBody() { @Nullable
/*     */         public MediaType contentType() {
/*  92 */           return contentType;
/*     */         }
/*     */         
/*     */         public long contentLength() {
/*  96 */           return byteCount;
/*     */         }
/*     */         
/*     */         public void writeTo(BufferedSink sink) throws IOException {
/* 100 */           sink.write(content, offset, byteCount);
/*     */         } }
/*     */       ;
/*     */   }
/*     */ 
/*     */   
/*     */   public static RequestBody create(@Nullable final MediaType contentType, final File file) {
/* 107 */     if (file == null) throw new NullPointerException("file == null");
/*     */     
/* 109 */     return new RequestBody() { @Nullable
/*     */         public MediaType contentType() {
/* 111 */           return contentType;
/*     */         }
/*     */         
/*     */         public long contentLength() {
/* 115 */           return file.length();
/*     */         }
/*     */         
/*     */         public void writeTo(BufferedSink sink) throws IOException {
/* 119 */           try (Source source = Okio.source(file)) {
/* 120 */             sink.writeAll(source);
/*     */           } 
/*     */         } }
/*     */       ;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\RequestBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */