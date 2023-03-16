/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import java.time.OffsetDateTime;
/*    */ import java.time.ZoneOffset;
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.MessageChannel;
/*    */ import net.dv8tion.jda.api.entities.PrivateChannel;
/*    */ import net.dv8tion.jda.api.entities.User;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.user.UserTypingEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.EntityBuilder;
/*    */ import net.dv8tion.jda.internal.entities.GuildImpl;
/*    */ import net.dv8tion.jda.internal.entities.MemberImpl;
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
/*    */ public class TypingStartHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public TypingStartHandler(JDAImpl api) {
/* 37 */     super(api);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/*    */     User user;
/* 43 */     GuildImpl guild = null;
/* 44 */     if (!content.isNull("guild_id")) {
/*    */       
/* 46 */       long guildId = content.getUnsignedLong("guild_id");
/* 47 */       guild = (GuildImpl)getJDA().getGuildById(guildId);
/* 48 */       if (getJDA().getGuildSetupController().isLocked(guildId))
/* 49 */         return Long.valueOf(guildId); 
/* 50 */       if (guild == null) {
/* 51 */         return null;
/*    */       }
/*    */     } 
/* 54 */     long channelId = content.getLong("channel_id");
/* 55 */     MessageChannel channel = (MessageChannel)getJDA().getTextChannelsView().get(channelId);
/* 56 */     if (channel == null)
/* 57 */       channel = (MessageChannel)getJDA().getPrivateChannelsView().get(channelId); 
/* 58 */     if (channel == null) {
/* 59 */       return null;
/*    */     }
/*    */ 
/*    */     
/* 63 */     long userId = content.getLong("user_id");
/*    */     
/* 65 */     MemberImpl member = null;
/* 66 */     if (channel instanceof PrivateChannel) {
/* 67 */       user = ((PrivateChannel)channel).getUser();
/*    */     } else {
/* 69 */       user = (User)getJDA().getUsersView().get(userId);
/* 70 */     }  if (!content.isNull("member")) {
/*    */ 
/*    */       
/* 73 */       EntityBuilder entityBuilder = getJDA().getEntityBuilder();
/* 74 */       member = entityBuilder.createMember(guild, content.getObject("member"));
/* 75 */       entityBuilder.updateMemberCache(member);
/* 76 */       user = member.getUser();
/*    */     } 
/*    */     
/* 79 */     if (user == null) {
/* 80 */       return null;
/*    */     }
/*    */     
/* 83 */     OffsetDateTime timestamp = Instant.ofEpochSecond(content.getInt("timestamp")).atOffset(ZoneOffset.UTC);
/* 84 */     getJDA().handleEvent((GenericEvent)new UserTypingEvent((JDA)
/*    */           
/* 86 */           getJDA(), this.responseNumber, user, channel, timestamp, (Member)member));
/*    */     
/* 88 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\TypingStartHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */