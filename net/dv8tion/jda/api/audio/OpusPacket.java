/*     */ package net.dv8tion.jda.api.audio;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.internal.audio.AudioPacket;
/*     */ import net.dv8tion.jda.internal.audio.Decoder;
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
/*     */ public final class OpusPacket
/*     */   implements Comparable<OpusPacket>
/*     */ {
/*     */   public static final int OPUS_SAMPLE_RATE = 48000;
/*     */   public static final int OPUS_FRAME_SIZE = 960;
/*     */   public static final int OPUS_FRAME_TIME_AMOUNT = 20;
/*     */   public static final int OPUS_CHANNEL_COUNT = 2;
/*     */   private final long userId;
/*     */   private final byte[] opusAudio;
/*     */   private final Decoder decoder;
/*     */   private final AudioPacket rawPacket;
/*     */   private short[] decoded;
/*     */   private boolean triedDecode;
/*     */   
/*     */   public OpusPacket(@Nonnull AudioPacket packet, long userId, @Nullable Decoder decoder) {
/*  56 */     this.rawPacket = packet;
/*  57 */     this.userId = userId;
/*  58 */     this.decoder = decoder;
/*  59 */     this.opusAudio = packet.getEncodedAudio().array();
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
/*     */   public char getSequence() {
/*  75 */     return this.rawPacket.getSequence();
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
/*     */   public int getTimestamp() {
/*  87 */     return this.rawPacket.getTimestamp();
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
/*     */   public int getSSRC() {
/*  99 */     return this.rawPacket.getSSRC();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getUserId() {
/* 109 */     return this.userId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean canDecode() {
/* 119 */     return (this.decoder != null && this.decoder.isInOrder(getSequence()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public byte[] getOpusAudio() {
/* 131 */     return Arrays.copyOf(this.opusAudio, this.opusAudio.length);
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
/*     */   @Nullable
/*     */   public synchronized short[] decode() {
/* 152 */     if (this.triedDecode)
/* 153 */       return this.decoded; 
/* 154 */     if (this.decoder == null)
/* 155 */       throw new IllegalStateException("No decoder available"); 
/* 156 */     if (!this.decoder.isInOrder(getSequence()))
/* 157 */       throw new IllegalStateException("Packet is not in order"); 
/* 158 */     this.triedDecode = true;
/* 159 */     return this.decoded = this.decoder.decodeFromOpus(this.rawPacket);
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
/*     */   @Nonnull
/*     */   public byte[] getAudioData(double volume) {
/* 179 */     return getAudioData(decode(), volume);
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
/*     */   @Nonnull
/*     */   public static byte[] getAudioData(@Nonnull short[] decoded, double volume) {
/* 201 */     if (decoded == null)
/* 202 */       throw new IllegalArgumentException("Cannot get audio data from null"); 
/* 203 */     int byteIndex = 0;
/* 204 */     byte[] audio = new byte[decoded.length * 2];
/* 205 */     for (short s : decoded) {
/*     */       
/* 207 */       if (volume != 1.0D) {
/* 208 */         s = (short)(int)(s * volume);
/*     */       }
/* 210 */       byte leftByte = (byte)(s >>> 8 & 0xFF);
/* 211 */       byte rightByte = (byte)(s & 0xFF);
/* 212 */       audio[byteIndex] = leftByte;
/* 213 */       audio[byteIndex + 1] = rightByte;
/* 214 */       byteIndex += 2;
/*     */     } 
/* 216 */     return audio;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(@Nonnull OpusPacket o) {
/* 222 */     return getSequence() - o.getSequence();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 228 */     return Objects.hash(new Object[] { Character.valueOf(getSequence()), Integer.valueOf(getTimestamp()), getOpusAudio() });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 234 */     if (obj == this)
/* 235 */       return true; 
/* 236 */     if (!(obj instanceof OpusPacket))
/* 237 */       return false; 
/* 238 */     OpusPacket other = (OpusPacket)obj;
/* 239 */     return (getSequence() == other.getSequence() && 
/* 240 */       getTimestamp() == other.getTimestamp() && 
/* 241 */       getSSRC() == other.getSSRC());
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\OpusPacket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */