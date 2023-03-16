/*    */ package org.sqlite.util;
/*    */ 
/*    */ import java.net.URL;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ResourceFinder
/*    */ {
/*    */   public static URL find(Class<?> referenceClass, String resourceFileName) {
/* 44 */     return find(referenceClass.getClassLoader(), referenceClass.getPackage(), resourceFileName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static URL find(ClassLoader classLoader, Package basePackage, String resourceFileName) {
/* 55 */     return find(classLoader, basePackage.getName(), resourceFileName);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static URL find(ClassLoader classLoader, String packageName, String resourceFileName) {
/* 66 */     String packagePath = packagePath(packageName);
/* 67 */     String resourcePath = packagePath + resourceFileName;
/* 68 */     if (!resourcePath.startsWith("/")) resourcePath = "/" + resourcePath;
/*    */     
/* 70 */     return classLoader.getResource(resourcePath);
/*    */   }
/*    */ 
/*    */   
/*    */   private static String packagePath(Class<?> referenceClass) {
/* 75 */     return packagePath(referenceClass.getPackage());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String packagePath(Package basePackage) {
/* 83 */     return packagePath(basePackage.getName());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String packagePath(String packageName) {
/* 91 */     String packageAsPath = packageName.replaceAll("\\.", "/");
/* 92 */     return packageAsPath.endsWith("/") ? packageAsPath : (packageAsPath + "/");
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\sqlit\\util\ResourceFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */