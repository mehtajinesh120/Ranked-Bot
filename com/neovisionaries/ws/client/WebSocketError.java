/*     */ package com.neovisionaries.ws.client;
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
/*     */ public enum WebSocketError
/*     */ {
/*  35 */   NOT_IN_CREATED_STATE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  41 */   SOCKET_INPUT_STREAM_FAILURE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   SOCKET_OUTPUT_STREAM_FAILURE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   OPENING_HAHDSHAKE_REQUEST_FAILURE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  59 */   OPENING_HANDSHAKE_RESPONSE_FAILURE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  65 */   STATUS_LINE_EMPTY,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  71 */   STATUS_LINE_BAD_FORMAT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   NOT_SWITCHING_PROTOCOLS,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  83 */   HTTP_HEADER_FAILURE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   NO_UPGRADE_HEADER,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  95 */   NO_WEBSOCKET_IN_UPGRADE_HEADER,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   NO_CONNECTION_HEADER,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 107 */   NO_UPGRADE_IN_CONNECTION_HEADER,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 113 */   NO_SEC_WEBSOCKET_ACCEPT_HEADER,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 119 */   UNEXPECTED_SEC_WEBSOCKET_ACCEPT_HEADER,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 125 */   EXTENSION_PARSE_ERROR,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 131 */   UNSUPPORTED_EXTENSION,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 140 */   EXTENSIONS_CONFLICT,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 146 */   UNSUPPORTED_PROTOCOL,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 152 */   INSUFFICENT_DATA,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 158 */   INVALID_PAYLOAD_LENGTH,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 164 */   TOO_LONG_PAYLOAD,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 170 */   INSUFFICIENT_MEMORY_FOR_PAYLOAD,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 176 */   INTERRUPTED_IN_READING,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 182 */   IO_ERROR_IN_READING,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 188 */   IO_ERROR_IN_WRITING,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 194 */   FLUSH_ERROR,
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
/* 220 */   NON_ZERO_RESERVED_BITS,
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
/* 244 */   UNEXPECTED_RESERVED_BIT,
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
/* 259 */   FRAME_MASKED,
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
/* 270 */   UNKNOWN_OPCODE,
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
/* 285 */   FRAGMENTED_CONTROL_FRAME,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 291 */   UNEXPECTED_CONTINUATION_FRAME,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 297 */   CONTINUATION_NOT_CLOSED,
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
/* 312 */   TOO_LONG_CONTROL_FRAME_PAYLOAD,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 318 */   MESSAGE_CONSTRUCTION_ERROR,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 324 */   TEXT_MESSAGE_CONSTRUCTION_ERROR,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 331 */   UNEXPECTED_ERROR_IN_READING_THREAD,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 338 */   UNEXPECTED_ERROR_IN_WRITING_THREAD,
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
/* 352 */   PERMESSAGE_DEFLATE_UNSUPPORTED_PARAMETER,
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
/* 368 */   PERMESSAGE_DEFLATE_INVALID_MAX_WINDOW_BITS,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 376 */   COMPRESSION_ERROR,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 384 */   DECOMPRESSION_ERROR,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 393 */   SOCKET_CONNECT_ERROR,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 401 */   PROXY_HANDSHAKE_ERROR,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 409 */   SOCKET_OVERLAY_ERROR,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 417 */   SSL_HANDSHAKE_ERROR,
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
/* 434 */   NO_MORE_FRAME,
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
/* 453 */   HOSTNAME_UNVERIFIED;
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\WebSocketError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */