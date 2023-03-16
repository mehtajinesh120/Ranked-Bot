/*     */ package okhttp3.internal.platform;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.Security;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import okhttp3.OkHttpClient;
/*     */ import okhttp3.Protocol;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.tls.BasicCertificateChainCleaner;
/*     */ import okhttp3.internal.tls.CertificateChainCleaner;
/*     */ import okio.Buffer;
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
/*     */ public class Platform
/*     */ {
/*  77 */   private static final Platform PLATFORM = findPlatform();
/*     */   public static final int INFO = 4;
/*     */   public static final int WARN = 5;
/*  80 */   private static final Logger logger = Logger.getLogger(OkHttpClient.class.getName());
/*     */   
/*     */   public static Platform get() {
/*  83 */     return PLATFORM;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPrefix() {
/*  88 */     return "OkHttp";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   protected X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
/*     */     try {
/*  96 */       Class<?> sslContextClass = Class.forName("sun.security.ssl.SSLContextImpl");
/*  97 */       Object context = readFieldOrNull(sslSocketFactory, sslContextClass, "context");
/*  98 */       if (context == null) return null; 
/*  99 */       return readFieldOrNull(context, X509TrustManager.class, "trustManager");
/* 100 */     } catch (ClassNotFoundException e) {
/* 101 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void configureTlsExtensions(SSLSocket sslSocket, @Nullable String hostname, List<Protocol> protocols) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterHandshake(SSLSocket sslSocket) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getSelectedProtocol(SSLSocket socket) {
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void connectSocket(Socket socket, InetSocketAddress address, int connectTimeout) throws IOException {
/* 128 */     socket.connect(address, connectTimeout);
/*     */   }
/*     */   
/*     */   public void log(int level, String message, @Nullable Throwable t) {
/* 132 */     Level logLevel = (level == 5) ? Level.WARNING : Level.INFO;
/* 133 */     logger.log(logLevel, message, t);
/*     */   }
/*     */   
/*     */   public boolean isCleartextTrafficPermitted(String hostname) {
/* 137 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getStackTraceForCloseable(String closer) {
/* 146 */     if (logger.isLoggable(Level.FINE)) {
/* 147 */       return new Throwable(closer);
/*     */     }
/* 149 */     return null;
/*     */   }
/*     */   
/*     */   public void logCloseableLeak(String message, Object stackTrace) {
/* 153 */     if (stackTrace == null) {
/* 154 */       message = message + " To see where this was allocated, set the OkHttpClient logger level to FINE: Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);";
/*     */     }
/*     */     
/* 157 */     log(5, message, (Throwable)stackTrace);
/*     */   }
/*     */   
/*     */   public static List<String> alpnProtocolNames(List<Protocol> protocols) {
/* 161 */     List<String> names = new ArrayList<>(protocols.size());
/* 162 */     for (int i = 0, size = protocols.size(); i < size; i++) {
/* 163 */       Protocol protocol = protocols.get(i);
/* 164 */       if (protocol != Protocol.HTTP_1_0)
/* 165 */         names.add(protocol.toString()); 
/*     */     } 
/* 167 */     return names;
/*     */   }
/*     */   
/*     */   public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager trustManager) {
/* 171 */     return (CertificateChainCleaner)new BasicCertificateChainCleaner(trustManager.getAcceptedIssuers());
/*     */   }
/*     */   
/*     */   public CertificateChainCleaner buildCertificateChainCleaner(SSLSocketFactory sslSocketFactory) {
/* 175 */     X509TrustManager trustManager = trustManager(sslSocketFactory);
/*     */     
/* 177 */     if (trustManager == null) {
/* 178 */       throw new IllegalStateException("Unable to extract the trust manager on " + 
/* 179 */           get() + ", sslSocketFactory is " + sslSocketFactory
/*     */           
/* 181 */           .getClass());
/*     */     }
/*     */     
/* 184 */     return buildCertificateChainCleaner(trustManager);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isConscryptPreferred() {
/* 189 */     if ("conscrypt".equals(Util.getSystemProperty("okhttp.platform", null))) {
/* 190 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 194 */     String preferredProvider = Security.getProviders()[0].getName();
/* 195 */     return "Conscrypt".equals(preferredProvider);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Platform findPlatform() {
/* 200 */     Platform android = AndroidPlatform.buildIfSupported();
/*     */     
/* 202 */     if (android != null) {
/* 203 */       return android;
/*     */     }
/*     */     
/* 206 */     if (isConscryptPreferred()) {
/* 207 */       Platform conscrypt = ConscryptPlatform.buildIfSupported();
/*     */       
/* 209 */       if (conscrypt != null) {
/* 210 */         return conscrypt;
/*     */       }
/*     */     } 
/*     */     
/* 214 */     Platform jdk9 = Jdk9Platform.buildIfSupported();
/*     */     
/* 216 */     if (jdk9 != null) {
/* 217 */       return jdk9;
/*     */     }
/*     */     
/* 220 */     Platform jdkWithJettyBoot = Jdk8WithJettyBootPlatform.buildIfSupported();
/*     */     
/* 222 */     if (jdkWithJettyBoot != null) {
/* 223 */       return jdkWithJettyBoot;
/*     */     }
/*     */ 
/*     */     
/* 227 */     return new Platform();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static byte[] concatLengthPrefixed(List<Protocol> protocols) {
/* 235 */     Buffer result = new Buffer();
/* 236 */     for (int i = 0, size = protocols.size(); i < size; i++) {
/* 237 */       Protocol protocol = protocols.get(i);
/* 238 */       if (protocol != Protocol.HTTP_1_0) {
/* 239 */         result.writeByte(protocol.toString().length());
/* 240 */         result.writeUtf8(protocol.toString());
/*     */       } 
/* 242 */     }  return result.readByteArray();
/*     */   }
/*     */   @Nullable
/*     */   static <T> T readFieldOrNull(Object instance, Class<T> fieldType, String fieldName) {
/* 246 */     for (Class<?> c = instance.getClass(); c != Object.class; c = c.getSuperclass()) {
/*     */       
/* 248 */       try { Field field = c.getDeclaredField(fieldName);
/* 249 */         field.setAccessible(true);
/* 250 */         Object value = field.get(instance);
/* 251 */         if (!fieldType.isInstance(value)) return null; 
/* 252 */         return fieldType.cast(value); }
/* 253 */       catch (NoSuchFieldException noSuchFieldException) {  }
/* 254 */       catch (IllegalAccessException e)
/* 255 */       { throw new AssertionError(); }
/*     */     
/*     */     } 
/*     */ 
/*     */     
/* 260 */     if (!fieldName.equals("delegate")) {
/* 261 */       Object delegate = readFieldOrNull(instance, Object.class, "delegate");
/* 262 */       if (delegate != null) return readFieldOrNull(delegate, fieldType, fieldName);
/*     */     
/*     */     } 
/* 265 */     return null;
/*     */   }
/*     */   
/*     */   public SSLContext getSSLContext() {
/*     */     try {
/* 270 */       return SSLContext.getInstance("TLS");
/* 271 */     } catch (NoSuchAlgorithmException e) {
/* 272 */       throw new IllegalStateException("No TLS provider", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void configureSslSocketFactory(SSLSocketFactory socketFactory) {}
/*     */   
/*     */   public String toString() {
/* 280 */     return getClass().getSimpleName();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\platform\Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */