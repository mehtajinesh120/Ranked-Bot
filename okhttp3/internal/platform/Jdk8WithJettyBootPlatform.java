/*     */ package okhttp3.internal.platform;
/*     */ 
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import okhttp3.Protocol;
/*     */ import okhttp3.internal.Util;
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
/*     */ class Jdk8WithJettyBootPlatform
/*     */   extends Platform
/*     */ {
/*     */   private final Method putMethod;
/*     */   private final Method getMethod;
/*     */   private final Method removeMethod;
/*     */   private final Class<?> clientProviderClass;
/*     */   private final Class<?> serverProviderClass;
/*     */   
/*     */   Jdk8WithJettyBootPlatform(Method putMethod, Method getMethod, Method removeMethod, Class<?> clientProviderClass, Class<?> serverProviderClass) {
/*  38 */     this.putMethod = putMethod;
/*  39 */     this.getMethod = getMethod;
/*  40 */     this.removeMethod = removeMethod;
/*  41 */     this.clientProviderClass = clientProviderClass;
/*  42 */     this.serverProviderClass = serverProviderClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public void configureTlsExtensions(SSLSocket sslSocket, String hostname, List<Protocol> protocols) {
/*  47 */     List<String> names = alpnProtocolNames(protocols);
/*     */     
/*     */     try {
/*  50 */       Object alpnProvider = Proxy.newProxyInstance(Platform.class.getClassLoader(), new Class[] { this.clientProviderClass, this.serverProviderClass }, new AlpnProvider(names));
/*     */       
/*  52 */       this.putMethod.invoke(null, new Object[] { sslSocket, alpnProvider });
/*  53 */     } catch (InvocationTargetException|IllegalAccessException e) {
/*  54 */       throw new AssertionError("failed to set ALPN", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void afterHandshake(SSLSocket sslSocket) {
/*     */     try {
/*  60 */       this.removeMethod.invoke(null, new Object[] { sslSocket });
/*  61 */     } catch (IllegalAccessException|InvocationTargetException e) {
/*  62 */       throw new AssertionError("failed to remove ALPN", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Nullable
/*     */   public String getSelectedProtocol(SSLSocket socket) {
/*     */     try {
/*  69 */       AlpnProvider provider = (AlpnProvider)Proxy.getInvocationHandler(this.getMethod.invoke(null, new Object[] { socket }));
/*  70 */       if (!provider.unsupported && provider.selected == null) {
/*  71 */         Platform.get().log(4, "ALPN callback dropped: HTTP/2 is disabled. Is alpn-boot on the boot class path?", null);
/*     */         
/*  73 */         return null;
/*     */       } 
/*  75 */       return provider.unsupported ? null : provider.selected;
/*  76 */     } catch (InvocationTargetException|IllegalAccessException e) {
/*  77 */       throw new AssertionError("failed to get ALPN selected protocol", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Platform buildIfSupported() {
/*     */     try {
/*  84 */       String alpnClassName = "org.eclipse.jetty.alpn.ALPN";
/*  85 */       Class<?> alpnClass = Class.forName(alpnClassName);
/*  86 */       Class<?> providerClass = Class.forName(alpnClassName + "$Provider");
/*  87 */       Class<?> clientProviderClass = Class.forName(alpnClassName + "$ClientProvider");
/*  88 */       Class<?> serverProviderClass = Class.forName(alpnClassName + "$ServerProvider");
/*  89 */       Method putMethod = alpnClass.getMethod("put", new Class[] { SSLSocket.class, providerClass });
/*  90 */       Method getMethod = alpnClass.getMethod("get", new Class[] { SSLSocket.class });
/*  91 */       Method removeMethod = alpnClass.getMethod("remove", new Class[] { SSLSocket.class });
/*  92 */       return new Jdk8WithJettyBootPlatform(putMethod, getMethod, removeMethod, clientProviderClass, serverProviderClass);
/*     */     }
/*  94 */     catch (ClassNotFoundException|NoSuchMethodException classNotFoundException) {
/*     */ 
/*     */       
/*  97 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static class AlpnProvider
/*     */     implements InvocationHandler
/*     */   {
/*     */     private final List<String> protocols;
/*     */     
/*     */     boolean unsupported;
/*     */     
/*     */     String selected;
/*     */ 
/*     */     
/*     */     AlpnProvider(List<String> protocols) {
/* 113 */       this.protocols = protocols;
/*     */     }
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 117 */       String arrayOfString[], methodName = method.getName();
/* 118 */       Class<?> returnType = method.getReturnType();
/* 119 */       if (args == null) {
/* 120 */         arrayOfString = Util.EMPTY_STRING_ARRAY;
/*     */       }
/* 122 */       if (methodName.equals("supports") && boolean.class == returnType)
/* 123 */         return Boolean.valueOf(true); 
/* 124 */       if (methodName.equals("unsupported") && void.class == returnType) {
/* 125 */         this.unsupported = true;
/* 126 */         return null;
/* 127 */       }  if (methodName.equals("protocols") && arrayOfString.length == 0)
/* 128 */         return this.protocols; 
/* 129 */       if ((methodName.equals("selectProtocol") || methodName.equals("select")) && String.class == returnType && arrayOfString.length == 1 && arrayOfString[0] instanceof List) {
/*     */         
/* 131 */         List<?> peerProtocols = (List)arrayOfString[0];
/*     */         
/* 133 */         for (int i = 0, size = peerProtocols.size(); i < size; i++) {
/* 134 */           String protocol = (String)peerProtocols.get(i);
/* 135 */           if (this.protocols.contains(protocol)) {
/* 136 */             return this.selected = protocol;
/*     */           }
/*     */         } 
/* 139 */         return this.selected = this.protocols.get(0);
/* 140 */       }  if ((methodName.equals("protocolSelected") || methodName.equals("selected")) && arrayOfString.length == 1) {
/*     */         
/* 142 */         this.selected = arrayOfString[0];
/* 143 */         return null;
/*     */       } 
/* 145 */       return method.invoke(this, (Object[])arrayOfString);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\platform\Jdk8WithJettyBootPlatform.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */