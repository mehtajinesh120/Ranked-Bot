/*    */ package net.dv8tion.jda.api.events.channel.store.update;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Nonnull;
/*    */ import net.dv8tion.jda.annotations.DeprecatedSince;
/*    */ import net.dv8tion.jda.annotations.ForRemoval;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.IPermissionHolder;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Role;
/*    */ import net.dv8tion.jda.api.entities.StoreChannel;
/*    */ import net.dv8tion.jda.api.events.channel.store.GenericStoreChannelEvent;
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
/*    */ public class StoreChannelUpdatePermissionsEvent
/*    */   extends GenericStoreChannelEvent
/*    */ {
/*    */   private final List<IPermissionHolder> changed;
/*    */   
/*    */   public StoreChannelUpdatePermissionsEvent(@Nonnull JDA api, long responseNumber, @Nonnull StoreChannel channel, List<IPermissionHolder> permHolders) {
/* 49 */     super(api, responseNumber, channel);
/* 50 */     this.changed = permHolders;
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
/*    */   @Nonnull
/*    */   public List<IPermissionHolder> getChangedPermissionHolders() {
/* 64 */     return this.changed;
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
/* 77 */     Objects.requireNonNull(Role.class); return (List<Role>)this.changed.stream().filter(it -> it instanceof Role).map(Role.class::cast)
/* 78 */       .collect(Collectors.toList());
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
/* 91 */     Objects.requireNonNull(Member.class); return (List<Member>)this.changed.stream().filter(it -> it instanceof Member).map(Member.class::cast)
/* 92 */       .collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\stor\\update\StoreChannelUpdatePermissionsEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */