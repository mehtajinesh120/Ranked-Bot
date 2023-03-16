/*     */ package net.dv8tion.jda.api.audio.hooks;
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
/*     */ public enum ConnectionStatus
/*     */ {
/*  25 */   NOT_CONNECTED(false),
/*     */   
/*  27 */   SHUTTING_DOWN(false),
/*     */   
/*  29 */   CONNECTING_AWAITING_ENDPOINT,
/*     */   
/*  31 */   CONNECTING_AWAITING_WEBSOCKET_CONNECT,
/*     */   
/*  33 */   CONNECTING_AWAITING_AUTHENTICATION,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   CONNECTING_ATTEMPTING_UDP_DISCOVERY,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  44 */   CONNECTING_AWAITING_READY,
/*     */   
/*  46 */   CONNECTED,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  51 */   DISCONNECTED_LOST_PERMISSION(false),
/*     */ 
/*     */ 
/*     */   
/*  55 */   DISCONNECTED_CHANNEL_DELETED(false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  60 */   DISCONNECTED_REMOVED_FROM_GUILD(false),
/*     */   
/*  62 */   DISCONNECTED_KICKED_FROM_CHANNEL(false),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   DISCONNECTED_REMOVED_DURING_RECONNECT(false),
/*     */ 
/*     */ 
/*     */   
/*  71 */   DISCONNECTED_AUTHENTICATION_FAILURE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   AUDIO_REGION_CHANGE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   ERROR_LOST_CONNECTION,
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  90 */   ERROR_CANNOT_RESUME,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   ERROR_WEBSOCKET_UNABLE_TO_CONNECT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 102 */   ERROR_UNSUPPORTED_ENCRYPTION_MODES,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 109 */   ERROR_UDP_UNABLE_TO_CONNECT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   ERROR_CONNECTION_TIMEOUT;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final boolean shouldReconnect;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ConnectionStatus(boolean shouldReconnect) {
/* 127 */     this.shouldReconnect = shouldReconnect;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldReconnect() {
/* 132 */     return this.shouldReconnect;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\hooks\ConnectionStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */