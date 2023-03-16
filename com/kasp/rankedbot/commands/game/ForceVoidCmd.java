/*    */ package com.kasp.rankedbot.commands.game;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.GameState;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Game;
/*    */ import com.kasp.rankedbot.instance.cache.GameCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class ForceVoidCmd extends Command {
/*    */   public ForceVoidCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
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
/* 37 */     game.setState(GameState.VOIDED);
/* 38 */     game.setScoredBy(sender);
/*    */     
/* 40 */     game.closeChannel(60);
/*    */     
/* 42 */     Embed done = new Embed(EmbedType.DEFAULT, "Game`#" + game.getNumber() + "` Has Been Voided", "if this command was abused, please screenshot this and make a report ticket\n\ngame channel closing in `60s`", 1);
/* 43 */     done.addField("Voided by: ", sender.getAsMention(), false);
/*    */     
/* 45 */     msg.replyEmbeds(done.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\game\ForceVoidCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */