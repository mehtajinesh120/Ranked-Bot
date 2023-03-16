/*    */ package com.kasp.rankedbot.commands.party;
/*    */ 
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
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class PartyListCmd extends Command {
/*    */   public PartyListCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 20 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*    */     String ID;
/* 25 */     if (args.length > 2) {
/* 26 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 27 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */ 
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 33 */     if (args.length == 2) {
/* 34 */       ID = args[1].replaceAll("[^0-9]", "");
/*    */     } else {
/*    */       
/* 37 */       ID = sender.getId();
/*    */     } 
/*    */     
/* 40 */     if (PartyCache.getParty(PlayerCache.getPlayer(ID)) == null) {
/*    */       Embed reply;
/* 42 */       if (args.length == 1) {
/* 43 */         reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-in-party"), 1);
/*    */       } else {
/*    */         
/* 46 */         reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("player-not-in-party"), 1);
/*    */       } 
/*    */       
/* 49 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 54 */     Party party = PartyCache.getParty(PlayerCache.getPlayer(ID));
/*    */     
/* 56 */     String title = "players `[" + party.getMembers().size() + "/" + Config.getValue("max-party-members") + "]`";
/* 57 */     String players = "";
/*    */     
/* 59 */     for (Player p : party.getMembers()) {
/* 60 */       players = players + "<@" + players + "> ";
/*    */     }
/*    */     
/* 63 */     String invited = "";
/*    */     
/* 65 */     for (Player p : party.getInvitedPlayers()) {
/* 66 */       invited = invited + "<@" + invited + "> ";
/*    */     }
/*    */     
/* 69 */     Embed embed = new Embed(EmbedType.DEFAULT, party.getLeader().getIgn() + " party info", "", 1);
/* 70 */     embed.addField(title, players, false);
/*    */     
/* 72 */     if (!invited.equals("")) {
/* 73 */       embed.addField("Invited", invited, false);
/*    */     }
/*    */     
/* 76 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\party\PartyListCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */