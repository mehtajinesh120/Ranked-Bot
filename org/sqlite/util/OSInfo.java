/*     */ package org.sqlite.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OSInfo
/*     */ {
/*  41 */   protected static ProcessRunner processRunner = new ProcessRunner();
/*  42 */   private static final HashMap<String, String> archMapping = new HashMap<>();
/*     */   
/*     */   public static final String X86 = "x86";
/*     */   
/*     */   public static final String X86_64 = "x86_64";
/*     */   public static final String IA64_32 = "ia64_32";
/*     */   public static final String IA64 = "ia64";
/*     */   public static final String PPC = "ppc";
/*     */   public static final String PPC64 = "ppc64";
/*     */   
/*     */   static {
/*  53 */     archMapping.put("x86", "x86");
/*  54 */     archMapping.put("i386", "x86");
/*  55 */     archMapping.put("i486", "x86");
/*  56 */     archMapping.put("i586", "x86");
/*  57 */     archMapping.put("i686", "x86");
/*  58 */     archMapping.put("pentium", "x86");
/*     */ 
/*     */     
/*  61 */     archMapping.put("x86_64", "x86_64");
/*  62 */     archMapping.put("amd64", "x86_64");
/*  63 */     archMapping.put("em64t", "x86_64");
/*  64 */     archMapping.put("universal", "x86_64");
/*     */ 
/*     */     
/*  67 */     archMapping.put("ia64", "ia64");
/*  68 */     archMapping.put("ia64w", "ia64");
/*     */ 
/*     */     
/*  71 */     archMapping.put("ia64_32", "ia64_32");
/*  72 */     archMapping.put("ia64n", "ia64_32");
/*     */ 
/*     */     
/*  75 */     archMapping.put("ppc", "ppc");
/*  76 */     archMapping.put("power", "ppc");
/*  77 */     archMapping.put("powerpc", "ppc");
/*  78 */     archMapping.put("power_pc", "ppc");
/*  79 */     archMapping.put("power_rs", "ppc");
/*     */ 
/*     */     
/*  82 */     archMapping.put("ppc64", "ppc64");
/*  83 */     archMapping.put("power64", "ppc64");
/*  84 */     archMapping.put("powerpc64", "ppc64");
/*  85 */     archMapping.put("power_pc64", "ppc64");
/*  86 */     archMapping.put("power_rs64", "ppc64");
/*  87 */     archMapping.put("ppc64el", "ppc64");
/*  88 */     archMapping.put("ppc64le", "ppc64");
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*  92 */     if (args.length >= 1) {
/*  93 */       if ("--os".equals(args[0])) {
/*  94 */         System.out.print(getOSName()); return;
/*     */       } 
/*  96 */       if ("--arch".equals(args[0])) {
/*  97 */         System.out.print(getArchName());
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 102 */     System.out.print(getNativeLibFolderPathForCurrentOS());
/*     */   }
/*     */   
/*     */   public static String getNativeLibFolderPathForCurrentOS() {
/* 106 */     return getOSName() + "/" + getArchName();
/*     */   }
/*     */   
/*     */   public static String getOSName() {
/* 110 */     return translateOSNameToFolderName(System.getProperty("os.name"));
/*     */   }
/*     */   
/*     */   public static boolean isAndroid() {
/* 114 */     return System.getProperty("java.runtime.name", "").toLowerCase().contains("android");
/*     */   }
/*     */   
/*     */   public static boolean isMusl() {
/* 118 */     Path mapFilesDir = Paths.get("/proc/self/map_files", new String[0]);
/* 119 */     try (Stream<Path> dirStream = Files.list(mapFilesDir)) {
/* 120 */       return dirStream
/* 121 */         .map(path -> {
/*     */             
/*     */             try {
/*     */               return path.toRealPath(new java.nio.file.LinkOption[0]).toString();
/* 125 */             } catch (IOException e) {
/*     */               
/*     */               return "";
/*     */             } 
/* 129 */           }).anyMatch(s -> s.toLowerCase().contains("musl"));
/* 130 */     } catch (Exception ignored) {
/*     */ 
/*     */       
/* 133 */       return isAlpineLinux();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean isAlpineLinux() {
/* 138 */     try (Stream<String> osLines = Files.lines(Paths.get("/etc/os-release", new String[0]))) {
/* 139 */       return osLines.anyMatch(l -> (l.startsWith("ID") && l.contains("alpine")));
/* 140 */     } catch (Exception exception) {
/*     */       
/* 142 */       return false;
/*     */     } 
/*     */   }
/*     */   static String getHardwareName() {
/*     */     try {
/* 147 */       return processRunner.runAndWaitFor("uname -m");
/* 148 */     } catch (Throwable e) {
/* 149 */       System.err.println("Error while running uname -m: " + e.getMessage());
/* 150 */       return "unknown";
/*     */     } 
/*     */   }
/*     */   
/*     */   static String resolveArmArchType() {
/* 155 */     if (System.getProperty("os.name").contains("Linux")) {
/* 156 */       String armType = getHardwareName();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 161 */       if (isAndroid()) {
/* 162 */         if (armType.startsWith("aarch64"))
/*     */         {
/* 164 */           return "aarch64";
/*     */         }
/* 166 */         return "arm";
/*     */       } 
/*     */ 
/*     */       
/* 170 */       if (armType.startsWith("armv6"))
/*     */       {
/* 172 */         return "armv6"; } 
/* 173 */       if (armType.startsWith("armv7"))
/*     */       {
/* 175 */         return "armv7"; } 
/* 176 */       if (armType.startsWith("armv5"))
/*     */       {
/* 178 */         return "arm"; } 
/* 179 */       if (armType.startsWith("aarch64"))
/*     */       {
/* 181 */         return "aarch64";
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 186 */       String abi = System.getProperty("sun.arch.abi");
/* 187 */       if (abi != null && abi.startsWith("gnueabihf")) {
/* 188 */         return "armv7";
/*     */       }
/*     */ 
/*     */       
/* 192 */       String javaHome = System.getProperty("java.home");
/*     */       
/*     */       try {
/* 195 */         int exitCode = Runtime.getRuntime().exec("which readelf").waitFor();
/* 196 */         if (exitCode == 0) {
/* 197 */           String[] cmdarray = { "/bin/sh", "-c", "find '" + javaHome + "' -name 'libjvm.so' | head -1 | xargs readelf -A | grep 'Tag_ABI_VFP_args: VFP registers'" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 205 */           exitCode = Runtime.getRuntime().exec(cmdarray).waitFor();
/* 206 */           if (exitCode == 0) {
/* 207 */             return "armv7";
/*     */           }
/*     */         } else {
/* 210 */           System.err.println("WARNING! readelf not found. Cannot check if running on an armhf system, armel architecture will be presumed.");
/*     */         }
/*     */       
/* 213 */       } catch (IOException|InterruptedException iOException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 218 */     return "arm";
/*     */   }
/*     */   
/*     */   public static String getArchName() {
/* 222 */     String osArch = System.getProperty("os.arch");
/*     */     
/* 224 */     if (osArch.startsWith("arm")) {
/* 225 */       osArch = resolveArmArchType();
/*     */     } else {
/* 227 */       String lc = osArch.toLowerCase(Locale.US);
/* 228 */       if (archMapping.containsKey(lc)) return archMapping.get(lc); 
/*     */     } 
/* 230 */     return translateArchNameToFolderName(osArch);
/*     */   }
/*     */   
/*     */   static String translateOSNameToFolderName(String osName) {
/* 234 */     if (osName.contains("Windows"))
/* 235 */       return "Windows"; 
/* 236 */     if (osName.contains("Mac") || osName.contains("Darwin"))
/* 237 */       return "Mac"; 
/* 238 */     if (osName.contains("AIX"))
/* 239 */       return "AIX"; 
/* 240 */     if (isMusl())
/* 241 */       return "Linux-Musl"; 
/* 242 */     if (isAndroid())
/* 243 */       return "Linux-Android"; 
/* 244 */     if (osName.contains("Linux")) {
/* 245 */       return "Linux";
/*     */     }
/* 247 */     return osName.replaceAll("\\W", "");
/*     */   }
/*     */ 
/*     */   
/*     */   static String translateArchNameToFolderName(String archName) {
/* 252 */     return archName.replaceAll("\\W", "");
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlit\\util\OSInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */