/*    */ package com.kasp.rankedbot.commands.party;
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.config.Config;
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
/*    */ public class PartyJoinCmd extends Command {
/*    */   public PartyJoinCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 20 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 25 */     if (args.length != 2) {
/* 26 */       Embed embed1 = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 27 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 31 */     Player player = PlayerCache.getPlayer(sender.getId());
/*    */     
/* 33 */     if (PartyCache.getParty(player) != null) {
/* 34 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("already-in-party"), 1);
/* 35 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 39 */     String ID = args[1].replaceAll("[^0-9]", "");
/*    */     
/* 41 */     Player leader = PlayerCache.getPlayer(ID);
/*    */     
/* 43 */     if (PartyCache.getParty(leader) == null) {
/* 44 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("player-not-in-party"), 1);
/* 45 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 49 */     Party party = PartyCache.getParty(leader);
/*    */     
/* 51 */     if (!party.getInvitedPlayers().contains(PlayerCache.getPlayer(sender.getId()))) {
/* 52 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-invited"), 1);
/* 53 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 57 */     if (party.getMembers().size() >= Integer.parseInt(Config.getValue("max-party-members"))) {
/* 58 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("this-party-full"), 1);
/* 59 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 63 */     int partyElo = 0;
/* 64 */     for (Player p : party.getMembers()) {
/* 65 */       partyElo += p.getElo();
/*    */     }
/*    */     
/* 68 */     if (partyElo + player.getElo() > Integer.parseInt(Config.getValue("max-party-elo"))) {
/* 69 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", "You have too much elo to join this party\nParty elo: `" + partyElo + "`\nYour elo: `" + player.getElo() + "`\nParty elo limit: `" + Config.getValue("max-party-elo") + "`", 1);
/* 70 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 74 */     player.joinParty(party);
/*    */     
/* 76 */     Embed reply = new Embed(EmbedType.SUCCESS, "", Msg.getMsg("joined-party"), 1);
/* 77 */     msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */     
/* 79 */     Embed embed = new Embed(EmbedType.DEFAULT, "", "<@" + player.getID() + "> has joined your party", 1);
/* 80 */     channel.sendMessage("<@" + leader.getID() + ">").setEmbeds(new MessageEmbed[] { embed.build() }).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\party\PartyJoinCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */