/*     */ package net.dv8tion.jda.api.utils;
/*     */ 
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
/*     */ public final class MarkdownUtil
/*     */ {
/*     */   @Nonnull
/*     */   public static String bold(@Nonnull String input) {
/*  39 */     String sanitized = MarkdownSanitizer.escape(input, -2);
/*  40 */     return "**" + sanitized + "**";
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
/*     */   @Nonnull
/*     */   public static String italics(@Nonnull String input) {
/*  56 */     String sanitized = MarkdownSanitizer.escape(input, -3);
/*  57 */     return "_" + sanitized + "_";
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
/*     */   @Nonnull
/*     */   public static String underline(@Nonnull String input) {
/*  73 */     String sanitized = MarkdownSanitizer.escape(input, -129);
/*  74 */     return "__" + sanitized + "__";
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
/*     */   @Nonnull
/*     */   public static String monospace(@Nonnull String input) {
/*  90 */     String sanitized = MarkdownSanitizer.escape(input, -9);
/*  91 */     return "`" + sanitized + "`";
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
/*     */   @Nonnull
/*     */   public static String codeblock(@Nonnull String input) {
/* 107 */     return codeblock(null, input);
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
/*     */   @Nonnull
/*     */   public static String codeblock(@Nullable String language, @Nonnull String input) {
/* 125 */     String sanitized = MarkdownSanitizer.escape(input, -33);
/* 126 */     if (language != null)
/* 127 */       return "```" + language.trim() + "\n" + sanitized + "```"; 
/* 128 */     return "```" + sanitized + "```";
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
/*     */   @Nonnull
/*     */   public static String spoiler(@Nonnull String input) {
/* 144 */     String sanitized = MarkdownSanitizer.escape(input, -65);
/* 145 */     return "||" + sanitized + "||";
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
/*     */   @Nonnull
/*     */   public static String strike(@Nonnull String input) {
/* 161 */     String sanitized = MarkdownSanitizer.escape(input, -257);
/* 162 */     return "~~" + sanitized + "~~";
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
/*     */   @Nonnull
/*     */   public static String quote(@Nonnull String input) {
/* 178 */     String sanitized = MarkdownSanitizer.escape(input, -513);
/* 179 */     return "> " + sanitized.replace("\n", "\n> ");
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
/*     */   public static String quoteBlock(@Nonnull String input) {
/* 194 */     return ">>> " + input;
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
/*     */   @Nonnull
/*     */   public static String maskedLink(@Nonnull String text, @Nonnull String url) {
/* 212 */     return "[" + text.replace("]", "\\]") + "](" + url.replace(")", "%29") + ")";
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\MarkdownUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */