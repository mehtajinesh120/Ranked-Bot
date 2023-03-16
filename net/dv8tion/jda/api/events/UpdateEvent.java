/*    */ package net.dv8tion.jda.api.events;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
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
/*    */ public interface UpdateEvent<E, T>
/*    */   extends GenericEvent
/*    */ {
/*    */   @Nonnull
/*    */   default Class<E> getEntityType() {
/* 41 */     return (Class)getEntity().getClass();
/*    */   }
/*    */   
/*    */   @Nonnull
/*    */   String getPropertyIdentifier();
/*    */   
/*    */   @Nonnull
/*    */   E getEntity();
/*    */   
/*    */   @Nullable
/*    */   T getOldValue();
/*    */   
/*    */   @Nullable
/*    */   T getNewValue();
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\UpdateEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */