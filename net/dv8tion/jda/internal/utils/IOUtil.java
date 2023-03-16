/*     */ package net.dv8tion.jda.internal.utils;
/*     */ 
/*     */ import com.neovisionaries.ws.client.WebSocketFactory;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.Inflater;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ import java.util.zip.ZipException;
/*     */ import okhttp3.ConnectionPool;
/*     */ import okhttp3.Dispatcher;
/*     */ import okhttp3.MediaType;
/*     */ import okhttp3.OkHttpClient;
/*     */ import okhttp3.RequestBody;
/*     */ import okhttp3.Response;
/*     */ import okio.Okio;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IOUtil
/*     */ {
/*  35 */   private static final Logger log = JDALogger.getLog(IOUtil.class);
/*     */ 
/*     */ 
/*     */   
/*     */   public static void silentClose(AutoCloseable closeable) {
/*     */     try {
/*  41 */       closeable.close();
/*     */     }
/*  43 */     catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void silentClose(Closeable closeable) {
/*     */     try {
/*  50 */       closeable.close();
/*     */     }
/*  52 */     catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getHost(String uri) {
/*  57 */     return URI.create(uri).getHost();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setServerName(WebSocketFactory factory, String url) {
/*  62 */     String host = getHost(url);
/*     */     
/*  64 */     if (host != null) {
/*  65 */       factory.setServerName(host);
/*     */     }
/*     */   }
/*     */   
/*     */   public static OkHttpClient.Builder newHttpClientBuilder() {
/*  70 */     Dispatcher dispatcher = new Dispatcher();
/*     */     
/*  72 */     dispatcher.setMaxRequestsPerHost(25);
/*     */     
/*  74 */     ConnectionPool connectionPool = new ConnectionPool(5, 10L, TimeUnit.SECONDS);
/*  75 */     return (new OkHttpClient.Builder())
/*  76 */       .connectionPool(connectionPool)
/*  77 */       .dispatcher(dispatcher);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] readFully(File file) throws IOException {
/*  99 */     Checks.notNull(file, "File");
/* 100 */     Checks.check(file.exists(), "Provided file does not exist!");
/*     */     
/* 102 */     InputStream is = new FileInputStream(file);
/*     */     
/*     */     try {
/* 105 */       long length = file.length();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 111 */       if (length > 2147483647L)
/*     */       {
/* 113 */         throw new IOException("Cannot read the file into memory completely due to it being too large!");
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 118 */       byte[] bytes = new byte[(int)length];
/*     */ 
/*     */       
/* 121 */       int offset = 0;
/* 122 */       int numRead = 0;
/* 123 */       while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0)
/*     */       {
/* 125 */         offset += numRead;
/*     */       }
/*     */ 
/*     */       
/* 129 */       if (offset < bytes.length)
/*     */       {
/* 131 */         throw new IOException("Could not completely read file " + file.getName());
/*     */       }
/*     */ 
/*     */       
/* 135 */       is.close();
/* 136 */       byte[] arrayOfByte1 = bytes;
/* 137 */       is.close();
/*     */       return arrayOfByte1;
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         is.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] readFully(InputStream stream) throws IOException {
/* 157 */     Checks.notNull(stream, "InputStream");
/*     */     
/* 159 */     byte[] buffer = new byte[1024];
/* 160 */     ByteArrayOutputStream bos = new ByteArrayOutputStream();
/*     */     try {
/* 162 */       int readAmount = 0;
/* 163 */       while ((readAmount = stream.read(buffer)) != -1)
/*     */       {
/* 165 */         bos.write(buffer, 0, readAmount);
/*     */       }
/* 167 */       byte[] arrayOfByte = bos.toByteArray();
/* 168 */       bos.close();
/*     */       return arrayOfByte;
/*     */     } catch (Throwable throwable) {
/*     */       try {
/*     */         bos.close();
/*     */       } catch (Throwable throwable1) {
/*     */         throwable.addSuppressed(throwable1);
/*     */       } 
/*     */       throw throwable;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static RequestBody createRequestBody(MediaType contentType, InputStream stream) {
/* 183 */     return new BufferedRequestBody(Okio.source(stream), contentType);
/*     */   }
/*     */ 
/*     */   
/*     */   public static short getShortBigEndian(byte[] arr, int offset) {
/* 188 */     return (short)((arr[offset] & 0xFF) << 8 | arr[offset + 1] & 0xFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static short getShortLittleEndian(byte[] arr, int offset) {
/* 195 */     return (short)(arr[offset] & 0xFF | (arr[offset + 1] & 0xFF) << 8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getIntBigEndian(byte[] arr, int offset) {
/* 201 */     return arr[offset + 3] & 0xFF | (arr[offset + 2] & 0xFF) << 8 | (arr[offset + 1] & 0xFF) << 16 | (arr[offset] & 0xFF) << 24;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setIntBigEndian(byte[] arr, int offset, int it) {
/* 209 */     arr[offset] = (byte)(it >>> 24 & 0xFF);
/* 210 */     arr[offset + 1] = (byte)(it >>> 16 & 0xFF);
/* 211 */     arr[offset + 2] = (byte)(it >>> 8 & 0xFF);
/* 212 */     arr[offset + 3] = (byte)(it & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ByteBuffer reallocate(ByteBuffer original, int length) {
/* 217 */     ByteBuffer buffer = ByteBuffer.allocate(length);
/* 218 */     buffer.put(original);
/* 219 */     return buffer;
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
/*     */   public static InputStream getBody(Response response) throws IOException {
/* 237 */     String encoding = response.header("content-encoding", "");
/* 238 */     InputStream data = new BufferedInputStream(response.body().byteStream());
/* 239 */     data.mark(256);
/*     */     
/*     */     try {
/* 242 */       if (encoding.equalsIgnoreCase("gzip"))
/* 243 */         return new GZIPInputStream(data); 
/* 244 */       if (encoding.equalsIgnoreCase("deflate")) {
/* 245 */         return new InflaterInputStream(data, new Inflater(true));
/*     */       }
/* 247 */     } catch (ZipException|java.io.EOFException ex) {
/*     */       
/* 249 */       data.reset();
/* 250 */       log.error("Failed to read gzip content for response. Headers: {}\nContent: '{}'", new Object[] { response
/* 251 */             .headers(), JDALogger.getLazyString(() -> new String(readFully(data))), ex });
/* 252 */       return null;
/*     */     } 
/* 254 */     return data;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\IOUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */