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
/*    */ public class ForceRegisterCmd extends Command {
/*    */   public ForceRegisterCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 17 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 22 */     if (args.length != 3) {
/* 23 */       Embed embed = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 24 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     String ID = args[1].replaceAll("[^0-9]", "");
/*    */     
/* 30 */     if (Player.isRegistered(ID)) {
/* 31 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("player-already-registered"), 1);
/* 32 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     String ign = args[2];
/* 37 */     ign = ign.replaceAll(" ", "").trim();
/*    */     
/* 39 */     SQLPlayerManager.createPlayer(ID, ign);
/* 40 */     Player player = new Player(ID);
/* 41 */     player.fix();
/*    */     
/* 43 */     Embed reply = new Embed(EmbedType.SUCCESS, "", "You have registered " + guild.getMemberById(ID).getAsMention() + " as `" + ign + "`", 1);
/* 44 */     msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\player\ForceRegisterCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */