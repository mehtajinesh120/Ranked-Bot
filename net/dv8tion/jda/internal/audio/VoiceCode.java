/*    */ package net.dv8tion.jda.internal.audio;
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
/*    */ public final class VoiceCode
/*    */ {
/*    */   public static final int IDENTIFY = 0;
/*    */   public static final int SELECT_PROTOCOL = 1;
/*    */   public static final int READY = 2;
/*    */   public static final int HEARTBEAT = 3;
/*    */   public static final int SESSION_DESCRIPTION = 4;
/*    */   public static final int USER_SPEAKING_UPDATE = 5;
/*    */   public static final int HEARTBEAT_ACK = 6;
/*    */   public static final int RESUME = 7;
/*    */   public static final int HELLO = 8;
/*    */   public static final int RESUMED = 9;
/*    */   public static final int USER_DISCONNECT = 13;
/*    */   
/*    */   public enum Close
/*    */   {
/* 38 */     HEARTBEAT_TIMEOUT(1000, "We did not heartbeat in time"),
/* 39 */     UNKNOWN_OP_CODE(4001, "Sent an invalid op code"),
/* 40 */     NOT_AUTHENTICATED(4003, "Tried to send payload before authenticating session"),
/* 41 */     AUTHENTICATION_FAILED(4004, "The token sent in the identify payload is incorrect"),
/* 42 */     ALREADY_AUTHENTICATED(4005, "Tried to authenticate when already authenticated"),
/* 43 */     INVALID_SESSION(4006, "The session with which we attempted to resume is invalid"),
/* 44 */     SESSION_TIMEOUT(4009, "Heartbeat timed out"),
/* 45 */     SERVER_NOT_FOUND(4011, "The server we attempted to connect to was not found"),
/* 46 */     UNKNOWN_PROTOCOL(4012, "The selected protocol is not supported"),
/* 47 */     DISCONNECTED(4014, "The connection has been dropped normally"),
/* 48 */     SERVER_CRASH(4015, "The server we were connected to has crashed"),
/* 49 */     UNKNOWN_ENCRYPTION_MODE(4016, "The specified encryption method is not supported"),
/*    */     
/* 51 */     UNKNOWN(0, "Unknown code");
/*    */     
/*    */     private final int code;
/*    */     
/*    */     private final String meaning;
/*    */     
/*    */     Close(int code, String meaning) {
/* 58 */       this.code = code;
/* 59 */       this.meaning = meaning;
/*    */     }
/*    */ 
/*    */     
/*    */     public static Close from(int code) {
/* 64 */       for (Close c : values()) {
/*    */         
/* 66 */         if (c.code == code)
/* 67 */           return c; 
/*    */       } 
/* 69 */       return UNKNOWN;
/*    */     }
/*    */ 
/*    */     
/*    */     public int getCode() {
/* 74 */       return this.code;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getMeaning() {
/* 79 */       return this.meaning;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\audio\VoiceCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */