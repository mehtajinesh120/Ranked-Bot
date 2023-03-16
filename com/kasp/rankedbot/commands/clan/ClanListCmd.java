/*    */ package com.kasp.rankedbot.commands.clan;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.config.Config;
/*    */ import com.kasp.rankedbot.instance.Clan;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Leaderboard;
/*    */ import com.kasp.rankedbot.instance.cache.ClanCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.dv8tion.jda.api.EmbedBuilder;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class ClanListCmd extends Command {
/*    */   public ClanListCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 23 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 28 */     if (args.length != 1) {
/* 29 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 30 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 34 */     Message embedmsg = (Message)msg.replyEmbeds((new EmbedBuilder()).setTitle("loading...").build(), new MessageEmbed[0]).complete();
/*    */     
/* 36 */     List<Clan> clans = new ArrayList<>(ClanCache.getClans().values());
/* 37 */     List<Clan> clanLB = new ArrayList<>(Leaderboard.getClansLeaderboard());
/*    */     
/* 39 */     for (int j = 0; j < Math.ceil(clans.size()); j += 5) {
/*    */       
/* 41 */       Embed reply = new Embed(EmbedType.DEFAULT, "All clans", "", (int)Math.ceil(clans.size() / 5.0D));
/*    */       
/* 43 */       for (int i = 0; i < 5; i++) {
/* 44 */         if (i + j < ClanCache.getClans().size()) {
/* 45 */           reply.addField("• " + ((Clan)clans.get(i + j)).getName() + " `[" + ((Clan)clans.get(i + j)).getMembers().size() + "/" + Config.getValue("l" + ((Clan)clans.get(i + j)).getLevel().getLevel()) + "]`", ((Clan)clans
/* 46 */               .get(i + j)).getDescription() + "\n> Private: `" + ((Clan)clans.get(i + j)).getDescription() + "`\n\n> Leader: <@" + ((Clan)clans
/* 47 */               .get(i + j)).isPrivate() + ">\n\n> Reputation: **" + ((Clan)clans
/* 48 */               .get(i + j)).getLeader().getID() + "** `[#" + ((Clan)clans
/* 49 */               .get(i + j)).getReputation() + "`\n", false);
/*    */         }
/*    */       } 
/*    */       
/* 53 */       reply.addField("Note", "You can view more stats ab a certain clan\nby using `=cstats <name>` and `=cinfo <name>`", false);
/*    */       
/* 55 */       if (j == 0) {
/* 56 */         embedmsg.editMessageEmbeds(new MessageEmbed[] { reply.build() }).setActionRow(Embed.createButtons(reply.getCurrentPage())).queue();
/*    */       }
/*    */       
/* 59 */       Embed.addPage(embedmsg.getId(), reply);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\clan\ClanListCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */