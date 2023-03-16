/*    */ package com.kasp.rankedbot.commands.player;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class RenameCmd extends Command {
/*    */   public RenameCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 17 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 22 */     if (args.length != 2) {
/* 23 */       Embed embed = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 24 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     String ign = args[1].replaceAll("[^a-zA-Z0-9_-]", "");
/* 29 */     ign = ign.replaceAll(" ", "").trim();
/*    */     
/* 31 */     if (ign.length() > 16) {
/* 32 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("ign-too-long"), 1);
/* 33 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 37 */     Player player = PlayerCache.getPlayer(sender.getId());
/* 38 */     player.setIgn(ign);
/* 39 */     player.fix();
/*    */     
/* 41 */     Embed reply = new Embed(EmbedType.SUCCESS, "", "You have successfully renamed to `" + ign + "`", 1);
/* 42 */     msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\player\RenameCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */