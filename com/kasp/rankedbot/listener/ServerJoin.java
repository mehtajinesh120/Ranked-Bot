/*    */ package com.kasp.rankedbot.listener;
/*    */ 
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
/*    */ import net.dv8tion.jda.api.hooks.ListenerAdapter;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ 
/*    */ public class ServerJoin
/*    */   extends ListenerAdapter
/*    */ {
/*    */   public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
/* 17 */     if (Player.isRegistered(event.getMember().getId())) {
/* 18 */       Player player = PlayerCache.getPlayer(event.getMember().getId());
/* 19 */       player.fix();
/*    */       
/* 21 */       Embed embed = new Embed(EmbedType.DEFAULT, "Welcome Back", "I've noticed it's not your first time in this server\nI corrected your stats and updated your nickname - you don't need to register again!", 1);
/*    */ 
/*    */       
/* 24 */       event.getGuild().getTextChannelById(Config.getValue("alerts-channel")).sendMessage(event.getMember().getAsMention()).setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\listener\ServerJoin.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */