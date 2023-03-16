/*     */ package org.apache.commons.collections4.list;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NodeCachingLinkedList<E>
/*     */   extends AbstractLinkedList<E>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 6897789178562232073L;
/*     */   private static final int DEFAULT_MAXIMUM_CACHE_SIZE = 20;
/*     */   private transient AbstractLinkedList.Node<E> firstCachedNode;
/*     */   private transient int cacheSize;
/*     */   private int maximumCacheSize;
/*     */   
/*     */   public NodeCachingLinkedList() {
/*  74 */     this(20);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NodeCachingLinkedList(Collection<? extends E> coll) {
/*  83 */     super(coll);
/*  84 */     this.maximumCacheSize = 20;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NodeCachingLinkedList(int maximumCacheSize) {
/*  94 */     this.maximumCacheSize = maximumCacheSize;
/*  95 */     init();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getMaximumCacheSize() {
/* 105 */     return this.maximumCacheSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setMaximumCacheSize(int maximumCacheSize) {
/* 114 */     this.maximumCacheSize = maximumCacheSize;
/* 115 */     shrinkCacheToMaximumSize();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void shrinkCacheToMaximumSize() {
/* 123 */     while (this.cacheSize > this.maximumCacheSize) {
/* 124 */       getNodeFromCache();
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
/*     */   protected AbstractLinkedList.Node<E> getNodeFromCache() {
/* 136 */     if (this.cacheSize == 0) {
/* 137 */       return null;
/*     */     }
/* 139 */     AbstractLinkedList.Node<E> cachedNode = this.firstCachedNode;
/* 140 */     this.firstCachedNode = cachedNode.next;
/* 141 */     cachedNode.next = null;
/*     */     
/* 143 */     this.cacheSize--;
/* 144 */     return cachedNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean isCacheFull() {
/* 153 */     return (this.cacheSize >= this.maximumCacheSize);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addNodeToCache(AbstractLinkedList.Node<E> node) {
/* 163 */     if (isCacheFull()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 168 */     AbstractLinkedList.Node<E> nextCachedNode = this.firstCachedNode;
/* 169 */     node.previous = null;
/* 170 */     node.next = nextCachedNode;
/* 171 */     node.setValue(null);
/* 172 */     this.firstCachedNode = node;
/* 173 */     this.cacheSize++;
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
/*     */   protected AbstractLinkedList.Node<E> createNode(E value) {
/* 186 */     AbstractLinkedList.Node<E> cachedNode = getNodeFromCache();
/* 187 */     if (cachedNode == null) {
/* 188 */       return super.createNode(value);
/*     */     }
/* 190 */     cachedNode.setValue(value);
/* 191 */     return cachedNode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void removeNode(AbstractLinkedList.Node<E> node) {
/* 202 */     super.removeNode(node);
/* 203 */     addNodeToCache(node);
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
/*     */   protected void removeAllNodes() {
/* 217 */     int numberOfNodesToCache = Math.min(this.size, this.maximumCacheSize - this.cacheSize);
/* 218 */     AbstractLinkedList.Node<E> node = this.header.next;
/* 219 */     for (int currentIndex = 0; currentIndex < numberOfNodesToCache; currentIndex++) {
/* 220 */       AbstractLinkedList.Node<E> oldNode = node;
/* 221 */       node = node.next;
/* 222 */       addNodeToCache(oldNode);
/*     */     } 
/* 224 */     super.removeAllNodes();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 232 */     out.defaultWriteObject();
/* 233 */     doWriteObject(out);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 240 */     in.defaultReadObject();
/* 241 */     doReadObject(in);
/*     */   }
/*     */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\list\NodeCachingLinkedList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */