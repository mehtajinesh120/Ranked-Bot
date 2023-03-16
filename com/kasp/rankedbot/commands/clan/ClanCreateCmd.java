/*    */ package com.kasp.rankedbot.commands.clan;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Clan;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.ClanCache;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class ClanCreateCmd extends Command {
/*    */   public ClanCreateCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 20 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 25 */     if (args.length != 2) {
/* 26 */       Embed embed = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 27 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 31 */     Player player = PlayerCache.getPlayer(sender.getId());
/* 32 */     String name = args[1];
/*    */     
/* 34 */     if (name.length() > Integer.parseInt(Config.getValue("clan-name-max"))) {
/* 35 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("name-too-long"), 1);
/* 36 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 40 */     if (ClanCache.containsClan(name)) {
/* 41 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("clan-already-exists"), 1);
/* 42 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 46 */     if (ClanCache.getClan(player) != null) {
/* 47 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("already-in-clan"), 1);
/* 48 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 52 */     if (player.getElo() < Integer.parseInt(Config.getValue("elo-to-create"))) {
/* 53 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-enough-elo"), 1);
/* 54 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 58 */     if (player.getGold() < Integer.parseInt(Config.getValue("gold-to-create"))) {
/* 59 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-enough-gold"), 1);
/* 60 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 64 */     player.setGold(player.getGold() - Integer.parseInt(Config.getValue("elo-to-create")));
/* 65 */     new Clan(name, player);
/*    */     
/* 67 */     Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("clan-created"), 1);
/* 68 */     msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\clan\ClanCreateCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */