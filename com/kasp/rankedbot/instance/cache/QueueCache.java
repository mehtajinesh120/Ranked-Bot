/*    */ package com.kasp.rankedbot.instance.cache;
/*    */ 
/*    */ import com.kasp.rankedbot.instance.Queue;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class QueueCache
/*    */ {
/* 10 */   private static HashMap<String, Queue> queues = new HashMap<>();
/*    */   
/*    */   public static Queue getQueue(String ID) {
/* 13 */     return queues.get(ID);
/*    */   }
/*    */   
/*    */   public static void addQueue(Queue queue) {
/* 17 */     queues.put(queue.getID(), queue);
/*    */     
/* 19 */     System.out.println("Queue " + queue.getID() + " has been loaded into memory");
/*    */   }
/*    */   
/*    */   public static void removeQueue(Queue queue) {
/* 23 */     queues.remove(queue.getID());
/*    */   }
/*    */   
/*    */   public static boolean containsQueue(String ID) {
/* 27 */     return queues.containsKey(ID);
/*    */   }
/*    */   
/*    */   public static Queue initializeQueue(String ID, Queue queue) {
/* 31 */     if (!containsQueue(ID)) {
/* 32 */       addQueue(queue);
/*    */     }
/* 34 */     return getQueue(ID);
/*    */   }
/*    */   
/*    */   public static Map<String, Queue> getQueues() {
/* 38 */     return queues;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\instance\cache\QueueCache.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */