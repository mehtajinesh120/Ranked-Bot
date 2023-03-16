/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface RichPresence
/*     */   extends Activity
/*     */ {
/*     */   long getApplicationIdLong();
/*     */   
/*     */   @Nonnull
/*     */   String getApplicationId();
/*     */   
/*     */   @Nullable
/*     */   String getSessionId();
/*     */   
/*     */   @Nullable
/*     */   String getSyncId();
/*     */   
/*     */   int getFlags();
/*     */   
/*     */   EnumSet<ActivityFlag> getFlagSet();
/*     */   
/*     */   @Nullable
/*     */   String getState();
/*     */   
/*     */   @Nullable
/*     */   String getDetails();
/*     */   
/*     */   @Nullable
/*     */   Party getParty();
/*     */   
/*     */   @Nullable
/*     */   Image getLargeImage();
/*     */   
/*     */   @Nullable
/*     */   Image getSmallImage();
/*     */   
/*     */   public static class Image
/*     */   {
/*     */     protected final String key;
/*     */     protected final String text;
/*     */     protected final String applicationId;
/*     */     
/*     */     public Image(long applicationId, String key, String text) {
/* 142 */       this.applicationId = Long.toUnsignedString(applicationId);
/* 143 */       this.key = key;
/* 144 */       this.text = text;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getKey() {
/* 155 */       return this.key;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getText() {
/* 166 */       return this.text;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getUrl() {
/* 177 */       if (this.key.startsWith("spotify:"))
/* 178 */         return "https://i.scdn.co/image/" + this.key.substring("spotify:".length()); 
/* 179 */       if (this.key.startsWith("twitch:"))
/* 180 */         return String.format("https://static-cdn.jtvnw.net/previews-ttv/live_user_%s-1920x1080.png", new Object[] { this.key.substring("twitch:".length()) }); 
/* 181 */       return "https://cdn.discordapp.com/app-assets/" + this.applicationId + "/" + this.key + ".png";
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 187 */       return String.format("RichPresenceImage(%s | %s)", new Object[] { this.key, this.text });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 193 */       if (!(obj instanceof Image))
/* 194 */         return false; 
/* 195 */       Image i = (Image)obj;
/* 196 */       return (Objects.equals(this.key, i.key) && Objects.equals(this.text, i.text));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 202 */       return Objects.hash(new Object[] { this.key, this.text });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class Party
/*     */   {
/*     */     protected final String id;
/*     */     
/*     */     protected final long size;
/*     */     
/*     */     protected final long max;
/*     */ 
/*     */     
/*     */     public Party(String id, long size, long max) {
/* 217 */       this.id = id;
/* 218 */       this.size = size;
/* 219 */       this.max = max;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nullable
/*     */     public String getId() {
/* 230 */       return this.id;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getSize() {
/* 240 */       return this.size;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public long getMax() {
/* 250 */       return this.max;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 256 */       return Helpers.format("RichPresenceParty(%s | [%d, %d])", new Object[] { this.id, Long.valueOf(this.size), Long.valueOf(this.max) });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 262 */       if (!(obj instanceof Party))
/* 263 */         return false; 
/* 264 */       Party p = (Party)obj;
/* 265 */       return (this.size == p.size && this.max == p.max && Objects.equals(this.id, p.id));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 271 */       return Objects.hash(new Object[] { this.id, Long.valueOf(this.size), Long.valueOf(this.max) });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\RichPresence.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */