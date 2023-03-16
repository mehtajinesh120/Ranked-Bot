/*     */ package net.dv8tion.jda.internal.requests.restaction.order;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import javax.annotation.Nonnull;
/*     */ import net.dv8tion.jda.api.JDA;
/*     */ import net.dv8tion.jda.api.requests.RestAction;
/*     */ import net.dv8tion.jda.api.requests.restaction.order.OrderAction;
/*     */ import net.dv8tion.jda.internal.requests.RestActionImpl;
/*     */ import net.dv8tion.jda.internal.requests.Route;
/*     */ import net.dv8tion.jda.internal.utils.Checks;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class OrderActionImpl<T, M extends OrderAction<T, M>>
/*     */   extends RestActionImpl<Void>
/*     */   implements OrderAction<T, M>
/*     */ {
/*     */   protected final List<T> orderList;
/*     */   protected final boolean ascendingOrder;
/*  39 */   protected int selectedPosition = -1;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OrderActionImpl(JDA api, Route.CompiledRoute route) {
/*  53 */     this(api, true, route);
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
/*     */   public OrderActionImpl(JDA api, boolean ascendingOrder, Route.CompiledRoute route) {
/*  70 */     super(api, route);
/*  71 */     this.orderList = new ArrayList<>();
/*  72 */     this.ascendingOrder = ascendingOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M setCheck(BooleanSupplier checks) {
/*  80 */     return (M)super.setCheck(checks);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M timeout(long timeout, @Nonnull TimeUnit unit) {
/*  88 */     return (M)super.timeout(timeout, unit);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M deadline(long timestamp) {
/*  96 */     return (M)super.deadline(timestamp);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAscendingOrder() {
/* 102 */     return this.ascendingOrder;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public List<T> getCurrentOrder() {
/* 109 */     return Collections.unmodifiableList(this.orderList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M selectPosition(int selectedPosition) {
/* 117 */     Checks.notNegative(selectedPosition, "Provided selectedPosition");
/* 118 */     Checks.check((selectedPosition < this.orderList.size()), "Provided selectedPosition is too big and is out of bounds. selectedPosition: " + selectedPosition);
/*     */     
/* 120 */     this.selectedPosition = selectedPosition;
/*     */     
/* 122 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M selectPosition(@Nonnull T selectedEntity) {
/* 129 */     Checks.notNull(selectedEntity, "Channel");
/* 130 */     validateInput(selectedEntity);
/*     */     
/* 132 */     return selectPosition(this.orderList.indexOf(selectedEntity));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSelectedPosition() {
/* 138 */     return this.selectedPosition;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public T getSelectedEntity() {
/* 145 */     if (this.selectedPosition == -1) {
/* 146 */       throw new IllegalStateException("No position has been selected yet");
/*     */     }
/* 148 */     return this.orderList.get(this.selectedPosition);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M moveUp(int amount) {
/* 155 */     Checks.notNegative(amount, "Provided amount");
/* 156 */     if (this.selectedPosition == -1)
/* 157 */       throw new IllegalStateException("Cannot move until an item has been selected. Use #selectPosition first."); 
/* 158 */     if (this.ascendingOrder) {
/*     */       
/* 160 */       Checks.check((this.selectedPosition - amount >= 0), "Amount provided to move up is too large and would be out of bounds.Selected position: " + this.selectedPosition + " Amount: " + amount + " Largest Position: " + this.orderList
/*     */           
/* 162 */           .size());
/*     */     }
/*     */     else {
/*     */       
/* 166 */       Checks.check((this.selectedPosition + amount < this.orderList.size()), "Amount provided to move up is too large and would be out of bounds.Selected position: " + this.selectedPosition + " Amount: " + amount + " Largest Position: " + this.orderList
/*     */           
/* 168 */           .size());
/*     */     } 
/*     */     
/* 171 */     if (this.ascendingOrder) {
/* 172 */       return moveTo(this.selectedPosition - amount);
/*     */     }
/* 174 */     return moveTo(this.selectedPosition + amount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M moveDown(int amount) {
/* 181 */     Checks.notNegative(amount, "Provided amount");
/* 182 */     if (this.selectedPosition == -1) {
/* 183 */       throw new IllegalStateException("Cannot move until an item has been selected. Use #selectPosition first.");
/*     */     }
/* 185 */     if (this.ascendingOrder) {
/*     */       
/* 187 */       Checks.check((this.selectedPosition + amount < this.orderList.size()), "Amount provided to move down is too large and would be out of bounds.Selected position: " + this.selectedPosition + " Amount: " + amount + " Largest Position: " + this.orderList
/*     */           
/* 189 */           .size());
/*     */     }
/*     */     else {
/*     */       
/* 193 */       Checks.check((this.selectedPosition - amount >= this.orderList.size()), "Amount provided to move down is too large and would be out of bounds.Selected position: " + this.selectedPosition + " Amount: " + amount + " Largest Position: " + this.orderList
/*     */           
/* 195 */           .size());
/*     */     } 
/*     */     
/* 198 */     if (this.ascendingOrder) {
/* 199 */       return moveTo(this.selectedPosition + amount);
/*     */     }
/* 201 */     return moveTo(this.selectedPosition - amount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M moveTo(int position) {
/* 209 */     Checks.notNegative(position, "Provided position");
/* 210 */     Checks.check((position < this.orderList.size()), "Provided position is too big and is out of bounds.");
/*     */     
/* 212 */     T selectedItem = this.orderList.remove(this.selectedPosition);
/* 213 */     this.orderList.add(position, selectedItem);
/*     */     
/* 215 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M swapPosition(int swapPosition) {
/* 223 */     Checks.notNegative(swapPosition, "Provided swapPosition");
/* 224 */     Checks.check((swapPosition < this.orderList.size()), "Provided swapPosition is too big and is out of bounds. swapPosition: " + swapPosition);
/*     */ 
/*     */     
/* 227 */     T selectedItem = this.orderList.get(this.selectedPosition);
/* 228 */     T swapItem = this.orderList.get(swapPosition);
/* 229 */     this.orderList.set(swapPosition, selectedItem);
/* 230 */     this.orderList.set(this.selectedPosition, swapItem);
/*     */     
/* 232 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M swapPosition(@Nonnull T swapEntity) {
/* 240 */     Checks.notNull(swapEntity, "Provided swapEntity");
/* 241 */     validateInput(swapEntity);
/*     */     
/* 243 */     return swapPosition(this.orderList.indexOf(swapEntity));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M reverseOrder() {
/* 251 */     Collections.reverse(this.orderList);
/* 252 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M shuffleOrder() {
/* 260 */     Collections.shuffle(this.orderList);
/* 261 */     return (M)this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nonnull
/*     */   public M sortOrder(@Nonnull Comparator<T> comparator) {
/* 269 */     Checks.notNull(comparator, "Provided comparator");
/*     */     
/* 271 */     this.orderList.sort(comparator);
/* 272 */     return (M)this;
/*     */   }
/*     */   
/*     */   protected abstract void validateInput(T paramT);
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\order\OrderActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */