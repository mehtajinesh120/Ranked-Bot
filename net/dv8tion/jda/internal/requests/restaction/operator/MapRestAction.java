/*    */ package net.dv8tion.jda.internal.requests.restaction.operator;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*    */ import net.dv8tion.jda.api.requests.RestAction;
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
/*    */ public class MapRestAction<I, O>
/*    */   extends RestActionOperator<I, O>
/*    */ {
/*    */   private final Function<? super I, ? extends O> function;
/*    */   
/*    */   public MapRestAction(RestAction<I> action, Function<? super I, ? extends O> function) {
/* 34 */     super(action);
/* 35 */     this.function = function;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void queue(@Nullable Consumer<? super O> success, @Nullable Consumer<? super Throwable> failure) {
/* 41 */     this.action.queue(result -> doSuccess(success, this.function.apply((I)result)), contextWrap(failure));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public O complete(boolean shouldQueue) throws RateLimitedException {
/* 47 */     return this.function.apply((I)this.action.complete(shouldQueue));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public CompletableFuture<O> submit(boolean shouldQueue) {
/* 54 */     return this.action.submit(shouldQueue).thenApply(this.function);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\operator\MapRestAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */