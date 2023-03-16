/*     */ package net.dv8tion.jda.api.events.guild.update;
/*     */ 
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.Member;
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
/*     */ public class GuildUpdateOwnerEvent
/*     */   extends GenericGuildUpdateEvent<Member>
/*     */ {
/*     */   public static final String IDENTIFIER = "owner";
/*     */   private final long prevId;
/*     */   private final long nextId;
/*     */   
/*     */   public GuildUpdateOwnerEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nullable Member oldOwner, long prevId, long nextId) {
/*  41 */     super(api, responseNumber, guild, oldOwner, guild.getOwner(), "owner");
/*  42 */     this.prevId = prevId;
/*  43 */     this.nextId = nextId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getNewOwnerIdLong() {
/*  53 */     return this.nextId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getNewOwnerId() {
/*  64 */     return Long.toUnsignedString(this.nextId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getOldOwnerIdLong() {
/*  74 */     return this.prevId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getOldOwnerId() {
/*  85 */     return Long.toUnsignedString(this.prevId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Member getOldOwner() {
/*  96 */     return getOldValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Member getNewOwner() {
/* 107 */     return getNewValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guil\\update\GuildUpdateOwnerEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */