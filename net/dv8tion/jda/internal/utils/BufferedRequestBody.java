/*    */ package net.dv8tion.jda.internal.utils;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import okhttp3.MediaType;
/*    */ import okhttp3.RequestBody;
/*    */ import okio.BufferedSink;
/*    */ import okio.BufferedSource;
/*    */ import okio.Okio;
/*    */ import okio.Source;
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
/*    */ public class BufferedRequestBody
/*    */   extends RequestBody
/*    */ {
/*    */   private final Source source;
/*    */   private final MediaType type;
/*    */   private byte[] data;
/*    */   
/*    */   public BufferedRequestBody(Source source, MediaType type) {
/* 38 */     this.source = source;
/* 39 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public MediaType contentType() {
/* 46 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void writeTo(@Nonnull BufferedSink sink) throws IOException {
/* 52 */     if (this.data != null) {
/*    */       
/* 54 */       sink.write(this.data);
/*    */       
/*    */       return;
/*    */     } 
/* 58 */     BufferedSource s = Okio.buffer(this.source);
/*    */     try {
/* 60 */       this.data = s.readByteArray();
/* 61 */       sink.write(this.data);
/* 62 */       if (s != null) s.close(); 
/*    */     } catch (Throwable throwable) {
/*    */       if (s != null)
/*    */         try {
/*    */           s.close();
/*    */         } catch (Throwable throwable1) {
/*    */           throwable.addSuppressed(throwable1);
/*    */         }  
/*    */       throw throwable;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\BufferedRequestBody.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */