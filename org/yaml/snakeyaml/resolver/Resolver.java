/*     */ package org.yaml.snakeyaml.resolver;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
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
/*     */ public class Resolver
/*     */ {
/*  30 */   public static final Pattern BOOL = Pattern.compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   public static final Pattern FLOAT = Pattern.compile("^([-+]?(?:[0-9][0-9_]*)\\.[0-9_]*(?:[eE][-+]?[0-9]+)?|[-+]?(?:[0-9][0-9_]*)(?:[eE][-+]?[0-9]+)|[-+]?\\.[0-9_]+(?:[eE][-+]?[0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   public static final Pattern INT = Pattern.compile("^(?:[-+]?0b_*[0-1][0-1_]*|[-+]?0_*[0-7][0-7_]*|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x_*[0-9a-fA-F][0-9a-fA-F_]*|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   public static final Pattern MERGE = Pattern.compile("^(?:<<)$");
/*  49 */   public static final Pattern NULL = Pattern.compile("^(?:~|null|Null|NULL| )$");
/*  50 */   public static final Pattern EMPTY = Pattern.compile("^$");
/*  51 */   public static final Pattern TIMESTAMP = Pattern.compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
/*     */   
/*  53 */   public static final Pattern VALUE = Pattern.compile("^(?:=)$");
/*  54 */   public static final Pattern YAML = Pattern.compile("^(?:!|&|\\*)$");
/*     */   
/*  56 */   protected Map<Character, List<ResolverTuple>> yamlImplicitResolvers = new HashMap<>();
/*     */ 
/*     */   
/*     */   protected void addImplicitResolvers() {
/*  60 */     addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO", 10);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  65 */     addImplicitResolver(Tag.INT, INT, "-+0123456789");
/*  66 */     addImplicitResolver(Tag.FLOAT, FLOAT, "-+0123456789.");
/*  67 */     addImplicitResolver(Tag.MERGE, MERGE, "<", 10);
/*  68 */     addImplicitResolver(Tag.NULL, NULL, "~nN\000", 10);
/*  69 */     addImplicitResolver(Tag.NULL, EMPTY, null, 10);
/*  70 */     addImplicitResolver(Tag.TIMESTAMP, TIMESTAMP, "0123456789", 50);
/*     */ 
/*     */     
/*  73 */     addImplicitResolver(Tag.YAML, YAML, "!&*", 10);
/*     */   }
/*     */   
/*     */   public Resolver() {
/*  77 */     addImplicitResolvers();
/*     */   }
/*     */   
/*     */   public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
/*  81 */     addImplicitResolver(tag, regexp, first, 1024);
/*     */   }
/*     */   
/*     */   public void addImplicitResolver(Tag tag, Pattern regexp, String first, int limit) {
/*  85 */     if (first == null) {
/*  86 */       List<ResolverTuple> curr = this.yamlImplicitResolvers.get(null);
/*  87 */       if (curr == null) {
/*  88 */         curr = new ArrayList<>();
/*  89 */         this.yamlImplicitResolvers.put(null, curr);
/*     */       } 
/*  91 */       curr.add(new ResolverTuple(tag, regexp, limit));
/*     */     } else {
/*  93 */       char[] chrs = first.toCharArray();
/*  94 */       for (int i = 0, j = chrs.length; i < j; i++) {
/*  95 */         Character theC = Character.valueOf(chrs[i]);
/*  96 */         if (theC.charValue() == '\000')
/*     */         {
/*  98 */           theC = null;
/*     */         }
/* 100 */         List<ResolverTuple> curr = this.yamlImplicitResolvers.get(theC);
/* 101 */         if (curr == null) {
/* 102 */           curr = new ArrayList<>();
/* 103 */           this.yamlImplicitResolvers.put(theC, curr);
/*     */         } 
/* 105 */         curr.add(new ResolverTuple(tag, regexp, limit));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Tag resolve(NodeId kind, String value, boolean implicit) {
/* 111 */     if (kind == NodeId.scalar && implicit) {
/*     */       List<ResolverTuple> resolvers;
/* 113 */       if (value.length() == 0) {
/* 114 */         resolvers = this.yamlImplicitResolvers.get(Character.valueOf(false));
/*     */       } else {
/* 116 */         resolvers = this.yamlImplicitResolvers.get(Character.valueOf(value.charAt(0)));
/*     */       } 
/* 118 */       if (resolvers != null) {
/* 119 */         for (ResolverTuple v : resolvers) {
/* 120 */           Tag tag = v.getTag();
/* 121 */           Pattern regexp = v.getRegexp();
/* 122 */           if (value.length() <= v.getLimit() && regexp.matcher(value).matches()) {
/* 123 */             return tag;
/*     */           }
/*     */         } 
/*     */       }
/* 127 */       if (this.yamlImplicitResolvers.containsKey(null))
/*     */       {
/* 129 */         for (ResolverTuple v : this.yamlImplicitResolvers.get(null)) {
/* 130 */           Tag tag = v.getTag();
/* 131 */           Pattern regexp = v.getRegexp();
/* 132 */           if (value.length() <= v.getLimit() && regexp.matcher(value).matches()) {
/* 133 */             return tag;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 138 */     switch (kind) {
/*     */       case scalar:
/* 140 */         return Tag.STR;
/*     */       case sequence:
/* 142 */         return Tag.SEQ;
/*     */     } 
/* 144 */     return Tag.MAP;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\resolver\Resolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */