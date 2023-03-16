/*     */ package okhttp3.internal.publicsuffix;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.net.IDN;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import okhttp3.internal.platform.Platform;
/*     */ import okio.BufferedSource;
/*     */ import okio.GzipSource;
/*     */ import okio.Okio;
/*     */ import okio.Source;
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
/*     */ public final class PublicSuffixDatabase
/*     */ {
/*     */   public static final String PUBLIC_SUFFIX_RESOURCE = "publicsuffixes.gz";
/*  38 */   private static final byte[] WILDCARD_LABEL = new byte[] { 42 };
/*  39 */   private static final String[] EMPTY_RULE = new String[0];
/*  40 */   private static final String[] PREVAILING_RULE = new String[] { "*" };
/*     */   
/*     */   private static final byte EXCEPTION_MARKER = 33;
/*     */   
/*  44 */   private static final PublicSuffixDatabase instance = new PublicSuffixDatabase();
/*     */ 
/*     */   
/*  47 */   private final AtomicBoolean listRead = new AtomicBoolean(false);
/*     */ 
/*     */   
/*  50 */   private final CountDownLatch readCompleteLatch = new CountDownLatch(1);
/*     */ 
/*     */   
/*     */   private byte[] publicSuffixListBytes;
/*     */ 
/*     */   
/*     */   private byte[] publicSuffixExceptionListBytes;
/*     */ 
/*     */   
/*     */   public static PublicSuffixDatabase get() {
/*  60 */     return instance;
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
/*     */   public String getEffectiveTldPlusOne(String domain) {
/*     */     int firstLabelOffset;
/*  79 */     if (domain == null) throw new NullPointerException("domain == null");
/*     */ 
/*     */     
/*  82 */     String unicodeDomain = IDN.toUnicode(domain);
/*  83 */     String[] domainLabels = unicodeDomain.split("\\.");
/*  84 */     String[] rule = findMatchingRule(domainLabels);
/*  85 */     if (domainLabels.length == rule.length && rule[0].charAt(0) != '!')
/*     */     {
/*  87 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  91 */     if (rule[0].charAt(0) == '!') {
/*     */       
/*  93 */       firstLabelOffset = domainLabels.length - rule.length;
/*     */     } else {
/*     */       
/*  96 */       firstLabelOffset = domainLabels.length - rule.length + 1;
/*     */     } 
/*     */     
/*  99 */     StringBuilder effectiveTldPlusOne = new StringBuilder();
/* 100 */     String[] punycodeLabels = domain.split("\\.");
/* 101 */     for (int i = firstLabelOffset; i < punycodeLabels.length; i++) {
/* 102 */       effectiveTldPlusOne.append(punycodeLabels[i]).append('.');
/*     */     }
/* 104 */     effectiveTldPlusOne.deleteCharAt(effectiveTldPlusOne.length() - 1);
/*     */     
/* 106 */     return effectiveTldPlusOne.toString();
/*     */   }
/*     */   
/*     */   private String[] findMatchingRule(String[] domainLabels) {
/* 110 */     if (!this.listRead.get() && this.listRead.compareAndSet(false, true)) {
/* 111 */       readTheListUninterruptibly();
/*     */     } else {
/*     */       try {
/* 114 */         this.readCompleteLatch.await();
/* 115 */       } catch (InterruptedException ignored) {
/* 116 */         Thread.currentThread().interrupt();
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     synchronized (this) {
/* 121 */       if (this.publicSuffixListBytes == null) {
/* 122 */         throw new IllegalStateException("Unable to load publicsuffixes.gz resource from the classpath.");
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 128 */     byte[][] domainLabelsUtf8Bytes = new byte[domainLabels.length][];
/* 129 */     for (int i = 0; i < domainLabels.length; i++) {
/* 130 */       domainLabelsUtf8Bytes[i] = domainLabels[i].getBytes(StandardCharsets.UTF_8);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 135 */     String exactMatch = null;
/* 136 */     for (int j = 0; j < domainLabelsUtf8Bytes.length; j++) {
/* 137 */       String rule = binarySearchBytes(this.publicSuffixListBytes, domainLabelsUtf8Bytes, j);
/* 138 */       if (rule != null) {
/* 139 */         exactMatch = rule;
/*     */ 
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 149 */     String wildcardMatch = null;
/* 150 */     if (domainLabelsUtf8Bytes.length > 1) {
/* 151 */       byte[][] labelsWithWildcard = (byte[][])domainLabelsUtf8Bytes.clone();
/* 152 */       for (int labelIndex = 0; labelIndex < labelsWithWildcard.length - 1; labelIndex++) {
/* 153 */         labelsWithWildcard[labelIndex] = WILDCARD_LABEL;
/* 154 */         String rule = binarySearchBytes(this.publicSuffixListBytes, labelsWithWildcard, labelIndex);
/* 155 */         if (rule != null) {
/* 156 */           wildcardMatch = rule;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 163 */     String exception = null;
/* 164 */     if (wildcardMatch != null) {
/* 165 */       for (int labelIndex = 0; labelIndex < domainLabelsUtf8Bytes.length - 1; labelIndex++) {
/* 166 */         String rule = binarySearchBytes(this.publicSuffixExceptionListBytes, domainLabelsUtf8Bytes, labelIndex);
/*     */         
/* 168 */         if (rule != null) {
/* 169 */           exception = rule;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 175 */     if (exception != null) {
/*     */       
/* 177 */       exception = "!" + exception;
/* 178 */       return exception.split("\\.");
/* 179 */     }  if (exactMatch == null && wildcardMatch == null) {
/* 180 */       return PREVAILING_RULE;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 185 */     String[] exactRuleLabels = (exactMatch != null) ? exactMatch.split("\\.") : EMPTY_RULE;
/*     */ 
/*     */ 
/*     */     
/* 189 */     String[] wildcardRuleLabels = (wildcardMatch != null) ? wildcardMatch.split("\\.") : EMPTY_RULE;
/*     */     
/* 191 */     return (exactRuleLabels.length > wildcardRuleLabels.length) ? 
/* 192 */       exactRuleLabels : 
/* 193 */       wildcardRuleLabels;
/*     */   }
/*     */   
/*     */   private static String binarySearchBytes(byte[] bytesToSearch, byte[][] labels, int labelIndex) {
/* 197 */     int low = 0;
/* 198 */     int high = bytesToSearch.length;
/* 199 */     String match = null;
/* 200 */     while (low < high) {
/* 201 */       int compareResult, mid = (low + high) / 2;
/*     */ 
/*     */       
/* 204 */       while (mid > -1 && bytesToSearch[mid] != 10) {
/* 205 */         mid--;
/*     */       }
/* 207 */       mid++;
/*     */ 
/*     */       
/* 210 */       int end = 1;
/* 211 */       while (bytesToSearch[mid + end] != 10) {
/* 212 */         end++;
/*     */       }
/* 214 */       int publicSuffixLength = mid + end - mid;
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 219 */       int currentLabelIndex = labelIndex;
/* 220 */       int currentLabelByteIndex = 0;
/* 221 */       int publicSuffixByteIndex = 0;
/*     */       
/* 223 */       boolean expectDot = false;
/*     */       while (true) {
/*     */         int byte0;
/* 226 */         if (expectDot) {
/* 227 */           byte0 = 46;
/* 228 */           expectDot = false;
/*     */         } else {
/* 230 */           byte0 = labels[currentLabelIndex][currentLabelByteIndex] & 0xFF;
/*     */         } 
/*     */         
/* 233 */         int byte1 = bytesToSearch[mid + publicSuffixByteIndex] & 0xFF;
/*     */         
/* 235 */         compareResult = byte0 - byte1;
/* 236 */         if (compareResult != 0)
/*     */           break; 
/* 238 */         publicSuffixByteIndex++;
/* 239 */         currentLabelByteIndex++;
/* 240 */         if (publicSuffixByteIndex == publicSuffixLength)
/*     */           break; 
/* 242 */         if ((labels[currentLabelIndex]).length == currentLabelByteIndex) {
/*     */ 
/*     */           
/* 245 */           if (currentLabelIndex == labels.length - 1) {
/*     */             break;
/*     */           }
/* 248 */           currentLabelIndex++;
/* 249 */           currentLabelByteIndex = -1;
/* 250 */           expectDot = true;
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 255 */       if (compareResult < 0) {
/* 256 */         high = mid - 1; continue;
/* 257 */       }  if (compareResult > 0) {
/* 258 */         low = mid + end + 1;
/*     */         continue;
/*     */       } 
/* 261 */       int publicSuffixBytesLeft = publicSuffixLength - publicSuffixByteIndex;
/* 262 */       int labelBytesLeft = (labels[currentLabelIndex]).length - currentLabelByteIndex;
/* 263 */       for (int i = currentLabelIndex + 1; i < labels.length; i++) {
/* 264 */         labelBytesLeft += (labels[i]).length;
/*     */       }
/*     */       
/* 267 */       if (labelBytesLeft < publicSuffixBytesLeft) {
/* 268 */         high = mid - 1; continue;
/* 269 */       }  if (labelBytesLeft > publicSuffixBytesLeft) {
/* 270 */         low = mid + end + 1;
/*     */         continue;
/*     */       } 
/* 273 */       match = new String(bytesToSearch, mid, publicSuffixLength, StandardCharsets.UTF_8);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 278 */     return match;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readTheListUninterruptibly() {
/* 287 */     boolean interrupted = false;
/*     */     
/*     */     while (true) {
/*     */       try {
/* 291 */         readTheList();
/*     */         return;
/* 293 */       } catch (InterruptedIOException e) {
/* 294 */         Thread.interrupted();
/*     */       }
/* 296 */       catch (IOException e) {
/* 297 */         Platform.get().log(5, "Failed to read public suffix list", e);
/*     */ 
/*     */         
/*     */         return;
/*     */       } finally {
/* 302 */         if (interrupted) {
/* 303 */           Thread.currentThread().interrupt();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void readTheList() throws IOException {
/*     */     byte[] publicSuffixListBytes, publicSuffixExceptionListBytes;
/* 312 */     InputStream resource = PublicSuffixDatabase.class.getResourceAsStream("publicsuffixes.gz");
/* 313 */     if (resource == null)
/*     */       return; 
/* 315 */     try (BufferedSource bufferedSource = Okio.buffer((Source)new GzipSource(Okio.source(resource)))) {
/* 316 */       int totalBytes = bufferedSource.readInt();
/* 317 */       publicSuffixListBytes = new byte[totalBytes];
/* 318 */       bufferedSource.readFully(publicSuffixListBytes);
/*     */       
/* 320 */       int totalExceptionBytes = bufferedSource.readInt();
/* 321 */       publicSuffixExceptionListBytes = new byte[totalExceptionBytes];
/* 322 */       bufferedSource.readFully(publicSuffixExceptionListBytes);
/*     */     } 
/*     */     
/* 325 */     synchronized (this) {
/* 326 */       this.publicSuffixListBytes = publicSuffixListBytes;
/* 327 */       this.publicSuffixExceptionListBytes = publicSuffixExceptionListBytes;
/*     */     } 
/*     */     
/* 330 */     this.readCompleteLatch.countDown();
/*     */   }
/*     */ 
/*     */   
/*     */   void setListBytes(byte[] publicSuffixListBytes, byte[] publicSuffixExceptionListBytes) {
/* 335 */     this.publicSuffixListBytes = publicSuffixListBytes;
/* 336 */     this.publicSuffixExceptionListBytes = publicSuffixExceptionListBytes;
/* 337 */     this.listRead.set(true);
/* 338 */     this.readCompleteLatch.countDown();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\publicsuffix\PublicSuffixDatabase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */