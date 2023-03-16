/*    */ package com.kasp.rankedbot.listener;
/*    */ 
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.Queue;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.instance.cache.QueueCache;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
/*    */ import net.dv8tion.jda.api.hooks.ListenerAdapter;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QueueJoin
/*    */   extends ListenerAdapter
/*    */ {
/*    */   public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
/* 22 */     if (event.getChannelJoined() != null && 
/* 23 */       QueueCache.containsQueue(event.getChannelJoined().getId())) {
/* 24 */       TextChannel alerts = event.getGuild().getTextChannelById(Config.getValue("alerts-channel"));
/*    */       
/* 26 */       String ID = event.getMember().getId();
/* 27 */       Queue queue = QueueCache.getQueue(event.getChannelJoined().getId());
/*    */       
/* 29 */       Player player = PlayerCache.getPlayer(ID);
/*    */       
/* 31 */       if (!Player.isRegistered(ID)) {
/* 32 */         event.getGuild().kickVoiceMember(event.getMember()).queue();
/*    */         
/* 34 */         Embed embed = new Embed(EmbedType.ERROR, "You Cannot Queue", "", 1);
/* 35 */         embed.setDescription("You are not registered. Please do `=register <your ign>` then try queueing again");
/* 36 */         alerts.sendMessage(event.getMember().getAsMention()).setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 40 */       if (player.isBanned()) {
/* 41 */         event.getGuild().kickVoiceMember(event.getMember()).queue();
/*    */         
/* 43 */         Embed embed = new Embed(EmbedType.ERROR, "You Cannot Queue", "", 1);
/* 44 */         embed.addField("It appears that you've been banned", "If this is a mistake, please do `=fix`. If it still doesn't remove your banned role, you can open an appeal ticket / contact staff", false);
/* 45 */         alerts.sendMessage(event.getMember().getAsMention()).setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*    */       } else {
/*    */         
/* 48 */         queue.addPlayer(player);
/*    */       } 
/*    */     } 
/*    */ 
/*    */     
/* 53 */     if (event.getChannelLeft() != null && 
/* 54 */       QueueCache.containsQueue(event.getChannelLeft().getId())) {
/* 55 */       String ID = event.getMember().getId();
/* 56 */       Queue queue = QueueCache.getQueue(event.getChannelLeft().getId());
/*    */       
/* 58 */       Player player = PlayerCache.getPlayer(ID);
/*    */       
/* 60 */       queue.removePlayer(player);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\listener\QueueJoin.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */