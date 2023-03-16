/*    */ package com.kasp.rankedbot.commands.player;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.database.SQLPlayerManager;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class RegisterCmd
/*    */   extends Command {
/*    */   public RegisterCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 18 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 23 */     if (args.length != 2) {
/* 24 */       Embed embed = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 25 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     if (Player.isRegistered(sender.getId())) {
/* 30 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("already-registered"), 1);
/* 31 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 35 */     String ign = args[1].replaceAll("[^a-zA-Z0-9_-]", "");
/* 36 */     ign = ign.replaceAll(" ", "").trim();
/*    */     
/* 38 */     if (ign.length() > 16) {
/* 39 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("ign-too-long"), 1);
/* 40 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 44 */     SQLPlayerManager.createPlayer(sender.getId(), ign);
/* 45 */     Player player = new Player(sender.getId());
/* 46 */     player.fix();
/*    */     
/* 48 */     Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("successfully-registered"), 1);
/* 49 */     msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\player\RegisterCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */