/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
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
/*     */ abstract class NodeCursor
/*     */   extends JsonStreamContext
/*     */ {
/*     */   protected final NodeCursor _parent;
/*     */   protected String _currentName;
/*     */   protected Object _currentValue;
/*     */   
/*     */   public NodeCursor(int contextType, NodeCursor p) {
/*  34 */     this._type = contextType;
/*  35 */     this._index = -1;
/*  36 */     this._parent = p;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final NodeCursor getParent() {
/*  47 */     return this._parent;
/*     */   }
/*     */   
/*     */   public final String getCurrentName() {
/*  51 */     return this._currentName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void overrideCurrentName(String name) {
/*  58 */     this._currentName = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getCurrentValue() {
/*  63 */     return this._currentValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentValue(Object v) {
/*  68 */     this._currentValue = v;
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
/*     */   public final NodeCursor iterateChildren() {
/*  89 */     JsonNode n = currentNode();
/*  90 */     if (n == null) throw new IllegalStateException("No current node"); 
/*  91 */     if (n.isArray()) {
/*  92 */       return new ArrayCursor(n, this);
/*     */     }
/*  94 */     if (n.isObject()) {
/*  95 */       return new ObjectCursor(n, this);
/*     */     }
/*  97 */     throw new IllegalStateException("Current node of type " + n.getClass().getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract JsonToken nextToken();
/*     */ 
/*     */   
/*     */   public abstract JsonToken nextValue();
/*     */   
/*     */   public abstract JsonToken endToken();
/*     */   
/*     */   public abstract JsonNode currentNode();
/*     */   
/*     */   public abstract boolean currentHasChildren();
/*     */   
/*     */   protected static final class RootCursor
/*     */     extends NodeCursor
/*     */   {
/*     */     protected JsonNode _node;
/*     */     protected boolean _done = false;
/*     */     
/*     */     public RootCursor(JsonNode n, NodeCursor p) {
/* 119 */       super(0, p);
/* 120 */       this._node = n;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void overrideCurrentName(String name) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonToken nextToken() {
/* 130 */       if (!this._done) {
/* 131 */         this._index++;
/* 132 */         this._done = true;
/* 133 */         return this._node.asToken();
/*     */       } 
/* 135 */       this._node = null;
/* 136 */       return null;
/*     */     }
/*     */     
/*     */     public JsonToken nextValue() {
/* 140 */       return nextToken();
/*     */     } public JsonToken endToken() {
/* 142 */       return null;
/*     */     } public JsonNode currentNode() {
/* 144 */       return this._node;
/*     */     } public boolean currentHasChildren() {
/* 146 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected static final class ArrayCursor
/*     */     extends NodeCursor
/*     */   {
/*     */     protected Iterator<JsonNode> _contents;
/*     */     
/*     */     protected JsonNode _currentNode;
/*     */ 
/*     */     
/*     */     public ArrayCursor(JsonNode n, NodeCursor p) {
/* 160 */       super(1, p);
/* 161 */       this._contents = n.elements();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonToken nextToken() {
/* 167 */       if (!this._contents.hasNext()) {
/* 168 */         this._currentNode = null;
/* 169 */         return null;
/*     */       } 
/* 171 */       this._index++;
/* 172 */       this._currentNode = this._contents.next();
/* 173 */       return this._currentNode.asToken();
/*     */     }
/*     */     
/*     */     public JsonToken nextValue() {
/* 177 */       return nextToken();
/*     */     } public JsonToken endToken() {
/* 179 */       return JsonToken.END_ARRAY;
/*     */     }
/*     */     public JsonNode currentNode() {
/* 182 */       return this._currentNode;
/*     */     }
/*     */     
/*     */     public boolean currentHasChildren() {
/* 186 */       return (((ContainerNode)currentNode()).size() > 0);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static final class ObjectCursor
/*     */     extends NodeCursor
/*     */   {
/*     */     protected Iterator<Map.Entry<String, JsonNode>> _contents;
/*     */     
/*     */     protected Map.Entry<String, JsonNode> _current;
/*     */     
/*     */     protected boolean _needEntry;
/*     */ 
/*     */     
/*     */     public ObjectCursor(JsonNode n, NodeCursor p) {
/* 203 */       super(2, p);
/* 204 */       this._contents = ((ObjectNode)n).fields();
/* 205 */       this._needEntry = true;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonToken nextToken() {
/* 212 */       if (this._needEntry) {
/* 213 */         if (!this._contents.hasNext()) {
/* 214 */           this._currentName = null;
/* 215 */           this._current = null;
/* 216 */           return null;
/*     */         } 
/* 218 */         this._index++;
/* 219 */         this._needEntry = false;
/* 220 */         this._current = this._contents.next();
/* 221 */         this._currentName = (this._current == null) ? null : this._current.getKey();
/* 222 */         return JsonToken.FIELD_NAME;
/*     */       } 
/* 224 */       this._needEntry = true;
/* 225 */       return ((JsonNode)this._current.getValue()).asToken();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public JsonToken nextValue() {
/* 231 */       JsonToken t = nextToken();
/* 232 */       if (t == JsonToken.FIELD_NAME) {
/* 233 */         t = nextToken();
/*     */       }
/* 235 */       return t;
/*     */     }
/*     */     
/*     */     public JsonToken endToken() {
/* 239 */       return JsonToken.END_OBJECT;
/*     */     }
/*     */     
/*     */     public JsonNode currentNode() {
/* 243 */       return (this._current == null) ? null : this._current.getValue();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean currentHasChildren() {
/* 248 */       return (((ContainerNode)currentNode()).size() > 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\fasterxml\jackson\databind\node\NodeCursor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */