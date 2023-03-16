/*    */ package org.apache.commons.collections4.multiset;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
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
/*    */ 
/*    */ 
/*    */ public class HashMultiSet<E>
/*    */   extends AbstractMapMultiSet<E>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 20150610L;
/*    */   
/*    */   public HashMultiSet() {
/* 46 */     super(new HashMap<E, AbstractMapMultiSet.MutableInteger>());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HashMultiSet(Collection<? extends E> coll) {
/* 55 */     this();
/* 56 */     addAll(coll);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 64 */     out.defaultWriteObject();
/* 65 */     doWriteObject(out);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 72 */     in.defaultReadObject();
/* 73 */     setMap(new HashMap<E, AbstractMapMultiSet.MutableInteger>());
/* 74 */     doReadObject(in);
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\multiset\HashMultiSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */