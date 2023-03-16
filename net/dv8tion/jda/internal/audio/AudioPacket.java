/*     */ package net.dv8tion.jda.internal.audio;
/*     */ 
/*     */ import com.iwebpp.crypto.TweetNaclFast;
/*     */ import java.net.DatagramPacket;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import net.dv8tion.jda.internal.utils.IOUtil;
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
/*     */ public class AudioPacket
/*     */ {
/*     */   public static final int RTP_HEADER_BYTE_LENGTH = 12;
/*     */   public static final byte RTP_VERSION_PAD_EXTEND = -128;
/*     */   public static final byte RTP_PAYLOAD_TYPE = 120;
/*     */   public static final short RTP_DISCORD_EXTENSION = -16674;
/*     */   public static final int PT_INDEX = 1;
/*     */   public static final int SEQ_INDEX = 2;
/*     */   public static final int TIMESTAMP_INDEX = 4;
/*     */   public static final int SSRC_INDEX = 8;
/*     */   private final byte type;
/*     */   private final char seq;
/*     */   private final int timestamp;
/*     */   private final int ssrc;
/*     */   private final byte[] rawPacket;
/*     */   private final ByteBuffer encodedAudio;
/*     */   
/*     */   public AudioPacket(DatagramPacket packet) {
/*  70 */     this(Arrays.copyOf(packet.getData(), packet.getLength()));
/*     */   }
/*     */ 
/*     */   
/*     */   public AudioPacket(byte[] rawPacket) {
/*  75 */     this.rawPacket = rawPacket;
/*     */     
/*  77 */     ByteBuffer buffer = ByteBuffer.wrap(rawPacket);
/*  78 */     this.seq = buffer.getChar(2);
/*  79 */     this.timestamp = buffer.getInt(4);
/*  80 */     this.ssrc = buffer.getInt(8);
/*  81 */     this.type = buffer.get(1);
/*     */     
/*  83 */     byte profile = buffer.get(0);
/*  84 */     byte[] data = buffer.array();
/*  85 */     boolean hasExtension = ((profile & 0x10) != 0);
/*  86 */     byte cc = (byte)(profile & 0xF);
/*  87 */     int csrcLength = cc * 4;
/*     */     
/*  89 */     short extension = hasExtension ? IOUtil.getShortBigEndian(data, 12 + csrcLength) : 0;
/*     */     
/*  91 */     int offset = 12 + csrcLength;
/*  92 */     if (hasExtension && extension == -16674) {
/*  93 */       offset = getPayloadOffset(data, csrcLength);
/*     */     }
/*  95 */     this.encodedAudio = ByteBuffer.allocate(data.length - offset);
/*  96 */     this.encodedAudio.put(data, offset, this.encodedAudio.capacity());
/*  97 */     this.encodedAudio.flip();
/*     */   }
/*     */ 
/*     */   
/*     */   public AudioPacket(ByteBuffer buffer, char seq, int timestamp, int ssrc, ByteBuffer encodedAudio) {
/* 102 */     this.seq = seq;
/* 103 */     this.ssrc = ssrc;
/* 104 */     this.timestamp = timestamp;
/* 105 */     this.encodedAudio = encodedAudio;
/* 106 */     this.type = 120;
/* 107 */     this.rawPacket = generateRawPacket(buffer, seq, timestamp, ssrc, encodedAudio);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int getPayloadOffset(byte[] data, int csrcLength) {
/* 113 */     short headerLength = IOUtil.getShortBigEndian(data, 14 + csrcLength);
/* 114 */     int i = 16 + csrcLength + headerLength * 4;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 120 */     while (data[i] == 0)
/* 121 */       i++; 
/* 122 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getHeader() {
/* 129 */     return Arrays.copyOf(this.rawPacket, 12);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getNoncePadded() {
/* 134 */     byte[] nonce = new byte[24];
/*     */     
/* 136 */     System.arraycopy(this.rawPacket, 0, nonce, 0, 12);
/* 137 */     return nonce;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte[] getRawPacket() {
/* 142 */     return this.rawPacket;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteBuffer getEncodedAudio() {
/* 147 */     return this.encodedAudio;
/*     */   }
/*     */ 
/*     */   
/*     */   public char getSequence() {
/* 152 */     return this.seq;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSSRC() {
/* 157 */     return this.ssrc;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTimestamp() {
/* 162 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected ByteBuffer asEncryptedPacket(TweetNaclFast.SecretBox boxer, ByteBuffer buffer, byte[] nonce, int nlen) {
/* 170 */     byte[] extendedNonce = nonce;
/* 171 */     if (nlen == 0) {
/* 172 */       extendedNonce = getNoncePadded();
/*     */     }
/*     */     
/* 175 */     byte[] array = this.encodedAudio.array();
/* 176 */     int offset = this.encodedAudio.arrayOffset() + this.encodedAudio.position();
/* 177 */     int length = this.encodedAudio.remaining();
/* 178 */     byte[] encryptedAudio = boxer.box(array, offset, length, extendedNonce);
/*     */     
/* 180 */     buffer.clear();
/* 181 */     int capacity = 12 + encryptedAudio.length + nlen;
/* 182 */     if (capacity > buffer.remaining())
/* 183 */       buffer = ByteBuffer.allocate(capacity); 
/* 184 */     populateBuffer(this.seq, this.timestamp, this.ssrc, ByteBuffer.wrap(encryptedAudio), buffer);
/* 185 */     if (nlen > 0) {
/* 186 */       buffer.put(nonce, 0, nlen);
/*     */     }
/* 188 */     buffer.flip();
/* 189 */     return buffer;
/*     */   }
/*     */   
/*     */   protected static AudioPacket decryptAudioPacket(AudioEncryption encryption, DatagramPacket packet, byte[] secretKey) {
/*     */     byte[] extendedNonce;
/* 194 */     TweetNaclFast.SecretBox boxer = new TweetNaclFast.SecretBox(secretKey);
/* 195 */     AudioPacket encryptedPacket = new AudioPacket(packet);
/* 196 */     if (encryptedPacket.type != 120) {
/* 197 */       return null;
/*     */     }
/*     */     
/* 200 */     byte[] rawPacket = encryptedPacket.getRawPacket();
/* 201 */     switch (encryption) {
/*     */       
/*     */       case XSALSA20_POLY1305:
/* 204 */         extendedNonce = encryptedPacket.getNoncePadded();
/*     */         break;
/*     */       case XSALSA20_POLY1305_SUFFIX:
/* 207 */         extendedNonce = new byte[24];
/* 208 */         System.arraycopy(rawPacket, rawPacket.length - extendedNonce.length, extendedNonce, 0, extendedNonce.length);
/*     */         break;
/*     */       case XSALSA20_POLY1305_LITE:
/* 211 */         extendedNonce = new byte[24];
/* 212 */         System.arraycopy(rawPacket, rawPacket.length - 4, extendedNonce, 0, 4);
/*     */         break;
/*     */       default:
/* 215 */         AudioConnection.LOG.debug("Failed to decrypt audio packet, unsupported encryption mode!");
/* 216 */         return null;
/*     */     } 
/*     */     
/* 219 */     ByteBuffer encodedAudio = encryptedPacket.encodedAudio;
/* 220 */     int length = encodedAudio.remaining();
/* 221 */     int offset = encodedAudio.arrayOffset() + encodedAudio.position();
/* 222 */     switch (encryption) {
/*     */       case XSALSA20_POLY1305:
/*     */         break;
/*     */ 
/*     */       
/*     */       case XSALSA20_POLY1305_LITE:
/* 228 */         length -= 4;
/*     */         break;
/*     */       case XSALSA20_POLY1305_SUFFIX:
/* 231 */         length -= 24;
/*     */         break;
/*     */       default:
/* 234 */         AudioConnection.LOG.debug("Failed to decrypt audio packet, unsupported encryption mode!");
/* 235 */         return null;
/*     */     } 
/*     */     
/* 238 */     byte[] decryptedAudio = boxer.open(encodedAudio.array(), offset, length, extendedNonce);
/* 239 */     if (decryptedAudio == null) {
/*     */       
/* 241 */       AudioConnection.LOG.trace("Failed to decrypt audio packet");
/* 242 */       return null;
/*     */     } 
/* 244 */     byte[] decryptedRawPacket = new byte[12 + decryptedAudio.length];
/*     */ 
/*     */ 
/*     */     
/* 248 */     System.arraycopy(encryptedPacket.rawPacket, 0, decryptedRawPacket, 0, 12);
/* 249 */     System.arraycopy(decryptedAudio, 0, decryptedRawPacket, 12, decryptedAudio.length);
/*     */     
/* 251 */     return new AudioPacket(decryptedRawPacket);
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] generateRawPacket(ByteBuffer buffer, char seq, int timestamp, int ssrc, ByteBuffer data) {
/* 256 */     if (buffer == null)
/* 257 */       buffer = ByteBuffer.allocate(12 + data.remaining()); 
/* 258 */     populateBuffer(seq, timestamp, ssrc, data, buffer);
/* 259 */     return buffer.array();
/*     */   }
/*     */ 
/*     */   
/*     */   private static void populateBuffer(char seq, int timestamp, int ssrc, ByteBuffer data, ByteBuffer buffer) {
/* 264 */     buffer.put(-128);
/* 265 */     buffer.put((byte)120);
/* 266 */     buffer.putChar(seq);
/* 267 */     buffer.putInt(timestamp);
/* 268 */     buffer.putInt(ssrc);
/* 269 */     buffer.put(data);
/* 270 */     data.flip();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\audio\AudioPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */