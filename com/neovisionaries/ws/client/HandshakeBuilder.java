/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.net.URI;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ class HandshakeBuilder
/*     */ {
/*  28 */   private static final String[] CONNECTION_HEADER = new String[] { "Connection", "Upgrade" };
/*  29 */   private static final String[] UPGRADE_HEADER = new String[] { "Upgrade", "websocket" };
/*  30 */   private static final String[] VERSION_HEADER = new String[] { "Sec-WebSocket-Version", "13" };
/*     */   
/*     */   private static final String RN = "\r\n";
/*     */   
/*     */   private boolean mSecure;
/*     */   
/*     */   private String mUserInfo;
/*     */   
/*     */   private final String mHost;
/*     */   private final String mPath;
/*     */   private final URI mUri;
/*     */   private String mKey;
/*     */   private Set<String> mProtocols;
/*     */   private List<WebSocketExtension> mExtensions;
/*     */   private List<String[]> mHeaders;
/*     */   
/*     */   public HandshakeBuilder(boolean secure, String userInfo, String host, String path) {
/*  47 */     this.mSecure = secure;
/*  48 */     this.mUserInfo = userInfo;
/*  49 */     this.mHost = host;
/*  50 */     this.mPath = path;
/*     */ 
/*     */ 
/*     */     
/*  54 */     this.mUri = URI.create(String.format("%s://%s%s", new Object[] { secure ? "wss" : "ws", host, path }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public HandshakeBuilder(HandshakeBuilder source) {
/*  61 */     this.mSecure = source.mSecure;
/*  62 */     this.mUserInfo = source.mUserInfo;
/*  63 */     this.mHost = source.mHost;
/*  64 */     this.mPath = source.mPath;
/*  65 */     this.mUri = source.mUri;
/*  66 */     this.mKey = source.mKey;
/*  67 */     this.mProtocols = copyProtocols(source.mProtocols);
/*  68 */     this.mExtensions = copyExtensions(source.mExtensions);
/*  69 */     this.mHeaders = copyHeaders(source.mHeaders);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addProtocol(String protocol) {
/*  75 */     if (!isValidProtocol(protocol))
/*     */     {
/*  77 */       throw new IllegalArgumentException("'protocol' must be a non-empty string with characters in the range U+0021 to U+007E not including separator characters.");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  82 */     synchronized (this) {
/*     */       
/*  84 */       if (this.mProtocols == null)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/*  89 */         this.mProtocols = new LinkedHashSet<String>();
/*     */       }
/*     */       
/*  92 */       this.mProtocols.add(protocol);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeProtocol(String protocol) {
/*  99 */     if (protocol == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 104 */     synchronized (this) {
/*     */       
/* 106 */       if (this.mProtocols == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 111 */       this.mProtocols.remove(protocol);
/*     */       
/* 113 */       if (this.mProtocols.size() == 0)
/*     */       {
/* 115 */         this.mProtocols = null;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearProtocols() {
/* 123 */     synchronized (this) {
/*     */       
/* 125 */       this.mProtocols = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isValidProtocol(String protocol) {
/* 132 */     if (protocol == null || protocol.length() == 0)
/*     */     {
/* 134 */       return false;
/*     */     }
/*     */     
/* 137 */     int len = protocol.length();
/*     */     
/* 139 */     for (int i = 0; i < len; i++) {
/*     */       
/* 141 */       char ch = protocol.charAt(i);
/*     */       
/* 143 */       if (ch < '!' || '~' < ch || Token.isSeparator(ch))
/*     */       {
/* 145 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 149 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsProtocol(String protocol) {
/* 155 */     synchronized (this) {
/*     */       
/* 157 */       if (this.mProtocols == null)
/*     */       {
/* 159 */         return false;
/*     */       }
/*     */       
/* 162 */       return this.mProtocols.contains(protocol);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExtension(WebSocketExtension extension) {
/* 169 */     if (extension == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 174 */     synchronized (this) {
/*     */       
/* 176 */       if (this.mExtensions == null)
/*     */       {
/* 178 */         this.mExtensions = new ArrayList<WebSocketExtension>();
/*     */       }
/*     */       
/* 181 */       this.mExtensions.add(extension);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addExtension(String extension) {
/* 188 */     addExtension(WebSocketExtension.parse(extension));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeExtension(WebSocketExtension extension) {
/* 194 */     if (extension == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 199 */     synchronized (this) {
/*     */       
/* 201 */       if (this.mExtensions == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 206 */       this.mExtensions.remove(extension);
/*     */       
/* 208 */       if (this.mExtensions.size() == 0)
/*     */       {
/* 210 */         this.mExtensions = null;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeExtensions(String name) {
/* 218 */     if (name == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 223 */     synchronized (this) {
/*     */       
/* 225 */       if (this.mExtensions == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 230 */       List<WebSocketExtension> extensionsToRemove = new ArrayList<WebSocketExtension>();
/*     */       
/* 232 */       for (WebSocketExtension extension : this.mExtensions) {
/*     */         
/* 234 */         if (extension.getName().equals(name))
/*     */         {
/* 236 */           extensionsToRemove.add(extension);
/*     */         }
/*     */       } 
/*     */       
/* 240 */       for (WebSocketExtension extension : extensionsToRemove)
/*     */       {
/* 242 */         this.mExtensions.remove(extension);
/*     */       }
/*     */       
/* 245 */       if (this.mExtensions.size() == 0)
/*     */       {
/* 247 */         this.mExtensions = null;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearExtensions() {
/* 255 */     synchronized (this) {
/*     */       
/* 257 */       this.mExtensions = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsExtension(WebSocketExtension extension) {
/* 264 */     if (extension == null)
/*     */     {
/* 266 */       return false;
/*     */     }
/*     */     
/* 269 */     synchronized (this) {
/*     */       
/* 271 */       if (this.mExtensions == null)
/*     */       {
/* 273 */         return false;
/*     */       }
/*     */       
/* 276 */       return this.mExtensions.contains(extension);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsExtension(String name) {
/* 283 */     if (name == null)
/*     */     {
/* 285 */       return false;
/*     */     }
/*     */     
/* 288 */     synchronized (this) {
/*     */       
/* 290 */       if (this.mExtensions == null)
/*     */       {
/* 292 */         return false;
/*     */       }
/*     */       
/* 295 */       for (WebSocketExtension extension : this.mExtensions) {
/*     */         
/* 297 */         if (extension.getName().equals(name))
/*     */         {
/* 299 */           return true;
/*     */         }
/*     */       } 
/*     */       
/* 303 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHeader(String name, String value) {
/* 310 */     if (name == null || name.length() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 315 */     if (value == null)
/*     */     {
/* 317 */       value = "";
/*     */     }
/*     */     
/* 320 */     synchronized (this) {
/*     */       
/* 322 */       if (this.mHeaders == null)
/*     */       {
/* 324 */         this.mHeaders = (List)new ArrayList<String>();
/*     */       }
/*     */       
/* 327 */       this.mHeaders.add(new String[] { name, value });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeHeaders(String name) {
/* 334 */     if (name == null || name.length() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 339 */     synchronized (this) {
/*     */       
/* 341 */       if (this.mHeaders == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 346 */       List<String[]> headersToRemove = (List)new ArrayList<String>();
/*     */       
/* 348 */       for (String[] header : this.mHeaders) {
/*     */         
/* 350 */         if (header[0].equals(name))
/*     */         {
/* 352 */           headersToRemove.add(header);
/*     */         }
/*     */       } 
/*     */       
/* 356 */       for (String[] header : headersToRemove)
/*     */       {
/* 358 */         this.mHeaders.remove(header);
/*     */       }
/*     */       
/* 361 */       if (this.mHeaders.size() == 0)
/*     */       {
/* 363 */         this.mHeaders = null;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearHeaders() {
/* 371 */     synchronized (this) {
/*     */       
/* 373 */       this.mHeaders = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserInfo(String userInfo) {
/* 380 */     synchronized (this) {
/*     */       
/* 382 */       this.mUserInfo = userInfo;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUserInfo(String id, String password) {
/* 389 */     if (id == null)
/*     */     {
/* 391 */       id = "";
/*     */     }
/*     */     
/* 394 */     if (password == null)
/*     */     {
/* 396 */       password = "";
/*     */     }
/*     */     
/* 399 */     String userInfo = String.format("%s:%s", new Object[] { id, password });
/*     */     
/* 401 */     setUserInfo(userInfo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearUserInfo() {
/* 407 */     synchronized (this) {
/*     */       
/* 409 */       this.mUserInfo = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public URI getURI() {
/* 416 */     return this.mUri;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKey(String key) {
/* 422 */     this.mKey = key;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String buildRequestLine() {
/* 428 */     return String.format("GET %s HTTP/1.1", new Object[] { this.mPath });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String[]> buildHeaders() {
/* 434 */     List<String[]> headers = (List)new ArrayList<String>();
/*     */ 
/*     */     
/* 437 */     headers.add(new String[] { "Host", this.mHost });
/*     */ 
/*     */     
/* 440 */     headers.add(CONNECTION_HEADER);
/*     */ 
/*     */     
/* 443 */     headers.add(UPGRADE_HEADER);
/*     */ 
/*     */     
/* 446 */     headers.add(VERSION_HEADER);
/*     */ 
/*     */     
/* 449 */     headers.add(new String[] { "Sec-WebSocket-Key", this.mKey });
/*     */ 
/*     */     
/* 452 */     if (this.mProtocols != null && this.mProtocols.size() != 0)
/*     */     {
/* 454 */       headers.add(new String[] { "Sec-WebSocket-Protocol", Misc.join(this.mProtocols, ", ") });
/*     */     }
/*     */ 
/*     */     
/* 458 */     if (this.mExtensions != null && this.mExtensions.size() != 0)
/*     */     {
/* 460 */       headers.add(new String[] { "Sec-WebSocket-Extensions", Misc.join(this.mExtensions, ", ") });
/*     */     }
/*     */ 
/*     */     
/* 464 */     if (this.mUserInfo != null && this.mUserInfo.length() != 0)
/*     */     {
/* 466 */       headers.add(new String[] { "Authorization", "Basic " + Base64.encode(this.mUserInfo) });
/*     */     }
/*     */ 
/*     */     
/* 470 */     if (this.mHeaders != null && this.mHeaders.size() != 0)
/*     */     {
/* 472 */       headers.addAll(this.mHeaders);
/*     */     }
/*     */     
/* 475 */     return headers;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String build(String requestLine, List<String[]> headers) {
/* 481 */     StringBuilder builder = new StringBuilder();
/*     */ 
/*     */     
/* 484 */     builder.append(requestLine).append("\r\n");
/*     */ 
/*     */     
/* 487 */     for (String[] header : headers)
/*     */     {
/*     */       
/* 490 */       builder.append(header[0]).append(": ").append(header[1]).append("\r\n");
/*     */     }
/*     */ 
/*     */     
/* 494 */     builder.append("\r\n");
/*     */     
/* 496 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Set<String> copyProtocols(Set<String> protocols) {
/* 502 */     if (protocols == null)
/*     */     {
/* 504 */       return null;
/*     */     }
/*     */     
/* 507 */     Set<String> newProtocols = new LinkedHashSet<String>(protocols.size());
/*     */     
/* 509 */     newProtocols.addAll(protocols);
/*     */     
/* 511 */     return newProtocols;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<WebSocketExtension> copyExtensions(List<WebSocketExtension> extensions) {
/* 517 */     if (extensions == null)
/*     */     {
/* 519 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 523 */     List<WebSocketExtension> newExtensions = new ArrayList<WebSocketExtension>(extensions.size());
/*     */     
/* 525 */     for (WebSocketExtension extension : extensions)
/*     */     {
/* 527 */       newExtensions.add(new WebSocketExtension(extension));
/*     */     }
/*     */     
/* 530 */     return newExtensions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<String[]> copyHeaders(List<String[]> headers) {
/* 536 */     if (headers == null)
/*     */     {
/* 538 */       return null;
/*     */     }
/*     */     
/* 541 */     List<String[]> newHeaders = (List)new ArrayList<String>(headers.size());
/*     */     
/* 543 */     for (String[] header : headers)
/*     */     {
/* 545 */       newHeaders.add(copyHeader(header));
/*     */     }
/*     */     
/* 548 */     return newHeaders;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] copyHeader(String[] header) {
/* 554 */     String[] newHeader = new String[2];
/*     */     
/* 556 */     newHeader[0] = header[0];
/* 557 */     newHeader[1] = header[1];
/*     */     
/* 559 */     return newHeader;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\HandshakeBuilder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */