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
/*    */ public class PartyKickCmd extends Command {
/*    */   public PartyKickCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
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
/* 48 */     if (PlayerCache.getPlayer(ID) == null) {
/* 49 */       Embed embed1 = new Embed(EmbedType.ERROR, "Invalid Player", Msg.getMsg("invalid-player"), 1);
/* 50 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 54 */     Player kicked = PlayerCache.getPlayer(ID);
/*    */     
/* 56 */     if (!party.getMembers().contains(kicked)) {
/* 57 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("player-not-in-ur-party"), 1);
/* 58 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 62 */     kicked.leaveParty(party);
/*    */     
/* 64 */     Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("player-kicked-party"), 1);
/* 65 */     msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */     
/* 67 */     Embed embed = new Embed(EmbedType.DEFAULT, "", Msg.getMsg("youre-kicked-party"), 1);
/* 68 */     channel.sendMessage("<@" + kicked.getID() + ">").setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\party\PartyKickCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */