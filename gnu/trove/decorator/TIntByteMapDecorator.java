/*     */ package gnu.trove.decorator;
/*     */ 
/*     */ import gnu.trove.iterator.TIntByteIterator;
/*     */ import gnu.trove.map.TIntByteMap;
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class TIntByteMapDecorator
/*     */   extends AbstractMap<Integer, Byte>
/*     */   implements Map<Integer, Byte>, Externalizable, Cloneable
/*     */ {
/*     */   static final long serialVersionUID = 1L;
/*     */   protected TIntByteMap _map;
/*     */   
/*     */   public TIntByteMapDecorator() {}
/*     */   
/*     */   public TIntByteMapDecorator(TIntByteMap map) {
/*  72 */     this._map = map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TIntByteMap getMap() {
/*  82 */     return this._map;
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
/*     */   public Byte put(Integer key, Byte value) {
/*     */     int k;
/*     */     byte v;
/*  97 */     if (key == null) {
/*  98 */       k = this._map.getNoEntryKey();
/*     */     } else {
/* 100 */       k = unwrapKey(key);
/*     */     } 
/* 102 */     if (value == null) {
/* 103 */       v = this._map.getNoEntryValue();
/*     */     } else {
/* 105 */       v = unwrapValue(value);
/*     */     } 
/* 107 */     byte retval = this._map.put(k, v);
/* 108 */     if (retval == this._map.getNoEntryValue()) {
/* 109 */       return null;
/*     */     }
/* 111 */     return wrapValue(retval);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Byte get(Object key) {
/*     */     int k;
/* 123 */     if (key != null) {
/* 124 */       if (key instanceof Integer) {
/* 125 */         k = unwrapKey(key);
/*     */       } else {
/* 127 */         return null;
/*     */       } 
/*     */     } else {
/* 130 */       k = this._map.getNoEntryKey();
/*     */     } 
/* 132 */     byte v = this._map.get(k);
/*     */ 
/*     */ 
/*     */     
/* 136 */     if (v == this._map.getNoEntryValue()) {
/* 137 */       return null;
/*     */     }
/* 139 */     return wrapValue(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 148 */     this._map.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Byte remove(Object key) {
/*     */     int k;
/* 160 */     if (key != null) {
/* 161 */       if (key instanceof Integer) {
/* 162 */         k = unwrapKey(key);
/*     */       } else {
/* 164 */         return null;
/*     */       } 
/*     */     } else {
/* 167 */       k = this._map.getNoEntryKey();
/*     */     } 
/* 169 */     byte v = this._map.remove(k);
/*     */ 
/*     */ 
/*     */     
/* 173 */     if (v == this._map.getNoEntryValue()) {
/* 174 */       return null;
/*     */     }
/* 176 */     return wrapValue(v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<Map.Entry<Integer, Byte>> entrySet() {
/* 187 */     return new AbstractSet<Map.Entry<Integer, Byte>>() {
/*     */         public int size() {
/* 189 */           return TIntByteMapDecorator.this._map.size();
/*     */         }
/*     */         
/*     */         public boolean isEmpty() {
/* 193 */           return TIntByteMapDecorator.this.isEmpty();
/*     */         }
/*     */         
/*     */         public boolean contains(Object o) {
/* 197 */           if (o instanceof Map.Entry) {
/* 198 */             Object k = ((Map.Entry)o).getKey();
/* 199 */             Object v = ((Map.Entry)o).getValue();
/* 200 */             return (TIntByteMapDecorator.this.containsKey(k) && TIntByteMapDecorator.this.get(k).equals(v));
/*     */           } 
/*     */           
/* 203 */           return false;
/*     */         }
/*     */ 
/*     */         
/*     */         public Iterator<Map.Entry<Integer, Byte>> iterator() {
/* 208 */           return new Iterator<Map.Entry<Integer, Byte>>() {
/* 209 */               private final TIntByteIterator it = TIntByteMapDecorator.this._map.iterator();
/*     */               
/*     */               public Map.Entry<Integer, Byte> next() {
/* 212 */                 this.it.advance();
/* 213 */                 int ik = this.it.key();
/* 214 */                 final Integer key = (ik == TIntByteMapDecorator.this._map.getNoEntryKey()) ? null : TIntByteMapDecorator.this.wrapKey(ik);
/* 215 */                 byte iv = this.it.value();
/* 216 */                 final Byte v = (iv == TIntByteMapDecorator.this._map.getNoEntryValue()) ? null : TIntByteMapDecorator.this.wrapValue(iv);
/* 217 */                 return new Map.Entry<Integer, Byte>() {
/* 218 */                     private Byte val = v;
/*     */                     
/*     */                     public boolean equals(Object o) {
/* 221 */                       return (o instanceof Map.Entry && ((Map.Entry)o).getKey().equals(key) && ((Map.Entry)o).getValue().equals(this.val));
/*     */                     }
/*     */ 
/*     */ 
/*     */                     
/*     */                     public Integer getKey() {
/* 227 */                       return key;
/*     */                     }
/*     */                     
/*     */                     public Byte getValue() {
/* 231 */                       return this.val;
/*     */                     }
/*     */                     
/*     */                     public int hashCode() {
/* 235 */                       return key.hashCode() + this.val.hashCode();
/*     */                     }
/*     */                     
/*     */                     public Byte setValue(Byte value) {
/* 239 */                       this.val = value;
/* 240 */                       return TIntByteMapDecorator.this.put(key, value);
/*     */                     }
/*     */                   };
/*     */               }
/*     */               
/*     */               public boolean hasNext() {
/* 246 */                 return this.it.hasNext();
/*     */               }
/*     */               
/*     */               public void remove() {
/* 250 */                 this.it.remove();
/*     */               }
/*     */             };
/*     */         }
/*     */         
/*     */         public boolean add(Map.Entry<Integer, Byte> o) {
/* 256 */           throw new UnsupportedOperationException();
/*     */         }
/*     */         
/*     */         public boolean remove(Object o) {
/* 260 */           boolean modified = false;
/* 261 */           if (contains(o)) {
/*     */             
/* 263 */             Integer key = (Integer)((Map.Entry)o).getKey();
/* 264 */             TIntByteMapDecorator.this._map.remove(TIntByteMapDecorator.this.unwrapKey(key));
/* 265 */             modified = true;
/*     */           } 
/* 267 */           return modified;
/*     */         }
/*     */         
/*     */         public boolean addAll(Collection<? extends Map.Entry<Integer, Byte>> c) {
/* 271 */           throw new UnsupportedOperationException();
/*     */         }
/*     */         
/*     */         public void clear() {
/* 275 */           TIntByteMapDecorator.this.clear();
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsValue(Object val) {
/* 288 */     return (val instanceof Byte && this._map.containsValue(unwrapValue(val)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean containsKey(Object key) {
/* 299 */     if (key == null) return this._map.containsKey(this._map.getNoEntryKey()); 
/* 300 */     return (key instanceof Integer && this._map.containsKey(unwrapKey(key)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/* 310 */     return this._map.size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 320 */     return (size() == 0);
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
/*     */   public void putAll(Map<? extends Integer, ? extends Byte> map) {
/* 332 */     Iterator<? extends Map.Entry<? extends Integer, ? extends Byte>> it = map.entrySet().iterator();
/*     */     
/* 334 */     for (int i = map.size(); i-- > 0; ) {
/* 335 */       Map.Entry<? extends Integer, ? extends Byte> e = it.next();
/* 336 */       put(e.getKey(), e.getValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Integer wrapKey(int k) {
/* 348 */     return Integer.valueOf(k);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int unwrapKey(Object key) {
/* 359 */     return ((Integer)key).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Byte wrapValue(byte k) {
/* 370 */     return Byte.valueOf(k);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte unwrapValue(Object value) {
/* 381 */     return ((Byte)value).byteValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
/* 390 */     in.readByte();
/*     */ 
/*     */     
/* 393 */     this._map = (TIntByteMap)in.readObject();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeExternal(ObjectOutput out) throws IOException {
/* 400 */     out.writeByte(0);
/*     */ 
/*     */     
/* 403 */     out.writeObject(this._map);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\gnu\trove\decorator\TIntByteMapDecorator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */