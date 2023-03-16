/*     */ package org.apache.commons.collections4.comparators;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ public class FixedOrderComparator<T>
/*     */   implements Comparator<T>, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 82794675842863201L;
/*     */   
/*     */   public enum UnknownObjectBehavior
/*     */   {
/*  59 */     BEFORE, AFTER, EXCEPTION;
/*     */   }
/*     */ 
/*     */   
/*  63 */   private final Map<T, Integer> map = new HashMap<T, Integer>();
/*     */ 
/*     */   
/*  66 */   private int counter = 0;
/*     */ 
/*     */   
/*     */   private boolean isLocked = false;
/*     */ 
/*     */   
/*  72 */   private UnknownObjectBehavior unknownObjectBehavior = UnknownObjectBehavior.EXCEPTION;
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
/*     */   public FixedOrderComparator() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public FixedOrderComparator(T... items) {
/*  94 */     if (items == null) {
/*  95 */       throw new NullPointerException("The list of items must not be null");
/*     */     }
/*  97 */     for (T item : items) {
/*  98 */       add(item);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public FixedOrderComparator(List<T> items) {
/* 113 */     if (items == null) {
/* 114 */       throw new NullPointerException("The list of items must not be null");
/*     */     }
/* 116 */     for (T t : items) {
/* 117 */       add(t);
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
/*     */ 
/*     */   
/*     */   public boolean isLocked() {
/* 131 */     return this.isLocked;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkLocked() {
/* 140 */     if (isLocked()) {
/* 141 */       throw new UnsupportedOperationException("Cannot modify a FixedOrderComparator after a comparison");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public UnknownObjectBehavior getUnknownObjectBehavior() {
/* 151 */     return this.unknownObjectBehavior;
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
/*     */   public void setUnknownObjectBehavior(UnknownObjectBehavior unknownObjectBehavior) {
/* 163 */     checkLocked();
/* 164 */     if (unknownObjectBehavior == null) {
/* 165 */       throw new NullPointerException("Unknown object behavior must not be null");
/*     */     }
/* 167 */     this.unknownObjectBehavior = unknownObjectBehavior;
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
/*     */   public boolean add(T obj) {
/* 183 */     checkLocked();
/* 184 */     Integer position = this.map.put(obj, Integer.valueOf(this.counter++));
/* 185 */     return (position == null);
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
/*     */   public boolean addAsEqual(T existingObj, T newObj) {
/* 202 */     checkLocked();
/* 203 */     Integer position = this.map.get(existingObj);
/* 204 */     if (position == null) {
/* 205 */       throw new IllegalArgumentException((new StringBuilder()).append(existingObj).append(" not known to ").append(this).toString());
/*     */     }
/* 207 */     Integer result = this.map.put(newObj, position);
/* 208 */     return (result == null);
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
/*     */   public int compare(T obj1, T obj2) {
/* 229 */     this.isLocked = true;
/* 230 */     Integer position1 = this.map.get(obj1);
/* 231 */     Integer position2 = this.map.get(obj2);
/* 232 */     if (position1 == null || position2 == null) {
/* 233 */       Object unknownObj; switch (this.unknownObjectBehavior) {
/*     */         case BEFORE:
/* 235 */           return (position1 == null) ? ((position2 == null) ? 0 : -1) : 1;
/*     */         case AFTER:
/* 237 */           return (position1 == null) ? ((position2 == null) ? 0 : 1) : -1;
/*     */         case EXCEPTION:
/* 239 */           unknownObj = (position1 == null) ? obj1 : obj2;
/* 240 */           throw new IllegalArgumentException("Attempting to compare unknown object " + unknownObj);
/*     */       } 
/*     */       
/* 243 */       throw new UnsupportedOperationException("Unknown unknownObjectBehavior: " + this.unknownObjectBehavior);
/*     */     } 
/*     */ 
/*     */     
/* 247 */     return position1.compareTo(position2);
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
/*     */   public int hashCode() {
/* 259 */     int total = 17;
/* 260 */     total = total * 37 + ((this.map == null) ? 0 : this.map.hashCode());
/* 261 */     total = total * 37 + ((this.unknownObjectBehavior == null) ? 0 : this.unknownObjectBehavior.hashCode());
/* 262 */     total = total * 37 + this.counter;
/* 263 */     total = total * 37 + (this.isLocked ? 0 : 1);
/* 264 */     return total;
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
/*     */   public boolean equals(Object object) {
/* 281 */     if (this == object) {
/* 282 */       return true;
/*     */     }
/* 284 */     if (null == object) {
/* 285 */       return false;
/*     */     }
/* 287 */     if (object.getClass().equals(getClass())) {
/* 288 */       FixedOrderComparator<?> comp = (FixedOrderComparator)object;
/* 289 */       return (((null == this.map) ? (null == comp.map) : this.map.equals(comp.map)) && ((null == this.unknownObjectBehavior) ? (null == comp.unknownObjectBehavior) : (this.unknownObjectBehavior == comp.unknownObjectBehavior && this.counter == comp.counter && this.isLocked == comp.isLocked && this.unknownObjectBehavior == comp.unknownObjectBehavior)));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 296 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\comparators\FixedOrderComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */