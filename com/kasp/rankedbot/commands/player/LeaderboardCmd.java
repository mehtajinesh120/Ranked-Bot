/*    */ package com.kasp.rankedbot.commands.player;
/*    */ 
/*    */ import com.kasp.rankedbot.CommandSubsystem;
/*    */ import com.kasp.rankedbot.EmbedType;
/*    */ import com.kasp.rankedbot.Statistic;
/*    */ import com.kasp.rankedbot.commands.Command;
/*    */ import com.kasp.rankedbot.instance.Embed;
/*    */ import com.kasp.rankedbot.instance.Leaderboard;
/*    */ import com.kasp.rankedbot.instance.Player;
/*    */ import com.kasp.rankedbot.instance.cache.PlayerCache;
/*    */ import com.kasp.rankedbot.messages.Msg;
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.NumberFormat;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.dv8tion.jda.api.EmbedBuilder;
/*    */ import net.dv8tion.jda.api.entities.Guild;
/*    */ import net.dv8tion.jda.api.entities.Member;
/*    */ import net.dv8tion.jda.api.entities.Message;
/*    */ import net.dv8tion.jda.api.entities.MessageEmbed;
/*    */ import net.dv8tion.jda.api.entities.TextChannel;
/*    */ 
/*    */ public class LeaderboardCmd extends Command {
/*    */   public LeaderboardCmd(String command, String usage, String[] aliases, String description, CommandSubsystem subsystem) {
/* 25 */     super(command, usage, aliases, description, subsystem);
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] args, Guild guild, Member sender, TextChannel channel, Message msg) {
/* 30 */     if (args.length > 2) {
/* 31 */       Embed reply = new Embed(EmbedType.ERROR, "Invalid Arguments", Msg.getMsg("wrong-usage").replaceAll("%usage%", getUsage()), 1);
/* 32 */       msg.replyEmbeds(reply.build(), new MessageEmbed[0]).queue();
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     NumberFormat formatter = new DecimalFormat("#0");
/* 37 */     Statistic statistic = Statistic.ELO;
/* 38 */     if (args.length > 1) {
/*    */       try {
/* 40 */         statistic = Statistic.valueOf(args[1].toUpperCase());
/* 41 */       } catch (Exception e) {
/* 42 */         String stats = "";
/* 43 */         for (Statistic s : Statistic.values()) {
/* 44 */           stats = stats + "`" + stats + "` ";
/*    */         }
/* 46 */         Embed embed = new Embed(EmbedType.ERROR, "Error", "This statistic does not exist\nAvailable stats: " + stats, 1);
/* 47 */         msg.replyEmbeds(embed.build(), new MessageEmbed[0]).queue();
/*    */         
/*    */         return;
/*    */       } 
/*    */     }
/* 52 */     List<String> lb = new ArrayList<>(Leaderboard.getLeaderboard(statistic));
/*    */     
/* 54 */     Message embedmsg = (Message)msg.replyEmbeds((new EmbedBuilder()).setTitle("loading...").build(), new MessageEmbed[0]).complete();
/*    */     
/* 56 */     for (int j = 0; j < Math.ceil(lb.size() / 10.0D); j++) {
/*    */       
/* 58 */       Embed reply = new Embed(EmbedType.DEFAULT, "" + statistic + " Leaderboard", "", (int)Math.ceil(lb.size() / 10.0D));
/*    */       
/* 60 */       String lbmsg = "";
/* 61 */       for (int i = j * 10; i < j * 10 + 10; i++) {
/* 62 */         if (i < lb.size()) {
/* 63 */           String[] values = ((String)lb.get(i)).split("=");
/* 64 */           Player p = PlayerCache.getPlayer(values[0]);
/* 65 */           lbmsg = lbmsg + "**#" + lbmsg + "** `" + i + 1 + "` — " + p.getIgn() + "\n";
/*    */         } 
/*    */       } 
/* 68 */       reply.setDescription(lbmsg);
/*    */       
/* 70 */       if (j == 0) {
/* 71 */         embedmsg.editMessageEmbeds(new MessageEmbed[] { reply.build() }).setActionRow(Embed.createButtons(reply.getCurrentPage())).queue();
/*    */       }
/*    */       
/* 74 */       Embed.addPage(embedmsg.getId(), reply);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\JineshGamer\Downloads\RankedBot.jar!\com\kasp\rankedbot\commands\player\LeaderboardCmd.class
 * Java compiler version: 11 (55.0)
 * JD-Core Version:       1.1.3
 */