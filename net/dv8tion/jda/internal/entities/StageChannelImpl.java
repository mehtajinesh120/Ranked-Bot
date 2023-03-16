/*    */ package net.dv8tion.jda.internal.entities;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.Permission;
/*    */ import net.dv8tion.jda.api.entities.ChannelType;
/*    */ import net.dv8tion.jda.api.entities.StageChannel;
/*    */ import net.dv8tion.jda.api.entities.StageInstance;
/*    */ import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
/*    */ import net.dv8tion.jda.api.requests.restaction.StageInstanceAction;
/*    */ import net.dv8tion.jda.internal.requests.restaction.StageInstanceActionImpl;
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
/*    */ public class StageChannelImpl
/*    */   extends VoiceChannelImpl
/*    */   implements StageChannel
/*    */ {
/*    */   private StageInstance instance;
/*    */   
/*    */   public StageChannelImpl(long id, GuildImpl guild) {
/* 37 */     super(id, guild);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public ChannelType getType() {
/* 44 */     return ChannelType.STAGE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public StageInstance getStageInstance() {
/* 51 */     return this.instance;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Nonnull
/*    */   public StageInstanceAction createStageInstance(@Nonnull String topic) {
/* 58 */     EnumSet<Permission> permissions = getGuild().getSelfMember().getPermissions(this);
/* 59 */     EnumSet<Permission> required = EnumSet.of(Permission.MANAGE_CHANNEL, Permission.VOICE_MUTE_OTHERS, Permission.VOICE_MOVE_OTHERS);
/* 60 */     for (Permission perm : required) {
/*    */       
/* 62 */       if (!permissions.contains(perm)) {
/* 63 */         throw new InsufficientPermissionException(this, perm, "You must be a stage moderator to create a stage instance! Missing Permission: " + perm);
/*    */       }
/*    */     } 
/* 66 */     return (new StageInstanceActionImpl(this)).setTopic(topic);
/*    */   }
/*    */ 
/*    */   
/*    */   public StageChannelImpl setStageInstance(StageInstance instance) {
/* 71 */     this.instance = instance;
/* 72 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\entities\StageChannelImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */