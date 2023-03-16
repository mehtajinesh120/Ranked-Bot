/*     */ package net.dv8tion.jda.api.events;
/*     */ 
/*     */ import com.neovisionaries.ws.client.WebSocketFrame;
/*     */ import java.time.OffsetDateTime;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.requests.CloseCode;
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
/*     */ public class DisconnectEvent
/*     */   extends Event
/*     */ {
/*     */   protected final WebSocketFrame serverCloseFrame;
/*     */   protected final WebSocketFrame clientCloseFrame;
/*     */   protected final boolean closedByServer;
/*     */   protected final OffsetDateTime disconnectTime;
/*     */   
/*     */   public DisconnectEvent(@Nonnull JDA api, @Nullable WebSocketFrame serverCloseFrame, @Nullable WebSocketFrame clientCloseFrame, boolean closedByServer, @Nonnull OffsetDateTime disconnectTime) {
/*  47 */     super(api);
/*  48 */     this.serverCloseFrame = serverCloseFrame;
/*  49 */     this.clientCloseFrame = clientCloseFrame;
/*  50 */     this.closedByServer = closedByServer;
/*  51 */     this.disconnectTime = disconnectTime;
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
/*     */   @Nullable
/*     */   public CloseCode getCloseCode() {
/*  66 */     return (this.serverCloseFrame != null) ? CloseCode.from(this.serverCloseFrame.getCloseCode()) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public WebSocketFrame getServiceCloseFrame() {
/*  77 */     return this.serverCloseFrame;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public WebSocketFrame getClientCloseFrame() {
/*  88 */     return this.clientCloseFrame;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isClosedByServer() {
/*  98 */     return this.closedByServer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public OffsetDateTime getTimeDisconnected() {
/* 109 */     return this.disconnectTime;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\DisconnectEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */