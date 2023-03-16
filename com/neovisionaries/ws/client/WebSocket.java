/*      */ package com.neovisionaries.ws.client;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.net.Socket;
/*      */ import java.net.URI;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.concurrent.ExecutorService;
/*      */ import java.util.concurrent.Future;
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
/*      */ public class WebSocket
/*      */ {
/*      */   private static final long DEFAULT_CLOSE_DELAY = 10000L;
/*      */   private final WebSocketFactory mWebSocketFactory;
/*      */   private final SocketConnector mSocketConnector;
/*      */   private final StateManager mStateManager;
/*      */   private HandshakeBuilder mHandshakeBuilder;
/*      */   private final ListenerManager mListenerManager;
/*      */   private final PingSender mPingSender;
/*      */   private final PongSender mPongSender;
/* 1113 */   private final Object mThreadsLock = new Object();
/*      */   private WebSocketInputStream mInput;
/*      */   private WebSocketOutputStream mOutput;
/*      */   private ReadingThread mReadingThread;
/*      */   private WritingThread mWritingThread;
/*      */   private Map<String, List<String>> mServerHeaders;
/*      */   private List<WebSocketExtension> mAgreedExtensions;
/*      */   private String mAgreedProtocol;
/*      */   private boolean mExtended;
/*      */   private boolean mAutoFlush = true;
/*      */   private boolean mMissingCloseFrameAllowed = true;
/*      */   private boolean mDirectTextMessage;
/*      */   private int mFrameQueueSize;
/*      */   private int mMaxPayloadSize;
/*      */   private boolean mOnConnectedCalled;
/* 1128 */   private Object mOnConnectedCalledLock = new Object();
/*      */   
/*      */   private boolean mReadingThreadStarted;
/*      */   
/*      */   private boolean mWritingThreadStarted;
/*      */   
/*      */   private boolean mReadingThreadFinished;
/*      */   private boolean mWritingThreadFinished;
/*      */   private WebSocketFrame mServerCloseFrame;
/*      */   private WebSocketFrame mClientCloseFrame;
/*      */   private PerMessageCompressionExtension mPerMessageCompressionExtension;
/*      */   
/*      */   WebSocket(WebSocketFactory factory, boolean secure, String userInfo, String host, String path, SocketConnector connector) {
/* 1141 */     this.mWebSocketFactory = factory;
/* 1142 */     this.mSocketConnector = connector;
/* 1143 */     this.mStateManager = new StateManager();
/* 1144 */     this.mHandshakeBuilder = new HandshakeBuilder(secure, userInfo, host, path);
/* 1145 */     this.mListenerManager = new ListenerManager(this);
/* 1146 */     this.mPingSender = new PingSender(this, new CounterPayloadGenerator());
/* 1147 */     this.mPongSender = new PongSender(this, new CounterPayloadGenerator());
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
/*      */   public WebSocket recreate() throws IOException {
/* 1178 */     return recreate(this.mSocketConnector.getConnectionTimeout());
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
/*      */   public WebSocket recreate(int timeout) throws IOException {
/* 1209 */     if (timeout < 0)
/*      */     {
/* 1211 */       throw new IllegalArgumentException("The given timeout value is negative.");
/*      */     }
/*      */     
/* 1214 */     WebSocket instance = this.mWebSocketFactory.createSocket(getURI(), timeout);
/*      */ 
/*      */     
/* 1217 */     instance.mHandshakeBuilder = new HandshakeBuilder(this.mHandshakeBuilder);
/* 1218 */     instance.setPingInterval(getPingInterval());
/* 1219 */     instance.setPongInterval(getPongInterval());
/* 1220 */     instance.setPingPayloadGenerator(getPingPayloadGenerator());
/* 1221 */     instance.setPongPayloadGenerator(getPongPayloadGenerator());
/* 1222 */     instance.mExtended = this.mExtended;
/* 1223 */     instance.mAutoFlush = this.mAutoFlush;
/* 1224 */     instance.mMissingCloseFrameAllowed = this.mMissingCloseFrameAllowed;
/* 1225 */     instance.mDirectTextMessage = this.mDirectTextMessage;
/* 1226 */     instance.mFrameQueueSize = this.mFrameQueueSize;
/*      */ 
/*      */     
/* 1229 */     List<WebSocketListener> listeners = this.mListenerManager.getListeners();
/* 1230 */     synchronized (listeners) {
/*      */       
/* 1232 */       instance.addListeners(listeners);
/*      */     } 
/*      */     
/* 1235 */     return instance;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void finalize() throws Throwable {
/* 1242 */     if (isInState(WebSocketState.CREATED))
/*      */     {
/*      */       
/* 1245 */       finish();
/*      */     }
/*      */     
/* 1248 */     super.finalize();
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
/*      */   public WebSocketState getState() {
/* 1277 */     synchronized (this.mStateManager) {
/*      */       
/* 1279 */       return this.mStateManager.getState();
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
/*      */   public boolean isOpen() {
/* 1295 */     return isInState(WebSocketState.OPEN);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isInState(WebSocketState state) {
/* 1304 */     synchronized (this.mStateManager) {
/*      */       
/* 1306 */       return (this.mStateManager.getState() == state);
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
/*      */   public WebSocket addProtocol(String protocol) {
/* 1327 */     this.mHandshakeBuilder.addProtocol(protocol);
/*      */     
/* 1329 */     return this;
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
/*      */   public WebSocket removeProtocol(String protocol) {
/* 1346 */     this.mHandshakeBuilder.removeProtocol(protocol);
/*      */     
/* 1348 */     return this;
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
/*      */   public WebSocket clearProtocols() {
/* 1362 */     this.mHandshakeBuilder.clearProtocols();
/*      */     
/* 1364 */     return this;
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
/*      */   public WebSocket addExtension(WebSocketExtension extension) {
/* 1379 */     this.mHandshakeBuilder.addExtension(extension);
/*      */     
/* 1381 */     return this;
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
/*      */   public WebSocket addExtension(String extension) {
/* 1404 */     this.mHandshakeBuilder.addExtension(extension);
/*      */     
/* 1406 */     return this;
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
/*      */   public WebSocket removeExtension(WebSocketExtension extension) {
/* 1423 */     this.mHandshakeBuilder.removeExtension(extension);
/*      */     
/* 1425 */     return this;
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
/*      */   public WebSocket removeExtensions(String name) {
/* 1443 */     this.mHandshakeBuilder.removeExtensions(name);
/*      */     
/* 1445 */     return this;
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
/*      */   public WebSocket clearExtensions() {
/* 1459 */     this.mHandshakeBuilder.clearExtensions();
/*      */     
/* 1461 */     return this;
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
/*      */   public WebSocket addHeader(String name, String value) {
/* 1480 */     this.mHandshakeBuilder.addHeader(name, value);
/*      */     
/* 1482 */     return this;
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
/*      */   public WebSocket removeHeaders(String name) {
/* 1499 */     this.mHandshakeBuilder.removeHeaders(name);
/*      */     
/* 1501 */     return this;
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
/*      */   public WebSocket clearHeaders() {
/* 1515 */     this.mHandshakeBuilder.clearHeaders();
/*      */     
/* 1517 */     return this;
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
/*      */   public WebSocket setUserInfo(String userInfo) {
/* 1533 */     this.mHandshakeBuilder.setUserInfo(userInfo);
/*      */     
/* 1535 */     return this;
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
/*      */   public WebSocket setUserInfo(String id, String password) {
/* 1553 */     this.mHandshakeBuilder.setUserInfo(id, password);
/*      */     
/* 1555 */     return this;
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
/*      */   public WebSocket clearUserInfo() {
/* 1569 */     this.mHandshakeBuilder.clearUserInfo();
/*      */     
/* 1571 */     return this;
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
/*      */   public boolean isExtended() {
/* 1594 */     return this.mExtended;
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
/*      */   public WebSocket setExtended(boolean extended) {
/* 1609 */     this.mExtended = extended;
/*      */     
/* 1611 */     return this;
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
/*      */   public boolean isAutoFlush() {
/* 1627 */     return this.mAutoFlush;
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
/*      */   public WebSocket setAutoFlush(boolean auto) {
/* 1645 */     this.mAutoFlush = auto;
/*      */     
/* 1647 */     return this;
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
/*      */   public boolean isMissingCloseFrameAllowed() {
/* 1671 */     return this.mMissingCloseFrameAllowed;
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
/*      */   public WebSocket setMissingCloseFrameAllowed(boolean allowed) {
/* 1695 */     this.mMissingCloseFrameAllowed = allowed;
/*      */     
/* 1697 */     return this;
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
/*      */   public boolean isDirectTextMessage() {
/* 1722 */     return this.mDirectTextMessage;
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
/*      */   public WebSocket setDirectTextMessage(boolean direct) {
/* 1750 */     this.mDirectTextMessage = direct;
/*      */     
/* 1752 */     return this;
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
/*      */   public WebSocket flush() {
/* 1766 */     synchronized (this.mStateManager) {
/*      */       
/* 1768 */       WebSocketState state = this.mStateManager.getState();
/*      */       
/* 1770 */       if (state != WebSocketState.OPEN && state != WebSocketState.CLOSING)
/*      */       {
/* 1772 */         return this;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1777 */     WritingThread wt = this.mWritingThread;
/*      */ 
/*      */     
/* 1780 */     if (wt != null)
/*      */     {
/*      */       
/* 1783 */       wt.queueFlush();
/*      */     }
/*      */     
/* 1786 */     return this;
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
/*      */   public int getFrameQueueSize() {
/* 1801 */     return this.mFrameQueueSize;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public WebSocket setFrameQueueSize(int size) throws IllegalArgumentException {
/* 1839 */     if (size < 0)
/*      */     {
/* 1841 */       throw new IllegalArgumentException("size must not be negative.");
/*      */     }
/*      */     
/* 1844 */     this.mFrameQueueSize = size;
/*      */     
/* 1846 */     return this;
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
/*      */   public int getMaxPayloadSize() {
/* 1862 */     return this.mMaxPayloadSize;
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
/*      */   public WebSocket setMaxPayloadSize(int size) throws IllegalArgumentException {
/* 1889 */     if (size < 0)
/*      */     {
/* 1891 */       throw new IllegalArgumentException("size must not be negative.");
/*      */     }
/*      */     
/* 1894 */     this.mMaxPayloadSize = size;
/*      */     
/* 1896 */     return this;
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
/*      */   public long getPingInterval() {
/* 1912 */     return this.mPingSender.getInterval();
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
/*      */   public WebSocket setPingInterval(long interval) {
/* 1938 */     this.mPingSender.setInterval(interval);
/*      */     
/* 1940 */     return this;
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
/*      */   public long getPongInterval() {
/* 1956 */     return this.mPongSender.getInterval();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public WebSocket setPongInterval(long interval) {
/* 1999 */     this.mPongSender.setInterval(interval);
/*      */     
/* 2001 */     return this;
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
/*      */   public PayloadGenerator getPingPayloadGenerator() {
/* 2015 */     return this.mPingSender.getPayloadGenerator();
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
/*      */   public WebSocket setPingPayloadGenerator(PayloadGenerator generator) {
/* 2029 */     this.mPingSender.setPayloadGenerator(generator);
/*      */     
/* 2031 */     return this;
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
/*      */   public PayloadGenerator getPongPayloadGenerator() {
/* 2045 */     return this.mPongSender.getPayloadGenerator();
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
/*      */   public WebSocket setPongPayloadGenerator(PayloadGenerator generator) {
/* 2059 */     this.mPongSender.setPayloadGenerator(generator);
/*      */     
/* 2061 */     return this;
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
/*      */   public String getPingSenderName() {
/* 2075 */     return this.mPingSender.getTimerName();
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
/*      */   public WebSocket setPingSenderName(String name) {
/* 2092 */     this.mPingSender.setTimerName(name);
/*      */     
/* 2094 */     return this;
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
/*      */   public String getPongSenderName() {
/* 2108 */     return this.mPongSender.getTimerName();
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
/*      */   public WebSocket setPongSenderName(String name) {
/* 2125 */     this.mPongSender.setTimerName(name);
/*      */     
/* 2127 */     return this;
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
/*      */   public WebSocket addListener(WebSocketListener listener) {
/* 2142 */     this.mListenerManager.addListener(listener);
/*      */     
/* 2144 */     return this;
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
/*      */   public WebSocket addListeners(List<WebSocketListener> listeners) {
/* 2162 */     this.mListenerManager.addListeners(listeners);
/*      */     
/* 2164 */     return this;
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
/*      */   public WebSocket removeListener(WebSocketListener listener) {
/* 2181 */     this.mListenerManager.removeListener(listener);
/*      */     
/* 2183 */     return this;
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
/*      */   public WebSocket removeListeners(List<WebSocketListener> listeners) {
/* 2201 */     this.mListenerManager.removeListeners(listeners);
/*      */     
/* 2203 */     return this;
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
/*      */   public WebSocket clearListeners() {
/* 2217 */     this.mListenerManager.clearListeners();
/*      */     
/* 2219 */     return this;
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
/*      */   public Socket getSocket() {
/* 2242 */     return this.mSocketConnector.getSocket();
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
/*      */   public Socket getConnectedSocket() throws WebSocketException {
/* 2257 */     return this.mSocketConnector.getConnectedSocket();
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
/*      */   public URI getURI() {
/* 2272 */     return this.mHandshakeBuilder.getURI();
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
/*      */   public WebSocket connect() throws WebSocketException {
/*      */     Map<String, List<String>> headers;
/* 2343 */     changeStateOnConnect();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 2351 */       Socket socket = this.mSocketConnector.connect();
/*      */ 
/*      */       
/* 2354 */       headers = shakeHands(socket);
/*      */     }
/* 2356 */     catch (WebSocketException e) {
/*      */ 
/*      */       
/* 2359 */       this.mSocketConnector.closeSilently();
/*      */ 
/*      */       
/* 2362 */       this.mStateManager.setState(WebSocketState.CLOSED);
/*      */ 
/*      */       
/* 2365 */       this.mListenerManager.callOnStateChanged(WebSocketState.CLOSED);
/*      */ 
/*      */       
/* 2368 */       throw e;
/*      */     } 
/*      */ 
/*      */     
/* 2372 */     this.mServerHeaders = headers;
/*      */ 
/*      */     
/* 2375 */     this.mPerMessageCompressionExtension = findAgreedPerMessageCompressionExtension();
/*      */ 
/*      */     
/* 2378 */     this.mStateManager.setState(WebSocketState.OPEN);
/*      */ 
/*      */     
/* 2381 */     this.mListenerManager.callOnStateChanged(WebSocketState.OPEN);
/*      */ 
/*      */     
/* 2384 */     startThreads();
/*      */     
/* 2386 */     return this;
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
/*      */   public Future<WebSocket> connect(ExecutorService executorService) {
/* 2418 */     return executorService.submit(connectable());
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
/*      */   public Callable<WebSocket> connectable() {
/* 2437 */     return new Connectable(this);
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
/*      */   public WebSocket connectAsynchronously() {
/* 2454 */     Thread thread = new ConnectThread(this);
/*      */ 
/*      */     
/* 2457 */     ListenerManager lm = this.mListenerManager;
/*      */     
/* 2459 */     if (lm != null)
/*      */     {
/* 2461 */       lm.callOnThreadCreated(ThreadType.CONNECT_THREAD, thread);
/*      */     }
/*      */     
/* 2464 */     thread.start();
/*      */     
/* 2466 */     return this;
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
/*      */   public WebSocket disconnect() {
/* 2483 */     return disconnect(1000, null);
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
/*      */   public WebSocket disconnect(int closeCode) {
/* 2507 */     return disconnect(closeCode, null);
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
/*      */   public WebSocket disconnect(String reason) {
/* 2535 */     return disconnect(1000, reason);
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
/*      */ 
/*      */   
/*      */   public WebSocket disconnect(int closeCode, String reason) {
/* 2572 */     return disconnect(closeCode, reason, 10000L);
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
/*      */   public WebSocket disconnect(int closeCode, String reason, long closeDelay) {
/* 2620 */     synchronized (this.mStateManager) {
/*      */       
/* 2622 */       switch (this.mStateManager.getState()) {
/*      */         
/*      */         case CREATED:
/* 2625 */           finishAsynchronously();
/* 2626 */           return this;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case OPEN:
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/* 2641 */           return this;
/*      */       } 
/*      */ 
/*      */       
/* 2645 */       this.mStateManager.changeToClosing(StateManager.CloseInitiator.CLIENT);
/*      */ 
/*      */       
/* 2648 */       WebSocketFrame frame = WebSocketFrame.createCloseFrame(closeCode, reason);
/*      */ 
/*      */       
/* 2651 */       sendFrame(frame);
/*      */     } 
/*      */ 
/*      */     
/* 2655 */     this.mListenerManager.callOnStateChanged(WebSocketState.CLOSING);
/*      */ 
/*      */     
/* 2658 */     if (closeDelay < 0L)
/*      */     {
/*      */       
/* 2661 */       closeDelay = 10000L;
/*      */     }
/*      */ 
/*      */     
/* 2665 */     stopThreads(closeDelay);
/*      */     
/* 2667 */     return this;
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
/*      */   public List<WebSocketExtension> getAgreedExtensions() {
/* 2684 */     return this.mAgreedExtensions;
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
/*      */   public String getAgreedProtocol() {
/* 2701 */     return this.mAgreedProtocol;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public WebSocket sendFrame(WebSocketFrame frame) {
/* 2743 */     if (frame == null)
/*      */     {
/* 2745 */       return this;
/*      */     }
/*      */     
/* 2748 */     synchronized (this.mStateManager) {
/*      */       
/* 2750 */       WebSocketState state = this.mStateManager.getState();
/*      */       
/* 2752 */       if (state != WebSocketState.OPEN && state != WebSocketState.CLOSING)
/*      */       {
/* 2754 */         return this;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2761 */     WritingThread wt = this.mWritingThread;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2769 */     if (wt == null)
/*      */     {
/*      */       
/* 2772 */       return this;
/*      */     }
/*      */ 
/*      */     
/* 2776 */     List<WebSocketFrame> frames = splitIfNecessary(frame);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2782 */     if (frames == null) {
/*      */ 
/*      */       
/* 2785 */       wt.queueFrame(frame);
/*      */     }
/*      */     else {
/*      */       
/* 2789 */       for (WebSocketFrame f : frames)
/*      */       {
/*      */         
/* 2792 */         wt.queueFrame(f);
/*      */       }
/*      */     } 
/*      */     
/* 2796 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private List<WebSocketFrame> splitIfNecessary(WebSocketFrame frame) {
/* 2802 */     return WebSocketFrame.splitIfNecessary(frame, this.mMaxPayloadSize, this.mPerMessageCompressionExtension);
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
/*      */   public WebSocket sendContinuation() {
/* 2827 */     return sendFrame(WebSocketFrame.createContinuationFrame());
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
/*      */   public WebSocket sendContinuation(boolean fin) {
/* 2850 */     return sendFrame(WebSocketFrame.createContinuationFrame().setFin(fin));
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
/*      */   public WebSocket sendContinuation(String payload) {
/* 2879 */     return sendFrame(WebSocketFrame.createContinuationFrame(payload));
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
/*      */   public WebSocket sendContinuation(String payload, boolean fin) {
/* 2905 */     return sendFrame(WebSocketFrame.createContinuationFrame(payload).setFin(fin));
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
/*      */   public WebSocket sendContinuation(byte[] payload) {
/* 2934 */     return sendFrame(WebSocketFrame.createContinuationFrame(payload));
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
/*      */   public WebSocket sendContinuation(byte[] payload, boolean fin) {
/* 2960 */     return sendFrame(WebSocketFrame.createContinuationFrame(payload).setFin(fin));
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
/*      */   public WebSocket sendText(String message) {
/* 2988 */     return sendFrame(WebSocketFrame.createTextFrame(message));
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
/*      */   public WebSocket sendText(String payload, boolean fin) {
/* 3014 */     return sendFrame(WebSocketFrame.createTextFrame(payload).setFin(fin));
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
/*      */   public WebSocket sendBinary(byte[] message) {
/* 3042 */     return sendFrame(WebSocketFrame.createBinaryFrame(message));
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
/*      */   public WebSocket sendBinary(byte[] payload, boolean fin) {
/* 3068 */     return sendFrame(WebSocketFrame.createBinaryFrame(payload).setFin(fin));
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
/*      */   public WebSocket sendClose() {
/* 3086 */     return sendFrame(WebSocketFrame.createCloseFrame());
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
/*      */   public WebSocket sendClose(int closeCode) {
/* 3110 */     return sendFrame(WebSocketFrame.createCloseFrame(closeCode));
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
/*      */   public WebSocket sendClose(int closeCode, String reason) {
/* 3140 */     return sendFrame(WebSocketFrame.createCloseFrame(closeCode, reason));
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
/*      */   public WebSocket sendPing() {
/* 3158 */     return sendFrame(WebSocketFrame.createPingFrame());
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
/*      */   public WebSocket sendPing(byte[] payload) {
/* 3183 */     return sendFrame(WebSocketFrame.createPingFrame(payload));
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
/*      */   public WebSocket sendPing(String payload) {
/* 3208 */     return sendFrame(WebSocketFrame.createPingFrame(payload));
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
/*      */   public WebSocket sendPong() {
/* 3226 */     return sendFrame(WebSocketFrame.createPongFrame());
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
/*      */   public WebSocket sendPong(byte[] payload) {
/* 3251 */     return sendFrame(WebSocketFrame.createPongFrame(payload));
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
/*      */   public WebSocket sendPong(String payload) {
/* 3276 */     return sendFrame(WebSocketFrame.createPongFrame(payload));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void changeStateOnConnect() throws WebSocketException {
/* 3282 */     synchronized (this.mStateManager) {
/*      */ 
/*      */       
/* 3285 */       if (this.mStateManager.getState() != WebSocketState.CREATED)
/*      */       {
/* 3287 */         throw new WebSocketException(WebSocketError.NOT_IN_CREATED_STATE, "The current state of the WebSocket is not CREATED.");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3293 */       this.mStateManager.setState(WebSocketState.CONNECTING);
/*      */     } 
/*      */ 
/*      */     
/* 3297 */     this.mListenerManager.callOnStateChanged(WebSocketState.CONNECTING);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<String, List<String>> shakeHands(Socket socket) throws WebSocketException {
/* 3307 */     WebSocketInputStream input = openInputStream(socket);
/*      */ 
/*      */     
/* 3310 */     WebSocketOutputStream output = openOutputStream(socket);
/*      */ 
/*      */     
/* 3313 */     String key = generateWebSocketKey();
/*      */ 
/*      */     
/* 3316 */     writeHandshake(output, key);
/*      */ 
/*      */     
/* 3319 */     Map<String, List<String>> headers = readHandshake(input, key);
/*      */ 
/*      */ 
/*      */     
/* 3323 */     this.mInput = input;
/* 3324 */     this.mOutput = output;
/*      */ 
/*      */     
/* 3327 */     return headers;
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
/*      */   private WebSocketInputStream openInputStream(Socket socket) throws WebSocketException {
/*      */     try {
/* 3341 */       return new WebSocketInputStream(new BufferedInputStream(socket
/* 3342 */             .getInputStream()));
/*      */     }
/* 3344 */     catch (IOException e) {
/*      */ 
/*      */       
/* 3347 */       throw new WebSocketException(WebSocketError.SOCKET_INPUT_STREAM_FAILURE, "Failed to get the input stream of the raw socket: " + e
/*      */           
/* 3349 */           .getMessage(), e);
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
/*      */   private WebSocketOutputStream openOutputStream(Socket socket) throws WebSocketException {
/*      */     try {
/* 3364 */       return new WebSocketOutputStream(new BufferedOutputStream(socket
/* 3365 */             .getOutputStream()));
/*      */     }
/* 3367 */     catch (IOException e) {
/*      */ 
/*      */       
/* 3370 */       throw new WebSocketException(WebSocketError.SOCKET_OUTPUT_STREAM_FAILURE, "Failed to get the output stream from the raw socket: " + e
/*      */           
/* 3372 */           .getMessage(), e);
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
/*      */   private static String generateWebSocketKey() {
/* 3395 */     byte[] data = new byte[16];
/*      */ 
/*      */     
/* 3398 */     Misc.nextBytes(data);
/*      */ 
/*      */     
/* 3401 */     return Base64.encode(data);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeHandshake(WebSocketOutputStream output, String key) throws WebSocketException {
/* 3411 */     this.mHandshakeBuilder.setKey(key);
/* 3412 */     String requestLine = this.mHandshakeBuilder.buildRequestLine();
/* 3413 */     List<String[]> headers = this.mHandshakeBuilder.buildHeaders();
/* 3414 */     String handshake = HandshakeBuilder.build(requestLine, headers);
/*      */ 
/*      */     
/* 3417 */     this.mListenerManager.callOnSendingHandshake(requestLine, headers);
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/* 3422 */       output.write(handshake);
/* 3423 */       output.flush();
/*      */     }
/* 3425 */     catch (IOException e) {
/*      */ 
/*      */       
/* 3428 */       throw new WebSocketException(WebSocketError.OPENING_HAHDSHAKE_REQUEST_FAILURE, "Failed to send an opening handshake request to the server: " + e
/*      */           
/* 3430 */           .getMessage(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<String, List<String>> readHandshake(WebSocketInputStream input, String key) throws WebSocketException {
/* 3440 */     return (new HandshakeReader(this)).readHandshake(input, key);
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
/*      */   private void startThreads() {
/* 3457 */     ReadingThread readingThread = new ReadingThread(this);
/* 3458 */     WritingThread writingThread = new WritingThread(this);
/*      */     
/* 3460 */     synchronized (this.mThreadsLock) {
/*      */       
/* 3462 */       this.mReadingThread = readingThread;
/* 3463 */       this.mWritingThread = writingThread;
/*      */     } 
/*      */ 
/*      */     
/* 3467 */     readingThread.callOnThreadCreated();
/* 3468 */     writingThread.callOnThreadCreated();
/*      */     
/* 3470 */     readingThread.start();
/* 3471 */     writingThread.start();
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
/*      */   private void stopThreads(long closeDelay) {
/*      */     ReadingThread readingThread;
/*      */     WritingThread writingThread;
/* 3491 */     synchronized (this.mThreadsLock) {
/*      */       
/* 3493 */       readingThread = this.mReadingThread;
/* 3494 */       writingThread = this.mWritingThread;
/*      */       
/* 3496 */       this.mReadingThread = null;
/* 3497 */       this.mWritingThread = null;
/*      */     } 
/*      */     
/* 3500 */     if (readingThread != null)
/*      */     {
/* 3502 */       readingThread.requestStop(closeDelay);
/*      */     }
/*      */     
/* 3505 */     if (writingThread != null)
/*      */     {
/* 3507 */       writingThread.requestStop();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   WebSocketInputStream getInput() {
/* 3517 */     return this.mInput;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   WebSocketOutputStream getOutput() {
/* 3526 */     return this.mOutput;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   StateManager getStateManager() {
/* 3535 */     return this.mStateManager;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   ListenerManager getListenerManager() {
/* 3544 */     return this.mListenerManager;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   HandshakeBuilder getHandshakeBuilder() {
/* 3553 */     return this.mHandshakeBuilder;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setAgreedExtensions(List<WebSocketExtension> extensions) {
/* 3562 */     this.mAgreedExtensions = extensions;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setAgreedProtocol(String protocol) {
/* 3571 */     this.mAgreedProtocol = protocol;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void onReadingThreadStarted() {
/* 3580 */     boolean bothStarted = false;
/*      */     
/* 3582 */     synchronized (this.mThreadsLock) {
/*      */       
/* 3584 */       this.mReadingThreadStarted = true;
/*      */       
/* 3586 */       if (this.mWritingThreadStarted)
/*      */       {
/*      */         
/* 3589 */         bothStarted = true;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 3594 */     callOnConnectedIfNotYet();
/*      */ 
/*      */     
/* 3597 */     if (bothStarted)
/*      */     {
/* 3599 */       onThreadsStarted();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void onWritingThreadStarted() {
/* 3609 */     boolean bothStarted = false;
/*      */     
/* 3611 */     synchronized (this.mThreadsLock) {
/*      */       
/* 3613 */       this.mWritingThreadStarted = true;
/*      */       
/* 3615 */       if (this.mReadingThreadStarted)
/*      */       {
/*      */         
/* 3618 */         bothStarted = true;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 3623 */     callOnConnectedIfNotYet();
/*      */ 
/*      */     
/* 3626 */     if (bothStarted)
/*      */     {
/* 3628 */       onThreadsStarted();
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
/*      */   private void callOnConnectedIfNotYet() {
/* 3640 */     synchronized (this.mOnConnectedCalledLock) {
/*      */ 
/*      */       
/* 3643 */       if (this.mOnConnectedCalled) {
/*      */         return;
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 3649 */       this.mOnConnectedCalled = true;
/*      */     } 
/*      */ 
/*      */     
/* 3653 */     this.mListenerManager.callOnConnected(this.mServerHeaders);
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
/*      */   private void onThreadsStarted() {
/* 3666 */     this.mPingSender.start();
/*      */ 
/*      */     
/* 3669 */     this.mPongSender.start();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void onReadingThreadFinished(WebSocketFrame closeFrame) {
/* 3678 */     synchronized (this.mThreadsLock) {
/*      */       
/* 3680 */       this.mReadingThreadFinished = true;
/* 3681 */       this.mServerCloseFrame = closeFrame;
/*      */       
/* 3683 */       if (!this.mWritingThreadFinished) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3691 */     onThreadsFinished();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void onWritingThreadFinished(WebSocketFrame closeFrame) {
/* 3700 */     synchronized (this.mThreadsLock) {
/*      */       
/* 3702 */       this.mWritingThreadFinished = true;
/* 3703 */       this.mClientCloseFrame = closeFrame;
/*      */       
/* 3705 */       if (!this.mReadingThreadFinished) {
/*      */         return;
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3713 */     onThreadsFinished();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void onThreadsFinished() {
/* 3724 */     finish();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void finish() {
/* 3731 */     this.mPingSender.stop();
/* 3732 */     this.mPongSender.stop();
/*      */ 
/*      */     
/* 3735 */     Socket socket = this.mSocketConnector.getSocket();
/* 3736 */     if (socket != null) {
/*      */       
/*      */       try {
/*      */         
/* 3740 */         socket.close();
/*      */       }
/* 3742 */       catch (Throwable throwable) {}
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3748 */     synchronized (this.mStateManager) {
/*      */ 
/*      */       
/* 3751 */       this.mStateManager.setState(WebSocketState.CLOSED);
/*      */     } 
/*      */ 
/*      */     
/* 3755 */     this.mListenerManager.callOnStateChanged(WebSocketState.CLOSED);
/*      */ 
/*      */     
/* 3758 */     this.mListenerManager.callOnDisconnected(this.mServerCloseFrame, this.mClientCloseFrame, this.mStateManager
/* 3759 */         .getClosedByServer());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void finishAsynchronously() {
/* 3768 */     WebSocketThread thread = new FinishThread(this);
/*      */ 
/*      */     
/* 3771 */     thread.callOnThreadCreated();
/*      */     
/* 3773 */     thread.start();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private PerMessageCompressionExtension findAgreedPerMessageCompressionExtension() {
/* 3782 */     if (this.mAgreedExtensions == null)
/*      */     {
/* 3784 */       return null;
/*      */     }
/*      */     
/* 3787 */     for (WebSocketExtension extension : this.mAgreedExtensions) {
/*      */       
/* 3789 */       if (extension instanceof PerMessageCompressionExtension)
/*      */       {
/* 3791 */         return (PerMessageCompressionExtension)extension;
/*      */       }
/*      */     } 
/*      */     
/* 3795 */     return null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   PerMessageCompressionExtension getPerMessageCompressionExtension() {
/* 3806 */     return this.mPerMessageCompressionExtension;
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\WebSocket.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */