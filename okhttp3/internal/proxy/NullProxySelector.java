/*    */ package okhttp3.internal.proxy;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.Proxy;
/*    */ import java.net.ProxySelector;
/*    */ import java.net.SocketAddress;
/*    */ import java.net.URI;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ 
/*    */ public class NullProxySelector
/*    */   extends ProxySelector
/*    */ {
/*    */   public List<Proxy> select(URI uri) {
/* 31 */     if (uri == null) {
/* 32 */       throw new IllegalArgumentException("uri must not be null");
/*    */     }
/* 34 */     return Collections.singletonList(Proxy.NO_PROXY);
/*    */   }
/*    */   
/*    */   public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {}
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\proxy\NullProxySelector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */