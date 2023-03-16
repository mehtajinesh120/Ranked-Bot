/*     */ package net.dv8tion.jda.api.requests;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.Closeable;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.utils.IOFunction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.utils.IOUtil;
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
/*     */ public class Response
/*     */   implements Closeable
/*     */ {
/*     */   public static final int ERROR_CODE = -1;
/*     */   public static final String ERROR_MESSAGE = "ERROR";
/*  36 */   public static final IOFunction<BufferedReader, DataObject> JSON_SERIALIZE_OBJECT = DataObject::fromJson;
/*  37 */   public static final IOFunction<BufferedReader, DataArray> JSON_SERIALIZE_ARRAY = DataArray::fromJson;
/*     */   
/*     */   public final int code;
/*     */   
/*     */   public final String message;
/*     */   public final long retryAfter;
/*     */   private final InputStream body;
/*     */   private final okhttp3.Response rawResponse;
/*     */   private final Set<String> cfRays;
/*     */   private String fallbackString;
/*     */   private Object object;
/*     */   private boolean attemptedParsing = false;
/*     */   private Exception exception;
/*     */   
/*     */   public Response(@Nonnull Exception exception, @Nonnull Set<String> cfRays) {
/*  52 */     this(null, -1, "ERROR", -1L, cfRays);
/*  53 */     this.exception = exception;
/*     */   }
/*     */ 
/*     */   
/*     */   public Response(@Nullable okhttp3.Response response, int code, @Nonnull String message, long retryAfter, @Nonnull Set<String> cfRays) {
/*  58 */     this.rawResponse = response;
/*  59 */     this.code = code;
/*  60 */     this.message = message;
/*  61 */     this.exception = null;
/*  62 */     this.retryAfter = retryAfter;
/*  63 */     this.cfRays = cfRays;
/*     */     
/*  65 */     if (response == null) {
/*     */       
/*  67 */       this.body = null;
/*     */     } else {
/*     */ 
/*     */       
/*     */       try {
/*  72 */         this.body = IOUtil.getBody(response);
/*     */       }
/*  74 */       catch (Exception e) {
/*     */         
/*  76 */         throw new IllegalStateException("An error occurred while parsing the response for a RestAction", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Response(long retryAfter, @Nonnull Set<String> cfRays) {
/*  82 */     this(null, 429, "TOO MANY REQUESTS", retryAfter, cfRays);
/*     */   }
/*     */ 
/*     */   
/*     */   public Response(@Nonnull okhttp3.Response response, long retryAfter, @Nonnull Set<String> cfRays) {
/*  87 */     this(response, response.code(), response.message(), retryAfter, cfRays);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataArray getArray() {
/*  93 */     return get(DataArray.class, JSON_SERIALIZE_ARRAY);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Optional<DataArray> optArray() {
/*  99 */     return parseBody(true, DataArray.class, JSON_SERIALIZE_ARRAY);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public DataObject getObject() {
/* 105 */     return get(DataObject.class, JSON_SERIALIZE_OBJECT);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Optional<DataObject> optObject() {
/* 111 */     return parseBody(true, DataObject.class, JSON_SERIALIZE_OBJECT);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getString() {
/* 117 */     return parseBody(String.class, this::readString)
/* 118 */       .orElseGet(() -> (this.fallbackString == null) ? "N/A" : this.fallbackString);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public <T> T get(Class<T> clazz, IOFunction<BufferedReader, T> parser) {
/* 124 */     return (T)parseBody(clazz, parser).orElseThrow(IllegalStateException::new);
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public okhttp3.Response getRawResponse() {
/* 130 */     return this.rawResponse;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Set<String> getCFRays() {
/* 136 */     return this.cfRays;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Exception getException() {
/* 142 */     return this.exception;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isError() {
/* 147 */     return (this.code == -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOk() {
/* 152 */     return (this.code > 199 && this.code < 300);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRateLimit() {
/* 157 */     return (this.code == 429);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 163 */     return (this.exception == null) ? (
/* 164 */       "HTTPResponse[" + this.code + ((this.object == null) ? "" : (", " + this.object.toString())) + ']') : (
/* 165 */       "HTTPException[" + this.exception.getMessage() + ']');
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 171 */     if (this.rawResponse != null) {
/* 172 */       this.rawResponse.close();
/*     */     }
/*     */   }
/*     */   
/*     */   private String readString(BufferedReader reader) {
/* 177 */     return reader.lines().collect(Collectors.joining("\n"));
/*     */   }
/*     */ 
/*     */   
/*     */   private <T> Optional<T> parseBody(Class<T> clazz, IOFunction<BufferedReader, T> parser) {
/* 182 */     return parseBody(false, clazz, parser);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <T> Optional<T> parseBody(boolean opt, Class<T> clazz, IOFunction<BufferedReader, T> parser) {
/* 188 */     if (this.attemptedParsing) {
/*     */       
/* 190 */       if (this.object != null && clazz.isAssignableFrom(this.object.getClass()))
/* 191 */         return Optional.of(clazz.cast(this.object)); 
/* 192 */       return Optional.empty();
/*     */     } 
/*     */     
/* 195 */     this.attemptedParsing = true;
/* 196 */     if (this.body == null || this.rawResponse == null || this.rawResponse.body().contentLength() == 0L) {
/* 197 */       return Optional.empty();
/*     */     }
/* 199 */     BufferedReader reader = null;
/*     */     
/*     */     try {
/* 202 */       reader = new BufferedReader(new InputStreamReader(this.body));
/* 203 */       reader.mark(1024);
/* 204 */       T t = (T)parser.apply(reader);
/* 205 */       this.object = t;
/* 206 */       return Optional.ofNullable(t);
/*     */     }
/* 208 */     catch (Exception e) {
/*     */ 
/*     */       
/*     */       try {
/* 212 */         reader.reset();
/* 213 */         this.fallbackString = readString(reader);
/* 214 */         reader.close();
/*     */       }
/* 216 */       catch (NullPointerException|java.io.IOException nullPointerException) {}
/* 217 */       if (opt && e instanceof net.dv8tion.jda.api.exceptions.ParsingException) {
/* 218 */         return Optional.empty();
/*     */       }
/* 220 */       throw new IllegalStateException("An error occurred while parsing the response for a RestAction", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\Response.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */