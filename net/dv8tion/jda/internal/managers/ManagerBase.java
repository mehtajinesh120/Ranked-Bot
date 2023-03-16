/*     */ package net.dv8tion.jda.internal.managers;
/*     */ 
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.exceptions.RateLimitedException;
/*     */ import net.dv8tion.jda.api.managers.Manager;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.requests.restaction.AuditableRestActionImpl;
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
/*     */ public abstract class ManagerBase<M extends Manager<M>>
/*     */   extends AuditableRestActionImpl<Void>
/*     */   implements Manager<M>
/*     */ {
/*     */   private static boolean enablePermissionChecks = true;
/*  34 */   protected long set = 0L;
/*     */ 
/*     */   
/*     */   public static void setPermissionChecksEnabled(boolean enable) {
/*  38 */     enablePermissionChecks = enable;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isPermissionChecksEnabled() {
/*  43 */     return enablePermissionChecks;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ManagerBase(JDA api, Route.CompiledRoute route) {
/*  48 */     super(api, route);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M setCheck(BooleanSupplier checks) {
/*  56 */     return (M)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M timeout(long timeout, @Nonnull TimeUnit unit) {
/*  64 */     return (M)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M deadline(long timestamp) {
/*  72 */     return (M)super.deadline(timestamp);
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
/*     */   @Nonnull
/*     */   public M reset(long fields) {
/*  85 */     this.set &= fields ^ 0xFFFFFFFFFFFFFFFFL;
/*  86 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M reset(long... fields) {
/*  94 */     Checks.notNull(fields, "Fields");
/*     */     
/*  96 */     if (fields.length == 0)
/*  97 */       return (M)this; 
/*  98 */     if (fields.length == 1) {
/*  99 */       return reset(fields[0]);
/*     */     }
/*     */     
/* 102 */     long sum = fields[0];
/* 103 */     for (int i = 1; i < fields.length; i++)
/* 104 */       sum |= fields[i]; 
/* 105 */     return reset(sum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M reset() {
/* 113 */     this.set = 0L;
/* 114 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void queue(Consumer<? super Void> success, Consumer<? super Throwable> failure) {
/* 120 */     if (shouldUpdate()) {
/* 121 */       super.queue(success, failure);
/* 122 */     } else if (success != null) {
/* 123 */       success.accept(null);
/*     */     } else {
/* 125 */       getDefaultSuccess().accept(null);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Void complete(boolean shouldQueue) throws RateLimitedException {
/* 131 */     if (shouldUpdate())
/* 132 */       return (Void)super.complete(shouldQueue); 
/* 133 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected BooleanSupplier finalizeChecks() {
/* 139 */     return enablePermissionChecks ? this::checkPermissions : super.finalizeChecks();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldUpdate() {
/* 144 */     return (this.set != 0L);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean shouldUpdate(long bit) {
/* 149 */     return ((this.set & bit) == bit);
/*     */   }
/*     */ 
/*     */   
/*     */   protected <E> void withLock(E object, Consumer<? super E> consumer) {
/* 154 */     synchronized (object) {
/*     */       
/* 156 */       consumer.accept(object);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean checkPermissions() {
/* 162 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\managers\ManagerBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */