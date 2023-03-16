/*    */ package okhttp3.internal.platform;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.List;
/*    */ import javax.annotation.Nullable;
/*    */ import javax.net.ssl.SSLParameters;
/*    */ import javax.net.ssl.SSLSocket;
/*    */ import javax.net.ssl.SSLSocketFactory;
/*    */ import javax.net.ssl.X509TrustManager;
/*    */ import okhttp3.Protocol;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class Jdk9Platform
/*    */   extends Platform
/*    */ {
/*    */   final Method setProtocolMethod;
/*    */   final Method getProtocolMethod;
/*    */   
/*    */   Jdk9Platform(Method setProtocolMethod, Method getProtocolMethod) {
/* 34 */     this.setProtocolMethod = setProtocolMethod;
/* 35 */     this.getProtocolMethod = getProtocolMethod;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> protocols) {
/*    */     try {
/* 42 */       SSLParameters sslParameters = sslSocket.getSSLParameters();
/*    */       
/* 44 */       List<String> names = alpnProtocolNames(protocols);
/*    */       
/* 46 */       this.setProtocolMethod.invoke(sslParameters, new Object[] { names
/* 47 */             .toArray(new String[names.size()]) });
/*    */       
/* 49 */       sslSocket.setSSLParameters(sslParameters);
/* 50 */     } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 51 */       throw new AssertionError("failed to set SSL parameters", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public String getSelectedProtocol(SSLSocket socket) {
/*    */     try {
/* 58 */       String protocol = (String)this.getProtocolMethod.invoke(socket, new Object[0]);
/*    */ 
/*    */ 
/*    */       
/* 62 */       if (protocol == null || protocol.equals("")) {
/* 63 */         return null;
/*    */       }
/*    */       
/* 66 */       return protocol;
/* 67 */     } catch (IllegalAccessException|java.lang.reflect.InvocationTargetException e) {
/* 68 */       throw new AssertionError("failed to get ALPN selected protocol", e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public X509TrustManager trustManager(SSLSocketFactory sslSocketFactory) {
/* 77 */     throw new UnsupportedOperationException("clientBuilder.sslSocketFactory(SSLSocketFactory) not supported on JDK 9+");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Jdk9Platform buildIfSupported() {
/*    */     try {
/* 85 */       Method setProtocolMethod = SSLParameters.class.getMethod("setApplicationProtocols", new Class[] { String[].class });
/* 86 */       Method getProtocolMethod = SSLSocket.class.getMethod("getApplicationProtocol", new Class[0]);
/*    */       
/* 88 */       return new Jdk9Platform(setProtocolMethod, getProtocolMethod);
/* 89 */     } catch (NoSuchMethodException noSuchMethodException) {
/*    */ 
/*    */ 
/*    */       
/* 93 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\platform\Jdk9Platform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */