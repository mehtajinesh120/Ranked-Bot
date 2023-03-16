/*    */ package net.dv8tion.jda.internal.requests.restaction.order;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.api.entities.Category;
/*    */ import net.dv8tion.jda.api.entities.GuildChannel;
/*    */ import net.dv8tion.jda.api.requests.restaction.order.CategoryOrderAction;
/*    */ import net.dv8tion.jda.internal.utils.Checks;
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
/*    */ public class CategoryOrderActionImpl
/*    */   extends ChannelOrderActionImpl
/*    */   implements CategoryOrderAction
/*    */ {
/*    */   protected final Category category;
/*    */   
/*    */   public CategoryOrderActionImpl(Category category, int bucket) {
/* 45 */     super(category.getGuild(), bucket, getChannelsOfType(category, bucket));
/* 46 */     this.category = category;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public Category getCategory() {
/* 53 */     return this.category;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void validateInput(GuildChannel entity) {
/* 59 */     Checks.notNull(entity, "Provided channel");
/* 60 */     Checks.check(getCategory().equals(entity.getParent()), "Provided channel's Category is not this Category!");
/* 61 */     Checks.check(this.orderList.contains(entity), "Provided channel is not in the list of orderable channels!");
/*    */   }
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   private static Collection<GuildChannel> getChannelsOfType(Category category, int bucket) {
/* 67 */     Checks.notNull(category, "Category");
/* 68 */     return (Collection<GuildChannel>)ChannelOrderActionImpl.getChannelsOfType(category.getGuild(), bucket).stream()
/* 69 */       .filter(it -> category.equals(it.getParent()))
/* 70 */       .sorted()
/* 71 */       .collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\requests\restaction\order\CategoryOrderActionImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */