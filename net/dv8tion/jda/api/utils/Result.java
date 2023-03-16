/*     */ package net.dv8tion.jda.api.utils;
/*     */ 
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.CheckReturnValue;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
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
/*     */ public class Result<T>
/*     */ {
/*     */   private final T value;
/*     */   private final Throwable error;
/*     */   
/*     */   private Result(T value, Throwable error) {
/*  47 */     this.value = value;
/*  48 */     this.error = error;
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
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public static <E> Result<E> success(@Nullable E value) {
/*  65 */     return new Result<>(value, null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public static <E> Result<E> failure(@Nonnull Throwable error) {
/*  85 */     Checks.notNull(error, "Error");
/*  86 */     return new Result<>(null, error);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public static <E> Result<E> defer(@Nonnull Supplier<? extends E> supplier) {
/* 107 */     Checks.notNull(supplier, "Supplier");
/*     */     
/*     */     try {
/* 110 */       return success(supplier.get());
/*     */     }
/* 112 */     catch (Exception ex) {
/*     */       
/* 114 */       return failure(ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isFailure() {
/* 126 */     return (this.error != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSuccess() {
/* 137 */     return (this.error == null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Result<T> onFailure(@Nonnull Consumer<? super Throwable> callback) {
/* 156 */     Checks.notNull(callback, "Callback");
/* 157 */     if (isFailure())
/* 158 */       callback.accept(this.error); 
/* 159 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Result<T> onSuccess(@Nonnull Consumer<? super T> callback) {
/* 178 */     Checks.notNull(callback, "Callback");
/* 179 */     if (isSuccess())
/* 180 */       callback.accept(this.value); 
/* 181 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public <U> Result<U> map(@Nonnull Function<? super T, ? extends U> function) {
/* 205 */     Checks.notNull(function, "Function");
/* 206 */     if (isSuccess())
/* 207 */       return defer(() -> function.apply(this.value)); 
/* 208 */     return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   @CheckReturnValue
/*     */   public <U> Result<U> flatMap(@Nonnull Function<? super T, ? extends Result<U>> function) {
/* 230 */     Checks.notNull(function, "Function");
/*     */     
/*     */     try {
/* 233 */       if (isSuccess()) {
/* 234 */         return function.apply(this.value);
/*     */       }
/* 236 */     } catch (Exception ex) {
/*     */       
/* 238 */       return failure(ex);
/*     */     } 
/* 240 */     return this;
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
/*     */   public T get() {
/* 254 */     if (isFailure())
/* 255 */       throw new IllegalStateException(this.error); 
/* 256 */     return this.value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Throwable getFailure() {
/* 268 */     return this.error;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public Result<T> expect(@Nonnull Predicate<? super Throwable> predicate) {
/* 289 */     Checks.notNull(predicate, "Predicate");
/* 290 */     if (isFailure() && predicate.test(this.error))
/* 291 */       throw new IllegalStateException(this.error); 
/* 292 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 298 */     return isSuccess() ? ("Result(success=" + this.value + ")") : ("Result(error=" + this.error + ")");
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\Result.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */