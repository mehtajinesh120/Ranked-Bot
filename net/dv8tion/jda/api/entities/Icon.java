/*     */ package net.dv8tion.jda.api.entities;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Base64;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.IOUtil;
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
/*     */ public class Icon
/*     */ {
/*     */   protected final String encoding;
/*     */   
/*     */   protected Icon(@Nonnull IconType type, @Nonnull String base64Encoding) {
/*  52 */     this.encoding = type.getHeader() + base64Encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public String getEncoding() {
/*  63 */     return this.encoding;
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
/*     */   
/*     */   @Nonnull
/*     */   public static Icon from(@Nonnull File file) throws IOException {
/*  83 */     Checks.notNull(file, "Provided File");
/*  84 */     Checks.check(file.exists(), "Provided file does not exist!");
/*  85 */     int index = file.getName().lastIndexOf('.');
/*  86 */     if (index < 0)
/*  87 */       return from(file, IconType.JPEG); 
/*  88 */     String ext = file.getName().substring(index + 1);
/*  89 */     IconType type = IconType.fromExtension(ext);
/*  90 */     return from(file, type);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Icon from(@Nonnull InputStream stream) throws IOException {
/* 113 */     return from(stream, IconType.JPEG);
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
/*     */   @Nonnull
/*     */   public static Icon from(@Nonnull byte[] data) {
/* 132 */     return from(data, IconType.JPEG);
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
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Icon from(@Nonnull File file, @Nonnull IconType type) throws IOException {
/* 154 */     Checks.notNull(file, "Provided File");
/* 155 */     Checks.notNull(type, "IconType");
/* 156 */     Checks.check(file.exists(), "Provided file does not exist!");
/*     */     
/* 158 */     return from(IOUtil.readFully(file), type);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public static Icon from(@Nonnull InputStream stream, @Nonnull IconType type) throws IOException {
/* 181 */     Checks.notNull(stream, "InputStream");
/* 182 */     Checks.notNull(type, "IconType");
/*     */     
/* 184 */     return from(IOUtil.readFully(stream), type);
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
/*     */   @Nonnull
/*     */   public static Icon from(@Nonnull byte[] data, @Nonnull IconType type) {
/* 203 */     Checks.notNull(data, "Provided byte[]");
/* 204 */     Checks.notNull(type, "IconType");
/*     */     
/* 206 */     return new Icon(type, new String(Base64.getEncoder().encode(data), StandardCharsets.UTF_8));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum IconType
/*     */   {
/* 215 */     JPEG("image/jpeg"),
/*     */     
/* 217 */     PNG("image/png"),
/*     */     
/* 219 */     WEBP("image/webp"),
/*     */     
/* 221 */     GIF("image/gif"),
/*     */ 
/*     */     
/* 224 */     UNKNOWN("image/jpeg");
/*     */     
/*     */     private final String mime;
/*     */     
/*     */     private final String header;
/*     */     
/*     */     IconType(String mime) {
/* 231 */       this.mime = mime;
/* 232 */       this.header = "data:" + mime + ";base64,";
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
/*     */     @Nonnull
/*     */     public String getMIME() {
/* 245 */       return this.mime;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getHeader() {
/* 256 */       return this.header;
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
/*     */     
/*     */     @Nonnull
/*     */     public static IconType fromMIME(@Nonnull String mime) {
/* 271 */       Checks.notNull(mime, "MIME Type");
/* 272 */       for (IconType type : values()) {
/*     */         
/* 274 */         if (type.mime.equalsIgnoreCase(mime))
/* 275 */           return type; 
/*     */       } 
/* 277 */       return UNKNOWN;
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
/*     */     
/*     */     @Nonnull
/*     */     public static IconType fromExtension(@Nonnull String extension) {
/* 292 */       Checks.notNull(extension, "Extension Type");
/* 293 */       switch (extension.toLowerCase()) {
/*     */         
/*     */         case "jpe":
/*     */         case "jif":
/*     */         case "jfif":
/*     */         case "jfi":
/*     */         case "jpg":
/*     */         case "jpeg":
/* 301 */           return JPEG;
/*     */         case "png":
/* 303 */           return PNG;
/*     */         case "webp":
/* 305 */           return WEBP;
/*     */         case "gif":
/* 307 */           return GIF;
/*     */       } 
/* 309 */       return UNKNOWN;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\entities\Icon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */