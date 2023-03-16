/*     */ package gnu.trove.impl.hash;
/*     */ 
/*     */ import gnu.trove.impl.HashFunctions;
/*     */ import gnu.trove.procedure.TFloatProcedure;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class TFloatDoubleHash
/*     */   extends TPrimitiveHash
/*     */ {
/*     */   static final long serialVersionUID = 1L;
/*     */   public transient float[] _set;
/*     */   protected float no_entry_key;
/*     */   protected double no_entry_value;
/*     */   protected boolean consumeFreeSlot;
/*     */   
/*     */   public TFloatDoubleHash() {
/*  80 */     this.no_entry_key = 0.0F;
/*  81 */     this.no_entry_value = 0.0D;
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
/*     */   public TFloatDoubleHash(int initialCapacity) {
/*  93 */     super(initialCapacity);
/*  94 */     this.no_entry_key = 0.0F;
/*  95 */     this.no_entry_value = 0.0D;
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
/*     */   public TFloatDoubleHash(int initialCapacity, float loadFactor) {
/* 108 */     super(initialCapacity, loadFactor);
/* 109 */     this.no_entry_key = 0.0F;
/* 110 */     this.no_entry_value = 0.0D;
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
/*     */   public TFloatDoubleHash(int initialCapacity, float loadFactor, float no_entry_key, double no_entry_value) {
/* 125 */     super(initialCapacity, loadFactor);
/* 126 */     this.no_entry_key = no_entry_key;
/* 127 */     this.no_entry_value = no_entry_value;
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
/*     */   public float getNoEntryKey() {
/* 139 */     return this.no_entry_key;
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
/*     */   public double getNoEntryValue() {
/* 151 */     return this.no_entry_value;
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
/*     */   protected int setUp(int initialCapacity) {
/* 165 */     int capacity = super.setUp(initialCapacity);
/* 166 */     this._set = new float[capacity];
/* 167 */     return capacity;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(float val) {
/* 178 */     return (index(val) >= 0);
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
/*     */   public boolean forEach(TFloatProcedure procedure) {
/* 190 */     byte[] states = this._states;
/* 191 */     float[] set = this._set;
/* 192 */     for (int i = set.length; i-- > 0;) {
/* 193 */       if (states[i] == 1 && !procedure.execute(set[i])) {
/* 194 */         return false;
/*     */       }
/*     */     } 
/* 197 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeAt(int index) {
/* 207 */     this._set[index] = this.no_entry_key;
/* 208 */     super.removeAt(index);
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
/*     */   protected int index(float key) {
/* 221 */     byte[] states = this._states;
/* 222 */     float[] set = this._set;
/* 223 */     int length = states.length;
/* 224 */     int hash = HashFunctions.hash(key) & Integer.MAX_VALUE;
/* 225 */     int index = hash % length;
/* 226 */     byte state = states[index];
/*     */     
/* 228 */     if (state == 0) {
/* 229 */       return -1;
/*     */     }
/* 231 */     if (state == 1 && set[index] == key) {
/* 232 */       return index;
/*     */     }
/* 234 */     return indexRehashed(key, index, hash, state);
/*     */   }
/*     */ 
/*     */   
/*     */   int indexRehashed(float key, int index, int hash, byte state) {
/* 239 */     int length = this._set.length;
/* 240 */     int probe = 1 + hash % (length - 2);
/* 241 */     int loopIndex = index;
/*     */     
/*     */     do {
/* 244 */       index -= probe;
/* 245 */       if (index < 0) {
/* 246 */         index += length;
/*     */       }
/* 248 */       state = this._states[index];
/*     */       
/* 250 */       if (state == 0) {
/* 251 */         return -1;
/*     */       }
/*     */       
/* 254 */       if (key == this._set[index] && state != 2)
/* 255 */         return index; 
/* 256 */     } while (index != loopIndex);
/*     */     
/* 258 */     return -1;
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
/*     */   protected int insertKey(float val) {
/* 273 */     int hash = HashFunctions.hash(val) & Integer.MAX_VALUE;
/* 274 */     int index = hash % this._states.length;
/* 275 */     byte state = this._states[index];
/*     */     
/* 277 */     this.consumeFreeSlot = false;
/*     */     
/* 279 */     if (state == 0) {
/* 280 */       this.consumeFreeSlot = true;
/* 281 */       insertKeyAt(index, val);
/*     */       
/* 283 */       return index;
/*     */     } 
/*     */     
/* 286 */     if (state == 1 && this._set[index] == val) {
/* 287 */       return -index - 1;
/*     */     }
/*     */ 
/*     */     
/* 291 */     return insertKeyRehash(val, index, hash, state);
/*     */   }
/*     */ 
/*     */   
/*     */   int insertKeyRehash(float val, int index, int hash, byte state) {
/* 296 */     int length = this._set.length;
/* 297 */     int probe = 1 + hash % (length - 2);
/* 298 */     int loopIndex = index;
/* 299 */     int firstRemoved = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     do {
/* 306 */       if (state == 2 && firstRemoved == -1) {
/* 307 */         firstRemoved = index;
/*     */       }
/* 309 */       index -= probe;
/* 310 */       if (index < 0) {
/* 311 */         index += length;
/*     */       }
/* 313 */       state = this._states[index];
/*     */ 
/*     */       
/* 316 */       if (state == 0) {
/* 317 */         if (firstRemoved != -1) {
/* 318 */           insertKeyAt(firstRemoved, val);
/* 319 */           return firstRemoved;
/*     */         } 
/* 321 */         this.consumeFreeSlot = true;
/* 322 */         insertKeyAt(index, val);
/* 323 */         return index;
/*     */       } 
/*     */ 
/*     */       
/* 327 */       if (state == 1 && this._set[index] == val) {
/* 328 */         return -index - 1;
/*     */       
/*     */       }
/*     */     }
/* 332 */     while (index != loopIndex);
/*     */ 
/*     */ 
/*     */     
/* 336 */     if (firstRemoved != -1) {
/* 337 */       insertKeyAt(firstRemoved, val);
/* 338 */       return firstRemoved;
/*     */     } 
/*     */ 
/*     */     
/* 342 */     throw new IllegalStateException("No free or removed slots available. Key set full?!!");
/*     */   }
/*     */   
/*     */   void insertKeyAt(int index, float val) {
/* 346 */     this._set[index] = val;
/* 347 */     this._states[index] = 1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected int XinsertKey(float key) {
/* 353 */     byte[] states = this._states;
/* 354 */     float[] set = this._set;
/* 355 */     int length = states.length;
/* 356 */     int hash = HashFunctions.hash(key) & Integer.MAX_VALUE;
/* 357 */     int index = hash % length;
/* 358 */     byte state = states[index];
/*     */     
/* 360 */     this.consumeFreeSlot = false;
/*     */     
/* 362 */     if (state == 0) {
/* 363 */       this.consumeFreeSlot = true;
/* 364 */       set[index] = key;
/* 365 */       states[index] = 1;
/*     */       
/* 367 */       return index;
/* 368 */     }  if (state == 1 && set[index] == key) {
/* 369 */       return -index - 1;
/*     */     }
/*     */     
/* 372 */     int probe = 1 + hash % (length - 2);
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
/* 386 */     if (state != 2) {
/*     */       
/*     */       do {
/*     */         
/* 390 */         index -= probe;
/* 391 */         if (index < 0) {
/* 392 */           index += length;
/*     */         }
/* 394 */         state = states[index];
/* 395 */       } while (state == 1 && set[index] != key);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 401 */     if (state == 2) {
/* 402 */       int firstRemoved = index;
/* 403 */       while (state != 0 && (state == 2 || set[index] != key)) {
/* 404 */         index -= probe;
/* 405 */         if (index < 0) {
/* 406 */           index += length;
/*     */         }
/* 408 */         state = states[index];
/*     */       } 
/*     */       
/* 411 */       if (state == 1) {
/* 412 */         return -index - 1;
/*     */       }
/* 414 */       set[index] = key;
/* 415 */       states[index] = 1;
/*     */       
/* 417 */       return firstRemoved;
/*     */     } 
/*     */ 
/*     */     
/* 421 */     if (state == 1) {
/* 422 */       return -index - 1;
/*     */     }
/* 424 */     this.consumeFreeSlot = true;
/* 425 */     set[index] = key;
/* 426 */     states[index] = 1;
/*     */     
/* 428 */     return index;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 437 */     out.writeByte(0);
/*     */ 
/*     */     
/* 440 */     super.writeExternal(out);
/*     */ 
/*     */     
/* 443 */     out.writeFloat(this.no_entry_key);
/*     */ 
/*     */     
/* 446 */     out.writeDouble(this.no_entry_value);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 453 */     in.readByte();
/*     */ 
/*     */     
/* 456 */     super.readExternal(in);
/*     */ 
/*     */     
/* 459 */     this.no_entry_key = in.readFloat();
/*     */ 
/*     */     
/* 462 */     this.no_entry_value = in.readDouble();
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\impl\hash\TFloatDoubleHash.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */