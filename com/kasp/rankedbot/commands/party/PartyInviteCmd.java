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
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ import net.dv8tion.jda.api.interactions.components.Button;
/*    */ 
/*    */ public class PartyInviteCmd extends Command {
/*    */   public PartyInviteCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 24 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 29 */     if (args.length != 2) {
/* 30 */       Embed embed1 = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 31 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 35 */     Player player = PlayerCache.getPlayer(sender.getId());
/*    */     
/* 37 */     if (PartyCache.getParty(player) == null) {
/* 38 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-in-party"), 1);
/* 39 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 43 */     Party party = PartyCache.getParty(player);
/*    */     
/* 45 */     if (party.getLeader() != player) {
/* 46 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-party-leader"), 1);
/* 47 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 51 */     String ID = args[1].replaceAll("[^0-9]", "");
/*    */     
/* 53 */     if (PlayerCache.getPlayer(ID) == null) {
/* 54 */       Embed embed1 = new Embed(EmbedType.ERROR, "Invalid Player", Msg.getMsg("invalid-player"), 1);
/* 55 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 59 */     Player invited = PlayerCache.getPlayer(ID);
/*    */     
/* 61 */     if (party.getInvitedPlayers().contains(invited)) {
/* 62 */       Embed embed1 = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("player-already-invited"), 1);
/* 63 */       msg.replyEmbeds(embed1.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 67 */     party.invite(invited);
/*    */     
/* 69 */     Embed reply = new Embed(EmbedType.SUCCESS, "", "Player <@" + ID + "> has been invited to your party. They have `" + Config.getValue("invite-expiration") + "` mins to accept the invite", 1);
/* 70 */     msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */     
/* 72 */     List<Button> buttons = new ArrayList<>();
/* 73 */     buttons.add(Button.primary("rankedbot-pinvitation-" + player.getID() + "=" + invited.getID(), "Accept Invitation"));
/*    */     
/* 75 */     Embed embed = new Embed(EmbedType.DEFAULT, "", "You have been invited to join <@" + sender.getId() + ">'s party\nType `=pjoin " + sender.getId() + "` or click the button below to join it\nThis invite expires after `" + Config.getValue("invite-expiration") + "` mins", 1);
/* 76 */     channel.sendMessage("<@" + ID + ">").setEmbeds(new MessageEmbed[] { embed.build() }).setActionRow(buttons).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\party\PartyInviteCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */