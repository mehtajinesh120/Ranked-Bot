/*     */ package okhttp3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import javax.annotation.Nullable;
/*     */ import okhttp3.internal.Util;
/*     */ import okio.Buffer;
/*     */ import okio.BufferedSink;
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
/*     */ public final class MultipartBody
/*     */   extends RequestBody
/*     */ {
/*  35 */   public static final MediaType MIXED = MediaType.get("multipart/mixed");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static final MediaType ALTERNATIVE = MediaType.get("multipart/alternative");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static final MediaType DIGEST = MediaType.get("multipart/digest");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public static final MediaType PARALLEL = MediaType.get("multipart/parallel");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  62 */   public static final MediaType FORM = MediaType.get("multipart/form-data");
/*     */   
/*  64 */   private static final byte[] COLONSPACE = new byte[] { 58, 32 };
/*  65 */   private static final byte[] CRLF = new byte[] { 13, 10 };
/*  66 */   private static final byte[] DASHDASH = new byte[] { 45, 45 };
/*     */   
/*     */   private final ByteString boundary;
/*     */   private final MediaType originalType;
/*     */   private final MediaType contentType;
/*     */   private final List<Part> parts;
/*  72 */   private long contentLength = -1L;
/*     */   
/*     */   MultipartBody(ByteString boundary, MediaType type, List<Part> parts) {
/*  75 */     this.boundary = boundary;
/*  76 */     this.originalType = type;
/*  77 */     this.contentType = MediaType.get(type + "; boundary=" + boundary.utf8());
/*  78 */     this.parts = Util.immutableList(parts);
/*     */   }
/*     */   
/*     */   public MediaType type() {
/*  82 */     return this.originalType;
/*     */   }
/*     */   
/*     */   public String boundary() {
/*  86 */     return this.boundary.utf8();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  91 */     return this.parts.size();
/*     */   }
/*     */   
/*     */   public List<Part> parts() {
/*  95 */     return this.parts;
/*     */   }
/*     */   
/*     */   public Part part(int index) {
/*  99 */     return this.parts.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public MediaType contentType() {
/* 104 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public long contentLength() throws IOException {
/* 108 */     long result = this.contentLength;
/* 109 */     if (result != -1L) return result; 
/* 110 */     return this.contentLength = writeOrCountBytes(null, true);
/*     */   }
/*     */   
/*     */   public void writeTo(BufferedSink sink) throws IOException {
/* 114 */     writeOrCountBytes(sink, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long writeOrCountBytes(@Nullable BufferedSink sink, boolean countBytes) throws IOException {
/*     */     Buffer buffer1;
/* 125 */     long byteCount = 0L;
/*     */     
/* 127 */     Buffer byteCountBuffer = null;
/* 128 */     if (countBytes) {
/* 129 */       buffer1 = byteCountBuffer = new Buffer();
/*     */     }
/*     */     
/* 132 */     for (int p = 0, partCount = this.parts.size(); p < partCount; p++) {
/* 133 */       Part part = this.parts.get(p);
/* 134 */       Headers headers = part.headers;
/* 135 */       RequestBody body = part.body;
/*     */       
/* 137 */       buffer1.write(DASHDASH);
/* 138 */       buffer1.write(this.boundary);
/* 139 */       buffer1.write(CRLF);
/*     */       
/* 141 */       if (headers != null) {
/* 142 */         for (int h = 0, headerCount = headers.size(); h < headerCount; h++) {
/* 143 */           buffer1.writeUtf8(headers.name(h))
/* 144 */             .write(COLONSPACE)
/* 145 */             .writeUtf8(headers.value(h))
/* 146 */             .write(CRLF);
/*     */         }
/*     */       }
/*     */       
/* 150 */       MediaType contentType = body.contentType();
/* 151 */       if (contentType != null) {
/* 152 */         buffer1.writeUtf8("Content-Type: ")
/* 153 */           .writeUtf8(contentType.toString())
/* 154 */           .write(CRLF);
/*     */       }
/*     */       
/* 157 */       long contentLength = body.contentLength();
/* 158 */       if (contentLength != -1L) {
/* 159 */         buffer1.writeUtf8("Content-Length: ")
/* 160 */           .writeDecimalLong(contentLength)
/* 161 */           .write(CRLF);
/* 162 */       } else if (countBytes) {
/*     */         
/* 164 */         byteCountBuffer.clear();
/* 165 */         return -1L;
/*     */       } 
/*     */       
/* 168 */       buffer1.write(CRLF);
/*     */       
/* 170 */       if (countBytes) {
/* 171 */         byteCount += contentLength;
/*     */       } else {
/* 173 */         body.writeTo((BufferedSink)buffer1);
/*     */       } 
/*     */       
/* 176 */       buffer1.write(CRLF);
/*     */     } 
/*     */     
/* 179 */     buffer1.write(DASHDASH);
/* 180 */     buffer1.write(this.boundary);
/* 181 */     buffer1.write(DASHDASH);
/* 182 */     buffer1.write(CRLF);
/*     */     
/* 184 */     if (countBytes) {
/* 185 */       byteCount += byteCountBuffer.size();
/* 186 */       byteCountBuffer.clear();
/*     */     } 
/*     */     
/* 189 */     return byteCount;
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
/*     */   static void appendQuotedString(StringBuilder target, String key) {
/* 203 */     target.append('"');
/* 204 */     for (int i = 0, len = key.length(); i < len; i++) {
/* 205 */       char ch = key.charAt(i);
/* 206 */       switch (ch) {
/*     */         case '\n':
/* 208 */           target.append("%0A");
/*     */           break;
/*     */         case '\r':
/* 211 */           target.append("%0D");
/*     */           break;
/*     */         case '"':
/* 214 */           target.append("%22");
/*     */           break;
/*     */         default:
/* 217 */           target.append(ch);
/*     */           break;
/*     */       } 
/*     */     } 
/* 221 */     target.append('"');
/*     */   }
/*     */   public static final class Part { @Nullable
/*     */     final Headers headers;
/*     */     public static Part create(RequestBody body) {
/* 226 */       return create(null, body);
/*     */     }
/*     */     final RequestBody body;
/*     */     public static Part create(@Nullable Headers headers, RequestBody body) {
/* 230 */       if (body == null) {
/* 231 */         throw new NullPointerException("body == null");
/*     */       }
/* 233 */       if (headers != null && headers.get("Content-Type") != null) {
/* 234 */         throw new IllegalArgumentException("Unexpected header: Content-Type");
/*     */       }
/* 236 */       if (headers != null && headers.get("Content-Length") != null) {
/* 237 */         throw new IllegalArgumentException("Unexpected header: Content-Length");
/*     */       }
/* 239 */       return new Part(headers, body);
/*     */     }
/*     */     
/*     */     public static Part createFormData(String name, String value) {
/* 243 */       return createFormData(name, null, RequestBody.create((MediaType)null, value));
/*     */     }
/*     */     
/*     */     public static Part createFormData(String name, @Nullable String filename, RequestBody body) {
/* 247 */       if (name == null) {
/* 248 */         throw new NullPointerException("name == null");
/*     */       }
/* 250 */       StringBuilder disposition = new StringBuilder("form-data; name=");
/* 251 */       MultipartBody.appendQuotedString(disposition, name);
/*     */       
/* 253 */       if (filename != null) {
/* 254 */         disposition.append("; filename=");
/* 255 */         MultipartBody.appendQuotedString(disposition, filename);
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 260 */       Headers headers = (new Headers.Builder()).addUnsafeNonAscii("Content-Disposition", disposition.toString()).build();
/*     */       
/* 262 */       return create(headers, body);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Part(@Nullable Headers headers, RequestBody body) {
/* 269 */       this.headers = headers;
/* 270 */       this.body = body;
/*     */     }
/*     */     @Nullable
/*     */     public Headers headers() {
/* 274 */       return this.headers;
/*     */     }
/*     */     
/*     */     public RequestBody body() {
/* 278 */       return this.body;
/*     */     } }
/*     */ 
/*     */   
/*     */   public static final class Builder {
/*     */     private final ByteString boundary;
/* 284 */     private MediaType type = MultipartBody.MIXED;
/* 285 */     private final List<MultipartBody.Part> parts = new ArrayList<>();
/*     */     
/*     */     public Builder() {
/* 288 */       this(UUID.randomUUID().toString());
/*     */     }
/*     */     
/*     */     public Builder(String boundary) {
/* 292 */       this.boundary = ByteString.encodeUtf8(boundary);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Builder setType(MediaType type) {
/* 300 */       if (type == null) {
/* 301 */         throw new NullPointerException("type == null");
/*     */       }
/* 303 */       if (!type.type().equals("multipart")) {
/* 304 */         throw new IllegalArgumentException("multipart != " + type);
/*     */       }
/* 306 */       this.type = type;
/* 307 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder addPart(RequestBody body) {
/* 312 */       return addPart(MultipartBody.Part.create(body));
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder addPart(@Nullable Headers headers, RequestBody body) {
/* 317 */       return addPart(MultipartBody.Part.create(headers, body));
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder addFormDataPart(String name, String value) {
/* 322 */       return addPart(MultipartBody.Part.createFormData(name, value));
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder addFormDataPart(String name, @Nullable String filename, RequestBody body) {
/* 327 */       return addPart(MultipartBody.Part.createFormData(name, filename, body));
/*     */     }
/*     */ 
/*     */     
/*     */     public Builder addPart(MultipartBody.Part part) {
/* 332 */       if (part == null) throw new NullPointerException("part == null"); 
/* 333 */       this.parts.add(part);
/* 334 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public MultipartBody build() {
/* 339 */       if (this.parts.isEmpty()) {
/* 340 */         throw new IllegalStateException("Multipart body must have at least one part.");
/*     */       }
/* 342 */       return new MultipartBody(this.boundary, this.type, this.parts);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\MultipartBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */