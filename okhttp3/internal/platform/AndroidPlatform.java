/*     */ package okhttp3.internal.platform;
/*     */ 
/*     */ import android.os.Build;
/*     */ import android.util.Log;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import okhttp3.Protocol;
/*     */ import okhttp3.internal.Util;
/*     */ import okhttp3.internal.tls.CertificateChainCleaner;
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
/*     */ class AndroidPlatform
/*     */   extends Platform
/*     */ {
/*     */   private static final int MAX_LOG_LENGTH = 4000;
/*     */   private final Class<?> sslParametersClass;
/*     */   private final Method setUseSessionTickets;
/*     */   private final Method setHostname;
/*     */   private final Method getAlpnSelectedProtocol;
/*     */   private final Method setAlpnProtocols;
/*  52 */   private final CloseGuard closeGuard = CloseGuard.get();
/*     */ 
/*     */   
/*     */   AndroidPlatform(Class<?> sslParametersClass, Method setUseSessionTickets, Method setHostname, Method getAlpnSelectedProtocol, Method setAlpnProtocols) {
/*  56 */     this.sslParametersClass = sslParametersClass;
/*  57 */     this.setUseSessionTickets = setUseSessionTickets;
/*  58 */     this.setHostname = setHostname;
/*  59 */     this.getAlpnSelectedProtocol = getAlpnSelectedProtocol;
/*  60 */     this.setAlpnProtocols = setAlpnProtocols;
/*     */   }
/*     */ 
/*     */   
/*     */   public void connectSocket(Socket socket, InetSocketAddress address, int connectTimeout) throws IOException {
/*     */     try {
/*  66 */       socket.connect(address, connectTimeout);
/*  67 */     } catch (AssertionError e) {
/*  68 */       if (Util.isAndroidGetsocknameError(e)) throw new IOException(e); 
/*  69 */       throw e;
/*  70 */     } catch (ClassCastException e) {
/*     */ 
/*     */       
/*  73 */       if (Build.VERSION.SDK_INT == 26) {
/*  74 */         throw new IOException("Exception in connect", e);
/*     */       }
/*  76 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   protected X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
/*  82 */     Object context = readFieldOrNull(sslSocketFactory, this.sslParametersClass, "sslParameters");
/*  83 */     if (context == null) {
/*     */       
/*     */       try {
/*     */         
/*  87 */         Class<?> gmsSslParametersClass = Class.forName("com.google.android.gms.org.conscrypt.SSLParametersImpl", false, sslSocketFactory
/*     */             
/*  89 */             .getClass().getClassLoader());
/*  90 */         context = readFieldOrNull(sslSocketFactory, gmsSslParametersClass, "sslParameters");
/*  91 */       } catch (ClassNotFoundException e) {
/*  92 */         return super.trustManager(sslSocketFactory);
/*     */       } 
/*     */     }
/*     */     
/*  96 */     X509TrustManager x509TrustManager = readFieldOrNull(context, X509TrustManager.class, "x509TrustManager");
/*     */     
/*  98 */     if (x509TrustManager != null) return x509TrustManager;
/*     */     
/* 100 */     return readFieldOrNull(context, X509TrustManager.class, "trustManager");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> protocols) {
/*     */     try {
/* 107 */       if (hostname != null) {
/* 108 */         this.setUseSessionTickets.invoke(sslSocket, new Object[] { Boolean.valueOf(true) });
/*     */         
/* 110 */         this.setHostname.invoke(sslSocket, new Object[] { hostname });
/*     */       } 
/*     */ 
/*     */       
/* 114 */       this.setAlpnProtocols.invoke(sslSocket, new Object[] { concatLengthPrefixed(protocols) });
/* 115 */     } catch (IllegalAccessException|InvocationTargetException e) {
/* 116 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   @Nullable
/*     */   public String getSelectedProtocol(SSLSocket socket) {
/*     */     try {
/* 122 */       byte[] alpnResult = (byte[])this.getAlpnSelectedProtocol.invoke(socket, new Object[0]);
/* 123 */       return (alpnResult != null) ? new String(alpnResult, StandardCharsets.UTF_8) : null;
/* 124 */     } catch (IllegalAccessException|InvocationTargetException e) {
/* 125 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void log(int level, String message, @Nullable Throwable t) {
/* 130 */     int logLevel = (level == 5) ? 5 : 3;
/* 131 */     if (t != null) message = message + '\n' + Log.getStackTraceString(t);
/*     */ 
/*     */     
/* 134 */     for (int i = 0, length = message.length(); i < length; ) {
/* 135 */       int newline = message.indexOf('\n', i);
/* 136 */       newline = (newline != -1) ? newline : length;
/*     */       while (true) {
/* 138 */         int end = Math.min(newline, i + 4000);
/* 139 */         Log.println(logLevel, "OkHttp", message.substring(i, end));
/* 140 */         i = end;
/* 141 */         if (i >= newline)
/*     */           i++; 
/*     */       } 
/*     */     } 
/*     */   } public Object getStackTraceForCloseable(String closer) {
/* 146 */     return this.closeGuard.createAndOpen(closer);
/*     */   }
/*     */   
/*     */   public void logCloseableLeak(String message, Object stackTrace) {
/* 150 */     boolean reported = this.closeGuard.warnIfOpen(stackTrace);
/* 151 */     if (!reported)
/*     */     {
/* 153 */       log(5, message, (Throwable)null);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isCleartextTrafficPermitted(String hostname) {
/*     */     try {
/* 159 */       Class<?> networkPolicyClass = Class.forName("android.security.NetworkSecurityPolicy");
/* 160 */       Method getInstanceMethod = networkPolicyClass.getMethod("getInstance", new Class[0]);
/* 161 */       Object networkSecurityPolicy = getInstanceMethod.invoke(null, new Object[0]);
/* 162 */       return api24IsCleartextTrafficPermitted(hostname, networkPolicyClass, networkSecurityPolicy);
/* 163 */     } catch (ClassNotFoundException|NoSuchMethodException e) {
/* 164 */       return super.isCleartextTrafficPermitted(hostname);
/* 165 */     } catch (IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
/* 166 */       throw new AssertionError("unable to determine cleartext support", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean api24IsCleartextTrafficPermitted(String hostname, Class<?> networkPolicyClass, Object networkSecurityPolicy) throws InvocationTargetException, IllegalAccessException {
/*     */     try {
/* 174 */       Method isCleartextTrafficPermittedMethod = networkPolicyClass.getMethod("isCleartextTrafficPermitted", new Class[] { String.class });
/* 175 */       return ((Boolean)isCleartextTrafficPermittedMethod.invoke(networkSecurityPolicy, new Object[] { hostname })).booleanValue();
/* 176 */     } catch (NoSuchMethodException e) {
/* 177 */       return api23IsCleartextTrafficPermitted(hostname, networkPolicyClass, networkSecurityPolicy);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean api23IsCleartextTrafficPermitted(String hostname, Class<?> networkPolicyClass, Object networkSecurityPolicy) throws InvocationTargetException, IllegalAccessException {
/*     */     try {
/* 185 */       Method isCleartextTrafficPermittedMethod = networkPolicyClass.getMethod("isCleartextTrafficPermitted", new Class[0]);
/* 186 */       return ((Boolean)isCleartextTrafficPermittedMethod.invoke(networkSecurityPolicy, new Object[0])).booleanValue();
/* 187 */     } catch (NoSuchMethodException e) {
/* 188 */       return super.isCleartextTrafficPermitted(hostname);
/*     */     } 
/*     */   }
/*     */   
/*     */   public CertificateChainCleaner buildCertificateChainCleaner(X509TrustManager trustManager) {
/*     */     try {
/* 194 */       Class<?> extensionsClass = Class.forName("android.net.http.X509TrustManagerExtensions");
/* 195 */       Constructor<?> constructor = extensionsClass.getConstructor(new Class[] { X509TrustManager.class });
/* 196 */       Object extensions = constructor.newInstance(new Object[] { trustManager });
/* 197 */       Method checkServerTrusted = extensionsClass.getMethod("checkServerTrusted", new Class[] { X509Certificate[].class, String.class, String.class });
/*     */       
/* 199 */       return new AndroidCertificateChainCleaner(extensions, checkServerTrusted);
/* 200 */     } catch (Exception e) {
/* 201 */       throw new AssertionError(e);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public static Platform buildIfSupported() {
/*     */     Class<?> sslParametersClass;
/*     */     Class<?> sslSocketClass;
/*     */     try {
/* 210 */       sslParametersClass = Class.forName("com.android.org.conscrypt.SSLParametersImpl");
/* 211 */       sslSocketClass = Class.forName("com.android.org.conscrypt.OpenSSLSocketImpl");
/* 212 */     } catch (ClassNotFoundException ignored) {
/* 213 */       return null;
/*     */     } 
/* 215 */     if (Build.VERSION.SDK_INT >= 21) {
/*     */       try {
/* 217 */         Method setUseSessionTickets = sslSocketClass.getDeclaredMethod("setUseSessionTickets", new Class[] { boolean.class });
/*     */         
/* 219 */         Method setHostname = sslSocketClass.getMethod("setHostname", new Class[] { String.class });
/* 220 */         Method getAlpnSelectedProtocol = sslSocketClass.getMethod("getAlpnSelectedProtocol", new Class[0]);
/* 221 */         Method setAlpnProtocols = sslSocketClass.getMethod("setAlpnProtocols", new Class[] { byte[].class });
/* 222 */         return new AndroidPlatform(sslParametersClass, setUseSessionTickets, setHostname, getAlpnSelectedProtocol, setAlpnProtocols);
/*     */       }
/* 224 */       catch (NoSuchMethodException noSuchMethodException) {}
/*     */     }
/*     */     
/* 227 */     throw new IllegalStateException("Expected Android API level 21+ but was " + Build.VERSION.SDK_INT);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static final class AndroidCertificateChainCleaner
/*     */     extends CertificateChainCleaner
/*     */   {
/*     */     private final Object x509TrustManagerExtensions;
/*     */     
/*     */     private final Method checkServerTrusted;
/*     */ 
/*     */     
/*     */     AndroidCertificateChainCleaner(Object x509TrustManagerExtensions, Method checkServerTrusted) {
/* 241 */       this.x509TrustManagerExtensions = x509TrustManagerExtensions;
/* 242 */       this.checkServerTrusted = checkServerTrusted;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public List<Certificate> clean(List<Certificate> chain, String hostname) throws SSLPeerUnverifiedException {
/*     */       try {
/* 249 */         X509Certificate[] certificates = chain.<X509Certificate>toArray(new X509Certificate[chain.size()]);
/* 250 */         return (List<Certificate>)this.checkServerTrusted.invoke(this.x509TrustManagerExtensions, new Object[] { certificates, "RSA", hostname });
/*     */       }
/* 252 */       catch (InvocationTargetException e) {
/* 253 */         SSLPeerUnverifiedException exception = new SSLPeerUnverifiedException(e.getMessage());
/* 254 */         exception.initCause(e);
/* 255 */         throw exception;
/* 256 */       } catch (IllegalAccessException e) {
/* 257 */         throw new AssertionError(e);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean equals(Object other) {
/* 262 */       return other instanceof AndroidCertificateChainCleaner;
/*     */     }
/*     */     
/*     */     public int hashCode() {
/* 266 */       return 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class CloseGuard
/*     */   {
/*     */     private final Method getMethod;
/*     */     
/*     */     private final Method openMethod;
/*     */     
/*     */     private final Method warnIfOpenMethod;
/*     */ 
/*     */     
/*     */     CloseGuard(Method getMethod, Method openMethod, Method warnIfOpenMethod) {
/* 281 */       this.getMethod = getMethod;
/* 282 */       this.openMethod = openMethod;
/* 283 */       this.warnIfOpenMethod = warnIfOpenMethod;
/*     */     }
/*     */     
/*     */     Object createAndOpen(String closer) {
/* 287 */       if (this.getMethod != null) {
/*     */         try {
/* 289 */           Object closeGuardInstance = this.getMethod.invoke(null, new Object[0]);
/* 290 */           this.openMethod.invoke(closeGuardInstance, new Object[] { closer });
/* 291 */           return closeGuardInstance;
/* 292 */         } catch (Exception exception) {}
/*     */       }
/*     */       
/* 295 */       return null;
/*     */     }
/*     */     
/*     */     boolean warnIfOpen(Object closeGuardInstance) {
/* 299 */       boolean reported = false;
/* 300 */       if (closeGuardInstance != null) {
/*     */         try {
/* 302 */           this.warnIfOpenMethod.invoke(closeGuardInstance, new Object[0]);
/* 303 */           reported = true;
/* 304 */         } catch (Exception exception) {}
/*     */       }
/*     */       
/* 307 */       return reported;
/*     */     }
/*     */ 
/*     */     
/*     */     static CloseGuard get() {
/*     */       Method getMethod;
/*     */       Method openMethod;
/*     */       Method warnIfOpenMethod;
/*     */       try {
/* 316 */         Class<?> closeGuardClass = Class.forName("dalvik.system.CloseGuard");
/* 317 */         getMethod = closeGuardClass.getMethod("get", new Class[0]);
/* 318 */         openMethod = closeGuardClass.getMethod("open", new Class[] { String.class });
/* 319 */         warnIfOpenMethod = closeGuardClass.getMethod("warnIfOpen", new Class[0]);
/* 320 */       } catch (Exception ignored) {
/* 321 */         getMethod = null;
/* 322 */         openMethod = null;
/* 323 */         warnIfOpenMethod = null;
/*     */       } 
/* 325 */       return new CloseGuard(getMethod, openMethod, warnIfOpenMethod);
/*     */     }
/*     */   }
/*     */   
/*     */   public SSLContext getSSLContext() {
/*     */     boolean tryTls12;
/*     */     try {
/* 332 */       tryTls12 = (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22);
/* 333 */     } catch (NoClassDefFoundError e) {
/*     */ 
/*     */       
/* 336 */       tryTls12 = true;
/*     */     } 
/*     */     
/* 339 */     if (tryTls12) {
/*     */       try {
/* 341 */         return SSLContext.getInstance("TLSv1.2");
/* 342 */       } catch (NoSuchAlgorithmException noSuchAlgorithmException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 348 */       return SSLContext.getInstance("TLS");
/* 349 */     } catch (NoSuchAlgorithmException e) {
/* 350 */       throw new IllegalStateException("No TLS provider", e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\platform\AndroidPlatform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */