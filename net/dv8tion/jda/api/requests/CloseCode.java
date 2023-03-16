/*     */ package net.dv8tion.jda.api.requests;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
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
/*     */ public enum CloseCode
/*     */ {
/*  28 */   RECONNECT(4900, "The connection has been closed to reconnect."),
/*  29 */   GRACEFUL_CLOSE(1000, "The connection was closed gracefully or your heartbeats timed out."),
/*  30 */   CLOUD_FLARE_LOAD(1001, "The connection was closed due to CloudFlare load balancing."),
/*  31 */   INTERNAL_SERVER_ERROR(1006, "Something broke on the remote's end, sorry 'bout that... Try reconnecting!"),
/*  32 */   UNKNOWN_ERROR(4000, "The server is not sure what went wrong. Try reconnecting?"),
/*  33 */   UNKNOWN_OPCODE(4001, "You sent an invalid Gateway OP Code. Don't do that!"),
/*  34 */   DECODE_ERROR(4002, "You sent an invalid payload to the server. Don't do that!"),
/*  35 */   NOT_AUTHENTICATED(4003, "You sent a payload prior to identifying."),
/*  36 */   AUTHENTICATION_FAILED(4004, "The account token sent with your identify payload is incorrect.", false),
/*  37 */   ALREADY_AUTHENTICATED(4005, "You sent more than one identify payload. Don't do that!"),
/*  38 */   INVALID_SEQ(4007, "The seq sent when resuming the session was invalid. Reconnect and start a new session."),
/*  39 */   RATE_LIMITED(4008, "Woah nelly! You're sending payloads to us too quickly. Slow it down!"),
/*  40 */   SESSION_TIMEOUT(4009, "Your session timed out. Reconnect and start a new one."),
/*  41 */   INVALID_SHARD(4010, "You sent an invalid shard when identifying.", false),
/*  42 */   SHARDING_REQUIRED(4011, "The session would have handled too many guilds - you are required to shard your connection in order to connect.", false),
/*  43 */   INVALID_INTENTS(4013, "Invalid intents.", false),
/*  44 */   DISALLOWED_INTENTS(4014, "Disallowed intents. Your bot might not be eligible to request a privileged intent such as GUILD_PRESENCES or GUILD_MEMBERS.", false);
/*     */ 
/*     */   
/*     */   private final int code;
/*     */ 
/*     */   
/*     */   private final boolean isReconnect;
/*     */ 
/*     */   
/*     */   private final String meaning;
/*     */ 
/*     */   
/*     */   CloseCode(int code, String meaning, boolean isReconnect) {
/*  57 */     this.code = code;
/*  58 */     this.meaning = meaning;
/*  59 */     this.isReconnect = isReconnect;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getCode() {
/*  69 */     return this.code;
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
/*     */   public String getMeaning() {
/*  81 */     return this.meaning;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isReconnect() {
/*  92 */     return this.isReconnect;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  98 */     return "CloseCode(" + this.code + " / " + this.meaning + ")";
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
/*     */   @Nullable
/*     */   public static CloseCode from(int code) {
/* 114 */     for (CloseCode c : values()) {
/* 115 */       if (c.code == code) return c; 
/* 116 */     }  return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\requests\CloseCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */