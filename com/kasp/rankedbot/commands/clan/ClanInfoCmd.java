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
/*    */ public class ClanInfoCmd extends Command {
/*    */   public ClanInfoCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 20 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*    */     String clanName;
/* 25 */     if (args.length > 2) {
/* 26 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 27 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 32 */     if (args.length == 1) {
/* 33 */       if (ClanCache.getClan(PlayerCache.getPlayer(sender.getId())) != null) {
/* 34 */         clanName = ClanCache.getClan(PlayerCache.getPlayer(sender.getId())).getName();
/*    */       } else {
/*    */         
/* 37 */         Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-in-clan"), 1);
/* 38 */         msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */         return;
/*    */       } 
/*    */     } else {
/* 42 */       clanName = args[1];
/*    */     } 
/*    */     
/* 45 */     if (ClanCache.getClan(clanName) == null) {
/* 46 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Clan", Msg.getMsg("clan-doesnt-exist"), 1);
/* 47 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 51 */     Clan clan = ClanCache.getClan(clanName);
/*    */     
/* 53 */     String members = "";
/* 54 */     for (Player p : clan.getMembers()) {
/* 55 */       members = members + "<@" + members + "> ";
/*    */     }
/*    */     
/* 58 */     String invited = "";
/* 59 */     for (Player p : clan.getInvitedPlayers()) {
/* 60 */       invited = invited + "<@" + invited + "> ";
/*    */     }
/*    */     
/* 63 */     String eloReq = "";
/* 64 */     if (!clan.isPrivate()) {
/* 65 */       eloReq = " - `" + clan.getEloJoinReq() + "` ELO required to join";
/*    */     }
/*    */     
/* 68 */     Embed embed = new Embed(EmbedType.DEFAULT, clan.getName() + " Clan Info", "- Stats that don't show up on `=cstats`", 1);
/* 69 */     embed.addField("Private", "" + clan.isPrivate() + clan.isPrivate(), false);
/* 70 */     embed.addField("Clan Leader", "<@" + clan.getLeader().getID() + ">", false);
/* 71 */     embed.addField("All Members `[" + clan.getMembers().size() + "/" + Config.getValue("l" + clan.getLevel().getLevel()) + "]`", members, false);
/* 72 */     if (clan.getInvitedPlayers().size() != 0) {
/* 73 */       embed.addField("All Invited Players `[" + clan.getInvitedPlayers().size() + "]`", invited, false);
/*    */     }
/* 75 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\clan\ClanInfoCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */