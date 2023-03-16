/*    */ package com.kasp.rankedbot.commands.party;
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Party;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.PartyCache;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class PartyPromoteCmd extends Command {
/*    */   public PartyPromoteCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 19 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 24 */     if (args.length != 2) {
/* 25 */       Embed embed1 = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 26 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 30 */     Player player = PlayerCache.getPlayer(sender.getId());
/*    */     
/* 32 */     if (PartyCache.getParty(player) == null) {
/* 33 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-in-party"), 1);
/* 34 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 38 */     Party party = PartyCache.getParty(player);
/*    */     
/* 40 */     if (party.getLeader() != player) {
/* 41 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-party-leader"), 1);
/* 42 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 46 */     String ID = args[1].replaceAll("[^0-9]", "");
/*    */     
/* 48 */     Player promoted = PlayerCache.getPlayer(ID);
/*    */     
/* 50 */     if (!party.getInvitedPlayers().contains(promoted)) {
/* 51 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("player-not-in-your-party"), 1);
/* 52 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 56 */     party.promote(promoted);
/*    */     
/* 58 */     Embed reply = new Embed(EmbedType.SUCCESS, "", "Player <@" + ID + "> has been promoted and is now your party leader", 1);
/* 59 */     msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */     
/* 61 */     Embed embed = new Embed(EmbedType.DEFAULT, "", "You have been promoted to leader in <@" + sender.getId() + ">'s party", 1);
/* 62 */     channel.sendMessage("<@" + ID + ">").setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\party\PartyPromoteCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */