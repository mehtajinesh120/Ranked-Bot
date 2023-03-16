/*     */ package com.neovisionaries.ws.client;
/*     */ 
/*     */ import java.util.LinkedHashMap;
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
/*     */ public class WebSocketExtension
/*     */ {
/*     */   public static final String PERMESSAGE_DEFLATE = "permessage-deflate";
/*     */   private final String mName;
/*     */   private final Map<String, String> mParameters;
/*     */   
/*     */   public WebSocketExtension(String name) {
/*  56 */     if (!Token.isValid(name))
/*     */     {
/*     */       
/*  59 */       throw new IllegalArgumentException("'name' is not a valid token.");
/*     */     }
/*     */     
/*  62 */     this.mName = name;
/*  63 */     this.mParameters = new LinkedHashMap<String, String>();
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
/*     */   public WebSocketExtension(WebSocketExtension source) {
/*  80 */     if (source == null)
/*     */     {
/*     */       
/*  83 */       throw new IllegalArgumentException("'source' is null.");
/*     */     }
/*     */     
/*  86 */     this.mName = source.getName();
/*  87 */     this.mParameters = new LinkedHashMap<String, String>(source.getParameters());
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
/*     */   public String getName() {
/*  99 */     return this.mName;
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
/*     */   public Map<String, String> getParameters() {
/* 111 */     return this.mParameters;
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
/*     */   public boolean containsParameter(String key) {
/* 126 */     return this.mParameters.containsKey(key);
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
/*     */   public String getParameter(String key) {
/* 141 */     return this.mParameters.get(key);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public WebSocketExtension setParameter(String key, String value) {
/* 170 */     if (!Token.isValid(key))
/*     */     {
/*     */       
/* 173 */       throw new IllegalArgumentException("'key' is not a valid token.");
/*     */     }
/*     */ 
/*     */     
/* 177 */     if (value != null)
/*     */     {
/*     */       
/* 180 */       if (!Token.isValid(value))
/*     */       {
/*     */         
/* 183 */         throw new IllegalArgumentException("'value' is not a valid token.");
/*     */       }
/*     */     }
/*     */     
/* 187 */     this.mParameters.put(key, value);
/*     */     
/* 189 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 199 */     StringBuilder builder = new StringBuilder(this.mName);
/*     */     
/* 201 */     for (Map.Entry<String, String> entry : this.mParameters.entrySet()) {
/*     */ 
/*     */       
/* 204 */       builder.append("; ").append(entry.getKey());
/*     */       
/* 206 */       String value = entry.getValue();
/*     */       
/* 208 */       if (value != null && value.length() != 0)
/*     */       {
/*     */         
/* 211 */         builder.append("=").append(value);
/*     */       }
/*     */     } 
/*     */     
/* 215 */     return builder.toString();
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
/*     */   void validate() throws WebSocketException {}
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
/*     */   public static WebSocketExtension parse(String string) {
/* 244 */     if (string == null)
/*     */     {
/* 246 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 250 */     String[] elements = string.trim().split("\\s*;\\s*");
/*     */     
/* 252 */     if (elements.length == 0)
/*     */     {
/*     */       
/* 255 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 259 */     String name = elements[0];
/*     */     
/* 261 */     if (!Token.isValid(name))
/*     */     {
/*     */       
/* 264 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 268 */     WebSocketExtension extension = createInstance(name);
/*     */ 
/*     */     
/* 271 */     for (int i = 1; i < elements.length; i++) {
/*     */ 
/*     */       
/* 274 */       String[] pair = elements[i].split("\\s*=\\s*", 2);
/*     */ 
/*     */       
/* 277 */       if (pair.length == 0 || pair[0].length() == 0) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 284 */       String key = pair[0];
/*     */       
/* 286 */       if (!Token.isValid(key)) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 294 */       String value = extractValue(pair);
/*     */       
/* 296 */       if (value != null)
/*     */       {
/* 298 */         if (!Token.isValid(value)) {
/*     */           continue;
/*     */         }
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 307 */       extension.setParameter(key, value);
/*     */       continue;
/*     */     } 
/* 310 */     return extension;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String extractValue(String[] pair) {
/* 316 */     if (pair.length != 2)
/*     */     {
/* 318 */       return null;
/*     */     }
/*     */     
/* 321 */     return Token.unquote(pair[1]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static WebSocketExtension createInstance(String name) {
/* 327 */     if ("permessage-deflate".equals(name))
/*     */     {
/* 329 */       return new PerMessageDeflateExtension(name);
/*     */     }
/*     */     
/* 332 */     return new WebSocketExtension(name);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\WebSocketExtension.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */