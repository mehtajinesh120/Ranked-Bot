/*    */ package org.yaml.snakeyaml.extensions.compactnotation;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public class CompactData
/*    */ {
/*    */   private final String prefix;
/* 24 */   private final List<String> arguments = new ArrayList<>();
/* 25 */   private final Map<String, String> properties = new HashMap<>();
/*    */   
/*    */   public CompactData(String prefix) {
/* 28 */     this.prefix = prefix;
/*    */   }
/*    */   
/*    */   public String getPrefix() {
/* 32 */     return this.prefix;
/*    */   }
/*    */   
/*    */   public Map<String, String> getProperties() {
/* 36 */     return this.properties;
/*    */   }
/*    */   
/*    */   public List<String> getArguments() {
/* 40 */     return this.arguments;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return "CompactData: " + this.prefix + " " + this.properties;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\extensions\compactnotation\CompactData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */