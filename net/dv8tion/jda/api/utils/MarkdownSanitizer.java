/*     */ package net.dv8tion.jda.api.utils;
/*     */ 
/*     */ import gnu.trove.map.TIntObjectMap;
/*     */ import gnu.trove.map.hash.TIntObjectHashMap;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.annotation.Nonnull;
/*     */ import javax.annotation.Nullable;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MarkdownSanitizer
/*     */ {
/*     */   public static final int NORMAL = 0;
/*     */   public static final int BOLD = 1;
/*     */   public static final int ITALICS_U = 2;
/*     */   public static final int ITALICS_A = 4;
/*     */   public static final int MONO = 8;
/*     */   public static final int MONO_TWO = 16;
/*     */   public static final int BLOCK = 32;
/*     */   public static final int SPOILER = 64;
/*     */   public static final int UNDERLINE = 128;
/*     */   public static final int STRIKE = 256;
/*     */   public static final int QUOTE = 512;
/*     */   public static final int QUOTE_BLOCK = 1024;
/*     */   private static final int ESCAPED_BOLD = -2147483647;
/*     */   private static final int ESCAPED_ITALICS_U = -2147483646;
/*     */   private static final int ESCAPED_ITALICS_A = -2147483644;
/*     */   private static final int ESCAPED_MONO = -2147483640;
/*     */   private static final int ESCAPED_MONO_TWO = -2147483632;
/*     */   private static final int ESCAPED_BLOCK = -2147483616;
/*     */   private static final int ESCAPED_SPOILER = -2147483584;
/*     */   private static final int ESCAPED_UNDERLINE = -2147483520;
/*     */   private static final int ESCAPED_STRIKE = -2147483392;
/*     */   private static final int ESCAPED_QUOTE = -2147483136;
/*     */   private static final int ESCAPED_QUOTE_BLOCK = -2147482624;
/*  73 */   private static final Pattern codeLanguage = Pattern.compile("^\\w+\n.*", 40);
/*  74 */   private static final Pattern quote = Pattern.compile("> +.*", 40);
/*  75 */   private static final Pattern quoteBlock = Pattern.compile(">>>\\s+\\S.*", 40);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  81 */   private static final TIntObjectMap<String> tokens = (TIntObjectMap<String>)new TIntObjectHashMap(); static {
/*  82 */     tokens.put(0, "");
/*  83 */     tokens.put(1, "**");
/*  84 */     tokens.put(2, "_");
/*  85 */     tokens.put(4, "*");
/*  86 */     tokens.put(5, "***");
/*  87 */     tokens.put(8, "`");
/*  88 */     tokens.put(16, "``");
/*  89 */     tokens.put(32, "```");
/*  90 */     tokens.put(64, "||");
/*  91 */     tokens.put(128, "__");
/*  92 */     tokens.put(256, "~~");
/*     */   }
/*     */ 
/*     */   
/*     */   private int ignored;
/*     */   private SanitizationStrategy strategy;
/*     */   
/*     */   public MarkdownSanitizer() {
/* 100 */     this.ignored = 0;
/* 101 */     this.strategy = SanitizationStrategy.REMOVE;
/*     */   }
/*     */ 
/*     */   
/*     */   public MarkdownSanitizer(int ignored, @Nullable SanitizationStrategy strategy) {
/* 106 */     this.ignored = ignored;
/* 107 */     this.strategy = (strategy == null) ? SanitizationStrategy.REMOVE : strategy;
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
/*     */   public static String sanitize(@Nonnull String sequence) {
/* 122 */     return sanitize(sequence, SanitizationStrategy.REMOVE);
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
/*     */   public static String sanitize(@Nonnull String sequence, @Nonnull SanitizationStrategy strategy) {
/* 144 */     Checks.notNull(sequence, "String");
/* 145 */     Checks.notNull(strategy, "Strategy");
/* 146 */     return (new MarkdownSanitizer()).withStrategy(strategy).compute(sequence);
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
/*     */   public static String escape(@Nonnull String sequence) {
/* 165 */     return escape(sequence, 0);
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
/*     */   public static String escape(@Nonnull String sequence, int ignored) {
/* 185 */     return (new MarkdownSanitizer())
/* 186 */       .withIgnored(ignored)
/* 187 */       .withStrategy(SanitizationStrategy.ESCAPE)
/* 188 */       .compute(sequence);
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
/*     */   public static String escape(@Nonnull String sequence, boolean single) {
/* 208 */     Checks.notNull(sequence, "Input");
/* 209 */     if (!single) return escape(sequence);
/*     */     
/* 211 */     StringBuilder builder = new StringBuilder();
/* 212 */     boolean escaped = false;
/* 213 */     boolean newline = true;
/* 214 */     for (int i = 0; i < sequence.length(); i++) {
/*     */       
/* 216 */       char current = sequence.charAt(i);
/* 217 */       if (newline) {
/*     */         
/* 219 */         newline = Character.isWhitespace(current);
/* 220 */         if (current == '>') {
/*     */ 
/*     */           
/* 223 */           if (i + 1 < sequence.length() && Character.isWhitespace(sequence.charAt(i + 1))) {
/*     */             
/* 225 */             builder.append("\\>");
/*     */           }
/* 227 */           else if (i + 3 < sequence.length() && sequence.startsWith(">>>", i) && Character.isWhitespace(sequence.charAt(i + 3))) {
/*     */             
/* 229 */             builder.append("\\>\\>\\>").append(sequence.charAt(i + 3));
/* 230 */             i += 3;
/*     */           }
/*     */           else {
/*     */             
/* 234 */             builder.append(current);
/*     */           } 
/*     */           
/*     */           continue;
/*     */         } 
/*     */       } 
/* 240 */       if (escaped) {
/*     */         
/* 242 */         builder.append(current);
/* 243 */         escaped = false;
/*     */       }
/*     */       else {
/*     */         
/* 247 */         switch (current) {
/*     */           
/*     */           case '*':
/*     */           case '_':
/*     */           case '`':
/* 252 */             builder.append('\\').append(current);
/*     */             break;
/*     */           case '|':
/*     */           case '~':
/* 256 */             if (i + 1 < sequence.length() && sequence.charAt(i + 1) == current) {
/*     */               
/* 258 */               builder.append('\\').append(current)
/* 259 */                 .append('\\').append(current);
/* 260 */               i++;
/*     */               break;
/*     */             } 
/* 263 */             builder.append(current);
/*     */             break;
/*     */           case '\\':
/* 266 */             builder.append(current);
/* 267 */             escaped = true;
/*     */             break;
/*     */           case '\n':
/* 270 */             builder.append(current);
/* 271 */             newline = true;
/*     */             break;
/*     */           default:
/* 274 */             builder.append(current); break;
/*     */         } 
/*     */       }  continue;
/* 277 */     }  return builder.toString();
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
/*     */   @Nonnull
/*     */   public MarkdownSanitizer withStrategy(@Nonnull SanitizationStrategy strategy) {
/* 294 */     Checks.notNull(strategy, "Strategy");
/* 295 */     this.strategy = strategy;
/* 296 */     return this;
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
/*     */   public MarkdownSanitizer withIgnored(int ignored) {
/* 311 */     this.ignored |= ignored;
/* 312 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   private int getRegion(int index, @Nonnull String sequence) {
/* 317 */     if (sequence.length() - index >= 3) {
/*     */       
/* 319 */       String threeChars = sequence.substring(index, index + 3);
/* 320 */       switch (threeChars) {
/*     */         
/*     */         case "```":
/* 323 */           return doesEscape(index, sequence) ? -2147483616 : 32;
/*     */         case "***":
/* 325 */           return doesEscape(index, sequence) ? -2147483643 : 5;
/*     */       } 
/*     */     } 
/* 328 */     if (sequence.length() - index >= 2) {
/*     */       
/* 330 */       String twoChars = sequence.substring(index, index + 2);
/* 331 */       switch (twoChars) {
/*     */         
/*     */         case "**":
/* 334 */           return doesEscape(index, sequence) ? -2147483647 : 1;
/*     */         case "__":
/* 336 */           return doesEscape(index, sequence) ? -2147483520 : 128;
/*     */         case "~~":
/* 338 */           return doesEscape(index, sequence) ? -2147483392 : 256;
/*     */         case "``":
/* 340 */           return doesEscape(index, sequence) ? -2147483632 : 16;
/*     */         case "||":
/* 342 */           return doesEscape(index, sequence) ? -2147483584 : 64;
/*     */       } 
/*     */     } 
/* 345 */     char current = sequence.charAt(index);
/* 346 */     switch (current) {
/*     */       
/*     */       case '*':
/* 349 */         return doesEscape(index, sequence) ? -2147483644 : 4;
/*     */       case '_':
/* 351 */         return doesEscape(index, sequence) ? -2147483646 : 2;
/*     */       case '`':
/* 353 */         return doesEscape(index, sequence) ? -2147483640 : 8;
/*     */     } 
/* 355 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean hasCollision(int index, @Nonnull String sequence, char c) {
/* 360 */     if (index < 0)
/* 361 */       return false; 
/* 362 */     return (index < sequence.length() - 1 && sequence.charAt(index + 1) == c);
/*     */   }
/*     */ 
/*     */   
/*     */   private int findEndIndex(int afterIndex, int region, @Nonnull String sequence) {
/* 367 */     if (isEscape(region))
/* 368 */       return -1; 
/* 369 */     int lastMatch = afterIndex + getDelta(region) + 1;
/* 370 */     while (lastMatch != -1) {
/*     */       
/* 372 */       switch (region) {
/*     */         
/*     */         case 5:
/* 375 */           lastMatch = sequence.indexOf("***", lastMatch);
/*     */           break;
/*     */         case 1:
/* 378 */           lastMatch = sequence.indexOf("**", lastMatch);
/* 379 */           if (lastMatch != -1 && hasCollision(lastMatch + 1, sequence, '*')) {
/*     */             
/* 381 */             lastMatch += 3;
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         case 4:
/* 386 */           lastMatch = sequence.indexOf('*', lastMatch);
/* 387 */           if (lastMatch != -1 && hasCollision(lastMatch, sequence, '*')) {
/*     */             
/* 389 */             if (hasCollision(lastMatch + 1, sequence, '*')) {
/* 390 */               lastMatch += 3; continue;
/*     */             } 
/* 392 */             lastMatch += 2;
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         case 128:
/* 397 */           lastMatch = sequence.indexOf("__", lastMatch);
/*     */           break;
/*     */         case 2:
/* 400 */           lastMatch = sequence.indexOf('_', lastMatch);
/* 401 */           if (lastMatch != -1 && hasCollision(lastMatch, sequence, '_')) {
/*     */             
/* 403 */             lastMatch += 2;
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         case 64:
/* 408 */           lastMatch = sequence.indexOf("||", lastMatch);
/*     */           break;
/*     */         case 32:
/* 411 */           lastMatch = sequence.indexOf("```", lastMatch);
/*     */           break;
/*     */         case 16:
/* 414 */           lastMatch = sequence.indexOf("``", lastMatch);
/* 415 */           if (lastMatch != -1 && hasCollision(lastMatch + 1, sequence, '`')) {
/*     */             
/* 417 */             lastMatch += 3;
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         case 8:
/* 422 */           lastMatch = sequence.indexOf('`', lastMatch);
/* 423 */           if (lastMatch != -1 && hasCollision(lastMatch, sequence, '`')) {
/*     */             
/* 425 */             if (hasCollision(lastMatch + 1, sequence, '`')) {
/* 426 */               lastMatch += 3; continue;
/*     */             } 
/* 428 */             lastMatch += 2;
/*     */             continue;
/*     */           } 
/*     */           break;
/*     */         case 256:
/* 433 */           lastMatch = sequence.indexOf("~~", lastMatch);
/*     */           break;
/*     */         default:
/* 436 */           return -1;
/*     */       } 
/* 438 */       if (lastMatch == -1 || !doesEscape(lastMatch, sequence))
/* 439 */         return lastMatch; 
/* 440 */       lastMatch++;
/*     */     } 
/* 442 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   private String handleRegion(int start, int end, @Nonnull String sequence, int region) {
/* 448 */     String resolved = sequence.substring(start, end);
/* 449 */     switch (region) {
/*     */       
/*     */       case 8:
/*     */       case 16:
/*     */       case 32:
/* 454 */         return resolved;
/*     */     } 
/* 456 */     return (new MarkdownSanitizer(this.ignored, this.strategy)).compute(resolved);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int getDelta(int region) {
/* 462 */     switch (region) {
/*     */       
/*     */       case -2147483643:
/*     */       case -2147483616:
/*     */       case 5:
/*     */       case 32:
/* 468 */         return 3;
/*     */       case -2147483647:
/*     */       case -2147483632:
/*     */       case -2147483584:
/*     */       case -2147483520:
/*     */       case -2147483392:
/*     */       case 1:
/*     */       case 16:
/*     */       case 64:
/*     */       case 128:
/*     */       case 256:
/* 479 */         return 2;
/*     */       case 8:
/*     */       case -2147483646:
/*     */       case -2147483644:
/*     */       case -2147483640:
/*     */       case -2147483136:
/*     */       case 2:
/*     */       case 4:
/* 487 */         return 1;
/*     */     } 
/* 489 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void applyStrategy(int region, @Nonnull String seq, @Nonnull StringBuilder builder) {
/* 495 */     if (this.strategy == SanitizationStrategy.REMOVE) {
/*     */       
/* 497 */       if (codeLanguage.matcher(seq).matches()) {
/* 498 */         builder.append(seq.substring(seq.indexOf("\n") + 1));
/*     */       } else {
/* 500 */         builder.append(seq);
/*     */       }  return;
/*     */     } 
/* 503 */     String token = (String)tokens.get(region);
/* 504 */     if (token == null)
/* 505 */       throw new IllegalStateException("Found illegal region for strategy ESCAPE '" + region + "' with no known format token!"); 
/* 506 */     if (region == 128) {
/* 507 */       token = "_\\_";
/* 508 */     } else if (region == 1) {
/* 509 */       token = "*\\*";
/* 510 */     } else if (region == 5) {
/* 511 */       token = "*\\*\\*";
/* 512 */     }  builder.append("\\").append(token)
/* 513 */       .append(seq)
/* 514 */       .append("\\").append(token);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean doesEscape(int index, @Nonnull String seq) {
/* 519 */     int backslashes = 0;
/* 520 */     for (int i = index - 1; i > -1; i--) {
/*     */       
/* 522 */       if (seq.charAt(i) != '\\')
/*     */         break; 
/* 524 */       backslashes++;
/*     */     } 
/* 526 */     return (backslashes % 2 != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isEscape(int region) {
/* 531 */     return ((Integer.MIN_VALUE & region) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isIgnored(int nextRegion) {
/* 536 */     return ((nextRegion & this.ignored) == nextRegion);
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
/*     */   public String compute(@Nonnull String sequence) {
/* 555 */     Checks.notNull(sequence, "Input");
/* 556 */     StringBuilder builder = new StringBuilder();
/* 557 */     String end = handleQuote(sequence, false);
/* 558 */     if (end != null) return end; 
/*     */     int i;
/* 560 */     for (i = 0; i < sequence.length(); ) {
/*     */       
/* 562 */       int nextRegion = getRegion(i, sequence);
/* 563 */       if (nextRegion == 0) {
/*     */         
/* 565 */         if (sequence.charAt(i) == '\n' && i + 1 < sequence.length()) {
/*     */           
/* 567 */           String result = handleQuote(sequence.substring(i + 1), true);
/* 568 */           if (result != null) {
/* 569 */             return builder.append(result).toString();
/*     */           }
/*     */         } 
/* 572 */         builder.append(sequence.charAt(i++));
/*     */         
/*     */         continue;
/*     */       } 
/* 576 */       int endRegion = findEndIndex(i, nextRegion, sequence);
/* 577 */       if (isIgnored(nextRegion) || endRegion == -1) {
/*     */         
/* 579 */         int k = getDelta(nextRegion);
/* 580 */         for (int j = 0; j < k; j++)
/* 581 */           builder.append(sequence.charAt(i++)); 
/*     */         continue;
/*     */       } 
/* 584 */       int delta = getDelta(nextRegion);
/* 585 */       applyStrategy(nextRegion, handleRegion(i + delta, endRegion, sequence, nextRegion), builder);
/* 586 */       i = endRegion + delta;
/*     */     } 
/* 588 */     return builder.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private String handleQuote(@Nonnull String sequence, boolean newline) {
/* 594 */     if (!isIgnored(512) && quote.matcher(sequence).matches()) {
/*     */       
/* 596 */       int start = sequence.indexOf('>');
/* 597 */       if (start < 0)
/* 598 */         start = 0; 
/* 599 */       StringBuilder builder = new StringBuilder(compute(sequence.substring(start + 2)));
/* 600 */       if (this.strategy == SanitizationStrategy.ESCAPE)
/* 601 */         builder.insert(0, "\\> "); 
/* 602 */       if (newline)
/* 603 */         builder.insert(0, '\n'); 
/* 604 */       return builder.toString();
/*     */     } 
/*     */     
/* 607 */     if (!isIgnored(1024) && quoteBlock.matcher(sequence).matches()) {
/*     */       
/* 609 */       if (this.strategy == SanitizationStrategy.ESCAPE)
/* 610 */         return compute("\\".concat(sequence)); 
/* 611 */       return compute(sequence.substring(4));
/*     */     } 
/* 613 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public enum SanitizationStrategy
/*     */   {
/* 622 */     REMOVE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 628 */     ESCAPE;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\MarkdownSanitizer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */