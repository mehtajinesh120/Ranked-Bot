/*    */ package net.dv8tion.jda.api.events.channel.category.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Category;
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
/*    */ public class CategoryUpdatePositionEvent
/*    */   extends GenericCategoryUpdateEvent<Integer>
/*    */ {
/*    */   public static final String IDENTIFIER = "position";
/*    */   
/*    */   public CategoryUpdatePositionEvent(@Nonnull JDA api, long responseNumber, @Nonnull Category category, int oldPosition) {
/* 37 */     super(api, responseNumber, category, Integer.valueOf(oldPosition), Integer.valueOf(category.getPositionRaw()), "position");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getOldPosition() {
/* 47 */     return getOldValue().intValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getNewPosition() {
/* 57 */     return getNewValue().intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\categor\\update\CategoryUpdatePositionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */