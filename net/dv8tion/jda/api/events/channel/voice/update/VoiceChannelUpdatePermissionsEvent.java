/*    */ package net.dv8tion.jda.api.events.channel.voice.update;
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
/*    */ import net.dv8tion.jda.api.entities.VoiceChannel;
/*    */ import net.dv8tion.jda.api.events.channel.voice.GenericVoiceChannelEvent;
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
/*    */ public class VoiceChannelUpdatePermissionsEvent
/*    */   extends GenericVoiceChannelEvent
/*    */ {
/*    */   private final List<IPermissionHolder> changedPermHolders;
/*    */   
/*    */   public VoiceChannelUpdatePermissionsEvent(@Nonnull JDA api, long responseNumber, @Nonnull VoiceChannel channel, @Nonnull List<IPermissionHolder> changed) {
/* 48 */     super(api, responseNumber, channel);
/* 49 */     this.changedPermHolders = changed;
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
/* 63 */     return this.changedPermHolders;
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
/* 76 */     Objects.requireNonNull(Role.class); return (List<Role>)this.changedPermHolders.stream().filter(p -> p instanceof Role).map(Role.class::cast)
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
/* 90 */     Objects.requireNonNull(Member.class); return (List<Member>)this.changedPermHolders.stream().filter(p -> p instanceof Member).map(Member.class::cast)
/* 91 */       .collect(Collectors.toList());
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\channel\voic\\update\VoiceChannelUpdatePermissionsEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */