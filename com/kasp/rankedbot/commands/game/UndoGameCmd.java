/*    */ package com.kasp.rankedbot.commands.game;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.GameState;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Game;
/*    */ import com.kasp.rankedbot.instance.cache.GameCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import java.util.Objects;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class UndoGameCmd
/*    */   extends Command {
/*    */   public UndoGameCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 21 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 26 */     if (args.length != 2) {
/* 27 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 28 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     int number = Integer.parseInt(args[1]);
/*    */     
/* 34 */     if (GameCache.getGame(number) == null) {
/* 35 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("invalid-game"), 1);
/* 36 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 40 */     Game game = GameCache.getGame(number);
/*    */     
/* 42 */     if (game.getState() != GameState.SCORED) {
/* 43 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-scored"), 1);
/* 44 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 48 */     game.undo();
/*    */     
/* 50 */     Embed embed = new Embed(EmbedType.SUCCESS, "Game `#" + game.getNumber() + "` has been undone", "You can re-score it by simply using `=score` again", 1);
/* 51 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */     
/* 53 */     embed.addField("Unscored By", sender.getAsMention(), false);
/* 54 */     embed.setDescription("");
/*    */     
/* 56 */     if (!Objects.equals(Config.getValue("scored-announcing"), null))
/* 57 */       guild.getTextChannelById(Config.getValue("scored-announcing")).sendMessageEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\game\UndoGameCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */