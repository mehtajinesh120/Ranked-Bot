/*     */ package net.dv8tion.jda.api.audit;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
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
/*     */ public class AuditLogChange
/*     */ {
/*     */   protected final Object oldValue;
/*     */   protected final Object newValue;
/*     */   protected final String key;
/*     */   
/*     */   public AuditLogChange(Object oldValue, Object newValue, String key) {
/*  37 */     this.oldValue = oldValue;
/*  38 */     this.newValue = newValue;
/*  39 */     this.key = key;
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
/*     */   @Nullable
/*     */   public <T> T getOldValue() {
/*  58 */     return (T)this.oldValue;
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
/*     */   @Nullable
/*     */   public <T> T getNewValue() {
/*  77 */     return (T)this.newValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getKey() {
/*  89 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  95 */     return Objects.hash(new Object[] { this.key, this.oldValue, this.newValue });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 101 */     if (!(obj instanceof AuditLogChange))
/* 102 */       return false; 
/* 103 */     AuditLogChange other = (AuditLogChange)obj;
/* 104 */     return (other.key.equals(this.key) && 
/* 105 */       Objects.equals(other.oldValue, this.oldValue) && 
/* 106 */       Objects.equals(other.newValue, this.newValue));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 112 */     return String.format("ALC:%s(%s -> %s)", new Object[] { this.key, this.oldValue, this.newValue });
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audit\AuditLogChange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */