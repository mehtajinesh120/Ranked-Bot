/*    */ package com.kasp.rankedbot.commands.clan;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
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
/*    */ public class ClanInviteCmd extends Command {
/*    */   public ClanInviteCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 19 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 24 */     if (args.length != 2) {
/* 25 */       Embed embed = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 26 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 30 */     Player player = PlayerCache.getPlayer(sender.getId());
/*    */     
/* 32 */     if (ClanCache.getClan(player) == null) {
/* 33 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-in-clan"), 1);
/* 34 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 38 */     Clan clan = ClanCache.getClan(player);
/*    */     
/* 40 */     if (clan.getLeader() != player) {
/* 41 */       Embed embed = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-clan-leader"), 1);
/* 42 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 46 */     String ID = args[1].replaceAll("[^0-9]", "");
/*    */     
/* 48 */     if (PlayerCache.getPlayer(ID) == null) {
/* 49 */       Embed embed = new Embed(EmbedType.ERROR, "Invalid Player", Msg.getMsg("invalid-player"), 1);
/* 50 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 54 */     Player invited = PlayerCache.getPlayer(ID);
/*    */     
/* 56 */     if (clan.getInvitedPlayers().contains(invited)) {
/* 57 */       Embed embed = new Embed(EmbedType.ERROR, "Invalid Player", Msg.getMsg("player-already-invited"), 1);
/* 58 */       msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 62 */     clan.getInvitedPlayers().add(invited);
/*    */     
/* 64 */     Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("player-invited"), 1);
/* 65 */     msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\clan\ClanInviteCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */