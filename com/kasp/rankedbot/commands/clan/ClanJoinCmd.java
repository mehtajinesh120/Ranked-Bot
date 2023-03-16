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
/*    */ public class ClanJoinCmd extends Command {
/*    */   public ClanJoinCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
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
/*    */     
/* 33 */     if (ClanCache.getClan(player) != null) {
/* 34 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("already-in-clan"), 1);
/* 35 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 39 */     if (!ClanCache.containsClan(args[1])) {
/* 40 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("clan-doesnt-exist"), 1);
/* 41 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 45 */     Clan clan = ClanCache.getClan(args[1]);
/*    */     
/* 47 */     if (!clan.isPrivate()) {
/* 48 */       if (clan.getMembers().size() >= Integer.parseInt(Config.getValue("l" + clan.getLevel().getLevel()))) {
/* 49 */         Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("clan-max-players"), 1);
/* 50 */         msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 54 */       if (player.getElo() < clan.getEloJoinReq()) {
/* 55 */         Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-enough-elo-join"), 1);
/* 56 */         msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */         
/*    */         return;
/*    */       } 
/*    */     } else {
/* 61 */       if (!clan.getInvitedPlayers().contains(player)) {
/* 62 */         Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("clan-not-invited"), 1);
/* 63 */         msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 67 */       if (clan.getMembers().size() >= Integer.parseInt(Config.getValue("l" + clan.getLevel().getLevel()))) {
/* 68 */         Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("clan-max-players"), 1);
/* 69 */         msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */         
/*    */         return;
/*    */       } 
/* 73 */       clan.getInvitedPlayers().remove(player);
/*    */     } 
/*    */     
/* 76 */     clan.getMembers().add(player);
/*    */     
/* 78 */     Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("clan-joined"), 1);
/* 79 */     msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\clan\ClanJoinCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */