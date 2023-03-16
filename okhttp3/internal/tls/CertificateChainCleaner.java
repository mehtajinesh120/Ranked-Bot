/*    */ package okhttp3.internal.tls;
/*    */ 
/*    */ import java.security.cert.Certificate;
/*    */ import java.security.cert.X509Certificate;
/*    */ import java.util.List;
/*    */ import javax.net.ssl.SSLPeerUnverifiedException;
/*    */ import javax.net.ssl.X509TrustManager;
/*    */ import okhttp3.internal.platform.Platform;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CertificateChainCleaner
/*    */ {
/*    */   public abstract List<Certificate> clean(List<Certificate> paramList, String paramString) throws SSLPeerUnverifiedException;
/*    */   
/*    */   public static CertificateChainCleaner get(X509TrustManager trustManager) {
/* 41 */     return Platform.get().buildCertificateChainCleaner(trustManager);
/*    */   }
/*    */   
/*    */   public static CertificateChainCleaner get(X509Certificate... caCerts) {
/* 45 */     return new BasicCertificateChainCleaner(caCerts);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\tls\CertificateChainCleaner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */