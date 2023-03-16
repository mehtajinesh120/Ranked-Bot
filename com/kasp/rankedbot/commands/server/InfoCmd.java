/*    */ package com.kasp.rankedbot.commands.server;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.GameState;
/*    */ import com.kasp.rankedbot.RankedBot;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Game;
/*    */ import com.kasp.rankedbot.instance.Queue;
/*    */ import com.kasp.rankedbot.instance.cache.GameCache;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.instance.cache.QueueCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class InfoCmd extends Command {
/*    */   public InfoCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 22 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 27 */     if (args.length != 1) {
/* 28 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 29 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 33 */     Embed embed = new Embed(EmbedType.DEFAULT, "Server Info", "Ranked Bot v" + RankedBot.version + " by `kasp#0675`\nhttps://discord.gg/5DdEvfRwz2", 1);
/* 34 */     embed.addField("Players", "`" + PlayerCache.getPlayers().size() + "` registered | `" + guild.getMemberCount() + "` total", false);
/* 35 */     int qing = 0;
/* 36 */     for (Queue q : QueueCache.getQueues().values()) {
/* 37 */       qing += q.getPlayers().size();
/*    */     }
/* 39 */     int playing = 0;
/* 40 */     for (Game g : GameCache.getGames().values()) {
/* 41 */       if (g.getState() == GameState.PLAYING) {
/* 42 */         playing += g.getPlayers().size();
/*    */       }
/*    */     } 
/*    */     
/* 46 */     embed.addField("Currently playing", "`" + qing + "` queueing | `" + playing + "` playing", false);
/* 47 */     embed.addField("RAM Usage", "`" + Runtime.getRuntime().freeMemory() / 1048576L + "`/`" + Runtime.getRuntime().maxMemory() / 1048576L + "` MB", false);
/* 48 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\server\InfoCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */