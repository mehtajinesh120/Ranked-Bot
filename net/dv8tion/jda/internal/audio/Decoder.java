/*     */ package net.dv8tion.jda.internal.audio;
/*     */ 
/*     */ import com.sun.jna.ptr.PointerByReference;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.IntBuffer;
/*     */ import java.nio.ShortBuffer;
/*     */ import tomp2p.opuswrapper.Opus;
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
/*     */ public class Decoder
/*     */ {
/*     */   protected int ssrc;
/*     */   protected char lastSeq;
/*     */   protected int lastTimestamp;
/*     */   protected PointerByReference opusDecoder;
/*     */   
/*     */   protected Decoder(int ssrc) {
/*  39 */     this.ssrc = ssrc;
/*  40 */     this.lastSeq = Character.MAX_VALUE;
/*  41 */     this.lastTimestamp = -1;
/*     */     
/*  43 */     IntBuffer error = IntBuffer.allocate(1);
/*  44 */     this.opusDecoder = Opus.INSTANCE.opus_decoder_create(48000, 2, error);
/*  45 */     if (error.get() != 0 && this.opusDecoder == null) {
/*  46 */       throw new IllegalStateException("Received error code from opus_decoder_create(...): " + error.get());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isInOrder(char newSeq) {
/*  51 */     return (this.lastSeq == Character.MAX_VALUE || newSeq > this.lastSeq || this.lastSeq - newSeq > 10);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean wasPacketLost(char newSeq) {
/*  56 */     return (newSeq > this.lastSeq + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public short[] decodeFromOpus(AudioPacket decryptedPacket) {
/*     */     int result;
/*  62 */     ShortBuffer decoded = ShortBuffer.allocate(4096);
/*  63 */     if (decryptedPacket == null) {
/*     */       
/*  65 */       result = Opus.INSTANCE.opus_decode(this.opusDecoder, null, 0, decoded, 960, 0);
/*  66 */       this.lastSeq = Character.MAX_VALUE;
/*  67 */       this.lastTimestamp = -1;
/*     */     }
/*     */     else {
/*     */       
/*  71 */       this.lastSeq = decryptedPacket.getSequence();
/*  72 */       this.lastTimestamp = decryptedPacket.getTimestamp();
/*     */       
/*  74 */       ByteBuffer encodedAudio = decryptedPacket.getEncodedAudio();
/*  75 */       int length = encodedAudio.remaining();
/*  76 */       int offset = encodedAudio.arrayOffset() + encodedAudio.position();
/*  77 */       byte[] buf = new byte[length];
/*  78 */       byte[] data = encodedAudio.array();
/*  79 */       System.arraycopy(data, offset, buf, 0, length);
/*  80 */       result = Opus.INSTANCE.opus_decode(this.opusDecoder, buf, buf.length, decoded, 960, 0);
/*     */     } 
/*     */ 
/*     */     
/*  84 */     if (result < 0) {
/*     */       
/*  86 */       handleDecodeError(result);
/*  87 */       return null;
/*     */     } 
/*     */     
/*  90 */     short[] audio = new short[result * 2];
/*  91 */     decoded.get(audio);
/*  92 */     return audio;
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleDecodeError(int result) {
/*  97 */     StringBuilder b = new StringBuilder("Decoder failed to decode audio from user with code ");
/*  98 */     switch (result) {
/*     */       
/*     */       case -1:
/* 101 */         b.append("OPUS_BAD_ARG");
/*     */         break;
/*     */       case -2:
/* 104 */         b.append("OPUS_BUFFER_TOO_SMALL");
/*     */         break;
/*     */       case -3:
/* 107 */         b.append("OPUS_INTERNAL_ERROR");
/*     */         break;
/*     */       case -4:
/* 110 */         b.append("OPUS_INVALID_PACKET");
/*     */         break;
/*     */       case -5:
/* 113 */         b.append("OPUS_UNIMPLEMENTED");
/*     */         break;
/*     */       case -6:
/* 116 */         b.append("OPUS_INVALID_STATE");
/*     */         break;
/*     */       case -7:
/* 119 */         b.append("OPUS_ALLOC_FAIL");
/*     */         break;
/*     */       default:
/* 122 */         b.append(result); break;
/*     */     } 
/* 124 */     AudioConnection.LOG.debug("{}", b);
/*     */   }
/*     */ 
/*     */   
/*     */   protected synchronized void close() {
/* 129 */     if (this.opusDecoder != null) {
/*     */       
/* 131 */       Opus.INSTANCE.opus_decoder_destroy(this.opusDecoder);
/* 132 */       this.opusDecoder = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void finalize() throws Throwable {
/* 140 */     super.finalize();
/* 141 */     close();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\audio\Decoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */