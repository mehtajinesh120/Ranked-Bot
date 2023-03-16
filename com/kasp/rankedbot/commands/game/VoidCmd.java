/*    */ package com.kasp.rankedbot.commands.game;
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.GameState;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Game;
/*    */ import com.kasp.rankedbot.instance.cache.GameCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import java.util.Timer;
/*    */ import java.util.TimerTask;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.MessageReaction;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class VoidCmd extends Command {
/*    */   public VoidCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 21 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, final Member sender, final TextChannel channel, final Message msg) {
/* 26 */     if (args.length != 1) {
/* 27 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 28 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     if (GameCache.getGame(channel.getId()) == null) {
/* 33 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-game-channel"), 1);
/* 34 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 38 */     final Game game = GameCache.getGame(channel.getId());
/*    */     
/* 40 */     final int number = game.getNumber();
/*    */     
/* 42 */     Embed embed = new Embed(EmbedType.DEFAULT, "Game`#" + number + "` Voiding", "votes will be counted in `30s`", 1);
/* 43 */     embed.addField("Requested by: ", sender.getAsMention(), false);
/* 44 */     channel.sendMessageEmbeds(embed.build(), new MessageEmbed[0]).queue(message -> {
/*    */           message.addReaction("✔").queue();
/*    */           
/*    */           message.addReaction("❌").queue();
/*    */           TimerTask task = new TimerTask()
/*    */             {
/*    */               public void run()
/*    */               {
/* 52 */                 Message message1 = (Message)channel.retrieveMessageById(message.getId()).complete();
/*    */                 
/* 54 */                 if (((MessageReaction)message1.getReactions().get(0)).getCount() - 1 < ((MessageReaction)message1.getReactions().get(1)).getCount()) {
/* 55 */                   Embed reply = new Embed(EmbedType.ERROR, "", "Voiding has been cancelled", 1);
/* 56 */                   msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */                   
/*    */                   return;
/*    */                 } 
/* 60 */                 Embed done = new Embed(EmbedType.DEFAULT, "Game`#" + number + "` Has Been Voided", "if this command was abused, please screenshot this and make a report ticket\n\ngame channel closing in `60s`", 1);
/*    */                 
/* 62 */                 done.addField("Requested by: ", sender.getAsMention(), false);
/* 63 */                 game.setState(GameState.VOIDED);
/* 64 */                 game.setScoredBy(sender);
/*    */                 
/* 66 */                 game.closeChannel(60);
/*    */                 
/* 68 */                 message1.editMessageEmbeds(new MessageEmbed[] { done.build() }).queue();
/* 69 */                 message1.clearReactions().queue();
/*    */               }
/*    */             };
/*    */           Timer timer = new Timer();
/*    */           timer.schedule(task, 30000L);
/*    */         });
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\game\VoidCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */