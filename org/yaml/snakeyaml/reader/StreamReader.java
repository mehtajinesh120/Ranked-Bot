/*     */ package org.yaml.snakeyaml.reader;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import java.util.Arrays;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.scanner.Constant;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StreamReader
/*     */ {
/*     */   private String name;
/*     */   private final Reader stream;
/*     */   private int[] dataWindow;
/*     */   private int dataLength;
/*  45 */   private int pointer = 0;
/*     */   private boolean eof;
/*  47 */   private int index = 0;
/*  48 */   private int line = 0;
/*  49 */   private int column = 0;
/*     */   
/*     */   private final char[] buffer;
/*     */   
/*     */   private static final int BUFFER_SIZE = 1025;
/*     */   
/*     */   public StreamReader(String stream) {
/*  56 */     this(new StringReader(stream));
/*  57 */     this.name = "'string'";
/*     */   }
/*     */   
/*     */   public StreamReader(Reader reader) {
/*  61 */     this.name = "'reader'";
/*  62 */     this.dataWindow = new int[0];
/*  63 */     this.dataLength = 0;
/*  64 */     this.stream = reader;
/*  65 */     this.eof = false;
/*  66 */     this.buffer = new char[1025];
/*     */   }
/*     */   
/*     */   public static boolean isPrintable(String data) {
/*  70 */     int length = data.length();
/*  71 */     for (int offset = 0; offset < length; ) {
/*  72 */       int codePoint = data.codePointAt(offset);
/*     */       
/*  74 */       if (!isPrintable(codePoint)) {
/*  75 */         return false;
/*     */       }
/*     */       
/*  78 */       offset += Character.charCount(codePoint);
/*     */     } 
/*     */     
/*  81 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isPrintable(int c) {
/*  85 */     return ((c >= 32 && c <= 126) || c == 9 || c == 10 || c == 13 || c == 133 || (c >= 160 && c <= 55295) || (c >= 57344 && c <= 65533) || (c >= 65536 && c <= 1114111));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Mark getMark() {
/*  91 */     return new Mark(this.name, this.index, this.line, this.column, this.dataWindow, this.pointer);
/*     */   }
/*     */   
/*     */   public void forward() {
/*  95 */     forward(1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forward(int length) {
/* 105 */     for (int i = 0; i < length && ensureEnoughData(); i++) {
/* 106 */       int c = this.dataWindow[this.pointer++];
/* 107 */       this.index++;
/* 108 */       if (Constant.LINEBR.has(c) || (c == 13 && 
/* 109 */         ensureEnoughData() && this.dataWindow[this.pointer] != 10)) {
/* 110 */         this.line++;
/* 111 */         this.column = 0;
/* 112 */       } else if (c != 65279) {
/* 113 */         this.column++;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public int peek() {
/* 119 */     return ensureEnoughData() ? this.dataWindow[this.pointer] : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int peek(int index) {
/* 129 */     return ensureEnoughData(index) ? this.dataWindow[this.pointer + index] : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String prefix(int length) {
/* 139 */     if (length == 0)
/* 140 */       return ""; 
/* 141 */     if (ensureEnoughData(length)) {
/* 142 */       return new String(this.dataWindow, this.pointer, length);
/*     */     }
/* 144 */     return new String(this.dataWindow, this.pointer, Math.min(length, this.dataLength - this.pointer));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String prefixForward(int length) {
/* 155 */     String prefix = prefix(length);
/* 156 */     this.pointer += length;
/* 157 */     this.index += length;
/*     */     
/* 159 */     this.column += length;
/* 160 */     return prefix;
/*     */   }
/*     */   
/*     */   private boolean ensureEnoughData() {
/* 164 */     return ensureEnoughData(0);
/*     */   }
/*     */   
/*     */   private boolean ensureEnoughData(int size) {
/* 168 */     if (!this.eof && this.pointer + size >= this.dataLength) {
/* 169 */       update();
/*     */     }
/* 171 */     return (this.pointer + size < this.dataLength);
/*     */   }
/*     */   
/*     */   private void update() {
/*     */     try {
/* 176 */       int read = this.stream.read(this.buffer, 0, 1024);
/* 177 */       if (read > 0) {
/* 178 */         int cpIndex = this.dataLength - this.pointer;
/* 179 */         this.dataWindow = Arrays.copyOfRange(this.dataWindow, this.pointer, this.dataLength + read);
/*     */         
/* 181 */         if (Character.isHighSurrogate(this.buffer[read - 1])) {
/* 182 */           if (this.stream.read(this.buffer, read, 1) == -1) {
/* 183 */             this.eof = true;
/*     */           } else {
/* 185 */             read++;
/*     */           } 
/*     */         }
/*     */         
/* 189 */         int nonPrintable = 32;
/* 190 */         for (int i = 0; i < read; cpIndex++) {
/* 191 */           int codePoint = Character.codePointAt(this.buffer, i);
/* 192 */           this.dataWindow[cpIndex] = codePoint;
/* 193 */           if (isPrintable(codePoint)) {
/* 194 */             i += Character.charCount(codePoint);
/*     */           } else {
/* 196 */             nonPrintable = codePoint;
/* 197 */             i = read;
/*     */           } 
/*     */         } 
/*     */         
/* 201 */         this.dataLength = cpIndex;
/* 202 */         this.pointer = 0;
/* 203 */         if (nonPrintable != 32) {
/* 204 */           throw new ReaderException(this.name, cpIndex - 1, nonPrintable, "special characters are not allowed");
/*     */         }
/*     */       } else {
/*     */         
/* 208 */         this.eof = true;
/*     */       } 
/* 210 */     } catch (IOException ioe) {
/* 211 */       throw new YAMLException(ioe);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColumn() {
/* 217 */     return this.column;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIndex() {
/* 224 */     return this.index;
/*     */   }
/*     */   
/*     */   public int getLine() {
/* 228 */     return this.line;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\reader\StreamReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */