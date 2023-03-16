package com.neovisionaries.ws.client;

import java.util.List;
import java.util.Map;

public interface WebSocketListener {
  void onStateChanged(WebSocket paramWebSocket, WebSocketState paramWebSocketState) throws Exception;
  
  void onConnected(WebSocket paramWebSocket, Map<String, List<String>> paramMap) throws Exception;
  
  void onConnectError(WebSocket paramWebSocket, WebSocketException paramWebSocketException) throws Exception;
  
  void onDisconnected(WebSocket paramWebSocket, WebSocketFrame paramWebSocketFrame1, WebSocketFrame paramWebSocketFrame2, boolean paramBoolean) throws Exception;
  
  void onFrame(WebSocket paramWebSocket, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onContinuationFrame(WebSocket paramWebSocket, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onTextFrame(WebSocket paramWebSocket, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onBinaryFrame(WebSocket paramWebSocket, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onCloseFrame(WebSocket paramWebSocket, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onPingFrame(WebSocket paramWebSocket, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onPongFrame(WebSocket paramWebSocket, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onTextMessage(WebSocket paramWebSocket, String paramString) throws Exception;
  
  void onTextMessage(WebSocket paramWebSocket, byte[] paramArrayOfbyte) throws Exception;
  
  void onBinaryMessage(WebSocket paramWebSocket, byte[] paramArrayOfbyte) throws Exception;
  
  void onSendingFrame(WebSocket paramWebSocket, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onFrameSent(WebSocket paramWebSocket, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onFrameUnsent(WebSocket paramWebSocket, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onThreadCreated(WebSocket paramWebSocket, ThreadType paramThreadType, Thread paramThread) throws Exception;
  
  void onThreadStarted(WebSocket paramWebSocket, ThreadType paramThreadType, Thread paramThread) throws Exception;
  
  void onThreadStopping(WebSocket paramWebSocket, ThreadType paramThreadType, Thread paramThread) throws Exception;
  
  void onError(WebSocket paramWebSocket, WebSocketException paramWebSocketException) throws Exception;
  
  void onFrameError(WebSocket paramWebSocket, WebSocketException paramWebSocketException, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onMessageError(WebSocket paramWebSocket, WebSocketException paramWebSocketException, List<WebSocketFrame> paramList) throws Exception;
  
  void onMessageDecompressionError(WebSocket paramWebSocket, WebSocketException paramWebSocketException, byte[] paramArrayOfbyte) throws Exception;
  
  void onTextMessageError(WebSocket paramWebSocket, WebSocketException paramWebSocketException, byte[] paramArrayOfbyte) throws Exception;
  
  void onSendError(WebSocket paramWebSocket, WebSocketException paramWebSocketException, WebSocketFrame paramWebSocketFrame) throws Exception;
  
  void onUnexpectedError(WebSocket paramWebSocket, WebSocketException paramWebSocketException) throws Exception;
  
  void handleCallbackError(WebSocket paramWebSocket, Throwable paramThrowable) throws Exception;
  
  void onSendingHandshake(WebSocket paramWebSocket, String paramString, List<String[]> paramList) throws Exception;
}


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\WebSocketListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */