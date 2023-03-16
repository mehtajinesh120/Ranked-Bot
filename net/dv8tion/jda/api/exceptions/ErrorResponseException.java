/*     */ package net.dv8tion.jda.api.exceptions;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.requests.ErrorResponse;
/*     */ import net.dv8tion.jda.api.requests.Response;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.utils.data.DataArray;
/*     */ import net.dv8tion.jda.api.utils.data.DataObject;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ import net.dv8tion.jda.internal.utils.Helpers;
/*     */ import net.dv8tion.jda.internal.utils.JDALogger;
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
/*     */ public class ErrorResponseException
/*     */   extends RuntimeException
/*     */ {
/*     */   private final ErrorResponse errorResponse;
/*     */   private final Response response;
/*     */   private final String meaning;
/*     */   private final int code;
/*     */   private final List<SchemaError> schemaErrors;
/*     */   
/*     */   private ErrorResponseException(ErrorResponse errorResponse, Response response, int code, String meaning, List<SchemaError> schemaErrors) {
/*  58 */     super(code + ": " + meaning + (schemaErrors.isEmpty() ? "" : (
/*  59 */         "\n" + (String)schemaErrors.stream().map(SchemaError::toString).collect(Collectors.joining("\n")))));
/*     */     
/*  61 */     this.response = response;
/*  62 */     if (response != null && response.getException() != null)
/*  63 */       initCause(response.getException()); 
/*  64 */     this.errorResponse = errorResponse;
/*  65 */     this.code = code;
/*  66 */     this.meaning = meaning;
/*  67 */     this.schemaErrors = schemaErrors;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isServerError() {
/*  78 */     return (this.errorResponse == ErrorResponse.SERVER_ERROR);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMeaning() {
/*  89 */     return this.meaning;
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
/*     */   public int getErrorCode() {
/* 101 */     return this.code;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ErrorResponse getErrorResponse() {
/* 112 */     return this.errorResponse;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Response getResponse() {
/* 122 */     return this.response;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<SchemaError> getSchemaErrors() {
/* 134 */     return this.schemaErrors;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ErrorResponseException create(ErrorResponse errorResponse, Response response) {
/* 139 */     String meaning = errorResponse.getMeaning();
/* 140 */     int code = errorResponse.getCode();
/* 141 */     List<SchemaError> schemaErrors = new ArrayList<>();
/*     */     
/*     */     try {
/* 144 */       Optional<DataObject> optObj = response.optObject();
/* 145 */       if (response.isError() && response.getException() != null)
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 150 */         code = response.code;
/* 151 */         meaning = response.getException().getClass().getName();
/*     */       }
/* 153 */       else if (optObj.isPresent())
/*     */       {
/* 155 */         DataObject obj = optObj.get();
/* 156 */         if (!obj.isNull("code") || !obj.isNull("message")) {
/*     */           
/* 158 */           if (!obj.isNull("code"))
/* 159 */             code = obj.getInt("code"); 
/* 160 */           if (!obj.isNull("message")) {
/* 161 */             meaning = obj.getString("message");
/*     */           
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 167 */           code = response.code;
/* 168 */           meaning = obj.toString();
/*     */         } 
/*     */         
/* 171 */         obj.optObject("errors").ifPresent(schema -> parseSchema(schemaErrors, "", schema));
/*     */       
/*     */       }
/*     */       else
/*     */       {
/* 176 */         code = response.code;
/* 177 */         meaning = response.getString();
/*     */       }
/*     */     
/* 180 */     } catch (Exception e) {
/*     */       
/* 182 */       JDALogger.getLog(ErrorResponseException.class).error("Failed to parse parts of error response. Body: {}", response.getString(), e);
/*     */     } 
/*     */     
/* 185 */     return new ErrorResponseException(errorResponse, response, code, meaning, schemaErrors);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void parseSchema(List<SchemaError> schemaErrors, String currentLocation, DataObject errors) {
/* 191 */     for (String name : errors.keys()) {
/*     */       
/* 193 */       if (name.equals("_errors")) {
/*     */         
/* 195 */         schemaErrors.add(parseSchemaError(currentLocation, errors));
/*     */         continue;
/*     */       } 
/* 198 */       DataObject schemaError = errors.getObject(name);
/* 199 */       if (!schemaError.isNull("_errors")) {
/*     */ 
/*     */         
/* 202 */         schemaErrors.add(parseSchemaError(currentLocation + name, schemaError)); continue;
/*     */       } 
/* 204 */       if (schemaError.keys().stream().allMatch(Helpers::isNumeric)) {
/*     */ 
/*     */         
/* 207 */         for (String index : schemaError.keys()) {
/*     */           
/* 209 */           DataObject properties = schemaError.getObject(index);
/* 210 */           String str1 = String.format("%s%s[%s].", new Object[] { currentLocation, name, index });
/* 211 */           if (properties.hasKey("_errors")) {
/* 212 */             schemaErrors.add(parseSchemaError(str1.substring(0, str1.length() - 1), properties)); continue;
/*     */           } 
/* 214 */           parseSchema(schemaErrors, str1, properties);
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 220 */       String location = String.format("%s%s.", new Object[] { currentLocation, name });
/* 221 */       parseSchema(schemaErrors, location, schemaError);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static SchemaError parseSchemaError(String location, DataObject obj) {
/* 231 */     List<ErrorCode> codes = (List<ErrorCode>)obj.getArray("_errors").stream(DataArray::getObject).map(json -> new ErrorCode(json.getString("code"), json.getString("message"))).collect(Collectors.toList());
/* 232 */     return new SchemaError(location, codes);
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
/*     */   @Nonnull
/*     */   public static Consumer<Throwable> ignore(@Nonnull Collection<ErrorResponse> set) {
/* 260 */     return ignore(RestAction.getDefaultFailure(), set);
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
/*     */   @Nonnull
/*     */   public static Consumer<Throwable> ignore(@Nonnull ErrorResponse ignored, @Nonnull ErrorResponse... errorResponses) {
/* 290 */     return ignore(RestAction.getDefaultFailure(), ignored, errorResponses);
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
/*     */   @Nonnull
/*     */   public static Consumer<Throwable> ignore(@Nonnull Consumer<? super Throwable> orElse, @Nonnull ErrorResponse ignored, @Nonnull ErrorResponse... errorResponses) {
/* 322 */     return ignore(orElse, EnumSet.of(ignored, errorResponses));
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
/*     */   @Nonnull
/*     */   public static Consumer<Throwable> ignore(@Nonnull Consumer<? super Throwable> orElse, @Nonnull Collection<ErrorResponse> set) {
/* 352 */     Checks.notNull(orElse, "Callback");
/* 353 */     Checks.notEmpty(set, "Ignored collection");
/*     */     
/* 355 */     EnumSet<ErrorResponse> ignored = EnumSet.copyOf(set);
/* 356 */     return (new ErrorHandler(orElse)).ignore(ignored);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class ErrorCode
/*     */   {
/*     */     private final String code;
/*     */ 
/*     */     
/*     */     private final String message;
/*     */ 
/*     */     
/*     */     ErrorCode(String code, String message) {
/* 370 */       this.code = code;
/* 371 */       this.message = message;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getCode() {
/* 382 */       return this.code;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public String getMessage() {
/* 393 */       return this.message;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 399 */       return this.code + ": " + this.message;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static class SchemaError
/*     */   {
/*     */     private final String location;
/*     */ 
/*     */     
/*     */     private final List<ErrorResponseException.ErrorCode> errors;
/*     */ 
/*     */     
/*     */     private SchemaError(String location, List<ErrorResponseException.ErrorCode> codes) {
/* 414 */       this.location = location;
/* 415 */       this.errors = codes;
/*     */     }
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
/*     */     @Nonnull
/*     */     public String getLocation() {
/* 429 */       return this.location;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     @Nonnull
/*     */     public List<ErrorResponseException.ErrorCode> getErrors() {
/* 440 */       return this.errors;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 446 */       return (this.location.isEmpty() ? "" : (this.location + "\n")) + "\t- " + (String)this.errors.stream().map(Object::toString).collect(Collectors.joining("\n\t- "));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\exceptions\ErrorResponseException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */