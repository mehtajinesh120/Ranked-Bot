/*     */ package net.dv8tion.jda.internal.entities;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.entities.Guild;
/*     */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*     */ import net.dv8tion.jda.api.entities.User;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import org.jetbrains.annotations.Contract;
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
/*     */ public class UserById
/*     */   implements User
/*     */ {
/*     */   protected final long id;
/*     */   
/*     */   public UserById(long id) {
/*  37 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public long getIdLong() {
/*  43 */     return this.id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getAsMention() {
/*  50 */     return "<@" + getId() + ">";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  56 */     return Long.hashCode(this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  62 */     if (obj == this)
/*  63 */       return true; 
/*  64 */     if (!(obj instanceof User))
/*  65 */       return false; 
/*  66 */     return (((User)obj).getIdLong() == this.id);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  72 */     return "U:(" + getId() + ')';
/*     */   }
/*     */ 
/*     */   
/*     */   @Contract("->fail")
/*     */   private void unsupported() {
/*  78 */     throw new UnsupportedOperationException("This User instance only wraps an ID. Other operations are unsupported");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getName() {
/*  85 */     unsupported();
/*  86 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getDiscriminator() {
/*  93 */     unsupported();
/*  94 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public String getAvatarId() {
/* 101 */     unsupported();
/* 102 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<User.Profile> retrieveProfile() {
/* 109 */     unsupported();
/* 110 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getDefaultAvatarId() {
/* 117 */     unsupported();
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getAsTag() {
/* 125 */     unsupported();
/* 126 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasPrivateChannel() {
/* 132 */     unsupported();
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public RestAction<PrivateChannel> openPrivateChannel() {
/* 140 */     unsupported();
/* 141 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<Guild> getMutualGuilds() {
/* 148 */     unsupported();
/* 149 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBot() {
/* 155 */     unsupported();
/* 156 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSystem() {
/* 162 */     unsupported();
/* 163 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public JDA getJDA() {
/* 170 */     unsupported();
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public EnumSet<User.UserFlag> getFlags() {
/* 178 */     unsupported();
/* 179 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getFlagsRaw() {
/* 185 */     unsupported();
/* 186 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\UserById.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */