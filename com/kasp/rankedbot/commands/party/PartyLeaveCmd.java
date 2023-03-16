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
/*    */ public class PartyLeaveCmd extends Command {
/*    */   public PartyLeaveCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 19 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 24 */     if (args.length != 1) {
/* 25 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 26 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 30 */     Player player = PlayerCache.getPlayer(sender.getId());
/*    */     
/* 32 */     if (PartyCache.getParty(player) == null) {
/* 33 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-in-party"), 1);
/* 34 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 38 */     Party party = PartyCache.getParty(player);
/*    */     
/* 40 */     if (party.getLeader() == player) {
/* 41 */       Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("party-disbanded"), 1);
/* 42 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/* 44 */       String mentions = "";
/* 45 */       for (Player p : party.getMembers()) {
/* 46 */         mentions = mentions + "<@" + mentions + ">";
/*    */       }
/*    */       
/* 49 */       Embed embed = new Embed(EmbedType.DEFAULT, "", Msg.getMsg("your-party-disbanded"), 1);
/* 50 */       channel.sendMessage(mentions).setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*    */       
/* 52 */       party.disband();
/*    */     } else {
/*    */       
/* 55 */       player.leaveParty(party);
/*    */       
/* 57 */       Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("party-left"), 1);
/* 58 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/* 60 */       Embed embed = new Embed(EmbedType.DEFAULT, "", "<@" + player.getID() + "> has left your party", 1);
/* 61 */       channel.sendMessage("<@" + party.getLeader().getID() + ">").setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\party\PartyLeaveCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */