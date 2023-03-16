/*     */ package okhttp3.internal.tls;
/*     */ 
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.SSLPeerUnverifiedException;
/*     */ import javax.security.auth.x500.X500Principal;
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
/*     */ public final class BasicCertificateChainCleaner
/*     */   extends CertificateChainCleaner
/*     */ {
/*     */   private static final int MAX_SIGNERS = 9;
/*  51 */   private final Map<X500Principal, Set<X509Certificate>> subjectToCaCerts = new LinkedHashMap<>(); public BasicCertificateChainCleaner(X509Certificate... caCerts) {
/*  52 */     for (X509Certificate caCert : caCerts) {
/*  53 */       X500Principal subject = caCert.getSubjectX500Principal();
/*  54 */       Set<X509Certificate> subjectCaCerts = this.subjectToCaCerts.get(subject);
/*  55 */       if (subjectCaCerts == null) {
/*  56 */         subjectCaCerts = new LinkedHashSet<>(1);
/*  57 */         this.subjectToCaCerts.put(subject, subjectCaCerts);
/*     */       } 
/*  59 */       subjectCaCerts.add(caCert);
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
/*     */   public List<Certificate> clean(List<Certificate> chain, String hostname) throws SSLPeerUnverifiedException {
/*  72 */     Deque<Certificate> queue = new ArrayDeque<>(chain);
/*  73 */     List<Certificate> result = new ArrayList<>();
/*  74 */     result.add(queue.removeFirst());
/*  75 */     boolean foundTrustedCertificate = false;
/*     */     
/*     */     int c;
/*  78 */     label27: for (c = 0; c < 9; c++) {
/*  79 */       X509Certificate toVerify = (X509Certificate)result.get(result.size() - 1);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       X509Certificate trustedCert = findByIssuerAndSignature(toVerify);
/*  85 */       if (trustedCert != null) {
/*  86 */         if (result.size() > 1 || !toVerify.equals(trustedCert)) {
/*  87 */           result.add(trustedCert);
/*     */         }
/*  89 */         if (verifySignature(trustedCert, trustedCert)) {
/*  90 */           return result;
/*     */         }
/*  92 */         foundTrustedCertificate = true;
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */         
/*  98 */         for (Iterator<Certificate> i = queue.iterator(); i.hasNext(); ) {
/*  99 */           X509Certificate signingCert = (X509Certificate)i.next();
/* 100 */           if (verifySignature(toVerify, signingCert)) {
/* 101 */             i.remove();
/* 102 */             result.add(signingCert);
/*     */             
/*     */             continue label27;
/*     */           } 
/*     */         } 
/*     */         
/* 108 */         if (foundTrustedCertificate) {
/* 109 */           return result;
/*     */         }
/*     */ 
/*     */         
/* 113 */         throw new SSLPeerUnverifiedException("Failed to find a trusted cert that signed " + toVerify);
/*     */       } 
/*     */     } 
/*     */     
/* 117 */     throw new SSLPeerUnverifiedException("Certificate chain too long: " + result);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean verifySignature(X509Certificate toVerify, X509Certificate signingCert) {
/* 122 */     if (!toVerify.getIssuerDN().equals(signingCert.getSubjectDN())) return false; 
/*     */     try {
/* 124 */       toVerify.verify(signingCert.getPublicKey());
/* 125 */       return true;
/* 126 */     } catch (GeneralSecurityException verifyFailed) {
/* 127 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private X509Certificate findByIssuerAndSignature(X509Certificate cert) {
/* 133 */     X500Principal issuer = cert.getIssuerX500Principal();
/* 134 */     Set<X509Certificate> subjectCaCerts = this.subjectToCaCerts.get(issuer);
/* 135 */     if (subjectCaCerts == null) return null;
/*     */     
/* 137 */     for (X509Certificate caCert : subjectCaCerts) {
/* 138 */       PublicKey publicKey = caCert.getPublicKey();
/*     */       try {
/* 140 */         cert.verify(publicKey);
/* 141 */         return caCert;
/* 142 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/* 146 */     return null;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 150 */     return this.subjectToCaCerts.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object other) {
/* 154 */     if (other == this) return true; 
/* 155 */     return (other instanceof BasicCertificateChainCleaner && ((BasicCertificateChainCleaner)other).subjectToCaCerts
/* 156 */       .equals(this.subjectToCaCerts));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\tls\BasicCertificateChainCleaner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */