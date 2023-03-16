/*    */ package okhttp3;
/*    */ 
/*    */ import java.net.InetAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.Arrays;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface Dns
/*    */ {
/*    */   public static final Dns SYSTEM;
/*    */   
/*    */   static {
/* 36 */     SYSTEM = (hostname -> {
/*    */         if (hostname == null)
/*    */           throw new UnknownHostException("hostname == null");  try {
/*    */           return Arrays.asList(InetAddress.getAllByName(hostname));
/* 40 */         } catch (NullPointerException e) {
/*    */           UnknownHostException unknownHostException = new UnknownHostException("Broken system behaviour for dns lookup of " + hostname);
/*    */           unknownHostException.initCause(e);
/*    */           throw unknownHostException;
/*    */         } 
/*    */       });
/*    */   }
/*    */   
/*    */   List<InetAddress> lookup(String paramString) throws UnknownHostException;
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Dns.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */