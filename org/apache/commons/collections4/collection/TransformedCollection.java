/*     */ package org.apache.commons.collections4.collection;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TransformedCollection<E>
/*     */   extends AbstractCollectionDecorator<E>
/*     */ {
/*     */   private static final long serialVersionUID = 8692300188161871514L;
/*     */   protected final Transformer<? super E, ? extends E> transformer;
/*     */   
/*     */   public static <E> TransformedCollection<E> transformingCollection(Collection<E> coll, Transformer<? super E, ? extends E> transformer) {
/*  63 */     return new TransformedCollection<E>(coll, transformer);
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
/*     */   public static <E> TransformedCollection<E> transformedCollection(Collection<E> collection, Transformer<? super E, ? extends E> transformer) {
/*  84 */     TransformedCollection<E> decorated = new TransformedCollection<E>(collection, transformer);
/*     */     
/*  86 */     if (collection.size() > 0) {
/*     */       
/*  88 */       E[] values = (E[])collection.toArray();
/*  89 */       collection.clear();
/*  90 */       for (E value : values) {
/*  91 */         decorated.decorated().add(transformer.transform(value));
/*     */       }
/*     */     } 
/*  94 */     return decorated;
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
/*     */   protected TransformedCollection(Collection<E> coll, Transformer<? super E, ? extends E> transformer) {
/* 109 */     super(coll);
/* 110 */     if (transformer == null) {
/* 111 */       throw new NullPointerException("Transformer must not be null");
/*     */     }
/* 113 */     this.transformer = transformer;
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
/*     */   protected E transform(E object) {
/* 125 */     return (E)this.transformer.transform(object);
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
/*     */   protected Collection<E> transform(Collection<? extends E> coll) {
/* 137 */     List<E> list = new ArrayList<E>(coll.size());
/* 138 */     for (E item : coll) {
/* 139 */       list.add(transform(item));
/*     */     }
/* 141 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E object) {
/* 147 */     return decorated().add(transform(object));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> coll) {
/* 152 */     return decorated().addAll(transform(coll));
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\collection\TransformedCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */