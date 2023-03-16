/*      */ package com.iwebpp.crypto;
/*      */ 
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.security.SecureRandom;
/*      */ import java.util.Base64;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class TweetNaclFast
/*      */ {
/*      */   private static final String TAG = "TweetNaclFast";
/*      */   
/*      */   public static final class Box
/*      */   {
/*      */     private static final String TAG = "Box";
/*      */     private AtomicLong nonce;
/*      */     private byte[] theirPublicKey;
/*      */     private byte[] mySecretKey;
/*      */     private byte[] sharedKey;
/*      */     public static final int publicKeyLength = 32;
/*      */     public static final int secretKeyLength = 32;
/*      */     public static final int sharedKeyLength = 32;
/*      */     public static final int nonceLength = 24;
/*      */     public static final int zerobytesLength = 32;
/*      */     public static final int boxzerobytesLength = 16;
/*      */     public static final int overheadLength = 16;
/*      */     
/*      */     public Box(byte[] theirPublicKey, byte[] mySecretKey) {
/*   38 */       this(theirPublicKey, mySecretKey, 68L);
/*      */     }
/*      */     
/*      */     public Box(byte[] theirPublicKey, byte[] mySecretKey, long nonce) {
/*   42 */       this.theirPublicKey = theirPublicKey;
/*   43 */       this.mySecretKey = mySecretKey;
/*      */       
/*   45 */       this.nonce = new AtomicLong(nonce);
/*      */ 
/*      */       
/*   48 */       before();
/*      */     }
/*      */     
/*      */     public void setNonce(long nonce) {
/*   52 */       this.nonce.set(nonce);
/*      */     }
/*      */     public long getNonce() {
/*   55 */       return this.nonce.get();
/*      */     }
/*      */     public long incrNonce() {
/*   58 */       return this.nonce.incrementAndGet();
/*      */     }
/*      */     
/*      */     private byte[] generateNonce() {
/*   62 */       long nonce = this.nonce.get();
/*      */       
/*   64 */       byte[] n = new byte[24];
/*   65 */       for (int i = 0; i < 24; i += 8) {
/*   66 */         n[i + 0] = (byte)(int)(nonce >>> 0L);
/*   67 */         n[i + 1] = (byte)(int)(nonce >>> 8L);
/*   68 */         n[i + 2] = (byte)(int)(nonce >>> 16L);
/*   69 */         n[i + 3] = (byte)(int)(nonce >>> 24L);
/*   70 */         n[i + 4] = (byte)(int)(nonce >>> 32L);
/*   71 */         n[i + 5] = (byte)(int)(nonce >>> 40L);
/*   72 */         n[i + 6] = (byte)(int)(nonce >>> 48L);
/*   73 */         n[i + 7] = (byte)(int)(nonce >>> 56L);
/*      */       } 
/*      */       
/*   76 */       return n;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] box(byte[] message) {
/*   89 */       if (message == null) return null; 
/*   90 */       return box(message, 0, message.length);
/*      */     }
/*      */     
/*      */     public byte[] box(byte[] message, int moff) {
/*   94 */       if (message == null || message.length <= moff) return null; 
/*   95 */       return box(message, moff, message.length - moff);
/*      */     }
/*      */     
/*      */     public byte[] box(byte[] message, int moff, int mlen) {
/*   99 */       if (message == null || message.length < moff + mlen) return null;
/*      */ 
/*      */       
/*  102 */       if (this.sharedKey == null) before();
/*      */       
/*  104 */       return after(message, moff, mlen);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] box(byte[] message, byte[] theNonce) {
/*  119 */       if (message == null) return null; 
/*  120 */       return box(message, 0, message.length, theNonce);
/*      */     }
/*      */     
/*      */     public byte[] box(byte[] message, int moff, byte[] theNonce) {
/*  124 */       if (message == null || message.length <= moff) return null; 
/*  125 */       return box(message, moff, message.length - moff, theNonce);
/*      */     }
/*      */     
/*      */     public byte[] box(byte[] message, int moff, int mlen, byte[] theNonce) {
/*  129 */       if (message == null || message.length < moff + mlen || theNonce == null || theNonce.length != 24)
/*      */       {
/*  131 */         return null;
/*      */       }
/*      */       
/*  134 */       if (this.sharedKey == null) before();
/*      */       
/*  136 */       return after(message, moff, mlen, theNonce);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] open(byte[] box) {
/*  147 */       if (box == null) return null;
/*      */ 
/*      */       
/*  150 */       if (this.sharedKey == null) before();
/*      */       
/*  152 */       return open_after(box, 0, box.length);
/*      */     }
/*      */     public byte[] open(byte[] box, int boxoff) {
/*  155 */       if (box == null || box.length <= boxoff) return null;
/*      */ 
/*      */       
/*  158 */       if (this.sharedKey == null) before();
/*      */       
/*  160 */       return open_after(box, boxoff, box.length - boxoff);
/*      */     }
/*      */     public byte[] open(byte[] box, int boxoff, int boxlen) {
/*  163 */       if (box == null || box.length < boxoff + boxlen) return null;
/*      */ 
/*      */       
/*  166 */       if (this.sharedKey == null) before();
/*      */       
/*  168 */       return open_after(box, boxoff, boxlen);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] open(byte[] box, byte[] theNonce) {
/*  180 */       if (box == null || theNonce == null || theNonce.length != 24)
/*      */       {
/*  182 */         return null;
/*      */       }
/*      */       
/*  185 */       if (this.sharedKey == null) before();
/*      */       
/*  187 */       return open_after(box, 0, box.length, theNonce);
/*      */     }
/*      */     public byte[] open(byte[] box, int boxoff, byte[] theNonce) {
/*  190 */       if (box == null || box.length <= boxoff || theNonce == null || theNonce.length != 24)
/*      */       {
/*  192 */         return null;
/*      */       }
/*      */       
/*  195 */       if (this.sharedKey == null) before();
/*      */       
/*  197 */       return open_after(box, boxoff, box.length - boxoff, theNonce);
/*      */     }
/*      */     public byte[] open(byte[] box, int boxoff, int boxlen, byte[] theNonce) {
/*  200 */       if (box == null || box.length < boxoff + boxlen || theNonce == null || theNonce.length != 24)
/*      */       {
/*  202 */         return null;
/*      */       }
/*      */       
/*  205 */       if (this.sharedKey == null) before();
/*      */       
/*  207 */       return open_after(box, boxoff, boxlen, theNonce);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] before() {
/*  217 */       if (this.sharedKey == null) {
/*  218 */         this.sharedKey = new byte[32];
/*  219 */         TweetNaclFast.crypto_box_beforenm(this.sharedKey, this.theirPublicKey, this.mySecretKey);
/*      */       } 
/*      */       
/*  222 */       return this.sharedKey;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] after(byte[] message, int moff, int mlen) {
/*  230 */       return after(message, moff, mlen, generateNonce());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] after(byte[] message, int moff, int mlen, byte[] theNonce) {
/*  240 */       if (message == null || message.length < moff + mlen || theNonce == null || theNonce.length != 24)
/*      */       {
/*  242 */         return null;
/*      */       }
/*      */       
/*  245 */       byte[] m = new byte[mlen + 32];
/*      */ 
/*      */       
/*  248 */       byte[] c = new byte[m.length];
/*      */       
/*  250 */       for (int i = 0; i < mlen; i++) {
/*  251 */         m[i + 32] = message[i + moff];
/*      */       }
/*  253 */       if (0 != TweetNaclFast.crypto_box_afternm(c, m, m.length, theNonce, this.sharedKey)) {
/*  254 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  258 */       byte[] ret = new byte[c.length - 16];
/*      */       
/*  260 */       for (int j = 0; j < ret.length; j++) {
/*  261 */         ret[j] = c[j + 16];
/*      */       }
/*  263 */       return ret;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] open_after(byte[] box, int boxoff, int boxlen) {
/*  272 */       return open_after(box, boxoff, boxlen, generateNonce());
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] open_after(byte[] box, int boxoff, int boxlen, byte[] theNonce) {
/*  277 */       if (box == null || box.length < boxoff + boxlen || boxlen < 16) {
/*  278 */         return null;
/*      */       }
/*      */       
/*  281 */       byte[] c = new byte[boxlen + 16];
/*      */ 
/*      */       
/*  284 */       byte[] m = new byte[c.length];
/*      */       
/*  286 */       for (int i = 0; i < boxlen; i++) {
/*  287 */         c[i + 16] = box[i + boxoff];
/*      */       }
/*  289 */       if (TweetNaclFast.crypto_box_open_afternm(m, c, c.length, theNonce, this.sharedKey) != 0) {
/*  290 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  294 */       byte[] ret = new byte[m.length - 32];
/*      */       
/*  296 */       for (int j = 0; j < ret.length; j++) {
/*  297 */         ret[j] = m[j + 32];
/*      */       }
/*  299 */       return ret;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static class KeyPair
/*      */     {
/*  348 */       private byte[] publicKey = new byte[32];
/*  349 */       private byte[] secretKey = new byte[32];
/*      */ 
/*      */       
/*      */       public byte[] getPublicKey() {
/*  353 */         return this.publicKey;
/*      */       }
/*      */       
/*      */       public byte[] getSecretKey() {
/*  357 */         return this.secretKey;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static KeyPair keyPair() {
/*  367 */       KeyPair kp = new KeyPair();
/*      */       
/*  369 */       TweetNaclFast.crypto_box_keypair(kp.getPublicKey(), kp.getSecretKey());
/*  370 */       return kp;
/*      */     }
/*      */     
/*      */     public static KeyPair keyPair_fromSecretKey(byte[] secretKey) {
/*  374 */       KeyPair kp = new KeyPair();
/*  375 */       byte[] sk = kp.getSecretKey();
/*  376 */       byte[] pk = kp.getPublicKey();
/*      */ 
/*      */       
/*  379 */       for (int i = 0; i < sk.length; i++) {
/*  380 */         sk[i] = secretKey[i];
/*      */       }
/*  382 */       TweetNaclFast.crypto_scalarmult_base(pk, sk);
/*  383 */       return kp;
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public static final class SecretBox
/*      */   {
/*      */     private static final String TAG = "SecretBox";
/*      */     
/*      */     private AtomicLong nonce;
/*      */     private byte[] key;
/*      */     public static final int keyLength = 32;
/*      */     public static final int nonceLength = 24;
/*      */     public static final int overheadLength = 16;
/*      */     public static final int zerobytesLength = 32;
/*      */     public static final int boxzerobytesLength = 16;
/*      */     
/*      */     public SecretBox(byte[] key) {
/*  401 */       this(key, 68L);
/*      */     }
/*      */     
/*      */     public SecretBox(byte[] key, long nonce) {
/*  405 */       this.key = key;
/*      */       
/*  407 */       this.nonce = new AtomicLong(nonce);
/*      */     }
/*      */     
/*      */     public void setNonce(long nonce) {
/*  411 */       this.nonce.set(nonce);
/*      */     }
/*      */     public long getNonce() {
/*  414 */       return this.nonce.get();
/*      */     }
/*      */     public long incrNonce() {
/*  417 */       return this.nonce.incrementAndGet();
/*      */     }
/*      */     
/*      */     private byte[] generateNonce() {
/*  421 */       long nonce = this.nonce.get();
/*      */       
/*  423 */       byte[] n = new byte[24];
/*  424 */       for (int i = 0; i < 24; i += 8) {
/*  425 */         n[i + 0] = (byte)(int)(nonce >>> 0L);
/*  426 */         n[i + 1] = (byte)(int)(nonce >>> 8L);
/*  427 */         n[i + 2] = (byte)(int)(nonce >>> 16L);
/*  428 */         n[i + 3] = (byte)(int)(nonce >>> 24L);
/*  429 */         n[i + 4] = (byte)(int)(nonce >>> 32L);
/*  430 */         n[i + 5] = (byte)(int)(nonce >>> 40L);
/*  431 */         n[i + 6] = (byte)(int)(nonce >>> 48L);
/*  432 */         n[i + 7] = (byte)(int)(nonce >>> 56L);
/*      */       } 
/*      */       
/*  435 */       return n;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] box(byte[] message) {
/*  447 */       if (message == null) return null; 
/*  448 */       return box(message, 0, message.length);
/*      */     }
/*      */     
/*      */     public byte[] box(byte[] message, int moff) {
/*  452 */       if (message == null || message.length <= moff) return null; 
/*  453 */       return box(message, moff, message.length - moff);
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] box(byte[] message, int moff, int mlen) {
/*  458 */       if (message == null || message.length < moff + mlen)
/*  459 */         return null; 
/*  460 */       return box(message, moff, message.length - moff, generateNonce());
/*      */     }
/*      */     
/*      */     public byte[] box(byte[] message, byte[] theNonce) {
/*  464 */       if (message == null) return null; 
/*  465 */       return box(message, 0, message.length, theNonce);
/*      */     }
/*      */     
/*      */     public byte[] box(byte[] message, int moff, byte[] theNonce) {
/*  469 */       if (message == null || message.length <= moff) return null; 
/*  470 */       return box(message, moff, message.length - moff, theNonce);
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] box(byte[] message, int moff, int mlen, byte[] theNonce) {
/*  475 */       if (message == null || message.length < moff + mlen || theNonce == null || theNonce.length != 24)
/*      */       {
/*  477 */         return null;
/*      */       }
/*      */       
/*  480 */       byte[] m = new byte[mlen + 32];
/*      */ 
/*      */       
/*  483 */       byte[] c = new byte[m.length];
/*      */       
/*  485 */       for (int i = 0; i < mlen; i++) {
/*  486 */         m[i + 32] = message[i + moff];
/*      */       }
/*  488 */       if (0 != TweetNaclFast.crypto_secretbox(c, m, m.length, theNonce, this.key)) {
/*  489 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  494 */       byte[] ret = new byte[c.length - 16];
/*      */       
/*  496 */       for (int j = 0; j < ret.length; j++) {
/*  497 */         ret[j] = c[j + 16];
/*      */       }
/*  499 */       return ret;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] open(byte[] box) {
/*  510 */       if (box == null) return null; 
/*  511 */       return open(box, 0, box.length);
/*      */     }
/*      */     
/*      */     public byte[] open(byte[] box, int boxoff) {
/*  515 */       if (box == null || box.length <= boxoff) return null; 
/*  516 */       return open(box, boxoff, box.length - boxoff);
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] open(byte[] box, int boxoff, int boxlen) {
/*  521 */       if (box == null || box.length < boxoff + boxlen || boxlen < 16)
/*  522 */         return null; 
/*  523 */       return open(box, boxoff, box.length - boxoff, generateNonce());
/*      */     }
/*      */     
/*      */     public byte[] open(byte[] box, byte[] theNonce) {
/*  527 */       if (box == null) return null; 
/*  528 */       return open(box, 0, box.length, theNonce);
/*      */     }
/*      */     
/*      */     public byte[] open(byte[] box, int boxoff, byte[] theNonce) {
/*  532 */       if (box == null || box.length <= boxoff) return null; 
/*  533 */       return open(box, boxoff, box.length - boxoff, theNonce);
/*      */     }
/*      */ 
/*      */     
/*      */     public byte[] open(byte[] box, int boxoff, int boxlen, byte[] theNonce) {
/*  538 */       if (box == null || box.length < boxoff + boxlen || boxlen < 16 || theNonce == null || theNonce.length != 24)
/*      */       {
/*  540 */         return null;
/*      */       }
/*      */       
/*  543 */       byte[] c = new byte[boxlen + 16];
/*      */ 
/*      */       
/*  546 */       byte[] m = new byte[c.length];
/*      */       
/*  548 */       for (int i = 0; i < boxlen; i++) {
/*  549 */         c[i + 16] = box[i + boxoff];
/*      */       }
/*  551 */       if (0 != TweetNaclFast.crypto_secretbox_open(m, c, c.length, theNonce, this.key)) {
/*  552 */         return null;
/*      */       }
/*      */ 
/*      */       
/*  556 */       byte[] ret = new byte[m.length - 32];
/*      */       
/*  558 */       for (int j = 0; j < ret.length; j++) {
/*  559 */         ret[j] = m[j + 32];
/*      */       }
/*  561 */       return ret;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class ScalarMult
/*      */   {
/*      */     private static final String TAG = "ScalarMult";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final int scalarLength = 32;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final int groupElementLength = 32;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static byte[] scalseMult(byte[] n, byte[] p) {
/*  609 */       if (n.length != 32 || p.length != 32) {
/*  610 */         return null;
/*      */       }
/*  612 */       byte[] q = new byte[32];
/*      */       
/*  614 */       TweetNaclFast.crypto_scalarmult(q, n, p);
/*      */       
/*  616 */       return q;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static byte[] scalseMult_base(byte[] n) {
/*  625 */       if (n.length != 32) {
/*  626 */         return null;
/*      */       }
/*  628 */       byte[] q = new byte[32];
/*      */       
/*  630 */       TweetNaclFast.crypto_scalarmult_base(q, n);
/*      */       
/*  632 */       return q;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Hash
/*      */   {
/*      */     private static final String TAG = "Hash";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static final int hashLength = 64;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static byte[] sha512(byte[] message) {
/*  663 */       if (message == null || message.length <= 0) {
/*  664 */         return null;
/*      */       }
/*  666 */       byte[] out = new byte[64];
/*      */       
/*  668 */       TweetNaclFast.crypto_hash(out, message);
/*      */       
/*  670 */       return out;
/*      */     }
/*      */     public static byte[] sha512(String message) throws UnsupportedEncodingException {
/*  673 */       return sha512(message.getBytes("utf-8"));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class Signature
/*      */   {
/*      */     private static final String TAG = "Signature";
/*      */     
/*      */     private byte[] theirPublicKey;
/*      */     
/*      */     private byte[] mySecretKey;
/*      */     
/*      */     public static final int publicKeyLength = 32;
/*      */     
/*      */     public static final int secretKeyLength = 64;
/*      */     
/*      */     public static final int seedLength = 32;
/*      */     
/*      */     public static final int signatureLength = 64;
/*      */ 
/*      */     
/*      */     public Signature(byte[] theirPublicKey, byte[] mySecretKey) {
/*  697 */       this.theirPublicKey = theirPublicKey;
/*  698 */       this.mySecretKey = mySecretKey;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] sign(byte[] message) {
/*  706 */       if (message == null) return null;
/*      */       
/*  708 */       return sign(message, 0, message.length);
/*      */     }
/*      */     public byte[] sign(byte[] message, int moff) {
/*  711 */       if (message == null || message.length <= moff) return null;
/*      */       
/*  713 */       return sign(message, moff, message.length - moff);
/*      */     }
/*      */     
/*      */     public byte[] sign(byte[] message, int moff, int mlen) {
/*  717 */       if (message == null || message.length < moff + mlen) {
/*  718 */         return null;
/*      */       }
/*      */       
/*  721 */       byte[] sm = new byte[mlen + 64];
/*      */       
/*  723 */       TweetNaclFast.crypto_sign(sm, -1L, message, moff, mlen, this.mySecretKey);
/*      */       
/*  725 */       return sm;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] open(byte[] signedMessage) {
/*  734 */       if (signedMessage == null) return null;
/*      */       
/*  736 */       return open(signedMessage, 0, signedMessage.length);
/*      */     }
/*      */     public byte[] open(byte[] signedMessage, int smoff) {
/*  739 */       if (signedMessage == null || signedMessage.length <= smoff) return null;
/*      */       
/*  741 */       return open(signedMessage, smoff, signedMessage.length - smoff);
/*      */     }
/*      */     
/*      */     public byte[] open(byte[] signedMessage, int smoff, int smlen) {
/*  745 */       if (signedMessage == null || signedMessage.length < smoff + smlen || smlen < 64) {
/*  746 */         return null;
/*      */       }
/*      */       
/*  749 */       byte[] tmp = new byte[smlen];
/*      */       
/*  751 */       if (0 != TweetNaclFast.crypto_sign_open(tmp, -1L, signedMessage, smoff, smlen, this.theirPublicKey)) {
/*  752 */         return null;
/*      */       }
/*      */       
/*  755 */       byte[] msg = new byte[smlen - 64];
/*  756 */       for (int i = 0; i < msg.length; i++) {
/*  757 */         msg[i] = signedMessage[smoff + i + 64];
/*      */       }
/*  759 */       return msg;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public byte[] detached(byte[] message) {
/*  767 */       byte[] signedMsg = sign(message);
/*  768 */       byte[] sig = new byte[64];
/*  769 */       for (int i = 0; i < sig.length; i++)
/*  770 */         sig[i] = signedMsg[i]; 
/*  771 */       return sig;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean detached_verify(byte[] message, byte[] signature) {
/*  780 */       if (signature.length != 64)
/*  781 */         return false; 
/*  782 */       if (this.theirPublicKey.length != 32)
/*  783 */         return false; 
/*  784 */       byte[] sm = new byte[64 + message.length];
/*  785 */       byte[] m = new byte[64 + message.length]; int i;
/*  786 */       for (i = 0; i < 64; i++)
/*  787 */         sm[i] = signature[i]; 
/*  788 */       for (i = 0; i < message.length; i++)
/*  789 */         sm[i + 64] = message[i]; 
/*  790 */       return (TweetNaclFast.crypto_sign_open(m, -1L, sm, 0, sm.length, this.theirPublicKey) >= 0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static class KeyPair
/*      */     {
/*  803 */       private byte[] publicKey = new byte[32];
/*  804 */       private byte[] secretKey = new byte[64];
/*      */ 
/*      */       
/*      */       public byte[] getPublicKey() {
/*  808 */         return this.publicKey;
/*      */       }
/*      */       
/*      */       public byte[] getSecretKey() {
/*  812 */         return this.secretKey;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static KeyPair keyPair() {
/*  821 */       KeyPair kp = new KeyPair();
/*      */       
/*  823 */       TweetNaclFast.crypto_sign_keypair(kp.getPublicKey(), kp.getSecretKey(), false);
/*  824 */       return kp;
/*      */     }
/*      */     public static KeyPair keyPair_fromSecretKey(byte[] secretKey) {
/*  827 */       KeyPair kp = new KeyPair();
/*  828 */       byte[] pk = kp.getPublicKey();
/*  829 */       byte[] sk = kp.getSecretKey();
/*      */       
/*      */       int i;
/*  832 */       for (i = 0; i < (kp.getSecretKey()).length; i++) {
/*  833 */         sk[i] = secretKey[i];
/*      */       }
/*      */       
/*  836 */       for (i = 0; i < (kp.getPublicKey()).length; i++) {
/*  837 */         pk[i] = secretKey[32 + i];
/*      */       }
/*  839 */       return kp;
/*      */     }
/*      */     
/*      */     public static KeyPair keyPair_fromSeed(byte[] seed) {
/*  843 */       KeyPair kp = new KeyPair();
/*  844 */       byte[] pk = kp.getPublicKey();
/*  845 */       byte[] sk = kp.getSecretKey();
/*      */ 
/*      */       
/*  848 */       for (int i = 0; i < 32; i++) {
/*  849 */         sk[i] = seed[i];
/*      */       }
/*      */       
/*  852 */       TweetNaclFast.crypto_sign_keypair(pk, sk, true);
/*      */       
/*  854 */       return kp;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  889 */   private static final byte[] _0 = new byte[16];
/*  890 */   private static final byte[] _9 = new byte[32];
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  895 */     _9[0] = 9;
/*      */   }
/*      */   
/*  898 */   private static final long[] gf0 = new long[16];
/*  899 */   private static final long[] gf1 = new long[16];
/*  900 */   private static final long[] _121665 = new long[16];
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  905 */     gf1[0] = 1L;
/*      */ 
/*      */     
/*  908 */     _121665[0] = 56129L; _121665[1] = 1L;
/*      */   }
/*      */   
/*  911 */   private static final long[] D = new long[] { 30883L, 4953L, 19914L, 30187L, 55467L, 16705L, 2637L, 112L, 59544L, 30585L, 16505L, 36039L, 65139L, 11119L, 27886L, 20995L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  917 */   private static final long[] D2 = new long[] { 61785L, 9906L, 39828L, 60374L, 45398L, 33411L, 5274L, 224L, 53552L, 61171L, 33010L, 6542L, 64743L, 22239L, 55772L, 9222L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  923 */   private static final long[] X = new long[] { 54554L, 36645L, 11616L, 51542L, 42930L, 38181L, 51040L, 26924L, 56412L, 64982L, 57905L, 49316L, 21502L, 52590L, 14035L, 8553L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  929 */   private static final long[] Y = new long[] { 26200L, 26214L, 26214L, 26214L, 26214L, 26214L, 26214L, 26214L, 26214L, 26214L, 26214L, 26214L, 26214L, 26214L, 26214L, 26214L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  935 */   private static final long[] I = new long[] { 41136L, 18958L, 6951L, 50414L, 58488L, 44335L, 6150L, 12099L, 55207L, 15867L, 153L, 11085L, 57099L, 20417L, 9344L, 11139L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void ts64(byte[] x, int xoff, long u) {
/*  947 */     x[7 + xoff] = (byte)(int)(u & 0xFFL); u >>>= 8L;
/*  948 */     x[6 + xoff] = (byte)(int)(u & 0xFFL); u >>>= 8L;
/*  949 */     x[5 + xoff] = (byte)(int)(u & 0xFFL); u >>>= 8L;
/*  950 */     x[4 + xoff] = (byte)(int)(u & 0xFFL); u >>>= 8L;
/*  951 */     x[3 + xoff] = (byte)(int)(u & 0xFFL); u >>>= 8L;
/*  952 */     x[2 + xoff] = (byte)(int)(u & 0xFFL); u >>>= 8L;
/*  953 */     x[1 + xoff] = (byte)(int)(u & 0xFFL); u >>>= 8L;
/*  954 */     x[0 + xoff] = (byte)(int)(u & 0xFFL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int vn(byte[] x, int xoff, byte[] y, int yoff, int n) {
/*  962 */     int d = 0;
/*  963 */     for (int i = 0; i < n; ) { d |= (x[i + xoff] ^ y[i + yoff]) & 0xFF; i++; }
/*  964 */      return (0x1 & d - 1 >>> 8) - 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int crypto_verify_16(byte[] x, int xoff, byte[] y, int yoff) {
/*  971 */     return vn(x, xoff, y, yoff, 16);
/*      */   }
/*      */   
/*      */   public static int crypto_verify_16(byte[] x, byte[] y) {
/*  975 */     return crypto_verify_16(x, 0, y, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int crypto_verify_32(byte[] x, int xoff, byte[] y, int yoff) {
/*  982 */     return vn(x, xoff, y, yoff, 32);
/*      */   }
/*      */   
/*      */   public static int crypto_verify_32(byte[] x, byte[] y) {
/*  986 */     return crypto_verify_32(x, 0, y, 0);
/*      */   }
/*      */   
/*      */   private static void core_salsa20(byte[] o, byte[] p, byte[] k, byte[] c) {
/*  990 */     int j0 = c[0] & 0xFF | (c[1] & 0xFF) << 8 | (c[2] & 0xFF) << 16 | (c[3] & 0xFF) << 24;
/*  991 */     int j1 = k[0] & 0xFF | (k[1] & 0xFF) << 8 | (k[2] & 0xFF) << 16 | (k[3] & 0xFF) << 24;
/*  992 */     int j2 = k[4] & 0xFF | (k[5] & 0xFF) << 8 | (k[6] & 0xFF) << 16 | (k[7] & 0xFF) << 24;
/*  993 */     int j3 = k[8] & 0xFF | (k[9] & 0xFF) << 8 | (k[10] & 0xFF) << 16 | (k[11] & 0xFF) << 24;
/*  994 */     int j4 = k[12] & 0xFF | (k[13] & 0xFF) << 8 | (k[14] & 0xFF) << 16 | (k[15] & 0xFF) << 24;
/*  995 */     int j5 = c[4] & 0xFF | (c[5] & 0xFF) << 8 | (c[6] & 0xFF) << 16 | (c[7] & 0xFF) << 24;
/*  996 */     int j6 = p[0] & 0xFF | (p[1] & 0xFF) << 8 | (p[2] & 0xFF) << 16 | (p[3] & 0xFF) << 24;
/*  997 */     int j7 = p[4] & 0xFF | (p[5] & 0xFF) << 8 | (p[6] & 0xFF) << 16 | (p[7] & 0xFF) << 24;
/*  998 */     int j8 = p[8] & 0xFF | (p[9] & 0xFF) << 8 | (p[10] & 0xFF) << 16 | (p[11] & 0xFF) << 24;
/*  999 */     int j9 = p[12] & 0xFF | (p[13] & 0xFF) << 8 | (p[14] & 0xFF) << 16 | (p[15] & 0xFF) << 24;
/* 1000 */     int j10 = c[8] & 0xFF | (c[9] & 0xFF) << 8 | (c[10] & 0xFF) << 16 | (c[11] & 0xFF) << 24;
/* 1001 */     int j11 = k[16] & 0xFF | (k[17] & 0xFF) << 8 | (k[18] & 0xFF) << 16 | (k[19] & 0xFF) << 24;
/* 1002 */     int j12 = k[20] & 0xFF | (k[21] & 0xFF) << 8 | (k[22] & 0xFF) << 16 | (k[23] & 0xFF) << 24;
/* 1003 */     int j13 = k[24] & 0xFF | (k[25] & 0xFF) << 8 | (k[26] & 0xFF) << 16 | (k[27] & 0xFF) << 24;
/* 1004 */     int j14 = k[28] & 0xFF | (k[29] & 0xFF) << 8 | (k[30] & 0xFF) << 16 | (k[31] & 0xFF) << 24;
/* 1005 */     int j15 = c[12] & 0xFF | (c[13] & 0xFF) << 8 | (c[14] & 0xFF) << 16 | (c[15] & 0xFF) << 24;
/*      */     
/* 1007 */     int x0 = j0, x1 = j1, x2 = j2, x3 = j3, x4 = j4, x5 = j5, x6 = j6, x7 = j7;
/* 1008 */     int x8 = j8, x9 = j9, x10 = j10, x11 = j11, x12 = j12, x13 = j13, x14 = j14;
/* 1009 */     int x15 = j15;
/*      */     
/* 1011 */     for (int i = 0; i < 20; i += 2) {
/* 1012 */       int u = x0 + x12 | 0x0;
/* 1013 */       x4 ^= u << 7 | u >>> 25;
/* 1014 */       u = x4 + x0 | 0x0;
/* 1015 */       x8 ^= u << 9 | u >>> 23;
/* 1016 */       u = x8 + x4 | 0x0;
/* 1017 */       x12 ^= u << 13 | u >>> 19;
/* 1018 */       u = x12 + x8 | 0x0;
/* 1019 */       x0 ^= u << 18 | u >>> 14;
/*      */       
/* 1021 */       u = x5 + x1 | 0x0;
/* 1022 */       x9 ^= u << 7 | u >>> 25;
/* 1023 */       u = x9 + x5 | 0x0;
/* 1024 */       x13 ^= u << 9 | u >>> 23;
/* 1025 */       u = x13 + x9 | 0x0;
/* 1026 */       x1 ^= u << 13 | u >>> 19;
/* 1027 */       u = x1 + x13 | 0x0;
/* 1028 */       x5 ^= u << 18 | u >>> 14;
/*      */       
/* 1030 */       u = x10 + x6 | 0x0;
/* 1031 */       x14 ^= u << 7 | u >>> 25;
/* 1032 */       u = x14 + x10 | 0x0;
/* 1033 */       x2 ^= u << 9 | u >>> 23;
/* 1034 */       u = x2 + x14 | 0x0;
/* 1035 */       x6 ^= u << 13 | u >>> 19;
/* 1036 */       u = x6 + x2 | 0x0;
/* 1037 */       x10 ^= u << 18 | u >>> 14;
/*      */       
/* 1039 */       u = x15 + x11 | 0x0;
/* 1040 */       x3 ^= u << 7 | u >>> 25;
/* 1041 */       u = x3 + x15 | 0x0;
/* 1042 */       x7 ^= u << 9 | u >>> 23;
/* 1043 */       u = x7 + x3 | 0x0;
/* 1044 */       x11 ^= u << 13 | u >>> 19;
/* 1045 */       u = x11 + x7 | 0x0;
/* 1046 */       x15 ^= u << 18 | u >>> 14;
/*      */       
/* 1048 */       u = x0 + x3 | 0x0;
/* 1049 */       x1 ^= u << 7 | u >>> 25;
/* 1050 */       u = x1 + x0 | 0x0;
/* 1051 */       x2 ^= u << 9 | u >>> 23;
/* 1052 */       u = x2 + x1 | 0x0;
/* 1053 */       x3 ^= u << 13 | u >>> 19;
/* 1054 */       u = x3 + x2 | 0x0;
/* 1055 */       x0 ^= u << 18 | u >>> 14;
/*      */       
/* 1057 */       u = x5 + x4 | 0x0;
/* 1058 */       x6 ^= u << 7 | u >>> 25;
/* 1059 */       u = x6 + x5 | 0x0;
/* 1060 */       x7 ^= u << 9 | u >>> 23;
/* 1061 */       u = x7 + x6 | 0x0;
/* 1062 */       x4 ^= u << 13 | u >>> 19;
/* 1063 */       u = x4 + x7 | 0x0;
/* 1064 */       x5 ^= u << 18 | u >>> 14;
/*      */       
/* 1066 */       u = x10 + x9 | 0x0;
/* 1067 */       x11 ^= u << 7 | u >>> 25;
/* 1068 */       u = x11 + x10 | 0x0;
/* 1069 */       x8 ^= u << 9 | u >>> 23;
/* 1070 */       u = x8 + x11 | 0x0;
/* 1071 */       x9 ^= u << 13 | u >>> 19;
/* 1072 */       u = x9 + x8 | 0x0;
/* 1073 */       x10 ^= u << 18 | u >>> 14;
/*      */       
/* 1075 */       u = x15 + x14 | 0x0;
/* 1076 */       x12 ^= u << 7 | u >>> 25;
/* 1077 */       u = x12 + x15 | 0x0;
/* 1078 */       x13 ^= u << 9 | u >>> 23;
/* 1079 */       u = x13 + x12 | 0x0;
/* 1080 */       x14 ^= u << 13 | u >>> 19;
/* 1081 */       u = x14 + x13 | 0x0;
/* 1082 */       x15 ^= u << 18 | u >>> 14;
/*      */     } 
/* 1084 */     x0 = x0 + j0 | 0x0;
/* 1085 */     x1 = x1 + j1 | 0x0;
/* 1086 */     x2 = x2 + j2 | 0x0;
/* 1087 */     x3 = x3 + j3 | 0x0;
/* 1088 */     x4 = x4 + j4 | 0x0;
/* 1089 */     x5 = x5 + j5 | 0x0;
/* 1090 */     x6 = x6 + j6 | 0x0;
/* 1091 */     x7 = x7 + j7 | 0x0;
/* 1092 */     x8 = x8 + j8 | 0x0;
/* 1093 */     x9 = x9 + j9 | 0x0;
/* 1094 */     x10 = x10 + j10 | 0x0;
/* 1095 */     x11 = x11 + j11 | 0x0;
/* 1096 */     x12 = x12 + j12 | 0x0;
/* 1097 */     x13 = x13 + j13 | 0x0;
/* 1098 */     x14 = x14 + j14 | 0x0;
/* 1099 */     x15 = x15 + j15 | 0x0;
/*      */     
/* 1101 */     o[0] = (byte)(x0 >>> 0 & 0xFF);
/* 1102 */     o[1] = (byte)(x0 >>> 8 & 0xFF);
/* 1103 */     o[2] = (byte)(x0 >>> 16 & 0xFF);
/* 1104 */     o[3] = (byte)(x0 >>> 24 & 0xFF);
/*      */     
/* 1106 */     o[4] = (byte)(x1 >>> 0 & 0xFF);
/* 1107 */     o[5] = (byte)(x1 >>> 8 & 0xFF);
/* 1108 */     o[6] = (byte)(x1 >>> 16 & 0xFF);
/* 1109 */     o[7] = (byte)(x1 >>> 24 & 0xFF);
/*      */     
/* 1111 */     o[8] = (byte)(x2 >>> 0 & 0xFF);
/* 1112 */     o[9] = (byte)(x2 >>> 8 & 0xFF);
/* 1113 */     o[10] = (byte)(x2 >>> 16 & 0xFF);
/* 1114 */     o[11] = (byte)(x2 >>> 24 & 0xFF);
/*      */     
/* 1116 */     o[12] = (byte)(x3 >>> 0 & 0xFF);
/* 1117 */     o[13] = (byte)(x3 >>> 8 & 0xFF);
/* 1118 */     o[14] = (byte)(x3 >>> 16 & 0xFF);
/* 1119 */     o[15] = (byte)(x3 >>> 24 & 0xFF);
/*      */     
/* 1121 */     o[16] = (byte)(x4 >>> 0 & 0xFF);
/* 1122 */     o[17] = (byte)(x4 >>> 8 & 0xFF);
/* 1123 */     o[18] = (byte)(x4 >>> 16 & 0xFF);
/* 1124 */     o[19] = (byte)(x4 >>> 24 & 0xFF);
/*      */     
/* 1126 */     o[20] = (byte)(x5 >>> 0 & 0xFF);
/* 1127 */     o[21] = (byte)(x5 >>> 8 & 0xFF);
/* 1128 */     o[22] = (byte)(x5 >>> 16 & 0xFF);
/* 1129 */     o[23] = (byte)(x5 >>> 24 & 0xFF);
/*      */     
/* 1131 */     o[24] = (byte)(x6 >>> 0 & 0xFF);
/* 1132 */     o[25] = (byte)(x6 >>> 8 & 0xFF);
/* 1133 */     o[26] = (byte)(x6 >>> 16 & 0xFF);
/* 1134 */     o[27] = (byte)(x6 >>> 24 & 0xFF);
/*      */     
/* 1136 */     o[28] = (byte)(x7 >>> 0 & 0xFF);
/* 1137 */     o[29] = (byte)(x7 >>> 8 & 0xFF);
/* 1138 */     o[30] = (byte)(x7 >>> 16 & 0xFF);
/* 1139 */     o[31] = (byte)(x7 >>> 24 & 0xFF);
/*      */     
/* 1141 */     o[32] = (byte)(x8 >>> 0 & 0xFF);
/* 1142 */     o[33] = (byte)(x8 >>> 8 & 0xFF);
/* 1143 */     o[34] = (byte)(x8 >>> 16 & 0xFF);
/* 1144 */     o[35] = (byte)(x8 >>> 24 & 0xFF);
/*      */     
/* 1146 */     o[36] = (byte)(x9 >>> 0 & 0xFF);
/* 1147 */     o[37] = (byte)(x9 >>> 8 & 0xFF);
/* 1148 */     o[38] = (byte)(x9 >>> 16 & 0xFF);
/* 1149 */     o[39] = (byte)(x9 >>> 24 & 0xFF);
/*      */     
/* 1151 */     o[40] = (byte)(x10 >>> 0 & 0xFF);
/* 1152 */     o[41] = (byte)(x10 >>> 8 & 0xFF);
/* 1153 */     o[42] = (byte)(x10 >>> 16 & 0xFF);
/* 1154 */     o[43] = (byte)(x10 >>> 24 & 0xFF);
/*      */     
/* 1156 */     o[44] = (byte)(x11 >>> 0 & 0xFF);
/* 1157 */     o[45] = (byte)(x11 >>> 8 & 0xFF);
/* 1158 */     o[46] = (byte)(x11 >>> 16 & 0xFF);
/* 1159 */     o[47] = (byte)(x11 >>> 24 & 0xFF);
/*      */     
/* 1161 */     o[48] = (byte)(x12 >>> 0 & 0xFF);
/* 1162 */     o[49] = (byte)(x12 >>> 8 & 0xFF);
/* 1163 */     o[50] = (byte)(x12 >>> 16 & 0xFF);
/* 1164 */     o[51] = (byte)(x12 >>> 24 & 0xFF);
/*      */     
/* 1166 */     o[52] = (byte)(x13 >>> 0 & 0xFF);
/* 1167 */     o[53] = (byte)(x13 >>> 8 & 0xFF);
/* 1168 */     o[54] = (byte)(x13 >>> 16 & 0xFF);
/* 1169 */     o[55] = (byte)(x13 >>> 24 & 0xFF);
/*      */     
/* 1171 */     o[56] = (byte)(x14 >>> 0 & 0xFF);
/* 1172 */     o[57] = (byte)(x14 >>> 8 & 0xFF);
/* 1173 */     o[58] = (byte)(x14 >>> 16 & 0xFF);
/* 1174 */     o[59] = (byte)(x14 >>> 24 & 0xFF);
/*      */     
/* 1176 */     o[60] = (byte)(x15 >>> 0 & 0xFF);
/* 1177 */     o[61] = (byte)(x15 >>> 8 & 0xFF);
/* 1178 */     o[62] = (byte)(x15 >>> 16 & 0xFF);
/* 1179 */     o[63] = (byte)(x15 >>> 24 & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void core_hsalsa20(byte[] o, byte[] p, byte[] k, byte[] c) {
/* 1188 */     int j0 = c[0] & 0xFF | (c[1] & 0xFF) << 8 | (c[2] & 0xFF) << 16 | (c[3] & 0xFF) << 24;
/* 1189 */     int j1 = k[0] & 0xFF | (k[1] & 0xFF) << 8 | (k[2] & 0xFF) << 16 | (k[3] & 0xFF) << 24;
/* 1190 */     int j2 = k[4] & 0xFF | (k[5] & 0xFF) << 8 | (k[6] & 0xFF) << 16 | (k[7] & 0xFF) << 24;
/* 1191 */     int j3 = k[8] & 0xFF | (k[9] & 0xFF) << 8 | (k[10] & 0xFF) << 16 | (k[11] & 0xFF) << 24;
/* 1192 */     int j4 = k[12] & 0xFF | (k[13] & 0xFF) << 8 | (k[14] & 0xFF) << 16 | (k[15] & 0xFF) << 24;
/* 1193 */     int j5 = c[4] & 0xFF | (c[5] & 0xFF) << 8 | (c[6] & 0xFF) << 16 | (c[7] & 0xFF) << 24;
/* 1194 */     int j6 = p[0] & 0xFF | (p[1] & 0xFF) << 8 | (p[2] & 0xFF) << 16 | (p[3] & 0xFF) << 24;
/* 1195 */     int j7 = p[4] & 0xFF | (p[5] & 0xFF) << 8 | (p[6] & 0xFF) << 16 | (p[7] & 0xFF) << 24;
/* 1196 */     int j8 = p[8] & 0xFF | (p[9] & 0xFF) << 8 | (p[10] & 0xFF) << 16 | (p[11] & 0xFF) << 24;
/* 1197 */     int j9 = p[12] & 0xFF | (p[13] & 0xFF) << 8 | (p[14] & 0xFF) << 16 | (p[15] & 0xFF) << 24;
/* 1198 */     int j10 = c[8] & 0xFF | (c[9] & 0xFF) << 8 | (c[10] & 0xFF) << 16 | (c[11] & 0xFF) << 24;
/* 1199 */     int j11 = k[16] & 0xFF | (k[17] & 0xFF) << 8 | (k[18] & 0xFF) << 16 | (k[19] & 0xFF) << 24;
/* 1200 */     int j12 = k[20] & 0xFF | (k[21] & 0xFF) << 8 | (k[22] & 0xFF) << 16 | (k[23] & 0xFF) << 24;
/* 1201 */     int j13 = k[24] & 0xFF | (k[25] & 0xFF) << 8 | (k[26] & 0xFF) << 16 | (k[27] & 0xFF) << 24;
/* 1202 */     int j14 = k[28] & 0xFF | (k[29] & 0xFF) << 8 | (k[30] & 0xFF) << 16 | (k[31] & 0xFF) << 24;
/* 1203 */     int j15 = c[12] & 0xFF | (c[13] & 0xFF) << 8 | (c[14] & 0xFF) << 16 | (c[15] & 0xFF) << 24;
/*      */     
/* 1205 */     int x0 = j0, x1 = j1, x2 = j2, x3 = j3, x4 = j4, x5 = j5, x6 = j6, x7 = j7;
/* 1206 */     int x8 = j8, x9 = j9, x10 = j10, x11 = j11, x12 = j12, x13 = j13, x14 = j14;
/* 1207 */     int x15 = j15;
/*      */     
/* 1209 */     for (int i = 0; i < 20; i += 2) {
/* 1210 */       int u = x0 + x12 | 0x0;
/* 1211 */       x4 ^= u << 7 | u >>> 25;
/* 1212 */       u = x4 + x0 | 0x0;
/* 1213 */       x8 ^= u << 9 | u >>> 23;
/* 1214 */       u = x8 + x4 | 0x0;
/* 1215 */       x12 ^= u << 13 | u >>> 19;
/* 1216 */       u = x12 + x8 | 0x0;
/* 1217 */       x0 ^= u << 18 | u >>> 14;
/*      */       
/* 1219 */       u = x5 + x1 | 0x0;
/* 1220 */       x9 ^= u << 7 | u >>> 25;
/* 1221 */       u = x9 + x5 | 0x0;
/* 1222 */       x13 ^= u << 9 | u >>> 23;
/* 1223 */       u = x13 + x9 | 0x0;
/* 1224 */       x1 ^= u << 13 | u >>> 19;
/* 1225 */       u = x1 + x13 | 0x0;
/* 1226 */       x5 ^= u << 18 | u >>> 14;
/*      */       
/* 1228 */       u = x10 + x6 | 0x0;
/* 1229 */       x14 ^= u << 7 | u >>> 25;
/* 1230 */       u = x14 + x10 | 0x0;
/* 1231 */       x2 ^= u << 9 | u >>> 23;
/* 1232 */       u = x2 + x14 | 0x0;
/* 1233 */       x6 ^= u << 13 | u >>> 19;
/* 1234 */       u = x6 + x2 | 0x0;
/* 1235 */       x10 ^= u << 18 | u >>> 14;
/*      */       
/* 1237 */       u = x15 + x11 | 0x0;
/* 1238 */       x3 ^= u << 7 | u >>> 25;
/* 1239 */       u = x3 + x15 | 0x0;
/* 1240 */       x7 ^= u << 9 | u >>> 23;
/* 1241 */       u = x7 + x3 | 0x0;
/* 1242 */       x11 ^= u << 13 | u >>> 19;
/* 1243 */       u = x11 + x7 | 0x0;
/* 1244 */       x15 ^= u << 18 | u >>> 14;
/*      */       
/* 1246 */       u = x0 + x3 | 0x0;
/* 1247 */       x1 ^= u << 7 | u >>> 25;
/* 1248 */       u = x1 + x0 | 0x0;
/* 1249 */       x2 ^= u << 9 | u >>> 23;
/* 1250 */       u = x2 + x1 | 0x0;
/* 1251 */       x3 ^= u << 13 | u >>> 19;
/* 1252 */       u = x3 + x2 | 0x0;
/* 1253 */       x0 ^= u << 18 | u >>> 14;
/*      */       
/* 1255 */       u = x5 + x4 | 0x0;
/* 1256 */       x6 ^= u << 7 | u >>> 25;
/* 1257 */       u = x6 + x5 | 0x0;
/* 1258 */       x7 ^= u << 9 | u >>> 23;
/* 1259 */       u = x7 + x6 | 0x0;
/* 1260 */       x4 ^= u << 13 | u >>> 19;
/* 1261 */       u = x4 + x7 | 0x0;
/* 1262 */       x5 ^= u << 18 | u >>> 14;
/*      */       
/* 1264 */       u = x10 + x9 | 0x0;
/* 1265 */       x11 ^= u << 7 | u >>> 25;
/* 1266 */       u = x11 + x10 | 0x0;
/* 1267 */       x8 ^= u << 9 | u >>> 23;
/* 1268 */       u = x8 + x11 | 0x0;
/* 1269 */       x9 ^= u << 13 | u >>> 19;
/* 1270 */       u = x9 + x8 | 0x0;
/* 1271 */       x10 ^= u << 18 | u >>> 14;
/*      */       
/* 1273 */       u = x15 + x14 | 0x0;
/* 1274 */       x12 ^= u << 7 | u >>> 25;
/* 1275 */       u = x12 + x15 | 0x0;
/* 1276 */       x13 ^= u << 9 | u >>> 23;
/* 1277 */       u = x13 + x12 | 0x0;
/* 1278 */       x14 ^= u << 13 | u >>> 19;
/* 1279 */       u = x14 + x13 | 0x0;
/* 1280 */       x15 ^= u << 18 | u >>> 14;
/*      */     } 
/*      */     
/* 1283 */     o[0] = (byte)(x0 >>> 0 & 0xFF);
/* 1284 */     o[1] = (byte)(x0 >>> 8 & 0xFF);
/* 1285 */     o[2] = (byte)(x0 >>> 16 & 0xFF);
/* 1286 */     o[3] = (byte)(x0 >>> 24 & 0xFF);
/*      */     
/* 1288 */     o[4] = (byte)(x5 >>> 0 & 0xFF);
/* 1289 */     o[5] = (byte)(x5 >>> 8 & 0xFF);
/* 1290 */     o[6] = (byte)(x5 >>> 16 & 0xFF);
/* 1291 */     o[7] = (byte)(x5 >>> 24 & 0xFF);
/*      */     
/* 1293 */     o[8] = (byte)(x10 >>> 0 & 0xFF);
/* 1294 */     o[9] = (byte)(x10 >>> 8 & 0xFF);
/* 1295 */     o[10] = (byte)(x10 >>> 16 & 0xFF);
/* 1296 */     o[11] = (byte)(x10 >>> 24 & 0xFF);
/*      */     
/* 1298 */     o[12] = (byte)(x15 >>> 0 & 0xFF);
/* 1299 */     o[13] = (byte)(x15 >>> 8 & 0xFF);
/* 1300 */     o[14] = (byte)(x15 >>> 16 & 0xFF);
/* 1301 */     o[15] = (byte)(x15 >>> 24 & 0xFF);
/*      */     
/* 1303 */     o[16] = (byte)(x6 >>> 0 & 0xFF);
/* 1304 */     o[17] = (byte)(x6 >>> 8 & 0xFF);
/* 1305 */     o[18] = (byte)(x6 >>> 16 & 0xFF);
/* 1306 */     o[19] = (byte)(x6 >>> 24 & 0xFF);
/*      */     
/* 1308 */     o[20] = (byte)(x7 >>> 0 & 0xFF);
/* 1309 */     o[21] = (byte)(x7 >>> 8 & 0xFF);
/* 1310 */     o[22] = (byte)(x7 >>> 16 & 0xFF);
/* 1311 */     o[23] = (byte)(x7 >>> 24 & 0xFF);
/*      */     
/* 1313 */     o[24] = (byte)(x8 >>> 0 & 0xFF);
/* 1314 */     o[25] = (byte)(x8 >>> 8 & 0xFF);
/* 1315 */     o[26] = (byte)(x8 >>> 16 & 0xFF);
/* 1316 */     o[27] = (byte)(x8 >>> 24 & 0xFF);
/*      */     
/* 1318 */     o[28] = (byte)(x9 >>> 0 & 0xFF);
/* 1319 */     o[29] = (byte)(x9 >>> 8 & 0xFF);
/* 1320 */     o[30] = (byte)(x9 >>> 16 & 0xFF);
/* 1321 */     o[31] = (byte)(x9 >>> 24 & 0xFF);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int crypto_core_salsa20(byte[] out, byte[] in, byte[] k, byte[] c) {
/* 1333 */     core_salsa20(out, in, k, c);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1339 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int crypto_core_hsalsa20(byte[] out, byte[] in, byte[] k, byte[] c) {
/* 1345 */     core_hsalsa20(out, in, k, c);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1351 */     return 0;
/*      */   }
/*      */ 
/*      */   
/* 1355 */   private static final byte[] sigma = new byte[] { 101, 120, 112, 97, 110, 100, 32, 51, 50, 45, 98, 121, 116, 101, 32, 107 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int crypto_stream_salsa20_xor(byte[] c, int cpos, byte[] m, int mpos, long b, byte[] n, byte[] k) {
/* 1367 */     byte[] z = new byte[16], x = new byte[64];
/*      */     int i;
/* 1369 */     for (i = 0; i < 16; ) { z[i] = 0; i++; }
/* 1370 */      for (i = 0; i < 8; ) { z[i] = n[i]; i++; }
/* 1371 */      while (b >= 64L) {
/* 1372 */       crypto_core_salsa20(x, z, k, sigma);
/* 1373 */       for (i = 0; i < 64; ) { c[cpos + i] = (byte)((m[mpos + i] ^ x[i]) & 0xFF); i++; }
/* 1374 */        int u = 1;
/* 1375 */       for (i = 8; i < 16; i++) {
/* 1376 */         u = u + (z[i] & 0xFF) | 0x0;
/* 1377 */         z[i] = (byte)(u & 0xFF);
/* 1378 */         u >>>= 8;
/*      */       } 
/* 1380 */       b -= 64L;
/* 1381 */       cpos += 64;
/* 1382 */       mpos += 64;
/*      */     } 
/* 1384 */     if (b > 0L) {
/* 1385 */       crypto_core_salsa20(x, z, k, sigma);
/* 1386 */       for (i = 0; i < b; ) { c[cpos + i] = (byte)((m[mpos + i] ^ x[i]) & 0xFF); i++; }
/*      */     
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1393 */     return 0;
/*      */   }
/*      */   
/*      */   public static int crypto_stream_salsa20(byte[] c, int cpos, long b, byte[] n, byte[] k) {
/* 1397 */     byte[] z = new byte[16], x = new byte[64];
/*      */     int i;
/* 1399 */     for (i = 0; i < 16; ) { z[i] = 0; i++; }
/* 1400 */      for (i = 0; i < 8; ) { z[i] = n[i]; i++; }
/* 1401 */      while (b >= 64L) {
/* 1402 */       crypto_core_salsa20(x, z, k, sigma);
/* 1403 */       for (i = 0; i < 64; ) { c[cpos + i] = x[i]; i++; }
/* 1404 */        int u = 1;
/* 1405 */       for (i = 8; i < 16; i++) {
/* 1406 */         u = u + (z[i] & 0xFF) | 0x0;
/* 1407 */         z[i] = (byte)(u & 0xFF);
/* 1408 */         u >>>= 8;
/*      */       } 
/* 1410 */       b -= 64L;
/* 1411 */       cpos += 64;
/*      */     } 
/* 1413 */     if (b > 0L) {
/* 1414 */       crypto_core_salsa20(x, z, k, sigma);
/* 1415 */       for (i = 0; i < b; ) { c[cpos + i] = x[i]; i++; }
/*      */     
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1422 */     return 0;
/*      */   }
/*      */   
/*      */   public static int crypto_stream(byte[] c, int cpos, long d, byte[] n, byte[] k) {
/* 1426 */     byte[] s = new byte[32];
/* 1427 */     crypto_core_hsalsa20(s, n, k, sigma);
/* 1428 */     byte[] sn = new byte[8];
/* 1429 */     for (int i = 0; i < 8; ) { sn[i] = n[i + 16]; i++; }
/* 1430 */      return crypto_stream_salsa20(c, cpos, d, sn, s);
/*      */   }
/*      */   
/*      */   public static int crypto_stream_xor(byte[] c, int cpos, byte[] m, int mpos, long d, byte[] n, byte[] k) {
/* 1434 */     byte[] s = new byte[32];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1445 */     crypto_core_hsalsa20(s, n, k, sigma);
/* 1446 */     byte[] sn = new byte[8];
/* 1447 */     for (int i = 0; i < 8; ) { sn[i] = n[i + 16]; i++; }
/* 1448 */      return crypto_stream_salsa20_xor(c, cpos, m, mpos, d, sn, s);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final class poly1305
/*      */   {
/* 1465 */     private byte[] buffer = new byte[16];
/* 1466 */     private int[] r = new int[10];
/* 1467 */     private int[] h = new int[10];
/* 1468 */     private int[] pad = new int[8];
/* 1469 */     private int leftover = 0;
/* 1470 */     private int fin = 0;
/*      */ 
/*      */     
/*      */     public poly1305(byte[] key) {
/* 1474 */       int t0 = key[0] & 0xFF | (key[1] & 0xFF) << 8; this.r[0] = t0 & 0x1FFF;
/* 1475 */       int t1 = key[2] & 0xFF | (key[3] & 0xFF) << 8; this.r[1] = (t0 >>> 13 | t1 << 3) & 0x1FFF;
/* 1476 */       int t2 = key[4] & 0xFF | (key[5] & 0xFF) << 8; this.r[2] = (t1 >>> 10 | t2 << 6) & 0x1F03;
/* 1477 */       int t3 = key[6] & 0xFF | (key[7] & 0xFF) << 8; this.r[3] = (t2 >>> 7 | t3 << 9) & 0x1FFF;
/* 1478 */       int t4 = key[8] & 0xFF | (key[9] & 0xFF) << 8; this.r[4] = (t3 >>> 4 | t4 << 12) & 0xFF;
/* 1479 */       this.r[5] = t4 >>> 1 & 0x1FFE;
/* 1480 */       int t5 = key[10] & 0xFF | (key[11] & 0xFF) << 8; this.r[6] = (t4 >>> 14 | t5 << 2) & 0x1FFF;
/* 1481 */       int t6 = key[12] & 0xFF | (key[13] & 0xFF) << 8; this.r[7] = (t5 >>> 11 | t6 << 5) & 0x1F81;
/* 1482 */       int t7 = key[14] & 0xFF | (key[15] & 0xFF) << 8; this.r[8] = (t6 >>> 8 | t7 << 8) & 0x1FFF;
/* 1483 */       this.r[9] = t7 >>> 5 & 0x7F;
/*      */       
/* 1485 */       this.pad[0] = key[16] & 0xFF | (key[17] & 0xFF) << 8;
/* 1486 */       this.pad[1] = key[18] & 0xFF | (key[19] & 0xFF) << 8;
/* 1487 */       this.pad[2] = key[20] & 0xFF | (key[21] & 0xFF) << 8;
/* 1488 */       this.pad[3] = key[22] & 0xFF | (key[23] & 0xFF) << 8;
/* 1489 */       this.pad[4] = key[24] & 0xFF | (key[25] & 0xFF) << 8;
/* 1490 */       this.pad[5] = key[26] & 0xFF | (key[27] & 0xFF) << 8;
/* 1491 */       this.pad[6] = key[28] & 0xFF | (key[29] & 0xFF) << 8;
/* 1492 */       this.pad[7] = key[30] & 0xFF | (key[31] & 0xFF) << 8;
/*      */     }
/*      */     
/*      */     public poly1305 blocks(byte[] m, int mpos, int bytes) {
/* 1496 */       int hibit = (this.fin != 0) ? 0 : 2048;
/*      */ 
/*      */ 
/*      */       
/* 1500 */       int h0 = this.h[0];
/* 1501 */       int h1 = this.h[1];
/* 1502 */       int h2 = this.h[2];
/* 1503 */       int h3 = this.h[3];
/* 1504 */       int h4 = this.h[4];
/* 1505 */       int h5 = this.h[5];
/* 1506 */       int h6 = this.h[6];
/* 1507 */       int h7 = this.h[7];
/* 1508 */       int h8 = this.h[8];
/* 1509 */       int h9 = this.h[9];
/*      */       
/* 1511 */       int r0 = this.r[0];
/* 1512 */       int r1 = this.r[1];
/* 1513 */       int r2 = this.r[2];
/* 1514 */       int r3 = this.r[3];
/* 1515 */       int r4 = this.r[4];
/* 1516 */       int r5 = this.r[5];
/* 1517 */       int r6 = this.r[6];
/* 1518 */       int r7 = this.r[7];
/* 1519 */       int r8 = this.r[8];
/* 1520 */       int r9 = this.r[9];
/*      */       
/* 1522 */       while (bytes >= 16) {
/* 1523 */         int t0 = m[mpos + 0] & 0xFF | (m[mpos + 1] & 0xFF) << 8; h0 += t0 & 0x1FFF;
/* 1524 */         int t1 = m[mpos + 2] & 0xFF | (m[mpos + 3] & 0xFF) << 8; h1 += (t0 >>> 13 | t1 << 3) & 0x1FFF;
/* 1525 */         int t2 = m[mpos + 4] & 0xFF | (m[mpos + 5] & 0xFF) << 8; h2 += (t1 >>> 10 | t2 << 6) & 0x1FFF;
/* 1526 */         int t3 = m[mpos + 6] & 0xFF | (m[mpos + 7] & 0xFF) << 8; h3 += (t2 >>> 7 | t3 << 9) & 0x1FFF;
/* 1527 */         int t4 = m[mpos + 8] & 0xFF | (m[mpos + 9] & 0xFF) << 8; h4 += (t3 >>> 4 | t4 << 12) & 0x1FFF;
/* 1528 */         h5 += t4 >>> 1 & 0x1FFF;
/* 1529 */         int t5 = m[mpos + 10] & 0xFF | (m[mpos + 11] & 0xFF) << 8; h6 += (t4 >>> 14 | t5 << 2) & 0x1FFF;
/* 1530 */         int t6 = m[mpos + 12] & 0xFF | (m[mpos + 13] & 0xFF) << 8; h7 += (t5 >>> 11 | t6 << 5) & 0x1FFF;
/* 1531 */         int t7 = m[mpos + 14] & 0xFF | (m[mpos + 15] & 0xFF) << 8; h8 += (t6 >>> 8 | t7 << 8) & 0x1FFF;
/* 1532 */         h9 += t7 >>> 5 | hibit;
/*      */         
/* 1534 */         int c = 0;
/*      */         
/* 1536 */         int d0 = c;
/* 1537 */         d0 += h0 * r0;
/* 1538 */         d0 += h1 * 5 * r9;
/* 1539 */         d0 += h2 * 5 * r8;
/* 1540 */         d0 += h3 * 5 * r7;
/* 1541 */         d0 += h4 * 5 * r6;
/* 1542 */         c = d0 >>> 13; d0 &= 0x1FFF;
/* 1543 */         d0 += h5 * 5 * r5;
/* 1544 */         d0 += h6 * 5 * r4;
/* 1545 */         d0 += h7 * 5 * r3;
/* 1546 */         d0 += h8 * 5 * r2;
/* 1547 */         d0 += h9 * 5 * r1;
/* 1548 */         c += d0 >>> 13; d0 &= 0x1FFF;
/*      */         
/* 1550 */         int d1 = c;
/* 1551 */         d1 += h0 * r1;
/* 1552 */         d1 += h1 * r0;
/* 1553 */         d1 += h2 * 5 * r9;
/* 1554 */         d1 += h3 * 5 * r8;
/* 1555 */         d1 += h4 * 5 * r7;
/* 1556 */         c = d1 >>> 13; d1 &= 0x1FFF;
/* 1557 */         d1 += h5 * 5 * r6;
/* 1558 */         d1 += h6 * 5 * r5;
/* 1559 */         d1 += h7 * 5 * r4;
/* 1560 */         d1 += h8 * 5 * r3;
/* 1561 */         d1 += h9 * 5 * r2;
/* 1562 */         c += d1 >>> 13; d1 &= 0x1FFF;
/*      */         
/* 1564 */         int d2 = c;
/* 1565 */         d2 += h0 * r2;
/* 1566 */         d2 += h1 * r1;
/* 1567 */         d2 += h2 * r0;
/* 1568 */         d2 += h3 * 5 * r9;
/* 1569 */         d2 += h4 * 5 * r8;
/* 1570 */         c = d2 >>> 13; d2 &= 0x1FFF;
/* 1571 */         d2 += h5 * 5 * r7;
/* 1572 */         d2 += h6 * 5 * r6;
/* 1573 */         d2 += h7 * 5 * r5;
/* 1574 */         d2 += h8 * 5 * r4;
/* 1575 */         d2 += h9 * 5 * r3;
/* 1576 */         c += d2 >>> 13; d2 &= 0x1FFF;
/*      */         
/* 1578 */         int d3 = c;
/* 1579 */         d3 += h0 * r3;
/* 1580 */         d3 += h1 * r2;
/* 1581 */         d3 += h2 * r1;
/* 1582 */         d3 += h3 * r0;
/* 1583 */         d3 += h4 * 5 * r9;
/* 1584 */         c = d3 >>> 13; d3 &= 0x1FFF;
/* 1585 */         d3 += h5 * 5 * r8;
/* 1586 */         d3 += h6 * 5 * r7;
/* 1587 */         d3 += h7 * 5 * r6;
/* 1588 */         d3 += h8 * 5 * r5;
/* 1589 */         d3 += h9 * 5 * r4;
/* 1590 */         c += d3 >>> 13; d3 &= 0x1FFF;
/*      */         
/* 1592 */         int d4 = c;
/* 1593 */         d4 += h0 * r4;
/* 1594 */         d4 += h1 * r3;
/* 1595 */         d4 += h2 * r2;
/* 1596 */         d4 += h3 * r1;
/* 1597 */         d4 += h4 * r0;
/* 1598 */         c = d4 >>> 13; d4 &= 0x1FFF;
/* 1599 */         d4 += h5 * 5 * r9;
/* 1600 */         d4 += h6 * 5 * r8;
/* 1601 */         d4 += h7 * 5 * r7;
/* 1602 */         d4 += h8 * 5 * r6;
/* 1603 */         d4 += h9 * 5 * r5;
/* 1604 */         c += d4 >>> 13; d4 &= 0x1FFF;
/*      */         
/* 1606 */         int d5 = c;
/* 1607 */         d5 += h0 * r5;
/* 1608 */         d5 += h1 * r4;
/* 1609 */         d5 += h2 * r3;
/* 1610 */         d5 += h3 * r2;
/* 1611 */         d5 += h4 * r1;
/* 1612 */         c = d5 >>> 13; d5 &= 0x1FFF;
/* 1613 */         d5 += h5 * r0;
/* 1614 */         d5 += h6 * 5 * r9;
/* 1615 */         d5 += h7 * 5 * r8;
/* 1616 */         d5 += h8 * 5 * r7;
/* 1617 */         d5 += h9 * 5 * r6;
/* 1618 */         c += d5 >>> 13; d5 &= 0x1FFF;
/*      */         
/* 1620 */         int d6 = c;
/* 1621 */         d6 += h0 * r6;
/* 1622 */         d6 += h1 * r5;
/* 1623 */         d6 += h2 * r4;
/* 1624 */         d6 += h3 * r3;
/* 1625 */         d6 += h4 * r2;
/* 1626 */         c = d6 >>> 13; d6 &= 0x1FFF;
/* 1627 */         d6 += h5 * r1;
/* 1628 */         d6 += h6 * r0;
/* 1629 */         d6 += h7 * 5 * r9;
/* 1630 */         d6 += h8 * 5 * r8;
/* 1631 */         d6 += h9 * 5 * r7;
/* 1632 */         c += d6 >>> 13; d6 &= 0x1FFF;
/*      */         
/* 1634 */         int d7 = c;
/* 1635 */         d7 += h0 * r7;
/* 1636 */         d7 += h1 * r6;
/* 1637 */         d7 += h2 * r5;
/* 1638 */         d7 += h3 * r4;
/* 1639 */         d7 += h4 * r3;
/* 1640 */         c = d7 >>> 13; d7 &= 0x1FFF;
/* 1641 */         d7 += h5 * r2;
/* 1642 */         d7 += h6 * r1;
/* 1643 */         d7 += h7 * r0;
/* 1644 */         d7 += h8 * 5 * r9;
/* 1645 */         d7 += h9 * 5 * r8;
/* 1646 */         c += d7 >>> 13; d7 &= 0x1FFF;
/*      */         
/* 1648 */         int d8 = c;
/* 1649 */         d8 += h0 * r8;
/* 1650 */         d8 += h1 * r7;
/* 1651 */         d8 += h2 * r6;
/* 1652 */         d8 += h3 * r5;
/* 1653 */         d8 += h4 * r4;
/* 1654 */         c = d8 >>> 13; d8 &= 0x1FFF;
/* 1655 */         d8 += h5 * r3;
/* 1656 */         d8 += h6 * r2;
/* 1657 */         d8 += h7 * r1;
/* 1658 */         d8 += h8 * r0;
/* 1659 */         d8 += h9 * 5 * r9;
/* 1660 */         c += d8 >>> 13; d8 &= 0x1FFF;
/*      */         
/* 1662 */         int d9 = c;
/* 1663 */         d9 += h0 * r9;
/* 1664 */         d9 += h1 * r8;
/* 1665 */         d9 += h2 * r7;
/* 1666 */         d9 += h3 * r6;
/* 1667 */         d9 += h4 * r5;
/* 1668 */         c = d9 >>> 13; d9 &= 0x1FFF;
/* 1669 */         d9 += h5 * r4;
/* 1670 */         d9 += h6 * r3;
/* 1671 */         d9 += h7 * r2;
/* 1672 */         d9 += h8 * r1;
/* 1673 */         d9 += h9 * r0;
/* 1674 */         c += d9 >>> 13; d9 &= 0x1FFF;
/*      */         
/* 1676 */         c = (c << 2) + c | 0x0;
/* 1677 */         c = c + d0 | 0x0;
/* 1678 */         d0 = c & 0x1FFF;
/* 1679 */         c >>>= 13;
/* 1680 */         d1 += c;
/*      */         
/* 1682 */         h0 = d0;
/* 1683 */         h1 = d1;
/* 1684 */         h2 = d2;
/* 1685 */         h3 = d3;
/* 1686 */         h4 = d4;
/* 1687 */         h5 = d5;
/* 1688 */         h6 = d6;
/* 1689 */         h7 = d7;
/* 1690 */         h8 = d8;
/* 1691 */         h9 = d9;
/*      */         
/* 1693 */         mpos += 16;
/* 1694 */         bytes -= 16;
/*      */       } 
/* 1696 */       this.h[0] = h0;
/* 1697 */       this.h[1] = h1;
/* 1698 */       this.h[2] = h2;
/* 1699 */       this.h[3] = h3;
/* 1700 */       this.h[4] = h4;
/* 1701 */       this.h[5] = h5;
/* 1702 */       this.h[6] = h6;
/* 1703 */       this.h[7] = h7;
/* 1704 */       this.h[8] = h8;
/* 1705 */       this.h[9] = h9;
/*      */       
/* 1707 */       return this;
/*      */     }
/*      */     
/*      */     public poly1305 finish(byte[] mac, int macpos) {
/* 1711 */       int[] g = new int[10];
/*      */ 
/*      */       
/* 1714 */       if (this.leftover != 0) {
/* 1715 */         int j = this.leftover;
/* 1716 */         this.buffer[j++] = 1;
/* 1717 */         while (j < 16) { this.buffer[j] = 0; j++; }
/* 1718 */          this.fin = 1;
/* 1719 */         blocks(this.buffer, 0, 16);
/*      */       } 
/*      */       
/* 1722 */       int c = this.h[1] >>> 13;
/* 1723 */       this.h[1] = this.h[1] & 0x1FFF; int i;
/* 1724 */       for (i = 2; i < 10; i++) {
/* 1725 */         this.h[i] = this.h[i] + c;
/* 1726 */         c = this.h[i] >>> 13;
/* 1727 */         this.h[i] = this.h[i] & 0x1FFF;
/*      */       } 
/* 1729 */       this.h[0] = this.h[0] + c * 5;
/* 1730 */       c = this.h[0] >>> 13;
/* 1731 */       this.h[0] = this.h[0] & 0x1FFF;
/* 1732 */       this.h[1] = this.h[1] + c;
/* 1733 */       c = this.h[1] >>> 13;
/* 1734 */       this.h[1] = this.h[1] & 0x1FFF;
/* 1735 */       this.h[2] = this.h[2] + c;
/*      */       
/* 1737 */       g[0] = this.h[0] + 5;
/* 1738 */       c = g[0] >>> 13;
/* 1739 */       g[0] = g[0] & 0x1FFF;
/* 1740 */       for (i = 1; i < 10; i++) {
/* 1741 */         g[i] = this.h[i] + c;
/* 1742 */         c = g[i] >>> 13;
/* 1743 */         g[i] = g[i] & 0x1FFF;
/*      */       } 
/* 1745 */       g[9] = g[9] - 8192; g[9] = g[9] & 0xFFFF;
/*      */       
/* 1747 */       int mask = (g[9] >>> 15) - 1; mask &= 0xFFFF;
/* 1748 */       for (i = 0; i < 10; ) { g[i] = g[i] & mask; i++; }
/* 1749 */        mask ^= 0xFFFFFFFF;
/* 1750 */       for (i = 0; i < 10; ) { this.h[i] = this.h[i] & mask | g[i]; i++; }
/*      */       
/* 1752 */       this.h[0] = (this.h[0] | this.h[1] << 13) & 0xFFFF;
/* 1753 */       this.h[1] = (this.h[1] >>> 3 | this.h[2] << 10) & 0xFFFF;
/* 1754 */       this.h[2] = (this.h[2] >>> 6 | this.h[3] << 7) & 0xFFFF;
/* 1755 */       this.h[3] = (this.h[3] >>> 9 | this.h[4] << 4) & 0xFFFF;
/* 1756 */       this.h[4] = (this.h[4] >>> 12 | this.h[5] << 1 | this.h[6] << 14) & 0xFFFF;
/* 1757 */       this.h[5] = (this.h[6] >>> 2 | this.h[7] << 11) & 0xFFFF;
/* 1758 */       this.h[6] = (this.h[7] >>> 5 | this.h[8] << 8) & 0xFFFF;
/* 1759 */       this.h[7] = (this.h[8] >>> 8 | this.h[9] << 5) & 0xFFFF;
/*      */       
/* 1761 */       int f = this.h[0] + this.pad[0];
/* 1762 */       this.h[0] = f & 0xFFFF;
/* 1763 */       for (i = 1; i < 8; i++) {
/* 1764 */         f = (this.h[i] + this.pad[i] | 0x0) + (f >>> 16) | 0x0;
/* 1765 */         this.h[i] = f & 0xFFFF;
/*      */       } 
/*      */       
/* 1768 */       mac[macpos + 0] = (byte)(this.h[0] >>> 0 & 0xFF);
/* 1769 */       mac[macpos + 1] = (byte)(this.h[0] >>> 8 & 0xFF);
/* 1770 */       mac[macpos + 2] = (byte)(this.h[1] >>> 0 & 0xFF);
/* 1771 */       mac[macpos + 3] = (byte)(this.h[1] >>> 8 & 0xFF);
/* 1772 */       mac[macpos + 4] = (byte)(this.h[2] >>> 0 & 0xFF);
/* 1773 */       mac[macpos + 5] = (byte)(this.h[2] >>> 8 & 0xFF);
/* 1774 */       mac[macpos + 6] = (byte)(this.h[3] >>> 0 & 0xFF);
/* 1775 */       mac[macpos + 7] = (byte)(this.h[3] >>> 8 & 0xFF);
/* 1776 */       mac[macpos + 8] = (byte)(this.h[4] >>> 0 & 0xFF);
/* 1777 */       mac[macpos + 9] = (byte)(this.h[4] >>> 8 & 0xFF);
/* 1778 */       mac[macpos + 10] = (byte)(this.h[5] >>> 0 & 0xFF);
/* 1779 */       mac[macpos + 11] = (byte)(this.h[5] >>> 8 & 0xFF);
/* 1780 */       mac[macpos + 12] = (byte)(this.h[6] >>> 0 & 0xFF);
/* 1781 */       mac[macpos + 13] = (byte)(this.h[6] >>> 8 & 0xFF);
/* 1782 */       mac[macpos + 14] = (byte)(this.h[7] >>> 0 & 0xFF);
/* 1783 */       mac[macpos + 15] = (byte)(this.h[7] >>> 8 & 0xFF);
/*      */       
/* 1785 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public poly1305 update(byte[] m, int mpos, int bytes) {
/* 1791 */       if (this.leftover != 0) {
/* 1792 */         int want = 16 - this.leftover;
/* 1793 */         if (want > bytes)
/* 1794 */           want = bytes; 
/* 1795 */         for (int i = 0; i < want; i++)
/* 1796 */           this.buffer[this.leftover + i] = m[mpos + i]; 
/* 1797 */         bytes -= want;
/* 1798 */         mpos += want;
/* 1799 */         this.leftover += want;
/* 1800 */         if (this.leftover < 16)
/* 1801 */           return this; 
/* 1802 */         blocks(this.buffer, 0, 16);
/* 1803 */         this.leftover = 0;
/*      */       } 
/*      */       
/* 1806 */       if (bytes >= 16) {
/* 1807 */         int want = bytes - bytes % 16;
/* 1808 */         blocks(m, mpos, want);
/* 1809 */         mpos += want;
/* 1810 */         bytes -= want;
/*      */       } 
/*      */       
/* 1813 */       if (bytes != 0) {
/* 1814 */         for (int i = 0; i < bytes; i++)
/* 1815 */           this.buffer[this.leftover + i] = m[mpos + i]; 
/* 1816 */         this.leftover += bytes;
/*      */       } 
/*      */       
/* 1819 */       return this;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int crypto_onetimeauth(byte[] out, int outpos, byte[] m, int mpos, int n, byte[] k) {
/* 1830 */     poly1305 s = new poly1305(k);
/* 1831 */     s.update(m, mpos, n);
/* 1832 */     s.finish(out, outpos);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1839 */     return 0;
/*      */   }
/*      */   public static int crypto_onetimeauth(byte[] out, byte[] m, int n, byte[] k) {
/* 1842 */     return crypto_onetimeauth(out, 0, m, 0, n, k);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int crypto_onetimeauth_verify(byte[] h, int hoff, byte[] m, int moff, int n, byte[] k) {
/* 1851 */     byte[] x = new byte[16];
/* 1852 */     crypto_onetimeauth(x, 0, m, moff, n, k);
/* 1853 */     return crypto_verify_16(h, hoff, x, 0);
/*      */   }
/*      */   public static int crypto_onetimeauth_verify(byte[] h, byte[] m, int n, byte[] k) {
/* 1856 */     return crypto_onetimeauth_verify(h, 0, m, 0, n, k);
/*      */   }
/*      */   public static int crypto_onetimeauth_verify(byte[] h, byte[] m, byte[] k) {
/* 1859 */     return crypto_onetimeauth_verify(h, m, (m != null) ? m.length : 0, k);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int crypto_secretbox(byte[] c, byte[] m, int d, byte[] n, byte[] k) {
/* 1865 */     if (d < 32) return -1; 
/* 1866 */     crypto_stream_xor(c, 0, m, 0, d, n, k);
/* 1867 */     crypto_onetimeauth(c, 16, c, 32, d - 32, c);
/*      */     
/* 1869 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static int crypto_secretbox_open(byte[] m, byte[] c, int d, byte[] n, byte[] k) {
/* 1875 */     byte[] x = new byte[32];
/* 1876 */     if (d < 32) return -1; 
/* 1877 */     crypto_stream(x, 0, 32L, n, k);
/* 1878 */     if (crypto_onetimeauth_verify(c, 16, c, 32, d - 32, x) != 0) return -1; 
/* 1879 */     crypto_stream_xor(m, 0, c, 0, d, n, k);
/*      */     
/* 1881 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void set25519(long[] r, long[] a) {
/* 1887 */     for (int i = 0; i < 16; ) { r[i] = a[i]; i++; }
/*      */   
/*      */   }
/*      */ 
/*      */   
/*      */   private static void car25519(long[] o) {
/* 1893 */     long c = 1L;
/* 1894 */     for (int i = 0; i < 16; i++) {
/* 1895 */       long v = o[i] + c + 65535L;
/* 1896 */       c = v >> 16L;
/* 1897 */       o[i] = v - c * 65536L;
/*      */     } 
/* 1899 */     o[0] = o[0] + c - 1L + 37L * (c - 1L);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void sel25519(long[] p, long[] q, int b) {
/* 1907 */     sel25519(p, 0, q, 0, b);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void sel25519(long[] p, int poff, long[] q, int qoff, int b) {
/* 1914 */     long c = (b - 1 ^ 0xFFFFFFFF);
/* 1915 */     for (int i = 0; i < 16; i++) {
/* 1916 */       long t = c & (p[i + poff] ^ q[i + qoff]);
/* 1917 */       p[i + poff] = p[i + poff] ^ t;
/* 1918 */       q[i + qoff] = q[i + qoff] ^ t;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void pack25519(byte[] o, long[] n, int noff) {
/* 1925 */     long[] m = new long[16], t = new long[16]; int i;
/* 1926 */     for (i = 0; i < 16; ) { t[i] = n[i + noff]; i++; }
/* 1927 */      car25519(t);
/* 1928 */     car25519(t);
/* 1929 */     car25519(t);
/* 1930 */     for (int j = 0; j < 2; j++) {
/* 1931 */       m[0] = t[0] - 65517L;
/* 1932 */       for (i = 1; i < 15; i++) {
/* 1933 */         m[i] = t[i] - 65535L - (m[i - 1] >> 16L & 0x1L);
/* 1934 */         m[i - 1] = m[i - 1] & 0xFFFFL;
/*      */       } 
/* 1936 */       m[15] = t[15] - 32767L - (m[14] >> 16L & 0x1L);
/* 1937 */       int b = (int)(m[15] >> 16L & 0x1L);
/* 1938 */       m[14] = m[14] & 0xFFFFL;
/* 1939 */       sel25519(t, 0, m, 0, 1 - b);
/*      */     } 
/* 1941 */     for (i = 0; i < 16; i++) {
/* 1942 */       o[2 * i] = (byte)(int)(t[i] & 0xFFL);
/* 1943 */       o[2 * i + 1] = (byte)(int)(t[i] >> 8L);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static int neq25519(long[] a, long[] b) {
/* 1948 */     return neq25519(a, 0, b, 0);
/*      */   }
/*      */   
/*      */   private static int neq25519(long[] a, int aoff, long[] b, int boff) {
/* 1952 */     byte[] c = new byte[32], d = new byte[32];
/* 1953 */     pack25519(c, a, aoff);
/* 1954 */     pack25519(d, b, boff);
/* 1955 */     return crypto_verify_32(c, 0, d, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private static byte par25519(long[] a) {
/* 1960 */     return par25519(a, 0);
/*      */   }
/*      */   
/*      */   private static byte par25519(long[] a, int aoff) {
/* 1964 */     byte[] d = new byte[32];
/* 1965 */     pack25519(d, a, aoff);
/* 1966 */     return (byte)(d[0] & 0x1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void unpack25519(long[] o, byte[] n) {
/* 1972 */     for (int i = 0; i < 16; ) { o[i] = (n[2 * i] & 0xFF) + (n[2 * i + 1] << 8 & 0xFFFF); i++; }
/* 1973 */      o[15] = o[15] & 0x7FFFL;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void A(long[] o, long[] a, long[] b) {
/* 1981 */     A(o, 0, a, 0, b, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void A(long[] o, int ooff, long[] a, int aoff, long[] b, int boff) {
/* 1989 */     for (int i = 0; i < 16; ) { o[i + ooff] = a[i + aoff] + b[i + boff]; i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void Z(long[] o, long[] a, long[] b) {
/* 1997 */     Z(o, 0, a, 0, b, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void Z(long[] o, int ooff, long[] a, int aoff, long[] b, int boff) {
/* 2005 */     for (int i = 0; i < 16; ) { o[i + ooff] = a[i + aoff] - b[i + boff]; i++; }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void M(long[] o, long[] a, long[] b) {
/* 2013 */     M(o, 0, a, 0, b, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void M(long[] o, int ooff, long[] a, int aoff, long[] b, int boff) {
/* 2021 */     long t0 = 0L, t1 = 0L, t2 = 0L, t3 = 0L, t4 = 0L, t5 = 0L, t6 = 0L, t7 = 0L;
/* 2022 */     long t8 = 0L, t9 = 0L, t10 = 0L, t11 = 0L, t12 = 0L, t13 = 0L, t14 = 0L, t15 = 0L;
/* 2023 */     long t16 = 0L, t17 = 0L, t18 = 0L, t19 = 0L, t20 = 0L, t21 = 0L, t22 = 0L, t23 = 0L;
/* 2024 */     long t24 = 0L, t25 = 0L, t26 = 0L, t27 = 0L, t28 = 0L, t29 = 0L, t30 = 0L;
/* 2025 */     long b0 = b[0 + boff];
/* 2026 */     long b1 = b[1 + boff];
/* 2027 */     long b2 = b[2 + boff];
/* 2028 */     long b3 = b[3 + boff];
/* 2029 */     long b4 = b[4 + boff];
/* 2030 */     long b5 = b[5 + boff];
/* 2031 */     long b6 = b[6 + boff];
/* 2032 */     long b7 = b[7 + boff];
/* 2033 */     long b8 = b[8 + boff];
/* 2034 */     long b9 = b[9 + boff];
/* 2035 */     long b10 = b[10 + boff];
/* 2036 */     long b11 = b[11 + boff];
/* 2037 */     long b12 = b[12 + boff];
/* 2038 */     long b13 = b[13 + boff];
/* 2039 */     long b14 = b[14 + boff];
/* 2040 */     long b15 = b[15 + boff];
/*      */     
/* 2042 */     long v = a[0 + aoff];
/* 2043 */     t0 += v * b0;
/* 2044 */     t1 += v * b1;
/* 2045 */     t2 += v * b2;
/* 2046 */     t3 += v * b3;
/* 2047 */     t4 += v * b4;
/* 2048 */     t5 += v * b5;
/* 2049 */     t6 += v * b6;
/* 2050 */     t7 += v * b7;
/* 2051 */     t8 += v * b8;
/* 2052 */     t9 += v * b9;
/* 2053 */     t10 += v * b10;
/* 2054 */     t11 += v * b11;
/* 2055 */     t12 += v * b12;
/* 2056 */     t13 += v * b13;
/* 2057 */     t14 += v * b14;
/* 2058 */     t15 += v * b15;
/* 2059 */     v = a[1 + aoff];
/* 2060 */     t1 += v * b0;
/* 2061 */     t2 += v * b1;
/* 2062 */     t3 += v * b2;
/* 2063 */     t4 += v * b3;
/* 2064 */     t5 += v * b4;
/* 2065 */     t6 += v * b5;
/* 2066 */     t7 += v * b6;
/* 2067 */     t8 += v * b7;
/* 2068 */     t9 += v * b8;
/* 2069 */     t10 += v * b9;
/* 2070 */     t11 += v * b10;
/* 2071 */     t12 += v * b11;
/* 2072 */     t13 += v * b12;
/* 2073 */     t14 += v * b13;
/* 2074 */     t15 += v * b14;
/* 2075 */     t16 += v * b15;
/* 2076 */     v = a[2 + aoff];
/* 2077 */     t2 += v * b0;
/* 2078 */     t3 += v * b1;
/* 2079 */     t4 += v * b2;
/* 2080 */     t5 += v * b3;
/* 2081 */     t6 += v * b4;
/* 2082 */     t7 += v * b5;
/* 2083 */     t8 += v * b6;
/* 2084 */     t9 += v * b7;
/* 2085 */     t10 += v * b8;
/* 2086 */     t11 += v * b9;
/* 2087 */     t12 += v * b10;
/* 2088 */     t13 += v * b11;
/* 2089 */     t14 += v * b12;
/* 2090 */     t15 += v * b13;
/* 2091 */     t16 += v * b14;
/* 2092 */     t17 += v * b15;
/* 2093 */     v = a[3 + aoff];
/* 2094 */     t3 += v * b0;
/* 2095 */     t4 += v * b1;
/* 2096 */     t5 += v * b2;
/* 2097 */     t6 += v * b3;
/* 2098 */     t7 += v * b4;
/* 2099 */     t8 += v * b5;
/* 2100 */     t9 += v * b6;
/* 2101 */     t10 += v * b7;
/* 2102 */     t11 += v * b8;
/* 2103 */     t12 += v * b9;
/* 2104 */     t13 += v * b10;
/* 2105 */     t14 += v * b11;
/* 2106 */     t15 += v * b12;
/* 2107 */     t16 += v * b13;
/* 2108 */     t17 += v * b14;
/* 2109 */     t18 += v * b15;
/* 2110 */     v = a[4 + aoff];
/* 2111 */     t4 += v * b0;
/* 2112 */     t5 += v * b1;
/* 2113 */     t6 += v * b2;
/* 2114 */     t7 += v * b3;
/* 2115 */     t8 += v * b4;
/* 2116 */     t9 += v * b5;
/* 2117 */     t10 += v * b6;
/* 2118 */     t11 += v * b7;
/* 2119 */     t12 += v * b8;
/* 2120 */     t13 += v * b9;
/* 2121 */     t14 += v * b10;
/* 2122 */     t15 += v * b11;
/* 2123 */     t16 += v * b12;
/* 2124 */     t17 += v * b13;
/* 2125 */     t18 += v * b14;
/* 2126 */     t19 += v * b15;
/* 2127 */     v = a[5 + aoff];
/* 2128 */     t5 += v * b0;
/* 2129 */     t6 += v * b1;
/* 2130 */     t7 += v * b2;
/* 2131 */     t8 += v * b3;
/* 2132 */     t9 += v * b4;
/* 2133 */     t10 += v * b5;
/* 2134 */     t11 += v * b6;
/* 2135 */     t12 += v * b7;
/* 2136 */     t13 += v * b8;
/* 2137 */     t14 += v * b9;
/* 2138 */     t15 += v * b10;
/* 2139 */     t16 += v * b11;
/* 2140 */     t17 += v * b12;
/* 2141 */     t18 += v * b13;
/* 2142 */     t19 += v * b14;
/* 2143 */     t20 += v * b15;
/* 2144 */     v = a[6 + aoff];
/* 2145 */     t6 += v * b0;
/* 2146 */     t7 += v * b1;
/* 2147 */     t8 += v * b2;
/* 2148 */     t9 += v * b3;
/* 2149 */     t10 += v * b4;
/* 2150 */     t11 += v * b5;
/* 2151 */     t12 += v * b6;
/* 2152 */     t13 += v * b7;
/* 2153 */     t14 += v * b8;
/* 2154 */     t15 += v * b9;
/* 2155 */     t16 += v * b10;
/* 2156 */     t17 += v * b11;
/* 2157 */     t18 += v * b12;
/* 2158 */     t19 += v * b13;
/* 2159 */     t20 += v * b14;
/* 2160 */     t21 += v * b15;
/* 2161 */     v = a[7 + aoff];
/* 2162 */     t7 += v * b0;
/* 2163 */     t8 += v * b1;
/* 2164 */     t9 += v * b2;
/* 2165 */     t10 += v * b3;
/* 2166 */     t11 += v * b4;
/* 2167 */     t12 += v * b5;
/* 2168 */     t13 += v * b6;
/* 2169 */     t14 += v * b7;
/* 2170 */     t15 += v * b8;
/* 2171 */     t16 += v * b9;
/* 2172 */     t17 += v * b10;
/* 2173 */     t18 += v * b11;
/* 2174 */     t19 += v * b12;
/* 2175 */     t20 += v * b13;
/* 2176 */     t21 += v * b14;
/* 2177 */     t22 += v * b15;
/* 2178 */     v = a[8 + aoff];
/* 2179 */     t8 += v * b0;
/* 2180 */     t9 += v * b1;
/* 2181 */     t10 += v * b2;
/* 2182 */     t11 += v * b3;
/* 2183 */     t12 += v * b4;
/* 2184 */     t13 += v * b5;
/* 2185 */     t14 += v * b6;
/* 2186 */     t15 += v * b7;
/* 2187 */     t16 += v * b8;
/* 2188 */     t17 += v * b9;
/* 2189 */     t18 += v * b10;
/* 2190 */     t19 += v * b11;
/* 2191 */     t20 += v * b12;
/* 2192 */     t21 += v * b13;
/* 2193 */     t22 += v * b14;
/* 2194 */     t23 += v * b15;
/* 2195 */     v = a[9 + aoff];
/* 2196 */     t9 += v * b0;
/* 2197 */     t10 += v * b1;
/* 2198 */     t11 += v * b2;
/* 2199 */     t12 += v * b3;
/* 2200 */     t13 += v * b4;
/* 2201 */     t14 += v * b5;
/* 2202 */     t15 += v * b6;
/* 2203 */     t16 += v * b7;
/* 2204 */     t17 += v * b8;
/* 2205 */     t18 += v * b9;
/* 2206 */     t19 += v * b10;
/* 2207 */     t20 += v * b11;
/* 2208 */     t21 += v * b12;
/* 2209 */     t22 += v * b13;
/* 2210 */     t23 += v * b14;
/* 2211 */     t24 += v * b15;
/* 2212 */     v = a[10 + aoff];
/* 2213 */     t10 += v * b0;
/* 2214 */     t11 += v * b1;
/* 2215 */     t12 += v * b2;
/* 2216 */     t13 += v * b3;
/* 2217 */     t14 += v * b4;
/* 2218 */     t15 += v * b5;
/* 2219 */     t16 += v * b6;
/* 2220 */     t17 += v * b7;
/* 2221 */     t18 += v * b8;
/* 2222 */     t19 += v * b9;
/* 2223 */     t20 += v * b10;
/* 2224 */     t21 += v * b11;
/* 2225 */     t22 += v * b12;
/* 2226 */     t23 += v * b13;
/* 2227 */     t24 += v * b14;
/* 2228 */     t25 += v * b15;
/* 2229 */     v = a[11 + aoff];
/* 2230 */     t11 += v * b0;
/* 2231 */     t12 += v * b1;
/* 2232 */     t13 += v * b2;
/* 2233 */     t14 += v * b3;
/* 2234 */     t15 += v * b4;
/* 2235 */     t16 += v * b5;
/* 2236 */     t17 += v * b6;
/* 2237 */     t18 += v * b7;
/* 2238 */     t19 += v * b8;
/* 2239 */     t20 += v * b9;
/* 2240 */     t21 += v * b10;
/* 2241 */     t22 += v * b11;
/* 2242 */     t23 += v * b12;
/* 2243 */     t24 += v * b13;
/* 2244 */     t25 += v * b14;
/* 2245 */     t26 += v * b15;
/* 2246 */     v = a[12 + aoff];
/* 2247 */     t12 += v * b0;
/* 2248 */     t13 += v * b1;
/* 2249 */     t14 += v * b2;
/* 2250 */     t15 += v * b3;
/* 2251 */     t16 += v * b4;
/* 2252 */     t17 += v * b5;
/* 2253 */     t18 += v * b6;
/* 2254 */     t19 += v * b7;
/* 2255 */     t20 += v * b8;
/* 2256 */     t21 += v * b9;
/* 2257 */     t22 += v * b10;
/* 2258 */     t23 += v * b11;
/* 2259 */     t24 += v * b12;
/* 2260 */     t25 += v * b13;
/* 2261 */     t26 += v * b14;
/* 2262 */     t27 += v * b15;
/* 2263 */     v = a[13 + aoff];
/* 2264 */     t13 += v * b0;
/* 2265 */     t14 += v * b1;
/* 2266 */     t15 += v * b2;
/* 2267 */     t16 += v * b3;
/* 2268 */     t17 += v * b4;
/* 2269 */     t18 += v * b5;
/* 2270 */     t19 += v * b6;
/* 2271 */     t20 += v * b7;
/* 2272 */     t21 += v * b8;
/* 2273 */     t22 += v * b9;
/* 2274 */     t23 += v * b10;
/* 2275 */     t24 += v * b11;
/* 2276 */     t25 += v * b12;
/* 2277 */     t26 += v * b13;
/* 2278 */     t27 += v * b14;
/* 2279 */     t28 += v * b15;
/* 2280 */     v = a[14 + aoff];
/* 2281 */     t14 += v * b0;
/* 2282 */     t15 += v * b1;
/* 2283 */     t16 += v * b2;
/* 2284 */     t17 += v * b3;
/* 2285 */     t18 += v * b4;
/* 2286 */     t19 += v * b5;
/* 2287 */     t20 += v * b6;
/* 2288 */     t21 += v * b7;
/* 2289 */     t22 += v * b8;
/* 2290 */     t23 += v * b9;
/* 2291 */     t24 += v * b10;
/* 2292 */     t25 += v * b11;
/* 2293 */     t26 += v * b12;
/* 2294 */     t27 += v * b13;
/* 2295 */     t28 += v * b14;
/* 2296 */     t29 += v * b15;
/* 2297 */     v = a[15 + aoff];
/* 2298 */     t15 += v * b0;
/* 2299 */     t16 += v * b1;
/* 2300 */     t17 += v * b2;
/* 2301 */     t18 += v * b3;
/* 2302 */     t19 += v * b4;
/* 2303 */     t20 += v * b5;
/* 2304 */     t21 += v * b6;
/* 2305 */     t22 += v * b7;
/* 2306 */     t23 += v * b8;
/* 2307 */     t24 += v * b9;
/* 2308 */     t25 += v * b10;
/* 2309 */     t26 += v * b11;
/* 2310 */     t27 += v * b12;
/* 2311 */     t28 += v * b13;
/* 2312 */     t29 += v * b14;
/* 2313 */     t30 += v * b15;
/*      */     
/* 2315 */     t0 += 38L * t16;
/* 2316 */     t1 += 38L * t17;
/* 2317 */     t2 += 38L * t18;
/* 2318 */     t3 += 38L * t19;
/* 2319 */     t4 += 38L * t20;
/* 2320 */     t5 += 38L * t21;
/* 2321 */     t6 += 38L * t22;
/* 2322 */     t7 += 38L * t23;
/* 2323 */     t8 += 38L * t24;
/* 2324 */     t9 += 38L * t25;
/* 2325 */     t10 += 38L * t26;
/* 2326 */     t11 += 38L * t27;
/* 2327 */     t12 += 38L * t28;
/* 2328 */     t13 += 38L * t29;
/* 2329 */     t14 += 38L * t30;
/*      */ 
/*      */ 
/*      */     
/* 2333 */     long c = 1L;
/* 2334 */     v = t0 + c + 65535L; c = v >> 16L; t0 = v - c * 65536L;
/* 2335 */     v = t1 + c + 65535L; c = v >> 16L; t1 = v - c * 65536L;
/* 2336 */     v = t2 + c + 65535L; c = v >> 16L; t2 = v - c * 65536L;
/* 2337 */     v = t3 + c + 65535L; c = v >> 16L; t3 = v - c * 65536L;
/* 2338 */     v = t4 + c + 65535L; c = v >> 16L; t4 = v - c * 65536L;
/* 2339 */     v = t5 + c + 65535L; c = v >> 16L; t5 = v - c * 65536L;
/* 2340 */     v = t6 + c + 65535L; c = v >> 16L; t6 = v - c * 65536L;
/* 2341 */     v = t7 + c + 65535L; c = v >> 16L; t7 = v - c * 65536L;
/* 2342 */     v = t8 + c + 65535L; c = v >> 16L; t8 = v - c * 65536L;
/* 2343 */     v = t9 + c + 65535L; c = v >> 16L; t9 = v - c * 65536L;
/* 2344 */     v = t10 + c + 65535L; c = v >> 16L; t10 = v - c * 65536L;
/* 2345 */     v = t11 + c + 65535L; c = v >> 16L; t11 = v - c * 65536L;
/* 2346 */     v = t12 + c + 65535L; c = v >> 16L; t12 = v - c * 65536L;
/* 2347 */     v = t13 + c + 65535L; c = v >> 16L; t13 = v - c * 65536L;
/* 2348 */     v = t14 + c + 65535L; c = v >> 16L; t14 = v - c * 65536L;
/* 2349 */     v = t15 + c + 65535L; c = v >> 16L; t15 = v - c * 65536L;
/* 2350 */     t0 += c - 1L + 37L * (c - 1L);
/*      */ 
/*      */     
/* 2353 */     c = 1L;
/* 2354 */     v = t0 + c + 65535L; c = v >> 16L; t0 = v - c * 65536L;
/* 2355 */     v = t1 + c + 65535L; c = v >> 16L; t1 = v - c * 65536L;
/* 2356 */     v = t2 + c + 65535L; c = v >> 16L; t2 = v - c * 65536L;
/* 2357 */     v = t3 + c + 65535L; c = v >> 16L; t3 = v - c * 65536L;
/* 2358 */     v = t4 + c + 65535L; c = v >> 16L; t4 = v - c * 65536L;
/* 2359 */     v = t5 + c + 65535L; c = v >> 16L; t5 = v - c * 65536L;
/* 2360 */     v = t6 + c + 65535L; c = v >> 16L; t6 = v - c * 65536L;
/* 2361 */     v = t7 + c + 65535L; c = v >> 16L; t7 = v - c * 65536L;
/* 2362 */     v = t8 + c + 65535L; c = v >> 16L; t8 = v - c * 65536L;
/* 2363 */     v = t9 + c + 65535L; c = v >> 16L; t9 = v - c * 65536L;
/* 2364 */     v = t10 + c + 65535L; c = v >> 16L; t10 = v - c * 65536L;
/* 2365 */     v = t11 + c + 65535L; c = v >> 16L; t11 = v - c * 65536L;
/* 2366 */     v = t12 + c + 65535L; c = v >> 16L; t12 = v - c * 65536L;
/* 2367 */     v = t13 + c + 65535L; c = v >> 16L; t13 = v - c * 65536L;
/* 2368 */     v = t14 + c + 65535L; c = v >> 16L; t14 = v - c * 65536L;
/* 2369 */     v = t15 + c + 65535L; c = v >> 16L; t15 = v - c * 65536L;
/* 2370 */     t0 += c - 1L + 37L * (c - 1L);
/*      */     
/* 2372 */     o[0 + ooff] = t0;
/* 2373 */     o[1 + ooff] = t1;
/* 2374 */     o[2 + ooff] = t2;
/* 2375 */     o[3 + ooff] = t3;
/* 2376 */     o[4 + ooff] = t4;
/* 2377 */     o[5 + ooff] = t5;
/* 2378 */     o[6 + ooff] = t6;
/* 2379 */     o[7 + ooff] = t7;
/* 2380 */     o[8 + ooff] = t8;
/* 2381 */     o[9 + ooff] = t9;
/* 2382 */     o[10 + ooff] = t10;
/* 2383 */     o[11 + ooff] = t11;
/* 2384 */     o[12 + ooff] = t12;
/* 2385 */     o[13 + ooff] = t13;
/* 2386 */     o[14 + ooff] = t14;
/* 2387 */     o[15 + ooff] = t15;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void S(long[] o, long[] a) {
/* 2394 */     S(o, 0, a, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static void S(long[] o, int ooff, long[] a, int aoff) {
/* 2400 */     M(o, ooff, a, aoff, a, aoff);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void inv25519(long[] o, int ooff, long[] i, int ioff) {
/* 2407 */     long[] c = new long[16];
/*      */     int a;
/* 2409 */     for (a = 0; a < 16; ) { c[a] = i[a + ioff]; a++; }
/* 2410 */      for (a = 253; a >= 0; a--) {
/* 2411 */       S(c, 0, c, 0);
/* 2412 */       if (a != 2 && a != 4) M(c, 0, c, 0, i, ioff); 
/*      */     } 
/* 2414 */     for (a = 0; a < 16; ) { o[a + ooff] = c[a]; a++; }
/*      */   
/*      */   }
/*      */   
/*      */   private static void pow2523(long[] o, long[] i) {
/* 2419 */     long[] c = new long[16];
/*      */     
/*      */     int a;
/* 2422 */     for (a = 0; a < 16; ) { c[a] = i[a]; a++; }
/*      */     
/* 2424 */     for (a = 250; a >= 0; a--) {
/* 2425 */       S(c, 0, c, 0);
/* 2426 */       if (a != 1) M(c, 0, c, 0, i, 0);
/*      */     
/*      */     } 
/* 2429 */     for (a = 0; a < 16; ) { o[a] = c[a]; a++; }
/*      */   
/*      */   }
/*      */   
/*      */   public static int crypto_scalarmult(byte[] q, byte[] n, byte[] p) {
/* 2434 */     byte[] z = new byte[32];
/* 2435 */     long[] x = new long[80];
/*      */     
/* 2437 */     long[] a = new long[16], b = new long[16], c = new long[16];
/* 2438 */     long[] d = new long[16], e = new long[16], f = new long[16]; int i;
/* 2439 */     for (i = 0; i < 31; ) { z[i] = n[i]; i++; }
/* 2440 */      z[31] = (byte)((n[31] & Byte.MAX_VALUE | 0x40) & 0xFF);
/* 2441 */     z[0] = (byte)(z[0] & 0xF8);
/* 2442 */     unpack25519(x, p);
/* 2443 */     for (i = 0; i < 16; i++) {
/* 2444 */       b[i] = x[i];
/* 2445 */       c[i] = 0L; a[i] = 0L; d[i] = 0L;
/*      */     } 
/* 2447 */     d[0] = 1L; a[0] = 1L;
/* 2448 */     for (i = 254; i >= 0; i--) {
/* 2449 */       int r = z[i >>> 3] >>> (i & 0x7) & 0x1;
/* 2450 */       sel25519(a, b, r);
/* 2451 */       sel25519(c, d, r);
/* 2452 */       A(e, a, c);
/* 2453 */       Z(a, a, c);
/* 2454 */       A(c, b, d);
/* 2455 */       Z(b, b, d);
/* 2456 */       S(d, e);
/* 2457 */       S(f, a);
/* 2458 */       M(a, c, a);
/* 2459 */       M(c, b, e);
/* 2460 */       A(e, a, c);
/* 2461 */       Z(a, a, c);
/* 2462 */       S(b, a);
/* 2463 */       Z(c, d, f);
/* 2464 */       M(a, c, _121665);
/* 2465 */       A(a, a, d);
/* 2466 */       M(c, c, a);
/* 2467 */       M(a, d, f);
/* 2468 */       M(d, b, x);
/* 2469 */       S(b, e);
/* 2470 */       sel25519(a, b, r);
/* 2471 */       sel25519(c, d, r);
/*      */     } 
/* 2473 */     for (i = 0; i < 16; i++) {
/* 2474 */       x[i + 16] = a[i];
/* 2475 */       x[i + 32] = c[i];
/* 2476 */       x[i + 48] = b[i];
/* 2477 */       x[i + 64] = d[i];
/*      */     } 
/* 2479 */     inv25519(x, 32, x, 32);
/* 2480 */     M(x, 16, x, 16, x, 32);
/* 2481 */     pack25519(q, x, 16);
/*      */     
/* 2483 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public static int crypto_scalarmult_base(byte[] q, byte[] n) {
/* 2488 */     return crypto_scalarmult(q, n, _9);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int crypto_box_keypair(byte[] y, byte[] x) {
/* 2493 */     randombytes(x, 32);
/* 2494 */     return crypto_scalarmult_base(y, x);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int crypto_box_beforenm(byte[] k, byte[] y, byte[] x) {
/* 2499 */     byte[] s = new byte[32];
/* 2500 */     crypto_scalarmult(s, x, y);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2514 */     return crypto_core_hsalsa20(k, _0, s, sigma);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int crypto_box_afternm(byte[] c, byte[] m, int d, byte[] n, byte[] k) {
/* 2519 */     return crypto_secretbox(c, m, d, n, k);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int crypto_box_open_afternm(byte[] m, byte[] c, int d, byte[] n, byte[] k) {
/* 2524 */     return crypto_secretbox_open(m, c, d, n, k);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int crypto_box(byte[] c, byte[] m, int d, byte[] n, byte[] y, byte[] x) {
/* 2529 */     byte[] k = new byte[32];
/*      */ 
/*      */ 
/*      */     
/* 2533 */     crypto_box_beforenm(k, y, x);
/* 2534 */     return crypto_box_afternm(c, m, d, n, k);
/*      */   }
/*      */ 
/*      */   
/*      */   public static int crypto_box_open(byte[] m, byte[] c, int d, byte[] n, byte[] y, byte[] x) {
/* 2539 */     byte[] k = new byte[32];
/* 2540 */     crypto_box_beforenm(k, y, x);
/* 2541 */     return crypto_box_open_afternm(m, c, d, n, k);
/*      */   }
/*      */   
/* 2544 */   private static final long[] K = new long[] { 4794697086780616226L, 8158064640168781261L, -5349999486874862801L, -1606136188198331460L, 4131703408338449720L, 6480981068601479193L, -7908458776815382629L, -6116909921290321640L, -2880145864133508542L, 1334009975649890238L, 2608012711638119052L, 6128411473006802146L, 8268148722764581231L, -9160688886553864527L, -7215885187991268811L, -4495734319001033068L, -1973867731355612462L, -1171420211273849373L, 1135362057144423861L, 2597628984639134821L, 3308224258029322869L, 5365058923640841347L, 6679025012923562964L, 8573033837759648693L, -7476448914759557205L, -6327057829258317296L, -5763719355590565569L, -4658551843659510044L, -4116276920077217854L, -3051310485924567259L, 489312712824947311L, 1452737877330783856L, 2861767655752347644L, 3322285676063803686L, 5560940570517711597L, 5996557281743188959L, 7280758554555802590L, 8532644243296465576L, -9096487096722542874L, -7894198246740708037L, -6719396339535248540L, -6333637450476146687L, -4446306890439682159L, -4076793802049405392L, -3345356375505022440L, -2983346525034927856L, -860691631967231958L, 1182934255886127544L, 1847814050463011016L, 2177327727835720531L, 2830643537854262169L, 3796741975233480872L, 4115178125766777443L, 5681478168544905931L, 6601373596472566643L, 7507060721942968483L, 8399075790359081724L, 8693463985226723168L, -8878714635349349518L, -8302665154208450068L, -8016688836872298968L, -6606660893046293015L, -4685533653050689259L, -4147400797238176981L, -3880063495543823972L, -3348786107499101689L, -1523767162380948706L, -757361751448694408L, 500013540394364858L, 748580250866718886L, 1242879168328830382L, 1977374033974150939L, 2944078676154940804L, 3659926193048069267L, 4368137639120453308L, 4836135668995329356L, 5532061633213252278L, 6448918945643986474L, 6902733635092675308L, 7801388544844847127L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int crypto_hashblocks_hl(int[] hh, int[] hl, byte[] m, int moff, int n) {
/* 2573 */     int[] wh = new int[16], wl = new int[16];
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2578 */     int ah0 = hh[0];
/* 2579 */     int ah1 = hh[1];
/* 2580 */     int ah2 = hh[2];
/* 2581 */     int ah3 = hh[3];
/* 2582 */     int ah4 = hh[4];
/* 2583 */     int ah5 = hh[5];
/* 2584 */     int ah6 = hh[6];
/* 2585 */     int ah7 = hh[7];
/*      */     
/* 2587 */     int al0 = hl[0];
/* 2588 */     int al1 = hl[1];
/* 2589 */     int al2 = hl[2];
/* 2590 */     int al3 = hl[3];
/* 2591 */     int al4 = hl[4];
/* 2592 */     int al5 = hl[5];
/* 2593 */     int al6 = hl[6];
/* 2594 */     int al7 = hl[7];
/*      */     
/* 2596 */     int pos = 0;
/* 2597 */     while (n >= 128) {
/* 2598 */       int i; for (i = 0; i < 16; i++) {
/* 2599 */         int j = 8 * i + pos;
/* 2600 */         wh[i] = (m[j + 0 + moff] & 0xFF) << 24 | (m[j + 1 + moff] & 0xFF) << 16 | (m[j + 2 + moff] & 0xFF) << 8 | (m[j + 3 + moff] & 0xFF) << 0;
/* 2601 */         wl[i] = (m[j + 4 + moff] & 0xFF) << 24 | (m[j + 5 + moff] & 0xFF) << 16 | (m[j + 6 + moff] & 0xFF) << 8 | (m[j + 7 + moff] & 0xFF) << 0;
/*      */       } 
/* 2603 */       for (i = 0; i < 80; i++) {
/* 2604 */         int bh0 = ah0;
/* 2605 */         int bh1 = ah1;
/* 2606 */         int bh2 = ah2;
/* 2607 */         int bh3 = ah3;
/* 2608 */         int bh4 = ah4;
/* 2609 */         int bh5 = ah5;
/* 2610 */         int bh6 = ah6;
/* 2611 */         int bh7 = ah7;
/*      */         
/* 2613 */         int bl0 = al0;
/* 2614 */         int bl1 = al1;
/* 2615 */         int bl2 = al2;
/* 2616 */         int bl3 = al3;
/* 2617 */         int bl4 = al4;
/* 2618 */         int bl5 = al5;
/* 2619 */         int bl6 = al6;
/* 2620 */         int bl7 = al7;
/*      */ 
/*      */         
/* 2623 */         int j = ah7;
/* 2624 */         int k = al7;
/*      */         
/* 2626 */         int i1 = k & 0xFFFF, i2 = k >>> 16;
/* 2627 */         int i3 = j & 0xFFFF, i4 = j >>> 16;
/*      */ 
/*      */         
/* 2630 */         j = (ah4 >>> 14 | al4 << 18) ^ (ah4 >>> 18 | al4 << 14) ^ (al4 >>> 9 | ah4 << 23);
/* 2631 */         k = (al4 >>> 14 | ah4 << 18) ^ (al4 >>> 18 | ah4 << 14) ^ (ah4 >>> 9 | al4 << 23);
/*      */         
/* 2633 */         i1 += k & 0xFFFF; i2 += k >>> 16;
/* 2634 */         i3 += j & 0xFFFF; i4 += j >>> 16;
/*      */ 
/*      */         
/* 2637 */         j = ah4 & ah5 ^ (ah4 ^ 0xFFFFFFFF) & ah6;
/* 2638 */         k = al4 & al5 ^ (al4 ^ 0xFFFFFFFF) & al6;
/*      */         
/* 2640 */         i1 += k & 0xFFFF; i2 += k >>> 16;
/* 2641 */         i3 += j & 0xFFFF; i4 += j >>> 16;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2646 */         j = (int)(K[i] >>> 32L & 0xFFFFFFFFFFFFFFFFL);
/* 2647 */         k = (int)(K[i] >>> 0L & 0xFFFFFFFFFFFFFFFFL);
/*      */ 
/*      */ 
/*      */         
/* 2651 */         i1 += k & 0xFFFF; i2 += k >>> 16;
/* 2652 */         i3 += j & 0xFFFF; i4 += j >>> 16;
/*      */ 
/*      */         
/* 2655 */         j = wh[i % 16];
/* 2656 */         k = wl[i % 16];
/*      */         
/* 2658 */         i1 += k & 0xFFFF; i2 += k >>> 16;
/* 2659 */         i3 += j & 0xFFFF; i4 += j >>> 16;
/*      */         
/* 2661 */         i2 += i1 >>> 16;
/* 2662 */         i3 += i2 >>> 16;
/* 2663 */         i4 += i3 >>> 16;
/*      */         
/* 2665 */         int th = i3 & 0xFFFF | i4 << 16;
/* 2666 */         int tl = i1 & 0xFFFF | i2 << 16;
/*      */ 
/*      */         
/* 2669 */         j = th;
/* 2670 */         k = tl;
/*      */         
/* 2672 */         i1 = k & 0xFFFF; i2 = k >>> 16;
/* 2673 */         i3 = j & 0xFFFF; i4 = j >>> 16;
/*      */ 
/*      */         
/* 2676 */         j = (ah0 >>> 28 | al0 << 4) ^ (al0 >>> 2 | ah0 << 30) ^ (al0 >>> 7 | ah0 << 25);
/* 2677 */         k = (al0 >>> 28 | ah0 << 4) ^ (ah0 >>> 2 | al0 << 30) ^ (ah0 >>> 7 | al0 << 25);
/*      */         
/* 2679 */         i1 += k & 0xFFFF; i2 += k >>> 16;
/* 2680 */         i3 += j & 0xFFFF; i4 += j >>> 16;
/*      */ 
/*      */         
/* 2683 */         j = ah0 & ah1 ^ ah0 & ah2 ^ ah1 & ah2;
/* 2684 */         k = al0 & al1 ^ al0 & al2 ^ al1 & al2;
/*      */         
/* 2686 */         i1 += k & 0xFFFF; i2 += k >>> 16;
/* 2687 */         i3 += j & 0xFFFF; i4 += j >>> 16;
/*      */         
/* 2689 */         i2 += i1 >>> 16;
/* 2690 */         i3 += i2 >>> 16;
/* 2691 */         i4 += i3 >>> 16;
/*      */         
/* 2693 */         bh7 = i3 & 0xFFFF | i4 << 16;
/* 2694 */         bl7 = i1 & 0xFFFF | i2 << 16;
/*      */ 
/*      */         
/* 2697 */         j = bh3;
/* 2698 */         k = bl3;
/*      */         
/* 2700 */         i1 = k & 0xFFFF; i2 = k >>> 16;
/* 2701 */         i3 = j & 0xFFFF; i4 = j >>> 16;
/*      */         
/* 2703 */         j = th;
/* 2704 */         k = tl;
/*      */         
/* 2706 */         i1 += k & 0xFFFF; i2 += k >>> 16;
/* 2707 */         i3 += j & 0xFFFF; i4 += j >>> 16;
/*      */         
/* 2709 */         i2 += i1 >>> 16;
/* 2710 */         i3 += i2 >>> 16;
/* 2711 */         i4 += i3 >>> 16;
/*      */         
/* 2713 */         bh3 = i3 & 0xFFFF | i4 << 16;
/* 2714 */         bl3 = i1 & 0xFFFF | i2 << 16;
/*      */         
/* 2716 */         ah1 = bh0;
/* 2717 */         ah2 = bh1;
/* 2718 */         ah3 = bh2;
/* 2719 */         ah4 = bh3;
/* 2720 */         ah5 = bh4;
/* 2721 */         ah6 = bh5;
/* 2722 */         ah7 = bh6;
/* 2723 */         ah0 = bh7;
/*      */         
/* 2725 */         al1 = bl0;
/* 2726 */         al2 = bl1;
/* 2727 */         al3 = bl2;
/* 2728 */         al4 = bl3;
/* 2729 */         al5 = bl4;
/* 2730 */         al6 = bl5;
/* 2731 */         al7 = bl6;
/* 2732 */         al0 = bl7;
/*      */         
/* 2734 */         if (i % 16 == 15) {
/* 2735 */           for (int i5 = 0; i5 < 16; i5++) {
/*      */             
/* 2737 */             j = wh[i5];
/* 2738 */             k = wl[i5];
/*      */             
/* 2740 */             i1 = k & 0xFFFF; i2 = k >>> 16;
/* 2741 */             i3 = j & 0xFFFF; i4 = j >>> 16;
/*      */             
/* 2743 */             j = wh[(i5 + 9) % 16];
/* 2744 */             k = wl[(i5 + 9) % 16];
/*      */             
/* 2746 */             i1 += k & 0xFFFF; i2 += k >>> 16;
/* 2747 */             i3 += j & 0xFFFF; i4 += j >>> 16;
/*      */ 
/*      */             
/* 2750 */             th = wh[(i5 + 1) % 16];
/* 2751 */             tl = wl[(i5 + 1) % 16];
/* 2752 */             j = (th >>> 1 | tl << 31) ^ (th >>> 8 | tl << 24) ^ th >>> 7;
/* 2753 */             k = (tl >>> 1 | th << 31) ^ (tl >>> 8 | th << 24) ^ (tl >>> 7 | th << 25);
/*      */             
/* 2755 */             i1 += k & 0xFFFF; i2 += k >>> 16;
/* 2756 */             i3 += j & 0xFFFF; i4 += j >>> 16;
/*      */ 
/*      */             
/* 2759 */             th = wh[(i5 + 14) % 16];
/* 2760 */             tl = wl[(i5 + 14) % 16];
/* 2761 */             j = (th >>> 19 | tl << 13) ^ (tl >>> 29 | th << 3) ^ th >>> 6;
/* 2762 */             k = (tl >>> 19 | th << 13) ^ (th >>> 29 | tl << 3) ^ (tl >>> 6 | th << 26);
/*      */             
/* 2764 */             i1 += k & 0xFFFF; i2 += k >>> 16;
/* 2765 */             i3 += j & 0xFFFF; i4 += j >>> 16;
/*      */             
/* 2767 */             i2 += i1 >>> 16;
/* 2768 */             i3 += i2 >>> 16;
/* 2769 */             i4 += i3 >>> 16;
/*      */             
/* 2771 */             wh[i5] = i3 & 0xFFFF | i4 << 16;
/* 2772 */             wl[i5] = i1 & 0xFFFF | i2 << 16;
/*      */           } 
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 2778 */       int h = ah0;
/* 2779 */       int l = al0;
/*      */       
/* 2781 */       int a = l & 0xFFFF, b = l >>> 16;
/* 2782 */       int c = h & 0xFFFF, d = h >>> 16;
/*      */       
/* 2784 */       h = hh[0];
/* 2785 */       l = hl[0];
/*      */       
/* 2787 */       a += l & 0xFFFF; b += l >>> 16;
/* 2788 */       c += h & 0xFFFF; d += h >>> 16;
/*      */       
/* 2790 */       b += a >>> 16;
/* 2791 */       c += b >>> 16;
/* 2792 */       d += c >>> 16;
/*      */       
/* 2794 */       hh[0] = ah0 = c & 0xFFFF | d << 16;
/* 2795 */       hl[0] = al0 = a & 0xFFFF | b << 16;
/*      */       
/* 2797 */       h = ah1;
/* 2798 */       l = al1;
/*      */       
/* 2800 */       a = l & 0xFFFF; b = l >>> 16;
/* 2801 */       c = h & 0xFFFF; d = h >>> 16;
/*      */       
/* 2803 */       h = hh[1];
/* 2804 */       l = hl[1];
/*      */       
/* 2806 */       a += l & 0xFFFF; b += l >>> 16;
/* 2807 */       c += h & 0xFFFF; d += h >>> 16;
/*      */       
/* 2809 */       b += a >>> 16;
/* 2810 */       c += b >>> 16;
/* 2811 */       d += c >>> 16;
/*      */       
/* 2813 */       hh[1] = ah1 = c & 0xFFFF | d << 16;
/* 2814 */       hl[1] = al1 = a & 0xFFFF | b << 16;
/*      */       
/* 2816 */       h = ah2;
/* 2817 */       l = al2;
/*      */       
/* 2819 */       a = l & 0xFFFF; b = l >>> 16;
/* 2820 */       c = h & 0xFFFF; d = h >>> 16;
/*      */       
/* 2822 */       h = hh[2];
/* 2823 */       l = hl[2];
/*      */       
/* 2825 */       a += l & 0xFFFF; b += l >>> 16;
/* 2826 */       c += h & 0xFFFF; d += h >>> 16;
/*      */       
/* 2828 */       b += a >>> 16;
/* 2829 */       c += b >>> 16;
/* 2830 */       d += c >>> 16;
/*      */       
/* 2832 */       hh[2] = ah2 = c & 0xFFFF | d << 16;
/* 2833 */       hl[2] = al2 = a & 0xFFFF | b << 16;
/*      */       
/* 2835 */       h = ah3;
/* 2836 */       l = al3;
/*      */       
/* 2838 */       a = l & 0xFFFF; b = l >>> 16;
/* 2839 */       c = h & 0xFFFF; d = h >>> 16;
/*      */       
/* 2841 */       h = hh[3];
/* 2842 */       l = hl[3];
/*      */       
/* 2844 */       a += l & 0xFFFF; b += l >>> 16;
/* 2845 */       c += h & 0xFFFF; d += h >>> 16;
/*      */       
/* 2847 */       b += a >>> 16;
/* 2848 */       c += b >>> 16;
/* 2849 */       d += c >>> 16;
/*      */       
/* 2851 */       hh[3] = ah3 = c & 0xFFFF | d << 16;
/* 2852 */       hl[3] = al3 = a & 0xFFFF | b << 16;
/*      */       
/* 2854 */       h = ah4;
/* 2855 */       l = al4;
/*      */       
/* 2857 */       a = l & 0xFFFF; b = l >>> 16;
/* 2858 */       c = h & 0xFFFF; d = h >>> 16;
/*      */       
/* 2860 */       h = hh[4];
/* 2861 */       l = hl[4];
/*      */       
/* 2863 */       a += l & 0xFFFF; b += l >>> 16;
/* 2864 */       c += h & 0xFFFF; d += h >>> 16;
/*      */       
/* 2866 */       b += a >>> 16;
/* 2867 */       c += b >>> 16;
/* 2868 */       d += c >>> 16;
/*      */       
/* 2870 */       hh[4] = ah4 = c & 0xFFFF | d << 16;
/* 2871 */       hl[4] = al4 = a & 0xFFFF | b << 16;
/*      */       
/* 2873 */       h = ah5;
/* 2874 */       l = al5;
/*      */       
/* 2876 */       a = l & 0xFFFF; b = l >>> 16;
/* 2877 */       c = h & 0xFFFF; d = h >>> 16;
/*      */       
/* 2879 */       h = hh[5];
/* 2880 */       l = hl[5];
/*      */       
/* 2882 */       a += l & 0xFFFF; b += l >>> 16;
/* 2883 */       c += h & 0xFFFF; d += h >>> 16;
/*      */       
/* 2885 */       b += a >>> 16;
/* 2886 */       c += b >>> 16;
/* 2887 */       d += c >>> 16;
/*      */       
/* 2889 */       hh[5] = ah5 = c & 0xFFFF | d << 16;
/* 2890 */       hl[5] = al5 = a & 0xFFFF | b << 16;
/*      */       
/* 2892 */       h = ah6;
/* 2893 */       l = al6;
/*      */       
/* 2895 */       a = l & 0xFFFF; b = l >>> 16;
/* 2896 */       c = h & 0xFFFF; d = h >>> 16;
/*      */       
/* 2898 */       h = hh[6];
/* 2899 */       l = hl[6];
/*      */       
/* 2901 */       a += l & 0xFFFF; b += l >>> 16;
/* 2902 */       c += h & 0xFFFF; d += h >>> 16;
/*      */       
/* 2904 */       b += a >>> 16;
/* 2905 */       c += b >>> 16;
/* 2906 */       d += c >>> 16;
/*      */       
/* 2908 */       hh[6] = ah6 = c & 0xFFFF | d << 16;
/* 2909 */       hl[6] = al6 = a & 0xFFFF | b << 16;
/*      */       
/* 2911 */       h = ah7;
/* 2912 */       l = al7;
/*      */       
/* 2914 */       a = l & 0xFFFF; b = l >>> 16;
/* 2915 */       c = h & 0xFFFF; d = h >>> 16;
/*      */       
/* 2917 */       h = hh[7];
/* 2918 */       l = hl[7];
/*      */       
/* 2920 */       a += l & 0xFFFF; b += l >>> 16;
/* 2921 */       c += h & 0xFFFF; d += h >>> 16;
/*      */       
/* 2923 */       b += a >>> 16;
/* 2924 */       c += b >>> 16;
/* 2925 */       d += c >>> 16;
/*      */       
/* 2927 */       hh[7] = ah7 = c & 0xFFFF | d << 16;
/* 2928 */       hl[7] = al7 = a & 0xFFFF | b << 16;
/*      */       
/* 2930 */       pos += 128;
/* 2931 */       n -= 128;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2942 */     return n;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int crypto_hash(byte[] out, byte[] m, int moff, int n) {
/* 2949 */     int[] hh = new int[8];
/* 2950 */     int[] hl = new int[8];
/* 2951 */     byte[] x = new byte[256];
/* 2952 */     int b = n;
/*      */ 
/*      */     
/* 2955 */     hh[0] = 1779033703;
/* 2956 */     hh[1] = -1150833019;
/* 2957 */     hh[2] = 1013904242;
/* 2958 */     hh[3] = -1521486534;
/* 2959 */     hh[4] = 1359893119;
/* 2960 */     hh[5] = -1694144372;
/* 2961 */     hh[6] = 528734635;
/* 2962 */     hh[7] = 1541459225;
/*      */     
/* 2964 */     hl[0] = -205731576;
/* 2965 */     hl[1] = -2067093701;
/* 2966 */     hl[2] = -23791573;
/* 2967 */     hl[3] = 1595750129;
/* 2968 */     hl[4] = -1377402159;
/* 2969 */     hl[5] = 725511199;
/* 2970 */     hl[6] = -79577749;
/* 2971 */     hl[7] = 327033209;
/*      */     
/* 2973 */     if (n >= 128) {
/* 2974 */       crypto_hashblocks_hl(hh, hl, m, moff, n);
/* 2975 */       n %= 128;
/*      */     } 
/*      */     int i;
/* 2978 */     for (i = 0; i < n; ) { x[i] = m[b - n + i + moff]; i++; }
/* 2979 */      x[n] = Byte.MIN_VALUE;
/*      */     
/* 2981 */     n = 256 - 128 * ((n < 112) ? 1 : 0);
/* 2982 */     x[n - 9] = 0;
/*      */     
/* 2984 */     ts64(x, n - 8, (b << 3));
/*      */     
/* 2986 */     crypto_hashblocks_hl(hh, hl, x, 0, n);
/*      */     
/* 2988 */     for (i = 0; i < 8; i++) {
/* 2989 */       long u = hh[i]; u <<= 32L; u |= hl[i] & 0xFFFFFFFFL;
/* 2990 */       ts64(out, 8 * i, u);
/*      */     } 
/*      */     
/* 2993 */     return 0;
/*      */   }
/*      */   public static int crypto_hash(byte[] out, byte[] m) {
/* 2996 */     return crypto_hash(out, m, 0, (m != null) ? m.length : 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void add(long[][] p, long[][] q) {
/* 3003 */     long[] a = new long[16];
/* 3004 */     long[] b = new long[16];
/* 3005 */     long[] c = new long[16];
/* 3006 */     long[] d = new long[16];
/* 3007 */     long[] t = new long[16];
/* 3008 */     long[] e = new long[16];
/* 3009 */     long[] f = new long[16];
/* 3010 */     long[] g = new long[16];
/* 3011 */     long[] h = new long[16];
/*      */ 
/*      */     
/* 3014 */     long[] p0 = p[0];
/* 3015 */     long[] p1 = p[1];
/* 3016 */     long[] p2 = p[2];
/* 3017 */     long[] p3 = p[3];
/*      */     
/* 3019 */     long[] q0 = q[0];
/* 3020 */     long[] q1 = q[1];
/* 3021 */     long[] q2 = q[2];
/* 3022 */     long[] q3 = q[3];
/*      */     
/* 3024 */     Z(a, 0, p1, 0, p0, 0);
/* 3025 */     Z(t, 0, q1, 0, q0, 0);
/* 3026 */     M(a, 0, a, 0, t, 0);
/* 3027 */     A(b, 0, p0, 0, p1, 0);
/* 3028 */     A(t, 0, q0, 0, q1, 0);
/* 3029 */     M(b, 0, b, 0, t, 0);
/* 3030 */     M(c, 0, p3, 0, q3, 0);
/* 3031 */     M(c, 0, c, 0, D2, 0);
/* 3032 */     M(d, 0, p2, 0, q2, 0);
/*      */     
/* 3034 */     A(d, 0, d, 0, d, 0);
/* 3035 */     Z(e, 0, b, 0, a, 0);
/* 3036 */     Z(f, 0, d, 0, c, 0);
/* 3037 */     A(g, 0, d, 0, c, 0);
/* 3038 */     A(h, 0, b, 0, a, 0);
/*      */     
/* 3040 */     M(p0, 0, e, 0, f, 0);
/* 3041 */     M(p1, 0, h, 0, g, 0);
/* 3042 */     M(p2, 0, g, 0, f, 0);
/* 3043 */     M(p3, 0, e, 0, h, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void cswap(long[][] p, long[][] q, byte b) {
/* 3050 */     for (int i = 0; i < 4; i++) {
/* 3051 */       sel25519(p[i], 0, q[i], 0, b);
/*      */     }
/*      */   }
/*      */   
/*      */   private static void pack(byte[] r, long[][] p) {
/* 3056 */     long[] tx = new long[16];
/* 3057 */     long[] ty = new long[16];
/* 3058 */     long[] zi = new long[16];
/*      */     
/* 3060 */     inv25519(zi, 0, p[2], 0);
/*      */     
/* 3062 */     M(tx, 0, p[0], 0, zi, 0);
/* 3063 */     M(ty, 0, p[1], 0, zi, 0);
/*      */     
/* 3065 */     pack25519(r, ty, 0);
/*      */     
/* 3067 */     r[31] = (byte)(r[31] ^ par25519(tx, 0) << 7);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void scalarmult(long[][] p, long[][] q, byte[] s, int soff) {
/* 3074 */     set25519(p[0], gf0);
/* 3075 */     set25519(p[1], gf1);
/* 3076 */     set25519(p[2], gf1);
/* 3077 */     set25519(p[3], gf0);
/*      */     
/* 3079 */     for (int i = 255; i >= 0; i--) {
/* 3080 */       byte b = (byte)(s[i / 8 + soff] >>> (i & 0x7) & 0x1);
/*      */       
/* 3082 */       cswap(p, q, b);
/* 3083 */       add(q, p);
/* 3084 */       add(p, p);
/* 3085 */       cswap(p, q, b);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void scalarbase(long[][] p, byte[] s, int soff) {
/* 3095 */     long[][] q = new long[4][];
/*      */     
/* 3097 */     q[0] = new long[16];
/* 3098 */     q[1] = new long[16];
/* 3099 */     q[2] = new long[16];
/* 3100 */     q[3] = new long[16];
/*      */     
/* 3102 */     set25519(q[0], X);
/* 3103 */     set25519(q[1], Y);
/* 3104 */     set25519(q[2], gf1);
/* 3105 */     M(q[3], 0, X, 0, Y, 0);
/* 3106 */     scalarmult(p, q, s, soff);
/*      */   }
/*      */   
/*      */   public static int crypto_sign_keypair(byte[] pk, byte[] sk, boolean seeded) {
/* 3110 */     byte[] d = new byte[64];
/* 3111 */     long[][] p = new long[4][];
/*      */     
/* 3113 */     p[0] = new long[16];
/* 3114 */     p[1] = new long[16];
/* 3115 */     p[2] = new long[16];
/* 3116 */     p[3] = new long[16];
/*      */ 
/*      */ 
/*      */     
/* 3120 */     if (!seeded) randombytes(sk, 32); 
/* 3121 */     crypto_hash(d, sk, 0, 32);
/* 3122 */     d[0] = (byte)(d[0] & 0xF8);
/* 3123 */     d[31] = (byte)(d[31] & Byte.MAX_VALUE);
/* 3124 */     d[31] = (byte)(d[31] | 0x40);
/*      */     
/* 3126 */     scalarbase(p, d, 0);
/* 3127 */     pack(pk, p);
/*      */     
/* 3129 */     for (int i = 0; i < 32; ) { sk[i + 32] = pk[i]; i++; }
/* 3130 */      return 0;
/*      */   }
/*      */   
/* 3133 */   private static final long[] L = new long[] { 237L, 211L, 245L, 92L, 26L, 99L, 18L, 88L, 214L, 156L, 247L, 162L, 222L, 249L, 222L, 20L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 0L, 16L };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void modL(byte[] r, int roff, long[] x) {
/*      */     int i;
/* 3145 */     for (i = 63; i >= 32; i--) {
/* 3146 */       long l = 0L; int k;
/* 3147 */       for (k = i - 32; k < i - 12; k++) {
/* 3148 */         x[k] = x[k] + l - 16L * x[i] * L[k - i - 32];
/* 3149 */         l = x[k] + 128L >> 8L;
/* 3150 */         x[k] = x[k] - (l << 8L);
/*      */       } 
/* 3152 */       x[k] = x[k] + l;
/* 3153 */       x[i] = 0L;
/*      */     } 
/* 3155 */     long carry = 0L;
/*      */     int j;
/* 3157 */     for (j = 0; j < 32; j++) {
/* 3158 */       x[j] = x[j] + carry - (x[31] >> 4L) * L[j];
/* 3159 */       carry = x[j] >> 8L;
/* 3160 */       x[j] = x[j] & 0xFFL;
/*      */     } 
/*      */     
/* 3163 */     for (j = 0; j < 32; ) { x[j] = x[j] - carry * L[j]; j++; }
/*      */     
/* 3165 */     for (i = 0; i < 32; i++) {
/* 3166 */       x[i + 1] = x[i + 1] + (x[i] >> 8L);
/* 3167 */       r[i + roff] = (byte)(int)(x[i] & 0xFFL);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void reduce(byte[] r) {
/* 3173 */     long[] x = new long[64];
/*      */     
/*      */     int i;
/* 3176 */     for (i = 0; i < 64; ) { x[i] = (r[i] & 0xFF); i++; }
/*      */     
/* 3178 */     for (i = 0; i < 64; ) { r[i] = 0; i++; }
/*      */     
/* 3180 */     modL(r, 0, x);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int crypto_sign(byte[] sm, long dummy, byte[] m, int moff, int n, byte[] sk) {
/* 3187 */     byte[] d = new byte[64], h = new byte[64], r = new byte[64];
/*      */ 
/*      */     
/* 3190 */     long[] x = new long[64];
/*      */     
/* 3192 */     long[][] p = new long[4][];
/* 3193 */     p[0] = new long[16];
/* 3194 */     p[1] = new long[16];
/* 3195 */     p[2] = new long[16];
/* 3196 */     p[3] = new long[16];
/*      */     
/* 3198 */     crypto_hash(d, sk, 0, 32);
/* 3199 */     d[0] = (byte)(d[0] & 0xF8);
/* 3200 */     d[31] = (byte)(d[31] & Byte.MAX_VALUE);
/* 3201 */     d[31] = (byte)(d[31] | 0x40);
/*      */     
/*      */     int i;
/*      */     
/* 3205 */     for (i = 0; i < n; ) { sm[64 + i] = m[i + moff]; i++; }
/*      */     
/* 3207 */     for (i = 0; i < 32; ) { sm[32 + i] = d[32 + i]; i++; }
/*      */     
/* 3209 */     crypto_hash(r, sm, 32, n + 32);
/* 3210 */     reduce(r);
/* 3211 */     scalarbase(p, r, 0);
/* 3212 */     pack(sm, p);
/*      */     
/* 3214 */     for (i = 0; i < 32; ) { sm[i + 32] = sk[i + 32]; i++; }
/* 3215 */      crypto_hash(h, sm, 0, n + 64);
/* 3216 */     reduce(h);
/*      */     
/* 3218 */     for (i = 0; i < 64; ) { x[i] = 0L; i++; }
/*      */     
/* 3220 */     for (i = 0; i < 32; ) { x[i] = (r[i] & 0xFF); i++; }
/*      */     
/* 3222 */     for (i = 0; i < 32; ) { for (int j = 0; j < 32; ) { x[i + j] = x[i + j] + (h[i] & 0xFF) * (d[j] & 0xFF); j++; }  i++; }
/*      */     
/* 3224 */     modL(sm, 32, x);
/*      */     
/* 3226 */     return 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private static int unpackneg(long[][] r, byte[] p) {
/* 3231 */     long[] t = new long[16];
/* 3232 */     long[] chk = new long[16];
/* 3233 */     long[] num = new long[16];
/* 3234 */     long[] den = new long[16];
/* 3235 */     long[] den2 = new long[16];
/* 3236 */     long[] den4 = new long[16];
/* 3237 */     long[] den6 = new long[16];
/*      */     
/* 3239 */     set25519(r[2], gf1);
/* 3240 */     unpack25519(r[1], p);
/* 3241 */     S(num, r[1]);
/* 3242 */     M(den, num, D);
/* 3243 */     Z(num, num, r[2]);
/* 3244 */     A(den, r[2], den);
/*      */     
/* 3246 */     S(den2, den);
/* 3247 */     S(den4, den2);
/* 3248 */     M(den6, den4, den2);
/* 3249 */     M(t, den6, num);
/* 3250 */     M(t, t, den);
/*      */     
/* 3252 */     pow2523(t, t);
/* 3253 */     M(t, t, num);
/* 3254 */     M(t, t, den);
/* 3255 */     M(t, t, den);
/* 3256 */     M(r[0], t, den);
/*      */     
/* 3258 */     S(chk, r[0]);
/* 3259 */     M(chk, chk, den);
/* 3260 */     if (neq25519(chk, num) != 0) M(r[0], r[0], I);
/*      */     
/* 3262 */     S(chk, r[0]);
/* 3263 */     M(chk, chk, den);
/* 3264 */     if (neq25519(chk, num) != 0) return -1;
/*      */     
/* 3266 */     if (par25519(r[0]) == (p[31] & 0xFF) >>> 7) Z(r[0], gf0, r[0]);
/*      */     
/* 3268 */     M(r[3], r[0], r[1]);
/*      */     
/* 3270 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int crypto_sign_open(byte[] m, long dummy, byte[] sm, int smoff, int n, byte[] pk) {
/* 3278 */     byte[] t = new byte[32], h = new byte[64];
/*      */     
/* 3280 */     long[][] p = new long[4][];
/* 3281 */     p[0] = new long[16];
/* 3282 */     p[1] = new long[16];
/* 3283 */     p[2] = new long[16];
/* 3284 */     p[3] = new long[16];
/*      */     
/* 3286 */     long[][] q = new long[4][];
/* 3287 */     q[0] = new long[16];
/* 3288 */     q[1] = new long[16];
/* 3289 */     q[2] = new long[16];
/* 3290 */     q[3] = new long[16];
/*      */ 
/*      */ 
/*      */     
/* 3294 */     if (n < 64) return -1;
/*      */     
/* 3296 */     if (unpackneg(q, pk) != 0) return -1; 
/*      */     int i;
/* 3298 */     for (i = 0; i < n; ) { m[i] = sm[i + smoff]; i++; }
/*      */     
/* 3300 */     for (i = 0; i < 32; ) { m[i + 32] = pk[i]; i++; }
/*      */     
/* 3302 */     crypto_hash(h, m, 0, n);
/*      */     
/* 3304 */     reduce(h);
/* 3305 */     scalarmult(p, q, h, 0);
/*      */     
/* 3307 */     scalarbase(q, sm, 32 + smoff);
/* 3308 */     add(p, q);
/* 3309 */     pack(t, p);
/*      */     
/* 3311 */     n -= 64;
/* 3312 */     if (crypto_verify_32(sm, smoff, t, 0) != 0)
/*      */     {
/*      */       
/* 3315 */       return -1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3322 */     return 0;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 3329 */   private static final SecureRandom jrandom = new SecureRandom();
/*      */   
/*      */   public static byte[] randombytes(byte[] x) {
/* 3332 */     jrandom.nextBytes(x);
/* 3333 */     return x;
/*      */   }
/*      */   
/*      */   public static byte[] randombytes(int len) {
/* 3337 */     return randombytes(new byte[len]);
/*      */   }
/*      */   
/*      */   public static byte[] randombytes(byte[] x, int len) {
/* 3341 */     byte[] b = randombytes(len);
/* 3342 */     System.arraycopy(b, 0, x, 0, len);
/* 3343 */     return x;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] makeBoxNonce() {
/* 3371 */     return randombytes(24);
/*      */   }
/*      */   
/*      */   public static byte[] makeSecretBoxNonce() {
/* 3375 */     return randombytes(24);
/*      */   }
/*      */   
/*      */   public static String base64EncodeToString(byte[] b) {
/* 3379 */     return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
/*      */   }
/*      */ 
/*      */   
/*      */   public static byte[] base64Decode(String s) {
/* 3384 */     return Base64.getUrlDecoder().decode(s);
/*      */   }
/*      */ 
/*      */   
/*      */   public static String hexEncodeToString(byte[] raw) {
/* 3389 */     String HEXES = "0123456789ABCDEF";
/* 3390 */     StringBuilder hex = new StringBuilder(2 * raw.length);
/* 3391 */     for (byte b : raw) {
/* 3392 */       hex.append(HEXES.charAt((b & 0xF0) >> 4))
/* 3393 */         .append(HEXES.charAt(b & 0xF));
/*      */     }
/* 3395 */     return hex.toString();
/*      */   }
/*      */   
/*      */   public static byte[] hexDecode(String s) {
/* 3399 */     byte[] b = new byte[s.length() / 2];
/* 3400 */     for (int i = 0; i < s.length(); i += 2) {
/* 3401 */       b[i / 2] = 
/* 3402 */         (byte)((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
/*      */     }
/* 3404 */     return b;
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\iwebpp\crypto\TweetNaclFast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */