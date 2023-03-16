/*     */ package org.jsoup.parser;
/*     */ 
/*     */ import org.jsoup.helper.Validate;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TokenQueue
/*     */ {
/*     */   private String queue;
/*  13 */   private int pos = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private static final char ESC = '\\';
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenQueue(String data) {
/*  22 */     Validate.notNull(data);
/*  23 */     this.queue = data;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  31 */     return (remainingLength() == 0);
/*     */   }
/*     */   
/*     */   private int remainingLength() {
/*  35 */     return this.queue.length() - this.pos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addFirst(String seq) {
/*  44 */     this.queue = seq + this.queue.substring(this.pos);
/*  45 */     this.pos = 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matches(String seq) {
/*  54 */     return this.queue.regionMatches(true, this.pos, seq, 0, seq.length());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matchesAny(String... seq) {
/*  63 */     for (String s : seq) {
/*  64 */       if (matches(s))
/*  65 */         return true; 
/*     */     } 
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   public boolean matchesAny(char... seq) {
/*  71 */     if (isEmpty()) {
/*  72 */       return false;
/*     */     }
/*  74 */     for (char c : seq) {
/*  75 */       if (this.queue.charAt(this.pos) == c)
/*  76 */         return true; 
/*     */     } 
/*  78 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matchChomp(String seq) {
/*  88 */     if (matches(seq)) {
/*  89 */       this.pos += seq.length();
/*  90 */       return true;
/*     */     } 
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matchesWhitespace() {
/* 101 */     return (!isEmpty() && StringUtil.isWhitespace(this.queue.charAt(this.pos)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean matchesWord() {
/* 109 */     return (!isEmpty() && Character.isLetterOrDigit(this.queue.charAt(this.pos)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void advance() {
/* 116 */     if (!isEmpty()) this.pos++;
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char consume() {
/* 124 */     return this.queue.charAt(this.pos++);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void consume(String seq) {
/* 135 */     if (!matches(seq))
/* 136 */       throw new IllegalStateException("Queue did not match expected sequence"); 
/* 137 */     int len = seq.length();
/* 138 */     if (len > remainingLength()) {
/* 139 */       throw new IllegalStateException("Queue not long enough to consume sequence");
/*     */     }
/* 141 */     this.pos += len;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String consumeTo(String seq) {
/* 150 */     int offset = this.queue.indexOf(seq, this.pos);
/* 151 */     if (offset != -1) {
/* 152 */       String consumed = this.queue.substring(this.pos, offset);
/* 153 */       this.pos += consumed.length();
/* 154 */       return consumed;
/*     */     } 
/* 156 */     return remainder();
/*     */   }
/*     */ 
/*     */   
/*     */   public String consumeToIgnoreCase(String seq) {
/* 161 */     int start = this.pos;
/* 162 */     String first = seq.substring(0, 1);
/* 163 */     boolean canScan = first.toLowerCase().equals(first.toUpperCase());
/* 164 */     while (!isEmpty() && 
/* 165 */       !matches(seq)) {
/*     */ 
/*     */       
/* 168 */       if (canScan) {
/* 169 */         int skip = this.queue.indexOf(first, this.pos) - this.pos;
/* 170 */         if (skip == 0) {
/* 171 */           this.pos++; continue;
/* 172 */         }  if (skip < 0) {
/* 173 */           this.pos = this.queue.length(); continue;
/*     */         } 
/* 175 */         this.pos += skip;
/*     */         continue;
/*     */       } 
/* 178 */       this.pos++;
/*     */     } 
/*     */     
/* 181 */     return this.queue.substring(start, this.pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String consumeToAny(String... seq) {
/* 192 */     int start = this.pos;
/* 193 */     while (!isEmpty() && !matchesAny(seq)) {
/* 194 */       this.pos++;
/*     */     }
/*     */     
/* 197 */     return this.queue.substring(start, this.pos);
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
/*     */   public String chompTo(String seq) {
/* 209 */     String data = consumeTo(seq);
/* 210 */     matchChomp(seq);
/* 211 */     return data;
/*     */   }
/*     */   
/*     */   public String chompToIgnoreCase(String seq) {
/* 215 */     String data = consumeToIgnoreCase(seq);
/* 216 */     matchChomp(seq);
/* 217 */     return data;
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
/*     */   public String chompBalanced(char open, char close) {
/*     */     // Byte code:
/*     */     //   0: iconst_m1
/*     */     //   1: istore_3
/*     */     //   2: iconst_m1
/*     */     //   3: istore #4
/*     */     //   5: iconst_0
/*     */     //   6: istore #5
/*     */     //   8: iconst_0
/*     */     //   9: istore #6
/*     */     //   11: iconst_0
/*     */     //   12: istore #7
/*     */     //   14: iconst_0
/*     */     //   15: istore #8
/*     */     //   17: iconst_0
/*     */     //   18: istore #9
/*     */     //   20: aload_0
/*     */     //   21: invokevirtual isEmpty : ()Z
/*     */     //   24: ifeq -> 30
/*     */     //   27: goto -> 210
/*     */     //   30: aload_0
/*     */     //   31: invokevirtual consume : ()C
/*     */     //   34: istore #10
/*     */     //   36: iload #6
/*     */     //   38: bipush #92
/*     */     //   40: if_icmpeq -> 162
/*     */     //   43: iload #10
/*     */     //   45: bipush #39
/*     */     //   47: if_icmpne -> 76
/*     */     //   50: iload #10
/*     */     //   52: iload_1
/*     */     //   53: if_icmpeq -> 76
/*     */     //   56: iload #8
/*     */     //   58: ifne -> 76
/*     */     //   61: iload #7
/*     */     //   63: ifne -> 70
/*     */     //   66: iconst_1
/*     */     //   67: goto -> 71
/*     */     //   70: iconst_0
/*     */     //   71: istore #7
/*     */     //   73: goto -> 106
/*     */     //   76: iload #10
/*     */     //   78: bipush #34
/*     */     //   80: if_icmpne -> 106
/*     */     //   83: iload #10
/*     */     //   85: iload_1
/*     */     //   86: if_icmpeq -> 106
/*     */     //   89: iload #7
/*     */     //   91: ifne -> 106
/*     */     //   94: iload #8
/*     */     //   96: ifne -> 103
/*     */     //   99: iconst_1
/*     */     //   100: goto -> 104
/*     */     //   103: iconst_0
/*     */     //   104: istore #8
/*     */     //   106: iload #7
/*     */     //   108: ifne -> 121
/*     */     //   111: iload #8
/*     */     //   113: ifne -> 121
/*     */     //   116: iload #9
/*     */     //   118: ifeq -> 128
/*     */     //   121: iload #10
/*     */     //   123: istore #6
/*     */     //   125: goto -> 205
/*     */     //   128: iload #10
/*     */     //   130: iload_1
/*     */     //   131: if_icmpne -> 150
/*     */     //   134: iinc #5, 1
/*     */     //   137: iload_3
/*     */     //   138: iconst_m1
/*     */     //   139: if_icmpne -> 185
/*     */     //   142: aload_0
/*     */     //   143: getfield pos : I
/*     */     //   146: istore_3
/*     */     //   147: goto -> 185
/*     */     //   150: iload #10
/*     */     //   152: iload_2
/*     */     //   153: if_icmpne -> 185
/*     */     //   156: iinc #5, -1
/*     */     //   159: goto -> 185
/*     */     //   162: iload #10
/*     */     //   164: bipush #81
/*     */     //   166: if_icmpne -> 175
/*     */     //   169: iconst_1
/*     */     //   170: istore #9
/*     */     //   172: goto -> 185
/*     */     //   175: iload #10
/*     */     //   177: bipush #69
/*     */     //   179: if_icmpne -> 185
/*     */     //   182: iconst_0
/*     */     //   183: istore #9
/*     */     //   185: iload #5
/*     */     //   187: ifle -> 201
/*     */     //   190: iload #6
/*     */     //   192: ifeq -> 201
/*     */     //   195: aload_0
/*     */     //   196: getfield pos : I
/*     */     //   199: istore #4
/*     */     //   201: iload #10
/*     */     //   203: istore #6
/*     */     //   205: iload #5
/*     */     //   207: ifgt -> 20
/*     */     //   210: iload #4
/*     */     //   212: iflt -> 228
/*     */     //   215: aload_0
/*     */     //   216: getfield queue : Ljava/lang/String;
/*     */     //   219: iload_3
/*     */     //   220: iload #4
/*     */     //   222: invokevirtual substring : (II)Ljava/lang/String;
/*     */     //   225: goto -> 230
/*     */     //   228: ldc ''
/*     */     //   230: astore #10
/*     */     //   232: iload #5
/*     */     //   234: ifle -> 265
/*     */     //   237: new java/lang/StringBuilder
/*     */     //   240: dup
/*     */     //   241: invokespecial <init> : ()V
/*     */     //   244: ldc 'Did not find balanced marker at ''
/*     */     //   246: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   249: aload #10
/*     */     //   251: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   254: ldc '''
/*     */     //   256: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   259: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   262: invokestatic fail : (Ljava/lang/String;)V
/*     */     //   265: aload #10
/*     */     //   267: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #230	-> 0
/*     */     //   #231	-> 2
/*     */     //   #232	-> 5
/*     */     //   #233	-> 8
/*     */     //   #234	-> 11
/*     */     //   #235	-> 14
/*     */     //   #236	-> 17
/*     */     //   #239	-> 20
/*     */     //   #240	-> 30
/*     */     //   #241	-> 36
/*     */     //   #242	-> 43
/*     */     //   #243	-> 61
/*     */     //   #244	-> 76
/*     */     //   #245	-> 94
/*     */     //   #246	-> 106
/*     */     //   #247	-> 121
/*     */     //   #248	-> 125
/*     */     //   #251	-> 128
/*     */     //   #252	-> 134
/*     */     //   #253	-> 137
/*     */     //   #254	-> 142
/*     */     //   #256	-> 150
/*     */     //   #257	-> 156
/*     */     //   #258	-> 162
/*     */     //   #259	-> 169
/*     */     //   #260	-> 175
/*     */     //   #261	-> 182
/*     */     //   #264	-> 185
/*     */     //   #265	-> 195
/*     */     //   #266	-> 201
/*     */     //   #267	-> 205
/*     */     //   #268	-> 210
/*     */     //   #269	-> 232
/*     */     //   #270	-> 237
/*     */     //   #272	-> 265
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   36	169	10	c	C
/*     */     //   0	268	0	this	Lorg/jsoup/parser/TokenQueue;
/*     */     //   0	268	1	open	C
/*     */     //   0	268	2	close	C
/*     */     //   2	266	3	start	I
/*     */     //   5	263	4	end	I
/*     */     //   8	260	5	depth	I
/*     */     //   11	257	6	last	C
/*     */     //   14	254	7	inSingleQuote	Z
/*     */     //   17	251	8	inDoubleQuote	Z
/*     */     //   20	248	9	inRegexQE	Z
/*     */     //   232	36	10	out	Ljava/lang/String;
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
/*     */   public static String unescape(String in) {
/* 281 */     StringBuilder out = StringUtil.borrowBuilder();
/* 282 */     char last = Character.MIN_VALUE;
/* 283 */     for (char c : in.toCharArray()) {
/* 284 */       if (c == '\\') {
/* 285 */         if (last == '\\') {
/* 286 */           out.append(c);
/* 287 */           c = Character.MIN_VALUE;
/*     */         } 
/*     */       } else {
/*     */         
/* 291 */         out.append(c);
/* 292 */       }  last = c;
/*     */     } 
/* 294 */     return StringUtil.releaseBuilder(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escapeCssIdentifier(String in) {
/* 302 */     StringBuilder out = StringUtil.borrowBuilder();
/* 303 */     TokenQueue q = new TokenQueue(in);
/* 304 */     while (!q.isEmpty()) {
/* 305 */       if (q.matchesCssIdentifier(ElementSelectorChars)) {
/* 306 */         out.append(q.consume()); continue;
/*     */       } 
/* 308 */       out.append('\\').append(q.consume());
/*     */     } 
/*     */     
/* 311 */     return StringUtil.releaseBuilder(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean consumeWhitespace() {
/* 319 */     boolean seen = false;
/* 320 */     while (matchesWhitespace()) {
/* 321 */       this.pos++;
/* 322 */       seen = true;
/*     */     } 
/* 324 */     return seen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String consumeWord() {
/* 332 */     int start = this.pos;
/* 333 */     while (matchesWord())
/* 334 */       this.pos++; 
/* 335 */     return this.queue.substring(start, this.pos);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String consumeElementSelector() {
/* 345 */     return consumeEscapedCssIdentifier(ElementSelectorChars);
/*     */   }
/* 347 */   private static final String[] ElementSelectorChars = new String[] { "*|", "|", "_", "-" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String consumeCssIdentifier() {
/* 355 */     return consumeEscapedCssIdentifier(CssIdentifierChars);
/*     */   }
/* 357 */   private static final String[] CssIdentifierChars = new String[] { "-", "_" };
/*     */ 
/*     */   
/*     */   private String consumeEscapedCssIdentifier(String... matches) {
/* 361 */     int start = this.pos;
/* 362 */     boolean escaped = false;
/* 363 */     while (!isEmpty()) {
/* 364 */       if (this.queue.charAt(this.pos) == '\\' && remainingLength() > 1) {
/* 365 */         escaped = true;
/* 366 */         this.pos += 2; continue;
/* 367 */       }  if (matchesCssIdentifier(matches)) {
/* 368 */         this.pos++;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 374 */     String consumed = this.queue.substring(start, this.pos);
/* 375 */     return escaped ? unescape(consumed) : consumed;
/*     */   }
/*     */   
/*     */   private boolean matchesCssIdentifier(String... matches) {
/* 379 */     return (matchesWord() || matchesAny(matches));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String remainder() {
/* 387 */     String remainder = this.queue.substring(this.pos);
/* 388 */     this.pos = this.queue.length();
/* 389 */     return remainder;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 394 */     return this.queue.substring(this.pos);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\TokenQueue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */