/*     */ package org.jsoup.nodes;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import org.jsoup.SerializationException;
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.parser.CharacterReader;
/*     */ import org.jsoup.parser.Parser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Entities
/*     */ {
/*     */   private static final int empty = -1;
/*     */   private static final String emptyName = "";
/*     */   static final int codepointRadix = 36;
/*  27 */   private static final char[] codeDelims = new char[] { ',', ';' };
/*  28 */   private static final HashMap<String, String> multipoints = new HashMap<>();
/*  29 */   private static final Document.OutputSettings DefaultOutput = new Document.OutputSettings();
/*     */ 
/*     */ 
/*     */   
/*     */   public enum EscapeMode
/*     */   {
/*  35 */     xhtml(EntitiesData.xmlPoints, 4),
/*     */ 
/*     */ 
/*     */     
/*  39 */     base(EntitiesData.basePoints, 106),
/*     */ 
/*     */ 
/*     */     
/*  43 */     extended(EntitiesData.fullPoints, 2125);
/*     */     
/*     */     private String[] nameKeys;
/*     */     
/*     */     private int[] codeVals;
/*     */     
/*     */     private int[] codeKeys;
/*     */     
/*     */     private String[] nameVals;
/*     */     
/*     */     EscapeMode(String file, int size) {
/*  54 */       Entities.load(this, file, size);
/*     */     }
/*     */     
/*     */     int codepointForName(String name) {
/*  58 */       int index = Arrays.binarySearch((Object[])this.nameKeys, name);
/*  59 */       return (index >= 0) ? this.codeVals[index] : -1;
/*     */     }
/*     */     
/*     */     String nameForCodepoint(int codepoint) {
/*  63 */       int index = Arrays.binarySearch(this.codeKeys, codepoint);
/*  64 */       if (index >= 0)
/*     */       {
/*     */         
/*  67 */         return (index < this.nameVals.length - 1 && this.codeKeys[index + 1] == codepoint) ? 
/*  68 */           this.nameVals[index + 1] : this.nameVals[index];
/*     */       }
/*  70 */       return "";
/*     */     }
/*     */     
/*     */     private int size() {
/*  74 */       return this.nameKeys.length;
/*     */     }
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
/*     */   public static boolean isNamedEntity(String name) {
/*  88 */     return (EscapeMode.extended.codepointForName(name) != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isBaseNamedEntity(String name) {
/*  99 */     return (EscapeMode.base.codepointForName(name) != -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getByName(String name) {
/* 109 */     String val = multipoints.get(name);
/* 110 */     if (val != null)
/* 111 */       return val; 
/* 112 */     int codepoint = EscapeMode.extended.codepointForName(name);
/* 113 */     if (codepoint != -1)
/* 114 */       return new String(new int[] { codepoint }, 0, 1); 
/* 115 */     return "";
/*     */   }
/*     */   
/*     */   public static int codepointsForName(String name, int[] codepoints) {
/* 119 */     String val = multipoints.get(name);
/* 120 */     if (val != null) {
/* 121 */       codepoints[0] = val.codePointAt(0);
/* 122 */       codepoints[1] = val.codePointAt(1);
/* 123 */       return 2;
/*     */     } 
/* 125 */     int codepoint = EscapeMode.extended.codepointForName(name);
/* 126 */     if (codepoint != -1) {
/* 127 */       codepoints[0] = codepoint;
/* 128 */       return 1;
/*     */     } 
/* 130 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escape(String string, Document.OutputSettings out) {
/* 141 */     if (string == null)
/* 142 */       return ""; 
/* 143 */     StringBuilder accum = StringUtil.borrowBuilder();
/*     */     try {
/* 145 */       escape(accum, string, out, false, false, false, false);
/* 146 */     } catch (IOException e) {
/* 147 */       throw new SerializationException(e);
/*     */     } 
/* 149 */     return StringUtil.releaseBuilder(accum);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escape(String string) {
/* 160 */     return escape(string, DefaultOutput);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void escape(Appendable accum, String string, Document.OutputSettings out, boolean inAttribute, boolean normaliseWhite, boolean stripLeadingWhite, boolean trimTrailing) throws IOException {
/*     */     // Byte code:
/*     */     //   0: iconst_0
/*     */     //   1: istore #7
/*     */     //   3: iconst_0
/*     */     //   4: istore #8
/*     */     //   6: aload_2
/*     */     //   7: invokevirtual escapeMode : ()Lorg/jsoup/nodes/Entities$EscapeMode;
/*     */     //   10: astore #9
/*     */     //   12: aload_2
/*     */     //   13: invokevirtual encoder : ()Ljava/nio/charset/CharsetEncoder;
/*     */     //   16: astore #10
/*     */     //   18: aload_2
/*     */     //   19: getfield coreCharset : Lorg/jsoup/nodes/Entities$CoreCharset;
/*     */     //   22: astore #11
/*     */     //   24: aload_1
/*     */     //   25: invokevirtual length : ()I
/*     */     //   28: istore #12
/*     */     //   30: iconst_0
/*     */     //   31: istore #14
/*     */     //   33: iconst_0
/*     */     //   34: istore #15
/*     */     //   36: iload #15
/*     */     //   38: iload #12
/*     */     //   40: if_icmpge -> 481
/*     */     //   43: aload_1
/*     */     //   44: iload #15
/*     */     //   46: invokevirtual codePointAt : (I)I
/*     */     //   49: istore #13
/*     */     //   51: iload #4
/*     */     //   53: ifeq -> 134
/*     */     //   56: iload #13
/*     */     //   58: invokestatic isWhitespace : (I)Z
/*     */     //   61: ifeq -> 111
/*     */     //   64: iload #5
/*     */     //   66: ifeq -> 77
/*     */     //   69: iload #8
/*     */     //   71: ifne -> 77
/*     */     //   74: goto -> 468
/*     */     //   77: iload #7
/*     */     //   79: ifeq -> 85
/*     */     //   82: goto -> 468
/*     */     //   85: iload #6
/*     */     //   87: ifeq -> 96
/*     */     //   90: iconst_1
/*     */     //   91: istore #14
/*     */     //   93: goto -> 468
/*     */     //   96: aload_0
/*     */     //   97: bipush #32
/*     */     //   99: invokeinterface append : (C)Ljava/lang/Appendable;
/*     */     //   104: pop
/*     */     //   105: iconst_1
/*     */     //   106: istore #7
/*     */     //   108: goto -> 468
/*     */     //   111: iconst_0
/*     */     //   112: istore #7
/*     */     //   114: iconst_1
/*     */     //   115: istore #8
/*     */     //   117: iload #14
/*     */     //   119: ifeq -> 134
/*     */     //   122: aload_0
/*     */     //   123: bipush #32
/*     */     //   125: invokeinterface append : (C)Ljava/lang/Appendable;
/*     */     //   130: pop
/*     */     //   131: iconst_0
/*     */     //   132: istore #14
/*     */     //   134: iload #13
/*     */     //   136: ldc 65536
/*     */     //   138: if_icmpge -> 424
/*     */     //   141: iload #13
/*     */     //   143: i2c
/*     */     //   144: istore #16
/*     */     //   146: iload #16
/*     */     //   148: lookupswitch default -> 382, 9 -> 370, 10 -> 370, 13 -> 370, 34 -> 342, 38 -> 224, 60 -> 268, 62 -> 314, 160 -> 236
/*     */     //   224: aload_0
/*     */     //   225: ldc '&amp;'
/*     */     //   227: invokeinterface append : (Ljava/lang/CharSequence;)Ljava/lang/Appendable;
/*     */     //   232: pop
/*     */     //   233: goto -> 421
/*     */     //   236: aload #9
/*     */     //   238: getstatic org/jsoup/nodes/Entities$EscapeMode.xhtml : Lorg/jsoup/nodes/Entities$EscapeMode;
/*     */     //   241: if_acmpeq -> 256
/*     */     //   244: aload_0
/*     */     //   245: ldc '&nbsp;'
/*     */     //   247: invokeinterface append : (Ljava/lang/CharSequence;)Ljava/lang/Appendable;
/*     */     //   252: pop
/*     */     //   253: goto -> 421
/*     */     //   256: aload_0
/*     */     //   257: ldc '&#xa0;'
/*     */     //   259: invokeinterface append : (Ljava/lang/CharSequence;)Ljava/lang/Appendable;
/*     */     //   264: pop
/*     */     //   265: goto -> 421
/*     */     //   268: iload_3
/*     */     //   269: ifeq -> 290
/*     */     //   272: aload #9
/*     */     //   274: getstatic org/jsoup/nodes/Entities$EscapeMode.xhtml : Lorg/jsoup/nodes/Entities$EscapeMode;
/*     */     //   277: if_acmpeq -> 290
/*     */     //   280: aload_2
/*     */     //   281: invokevirtual syntax : ()Lorg/jsoup/nodes/Document$OutputSettings$Syntax;
/*     */     //   284: getstatic org/jsoup/nodes/Document$OutputSettings$Syntax.xml : Lorg/jsoup/nodes/Document$OutputSettings$Syntax;
/*     */     //   287: if_acmpne -> 302
/*     */     //   290: aload_0
/*     */     //   291: ldc '&lt;'
/*     */     //   293: invokeinterface append : (Ljava/lang/CharSequence;)Ljava/lang/Appendable;
/*     */     //   298: pop
/*     */     //   299: goto -> 421
/*     */     //   302: aload_0
/*     */     //   303: iload #16
/*     */     //   305: invokeinterface append : (C)Ljava/lang/Appendable;
/*     */     //   310: pop
/*     */     //   311: goto -> 421
/*     */     //   314: iload_3
/*     */     //   315: ifne -> 330
/*     */     //   318: aload_0
/*     */     //   319: ldc '&gt;'
/*     */     //   321: invokeinterface append : (Ljava/lang/CharSequence;)Ljava/lang/Appendable;
/*     */     //   326: pop
/*     */     //   327: goto -> 421
/*     */     //   330: aload_0
/*     */     //   331: iload #16
/*     */     //   333: invokeinterface append : (C)Ljava/lang/Appendable;
/*     */     //   338: pop
/*     */     //   339: goto -> 421
/*     */     //   342: iload_3
/*     */     //   343: ifeq -> 358
/*     */     //   346: aload_0
/*     */     //   347: ldc '&quot;'
/*     */     //   349: invokeinterface append : (Ljava/lang/CharSequence;)Ljava/lang/Appendable;
/*     */     //   354: pop
/*     */     //   355: goto -> 421
/*     */     //   358: aload_0
/*     */     //   359: iload #16
/*     */     //   361: invokeinterface append : (C)Ljava/lang/Appendable;
/*     */     //   366: pop
/*     */     //   367: goto -> 421
/*     */     //   370: aload_0
/*     */     //   371: iload #16
/*     */     //   373: invokeinterface append : (C)Ljava/lang/Appendable;
/*     */     //   378: pop
/*     */     //   379: goto -> 421
/*     */     //   382: iload #16
/*     */     //   384: bipush #32
/*     */     //   386: if_icmplt -> 401
/*     */     //   389: aload #11
/*     */     //   391: iload #16
/*     */     //   393: aload #10
/*     */     //   395: invokestatic canEncode : (Lorg/jsoup/nodes/Entities$CoreCharset;CLjava/nio/charset/CharsetEncoder;)Z
/*     */     //   398: ifne -> 412
/*     */     //   401: aload_0
/*     */     //   402: aload #9
/*     */     //   404: iload #13
/*     */     //   406: invokestatic appendEncoded : (Ljava/lang/Appendable;Lorg/jsoup/nodes/Entities$EscapeMode;I)V
/*     */     //   409: goto -> 421
/*     */     //   412: aload_0
/*     */     //   413: iload #16
/*     */     //   415: invokeinterface append : (C)Ljava/lang/Appendable;
/*     */     //   420: pop
/*     */     //   421: goto -> 468
/*     */     //   424: new java/lang/String
/*     */     //   427: dup
/*     */     //   428: iload #13
/*     */     //   430: invokestatic toChars : (I)[C
/*     */     //   433: invokespecial <init> : ([C)V
/*     */     //   436: astore #16
/*     */     //   438: aload #10
/*     */     //   440: aload #16
/*     */     //   442: invokevirtual canEncode : (Ljava/lang/CharSequence;)Z
/*     */     //   445: ifeq -> 460
/*     */     //   448: aload_0
/*     */     //   449: aload #16
/*     */     //   451: invokeinterface append : (Ljava/lang/CharSequence;)Ljava/lang/Appendable;
/*     */     //   456: pop
/*     */     //   457: goto -> 468
/*     */     //   460: aload_0
/*     */     //   461: aload #9
/*     */     //   463: iload #13
/*     */     //   465: invokestatic appendEncoded : (Ljava/lang/Appendable;Lorg/jsoup/nodes/Entities$EscapeMode;I)V
/*     */     //   468: iload #15
/*     */     //   470: iload #13
/*     */     //   472: invokestatic charCount : (I)I
/*     */     //   475: iadd
/*     */     //   476: istore #15
/*     */     //   478: goto -> 36
/*     */     //   481: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #167	-> 0
/*     */     //   #168	-> 3
/*     */     //   #169	-> 6
/*     */     //   #170	-> 12
/*     */     //   #171	-> 18
/*     */     //   #172	-> 24
/*     */     //   #175	-> 30
/*     */     //   #176	-> 33
/*     */     //   #177	-> 43
/*     */     //   #179	-> 51
/*     */     //   #180	-> 56
/*     */     //   #181	-> 64
/*     */     //   #182	-> 77
/*     */     //   #183	-> 85
/*     */     //   #184	-> 90
/*     */     //   #185	-> 93
/*     */     //   #187	-> 96
/*     */     //   #188	-> 105
/*     */     //   #189	-> 108
/*     */     //   #191	-> 111
/*     */     //   #192	-> 114
/*     */     //   #193	-> 117
/*     */     //   #194	-> 122
/*     */     //   #195	-> 131
/*     */     //   #200	-> 134
/*     */     //   #201	-> 141
/*     */     //   #203	-> 146
/*     */     //   #205	-> 224
/*     */     //   #206	-> 233
/*     */     //   #208	-> 236
/*     */     //   #209	-> 244
/*     */     //   #211	-> 256
/*     */     //   #212	-> 265
/*     */     //   #215	-> 268
/*     */     //   #216	-> 290
/*     */     //   #218	-> 302
/*     */     //   #219	-> 311
/*     */     //   #221	-> 314
/*     */     //   #222	-> 318
/*     */     //   #224	-> 330
/*     */     //   #225	-> 339
/*     */     //   #227	-> 342
/*     */     //   #228	-> 346
/*     */     //   #230	-> 358
/*     */     //   #231	-> 367
/*     */     //   #236	-> 370
/*     */     //   #237	-> 379
/*     */     //   #239	-> 382
/*     */     //   #240	-> 401
/*     */     //   #242	-> 412
/*     */     //   #244	-> 421
/*     */     //   #245	-> 424
/*     */     //   #246	-> 438
/*     */     //   #247	-> 448
/*     */     //   #249	-> 460
/*     */     //   #176	-> 468
/*     */     //   #252	-> 481
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   146	275	16	c	C
/*     */     //   438	30	16	c	Ljava/lang/String;
/*     */     //   51	430	13	codePoint	I
/*     */     //   36	445	15	offset	I
/*     */     //   0	482	0	accum	Ljava/lang/Appendable;
/*     */     //   0	482	1	string	Ljava/lang/String;
/*     */     //   0	482	2	out	Lorg/jsoup/nodes/Document$OutputSettings;
/*     */     //   0	482	3	inAttribute	Z
/*     */     //   0	482	4	normaliseWhite	Z
/*     */     //   0	482	5	stripLeadingWhite	Z
/*     */     //   0	482	6	trimTrailing	Z
/*     */     //   3	479	7	lastWasWhite	Z
/*     */     //   6	476	8	reachedNonWhite	Z
/*     */     //   12	470	9	escapeMode	Lorg/jsoup/nodes/Entities$EscapeMode;
/*     */     //   18	464	10	encoder	Ljava/nio/charset/CharsetEncoder;
/*     */     //   24	458	11	coreCharset	Lorg/jsoup/nodes/Entities$CoreCharset;
/*     */     //   30	452	12	length	I
/*     */     //   33	449	14	skipped	Z
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void appendEncoded(Appendable accum, EscapeMode escapeMode, int codePoint) throws IOException {
/* 255 */     String name = escapeMode.nameForCodepoint(codePoint);
/* 256 */     if (!"".equals(name)) {
/* 257 */       accum.append('&').append(name).append(';');
/*     */     } else {
/* 259 */       accum.append("&#x").append(Integer.toHexString(codePoint)).append(';');
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String unescape(String string) {
/* 269 */     return unescape(string, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String unescape(String string, boolean strict) {
/* 280 */     return Parser.unescapeEntities(string, strict);
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
/*     */   private static boolean canEncode(CoreCharset charset, char c, CharsetEncoder fallback) {
/* 298 */     switch (charset) {
/*     */       case ascii:
/* 300 */         return (c < '');
/*     */       case utf:
/* 302 */         return true;
/*     */     } 
/* 304 */     return fallback.canEncode(c);
/*     */   }
/*     */   
/*     */   enum CoreCharset
/*     */   {
/* 309 */     ascii, utf, fallback;
/*     */     
/*     */     static CoreCharset byName(String name) {
/* 312 */       if (name.equals("US-ASCII"))
/* 313 */         return ascii; 
/* 314 */       if (name.startsWith("UTF-"))
/* 315 */         return utf; 
/* 316 */       return fallback;
/*     */     }
/*     */   }
/*     */   
/*     */   private static void load(EscapeMode e, String pointsData, int size) {
/* 321 */     e.nameKeys = new String[size];
/* 322 */     e.codeVals = new int[size];
/* 323 */     e.codeKeys = new int[size];
/* 324 */     e.nameVals = new String[size];
/*     */     
/* 326 */     int i = 0;
/* 327 */     CharacterReader reader = new CharacterReader(pointsData);
/*     */     try {
/* 329 */       while (!reader.isEmpty()) {
/*     */         int cp2;
/*     */         
/* 332 */         String name = reader.consumeTo('=');
/* 333 */         reader.advance();
/* 334 */         int cp1 = Integer.parseInt(reader.consumeToAny(codeDelims), 36);
/* 335 */         char codeDelim = reader.current();
/* 336 */         reader.advance();
/*     */         
/* 338 */         if (codeDelim == ',') {
/* 339 */           cp2 = Integer.parseInt(reader.consumeTo(';'), 36);
/* 340 */           reader.advance();
/*     */         } else {
/* 342 */           cp2 = -1;
/*     */         } 
/* 344 */         String indexS = reader.consumeTo('&');
/* 345 */         int index = Integer.parseInt(indexS, 36);
/* 346 */         reader.advance();
/*     */         
/* 348 */         e.nameKeys[i] = name;
/* 349 */         e.codeVals[i] = cp1;
/* 350 */         e.codeKeys[index] = cp1;
/* 351 */         e.nameVals[index] = name;
/*     */         
/* 353 */         if (cp2 != -1) {
/* 354 */           multipoints.put(name, new String(new int[] { cp1, cp2 }, 0, 2));
/*     */         }
/* 356 */         i++;
/*     */       } 
/*     */       
/* 359 */       Validate.isTrue((i == size), "Unexpected count of entities loaded");
/*     */     } finally {
/* 361 */       reader.close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\nodes\Entities.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */