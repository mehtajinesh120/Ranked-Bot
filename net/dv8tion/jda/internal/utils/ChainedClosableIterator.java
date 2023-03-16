/*     */ package net.dv8tion.jda.internal.utils;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import net.dv8tion.jda.api.utils.ClosableIterator;
/*     */ import net.dv8tion.jda.api.utils.cache.CacheView;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChainedClosableIterator<T>
/*     */   implements ClosableIterator<T>
/*     */ {
/*  30 */   private static final Logger log = JDALogger.getLog(ClosableIterator.class);
/*     */   
/*     */   private final Set<T> items;
/*     */   
/*     */   private final Iterator<? extends CacheView<T>> generator;
/*     */   private ClosableIterator<T> currentIterator;
/*     */   private T item;
/*     */   
/*     */   public ChainedClosableIterator(Iterator<? extends CacheView<T>> generator) {
/*  39 */     this.items = new HashSet<>();
/*  40 */     this.generator = generator;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<T> getItems() {
/*  45 */     return this.items;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/*  51 */     if (this.currentIterator != null)
/*  52 */       this.currentIterator.close(); 
/*  53 */     this.currentIterator = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasNext() {
/*  59 */     if (this.item != null) {
/*  60 */       return true;
/*     */     }
/*  62 */     if (this.currentIterator != null)
/*     */     {
/*  64 */       if (!this.currentIterator.hasNext()) {
/*     */         
/*  66 */         this.currentIterator.close();
/*  67 */         this.currentIterator = null;
/*     */       }
/*     */       else {
/*     */         
/*  71 */         if (findNext()) return true; 
/*  72 */         this.currentIterator.close();
/*  73 */         this.currentIterator = null;
/*     */       } 
/*     */     }
/*     */     
/*  77 */     return processChain();
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean processChain() {
/*  82 */     while (this.item == null) {
/*     */       
/*  84 */       CacheView<T> view = null;
/*  85 */       while (this.generator.hasNext()) {
/*     */         
/*  87 */         view = this.generator.next();
/*  88 */         if (!view.isEmpty())
/*     */           break; 
/*  90 */         view = null;
/*     */       } 
/*  92 */       if (view == null) {
/*  93 */         return false;
/*     */       }
/*     */       
/*  96 */       this.currentIterator = view.lockedIterator();
/*  97 */       if (findNext())
/*     */         break; 
/*  99 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean findNext() {
/* 104 */     while (this.currentIterator.hasNext()) {
/*     */       
/* 106 */       T next = (T)this.currentIterator.next();
/* 107 */       if (this.items.contains(next))
/*     */         continue; 
/* 109 */       this.item = next;
/* 110 */       this.items.add(this.item);
/* 111 */       return true;
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T next() {
/* 119 */     if (!hasNext())
/* 120 */       throw new NoSuchElementException(); 
/* 121 */     T tmp = this.item;
/* 122 */     this.item = null;
/* 123 */     return tmp;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected void finalize() {
/* 130 */     if (this.currentIterator != null) {
/*     */       
/* 132 */       log.error("Finalizing without closing, performing force close on lock");
/* 133 */       close();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\interna\\utils\ChainedClosableIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */