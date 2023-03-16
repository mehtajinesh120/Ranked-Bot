/*     */ package org.sqlite;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.nio.file.CopyOption;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.nio.file.StandardCopyOption;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.security.DigestInputStream;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Stream;
/*     */ import org.sqlite.util.OSInfo;
/*     */ import org.sqlite.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SQLiteJDBCLoader
/*     */ {
/*     */   private static final String LOCK_EXT = ".lck";
/*     */   private static boolean extracted = false;
/*     */   
/*     */   public static synchronized boolean initialize() throws Exception {
/*  65 */     if (!extracted) {
/*  66 */       cleanup();
/*     */     }
/*  68 */     loadSQLiteNativeLibrary();
/*  69 */     return extracted;
/*     */   }
/*     */   
/*     */   private static File getTempDir() {
/*  73 */     return new File(
/*  74 */         System.getProperty("org.sqlite.tmpdir", System.getProperty("java.io.tmpdir")));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void cleanup() {
/*  81 */     String searchPattern = "sqlite-" + getVersion();
/*     */     
/*  83 */     try (Stream<Path> dirList = Files.list(getTempDir().toPath())) {
/*  84 */       dirList.filter(path -> 
/*     */           
/*  86 */           (!path.getFileName().toString().endsWith(".lck") && path.getFileName().toString().startsWith(searchPattern)))
/*     */ 
/*     */ 
/*     */         
/*  90 */         .forEach(nativeLib -> {
/*     */             Path lckFile = Paths.get(nativeLib + ".lck", new String[0]);
/*     */             
/*     */             if (Files.notExists(lckFile, new java.nio.file.LinkOption[0])) {
/*     */               try {
/*     */                 Files.delete(nativeLib);
/*  96 */               } catch (Exception e) {
/*     */                 
/*     */                 System.err.println("Failed to delete old native lib: " + e.getMessage());
/*     */               }
/*     */             
/*     */             }
/*     */           });
/* 103 */     } catch (IOException e) {
/* 104 */       System.err.println("Failed to open directory: " + e.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   static boolean getPureJavaFlag() {
/* 114 */     return Boolean.parseBoolean(System.getProperty("sqlite.purejava", "false"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public static boolean isPureJavaMode() {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isNativeMode() throws Exception {
/* 135 */     initialize();
/* 136 */     return extracted;
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
/*     */   static String md5sum(InputStream input) throws IOException {
/* 148 */     BufferedInputStream in = new BufferedInputStream(input);
/*     */     
/*     */     try {
/* 151 */       MessageDigest digest = MessageDigest.getInstance("MD5");
/* 152 */       DigestInputStream digestInputStream = new DigestInputStream(in, digest);
/* 153 */       while (digestInputStream.read() >= 0);
/*     */       
/* 155 */       ByteArrayOutputStream md5out = new ByteArrayOutputStream();
/* 156 */       md5out.write(digest.digest());
/* 157 */       return md5out.toString();
/* 158 */     } catch (NoSuchAlgorithmException e) {
/* 159 */       throw new IllegalStateException("MD5 algorithm is not available: " + e);
/*     */     } finally {
/* 161 */       in.close();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean contentsEquals(InputStream in1, InputStream in2) throws IOException {
/* 166 */     if (!(in1 instanceof BufferedInputStream)) {
/* 167 */       in1 = new BufferedInputStream(in1);
/*     */     }
/* 169 */     if (!(in2 instanceof BufferedInputStream)) {
/* 170 */       in2 = new BufferedInputStream(in2);
/*     */     }
/*     */     
/* 173 */     int ch = in1.read();
/* 174 */     while (ch != -1) {
/* 175 */       int i = in2.read();
/* 176 */       if (ch != i) {
/* 177 */         return false;
/*     */       }
/* 179 */       ch = in1.read();
/*     */     } 
/* 181 */     int ch2 = in2.read();
/* 182 */     return (ch2 == -1);
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
/*     */   private static boolean extractAndLoadLibraryFile(String libFolderForCurrentOS, String libraryFileName, String targetFolder) {
/* 195 */     String nativeLibraryFilePath = libFolderForCurrentOS + "/" + libraryFileName;
/*     */ 
/*     */     
/* 198 */     String uuid = UUID.randomUUID().toString();
/*     */     
/* 200 */     String extractedLibFileName = String.format("sqlite-%s-%s-%s", new Object[] { getVersion(), uuid, libraryFileName });
/* 201 */     String extractedLckFileName = extractedLibFileName + ".lck";
/*     */     
/* 203 */     Path extractedLibFile = Paths.get(targetFolder, new String[] { extractedLibFileName });
/* 204 */     Path extractedLckFile = Paths.get(targetFolder, new String[] { extractedLckFileName });
/*     */ 
/*     */     
/*     */     try {
/* 208 */       try (InputStream reader = getResourceAsStream(nativeLibraryFilePath)) {
/* 209 */         if (Files.notExists(extractedLckFile, new java.nio.file.LinkOption[0])) {
/* 210 */           Files.createFile(extractedLckFile, (FileAttribute<?>[])new FileAttribute[0]);
/*     */         }
/*     */         
/* 213 */         Files.copy(reader, extractedLibFile, new CopyOption[] { StandardCopyOption.REPLACE_EXISTING });
/*     */       } finally {
/*     */         
/* 216 */         extractedLibFile.toFile().deleteOnExit();
/* 217 */         extractedLckFile.toFile().deleteOnExit();
/*     */       } 
/*     */ 
/*     */       
/* 221 */       extractedLibFile.toFile().setReadable(true);
/* 222 */       extractedLibFile.toFile().setWritable(true, true);
/* 223 */       extractedLibFile.toFile().setExecutable(true);
/*     */ 
/*     */ 
/*     */       
/* 227 */       try(InputStream nativeIn = getResourceAsStream(nativeLibraryFilePath); 
/* 228 */           InputStream extractedLibIn = Files.newInputStream(extractedLibFile, new java.nio.file.OpenOption[0])) {
/* 229 */         if (!contentsEquals(nativeIn, extractedLibIn)) {
/* 230 */           throw new RuntimeException(
/* 231 */               String.format("Failed to write a native library file at %s", new Object[] { extractedLibFile }));
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 237 */       return loadNativeLibrary(targetFolder, extractedLibFileName);
/* 238 */     } catch (IOException e) {
/* 239 */       e.printStackTrace();
/* 240 */       return false;
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
/*     */   private static InputStream getResourceAsStream(String name) {
/* 252 */     String resolvedName = name.substring(1);
/* 253 */     ClassLoader cl = SQLiteJDBCLoader.class.getClassLoader();
/* 254 */     URL url = cl.getResource(resolvedName);
/* 255 */     if (url == null) {
/* 256 */       return null;
/*     */     }
/*     */     try {
/* 259 */       URLConnection connection = url.openConnection();
/* 260 */       connection.setUseCaches(false);
/* 261 */       return connection.getInputStream();
/* 262 */     } catch (IOException e) {
/* 263 */       e.printStackTrace();
/* 264 */       return null;
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
/*     */   private static boolean loadNativeLibrary(String path, String name) {
/* 276 */     File libPath = new File(path, name);
/* 277 */     if (libPath.exists()) {
/*     */       
/*     */       try {
/* 280 */         System.load((new File(path, name)).getAbsolutePath());
/* 281 */         return true;
/* 282 */       } catch (UnsatisfiedLinkError e) {
/* 283 */         System.err.println("Failed to load native library:" + name + ". osinfo: " + 
/*     */ 
/*     */ 
/*     */             
/* 287 */             OSInfo.getNativeLibFolderPathForCurrentOS());
/* 288 */         e.printStackTrace();
/* 289 */         return false;
/*     */       } 
/*     */     }
/*     */     
/* 293 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void loadSQLiteNativeLibrary() throws Exception {
/* 303 */     if (extracted) {
/*     */       return;
/*     */     }
/*     */     
/* 307 */     List<String> triedPaths = new LinkedList<>();
/*     */ 
/*     */     
/* 310 */     String sqliteNativeLibraryPath = System.getProperty("org.sqlite.lib.path");
/* 311 */     String sqliteNativeLibraryName = System.getProperty("org.sqlite.lib.name");
/* 312 */     if (sqliteNativeLibraryName == null) {
/* 313 */       sqliteNativeLibraryName = System.mapLibraryName("sqlitejdbc");
/* 314 */       if (sqliteNativeLibraryName != null && sqliteNativeLibraryName.endsWith(".dylib")) {
/* 315 */         sqliteNativeLibraryName = sqliteNativeLibraryName.replace(".dylib", ".jnilib");
/*     */       }
/*     */     } 
/*     */     
/* 319 */     if (sqliteNativeLibraryPath != null) {
/* 320 */       if (loadNativeLibrary(sqliteNativeLibraryPath, sqliteNativeLibraryName)) {
/* 321 */         extracted = true;
/*     */         return;
/*     */       } 
/* 324 */       triedPaths.add(sqliteNativeLibraryPath);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 329 */     String packagePath = SQLiteJDBCLoader.class.getPackage().getName().replaceAll("\\.", "/");
/*     */     
/* 331 */     sqliteNativeLibraryPath = String.format("/%s/native/%s", new Object[] {
/* 332 */           packagePath, OSInfo.getNativeLibFolderPathForCurrentOS() });
/* 333 */     boolean hasNativeLib = hasResource(sqliteNativeLibraryPath + "/" + sqliteNativeLibraryName);
/*     */     
/* 335 */     if (!hasNativeLib && 
/* 336 */       OSInfo.getOSName().equals("Mac")) {
/*     */       
/* 338 */       String altName = "libsqlitejdbc.jnilib";
/* 339 */       if (hasResource(sqliteNativeLibraryPath + "/" + altName)) {
/* 340 */         sqliteNativeLibraryName = altName;
/* 341 */         hasNativeLib = true;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 346 */     if (hasNativeLib) {
/*     */       
/* 348 */       String tempFolder = getTempDir().getAbsolutePath();
/*     */       
/* 350 */       if (extractAndLoadLibraryFile(sqliteNativeLibraryPath, sqliteNativeLibraryName, tempFolder)) {
/*     */         
/* 352 */         extracted = true;
/*     */         return;
/*     */       } 
/* 355 */       triedPaths.add(sqliteNativeLibraryPath);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 360 */     String javaLibraryPath = System.getProperty("java.library.path", "");
/* 361 */     for (String ldPath : javaLibraryPath.split(File.pathSeparator)) {
/* 362 */       if (!ldPath.isEmpty()) {
/*     */ 
/*     */         
/* 365 */         if (loadNativeLibrary(ldPath, sqliteNativeLibraryName)) {
/* 366 */           extracted = true;
/*     */           return;
/*     */         } 
/* 369 */         triedPaths.add(ldPath);
/*     */       } 
/*     */     } 
/*     */     
/* 373 */     extracted = false;
/* 374 */     throw new Exception(
/* 375 */         String.format("No native library found for os.name=%s, os.arch=%s, paths=[%s]", new Object[] {
/*     */             
/* 377 */             OSInfo.getOSName(), 
/* 378 */             OSInfo.getArchName(), 
/* 379 */             StringUtils.join(triedPaths, File.pathSeparator) }));
/*     */   }
/*     */   
/*     */   private static boolean hasResource(String path) {
/* 383 */     return (SQLiteJDBCLoader.class.getResource(path) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   private static void getNativeLibraryFolderForTheCurrentOS() {
/* 388 */     String osName = OSInfo.getOSName();
/* 389 */     String archName = OSInfo.getArchName();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getMajorVersion() {
/* 394 */     String[] c = getVersion().split("\\.");
/* 395 */     return (c.length > 0) ? Integer.parseInt(c[0]) : 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getMinorVersion() {
/* 400 */     String[] c = getVersion().split("\\.");
/* 401 */     return (c.length > 1) ? Integer.parseInt(c[1]) : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getVersion() {
/* 408 */     URL versionFile = SQLiteJDBCLoader.class.getResource("/META-INF/maven/org.xerial/sqlite-jdbc/pom.properties");
/*     */     
/* 410 */     if (versionFile == null)
/*     */     {
/* 412 */       versionFile = SQLiteJDBCLoader.class.getResource("/META-INF/maven/org.xerial/sqlite-jdbc/VERSION");
/*     */     }
/*     */ 
/*     */     
/* 416 */     String version = "unknown";
/*     */     try {
/* 418 */       if (versionFile != null) {
/* 419 */         Properties versionData = new Properties();
/* 420 */         versionData.load(versionFile.openStream());
/* 421 */         version = versionData.getProperty("version", version);
/* 422 */         version = version.trim().replaceAll("[^0-9\\.]", "");
/*     */       } 
/* 424 */     } catch (IOException e) {
/* 425 */       e.printStackTrace();
/*     */     } 
/* 427 */     return version;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlite\SQLiteJDBCLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */