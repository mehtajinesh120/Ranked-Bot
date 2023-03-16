/*    */ package com.kasp.rankedbot.commands.game;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.GameState;
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
/*    */ public class GameInfoCmd extends Command {
/*    */   public GameInfoCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 19 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 24 */     if (args.length != 2) {
/* 25 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 26 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 30 */     int number = Integer.parseInt(args[1]);
/*    */     
/* 32 */     if (GameCache.getGame(number) == null) {
/* 33 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("invalid-game"), 1);
/* 34 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 38 */     Game game = GameCache.getGame(number);
/*    */     
/* 40 */     String t1 = "";
/* 41 */     for (Player p : game.getTeam1()) {
/* 42 */       t1 = t1 + "• <@" + t1 + ">\n";
/*    */     }
/*    */     
/* 45 */     String t2 = "";
/* 46 */     for (Player p : game.getTeam2()) {
/* 47 */       t2 = t2 + "• <@" + t2 + ">\n";
/*    */     }
/*    */     
/* 50 */     String remaining = "";
/* 51 */     for (Player p : game.getRemainingPlayers()) {
/* 52 */       remaining = remaining + "• <@" + remaining + ">\n";
/*    */     }
/*    */     
/* 55 */     Embed embed = new Embed(EmbedType.DEFAULT, "Game`#" + number + "` Info", "state: `" + game.getState() + "`", 1);
/* 56 */     embed.addField("Team 1", t1, true);
/* 57 */     embed.addField("Team 2", t2, true);
/* 58 */     if (!remaining.equals("")) {
/* 59 */       embed.addField("Remaining", remaining, false);
/*    */     }
/* 61 */     embed.addField("Map", game.getMap().getName(), true);
/* 62 */     embed.addField("Casual", String.valueOf(game.isCasual()), true);
/*    */     
/* 64 */     if (game.getState() == GameState.VOIDED) {
/* 65 */       embed.addField("Voided By", "<@" + game.getScoredBy().getId() + ">", true);
/* 66 */     } else if (game.getState() == GameState.SCORED) {
/* 67 */       embed.addField("Scored By", "<@" + game.getScoredBy().getId() + ">", true);
/*    */     } 
/*    */     
/* 70 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\game\GameInfoCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */