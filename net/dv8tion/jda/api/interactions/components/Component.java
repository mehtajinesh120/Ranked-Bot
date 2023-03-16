/*     */ package net.dv8tion.jda.api.interactions.components;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.utils.data.SerializableData;
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
/*     */ public interface Component
/*     */   extends SerializableData
/*     */ {
/*     */   @Nonnull
/*     */   Type getType();
/*     */   
/*     */   @Nullable
/*     */   String getId();
/*     */   
/*     */   default int getMaxPerRow() {
/*  59 */     return getType().getMaxPerRow();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum Type
/*     */   {
/*  67 */     UNKNOWN(-1, 0),
/*     */     
/*  69 */     ACTION_ROW(1, 0),
/*     */     
/*  71 */     BUTTON(2, 5),
/*     */     
/*  73 */     SELECTION_MENU(3, 1);
/*     */     
/*     */     private final int key;
/*     */     
/*     */     private final int maxPerRow;
/*     */ 
/*     */     
/*     */     Type(int key, int maxPerRow) {
/*  81 */       this.key = key;
/*  82 */       this.maxPerRow = maxPerRow;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int getMaxPerRow() {
/*  92 */       return this.maxPerRow;
/*     */     }
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
/*     */     @Nonnull
/*     */     public static Type fromKey(int type) {
/* 106 */       for (Type t : values()) {
/*     */         
/* 108 */         if (t.key == type)
/* 109 */           return t; 
/*     */       } 
/* 111 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\interactions\components\Component.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */