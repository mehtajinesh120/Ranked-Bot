/*     */ package org.jsoup;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.WillClose;
/*     */ import org.jsoup.helper.DataUtil;
/*     */ import org.jsoup.helper.HttpConnection;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.parser.Parser;
/*     */ import org.jsoup.safety.Cleaner;
/*     */ import org.jsoup.safety.Safelist;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Jsoup
/*     */ {
/*     */   public static Document parse(String html, String baseUri) {
/*  36 */     return Parser.parse(html, baseUri);
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
/*     */   public static Document parse(String html, String baseUri, Parser parser) {
/*  50 */     return parser.parseInput(html, baseUri);
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
/*     */   public static Document parse(String html, Parser parser) {
/*  64 */     return parser.parseInput(html, "");
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
/*     */   public static Document parse(String html) {
/*  77 */     return Parser.parse(html, "");
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
/*     */   public static Connection connect(String url) {
/*  94 */     return HttpConnection.connect(url);
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
/*     */   public static Connection newSession() {
/* 121 */     return (Connection)new HttpConnection();
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
/*     */   public static Document parse(File file, @Nullable String charsetName, String baseUri) throws IOException {
/* 136 */     return DataUtil.load(file, charsetName, baseUri);
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
/*     */   public static Document parse(File file, @Nullable String charsetName) throws IOException {
/* 151 */     return DataUtil.load(file, charsetName, file.getAbsolutePath());
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
/*     */   public static Document parse(File file) throws IOException {
/* 168 */     return DataUtil.load(file, null, file.getAbsolutePath());
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
/*     */   public static Document parse(File file, @Nullable String charsetName, String baseUri, Parser parser) throws IOException {
/* 185 */     return DataUtil.load(file, charsetName, baseUri, parser);
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
/*     */   public static Document parse(@WillClose InputStream in, @Nullable String charsetName, String baseUri) throws IOException {
/* 200 */     return DataUtil.load(in, charsetName, baseUri);
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
/*     */   public static Document parse(InputStream in, @Nullable String charsetName, String baseUri, Parser parser) throws IOException {
/* 217 */     return DataUtil.load(in, charsetName, baseUri, parser);
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
/*     */   public static Document parseBodyFragment(String bodyHtml, String baseUri) {
/* 230 */     return Parser.parseBodyFragment(bodyHtml, baseUri);
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
/*     */   public static Document parseBodyFragment(String bodyHtml) {
/* 242 */     return Parser.parseBodyFragment(bodyHtml, "");
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
/*     */   public static Document parse(URL url, int timeoutMillis) throws IOException {
/* 263 */     Connection con = HttpConnection.connect(url);
/* 264 */     con.timeout(timeoutMillis);
/* 265 */     return con.get();
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
/*     */   public static String clean(String bodyHtml, String baseUri, Safelist safelist) {
/* 280 */     Document dirty = parseBodyFragment(bodyHtml, baseUri);
/* 281 */     Cleaner cleaner = new Cleaner(safelist);
/* 282 */     Document clean = cleaner.clean(dirty);
/* 283 */     return clean.body().html();
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
/*     */   public static String clean(String bodyHtml, Safelist safelist) {
/* 317 */     return clean(bodyHtml, "", safelist);
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
/*     */   public static String clean(String bodyHtml, String baseUri, Safelist safelist, Document.OutputSettings outputSettings) {
/* 335 */     Document dirty = parseBodyFragment(bodyHtml, baseUri);
/* 336 */     Cleaner cleaner = new Cleaner(safelist);
/* 337 */     Document clean = cleaner.clean(dirty);
/* 338 */     clean.outputSettings(outputSettings);
/* 339 */     return clean.body().html();
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
/*     */   public static boolean isValid(String bodyHtml, Safelist safelist) {
/* 364 */     return (new Cleaner(safelist)).isValidBodyHtml(bodyHtml);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\Jsoup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */