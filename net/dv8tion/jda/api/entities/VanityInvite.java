/*    */ package net.dv8tion.jda.api.entities;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import javax.annotation.Nonnull;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VanityInvite
/*    */ {
/*    */   private final String code;
/*    */   private final int uses;
/*    */   
/*    */   public VanityInvite(@Nonnull String code, int uses) {
/* 34 */     this.code = code;
/* 35 */     this.uses = uses;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getCode() {
/* 46 */     return this.code;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getUses() {
/* 57 */     return this.uses;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public String getUrl() {
/* 68 */     return "https://discord.gg/" + getCode();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 74 */     if (this == obj)
/* 75 */       return true; 
/* 76 */     if (!(obj instanceof VanityInvite))
/* 77 */       return false; 
/* 78 */     VanityInvite other = (VanityInvite)obj;
/* 79 */     return (this.uses == other.uses && this.code.equals(other.code));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 85 */     return Objects.hash(new Object[] { this.code, Integer.valueOf(this.uses) });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 91 */     return "VanityInvite(" + this.code + ")";
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\VanityInvite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */