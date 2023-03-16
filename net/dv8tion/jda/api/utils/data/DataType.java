/*    */ package net.dv8tion.jda.api.utils.data;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
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
/*    */ public enum DataType
/*    */ {
/* 29 */   INT, FLOAT, STRING, OBJECT, ARRAY, BOOLEAN, NULL, UNKNOWN;
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
/*    */   @Nonnull
/*    */   public static DataType getType(@Nullable Object value) {
/* 42 */     for (DataType type : values()) {
/*    */       
/* 44 */       if (type.isType(value))
/* 45 */         return type; 
/*    */     } 
/* 47 */     return UNKNOWN;
/*    */   }
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
/*    */   public boolean isType(@Nullable Object value) {
/* 61 */     switch (this) {
/*    */       
/*    */       case INT:
/* 64 */         return (value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof Byte);
/*    */       case FLOAT:
/* 66 */         return (value instanceof Double || value instanceof Float);
/*    */       case STRING:
/* 68 */         return value instanceof String;
/*    */       case BOOLEAN:
/* 70 */         return value instanceof Boolean;
/*    */       case ARRAY:
/* 72 */         return value instanceof java.util.List;
/*    */       case OBJECT:
/* 74 */         return value instanceof java.util.Map;
/*    */       case NULL:
/* 76 */         return (value == null);
/*    */     } 
/* 78 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\ap\\utils\data\DataType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */