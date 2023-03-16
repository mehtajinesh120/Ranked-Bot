/*    */ package net.dv8tion.jda.api.exceptions;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.internal.utils.Helpers;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContextException
/*    */   extends Exception
/*    */ {
/*    */   @Nonnull
/*    */   public static Consumer<Throwable> herePrintingTrace() {
/* 39 */     return here(Throwable::printStackTrace);
/*    */   }
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
/*    */   @Nonnull
/*    */   public static Consumer<Throwable> here(@Nonnull Consumer<? super Throwable> acceptor) {
/* 54 */     return new ContextConsumer(new ContextException(), acceptor);
/*    */   }
/*    */   
/*    */   public static class ContextConsumer
/*    */     implements Consumer<Throwable>
/*    */   {
/*    */     private final ContextException context;
/*    */     private final Consumer<? super Throwable> callback;
/*    */     
/*    */     private ContextConsumer(ContextException context, Consumer<? super Throwable> callback) {
/* 64 */       this.context = context;
/* 65 */       this.callback = callback;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public void accept(Throwable throwable) {
/* 71 */       if (this.callback != null)
/* 72 */         this.callback.accept(Helpers.appendCause(throwable, this.context)); 
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\exceptions\ContextException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */