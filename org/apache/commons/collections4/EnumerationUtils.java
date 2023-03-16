/*    */ package org.apache.commons.collections4;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Enumeration;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.StringTokenizer;
/*    */ import org.apache.commons.collections4.iterators.EnumerationIterator;
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
/*    */ public class EnumerationUtils
/*    */ {
/*    */   public static <T> T get(Enumeration<T> e, int index) {
/* 55 */     int i = index;
/* 56 */     CollectionUtils.checkIndexBounds(i);
/* 57 */     while (e.hasMoreElements()) {
/* 58 */       i--;
/* 59 */       if (i == -1) {
/* 60 */         return e.nextElement();
/*    */       }
/* 62 */       e.nextElement();
/*    */     } 
/*    */     
/* 65 */     throw new IndexOutOfBoundsException("Entry does not exist: " + i);
/*    */   }
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
/*    */   public static <E> List<E> toList(Enumeration<? extends E> enumeration) {
/* 80 */     return IteratorUtils.toList((Iterator<? extends E>)new EnumerationIterator(enumeration));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<String> toList(StringTokenizer stringTokenizer) {
/* 91 */     List<String> result = new ArrayList<String>(stringTokenizer.countTokens());
/* 92 */     while (stringTokenizer.hasMoreTokens()) {
/* 93 */       result.add(stringTokenizer.nextToken());
/*    */     }
/* 95 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\apache\commons\collections4\EnumerationUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */