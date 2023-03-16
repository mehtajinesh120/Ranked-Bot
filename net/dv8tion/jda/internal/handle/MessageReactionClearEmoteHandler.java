/*    */ package net.dv8tion.jda.internal.handle;
/*    */ 
/*    */ import net.dv8tion.jda.api.JDA;
/*    */ import net.dv8tion.jda.api.entities.Emote;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.MessageChannel;
/*    */ import net.dv8tion.jda.api.entities.MessageReaction;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ import net.dv8tion.jda.api.events.GenericEvent;
/*    */ import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEmoteEvent;
/*    */ import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEmoteEvent;
/*    */ import net.dv8tion.jda.api.utils.data.DataObject;
/*    */ import net.dv8tion.jda.internal.JDAImpl;
/*    */ import net.dv8tion.jda.internal.entities.EmoteImpl;
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
/*    */ public class MessageReactionClearEmoteHandler
/*    */   extends SocketHandler
/*    */ {
/*    */   public MessageReactionClearEmoteHandler(JDAImpl api) {
/* 33 */     super(api);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected Long handleInternally(DataObject content) {
/* 39 */     long guildId = content.getUnsignedLong("guild_id");
/* 40 */     if (getJDA().getGuildSetupController().isLocked(guildId))
/* 41 */       return Long.valueOf(guildId); 
/* 42 */     Guild guild = getJDA().getGuildById(guildId);
/* 43 */     if (guild == null) {
/*    */       
/* 45 */       EventCache.LOG.debug("Caching MESSAGE_REACTION_REMOVE_EMOJI event for unknown guild {}", Long.valueOf(guildId));
/* 46 */       getJDA().getEventCache().cache(EventCache.Type.GUILD, guildId, this.responseNumber, this.allContent, this::handle);
/* 47 */       return null;
/*    */     } 
/*    */     
/* 50 */     long channelId = content.getUnsignedLong("channel_id");
/* 51 */     TextChannel channel = guild.getTextChannelById(channelId);
/* 52 */     if (channel == null) {
/*    */       
/* 54 */       EventCache.LOG.debug("Caching MESSAGE_REACTION_REMOVE_EMOJI event for unknown channel {}", Long.valueOf(channelId));
/* 55 */       getJDA().getEventCache().cache(EventCache.Type.CHANNEL, channelId, this.responseNumber, this.allContent, this::handle);
/* 56 */       return null;
/*    */     } 
/*    */     
/* 59 */     long messageId = content.getUnsignedLong("message_id");
/* 60 */     DataObject emoji = content.getObject("emoji");
/* 61 */     MessageReaction.ReactionEmote reactionEmote = null;
/* 62 */     if (emoji.isNull("id")) {
/*    */       
/* 64 */       reactionEmote = MessageReaction.ReactionEmote.fromUnicode(emoji.getString("name"), (JDA)getJDA());
/*    */     } else {
/*    */       EmoteImpl emoteImpl;
/*    */       
/* 68 */       long emoteId = emoji.getUnsignedLong("id");
/* 69 */       Emote emote = getJDA().getEmoteById(emoteId);
/* 70 */       if (emote == null)
/*    */       {
/*    */ 
/*    */         
/* 74 */         emoteImpl = (new EmoteImpl(emoteId, getJDA())).setAnimated(emoji.getBoolean("animated")).setName(emoji.getString("name", ""));
/*    */       }
/* 76 */       reactionEmote = MessageReaction.ReactionEmote.fromCustom((Emote)emoteImpl);
/*    */     } 
/*    */     
/* 79 */     MessageReaction reaction = new MessageReaction((MessageChannel)channel, reactionEmote, messageId, false, 0);
/* 80 */     getJDA().handleEvent((GenericEvent)new GuildMessageReactionRemoveEmoteEvent((JDA)getJDA(), this.responseNumber, channel, reaction, messageId));
/* 81 */     getJDA().handleEvent((GenericEvent)new MessageReactionRemoveEmoteEvent((JDA)getJDA(), this.responseNumber, messageId, (MessageChannel)channel, reaction));
/* 82 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\net\dv8tion\jda\internal\handle\MessageReactionClearEmoteHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */