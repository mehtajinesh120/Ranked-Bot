/*    */ package okhttp3.internal.http2;
/*    */ 
/*    */ import okhttp3.internal.Util;
/*    */ import okio.ByteString;
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
/*    */ public final class Header
/*    */ {
/* 24 */   public static final ByteString PSEUDO_PREFIX = ByteString.encodeUtf8(":");
/*    */   
/*    */   public static final String RESPONSE_STATUS_UTF8 = ":status";
/*    */   
/*    */   public static final String TARGET_METHOD_UTF8 = ":method";
/*    */   public static final String TARGET_PATH_UTF8 = ":path";
/*    */   public static final String TARGET_SCHEME_UTF8 = ":scheme";
/*    */   public static final String TARGET_AUTHORITY_UTF8 = ":authority";
/* 32 */   public static final ByteString RESPONSE_STATUS = ByteString.encodeUtf8(":status");
/* 33 */   public static final ByteString TARGET_METHOD = ByteString.encodeUtf8(":method");
/* 34 */   public static final ByteString TARGET_PATH = ByteString.encodeUtf8(":path");
/* 35 */   public static final ByteString TARGET_SCHEME = ByteString.encodeUtf8(":scheme");
/* 36 */   public static final ByteString TARGET_AUTHORITY = ByteString.encodeUtf8(":authority");
/*    */ 
/*    */   
/*    */   public final ByteString name;
/*    */   
/*    */   public final ByteString value;
/*    */   
/*    */   final int hpackSize;
/*    */ 
/*    */   
/*    */   public Header(String name, String value) {
/* 47 */     this(ByteString.encodeUtf8(name), ByteString.encodeUtf8(value));
/*    */   }
/*    */   
/*    */   public Header(ByteString name, String value) {
/* 51 */     this(name, ByteString.encodeUtf8(value));
/*    */   }
/*    */   
/*    */   public Header(ByteString name, ByteString value) {
/* 55 */     this.name = name;
/* 56 */     this.value = value;
/* 57 */     this.hpackSize = 32 + name.size() + value.size();
/*    */   }
/*    */   
/*    */   public boolean equals(Object other) {
/* 61 */     if (other instanceof Header) {
/* 62 */       Header that = (Header)other;
/* 63 */       return (this.name.equals(that.name) && this.value
/* 64 */         .equals(that.value));
/*    */     } 
/* 66 */     return false;
/*    */   }
/*    */   
/*    */   public int hashCode() {
/* 70 */     int result = 17;
/* 71 */     result = 31 * result + this.name.hashCode();
/* 72 */     result = 31 * result + this.value.hashCode();
/* 73 */     return result;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 77 */     return Util.format("%s: %s", new Object[] { this.name.utf8(), this.value.utf8() });
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\internal\http2\Header.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */