/*    */ package com.kasp.rankedbot.commands.game;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Game;
/*    */ import com.kasp.rankedbot.instance.cache.GameCache;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class PickCmd extends Command {
/*    */   public PickCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 18 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 23 */     if (args.length != 2) {
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
/* 37 */     String ID = args[1].replaceAll("[^0-9]", "");
/*    */     
/* 39 */     if (!game.pickPlayer(PlayerCache.getPlayer(sender.getId()), PlayerCache.getPlayer(ID))) {
/* 40 */       Embed reply = new Embed(EmbedType.ERROR, "Error", "Reason: `the game is already started` / `you aren't captain or it's not your turn to pick` / `you're trying to pick yourself` / `you're trying to pick someone not from this game or already picked`", 1);
/*    */       
/* 42 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\game\PickCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */