/*    */ package com.kasp.rankedbot.commands.game;
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.GameState;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Game;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.Queue;
/*    */ import com.kasp.rankedbot.instance.cache.GameCache;
/*    */ import com.kasp.rankedbot.instance.cache.QueueCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class SubmitCmd extends Command {
/*    */   public SubmitCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 22 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 27 */     if (args.length != 1) {
/* 28 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 29 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 33 */     if (GameCache.getGame(channel.getId()) == null) {
/* 34 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-game-channel"), 1);
/* 35 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 39 */     if (msg.getAttachments().size() != Integer.parseInt(Config.getValue("submitting-attachments"))) {
/* 40 */       Embed error = new Embed(EmbedType.ERROR, "", "You need to attach " + Config.getValue("submitting-attachments") + " screenshot(s) for proof", 1);
/* 41 */       msg.replyEmbeds(error.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 45 */     Game game = GameCache.getGame(channel.getId());
/*    */     
/* 47 */     if (game.getState() != GameState.PLAYING) {
/* 48 */       Embed error = new Embed(EmbedType.ERROR, "", "You cannot perform this command in the current state of the game", 1);
/* 49 */       msg.replyEmbeds(error.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 53 */     if (game.isCasual()) {
/* 54 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("casual-game"), 1);
/* 55 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 59 */     Embed embed = new Embed(EmbedType.SUCCESS, "Game `#" + game.getNumber() + "` submitted", "", 1);
/*    */     
/* 61 */     String queues = "";
/* 62 */     for (Queue q : QueueCache.getQueues().values()) {
/* 63 */       queues = queues + "<#" + queues + ">\n";
/*    */     }
/* 65 */     embed.addField("Requeue here:", queues, false);
/* 66 */     embed.setFooter("RankedBot by kasp#0675");
/* 67 */     channel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue();
/*    */     
/* 69 */     game.setState(GameState.SUBMITTED);
/*    */     
/* 71 */     Embed scoring = new Embed(EmbedType.DEFAULT, "Game `#" + game.getNumber() + "` Scoring", "", 1);
/*    */     
/* 73 */     String t1 = "";
/* 74 */     String t2 = "";
/* 75 */     for (Player p : game.getTeam1())
/* 76 */       t1 = t1 + "• <@" + t1 + ">\n"; 
/* 77 */     for (Player p : game.getTeam2()) {
/* 78 */       t2 = t2 + "• <@" + t2 + ">\n";
/*    */     }
/* 80 */     scoring.addField("Team 1:", t1, true);
/* 81 */     scoring.addField("Team 2:", t2, true);
/*    */     
/* 83 */     if (msg.getAttachments().size() > 0) {
/* 84 */       scoring.setImageURL(((Message.Attachment)msg.getAttachments().get(0)).getUrl());
/*    */     }
/*    */     
/* 87 */     scoring.setDescription("Map: `" + game.getMap().getName() + "`\n\nPlease use `=score` to score this game");
/*    */     
/* 89 */     channel.sendMessage("<@&" + Config.getValue("scorer-role") + ">").setEmbeds(new MessageEmbed[] { scoring.build() }).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\game\SubmitCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */