/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FileSerializer
/*    */   extends StdScalarSerializer<File>
/*    */ {
/*    */   public FileSerializer() {
/* 22 */     super(File.class);
/*    */   }
/*    */   
/*    */   public void serialize(File value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 26 */     g.writeString(value.getAbsolutePath());
/*    */   }
/*    */ 
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) {
/* 31 */     return (JsonNode)createSchemaNode("string", true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
/* 38 */     visitStringFormat(visitor, typeHint);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\ser\std\FileSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */