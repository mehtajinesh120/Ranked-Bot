/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import javax.annotation.Nonnull;
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
/*     */ public enum ChannelType
/*     */ {
/*  29 */   TEXT(0, 0, true),
/*     */ 
/*     */ 
/*     */   
/*  33 */   PRIVATE(1, -1),
/*     */ 
/*     */ 
/*     */   
/*  37 */   VOICE(2, 1, true),
/*     */ 
/*     */ 
/*     */   
/*  41 */   GROUP(3, -1),
/*     */ 
/*     */ 
/*     */   
/*  45 */   CATEGORY(4, 2, true),
/*     */ 
/*     */ 
/*     */   
/*  49 */   STORE(6, 0, true),
/*     */ 
/*     */ 
/*     */   
/*  53 */   STAGE(13, 1, true),
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  58 */   UNKNOWN(-1, -2);
/*     */ 
/*     */   
/*     */   private final int sortBucket;
/*     */ 
/*     */   
/*     */   private final int id;
/*     */ 
/*     */   
/*     */   private final boolean isGuild;
/*     */ 
/*     */   
/*     */   ChannelType(int id, int sortBucket, boolean isGuild) {
/*  71 */     this.id = id;
/*  72 */     this.sortBucket = sortBucket;
/*  73 */     this.isGuild = isGuild;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSortBucket() {
/*  83 */     return this.sortBucket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getId() {
/*  93 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGuild() {
/* 103 */     return this.isGuild;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAudio() {
/* 113 */     switch (this) {
/*     */       
/*     */       case VOICE:
/*     */       case STAGE:
/* 117 */         return true;
/*     */     } 
/* 119 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMessage() {
/* 130 */     switch (this) {
/*     */ 
/*     */       
/*     */       case TEXT:
/*     */       case PRIVATE:
/*     */       case GROUP:
/* 136 */         return true;
/*     */     } 
/* 138 */     return false;
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
/*     */   @Nonnull
/*     */   public static ChannelType fromId(int id) {
/* 153 */     if (id == 5)
/* 154 */       return TEXT; 
/* 155 */     for (ChannelType type : values()) {
/*     */       
/* 157 */       if (type.id == id)
/* 158 */         return type; 
/*     */     } 
/* 160 */     return UNKNOWN;
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
/*     */   public static EnumSet<ChannelType> fromSortBucket(int bucket) {
/* 174 */     EnumSet<ChannelType> types = EnumSet.noneOf(ChannelType.class);
/* 175 */     for (ChannelType type : values()) {
/*     */       
/* 177 */       if (type.getSortBucket() == bucket)
/* 178 */         types.add(type); 
/*     */     } 
/* 180 */     return types;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\ChannelType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */