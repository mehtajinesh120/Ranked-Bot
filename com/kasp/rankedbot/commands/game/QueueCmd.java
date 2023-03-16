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
/*    */ public class QueueCmd extends Command {
/*    */   public QueueCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
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
/* 37 */     int number = game.getNumber();
/*    */     
/* 39 */     String t1 = "";
/* 40 */     for (Player p : game.getTeam1()) {
/* 41 */       t1 = t1 + "• <@" + t1 + ">\n";
/*    */     }
/* 43 */     String t2 = "";
/* 44 */     for (Player p : game.getTeam2()) {
/* 45 */       t2 = t2 + "• <@" + t2 + ">\n";
/*    */     }
/*    */     
/* 48 */     String remaining = "";
/* 49 */     for (Player p : game.getRemainingPlayers()) {
/* 50 */       remaining = remaining + "• <@" + remaining + ">\n";
/*    */     }
/*    */     
/* 53 */     Embed embed = new Embed(EmbedType.DEFAULT, "Game`#" + number + "` Queue", "", 1);
/* 54 */     embed.addField("Team 1", t1, true);
/* 55 */     embed.addField("Team 2", t2, true);
/* 56 */     if (!remaining.equals("")) {
/* 57 */       embed.addField("Remaining", remaining, false);
/*    */     }
/* 59 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\game\QueueCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */