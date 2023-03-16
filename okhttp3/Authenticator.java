/*     */ package okhttp3;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.annotation.Nullable;
/*     */ 
/*     */ public interface Authenticator {
/*     */   public static final Authenticator NONE = (route, response) -> null;
/*     */   
/*     */   @Nullable
/*     */   Request authenticate(@Nullable Route paramRoute, Response paramResponse) throws IOException;
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\okhttp3\Authenticator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */