/*    */ package org.apache.commons.collections4.iterators;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import org.w3c.dom.Node;
/*    */ import org.w3c.dom.NodeList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NodeListIterator
/*    */   implements Iterator<Node>
/*    */ {
/*    */   private final NodeList nodeList;
/* 39 */   private int index = 0;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NodeListIterator(Node node) {
/* 49 */     if (node == null) {
/* 50 */       throw new NullPointerException("Node must not be null.");
/*    */     }
/* 52 */     this.nodeList = node.getChildNodes();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NodeListIterator(NodeList nodeList) {
/* 63 */     if (nodeList == null) {
/* 64 */       throw new NullPointerException("NodeList must not be null.");
/*    */     }
/* 66 */     this.nodeList = nodeList;
/*    */   }
/*    */   
/*    */   public boolean hasNext() {
/* 70 */     return (this.nodeList == null) ? false : ((this.index < this.nodeList.getLength()));
/*    */   }
/*    */   
/*    */   public Node next() {
/* 74 */     if (this.nodeList != null && this.index < this.nodeList.getLength()) {
/* 75 */       return this.nodeList.item(this.index++);
/*    */     }
/* 77 */     throw new NoSuchElementException("underlying nodeList has no more elements");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void remove() {
/* 86 */     throw new UnsupportedOperationException("remove() method not supported for a NodeListIterator.");
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\iterators\NodeListIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */