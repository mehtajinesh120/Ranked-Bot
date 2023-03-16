/*     */ package org.yaml.snakeyaml;
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
/*     */ public class LoaderOptions
/*     */ {
/*     */   private boolean allowDuplicateKeys = true;
/*     */   private boolean wrappedToRootException = false;
/*  20 */   private int maxAliasesForCollections = 50;
/*     */   
/*     */   private boolean allowRecursiveKeys = false;
/*     */   private boolean processComments = false;
/*     */   private boolean enumCaseSensitive = true;
/*  25 */   private int nestingDepthLimit = 50;
/*  26 */   private int codePointLimit = 3145728;
/*     */   
/*     */   public final boolean isAllowDuplicateKeys() {
/*  29 */     return this.allowDuplicateKeys;
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
/*     */   public void setAllowDuplicateKeys(boolean allowDuplicateKeys) {
/*  47 */     this.allowDuplicateKeys = allowDuplicateKeys;
/*     */   }
/*     */   
/*     */   public final boolean isWrappedToRootException() {
/*  51 */     return this.wrappedToRootException;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setWrappedToRootException(boolean wrappedToRootException) {
/*  62 */     this.wrappedToRootException = wrappedToRootException;
/*     */   }
/*     */   
/*     */   public final int getMaxAliasesForCollections() {
/*  66 */     return this.maxAliasesForCollections;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxAliasesForCollections(int maxAliasesForCollections) {
/*  76 */     this.maxAliasesForCollections = maxAliasesForCollections;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAllowRecursiveKeys(boolean allowRecursiveKeys) {
/*  87 */     this.allowRecursiveKeys = allowRecursiveKeys;
/*     */   }
/*     */   
/*     */   public final boolean getAllowRecursiveKeys() {
/*  91 */     return this.allowRecursiveKeys;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LoaderOptions setProcessComments(boolean processComments) {
/* 100 */     this.processComments = processComments;
/* 101 */     return this;
/*     */   }
/*     */   
/*     */   public final boolean isProcessComments() {
/* 105 */     return this.processComments;
/*     */   }
/*     */   
/*     */   public final boolean isEnumCaseSensitive() {
/* 109 */     return this.enumCaseSensitive;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnumCaseSensitive(boolean enumCaseSensitive) {
/* 119 */     this.enumCaseSensitive = enumCaseSensitive;
/*     */   }
/*     */   
/*     */   public final int getNestingDepthLimit() {
/* 123 */     return this.nestingDepthLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNestingDepthLimit(int nestingDepthLimit) {
/* 133 */     this.nestingDepthLimit = nestingDepthLimit;
/*     */   }
/*     */   
/*     */   public final int getCodePointLimit() {
/* 137 */     return this.codePointLimit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCodePointLimit(int codePointLimit) {
/* 147 */     this.codePointLimit = codePointLimit;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\yaml\snakeyaml\LoaderOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */