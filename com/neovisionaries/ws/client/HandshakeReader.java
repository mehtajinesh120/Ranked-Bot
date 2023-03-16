/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.MessageDigest;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
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
/*     */ class HandshakeReader
/*     */ {
/*     */   private static final String ACCEPT_MAGIC = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/*     */   private final WebSocket mWebSocket;
/*     */   
/*     */   public HandshakeReader(WebSocket websocket) {
/*  41 */     this.mWebSocket = websocket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, List<String>> readHandshake(WebSocketInputStream input, String key) throws WebSocketException {
/*  48 */     StatusLine statusLine = readStatusLine(input);
/*     */ 
/*     */     
/*  51 */     Map<String, List<String>> headers = readHttpHeaders(input);
/*     */ 
/*     */     
/*  54 */     validateStatusLine(statusLine, headers, input);
/*     */ 
/*     */     
/*  57 */     validateUpgrade(statusLine, headers);
/*     */ 
/*     */     
/*  60 */     validateConnection(statusLine, headers);
/*     */ 
/*     */     
/*  63 */     validateAccept(statusLine, headers, key);
/*     */ 
/*     */     
/*  66 */     validateExtensions(statusLine, headers);
/*     */ 
/*     */     
/*  69 */     validateProtocol(statusLine, headers);
/*     */ 
/*     */     
/*  72 */     return headers;
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
/*     */   private StatusLine readStatusLine(WebSocketInputStream input) throws WebSocketException {
/*     */     String line;
/*     */     try {
/*  86 */       line = input.readLine();
/*     */     }
/*  88 */     catch (IOException e) {
/*     */ 
/*     */       
/*  91 */       throw new WebSocketException(WebSocketError.OPENING_HANDSHAKE_RESPONSE_FAILURE, "Failed to read an opening handshake response from the server: " + e
/*     */           
/*  93 */           .getMessage(), e);
/*     */     } 
/*     */     
/*  96 */     if (line == null || line.length() == 0)
/*     */     {
/*     */       
/*  99 */       throw new WebSocketException(WebSocketError.STATUS_LINE_EMPTY, "The status line of the opening handshake response is empty.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 107 */       return new StatusLine(line);
/*     */     }
/* 109 */     catch (Exception e) {
/*     */ 
/*     */       
/* 112 */       throw new WebSocketException(WebSocketError.STATUS_LINE_BAD_FORMAT, "The status line of the opening handshake response is badly formatted. The status line is: " + line);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Map<String, List<String>> readHttpHeaders(WebSocketInputStream input) throws WebSocketException {
/* 122 */     Map<String, List<String>> headers = new TreeMap<String, List<String>>(String.CASE_INSENSITIVE_ORDER);
/*     */ 
/*     */     
/* 125 */     StringBuilder builder = null;
/*     */ 
/*     */     
/*     */     while (true) {
/*     */       String line;
/*     */       
/*     */       try {
/* 132 */         line = input.readLine();
/*     */       }
/* 134 */       catch (IOException e) {
/*     */ 
/*     */         
/* 137 */         throw new WebSocketException(WebSocketError.HTTP_HEADER_FAILURE, "An error occurred while HTTP header section was being read: " + e
/*     */             
/* 139 */             .getMessage(), e);
/*     */       } 
/*     */ 
/*     */       
/* 143 */       if (line == null || line.length() == 0) {
/*     */         
/* 145 */         if (builder != null)
/*     */         {
/* 147 */           parseHttpHeader(headers, builder.toString());
/*     */         }
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */ 
/*     */       
/* 155 */       char ch = line.charAt(0);
/*     */ 
/*     */       
/* 158 */       if (ch == ' ' || ch == '\t') {
/*     */         
/* 160 */         if (builder == null) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 167 */         line = line.replaceAll("^[ \t]+", " ");
/*     */ 
/*     */         
/* 170 */         builder.append(line);
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 175 */       if (builder != null)
/*     */       {
/* 177 */         parseHttpHeader(headers, builder.toString());
/*     */       }
/*     */       
/* 180 */       builder = new StringBuilder(line);
/*     */     } 
/*     */     
/* 183 */     return headers;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void parseHttpHeader(Map<String, List<String>> headers, String header) {
/* 190 */     String[] pair = header.split(":", 2);
/*     */     
/* 192 */     if (pair.length < 2) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 199 */     String name = pair[0].trim();
/*     */ 
/*     */     
/* 202 */     String value = pair[1].trim();
/*     */     
/* 204 */     List<String> list = headers.get(name);
/*     */     
/* 206 */     if (list == null) {
/*     */       
/* 208 */       list = new ArrayList<String>();
/* 209 */       headers.put(name, list);
/*     */     } 
/*     */     
/* 212 */     list.add(value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateStatusLine(StatusLine statusLine, Map<String, List<String>> headers, WebSocketInputStream input) throws WebSocketException {
/* 222 */     if (statusLine.getStatusCode() == 101) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     byte[] body = readBody(headers, input);
/*     */ 
/*     */     
/* 232 */     throw new OpeningHandshakeException(WebSocketError.NOT_SWITCHING_PROTOCOLS, "The status code of the opening handshake response is not '101 Switching Protocols'. The status line is: " + statusLine, statusLine, headers, body);
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
/*     */   private byte[] readBody(Map<String, List<String>> headers, WebSocketInputStream input) {
/* 245 */     int length = getContentLength(headers);
/*     */     
/* 247 */     if (length <= 0)
/*     */     {
/*     */       
/* 250 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 256 */       byte[] body = new byte[length];
/*     */ 
/*     */       
/* 259 */       input.readBytes(body, length);
/*     */ 
/*     */       
/* 262 */       return body;
/*     */     }
/* 264 */     catch (Throwable t) {
/*     */ 
/*     */       
/* 267 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int getContentLength(Map<String, List<String>> headers) {
/*     */     try {
/* 279 */       return Integer.parseInt(((List<String>)headers.get("Content-Length")).get(0));
/*     */     }
/* 281 */     catch (Exception e) {
/*     */       
/* 283 */       return -1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateUpgrade(StatusLine statusLine, Map<String, List<String>> headers) throws WebSocketException {
/* 303 */     List<String> values = headers.get("Upgrade");
/*     */     
/* 305 */     if (values == null || values.size() == 0)
/*     */     {
/*     */       
/* 308 */       throw new OpeningHandshakeException(WebSocketError.NO_UPGRADE_HEADER, "The opening handshake response does not contain 'Upgrade' header.", statusLine, headers);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 314 */     for (String value : values) {
/*     */ 
/*     */       
/* 317 */       String[] elements = value.split("\\s*,\\s*");
/*     */       
/* 319 */       for (String element : elements) {
/*     */         
/* 321 */         if ("websocket".equalsIgnoreCase(element)) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 330 */     throw new OpeningHandshakeException(WebSocketError.NO_WEBSOCKET_IN_UPGRADE_HEADER, "'websocket' was not found in 'Upgrade' header.", statusLine, headers);
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
/*     */   private void validateConnection(StatusLine statusLine, Map<String, List<String>> headers) throws WebSocketException {
/* 352 */     List<String> values = headers.get("Connection");
/*     */     
/* 354 */     if (values == null || values.size() == 0)
/*     */     {
/*     */       
/* 357 */       throw new OpeningHandshakeException(WebSocketError.NO_CONNECTION_HEADER, "The opening handshake response does not contain 'Connection' header.", statusLine, headers);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 363 */     for (String value : values) {
/*     */ 
/*     */       
/* 366 */       String[] elements = value.split("\\s*,\\s*");
/*     */       
/* 368 */       for (String element : elements) {
/*     */         
/* 370 */         if ("Upgrade".equalsIgnoreCase(element)) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 379 */     throw new OpeningHandshakeException(WebSocketError.NO_UPGRADE_IN_CONNECTION_HEADER, "'Upgrade' was not found in 'Connection' header.", statusLine, headers);
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
/*     */ 
/*     */   
/*     */   private void validateAccept(StatusLine statusLine, Map<String, List<String>> headers, String key) throws WebSocketException {
/*     */     String expected;
/* 404 */     List<String> values = headers.get("Sec-WebSocket-Accept");
/*     */     
/* 406 */     if (values == null)
/*     */     {
/*     */       
/* 409 */       throw new OpeningHandshakeException(WebSocketError.NO_SEC_WEBSOCKET_ACCEPT_HEADER, "The opening handshake response does not contain 'Sec-WebSocket-Accept' header.", statusLine, headers);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 416 */     String actual = values.get(0);
/*     */ 
/*     */     
/* 419 */     String input = key + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 427 */       MessageDigest md = MessageDigest.getInstance("SHA-1");
/*     */ 
/*     */       
/* 430 */       byte[] digest = md.digest(Misc.getBytesUTF8(input));
/*     */ 
/*     */       
/* 433 */       expected = Base64.encode(digest);
/*     */     }
/* 435 */     catch (Exception e) {
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 441 */     if (!expected.equals(actual))
/*     */     {
/*     */       
/* 444 */       throw new OpeningHandshakeException(WebSocketError.UNEXPECTED_SEC_WEBSOCKET_ACCEPT_HEADER, "The value of 'Sec-WebSocket-Accept' header is different from the expected one.", statusLine, headers);
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
/*     */   private void validateExtensions(StatusLine statusLine, Map<String, List<String>> headers) throws WebSocketException {
/* 471 */     List<String> values = headers.get("Sec-WebSocket-Extensions");
/*     */     
/* 473 */     if (values == null || values.size() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 479 */     List<WebSocketExtension> extensions = new ArrayList<WebSocketExtension>();
/*     */     
/* 481 */     for (String value : values) {
/*     */ 
/*     */       
/* 484 */       String[] elements = value.split("\\s*,\\s*");
/*     */       
/* 486 */       for (String element : elements) {
/*     */ 
/*     */         
/* 489 */         WebSocketExtension extension = WebSocketExtension.parse(element);
/*     */         
/* 491 */         if (extension == null)
/*     */         {
/*     */           
/* 494 */           throw new OpeningHandshakeException(WebSocketError.EXTENSION_PARSE_ERROR, "The value in 'Sec-WebSocket-Extensions' failed to be parsed: " + element, statusLine, headers);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 501 */         String name = extension.getName();
/*     */ 
/*     */         
/* 504 */         if (!this.mWebSocket.getHandshakeBuilder().containsExtension(name))
/*     */         {
/*     */           
/* 507 */           throw new OpeningHandshakeException(WebSocketError.UNSUPPORTED_EXTENSION, "The extension contained in the Sec-WebSocket-Extensions header is not supported: " + name, statusLine, headers);
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 514 */         extension.validate();
/*     */ 
/*     */         
/* 517 */         extensions.add(extension);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 522 */     validateExtensionCombination(statusLine, headers, extensions);
/*     */ 
/*     */     
/* 525 */     this.mWebSocket.setAgreedExtensions(extensions);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void validateExtensionCombination(StatusLine statusLine, Map<String, List<String>> headers, List<WebSocketExtension> extensions) throws WebSocketException {
/* 535 */     WebSocketExtension pmce = null;
/*     */     
/* 537 */     for (WebSocketExtension extension : extensions) {
/*     */ 
/*     */       
/* 540 */       if (!(extension instanceof PerMessageCompressionExtension)) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 546 */       if (pmce == null) {
/*     */ 
/*     */         
/* 549 */         pmce = extension;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 554 */       String message = String.format("'%s' extension and '%s' extension conflict with each other.", new Object[] { pmce
/*     */             
/* 556 */             .getName(), extension.getName() });
/*     */ 
/*     */       
/* 559 */       throw new OpeningHandshakeException(WebSocketError.EXTENSIONS_CONFLICT, message, statusLine, headers);
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
/*     */   private void validateProtocol(StatusLine statusLine, Map<String, List<String>> headers) throws WebSocketException {
/* 582 */     List<String> values = headers.get("Sec-WebSocket-Protocol");
/*     */     
/* 584 */     if (values == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 591 */     String protocol = values.get(0);
/*     */     
/* 593 */     if (protocol == null || protocol.length() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 601 */     if (!this.mWebSocket.getHandshakeBuilder().containsProtocol(protocol))
/*     */     {
/*     */       
/* 604 */       throw new OpeningHandshakeException(WebSocketError.UNSUPPORTED_PROTOCOL, "The protocol contained in the Sec-WebSocket-Protocol header is not supported: " + protocol, statusLine, headers);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 611 */     this.mWebSocket.setAgreedProtocol(protocol);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\HandshakeReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */