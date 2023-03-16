/*     */ package net.dv8tion.jda.internal.requests;
/*     */ 
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import org.jetbrains.annotations.Nullable;
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
/*     */ public class DeferredRestAction<T, R extends RestAction<T>>
/*     */   implements AuditableRestAction<T>
/*     */ {
/*     */   private final JDA api;
/*     */   private final Class<T> type;
/*     */   private final Supplier<T> valueSupplier;
/*     */   private final Supplier<R> actionSupplier;
/*     */   private String reason;
/*  41 */   private long deadline = -1L;
/*     */   
/*     */   private BooleanSupplier isAction;
/*     */   private BooleanSupplier transitiveChecks;
/*     */   
/*     */   public DeferredRestAction(JDA api, Supplier<R> actionSupplier) {
/*  47 */     this(api, null, null, actionSupplier);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DeferredRestAction(JDA api, Class<T> type, Supplier<T> valueSupplier, Supplier<R> actionSupplier) {
/*  54 */     this.api = api;
/*  55 */     this.type = type;
/*  56 */     this.valueSupplier = valueSupplier;
/*  57 */     this.actionSupplier = actionSupplier;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/*  64 */     return this.api;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<T> reason(String reason) {
/*  71 */     this.reason = reason;
/*  72 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<T> setCheck(BooleanSupplier checks) {
/*  79 */     this.transitiveChecks = checks;
/*  80 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public BooleanSupplier getCheck() {
/*  87 */     return this.transitiveChecks;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<T> timeout(long timeout, @Nonnull TimeUnit unit) {
/*  94 */     Checks.notNull(unit, "TimeUnit");
/*  95 */     return deadline((timeout <= 0L) ? 0L : (System.currentTimeMillis() + unit.toMillis(timeout)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public AuditableRestAction<T> deadline(long timestamp) {
/* 102 */     this.deadline = timestamp;
/* 103 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public AuditableRestAction<T> setCacheCheck(BooleanSupplier checks) {
/* 108 */     this.isAction = checks;
/* 109 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queue(Consumer<? super T> success, Consumer<? super Throwable> failure) {
/*     */     Consumer<? super T> finalSuccess;
/* 116 */     if (success != null) {
/* 117 */       finalSuccess = success;
/*     */     } else {
/* 119 */       finalSuccess = RestAction.getDefaultSuccess();
/*     */     } 
/* 121 */     if (this.type == null) {
/*     */       
/* 123 */       BooleanSupplier checks = this.isAction;
/* 124 */       if (checks != null && checks.getAsBoolean()) {
/* 125 */         getAction().queue(success, failure);
/*     */       } else {
/* 127 */         finalSuccess.accept(null);
/*     */       } 
/*     */       return;
/*     */     } 
/* 131 */     T value = this.valueSupplier.get();
/* 132 */     if (value == null) {
/*     */       
/* 134 */       getAction().queue(success, failure);
/*     */     }
/*     */     else {
/*     */       
/* 138 */       finalSuccess.accept(value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public CompletableFuture<T> submit(boolean shouldQueue) {
/* 146 */     if (this.type == null) {
/*     */       
/* 148 */       BooleanSupplier checks = this.isAction;
/* 149 */       if (checks != null && checks.getAsBoolean())
/* 150 */         return getAction().submit(shouldQueue); 
/* 151 */       return CompletableFuture.completedFuture(null);
/*     */     } 
/* 153 */     T value = this.valueSupplier.get();
/* 154 */     if (value != null)
/* 155 */       return CompletableFuture.completedFuture(value); 
/* 156 */     return getAction().submit(shouldQueue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T complete(boolean shouldQueue) throws RateLimitedException {
/* 162 */     if (this.type == null) {
/*     */       
/* 164 */       BooleanSupplier checks = this.isAction;
/* 165 */       if (checks != null && checks.getAsBoolean())
/* 166 */         return (T)getAction().complete(shouldQueue); 
/* 167 */       return null;
/*     */     } 
/* 169 */     T value = this.valueSupplier.get();
/* 170 */     if (value != null)
/* 171 */       return value; 
/* 172 */     return (T)getAction().complete(shouldQueue);
/*     */   }
/*     */ 
/*     */   
/*     */   private R getAction() {
/* 177 */     RestAction restAction = (RestAction)this.actionSupplier.get();
/* 178 */     restAction.setCheck(this.transitiveChecks);
/* 179 */     if (this.deadline >= 0L)
/* 180 */       restAction.deadline(this.deadline); 
/* 181 */     if (restAction instanceof AuditableRestAction && this.reason != null)
/* 182 */       ((AuditableRestAction)restAction).reason(this.reason); 
/* 183 */     return (R)restAction;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\DeferredRestAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */