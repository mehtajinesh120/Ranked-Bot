/*     */ package com.fasterxml.jackson.core.io;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class CharTypes
/*     */ {
/*   7 */   private static final char[] HC = "0123456789ABCDEF".toCharArray(); private static final byte[] HB; private static final int[] sInputCodes;
/*     */   
/*     */   static {
/*  10 */     int len = HC.length;
/*  11 */     HB = new byte[len]; int k;
/*  12 */     for (k = 0; k < len; k++) {
/*  13 */       HB[k] = (byte)HC[k];
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
/*     */ 
/*     */     
/*  28 */     int[] arrayOfInt1 = new int[256];
/*     */     
/*  30 */     for (k = 0; k < 32; k++) {
/*  31 */       arrayOfInt1[k] = -1;
/*     */     }
/*     */     
/*  34 */     arrayOfInt1[34] = 1;
/*  35 */     arrayOfInt1[92] = 1;
/*  36 */     sInputCodes = arrayOfInt1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  45 */     arrayOfInt1 = new int[sInputCodes.length];
/*  46 */     System.arraycopy(sInputCodes, 0, arrayOfInt1, 0, arrayOfInt1.length);
/*  47 */     for (int c = 128; c < 256; c++) {
/*     */       int code;
/*     */ 
/*     */       
/*  51 */       if ((c & 0xE0) == 192) {
/*  52 */         code = 2;
/*  53 */       } else if ((c & 0xF0) == 224) {
/*  54 */         code = 3;
/*  55 */       } else if ((c & 0xF8) == 240) {
/*     */         
/*  57 */         code = 4;
/*     */       } else {
/*     */         
/*  60 */         code = -1;
/*     */       } 
/*  62 */       arrayOfInt1[c] = code;
/*     */     } 
/*  64 */     sInputCodesUTF8 = arrayOfInt1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     arrayOfInt1 = new int[256];
/*     */     
/*  77 */     Arrays.fill(arrayOfInt1, -1);
/*     */     int j;
/*  79 */     for (j = 33; j < 256; j++) {
/*  80 */       if (Character.isJavaIdentifierPart((char)j)) {
/*  81 */         arrayOfInt1[j] = 0;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  87 */     arrayOfInt1[64] = 0;
/*  88 */     arrayOfInt1[35] = 0;
/*  89 */     arrayOfInt1[42] = 0;
/*  90 */     arrayOfInt1[45] = 0;
/*  91 */     arrayOfInt1[43] = 0;
/*  92 */     sInputCodesJsNames = arrayOfInt1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 102 */     arrayOfInt1 = new int[256];
/*     */     
/* 104 */     System.arraycopy(sInputCodesJsNames, 0, arrayOfInt1, 0, arrayOfInt1.length);
/* 105 */     Arrays.fill(arrayOfInt1, 128, 128, 0);
/* 106 */     sInputCodesUtf8JsNames = arrayOfInt1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     int[] buf = new int[256];
/*     */     
/* 117 */     System.arraycopy(sInputCodesUTF8, 128, buf, 128, 128);
/*     */ 
/*     */     
/* 120 */     Arrays.fill(buf, 0, 32, -1);
/* 121 */     buf[9] = 0;
/* 122 */     buf[10] = 10;
/* 123 */     buf[13] = 13;
/* 124 */     buf[42] = 42;
/* 125 */     sInputCodesComment = buf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 136 */     buf = new int[256];
/* 137 */     System.arraycopy(sInputCodesUTF8, 128, buf, 128, 128);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 142 */     Arrays.fill(buf, 0, 32, -1);
/* 143 */     buf[32] = 1;
/* 144 */     buf[9] = 1;
/* 145 */     buf[10] = 10;
/* 146 */     buf[13] = 13;
/* 147 */     buf[47] = 47;
/* 148 */     buf[35] = 35;
/* 149 */     sInputCodesWS = buf;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 158 */     int[] table = new int[128];
/*     */     
/* 160 */     for (j = 0; j < 32; j++)
/*     */     {
/* 162 */       table[j] = -1;
/*     */     }
/*     */     
/* 165 */     table[34] = 34;
/* 166 */     table[92] = 92;
/*     */     
/* 168 */     table[8] = 98;
/* 169 */     table[9] = 116;
/* 170 */     table[12] = 102;
/* 171 */     table[10] = 110;
/* 172 */     table[13] = 114;
/* 173 */     sOutputEscapes128 = table;
/*     */   }
/*     */ 
/*     */   
/*     */   private static final int[] sInputCodesUTF8;
/*     */   private static final int[] sInputCodesJsNames;
/*     */   private static final int[] sInputCodesUtf8JsNames;
/*     */   private static final int[] sInputCodesComment;
/*     */   private static final int[] sInputCodesWS;
/*     */   private static final int[] sOutputEscapes128;
/* 183 */   private static final int[] sHexValues = new int[256];
/*     */   static {
/* 185 */     Arrays.fill(sHexValues, -1); int i;
/* 186 */     for (i = 0; i < 10; i++) {
/* 187 */       sHexValues[48 + i] = i;
/*     */     }
/* 189 */     for (i = 0; i < 6; i++) {
/* 190 */       sHexValues[97 + i] = 10 + i;
/* 191 */       sHexValues[65 + i] = 10 + i;
/*     */     } 
/*     */   }
/*     */   
/* 195 */   public static int[] getInputCodeLatin1() { return sInputCodes; } public static int[] getInputCodeUtf8() {
/* 196 */     return sInputCodesUTF8;
/*     */   }
/* 198 */   public static int[] getInputCodeLatin1JsNames() { return sInputCodesJsNames; } public static int[] getInputCodeUtf8JsNames() {
/* 199 */     return sInputCodesUtf8JsNames;
/*     */   }
/* 201 */   public static int[] getInputCodeComment() { return sInputCodesComment; } public static int[] getInputCodeWS() {
/* 202 */     return sInputCodesWS;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] get7BitOutputEscapes() {
/* 211 */     return sOutputEscapes128;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] get7BitOutputEscapes(int quoteChar) {
/* 220 */     if (quoteChar == 34) {
/* 221 */       return sOutputEscapes128;
/*     */     }
/* 223 */     return AltEscapes.instance.escapesFor(quoteChar);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int charToHex(int ch) {
/* 230 */     return sHexValues[ch & 0xFF];
/*     */   }
/*     */ 
/*     */   
/*     */   public static void appendQuoted(StringBuilder sb, String content) {
/* 235 */     int[] escCodes = sOutputEscapes128;
/* 236 */     int escLen = escCodes.length;
/* 237 */     for (int i = 0, len = content.length(); i < len; i++) {
/* 238 */       char c = content.charAt(i);
/* 239 */       if (c >= escLen || escCodes[c] == 0) {
/* 240 */         sb.append(c);
/*     */       } else {
/*     */         
/* 243 */         sb.append('\\');
/* 244 */         int escCode = escCodes[c];
/* 245 */         if (escCode < 0) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 254 */           sb.append('u');
/* 255 */           sb.append('0');
/* 256 */           sb.append('0');
/* 257 */           int value = c;
/* 258 */           sb.append(HC[value >> 4]);
/* 259 */           sb.append(HC[value & 0xF]);
/*     */         } else {
/* 261 */           sb.append((char)escCode);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public static char[] copyHexChars() {
/* 267 */     return (char[])HC.clone();
/*     */   }
/*     */   
/*     */   public static byte[] copyHexBytes() {
/* 271 */     return (byte[])HB.clone();
/*     */   }
/*     */   
/*     */   private static class AltEscapes
/*     */   {
/* 276 */     public static final AltEscapes instance = new AltEscapes();
/*     */     
/* 278 */     private int[][] _altEscapes = new int[128][];
/*     */     
/*     */     public int[] escapesFor(int quoteChar) {
/* 281 */       int[] esc = this._altEscapes[quoteChar];
/* 282 */       if (esc == null) {
/* 283 */         esc = Arrays.copyOf(CharTypes.sOutputEscapes128, 128);
/*     */         
/* 285 */         if (esc[quoteChar] == 0) {
/* 286 */           esc[quoteChar] = -1;
/*     */         }
/* 288 */         this._altEscapes[quoteChar] = esc;
/*     */       } 
/* 290 */       return esc;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\core\io\CharTypes.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */