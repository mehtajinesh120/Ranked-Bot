/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.Socket;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ class ProxyHandshaker
/*     */ {
/*     */   private static final String RN = "\r\n";
/*     */   private final String mHost;
/*     */   private final int mPort;
/*     */   private final ProxySettings mSettings;
/*     */   
/*     */   public ProxyHandshaker(String host, int port, ProxySettings settings) {
/*  38 */     this.mHost = host;
/*  39 */     this.mPort = port;
/*  40 */     this.mSettings = settings;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void perform(Socket socket) throws IOException {
/*  47 */     sendRequest(socket);
/*     */ 
/*     */     
/*  50 */     receiveResponse(socket);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendRequest(Socket socket) throws IOException {
/*  57 */     String request = buildRequest();
/*     */ 
/*     */     
/*  60 */     byte[] requestBytes = Misc.getBytesUTF8(request);
/*     */ 
/*     */     
/*  63 */     OutputStream output = socket.getOutputStream();
/*     */ 
/*     */     
/*  66 */     output.write(requestBytes);
/*  67 */     output.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String buildRequest() {
/*  73 */     String host = String.format("%s:%d", new Object[] { this.mHost, Integer.valueOf(this.mPort) });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  78 */     StringBuilder builder = (new StringBuilder()).append("CONNECT ").append(host).append(" HTTP/1.1").append("\r\n").append("Host: ").append(host).append("\r\n");
/*     */ 
/*     */ 
/*     */     
/*  82 */     addHeaders(builder);
/*     */ 
/*     */     
/*  85 */     addProxyAuthorization(builder);
/*     */ 
/*     */     
/*  88 */     return builder.append("\r\n").toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addHeaders(StringBuilder builder) {
/*  95 */     for (Map.Entry<String, List<String>> header : this.mSettings.getHeaders().entrySet()) {
/*     */ 
/*     */       
/*  98 */       String name = header.getKey();
/*     */ 
/*     */       
/* 101 */       for (String value : header.getValue()) {
/*     */         
/* 103 */         if (value == null)
/*     */         {
/* 105 */           value = "";
/*     */         }
/*     */         
/* 108 */         builder.append(name).append(": ").append(value).append("\r\n");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void addProxyAuthorization(StringBuilder builder) {
/* 116 */     String id = this.mSettings.getId();
/*     */     
/* 118 */     if (id == null || id.length() == 0) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 123 */     String password = this.mSettings.getPassword();
/*     */     
/* 125 */     if (password == null)
/*     */     {
/* 127 */       password = "";
/*     */     }
/*     */ 
/*     */     
/* 131 */     String credentials = String.format("%s:%s", new Object[] { id, password });
/*     */ 
/*     */     
/* 134 */     builder
/* 135 */       .append("Proxy-Authorization: Basic ")
/* 136 */       .append(Base64.encode(credentials))
/* 137 */       .append("\r\n");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void receiveResponse(Socket socket) throws IOException {
/* 144 */     InputStream input = socket.getInputStream();
/*     */ 
/*     */     
/* 147 */     readStatusLine(input);
/*     */ 
/*     */ 
/*     */     
/* 151 */     skipHeaders(input);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readStatusLine(InputStream input) throws IOException {
/* 158 */     String statusLine = Misc.readLine(input, "UTF-8");
/*     */ 
/*     */     
/* 161 */     if (statusLine == null || statusLine.length() == 0)
/*     */     {
/* 163 */       throw new IOException("The response from the proxy server does not contain a status line.");
/*     */     }
/*     */ 
/*     */     
/* 167 */     String[] elements = statusLine.split(" +", 3);
/*     */     
/* 169 */     if (elements.length < 2)
/*     */     {
/* 171 */       throw new IOException("The status line in the response from the proxy server is badly formatted. The status line is: " + statusLine);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     if (!"200".equals(elements[1]))
/*     */     {
/* 179 */       throw new IOException("The status code in the response from the proxy server is not '200 Connection established'. The status line is: " + statusLine);
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
/*     */   private void skipHeaders(InputStream input) throws IOException {
/* 191 */     int count = 0;
/*     */ 
/*     */ 
/*     */     
/*     */     while (true) {
/* 196 */       int ch = input.read();
/*     */ 
/*     */       
/* 199 */       if (ch == -1)
/*     */       {
/*     */         
/* 202 */         throw new EOFException("The end of the stream from the proxy server was reached unexpectedly.");
/*     */       }
/*     */ 
/*     */       
/* 206 */       if (ch == 10) {
/*     */ 
/*     */         
/* 209 */         if (count == 0) {
/*     */           return;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 216 */         count = 0;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 221 */       if (ch != 13) {
/*     */ 
/*     */         
/* 224 */         count++;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 229 */       ch = input.read();
/*     */ 
/*     */       
/* 232 */       if (ch == -1)
/*     */       {
/*     */         
/* 235 */         throw new EOFException("The end of the stream from the proxy server was reached unexpectedly after a carriage return.");
/*     */       }
/*     */       
/* 238 */       if (ch != 10) {
/*     */ 
/*     */         
/* 241 */         count += 2;
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */       
/* 248 */       if (count == 0) {
/*     */         return;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 255 */       count = 0;
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
/*     */   String getProxiedHostname() {
/* 269 */     return this.mHost;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\ProxyHandshaker.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */