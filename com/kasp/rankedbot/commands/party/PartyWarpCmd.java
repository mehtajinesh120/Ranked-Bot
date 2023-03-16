/*    */ package com.kasp.rankedbot.commands.party;
/*    */ 
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
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class PartyWarpCmd extends Command {
/*    */   public PartyWarpCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 19 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/*    */     Embed embed;
/* 24 */     if (args.length != 1) {
/* 25 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 26 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 30 */     Player player = PlayerCache.getPlayer(sender.getId());
/*    */     
/* 32 */     if (PartyCache.getParty(player) == null) {
/* 33 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-in-party"), 1);
/* 34 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 38 */     Party party = PartyCache.getParty(player);
/*    */     
/* 40 */     if (party.getLeader() != player) {
/* 41 */       Embed reply = new Embed(EmbedType.ERROR, "Error", Msg.getMsg("not-party-leader"), 1);
/* 42 */       msg.replyEmbeds(reply.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 46 */     String warped = "";
/* 47 */     for (Player p : party.getMembers()) {
/* 48 */       if (!p.getID().equals(sender.getId())) {
/*    */         try {
/* 50 */           guild.moveVoiceMember(guild.getMemberById(p.getID()), sender.getVoiceState().getChannel()).queue();
/* 51 */           warped = warped + "<@" + warped + "> ";
/* 52 */         } catch (Exception exception) {}
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 57 */     if (warped.equals("")) {
/* 58 */       embed = new Embed(EmbedType.ERROR, "", Msg.getMsg("couldnt-warp"), 1);
/*    */     } else {
/*    */       
/* 61 */       embed = new Embed(EmbedType.SUCCESS, "", "Warped " + warped + " to your vc", 1);
/*    */     } 
/* 63 */     msg.replyEmbeds(embed.build(), new net.dv8tion.jda.api.entities.MessageEmbed[0]).queue();
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\party\PartyWarpCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */