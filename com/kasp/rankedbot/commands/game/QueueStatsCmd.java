/*    */ package com.kasp.rankedbot.commands.game;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Game;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.GameCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class QueueStatsCmd extends Command {
/*    */   public QueueStatsCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 18 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 23 */     if (args.length != 1) {
/* 24 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 25 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     if (GameCache.getGame(channel.getId()) == null) {
/* 30 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-game-channel"), 1);
/* 31 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 35 */     Game game = GameCache.getGame(channel.getId());
/*    */     
/* 37 */     String t1 = "";
/* 38 */     for (Player p : game.getTeam1()) {
/* 39 */       double templosses = 1.0D;
/* 40 */       if (p.getLosses() > 0) {
/* 41 */         templosses = p.getLosses();
/*    */       }
/* 43 */       double wlr = p.getWins() / templosses;
/* 44 */       t1 = t1 + "• <@" + t1 + "> — `" + p.getID() + "W/" + p.getWins() + "L` `(" + p.getLosses() + "WLR)`\n";
/*    */     } 
/*    */     
/* 47 */     String t2 = "";
/* 48 */     for (Player p : game.getTeam2()) {
/* 49 */       double templosses = 1.0D;
/* 50 */       if (p.getLosses() > 0) {
/* 51 */         templosses = p.getLosses();
/*    */       }
/* 53 */       double wlr = p.getWins() / templosses;
/* 54 */       t2 = t2 + "• <@" + t2 + "> — `" + p.getID() + "W/" + p.getWins() + "L` `(" + p.getLosses() + "WLR)`\n";
/*    */     } 
/*    */     
/* 57 */     String remaining = "";
/* 58 */     for (Player p : game.getRemainingPlayers()) {
/* 59 */       double templosses = 1.0D;
/* 60 */       if (p.getLosses() > 0) {
/* 61 */         templosses = p.getLosses();
/*    */       }
/* 63 */       double wlr = p.getWins() / templosses;
/* 64 */       remaining = remaining + "• <@" + remaining + "> — `" + p.getID() + "W/" + p.getWins() + "L` `(" + p.getLosses() + "WLR)`\n";
/*    */     } 
/*    */     
/* 67 */     Embed embed = new Embed(EmbedType.DEFAULT, "Game`#" + game.getNumber() + "` QueueStats", "", 1);
/* 68 */     embed.addField("Team 1", t1, true);
/* 69 */     embed.addField("Team 2", t2, true);
/* 70 */     if (!remaining.equals("")) {
/* 71 */       embed.addField("Remaining", remaining, false);
/*    */     }
/* 73 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\game\QueueStatsCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */