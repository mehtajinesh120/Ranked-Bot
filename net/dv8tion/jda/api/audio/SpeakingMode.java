/*     */ package net.dv8tion.jda.api.audio;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
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
/*     */ public enum SpeakingMode
/*     */ {
/*  29 */   VOICE(1), SOUNDSHARE(2), PRIORITY(4);
/*     */   
/*     */   private final int raw;
/*     */ 
/*     */   
/*     */   SpeakingMode(int raw) {
/*  35 */     this.raw = raw;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRaw() {
/*  45 */     return this.raw;
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
/*     */   @Nonnull
/*     */   public static EnumSet<SpeakingMode> getModes(int mask) {
/*  59 */     EnumSet<SpeakingMode> modes = EnumSet.noneOf(SpeakingMode.class);
/*  60 */     if (mask == 0)
/*  61 */       return modes; 
/*  62 */     SpeakingMode[] values = values();
/*  63 */     for (SpeakingMode mode : values) {
/*     */       
/*  65 */       if ((mode.raw & mask) == mode.raw)
/*  66 */         modes.add(mode); 
/*     */     } 
/*  68 */     return modes;
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
/*     */   public static int getRaw(@Nullable SpeakingMode... modes) {
/*  82 */     if (modes == null || modes.length == 0)
/*  83 */       return 0; 
/*  84 */     int mask = 0;
/*  85 */     for (SpeakingMode m : modes)
/*  86 */       mask |= m.raw; 
/*  87 */     return mask;
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
/*     */   public static int getRaw(@Nullable Collection<SpeakingMode> modes) {
/* 101 */     if (modes == null)
/* 102 */       return 0; 
/* 103 */     int raw = 0;
/* 104 */     for (SpeakingMode mode : modes)
/* 105 */       raw |= mode.getRaw(); 
/* 106 */     return raw;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\audio\SpeakingMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */