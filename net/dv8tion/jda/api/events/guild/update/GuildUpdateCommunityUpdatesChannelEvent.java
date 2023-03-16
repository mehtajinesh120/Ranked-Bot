/*    */ package net.dv8tion.jda.api.events.guild.update;
/*    */ 
/*    */ import javax.annotation.Nonnull;
/*    */ import javax.annotation.Nullable;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
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
/*    */ public class GuildUpdateCommunityUpdatesChannelEvent
/*    */   extends GenericGuildUpdateEvent<TextChannel>
/*    */ {
/*    */   public static final String IDENTIFIER = "community_updates_channel";
/*    */   
/*    */   public GuildUpdateCommunityUpdatesChannelEvent(@Nonnull JDA api, long responseNumber, @Nonnull Guild guild, @Nullable TextChannel oldCommunityUpdatesChannel) {
/* 39 */     super(api, responseNumber, guild, oldCommunityUpdatesChannel, guild.getCommunityUpdatesChannel(), "community_updates_channel");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public TextChannel getOldCommunityUpdatesChannel() {
/* 50 */     return getOldValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Nullable
/*    */   public TextChannel getNewCommunityUpdatesChannel() {
/* 61 */     return getNewValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\api\events\guil\\update\GuildUpdateCommunityUpdatesChannelEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */