/*    */ package org.yaml.snakeyaml.constructor;
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
/*    */ public class CustomClassLoaderConstructor
/*    */   extends Constructor
/*    */ {
/* 21 */   private ClassLoader loader = CustomClassLoaderConstructor.class.getClassLoader();
/*    */   
/*    */   public CustomClassLoaderConstructor(ClassLoader cLoader) {
/* 24 */     this(Object.class, cLoader);
/*    */   }
/*    */   
/*    */   public CustomClassLoaderConstructor(Class<? extends Object> theRoot, ClassLoader theLoader) {
/* 28 */     super(theRoot);
/* 29 */     if (theLoader == null) {
/* 30 */       throw new NullPointerException("Loader must be provided.");
/*    */     }
/* 32 */     this.loader = theLoader;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Class<?> getClassForName(String name) throws ClassNotFoundException {
/* 37 */     return Class.forName(name, true, this.loader);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\constructor\CustomClassLoaderConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */