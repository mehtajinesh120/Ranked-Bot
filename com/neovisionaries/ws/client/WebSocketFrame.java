/*      */ package com.neovisionaries.ws.client;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class WebSocketFrame
/*      */ {
/*      */   private boolean mFin;
/*      */   private boolean mRsv1;
/*      */   private boolean mRsv2;
/*      */   private boolean mRsv3;
/*      */   private int mOpcode;
/*      */   private boolean mMask;
/*      */   private byte[] mPayload;
/*      */   
/*      */   public boolean getFin() {
/*   55 */     return this.mFin;
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
/*      */   public WebSocketFrame setFin(boolean fin) {
/*   70 */     this.mFin = fin;
/*      */     
/*   72 */     return this;
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
/*      */   public boolean getRsv1() {
/*   84 */     return this.mRsv1;
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
/*      */   public WebSocketFrame setRsv1(boolean rsv1) {
/*   99 */     this.mRsv1 = rsv1;
/*      */     
/*  101 */     return this;
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
/*      */   public boolean getRsv2() {
/*  113 */     return this.mRsv2;
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
/*      */   public WebSocketFrame setRsv2(boolean rsv2) {
/*  128 */     this.mRsv2 = rsv2;
/*      */     
/*  130 */     return this;
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
/*      */   public boolean getRsv3() {
/*  142 */     return this.mRsv3;
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
/*      */   public WebSocketFrame setRsv3(boolean rsv3) {
/*  157 */     this.mRsv3 = rsv3;
/*      */     
/*  159 */     return this;
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
/*      */   public int getOpcode() {
/*  217 */     return this.mOpcode;
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
/*      */   public WebSocketFrame setOpcode(int opcode) {
/*  234 */     this.mOpcode = opcode;
/*      */     
/*  236 */     return this;
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
/*      */   public boolean isContinuationFrame() {
/*  254 */     return (this.mOpcode == 0);
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
/*      */   public boolean isTextFrame() {
/*  272 */     return (this.mOpcode == 1);
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
/*      */   public boolean isBinaryFrame() {
/*  290 */     return (this.mOpcode == 2);
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
/*      */   public boolean isCloseFrame() {
/*  308 */     return (this.mOpcode == 8);
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
/*      */   public boolean isPingFrame() {
/*  326 */     return (this.mOpcode == 9);
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
/*      */   public boolean isPongFrame() {
/*  344 */     return (this.mOpcode == 10);
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
/*      */   public boolean isDataFrame() {
/*  362 */     return (1 <= this.mOpcode && this.mOpcode <= 7);
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
/*      */   public boolean isControlFrame() {
/*  380 */     return (8 <= this.mOpcode && this.mOpcode <= 15);
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
/*      */   boolean getMask() {
/*  392 */     return this.mMask;
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
/*      */   WebSocketFrame setMask(boolean mask) {
/*  407 */     this.mMask = mask;
/*      */     
/*  409 */     return this;
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
/*      */   public boolean hasPayload() {
/*  421 */     return (this.mPayload != null);
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
/*      */   public int getPayloadLength() {
/*  433 */     if (this.mPayload == null)
/*      */     {
/*  435 */       return 0;
/*      */     }
/*      */     
/*  438 */     return this.mPayload.length;
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
/*      */   public byte[] getPayload() {
/*  450 */     return this.mPayload;
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
/*      */   public String getPayloadText() {
/*  463 */     if (this.mPayload == null)
/*      */     {
/*  465 */       return null;
/*      */     }
/*      */     
/*  468 */     return Misc.toStringUTF8(this.mPayload);
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
/*      */   public WebSocketFrame setPayload(byte[] payload) {
/*  490 */     if (payload != null && payload.length == 0)
/*      */     {
/*  492 */       payload = null;
/*      */     }
/*      */     
/*  495 */     this.mPayload = payload;
/*      */     
/*  497 */     return this;
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
/*      */   public WebSocketFrame setPayload(String payload) {
/*  520 */     if (payload == null || payload.length() == 0)
/*      */     {
/*  522 */       return setPayload((byte[])null);
/*      */     }
/*      */     
/*  525 */     return setPayload(Misc.getBytesUTF8(payload));
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
/*      */   public WebSocketFrame setCloseFramePayload(int closeCode, String reason) {
/*  563 */     byte[] encodedCloseCode = { (byte)(closeCode >> 8 & 0xFF), (byte)(closeCode & 0xFF) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  569 */     if (reason == null || reason.length() == 0)
/*      */     {
/*      */       
/*  572 */       return setPayload(encodedCloseCode);
/*      */     }
/*      */ 
/*      */     
/*  576 */     byte[] encodedReason = Misc.getBytesUTF8(reason);
/*      */ 
/*      */     
/*  579 */     byte[] payload = new byte[2 + encodedReason.length];
/*  580 */     System.arraycopy(encodedCloseCode, 0, payload, 0, 2);
/*  581 */     System.arraycopy(encodedReason, 0, payload, 2, encodedReason.length);
/*      */ 
/*      */     
/*  584 */     return setPayload(payload);
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
/*      */   public int getCloseCode() {
/*  611 */     if (this.mPayload == null || this.mPayload.length < 2)
/*      */     {
/*  613 */       return 1005;
/*      */     }
/*      */ 
/*      */     
/*  617 */     int closeCode = (this.mPayload[0] & 0xFF) << 8 | this.mPayload[1] & 0xFF;
/*      */     
/*  619 */     return closeCode;
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
/*      */   public String getCloseReason() {
/*  641 */     if (this.mPayload == null || this.mPayload.length < 3)
/*      */     {
/*  643 */       return null;
/*      */     }
/*      */     
/*  646 */     return Misc.toStringUTF8(this.mPayload, 2, this.mPayload.length - 2);
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
/*      */   public String toString() {
/*  659 */     StringBuilder builder = (new StringBuilder()).append("WebSocketFrame(FIN=").append(this.mFin ? "1" : "0").append(",RSV1=").append(this.mRsv1 ? "1" : "0").append(",RSV2=").append(this.mRsv2 ? "1" : "0").append(",RSV3=").append(this.mRsv3 ? "1" : "0").append(",Opcode=").append(Misc.toOpcodeName(this.mOpcode)).append(",Length=").append(getPayloadLength());
/*      */     
/*  661 */     switch (this.mOpcode) {
/*      */       
/*      */       case 1:
/*  664 */         appendPayloadText(builder);
/*      */         break;
/*      */       
/*      */       case 2:
/*  668 */         appendPayloadBinary(builder);
/*      */         break;
/*      */       
/*      */       case 8:
/*  672 */         appendPayloadClose(builder);
/*      */         break;
/*      */     } 
/*      */     
/*  676 */     return builder.append(")").toString();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean appendPayloadCommon(StringBuilder builder) {
/*  682 */     builder.append(",Payload=");
/*      */     
/*  684 */     if (this.mPayload == null) {
/*      */       
/*  686 */       builder.append("null");
/*      */ 
/*      */       
/*  689 */       return true;
/*      */     } 
/*      */     
/*  692 */     if (this.mRsv1) {
/*      */ 
/*      */ 
/*      */       
/*  696 */       builder.append("compressed");
/*      */ 
/*      */       
/*  699 */       return true;
/*      */     } 
/*      */ 
/*      */     
/*  703 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void appendPayloadText(StringBuilder builder) {
/*  709 */     if (appendPayloadCommon(builder)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  715 */     builder.append("\"");
/*  716 */     builder.append(getPayloadText());
/*  717 */     builder.append("\"");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void appendPayloadClose(StringBuilder builder) {
/*  723 */     builder
/*  724 */       .append(",CloseCode=").append(getCloseCode())
/*  725 */       .append(",Reason=");
/*      */     
/*  727 */     String reason = getCloseReason();
/*      */     
/*  729 */     if (reason == null) {
/*      */       
/*  731 */       builder.append("null");
/*      */     }
/*      */     else {
/*      */       
/*  735 */       builder.append("\"").append(reason).append("\"");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void appendPayloadBinary(StringBuilder builder) {
/*  742 */     if (appendPayloadCommon(builder)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  748 */     for (int i = 0; i < this.mPayload.length; i++) {
/*      */       
/*  750 */       builder.append(String.format("%02X ", new Object[] { Integer.valueOf(0xFF & this.mPayload[i]) }));
/*      */     } 
/*      */     
/*  753 */     if (this.mPayload.length != 0)
/*      */     {
/*      */       
/*  756 */       builder.setLength(builder.length() - 1);
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
/*      */   public static WebSocketFrame createContinuationFrame() {
/*  772 */     return (new WebSocketFrame())
/*  773 */       .setOpcode(0);
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
/*      */   public static WebSocketFrame createContinuationFrame(byte[] payload) {
/*  791 */     return createContinuationFrame().setPayload(payload);
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
/*      */   public static WebSocketFrame createContinuationFrame(String payload) {
/*  809 */     return createContinuationFrame().setPayload(payload);
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
/*      */   public static WebSocketFrame createTextFrame(String payload) {
/*  826 */     return (new WebSocketFrame())
/*  827 */       .setFin(true)
/*  828 */       .setOpcode(1)
/*  829 */       .setPayload(payload);
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
/*      */   public static WebSocketFrame createBinaryFrame(byte[] payload) {
/*  846 */     return (new WebSocketFrame())
/*  847 */       .setFin(true)
/*  848 */       .setOpcode(2)
/*  849 */       .setPayload(payload);
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
/*      */   public static WebSocketFrame createCloseFrame() {
/*  863 */     return (new WebSocketFrame())
/*  864 */       .setFin(true)
/*  865 */       .setOpcode(8);
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
/*      */   public static WebSocketFrame createCloseFrame(int closeCode) {
/*  884 */     return createCloseFrame().setCloseFramePayload(closeCode, null);
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
/*      */   public static WebSocketFrame createCloseFrame(int closeCode, String reason) {
/*  909 */     return createCloseFrame().setCloseFramePayload(closeCode, reason);
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
/*      */   public static WebSocketFrame createPingFrame() {
/*  923 */     return (new WebSocketFrame())
/*  924 */       .setFin(true)
/*  925 */       .setOpcode(9);
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
/*      */   public static WebSocketFrame createPingFrame(byte[] payload) {
/*  945 */     return createPingFrame().setPayload(payload);
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
/*      */   public static WebSocketFrame createPingFrame(String payload) {
/*  965 */     return createPingFrame().setPayload(payload);
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
/*      */   public static WebSocketFrame createPongFrame() {
/*  979 */     return (new WebSocketFrame())
/*  980 */       .setFin(true)
/*  981 */       .setOpcode(10);
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
/*      */   public static WebSocketFrame createPongFrame(byte[] payload) {
/* 1001 */     return createPongFrame().setPayload(payload);
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
/*      */   public static WebSocketFrame createPongFrame(String payload) {
/* 1021 */     return createPongFrame().setPayload(payload);
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
/*      */   static byte[] mask(byte[] maskingKey, byte[] payload) {
/* 1048 */     if (maskingKey == null || maskingKey.length < 4 || payload == null)
/*      */     {
/* 1050 */       return payload;
/*      */     }
/*      */     
/* 1053 */     for (int i = 0; i < payload.length; i++)
/*      */     {
/* 1055 */       payload[i] = (byte)(payload[i] ^ maskingKey[i % 4]);
/*      */     }
/*      */     
/* 1058 */     return payload;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static WebSocketFrame compressFrame(WebSocketFrame frame, PerMessageCompressionExtension pmce) {
/* 1065 */     if (pmce == null)
/*      */     {
/*      */       
/* 1068 */       return frame;
/*      */     }
/*      */ 
/*      */     
/* 1072 */     if (!frame.isTextFrame() && 
/* 1073 */       !frame.isBinaryFrame())
/*      */     {
/*      */       
/* 1076 */       return frame;
/*      */     }
/*      */ 
/*      */     
/* 1080 */     if (!frame.getFin())
/*      */     {
/*      */ 
/*      */ 
/*      */       
/* 1085 */       return frame;
/*      */     }
/*      */ 
/*      */     
/* 1089 */     if (frame.getRsv1())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1095 */       return frame;
/*      */     }
/*      */ 
/*      */     
/* 1099 */     byte[] payload = frame.getPayload();
/*      */ 
/*      */     
/* 1102 */     if (payload == null || payload.length == 0)
/*      */     {
/*      */       
/* 1105 */       return frame;
/*      */     }
/*      */ 
/*      */     
/* 1109 */     byte[] compressed = compress(payload, pmce);
/*      */ 
/*      */ 
/*      */     
/* 1113 */     if (payload.length <= compressed.length)
/*      */     {
/*      */       
/* 1116 */       return frame;
/*      */     }
/*      */ 
/*      */     
/* 1120 */     frame.setPayload(compressed);
/*      */ 
/*      */     
/* 1123 */     frame.setRsv1(true);
/*      */     
/* 1125 */     return frame;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static byte[] compress(byte[] data, PerMessageCompressionExtension pmce) {
/*      */     try {
/* 1134 */       return pmce.compress(data);
/*      */     }
/* 1136 */     catch (WebSocketException e) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1141 */       return data;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static List<WebSocketFrame> splitIfNecessary(WebSocketFrame frame, int maxPayloadSize, PerMessageCompressionExtension pmce) {
/* 1150 */     if (maxPayloadSize == 0)
/*      */     {
/*      */       
/* 1153 */       return null;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/* 1158 */     if (frame.getPayloadLength() <= maxPayloadSize)
/*      */     {
/*      */       
/* 1161 */       return null;
/*      */     }
/*      */ 
/*      */     
/* 1165 */     if (frame.isBinaryFrame() || frame.isTextFrame()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1171 */       frame = compressFrame(frame, pmce);
/*      */ 
/*      */ 
/*      */       
/* 1175 */       if (frame.getPayloadLength() <= maxPayloadSize)
/*      */       {
/*      */         
/* 1178 */         return null;
/*      */       }
/*      */     }
/* 1181 */     else if (!frame.isContinuationFrame()) {
/*      */ 
/*      */       
/* 1184 */       return null;
/*      */     } 
/*      */ 
/*      */     
/* 1188 */     return split(frame, maxPayloadSize);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static List<WebSocketFrame> split(WebSocketFrame frame, int maxPayloadSize) {
/* 1195 */     byte[] originalPayload = frame.getPayload();
/* 1196 */     boolean originalFin = frame.getFin();
/*      */     
/* 1198 */     List<WebSocketFrame> frames = new ArrayList<WebSocketFrame>();
/*      */ 
/*      */ 
/*      */     
/* 1202 */     byte[] payload = Arrays.copyOf(originalPayload, maxPayloadSize);
/* 1203 */     frame.setFin(false).setPayload(payload);
/* 1204 */     frames.add(frame);
/*      */     int from;
/* 1206 */     for (from = maxPayloadSize; from < originalPayload.length; from += maxPayloadSize) {
/*      */ 
/*      */       
/* 1209 */       int to = Math.min(from + maxPayloadSize, originalPayload.length);
/* 1210 */       payload = Arrays.copyOfRange(originalPayload, from, to);
/*      */ 
/*      */       
/* 1213 */       WebSocketFrame cont = createContinuationFrame(payload);
/* 1214 */       frames.add(cont);
/*      */     } 
/*      */     
/* 1217 */     if (originalFin)
/*      */     {
/*      */       
/* 1220 */       ((WebSocketFrame)frames.get(frames.size() - 1)).setFin(true);
/*      */     }
/*      */     
/* 1223 */     return frames;
/*      */   }
/*      */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\neovisionaries\ws\client\WebSocketFrame.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */