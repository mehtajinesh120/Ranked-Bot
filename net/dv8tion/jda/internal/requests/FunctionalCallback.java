/*    */ package net.dv8tion.jda.internal.requests;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.function.BiConsumer;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.utils.IOBiConsumer;
/*    */ import okhttp3.Call;
/*    */ import okhttp3.Callback;
/*    */ import okhttp3.Response;
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
/*    */ public class FunctionalCallback
/*    */   implements Callback
/*    */ {
/*    */   private final BiConsumer<Call, IOException> failure;
/*    */   private final IOBiConsumer<Call, Response> success;
/*    */   
/*    */   public FunctionalCallback(BiConsumer<Call, IOException> failure, IOBiConsumer<Call, Response> success) {
/* 35 */     this.failure = failure;
/* 36 */     this.success = success;
/*    */   }
/*    */ 
/*    */   
/*    */   public static Builder onSuccess(IOBiConsumer<Call, Response> callback) {
/* 41 */     return (new Builder()).onSuccess(callback);
/*    */   }
/*    */ 
/*    */   
/*    */   public static Builder onFailure(BiConsumer<Call, IOException> callback) {
/* 46 */     return (new Builder()).onFailure(callback);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onFailure(@Nonnull Call call, @Nonnull IOException e) {
/* 52 */     if (this.failure != null) {
/* 53 */       this.failure.accept(call, e);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void onResponse(@Nonnull Call call, @Nonnull Response response) throws IOException {
/* 59 */     if (this.success != null) {
/* 60 */       this.success.accept(call, response);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Builder
/*    */   {
/*    */     private BiConsumer<Call, IOException> failure;
/*    */     private IOBiConsumer<Call, Response> success;
/*    */     
/*    */     public Builder onSuccess(IOBiConsumer<Call, Response> callback) {
/* 70 */       this.success = callback;
/* 71 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public Builder onFailure(BiConsumer<Call, IOException> callback) {
/* 76 */       this.failure = callback;
/* 77 */       return this;
/*    */     }
/*    */ 
/*    */     
/*    */     public FunctionalCallback build() {
/* 82 */       return new FunctionalCallback(this.failure, this.success);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\FunctionalCallback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */