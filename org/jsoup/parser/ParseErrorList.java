/*    */ package org.jsoup.parser;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParseErrorList
/*    */   extends ArrayList<ParseError>
/*    */ {
/*    */   private static final int INITIAL_CAPACITY = 16;
/*    */   private final int initialCapacity;
/*    */   private final int maxSize;
/*    */   
/*    */   ParseErrorList(int initialCapacity, int maxSize) {
/* 16 */     super(initialCapacity);
/* 17 */     this.initialCapacity = initialCapacity;
/* 18 */     this.maxSize = maxSize;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   ParseErrorList(ParseErrorList copy) {
/* 26 */     this(copy.initialCapacity, copy.maxSize);
/*    */   }
/*    */   
/*    */   boolean canAddError() {
/* 30 */     return (size() < this.maxSize);
/*    */   }
/*    */   
/*    */   int getMaxSize() {
/* 34 */     return this.maxSize;
/*    */   }
/*    */   
/*    */   public static ParseErrorList noTracking() {
/* 38 */     return new ParseErrorList(0, 0);
/*    */   }
/*    */   
/*    */   public static ParseErrorList tracking(int maxSize) {
/* 42 */     return new ParseErrorList(16, maxSize);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object clone() {
/* 48 */     return super.clone();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\org\jsoup\parser\ParseErrorList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */