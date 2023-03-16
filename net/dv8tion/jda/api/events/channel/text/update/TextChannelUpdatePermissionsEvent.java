/*    */ package net.dv8tion.jda.api.events.channel.text.update;
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
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ import net.dv8tion.jda.api.events.channel.text.GenericTextChannelEvent;
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
/*    */ public class TextChannelUpdatePermissionsEvent
/*    */   extends GenericTextChannelEvent
/*    */ {
/*    */   private final List<IPermissionHolder> changed;
/*    */   
/*    */   public TextChannelUpdatePermissionsEvent(@Nonnull JDA api, long responseNumber, @Nonnull TextChannel channel, @Nonnull List<IPermissionHolder> permHolders) {
/* 48 */     super(api, responseNumber, channel);
/* 49 */     this.changed = permHolders;
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
/* 63 */     return this.changed;
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
/* 76 */     Objects.requireNonNull(Role.class); return (List<Role>)this.changed.stream().filter(it -> it instanceof Role).map(Role.class::cast)
/* 77 */       .collect(Collectors.toList());
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
/* 90 */     Objects.requireNonNull(Member.class); return (List<Member>)this.changed.stream().filter(it -> it instanceof Member).map(Member.class::cast)
/* 91 */       .collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\tex\\update\TextChannelUpdatePermissionsEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */