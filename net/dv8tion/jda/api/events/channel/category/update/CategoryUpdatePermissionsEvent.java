/*    */ package net.dv8tion.jda.api.events.channel.category.update;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*    */ import net.dv8tion.jda.annotations.ForRemoval;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Category;
/*    */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Role;
/*    */ import net.dv8tion.jda.api.events.channel.category.GenericCategoryEvent;
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
/*    */ @Deprecated
/*    */ @ForRemoval(deadline = "4.4.0")
/*    */ @DeprecatedSince("4.2.0")
/*    */ public class CategoryUpdatePermissionsEvent
/*    */   extends GenericCategoryEvent
/*    */ {
/*    */   protected final List<IPermissionHolder> changed;
/*    */   
/*    */   public CategoryUpdatePermissionsEvent(@Nonnull JDA api, long responseNumber, @Nonnull Category category, @Nonnull List<IPermissionHolder> changed) {
/* 49 */     super(api, responseNumber, category);
/* 50 */     this.changed = changed;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public List<IPermissionHolder> getChangedPermissionHolders() {
/* 61 */     return this.changed;
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
/*    */   @Nonnull
/*    */   public List<Role> getChangedRoles() {
/* 74 */     Objects.requireNonNull(Role.class); return (List<Role>)this.changed.stream().filter(it -> it instanceof Role).map(Role.class::cast)
/* 75 */       .collect(Collectors.toList());
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
/*    */   @Nonnull
/*    */   public List<Member> getChangedMembers() {
/* 88 */     Objects.requireNonNull(Member.class); return (List<Member>)this.changed.stream().filter(it -> it instanceof Member).map(Member.class::cast)
/* 89 */       .collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\categor\\update\CategoryUpdatePermissionsEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */