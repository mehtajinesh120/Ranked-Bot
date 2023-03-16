/*     */ package org.apache.commons.collections4.functors;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import org.apache.commons.collections4.Closure;
/*     */ import org.apache.commons.collections4.Predicate;
/*     */ import org.apache.commons.collections4.Transformer;
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
/*     */ class FunctorUtils
/*     */ {
/*     */   static <T> Predicate<T>[] copy(Predicate<? super T>... predicates) {
/*  50 */     if (predicates == null) {
/*  51 */       return null;
/*     */     }
/*  53 */     return (Predicate<T>[])predicates.clone();
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
/*     */   static <T> Predicate<T> coerce(Predicate<? super T> predicate) {
/*  70 */     return (Predicate)predicate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void validate(Predicate<?>... predicates) {
/*  79 */     if (predicates == null) {
/*  80 */       throw new NullPointerException("The predicate array must not be null");
/*     */     }
/*  82 */     for (int i = 0; i < predicates.length; i++) {
/*  83 */       if (predicates[i] == null) {
/*  84 */         throw new NullPointerException("The predicate array must not contain a null predicate, index " + i + " was null");
/*     */       }
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
/*     */   static <T> Predicate<? super T>[] validate(Collection<? extends Predicate<? super T>> predicates) {
/*  97 */     if (predicates == null) {
/*  98 */       throw new NullPointerException("The predicate collection must not be null");
/*     */     }
/*     */ 
/*     */     
/* 102 */     Predicate[] arrayOfPredicate = new Predicate[predicates.size()];
/* 103 */     int i = 0;
/* 104 */     for (Predicate<? super T> predicate : predicates) {
/* 105 */       arrayOfPredicate[i] = predicate;
/* 106 */       if (arrayOfPredicate[i] == null) {
/* 107 */         throw new NullPointerException("The predicate collection must not contain a null predicate, index " + i + " was null");
/*     */       }
/*     */       
/* 110 */       i++;
/*     */     } 
/* 112 */     return (Predicate<? super T>[])arrayOfPredicate;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <E> Closure<E>[] copy(Closure<? super E>... closures) {
/* 123 */     if (closures == null) {
/* 124 */       return null;
/*     */     }
/* 126 */     return (Closure<E>[])closures.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void validate(Closure<?>... closures) {
/* 135 */     if (closures == null) {
/* 136 */       throw new NullPointerException("The closure array must not be null");
/*     */     }
/* 138 */     for (int i = 0; i < closures.length; i++) {
/* 139 */       if (closures[i] == null) {
/* 140 */         throw new NullPointerException("The closure array must not contain a null closure, index " + i + " was null");
/*     */       }
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
/*     */ 
/*     */   
/*     */   static <T> Closure<T> coerce(Closure<? super T> closure) {
/* 158 */     return (Closure)closure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static <I, O> Transformer<I, O>[] copy(Transformer<? super I, ? extends O>... transformers) {
/* 169 */     if (transformers == null) {
/* 170 */       return null;
/*     */     }
/* 172 */     return (Transformer<I, O>[])transformers.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void validate(Transformer<?, ?>... transformers) {
/* 181 */     if (transformers == null) {
/* 182 */       throw new NullPointerException("The transformer array must not be null");
/*     */     }
/* 184 */     for (int i = 0; i < transformers.length; i++) {
/* 185 */       if (transformers[i] == null) {
/* 186 */         throw new NullPointerException("The transformer array must not contain a null transformer, index " + i + " was null");
/*     */       }
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
/*     */ 
/*     */   
/*     */   static <I, O> Transformer<I, O> coerce(Transformer<? super I, ? extends O> transformer) {
/* 204 */     return (Transformer)transformer;
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\functors\FunctorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */