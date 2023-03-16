/*    */ package com.kasp.rankedbot.commands.game;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.GameState;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Game;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.GameCache;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class ScoreCmd
/*    */   extends Command {
/*    */   public ScoreCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 24 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*    */     List<Player> winningTeam, losingTeam;
/* 29 */     if (args.length != 4) {
/* 30 */       Embed embed1 = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 31 */       msg.replyEmbeds(embed1.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 35 */     int number = Integer.parseInt(args[1]);
/*    */     
/* 37 */     if (GameCache.getGame(number) == null) {
/* 38 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("invalid-game"), 1);
/* 39 */       msg.replyEmbeds(embed1.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 43 */     Game game = GameCache.getGame(number);
/*    */     
/* 45 */     if (!sender.getUser().isBot() && game.getState() != GameState.SUBMITTED) {
/* 46 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-submitted"), 1);
/* 47 */       msg.replyEmbeds(embed1.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 51 */     String team = args[2];
/* 52 */     String mvpID = args[3].replaceAll("[^0-9]", "");
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 57 */     if (team.equals("1")) {
/* 58 */       winningTeam = game.getTeam1();
/* 59 */       losingTeam = game.getTeam2();
/*    */     } else {
/*    */       
/* 62 */       winningTeam = game.getTeam2();
/* 63 */       losingTeam = game.getTeam1();
/*    */     } 
/*    */     
/* 66 */     game.scoreGame(winningTeam, losingTeam, PlayerCache.getPlayer(mvpID), sender);
/*    */     
/* 68 */     Embed embed = new Embed(EmbedType.DEFAULT, "Game `#" + game.getNumber() + "` has been scored", "", 1);
/*    */     
/* 70 */     String team1 = "";
/* 71 */     for (Player p : game.getTeam1()) {
/* 72 */       team1 = team1 + "• <@" + team1 + "> `(+)`**" + p.getID() + "** `" + game.getEloGain().get(p) + "` > `" + p.getElo() - ((Integer)game.getEloGain().get(p)).intValue() + "`\n";
/*    */     }
/*    */     
/* 75 */     String team2 = "";
/* 76 */     for (Player p : game.getTeam2()) {
/* 77 */       team2 = team2 + "• <@" + team2 + "> `(+)`**" + p.getID() + "** `" + game.getEloGain().get(p) + "` > `" + p.getElo() - ((Integer)game.getEloGain().get(p)).intValue() + "`\n";
/*    */     }
/*    */     
/* 80 */     embed.addField("Team 1:", team1, false);
/* 81 */     embed.addField("Team 2:", team2, false);
/*    */     
/* 83 */     if (!mvpID.equals("")) {
/* 84 */       embed.addField("MVP", "<@" + mvpID + ">", false);
/*    */     }
/*    */     
/* 87 */     embed.addField("Scored by", sender.getAsMention(), false);
/*    */     
/* 89 */     if (!Objects.equals(Config.getValue("scored-announcing"), null)) {
/* 90 */       guild.getTextChannelById(Config.getValue("scored-announcing")).sendMessageEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */     }
/*    */     
/* 93 */     Embed reply = new Embed(EmbedType.SUCCESS, "", "You have scored Game`#" + game.getNumber() + "`", 1);
/* 94 */     msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */     
/* 96 */     if (guild.getTextChannelById(game.getChannelID()) != null)
/* 97 */       guild.getTextChannelById(game.getChannelID()).sendMessageEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\game\ScoreCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */