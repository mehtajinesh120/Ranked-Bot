/*     */ package org.yaml.snakeyaml.env;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.constructor.AbstractConstruct;
/*     */ import org.yaml.snakeyaml.constructor.Constructor;
/*     */ import org.yaml.snakeyaml.error.MissingEnvironmentVariableException;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public class EnvScalarConstructor
/*     */   extends Constructor
/*     */ {
/*  39 */   public static final Tag ENV_TAG = new Tag("!ENV");
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static final Pattern ENV_FORMAT = Pattern.compile("^\\$\\{\\s*((?<name>\\w+)((?<separator>:?(-|\\?))(?<value>\\S+)?)?)\\s*\\}$");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public EnvScalarConstructor() {
/*  49 */     this.yamlConstructors.put(ENV_TAG, new ConstructEnv());
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
/*     */   public EnvScalarConstructor(TypeDescription theRoot, Collection<TypeDescription> moreTDs, LoaderOptions loadingConfig) {
/*  61 */     super(theRoot, moreTDs, loadingConfig);
/*  62 */     this.yamlConstructors.put(ENV_TAG, new ConstructEnv());
/*     */   }
/*     */   
/*     */   private class ConstructEnv
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/*  68 */       String val = EnvScalarConstructor.this.constructScalar((ScalarNode)node);
/*  69 */       Matcher matcher = EnvScalarConstructor.ENV_FORMAT.matcher(val);
/*  70 */       matcher.matches();
/*  71 */       String name = matcher.group("name");
/*  72 */       String value = matcher.group("value");
/*  73 */       String separator = matcher.group("separator");
/*  74 */       return EnvScalarConstructor.this.apply(name, separator, (value != null) ? value : "", EnvScalarConstructor.this.getEnv(name));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private ConstructEnv() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String apply(String name, String separator, String value, String environment) {
/*  88 */     if (environment != null && !environment.isEmpty()) {
/*  89 */       return environment;
/*     */     }
/*     */     
/*  92 */     if (separator != null) {
/*     */       
/*  94 */       if (separator.equals("?") && 
/*  95 */         environment == null) {
/*  96 */         throw new MissingEnvironmentVariableException("Missing mandatory variable " + name + ": " + value);
/*     */       }
/*     */ 
/*     */       
/* 100 */       if (separator.equals(":?")) {
/* 101 */         if (environment == null) {
/* 102 */           throw new MissingEnvironmentVariableException("Missing mandatory variable " + name + ": " + value);
/*     */         }
/*     */         
/* 105 */         if (environment.isEmpty()) {
/* 106 */           throw new MissingEnvironmentVariableException("Empty mandatory variable " + name + ": " + value);
/*     */         }
/*     */       } 
/*     */       
/* 110 */       if (separator.startsWith(":")) {
/* 111 */         if (environment == null || environment.isEmpty()) {
/* 112 */           return value;
/*     */         }
/*     */       }
/* 115 */       else if (environment == null) {
/* 116 */         return value;
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEnv(String key) {
/* 130 */     return System.getenv(key);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\env\EnvScalarConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */