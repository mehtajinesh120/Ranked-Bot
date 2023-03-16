/*     */ package org.jsoup.helper;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.CharArrayReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.IllegalCharsetNameException;
/*     */ import java.util.Locale;
/*     */ import java.util.Random;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.WillClose;
/*     */ import org.jsoup.UncheckedIOException;
/*     */ import org.jsoup.internal.ConstrainableInputStream;
/*     */ import org.jsoup.internal.Normalizer;
/*     */ import org.jsoup.internal.StringUtil;
/*     */ import org.jsoup.nodes.Comment;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.jsoup.nodes.Node;
/*     */ import org.jsoup.nodes.XmlDeclaration;
/*     */ import org.jsoup.parser.Parser;
/*     */ import org.jsoup.select.Elements;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DataUtil
/*     */ {
/*  43 */   private static final Pattern charsetPattern = Pattern.compile("(?i)\\bcharset=\\s*(?:[\"'])?([^\\s,;\"']*)");
/*  44 */   public static final Charset UTF_8 = Charset.forName("UTF-8");
/*  45 */   static final String defaultCharsetName = UTF_8.name();
/*     */   private static final int firstReadBufferSize = 5120;
/*     */   static final int bufferSize = 32768;
/*  48 */   private static final char[] mimeBoundaryChars = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
/*  49 */     .toCharArray();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final int boundaryLength = 32;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Document load(File file, @Nullable String charsetName, String baseUri) throws IOException {
/*  66 */     return load(file, charsetName, baseUri, Parser.htmlParser());
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
/*     */   public static Document load(File file, @Nullable String charsetName, String baseUri, Parser parser) throws IOException {
/*  84 */     InputStream stream = new FileInputStream(file);
/*  85 */     String name = Normalizer.lowerCase(file.getName());
/*  86 */     if (name.endsWith(".gz") || name.endsWith(".z")) {
/*     */       boolean zipped;
/*     */       
/*     */       try {
/*  90 */         zipped = (stream.read() == 31 && stream.read() == 139);
/*     */       } finally {
/*  92 */         stream.close();
/*     */       } 
/*     */       
/*  95 */       stream = zipped ? new GZIPInputStream(new FileInputStream(file)) : new FileInputStream(file);
/*     */     } 
/*  97 */     return parseInputStream(stream, charsetName, baseUri, parser);
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
/*     */   public static Document load(@WillClose InputStream in, @Nullable String charsetName, String baseUri) throws IOException {
/* 109 */     return parseInputStream(in, charsetName, baseUri, Parser.htmlParser());
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
/*     */   public static Document load(@WillClose InputStream in, @Nullable String charsetName, String baseUri, Parser parser) throws IOException {
/* 122 */     return parseInputStream(in, charsetName, baseUri, parser);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void crossStreams(InputStream in, OutputStream out) throws IOException {
/* 132 */     byte[] buffer = new byte[32768];
/*     */     int len;
/* 134 */     while ((len = in.read(buffer)) != -1) {
/* 135 */       out.write(buffer, 0, len);
/*     */     }
/*     */   }
/*     */   
/*     */   static Document parseInputStream(@Nullable @WillClose InputStream input, @Nullable String charsetName, String baseUri, Parser parser) throws IOException {
/* 140 */     if (input == null)
/* 141 */       return new Document(baseUri); 
/* 142 */     ConstrainableInputStream constrainableInputStream = ConstrainableInputStream.wrap(input, 32768, 0);
/*     */     
/* 144 */     Document doc = null;
/*     */ 
/*     */     
/*     */     try {
/* 148 */       constrainableInputStream.mark(32768);
/* 149 */       ByteBuffer firstBytes = readToByteBuffer((InputStream)constrainableInputStream, 5119);
/* 150 */       boolean fullyRead = (constrainableInputStream.read() == -1);
/* 151 */       constrainableInputStream.reset();
/*     */ 
/*     */       
/* 154 */       BomCharset bomCharset = detectCharsetFromBom(firstBytes);
/* 155 */       if (bomCharset != null) {
/* 156 */         charsetName = bomCharset.charset;
/*     */       }
/* 158 */       if (charsetName == null) {
/*     */         try {
/* 160 */           CharBuffer defaultDecoded = UTF_8.decode(firstBytes);
/* 161 */           if (defaultDecoded.hasArray())
/* 162 */           { doc = parser.parseInput(new CharArrayReader(defaultDecoded.array(), defaultDecoded.arrayOffset(), defaultDecoded.limit()), baseUri); }
/*     */           else
/* 164 */           { doc = parser.parseInput(defaultDecoded.toString(), baseUri); } 
/* 165 */         } catch (UncheckedIOException e) {
/* 166 */           throw e.ioException();
/*     */         } 
/*     */ 
/*     */         
/* 170 */         Elements metaElements = doc.select("meta[http-equiv=content-type], meta[charset]");
/* 171 */         String foundCharset = null;
/* 172 */         for (Element meta : metaElements) {
/* 173 */           if (meta.hasAttr("http-equiv"))
/* 174 */             foundCharset = getCharsetFromContentType(meta.attr("content")); 
/* 175 */           if (foundCharset == null && meta.hasAttr("charset"))
/* 176 */             foundCharset = meta.attr("charset"); 
/* 177 */           if (foundCharset != null) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         
/* 182 */         if (foundCharset == null && doc.childNodeSize() > 0) {
/* 183 */           Node first = doc.childNode(0);
/* 184 */           XmlDeclaration decl = null;
/* 185 */           if (first instanceof XmlDeclaration) {
/* 186 */             decl = (XmlDeclaration)first;
/* 187 */           } else if (first instanceof Comment) {
/* 188 */             Comment comment = (Comment)first;
/* 189 */             if (comment.isXmlDeclaration())
/* 190 */               decl = comment.asXmlDeclaration(); 
/*     */           } 
/* 192 */           if (decl != null && 
/* 193 */             decl.name().equalsIgnoreCase("xml")) {
/* 194 */             foundCharset = decl.attr("encoding");
/*     */           }
/*     */         } 
/* 197 */         foundCharset = validateCharset(foundCharset);
/* 198 */         if (foundCharset != null && !foundCharset.equalsIgnoreCase(defaultCharsetName)) {
/* 199 */           foundCharset = foundCharset.trim().replaceAll("[\"']", "");
/* 200 */           charsetName = foundCharset;
/* 201 */           doc = null;
/* 202 */         } else if (!fullyRead) {
/* 203 */           doc = null;
/*     */         } 
/*     */       } else {
/* 206 */         Validate.notEmpty(charsetName, "Must set charset arg to character set of file to parse. Set to null to attempt to detect from HTML");
/*     */       } 
/* 208 */       if (doc == null) {
/* 209 */         if (charsetName == null)
/* 210 */           charsetName = defaultCharsetName; 
/* 211 */         BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)constrainableInputStream, Charset.forName(charsetName)), 32768);
/*     */         try {
/* 213 */           if (bomCharset != null && bomCharset.offset) {
/* 214 */             long skipped = reader.skip(1L);
/* 215 */             Validate.isTrue((skipped == 1L));
/*     */           } 
/*     */           try {
/* 218 */             doc = parser.parseInput(reader, baseUri);
/* 219 */           } catch (UncheckedIOException e) {
/*     */             
/* 221 */             throw e.ioException();
/*     */           } 
/* 223 */           Charset charset = charsetName.equals(defaultCharsetName) ? UTF_8 : Charset.forName(charsetName);
/* 224 */           doc.outputSettings().charset(charset);
/* 225 */           if (!charset.canEncode())
/*     */           {
/* 227 */             doc.charset(UTF_8);
/*     */           }
/*     */         } finally {
/*     */           
/* 231 */           reader.close();
/*     */         } 
/*     */       } 
/*     */     } finally {
/*     */       
/* 236 */       constrainableInputStream.close();
/*     */     } 
/* 238 */     return doc;
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
/*     */   public static ByteBuffer readToByteBuffer(InputStream inStream, int maxSize) throws IOException {
/* 250 */     Validate.isTrue((maxSize >= 0), "maxSize must be 0 (unlimited) or larger");
/* 251 */     ConstrainableInputStream input = ConstrainableInputStream.wrap(inStream, 32768, maxSize);
/* 252 */     return input.readToByteBuffer(maxSize);
/*     */   }
/*     */   
/*     */   static ByteBuffer emptyByteBuffer() {
/* 256 */     return ByteBuffer.allocate(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   static String getCharsetFromContentType(@Nullable String contentType) {
/* 266 */     if (contentType == null) return null; 
/* 267 */     Matcher m = charsetPattern.matcher(contentType);
/* 268 */     if (m.find()) {
/* 269 */       String charset = m.group(1).trim();
/* 270 */       charset = charset.replace("charset=", "");
/* 271 */       return validateCharset(charset);
/*     */     } 
/* 273 */     return null;
/*     */   }
/*     */   @Nullable
/*     */   private static String validateCharset(@Nullable String cs) {
/* 277 */     if (cs == null || cs.length() == 0) return null; 
/* 278 */     cs = cs.trim().replaceAll("[\"']", "");
/*     */     try {
/* 280 */       if (Charset.isSupported(cs)) return cs; 
/* 281 */       cs = cs.toUpperCase(Locale.ENGLISH);
/* 282 */       if (Charset.isSupported(cs)) return cs; 
/* 283 */     } catch (IllegalCharsetNameException illegalCharsetNameException) {}
/*     */ 
/*     */     
/* 286 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String mimeBoundary() {
/* 293 */     StringBuilder mime = StringUtil.borrowBuilder();
/* 294 */     Random rand = new Random();
/* 295 */     for (int i = 0; i < 32; i++) {
/* 296 */       mime.append(mimeBoundaryChars[rand.nextInt(mimeBoundaryChars.length)]);
/*     */     }
/* 298 */     return StringUtil.releaseBuilder(mime);
/*     */   }
/*     */   @Nullable
/*     */   private static BomCharset detectCharsetFromBom(ByteBuffer byteData) {
/* 302 */     Buffer buffer = byteData;
/* 303 */     buffer.mark();
/* 304 */     byte[] bom = new byte[4];
/* 305 */     if (byteData.remaining() >= bom.length) {
/* 306 */       byteData.get(bom);
/* 307 */       buffer.rewind();
/*     */     } 
/* 309 */     if ((bom[0] == 0 && bom[1] == 0 && bom[2] == -2 && bom[3] == -1) || (bom[0] == -1 && bom[1] == -2 && bom[2] == 0 && bom[3] == 0))
/*     */     {
/* 311 */       return new BomCharset("UTF-32", false); } 
/* 312 */     if ((bom[0] == -2 && bom[1] == -1) || (bom[0] == -1 && bom[1] == -2))
/*     */     {
/* 314 */       return new BomCharset("UTF-16", false); } 
/* 315 */     if (bom[0] == -17 && bom[1] == -69 && bom[2] == -65) {
/* 316 */       return new BomCharset("UTF-8", true);
/*     */     }
/*     */     
/* 319 */     return null;
/*     */   }
/*     */   
/*     */   private static class BomCharset {
/*     */     private final String charset;
/*     */     private final boolean offset;
/*     */     
/*     */     public BomCharset(String charset, boolean offset) {
/* 327 */       this.charset = charset;
/* 328 */       this.offset = offset;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\helper\DataUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */