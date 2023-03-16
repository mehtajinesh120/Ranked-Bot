/*    */ package net.dv8tion.jda.internal.requests.restaction;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.entities.PermissionOverride;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.api.utils.data.SerializableData;
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
/*    */ public class PermOverrideData
/*    */   implements SerializableData
/*    */ {
/*    */   public static final int ROLE_TYPE = 0;
/*    */   public static final int MEMBER_TYPE = 1;
/*    */   public final int type;
/*    */   public final long id;
/*    */   public final long allow;
/*    */   public final long deny;
/*    */   
/*    */   public PermOverrideData(int type, long id, long allow, long deny) {
/* 36 */     this.type = type;
/* 37 */     this.id = id;
/* 38 */     this.allow = allow;
/* 39 */     this.deny = deny & (allow ^ 0xFFFFFFFFFFFFFFFFL);
/*    */   }
/*    */ 
/*    */   
/*    */   public PermOverrideData(PermissionOverride override) {
/* 44 */     this.id = override.getIdLong();
/* 45 */     this.type = override.isMemberOverride() ? 1 : 0;
/* 46 */     this.allow = override.getAllowedRaw();
/* 47 */     this.deny = override.getDeniedRaw();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public DataObject toData() {
/* 54 */     DataObject o = DataObject.empty();
/* 55 */     o.put("type", Integer.valueOf(this.type));
/* 56 */     o.put("id", Long.valueOf(this.id));
/* 57 */     o.put("allow", Long.valueOf(this.allow));
/* 58 */     o.put("deny", Long.valueOf(this.deny));
/* 59 */     return o;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 65 */     return Long.hashCode(this.id);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 71 */     if (obj == this)
/* 72 */       return true; 
/* 73 */     if (!(obj instanceof PermOverrideData)) {
/* 74 */       return false;
/*    */     }
/* 76 */     PermOverrideData other = (PermOverrideData)obj;
/* 77 */     return (other.id == this.id);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\PermOverrideData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */