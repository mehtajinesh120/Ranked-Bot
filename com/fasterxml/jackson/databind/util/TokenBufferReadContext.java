/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.json.JsonReadContext;
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
/*     */ public class TokenBufferReadContext
/*     */   extends JsonStreamContext
/*     */ {
/*     */   protected final JsonStreamContext _parent;
/*     */   protected final JsonLocation _startLocation;
/*     */   protected String _currentName;
/*     */   protected Object _currentValue;
/*     */   
/*     */   protected TokenBufferReadContext(JsonStreamContext base, Object srcRef) {
/*  34 */     super(base);
/*  35 */     this._parent = base.getParent();
/*  36 */     this._currentName = base.getCurrentName();
/*  37 */     this._currentValue = base.getCurrentValue();
/*  38 */     if (base instanceof JsonReadContext) {
/*  39 */       JsonReadContext rc = (JsonReadContext)base;
/*  40 */       this._startLocation = rc.getStartLocation(srcRef);
/*     */     } else {
/*  42 */       this._startLocation = JsonLocation.NA;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected TokenBufferReadContext(JsonStreamContext base, JsonLocation startLoc) {
/*  47 */     super(base);
/*  48 */     this._parent = base.getParent();
/*  49 */     this._currentName = base.getCurrentName();
/*  50 */     this._currentValue = base.getCurrentValue();
/*  51 */     this._startLocation = startLoc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TokenBufferReadContext() {
/*  59 */     super(0, -1);
/*  60 */     this._parent = null;
/*  61 */     this._startLocation = JsonLocation.NA;
/*     */   }
/*     */   
/*     */   protected TokenBufferReadContext(TokenBufferReadContext parent, int type, int index) {
/*  65 */     super(type, index);
/*  66 */     this._parent = parent;
/*  67 */     this._startLocation = parent._startLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getCurrentValue() {
/*  72 */     return this._currentValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentValue(Object v) {
/*  77 */     this._currentValue = v;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static TokenBufferReadContext createRootContext(JsonStreamContext origContext) {
/*  88 */     if (origContext == null) {
/*  89 */       return new TokenBufferReadContext();
/*     */     }
/*  91 */     return new TokenBufferReadContext(origContext, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public TokenBufferReadContext createChildArrayContext() {
/*  96 */     this._index++;
/*  97 */     return new TokenBufferReadContext(this, 1, -1);
/*     */   }
/*     */ 
/*     */   
/*     */   public TokenBufferReadContext createChildObjectContext() {
/* 102 */     this._index++;
/* 103 */     return new TokenBufferReadContext(this, 2, -1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TokenBufferReadContext parentOrCopy() {
/* 114 */     if (this._parent instanceof TokenBufferReadContext) {
/* 115 */       return (TokenBufferReadContext)this._parent;
/*     */     }
/* 117 */     if (this._parent == null) {
/* 118 */       return new TokenBufferReadContext();
/*     */     }
/* 120 */     return new TokenBufferReadContext(this._parent, this._startLocation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCurrentName() {
/* 129 */     return this._currentName;
/*     */   }
/*     */   public boolean hasCurrentName() {
/* 132 */     return (this._currentName != null);
/*     */   } public JsonStreamContext getParent() {
/* 134 */     return this._parent;
/*     */   }
/*     */   public void setCurrentName(String name) throws JsonProcessingException {
/* 137 */     this._currentName = name;
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
/*     */   public void updateForValue() {
/* 150 */     this._index++;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databin\\util\TokenBufferReadContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */