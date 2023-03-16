/*    */ package org.yaml.snakeyaml.extensions.compactnotation;
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
/*    */ public class PackageCompactConstructor
/*    */   extends CompactConstructor
/*    */ {
/*    */   private final String packageName;
/*    */   
/*    */   public PackageCompactConstructor(String packageName) {
/* 21 */     this.packageName = packageName;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Class<?> getClassForName(String name) throws ClassNotFoundException {
/* 26 */     if (name.indexOf('.') < 0) {
/*    */       try {
/* 28 */         Class<?> clazz = Class.forName(this.packageName + "." + name);
/* 29 */         return clazz;
/* 30 */       } catch (ClassNotFoundException classNotFoundException) {}
/*    */     }
/*    */ 
/*    */     
/* 34 */     return super.getClassForName(name);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\extensions\compactnotation\PackageCompactConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */